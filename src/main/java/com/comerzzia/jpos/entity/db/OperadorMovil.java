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
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_OPERADORES_MOVILES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperadoreMovil.findAll", query = "SELECT o FROM OperadorMovil o"),
    @NamedQuery(name = "OperadoreMovil.findByDesoperador", query = "SELECT o FROM OperadorMovil o WHERE o.desoperador = :desoperador")})
public class OperadorMovil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DESOPERADOR")
    private String desoperador;

    public OperadorMovil() {
    }

    public OperadorMovil(String desoperador) {
        this.desoperador = desoperador;
    }

    public String getDesoperador() {
        return desoperador;
    }

    public void setDesoperador(String desoperador) {
        this.desoperador = desoperador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (desoperador != null ? desoperador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof OperadorMovil)) {
            return false;
        }
        OperadorMovil other = (OperadorMovil) object;
        if ((this.desoperador == null && other.desoperador != null) || (this.desoperador != null && !this.desoperador.equals(other.desoperador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.OperadoresMoviles[ desoperador=" + desoperador + " ]";
    }
    
}
