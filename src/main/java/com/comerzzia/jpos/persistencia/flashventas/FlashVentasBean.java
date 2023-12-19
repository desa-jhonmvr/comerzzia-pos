/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.flashventas;

import java.math.BigDecimal;

/**
 *
 * @author SMLM
 */
public class FlashVentasBean {
    private Long numClientes = new Long(0);
    private Long numFacturas = new Long(0);
    private BigDecimal importeFinal = new BigDecimal(0);
    private BigDecimal descuento = new BigDecimal(0);
    private BigDecimal acumuladoMes = new BigDecimal(0);

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    public Long getNumClientes() {
        return numClientes;
    }

    public void setNumClientes(Long numClientes) {
        this.numClientes = numClientes;
    }

    public Long getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(Long numFacturas) {
        this.numFacturas = numFacturas;
    }

    public BigDecimal getAcumuladoMes() {
        return acumuladoMes;
    }

    public void setAcumuladoMes(BigDecimal acumuladoMes) {
        this.acumuladoMes = acumuladoMes;
    }
    
}
