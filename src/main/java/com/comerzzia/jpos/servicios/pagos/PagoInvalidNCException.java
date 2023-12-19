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
public class PagoInvalidNCException extends com.comerzzia.util.base.Exception {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3538491015101477061L;

    public PagoInvalidNCException() {
        super();
    }

    public PagoInvalidNCException(String msg) {
        super(msg);
    }

    public PagoInvalidNCException(String msg, Throwable e) {
        super(msg, e);
    }

    public PagoInvalidNCException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public PagoInvalidNCException(String msg, String msgKey) {
        super(msg, msgKey);
    }
}
