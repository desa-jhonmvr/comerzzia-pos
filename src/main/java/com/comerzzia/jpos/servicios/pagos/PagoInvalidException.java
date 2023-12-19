/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author amos
 */
public class PagoInvalidException extends Exception {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3538491015101477061L;

    public PagoInvalidException() {
        super();
    }

    public PagoInvalidException(String msg) {
        super(msg);
    }

    public PagoInvalidException(String msg, Throwable e) {
        super(msg, e);
    }

    public PagoInvalidException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public PagoInvalidException(String msg, String msgKey) {
        super(msg, msgKey);
    }
}
