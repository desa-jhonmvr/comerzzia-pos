/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumTipoPagoPaginaWeb;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
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
@Table(name = "D_PEDIDO_ONLINE_PAG_TBL")
public class PedidoOnlinePagoTbl implements Serializable {

    private static final long serialVersionUID = -8748681376246780715L;

    @Id
    @Basic(optional = false)
    @Column(name = "UID_PEDIDO_PAGO")
    private String uidPedidoPago;

    @JoinColumn(name = "UID_PEDIDO", referencedColumnName = "UID_PEDIDO")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PedidoOnlineTbl pedidoOnlineTbl;

    @Column(name = "TIPO_PAGO")
    private EnumTipoPagoPaginaWeb tipoPago;

    @Column(name = "NUMERO_TARJETA")
    private String numeroTarjeta;

    @Column(name = "PORCENTAJE_DESCUENTO")
    private BigDecimal porcentajeDescuento;

    @Column(name = "VALOR_INTERES")
    private BigDecimal valorInteres;

    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;

    @Column(name = "NUMERO_MESES")
    private Integer numeroMeses;

    @Column(name = "REFERENCIA_AUTORIZACION")
    private String referenciaAutorizacion;

    @Column(name = "PORCENTAJE_INTERES")
    private BigDecimal porcentajeInteres;

    @Column(name = "IMPORTE_INTERES")
    private BigDecimal importeInteres;

    @Column(name = "LOTE")
    private String lote;

    @Column(name = "REFERENCIA")
    private String referencia;

    public PedidoOnlinePagoTbl() {
    }

    public PedidoOnlinePagoTbl(String uidPedidoPago, PedidoOnlineTbl pedidoOnlineTbl, EnumTipoPagoPaginaWeb tipoPago, String numeroTarjeta, BigDecimal porcentajeDescuento, BigDecimal valorInteres, BigDecimal valorTotal, Integer numeroMeses,
            String referenciaAutorizacion, BigDecimal porcentajeInteres, BigDecimal importeInteres, String lote, String referencia) {
        this.uidPedidoPago = uidPedidoPago;
        this.pedidoOnlineTbl = pedidoOnlineTbl;
        this.tipoPago = tipoPago;
        this.numeroTarjeta = numeroTarjeta;
        this.porcentajeDescuento = porcentajeDescuento;
        this.valorInteres = valorInteres;
        this.valorTotal = valorTotal;
        this.numeroMeses = numeroMeses;
        this.referenciaAutorizacion = referenciaAutorizacion;
        this.porcentajeInteres = porcentajeInteres;
        this.importeInteres = importeInteres;
        this.lote= lote;
        this.referencia= referencia;
    }

    public String getUidPedidoPago() {
        return uidPedidoPago;
    }

    public void setUidPedidoPago(String uidPedidoPago) {
        this.uidPedidoPago = uidPedidoPago;
    }

    public PedidoOnlineTbl getPedidoOnlineTbl() {
        return pedidoOnlineTbl;
    }

    public void setPedidoOnlineTbl(PedidoOnlineTbl pedidoOnlineTbl) {
        this.pedidoOnlineTbl = pedidoOnlineTbl;
    }

    public EnumTipoPagoPaginaWeb getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(EnumTipoPagoPaginaWeb tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(BigDecimal valorInteres) {
        this.valorInteres = valorInteres;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getNumeroMeses() {
        return numeroMeses;
    }

    public void setNumeroMeses(Integer numeroMeses) {
        this.numeroMeses = numeroMeses;
    }

    public String getReferenciaAutorizacion() {
        return referenciaAutorizacion;
    }

    public void setReferenciaAutorizacion(String referenciaAutorizacion) {
        this.referenciaAutorizacion = referenciaAutorizacion;
    }

    public BigDecimal getPorcentajeInteres() {
        return porcentajeInteres;
    }

    public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
        this.porcentajeInteres = porcentajeInteres;
    }

    public BigDecimal getImporteInteres() {
        return importeInteres;
    }

    public void setImporteInteres(BigDecimal importeInteres) {
        this.importeInteres = importeInteres;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

}
