/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 *
 * @author MGRI
 */
@Embeddable
public class TarifasPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CODTAR")
    private String codtar;
    @Basic(optional = false)
    @Column(name = "CODART")
    private String codart;

    public TarifasPK() {
    }

    public TarifasPK(String codtar, String codart) {
        this.codtar = codtar;
        this.codart = codart;
    }

    public String getCodtar() {
        return codtar;
    }

    public void setCodtar(String codtar) {
        this.codtar = codtar;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtar != null ? codtar.hashCode() : 0);
        hash += (codart != null ? codart.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TarifasPK)) {
            return false;
        }
        TarifasPK other = (TarifasPK) object;
        if ((this.codtar == null && other.codtar != null) || (this.codtar != null && !this.codtar.equals(other.codtar))) {
            return false;
        }
        if ((this.codart == null && other.codart != null) || (this.codart != null && !this.codart.equals(other.codart))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.TarifasPK[ codtar=" + codtar + ", codart=" + codart + " ]";
    }
    
}
