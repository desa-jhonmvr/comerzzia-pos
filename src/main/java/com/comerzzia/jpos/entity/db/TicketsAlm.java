/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_TICKETS_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketsAlm.findAll", query = "SELECT t FROM TicketsAlm t"),
    @NamedQuery(name = "TicketsAlm.findByUidTicket", query = "SELECT t FROM TicketsAlm t WHERE t.uidTicket = :uidTicket"),
    @NamedQuery(name = "TicketsAlm.findByIdTicket", query = "SELECT t FROM TicketsAlm t WHERE t.idTicket = :idTicket"),
    @NamedQuery(name = "TicketsAlm.findByFecha", query = "SELECT t FROM TicketsAlm t WHERE t.fecha = :fecha")})
public class TicketsAlm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;
    @Basic(optional = false)
    @Column(name = "ID_TICKET")
    private long idTicket;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Lob
    @Column(name = "TICKET")
    private byte[] ticket;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codCaja;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codAlm;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Column(name = "CODCLI")
    private String codCli;
    @Column(name = "ANULADO")
    private Character anulado;    
    @Column(name = "USUARIO")
    private String usuario;    
    @Column(name = "TOTAL_CON_DSTO_CON_IVA")
    private BigDecimal totalConDstoConIva;    
    @Column(name = "TOTAL_CON_DSTO_SIN_IVA")
    private BigDecimal totalConDstoSinIva;    
    @Column(name = "TOTAL_SIN_DSTO_CON_IVA")
    private BigDecimal totalSinDstoConIva;    
    @Column(name = "TOTAL_SIN_DSTO_SIN_IVA")
    private BigDecimal totalSinDstoSinIva;
    @Column(name ="MOTIVO_ANULACION")
    private String motivoAnulacion;
    @Column(name="AUTORIZADOR_ANULACION")
    private String autorizadorAnulacion;
    @Basic(optional = false)
    @Lob
    @Column(name = "E_TICKET")
    private byte[] eTicket;    
    @Column(name = "FECHA_ANULACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;    
    @Column(name ="FACT_DOCUMENTO")
    private String factDocumento;
    @Column(name ="FACT_TIPO_DOC")
    private String factTipoDoc;
    @Column(name ="FACT_NOMBRE")
    private String factNombre;
    @Column(name ="FACT_EMAIL")
    private String factEmail;
    @Column(name ="FACT_TELEFONO")
    private String factTelefonon;
    @Column(name ="FACT_DIRECCION")
    private String factDireccion;
    @Column(name ="FACT_PROVINCIA")
    private String factProvincia;
    @JoinColumn(name = "UID_PREFACTURA", referencedColumnName = "UID_CAB_ID")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CabPrefactura cabPrefactura;
    @Column(name ="CLAVE_ACCESO")
    private String claveAcceso;
    @Column(name ="ESTADO_FE")
    private String estadoFE;

    @Column(name = "TOTAL_INTERES")
    private BigDecimal totalInteres;
    public TicketsAlm() {
        this.procesado='N';
        this.anulado='N';
    }

    public TicketsAlm(String uidTicket) {
        this.uidTicket = uidTicket;
        this.procesado='N';
        this.anulado='N';
    }

    public TicketsAlm(String uidTicket, long idTicket, Date fecha, byte[] ticket,String codCaja) {
        this.uidTicket = uidTicket;
        this.idTicket = idTicket;
        this.fecha = fecha;
        this.ticket = ticket;
        this.codCaja= codCaja;
        this.procesado='N';
        this.anulado='N';
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(long idTicket) {
        this.idTicket = idTicket;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Serializable getTicket() {
        return ticket;
    }
    
    public byte[] getXMLTicket() {
        return ticket;
    }

    public void setTicket(byte[] ticket) {
        this.ticket = ticket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidTicket != null ? uidTicket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TicketsAlm)) {
            return false;
        }
        TicketsAlm other = (TicketsAlm) object;
        if ((this.uidTicket == null && other.uidTicket != null) || (this.uidTicket != null && !this.uidTicket.equals(other.uidTicket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.TicketsAlm[ uidTicket=" + uidTicket + " ]";
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }
     public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }
    
    public boolean isProcesado(){
        return getProcesado()!= null && getProcesado().equals('S');
    }

    public String getCodCli() {
        return codCli;
    }

    public void setCodCli(String codCli) {
        this.codCli = codCli;
    }

    public String getIdentificador() {
        return this.codAlm+"-"+this.codCaja+"-"+this.idTicket;
    }

    public Character getAnulado() {
        return anulado;
    }
    
    public boolean isAnulado() {
        return (anulado!=null && anulado.equals('S'));
    }

    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getTotalConDstoConIva() {
        return totalConDstoConIva;
    }

    public void setTotalConDstoConIva(BigDecimal totalConDstoConIva) {
        this.totalConDstoConIva = totalConDstoConIva;
    }

    public BigDecimal getTotalConDstoSinIva() {
        return totalConDstoSinIva;
    }

    public void setTotalConDstoSinIva(BigDecimal totalConDstoSinIva) {
        this.totalConDstoSinIva = totalConDstoSinIva;
    }

    public BigDecimal getTotalSinDstoConIva() {
        return totalSinDstoConIva;
    }

    public void setTotalSinDstoConIva(BigDecimal totalSinDstoConIva) {
        this.totalSinDstoConIva = totalSinDstoConIva;
    }

    public BigDecimal getTotalSinDstoSinIva() {
        return totalSinDstoSinIva;
    }

    public void setTotalSinDstoSinIva(BigDecimal totalSinDstoSinIva) {
        this.totalSinDstoSinIva = totalSinDstoSinIva;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }

    public String getAutorizadorAnulacion() {
        return autorizadorAnulacion;
    }

    public void setAutorizadorAnulacion(String autorizadorAnulacion) {
        this.autorizadorAnulacion = autorizadorAnulacion;
    }

    public byte[] geteTicket() {
        return eTicket;
    }

    public void seteTicket(byte[] eTicket) {
        this.eTicket = eTicket;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getFactDocumento() {
        return factDocumento;
    }

    public void setFactDocumento(String factDocumento) {
        this.factDocumento = factDocumento;
    }

    public String getFactTipoDoc() {
        return factTipoDoc;
    }

    public void setFactTipoDoc(String factTipoDoc) {
        this.factTipoDoc = factTipoDoc;
    }

    public String getFactNombre() {
        return factNombre;
    }

    public void setFactNombre(String factNombre) {
        this.factNombre = factNombre;
    }

    public String getFactEmail() {
        return factEmail;
    }

    public void setFactEmail(String factEmail) {
        this.factEmail = factEmail;
    }

    public String getFactTelefonon() {
        return factTelefonon;
    }

    public void setFactTelefonon(String factTelefonon) {
        this.factTelefonon = factTelefonon;
    }

    public String getFactDireccion() {
        return factDireccion;
    }

    public void setFactDireccion(String factDireccion) {
        this.factDireccion = factDireccion;
    }

    public String getFactProvincia() {
        return factProvincia;
    }

    public void setFactProvincia(String factProvincia) {
        this.factProvincia = factProvincia;
    }

    public CabPrefactura getCabPrefactura() {
        return cabPrefactura;
    }

    public void setCabPrefactura(CabPrefactura cabPrefactura) {
        this.cabPrefactura = cabPrefactura;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getEstadoFE() {
        return estadoFE;
    }

    public void setEstadoFE(String estadoFE) {
        this.estadoFE = estadoFE;
    }


    public BigDecimal getTotalInteres() {
        return totalInteres;
    }

    public void setTotalInteres(BigDecimal totalInteres) {
        this.totalInteres = totalInteres;
    }
}
