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
import com.comerzzia.util.Constantes;

/**
 *
 * @author SMLM
 */
public class PinPadManual implements IPinPad {

    @Override
    public RespuestaConfiguracion configuracionPinPad(String ip, String mascara, String gateway) throws PinPadException {
        return null;
    }

    @Override
    public RespuestaConsultaTarjeta consultaTarjeta() throws PinPadException {
        return null;
    }

    @Override
    public RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadException {
        return null;
    }

    @Override
    public RespuestaProcesoControl procesoControl() throws PinPadException {
        return null;
    }

    @Override
    public RespuestaProcesoActualizacion procesoActualizacion() throws PinPadException {
        return null;
    }

    @Override
    public RespuestaProcesoPagos procesoPagos(PeticionProcesoPagos peticion) throws PinPadException {
        return null;
    }

    @Override
    public String getLote() {
        return Constantes.VALOR_DEFECTO_LOTE_MANUAL;
    }
    
    public static String getLoteAuto(){
        return Constantes.VALOR_DEFECTO_LOTE_MANUAL;
    }

    @Override
    public RespuestaProcesoPagos procesoAnulacion(PeticionProcesoPagos peticion, String idReferencia, String numeroAutorizacion) throws PinPadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
