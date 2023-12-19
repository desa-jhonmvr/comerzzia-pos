package com.comerzzia.jpos.persistencia.promociones.combos;

import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import java.math.BigDecimal;

public class ComboArticuloCanjeablePuntos {

    private String codigo;
    private Integer cantidad;
    private Integer limite;
    private BigDecimal descuento;
    private LineaTicket linea;
    private PromocionTipoPuntosCanjeo promocion;
    private String descripcion;

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public LineaTicket getLinea() {
        return linea;
    }

    public void setLinea(LineaTicket linea) {
        this.linea = linea;
    }

    public PromocionTipoPuntosCanjeo getPromocion() {
        return promocion;
    }

    public void setPromocion(PromocionTipoPuntosCanjeo promocion) {
        this.promocion = promocion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }




}
