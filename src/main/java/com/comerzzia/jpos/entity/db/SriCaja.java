/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "SRI_TIENDAS_CAJAS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriCaja.findAll", query = "SELECT s FROM SriCaja s"),
    @NamedQuery(name = "SriCaja.findByCodalmSri", query = "SELECT s FROM SriCaja s WHERE s.sriCajaPK.codalmSri = :codalmSri"),
    @NamedQuery(name = "SriCaja.findByCodcaja", query = "SELECT s FROM SriCaja s WHERE s.sriCajaPK.codcaja = :codcaja"),
    @NamedQuery(name = "SriCaja.findByCodcajaSri", query = "SELECT s FROM SriCaja s WHERE s.codcajaSri = :codcajaSri"),
    @NamedQuery(name = "SriCaja.findByContFactura", query = "SELECT s FROM SriCaja s WHERE s.contFactura = :contFactura"),
    @NamedQuery(name = "SriCaja.findByContNotaCredito", query = "SELECT s FROM SriCaja s WHERE s.contNotaCredito = :contNotaCredito"),
    @NamedQuery(name = "SriCaja.findByContGuiaRemision", query = "SELECT s FROM SriCaja s WHERE s.contGuiaRemision = :contGuiaRemision"),
    @NamedQuery(name = "SriCaja.findByEmiteFactura", query = "SELECT s FROM SriCaja s WHERE s.emiteFactura = :emiteFactura"),
    @NamedQuery(name = "SriCaja.findByEmiteNotaCredito", query = "SELECT s FROM SriCaja s WHERE s.emiteNotaCredito = :emiteNotaCredito"),
    @NamedQuery(name = "SriCaja.findByEmiteGuiaRemision", query = "SELECT s FROM SriCaja s WHERE s.emiteGuiaRemision = :emiteGuiaRemision"),
    @NamedQuery(name = "SriCaja.findByEstado", query = "SELECT s FROM SriCaja s WHERE s.estado = :estado"),
    @NamedQuery(name = "SriCaja.findByProcesado", query = "SELECT s FROM SriCaja s WHERE s.procesado = :procesado")})
public class SriCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SriCajaPK sriCajaPK;
    @Basic(optional = false)
    @Column(name = "CODCAJA_SRI")
    private String codcajaSri;
    @Column(name = "CONT_FACTURA")
    private Integer contFactura;
    @Column(name = "CONT_NOTA_CREDITO")
    private Integer contNotaCredito;
    @Column(name = "CONT_GUIA_REMISION")
    private Integer contGuiaRemision;
    @Basic(optional = false)
    @Column(name = "EMITE_FACTURA")
    private char emiteFactura;
    @Basic(optional = false)
    @Column(name = "EMITE_NOTA_CREDITO")
    private char emiteNotaCredito;
    @Basic(optional = false)
    @Column(name = "EMITE_GUIA_REMISION")
    private char emiteGuiaRemision;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private char estado;
    @Basic(optional = false)
    @Column(name = "PROCESADO")
    private char procesado;
    @Column(name = "TID_DATAFAST")
    private String tidDatafast;
    @Column(name = "TID_MEDIANET")
    private String tidMedianet;
    @Column(name = "VERSION")
    private String version;
    @Column(name = "IP_PINPAD")
    private String ipPinpad;
    @Column(name = "DIRECCION_IP")
    private String direccionIp;
    @Column(name = "MASCARA_PINPAD")
    private String mascaraPinpad;
    @Column(name = "GATEWAY_PINPAD")
    private String gatewayPinpad;
     @Column(name = "SECUENCIAL_SUPER")
    private Integer secuencialSuper;
    
    public SriCaja() {
    }

    public SriCaja(SriCajaPK sriCajaPK) {
        this.sriCajaPK = sriCajaPK;
    }

    public SriCaja(SriCajaPK sriCajaPK, String codcajaSri, char emiteFactura, char emiteNotaCredito, char emiteGuiaRemision, char estado, char procesado) {
        this.sriCajaPK = sriCajaPK;
        this.codcajaSri = codcajaSri;
        this.emiteFactura = emiteFactura;
        this.emiteNotaCredito = emiteNotaCredito;
        this.emiteGuiaRemision = emiteGuiaRemision;
        this.estado = estado;
        this.procesado = procesado;
    }

    public SriCaja(String codalmSri, String codcaja) {
        this.sriCajaPK = new SriCajaPK(codalmSri, codcaja);
    }

    public SriCajaPK getSriCajaPK() {
        return sriCajaPK;
    }

    public void setSriCajaPK(SriCajaPK sriCajaPK) {
        this.sriCajaPK = sriCajaPK;
    }

    public String getCodcajaSri() {
        return codcajaSri;
    }

    public void setCodcajaSri(String codcajaSri) {
        this.codcajaSri = codcajaSri;
    }

    public Integer getContFactura() {
        return contFactura;
    }

    public void setContFactura(Integer contFactura) {
        this.contFactura = contFactura;
    }

    public Integer getContNotaCredito() {
        return contNotaCredito;
    }

    public void setContNotaCredito(Integer contNotaCredito) {
        this.contNotaCredito = contNotaCredito;
    }

    public Integer getContGuiaRemision() {
        return contGuiaRemision;
    }

    public void setContGuiaRemision(Integer contGuiaRemision) {
        this.contGuiaRemision = contGuiaRemision;
    }

    public char getEmiteFactura() {
        return emiteFactura;
    }

    public void setEmiteFactura(char emiteFactura) {
        this.emiteFactura = emiteFactura;
    }

    public char getEmiteNotaCredito() {
        return emiteNotaCredito;
    }

    public void setEmiteNotaCredito(char emiteNotaCredito) {
        this.emiteNotaCredito = emiteNotaCredito;
    }

    public char getEmiteGuiaRemision() {
        return emiteGuiaRemision;
    }

    public void setEmiteGuiaRemision(char emiteGuiaRemision) {
        this.emiteGuiaRemision = emiteGuiaRemision;
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
        hash += (sriCajaPK != null ? sriCajaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SriCaja)) {
            return false;
        }
        SriCaja other = (SriCaja) object;
        if ((this.sriCajaPK == null && other.sriCajaPK != null) || (this.sriCajaPK != null && !this.sriCajaPK.equals(other.sriCajaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.SriCaja[ sriCajaPK=" + sriCajaPK + " ]";
    }
    
    
    public boolean isEmiteFactura(){
        return (this.emiteFactura == '1');
    }
    
    public boolean isEmiteGuiaRemision(){
        return (this.emiteGuiaRemision == '1');
    }
    
    public boolean isEmiteNotaCredito(){
        return (this.emiteNotaCredito == '1');
    }

    public boolean isActiva() {
        return (this.estado == '1');
    }

    public String getTidDatafast() {
        return tidDatafast;
    }

    public void setTidDatafast(String tidDatafast) {
        this.tidDatafast = tidDatafast;
    }

    public String getTidMedianet() {
        return tidMedianet;
    }

    public void setTidMedianet(String tidMedianet) {
        this.tidMedianet = tidMedianet;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIpPinpad() {
        return ipPinpad;
    }

    public void setIpPinpad(String ipPinpad) {
        this.ipPinpad = ipPinpad;
    }

    public String getMascaraPinpad() {
        return mascaraPinpad;
    }

    public void setMascaraPinpad(String mascaraPinpad) {
        this.mascaraPinpad = mascaraPinpad;
    }

    public String getGatewayPinpad() {
        return gatewayPinpad;
    }

    public void setGatewayPinpad(String gatewayPinpad) {
        this.gatewayPinpad = gatewayPinpad;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    
    
    public Long getContador(String idContador) {
        if (idContador.equals(ServicioContadoresCaja.CONTADOR_FACTURA)){
            return contFactura.longValue();
        }
        if (idContador.equals(ServicioContadoresCaja.CONTADOR_GUIA_REMISION)){
            return contGuiaRemision.longValue();
        }        
        if (idContador.equals(ServicioContadoresCaja.CONTADOR_NOTA_CREDITO)){
            return contNotaCredito.longValue();
        }
        return 1L;
    }

    public Integer getSecuencialSuper() {
        return secuencialSuper;
    }

    public void setSecuencialSuper(Integer secuencialSuper) {
        this.secuencialSuper = secuencialSuper;
    }
    
}
