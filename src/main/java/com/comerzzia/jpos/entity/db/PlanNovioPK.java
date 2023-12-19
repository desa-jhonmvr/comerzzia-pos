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
public class PlanNovioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PLAN")
    private BigInteger idPlan;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;

    public PlanNovioPK() {
    }

    public PlanNovioPK(BigInteger idPlan, String codalm) {
        this.idPlan = idPlan;
        this.codalm = codalm;
    }

    public BigInteger getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(BigInteger idPlan) {
        this.idPlan = idPlan;
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
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlanNovioPK)) {
            return false;
        }
        PlanNovioPK other = (PlanNovioPK) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ID PLAN: " + codalm + " " + idPlan;
    }
    
}
