/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class SubtotalOnlineDTO implements Serializable {

    private static final long serialVersionUID = 5502566812011886210L;

    private BigDecimal subtotalIvaCero;
    private BigDecimal subtotalIva;
    private BigDecimal iva;
    private BigDecimal total;

    public SubtotalOnlineDTO() {
    }

    public BigDecimal getSubtotalIvaCero() {
        return subtotalIvaCero;
    }

    public void setSubtotalIvaCero(BigDecimal subtotalIvaCero) {
        this.subtotalIvaCero = subtotalIvaCero;
    }

    public BigDecimal getSubtotalIva() {
        return subtotalIva;
    }

    public void setSubtotalIva(BigDecimal subtotalIva) {
        this.subtotalIva = subtotalIva;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    

}
