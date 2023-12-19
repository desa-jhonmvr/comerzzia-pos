/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "D_IMPUESTOS_TBL")
@XmlRootElement
public class ImpuestosFact implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ImpuestosPK impuestosFactPK;
    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;
    @Basic(optional = false)
    @Column(name = "ID_IMPUESTOS")
    private Long idImpuestos;
    @Basic(optional = false)
    @Column(name = "BASE_IMPONIBLE")
    private Integer baseImponible;
    @Basic(optional = false)
    @Column(name = "PORCENTAJE")
    private Long porcentaje;
    @Basic(optional = false)
    @Column(name = "TARIFA", scale = 2)
    private BigDecimal tarifa;
    @Basic(optional = false)
    @Column(name = "VALOR", scale = 2)
    private BigDecimal valor;

    public ImpuestosFact() {

    }

    public ImpuestosPK getImpuestosFactPK() {
        return impuestosFactPK;
    }

    public void setImpuestosFactPK(ImpuestosPK impuestosFactPK) {
        this.impuestosFactPK = impuestosFactPK;
    }

    public ImpuestosFact(String codAlm, String codCaja) {
        this.impuestosFactPK = new ImpuestosPK(UUID.randomUUID().toString());
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public void setIdImpuestos(Long idImpuestos) {
        this.idImpuestos = idImpuestos;
    }

    public Integer getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(Integer baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Long getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Long porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
