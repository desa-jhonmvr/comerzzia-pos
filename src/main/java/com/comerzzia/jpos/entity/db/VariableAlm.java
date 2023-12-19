/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "x_config_variables_alm_tbl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableAlm.findAll", query = "SELECT v FROM VariableAlm v"),
    @NamedQuery(name = "VariableAlm.findByIdVariable", query = "SELECT v FROM VariableAlm v WHERE v.idVariable = :idVariable"),
    @NamedQuery(name = "VariableAlm.findByValor", query = "SELECT v FROM VariableAlm v WHERE v.valor = :valor")})
public class VariableAlm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_variable")
    private String idVariable;
    @Basic(optional = false)
    @Column(name = "valor")
    private String valor;
    @Transient
    private String valorDefecto;
   

    public VariableAlm() {
    }

    public VariableAlm(String idVariable) {
        this.idVariable = idVariable;
    }

    public VariableAlm(String idVariable, String valor) {
        this.idVariable = idVariable;
        this.valor = valor;
    }

    public String getIdVariable() {
        return idVariable;
    }

    public void setIdVariable(String idVariable) {
        this.idVariable = idVariable;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVariable != null ? idVariable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VariableAlm)) {
            return false;
        }
        VariableAlm other = (VariableAlm) object;
        if ((this.idVariable == null && other.idVariable != null) || (this.idVariable != null && !this.idVariable.equals(other.idVariable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.VariableAlm[ idVariable=" + idVariable + " ]";
    }

    public String getValorDefecto() {
        return valorDefecto;
    }

    public void setValorDefecto(String valorDefecto) {
        this.valorDefecto = valorDefecto;
    }
    
}
