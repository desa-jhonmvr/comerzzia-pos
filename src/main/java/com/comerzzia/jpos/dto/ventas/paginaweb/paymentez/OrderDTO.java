/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.paymentez;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class OrderDTO implements Serializable{
    
    private static final long serialVersionUID = -7484091171145743899L;
    
    private BigDecimal amount;
    private String description;
    private String dev_reference;
    private BigDecimal vat;
    private BigDecimal tax_percentage;
    private BigDecimal taxable_amount;
    private Integer installments;
    private Integer installments_type;

    public OrderDTO() {
        
    }
    
    public OrderDTO(BigDecimal amount, String description, String dev_reference, BigDecimal vat) {
        this.amount = amount;
        this.description = description;
        this.dev_reference = dev_reference;
        this.vat = vat;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDev_reference() {
        return dev_reference;
    }

    public void setDev_reference(String dev_reference) {
        this.dev_reference = dev_reference;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(BigDecimal tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public BigDecimal getTaxable_amount() {
        return taxable_amount;
    }

    public void setTaxable_amount(BigDecimal taxable_amount) {
        this.taxable_amount = taxable_amount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Integer getInstallments_type() {
        return installments_type;
    }

    public void setInstallments_type(Integer installments_type) {
        this.installments_type = installments_type;
    }
    
    
    
    
    
}
