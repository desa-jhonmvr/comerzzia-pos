/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos;

/**
 *
 * @author Mónica Enríquez
 */
public class PagoInvalidSupermaxiException extends com.comerzzia.util.base.Exception {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3538491015101477061L;

    public PagoInvalidSupermaxiException() {
        super();
    }

    public PagoInvalidSupermaxiException(String msg) {
        super(msg);
    }

    public PagoInvalidSupermaxiException(String msg, Throwable e) {
        super(msg, e);
    }

    public PagoInvalidSupermaxiException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public PagoInvalidSupermaxiException(String msg, String msgKey) {
        super(msg, msgKey);
    }
}
