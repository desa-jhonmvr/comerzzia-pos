/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_TARIFAS_CAB_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarifaId.findAll", query = "SELECT t FROM TarifaId t"),
    @NamedQuery(name = "TarifaId.findByCodtar", query = "SELECT t FROM TarifaId t WHERE t.codtar = :codtar"),
    @NamedQuery(name = "TarifaId.findByDestar", query = "SELECT t FROM TarifaId t WHERE t.destar = :destar"),
    @NamedQuery(name = "TarifaId.findByVersion", query = "SELECT t FROM TarifaId t WHERE t.version = :version"),
    @NamedQuery(name = "TarifaId.findByFechaVersion", query = "SELECT t FROM TarifaId t WHERE t.fechaVersion = :fechaVersion")})
public class TarifaId implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODTAR")
    private String codtar;
    @Basic(optional = false)
    @Column(name = "DESTAR")
    private String destar;
    @Basic(optional = false)
    @Column(name = "VERSION")
    private long version;
    @Column(name = "FECHA_VERSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVersion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarifaId", fetch = FetchType.LAZY)
    private Collection<Tarifas> tarifasCollection;

    public TarifaId() {
    }

    public TarifaId(String codtar) {
        this.codtar = codtar;
    }

    public TarifaId(String codtar, String destar, long version) {
        this.codtar = codtar;
        this.destar = destar;
        this.version = version;
    }

    public String getCodtar() {
        return codtar;
    }

    public void setCodtar(String codtar) {
        this.codtar = codtar;
    }

    public String getDestar() {
        return destar;
    }

    public void setDestar(String destar) {
        this.destar = destar;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getFechaVersion() {
        return fechaVersion;
    }

    public void setFechaVersion(Date fechaVersion) {
        this.fechaVersion = fechaVersion;
    }

    @XmlTransient
    public Collection<Tarifas> getTarifasCollection() {
        return tarifasCollection;
    }

    public void setTarifasCollection(Collection<Tarifas> tarifasCollection) {
        this.tarifasCollection = tarifasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtar != null ? codtar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TarifaId)) {
            return false;
        }
        TarifaId other = (TarifaId) object;
        if ((this.codtar == null && other.codtar != null) || (this.codtar != null && !this.codtar.equals(other.codtar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.TarifaId[ codtar=" + codtar + " ]";
    }
    
}
