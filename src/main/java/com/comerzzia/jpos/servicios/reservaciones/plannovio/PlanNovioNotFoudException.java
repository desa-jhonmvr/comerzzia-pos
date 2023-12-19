/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.reservaciones.plannovio;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class PlanNovioNotFoudException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1246262634569808488L;

	public PlanNovioNotFoudException() {
	}

	public PlanNovioNotFoudException(String msg) {
		super(msg);
	}

	public PlanNovioNotFoudException(String msg, Throwable e) {
		super(msg, e);
	}

	public PlanNovioNotFoudException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public PlanNovioNotFoudException(String msg, String msgKey,
			Throwable e) {
		super(msg, msgKey, e);
	}
}
