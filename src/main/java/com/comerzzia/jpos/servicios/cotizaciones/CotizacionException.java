/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cotizaciones;

/**
 *
 * @author SMLM
 */
public class CotizacionException extends Exception {
 	public CotizacionException () {
		super();
	}
	
	public CotizacionException (String msg) {
		super(msg);
	}
	
	public CotizacionException (String msg, Throwable e) {
            super(msg, e);
        }     
}
