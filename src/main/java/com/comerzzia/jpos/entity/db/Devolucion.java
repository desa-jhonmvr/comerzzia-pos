/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_DEVOLUCIONES_TBL")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Devolucion.findAll", query = "SELECT d FROM Devolucion d"),
    @NamedQuery(name = "Devolucion.findByUidDevolucion", query = "SELECT d FROM Devolucion d WHERE d.uidDevolucion = :uidDevolucion"),
    @NamedQuery(name = "Devolucion.findByEstadoMercaderia", query = "SELECT d FROM Devolucion d WHERE d.estadoMercaderia = :estadoMercaderia"),
    @NamedQuery(name = "Devolucion.findByObservaciones", query = "SELECT d FROM Devolucion d WHERE d.observaciones = :observaciones"),
    @NamedQuery(name = "Devolucion.findByUidNotaCredito", query = "SELECT d FROM Devolucion d WHERE d.uidNotaCredito = :uidNotaCredito"),
    @NamedQuery(name = "Devolucion.findByCodvendedor", query = "SELECT d FROM Devolucion d WHERE d.codvendedor = :codvendedor"),
    @NamedQuery(name = "Devolucion.findByIdUsuario", query = "SELECT d FROM Devolucion d WHERE d.idUsuario = :idUsuario")})
public class Devolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UID_DEVOLUCION")
    private String uidDevolucion;    
    @JoinColumn(name = "ID_MOTIVO", referencedColumnName = "ID_MOTIVO")
    @ManyToOne(fetch = FetchType.LAZY)
    private MotivoDevolucion motivo;
    @Column(name = "ESTADO_MERCADERIA")
    private String estadoMercaderia;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "UID_NOTA_CREDITO")
    private String uidNotaCredito;
    @Column(name = "CODVENDEDOR")
    private String codvendedor;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;    

    public Devolucion() {
    }

    public Devolucion(String uidDevolucion) {
        this.uidDevolucion = uidDevolucion;
    }

    public Devolucion(String uidDevolucion, Long idUsuario) {
        this.uidDevolucion = uidDevolucion;
        this.idUsuario = idUsuario;
    }

    public String getUidDevolucion() {
        return uidDevolucion;
    }

    public void setUidDevolucion(String uidDevolucion) {
        this.uidDevolucion = uidDevolucion;
    }

    public MotivoDevolucion getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoDevolucion idMotivo) {
        this.motivo = idMotivo;
    }

    public String getEstadoMercaderia() {
        return estadoMercaderia;
    }

    public void setEstadoMercaderia(String estadoMercaderia) {
        this.estadoMercaderia = estadoMercaderia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }

    public String getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(String codvendedor) {
        this.codvendedor = codvendedor;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidDevolucion != null ? uidDevolucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Devolucion)) {
            return false;
        }
        Devolucion other = (Devolucion) object;
        if ((this.uidDevolucion == null && other.uidDevolucion != null) || (this.uidDevolucion != null && !this.uidDevolucion.equals(other.uidDevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Devolucion[ uidDevolucion=" + uidDevolucion + " ]";
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }
    
}
