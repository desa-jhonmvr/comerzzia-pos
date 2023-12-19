/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class MedioPagoDTO implements Serializable{
    
    private static final long serialVersionUID = -7437620805927094618L;
    
    private String codMedioPago;
    private String desMedioPago;

    public MedioPagoDTO(String codMedioPago, String desMedioPago) {
        this.codMedioPago = codMedioPago;
        this.desMedioPago = desMedioPago;
    }

    /*Getters and Setters*/
    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public String getDesMedioPago() {
        return desMedioPago;
    }

    public void setDesMedioPago(String desMedioPago) {
        this.desMedioPago = desMedioPago;
    }
    
    
    
}
