/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica.enotaCredito;

import com.comerzzia.jpos.persistencia.facturaElectronica.CampoAdicionalBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.DetalleBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.InfoTributariaBean;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SMLM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "notaCredito")
public class NotaCreditoElectronicaBean {
    @XmlElement(name = "infoTributaria")  
    private InfoTributariaBean infoTributaria;
    @XmlElement(name = "infoNotaCredito")
    private InfoNotaCreditoBean infoNotaCredito;
    @XmlElementWrapper(name = "detalles")
    @XmlElement(name = "detalle")    
    private List<DetalleBean> detalles;
    @XmlElementWrapper(name = "infoAdicional")
    @XmlElement(name = "campoAdicional")    
    private List<CampoAdicionalBean> infoAdicional;
  
    @XmlAttribute(name="id")
    public String getId() {
        return "comprobante";
    }
    
    @XmlAttribute(name="version")
    public String getNumber() {
        return "1.0.0";
    }
    
    public InfoTributariaBean getInfoTributaria() {
        return infoTributaria;
    }

    public void setInfoTributaria(InfoTributariaBean infoTributaria) {
        this.infoTributaria = infoTributaria;
    }

    public InfoNotaCreditoBean getInfoNotaCredito() {
        return infoNotaCredito;
    }

    public void setInfoNotaCredito(InfoNotaCreditoBean infoNotaCredito) {
        this.infoNotaCredito = infoNotaCredito;
    }

    public List<DetalleBean> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleBean> detalles) {
        this.detalles = detalles;
    }

    public List<CampoAdicionalBean> getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(List<CampoAdicionalBean> infoAdicional) {
        this.infoAdicional = infoAdicional;
    }
    
}
