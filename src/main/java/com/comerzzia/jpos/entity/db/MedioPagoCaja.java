/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
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
@Table(name = "D_MEDIOS_PAGO_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedioPagoCaja.findAll", query = "SELECT m FROM MedioPagoCaja m"),
    @NamedQuery(name = "MedioPagoCaja.findByCodmedpag", query = "SELECT m FROM MedioPagoCaja m WHERE m.codmedpag = :codmedpag"),
    @NamedQuery(name = "MedioPagoCaja.findByDesmedpag", query = "SELECT m FROM MedioPagoCaja m WHERE m.desmedpag = :desmedpag")})
public class MedioPagoCaja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODMEDPAG")
    private String codmedpag;
    @Basic(optional = false)
    @Column(name = "DESMEDPAG")
    private String desmedpag;
    @Column(name = "ACTIVO")
    private char activo;
    
    public MedioPagoCaja() {
    }

    public MedioPagoCaja(String codmedpag) {
        this.codmedpag = codmedpag;
    }

    public MedioPagoCaja(MedioPagoBean medioPago) {
        this.codmedpag = medioPago.getCodMedioPago();
        this.desmedpag = medioPago.getDesMedioPago();
        this.activo = medioPago.isActivo() ? 'S' : 'N';
    }

    public MedioPagoCaja(String codmedpag, String desmedpag, char activo) {
        this.codmedpag = codmedpag;
        this.desmedpag = desmedpag;
        this.activo = activo;
    }

    public String getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(String codmedpag) {
        this.codmedpag = codmedpag;
    }

    public String getDesmedpag() {
        return desmedpag;
    }

    public void setDesmedpag(String desmedpag) {
        this.desmedpag = desmedpag;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codmedpag != null ? codmedpag.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null){
            return false;
        }
        if (object instanceof MedioPagoCaja) {
            MedioPagoCaja other = (MedioPagoCaja) object;
            if ((this.codmedpag == null && other.codmedpag != null) || (this.codmedpag != null && !this.codmedpag.equals(other.codmedpag))) {
                return false;
            }
        }
        else if (object instanceof String){
            String codMedPago = (String) object;
            if (this.codmedpag == null || !this.codmedpag.equals(codMedPago)) {
                return false;
            }
        }
        else{
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.MedioPagoCaja[ codmedpag=" + codmedpag + " ]";
    }
}