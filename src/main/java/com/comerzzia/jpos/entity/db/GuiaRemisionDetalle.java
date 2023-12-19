/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_GUIA_REMISION_DET_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GuiaRemisionDetalle.findAll", query = "SELECT g FROM GuiaRemisionDetalle g"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByUidGuiaRemision", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.guiaRemisionDetallePK.uidGuiaRemision = :uidGuiaRemision"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByIdLinea", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.guiaRemisionDetallePK.idLinea = :idLinea"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByCodart", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.codart = :codart"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByCantidad", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.cantidad = :cantidad"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByDescripcion", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByCodMarca", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.codMarca = :codMarca"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByCodItem", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.codItem = :codItem"),
    @NamedQuery(name = "GuiaRemisionDetalle.findByModelo", query = "SELECT g FROM GuiaRemisionDetalle g WHERE g.modelo = :modelo")})
public class GuiaRemisionDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GuiaRemisionDetallePK guiaRemisionDetallePK;
    @Basic(optional = false)
    @Column(name = "CODART")
    private String codart;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "COD_MARCA")
    private String codMarca;
    @Column(name = "COD_ITEM")
    private Integer codItem;
    @Column(name = "MODELO")
    private String modelo;
    @JoinColumn(name = "UID_GUIA_REMISION", referencedColumnName = "UID_GUIA_REMISION", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GuiaRemision guiaRemision;
    @Column(name = "ID_LINEA_ORIGEN")
    private Integer idLineaOrigen;
    
    @Transient
    LineaTicketOrigen lineaTicketOrigen;
    
    public GuiaRemisionDetalle() {
    }

    public GuiaRemisionDetalle(GuiaRemisionDetallePK guiaRemisionDetallePK) {
        this.guiaRemisionDetallePK = guiaRemisionDetallePK;
    }

    public GuiaRemisionDetalle(GuiaRemisionDetallePK guiaRemisionDetallePK, String codart, int cantidad, String descripcion) {
        this.guiaRemisionDetallePK = guiaRemisionDetallePK;
        this.codart = codart;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public GuiaRemisionDetalle(String uidGuiaRemision, short idLinea) {
        this.guiaRemisionDetallePK = new GuiaRemisionDetallePK(uidGuiaRemision, idLinea);
    }

    public GuiaRemisionDetallePK getGuiaRemisionDetallePK() {
        return guiaRemisionDetallePK;
    }

    public void setGuiaRemisionDetallePK(GuiaRemisionDetallePK guiaRemisionDetallePK) {
        this.guiaRemisionDetallePK = guiaRemisionDetallePK;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(String codMarca) {
        this.codMarca = codMarca;
    }

    public Integer getCodItem() {
        return codItem;
    }

    public void setCodItem(Integer codItem) {
        this.codItem = codItem;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public GuiaRemision getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(GuiaRemision guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guiaRemisionDetallePK != null ? guiaRemisionDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GuiaRemisionDetalle)) {
            return false;
        }
        GuiaRemisionDetalle other = (GuiaRemisionDetalle) object;
        if ((this.guiaRemisionDetallePK == null && other.guiaRemisionDetallePK != null) || (this.guiaRemisionDetallePK != null && !this.guiaRemisionDetallePK.equals(other.guiaRemisionDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.GuiaRemisionDetalle[ guiaRemisionDetallePK=" + guiaRemisionDetallePK + " ]";
    }
    
    public LineaTicketOrigen getLineaTicketOrigen() {
        return lineaTicketOrigen;
    }

    public void setLineaTicketOrigen(LineaTicketOrigen lineaTicketOrigen) {
        this.lineaTicketOrigen = lineaTicketOrigen;
    }

    public Integer getIdLineaOrigen() {
        return idLineaOrigen;
    }

    public void setIdLineaOrigen(Integer idLineaOrigen) {
        this.idLineaOrigen = idLineaOrigen;
    }
}
