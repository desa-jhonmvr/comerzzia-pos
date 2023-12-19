/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CONTABILIDAD
 */
@Entity
@Table(name = "X_CINTA_AUDITORA_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XCintaAuditoraTbl.findAll", query = "SELECT x FROM XCintaAuditoraTbl x")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByIdCintaAuditora", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.xCintaAuditoraTblPK.idCintaAuditora = :idCintaAuditora")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByCodalm", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.xCintaAuditoraTblPK.codalm = :codalm")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByCodcaja", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.xCintaAuditoraTblPK.codcaja = :codcaja")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByFechaInicio", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByFechaFin", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.fechaFin = :fechaFin")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByFecha", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.fecha = :fecha")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByUsuario", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.usuario = :usuario")
    , @NamedQuery(name = "XCintaAuditoraTbl.findByAutorizador", query = "SELECT x FROM XCintaAuditoraTbl x WHERE x.autorizador = :autorizador")})
public class XCintaAuditoraTbl implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XCintaAuditoraTblPK xCintaAuditoraTblPK;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "AUTORIZADOR")
    private String autorizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xCintaAuditoraTbl")
    private Collection<XCintaAuditoraItemTbl> xCintaAuditoraItemTblCollection;

    public XCintaAuditoraTbl() {
    }

    public XCintaAuditoraTbl(XCintaAuditoraTblPK xCintaAuditoraTblPK) {
        this.xCintaAuditoraTblPK = xCintaAuditoraTblPK;
    }

    public XCintaAuditoraTbl(XCintaAuditoraTblPK xCintaAuditoraTblPK, Date fechaFin, Date fecha, String usuario, String autorizador) {
        this.xCintaAuditoraTblPK = xCintaAuditoraTblPK;
        this.fechaFin = fechaFin;
        this.fecha = fecha;
        this.usuario = usuario;
        this.autorizador = autorizador;
    }

    public XCintaAuditoraTbl(BigInteger idCintaAuditora, String codalm, String codcaja) {
        this.xCintaAuditoraTblPK = new XCintaAuditoraTblPK(idCintaAuditora, codalm, codcaja);
    }

    public XCintaAuditoraTblPK getXCintaAuditoraTblPK() {
        return xCintaAuditoraTblPK;
    }

    public void setXCintaAuditoraTblPK(XCintaAuditoraTblPK xCintaAuditoraTblPK) {
        this.xCintaAuditoraTblPK = xCintaAuditoraTblPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    @XmlTransient
    public Collection<XCintaAuditoraItemTbl> getXCintaAuditoraItemTblCollection() {
        return xCintaAuditoraItemTblCollection;
    }

    public void setXCintaAuditoraItemTblCollection(Collection<XCintaAuditoraItemTbl> xCintaAuditoraItemTblCollection) {
        this.xCintaAuditoraItemTblCollection = xCintaAuditoraItemTblCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xCintaAuditoraTblPK != null ? xCintaAuditoraTblPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XCintaAuditoraTbl)) {
            return false;
        }
        XCintaAuditoraTbl other = (XCintaAuditoraTbl) object;
        if ((this.xCintaAuditoraTblPK == null && other.xCintaAuditoraTblPK != null) || (this.xCintaAuditoraTblPK != null && !this.xCintaAuditoraTblPK.equals(other.xCintaAuditoraTblPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.XCintaAuditoraTbl[ xCintaAuditoraTblPK=" + xCintaAuditoraTblPK + " ]";
    }
    
}
