/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class ClienteException extends Exception { 
    
    	public ClienteException() {
		super();
	}

	public ClienteException(String msg) {
		super(msg);
	}

	public ClienteException(String msg, Throwable e) {
		super(msg, e);
	}

	public ClienteException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public ClienteException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
}
