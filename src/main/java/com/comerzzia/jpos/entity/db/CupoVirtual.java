/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Sheyla Rivera
 */
@Entity
@Table(name = "D_CUPOVIRTUAL_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CupoVirtual.findByCredito", query = "SELECT c FROM CupoVirtual c WHERE c.credito = :credito")})
public class CupoVirtual implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "CREDITO")
    private Integer credito;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "CUPO")
    private BigDecimal cupo;
    
    @Column(name = "PROCESADO")
    private String procesado;
    
    public CupoVirtual() {
    }

    public CupoVirtual(Integer credito, Date fecha, BigDecimal cupo) {
        this.credito = credito;
        this.fecha = fecha;
        this.cupo = cupo;
    }

    public Integer getCredito() {
        return credito;
    }

    public void setCredito(Integer credito) {
        this.credito = credito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCupo() {
        return cupo;
    }

    public void setCupo(BigDecimal cupo) {
        this.cupo = cupo;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }
    
    
}
