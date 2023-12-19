/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author ME
 */
public enum EnumCodigoImpuestos {
    
    IMPUESTO_ICE(3680L);
    
    private final Long codigo;

    private EnumCodigoImpuestos(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigo() {
        return codigo;
    }

  
    
    
}
