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
public class AutorizadorException extends Exception{
    private static final long serialVersionUID = 1L;

    Pago pago;
    
    public AutorizadorException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public AutorizadorException(String string) {
        super(string);
    }

    public AutorizadorException(String msg, Throwable e, Pago pag) {
        super(msg, e);
        this.pago = pag;
    }
      
    public AutorizadorException(String msg, Pago pag) {
        super(msg);
        this.pago = pag;
    }
    
    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
    
    
}
