/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author SMLM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "campoAdicional")
public class CampoAdicionalBean {
    
    @XmlAttribute(name = "nombre")
    private String atributo;
    
    @XmlValue
    private String campoAdicional;

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getCampoAdicional() {
        return campoAdicional;
    }

    public void setCampoAdicional(String campoAdicional) {
        this.campoAdicional = campoAdicional;
    }
    
}
