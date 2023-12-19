/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumTipoPagoPaginaWeb;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "PW_FORMASPAGO_TBL")
public class PwFormasPago implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "FP_CODMEDPAG", referencedColumnName = "CODMEDPAG")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MedioPagoCaja medioPagoCaja;

    @Column(name = "FP_NUMERO_MESES")
    private Long pwpNumeroMeses;
    
    @Column(name = "FP_TIPO_PAGO")
    private EnumTipoPagoPaginaWeb pwpTipoPago;

    @Column(name = "FP_VALOR_BASE_12")
    private BigDecimal pwpValorBase12;

    @Column(name = "FP_VALOR_BASE_0")
    private BigDecimal pwpValorBase0;

    @Column(name = "FP_VALOR_IVA")
    private BigDecimal pwpValorIva;

    @Column(name = "FP_VALOR_TOTAL")
    private BigDecimal pwpValorTotal;

    @Column(name = "FP_NAUTORIZACION")
    private String pwpNAutorizacion;

    @Column(name = "FP_LONGITUD_TARJETA")
    private Long pwpLongitudTarjeta;

    @Id
    @Column(name = "FP_NUMERO_TARJETA")
    private String pwpNumeroTarjeta;

    @Column(name = "FP_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pwpFecha;

    @Column(name = "FP_HORA")
    private String pwpHora;

    @Column(name = "FP_PROCESADO")
    private String pwpProcesado;

    @Id
    @JoinColumn(name = "UID_CAB_ID", referencedColumnName = "UID_CAB_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CabPrefactura cabPrefactura;
    
    @Column(name = "FP_ANULADO")
    private String pwpAnulado;
    
    @Column(name = "FP_PORCE_DESCUENTO")
    private BigDecimal pwpDescuentoPago;
    
    @Column(name = "FP_TOTAL")
    private BigDecimal pwpTotalSinDescuento;
    
    /*Getters y Setters */

    public MedioPagoCaja getMedioPagoCaja() {
        return medioPagoCaja;
    }

    public void setMedioPagoCaja(MedioPagoCaja medioPagoCaja) {
        this.medioPagoCaja = medioPagoCaja;
    }

    public Long getPwpNumeroMeses() {
        return pwpNumeroMeses;
    }

    public void setPwpNumeroMeses(Long pwpNumeroMeses) {
        this.pwpNumeroMeses = pwpNumeroMeses;
    }

    public BigDecimal getPwpValorBase12() {
        return pwpValorBase12;
    }

    public void setPwpValorBase12(BigDecimal pwpValorBase12) {
        this.pwpValorBase12 = pwpValorBase12;
    }

    public BigDecimal getPwpValorBase0() {
        return pwpValorBase0;
    }

    public void setPwpValorBase0(BigDecimal pwpValorBase0) {
        this.pwpValorBase0 = pwpValorBase0;
    }

    public BigDecimal getPwpValorIva() {
        return pwpValorIva;
    }

    public void setPwpValorIva(BigDecimal pwpValorIva) {
        this.pwpValorIva = pwpValorIva;
    }

    public BigDecimal getPwpValorTotal() {
        return pwpValorTotal;
    }

    public void setPwpValorTotal(BigDecimal pwpValorTotal) {
        this.pwpValorTotal = pwpValorTotal;
    }

    public String getPwpNAutorizacion() {
        return pwpNAutorizacion;
    }

    public void setPwpNAutorizacion(String pwpNAutorizacion) {
        this.pwpNAutorizacion = pwpNAutorizacion;
    }

    public Long getPwpLongitudTarjeta() {
        return pwpLongitudTarjeta;
    }

    public void setPwpLongitudTarjeta(Long pwpLongitudTarjeta) {
        this.pwpLongitudTarjeta = pwpLongitudTarjeta;
    }

    public String getPwpNumeroTarjeta() {
        return pwpNumeroTarjeta;
    }

    public void setPwpNumeroTarjeta(String pwpNumeroTarjeta) {
        this.pwpNumeroTarjeta = pwpNumeroTarjeta;
    }

    public Date getPwpFecha() {
        return pwpFecha;
    }

    public void setPwpFecha(Date pwpFecha) {
        this.pwpFecha = pwpFecha;
    }

    public String getPwpHora() {
        return pwpHora;
    }

    public void setPwpHora(String pwpHora) {
        this.pwpHora = pwpHora;
    }

    public String getPwpProcesado() {
        return pwpProcesado;
    }

    public void setPwpProcesado(String pwpProcesado) {
        this.pwpProcesado = pwpProcesado;
    }

    public CabPrefactura getCabPrefactura() {
        return cabPrefactura;
    }

    public void setCabPrefactura(CabPrefactura cabPrefactura) {
        this.cabPrefactura = cabPrefactura;
    }

    public String getPwpAnulado() {
        return pwpAnulado;
    }

    public void setPwpAnulado(String pwpAnulado) {
        this.pwpAnulado = pwpAnulado;
    }

    public EnumTipoPagoPaginaWeb getPwpTipoPago() {
        return pwpTipoPago;
    }

    public void setPwpTipoPago(EnumTipoPagoPaginaWeb pwpTipoPago) {
        this.pwpTipoPago = pwpTipoPago;
    }

    public BigDecimal getPwpDescuentoPago() {
        return pwpDescuentoPago;
    }

    public void setPwpDescuentoPago(BigDecimal pwpDescuentoPago) {
        this.pwpDescuentoPago = pwpDescuentoPago;
    }

    public BigDecimal getPwpTotalSinDescuento() {
        return pwpTotalSinDescuento;
    }

    public void setPwpTotalSinDescuento(BigDecimal pwpTotalSinDescuento) {
        this.pwpTotalSinDescuento = pwpTotalSinDescuento;
    }
    
    
    
}




