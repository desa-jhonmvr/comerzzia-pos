/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.empresa;
import com.comerzzia.util.base.Exception;
/**
 *
 * @author MGRI
 */
public class EmpresaException extends Exception {

	public EmpresaException() {
		super();
	}
	public EmpresaException(String msg) {
		super(msg);
	}
	
	public EmpresaException(String msg, Throwable e) {
        super(msg, e);
    }

	public EmpresaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public EmpresaException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}
