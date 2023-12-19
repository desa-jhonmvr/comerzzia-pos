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
public class InvitadoPlanNovioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PLAN")
    private BigInteger idPlan;
    @Basic(optional = false)
    @Column(name = "ID_INVITADO")
    private BigInteger idInvitado;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "CODALM_INVITADO")
    private String codAlmInvitado;
    
    public InvitadoPlanNovioPK() {
    }

    public InvitadoPlanNovioPK(BigInteger idPlan, BigInteger idInvitado, String codalm, String codAlmInvitado) {
        this.idPlan = idPlan;
        this.idInvitado = idInvitado;
        this.codalm = codalm;
        this.codAlmInvitado = codAlmInvitado;
    }

    public BigInteger getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(BigInteger idPlan) {
        this.idPlan = idPlan;
    }

    public BigInteger getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(BigInteger idInvitado) {
        this.idInvitado = idInvitado;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCodAlmInvitado() {
        return codAlmInvitado;
    }

    public void setCodAlmInvitado(String codAlmInvitado) {
        this.codAlmInvitado = codAlmInvitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        hash += (idInvitado != null ? idInvitado.hashCode() : 0);
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (codAlmInvitado != null ? codAlmInvitado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InvitadoPlanNovioPK)) {
            return false;
        }
        InvitadoPlanNovioPK other = (InvitadoPlanNovioPK) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        if ((this.idInvitado == null && other.idInvitado != null) || (this.idInvitado != null && !this.idInvitado.equals(other.idInvitado))) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if ((this.codAlmInvitado == null && other.codAlmInvitado != null) || (this.codAlmInvitado != null && !this.codAlmInvitado.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.InvitadoPlanNovioPK[ idPlan=" + idPlan + ", idInvitado=" + idInvitado + ", codalm=" + codalm + ", codAlmInvitado=" + codAlmInvitado + " ]";
    }
    
}
