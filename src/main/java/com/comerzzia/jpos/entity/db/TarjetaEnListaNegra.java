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
@Table(name = "d_lista_negra_tarjeta_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarjetaEnListaNegra.findAll", query = "SELECT t FROM TarjetaEnListaNegra t"),
    @NamedQuery(name = "TarjetaEnListaNegra.findById", query = "SELECT t FROM TarjetaEnListaNegra t WHERE t.id = :id"),
    @NamedQuery(name = "TarjetaEnListaNegra.findByNumeroTarjeta", query = "SELECT t FROM TarjetaEnListaNegra t WHERE t.numeroTarjeta = :numeroTarjeta"),
    @NamedQuery(name = "TarjetaEnListaNegra.findByFechaAlta", query = "SELECT t FROM TarjetaEnListaNegra t WHERE t.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "TarjetaEnListaNegra.findByActivo", query = "SELECT t FROM TarjetaEnListaNegra t WHERE t.activo = :activo")})
public class TarjetaEnListaNegra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NUMERO_TARJETA")
    private long numeroTarjeta;
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "ACTIVO")
    private Character activo;

    public TarjetaEnListaNegra() {
    }

    public TarjetaEnListaNegra(Integer id) {
        this.id = id;
    }

    public TarjetaEnListaNegra(Integer id, long numeroTarjeta) {
        this.id = id;
        this.numeroTarjeta = numeroTarjeta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(long numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Character getActivo() {
        return activo;
    }

    public void setActivo(Character activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TarjetaEnListaNegra)) {
            return false;
        }
        TarjetaEnListaNegra other = (TarjetaEnListaNegra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.tarjetaEnListaNegra[ id=" + id + " ]";
    }
    
}
