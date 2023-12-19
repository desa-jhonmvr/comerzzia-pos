/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.sukupon;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

/**
 *
 * @author SMLM
 */
public class SukuponBean {

    private String codAlm;
    private String idCupon;
    private String codBarras;
    private Fecha fechaExpedicion;
    private Fecha fechaValidez;
    private String utilizado;
    private BigDecimal total;
    private String notaCredito;
    private String factura;
    private BigDecimal utilizoItem;
    private BigDecimal saldoItem;

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(String idCupon) {
        this.idCupon = idCupon;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public Fecha getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Fecha fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Fecha getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(Fecha fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public String getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(String utilizado) {
        this.utilizado = utilizado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(String notaCredito) {
        this.notaCredito = notaCredito;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public BigDecimal getUtilizoItem() {
        return utilizoItem;
    }

    public void setUtilizoItem(BigDecimal utilizoItem) {
        this.utilizoItem = utilizoItem;
    }

    public BigDecimal getSaldoItem() {
        return saldoItem;
    }

    public void setSaldoItem(BigDecimal saldoItem) {
        this.saldoItem = saldoItem;
    }
 
}
