/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.sms;

import java.io.Serializable;
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
 *@author RD envio de mensajes
 */
@Entity
@Table(name = "ssms_datainfo_masive_sms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsmsDatainfoMasiveSms.findAll", query = "SELECT s FROM SsmsDatainfoMasiveSms s")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findById", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.id = :id")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByPrice", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.price = :price")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByDate", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.date = :date")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByOverdueBalance1", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.overdueBalance1 = :overdueBalance1")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByTotalValue", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.totalValue = :totalValue")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByBankDebit", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.bankDebit = :bankDebit")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByOverdueBalance2", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.overdueBalance2 = :overdueBalance2")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByDays", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.days = :days")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByCellNumber", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.cellNumber = :cellNumber")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByTypeMessage", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.typeMessage = :typeMessage")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByStatusSms", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.statusSms = :statusSms")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByCreatedAt", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.createdAt = :createdAt")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByUpdatedAt", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.updatedAt = :updatedAt")
    , @NamedQuery(name = "SsmsDatainfoMasiveSms.findByLocalCode", query = "SELECT s FROM SsmsDatainfoMasiveSms s WHERE s.localCode = :localCode")})
public class SsmsDatainfoMasiveSms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "price")
    private String price;
    @Column(name = "date")
    private String date;
    @Column(name = "overdue_balance_1")
    private String overdueBalance1;
    @Column(name = "total_value")
    private String totalValue;
    @Column(name = "bank_debit")
    private String bankDebit;
    @Column(name = "overdue_balance_2")
    private String overdueBalance2;
    @Column(name = "days")
    private Integer days;
    @Column(name = "cell_number")
    private String cellNumber;
    @Column(name = "type_message")
    private String typeMessage;
    @Column(name = "status_sms")
    private String statusSms;
    @Lob
    @Column(name = "sms_response")
    private String smsResponse;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "local_code")
    private String localCode;

    public SsmsDatainfoMasiveSms() {
    }

    public SsmsDatainfoMasiveSms(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverdueBalance1() {
        return overdueBalance1;
    }

    public void setOverdueBalance1(String overdueBalance1) {
        this.overdueBalance1 = overdueBalance1;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getBankDebit() {
        return bankDebit;
    }

    public void setBankDebit(String bankDebit) {
        this.bankDebit = bankDebit;
    }

    public String getOverdueBalance2() {
        return overdueBalance2;
    }

    public void setOverdueBalance2(String overdueBalance2) {
        this.overdueBalance2 = overdueBalance2;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
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

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
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
        if (!(object instanceof SsmsDatainfoMasiveSms)) {
            return false;
        }
        SsmsDatainfoMasiveSms other = (SsmsDatainfoMasiveSms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.sms.SsmsDatainfoMasiveSms[ id=" + id + " ]";
    }
    
}
