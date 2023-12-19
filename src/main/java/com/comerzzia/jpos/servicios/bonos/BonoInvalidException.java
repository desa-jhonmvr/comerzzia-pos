/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bonos;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author MGRI
 */
public class BonoInvalidException  extends Exception {
    
    private static final long serialVersionUID=1L;
    
    	public BonoInvalidException() {
		super();
	}
	public BonoInvalidException(String msg) {
		super(msg);
	}
	
	public BonoInvalidException(String msg, Throwable e) {
            super(msg, e);
    }

	public BonoInvalidException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public BonoInvalidException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}