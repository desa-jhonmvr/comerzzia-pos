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
@Table(name = "x_log_consultas_articulos_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogConsultaArticulo.findAll", query = "SELECT l FROM LogConsultaArticulo l"),
    @NamedQuery(name = "LogConsultaArticulo.findByCodAlm", query = "SELECT l FROM LogConsultaArticulo l WHERE l.logConsultaArticuloPK.codAlm = :codAlm"),
    @NamedQuery(name = "LogConsultaArticulo.findByCodCaja", query = "SELECT l FROM LogConsultaArticulo l WHERE l.logConsultaArticuloPK.codCaja = :codCaja"),
    @NamedQuery(name = "LogConsultaArticulo.findByUid", query = "SELECT l FROM LogConsultaArticulo l WHERE l.logConsultaArticuloPK.uid = :uid"),
    @NamedQuery(name = "LogConsultaArticulo.findByFechaHora", query = "SELECT l FROM LogConsultaArticulo l WHERE l.fechaHora = :fechaHora"),
    @NamedQuery(name = "LogConsultaArticulo.findByCodOperacion", query = "SELECT l FROM LogConsultaArticulo l WHERE l.codOperacion = :codOperacion"),
    @NamedQuery(name = "LogConsultaArticulo.findByUsuario", query = "SELECT l FROM LogConsultaArticulo l WHERE l.usuario = :usuario"),
    @NamedQuery(name = "LogConsultaArticulo.findByAutorizador", query = "SELECT l FROM LogConsultaArticulo l WHERE l.autorizador = :autorizador"),
    @NamedQuery(name = "LogConsultaArticulo.findByReferencia", query = "SELECT l FROM LogConsultaArticulo l WHERE l.referencia = :referencia"),
    @NamedQuery(name = "LogConsultaArticulo.findByObservaciones", query = "SELECT l FROM LogConsultaArticulo l WHERE l.observaciones = :observaciones"),
    @NamedQuery(name = "LogConsultaArticulo.findByProcesado", query = "SELECT l FROM LogConsultaArticulo l WHERE l.procesado = :procesado")})
public class LogConsultaArticulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LogConsultaArticuloPK logConsultaArticuloPK;
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

    public LogConsultaArticulo() {
    }

    public LogConsultaArticulo(LogConsultaArticuloPK logConsultaArticuloPK) {
        this.logConsultaArticuloPK = logConsultaArticuloPK;
    }

    public LogConsultaArticulo(LogConsultaArticuloPK logConsultaArticuloPK, Date fechaHora, String codOperacion, char procesado) {
        this.logConsultaArticuloPK = logConsultaArticuloPK;
        this.fechaHora = fechaHora;
        this.codOperacion = codOperacion;
        this.procesado = procesado;
    }

    public LogConsultaArticulo(String codAlm, String codCaja, String idLog) {
        this.logConsultaArticuloPK = new LogConsultaArticuloPK(codAlm, codCaja, idLog);
    }
    
    public LogConsultaArticulo(String codAlm, String codCaja) {
        
        this.logConsultaArticuloPK = new LogConsultaArticuloPK(codAlm, codCaja,UUID.randomUUID().toString());
    }

    public LogConsultaArticuloPK getLogConsultaArticuloPK() {
        return logConsultaArticuloPK;
    }

    public void setLogConsultaArticuloPK(LogConsultaArticuloPK logConsultaArticuloPK) {
        this.logConsultaArticuloPK = logConsultaArticuloPK;
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
        hash += (logConsultaArticuloPK != null ? logConsultaArticuloPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogConsultaArticulo)) {
            return false;
        }
        LogConsultaArticulo other = (LogConsultaArticulo) object;
        if ((this.logConsultaArticuloPK == null && other.logConsultaArticuloPK != null) || (this.logConsultaArticuloPK != null && !this.logConsultaArticuloPK.equals(other.logConsultaArticuloPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogConsultaArticulo[ logConsultaArticuloPK=" + logConsultaArticuloPK + " ]";
    }
 
}
