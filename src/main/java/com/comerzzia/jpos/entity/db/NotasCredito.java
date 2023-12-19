/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.devoluciones.TagNotaCreditoXML;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "X_NOTAS_CREDITO_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasCredito.findAll", query = "SELECT n FROM NotasCredito n")
    ,
    @NamedQuery(name = "NotasCredito.findByUidNotaCredito", query = "SELECT n FROM NotasCredito n WHERE n.uidNotaCredito = :uidNotaCredito")
    ,
    @NamedQuery(name = "NotasCredito.findByIdNotaCredito", query = "SELECT n FROM NotasCredito n WHERE n.idNotaCredito = :idNotaCredito")
    ,
    @NamedQuery(name = "NotasCredito.findByCodcaja", query = "SELECT n FROM NotasCredito n WHERE n.codcaja = :codcaja")
    ,
    @NamedQuery(name = "NotasCredito.findByFecha", query = "SELECT n FROM NotasCredito n WHERE n.fecha = :fecha")
    ,
    @NamedQuery(name = "NotasCredito.findByFechaValidez", query = "SELECT n FROM NotasCredito n WHERE n.fechaValidez = :fechaValidez")
    ,
    @NamedQuery(name = "NotasCredito.findByTotal", query = "SELECT n FROM NotasCredito n WHERE n.total = :total")
    ,
    @NamedQuery(name = "NotasCredito.findBySaldo", query = "SELECT n FROM NotasCredito n WHERE n.saldo = :saldo")
    ,
    @NamedQuery(name = "NotasCredito.findBySaldoUsado", query = "SELECT n FROM NotasCredito n WHERE n.saldoUsado = :saldoUsado")
    ,
    @NamedQuery(name = "NotasCredito.findByCliente", query = "SELECT n FROM NotasCredito n WHERE n.codcli = :codcli")})

public class NotasCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_NOTA_CREDITO")
    private String uidNotaCredito;
    @Basic(optional = false)
    @Column(name = "ID_NOTA_CREDITO")
    private Long idNotaCredito;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "FECHA_VALIDEZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidez;
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Basic(optional = false)
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Basic(optional = true)
    @Column(name = "UID_DEVOLUCION")
    private String uidDevolucion;
    @Basic(optional = false)
    @Lob
    @Column(name = "NOTA_CREDITO")
    private byte[] notaCredito;
    @Column(name = "ANULADO")
    private Character anulado;
    @Column(name = "MOTIVO_ANULACION")
    private String motivoAnulacion;
    @Column(name = "AUTORIZADOR_ANULACION")
    private String autorizadorAnulacion;
    @Basic(optional = false)
    @Column(name = "SALDO_USADO")
    private BigDecimal saldoUsado;
    @Basic(optional = false)
    @Lob
    @Column(name = "E_NOTA_CREDITO")
    private byte[] eNotaCredito;
    @Column(name = "FECHA_ANULACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name = "CODCLI")
    private String codcli;
    @Basic(optional = true)
    @Column(name = "AUTORIZADOR")
    private String autorizador;
    @Column(name = "REFERENCIA_USO")
    private String referenciaUso;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DOCUMENTO")
    private String documento;
    @Column(name = "TIPO_DEVOLUCION")
    private Long tipoDevolucion;
    @Column(name = "LOCAL_ORIGEN")
    private String localOrigen;
    @Column(name = "CLAVE_ACCESO")
    private String claveAcceso;
    @Column(name = "FACT_DOCUMENTO")
    private String factDocumento;
    @Transient
    private String cliProceso;

    public NotasCredito() {
        this.procesado = 'N';
        this.anulado = 'N';
    }

    public NotasCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
        this.procesado = 'N';
        this.anulado = 'N';
    }

    public NotasCredito(String uidNotaCredito, Long idNotaCredito, String codcaja, Date fecha, Date fechaValidez, BigDecimal total, BigDecimal saldo, byte[] notaCredito) {
        this.uidNotaCredito = uidNotaCredito;
        this.idNotaCredito = idNotaCredito;
        this.codcaja = codcaja;
        this.fecha = fecha;
        this.fechaValidez = fechaValidez;
        this.total = total;
        this.saldo = saldo;
        this.notaCredito = notaCredito;
        this.procesado = 'N';
        this.anulado = 'N';
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }

    public Long getIdNotaCredito() {
        return idNotaCredito;
    }

    public void setIdNotaCredito(Long idNotaCredito) {
        this.idNotaCredito = idNotaCredito;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(Date fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public byte[] getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(byte[] notaCredito) {
        this.notaCredito = notaCredito;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidNotaCredito != null ? uidNotaCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof NotasCredito)) {
            return false;
        }
        NotasCredito other = (NotasCredito) object;
        if ((this.uidNotaCredito == null && other.uidNotaCredito != null) || (this.uidNotaCredito != null && !this.uidNotaCredito.equals(other.uidNotaCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.NotasCredito[ uidNotaCredito=" + uidNotaCredito + " ]";
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String noTrans() {
        String a = new Long(idNotaCredito).toString();
        String b = new Long(idNotaCredito).toString();
        for (int i = a.length(); i < 9; i++) {
            b = "0" + b;
        }
        return codalm + "-" + codcaja + "-" + b;
    }

    public String getFechaDesde() {
        return new Fecha(fecha).getString();
    }

    public String getFechaHasta() {
        return new Fecha(fechaValidez).getString();
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public boolean isProcesado() {
        return getProcesado() != null && getProcesado().equals('S');
    }

    public String getUidDevolucion() {
        return uidDevolucion;
    }

    public void setUidDevolucion(String uidDevolucion) {
        this.uidDevolucion = uidDevolucion;
    }

    public String getTotalRedondeado() {
        return Numero.redondear(total).toString();
    }

    public String getIdNotaCreditoCompleto() {
        String a = new Long(idNotaCredito).toString();
        String b = new Long(idNotaCredito).toString();
        for (int i = a.length(); i < 9; i++) {
            b = "0" + b;
        }
        return codalm + "-" + codcaja + "-" + b;
    }

    public BigDecimal getDescuento() {
        try {
            XMLDocument documento = new XMLDocument(getNotaCredito());
            XMLDocumentNode cabecera = documento.getNodo(TagNotaCreditoXML.TAG_CABECERA);
            XMLDocumentNode totales = cabecera.getNodo(TagNotaCreditoXML.TAG_TOTALES);
            String pdnc = totales.getNodo(TagNotaCreditoXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS, true).getValue();
            if (pdnc != null && !pdnc.isEmpty()) {
                return (new BigDecimal(pdnc));
            }
            return null;
        } catch (NumberFormatException ex) {
            //log.error("Error formateando porcentaje de descuento en Nota de crédito.");
        } catch (XMLDocumentException ex) {
            //  log.debug("No se pudo consultar el porcentaje de descuento de la nota de crédito.");
        } catch (Exception ex) {
            //  log.error("Error consultando el porcentaje de descuento de la nota de crédito.");
        }
        return null;

    }

    public Character getAnulado() {
        return anulado;
    }

    public void setAnulado(Character anulado) {
        this.anulado = anulado;
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

    public BigDecimal getSaldoUsado() {
        return saldoUsado;
    }

    public void setSaldoUsado(BigDecimal saldoUsado) {
        this.saldoUsado = saldoUsado;
    }

    public byte[] geteNotaCredito() {
        return eNotaCredito;
    }

    public void seteNotaCredito(byte[] eNotaCredito) {
        this.eNotaCredito = eNotaCredito;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public String getReferenciaUso() {
        return referenciaUso;
    }

    public void setReferenciaUso(String referenciaUso) {
        this.referenciaUso = referenciaUso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Long getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(Long tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
    }

    public String getLocalOrigen() {
        return localOrigen;
    }

    public void setLocalOrigen(String localOrigen) {
        this.localOrigen = localOrigen;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCliProceso() {
        return cliProceso;
    }

    public void setCliProceso(String cliProceso) {
        this.cliProceso = cliProceso;
    }

    public String getFactDocumento() {
        return factDocumento;
    }

    public void setFactDocumento(String factDocumento) {
        this.factDocumento = factDocumento;
    }
    
}
