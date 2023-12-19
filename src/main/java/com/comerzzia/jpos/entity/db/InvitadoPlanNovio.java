/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_PLAN_NOVIOS_INV_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvitadoPlanNovio.findAll", query = "SELECT i FROM InvitadoPlanNovio i"),
    @NamedQuery(name = "InvitadoPlanNovio.findByIdPlan", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.invitadoPlanNovioPK.idPlan = :idPlan"),
    @NamedQuery(name = "InvitadoPlanNovio.findByIdInvitado", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.invitadoPlanNovioPK.idInvitado = :idInvitado"),
    @NamedQuery(name = "InvitadoPlanNovio.findByNombre", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "InvitadoPlanNovio.findByApellido", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.apellido = :apellido"),
    @NamedQuery(name = "InvitadoPlanNovio.findByTelefono", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.telefono = :telefono"),
    @NamedQuery(name = "InvitadoPlanNovio.findByTitulo", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.titulo = :titulo"),
    @NamedQuery(name = "InvitadoPlanNovio.findByProcesado", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.procesado = :procesado"),
    @NamedQuery(name = "InvitadoPlanNovio.findByCodalm", query = "SELECT i FROM InvitadoPlanNovio i WHERE i.invitadoPlanNovioPK.codalm = :codalm")})
public class InvitadoPlanNovio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvitadoPlanNovioPK invitadoPlanNovioPK;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "TITULO")
    private String titulo;
    @Column(name = "PROCESADO")
    private Character procesado;
    @JoinColumns({
        @JoinColumn(name = "ID_PLAN", referencedColumnName = "ID_PLAN", insertable = false, updatable = false),
        @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanNovio planNovio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invitadoPlanNovio", fetch = FetchType.LAZY)
    private List<AbonoPlanNovio> abonoPlanNovioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invitadoPlanNovio", fetch = FetchType.LAZY)
    private List<ArticuloPlanNovio> articuloPlanNovioList;

    public InvitadoPlanNovio() {
    }

    public InvitadoPlanNovio(InvitadoPlanNovioPK invitadoPlanNovioPK) {
        this.invitadoPlanNovioPK = invitadoPlanNovioPK;
    }

    public InvitadoPlanNovio(InvitadoPlanNovioPK invitadoPlanNovioPK, String nombre, String telefono) {
        this.invitadoPlanNovioPK = invitadoPlanNovioPK;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public InvitadoPlanNovio(BigInteger idPlan, BigInteger idInvitado, String codAlm, String codAlmInvitado) {
        this.invitadoPlanNovioPK = new InvitadoPlanNovioPK(idPlan, idInvitado, codAlm, codAlmInvitado);
    }

    public InvitadoPlanNovioPK getInvitadoPlanNovioPK() {
        return invitadoPlanNovioPK;
    }

    public void setInvitadoPlanNovioPK(InvitadoPlanNovioPK invitadoPlanNovioPK) {
        this.invitadoPlanNovioPK = invitadoPlanNovioPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public PlanNovio getPlanNovio() {
        return planNovio;
    }

    public void setPlanNovio(PlanNovio planNovio) {
        this.planNovio = planNovio;
    }

    @XmlTransient
    public List<AbonoPlanNovio> getAbonoPlanNovioList() {
        return abonoPlanNovioList;
    }

    public void setAbonoPlanNovioList(List<AbonoPlanNovio> abonoPlanNovioList) {
        this.abonoPlanNovioList = abonoPlanNovioList;
    }

    @XmlTransient
    public List<ArticuloPlanNovio> getArticuloPlanNovioList() {
        return articuloPlanNovioList;
    }

    public void setArticuloPlanNovioList(List<ArticuloPlanNovio> articuloPlanNovioList) {
        this.articuloPlanNovioList = articuloPlanNovioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invitadoPlanNovioPK != null ? invitadoPlanNovioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InvitadoPlanNovio)) {
            return false;
        }
        InvitadoPlanNovio other = (InvitadoPlanNovio) object;
        if ((this.invitadoPlanNovioPK == null && other.invitadoPlanNovioPK != null) || (this.invitadoPlanNovioPK != null && !this.invitadoPlanNovioPK.equals(other.invitadoPlanNovioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.InvitadoPlanNovio[ invitadoPlanNovioPK=" + invitadoPlanNovioPK + " ]";
    }
    
}
