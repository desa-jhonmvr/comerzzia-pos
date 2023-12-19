/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad;

import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.pinpad.peticion.PeticionProcesoPagos;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaConfiguracion;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaConsultaTarjeta;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaLecturaTarjeta;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoActualizacion;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoControl;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoPagos;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.FacturacionTarjetaException;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.ServicioFacturacionTarjetas;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.cadenas.Cadena;
import com.lectora.cnx.Pinpad;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author SMLM
 */
@Deprecated
public class PinPadAutomatico implements IPinPad {
    
    public static final String CONFIGURACION_PINPAD = "CP";
    public static final String CONSULTA_TARJETA="CT";
    public static final String LECTURA_TARJETA="LT";
    public static final String PROCESO_PAGOS = "PP";
    public static final String PROCESO_CONTROL = "PC";
    public static final String PROCESO_ACTUALIAZCION="PA";
    
    public static final Integer COM=9;
    public static final Integer grabaLog=1;
    public static final Integer grabaMsg = 1;
    
    protected static Logger log = Logger.getMLogger(PinPadAutomatico.class);
    private static PinPadAutomatico operPinPad;
    private static Pinpad pinpad;
    private static Integer timeout;
    private RespuestaProcesoPagos pinpadRespuestaPagos;
    private TicketS ticket;
    private PagoCredito pago;
    
    private String lote;
    
    
    public static PinPadAutomatico getInstance() {
        if(operPinPad==null){
            operPinPad = new PinPadAutomatico();
            timeout = VariablesAlm.getVariableAsInt(VariablesAlm.PINPAD_TIMEOUT);
        }
        if(pinpad ==null){
            pinpad = new Pinpad();
        }
        return operPinPad;
    }
     
    @Override
    public RespuestaConfiguracion configuracionPinPad(String ip, String mascara, String gateway) throws PinPadException{
        RespuestaConfiguracion rc = null;
        try{
            String tramaConfiguracion = CONFIGURACION_PINPAD;
            tramaConfiguracion+=Cadena.completaconBlancos(ip, 15);
            tramaConfiguracion+=Cadena.completaconBlancos(mascara,15);
            tramaConfiguracion+=Cadena.completaconBlancos(gateway, 15);
            log.debug("configuracionPinPad() - Configurando PinPad con timeout "+timeout+" sgs y trama: "+tramaConfiguracion);
            String respuesta = pinpad.SendPinpad(tramaConfiguracion, COM, timeout, grabaLog, grabaMsg);
            log.debug("configuracionPinPad() - Respuesta del PinPad: "+respuesta);
            rc = new RespuestaConfiguracion();
            rc.setRespuesta(respuesta);
            rc.setTipoMensaje(respuesta.substring(0,2));
            rc.setCodigoRespuestaMensaje(respuesta.substring(2,4));
            if(!rc.getCodigoRespuestaMensaje().equals("00")){
                log.error("configuracionPinPad() - Error configurando PinPad, error en el código de respuesta del mensaje, código de error: "+rc.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error configurando PinPad, error en el código de respuesta del mensaje, código de error: "+rc.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
            }              
            rc.setMensajeRespuesta(respuesta.substring(4));
        } catch (Exception e){
            log.error("configuracionPinpad() - Error al mandar la configuración del PinPad: "+e.getMessage(),e);
            throw new PinPadException(e);
        }
        return rc;
    }
   
    @Override
    public RespuestaConsultaTarjeta consultaTarjeta() throws PinPadException {
        RespuestaConsultaTarjeta rc = null;
        try{
            String tramaConsulta = CONSULTA_TARJETA;
            log.debug("consultaTarjeta() - Consultando tarjeta con timeout "+timeout+" sgs y trama: "+tramaConsulta);
            String respuesta = pinpad.SendPinpad(tramaConsulta, COM, timeout, grabaLog, grabaMsg);
            log.debug("consultaTarjeta() - Respuesta del PinPad: "+respuesta);
            rc = new RespuestaConsultaTarjeta();
            if(respuesta.length()==67){
                rc.setRespuesta(respuesta);
                rc.setTipoMensaje(respuesta.substring(0,2));
                rc.setCodigoRespuestaMensaje(respuesta.substring(2,4)); 
                rc.setNumeroTarjeta(respuesta.substring(4,41));
                rc.setBinTarjeta(respuesta.substring(41,47));
                rc.setMensajeRespuesta(respuesta.substring(47));
            } else {
                rc.setRespuesta(respuesta);
                rc.setTipoMensaje(respuesta.substring(0,2));
                rc.setCodigoRespuestaMensaje(respuesta.substring(2,4));
                rc.setNumeroTarjeta(respuesta.substring(4,41));
                rc.setMensajeRespuesta(respuesta.substring(41));
            }
            if(!rc.getCodigoRespuestaMensaje().equals("00")){
                log.error("consultaTarjeta() - Error consultando tarjeta PinPad, error en el código de respuesta del mensaje, código de error: "+rc.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error consultando tarjeta PinPad, error en el código de respuesta del mensaje, código de error: "+rc.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
            }            
        } catch (Exception e){
            log.error("consultaTarjeta() - Error al consultar la tarjeta: "+e.getMessage(),e);
            throw new PinPadException(e);
        }        
        return rc;
    }
    

    @Override
    public RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadException {
        RespuestaLecturaTarjeta rl = null;
        try{
            String tramaLectura = LECTURA_TARJETA;
            log.debug("lecturaTarjeta() - Leyendo tarjeta con timeout "+timeout+"sgs y con trama: "+tramaLectura);
            log.debug("lecturaTarjeta() - Esperando tiempo antes de nueva petición: " + Sesion.getDatosConfiguracion().getTimePinPad());
            Thread.sleep(Sesion.getDatosConfiguracion().getTimePinPad());
            String respuesta = pinpad.SendPinpad(tramaLectura, COM, timeout, grabaLog, grabaMsg);
            log.debug("lecturaTarjeta() - Respuesta del PinPad: "+respuesta);
            rl = new RespuestaLecturaTarjeta();
            if(respuesta.length()==88){
                //Parámetro XTA=1
                rl.setRespuesta(respuesta);
                rl.setTipoMensaje(respuesta.substring(0,2));
                rl.setCodigoRespuestaMensaje(respuesta.substring(2,4));
                rl.setValorFijo(respuesta.substring(4,6));
                rl.setNumeroTarjetaTruncada(respuesta.substring(6,31));
                rl.setNumeroTarjeta(respuesta.substring(31,68));
                rl.setMensajeRespuesta(respuesta.substring(68));
            } else {
                rl.setRespuesta(respuesta);
                rl.setTipoMensaje(respuesta.substring(0,2));
                rl.setCodigoRespuestaMensaje(respuesta.substring(2,4));
                rl.setValorFijo(respuesta.substring(4,6));
                rl.setNumeroTarjeta(respuesta.substring(6,31));
                rl.setMensajeRespuesta(respuesta.substring(31));
            }
            if(!rl.getCodigoRespuestaMensaje().equals("00")){
                log.error("lecturaTarjeta() - Error leyendo tarjeta PinPad, error en el código de respuesta del mensaje, código de error: "+rl.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error leyendo tarjeta PinPad, error en el código de respuesta del mensaje, código de error: "+rl.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
            }
        } catch (Throwable e){
            log.error("lecturaTarjeta() - Error al leer la tarjeta: "+e.getMessage(),e);
            throw new PinPadException(e);
        }            
        return rl;
    }
    
    @Override
    public RespuestaProcesoControl procesoControl() throws PinPadException {
        RespuestaProcesoControl rp = null;
        try{
            String tramaControl = PROCESO_CONTROL;
            log.debug("procesoControl() - Proceso Control con timeout: "+timeout+"sgs y trama: "+tramaControl);
            String respuesta = pinpad.SendPinpad(tramaControl, COM, timeout, grabaLog, grabaMsg);
            log.debug("procesoControl() - Respuesta del PinPad: "+respuesta);
            rp = new RespuestaProcesoControl();
            rp.setRespuesta(respuesta);
            rp.setTipoMensaje(respuesta.substring(0,2));
            rp.setCodigoRespuestaMensaje(respuesta.substring(2,4));
            if(!rp.getCodigoRespuestaMensaje().equals("00")){
                log.error("procesoControl() - Error en el proceso de control del PinPad, error en el código de respuesta del mensaje, código de error: "+rp.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error en el proceso de control del PinPad, error en el código de respuesta del mensaje, código de error: "+rp.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
            }            
            rp.setCodigoIdentificacionRed(respuesta.substring(4,6));
            rp.setMensajeRespuesta(respuesta.substring(6));
        } catch (Exception e){
            log.error("procesoControl() - Error al realizar el proceso de control: "+e.getMessage(),e);
            throw new PinPadException(e);
        }            
        return rp;        
    }
    
    
    @Override
    public RespuestaProcesoActualizacion procesoActualizacion() throws PinPadException {
        RespuestaProcesoActualizacion rp = null;
        try{
            String tramaActualizacion = PROCESO_ACTUALIAZCION;
            log.debug("procesoActualizacion() - Proceso Actualización con timeout "+timeout+" sgs y trama: "+tramaActualizacion);
            String respuesta = pinpad.SendPinpad(tramaActualizacion, COM, timeout, grabaLog, grabaMsg);    
            log.debug("procesoActualizacion() - Respuesta del PinPad: "+respuesta);
            rp = new RespuestaProcesoActualizacion();
            rp.setRespuesta(respuesta);
            rp.setTipoMensaje(respuesta.substring(0,2));
            rp.setCodigoRespuestaMensaje(respuesta.substring(2,4));
            if(!rp.getCodigoRespuestaMensaje().equals("00")){
                log.error("procesoActualizacion() - Error actualizando PinPad, error en el código de respuesta del mensaje, código de error: "+rp.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error actualizando PinPad, error en el código de respuesta del mensaje, código de error: "+rp.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
            }
            rp.setCodigoIdentificacionRed(respuesta.substring(4,6));
            rp.setMensajeRespuesta(respuesta.substring(6));
        } catch (Exception e){
            log.error("procesoActualizacion() - Error al realizar el proceso de actualización: "+e.getMessage(),e);
            throw new PinPadException(e);
        }            
        return rp;        
    }
    
    
    @Override
    public RespuestaProcesoPagos procesoPagos(PeticionProcesoPagos peticion) throws PinPadException {
       RespuestaProcesoPagos rp = null;
       String operacion = peticion.isAnulacion()?" Anulación ":" Pago ";
        try{
            String tramaPagos = peticion.dameTramaPagos();
            log.debug("procesoPagos() - Esperando tiempo antes de nueva petición: " + Sesion.getDatosConfiguracion().getTimePinPad()+" msgs");
            Thread.sleep(Sesion.getDatosConfiguracion().getTimePinPad());
            log.debug("procesoPagos() - Proceso Pagos, Operacion: "+operacion+", timeout: "+timeout+"sgs, con trama: "+tramaPagos);
            String respuesta = pinpad.SendPinpad(tramaPagos, COM, timeout, grabaLog, grabaMsg);
            log.debug("procesoPagos() - Respuesta del PinPad: "+respuesta);
            pinpadRespuestaPagos = new RespuestaProcesoPagos(respuesta);
            pago.setPinpadRespuesta(pinpadRespuestaPagos);
            if(pinpadRespuestaPagos!=null){
                pago.setMensajePromocional(pinpadRespuestaPagos.getCodigoRespuestaTransaccion()+". Trama de respuesta: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje());
            }
            
            //Guardamos la transaccion
            try {
                FacturacionTarjeta ft = new FacturacionTarjeta(pago,ticket, peticion.isAnulacion()?"R":"O");
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                pago.setUidFacturacion(ft.getId());
            } catch(FacturacionTarjetaException ex){
                log.error("Ha ocurrido un error insertando la trama de facturacion: "+ex.getMessage());
            }
            if(!pinpadRespuestaPagos.getCodigoRespuestaMensaje().equals("00")){
                if(pinpadRespuestaPagos.getCodigoRespuestaMensaje().equals("TO")){
                    //Es un timeout, tenemos que enviar un reverso automático de la transacción (Código 04)
                    log.debug("prcesoPagos() - Se ha producido un Timeout al realizar el proceso de pagos, enviando trama de reverso...");
                    peticion.setTipoTransaccion(PeticionProcesoPagos.REVERSO_PAGO);
                    String tramaReverso = peticion.dameTramaPagos();
                    log.debug("procesoPagos() - Proceso Pagos, reverso con trama: "+tramaReverso);
                    String respuestaReverso = pinpad.SendPinpad(tramaReverso, COM, timeout, grabaLog, grabaMsg);
                    pinpadRespuestaPagos = new RespuestaProcesoPagos(respuestaReverso);                   
                    log.debug("procesoPagos() - Respuesta del PinPad de la trama de reverso: "+respuestaReverso);
                    pago.setPinpadRespuesta(pinpadRespuestaPagos);
                    //Guardamos el reverso
                    try {
                        FacturacionTarjeta ft = new FacturacionTarjeta(pago,ticket, "R");
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    } catch(FacturacionTarjetaException ex){
                        log.error("Ha ocurrido un error insertando la trama de reverso: "+ex.getMessage());
                    }                    
                    throw new PinPadException("Se ha producido un timeout al realizar el proceso de pagos y se ha enviado una trama de reverso de la operación");
                } else {
                    log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta del mensaje, código de error: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                    throw new PinPadException("Error en el código de respuesta del mensaje. Código de error: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje());                    
                }

            }
            //cambio para la autorizacion automatica que esta vacia
            if(!pinpadRespuestaPagos.getCodigoRespuestaTransaccion().equals("00") || pinpadRespuestaPagos.getNumeroAutorizacion().trim().isEmpty()){
                log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta de la transacción, código de error: "+pinpadRespuestaPagos.getCodigoRespuestaTransaccion()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error en el código de respuesta de la transacción. Código de error: "+pinpadRespuestaPagos.getCodigoRespuestaTransaccion());
            }
            lote = pinpadRespuestaPagos.getNumeroLote();
        } 
        catch (PinPadException e){
            throw e;
        }
        catch (Exception e){
            log.error("procesoPagos() - Error al realizar el proceso de pagos: "+e.getMessage(),e);
            throw new PinPadException("Error inesperado: " + e.getMessage(), e);
        }             
       return pinpadRespuestaPagos;
    }

    @Override
    public String getLote() {
        if(lote == null){
            //Si el lote es null, es que aunque fuera automatica hemos terminado validando manualmente
            return PinPadManual.getLoteAuto();
        }        
        return lote;
    }

    public RespuestaProcesoPagos getPinpadRespuestaPagos() {
        return pinpadRespuestaPagos;
    }

    public void setPinpadRespuestaPagos(RespuestaProcesoPagos pinpadRespuestaPagos) {
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
    public RespuestaProcesoPagos procesoAnulacion(PeticionProcesoPagos peticion, String idReferencia, String numeroAutorizacion) throws PinPadException {
       RespuestaProcesoPagos rp = null;
       String operacion = peticion.isAnulacion()?" Anulación ":" Pago ";
        try{
            String tramaPagos = peticion.dameTramaPagos();
            log.debug("procesoPagos() - Esperando tiempo antes de nueva petición: " + Sesion.getDatosConfiguracion().getTimePinPad()+" msgs");
            Thread.sleep(Sesion.getDatosConfiguracion().getTimePinPad());
            log.debug("procesoPagos() - Proceso Pagos, Operacion: "+operacion+", timeout: "+timeout+"sgs, con trama: "+tramaPagos);
            String respuesta = pinpad.SendPinpad(tramaPagos, COM, timeout, grabaLog, grabaMsg);
            //String respuesta = "PP000100APROBADA  TRANS.    00147700120512520920190408      402285311000009681                                                                                                 002DINERS CLUB                   DISCOVER                 03RIVERA/SHEYLA                                       DinersClub          AID:   A000000152301TC:    4F4ED1FABD2E391               ARQC:  B21EA27F3";
            log.debug("procesoPagos() - Respuesta del PinPad: "+respuesta);
            pinpadRespuestaPagos = new RespuestaProcesoPagos(respuesta);
            pago.setPinpadRespuesta(pinpadRespuestaPagos);
            //Guardamos la transaccion
            try {
                FacturacionTarjeta ft = new FacturacionTarjeta(pago, peticion.isAnulacion()?"R":"O", idReferencia, numeroAutorizacion);
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                pago.setUidFacturacion(ft.getId());
            } catch(FacturacionTarjetaException ex){
                log.error("Ha ocurrido un error insertando la trama de facturacion: "+ex.getMessage());
            }
            if(!pinpadRespuestaPagos.getCodigoRespuestaMensaje().equals("00")){
                if(pinpadRespuestaPagos.getCodigoRespuestaMensaje().equals("TO")){
                    //Es un timeout, tenemos que enviar un reverso automático de la transacción (Código 04)
                    log.debug("prcesoPagos() - Se ha producido un Timeout al realizar el proceso de pagos, enviando trama de reverso...");
                    peticion.setTipoTransaccion(PeticionProcesoPagos.REVERSO_PAGO);
                    String tramaReverso = peticion.dameTramaPagos();
                    log.debug("procesoPagos() - Proceso Pagos, reverso con trama: "+tramaReverso);
                    String respuestaReverso = pinpad.SendPinpad(tramaReverso, COM, timeout, grabaLog, grabaMsg);
                    pinpadRespuestaPagos = new RespuestaProcesoPagos(respuestaReverso);                   
                    log.debug("procesoPagos() - Respuesta del PinPad de la trama de reverso: "+respuestaReverso);
                    pago.setPinpadRespuesta(pinpadRespuestaPagos);
                    if(pinpadRespuestaPagos!=null){
                        pago.setMensajePromocional(pinpadRespuestaPagos.getCodigoRespuestaTransaccion()+". Trama de respuesta: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje());
                    }
                    //Guardamos el reverso
                    try {
                        FacturacionTarjeta ft = new FacturacionTarjeta(pago,ticket, "R");
                        ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft);
                    } catch(FacturacionTarjetaException ex){
                        log.error("Ha ocurrido un error insertando la trama de reverso: "+ex.getMessage());
                    }                    
                    throw new PinPadException("Se ha producido un timeout al realizar el proceso de pagos y se ha enviado una trama de reverso de la operación");
                } else {
                    log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta del mensaje, código de error: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje()+". Trama de respuesta: "+respuesta);
                    throw new PinPadException("Error en el código de respuesta del mensaje. Código de error: "+pinpadRespuestaPagos.getCodigoRespuestaMensaje());                    
                }

            }
            if(!pinpadRespuestaPagos.getCodigoRespuestaTransaccion().equals("00")){
                log.error("procesoPagos() - Error al realizar el proceso de pagos, error en el código de respuesta de la transacción, código de error: "+pinpadRespuestaPagos.getCodigoRespuestaTransaccion()+". Trama de respuesta: "+respuesta);
                throw new PinPadException("Error en el código de respuesta de la transacción. Código de error: "+pinpadRespuestaPagos.getCodigoRespuestaTransaccion());
            }
            lote = pinpadRespuestaPagos.getNumeroLote();
        } 
        catch (PinPadException e){
            throw e;
        }
        catch (Exception e){
            log.error("procesoPagos() - Error al realizar el proceso de pagos: "+e.getMessage(),e);
            throw new PinPadException("Error inesperado: " + e.getMessage(), e);
        }             
       return pinpadRespuestaPagos;
    }
    
}
