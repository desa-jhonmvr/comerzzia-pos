/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago.tarjetas;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;

/**
 *
 * @author amos
 */
public class TarjetaCreditoBuilder {
    
    public static TarjetaCredito create(MedioPagoBean medioPago, String banda){
        TarjetaCredito tarjeta = null;
        if (medioPago.isTarjetaSukasa()){
            tarjeta = new TarjetaCreditoSK(banda);
        }
        else if (medioPago.isTarjetaCredito() || medioPago.isOtros()){
            tarjeta = new TarjetaCredito(banda);
        }
        else{
            throw new IllegalArgumentException("ERROR: Intentando instanciar una tarjeta de crédito con un medio de pago no permitido.");
        }
        tarjeta.setMedioPago(medioPago);
        return tarjeta;
    }

    public static TarjetaCredito create(MedioPagoBean medioPago, String numero, String caducidad, String cvv, boolean esLecturaDesdeCedula){
        TarjetaCredito tarjeta = null;
        if (medioPago.isTarjetaSukasa()){
            tarjeta = new TarjetaCreditoSK(numero, caducidad, cvv, esLecturaDesdeCedula);
        }
        else if (medioPago.isTarjetaCredito() || medioPago.isBonoSuperMaxiNavidad() || medioPago.isCreditoFilial()){
            tarjeta = new TarjetaCredito(numero, caducidad, cvv, false);
        }
        else{
            throw new IllegalArgumentException("ERROR: Intentando instanciar una tarjeta de crédito con un medio de pago no permitido.");
        }
        tarjeta.setMedioPago(medioPago);
        return tarjeta;
    }

    public static TarjetaCredito create(MedioPagoBean medioPago, String numero, boolean esLecturaDesdeCedula) {
        TarjetaCredito tarjeta = null;
        if (medioPago.isTarjetaSukasa()){
            tarjeta = new TarjetaCreditoSK(numero, esLecturaDesdeCedula);
        }
        else if (medioPago.isTarjetaCredito()){
            tarjeta = new TarjetaCredito(numero,  false);
        }
        else{
            throw new IllegalArgumentException("ERROR: Intentando instanciar una tarjeta de crédito con un medio de pago no permitido.");
        }
        tarjeta.setMedioPago(medioPago);
        return tarjeta;        
    }

}
