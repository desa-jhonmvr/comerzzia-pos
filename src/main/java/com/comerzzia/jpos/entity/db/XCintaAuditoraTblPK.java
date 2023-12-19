/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author CONTABILIDAD
 */
@Embeddable
public class XCintaAuditoraTblPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_CINTA_AUDITORA")
    private BigInteger idCintaAuditora;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;

    public XCintaAuditoraTblPK() {
    }

    public XCintaAuditoraTblPK(BigInteger idCintaAuditora, String codalm, String codcaja) {
        this.idCintaAuditora = idCintaAuditora;
        this.codalm = codalm;
        this.codcaja = codcaja;
    }

    public BigInteger getIdCintaAuditora() {
        return idCintaAuditora;
    }

    public void setIdCintaAuditora(BigInteger idCintaAuditora) {
        this.idCintaAuditora = idCintaAuditora;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
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
        hash += (idCintaAuditora != null ? idCintaAuditora.hashCode() : 0);
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (codcaja != null ? codcaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XCintaAuditoraTblPK)) {
            return false;
        }
        XCintaAuditoraTblPK other = (XCintaAuditoraTblPK) object;
        if ((this.idCintaAuditora == null && other.idCintaAuditora != null) || (this.idCintaAuditora != null && !this.idCintaAuditora.equals(other.idCintaAuditora))) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if ((this.codcaja == null && other.codcaja != null) || (this.codcaja != null && !this.codcaja.equals(other.codcaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.XCintaAuditoraTblPK[ idCintaAuditora=" + idCintaAuditora + ", codalm=" + codalm + ", codcaja=" + codcaja + " ]";
    }
    
}
