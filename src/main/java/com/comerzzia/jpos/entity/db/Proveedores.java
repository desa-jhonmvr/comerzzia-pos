/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_PROVEEDORES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedores.findAll", query = "SELECT p FROM Proveedores p"),
    @NamedQuery(name = "Proveedores.findByCodpro", query = "SELECT p FROM Proveedores p WHERE p.codpro = :codpro"),
    @NamedQuery(name = "Proveedores.findByDespro", query = "SELECT p FROM Proveedores p WHERE p.despro = :despro"),
    @NamedQuery(name = "Proveedores.findByTipoProveedor", query = "SELECT p FROM Proveedores p WHERE p.tipoProveedor = :tipoProveedor"),
    @NamedQuery(name = "Proveedores.findByNombreComercial", query = "SELECT p FROM Proveedores p WHERE p.nombreComercial = :nombreComercial"),
    @NamedQuery(name = "Proveedores.findByDomicilio", query = "SELECT p FROM Proveedores p WHERE p.domicilio = :domicilio"),
    @NamedQuery(name = "Proveedores.findByPoblacion", query = "SELECT p FROM Proveedores p WHERE p.poblacion = :poblacion"),
    @NamedQuery(name = "Proveedores.findByProvincia", query = "SELECT p FROM Proveedores p WHERE p.provincia = :provincia"),
    @NamedQuery(name = "Proveedores.findByCp", query = "SELECT p FROM Proveedores p WHERE p.cp = :cp"),
    @NamedQuery(name = "Proveedores.findByTelefono1", query = "SELECT p FROM Proveedores p WHERE p.telefono1 = :telefono1"),
    @NamedQuery(name = "Proveedores.findByTelefono2", query = "SELECT p FROM Proveedores p WHERE p.telefono2 = :telefono2"),
    @NamedQuery(name = "Proveedores.findByFax", query = "SELECT p FROM Proveedores p WHERE p.fax = :fax"),
    @NamedQuery(name = "Proveedores.findByPersonaContacto", query = "SELECT p FROM Proveedores p WHERE p.personaContacto = :personaContacto"),
    @NamedQuery(name = "Proveedores.findByEmail", query = "SELECT p FROM Proveedores p WHERE p.email = :email"),
    @NamedQuery(name = "Proveedores.findByCif", query = "SELECT p FROM Proveedores p WHERE p.cif = :cif"),
    @NamedQuery(name = "Proveedores.findByObservaciones", query = "SELECT p FROM Proveedores p WHERE p.observaciones = :observaciones"),
    @NamedQuery(name = "Proveedores.findByActivo", query = "SELECT p FROM Proveedores p WHERE p.activo = :activo"),
    @NamedQuery(name = "Proveedores.findByFechaAlta", query = "SELECT p FROM Proveedores p WHERE p.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Proveedores.findByFechaBaja", query = "SELECT p FROM Proveedores p WHERE p.fechaBaja = :fechaBaja"),
    @NamedQuery(name = "Proveedores.findByBanco", query = "SELECT p FROM Proveedores p WHERE p.banco = :banco"),
    @NamedQuery(name = "Proveedores.findByBancoDomicilio", query = "SELECT p FROM Proveedores p WHERE p.bancoDomicilio = :bancoDomicilio"),
    @NamedQuery(name = "Proveedores.findByBancoPoblacion", query = "SELECT p FROM Proveedores p WHERE p.bancoPoblacion = :bancoPoblacion"),
    @NamedQuery(name = "Proveedores.findByCcc", query = "SELECT p FROM Proveedores p WHERE p.ccc = :ccc")})
public class Proveedores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODPRO")
    private String codpro;
    @Basic(optional = false)
    @Column(name = "DESPRO")
    private String despro;
    @Column(name = "TIPO_PROVEEDOR")
    private String tipoProveedor;
    @Column(name = "NOMBRE_COMERCIAL")
    private String nombreComercial;
    @Column(name = "DOMICILIO")
    private String domicilio;
    @Column(name = "POBLACION")
    private String poblacion;
    @Column(name = "PROVINCIA")
    private String provincia;
    @Column(name = "CP")
    private String cp;
    @Column(name = "TELEFONO1")
    private String telefono1;
    @Column(name = "TELEFONO2")
    private String telefono2;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "PERSONA_CONTACTO")
    private String personaContacto;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CIF")
    private String cif;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "FECHA_BAJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Column(name = "BANCO")
    private String banco;
    @Column(name = "BANCO_DOMICILIO")
    private String bancoDomicilio;
    @Column(name = "BANCO_POBLACION")
    private String bancoPoblacion;
    @Column(name = "CCC")
    private String ccc;
    @OneToMany(mappedBy = "codpro", fetch = FetchType.LAZY)
    private Collection<Articulos> articulosCollection;

    public Proveedores() {
    }

    public Proveedores(String codpro) {
        this.codpro = codpro;
    }

    public Proveedores(String codpro, String despro, char activo) {
        this.codpro = codpro;
        this.despro = despro;
        this.activo = activo;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getDespro() {
        return despro;
    }

    public void setDespro(String despro) {
        this.despro = despro;
    }

    public String getTipoProveedor() {
        return tipoProveedor;
    }

    public void setTipoProveedor(String tipoProveedor) {
        this.tipoProveedor = tipoProveedor;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBancoDomicilio() {
        return bancoDomicilio;
    }

    public void setBancoDomicilio(String bancoDomicilio) {
        this.bancoDomicilio = bancoDomicilio;
    }

    public String getBancoPoblacion() {
        return bancoPoblacion;
    }

    public void setBancoPoblacion(String bancoPoblacion) {
        this.bancoPoblacion = bancoPoblacion;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    @XmlTransient
    public Collection<Articulos> getArticulosCollection() {
        return articulosCollection;
    }

    public void setArticulosCollection(Collection<Articulos> articulosCollection) {
        this.articulosCollection = articulosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpro != null ? codpro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Proveedores)) {
            return false;
        }
        Proveedores other = (Proveedores) object;
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Proveedores[ codpro=" + codpro + " ]";
    }
    
}
