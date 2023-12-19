/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.sms;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RD envio de mensajes
 */
@Entity
@Table(name = "ssms_catalogs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsmsCatalogs.findAll", query = "SELECT s FROM SsmsCatalogs s")
    , @NamedQuery(name = "SsmsCatalogs.findById", query = "SELECT s FROM SsmsCatalogs s WHERE s.id = :id")
    , @NamedQuery(name = "SsmsCatalogs.findByComplexCode", query = "SELECT s FROM SsmsCatalogs s WHERE s.complexCode = :complexCode")
    , @NamedQuery(name = "SsmsCatalogs.findByComplexName", query = "SELECT s FROM SsmsCatalogs s WHERE s.complexName = :complexName")
    , @NamedQuery(name = "SsmsCatalogs.findByActiveInactive", query = "SELECT s FROM SsmsCatalogs s WHERE s.activeInactive = :activeInactive")
    , @NamedQuery(name = "SsmsCatalogs.findByTypeMessage", query = "SELECT s FROM SsmsCatalogs s WHERE s.typeMessage = :typeMessage")})
public class SsmsCatalogs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "complex_code")
    private String complexCode;
    @Column(name = "complex_name")
    private String complexName;
    @Lob
    @Column(name = "message_text")
    private String messageText;
    @Column(name = "active_inactive")
    private Boolean activeInactive;
    @Column(name = "type_message")
    private String typeMessage;

    public SsmsCatalogs() {
    }

    public SsmsCatalogs(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComplexCode() {
        return complexCode;
    }

    public void setComplexCode(String complexCode) {
        this.complexCode = complexCode;
    }

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Boolean getActiveInactive() {
        return activeInactive;
    }

    public void setActiveInactive(Boolean activeInactive) {
        this.activeInactive = activeInactive;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
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
        if (!(object instanceof SsmsCatalogs)) {
            return false;
        }
        SsmsCatalogs other = (SsmsCatalogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.sms.SsmsCatalogs[ id=" + id + " ]";
    }
    
}
