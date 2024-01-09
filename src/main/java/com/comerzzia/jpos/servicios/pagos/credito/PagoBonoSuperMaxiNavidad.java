/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.dto.supermaxi.SolicitudSupermaxiDTO;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoPagos;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.FacturacionTarjetaException;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.ServicioFacturacionTarjetas;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.log.Logger;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 *
 * @author DAS
 */
public class PagoBonoSuperMaxiNavidad extends PagoCredito {

    protected static Logger log = Logger.getMLogger(PagoBonoSuperMaxiNavidad.class);

    public static final String ERROR_TIMEOUT = "ERROR TIMEOUT";
    public static final String ERROR_TIMEOUT_OBSERVACION = "Existen problemas de comunicaci\u00F3n, Solicitar autorizaci\u00F3n a la corporaci\u00F3n";
    public static final String ERROR_TARJETA_NO_VALIDA = "Tarjeta no V\u00E1lida";

    protected Integer mesesPosfechado;
    private String numAutorizacion;
    private String numSecuencialEnvio;
    private String numSecuencialRespuesta;
    private String cedula;
    private String establecimiento;
    private String idFactura;
    private String peticionTramaEnvio;
    private boolean respuestaAnulacion;
    private String respuestaAnular;

    public PagoBonoSuperMaxiNavidad(PagosTicket pagos, Cliente cliente, String factura, String autorizador) {
        super(pagos, cliente, autorizador);
        mesesPosfechado = 0;
        idFactura = factura;
    }

//    @Override
//    public void autorizarPago() throws AutorizadorException {
//        String trama = "";
//        String respuesta = "";
//        numAutorizacion = "      ";
//        try {
//            PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.COMPRA, idFactura, null);
//            trama = peticionBonoSuperMaxiNavidad.devolverTrama();
//            respuesta = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
//            numAutorizacion = respuesta.substring(30, 36); //Rescatamos el numero de autorización de la respuesta
//
//            //Comprobamos que la longitud de la respuesta sea correcta y que la autorización no esté en blanco
//            if (respuesta.length() == 222 && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000")) {
//                log.debug("autorizarPago() - La respuesta ha sido correcta");
//                setValidadoAutomatico(true);
//                RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
//                //Construimos la respuesta
//                respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
//                respuestaPago.setNumeroAutorizacion(numAutorizacion);
//                establecimiento = respuesta.substring(66, 76);
//                respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
//                cedula = respuesta.substring(116, 131);
//                respuestaPago.setTerminalId(respuesta.substring(58, 66));
//
//                setPinpadRespuesta(respuestaPago);
//
//                try {
//                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 1");
//                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
//                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
//                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
//                    ft.setCodigoRespuesta(respuesta.substring(36, 38));
//                    ft.setMensajeProceso(respuesta.substring(38, 58));
//                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
//                    setUidFacturacion(ft.getId());
//                } catch (FacturacionTarjetaException ex) {
//                    log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
//                }
//            } else { //Respuesta recibida NO es correcta
//                RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
//                //Construimos la respuesta
//                respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
//                respuestaPago.setNumeroAutorizacion(numAutorizacion);
//                establecimiento = respuesta.substring(66, 76);
//                respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
//                cedula = respuesta.substring(116, 131);
//                respuestaPago.setTerminalId(respuesta.substring(58, 66));
//
//                setPinpadRespuesta(respuestaPago);
//                try {
//                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 2");
//                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
//                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
//                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
//                    ft.setCodigoRespuesta(respuesta.substring(36, 38));
//                    ft.setMensajeProceso(respuesta.substring(38, 58));
//                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
//                    setUidFacturacion(ft.getId());
//                } catch (FacturacionTarjetaException ex) {
//                    log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
//                }
//                throw new IOException("");
//            }
//        } catch (SocketTimeoutException e) { //REVERSA: Se ha producido un TIMEOUT
//            try {
//                log.error("autorizarPago() - No ha habido respuesta, enviamos una trama de Reversa");
//                FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
//                ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
//                ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
//                ft.setCodigoRespuesta(null);
//                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
//                setUidFacturacion(ft.getId());
//
//                PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.REVERSA, idFactura, null);
//                trama = peticionBonoSuperMaxiNavidad.devolverTrama();
//                String respuestaReversa = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
//                log.debug("autorizarPago() - Respuesta de la trama de reversa: " + respuestaReversa);
//
//                RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
//                //Construimos la respuesta
//                respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
//                respuestaPago.setNumeroAutorizacion(numAutorizacion);
//                establecimiento = respuesta.substring(66, 76);
//                respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
//                cedula = respuesta.substring(116, 131);
//                respuestaPago.setTerminalId(respuesta.substring(58, 66));
//                setPinpadRespuesta(respuestaPago);
//                log.debug("autorizarPago() - Insertando FacturacionTarjeta 3");
//                FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
//                ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
//                ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
//                ftr.setCodigoRespuesta(null);
//                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
//                setUidFacturacion(ftr.getId());
//            } catch (Exception e2) {
//                try {
//                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 4");
//                    FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
//                    ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
//                    ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
//                    ftr.setCodigoRespuesta(null);
//                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
//                    setUidFacturacion(ftr.getId());
//                    log.debug("autorizarPago() - No se ha recibido respuesta al enviar la trama de Reversa");
//                } catch (FacturacionTarjetaException ex) {
//                    java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            throw new AutorizadorException("No se ha recibido respuesta.");
//        } catch (IOException e) { //ANULACIÓN: No se ha procesado el pago correctamente
//            throw new AutorizadorException("No se ha podido realizar el pago");
//        }
//    }
    @Override
    public void anularAutorizacionPago(List<Pago> anuladosImpresos) throws AutorizadorException {
        if (Variables.getVariable(Variables.ACTIVO_SERVICIO_WEB_SUPERMAXI).equals("S")) {
            try {
                if (isValidadoManual()) {
                    setValidadoManual(false);
                } else if (isValidadoAutomatico()) { // Validado automático     
                    setValidadoAutomatico(false);
                    log.error("autorizarPago() - No se ha podido procesar el pago, enviamos una trama de Anulación");
                    //d factura Rd
                    PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.ANULACION, idFactura, numAutorizacion, numSecuencialEnvio);
                    String trama = peticionBonoSuperMaxiNavidad.devolverTrama();
//                String respuestaAnulacion = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
                    SolicitudSupermaxiDTO solicitudSupermaxiDTO = new SolicitudSupermaxiDTO();
                    solicitudSupermaxiDTO.setUidTicket(UUID.randomUUID().toString());
                    solicitudSupermaxiDTO.setTrama(trama);
                    peticionTramaEnvio = peticionJson(solicitudSupermaxiDTO);
                    log.info("Se envia trama de anulación.....Entrada ");
                    respuestaAnulacion = peticionBonoSuperMaxiNavidad.enviarTramaServicioWebAnulacion(solicitudSupermaxiDTO);
                    log.info("Se envia trama de anulación.....Salida ");
                    setTramaEnvio(peticionTramaEnvio);
                    respuestaAnular = (respuestaAnulacion) ? "true" : "false";
                    setTramaRespuesta(respuestaAnular);
                    log.debug("autorizarPago() - Respuesta de la trama de anulación: " + respuestaAnulacion);

//                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
//                    respuestaPago.setSecuencialTransaccion(respuestaAnulacion.substring(10, 16));
//                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
//                    establecimiento = respuestaAnulacion.substring(66, 76);
//                    respuestaPago.setNombreTarjetaHabiente(respuestaAnulacion.substring(76, 116));
//                    cedula = respuestaAnulacion.substring(116, 131);
//                    respuestaPago.setTerminalId(respuestaAnulacion.substring(58, 66));
//                    setPinpadRespuesta(respuestaAnular);
                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 5");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setMensajeProceso(respuestaAnular);
                    ft.setTramaEnvio(peticionTramaEnvio);
                    ft.setTramaRespuesta(respuestaAnular);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
//            setTramaEnvio(trama);
//            setTramaRespuesta(respuestaAnulacion);
                }
            } catch (Exception e2) {
                try {
                    log.debug("autorizarPago() - Ha ocurrido un error al enviar la trama de Anulación");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setCodigoRespuesta(null);
                    ft.setTramaEnvio(peticionTramaEnvio);
                    ft.setTramaRespuesta(respuestaAnular);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
                    throw new AutorizadorException("Ha ocurrido un error al procesar la Anulación");
                } catch (FacturacionTarjetaException ex) {
                    java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            try {
                if (isValidadoManual()) {
                    setValidadoManual(false);
                } else if (isValidadoAutomatico()) { // Validado automático     
                    setValidadoAutomatico(false);
                    log.error("autorizarPago() - No se ha podido procesar el pago, enviamos una trama de Anulación");
                    //d factura Rd
                    PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.ANULACION, idFactura, numAutorizacion, numSecuencialEnvio);
                    String trama = peticionBonoSuperMaxiNavidad.devolverTrama();
                    String respuestaAnulacion = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
                    log.debug("autorizarPago() - Respuesta de la trama de anulación: " + respuestaAnulacion);

                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuestaAnulacion.substring(10, 16));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuestaAnulacion.substring(66, 76);
                    respuestaPago.setNombreTarjetaHabiente(respuestaAnulacion.substring(76, 116));
                    cedula = respuestaAnulacion.substring(116, 131);
                    respuestaPago.setTerminalId(respuestaAnulacion.substring(58, 66));
                    setPinpadRespuesta(respuestaPago);

                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 5");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setCodigoRespuesta(respuestaAnulacion.substring(36, 38));
                    ft.setMensajeProceso(respuestaAnulacion.substring(38, 58));
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
                    setTramaEnvio(trama);
                    setTramaRespuesta(respuestaAnulacion);
                }
            } catch (Exception e2) {
                try {
                    log.debug("autorizarPago() - Ha ocurrido un error al enviar la trama de Anulación");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setCodigoRespuesta(null);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
                    throw new AutorizadorException("Ha ocurrido un error al procesar la Anulación");
                } catch (FacturacionTarjetaException ex) {
                    java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public String consultarSaldo(String tident) throws AutorizadorException {
        String trama = "";
        String respuesta = "";
        numAutorizacion = "      ";
        try {
            PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(tident);
            trama = peticionBonoSuperMaxiNavidad.devolverTrama();
            respuesta = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
            return respuesta;
        } catch (SocketTimeoutException e) {
            return ERROR_TIMEOUT;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ERROR";

    }

    @Override
    public String consultarSaldoWeb(String tident) throws AutorizadorException {
        String trama = "";
        String respuesta = "";
        numAutorizacion = "      ";
        try {
            PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(tident);
            SolicitudSupermaxiDTO solicitudSupermaxiDTO = new SolicitudSupermaxiDTO();
            trama = peticionBonoSuperMaxiNavidad.devolverTrama();
            solicitudSupermaxiDTO.setUidTicket(UUID.randomUUID().toString());
            solicitudSupermaxiDTO.setTrama(trama);
            respuesta = peticionBonoSuperMaxiNavidad.enviarTramaServicioWeb(solicitudSupermaxiDTO);
            return respuesta;
        } catch (SocketTimeoutException e) {
            return ERROR_TIMEOUT;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ERROR";

    }

    /**
     * Este método consulta Servicio Web al Supermaxi o por Socket.
     *
     * @throws AutorizadorException
     */
    @Override
    public void autorizarPago() throws AutorizadorException {
        if (Variables.getVariable(Variables.ACTIVO_SERVICIO_WEB_SUPERMAXI).equals("S")) {
            SolicitudSupermaxiDTO solicitudSupermaxiDTO = new SolicitudSupermaxiDTO();
            String trama = "";
            String respuesta = "";
            String msmAprobacion = "";
            numAutorizacion = "      ";
            try {
                PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.COMPRA, idFactura, null, null);
                trama = peticionBonoSuperMaxiNavidad.devolverTrama();
                solicitudSupermaxiDTO.setUidTicket(UUID.randomUUID().toString());
                solicitudSupermaxiDTO.setTrama(trama);
                peticionTramaEnvio = peticionJson(solicitudSupermaxiDTO);
                log.info("Se envía trama al servicio..... Entrada...");
                respuesta = peticionBonoSuperMaxiNavidad.enviarTramaServicioWeb(solicitudSupermaxiDTO);
                log.info("Se envía trama al servicio..... Salida....");
                if (respuesta == null) {
                    throw new AutorizadorException("No se ha podido realizar el pago");
                }
                numAutorizacion = respuesta.substring(36, 42); //Rescatamos el numero de autorización de la respuesta
                numSecuencialEnvio = trama.substring(71, 77); //Rescatamos el numero de secuencial de la trama de envio
                numSecuencialRespuesta = respuesta.substring(16, 22); //Rescatamos el numero de secuencial de la trama de respuesta
                msmAprobacion = respuesta.substring(44, 52);
                //Comprobamos que la longitud de la respuesta sea correcta y que la autorización no esté en blanco
                if (!numAutorizacion.equals("      ") && !numAutorizacion.equals("000000") && numSecuencialEnvio.equals(numSecuencialRespuesta)) {
//                    if ((respuesta.length() == 216 && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000") && numSecuencialEnvio.equals(numSecuencialRespuesta)) || (respuesta.length() == 257 && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000") && numSecuencialEnvio.equals(numSecuencialRespuesta))) {
//                if ((msmAprobacion.equals("APROBADO") && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000")) || (msmAprobacion.equals("APROBADO") && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000"))) {
                    log.debug("autorizarPagoPrueba() - La respuesta ha sido correcta");
                    setValidadoAutomatico(true);
                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(16, 22));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(72, 82);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(82, 122));
                    cedula = respuesta.substring(122, 137);
                    respuestaPago.setTerminalId(respuesta.substring(64, 72));

                    setPinpadRespuesta(respuestaPago);

                    try {
                        log.debug("autorizarPagoServicioWeb() - Insertando FacturacionTarjeta 1");
                        FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                        ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ft.setCodigoRespuesta(respuesta.substring(42, 44));
                        ft.setMensajeProceso(respuesta.substring(44, 64));
                        ft.setTramaEnvio(peticionTramaEnvio);
                        ft.setTramaRespuesta(respuesta);
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                        setUidFacturacion(ft.getId());
                        setTramaEnvio(peticionTramaEnvio);
                        setTramaRespuesta(respuesta);
                    } catch (FacturacionTarjetaException ex) {
                        log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
                    }
                } else { //Respuesta recibida NO es correcta
                    numAutorizacion = "      ";
                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(16, 22));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(72, 82);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(82, 122));
                    cedula = respuesta.substring(122, 137);
                    respuestaPago.setTerminalId(respuesta.substring(64, 72));

                    setPinpadRespuesta(respuestaPago);
                    try {
                        log.debug("autorizarPago() - Insertando FacturacionTarjeta 2");
                        FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                        ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ft.setCodigoRespuesta(respuesta.substring(42, 44));
                        ft.setMensajeProceso(respuesta.substring(44, 64));
                        ft.setTramaEnvio(peticionTramaEnvio);
                        ft.setTramaRespuesta(respuesta);
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    } catch (FacturacionTarjetaException ex) {
                        log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
                    }
                    throw new IOException("");
                }
            } catch (SocketTimeoutException e) {
                String respuestaReversa = "";//REVERSA: Se ha producido un TIMEOUT
                try {
                    log.error("autorizarPago() - No ha habido respuesta, enviamos una trama de Reversa");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setCodigoRespuesta(null);
                    ft.setTramaEnvio(peticionTramaEnvio);
                    ft.setTramaRespuesta(respuesta);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
                    setTramaEnvio(peticionTramaEnvio);
                    setTramaRespuesta(respuesta);
                    PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.REVERSA, idFactura, null, null);
                    solicitudSupermaxiDTO = new SolicitudSupermaxiDTO();
                    trama = peticionBonoSuperMaxiNavidad.devolverTrama();
                    solicitudSupermaxiDTO.setUidTicket(UUID.randomUUID().toString());
                    solicitudSupermaxiDTO.setTrama(trama);
                    peticionTramaEnvio = peticionJson(solicitudSupermaxiDTO);
                    respuestaReversa = peticionBonoSuperMaxiNavidad.enviarTramaServicioWeb(solicitudSupermaxiDTO);
                    log.debug("autorizarPagoPrueba() - Respuesta de la trama de reversa: " + respuestaReversa);

                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(16, 22));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(72, 82);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(82, 122));
                    cedula = respuesta.substring(122, 137);
                    respuestaPago.setTerminalId(respuesta.substring(64, 72));
                    setPinpadRespuesta(respuestaPago);
                    log.debug("autorizarPagoPrueba() - Insertando FacturacionTarjeta 3");
                    FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ftr.setCodigoRespuesta(null);
                    ft.setTramaEnvio(peticionTramaEnvio);
                    ft.setTramaRespuesta(respuestaReversa);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
                    setUidFacturacion(ftr.getId());
                    setTramaEnvio(peticionTramaEnvio);
                    setTramaRespuesta(respuesta);
                } catch (Exception e2) {
                    try {
                        log.debug("autorizarPagoPrueba() - Insertando FacturacionTarjeta 4");
                        FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                        ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ftr.setCodigoRespuesta(null);
                        ftr.setTramaEnvio(peticionTramaEnvio);
                        ftr.setTramaRespuesta(respuesta);
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
                        setUidFacturacion(ftr.getId());
                        setTramaEnvio(peticionTramaEnvio);
                        setTramaRespuesta(respuesta);
                        log.debug("autorizarPagoPrueba() - No se ha recibido respuesta al enviar la trama de Reversa");
                    } catch (FacturacionTarjetaException ex) {
                        java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                throw new AutorizadorException("No se ha recibido respuesta.");
            } catch (IOException e) { //ANULACIÓN: No se ha procesado el pago correctamente
                throw new AutorizadorException("No se ha podido realizar el pago");
            }
        } else {
            String trama = "";
            String respuesta = "";
            numAutorizacion = "      ";
            try {
                PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad;
                peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.COMPRA, idFactura, null, null);
                trama = peticionBonoSuperMaxiNavidad.devolverTrama();
                respuesta = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
                numAutorizacion = respuesta.substring(30, 36); //Rescatamos el numero de autorización de la respuesta

                //Comprobamos que la longitud de la respuesta sea correcta y que la autorización no esté en blanco
                if (respuesta.length() == 222 && !numAutorizacion.equals("      ") && !numAutorizacion.equals("000000")) {
                    log.debug("autorizarPago() - La respuesta ha sido correcta");
                    setValidadoAutomatico(true);
                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(66, 76);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
                    cedula = respuesta.substring(116, 131);
                    respuestaPago.setTerminalId(respuesta.substring(58, 66));

                    setPinpadRespuesta(respuestaPago);

                    try {
                        log.debug("autorizarPago() - Insertando FacturacionTarjeta 1");
                        FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                        ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ft.setCodigoRespuesta(respuesta.substring(36, 38));
                        ft.setMensajeProceso(respuesta.substring(38, 58));
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                        setUidFacturacion(ft.getId());
                        setTramaEnvio(peticionTramaEnvio);
                        setTramaRespuesta(respuesta);
                    } catch (FacturacionTarjetaException ex) {
                        log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
                    }
                } else { //Respuesta recibida NO es correcta
                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(66, 76);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
                    cedula = respuesta.substring(116, 131);
                    respuestaPago.setTerminalId(respuesta.substring(58, 66));

                    setPinpadRespuesta(respuestaPago);
                    try {
                        log.debug("autorizarPago() - Insertando FacturacionTarjeta 2");
                        FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                        ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ft.setCodigoRespuesta(respuesta.substring(36, 38));
                        ft.setMensajeProceso(respuesta.substring(38, 58));
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                        setUidFacturacion(ft.getId());
                        setTramaEnvio(peticionTramaEnvio);
                        setTramaRespuesta(respuesta);
                    } catch (FacturacionTarjetaException ex) {
                        log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
                    }
                    throw new IOException("");
                }
            } catch (SocketTimeoutException e) { //REVERSA: Se ha producido un TIMEOUT
                try {
                    log.error("autorizarPago() - No ha habido respuesta, enviamos una trama de Reversa");
                    FacturacionTarjeta ft = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "O");
                    ft.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ft.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ft.setCodigoRespuesta(null);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    setUidFacturacion(ft.getId());
                    setTramaEnvio(peticionTramaEnvio);
                    setTramaRespuesta(respuesta);
                    PeticionBonoSuperMaxiNavidad peticionBonoSuperMaxiNavidad = new PeticionBonoSuperMaxiNavidad(this, PeticionBonoSuperMaxiNavidad.REVERSA, idFactura, null, null);
                    trama = peticionBonoSuperMaxiNavidad.devolverTrama();
                    String respuestaReversa = peticionBonoSuperMaxiNavidad.enviarTrama(trama);
                    log.debug("autorizarPago() - Respuesta de la trama de reversa: " + respuestaReversa);

                    RespuestaProcesoPagos respuestaPago = new RespuestaProcesoPagos();
                    //Construimos la respuesta
                    respuestaPago.setSecuencialTransaccion(respuesta.substring(10, 16));
                    respuestaPago.setNumeroAutorizacion(numAutorizacion);
                    establecimiento = respuesta.substring(66, 76);
                    respuestaPago.setNombreTarjetaHabiente(respuesta.substring(76, 116));
                    cedula = respuesta.substring(116, 131);
                    respuestaPago.setTerminalId(respuesta.substring(58, 66));
                    setPinpadRespuesta(respuestaPago);
                    log.debug("autorizarPago() - Insertando FacturacionTarjeta 3");
                    FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                    ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                    ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
                    ftr.setCodigoRespuesta(null);
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
                    setUidFacturacion(ftr.getId());
                    setTramaEnvio(peticionTramaEnvio);
                    setTramaRespuesta(respuesta);
                } catch (Exception e2) {
                    try {
                        log.debug("autorizarPago() - Insertando FacturacionTarjeta 4");
                        FacturacionTarjeta ftr = new FacturacionTarjeta(PagoBonoSuperMaxiNavidad.this, this.getTicket(), "R");
                        ftr.setValor(Cadena.ofuscarTarjeta(this.getTarjetaCredito().getNumero()).replaceAll(" ", ""));
                        ftr.setLongitudTarjeta(this.getNumeroTarjeta().length());
                        ftr.setCodigoRespuesta(null);
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ftr);
                        setUidFacturacion(ftr.getId());
                        setTramaEnvio(peticionTramaEnvio);
                        setTramaRespuesta(respuesta);
                        log.debug("autorizarPago() - No se ha recibido respuesta al enviar la trama de Reversa");
                    } catch (FacturacionTarjetaException ex) {
                        java.util.logging.Logger.getLogger(PagoBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                throw new AutorizadorException("No se ha recibido respuesta.");
            } catch (IOException e) { //ANULACIÓN: No se ha procesado el pago correctamente
                throw new AutorizadorException("No se ha podido realizar el pago");
            }
        }

    }

    public String peticionJson(Object object) {
        String input;

        if (object instanceof String) {
            input = (String) object;
        } else {
            input = JsonUtil.objectToJson(object);
        }
        return input;
    }

    public String getCedula() {
        return cedula;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    @Override
    public boolean isSoloPagoCredito() {
        return false;
    }

    public Integer getMesesPosfechado() {
        return mesesPosfechado;
    }

    public void setMesesPosfechado(Integer mesesPosfechado) {
        this.mesesPosfechado = mesesPosfechado;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumSecuencialRespuesta() {
        return numSecuencialRespuesta;
    }

    public void setNumSecuencialRespuesta(String numSecuencialRespuesta) {
        this.numSecuencialRespuesta = numSecuencialRespuesta;
    }

}
