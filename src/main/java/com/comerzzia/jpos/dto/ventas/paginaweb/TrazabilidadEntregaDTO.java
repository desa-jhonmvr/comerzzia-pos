/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ventas.BdgRequerimientoDTO;
import com.comerzzia.jpos.dto.ventas.EntregaDomilicioDTO;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class TrazabilidadEntregaDTO implements Serializable {

    private static final long serialVersionUID = -8173990095955794586L;
    private String numeroFactura;
    private EntregaDomilicioDTO entregaDomilicio;
    private ClienteDTO cliente;
    private BigDecimal valorFactura;
    private BigDecimal valorDescuento;
    private BigDecimal gastoEnvio;
    private String numeroPedido;
    private String referenciaPedido;
    private BdgRequerimientoDTO requerimiento;

    public EntregaDomilicioDTO getEntregaDomilicio() {
        return entregaDomilicio;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setEntregaDomilicio(EntregaDomilicioDTO entregaDomilicio) {
        this.entregaDomilicio = entregaDomilicio;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public BigDecimal getGastoEnvio() {
        return gastoEnvio;
    }

    public void setGastoEnvio(BigDecimal gastoEnvio) {
        this.gastoEnvio = gastoEnvio;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getReferenciaPedido() {
        return referenciaPedido;
    }

    public void setReferenciaPedido(String referenciaPedido) {
        this.referenciaPedido = referenciaPedido;
    }

    public BdgRequerimientoDTO getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(BdgRequerimientoDTO requerimiento) {
        this.requerimiento = requerimiento;
    }

}
