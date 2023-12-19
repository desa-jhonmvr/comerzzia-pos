package com.comerzzia.jpos.persistencia.promociones.combos;

import java.math.BigDecimal;

public class ComboArticuloDtoBean {

    private String codigo;
    private String descripcion;
    private BigDecimal descuento;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    
}
