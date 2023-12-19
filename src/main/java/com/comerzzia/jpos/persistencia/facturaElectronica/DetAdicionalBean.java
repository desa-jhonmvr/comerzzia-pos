/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SMLM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "detAdicional")
public class DetAdicionalBean {
    @XmlElement(name = "detAdicional")
    private String detAdicional;

    public String getDetAdicional() {
        return detAdicional;
    }

    public void setDetAdicional(String detAdicional) {
        this.detAdicional = detAdicional;
    }
   
}
