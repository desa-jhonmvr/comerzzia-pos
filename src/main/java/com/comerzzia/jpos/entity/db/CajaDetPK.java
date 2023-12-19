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
public class CajaDetPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "UID_CAJERO_CAJA")
    private String uidCajeroCaja;
    @Basic(optional = false)
    @Column(name = "LINEA")
    private int linea;

    public CajaDetPK() {
    }

    public CajaDetPK(String uidCajeroCaja,int linea) {
        this.linea = linea;
        this.uidCajeroCaja = uidCajeroCaja;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getUidCajeroCaja() {
        return uidCajeroCaja;
    }

    public void setUidCajeroCaja(String uidCajeroCaja) {
        this.uidCajeroCaja = uidCajeroCaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) linea;
        hash += (uidCajeroCaja != null ? uidCajeroCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof CajaDetPK)) {
            return false;
        }
        CajaDetPK other = (CajaDetPK) object;
        if (this.linea != other.linea) {
            return false;
        }
        if ((this.uidCajeroCaja == null && other.uidCajeroCaja != null) || (this.uidCajeroCaja != null && !this.uidCajeroCaja.equals(other.uidCajeroCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.db.CajaDetPK[ linea=" + linea + ", uidCajeroCaja=" + uidCajeroCaja + " ]";
    }
    
}
