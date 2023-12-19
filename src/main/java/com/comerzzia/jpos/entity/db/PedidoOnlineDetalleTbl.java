/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "D_PEDIDO_ONLINE_DET_TBL")
public class PedidoOnlineDetalleTbl implements Serializable {

    private static final long serialVersionUID = -3143386881317043766L;

    @JoinColumn(name = "UID_PEDIDO", referencedColumnName = "UID_PEDIDO")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PedidoOnlineTbl pedidoOnlineTbl;

    @Id
    @Column(name = "ID_LINEA")
    private Long idLinea;

    @Column(name = "ITM_CODIGO_I")
    private String itmCodigoI;

    @Column(name = "CANTIDAD")
    private Long cantidad;

    @Column(name = "COBRA_IVA")
    private Boolean itmCobraIva;

    @Column(name = "ITM_PVP_UNITARIO")
    private BigDecimal itmPvpUnitario;

    @Column(name = "ITM_PRECIO_FINANCIAMIENTO")
    private BigDecimal itmPrecioFinanciamiento;

    @Column(name = "ITM_DESCUENTO")
    private BigDecimal itmDescuento;

    @Column(name = "ITM_PRECIO_TOTAL")
    private BigDecimal itmPrecioTotal;

    @Column(name = "PORCENTAJE_DESCUENTO")
    private BigDecimal porcentajeDescuento;

    @Column(name = "ID_PROMOCION")
    private Long idPromocion;

    public PedidoOnlineDetalleTbl() {
    }

    public PedidoOnlineDetalleTbl(PedidoOnlineTbl pedidoOnlineTbl, Long idLinea, String itmCodigoI, Long cantidad, Boolean itmCobraIva, BigDecimal itmPvpUnitario, BigDecimal itmPrecioFinanciamiento, BigDecimal itmPrecioTotal,
             BigDecimal porcentajeDescuento, Long idPromocion, BigDecimal itmDescuento) {
        this.pedidoOnlineTbl = pedidoOnlineTbl;
        this.idLinea = idLinea;
        this.itmCodigoI = itmCodigoI;
        this.cantidad = cantidad;
        this.itmCobraIva = itmCobraIva;
        this.itmPvpUnitario = itmPvpUnitario;
        this.itmPrecioFinanciamiento = itmPrecioFinanciamiento;
        this.itmPrecioTotal = itmPrecioTotal;
        this.porcentajeDescuento = porcentajeDescuento;
        this.idPromocion = idPromocion;
        this.itmDescuento = itmDescuento;
    }

    public PedidoOnlineTbl getPedidoOnlineTbl() {
        return pedidoOnlineTbl;
    }

    public void setPedidoOnlineTbl(PedidoOnlineTbl pedidoOnlineTbl) {
        this.pedidoOnlineTbl = pedidoOnlineTbl;
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

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getItmCobraIva() {
        return itmCobraIva;
    }

    public void setItmCobraIva(Boolean itmCobraIva) {
        this.itmCobraIva = itmCobraIva;
    }

    public BigDecimal getItmPvpUnitario() {
        return itmPvpUnitario;
    }

    public void setItmPvpUnitario(BigDecimal itmPvpUnitario) {
        this.itmPvpUnitario = itmPvpUnitario;
    }

    public BigDecimal getItmPrecioFinanciamiento() {
        return itmPrecioFinanciamiento;
    }

    public void setItmPrecioFinanciamiento(BigDecimal itmPrecioFinanciamiento) {
        this.itmPrecioFinanciamiento = itmPrecioFinanciamiento;
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

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public BigDecimal getItmDescuento() {
        return itmDescuento;
    }

    public void setItmDescuento(BigDecimal itmDescuento) {
        this.itmDescuento = itmDescuento;
    }

}
