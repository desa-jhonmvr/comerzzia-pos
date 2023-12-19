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
public class MovimientoCajaException extends Exception { 
    
    	public MovimientoCajaException() {
		super();
	}

	public MovimientoCajaException(String msg) {
		super(msg);
	}

	public MovimientoCajaException(String msg, Throwable e) {
		super(msg, e);
	}

	public MovimientoCajaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public MovimientoCajaException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
    
}

