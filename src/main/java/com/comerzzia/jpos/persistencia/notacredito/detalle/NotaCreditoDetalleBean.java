/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.notacredito.detalle;

import com.comerzzia.util.base.MantenimientoBean;
import java.math.BigDecimal;

/**
 *
 * @author SMLM
 */
public class NotaCreditoDetalleBean extends MantenimientoBean {
    
    private String uidNotaCredito;
    private Long idLinea;
    private Long idCajero;
    private String codVendedor;
    private String codart;
    private String codigoBarras;
    private int cantidad;
    private BigDecimal precio;
    private BigDecimal precioTotal;
    private BigDecimal importeFinal;
    private BigDecimal importeTotalFinal;
    private String uidTicket;
    private String codImp;
    private BigDecimal porcentaje;
    //UTILIDAD RD
    private BigDecimal costoLanded;
    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

 public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public Long getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(Long idCajero) {
        this.idCajero = idCajero;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
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

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getCodImp() {
        return codImp;
    }

    public void setCodImp(String codImp) {
        this.codImp = codImp;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    protected void initNuevoBean() {
    }

    public BigDecimal getCostoLanded() {
        return costoLanded;
    }

    public void setCostoLanded(BigDecimal costoLanded) {
        this.costoLanded = costoLanded;
    }
     
}
