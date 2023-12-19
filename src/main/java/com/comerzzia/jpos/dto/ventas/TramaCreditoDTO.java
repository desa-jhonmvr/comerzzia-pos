/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author Gabriel Simbania
 */
public class TramaCreditoDTO implements Serializable {

    private static final long serialVersionUID = -7180662651115335437L;

   private Integer credito;
   private BigDecimal valor;
   private String local;

    public TramaCreditoDTO(Integer credito, BigDecimal valor, String local) {
        this.credito = credito;
        this.valor = valor;
        this.local = local;
    }

   
   
    public Integer getCredito() {
        return credito;
    }

    public void setCredito(Integer credito) {
        this.credito = credito;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

   
   
   
}
