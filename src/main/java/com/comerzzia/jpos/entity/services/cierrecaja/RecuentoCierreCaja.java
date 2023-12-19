/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.cierrecaja;

import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class RecuentoCierreCaja {
    private BigDecimal venta;
    private BigDecimal movimientos;
    private BigDecimal bonos;
    private BigDecimal abonos;
    private BigDecimal salida;

    public BigDecimal getVenta() {
        return venta;
    }

    public void setVenta(BigDecimal venta) {
        this.venta = venta;
    }

    public BigDecimal getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(BigDecimal movimientos) {
        this.movimientos = movimientos;
    }

    public BigDecimal getSalida() {
        return salida;
    }

    public void setSalida(BigDecimal salida) {
        this.salida = salida;
    }

    public BigDecimal getBonos() {
        return bonos;
    }

    public void setBonos(BigDecimal bonos) {
        this.bonos = bonos;
    }

    public BigDecimal getAbonos() {
        return abonos;
    }

    public void setAbonos(BigDecimal abonos) {
        this.abonos = abonos;
    }
}
