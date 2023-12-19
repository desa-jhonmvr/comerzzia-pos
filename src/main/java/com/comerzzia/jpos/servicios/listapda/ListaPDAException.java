/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.listapda;

/**
 *
 * @author amos
 */
public class ListaPDAException extends Exception{
    private static final long serialVersionUID = 1L;

    public ListaPDAException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ListaPDAException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ListaPDAException(String string) {
        super(string);
    }

    public ListaPDAException() {
    }
    
}
