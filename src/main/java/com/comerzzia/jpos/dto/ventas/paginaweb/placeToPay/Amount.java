/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class Amount implements Serializable{
    
    private static final long serialVersionUID = 2137598362051881483L;
    
    private String currency;
    private BigDecimal total;
    private List<TaxDetail>  taxes;
    private List<AmountDetail>  details;

    public Amount() {
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<TaxDetail> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxDetail> taxes) {
        this.taxes = taxes;
    }

    public List<AmountDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AmountDetail> details) {
        this.details = details;
    }
    
    
    
    
    
    
}
