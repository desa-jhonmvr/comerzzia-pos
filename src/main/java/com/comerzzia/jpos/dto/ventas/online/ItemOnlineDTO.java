/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.util.enums.catalogo.EnumTipoItem;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class ItemOnlineDTO implements Cloneable {

    //private static final long serialVersionUID = 2429361335825684667L;
    private Long idLinea;
    private String itmCodigoI;
    private Long itmCantidad;
    private Boolean itmCobraIva;
    //@SerializedName("itmPvpAfiva")
    private BigDecimal itmPrecioFinanciamiento;
    //@SerializedName("itmPvpNAfiva")
    private BigDecimal itmPvpUnitario;
    //@SerializedName("itmPvpAfivaTotal")
    private BigDecimal itmPrecioTotal;
    private BigDecimal porcentajeDescuento;
    private BigDecimal valorDescuento;
    private String observacionPromocion;
    private Long idPromocion;
    private Long idLineaReferencia;
    private EnumTipoItem tipoItem;
    private transient boolean isGarantiaExtendida;
    private transient Articulos articulo;
    private transient boolean entregaDomicilio;

    @Override
    public ItemOnlineDTO clone() throws CloneNotSupportedException {
        return (ItemOnlineDTO) super.clone();
    }

    public ItemOnlineDTO() {
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public String getItmCodigoI() {
        return itmCodigoI;
    }

    public void setItmCodigoI(String itmCodigoI) {
        this.itmCodigoI = itmCodigoI;
    }

    public Long getItmCantidad() {
        return itmCantidad;
    }

    public void setItmCantidad(Long itmCantidad) {
        this.itmCantidad = itmCantidad;
    }

    public Boolean getItmCobraIva() {
        return itmCobraIva;
    }

    public void setItmCobraIva(Boolean itmCobraIva) {
        this.itmCobraIva = itmCobraIva;
    }

    public BigDecimal getItmPrecioFinanciamiento() {
        return itmPrecioFinanciamiento;
    }

    public void setItmPrecioFinanciamiento(BigDecimal itmPrecioFinanciamiento) {
        this.itmPrecioFinanciamiento = itmPrecioFinanciamiento;
    }

    public BigDecimal getItmPvpUnitario() {
        return itmPvpUnitario;
    }

    public void setItmPvpUnitario(BigDecimal itmPvpUnitario) {
        this.itmPvpUnitario = itmPvpUnitario;
    }

    public BigDecimal getItmPrecioTotal() {
        return itmPrecioTotal;
    }

    public void setItmPrecioTotal(BigDecimal itmPrecioTotal) {
        this.itmPrecioTotal = itmPrecioTotal;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public String getObservacionPromocion() {
        return observacionPromocion;
    }

    public void setObservacionPromocion(String observacionPromocion) {
        this.observacionPromocion = observacionPromocion;
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public Long getIdLineaReferencia() {
        return idLineaReferencia;
    }

    public void setIdLineaReferencia(Long idLineaReferencia) {
        this.idLineaReferencia = idLineaReferencia;
    }

    public boolean isIsGarantiaExtendida() {
        return isGarantiaExtendida;
    }

    public void setIsGarantiaExtendida(boolean isGarantiaExtendida) {
        this.isGarantiaExtendida = isGarantiaExtendida;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public boolean isEntregaDomicilio() {
        return entregaDomicilio;
    }

    public void setEntregaDomicilio(boolean entregaDomicilio) {
        this.entregaDomicilio = entregaDomicilio;
    }

    public EnumTipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(EnumTipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }
    
}
