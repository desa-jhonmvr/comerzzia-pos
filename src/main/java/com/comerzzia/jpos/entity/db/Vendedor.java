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
@Table(name = "X_VENDEDORES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vendedor.findAll", query = "SELECT v FROM Vendedor v"),
    @NamedQuery(name = "Vendedor.findByCodvendedor", query = "SELECT v FROM Vendedor v WHERE v.codvendedor = :codvendedor"),
    @NamedQuery(name = "Vendedor.findByNombreVendedor", query = "SELECT v FROM Vendedor v WHERE v.nombreVendedor = :nombreVendedor"),
    @NamedQuery(name = "Vendedor.findByApellidosVendedor", query = "SELECT v FROM Vendedor v WHERE v.apellidosVendedor = :apellidosVendedor")})
public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODVENDEDOR")
    private String codvendedor;
    @Basic(optional = false)
    @Column(name = "NOMBRE_VENDEDOR")
    private String nombreVendedor;
    @Column(name = "APELLIDOS_VENDEDOR")
    private String apellidosVendedor;
    @Column(name = "CODALM")
    private String codAlm;

    public Vendedor() {
    }

    public Vendedor(String codvendedor) {
        this.codvendedor = codvendedor;
    }

    public Vendedor(String codvendedor, String nombreVendedor) {
        this.codvendedor = codvendedor;
        this.nombreVendedor = nombreVendedor;
    }

    public String getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(String codvendedor) {
        this.codvendedor = codvendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getApellidosVendedor() {
        return apellidosVendedor;
    }

    public void setApellidosVendedor(String apellidosVendedor) {
        this.apellidosVendedor = apellidosVendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codvendedor != null ? codvendedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vendedor)) {
            return false;
        }
        Vendedor other = (Vendedor) object;
        if ((this.codvendedor == null && other.codvendedor != null) || (this.codvendedor != null && !this.codvendedor.equals(other.codvendedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Vendedor[ codvendedor=" + codvendedor + " ]";
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }
    
}
