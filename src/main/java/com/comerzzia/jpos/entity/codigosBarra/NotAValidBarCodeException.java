/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.codigosBarra;

/**
 *
 * @author Admin
 */
public class NotAValidBarCodeException extends Exception {

    public NotAValidBarCodeException() {
        super();
    }
    public NotAValidBarCodeException(String string){
        super(string);
    }
    public NotAValidBarCodeException(String string, Throwable thrwbl){
        super(string, thrwbl);
    }
    public NotAValidBarCodeException(Throwable thrwbl){
        super(thrwbl);
    }
    
}
