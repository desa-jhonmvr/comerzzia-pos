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
public class FormaPagoOnlineDTO implements Serializable {

    private static final long serialVersionUID = -4726749965515871893L;

    private Integer tipoPago;
    private String numeroTarjeta;
    private Long idPlan;
    // @SerializedName("descuento")
    private BigDecimal porcentajeDescuento;
    private BigDecimal ahorro;
    private BigDecimal aPagar;
    private BigDecimal cuota;
    //@SerializedName("total")
    private BigDecimal totalSinDescuento;
    private Integer numCuotas;
    private BigDecimal subtotalIvaCero;
    private BigDecimal subtotalIva;
    private BigDecimal iva;
    private BigDecimal porcentajeInteres;
    private BigDecimal importeInteres;
    private String referenciaAutorizacion;
    private String nReferenciaAutorizacion;
    private String lote;
    private String referencia;

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getAhorro() {
        return ahorro;
    }

    public void setAhorro(BigDecimal ahorro) {
        this.ahorro = ahorro;
    }

    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getTotalSinDescuento() {
        return totalSinDescuento;
    }

    public void setTotalSinDescuento(BigDecimal totalSinDescuento) {
        this.totalSinDescuento = totalSinDescuento;
    }

    public Integer getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(Integer numCuotas) {
        this.numCuotas = numCuotas;
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

    public BigDecimal getPorcentajeInteres() {
        return porcentajeInteres;
    }

    public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
        this.porcentajeInteres = porcentajeInteres;
    }

    public BigDecimal getImporteInteres() {
        return importeInteres;
    }

    public void setImporteInteres(BigDecimal importeInteres) {
        this.importeInteres = importeInteres;
    }

    public String getReferenciaAutorizacion() {
        return referenciaAutorizacion;
    }

    public void setReferenciaAutorizacion(String referenciaAutorizacion) {
        this.referenciaAutorizacion = referenciaAutorizacion;
    }

    public String getnReferenciaAutorizacion() {
        return nReferenciaAutorizacion;
    }

    public void setnReferenciaAutorizacion(String nReferenciaAutorizacion) {
        this.nReferenciaAutorizacion = nReferenciaAutorizacion;
    }
    
    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

}
