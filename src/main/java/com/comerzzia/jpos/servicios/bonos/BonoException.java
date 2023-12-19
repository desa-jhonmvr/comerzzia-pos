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
public class BonoException  extends Exception {
    
    private static final long serialVersionUID=1L;
    
    	public BonoException() {
		super();
	}
	public BonoException(String msg) {
		super(msg);
	}
	
	public BonoException(String msg, Throwable e) {
            super(msg, e);
    }

	public BonoException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public BonoException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}