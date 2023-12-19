/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.garantia;


/**
 *
 * @author MGRI
 */
public class GarantiaException  extends Exception {
    private static final long serialVersionUID = 1L;

    public GarantiaException() {
        super();
    }

    public GarantiaException(String msg) {
        super(msg);
    }

    public GarantiaException(String msg, Throwable e) {
        super(msg, e);
    }
}
