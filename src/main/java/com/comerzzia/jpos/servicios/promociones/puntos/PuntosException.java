/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.puntos;

/**
 *
 * @author amos
 */
public class PuntosException extends Exception {

    public PuntosException(Throwable thrwbl) {
        super(thrwbl);
    }

    public PuntosException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public PuntosException(String string) {
        super(string);
    }

    public PuntosException() {
    }


}
