/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.letras;

import com.comerzzia.jpos.servicios.guiasremision.*;

/**
 *
 * @author MGRI
 */
public class LetraCambioException  extends Exception {

	public LetraCambioException () {
		super();
	}
	
	public LetraCambioException (String msg) {
		super(msg);
	}
	
	public LetraCambioException (String msg, Throwable e) {
        super(msg, e);
    }
	
}
