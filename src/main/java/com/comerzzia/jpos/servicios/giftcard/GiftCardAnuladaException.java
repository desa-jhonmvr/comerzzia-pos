/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.giftcard;

/**
 *
 * @author SMLM
 */
public class GiftCardAnuladaException extends com.comerzzia.util.base.Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5527191541825090100L;

	public GiftCardAnuladaException() {
	}

	public GiftCardAnuladaException(String msg) {
		super(msg);
	}

	public GiftCardAnuladaException(String msg, Throwable e) {
		super(msg, e);
	}

	public GiftCardAnuladaException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public GiftCardAnuladaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}
}
