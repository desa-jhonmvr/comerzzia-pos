/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.ticket;

/**
 *
 * @author MGRI
 */
public class IDTicketMalFormadoException extends Exception{
    
     public IDTicketMalFormadoException(Throwable thrwbl) {
        super(thrwbl);
    }

    public IDTicketMalFormadoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public IDTicketMalFormadoException(String string) {
        super(string);
    }

    public IDTicketMalFormadoException() {
    }
    
}
