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
public class GuiaRemisionDetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "UID_GUIA_REMISION")
    private String uidGuiaRemision;
    @Basic(optional = false)
    @Column(name = "ID_LINEA")
    private short idLinea;

    public GuiaRemisionDetallePK() {
    }

    public GuiaRemisionDetallePK(String uidGuiaRemision, short idLinea) {
        this.uidGuiaRemision = uidGuiaRemision;
        this.idLinea = idLinea;
    }

    public String getUidGuiaRemision() {
        return uidGuiaRemision;
    }

    public void setUidGuiaRemision(String uidGuiaRemision) {
        this.uidGuiaRemision = uidGuiaRemision;
    }

    public short getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(short idLinea) {
        this.idLinea = idLinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidGuiaRemision != null ? uidGuiaRemision.hashCode() : 0);
        hash += (int) idLinea;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GuiaRemisionDetallePK)) {
            return false;
        }
        GuiaRemisionDetallePK other = (GuiaRemisionDetallePK) object;
        if ((this.uidGuiaRemision == null && other.uidGuiaRemision != null) || (this.uidGuiaRemision != null && !this.uidGuiaRemision.equals(other.uidGuiaRemision))) {
            return false;
        }
        if (this.idLinea != other.idLinea) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.GuiaRemisionDetallePK[ uidGuiaRemision=" + uidGuiaRemision + ", idLinea=" + idLinea + " ]";
    }
    
}
