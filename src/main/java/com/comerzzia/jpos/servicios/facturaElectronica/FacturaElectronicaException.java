/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.facturaElectronica;

/**
 *
 * @author SMLM
 */
public class FacturaElectronicaException extends Exception {

    public FacturaElectronicaException() {
        super();
    }

    public FacturaElectronicaException(String msg) {
        super(msg);
    }

    public FacturaElectronicaException(String msg, Throwable e) {
        super(msg, e);
    }  
    
    public FacturaElectronicaException(Throwable e) {
        super(e);
    }      
}
