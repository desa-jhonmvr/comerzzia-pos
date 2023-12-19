/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.fasttrack;

import DF.EnvioProcesoConfigPinPad;
import DF.EnvioProcesoControl;
import DF.EnvioProcesoPago;
import DF.LAN;
import DF.RespuestaConsultaTarjeta;
import DF.RespuestaLecturaTarjeta;
import DF.RespuestaProcesoConfigPinPad;
import DF.RespuestaProcesoControl;
import DF.RespuestaProcesoPago;
import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.pinpad.peticion.PeticionProcesoPagos;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.FacturacionTarjetaException;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.ServicioFacturacionTarjetas;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author SMLM
 */
public class PinPadFasttrackAutomatico implements IPinPadFasttrack {

    public static final String CONFIGURACION_PINPAD = "CP";
    public static final String CONFIGURACION_BASICA_PINPAD = "CB";
    public static final String CONSULTA_TARJETA = "CT";
    public static final String LECTURA_TARJETA = "LT";
    public static final String PROCESO_PAGOS = "PP";
    public static final String PROCESO_CONTROL = "PC";
    public static final String PROCESO_ACTUALIAZCION = "PA";

    private static LAN lan;

    public static final Integer COM = 9;
    public static final Integer grabaLog = 1;
    public static final Integer grabaMsg = 1;
    public int maxLength = 3000;

    protected static Logger log = Logger.getMLogger(PinPadFasttrackAutomatico.class);
    private static PinPadFasttrackAutomatico operPinPad;
    private RespuestaProcesoPago pinpadRespuestaPagos;
    private TicketS ticket;
    private PagoCredito pago;

    private String lote;

    public static PinPadFasttrackAutomatico getInstance() {
        if (operPinPad == null) {
            operPinPad = new PinPadFasttrackAutomatico();
            lan = new LAN(PinPadFasttrack.cfg);
        }
        return operPinPad;
    }

    @Override
    public RespuestaProcesoConfigPinPad configuracionPinPad() throws PinPadFasttrackException {
        EnvioProcesoConfigPinPad env = new EnvioProcesoConfigPinPad();
        env.DireccionIP = Sesion.getTienda().getSriTienda().getCajaActiva().getIpPinpad();
        env.Mascara = Sesion.getTienda().getSriTienda().getCajaActiva().getMascaraPinpad();
        env.Gateway = Sesion.getTienda().getSriTienda().getCajaActiva().getGatewayPinpad();
        RespuestaProcesoConfigPinPad res = lan.ProcesoConfigPinPad(env);
        return res;
    }

    @Override
    public RespuestaConsultaTarjeta consultaTarjeta() throws PinPadFasttrackException {
        RespuestaConsultaTarjeta res = lan.ConsultaTarjeta();
        return res;
    }

    @Override
    public RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadFasttrackException {
        RespuestaLecturaTarjeta res = lan.LecturaTarjeta();
        if (res == null || res.TarjetaTruncada == null) {
            log.error("lecturaTarjeta() - Error al leer la tarjeta ");
            throw new PinPadFasttrackException("lecturaTarjeta() - Error al leer la tarjeta ");
        }
        return res;
    }

    @Override
    public RespuestaProcesoControl procesoControl() throws PinPadFasttrackException {
        try {
            FacturacionTarjeta facturaMaximaPinPadDataFast = ServicioFacturacionTarjetas.obtenerMaximoSecuencialPinPad("1", Sesion.getCajaActual().getCajaActual().getCodcaja());
            EnvioProcesoControl env = new EnvioProcesoControl();
            env.MID = VariablesAlm.getVariable(VariablesAlm.PINPAD_MID_DATAFAST);
            env.TID = Sesion.getTienda().getSriTienda().getCajaActiva().getTidDatafast();
            env.CajaID = "00" + Numero.completaconCeros(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), 3) + Numero.completaconCeros(Sesion.getDatosConfiguracion().getCodcaja(), 3);
            env.Lote = Cadena.completaconCeros("" + (Integer.parseInt(facturaMaximaPinPadDataFast.getLote()) + 1), 6);
            env.Referencia = "000001";
            RespuestaProcesoControl res = lan.ProcesoControl(env);
            return res;
        } catch (FacturacionTarjetaException ex) {
            java.util.logging.Logger.getLogger(PinPadFasttrackAutomatico.class.getName()).log(Level.SEVERE, null, ex);
            throw new PinPadFasttrackException(ex);
        }
        // return null;
    }

    @Override
    public RespuestaProcesoPago procesoPagos(int tipoTransaccion, String codigoDiferido, String base0, String baseImponible, String iva, String montoTotal, String plazoDiferido, String redAdquiriente) throws PinPadFasttrackException {
        EnvioProcesoPago env = new EnvioProcesoPago();
        env.TipoTransaccion = tipoTransaccion;
        env.RedAdquirente = Integer.valueOf(redAdquiriente);
        //env.CodigoDiferido = codigoDiferido;
        env.Base0 = base0;
        env.BaseImponible = baseImponible;
        env.IVA = iva;
        env.MontoTotal = montoTotal;
        env.Fecha = new Date();
        env.Hora = env.Fecha;
        if (plazoDiferido.trim().equals("")) {
            plazoDiferido = "00";
            env.CodigoDiferido = "00";
        } else {
            env.CodigoDiferido = "01";
        }

        env.PlazoDiferido = plazoDiferido;
        RespuestaProcesoPago res = null;
        res = lan.ProcesoPago(env);
        if (res != null) {
            pago.setMensajePromocional(res.CodigoRespuestaAut + " " + res.MensajeRespuestaAut + " / " + res.CodigoRespuesta);
        }

        //Guardamos la transaccion
        try {
            FacturacionTarjeta ft = new FacturacionTarjeta(pago, ticket, "O");
            if (res.ObtenerTrama().length() >= maxLength) {
                ft.setTramaEnvio(env.ObtenerTrama().substring(0, 2990));
                ft.setTramaRespuesta(res.ObtenerTrama().substring(0, 2990));
                pago.setTramaEnvio(env.ObtenerTrama().substring(1, 2990));
            } else {
                ft.setTramaEnvio(env.ObtenerTrama());
                ft.setTramaRespuesta(res.ObtenerTrama());
                pago.setTramaEnvio(env.ObtenerTrama());
            }
            ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
            pago.setUidFacturacion(ft.getId());
        } catch (FacturacionTarjetaException ex) {
            log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
        }
        if (res == null || res.CodigoRespuesta == null) {
            log.error("procesoPagos() - Respuesta null");
            log.debug("procesoPagos() - res == null || res.CodigoRespuestaAut == null , enviando trama de reverso...");
            // procesoReversoAutomatico(Integer.valueOf(PeticionProcesoPagos.REVERSO_PAGO), codigoDiferido, base0, baseImponible, iva, montoTotal, env.Fecha);
            //Guardamos el reverso
            try {
                FacturacionTarjeta ft = new FacturacionTarjeta(pago, ticket, "R");
                if (res.ObtenerTrama().length() >= maxLength) {
                    ft.setTramaEnvio(env.ObtenerTrama().substring(0, 2990));
                    ft.setTramaRespuesta(res.ObtenerTrama().substring(0, 2990));
                    pago.setTramaEnvio(env.ObtenerTrama().substring(1, 2990));
                } else {
                    ft.setTramaEnvio(env.ObtenerTrama());
                    ft.setTramaRespuesta(res.ObtenerTrama());
                    pago.setTramaEnvio(env.ObtenerTrama());
                }
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
            } catch (FacturacionTarjetaException ex) {
                log.error("Ha ocurrido un error insertando la trama de reverso: " + ex.getMessage());
            }
            throw new PinPadFasttrackException("Revise el cable de conexión a la red de su equipo pinpad ");
        }

        if (!res.CodigoRespuesta.equals("00")) {
            if (res.CodigoRespuesta.equals("TO")) {
                // if(res.CodigoRespuesta.equals("TO") || res.CodigoRespuesta.equals("ER") || res.CodigoRespuesta.equals("20")){
                //Es un timeout, tenemos que enviar un reverso automático de la transacción (Código 04)
                log.debug("procesoPagos() - Se ha producido un Timeout al realizar el proceso de pagos, enviando trama de reverso...");
                procesoReversoAutomatico(Integer.valueOf(PeticionProcesoPagos.REVERSO_PAGO), codigoDiferido, base0, baseImponible, iva, montoTotal, env.Fecha);
                //Guardamos el reverso
                try {
                    FacturacionTarjeta ft = new FacturacionTarjeta(pago, ticket, "R");
                    if (res.ObtenerTrama().length() >= maxLength) {
                        ft.setTramaEnvio(env.ObtenerTrama().substring(0, 2990));
                        ft.setTramaRespuesta(res.ObtenerTrama().substring(0, 2990));
                        pago.setTramaEnvio(env.ObtenerTrama().substring(1, 2990));
                    } else {
                        ft.setTramaEnvio(env.ObtenerTrama());
                        ft.setTramaRespuesta(res.ObtenerTrama());
                        pago.setTramaEnvio(env.ObtenerTrama());
                    }
                    ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                } catch (FacturacionTarjetaException ex) {
                    log.error("Ha ocurrido un error insertando la trama de reverso: " + ex.getMessage());
                }
                throw new PinPadFasttrackException("Se ha producido un timeout al realizar el proceso de pagos y se ha enviado una trama de reverso de la operación codigo " + res.CodigoRespuestaAut + " / " + res.CodigoRespuesta);
            } else {
                log.error("procesoPagos() - Otro Error al realizar el proceso de pagos, error en el código de respuesta del mensaje, código de error: " + res.CodigoRespuesta + ". Trama de respuesta: " + res.MensajeRespuestaAut);
                throw new PinPadFasttrackException("Otro Error en el código de respuesta del mensaje. Código de error: " + res.CodigoRespuestaAut + " " + res.MensajeRespuestaAut + " / " + res.CodigoRespuesta);
            }

        }
        if (!res.CodigoRespuestaAut.equals("00") || !res.CodigoRespuesta.equals("00")) {
            log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta de la transacción, código de error: " + res.CodigoRespuestaAut + ". Trama de respuesta: " + res.MensajeRespuestaAut + " / " + res.CodigoRespuesta);
            throw new PinPadFasttrackException("Error en el código de respuesta de la transacción. Código de error: " + res.CodigoRespuestaAut + " " + res.MensajeRespuestaAut + " / " + res.CodigoRespuesta);
        }

        if (res.Autorizacion == null || res.Autorizacion.trim().isEmpty()) {
            log.error("procesoPagos() - Error al realizar el proceso de pagos, ");
            throw new PinPadFasttrackException("El número de autorización es nulo. ");
        }
        return res;
    }

    public RespuestaProcesoPago procesoReversoAutomatico(int tipoTransaccion, String codigoDiferido, String base0, String baseImponible, String iva, String montoTotal, Date fecha) throws PinPadFasttrackException {
        EnvioProcesoPago env = new EnvioProcesoPago();
        env.TipoTransaccion = tipoTransaccion;
        env.RedAdquirente = 1;
        env.CodigoDiferido = codigoDiferido;
        env.Base0 = base0;
        env.BaseImponible = baseImponible;
        env.IVA = iva;
        env.MontoTotal = montoTotal;
        env.Fecha = fecha;
        env.Hora = fecha;
        RespuestaProcesoPago res = null;
        res = lan.ProcesoPago(env);
        return res;
    }

    @Override
    public String getLote() {
        /* if(lote == null){
            //Si el lote es null, es que aunque fuera automatica hemos terminado validando manualmente
            return PinPadManual.getLoteAuto();
        }       */
        return lote;
    }

    public RespuestaProcesoPago getPinpadRespuestaPagos() {
        return pinpadRespuestaPagos;
    }

    public void setPinpadRespuestaPagos(RespuestaProcesoPago pinpadRespuestaPagos) {
        this.pinpadRespuestaPagos = pinpadRespuestaPagos;
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public PagoCredito getPago() {
        return pago;
    }

    public void setPago(PagoCredito pago) {
        this.pago = pago;
    }

    @Override
    public RespuestaProcesoPago procesoAnulacion(int tipoTransaccion, String idReferencia, String numeroAutorizacion, String secuencial, String redAdquiriente) throws PinPadFasttrackException {
        RespuestaProcesoPago res = null;
        try {
            EnvioProcesoPago env = new EnvioProcesoPago();
            env.TipoTransaccion = tipoTransaccion;
            env.RedAdquirente = new Integer(redAdquiriente);
            env.Referencia = secuencial;
            env.Autorizacion = numeroAutorizacion;
            res = lan.ProcesoPago(env);
            log.debug("procesoPagos() - Respuesta del PinPad: " + res.toString());
            //Guardamos la transaccion
            try {
                FacturacionTarjeta ft = new FacturacionTarjeta(pago, "R", null, numeroAutorizacion);
                if (res.ObtenerTrama().length() >= maxLength) {
                    ft.setTramaEnvio(env.ObtenerTrama().substring(0, 2990));
                    ft.setTramaRespuesta(res.ObtenerTrama().substring(0, 2990));
                    pago.setTramaEnvio(env.ObtenerTrama().substring(1, 2990));
                } else {
                    ft.setTramaEnvio(env.ObtenerTrama());
                    ft.setTramaRespuesta(res.ObtenerTrama());
                    pago.setTramaEnvio(env.ObtenerTrama());
                }
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                pago.setUidFacturacion(ft.getId());
            } catch (FacturacionTarjetaException ex) {
                log.error("Ha ocurrido un error insertando la trama de facturacion: " + ex.getMessage());
            }
            if (!res.CodigoRespuesta.equals("00")) {
                if (res.CodigoRespuesta.equals("TO")) {
                    /* //Es un timeout, tenemos que enviar un reverso automático de la transacción (Código 04)
                    log.debug("procesoPagos() - Se ha producido un Timeout al realizar el proceso de pagos, enviando trama de reverso...");
                    peticion.setTipoTransaccion(PeticionProcesoPagos.REVERSO_PAGO);
                    String tramaReverso = peticion.dameTramaPagos();
                    log.debug("procesoPagos() - Proceso Pagos, reverso con trama: "+tramaReverso);
                    String respuestaReverso = pinpad.SendPinpad(tramaReverso, COM, timeout, grabaLog, grabaMsg);
                    pinpadRespuestaPagos = new RespuestaProcesoPagos(respuestaReverso);                   
                    log.debug("procesoPagos() - Respuesta del PinPad de la trama de reverso: "+respuestaReverso);
                    pago.setPinpadFasttrackRespuesta(res);
                    //Guardamos el reverso
                    try {
                        FacturacionTarjeta ft = new FacturacionTarjeta(pago,ticket, "R");
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    } catch(FacturacionTarjetaException ex){
                        log.error("Ha ocurrido un error insertando la trama de reverso: "+ex.getMessage());
                    }      */
                    throw new PinPadFasttrackException("Se ha producido un timeout al realizar el proceso de pagos y se ha enviado una trama de reverso de la operación");
                } else {
                    log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta del mensaje, código de error: " + res.CodigoRespuesta + ". Trama de respuesta: " + res.ObtenerTrama());
                    throw new PinPadFasttrackException("Error en el código de respuesta del mensaje. Código de error: " + res.MensajeRespuestaAut);
                }

            }
            lote = res.Lote;
            pago.setPinpadFasttrackRespuesta(res);

        } catch (PinPadFasttrackException e) {
            throw e;
        } catch (Exception e) {
            log.error("procesoPagos() - Error al realizar el proceso de pagos: " + e.getMessage(), e);
            throw new PinPadFasttrackException("Error inesperado: " + e.getMessage(), e);
        }
        return res;
    }

}
