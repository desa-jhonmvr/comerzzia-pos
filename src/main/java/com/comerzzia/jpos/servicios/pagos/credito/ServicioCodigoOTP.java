/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.sms.EnvioSmsGeneralDTO;
import com.comerzzia.jpos.entity.db.CodigoOtp;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.persistencia.core.variables.VariablesDao;
import com.comerzzia.jpos.persistencia.credito.otp.CodigoOtpDao;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.FacturacionTarjetaException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.exception.ValidacionOTPException;
import com.comerzzia.jpos.webservice.credito.CreditoDao;
import com.comerzzia.util.ClienteRest;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Simbania
 */
public class ServicioCodigoOTP {

    private static final Logger LOG_POS = Logger.getMLogger(ServicioCodigoOTP.class);

    private static final String URL_LOGIN_JWT = "/seg/loginjwt";
    private static final String URL_ENVIO_SMS = "/cor/enviarSms";

    /**
     *
     * @param credito
     * @param usuario
     * @param uidTicket
     * @throws FacturacionTarjetaException
     * @throws Exception
     */
    public static void enviarOTP(Long credito, Usuarios usuario, String uidTicket) throws FacturacionTarjetaException, Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            enviarOTP(credito, usuario, uidTicket, em);

            em.getTransaction().commit();
        } catch (Exception e) {
            LOG_POS.error("Error al enviar el OTP", e);
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new ValidacionOTPException("No se pudo enviar el OTP. " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param credito
     * @param usuario
     * @param uidTicket
     * @param em
     * @throws FacturacionTarjetaException
     * @throws Exception
     */
    public static void enviarOTP(Long credito, Usuarios usuario, String uidTicket, EntityManager em) throws FacturacionTarjetaException, Exception {

        ClienteRest clienteRest = new ClienteRest();

        String credenciales = Variables.getVariable(Variables.CREDENCIALES_AUTENTICACION_ERP_MOVIL_JWT);
        String url = Variables.getVariable(Variables.WEBSERVICE_ERP_MOVIL_ENDPOINT_URL);

        String variablesOTP = Variables.getVariable(Variables.VARIABLES_OTP_CREDITO_DIRECTO);
        String codigoPlantilla = JsonUtil.getElementJson(variablesOTP, "codigoPlantilla");
        if (codigoPlantilla == null) {
            throw new ValidacionOTPException("No existe la variable VARIABLES.OTP.CREDITO.DIRECTO ");
        }

        ResponseDTO responseJwt = clienteRest.clientRestPOST(url + URL_LOGIN_JWT, credenciales, null, ResponseDTO.class);
        Map<String, String> headers = new HashMap<>();
        if (responseJwt.getExito()) {
            headers.put("Authorization", (String) responseJwt.getObjetoRespuesta());
        } else {
            throw new ValidacionOTPException("Error al autenticar el servicio web");
        }

        Integer numeroOtp = Math.abs(ThreadLocalRandom.current().nextInt(100000, 999999));
        Date fechaIngreso = VariablesDao.consultaFechaHoraServidor();
        List<String> lista = new ArrayList<>();
        lista.add(Fechas.dateToString(fechaIngreso, Fecha.PATRON_FECHA_HORA));
        lista.add(String.valueOf(numeroOtp));

        String numeroTelefono = CreditoDao.getNumeroTelefono(credito);

        EnvioSmsGeneralDTO envioSmsGeneralDTO = new EnvioSmsGeneralDTO();
        envioSmsGeneralDTO.setIdProceso(Integer.parseInt(codigoPlantilla));
        envioSmsGeneralDTO.setNumeroTelefono(numeroTelefono);
        envioSmsGeneralDTO.setDatos(lista);

        String uidCodigoOtp = UUID.randomUUID().toString();
        CodigoOtp codigoOtp = new CodigoOtp();
        codigoOtp.setUidCodigoOtp(uidCodigoOtp);
        codigoOtp.setIdCredito(credito);
        codigoOtp.setOtp((long) numeroOtp);
        codigoOtp.setCodAlm(Sesion.getTienda().getCodalm());
        codigoOtp.setFechaCreacion(fechaIngreso);
        codigoOtp.setUsuarioSolicita(usuario);
        codigoOtp.setNumeroTelefono(numeroTelefono);
        codigoOtp.setUidTicket(uidTicket);
        CodigoOtpDao.crear(codigoOtp, em);

        ResponseDTO responseDTO = clienteRest.clientRestPOST(url + URL_ENVIO_SMS, envioSmsGeneralDTO, headers, ResponseDTO.class);
        if (!responseDTO.getExito()) {
            throw new ValidacionOTPException("Error en el servicio de smsplus " + responseDTO.getDescripcion());
        }

    }

    /**
     *
     * @param otp
     * @param uidTicket
     * @return
     * @throws com.comerzzia.jpos.util.exception.ValidacionOTPException
     * @throws Exception
     */
    public static boolean validarOTP(Long otp, String uidTicket) throws ValidacionOTPException, Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            boolean resultado = validarOTP(otp, uidTicket, em);
            em.getTransaction().commit();

            return resultado;

        } catch (ValidacionOTPException e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            LOG_POS.error("Error al validar el OTP", e);
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param otp
     * @param uidTicket
     * @param em
     * @return
     * @throws ValidacionOTPException
     * @throws Exception
     */
    public static boolean validarOTP(Long otp, String uidTicket, EntityManager em) throws ValidacionOTPException, Exception {

        CodigoOtp codigoOtp = CodigoOtpDao.consultarCodigoOtp(em, Sesion.getTienda().getCodalm(), otp, uidTicket);
        if (codigoOtp != null) {
            String variablesOTP = Variables.getVariable(Variables.VARIABLES_OTP_CREDITO_DIRECTO);
            String tiempoExpiracion = JsonUtil.getElementJson(variablesOTP, "tiempoExpiracion");
            if (tiempoExpiracion == null) {
                throw new ValidacionOTPException("No existe la variable VARIABLES.OTP.CREDITO.DIRECTO ");
            }
            Date fechaMaxima = Fechas.sumaMinutos(codigoOtp.getFechaCreacion(), Integer.parseInt(tiempoExpiracion));
            if (fechaMaxima.before(VariablesDao.consultaFechaHoraServidor())) {
                LOG_POS.info("Superó la fecha del otp, fecha máxima " + fechaMaxima + " y la hora actual es " + VariablesDao.consultaFechaHoraServidor());
                throw new ValidacionOTPException("El código OTP superó el tiempo de expiración");
            } else {
                codigoOtp.setFechaAprobacion(VariablesDao.consultaFechaHoraServidor());
                CodigoOtpDao.actualizar(codigoOtp, em);
                LOG_POS.info("Otp válido " + codigoOtp.getFechaCreacion() + " y la hora actual es " + VariablesDao.consultaFechaHoraServidor());
            }

        } else {
            throw new ValidacionOTPException("No existe el código OTP ");
        }

        return Boolean.TRUE;
    }

    /**
     * 
     * @param uidTicket
     * @return
     * @throws ValidacionOTPException
     * @throws Exception 
     */
    public static boolean validarTiempoExpiracion(String uidTicket) throws ValidacionOTPException, Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();

            boolean resultado = validarTiempoExpiracion(uidTicket, em);

            return resultado;

        } catch (Exception e) {
            LOG_POS.error("Error al validar el OTP", e);
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * 
     * @param uidTicket
     * @param em
     * @return
     * @throws ValidacionOTPException
     * @throws Exception 
     */
    public static boolean validarTiempoExpiracion(String uidTicket, EntityManager em) throws ValidacionOTPException, Exception {

        CodigoOtp codigoOtp = CodigoOtpDao.consultarCodigoOtpActual(em, Sesion.getTienda().getCodalm(), uidTicket);
        if (codigoOtp != null) {
            String variablesOTP = Variables.getVariable(Variables.VARIABLES_OTP_CREDITO_DIRECTO);
            String tiempoExpiracion = JsonUtil.getElementJson(variablesOTP, "tiempoExpiracion");
            if (tiempoExpiracion == null) {
                throw new ValidacionOTPException("No existe la variable VARIABLES.OTP.CREDITO.DIRECTO ");
            }
            Date fechaMaxima = Fechas.sumaMinutos(codigoOtp.getFechaCreacion(), Integer.parseInt(tiempoExpiracion));
            if (fechaMaxima.before(VariablesDao.consultaFechaHoraServidor())) {
                return Boolean.TRUE;
            } else {
                throw new ValidacionOTPException("El código OTP todavia no supera el tiempo de expiración");
            }

        } else {
            LOG_POS.error("No existe el código OTP ");
            return Boolean.TRUE;
        }
        
    }

}
