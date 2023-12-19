/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.articulos.bloqueos;
import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class BloqueoFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6773085643659183673L;

	public BloqueoFoundException() {
	}

	public BloqueoFoundException(String msg) {
		super(msg);
	}

	public BloqueoFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	public BloqueoFoundException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public BloqueoFoundException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

}
