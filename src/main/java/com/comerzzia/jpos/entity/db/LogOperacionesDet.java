/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "x_log_operaciones_det_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogOperacionesDet.findAll", query = "SELECT l FROM LogOperacionesDet l")})
//        ,
//    @NamedQuery(name = "LogOperacionesDet.findByCodAlm", query = "SELECT l FROM LogOperacionesDet l WHERE l.logOperacionesPK.codAlm = :codAlm"),
//    @NamedQuery(name = "LogOperacionesDet.findByCodCaja", query = "SELECT l FROM LogOperacionesDet l WHERE l.logOperacionesPK.codCaja = :codCaja"),
//    @NamedQuery(name = "LogOperacionesDet.findByUid", query = "SELECT l FROM LogOperacionesDet l WHERE l.logOperacionesPK.uid = :uid"),
//    @NamedQuery(name = "LogOperacionesDet.findByFechaHora", query = "SELECT l FROM LogOperacionesDet l WHERE l.fechaHora = :fechaHora"),
//    @NamedQuery(name = "LogOperacionesDet.findByCodOperacion", query = "SELECT l FROM LogOperacionesDet l WHERE l.codOperacion = :codOperacion"),
//    @NamedQuery(name = "LogOperacionesDet.findByUsuario", query = "SELECT l FROM LogOperacionesDet l WHERE l.usuario = :usuario"),
//    @NamedQuery(name = "LogOperacionesDet.findByAutorizador", query = "SELECT l FROM LogOperacionesDet l WHERE l.autorizador = :autorizador"),
//    @NamedQuery(name = "LogOperacionesDet.findByReferencia", query = "SELECT l FROM LogOperacionesDet l WHERE l.referencia = :referencia"),
//    @NamedQuery(name = "LogOperacionesDet.findByObservaciones", query = "SELECT l FROM LogOperacionesDet l WHERE l.observaciones = :observaciones"),
//    @NamedQuery(name = "LogOperacionesDet.findByProcesado", query = "SELECT l FROM LogOperacionesDet l WHERE l.procesado = :procesado")})
public class LogOperacionesDet implements Serializable {

    private static final long serialVersionUID = 1L;
//    @EmbeddedId
//    protected LogOperacionesPK logOperacionesPK;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "UID_LOG_DET")
//    private String uidLogDet;
    @EmbeddedId
    protected LogOperacionesDetPK logOperacionesDetPK;
    @Column(name = "UID_LOG")
    private String uidLog;
//    @Column(name = "UID_LOG")
////    private String uidLog;
    @Column(name = "UID_TICKET")
    private String uidTicket;
//    @JoinColumn(name = "UID_TICKET", referencedColumnName = "UID_TICKET")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private TicketsAlm uidTicket;
    @Column(name = "autorizado")
    private String autorizado;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "CODART")
    private String codart;
//    @JoinColumn(name = "CODART", referencedColumnName = "CODART")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Articulos codart;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Column(name = "MOTIVO_DESCUENTO")
    private String motivoDescuento;
    @Basic(optional = false)
    @Column(name = "PRECIO_REAL")
    private BigDecimal precioReal;
    @Basic(optional = false)
    @Column(name = "PRECIO_TOTAL_ORIGEN")
    private BigDecimal precioTotalOrigen;
    @Basic(optional = false)
    @Column(name = "DESCUENTO_ORIGINAL")
    private BigDecimal descuentoOriginal;
    @Basic(optional = false)
    @Column(name = "DESCUENTO_MODIFICADO")
    private BigDecimal descuentoModificado;

    public LogOperacionesDet() {

        this.logOperacionesDetPK = new LogOperacionesDetPK(UUID.randomUUID().toString());
    }
//    
//    @Override
//    public String toString() {
//        return "com.comerzzia.jpos.entity.db.LogOperaciones[ logOperacionesPK=" + logOperacionesPK + " ]";
//    }

    public String getUidLog() {
        return uidLog;
    }

    public void setUidLog(String uidLog) {
        this.uidLog = uidLog;
    }
//
//    public TicketsAlm getUidTicket() {
//        return uidTicket;
//    }
//
//    public void setUidTicket(TicketsAlm uidTicket) {
//        this.uidTicket = uidTicket;
//    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

//    public TicketsAlm getUidTicket() {
//        return uidTicket;
//    }
//
//    public void setUidTicket(TicketsAlm uidTicket) {
//        this.uidTicket = uidTicket;
//    }
    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//
//    public Articulos getCodart() {
//        return codart;
//    }
//
//    public void setCodart(Articulos codart) {
//        this.codart = codart;
//    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }

    public void setMotivoDescuento(String motivoDescuento) {
        this.motivoDescuento = motivoDescuento;
    }

    public BigDecimal getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(BigDecimal precioReal) {
        this.precioReal = precioReal;
    }

    public BigDecimal getPrecioTotalOrigen() {
        return precioTotalOrigen;
    }

    public void setPrecioTotalOrigen(BigDecimal precioTotalOrigen) {
        this.precioTotalOrigen = precioTotalOrigen;
    }

    public BigDecimal getDescuentoOriginal() {
        return descuentoOriginal;
    }

    public void setDescuentoOriginal(BigDecimal descuentoOriginal) {
        this.descuentoOriginal = descuentoOriginal;
    }

    public BigDecimal getDescuentoModificado() {
        return descuentoModificado;
    }

    public void setDescuentoModificado(BigDecimal descuentoModificado) {
        this.descuentoModificado = descuentoModificado;
    }

}
