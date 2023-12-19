/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.CupoVirtualMemoria;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author RD mejora en cupo virtual
 */
public class ValorCupo {
    private Integer numeroCredito;
    private BigDecimal cupo;
    private BigDecimal totalPago=new BigDecimal(BigInteger.ZERO);
    private boolean cupoValidado=false;

    public Integer getNumeroCredito() {
        return numeroCredito;
    }

    public void setNumeroCredito(Integer numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

    public BigDecimal getCupo() {
        return cupo;
    }

    public void setCupo(BigDecimal cupo) {
        this.cupo = cupo;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }
    
    
}
