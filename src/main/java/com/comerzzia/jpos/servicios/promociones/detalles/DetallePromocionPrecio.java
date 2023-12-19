package com.comerzzia.jpos.servicios.promociones.detalles;

import java.math.BigDecimal;

public class DetallePromocionPrecio extends DetallePromocion {

    private String codArticulo;
    private String desArticulo;
    private BigDecimal precioTarifa;
    private BigDecimal precioTarifaConImpuestos;
    private BigDecimal precioVenta;
    private BigDecimal precioTotal;

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

    public BigDecimal getPrecioTarifaConImpuestos() {
        return precioTarifaConImpuestos;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioTarifa(BigDecimal precioTarifa) {
        this.precioTarifa = precioTarifa;
    }

    public void setPrecioTarifaConImpuestos(BigDecimal precioTarifaConImpuestos) {
        this.precioTarifaConImpuestos = precioTarifaConImpuestos;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    
    
    
    
}
