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
@XmlRootElement(name = "totalImpuesto")
public class TotalImpuestoBean {

    @XmlElement(name = "codigo")
    private int codigo;
    @XmlElement(name = "codigoPorcentaje")
    private int codigoPorcentaje;
    @XmlElement(name = "descuentoAdicional")
    private Double descuentoAdicional;
    @XmlElement(name = "baseImponible")
    private String baseImponible;
    @XmlElement(name = "tarifa")
    private Double tarifa;
    @XmlElement(name = "valor")
    private Double valor;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(int codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public Double getDescuentoAdicional() {
        return descuentoAdicional;
    }

    public void setDescuentoAdicional(Double descuentoAdicional) {
        this.descuentoAdicional = descuentoAdicional;
    }

    public String getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
