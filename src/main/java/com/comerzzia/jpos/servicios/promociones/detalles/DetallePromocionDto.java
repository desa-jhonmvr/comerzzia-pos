package com.comerzzia.jpos.servicios.promociones.detalles;

import java.math.BigDecimal;


public class DetallePromocionDto extends DetallePromocion {

    private String codArticulo;
    private String desArticulo;
    private BigDecimal precioTarifa;
    private BigDecimal precioTarifaConImpuestos;
    private BigDecimal precioVenta;
    private BigDecimal precioTotal;
    private Double descuento;

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDesArticulo() {
        return desArticulo;
    }

    public void setDesArticulo(String desArticulo) {
        this.desArticulo = desArticulo;
    }

    public BigDecimal getPrecioTarifa() {
        return precioTarifa;
    }

    public void setPrecioTarifa(BigDecimal precioTarifa) {
        this.precioTarifa = precioTarifa;
    }

    public BigDecimal getPrecioTarifaConImpuestos() {
        return precioTarifaConImpuestos;
    }

    public void setPrecioTarifaConImpuestos(BigDecimal precioTarifaConImpuestos) {
        this.precioTarifaConImpuestos = precioTarifaConImpuestos;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

}
