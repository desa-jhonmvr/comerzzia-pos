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
public class AbonoPlanNovioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PLAN")
    private BigInteger idPlan;
    @Basic(optional = false)
    @Column(name = "ID_ABONO")
    private BigInteger idAbono;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Column(name = "CODALM_ABONO")
    private String codAlmAbono;
    
    public AbonoPlanNovioPK() {
    }

    public AbonoPlanNovioPK(BigInteger idPlan, BigInteger idAbono, String codalm) {
        this.idPlan = idPlan;
        this.idAbono = idAbono;
        this.codalm = codalm;
    }

    public BigInteger getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(BigInteger idPlan) {
        this.idPlan = idPlan;
    }

    public BigInteger getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(BigInteger idAbono) {
        this.idAbono = idAbono;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

        public String getCodAlmAbono() {
        return codAlmAbono;
    }

    public void setCodAlmAbono(String codAlmAbono) {
        this.codAlmAbono = codAlmAbono;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        hash += (idAbono != null ? idAbono.hashCode() : 0);
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbonoPlanNovioPK)) {
            return false;
        }
        AbonoPlanNovioPK other = (AbonoPlanNovioPK) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        if ((this.idAbono == null && other.idAbono != null) || (this.idAbono != null && !this.idAbono.equals(other.idAbono))) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.AbonoPlanNovioPK[ idPlan=" + idPlan + ", idAbono=" + idAbono + ", codalm=" + codalm + " ]";
    }
    
}
