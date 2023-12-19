/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "D_CODIGO_OTP_TBL")
public class CodigoOtp implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "UID_CODIGO_OTP")
    private String uidCodigoOtp;

    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codAlm;

    @Basic(optional = false)
    @Column(name = "ID_CREDITO")
    private Long idCredito;

    @Basic(optional = false)
    @Column(name = "OTP")
    private Long otp;

    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;
    
    @Basic(optional = false)
    @Column(name = "NUMERO_TELEFONO")
    private String numeroTelefono;
    
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @JoinColumn(name = "ID_USUARIO_SOLICITA", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuarios usuarioSolicita;

    @Basic(optional = false)
    @Column(name = "FECHA_APROBACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;

    public String getUidCodigoOtp() {
        return uidCodigoOtp;
    }

    public void setUidCodigoOtp(String uidCodigoOtp) {
        this.uidCodigoOtp = uidCodigoOtp;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public Long getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Long idCredito) {
        this.idCredito = idCredito;
    }

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuarios getUsuarioSolicita() {
        return usuarioSolicita;
    }

    public void setUsuarioSolicita(Usuarios usuarioSolicita) {
        this.usuarioSolicita = usuarioSolicita;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

}
