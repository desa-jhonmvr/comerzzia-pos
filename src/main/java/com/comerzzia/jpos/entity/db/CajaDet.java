/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.login.CajaSesion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_CAJA_DET_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaDet.findAll", query = "SELECT c FROM CajaDet c"),
    @NamedQuery(name = "CajaDet.findByLinea", query = "SELECT c FROM CajaDet c WHERE c.cajaDetPK.linea = :linea"),
    @NamedQuery(name = "CajaDet.findByFecha", query = "SELECT c FROM CajaDet c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CajaDet.findByCargo", query = "SELECT c FROM CajaDet c WHERE c.cargo = :cargo"),
    @NamedQuery(name = "CajaDet.findByAbono", query = "SELECT c FROM CajaDet c WHERE c.abono = :abono"),
    @NamedQuery(name = "CajaDet.findByConcepto", query = "SELECT c FROM CajaDet c WHERE c.concepto = :concepto"),
    @NamedQuery(name = "CajaDet.findByDocumento", query = "SELECT c FROM CajaDet c WHERE c.documento = :documento"),
    @NamedQuery(name = "CajaDet.findByIdDocumento", query = "SELECT c FROM CajaDet c WHERE c.idDocumento = :idDocumento"),
    @NamedQuery(name = "CajaDet.findByTipo", query = "SELECT c FROM CajaDet c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CajaDet.findByUidCajeroCaja", query = "SELECT c FROM CajaDet c WHERE c.cajaDetPK.uidCajeroCaja = :uidCajeroCaja")})
public class CajaDet implements Serializable {
    private final static char TIPO_MOVIMIENTO='M';
    private final static char TIPO_VENTA='T';
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CajaDetPK cajaDetPK;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CARGO")
    private BigDecimal cargo;
    @Column(name = "ABONO")
    private BigDecimal abono;
    @Basic(optional = false)
    @Column(name = "CONCEPTO")
    private String concepto;
    @Column(name = "DOCUMENTO")
    private String documento;
    @JoinColumn(name = "CODMEDPAG", referencedColumnName = "CODMEDPAG")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MedioPagoCaja codmedpag;
    @Column(name = "ID_DOCUMENTO")
    private String idDocumento;
    @JoinColumn(name = "UID_DIARIO_CAJA", referencedColumnName = "UID_DIARIO_CAJA")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Caja caja;
    @JoinColumn(name = "UID_CAJERO_CAJA", referencedColumnName = "UID_CAJERO_CAJA", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CajaCajero cajaCajero;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private char tipo;
    @Basic(optional = true)
    @Column(name = "DIFERIDO")
    private Character diferido;

    @Basic(optional = true)
    @Column(name = "INTERES")
    private BigDecimal interes;
    @Basic(optional = true)
    @Column(name = "NUM_CUOTAS")
    private Integer numCuotas;
    @Basic(optional = true)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Basic(optional = true)
    @Column(name = "ID_ABONO")
    private Long idAbono;
    @Column(name = "PREFACTURA")
    private String prefactura;
    
    @Transient
    private String cruzaEfectivo;
    
    public CajaDet() {
    }

    public CajaDet(Caja cajaActual,CajaCajero cajaCajero, int linea, String concepto, String documento, Date fecha, BigDecimal importe, MedioPagoBean medioPago, char tipo, Character diferido) {
        createCajaDet(cajaActual, cajaCajero, linea, concepto, documento, medioPago, tipo, diferido, fecha, importe);
    }

    public CajaDet(CajaSesion cajaSesion, int linea, String concepto, String documento, Date fecha, BigDecimal importe, MedioPagoBean medioPago, char tipo, Character diferido) {
        createCajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), linea, concepto, documento, medioPago, tipo, diferido, fecha, importe);
    }
    
    public CajaDet(Caja cajaActual,CajaCajero cajaCajero, int linea, String concepto, String documento, Date fecha, BigDecimal importe, MedioPagoBean medioPago, char tipo, Character diferido, String idDocumento) {
        createCajaDet2(cajaActual, cajaCajero, linea, concepto, documento, medioPago, tipo, diferido, fecha, importe, idDocumento);
    }
    
    
    private void createCajaDet2(Caja cajaActual, CajaCajero cajaCajero, int linea, String concepto, String documento, MedioPagoBean medioPago, char tipo, Character diferido, Date fecha, BigDecimal importe, String idDocumento) {
        this.numCuotas = 1;
        this.interes = BigDecimal.ZERO;
        this.caja = cajaActual;
        this.cajaDetPK = new CajaDetPK(cajaCajero.getUidCajeroCaja(), linea);
        this.caja= cajaActual;
        this.concepto = concepto;
        this.documento = documento;
        this.codmedpag = new MedioPagoCaja(medioPago);
        this.tipo=tipo;
        this.diferido = diferido;
        this.cajaCajero=cajaCajero;
        
        this.idDocumento = idDocumento;
        
        if (fecha == null) {
            this.fecha = new Date();
        }
        else {
            this.fecha = fecha;
        }

        if (importe.compareTo(BigDecimal.ZERO)>0) {
            this.setAbono(BigDecimal.ZERO);
            this.setCargo(importe);            
        }
        else {
            this.setAbono(importe);
            this.setCargo(BigDecimal.ZERO);
        }
    }

    private void createCajaDet(Caja cajaActual, CajaCajero cajaCajero, int linea, String concepto, String documento, MedioPagoBean medioPago, char tipo, Character diferido, Date fecha, BigDecimal importe) {
        this.numCuotas = 1;
        this.interes = BigDecimal.ZERO;
        this.caja = cajaActual;
        this.cajaDetPK = new CajaDetPK(cajaCajero.getUidCajeroCaja(), linea);
        this.caja= cajaActual;
        this.concepto = concepto;
        this.documento = documento;
        this.codmedpag = new MedioPagoCaja(medioPago);
        this.tipo=tipo;
        this.diferido = diferido;
        this.cajaCajero=cajaCajero;
        if (fecha == null) {
            this.fecha = new Date();
        }
        else {
            this.fecha = fecha;
        }

        if (importe.compareTo(BigDecimal.ZERO)>0) {
            this.setAbono(BigDecimal.ZERO);
            this.setCargo(importe);            
        }
        else {
            this.setAbono(importe);
            this.setCargo(BigDecimal.ZERO);
        }
    }


    
    public CajaDet(Caja cajaActual, CajaCajero cajaCajero, int linea, String concepto, String documento, BigDecimal importe, String idDocumento, MedioPagoBean codmedpag, char tipo, char diferido) {
        this(cajaActual, cajaCajero,linea, concepto, documento, null, importe, codmedpag, tipo, diferido);
        this.cajaCajero=cajaCajero;             
        this.idDocumento = idDocumento;       
    }
   
    public CajaDet(CajaDetPK cajaDetPK) {
        this.cajaDetPK = cajaDetPK;
    }

    public CajaDet(CajaDetPK cajaDetPK, Date fecha, String concepto) {
        this.cajaDetPK = cajaDetPK;
        this.fecha = fecha;
        this.concepto = concepto;
    }

    public CajaDet(String uidCajeroCaja, int linea) {
        this.cajaDetPK = new CajaDetPK(uidCajeroCaja, linea);
    }

    public CajaDetPK getCajaDetPK() {
        return cajaDetPK;
    }

    public void setCajaDetPK(CajaDetPK cajaDetPK) {
        this.cajaDetPK = cajaDetPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public CajaCajero getCajaCajero() {
        return cajaCajero;
    }

    public void setCajaCajero(CajaCajero cajaCajero) {
        this.cajaCajero = cajaCajero;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCargo() {
        return cargo;
    }

    public void setCargo(BigDecimal cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cajaDetPK != null ? cajaDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CajaDet)) {
            return false;
        }
        CajaDet other = (CajaDet) object;
        if ((this.cajaDetPK == null && other.cajaDetPK != null) || (this.cajaDetPK != null && !this.cajaDetPK.equals(other.cajaDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.CajaDet[ cajaDetPK=" + cajaDetPK + " ]";
    }

    public MedioPagoCaja getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(MedioPagoCaja codmedpag) {
        this.codmedpag = codmedpag;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public char getDiferido() {
        return diferido;
    }

    public void setDiferido(char diferido) {
        this.diferido = diferido;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public Integer getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(Integer numCuotas) {
        this.numCuotas = numCuotas;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }  

    public Long getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(Long idAbono) {
        this.idAbono = idAbono;
    }

    public String getCruzaEfectivo() {
        return cruzaEfectivo;
    }

    public void setCruzaEfectivo(String cruzaEfectivo) {
        this.cruzaEfectivo = cruzaEfectivo;
    }

    public String getPrefactura() {
        return prefactura;
    }

    public void setPrefactura(String prefactura) {
        this.prefactura = prefactura;
    }
    
}
