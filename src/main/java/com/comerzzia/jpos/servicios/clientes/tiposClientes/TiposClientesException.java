/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes.tiposClientes;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author jmc
 */
public class TiposClientesException extends Exception {

    public TiposClientesException() {
        super();
    }

    public TiposClientesException(String msg) {
        super(msg);
    }

    public TiposClientesException(String msg, Throwable e) {
        super(msg, e);
    }

    public TiposClientesException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public TiposClientesException(String msg, String msgKey) {
        super(msg, msgKey);
    }
}
