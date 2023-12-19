/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos;

/**
 *
 * @author Gabriel Simbania
 */
public class PagoPrefacturaException extends com.comerzzia.util.base.Exception {
    
    private static final long serialVersionUID = -7853247286377315006L;
    
    public PagoPrefacturaException() {
        super();
    }

    public PagoPrefacturaException(String msg) {
        super(msg);
    }

    public PagoPrefacturaException(String msg, Throwable e) {
        super(msg, e);
    }

    public PagoPrefacturaException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public PagoPrefacturaException(String msg, String msgKey) {
        super(msg, msgKey);
    }
    
}
