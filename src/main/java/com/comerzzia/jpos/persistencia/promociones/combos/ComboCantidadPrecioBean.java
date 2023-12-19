package com.comerzzia.jpos.persistencia.promociones.combos;

import java.math.BigDecimal;

public class ComboCantidadPrecioBean implements Comparable<ComboCantidadPrecioBean> {

    private String id;
    private Integer cantidad;
    private BigDecimal precioTotal;
    private BigDecimal precioVenta;
    private Double importe;
    private Double importeTotal;

    @Override
    public int compareTo(ComboCantidadPrecioBean t) {
        return cantidad.compareTo(t.getCantidad());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }
}
