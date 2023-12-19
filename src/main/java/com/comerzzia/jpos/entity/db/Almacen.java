/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_ALMACENES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a"),
    @NamedQuery(name = "Almacen.findByCodalm", query = "SELECT a FROM Almacen a WHERE a.codalm = :codalm"),
    @NamedQuery(name = "Almacen.findByDesalm", query = "SELECT a FROM Almacen a WHERE a.desalm = :desalm"),
    @NamedQuery(name = "Almacen.findByDomicilio", query = "SELECT a FROM Almacen a WHERE a.domicilio = :domicilio"),
    @NamedQuery(name = "Almacen.findByPoblacion", query = "SELECT a FROM Almacen a WHERE a.poblacion = :poblacion"),
    @NamedQuery(name = "Almacen.findByProvincia", query = "SELECT a FROM Almacen a WHERE a.provincia = :provincia"),
    @NamedQuery(name = "Almacen.findByCp", query = "SELECT a FROM Almacen a WHERE a.cp = :cp"),
    @NamedQuery(name = "Almacen.findByTelefono1", query = "SELECT a FROM Almacen a WHERE a.telefono1 = :telefono1"),
    @NamedQuery(name = "Almacen.findByTelefono2", query = "SELECT a FROM Almacen a WHERE a.telefono2 = :telefono2"),
    @NamedQuery(name = "Almacen.findByFax", query = "SELECT a FROM Almacen a WHERE a.fax = :fax"),
    @NamedQuery(name = "Almacen.findByPersonaContacto", query = "SELECT a FROM Almacen a WHERE a.personaContacto = :personaContacto"),
    @NamedQuery(name = "Almacen.findByActivo", query = "SELECT a FROM Almacen a WHERE a.activo = :activo")})
public class Almacen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "DESALM")
    private String desalm;
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
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @JoinColumn(name = "CODEMP", referencedColumnName = "CODEMP")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empresa codemp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "almacen", fetch = FetchType.LAZY)
    private Tienda tienda;

    public Almacen() {
    }

    public Almacen(String codalm) {
        this.codalm = codalm;
    }

    public Almacen(String codalm, String desalm, char activo) {
        this.codalm = codalm;
        this.desalm = desalm;
        this.activo = activo;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getDesalm() {
        return desalm;
    }

    public void setDesalm(String desalm) {
        this.desalm = desalm;
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

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Empresa getCodemp() {
        return codemp;
    }

    public void setCodemp(Empresa codemp) {
        this.codemp = codemp;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Almacen[ codalm=" + codalm + " ]";
    }

    public String getTelefonos() {
        if (telefono1 != null && !telefono1.isEmpty() && telefono2 != null && !telefono2.isEmpty()){
            return telefono1 + " - " + telefono2;
        }
        else if (telefono1 != null && !telefono1.isEmpty()){
            return telefono1;
        }
        else if (telefono2 != null && !telefono2.isEmpty()){
            return telefono2;
        }
        else{
            return "";
        }
    }
    
}
