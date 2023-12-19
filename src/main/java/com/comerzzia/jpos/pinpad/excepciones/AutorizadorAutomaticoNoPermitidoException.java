/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.excepciones;

import com.comerzzia.jpos.servicios.pagos.Pago;

/**
 *
 * @author amos
 */
public class AutorizadorAutomaticoNoPermitidoException extends AutorizadorException{
    private static final long serialVersionUID = 1L;

    public AutorizadorAutomaticoNoPermitidoException(String msg, Pago pag) {
        super(msg, pag);
    }

    public AutorizadorAutomaticoNoPermitidoException(String msg, Throwable e, Pago pag) {
        super(msg, e, pag);
    }

    public AutorizadorAutomaticoNoPermitidoException(String string) {
        super(string);
    }

    public AutorizadorAutomaticoNoPermitidoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
