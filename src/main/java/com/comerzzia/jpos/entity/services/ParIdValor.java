/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services;

import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class ParIdValor {
    private String id;
    private BigDecimal valor;
    private BigDecimal valorUsado;

    public ParIdValor() {
    }
    
    public ParIdValor(String id, BigDecimal valor) {
        this.id = id;
        this.valor = valor;
    }
    
    public ParIdValor(String id, BigDecimal valor, BigDecimal valorUsado) {
        this.id = id;
        this.valor = valor;
        this.valorUsado = valorUsado;
    } 
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorUsado() {
        return valorUsado;
    }

    public void setValorUsado(BigDecimal valorUsado) {
        this.valorUsado = valorUsado;
    }
   
}
