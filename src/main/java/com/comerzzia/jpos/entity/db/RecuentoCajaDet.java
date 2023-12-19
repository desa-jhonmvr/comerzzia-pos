/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_CAJA_DET_RECUENTO_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecuentoCajaDet.findAll", query = "SELECT r FROM RecuentoCajaDet r"),
    @NamedQuery(name = "RecuentoCajaDet.findByUidCajeroCaja", query = "SELECT r FROM RecuentoCajaDet r WHERE r.recuentoCajaDetPK.uidCajeroCaja = :uidCajeroCaja"),
    @NamedQuery(name = "RecuentoCajaDet.findByLinea", query = "SELECT r FROM RecuentoCajaDet r WHERE r.recuentoCajaDetPK.linea = :linea"),
    @NamedQuery(name = "RecuentoCajaDet.findByCodmedpag", query = "SELECT r FROM RecuentoCajaDet r WHERE r.codmedpag = :codmedpag"),
    @NamedQuery(name = "RecuentoCajaDet.findByCantidad", query = "SELECT r FROM RecuentoCajaDet r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "RecuentoCajaDet.findByValor", query = "SELECT r FROM RecuentoCajaDet r WHERE r.valor = :valor"),
    @NamedQuery(name = "RecuentoCajaDet.findBySubValor", query= "SELECT r FROM RecuentoCajaDet r WHERE r.subvalor = :subvalor")})
public class RecuentoCajaDet implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecuentoCajaDetPK recuentoCajaDetPK;
    @JoinColumn(name = "CODMEDPAG", referencedColumnName = "CODMEDPAG")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MedioPagoCaja codmedpag;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional=false)
    @Column(name = "SUBVALOR")
    private BigDecimal subvalor;
    @JoinColumn(name = "UID_CAJERO_CAJA", referencedColumnName = "UID_CAJERO_CAJA", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CajaCajero cajaCajero;
    @JoinColumn(name = "UID_DIARIO_CAJA", referencedColumnName = "UID_DIARIO_CAJA")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Caja caja;

    public RecuentoCajaDet() {
            }

    public RecuentoCajaDet(RecuentoCajaDetPK recuentoCajaDetPK) {
        this.recuentoCajaDetPK = recuentoCajaDetPK;
    }

    public RecuentoCajaDet(RecuentoCajaDetPK recuentoCajaDetPK, MedioPagoCaja codmedpag, int cantidad, BigDecimal valor) {
        this.recuentoCajaDetPK = recuentoCajaDetPK;
        this.codmedpag = codmedpag;
        this.cantidad = cantidad;
        this.valor = valor;
    }

    public RecuentoCajaDet(String uidDiarioCaja, int linea) {
        this.recuentoCajaDetPK = new RecuentoCajaDetPK(uidDiarioCaja, linea);
    }

    public RecuentoCajaDetPK getRecuentoCajaDetPK() {
        return recuentoCajaDetPK;
    }

    public void setRecuentoCajaDetPK(RecuentoCajaDetPK recuentoCajaDetPK) {
        this.recuentoCajaDetPK = recuentoCajaDetPK;
    }

    public MedioPagoCaja getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(MedioPagoCaja codmedpag) {
        this.codmedpag = codmedpag;
    }

    public void setMedioPago(MedioPagoBean medioPago){
        this.codmedpag = new MedioPagoCaja(medioPago);
    }
    
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public CajaCajero getCajaCajero() {
        return cajaCajero;
    }

    public void setCajaCajero(CajaCajero cajaCajero) {
        this.cajaCajero = cajaCajero;
    }

    public Caja getUidDiarioCaja() {
        return caja;
    }

    public void setUidDiarioCaja(Caja uidDiarioCaja) {
        this.caja = uidDiarioCaja;
    }

    public BigDecimal getSubvalor() {
        return subvalor;
    }

    public void setSubvalor(BigDecimal subvalor) {
        this.subvalor = subvalor;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recuentoCajaDetPK != null ? recuentoCajaDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RecuentoCajaDet)) {
            return false;
        }
        RecuentoCajaDet other = (RecuentoCajaDet) object;
        if ((this.recuentoCajaDetPK == null && other.recuentoCajaDetPK != null) || (this.recuentoCajaDetPK != null && !this.recuentoCajaDetPK.equals(other.recuentoCajaDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.RecuentoCajaDet[ recuentoCajaDetPK=" + recuentoCajaDetPK + " ]";
    }
    
}
