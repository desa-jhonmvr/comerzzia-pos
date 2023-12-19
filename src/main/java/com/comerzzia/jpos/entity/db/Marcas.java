/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "X_MARCAS_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Marcas.findAll", query = "SELECT m FROM Marcas m"),
    @NamedQuery(name = "Marcas.findByCodmarca", query = "SELECT m FROM Marcas m WHERE m.codmarca = :codmarca"),
    @NamedQuery(name = "Marcas.findByDesmarca", query = "SELECT m FROM Marcas m WHERE m.desmarca = :desmarca")})
public class Marcas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODMARCA")
    private String codmarca;
    @Basic(optional = false)
    @Column(name = "DESMARCA")
    private String desmarca;

    @OneToMany(mappedBy = "codmarca", fetch = FetchType.LAZY)
    private Collection<Articulos> articulosCollection;
    public Marcas() {
    }

    public Marcas(String codmarca) {
        this.codmarca = codmarca;
    }

    public Marcas(String codmarca, String desmarca) {
        this.codmarca = codmarca;
        this.desmarca = desmarca;
    }

        public String getCodmarca() {
        return codmarca;
    }

    public void setCodmarca(String codmarca) {
        this.codmarca = codmarca;
    }

    public String getDesmarca() {
        return desmarca;
    }

    public void setDesmarca(String desmarca) {
        this.desmarca = desmarca;
    }
    
    @XmlTransient
    public Collection<Articulos> getDArticulosTblCollection() {
        return articulosCollection;
    }

    public void setDArticulosTblCollection(Collection<Articulos> articulosCollection) {
        this.articulosCollection = articulosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codmarca != null ? codmarca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Marcas)) {
            return false;
        }
        Marcas other = (Marcas) object;
        if ((this.codmarca == null && other.codmarca != null) || (this.codmarca != null && !this.codmarca.equals(other.codmarca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Marcas[ codmarca=" + codmarca + " ]";
    }
    
}
