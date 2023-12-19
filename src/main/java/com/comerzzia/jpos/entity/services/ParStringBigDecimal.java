/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services;

import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class ParStringBigDecimal {
    private String valorString;
    private BigDecimal valorBigDecimal;

    public ParStringBigDecimal(String valorString, BigDecimal valorBigDecimal) {
        this.valorString = valorString;
        this.valorBigDecimal = valorBigDecimal;
    }

    public ParStringBigDecimal() {
    }


    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public BigDecimal getValorBigDecimal() {
        return valorBigDecimal;
    }

    public void setValorBigDecimal(BigDecimal valorBigDecimal) {
        this.valorBigDecimal = valorBigDecimal;
    }
    
    
    
}
