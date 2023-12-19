/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 *
 * @author MGRI
 */
@Embeddable
public class ArticulosCodbarPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CODART")
    private String codart;
    @Basic(optional = false)
    @Column(name = "CODIGO_BARRAS")
    private String codigoBarras;

    public ArticulosCodbarPK() {
    }

    public ArticulosCodbarPK(String codart, String codigoBarras) {
        this.codart = codart;
        this.codigoBarras = codigoBarras;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codart != null ? codart.hashCode() : 0);
        hash += (codigoBarras != null ? codigoBarras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArticulosCodbarPK)) {
            return false;
        }
        ArticulosCodbarPK other = (ArticulosCodbarPK) object;
        if ((this.codart == null && other.codart != null) || (this.codart != null && !this.codart.equals(other.codart))) {
            return false;
        }
        if ((this.codigoBarras == null && other.codigoBarras != null) || (this.codigoBarras != null && !this.codigoBarras.equals(other.codigoBarras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ArticulosCodbarPK[ codart=" + codart + ", codigoBarras=" + codigoBarras + " ]";
    }
    
}
