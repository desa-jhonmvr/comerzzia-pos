package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.util.base.Exception;

public class CuponException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055368944126016144L;

	public CuponException() {
		super();
	}
	public CuponException(String msg) {
		super(msg);
	}
	
	public CuponException(String msg, Throwable e) {
        super(msg, e);
    }

	public CuponException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public CuponException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}
