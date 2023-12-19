/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad.excepciones;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author MGRI
 */
public class AutorizadorRespuestaException extends Exception {



	public AutorizadorRespuestaException() {
	}

	public AutorizadorRespuestaException(String msg) {
		super(msg);
	}

	public AutorizadorRespuestaException(String msg, Throwable e) {
		super(msg, e);
	}

	public AutorizadorRespuestaException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public AutorizadorRespuestaException(String msg, String msgKey,
			Throwable e) {
		super(msg, msgKey, e);
	}
}
