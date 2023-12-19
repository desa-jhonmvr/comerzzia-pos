/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
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
@Table(name = "X_PLAN_NOVIOS_ABO_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AbonoPlanNovio.findAll", query = "SELECT a FROM AbonoPlanNovio a"),
    @NamedQuery(name = "AbonoPlanNovio.findByIdPlan", query = "SELECT a FROM AbonoPlanNovio a WHERE a.abonoPlanNovioPK.idPlan = :idPlan"),
    @NamedQuery(name = "AbonoPlanNovio.findByIdAbono", query = "SELECT a FROM AbonoPlanNovio a WHERE a.abonoPlanNovioPK.idAbono = :idAbono"),
    @NamedQuery(name = "AbonoPlanNovio.findByTipo", query = "SELECT a FROM AbonoPlanNovio a WHERE a.tipo = :tipo"),
    @NamedQuery(name = "AbonoPlanNovio.findByFecha", query = "SELECT a FROM AbonoPlanNovio a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AbonoPlanNovio.findByIdCajero", query = "SELECT a FROM AbonoPlanNovio a WHERE a.idCajero = :idCajero"),
    @NamedQuery(name = "AbonoPlanNovio.findByCodVendedor", query = "SELECT a FROM AbonoPlanNovio a WHERE a.codVendedor = :codVendedor"),
    @NamedQuery(name = "AbonoPlanNovio.findByCodcaja", query = "SELECT a FROM AbonoPlanNovio a WHERE a.codcaja = :codcaja"),
    @NamedQuery(name = "AbonoPlanNovio.findByCantidadSinDcto", query = "SELECT a FROM AbonoPlanNovio a WHERE a.cantidadSinDcto = :cantidadSinDcto"),
    @NamedQuery(name = "AbonoPlanNovio.findByCantidadConDcto", query = "SELECT a FROM AbonoPlanNovio a WHERE a.cantidadConDcto = :cantidadConDcto"),
    @NamedQuery(name = "AbonoPlanNovio.findByProcesado", query = "SELECT a FROM AbonoPlanNovio a WHERE a.procesado = :procesado"),
    @NamedQuery(name = "AbonoPlanNovio.findByCodalm", query = "SELECT a FROM AbonoPlanNovio a WHERE a.abonoPlanNovioPK.codalm = :codalm"),
    @NamedQuery(name = "AbonoPlanNovio.findByAnulado", query = "SELECT a FROM AbonoPlanNovio a WHERE a.anulado = :anulado")})
public class AbonoPlanNovio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AbonoPlanNovioPK abonoPlanNovioPK;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private char tipo;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "ID_CAJERO")
    private String idCajero;
    @Column(name = "COD_VENDEDOR")
    private String codVendedor;
    @Column(name = "CODCAJA")
    private String codcaja;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "CANTIDAD_SIN_DCTO")
    private BigDecimal cantidadSinDcto;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_CON_DCTO")
    private BigDecimal cantidadConDcto;
    @Basic(optional = false)
    @Lob
    @Column(name = "PAGOS")
    private byte[] pagos;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Column(name = "PROCESADO_TIENDA")
    private Character procesadoTienda;    
    @Column(name = "ANULADO")
    private Character anulado;
    @Basic(optional = false)
    @Column(name = "FECHA_LIQUIDACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLiquidacion;
    @Column(name = "ESTADO_LIQUIDACION")
    private String estadoLiquidacion;
    @Column(name = "UID_REFERENCIA")
    private String uidReferencia;
    @Column(name = "CODALM_INVITADO")
    private String codAlmInvitado;

    @JoinColumns({
        @JoinColumn(name = "ID_PLAN", referencedColumnName = "ID_PLAN", insertable = false, updatable = false),
        @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanNovio planNovio;
    @JoinColumns({
        @JoinColumn(name = "ID_PLAN", referencedColumnName = "ID_PLAN", insertable = false, updatable = false),
        @JoinColumn(name = "ID_INVITADO", referencedColumnName = "ID_INVITADO"),
        @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false),
        @JoinColumn(name = "CODALM_INVITADO", referencedColumnName = "CODALM_INVITADO", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InvitadoPlanNovio invitadoPlanNovio;

    @Transient
    private String nombreAlmacenAbono;
    
    public AbonoPlanNovio() {
    }

    public AbonoPlanNovio(AbonoPlanNovioPK abonoPlanNovioPK) {
        this.abonoPlanNovioPK = abonoPlanNovioPK;
    }

    public AbonoPlanNovio(BigInteger idPlan, BigInteger idAbono, String codAlm) {
        this.abonoPlanNovioPK = new AbonoPlanNovioPK(idPlan, idAbono, codAlm);
    }

    public AbonoPlanNovioPK getAbonoPlanNovioPK() {
        return abonoPlanNovioPK;
    }

    public void setAbonoPlanNovioPK(AbonoPlanNovioPK abonoPlanNovioPK) {
        this.abonoPlanNovioPK = abonoPlanNovioPK;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(String idCajero) {
        this.idCajero = idCajero;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public BigDecimal getCantidadSinDcto() {
        return cantidadSinDcto;
    }

    public void setCantidadSinDcto(BigDecimal cantidadSinDcto) {
        this.cantidadSinDcto = cantidadSinDcto;
    }

    public BigDecimal getCantidadConDcto() {
        return cantidadConDcto;
    }

    public void setCantidadConDcto(BigDecimal cantidadConDcto) {
        this.cantidadConDcto = cantidadConDcto;
    }

    public byte[] getPagos() {
        return pagos;
    }

    public void setPagos(byte[] pagos) {
        this.pagos = pagos;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public Character getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Character procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }

    public PlanNovio getPlanNovio() {
        return planNovio;
    }

    public void setPlanNovio(PlanNovio planNovio) {
        this.planNovio = planNovio;
    }

    public InvitadoPlanNovio getInvitadoPlanNovio() {
        return invitadoPlanNovio;
    }

    public void setInvitadoPlanNovio(InvitadoPlanNovio invitadoPlanNovio) {
        this.invitadoPlanNovio = invitadoPlanNovio;
    }
    
    public Character getAnulado() {
        return anulado;
    }

    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }    

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public String getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(String estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public String getUidReferencia() {
        return uidReferencia;
    }

    public void setUidReferencia(String uidReferencia) {
        this.uidReferencia = uidReferencia;
    }

    public String getCodAlmInvitado() {
        return codAlmInvitado;
    }

    public void setCodAlmInvitado(String codAlmInvitado) {
        this.codAlmInvitado = codAlmInvitado;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (abonoPlanNovioPK != null ? abonoPlanNovioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbonoPlanNovio)) {
            return false;
        }
        AbonoPlanNovio other = (AbonoPlanNovio) object;
        if ((this.abonoPlanNovioPK == null && other.abonoPlanNovioPK != null) || (this.abonoPlanNovioPK != null && !this.abonoPlanNovioPK.equals(other.abonoPlanNovioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.AbonoPlanNovio[ abonoPlanNovioPK=" + abonoPlanNovioPK + " ]";
    }

    public String getNombreAlmacenAbono() {
        return nombreAlmacenAbono;
    }

    public void setNombreAlmacenAbono(String nombreAlmacenAbono) {
        this.nombreAlmacenAbono = nombreAlmacenAbono;
    }
    
}
