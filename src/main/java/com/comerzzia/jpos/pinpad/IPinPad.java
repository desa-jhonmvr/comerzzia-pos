/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad;

import com.comerzzia.jpos.pinpad.peticion.PeticionProcesoPagos;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaConfiguracion;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaConsultaTarjeta;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaLecturaTarjeta;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoActualizacion;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoControl;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoPagos;

/**
 *
 * @author SMLM
 */
public interface IPinPad {
    
    public RespuestaConfiguracion configuracionPinPad(String ip, String mascara, String gateway) throws PinPadException;
    
    public RespuestaConsultaTarjeta consultaTarjeta() throws PinPadException;
    
    public RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadException;
    
    public RespuestaProcesoControl procesoControl() throws PinPadException;
    
    public RespuestaProcesoActualizacion procesoActualizacion() throws PinPadException;
    
    public RespuestaProcesoPagos procesoPagos(PeticionProcesoPagos peticion) throws PinPadException;
    
    public RespuestaProcesoPagos procesoAnulacion(PeticionProcesoPagos peticion, String idReferencia, String numeroAutorizacion) throws PinPadException;
    
    public String getLote();
}
