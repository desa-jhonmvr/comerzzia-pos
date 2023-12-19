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
public class BonoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_bono")
    private long idBono;
    @Basic(optional = false)
    @Column(name = "codalm")
    private String codalm;

    public BonoPK() {
    }

    public BonoPK(long idBono, String codalm) {
        this.idBono = idBono;
        this.codalm = codalm;
    }

    public long getIdBono() {
        return idBono;
    }

    public void setIdBono(long idBono) {
        this.idBono = idBono;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idBono;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BonoPK)) {
            return false;
        }
        BonoPK other = (BonoPK) object;
        if (this.idBono != other.idBono) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.BonoPK[ idBono=" + idBono + ", codalm=" + codalm + " ]";
    }
    
}
