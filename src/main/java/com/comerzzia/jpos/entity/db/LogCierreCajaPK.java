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
public class LogCierreCajaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "uid_log")
    private String uidLog;
    @Basic(optional = false)
    @Column(name = "cod_alm")
    private String codAlm;
    @Basic(optional = false)
    @Column(name = "cod_caja")
    private String codCaja;

    public LogCierreCajaPK() {
    }

    public LogCierreCajaPK(String uidLog, String codAlm, String codCaja) {
        this.uidLog = uidLog;
        this.codAlm = codAlm;
        this.codCaja = codCaja;
    }
    public LogCierreCajaPK(String codAlm, String codCaja) {
        this.uidLog = uidLog;
        this.codAlm = codAlm;
        this.codCaja = codCaja;
    }
    

    public String getUidLog() {
        return uidLog;
    }

    public void setUidLog(String uidLog) {
        this.uidLog = uidLog;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidLog != null ? uidLog.hashCode() : 0);
        hash += (codAlm != null ? codAlm.hashCode() : 0);
        hash += (codCaja != null ? codCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogCierreCajaPK)) {
            return false;
        }
        LogCierreCajaPK other = (LogCierreCajaPK) object;
        if ((this.uidLog == null && other.uidLog != null) || (this.uidLog != null && !this.uidLog.equals(other.uidLog))) {
            return false;
        }
        if ((this.codAlm == null && other.codAlm != null) || (this.codAlm != null && !this.codAlm.equals(other.codAlm))) {
            return false;
        }
        if ((this.codCaja == null && other.codCaja != null) || (this.codCaja != null && !this.codCaja.equals(other.codCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LogCierreCajaPK[ uidLog=" + uidLog + ", codAlm=" + codAlm + ", codCaja=" + codCaja + " ]";
    }
    
}
