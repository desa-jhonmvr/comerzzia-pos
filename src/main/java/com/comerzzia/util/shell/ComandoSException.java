/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.shell;

/**
 *
 * @author MGRI
 */
public class ComandoSException extends Exception{
    
    ComandoSException(){
        super();
    }
    ComandoSException(String string){
        super(string);
    }
    ComandoSException(String string, Throwable thrwbl){
        super(string,thrwbl);        
    }
    ComandoSException(Throwable thrwbl){
        super(thrwbl);        
    }
}
