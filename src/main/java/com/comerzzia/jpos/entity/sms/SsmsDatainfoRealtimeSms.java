/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.sms;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RD envio de mensajes
 */
@Entity
@Table(name = "ssms_datainfo_realtime_sms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsmsDatainfoRealtimeSms.findAll", query = "SELECT s FROM SsmsDatainfoRealtimeSms s")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findById", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.id = :id")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByFormat", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.format = :format")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByMask", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.mask = :mask")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByLocalCode", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.localCode = :localCode")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByDatetimePurchase", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.datetimePurchase = :datetimePurchase")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByPrice", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.price = :price")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByCellNumber", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.cellNumber = :cellNumber")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByCaja", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.caja = :caja")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByFactura", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.factura = :factura")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByStatusSms", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.statusSms = :statusSms")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByCedula", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.cedula = :cedula")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByCreatedAt", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.createdAt = :createdAt")
    , @NamedQuery(name = "SsmsDatainfoRealtimeSms.findByUpdatedAt", query = "SELECT s FROM SsmsDatainfoRealtimeSms s WHERE s.updatedAt = :updatedAt")})
public class SsmsDatainfoRealtimeSms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "format")
    private String format;
    @Column(name = "mask")
    private String mask;
    @Column(name = "local_code")
    private String localCode;
    @Column(name = "datetime_purchase")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimePurchase;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "cell_number")
    private String cellNumber;
    @Column(name = "caja")
    private String caja;
    @Column(name = "factura")
    private String factura;
    @Column(name = "status_sms")
    private String statusSms;
    @Lob
    @Column(name = "sms_response")
    private String smsResponse;
    @Column(name = "cedula")
    private String cedula;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public SsmsDatainfoRealtimeSms() {
    }

    public SsmsDatainfoRealtimeSms(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public Date getDatetimePurchase() {
        return datetimePurchase;
    }

    public void setDatetimePurchase(Date datetimePurchase) {
        this.datetimePurchase = datetimePurchase;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getStatusSms() {
        return statusSms;
    }

    public void setStatusSms(String statusSms) {
        this.statusSms = statusSms;
    }

    public String getSmsResponse() {
        return smsResponse;
    }

    public void setSmsResponse(String smsResponse) {
        this.smsResponse = smsResponse;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsmsDatainfoRealtimeSms)) {
            return false;
        }
        SsmsDatainfoRealtimeSms other = (SsmsDatainfoRealtimeSms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.sms.SsmsDatainfoRealtimeSms[ id=" + id + " ]";
    }
    
}
