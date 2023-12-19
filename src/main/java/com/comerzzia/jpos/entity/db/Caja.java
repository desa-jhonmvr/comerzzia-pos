/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
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
import java.util.UUID;
/**
 *
 * @author MGRI
 */

@Entity
@Table(name = "D_CAJA_CAB_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caja.findAll", query = "SELECT c FROM Caja c"),
    @NamedQuery(name = "Caja.findByUidDiarioCaja", query = "SELECT c FROM Caja c WHERE c.uidDiarioCaja = :uidDiarioCaja"),
    @NamedQuery(name = "Caja.findByCodalm", query = "SELECT c FROM Caja c WHERE c.codalm = :codalm"),
    @NamedQuery(name = "Caja.findByCodcaja", query = "SELECT c FROM Caja c WHERE c.codcaja = :codcaja"),
    @NamedQuery(name = "Caja.findByFechaApertura", query = "SELECT c FROM Caja c WHERE c.fechaApertura = :fechaApertura"),
    @NamedQuery(name = "Caja.findByFechaCierre", query = "SELECT c FROM Caja c WHERE c.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "Caja.findByIdUsuario", query = "SELECT c FROM Caja c WHERE c.idUsuario = :idUsuario")})
public class Caja implements Serializable {
    @Basic(optional = false)
    @Column(name = "FECHA_APERTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaApertura;
    @Column(name = "FECHA_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_DIARIO_CAJA")
    private String uidDiarioCaja;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "caja", fetch = FetchType.LAZY)
    // private List<CajaDet> cajaDetList;
    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "uidDiarioCaja", fetch = FetchType.LAZY)
    // private List<CajaCajero> cajaCajeroList;

    public Caja() {
    }

    public Caja(String uidDiarioCaja) {
        this.uidDiarioCaja = uidDiarioCaja;
    }

    public Caja(String uidDiarioCaja, String codalm, String codcaja, Date fechaApertura) {
        this.uidDiarioCaja = uidDiarioCaja;
        this.codalm = codalm;
        this.codcaja = codcaja;
        this.fechaApertura = fechaApertura;
    }

    public String getUidDiarioCaja() {
        return uidDiarioCaja;
    }

    public void setUidDiarioCaja(String uidDiarioCaja) {
        this.uidDiarioCaja = uidDiarioCaja;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
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
/*
    @XmlTransient
    public List<CajaDet> getCajaDetList() {
        return cajaDetList;
    }

    public void setCajaDetList(List<CajaDet> cajaDetList) {
        this.cajaDetList = cajaDetList;
    }
    
    @XmlTransient
    public List<CajaCajero> getCajaCajeroList() {
        return cajaCajeroList;
    }

    public void setCajaCajeroList(List<CajaCajero> cajaCajeroList) {
        this.cajaCajeroList = cajaCajeroList;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidDiarioCaja != null ? uidDiarioCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Caja)) {
            return false;
        }
        Caja other = (Caja) object;
        if ((this.uidDiarioCaja == null && other.uidDiarioCaja != null) || (this.uidDiarioCaja != null && !this.uidDiarioCaja.equals(other.uidDiarioCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Caja[ uidDiarioCaja=" + uidDiarioCaja + " ]";
    }

    public void setUidDiarioCaja() {
        this.uidDiarioCaja=UUID.randomUUID().toString();
    }

}
