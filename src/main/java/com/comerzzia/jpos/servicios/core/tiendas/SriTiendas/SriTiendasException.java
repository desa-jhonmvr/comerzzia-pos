/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.core.tiendas.SriTiendas;

import com.comerzzia.util.base.Exception;
/**
 *
 * @author SMLM
 */
public class SriTiendasException extends Exception {

	public SriTiendasException() {
	}

	public SriTiendasException(String msg) {
		super(msg);
	}

	public SriTiendasException(String msg, Throwable e) {
		super(msg, e);
	}

	public SriTiendasException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public SriTiendasException(String msg, String msgKey,
			Throwable e) {
		super(msg, msgKey, e);
	}   
}
