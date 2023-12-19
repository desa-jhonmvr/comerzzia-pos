/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SMLM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "detalle")
public class DetalleBean {
    // De factura
    @XmlElement(name = "codigoPrincipal")
    private String codigoPrincipal;
    @XmlElement(name = "codigoAuxiliar")
    private String codigoAuxiliar;
    // De nota Credito
    @XmlElement(name = "codigoInterno")
    private String codigoInterno;
    @XmlElement(name = "codigoAdicional")
    private String codigoAdicional;
    // Comunes
    @XmlElement(name = "descripcion")
    private String descripcion;
    @XmlElement(name = "cantidad")
    private int cantidad;
    @XmlElement(name = "precioUnitario")
    private Double precioUnitario;
    @XmlElement(name = "descuento")
    private Double descuento;
    @XmlElement(name = "precioTotalSinImpuesto")
    private Double precioTotalSinImpuesto;
    @XmlElementWrapper(name = "detallesAdicionales")
    @XmlElement(name = "detAdicional")    
    private List<DetAdicionalBean> detallesAdicionales;
    @XmlElementWrapper(name = "impuestos")
    @XmlElement(name = "impuesto")
    private List<ImpuestoBean> impuestos;

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoAdicional() {
        return codigoAdicional;
    }

    public void setCodigoAdicional(String codigoAdicional) {
        this.codigoAdicional = codigoAdicional;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getPrecioTotalSinImpuesto() {
        return precioTotalSinImpuesto;
    }

    public void setPrecioTotalSinImpuesto(Double precioTotalSinImpuesto) {
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
    }

    public List<DetAdicionalBean> getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(List<DetAdicionalBean> detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

    public List<ImpuestoBean> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoBean> impuestos) {
        this.impuestos = impuestos;
    }
     
}
