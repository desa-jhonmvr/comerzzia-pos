/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito;

/**
 *
 * @author MGRI
 */
public class CreditoException  extends Exception {

    public CreditoException() {
        super();
    }

    public CreditoException(String msg) {
        super(msg);
    }

    public CreditoException(String msg, Throwable e) {
        super(msg, e);
    }
}
