/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito;

/**
 *
 * @author MGRI
 */
public class CreditoNotFoundException  extends Exception {
    private static final long serialVersionUID = 1L;

    public CreditoNotFoundException() {
        super();
    }

    public CreditoNotFoundException(String msg) {
        super(msg);
    }

    public CreditoNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }
}
