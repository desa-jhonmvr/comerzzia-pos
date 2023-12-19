/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumEstado;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "X_GARANTIAS_EXT_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GarantiaExtendidaReg.findAll", query = "SELECT g FROM GarantiaExtendidaReg g")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByUidTicket", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.garantiaExtendidaRegPK.uidTicket = :uidTicket")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodalm", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codalm = :codalm")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodcaja", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codcaja = :codcaja")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByIdTicket", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.idTicket = :idTicket")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByFechaHora", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.fechaHora = :fechaHora")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodcli", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codcli = :codcli")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByIdUsuario", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.idUsuario = :idUsuario")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodvendedor", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codvendedor = :codvendedor")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByIdLinea", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.garantiaExtendidaRegPK.idLinea = :idLinea")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodItem", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codItem = :codItem")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodmarca", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codmarca = :codmarca")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodart", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codart = :codart")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByCodbarras", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.codbarras = :codbarras")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByImporteOrigen", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.importeOrigen = :importeOrigen")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByImporteFinal", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.importeFinal = :importeFinal")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByGarantia", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.garantia = :garantia")
    ,
    @NamedQuery(name = "GarantiaExtendidaReg.findByImporteGarantia", query = "SELECT g FROM GarantiaExtendidaReg g WHERE g.importeGarantia = :importeGarantia")})
public class GarantiaExtendidaReg implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GarantiaExtendidaRegPK garantiaExtendidaRegPK;
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;
    @Basic(optional = false)
    @Column(name = "ID_TICKET")
    private long idTicket;
    @Basic(optional = false)
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "CODCLI")
    private String codcli;
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Column(name = "CODVENDEDOR")
    private String codvendedor;
    @Basic(optional = false)
    @Column(name = "COD_ITEM")
    private int codItem;
    @Basic(optional = false)
    @Column(name = "CODMARCA")
    private String codmarca;
    @Column(name = "CODMODELO")
    private String codmodelo;
    @Basic(optional = false)
    @Column(name = "CODART")
    private String codart;
    @Basic(optional = false)
    @Column(name = "CODBARRAS")
    private String codbarras;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "IMPORTE_ORIGEN")
    private BigDecimal importeOrigen;
    @Basic(optional = false)
    @Column(name = "IMPORTE_FINAL")
    private BigDecimal importeFinal;
    @Basic(optional = false)
    @Column(name = "GARANTIA")
    private short garantia;
    @Basic(optional = false)
    @Column(name = "IMPORTE_GARANTIA")
    private BigDecimal importeGarantia;
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "ESTADO")
    private EnumEstado estado;

    public GarantiaExtendidaReg() {
    }

    public GarantiaExtendidaReg(GarantiaExtendidaRegPK garantiaExtendidaRegPK) {
        this.garantiaExtendidaRegPK = garantiaExtendidaRegPK;
        this.estado = EnumEstado.ACTIVO;
    }

    public GarantiaExtendidaReg(GarantiaExtendidaRegPK garantiaExtendidaRegPK, String codalm, String codcaja, long idTicket, Date fechaHora, String codcli, int codItem, String codmarca, String codart, String codbarras, BigDecimal importeOrigen, BigDecimal importeFinal, short garantia, BigDecimal importeGarantia, String codmodelo, String observaciones) {
        this.garantiaExtendidaRegPK = garantiaExtendidaRegPK;
        this.codalm = codalm;
        this.codcaja = codcaja;
        this.idTicket = idTicket;
        this.fechaHora = fechaHora;
        this.codcli = codcli;
        this.codItem = codItem;
        this.codmarca = codmarca;
        this.codart = codart;
        this.codbarras = codbarras;
        this.importeOrigen = importeOrigen;
        this.importeFinal = importeFinal;
        this.garantia = garantia;
        this.importeGarantia = importeGarantia;
        this.codmodelo = codmodelo;
        this.observaciones = observaciones;
        this.estado = EnumEstado.ACTIVO;
    }

    public GarantiaExtendidaReg(String uidTicket, short idLinea, String uidTicketReferencia) {
        this.garantiaExtendidaRegPK = new GarantiaExtendidaRegPK(uidTicket, idLinea, uidTicketReferencia);
        this.estado = EnumEstado.ACTIVO;
    }

    public GarantiaExtendidaRegPK getGarantiaExtendidaRegPK() {
        return garantiaExtendidaRegPK;
    }

    public void setGarantiaExtendidaRegPK(GarantiaExtendidaRegPK garantiaExtendidaRegPK) {
        this.garantiaExtendidaRegPK = garantiaExtendidaRegPK;
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

    public long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(long idTicket) {
        this.idTicket = idTicket;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(String codvendedor) {
        this.codvendedor = codvendedor;
    }

    public int getCodItem() {
        return codItem;
    }

    public void setCodItem(int codItem) {
        this.codItem = codItem;
    }

    public String getCodmarca() {
        return codmarca;
    }

    public void setCodmarca(String codmarca) {
        this.codmarca = codmarca;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getCodbarras() {
        return codbarras;
    }

    public void setCodbarras(String codbarras) {
        this.codbarras = codbarras;
    }

    public BigDecimal getImporteOrigen() {
        return importeOrigen;
    }

    public void setImporteOrigen(BigDecimal importeOrigen) {
        this.importeOrigen = importeOrigen;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    public short getGarantia() {
        return garantia;
    }

    public void setGarantia(short garantia) {
        this.garantia = garantia;
    }

    public BigDecimal getImporteGarantia() {
        return importeGarantia;
    }

    public void setImporteGarantia(BigDecimal importeGarantia) {
        this.importeGarantia = importeGarantia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (garantiaExtendidaRegPK != null ? garantiaExtendidaRegPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GarantiaExtendidaReg)) {
            return false;
        }
        GarantiaExtendidaReg other = (GarantiaExtendidaReg) object;
        if ((this.garantiaExtendidaRegPK == null && other.garantiaExtendidaRegPK != null) || (this.garantiaExtendidaRegPK != null && !this.garantiaExtendidaRegPK.equals(other.garantiaExtendidaRegPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.GarantiaExtendidaReg[ garantiaExtendidaRegPK=" + garantiaExtendidaRegPK + " ]";
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad(Integer cantidad) {
        if (cantidad != null) {
            return this.cantidad;
        } else {
            return 0;
        }
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public String getCodmodelo() {
        return codmodelo;
    }

    public void setCodmodelo(String codmodelo) {
        this.codmodelo = codmodelo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EnumEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumEstado estado) {
        this.estado = estado;
    }

}
