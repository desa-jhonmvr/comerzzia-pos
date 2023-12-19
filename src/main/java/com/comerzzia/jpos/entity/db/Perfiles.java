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
@Table(name = "CONFIG_PERFILES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perfiles.findAll", query = "SELECT p FROM Perfiles p"),
    @NamedQuery(name = "Perfiles.findByIdPerfil", query = "SELECT p FROM Perfiles p WHERE p.idPerfil = :idPerfil"),
    @NamedQuery(name = "Perfiles.findByDesperfil", query = "SELECT p FROM Perfiles p WHERE p.desperfil = :desperfil"),
    @NamedQuery(name = "Perfiles.findByCaducidadPassword", query = "SELECT p FROM Perfiles p WHERE p.caducidadPassword = :caducidadPassword")})
public class Perfiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PERFIL")
    private Long idPerfil;
    @Column(name = "DESPERFIL")
    private String desperfil;
    @Column(name = "CADUCIDAD_PASSWORD")
    private BigInteger caducidadPassword;
    /*
    @JoinTable(name = "CONFIG_USUARIOS_PERFILES_TBL", joinColumns = {
        @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID_PERFIL")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Usuarios> usuariosCollection;
     * */
    public Perfiles() {
    }

    public Perfiles(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDesperfil() {
        return desperfil;
    }

    public void setDesperfil(String desperfil) {
        this.desperfil = desperfil;
    }

    public BigInteger getCaducidadPassword() {
        return caducidadPassword;
    }

    public void setCaducidadPassword(BigInteger caducidadPassword) {
        this.caducidadPassword = caducidadPassword;
    }

    /*
    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }
     * */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfil != null ? idPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Perfiles)) {
            return false;
        }
        Perfiles other = (Perfiles) object;
        if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Perfiles[ idPerfil=" + idPerfil + " ]";
    }
    
}
