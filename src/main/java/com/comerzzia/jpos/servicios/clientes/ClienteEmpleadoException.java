/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes;

/**
 *
 * @author MGRI
 */
public class ClienteEmpleadoException extends Exception {

    public ClienteEmpleadoException() {
        super();
    }

    public ClienteEmpleadoException(String msg) {
        super(msg);
    }

    public ClienteEmpleadoException(String msg, Throwable e) {
        super(msg, e);
    }
}
