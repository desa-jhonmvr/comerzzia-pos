/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.guiasremision;

/**
 *
 * @author MGRI
 */
public class GuiaRemisionException  extends Exception {

	public GuiaRemisionException () {
		super();
	}
	
	public GuiaRemisionException (String msg) {
		super(msg);
	}
	
	public GuiaRemisionException (String msg, Throwable e) {
        super(msg, e);
    }
	
}
