/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "D_EMPRESAS_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByCodemp", query = "SELECT e FROM Empresa e WHERE e.codemp = :codemp"),
    @NamedQuery(name = "Empresa.findByDesemp", query = "SELECT e FROM Empresa e WHERE e.desemp = :desemp"),
    @NamedQuery(name = "Empresa.findByNombreComercial", query = "SELECT e FROM Empresa e WHERE e.nombreComercial = :nombreComercial"),
    @NamedQuery(name = "Empresa.findByDomicilio", query = "SELECT e FROM Empresa e WHERE e.domicilio = :domicilio"),
    @NamedQuery(name = "Empresa.findByPoblacion", query = "SELECT e FROM Empresa e WHERE e.poblacion = :poblacion"),
    @NamedQuery(name = "Empresa.findByProvincia", query = "SELECT e FROM Empresa e WHERE e.provincia = :provincia"),
    @NamedQuery(name = "Empresa.findByCp", query = "SELECT e FROM Empresa e WHERE e.cp = :cp"),
    @NamedQuery(name = "Empresa.findByCif", query = "SELECT e FROM Empresa e WHERE e.cif = :cif"),
    @NamedQuery(name = "Empresa.findByTelefono1", query = "SELECT e FROM Empresa e WHERE e.telefono1 = :telefono1"),
    @NamedQuery(name = "Empresa.findByTelefono2", query = "SELECT e FROM Empresa e WHERE e.telefono2 = :telefono2"),
    @NamedQuery(name = "Empresa.findByFax", query = "SELECT e FROM Empresa e WHERE e.fax = :fax"),
    @NamedQuery(name = "Empresa.findByActivo", query = "SELECT e FROM Empresa e WHERE e.activo = :activo"),
    @NamedQuery(name = "Empresa.findByRegistroMercantil", query = "SELECT e FROM Empresa e WHERE e.registroMercantil = :registroMercantil")})
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODEMP")
    private String codemp;
    @Column(name = "DESEMP")
    private String desemp;
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
    @Column(name = "CIF")
    private String cif;
    @Column(name = "TELEFONO1")
    private String telefono1;
    @Column(name = "TELEFONO2")
    private String telefono2;
    @Column(name = "FAX")
    private String fax;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @Lob
    @Column(name = "LOGOTIPO")
    private byte[] logotipo;
    @Column(name = "REGISTRO_MERCANTIL")
    private String registroMercantil;
    @Column(name = "NUM_AUTORIZACION")
    private Long numAutorizacion;
    @Column(name = "FECHA_INICIO_AUTORIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioAuorizacion;
    @Column(name = "FECHA_FIN_AUTORIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinAutorizacion;
    @Column(name = "CLASE_CONTRIBUYENTE")
    private String claseContribuyente;
    @Column(name = "NRO_RESOLUCION_CONTRIBUYENTE")
    private String nroResolucionContribuyente;
    @Column(name = "REINICIO_CONTADORES")
    private Character reinicioContadores;
    @Column(name = "PORCENTAJE_IVA")
    private BigDecimal porcentajeIva;
    @Column(name = "PORCENTAJE_RETENCION")
    private BigDecimal porcentajeRetencion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codemp", fetch = FetchType.LAZY)
    private Collection<Almacen> almacenCollection;

    public Empresa() {
    }

    public Empresa(String codemp) {
        this.codemp = codemp;
    }

    public Empresa(String codemp, char activo) {
        this.codemp = codemp;
        this.activo = activo;
    }

    public String getCodemp() {
        return codemp;
    }

    public void setCodemp(String codemp) {
        this.codemp = codemp;
    }

    public String getDesemp() {
        return desemp;
    }

    public void setDesemp(String desemp) {
        this.desemp = desemp;
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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
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

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Serializable getLogotipo() {
        return logotipo;
    }



    public String getRegistroMercantil() {
        return registroMercantil;
    }

    public void setRegistroMercantil(String registroMercantil) {
        this.registroMercantil = registroMercantil;
    }

    @XmlTransient
    public Collection<Almacen> getAlmacenCollection() {
        return almacenCollection;
    }

    public void setAlmacenCollection(Collection<Almacen> almacenCollection) {
        this.almacenCollection = almacenCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codemp != null ? codemp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.codemp == null && other.codemp != null) || (this.codemp != null && !this.codemp.equals(other.codemp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Empresa[ codemp=" + codemp + " ]";
    }

    public void setLogotipo(byte[] logotipo) {
        this.logotipo = logotipo;
    }

    public String getDireccionForPrint(int longmaxima) {
        String direccion = this.domicilio;
        if (direccion == null) {
            direccion = "[no establecida]";
        }
        if (direccion.length() > longmaxima) {
            direccion.substring(0, longmaxima);
        }
        return direccion;
    }
    
     public String getPoblacionForPrint(int longmaxima) {
        String poblacion = this.poblacion;
        if (poblacion == null) {
            poblacion = "[no establecida]";
        }
        if (poblacion.length() > longmaxima) {
            poblacion.substring(0, longmaxima);
        }
        return poblacion;
    }

    public Long getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(Long numAuorizacion) {
        this.numAutorizacion = numAuorizacion;
    }

    public Date getFechaInicioAuorizacion() {
        return fechaInicioAuorizacion;
    }

    public void setFechaInicioAuorizacion(Date fechaInicioAuorizacion) {
        this.fechaInicioAuorizacion = fechaInicioAuorizacion;
    }

    public Date getFechafinAutorizacion() {
        return fechafinAutorizacion;
    }

    public void setFechafinAutorizacion(Date fechafinAutorizacion) {
        this.fechafinAutorizacion = fechafinAutorizacion;
    }

    public String getClaseContribuyente() {
        return claseContribuyente;
    }

    public void setClaseContribuyente(String claseContribuyente) {
        this.claseContribuyente = claseContribuyente;
    }

    public String getNroResolucionContribuyente() {
        return nroResolucionContribuyente;
    }

    public void setNroResolucionContribuyente(String nroResolucionContribuyente) {
        this.nroResolucionContribuyente = nroResolucionContribuyente;
    }

    public Character getReinicioContadores() {
        return reinicioContadores;
    }
    
    public boolean isReinicioContadores(){
        return reinicioContadores == null ? false : reinicioContadores.equals('S');
    }

    public void setReinicioContadores(Character reinicioContadores) {
        this.reinicioContadores = reinicioContadores;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public BigDecimal getPorcentajeRetencion() {
        return porcentajeRetencion;
    }

    public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }
    
    public LineaEnTicket getDireccionEmpresaAsLineas() {
        return new LineaEnTicket("MATRIZ : " + this.domicilio);
    }
}
