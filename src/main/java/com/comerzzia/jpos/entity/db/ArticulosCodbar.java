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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_ARTICULOS_CODBAR_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArticulosCodbar.findAll", query = "SELECT a FROM ArticulosCodbar a"),
    @NamedQuery(name = "ArticulosCodbar.findByCodart", query = "SELECT a FROM ArticulosCodbar a WHERE a.articulosCodbarPK.codart = :codart AND a.dun14='N'"),
    @NamedQuery(name = "ArticulosCodbar.findByDesglose1", query = "SELECT a FROM ArticulosCodbar a WHERE a.desglose1 = :desglose1"),
    @NamedQuery(name = "ArticulosCodbar.findByDesglose2", query = "SELECT a FROM ArticulosCodbar a WHERE a.desglose2 = :desglose2"),
    @NamedQuery(name = "ArticulosCodbar.findByCodigoBarras", query = "SELECT a FROM ArticulosCodbar a WHERE a.articulosCodbarPK.codigoBarras = :codigoBarras"),
    @NamedQuery(name = "ArticulosCodbar.findByDun14", query = "SELECT a FROM ArticulosCodbar a WHERE a.dun14 = :dun14"),
    @NamedQuery(name = "ArticulosCodbar.findByFechaAlta", query = "SELECT a FROM ArticulosCodbar a WHERE a.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "ArticulosCodbar.findByFactorConversion", query = "SELECT a FROM ArticulosCodbar a WHERE a.factorConversion = :factorConversion")})
public class ArticulosCodbar implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArticulosCodbarPK articulosCodbarPK;
    @Basic(optional = false)
    @Column(name = "DESGLOSE1")
    private String desglose1;
    @Basic(optional = false)
    @Column(name = "DESGLOSE2")
    private String desglose2;
    @Basic(optional = false)
    @Column(name = "DUN14")
    private char dun14;
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FACTOR_CONVERSION")
    private BigDecimal factorConversion;

    public ArticulosCodbar() {
    }

    public ArticulosCodbar(ArticulosCodbarPK articulosCodbarPK) {
        this.articulosCodbarPK = articulosCodbarPK;
    }

    public ArticulosCodbar(ArticulosCodbarPK articulosCodbarPK, String desglose1, String desglose2, char dun14, Date fechaAlta) {
        this.articulosCodbarPK = articulosCodbarPK;
        this.desglose1 = desglose1;
        this.desglose2 = desglose2;
        this.dun14 = dun14;
        this.fechaAlta = fechaAlta;
    }

    public ArticulosCodbar(String codart, String codigoBarras) {
        this.articulosCodbarPK = new ArticulosCodbarPK(codart, codigoBarras);
    }

    public ArticulosCodbarPK getArticulosCodbarPK() {
        return articulosCodbarPK;
    }

    public void setArticulosCodbarPK(ArticulosCodbarPK articulosCodbarPK) {
        this.articulosCodbarPK = articulosCodbarPK;
    }

    public String getDesglose1() {
        return desglose1;
    }

    public void setDesglose1(String desglose1) {
        this.desglose1 = desglose1;
    }

    public String getDesglose2() {
        return desglose2;
    }

    public void setDesglose2(String desglose2) {
        this.desglose2 = desglose2;
    }

    public char getDun14() {
        return dun14;
    }

    public void setDun14(char dun14) {
        this.dun14 = dun14;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public BigDecimal getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(BigDecimal factorConversion) {
        this.factorConversion = factorConversion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (articulosCodbarPK != null ? articulosCodbarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArticulosCodbar)) {
            return false;
        }
        ArticulosCodbar other = (ArticulosCodbar) object;
        if ((this.articulosCodbarPK == null && other.articulosCodbarPK != null) || (this.articulosCodbarPK != null && !this.articulosCodbarPK.equals(other.articulosCodbarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ArticulosCodbar[ articulosCodbarPK=" + articulosCodbarPK + " ]";
    }
    
}
