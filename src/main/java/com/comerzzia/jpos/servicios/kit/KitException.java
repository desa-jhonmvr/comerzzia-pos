/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.kit;

/**
 *
 * @author MGRI
 */
public class KitException extends Exception {

	

	public KitException() {
		super();
	}
	
	public KitException(String msg) {
		super(msg);
	}
	
	public KitException(String msg, Throwable e) {
        super(msg, e);
    }

	
}
