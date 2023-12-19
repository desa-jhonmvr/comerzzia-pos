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
public class LogOperacionesDetPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "UID_LOG_DET")
    private String uidLogDet;

    public LogOperacionesDetPK() {
    }

    public LogOperacionesDetPK(String idLog) {
        this.uidLogDet = idLog;
    }

    public String getUidLogDet() {
        return uidLogDet;
    }

    public void setUidLogDet(String uidLogDet) {
        this.uidLogDet = uidLogDet;
    }

}
