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
public class GarantiaExtendidaRegPK implements Serializable {

    private static final long serialVersionUID = 5320852897904511266L;

    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;
    @Basic(optional = false)
    @Column(name = "ID_LINEA")
    private short idLinea;
    @Column(name = "UID_TICKET_REFERENCIA")
    private String uidTicketReferencia;

    public GarantiaExtendidaRegPK() {
    }

    /**
     * 
     * @author Gabriel Simbania
     * @param uidTicket uid de la factura que tiene el ítem original
     * @param idLinea id del item de la garantia
     * @param uidTicketReferencia  uid de la factura donde se compro el EGO
     */
    public GarantiaExtendidaRegPK(String uidTicket, short idLinea, String uidTicketReferencia) {
        this.uidTicket = uidTicket;
        this.idLinea = idLinea;
        this.uidTicketReferencia = uidTicketReferencia;
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

    public String getUidTicketReferencia() {
        return uidTicketReferencia;
    }

    public void setUidTicketReferencia(String uidTicketReferencia) {
        this.uidTicketReferencia = uidTicketReferencia;
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
        if (!(object instanceof GarantiaExtendidaRegPK)) {
            return false;
        }
        GarantiaExtendidaRegPK other = (GarantiaExtendidaRegPK) object;
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
        return "com.comerzzia.jpos.entity.db.GarantiaExtendidaRegPK[ uidTicket=" + uidTicket + ", idLinea=" + idLinea + " ]";
    }

}
