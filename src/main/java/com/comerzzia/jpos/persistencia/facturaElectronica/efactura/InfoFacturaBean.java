/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.facturaElectronica.efactura;

import com.comerzzia.jpos.persistencia.facturaElectronica.TotalImpuestoBean;
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
@XmlRootElement(name = "infoFactura")
public class InfoFacturaBean {
    @XmlElement(name = "fechaEmision")
    private String fechaEmision;
    @XmlElement(name = "dirEstablecimiento")
    private String dirEstablecimiento;
    @XmlElement(name = "contribuyenteEspecial")
    private String contribuyenteEspecial;
    @XmlElement(name = "obligadoContabilidad")
    private String obligadoContabilidad;
    @XmlElement(name = "tipoIdentificacionComprador")
    private String tipoIdentificacionComprador;
    @XmlElement(name = "guiaRemision")
    private String guiaRemision;
    @XmlElement(name = "razonSocialComprador")
    private String razonSocialComprador;
    @XmlElement(name = "identificacionComprador")
    private String identificacionComprador;
    @XmlElement(name = "direccionComprador")
    private String direccionComprador;    
    @XmlElement(name = "totalSinImpuestos")
    private Double totalSinImpuestos;
    @XmlElement(name = "totalDescuento")
    private Double totalDescuento;
    @XmlElementWrapper(name = "totalConImpuestos")
    @XmlElement(name = "totalImpuesto")    
    private List<TotalImpuestoBean> totalConImpuestos;
    @XmlElementWrapper(name = "compensaciones")
    @XmlElement(name = "compensacion") 
    private List<CompensacionBean> compensaciones;
    @XmlElement(name = "propina")
    private Double propina;
    @XmlElement(name = "importeTotal")
    private Double importeTotal;
    @XmlElement(name = "moneda")
    private String moneda;
    @XmlElementWrapper(name = "pagos")
    @XmlElement(name = "pago") 
    private List<PagoBean> pagos;

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

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
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

    public Double getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(Double totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public Double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public List<TotalImpuestoBean> getTotalConImpuestos() {
        return totalConImpuestos;
    }

    public void setTotalConImpuestos(List<TotalImpuestoBean> totalConImpuestos) {
        this.totalConImpuestos = totalConImpuestos;
    }

    public String getDireccionComprador() {
        return direccionComprador;
    }

    public void setDireccionComprador(String direccionComprador) {
        this.direccionComprador = direccionComprador;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public List<CompensacionBean> getCompensaciones() {
        return compensaciones;
    }

    public void setCompensaciones(List<CompensacionBean> compensaciones) {
        this.compensaciones = compensaciones;
    }

    public List<PagoBean> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoBean> pagos) {
        this.pagos = pagos;
    }
    
    
    
}
