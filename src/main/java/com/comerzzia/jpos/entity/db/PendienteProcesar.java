/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "D_PENDIENTE_PROCESAR")
public class PendienteProcesar implements Serializable {

    private static final long serialVersionUID = -3462620327039532297L;

    @Id

    @SequenceGenerator(name = "SQ_PENDIENTE_PROCESAR", sequenceName = "SQ_PENDIENTE_PROCESAR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ")
    @Column(name = "PENDIENTE_PROCESAR_ID")
    private Long pendienteProcesarId;

    @JoinColumn(name = "UID_TICKET", referencedColumnName = "UID_TICKET")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TicketsAlm ticketsAlm;

    @Column(name = "ID_LINEA")
    private Long idLinea;

    @Column(name = "NUM_FACTURA")
    private String numFactura;

    @Column(name = "CODART")
    private String codArt;

    @Column(name = "PROCESADO")
    private String procesado;

    @Column(name = "ERROR_PROCESO")
    private String errorProceso;

    @Column(name = "ID_USU_CREACION")
    private Long idUsuarioCreacion;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "ID_USU_APROBACION")
    private Long idUsuarioAprobacion;

    @Column(name = "FECHA_APROBACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    
    @Column(name = "UID_NOTA_CREDITO")
    private String uidNotaCredito;

    public Long getPendienteProcesarId() {
        return pendienteProcesarId;
    }

    public void setPendienteProcesarId(Long pendienteProcesarId) {
        this.pendienteProcesarId = pendienteProcesarId;
    }

    public TicketsAlm getTicketsAlm() {
        return ticketsAlm;
    }

    public void setTicketsAlm(TicketsAlm ticketsAlm) {
        this.ticketsAlm = ticketsAlm;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codArt) {
        this.codArt = codArt;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getErrorProceso() {
        return errorProceso;
    }

    public void setErrorProceso(String errorProceso) {
        this.errorProceso = errorProceso;
    }

    public Long getIdUsuarioCreacion() {
        return idUsuarioCreacion;
    }

    public void setIdUsuarioCreacion(Long idUsuarioCreacion) {
        this.idUsuarioCreacion = idUsuarioCreacion;
    }

    public Long getIdUsuarioAprobacion() {
        return idUsuarioAprobacion;
    }

    public void setIdUsuarioAprobacion(Long idUsuarioAprobacion) {
        this.idUsuarioAprobacion = idUsuarioAprobacion;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }
    
    

}
