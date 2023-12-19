/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "D_AUTORIZACION_CREDITO_TBL")
public class AutorizacionConsumoCredito implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "UID_AUTORIZACION")
    private String uidAutorizacion;

    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
   
    @Column(name = "CODCLI")
    private String codCli;

    @Column(name = "UID_ID_DISPOSITIVO")
    private String uidIdDispositivo;

    @Column(name = "UID_TICKET")
    private String uidTicket;

    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codAlm;

    @Basic(optional = false)
    @Column(name = "NOMBRE_ALMACEN")
    private String nombreAlmacen;

    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codCaja;

    @Basic(optional = false)
    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Basic(optional = false)
    @Column(name = "ID_CREDITO")
    private Long idCredito;

    @Basic(optional = false)
    @Column(name = "NUM_PLASTICO")
    private String numPlastico;

    @Basic(optional = false)
    @Column(name = "VALOR_SOLICITADO")
    private BigDecimal valorSolicitado;
    
    @Column(name = "CUOTAS")
    private Integer cuotas;

    @Basic(optional = false)
    @Column(name = "FECHA_APROBACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;

    @Basic(optional = false)
    @Column(name = "COD_USUARIO_APROBACION")
    private String codUsuarioAprobacion;

    @Basic(optional = false)
    @Column(name = "COD_ESTADO_APROBACION")
    private String codEstadoAprobacion;
    
    @Basic(optional = false)
    @Column(name = "NUMERO_AUTORIZACION")
    private String numAutorizacion;

    @Basic(optional = false)
    @Column(name = "DIRECCION_IP")
    private String direccionIp;

    /* Getters and Setters */

    public String getUidAutorizacion() {
        return uidAutorizacion;
    }

    public void setUidAutorizacion(String uidAutorizacion) {
        this.uidAutorizacion = uidAutorizacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCodCli() {
        return codCli;
    }

    public void setCodCli(String codCli) {
        this.codCli = codCli;
    }

    public String getUidIdDispositivo() {
        return uidIdDispositivo;
    }

    public void setUidIdDispositivo(String uidIdDispositivo) {
        this.uidIdDispositivo = uidIdDispositivo;
    }
    
    

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Long getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Long idCredito) {
        this.idCredito = idCredito;
    }

    public String getNumPlastico() {
        return numPlastico;
    }

    public void setNumPlastico(String numPlastico) {
        this.numPlastico = numPlastico;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getCodUsuarioAprobacion() {
        return codUsuarioAprobacion;
    }

    public void setCodUsuarioAprobacion(String codUsuarioAprobacion) {
        this.codUsuarioAprobacion = codUsuarioAprobacion;
    }

    public String getCodEstadoAprobacion() {
        return codEstadoAprobacion;
    }

    public void setCodEstadoAprobacion(String codEstadoAprobacion) {
        this.codEstadoAprobacion = codEstadoAprobacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

}
