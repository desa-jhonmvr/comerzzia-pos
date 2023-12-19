/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
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
@Table(name = "X_MOTIVOS_DEVOLUCION_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivoDevolucion.findAll", query = "SELECT m FROM MotivoDevolucion m"),
    @NamedQuery(name = "MotivoDevolucion.findByIdMotivo", query = "SELECT m FROM MotivoDevolucion m WHERE m.idMotivo = :idMotivo"),
    @NamedQuery(name = "MotivoDevolucion.findByDescripcionMotivo", query = "SELECT m FROM MotivoDevolucion m WHERE m.descripcionMotivo = :descripcionMotivo")})
public class MotivoDevolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_MOTIVO")
    private Integer idMotivo;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION_MOTIVO")
    private String descripcionMotivo;

    public MotivoDevolucion() {
    }

    public MotivoDevolucion(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public MotivoDevolucion(Integer idMotivo, String descripcionMotivo) {
        this.idMotivo = idMotivo;
        this.descripcionMotivo = descripcionMotivo;
    }

    public Integer getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescripcionMotivo() {
        return descripcionMotivo;
    }

    public void setDescripcionMotivo(String descripcionMotivo) {
        this.descripcionMotivo = descripcionMotivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMotivo != null ? idMotivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof MotivoDevolucion)) {
            return false;
        }
        MotivoDevolucion other = (MotivoDevolucion) object;
        if ((this.idMotivo == null && other.idMotivo != null) || (this.idMotivo != null && !this.idMotivo.equals(other.idMotivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.MotivoDevolucion[ idMotivo=" + idMotivo + " ]";
    }
    
}
