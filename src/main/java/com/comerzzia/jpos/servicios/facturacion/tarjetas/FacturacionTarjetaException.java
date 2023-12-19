/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.facturacion.tarjetas;

/**
 *
 * @author SMLM
 */
public class FacturacionTarjetaException  extends com.comerzzia.util.base.Exception { 
    
    	public FacturacionTarjetaException() {
		super();
	}

	public FacturacionTarjetaException(String msg) {
		super(msg);
	}

	public FacturacionTarjetaException(String msg, Throwable e) {
		super(msg, e);
	}

	public FacturacionTarjetaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}
        
	public FacturacionTarjetaException(String msg, Exception e) {
		super(msg, e);
	}

	public FacturacionTarjetaException(String msg, String msgKey, Exception e) {
		super(msg, msgKey, e);
	}        

	public FacturacionTarjetaException(String msg, String msgKey) {
		super(msg, msgKey);
	}  
}
