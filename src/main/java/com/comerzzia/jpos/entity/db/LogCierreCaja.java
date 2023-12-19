/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "x_log_cierres_caja_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogCierreCaja.findAll", query = "SELECT l FROM LogCierreCaja l"),
    @NamedQuery(name = "LogCierreCaja.findByUidLog", query = "SELECT l FROM LogCierreCaja l WHERE l.logCierreCajaPK.uidLog = :uidLog"),
    @NamedQuery(name = "LogCierreCaja.findByCodAlm", query = "SELECT l FROM LogCierreCaja l WHERE l.logCierreCajaPK.codAlm = :codAlm"),
    @NamedQuery(name = "LogCierreCaja.findByCodCaja", query = "SELECT l FROM LogCierreCaja l WHERE l.logCierreCajaPK.codCaja = :codCaja"),
    @NamedQuery(name = "LogCierreCaja.findByFechaHora", query = "SELECT l FROM LogCierreCaja l WHERE l.fechaHora = :fechaHora"),
    @NamedQuery(name = "LogCierreCaja.findByCodOperacion", query = "SELECT l FROM LogCierreCaja l WHERE l.codOperacion = :codOperacion"),
    @NamedQuery(name = "LogCierreCaja.findByUsuario", query = "SELECT l FROM LogCierreCaja l WHERE l.usuario = :usuario"),
    @NamedQuery(name = "LogCierreCaja.findByAutorizador", query = "SELECT l FROM LogCierreCaja l WHERE l.autorizador = :autorizador"),
    @NamedQuery(name = "LogCierreCaja.findByReferencia", query = "SELECT l FROM LogCierreCaja l WHERE l.referencia = :referencia"),
    @NamedQuery(name = "LogCierreCaja.findByObservaciones", query = "SELECT l FROM LogCierreCaja l WHERE l.observaciones = :observaciones"),
    @NamedQuery(name = "LogCierreCaja.findByProcesado", query = "SELECT l FROM LogCierreCaja l WHERE l.procesado = :procesado")})
public class LogCierreCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LogCierreCajaPK logCierreCajaPK;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "cod_operacion")
    private String codOperacion;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "autorizador")
    private String autorizador;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "procesado")
    private char procesado;

    public LogCierreCaja() {
    }

    public LogCierreCaja(LogCierreCajaPK logCierreCajaPK) {
        this.logCierreCajaPK = logCierreCajaPK;
    }

    public LogCierreCaja(LogCierreCajaPK logCierreCajaPK, Date fechaHora, String codOperacion, char procesado) {
        this.logCierreCajaPK = logCierreCajaPK;
        this.fechaHora = fechaHora;
        this.codOperacion = codOperacion;
        this.procesado = procesado;
    }

    public LogCierreCaja(String uidLog, String codAlm, String codCaja) {
        this.logCierreCajaPK = new LogCierreCajaPK(uidLog, codAlm, codCaja);
    }
    
    public LogCierreCaja(String codAlm, String codCaja) {
        this.logCierreCajaPK = new LogCierreCajaPK(UUID.randomUUID().toString(),codAlm, codCaja);
    }

    public LogCierreCajaPK getLogCierreCajaPK() {
        return logCierreCajaPK;
    }

    public void setLogCierreCajaPK(LogCierreCajaPK logCierreCajaPK) {
        this.logCierreCajaPK = logCierreCajaPK;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCodOperacion() {
        return codOperacion;
    }

    public void setCodOperacion(String codOperacion) {
        this.codOperacion = codOperacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public char getProcesado() {
        return procesado;
    }

    public void setProcesado(char procesado) {
        this.procesado = procesado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logCierreCajaPK != null ? logCierreCajaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogCierreCaja)) {
            return false;
        }
        LogCierreCaja other = (LogCierreCaja) object;
        if ((this.logCierreCajaPK == null && other.logCierreCajaPK != null) || (this.logCierreCajaPK != null && !this.logCierreCajaPK.equals(other.logCierreCajaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogCierreCaja[ logCierreCajaPK=" + logCierreCajaPK + " ]";
    }
    
}
