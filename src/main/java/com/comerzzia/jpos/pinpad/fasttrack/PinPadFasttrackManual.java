/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.fasttrack;

import DF.RespuestaProcesoConfigPinPad;
import DF.RespuestaProcesoPago;
import com.comerzzia.util.Constantes;

/**
 *
 * @author SMLM
 */
public class PinPadFasttrackManual implements IPinPadFasttrack {

    @Override
    public String getLote() {
        return Constantes.VALOR_DEFECTO_LOTE_MANUAL;
    }

    public static String getLoteAuto() {
        return Constantes.VALOR_DEFECTO_LOTE_MANUAL;
    }

    @Override
    public RespuestaProcesoConfigPinPad configuracionPinPad() throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DF.RespuestaConsultaTarjeta consultaTarjeta() throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DF.RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DF.RespuestaProcesoControl procesoControl() throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RespuestaProcesoPago procesoPagos(int tipoTransaccion, String codigoDiferido, String base0, String baseImponible, String iva, String montoTotal, String plazoDiferido, String redAdquiriente) throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RespuestaProcesoPago procesoAnulacion(int tipoTransaccion, String idReferencia, String numeroAutorizacion, String secuencial, String redAdquiriente) throws PinPadFasttrackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
