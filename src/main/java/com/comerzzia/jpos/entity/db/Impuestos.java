/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CONFIG_IMPUESTOS_TBL")
@XmlRootElement
public class Impuestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_IMPUESTOS")
    private Long idImpuestos;
    @Basic(optional = false)
    @Column(name = "IMPUESTOSS")
    private Long impuestos;
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private Long codigo;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "TARIFA_PORCENTAJE")
    private Long tarifaPorcentaje;
    @Basic(optional = false)
    @Column(name = "TARIFA_ESPECIFICA", scale = 2)
    private BigDecimal tarifaEspecifica;
    
    

    public Impuestos() {
    }

    public Long getIdImpuestos() {
        return idImpuestos;
    }

    public void setIdImpuestos(Long idImpuestos) {
        this.idImpuestos = idImpuestos;
    }

    public Long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Long impuestos) {
        this.impuestos = impuestos;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTarifaPorcentaje() {
        return tarifaPorcentaje;
    }

    public void setTarifaPorcentaje(Long tarifaPorcentaje) {
        this.tarifaPorcentaje = tarifaPorcentaje;
    }

    public BigDecimal getTarifaEspecifica() {
        return tarifaEspecifica;
    }

    public void setTarifaEspecifica(BigDecimal tarifaEspecifica) {
        this.tarifaEspecifica = tarifaEspecifica;
    }

  

}
