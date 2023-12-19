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
public class SriCajaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CODALM_SRI")
    private String codalmSri;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;

    public SriCajaPK() {
    }

    public SriCajaPK(String codalmSri, String codcaja) {
        this.codalmSri = codalmSri;
        this.codcaja = codcaja;
    }

    public String getCodalmSri() {
        return codalmSri;
    }

    public void setCodalmSri(String codalmSri) {
        this.codalmSri = codalmSri;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalmSri != null ? codalmSri.hashCode() : 0);
        hash += (codcaja != null ? codcaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SriCajaPK)) {
            return false;
        }
        SriCajaPK other = (SriCajaPK) object;
        if ((this.codalmSri == null && other.codalmSri != null) || (this.codalmSri != null && !this.codalmSri.equals(other.codalmSri))) {
            return false;
        }
        if ((this.codcaja == null && other.codcaja != null) || (this.codcaja != null && !this.codcaja.equals(other.codcaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.SriCajaPK[ codalmSri=" + codalmSri + ", codcaja=" + codcaja + " ]";
    }
    
}
