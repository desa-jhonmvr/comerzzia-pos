
package com.comerzzia.jpos.servicios.articulos.tarifas;

public class TarifaArticuloNotFoundException extends TarifaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6773085643659183673L;

	public TarifaArticuloNotFoundException() {
	}

	public TarifaArticuloNotFoundException(String msg) {
		super(msg);
	}

	public TarifaArticuloNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	public TarifaArticuloNotFoundException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public TarifaArticuloNotFoundException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

}
