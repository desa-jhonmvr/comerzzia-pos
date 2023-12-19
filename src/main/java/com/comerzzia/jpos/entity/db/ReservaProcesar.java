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
@Table(name = "D_RESERVA_PROCESAR")
public class ReservaProcesar implements Serializable {

    private static final long serialVersionUID = -498619603650805875L;

    @Id
    @SequenceGenerator(name = "SQ_RESERVA_PROCESAR", sequenceName = "SQ_RESERVA_PROCESAR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ")
    @Column(name = "RESERVA_PROCESAR_ID")
    private Long reservaProcesarId;

    @Column(name = "UID_RESERVACION")
    private String uidReservacion;

    @Column(name = "NUM_RESERVA")
    private String numReserva;

    @JoinColumn(name = "CODCLI", referencedColumnName = "CODCLI")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;

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

    @Column(name = "ID_BONO")
    private Long idBono;

    public Long getReservaProcesarId() {
        return reservaProcesarId;
    }

    public void setReservaProcesarId(Long reservaProcesarId) {
        this.reservaProcesarId = reservaProcesarId;
    }

    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion;
    }

    public String getNumReserva() {
        return numReserva;
    }

    public void setNumReserva(String numReserva) {
        this.numReserva = numReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public Long getIdBono() {
        return idBono;
    }

    public void setIdBono(Long idBono) {
        this.idBono = idBono;
    }

}
