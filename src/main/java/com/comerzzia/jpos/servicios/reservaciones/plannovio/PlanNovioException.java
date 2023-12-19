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
public class PlanNovioException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1246262634569808488L;

	public PlanNovioException() {
	}

	public PlanNovioException(String msg) {
		super(msg);
	}

	public PlanNovioException(String msg, Throwable e) {
		super(msg, e);
	}

	public PlanNovioException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public PlanNovioException(String msg, String msgKey,
			Throwable e) {
		super(msg, msgKey, e);
	}
}
