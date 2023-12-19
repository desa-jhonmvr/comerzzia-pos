/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.exception;

/**
 * Clase para la validacion de los mensajes OTP
 *
 * @author Gabriel Simbania
 */
public class ValidacionOTPException extends com.comerzzia.util.base.Exception {

    public ValidacionOTPException() {
    }

    public ValidacionOTPException(String msg) {
        super(msg);
    }

    public ValidacionOTPException(String msg, Throwable e) {
        super(msg, e);
    }

    public ValidacionOTPException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

}
