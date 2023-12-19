/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumEstadoPagoPrefactura;
import com.comerzzia.jpos.util.enums.EnumEstadoPrefactura;
import com.comerzzia.jpos.util.enums.EnumEstadoTrazabilidad;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "PW_CAB_PREFACTURA_TBL")
public class CabPrefactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "UID_CAB_ID")
    private String uidCabId;

    @Column(name = "CAB_ID_PEDIDO")
    private String cabIdPedido;

    @Column(name = "CAB_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cabFecha;

    @Column(name = "CAB_CODCLI")
    private String cabCodCli;
    
    @Column(name = "CAB_FACT_IDENTIFICACION")
    private String cabFactIdentificacion;
    
    @Column(name = "CAB_TIPO_DOC")
    private String cabTipoDoc;

    @Column(name = "CAB_NOMBRE")
    private String cabNombre;

    @Column(name = "CAB_APELLIDO")
    private String cabApellido;

    @Column(name = "CAB_EMAIL")
    private String cabEmail;

    @Column(name = "CAB_TELEFONO")
    private String cabTelefono;
    
    @Column(name = "CAB_DIRECCION")
    private String cabDireccion;

    @Column(name = "CAB_CODALM")
    private String cabCodAlm;

    @Column(name = "CAB_TOTAL_CON_DSTO_CON_IVA")
    private BigDecimal cabTotalConDstoConIva;

    @Column(name = "CAB_TOTAL_CON_DSTO_SIN_IVA")
    private BigDecimal cabTotalConDstoSinIva;

    @Column(name = "CAB_TOTAL_SIN_DSTO_CON_IVA")
    private BigDecimal cabTotalSinDstoConIva;

    @Column(name = "CAB_TOTAL_SIN_DSTO_SIN_IVA")
    private BigDecimal cabTotalSinDstoSinIva;

    @Column(name = "CAB_PROCESADO")
    private String cabProcesado;

    @Column(name = "CAB_ANULADO")
    private String cabAnulado;

    @Column(name = "CAB_MOTIVO_ANULADO")
    private String cabMotivoAnulado;

    @Column(name = "CAB_AUTORIZA_ANULADO")
    private Long cabAutorizaAnulado;

    @Column(name = "CAB_FECHA_ANULADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cabFechaAnulado;

    @Column(name = "CAB_ESTADO")
    private EnumEstadoPrefactura cabEstado;

    @Column(name = "UID_TICKET")
    private String uidTicket;

    @Column(name = "CAB_OBSERVACION")
    private String cabObservacion;

    @Column(name = "CAB_AUTORIZADOR")
    private String cabAutorizador;

    @Column(name = "CAB_ESTADO_PAGO")
    private EnumEstadoPagoPrefactura cabEstadoPago;
    
    @Column(name = "CAB_ESTADO_TRAZABILIDAD")
    private EnumEstadoTrazabilidad cabEstadoTrazabilidad;
    
    @Column(name = "CAB_DESCUENTO_FORMA_PAGO")
    private BigDecimal cabDescuentoFormaPago;
    
    @Column(name = "CAB_TOTAL_PVP_CON_PROMOCION")
    private BigDecimal caTotalPvpConPromocion;
    
    /*Getters y Setters*/
    public String getUidCabId() {
        return uidCabId;
    }

    public void setUidCabId(String uidCabId) {
        this.uidCabId = uidCabId;
    }

    public String getCabIdPedido() {
        return cabIdPedido;
    }

    public void setCabIdPedido(String cabIdPedido) {
        this.cabIdPedido = cabIdPedido;
    }

    public Date getCabFecha() {
        return cabFecha;
    }

    public void setCabFecha(Date cabFecha) {
        this.cabFecha = cabFecha;
    }

    public String getCabCodCli() {
        return cabCodCli;
    }

    public void setCabCodCli(String cabCodCli) {
        this.cabCodCli = cabCodCli;
    }

    public String getCabNombre() {
        return cabNombre;
    }

    public void setCabNombre(String cabNombre) {
        this.cabNombre = cabNombre;
    }

    public String getCabApellido() {
        return cabApellido;
    }

    public void setCabApellido(String cabApellido) {
        this.cabApellido = cabApellido;
    }

    public String getCabEmail() {
        return cabEmail;
    }

    public void setCabEmail(String cabEmail) {
        this.cabEmail = cabEmail;
    }

    public String getCabTelefono() {
        return cabTelefono;
    }

    public void setCabTelefono(String cabTelefono) {
        this.cabTelefono = cabTelefono;
    }

    public String getCabCodAlm() {
        return cabCodAlm;
    }

    public void setCabCodAlm(String cabCodAlm) {
        this.cabCodAlm = cabCodAlm;
    }

    public BigDecimal getCabTotalConDstoConIva() {
        return cabTotalConDstoConIva;
    }

    public void setCabTotalConDstoConIva(BigDecimal cabTotalConDstoConIva) {
        this.cabTotalConDstoConIva = cabTotalConDstoConIva;
    }

    public BigDecimal getCabTotalConDstoSinIva() {
        return cabTotalConDstoSinIva;
    }

    public void setCabTotalConDstoSinIva(BigDecimal cabTotalConDstoSinIva) {
        this.cabTotalConDstoSinIva = cabTotalConDstoSinIva;
    }

    public BigDecimal getCabTotalSinDstoConIva() {
        return cabTotalSinDstoConIva;
    }

    public void setCabTotalSinDstoConIva(BigDecimal cabTotalSinDstoConIva) {
        this.cabTotalSinDstoConIva = cabTotalSinDstoConIva;
    }

    public BigDecimal getCabTotalSinDstoSinIva() {
        return cabTotalSinDstoSinIva;
    }

    public void setCabTotalSinDstoSinIva(BigDecimal cabTotalSinDstoSinIva) {
        this.cabTotalSinDstoSinIva = cabTotalSinDstoSinIva;
    }

    public String getCabProcesado() {
        return cabProcesado;
    }

    public void setCabProcesado(String cabProcesado) {
        this.cabProcesado = cabProcesado;
    }

    public String getCabAnulado() {
        return cabAnulado;
    }

    public void setCabAnulado(String cabAnulado) {
        this.cabAnulado = cabAnulado;
    }

    public String getCabMotivoAnulado() {
        return cabMotivoAnulado;
    }

    public void setCabMotivoAnulado(String cabMotivoAnulado) {
        this.cabMotivoAnulado = cabMotivoAnulado;
    }

    public Long getCabAutorizaAnulado() {
        return cabAutorizaAnulado;
    }

    public void setCabAutorizaAnulado(Long cabAutorizaAnulado) {
        this.cabAutorizaAnulado = cabAutorizaAnulado;
    }

    public Date getCabFechaAnulado() {
        return cabFechaAnulado;
    }

    public void setCabFechaAnulado(Date cabFechaAnulado) {
        this.cabFechaAnulado = cabFechaAnulado;
    }

    public EnumEstadoPrefactura getCabEstado() {
        return cabEstado;
    }

    public void setCabEstado(EnumEstadoPrefactura cabEstado) {
        this.cabEstado = cabEstado;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getCabObservacion() {
        return cabObservacion;
    }

    public void setCabObservacion(String cabObservacion) {
        this.cabObservacion = cabObservacion;
    }

    public String getCabAutorizador() {
        return cabAutorizador;
    }

    public void setCabAutorizador(String cabAutorizador) {
        this.cabAutorizador = cabAutorizador;
    }

    public EnumEstadoPagoPrefactura getCabEstadoPago() {
        return cabEstadoPago;
    }

    public void setCabEstadoPago(EnumEstadoPagoPrefactura cabEstadoPago) {
        this.cabEstadoPago = cabEstadoPago;
    }

    public String getCabFactIdentificacion() {
        return cabFactIdentificacion;
    }

    public void setCabFactIdentificacion(String cabFactIdentificacion) {
        this.cabFactIdentificacion = cabFactIdentificacion;
    }

    public String getCabDireccion() {
        return cabDireccion;
    }

    public void setCabDireccion(String cabDireccion) {
        this.cabDireccion = cabDireccion;
    }

    public String getCabTipoDoc() {
        return cabTipoDoc;
    }

    public void setCabTipoDoc(String cabTipoDoc) {
        this.cabTipoDoc = cabTipoDoc;
    }

    public EnumEstadoTrazabilidad getCabEstadoTrazabilidad() {
        return cabEstadoTrazabilidad;
    }

    public void setCabEstadoTrazabilidad(EnumEstadoTrazabilidad cabEstadoTrazabilidad) {
        this.cabEstadoTrazabilidad = cabEstadoTrazabilidad;
    }

    public BigDecimal getCabDescuentoFormaPago() {
        return cabDescuentoFormaPago;
    }

    public void setCabDescuentoFormaPago(BigDecimal cabDescuentoFormaPago) {
        this.cabDescuentoFormaPago = cabDescuentoFormaPago;
    }

    public BigDecimal getCaTotalPvpConPromocion() {
        return caTotalPvpConPromocion;
    }

    public void setCaTotalPvpConPromocion(BigDecimal caTotalPvpConPromocion) {
        this.caTotalPvpConPromocion = caTotalPvpConPromocion;
    }
    
    
    
}
