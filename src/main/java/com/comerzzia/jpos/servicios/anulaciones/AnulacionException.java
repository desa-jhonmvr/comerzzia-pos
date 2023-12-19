/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.anulaciones;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author MGRI
 */
public class AnulacionException extends Exception { 
    
    	public AnulacionException() {
		super();
	}

	public AnulacionException(String msg) {
		super(msg);
	}

	public AnulacionException(String msg, Throwable e) {
		super(msg, e);
	}

	public AnulacionException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public AnulacionException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
    
}

