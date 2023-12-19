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
@Table(name = "X_FORMATOS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formato.findAll", query = "SELECT f FROM Formato f"),
    @NamedQuery(name = "Formato.findByCodformato", query = "SELECT f FROM Formato f WHERE f.codformato = :codformato"),
    @NamedQuery(name = "Formato.findByDesformato", query = "SELECT f FROM Formato f WHERE f.desformato = :desformato"),
    @NamedQuery(name = "Formato.findByAbreviatura", query = "SELECT f FROM Formato f WHERE f.abreviatura = :abreviatura")})
public class Formato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODFORMATO")
    private Long codformato;
    @Basic(optional = false)
    @Column(name = "DESFORMATO")
    private String desformato;
    @Column(name = "ABREVIATURA")
    private Character abreviatura;
    @OneToMany(mappedBy = "codformato", fetch = FetchType.LAZY)
    private List<Tienda> tiendaList;

    public Formato() {
    }

    public Formato(Long codformato) {
        this.codformato = codformato;
    }

    public Formato(Long codformato, String desformato) {
        this.codformato = codformato;
        this.desformato = desformato;
    }

    public Long getCodformato() {
        return codformato;
    }

    public void setCodformato(Long codformato) {
        this.codformato = codformato;
    }

    public String getDesformato() {
        return desformato;
    }

    public void setDesformato(String desformato) {
        this.desformato = desformato;
    }

    public Character getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(Character abreviatura) {
        this.abreviatura = abreviatura;
    }

    @XmlTransient
    public List<Tienda> getTiendaList() {
        return tiendaList;
    }

    public void setTiendaList(List<Tienda> tiendaList) {
        this.tiendaList = tiendaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codformato != null ? codformato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Formato)) {
            return false;
        }
        Formato other = (Formato) object;
        if ((this.codformato == null && other.codformato != null) || (this.codformato != null && !this.codformato.equals(other.codformato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Formato[ codformato=" + codformato + " ]";
    }
    
}
