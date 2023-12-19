/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author amos
 */
public class DescuentoTicket {
    private String descripcion;
    private BigDecimal descuentoTotal;
    private BigDecimal descuento;


    public DescuentoTicket() {
    }
    
    public DescuentoTicket(String descripcion, BigDecimal descuentoTotal, BigDecimal descuento) {
        this.descripcion = descripcion;
        this.descuentoTotal = descuentoTotal;
        this.descuento = descuento;
    }
    
    public String getDescuentoTotalString(){
        return "$ -" + descuentoTotal.setScale(2, RoundingMode.HALF_UP).toString();
    }
    public String getDescuentoString(){
        return "$ -" + descuento.setScale(2, RoundingMode.HALF_UP).toString();
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDescuentoTotal() {
        return descuentoTotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setDescuentoTotal(BigDecimal descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }
    
    
    
}
