/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones;

/**
 *
 * @author Mónica Enríquez
 */
public class PromocionFormaPagoException extends com.comerzzia.util.base.Exception {

    private static final long serialVersionUID = -1055368944126016144L;

    public PromocionFormaPagoException() {
        super();
    }

    public PromocionFormaPagoException(String msg) {
        super(msg);
    }

    public PromocionFormaPagoException(String msg, Throwable e) {
        super(msg, e);
    }

    public PromocionFormaPagoException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public PromocionFormaPagoException(String msg, String msgKey) {
        super(msg, msgKey);
    }

}
