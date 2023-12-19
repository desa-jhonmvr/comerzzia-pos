/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.stock;

import com.comerzzia.jpos.servicios.articulos.ArticuloException;


/**
 *
 * @author MGRI
 */
public class StockException extends ArticuloException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3250600293953028439L;

	
	public StockException () {
		super();
	}
	public StockException (String msg) {
		super(msg);
	}
	
	public StockException (String msg, Throwable e) {
        super(msg, e);
    }

	public StockException (String msg, String msgKey, Throwable e) {
		//super(msg, msgKey, e);
	}

	public StockException (String msg, String msgKey) {
		//super(msg, msgKey);
	}
}
