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
public class LogOperacionesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cod_alm")
    private String codAlm;
    @Basic(optional = false)
    @Column(name = "cod_caja")
    private String codCaja;
    @Basic(optional = false)
    @Column(name = "uid_log")
    private String uid;

    public LogOperacionesPK() {
    }

    public LogOperacionesPK(String codAlm, String codCaja, String idLog) {
        this.codAlm = codAlm;
        this.codCaja = codCaja;
        this.uid = idLog;
    }
    
    public LogOperacionesPK(String codAlm, String codCaja) {
        this.codAlm = codAlm;
        this.codCaja = codCaja;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String idLog) {
        this.uid = idLog;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAlm != null ? codAlm.hashCode() : 0);
        hash += (codCaja != null ? codCaja.hashCode() : 0);
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogOperacionesPK)) {
            return false;
        }
        LogOperacionesPK other = (LogOperacionesPK) object;
        if ((this.codAlm == null && other.codAlm != null) || (this.codAlm != null && !this.codAlm.equals(other.codAlm))) {
            return false;
        }
        if ((this.codCaja == null && other.codCaja != null) || (this.codCaja != null && !this.codCaja.equals(other.codCaja))) {
            return false;
        }
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogOperacionesPK[ codAlm=" + codAlm + ", codCaja=" + codCaja + ", idLog=" + uid + " ]";
    }
    
}
