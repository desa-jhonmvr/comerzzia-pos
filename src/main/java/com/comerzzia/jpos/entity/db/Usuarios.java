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
@Table(name = "CONFIG_USUARIOS_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByIdUsuario", query = "SELECT u FROM Usuarios u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuarios.findByUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "Usuarios.findByDesusuario", query = "SELECT u FROM Usuarios u WHERE u.desusuario = :desusuario"),
    @NamedQuery(name = "Usuarios.findByClave", query = "SELECT u FROM Usuarios u WHERE u.clave = :clave"),
    @NamedQuery(name = "Usuarios.findByActivo", query = "SELECT u FROM Usuarios u WHERE u.activo = :activo"),
    @NamedQuery(name = "Usuarios.findByAplicacionPorDefecto", query = "SELECT u FROM Usuarios u WHERE u.aplicacionPorDefecto = :aplicacionPorDefecto"),
    @NamedQuery(name = "Usuarios.findByPuedeCambiarAplicacion", query = "SELECT u FROM Usuarios u WHERE u.puedeCambiarAplicacion = :puedeCambiarAplicacion")})
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "DESUSUARIO")
    private String desusuario;
    @Column(name = "CLAVE")
    private String clave;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @Column(name = "APLICACION_POR_DEFECTO")
    private String aplicacionPorDefecto;
    @Basic(optional = false)
    @Column(name = "PUEDE_CAMBIAR_APLICACION")
    private char puedeCambiarAplicacion;/*
    @ManyToMany(mappedBy = "usuariosCollection", fetch = FetchType.EAGER)
    private Collection<Perfiles> perfilesCollection;
    */
    public Usuarios() {
    }

    public Usuarios(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuarios(Long idUsuario, String usuario, char activo, char puedeCambiarAplicacion) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.activo = activo;
        this.puedeCambiarAplicacion = puedeCambiarAplicacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDesusuario() {
        return desusuario;
    }

    public void setDesusuario(String desusuario) {
        this.desusuario = desusuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public String getAplicacionPorDefecto() {
        return aplicacionPorDefecto;
    }

    public void setAplicacionPorDefecto(String aplicacionPorDefecto) {
        this.aplicacionPorDefecto = aplicacionPorDefecto;
    }

    public char getPuedeCambiarAplicacion() {
        return puedeCambiarAplicacion;
    }

    public void setPuedeCambiarAplicacion(char puedeCambiarAplicacion) {
        this.puedeCambiarAplicacion = puedeCambiarAplicacion;
    }
    /*
    @XmlTransient
    public Collection<Perfiles> getPerfilesCollection() {
        return perfilesCollection;
    }

    public void setPerfilesCollection(Collection<Perfiles> perfilesCollection) {
        this.perfilesCollection = perfilesCollection;
    }
     * */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Usuarios[ idUsuario=" + idUsuario + " ]";
    }
    
}
