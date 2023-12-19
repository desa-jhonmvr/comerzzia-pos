/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.cajas;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author MGRI
 */
public class CajaException extends Exception { 
    
    	public CajaException() {
		super();
	}

	public CajaException(String msg) {
		super(msg);
	}

	public CajaException(String msg, Throwable e) {
		super(msg, e);
	}

	public CajaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public CajaException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
    
}

