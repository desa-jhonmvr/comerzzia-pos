/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MGRI
 */
@Embeddable
public class ArticuloPlanNovioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PLAN")
    private BigInteger idPlan;
    @Basic(optional = false)
    @Column(name = "ID_LINEA")
    private long idLinea;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;

    public ArticuloPlanNovioPK() {
    }

    public ArticuloPlanNovioPK(BigInteger idPlan, long idLinea, String codalm) {
        this.idPlan = idPlan;
        this.idLinea = idLinea;
        this.codalm = codalm;
    }

    public BigInteger getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(BigInteger idPlan) {
        this.idPlan = idPlan;
    }

    public long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(long idLinea) {
        this.idLinea = idLinea;
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
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        hash += (int) idLinea;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArticuloPlanNovioPK)) {
            return false;
        }
        ArticuloPlanNovioPK other = (ArticuloPlanNovioPK) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        if (this.idLinea != other.idLinea) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ArticuloPlanNovioPK[ idPlan=" + idPlan + ", idLinea=" + idLinea + ", codalm=" + codalm + " ]";
    }
    
}
