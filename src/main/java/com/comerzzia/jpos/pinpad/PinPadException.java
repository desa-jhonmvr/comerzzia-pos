/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad;

/**
 *
 * @author SMLM
 */
public class PinPadException extends Exception {

	public PinPadException () {
		super();
	}
	
	public PinPadException (String msg) {
		super(msg);
	}
	
	public PinPadException (String msg, Throwable e) {
            super(msg, e);
        }
        
        public PinPadException(Throwable e) {
            super(e);
        }
	
}
