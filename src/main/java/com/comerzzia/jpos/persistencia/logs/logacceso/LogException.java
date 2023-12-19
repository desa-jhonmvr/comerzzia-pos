/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.logs.logacceso;
import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class LogException extends Exception { 
    
    	public LogException() {
		super();
	}

	public LogException(String msg) {
		super(msg);
	}

	public LogException(String msg, Throwable e) {
		super(msg, e);
	}

	public LogException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public LogException(String msg, String msgKey) {
		super(msg, msgKey);
	}
    
    
}
