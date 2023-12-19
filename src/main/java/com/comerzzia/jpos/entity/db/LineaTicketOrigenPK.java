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
public class LineaTicketOrigenPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;
    @Basic(optional = false)
    @Column(name = "ID_LINEA")
    private short idLinea;

    public LineaTicketOrigenPK() {
    }

    public LineaTicketOrigenPK(String uidTicket, short idLinea) {
        this.uidTicket = uidTicket;
        this.idLinea = idLinea;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
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
        hash += (uidTicket != null ? uidTicket.hashCode() : 0);
        hash += (int) idLinea;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof LineaTicketOrigenPK)) {
            return false;
        }
        LineaTicketOrigenPK other = (LineaTicketOrigenPK) object;
        if ((this.uidTicket == null && other.uidTicket != null) || (this.uidTicket != null && !this.uidTicket.equals(other.uidTicket))) {
            return false;
        }
        if (this.idLinea != other.idLinea) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LineaTicketOrigenPK[ uidTicket=" + uidTicket + ", idLinea=" + idLinea + " ]";
    }
    
}
