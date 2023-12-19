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
public class AnulacionNoPosibleException extends Exception { 
    
    	public AnulacionNoPosibleException() {
		super();
	}

	public AnulacionNoPosibleException(String msg) {
		super(msg);
	}

	public AnulacionNoPosibleException(String msg, Throwable e) {
		super(msg, e);
	}

	public AnulacionNoPosibleException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public AnulacionNoPosibleException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
    
}

