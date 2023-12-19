/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

/**
 *
 * @author Gabriel Simbania
 */
public enum EnumTipoDocumento {
    
    FACTURA("FACTURA"),
    NOTA_CREDITO("NOTA_CREDITO"),
    RESERVA("RESERVA"),
    BONO("BONO"),
    RESERVA_LIQUIDADA("RESERVA_LIQUIDADA"),
    RESERVA_PREFACTURA("RESERVA_PREFACTURA"),
    RESERVA_PREFACTURA_FACTURADA("RESERVA_PREFACTURA_FACTURADA"),;
    
    private final String nombre;

    private EnumTipoDocumento(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    
    
}
