/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class CompletarOrdenDTO implements Serializable {

    private static final long serialVersionUID = -7180662651115335437L;

    @SerializedName("id_pre_factura")
    private String idPreFactura;

    @SerializedName("numero_factura")
    private String numeroFactura;

    @SerializedName("fecha_hora")
    private Long fechaHora;
    
    private String codAlm;
    
    @SerializedName("valor_pagar")
    private BigDecimal valorPagar;

    public CompletarOrdenDTO() {

    }

    public CompletarOrdenDTO(String idPreFactura, String numeroFactura, Long fechaHora,String codAlm,BigDecimal  valorPagar) {
        this.idPreFactura = idPreFactura;
        this.numeroFactura = numeroFactura;
        this.fechaHora = fechaHora;
        this.codAlm = codAlm;
        this.valorPagar = valorPagar;
    }

    public String getIdPreFactura() {
        return idPreFactura;
    }

    public void setIdPreFactura(String idPreFactura) {
        this.idPreFactura = idPreFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Long getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Long fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public BigDecimal getValorPagar() {
        return this.valorPagar;
    }

    public void setValorPagar(BigDecimal valorPagar) {
        this.valorPagar = valorPagar;
    }
    
    
    

}
