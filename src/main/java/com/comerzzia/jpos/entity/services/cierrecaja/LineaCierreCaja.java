/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.cierrecaja;

import com.comerzzia.jpos.entity.db.MedioPagoCaja;
import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class LineaCierreCaja {
    private MedioPagoCaja medioPago;
    private BigDecimal entrada;
    private BigDecimal salida;
    private BigDecimal total;
    private BigDecimal recuento;
    private BigDecimal descuadre;

    public LineaCierreCaja(MedioPagoCaja medioPago, BigDecimal entrada, BigDecimal salida, BigDecimal total) {
        this.medioPago=medioPago;
        this.entrada=entrada;
        this.salida=salida;
        this.total=total;
    }

    public MedioPagoCaja getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPagoCaja medioPago) {
        this.medioPago = medioPago;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getSalida() {
        return salida;
    }

    public void setSalida(BigDecimal salida) {
        this.salida = salida;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getRecuento() {
        return recuento;
    }

    public void setRecuento(BigDecimal recuento) {
        this.recuento = recuento;
    }

    public BigDecimal getDescuadre() {
        return descuadre;
    }

    public void setDescuadre(BigDecimal descuadre) {
        this.descuadre = descuadre;
    }
}
