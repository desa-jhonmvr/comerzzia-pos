/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import es.mpsistemas.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
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
@Table(name = "x_bonos_tbl")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bono.findAll", query = "SELECT b FROM Bono b"),
    @NamedQuery(name = "Bono.findByIdBono", query = "SELECT b FROM Bono b WHERE b.bonoPK.idBono = :idBono"),
    @NamedQuery(name = "Bono.findByCodalm", query = "SELECT b FROM Bono b WHERE b.bonoPK.codalm = :codalm"),
    @NamedQuery(name = "Bono.findByIdTipoBono", query = "SELECT b FROM Bono b WHERE b.idTipoBono = :idTipoBono"),
    @NamedQuery(name = "Bono.findByFechaExpedicion", query = "SELECT b FROM Bono b WHERE b.fechaExpedicion = :fechaExpedicion"),
    @NamedQuery(name = "Bono.findByFechaInicio", query = "SELECT b FROM Bono b WHERE b.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Bono.findByFechaCaducidad", query = "SELECT b FROM Bono b WHERE b.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "Bono.findByDescuento", query = "SELECT b FROM Bono b WHERE b.descuento = :descuento"),
    @NamedQuery(name = "Bono.findBySoloSinPromocion", query = "SELECT b FROM Bono b WHERE b.soloSinPromocion = :soloSinPromocion"),
    @NamedQuery(name = "Bono.findByUtilizado", query = "SELECT b FROM Bono b WHERE b.utilizado = :utilizado"),
    @NamedQuery(name = "Bono.findByReferenciaUso", query = "SELECT b FROM Bono b WHERE b.referenciaUso = :referenciaUso"),
    @NamedQuery(name = "Bono.findByTipoReferenciaUso", query = "SELECT b FROM Bono b WHERE b.tipoReferenciaUso = :tipoReferenciaUso"),
    @NamedQuery(name = "Bono.findByReferenciaOrigen", query = "SELECT b FROM Bono b WHERE b.referenciaOrigen = :referenciaOrigen"),
    @NamedQuery(name = "Bono.findByTipoReferenciaOrigen", query = "SELECT b FROM Bono b WHERE b.tipoReferenciaOrigen = :tipoReferenciaOrigen")})
public class Bono implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BonoPK bonoPK;
    @JoinColumn(name = "id_tipo_bono", referencedColumnName = "id_tipo_bono")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoBono idTipoBono;
    @Basic(optional = false)
    @Column(name = "fecha_expedicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpedicion;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Basic(optional = false)
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Basic(optional = false)
    @Column(name = "solo_sin_promocion")
    private char soloSinPromocion;
    @Basic(optional = false)
    @Column(name = "utilizado")
    private char utilizado;
    @Column(name = "referencia_uso")
    private String referenciaUso;
    @Column(name = "tipo_referencia_uso")
    private String tipoReferenciaUso;
    @Column(name = "referencia_origen")
    private String referenciaOrigen;
    @Column(name = "tipo_referencia_origen")
    private String tipoReferenciaOrigen;
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "procesado")
    private Character procesado;
    @Column(name = "mensaje_proceso")
    private String mensaje_proceso;  
    @Column(name = "fecha_proceso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProceso;
    @Column(name = "codcli")
    private String codCliente;
    @Column(name = "uid_nota_credito")
    private String uidNotaCredito;
    @Column(name = "uid_reservacion")
    private String uidReservacion;
    @Column(name = "saldo_usado")
    private BigDecimal saldoUsado;    
    
    @Transient 
    private String tipoTransaccion;
    @Transient 
    private String transaccion;
    @Transient
    private String observaciones;
    
    public Bono() {
    }

    public Bono(BonoPK bonoPK) {
        this.bonoPK = bonoPK;
    }

    public Bono(BonoPK bonoPK, long idTipoBono, Date fechaExpedicion, Date fechaInicio, Date fechaCaducidad, BigDecimal descuento, char soloSinPromocion, char utilizado) {
        this.bonoPK = bonoPK;
        this.idTipoBono = new TipoBono(idTipoBono);
        this.fechaExpedicion = fechaExpedicion;
        this.fechaInicio = fechaInicio;
        this.fechaCaducidad = fechaCaducidad;
        this.descuento = descuento;
        this.soloSinPromocion = soloSinPromocion;
        this.utilizado = utilizado;
    }

    public Bono(long idBonoNR, String codalm) {
        String strPreIdBono = Cadena.getRandomNumerico(3);
        Long idBono = new Long(idBonoNR+strPreIdBono);
        this.bonoPK = new BonoPK(idBono, codalm);
    }

    public BonoPK getBonoPK() {
        return bonoPK;
    }

    public void setBonoPK(BonoPK bonoPK) {
        this.bonoPK = bonoPK;
    }

    public TipoBono getIdTipoBono() {
        return idTipoBono;
    }

    public void setIdTipoBono(long idTipoBono) {
        this.idTipoBono = new TipoBono(idTipoBono);
    }
    public void setIdTipoBono(TipoBono tipoBono) {
        this.idTipoBono = tipoBono;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }
    
   public String getFechaInicioAsString() {
        return (new Fecha(fechaInicio)).getString("dd/MM/yyy");
   }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }
    
    public String getFechaCaducidadAsString() {
        return (new Fecha(fechaCaducidad)).getString("dd/MM/yyy");
    }
    

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public char getSoloSinPromocion() {
        return soloSinPromocion;
    }

    public void setSoloSinPromocion(char soloSinPromocion) {
        this.soloSinPromocion = soloSinPromocion;
    }

    public char getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(char utilizado) {
        this.utilizado = utilizado;
    }

    public String getReferenciaUso() {
        return referenciaUso;
    }

    public void setReferenciaUso(String referenciaUso) {
        this.referenciaUso = referenciaUso;
    }

    public String getTipoReferenciaUso() {
        return tipoReferenciaUso;
    }

    public void setTipoReferenciaUso(String tipoReferenciaUso) {
        this.tipoReferenciaUso = tipoReferenciaUso;
    }

    public String getReferenciaOrigen() {
        return referenciaOrigen;
    }

    public void setReferenciaOrigen(String referenciaOrigen) {
        this.referenciaOrigen = referenciaOrigen;
    }

    public String getTipoReferenciaOrigen() {
        return tipoReferenciaOrigen;
    }

    public void setTipoReferenciaOrigen(String tipoReferenciaOrigen) {
        int posicionDeCorte = tipoReferenciaOrigen.lastIndexOf("_");
        if (posicionDeCorte>0){
            this.tipoReferenciaOrigen = tipoReferenciaOrigen.substring(0, posicionDeCorte);
        }
        else{
            this.tipoReferenciaOrigen = tipoReferenciaOrigen;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bonoPK != null ? bonoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Bono)) {
            return false;
        }
        Bono other = (Bono) object;
        if ((this.bonoPK == null && other.bonoPK != null) || (this.bonoPK != null && !this.bonoPK.equals(other.bonoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Bono[ bonoPK=" + bonoPK + " ]";
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public String getMensaje_proceso() {
        return mensaje_proceso;
    }

    public void setMensaje_proceso(String mensaje_proceso) {
        this.mensaje_proceso = mensaje_proceso;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }
    
    public boolean hayUidNotaCredito() {
        return this.uidNotaCredito!=null;
    }
    
    public boolean hayUidReservacion() {
        return this.uidReservacion!=null;
    }
    
    public LineaEnTicket getFechaExpedicionAsLineas() {
        return new LineaEnTicket((new Fecha(this.fechaExpedicion)).getString("dd/MM/yyyy"));
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion;
    }
    
    public boolean isObservacionLlena(){
        if(observaciones == null || observaciones.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    
    public LineaEnTicket getObservacionesLineaEnTicket(){
        return new LineaEnTicket(getObservaciones(), false, true);
    }    

    public BigDecimal getSaldoUsado() {
        return saldoUsado;
    }

    public void setSaldoUsado(BigDecimal saldoUsado) {
        this.saldoUsado = saldoUsado;
    }
    
}
