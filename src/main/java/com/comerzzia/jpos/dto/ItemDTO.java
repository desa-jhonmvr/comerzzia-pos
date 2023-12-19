/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 2064353703124421183L;

    private String codigoI;
    private Long unidadManejo;
    private Long cantidad;
    private String descripcion;
    private String estado;
    private Integer idLinea;
    private BigDecimal precioOrigen;
    private BigDecimal precioTotalOrigen;
    private BigDecimal importeFinal;
    private BigDecimal importeTotalFinal;
    private String entregaDomicilio;
    private String pedidoFacturado;
    private String recogidaPosterior;
    private String instalacion;

    public ItemDTO() {
    }

    public ItemDTO(String codigoI, Long unidadManejo, Long cantidad, String descripcion, String estado, Integer idLinea, BigDecimal precioOrigen, BigDecimal precioTotalOrigen, BigDecimal importeFinal, BigDecimal importeTotalFinal, String entregaDomicilio, String pedidoFacturado, String recogidaPosterior) {
        this.codigoI = codigoI;
        this.unidadManejo = unidadManejo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idLinea = idLinea;
        this.precioOrigen = precioOrigen;
        this.precioTotalOrigen = precioTotalOrigen;
        this.importeFinal = importeFinal;
        this.importeTotalFinal = importeTotalFinal;
        this.entregaDomicilio = entregaDomicilio;
        this.pedidoFacturado = pedidoFacturado;
        this.recogidaPosterior = recogidaPosterior;
    }

    public String getCodigoI() {
        return codigoI;
    }

    public void setCodigoI(String codigoI) {
        this.codigoI = codigoI;
    }

    public Long getUnidadManejo() {
        return unidadManejo;
    }

    public void setUnidadManejo(Long unidadManejo) {
        this.unidadManejo = unidadManejo;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public BigDecimal getPrecioOrigen() {
        return precioOrigen;
    }

    public void setPrecioOrigen(BigDecimal precioOrigen) {
        this.precioOrigen = precioOrigen;
    }

    public BigDecimal getPrecioTotalOrigen() {
        return precioTotalOrigen;
    }

    public void setPrecioTotalOrigen(BigDecimal precioTotalOrigen) {
        this.precioTotalOrigen = precioTotalOrigen;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    public BigDecimal getImporteTotalFinal() {
        return importeTotalFinal;
    }

    public void setImporteTotalFinal(BigDecimal importeTotalFinal) {
        this.importeTotalFinal = importeTotalFinal;
    }

    public String getEntregaDomicilio() {
        return entregaDomicilio;
    }

    public void setEntregaDomicilio(String entregaDomicilio) {
        this.entregaDomicilio = entregaDomicilio;
    }

    public String getPedidoFacturado() {
        return pedidoFacturado;
    }

    public void setPedidoFacturado(String pedidoFacturado) {
        this.pedidoFacturado = pedidoFacturado;
    }

    public String getRecogidaPosterior() {
        return recogidaPosterior;
    }

    public void setRecogidaPosterior(String recogidaPosterior) {
        this.recogidaPosterior = recogidaPosterior;
    }

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

}
