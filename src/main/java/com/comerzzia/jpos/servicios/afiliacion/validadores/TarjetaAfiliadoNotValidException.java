/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

/**
 *
 * @author amos
 */
public class TarjetaAfiliadoNotValidException extends Exception{
   
    private static final long serialVersionUID = 1L;

    public TarjetaAfiliadoNotValidException(Throwable thrwbl) {
        super(thrwbl);
    }

    public TarjetaAfiliadoNotValidException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public TarjetaAfiliadoNotValidException(String string) {
        super(string);
    }

    public TarjetaAfiliadoNotValidException() {
    }
    
    
    
}
