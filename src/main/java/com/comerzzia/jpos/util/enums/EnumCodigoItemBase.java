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
public enum EnumCodigoItemBase {
    ITEM_BASE_INICIAL(0L),
    ITEM_BASE_ACTIVO(1L),
    ITEM_BASE_MAS(2L);
    
    private final Long codigo;

    private EnumCodigoItemBase(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigo() {
        return codigo;
    }

  
    
    
}
