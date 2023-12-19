/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.fasttrack;

import DF.RespuestaConsultaTarjeta;
import DF.RespuestaLecturaTarjeta;
import DF.RespuestaProcesoConfigPinPad;
import DF.RespuestaProcesoControl;
import DF.RespuestaProcesoPago;

/**
 *
 * @author SMLM
 */
public interface IPinPadFasttrack {

    public RespuestaProcesoConfigPinPad configuracionPinPad() throws PinPadFasttrackException;

    public RespuestaConsultaTarjeta consultaTarjeta() throws PinPadFasttrackException;

    public RespuestaLecturaTarjeta lecturaTarjeta() throws PinPadFasttrackException;

    public RespuestaProcesoControl procesoControl() throws PinPadFasttrackException;

    public RespuestaProcesoPago procesoPagos(int tipoTransaccion, String codigoDiferido, String base0, String baseImponible, String iva, String montoTotal, String plazoDiferido, String redAdquiriente) throws PinPadFasttrackException;

    public RespuestaProcesoPago procesoAnulacion(int tipoTransaccion, String idReferencia, String numeroAutorizacion, String secuencial, String redAdquiriente) throws PinPadFasttrackException;

    public String getLote();
}
