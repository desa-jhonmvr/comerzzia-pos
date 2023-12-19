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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "SRI_TIENDAS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriTienda.findAll", query = "SELECT s FROM SriTienda s"),
    @NamedQuery(name = "SriTienda.findByCodalminterno", query = "SELECT s FROM SriTienda s WHERE s.codalminterno = :codalminterno"),
    @NamedQuery(name = "SriTienda.findByCodalmsri", query = "SELECT s FROM SriTienda s WHERE s.codalmsri = :codalmsri"),
    @NamedQuery(name = "SriTienda.findByCodemp", query = "SELECT s FROM SriTienda s WHERE s.codemp = :codemp"),
    @NamedQuery(name = "SriTienda.findByDesalm", query = "SELECT s FROM SriTienda s WHERE s.desalm = :desalm"),
    @NamedQuery(name = "SriTienda.findByDomicilio", query = "SELECT s FROM SriTienda s WHERE s.domicilio = :domicilio"),
    @NamedQuery(name = "SriTienda.findByEstado", query = "SELECT s FROM SriTienda s WHERE s.estado = :estado"),
    @NamedQuery(name = "SriTienda.findByProcesado", query = "SELECT s FROM SriTienda s WHERE s.procesado = :procesado")})
public class SriTienda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "CODALMINTERNO")
    private String codalminterno;
    @Id
    @Basic(optional = false)
    @Column(name = "CODALMSRI")
    private String codalmsri;
    @Basic(optional = false)
    @Column(name = "CODEMP")
    private String codemp;
    @Basic(optional = false)
    @Column(name = "DESALM")
    private String desalm;
    @Basic(optional = false)
    @Column(name = "DOMICILIO")
    private String domicilio;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private char estado;
    @Basic(optional = false)
    @Column(name = "PROCESADO")
    private char procesado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "sriTienda", fetch = FetchType.LAZY)
    private Tienda tienda;
    
    @Transient    
    private SriCaja cajaActiva;

    public SriTienda() {
    }

    public SriTienda(String codalmsri) {
        this.codalmsri = codalmsri;
    }

    public SriTienda(String codalmsri, String codemp, String desalm, String domicilio, char estado, char procesado) {
        this.codalmsri = codalmsri;
        this.codemp = codemp;
        this.desalm = desalm;
        this.domicilio = domicilio;
        this.estado = estado;
        this.procesado = procesado;
    }

    public String getCodalminterno() {
        return codalminterno;
    }

    public void setCodalminterno(String codalminterno) {
        this.codalminterno = codalminterno;
    }

    public String getCodalmsri() {
        return codalmsri;
    }

    public void setCodalmsri(String codalmsri) {
        this.codalmsri = codalmsri;
    }

    public String getCodemp() {
        return codemp;
    }

    public void setCodemp(String codemp) {
        this.codemp = codemp;
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

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public char getProcesado() {
        return procesado;
    }

    public void setProcesado(char procesado) {
        this.procesado = procesado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalmsri != null ? codalmsri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SriTienda)) {
            return false;
        }
        SriTienda other = (SriTienda) object;
        if ((this.codalmsri == null && other.codalmsri != null) || (this.codalmsri != null && !this.codalmsri.equals(other.codalmsri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.SriTienda[ codalmsri=" + codalmsri + " ]";
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public SriCaja getCajaActiva() {
        return cajaActiva;
    }

    public void setCajaActiva(SriCaja cajaActiva) {
        this.cajaActiva = cajaActiva;
    }
    
    public boolean isActiva(){
        return (this.estado=='1');
    }
  
}
