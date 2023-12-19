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
 * @author ME
 */
@Embeddable
public class ImpuestosPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_IMP")
    private String idImp;

    public ImpuestosPK() {
    }

    public ImpuestosPK(String idLog) {
        this.idImp = idLog;
    }

}
