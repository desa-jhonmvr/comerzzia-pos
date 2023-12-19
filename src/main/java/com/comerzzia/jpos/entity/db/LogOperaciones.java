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
@Table(name = "x_log_operaciones_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogOperaciones.findAll", query = "SELECT l FROM LogOperaciones l"),
    @NamedQuery(name = "LogOperaciones.findByCodAlm", query = "SELECT l FROM LogOperaciones l WHERE l.logOperacionesPK.codAlm = :codAlm"),
    @NamedQuery(name = "LogOperaciones.findByCodCaja", query = "SELECT l FROM LogOperaciones l WHERE l.logOperacionesPK.codCaja = :codCaja"),
    @NamedQuery(name = "LogOperaciones.findByUid", query = "SELECT l FROM LogOperaciones l WHERE l.logOperacionesPK.uid = :uid"),
    @NamedQuery(name = "LogOperaciones.findByFechaHora", query = "SELECT l FROM LogOperaciones l WHERE l.fechaHora = :fechaHora"),
    @NamedQuery(name = "LogOperaciones.findByCodOperacion", query = "SELECT l FROM LogOperaciones l WHERE l.codOperacion = :codOperacion"),
    @NamedQuery(name = "LogOperaciones.findByUsuario", query = "SELECT l FROM LogOperaciones l WHERE l.usuario = :usuario"),
    @NamedQuery(name = "LogOperaciones.findByAutorizador", query = "SELECT l FROM LogOperaciones l WHERE l.autorizador = :autorizador"),
    @NamedQuery(name = "LogOperaciones.findByReferencia", query = "SELECT l FROM LogOperaciones l WHERE l.referencia = :referencia"),
    @NamedQuery(name = "LogOperaciones.findByObservaciones", query = "SELECT l FROM LogOperaciones l WHERE l.observaciones = :observaciones"),
    @NamedQuery(name = "LogOperaciones.findByProcesado", query = "SELECT l FROM LogOperaciones l WHERE l.procesado = :procesado")})
public class LogOperaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LogOperacionesPK logOperacionesPK;
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
    @Basic(optional = false)
    @Column(name = "id_accion")
    private Long idAccion;
    @Basic(optional = false)
    @Column(name = "id_operacion")    
    private Byte idOperacion;
    @Basic(optional = false)
    @Column(name = "envio_correo")
    private char envioCorreo;
    public LogOperaciones() {
    }

    public LogOperaciones(LogOperacionesPK logOperacionesPK) {
        this.logOperacionesPK = logOperacionesPK;
    }

    public LogOperaciones(LogOperacionesPK logOperacionesPK, Date fechaHora, String codOperacion, char procesado) {
        this.logOperacionesPK = logOperacionesPK;
        this.fechaHora = fechaHora;
        this.codOperacion = codOperacion;
        this.procesado = procesado;
    }

    public LogOperaciones(String codAlm, String codCaja, String idLog) {
        this.logOperacionesPK = new LogOperacionesPK(codAlm, codCaja, idLog);
    }
    
    public LogOperaciones(String codAlm, String codCaja) {
        
        this.logOperacionesPK = new LogOperacionesPK(codAlm, codCaja,UUID.randomUUID().toString());
    }

    public LogOperacionesPK getLogOperacionesPK() {
        return logOperacionesPK;
    }

    public void setLogOperacionesPK(LogOperacionesPK logOperacionesPK) {
        this.logOperacionesPK = logOperacionesPK;
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

    public Long getIdAccion() {
        return idAccion;
    }

    public void setIdAccion(Long idAccion) {
        this.idAccion = idAccion;
    }

    public Byte getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(Byte idOperacion) {
        this.idOperacion = idOperacion;
    }

    public char getEnvioCorreo() {
        return envioCorreo;
    }

    public void setEnvioCorreo(char envioCorreo) {
        this.envioCorreo = envioCorreo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logOperacionesPK != null ? logOperacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogOperaciones)) {
            return false;
        }
        LogOperaciones other = (LogOperaciones) object;
        if ((this.logOperacionesPK == null && other.logOperacionesPK != null) || (this.logOperacionesPK != null && !this.logOperacionesPK.equals(other.logOperacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogOperaciones[ logOperacionesPK=" + logOperacionesPK + " ]";
    }
 
}
