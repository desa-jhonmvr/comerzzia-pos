/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago.tarjetas;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class TarjetaInvalidaException extends Exception {

    public boolean permiteAutorizacion;

    public TarjetaInvalidaException() {
    }

    public TarjetaInvalidaException(String msg) {
        super(msg);
    }

    public TarjetaInvalidaException(String msg, boolean permiteAutorizacion) {
        super(msg);
        this.permiteAutorizacion = permiteAutorizacion;
    }

    public TarjetaInvalidaException(String msg, Throwable e, boolean permiteAutorizacion) {
        super(msg, e);
        this.permiteAutorizacion = permiteAutorizacion;
    }

    public boolean isPermiteAutorizacion() {
        return permiteAutorizacion;
    }

    public TarjetaInvalidaException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public TarjetaInvalidaException(String msg, Throwable e) {
        super(msg, e);
    }

    
    
    public void setPermiteAutorizacion(boolean permiteAutorizacion) {
        this.permiteAutorizacion = permiteAutorizacion;
    }
}
