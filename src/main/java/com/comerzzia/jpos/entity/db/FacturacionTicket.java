/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_RESERVA_DATOS_FACT_TBL")

@XmlRootElement
public class FacturacionTicket implements Serializable {
    
    @Id
    @Basic(optional = false)
    @Column(name = "UID_RESERVACION")
    private String uidReservacion;
    @Column(name = "TIPO_DOCUMENTO")
    private String tipoIdent;
    @Column(name = "DOCUMENTO")
    private String ident;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "PROVINCIA")
    private String ciudad;
    @Column(name = "EMAIL")
    private String email;

    public FacturacionTicket() {
    }

    public FacturacionTicket(String uidReservacion) {
        this.uidReservacion = uidReservacion;
    }
    
    public FacturacionTicket(Cliente cliente) {
        this();
        this.nombre= cliente.getNombre();
        this.apellidos= cliente.getApellido();
        this.ident= cliente.getIdentificacion();
        this.direccion= cliente.getDireccion();
        this.ciudad = cliente.getPoblacion();
        this.telefono= cliente.getTelefonoFacturacion();
        this.tipoIdent = cliente.getTipoIdentificacion();
        this.email = cliente.getEmail();
    }
 
    public FacturacionTicket(FacturacionTicketBean ft) {
        this.nombre= ft.getNombre();
        this.apellidos= ft.getApellidos();
        this.ident= ft.getDocumento();
        this.direccion= ft.getDireccion();
        this.ciudad = ft.getProvincia();
        this.telefono= ft.getTelefono();
        this.tipoIdent = ft.getTipoDocumento(); 
        this.email = ft.getEmail();
    }    
    
    public String getTipoIdent() {
        return tipoIdent;
    }

    public void setTipoIdent(String tipoIdent) {
        this.tipoIdent = tipoIdent;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getNombre() {
        return nombre;
    }
    public String getNombreImpresion() {
       String rep;
        rep=nombre.replaceAll("&", "&amp;");
        rep=rep.replaceAll("<", "&lt;");
        rep=rep.replaceAll(">", "&gt;");
        rep=rep.replaceAll("'", "&apos;");
        rep=rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }
    public String getApellidosImpresion() {
        String rep;
        rep=apellidos.replaceAll("&", "&amp;");
        rep=rep.replaceAll("<", "&lt;");
        rep=rep.replaceAll(">", "&gt;");
        rep=rep.replaceAll("'", "&apos;");
        rep=rep.replaceAll("\"", "&quot;");
        return rep;
    }
    

    public void setApellidos(String appellidos) {
        this.apellidos = appellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }
    public String getDireccionImpresion() {
        String rep;
        rep=direccion.replaceAll("&", "&amp;");
        rep=rep.replaceAll("<", "&lt;");
        rep=rep.replaceAll(">", "&gt;");
        rep=rep.replaceAll("'", "&apos;");
        rep=rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
        @Override
    public int hashCode() {
        int hash = 0;
        hash += (getUidReservacion() != null ? getUidReservacion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FacturacionTicket)) {
            return false;
        }
        FacturacionTicket other = (FacturacionTicket) object;
        if ((this.getUidReservacion() == null && other.getUidReservacion() != null) || (this.getUidReservacion() != null && !this.uidReservacion.equals(other.uidReservacion))) {
            return false;
        }
        return true;
    }

    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isCliente(Cliente cliente) {
        return (getApellidos().equals(cliente.getApellido()) && getNombre().equals(cliente.getNombre()));
    }
    
    
}
