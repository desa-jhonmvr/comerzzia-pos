/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CONTABILIDAD
 */
@Entity
@Table(name = "X_CINTA_AUDITORA_ITEM_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XCintaAuditoraItemTbl.findAll", query = "SELECT x FROM XCintaAuditoraItemTbl x")
    , @NamedQuery(name = "XCintaAuditoraItemTbl.findByIdCintaAuditoraItem", query = "SELECT x FROM XCintaAuditoraItemTbl x WHERE x.idCintaAuditoraItem = :idCintaAuditoraItem")
    , @NamedQuery(name = "XCintaAuditoraItemTbl.findByNombre", query = "SELECT x FROM XCintaAuditoraItemTbl x WHERE x.nombre = :nombre")
    , @NamedQuery(name = "XCintaAuditoraItemTbl.findByValor", query = "SELECT x FROM XCintaAuditoraItemTbl x WHERE x.valor = :valor")})
public class XCintaAuditoraItemTbl implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CINTA_AUDITORA_ITEM")
    private BigDecimal idCintaAuditoraItem;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private String valor;
    @JoinColumns({
        @JoinColumn(name = "ID_CINTA_AUDITORA", referencedColumnName = "ID_CINTA_AUDITORA")
        , @JoinColumn(name = "CODALM", referencedColumnName = "CODALM")
        , @JoinColumn(name = "CODCAJA", referencedColumnName = "CODCAJA")})
    @ManyToOne(optional = false)
    private XCintaAuditoraTbl xCintaAuditoraTbl;

    public XCintaAuditoraItemTbl() {
    }

    public XCintaAuditoraItemTbl(BigDecimal idCintaAuditoraItem) {
        this.idCintaAuditoraItem = idCintaAuditoraItem;
    }

    public XCintaAuditoraItemTbl(BigDecimal idCintaAuditoraItem, String nombre, String valor) {
        this.idCintaAuditoraItem = idCintaAuditoraItem;
        this.nombre = nombre;
        this.valor = valor;
    }

    public BigDecimal getIdCintaAuditoraItem() {
        return idCintaAuditoraItem;
    }

    public void setIdCintaAuditoraItem(BigDecimal idCintaAuditoraItem) {
        this.idCintaAuditoraItem = idCintaAuditoraItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public XCintaAuditoraTbl getXCintaAuditoraTbl() {
        return xCintaAuditoraTbl;
    }

    public void setXCintaAuditoraTbl(XCintaAuditoraTbl xCintaAuditoraTbl) {
        this.xCintaAuditoraTbl = xCintaAuditoraTbl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCintaAuditoraItem != null ? idCintaAuditoraItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XCintaAuditoraItemTbl)) {
            return false;
        }
        XCintaAuditoraItemTbl other = (XCintaAuditoraItemTbl) object;
        if ((this.idCintaAuditoraItem == null && other.idCintaAuditoraItem != null) || (this.idCintaAuditoraItem != null && !this.idCintaAuditoraItem.equals(other.idCintaAuditoraItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.XCintaAuditoraItemTbl[ idCintaAuditoraItem=" + idCintaAuditoraItem + " ]";
    }
    
}
