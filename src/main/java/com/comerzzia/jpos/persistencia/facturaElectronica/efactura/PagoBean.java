/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica.efactura;

/**
 *
 * @author SMLM
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SMLM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pago")
public class PagoBean {
    @XmlElement(name = "formaPago")
    private String formaPago;  
    @XmlElement(name = "total")
    private Double total;

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }       
}