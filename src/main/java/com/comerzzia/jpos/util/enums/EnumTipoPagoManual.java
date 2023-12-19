/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author Gabriel Simbania
 */
public enum EnumTipoPagoManual {
    
    POS_MOVIL("S"),
    DATA_LINK("L"),
    MANUAL("");
    
    private String codigo;

    private EnumTipoPagoManual(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
    
}
