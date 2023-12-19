/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.excepciones;

import com.comerzzia.jpos.servicios.pagos.Pago;

/**
 *
 * @author Gabriel Simbania
 */
public class AutorizadorExcedeCupoException extends AutorizadorException {

    public AutorizadorExcedeCupoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public AutorizadorExcedeCupoException(String string) {
        super(string);
    }

    public AutorizadorExcedeCupoException(String msg, Throwable e, Pago pag) {
        super(msg, e, pag);
    }

    public AutorizadorExcedeCupoException(String msg, Pago pag) {
        super(msg, pag);
    }

}
