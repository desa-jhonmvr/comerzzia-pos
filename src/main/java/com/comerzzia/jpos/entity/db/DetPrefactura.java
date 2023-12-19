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
@Table(name = "PW_DET_PREFACTURA_TBL")
public class DetPrefactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JoinColumn(name = "DET_CODART", referencedColumnName = "CODART")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Articulos articulo;

    @Id
    @Column(name = "DET_LINEA")
    private Long detLinea;

    @Column(name = "DET_CANTIDAD")
    private Integer detCantidad;

    @Column(name = "DET_PVP_UNI_SIN_IVA_SIN_DSTO")
    private BigDecimal detPvpUniSinIvaSinDsto;

    @Column(name = "DET_PVP_UNI_CON_IVA_SIN_DSTO")
    private BigDecimal detPvpUniConIvaSinDsto;

    @Column(name = "DET_PVP_TOTAL_SIN_IVA_CON_DSTO")
    private BigDecimal detPvpTotalSinIvaConDsto;

    @Column(name = "DET_PVP_TOTAL_CON_IVA_CON_DSTO")
    private BigDecimal detPvpTotalConIvaConDsto;

    @Column(name = "DET_PRECIO_REAL")
    private BigDecimal detPrecioReal;

    @Column(name = "DET_PORCE_DSTO")
    private BigDecimal detPorceDsto;

    @Column(name = "DET_PROCESADO")
    private String detProcesado;

    @JoinColumn(name = "UID_CAB_ID", referencedColumnName = "UID_CAB_ID")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CabPrefactura cabPrefactura;

    @Column(name = "DET_GARANTIA_EXTENDIDA")
    private Boolean detIsGarantiaExtendida;
    
    @Column(name = "DET_LINEA_REFERENCIA_EGO")
    private Long detLineaReferenciaEgo;
    
    @Column(name = "UID_FACTURA_REFERENCIA_EGO")
    private String uidFacturaReferenciaEgo;
    
    @Column(name = "DET_GARANTIA_EXTENDIDA_GRATIS")
    private Boolean detIsGarantiaExtendidaGratis;
    
    @Column(name = "DET_ANULADO")
    private String detAnulado;
    
    @Column(name = "DET_CODIMP")
    private String detCodImp;

    /*Getters y Setters*/

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Long getDetLinea() {
        return detLinea;
    }

    public void setDetLinea(Long detLinea) {
        this.detLinea = detLinea;
    }

    public Integer getDetCantidad() {
        return detCantidad;
    }

    public void setDetCantidad(Integer detCantidad) {
        this.detCantidad = detCantidad;
    }

    public BigDecimal getDetPvpUniSinIvaSinDsto() {
        return detPvpUniSinIvaSinDsto;
    }

    public void setDetPvpUniSinIvaSinDsto(BigDecimal detPvpUniSinIvaSinDsto) {
        this.detPvpUniSinIvaSinDsto = detPvpUniSinIvaSinDsto;
    }

    public BigDecimal getDetPvpUniConIvaSinDsto() {
        return detPvpUniConIvaSinDsto;
    }

    public void setDetPvpUniConIvaSinDsto(BigDecimal detPvpUniConIvaSinDsto) {
        this.detPvpUniConIvaSinDsto = detPvpUniConIvaSinDsto;
    }

    public BigDecimal getDetPvpTotalSinIvaConDsto() {
        return detPvpTotalSinIvaConDsto;
    }

    public void setDetPvpTotalSinIvaConDsto(BigDecimal detPvpTotalSinIvaConDsto) {
        this.detPvpTotalSinIvaConDsto = detPvpTotalSinIvaConDsto;
    }

    public BigDecimal getDetPvpTotalConIvaConDsto() {
        return detPvpTotalConIvaConDsto;
    }

    public void setDetPvpTotalConIvaConDsto(BigDecimal detPvpTotalConIvaConDsto) {
        this.detPvpTotalConIvaConDsto = detPvpTotalConIvaConDsto;
    }

    public BigDecimal getDetPrecioReal() {
        return detPrecioReal;
    }

    public void setDetPrecioReal(BigDecimal detPrecioReal) {
        this.detPrecioReal = detPrecioReal;
    }

    public BigDecimal getDetPorceDsto() {
        return detPorceDsto;
    }

    public void setDetPorceDsto(BigDecimal detPorceDsto) {
        this.detPorceDsto = detPorceDsto;
    }

    public String getDetProcesado() {
        return detProcesado;
    }

    public void setDetProcesado(String detProcesado) {
        this.detProcesado = detProcesado;
    }

    public CabPrefactura getCabPrefactura() {
        return cabPrefactura;
    }

    public void setCabPrefactura(CabPrefactura cabPrefactura) {
        this.cabPrefactura = cabPrefactura;
    }

    public Boolean getDetIsGarantiaExtendida() {
        if(detIsGarantiaExtendida==null){
            return Boolean.FALSE;
        }else{
            return detIsGarantiaExtendida;
        }
    }

    public void setDetIsGarantiaExtendida(Boolean detIsGarantiaExtendida) {
        this.detIsGarantiaExtendida = detIsGarantiaExtendida;
    }

    public Long getDetLineaReferenciaEgo() {
        return detLineaReferenciaEgo;
    }

    public void setDetLineaReferenciaEgo(Long detLineaReferenciaEgo) {
        this.detLineaReferenciaEgo = detLineaReferenciaEgo;
    }

    public String getUidFacturaReferenciaEgo() {
        return uidFacturaReferenciaEgo;
    }

    public void setUidFacturaReferenciaEgo(String uidFacturaReferenciaEgo) {
        this.uidFacturaReferenciaEgo = uidFacturaReferenciaEgo;
    }

    public Boolean getDetIsGarantiaExtendidaGratis() {
        
        if(detIsGarantiaExtendidaGratis==null){
            return Boolean.FALSE;
        }else{
            return detIsGarantiaExtendidaGratis;
        }

    }

    public void setDetIsGarantiaExtendidaGratis(Boolean detIsGarantiaExtendidaGratis) {
        this.detIsGarantiaExtendidaGratis = detIsGarantiaExtendidaGratis;
    }

    public String getDetAnulado() {
        return detAnulado;
    }

    public void setDetAnulado(String detAnulado) {
        this.detAnulado = detAnulado;
    }

    public String getDetCodImp() {
        return detCodImp;
    }

    public void setDetCodImp(String detCodImp) {
        this.detCodImp = detCodImp;
    }
    
}
