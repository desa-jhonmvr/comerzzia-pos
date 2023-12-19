/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes;

/**
 *
 * @author MGRI
 */
public class ClienteInactiveException extends Exception {

    public ClienteInactiveException() {
        super();
    }

    public ClienteInactiveException(String msg) {
        super(msg);
    }

    public ClienteInactiveException(String msg, Throwable e) {
        super(msg, e);
    }
}
