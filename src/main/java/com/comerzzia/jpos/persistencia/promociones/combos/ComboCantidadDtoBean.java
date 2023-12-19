/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.promociones.combos;

import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class ComboCantidadDtoBean implements Comparable<ComboCantidadDtoBean> {
    private Integer cantidad;
    private BigDecimal descuento;

    @Override
    public int compareTo(ComboCantidadDtoBean t) {
        return cantidad.compareTo(t.getCantidad());
    }

    
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    
}
