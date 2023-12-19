/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

/**
 *
 * @author MGRI
 */
public class ValidationException extends Exception {

    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable e) {
        super(msg, e);
    }
}
