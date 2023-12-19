/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_REGIONES_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Region.findAll", query = "SELECT r FROM Region r"),
    @NamedQuery(name = "Region.findByCodregion", query = "SELECT r FROM Region r WHERE r.codregion = :codregion"),
    @NamedQuery(name = "Region.findByDesregion", query = "SELECT r FROM Region r WHERE r.desregion = :desregion")})
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODREGION")
    private Long codregion;
    @Basic(optional = false)
    @Column(name = "DESREGION")
    private String desregion;

    public Region() {
    }

    public Region(Long codregion) {
        this.codregion = codregion;
    }

    public Region(Long codregion, String desregion) {
        this.codregion = codregion;
        this.desregion = desregion;
    }

    public Long getCodregion() {
        return codregion;
    }

    public void setCodregion(Long codregion) {
        this.codregion = codregion;
    }

    public String getDesregion() {
        return desregion;
    }

    public void setDesregion(String desregion) {
        this.desregion = desregion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codregion != null ? codregion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Region)) {
            return false;
        }
        Region other = (Region) object;
        if ((this.codregion == null && other.codregion != null) || (this.codregion != null && !this.codregion.equals(other.codregion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Region[ codregion=" + codregion + " ]";
    }
    
}
