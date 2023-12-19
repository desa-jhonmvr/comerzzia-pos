/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.excepciones;

/**
 *
 * @author MGRI
 */
public class ArticuloEnReservaNotFoundException extends Exception{

    public ArticuloEnReservaNotFoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ArticuloEnReservaNotFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ArticuloEnReservaNotFoundException(String string) {
        super(string);
    }

    public ArticuloEnReservaNotFoundException() {
    }
    
    
}
