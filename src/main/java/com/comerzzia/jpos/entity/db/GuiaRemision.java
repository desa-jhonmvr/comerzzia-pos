/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.gui.guiasremision.JNuevaGuiaRemision2;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_GUIA_REMISION_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GuiaRemision.findAll", query = "SELECT g FROM GuiaRemision g"),
    @NamedQuery(name = "GuiaRemision.findByUidGuiaRemision", query = "SELECT g FROM GuiaRemision g WHERE g.uidGuiaRemision = :uidGuiaRemision"),
    @NamedQuery(name = "GuiaRemision.findByCodalm", query = "SELECT g FROM GuiaRemision g WHERE g.codalm = :codalm"),
    @NamedQuery(name = "GuiaRemision.findByCodcaja", query = "SELECT g FROM GuiaRemision g WHERE g.codcaja = :codcaja"),
    @NamedQuery(name = "GuiaRemision.findByIdGuiaRemision", query = "SELECT g FROM GuiaRemision g WHERE g.idGuiaRemision = :idGuiaRemision"),
    @NamedQuery(name = "GuiaRemision.findByFecha", query = "SELECT g FROM GuiaRemision g WHERE g.fecha = :fecha"),
    @NamedQuery(name = "GuiaRemision.findByIdUsuario", query = "SELECT g FROM GuiaRemision g WHERE g.idUsuario = :idUsuario"),
    @NamedQuery(name = "GuiaRemision.findByTipoDocumento", query = "SELECT g FROM GuiaRemision g WHERE g.tipoDocumento = :tipoDocumento"),
    @NamedQuery(name = "GuiaRemision.findByFechaInicio", query = "SELECT g FROM GuiaRemision g WHERE g.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "GuiaRemision.findByFechaFin", query = "SELECT g FROM GuiaRemision g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "GuiaRemision.findByNumDocumento", query = "SELECT g FROM GuiaRemision g WHERE g.numDocumento = :numDocumento"),
    @NamedQuery(name = "GuiaRemision.findByCodLocal1", query = "SELECT g FROM GuiaRemision g WHERE g.codLocal1 = :codLocal1"),
    @NamedQuery(name = "GuiaRemision.findByCodLocal2", query = "SELECT g FROM GuiaRemision g WHERE g.codLocal2 = :codLocal2"),
    @NamedQuery(name = "GuiaRemision.findByMotivoTraslado", query = "SELECT g FROM GuiaRemision g WHERE g.motivoTraslado = :motivoTraslado"),
    @NamedQuery(name = "GuiaRemision.findByDestNombre", query = "SELECT g FROM GuiaRemision g WHERE g.destNombre = :destNombre"),
    @NamedQuery(name = "GuiaRemision.findByDestCedula", query = "SELECT g FROM GuiaRemision g WHERE g.destCedula = :destCedula"),
    @NamedQuery(name = "GuiaRemision.findByDestCodalm", query = "SELECT g FROM GuiaRemision g WHERE g.destCodalm = :destCodalm"),
    @NamedQuery(name = "GuiaRemision.findByDestCiudad", query = "SELECT g FROM GuiaRemision g WHERE g.destCiudad = :destCiudad"),
    @NamedQuery(name = "GuiaRemision.findByDestDireccion", query = "SELECT g FROM GuiaRemision g WHERE g.destDireccion = :destDireccion"),
    @NamedQuery(name = "GuiaRemision.findByTransNombre", query = "SELECT g FROM GuiaRemision g WHERE g.transNombre = :transNombre"),
    @NamedQuery(name = "GuiaRemision.findByTransCedula", query = "SELECT g FROM GuiaRemision g WHERE g.transCedula = :transCedula"),
    @NamedQuery(name = "GuiaRemision.findByTransDireccion", query = "SELECT g FROM GuiaRemision g WHERE g.transDireccion = :transDireccion"),
    @NamedQuery(name = "GuiaRemision.findByTransPlaca", query = "SELECT g FROM GuiaRemision g WHERE g.transPlaca = :transPlaca"),
    @NamedQuery(name = "GuiaRemision.findByBienesTransportados", query = "SELECT g FROM GuiaRemision g WHERE g.bienesTransportados = :bienesTransportados"),
    @NamedQuery(name = "GuiaRemision.findByEstado", query = "SELECT g FROM GuiaRemision g WHERE g.estado = :estado")})
public class GuiaRemision implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_GUIA_REMISION")
    private String uidGuiaRemision;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;
    @Basic(optional = false)
    @Column(name = "ID_GUIA_REMISION")
    private long idGuiaRemision;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Basic(optional = false)
    @Column(name = "TIPO_DOCUMENTO")
    private Integer tipoDocumento;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "NUM_DOCUMENTO")
    private String numDocumento;
    @Column(name = "COD_LOCAL_1")
    private String codLocal1;
    @Column(name = "COD_LOCAL_2")
    private String codLocal2;
    @Column(name = "MOTIVO_TRASLADO")
    private String motivoTraslado;
    @Column(name = "DEST_NOMBRE")
    private String destNombre;
    @Column(name = "DEST_CEDULA")
    private String destCedula;
    @Column(name = "DEST_CODALM")
    private String destCodalm;
    @Column(name = "DEST_CIUDAD")
    private String destCiudad;
    @Column(name = "DEST_DIRECCION")
    private String destDireccion;
    @Column(name = "TRANS_NOMBRE")
    private String transNombre;
    @Column(name = "TRANS_CEDULA")
    private String transCedula;
    @Column(name = "TRANS_DIRECCION")
    private String transDireccion;
    @Column(name = "TRANS_PLACA")
    private String transPlaca;
    @Column(name = "BIENES_TRANSPORTADOS")
    private String bienesTransportados;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private char estado;
    @Basic(optional = false)
    @Column(name = "PROCESADO")
    private char procesado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guiaRemision", fetch = FetchType.LAZY)
    private List<GuiaRemisionDetalle> guiaRemisionDetalleList;
    @Column(name = "UID_TICKET_REF")
    private String uidTicketRef;
    @Transient 
    private List<LineaTicket> lineas; // almacena las lineas del ticket consultado
   
    public GuiaRemision() {
    }

    public GuiaRemision(String uidGuiaRemision) {
        this.uidGuiaRemision = uidGuiaRemision;
    }

    public GuiaRemision(String uidGuiaRemision, String codalm, String codcaja, long idGuiaRemision, Date fecha, Integer tipoDocumento, char estado) {
        this.uidGuiaRemision = uidGuiaRemision;
        this.codalm = codalm;
        this.codcaja = codcaja;
        this.idGuiaRemision = idGuiaRemision;
        this.fecha = fecha;
        this.tipoDocumento = tipoDocumento;
        this.estado = estado;
    }

    public String getUidGuiaRemision() {
        return uidGuiaRemision;
    }

    public void setUidGuiaRemision(String uidGuiaRemision) {
        this.uidGuiaRemision = uidGuiaRemision;
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

    public long getIdGuiaRemision() {
        return idGuiaRemision;
    }

    public void setIdGuiaRemision(long idGuiaRemision) {
        this.idGuiaRemision = idGuiaRemision;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getCodLocal1() {
        return codLocal1;
    }

    public void setCodLocal1(String codLocal1) {
        this.codLocal1 = codLocal1;
    }

    public String getCodLocal2() {
        return codLocal2;
    }

    public void setCodLocal2(String codLocal2) {
        this.codLocal2 = codLocal2;
    }

    public String getMotivoTraslado() {
        return motivoTraslado;
    }

    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public String getDestNombre() {
        return destNombre;
    }

    public void setDestNombre(String destNombre) {
        this.destNombre = destNombre;
    }

    public String getDestCedula() {
        return destCedula;
    }

    public void setDestCedula(String destCedula) {
        this.destCedula = destCedula;
    }

    public String getDestCodalm() {
        return destCodalm;
    }

    public void setDestCodalm(String destCodalm) {
        this.destCodalm = destCodalm;
    }

    public String getDestCiudad() {
        return destCiudad;
    }

    public void setDestCiudad(String destCiudad) {
        this.destCiudad = destCiudad;
    }

    public String getDestDireccion() {
        return destDireccion;
    }

    public void setDestDireccion(String destDireccion) {
        this.destDireccion = destDireccion;
    }

    public String getTransNombre() {
        return transNombre;
    }

    public void setTransNombre(String transNombre) {
        this.transNombre = transNombre;
    }

    public String getTransCedula() {
        return transCedula;
    }

    public void setTransCedula(String transCedula) {
        this.transCedula = transCedula;
    }

    public String getTransDireccion() {
        return transDireccion;
    }

    public void setTransDireccion(String transDireccion) {
        this.transDireccion = transDireccion;
    }

    public String getBienesTransportados() {
        return bienesTransportados;
    }

    public void setBienesTransportados(String bienesTransportados) {
        this.bienesTransportados = bienesTransportados;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<GuiaRemisionDetalle> getGuiaRemisionDetalleList() {
        return guiaRemisionDetalleList;
    }

    public void setGuiaRemisionDetalleList(List<GuiaRemisionDetalle> guiaRemisionDetalleList) {
        this.guiaRemisionDetalleList = guiaRemisionDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidGuiaRemision != null ? uidGuiaRemision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GuiaRemision)) {
            return false;
        }
        GuiaRemision other = (GuiaRemision) object;
        if ((this.uidGuiaRemision == null && other.uidGuiaRemision != null) || (this.uidGuiaRemision != null && !this.uidGuiaRemision.equals(other.uidGuiaRemision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.GuiaRemision[ uidGuiaRemision=" + uidGuiaRemision + " ]";
    }

    public List<LineaTicket> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaTicket> lineas) {
        this.lineas = lineas;
    }

    public String getUidTicketRef() {
        return uidTicketRef;
    }

    public void setUidTicketRef(String uidTicketRef) {
        this.uidTicketRef = uidTicketRef;
    }

    public char getProcesado() {
        return procesado;
    }

    public void setProcesado(char procesado) {
        this.procesado = procesado;
    }

    public boolean isTipoFactura() {
        return (this.tipoDocumento == JNuevaGuiaRemision2.TIPO_FACTURA);
    }

    public String getTransPlaca() {
        return transPlaca;
    }

    public void setTransPlaca(String transPlaca) {
        this.transPlaca = transPlaca;
    }
  
}
