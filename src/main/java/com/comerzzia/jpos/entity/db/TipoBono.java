/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "x_tipos_bonos_tbl")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoBono.findAll", query = "SELECT t FROM TipoBono t"),
    @NamedQuery(name = "TipoBono.findByIdTipoBono", query = "SELECT t FROM TipoBono t WHERE t.idTipoBono = :idTipoBono"),
    @NamedQuery(name = "TipoBono.findByDescripcion", query = "SELECT t FROM TipoBono t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoBono.findByExpedicionManual", query = "SELECT t FROM TipoBono t WHERE t.expedicionManual = :expedicionManual"),
    @NamedQuery(name = "TipoBono.findByDescuento", query = "SELECT t FROM TipoBono t WHERE t.descuento = :descuento"),
    @NamedQuery(name = "TipoBono.findBySoloSinPromocion", query = "SELECT t FROM TipoBono t WHERE t.soloSinPromocion = :soloSinPromocion"),
    @NamedQuery(name = "TipoBono.findByPlazo", query = "SELECT t FROM TipoBono t WHERE t.plazo = :plazo"),
    @NamedQuery(name = "TipoBono.findByCantidad", query = "SELECT t FROM TipoBono t WHERE t.cantidad = :cantidad")})
public class TipoBono implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_bono")
    private Long idTipoBono;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "expedicion_manual")
    private char expedicionManual;
    @Basic(optional = false)
    @Column(name = "descuento")
    private long descuento;
    @Basic(optional = false)
    @Column(name = "solo_sin_promocion")
    private char soloSinPromocion;
    @Column(name = "plazo")
    private BigInteger plazo;
    @Column(name = "cantidad")
    private BigInteger cantidad;

    public TipoBono() {
    }

    public TipoBono(Long idTipoBono) {
        this.idTipoBono = idTipoBono;
    }

    public TipoBono(Long idTipoBono, String descripcion, char expedicionManual, long descuento, char soloSinPromocion) {
        this.idTipoBono = idTipoBono;
        this.descripcion = descripcion;
        this.expedicionManual = expedicionManual;
        this.descuento = descuento;
        this.soloSinPromocion = soloSinPromocion;
    }

    public Long getIdTipoBono() {
        return idTipoBono;
    }

    public void setIdTipoBono(Long idTipoBono) {
        this.idTipoBono = idTipoBono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public char getExpedicionManual() {
        return expedicionManual;
    }

    public void setExpedicionManual(char expedicionManual) {
        this.expedicionManual = expedicionManual;
    }

    public long getDescuento() {
        return descuento;
    }

    public void setDescuento(long descuento) {
        this.descuento = descuento;
    }

    public char getSoloSinPromocion() {
        return soloSinPromocion;
    }

    public void setSoloSinPromocion(char soloSinPromocion) {
        this.soloSinPromocion = soloSinPromocion;
    }

    public BigInteger getPlazo() {
        return plazo;
    }

    public void setPlazo(BigInteger plazo) {
        this.plazo = plazo;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoBono != null ? idTipoBono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoBono)) {
            return false;
        }
        TipoBono other = (TipoBono) object;
        if ((this.idTipoBono == null && other.idTipoBono != null) || (this.idTipoBono != null && !this.idTipoBono.equals(other.idTipoBono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.TipoBono[ idTipoBono=" + idTipoBono + " ]";
    }
    
}
