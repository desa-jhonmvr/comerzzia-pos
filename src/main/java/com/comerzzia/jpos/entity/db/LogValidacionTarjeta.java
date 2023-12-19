/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "x_log_validaciones_tarjet_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogValidacionTarjeta.findAll", query = "SELECT l FROM LogValidacionTarjeta l"),
    @NamedQuery(name = "LogValidacionTarjeta.findById", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.id = :id"),
    @NamedQuery(name = "LogValidacionTarjeta.findByTrama", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.trama = :trama"),
    @NamedQuery(name = "LogValidacionTarjeta.findByTarjeta", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.tarjeta = :tarjeta"),
    @NamedQuery(name = "LogValidacionTarjeta.findByFechaEnvio", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "LogValidacionTarjeta.findByCaja", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.caja = :caja"),
    @NamedQuery(name = "LogValidacionTarjeta.findByTienda", query = "SELECT l FROM LogValidacionTarjeta l WHERE l.tienda = :tienda")})
public class LogValidacionTarjeta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "trama")
    private String trama;
    @Column(name = "tarjeta")
    private String tarjeta;
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Column(name = "caja")
    private String caja;
    @Column(name = "tienda")
    private String tienda;
    @Column(name = "tipo")
    private Character tipo;

    public LogValidacionTarjeta() {
    }

    public LogValidacionTarjeta(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogValidacionTarjeta)) {
            return false;
        }
        LogValidacionTarjeta other = (LogValidacionTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogValidacionTarjeta[ id=" + id + " ]";
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }
    
}
