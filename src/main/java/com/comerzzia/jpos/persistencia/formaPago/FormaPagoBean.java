/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.formaPago;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author DESARROLLO
 */
public class FormaPagoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoAlmacen;
    private String codigoCaja;
    private Long idNotaCredito;
    private String codigoMedioPago;
    private String descripcionMedioPago;
    private BigDecimal valor;
    private String cruzaEfectivo;
    private String procesado;

    public FormaPagoBean() {
        this.procesado = "N";
    }

    public FormaPagoBean(String codigoAlmacen, String codigoCaja, Long idNotaCredito, String codigoMedioPago, String descripcionMedioPago, BigDecimal valor, String cruzaEfectivo) {
        this.codigoAlmacen = codigoAlmacen;
        this.codigoCaja = codigoCaja;
        this.idNotaCredito = idNotaCredito;
        this.codigoMedioPago = codigoMedioPago;
        this.descripcionMedioPago = descripcionMedioPago;
        this.valor = valor;
        this.cruzaEfectivo = cruzaEfectivo;
        this.procesado = "N";
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public Long getIdNotaCredito() {
        return idNotaCredito;
    }

    public void setIdNotaCredito(Long idNotaCredito) {
        this.idNotaCredito = idNotaCredito;
    }

    public String getCodigoMedioPago() {
        return codigoMedioPago;
    }

    public void setCodigoMedioPago(String codigoMedioPago) {
        this.codigoMedioPago = codigoMedioPago;
    }

    public String getDescripcionMedioPago() {
        return descripcionMedioPago;
    }

    public void setDescripcionMedioPago(String descripcionMedioPago) {
        this.descripcionMedioPago = descripcionMedioPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCruzaEfectivo() {
        return cruzaEfectivo;
    }

    public void setCruzaEfectivo(String cruzaEfectivo) {
        this.cruzaEfectivo = cruzaEfectivo;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

}
