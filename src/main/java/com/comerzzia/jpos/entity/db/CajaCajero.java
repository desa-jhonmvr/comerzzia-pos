/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_CAJA_CAJERO_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaCajero.findAll", query = "SELECT c FROM CajaCajero c"),
    @NamedQuery(name = "CajaCajero.findByUidCajeroCaja", query = "SELECT c FROM CajaCajero c WHERE c.uidCajeroCaja = :uidCajeroCaja"),
    @NamedQuery(name = "CajaCajero.findByFechaApertura", query = "SELECT c FROM CajaCajero c WHERE c.fechaApertura = :fechaApertura"),
    @NamedQuery(name = "CajaCajero.findByFechaCierre", query = "SELECT c FROM CajaCajero c WHERE c.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "CajaCajero.findByIdUsuario", query = "SELECT c FROM CajaCajero c WHERE c.idUsuario = :idUsuario")})
public class CajaCajero implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_CAJERO_CAJA")
    private String uidCajeroCaja;
    @Column(name = "FECHA_APERTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaApertura;
    @Column(name = "FECHA_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;  
    @Column(name = "ID_USUARIO")  
    private Long idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cajaCajero", fetch = FetchType.LAZY)
    private List<RecuentoCajaDet> recuentoCajaDetList;   
    @JoinColumn(name = "UID_DIARIO_CAJA", referencedColumnName = "UID_DIARIO_CAJA")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Caja uidDiarioCaja;
    @Transient
    private String desUsuario;

    public CajaCajero() {
    }

    public CajaCajero(String uidCajeroCaja) {
        this.uidCajeroCaja = uidCajeroCaja;
    }

    public CajaCajero(String uidCajeroCaja, Long usuario) {
        this.uidCajeroCaja = uidCajeroCaja;
        this.idUsuario = usuario;
    }

    public String getUidCajeroCaja() {
        return uidCajeroCaja;
    }

    public void setUidCajeroCaja(String uidCajeroCaja) {
        this.uidCajeroCaja = uidCajeroCaja;
    }
    
    public void setUidCajeroCaja() {
        this.uidCajeroCaja=UUID.randomUUID().toString();
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Caja getUidDiarioCaja() {
        return uidDiarioCaja;
    }

    public void setUidDiarioCaja(Caja uidDiarioCaja) {
        this.uidDiarioCaja = uidDiarioCaja;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidCajeroCaja != null ? uidCajeroCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof CajaCajero)) {
            return false;
        }
        CajaCajero other = (CajaCajero) object;
        if ((this.uidCajeroCaja == null && other.uidCajeroCaja != null) || (this.uidCajeroCaja != null && !this.uidCajeroCaja.equals(other.uidCajeroCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.CajaCajero[ uidCajeroCaja=" + uidCajeroCaja + " ]";
    }
    
   /* 
    @XmlTransient
    public List<RecuentoCajaDet> getRecuentoCajaDetList() {
        return recuentoCajaDetList;
    }

    public void setRecuentoCajaDetList(List<RecuentoCajaDet> recuentoCajaDetList) {
        this.recuentoCajaDetList = recuentoCajaDetList;
    }
     */

    public String getDesUsuario() {
        return desUsuario;
    }

    public void setDesUsuario(String desUsuario) {
        this.desUsuario = desUsuario;
    }
     
}
