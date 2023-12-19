/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.codigosBarra;

/**
 *
 * @author Admin
 */
public class NotAReservationOfThisShopException extends Exception {

    public NotAReservationOfThisShopException() {
        super();
    }
    
     public NotAReservationOfThisShopException(String string) {
         super(string);
    }
     
     public NotAReservationOfThisShopException(String string, Throwable thrwbl) {
         super(string, thrwbl);
    }
      public NotAReservationOfThisShopException(Throwable thrwbl) {
          super(thrwbl);
    }
}
