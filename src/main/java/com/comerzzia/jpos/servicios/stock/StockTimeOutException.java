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
public class StockTimeOutException extends ArticuloException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3250608293953028439L;

	
	public StockTimeOutException () {
		super();
	}
        
	public StockTimeOutException (String msg) {
		super(msg);
	}
	
	public StockTimeOutException (String msg, Throwable e) {
            super(msg, e);
        }

	public StockTimeOutException (String msg, String msgKey, Throwable e) {
		//super(msg, msgKey, e);
	}

	public StockTimeOutException (String msg, String msgKey) {
		//super(msg, msgKey);
	}
}
