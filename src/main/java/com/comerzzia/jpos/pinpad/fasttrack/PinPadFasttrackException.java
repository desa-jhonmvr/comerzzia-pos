/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.fasttrack;


/**
 *
 * @author SMLM
 */
public class PinPadFasttrackException extends Exception {

	public PinPadFasttrackException () {
		super();
	}
	
	public PinPadFasttrackException (String msg) {
		super(msg);
	}
	
	public PinPadFasttrackException (String msg, Throwable e) {
            super(msg, e);
        }
        
        public PinPadFasttrackException(Throwable e) {
            super(e);
        }
	
}
