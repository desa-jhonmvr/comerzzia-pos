/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.facturaElectronica.enotaCredito;

import com.comerzzia.jpos.persistencia.facturaElectronica.TotalImpuestoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.CompensacionBean;
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
@XmlRootElement(name = "infoNotaCredito")
public class InfoNotaCreditoBean {

    @XmlElement(name = "fechaEmision")
    private String fechaEmision;
    @XmlElement(name = "dirEstablecimiento")
    private String dirEstablecimiento;
    @XmlElement(name = "tipoIdentificacionComprador")
    private String tipoIdentificacionComprador;
    @XmlElement(name = "razonSocialComprador")
    private String razonSocialComprador;
    @XmlElement(name = "identificacionComprador")
    private String identificacionComprador;
    @XmlElement(name = "contribuyenteEspecial")
    private String contribuyenteEspecial;
    @XmlElement(name = "obligadoContabilidad")
    private String obligadoContabilidad;
    @XmlElement(name = "rise")
    private String rise;
    @XmlElement(name = "codDocModificado")
    private String codDocModificado;
    @XmlElement(name = "numDocModificado")
    private String numDocModificado;
    @XmlElement(name = "fechaEmisionDocSustento")
    private String fechaEmisionDocSustento;
    @XmlElement(name = "totalSinImpuestos")
    private Double totalSinImpuestos;
    @XmlElementWrapper(name = "compensaciones")
    @XmlElement(name = "compensacion")
    private List<CompensacionBean> compensaciones;
    @XmlElement(name = "valorModificacion")
    private Double valorModificacion;
    @XmlElement(name = "moneda")
    private String moneda;
    @XmlElementWrapper(name = "totalConImpuestos")
    @XmlElement(name = "totalImpuesto")
    private List<TotalImpuestoBean> totalConImpuestos;
    @XmlElement(name = "motivo")
    private String motivo;

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getCodDocModificado() {
        return codDocModificado;
    }

    public void setCodDocModificado(String codDocModificado) {
        this.codDocModificado = codDocModificado;
    }

    public String getNumDocModificado() {
        return numDocModificado;
    }

    public void setNumDocModificado(String numDocModificado) {
        this.numDocModificado = numDocModificado;
    }

    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public Double getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(Double totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public List<CompensacionBean> getCompensaciones() {
        return compensaciones;
    }

    public void setCompensaciones(List<CompensacionBean> compensaciones) {
        this.compensaciones = compensaciones;
    }
    
    public Double getValorModificacion() {
        return valorModificacion;
    }

    public void setValorModificacion(Double valorModificacion) {
        this.valorModificacion = valorModificacion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public List<TotalImpuestoBean> getTotalConImpuestos() {
        return totalConImpuestos;
    }

    public void setTotalConImpuestos(List<TotalImpuestoBean> totalConImpuestos) {
        this.totalConImpuestos = totalConImpuestos;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
