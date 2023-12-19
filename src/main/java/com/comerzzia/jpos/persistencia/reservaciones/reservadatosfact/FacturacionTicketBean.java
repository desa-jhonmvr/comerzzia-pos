package com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.FacturacionTicket;

public class FacturacionTicketBean {
    private String uidReservacion;

    private String tipoDocumento;

    private String documento;

    private String nombre;

    private String apellidos;

    private String direccion;

    private String provincia;

    private String telefono;
    
    private String email;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public FacturacionTicketBean() {
    }

    public FacturacionTicketBean(String uidReservacion) {
        this.uidReservacion = uidReservacion;
    }
    
    public FacturacionTicketBean(Cliente cliente) {
        this();
        this.nombre= cliente.getNombre();
        this.apellidos= cliente.getApellido();
        this.documento= cliente.getIdentificacion();
        this.direccion= cliente.getDireccion();
        this.provincia = cliente.getPoblacion();
        this.telefono= cliente.getTelefonoFacturacion();
        this.tipoDocumento = cliente.getTipoIdentificacion();
        this.email = cliente.getEmail();
    }
    
    public FacturacionTicketBean(FacturacionTicket ft) {
        this.nombre= ft.getNombre();
        this.apellidos= ft.getApellidos();
        this.documento= ft.getIdent();
        this.direccion= ft.getDireccion();
        this.provincia = ft.getCiudad();
        this.telefono= ft.getTelefono();
        this.tipoDocumento = ft.getTipoIdent();
        this.email = ft.getEmail();
    }
    
    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion == null ? null : uidReservacion.trim();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento == null ? null : tipoDocumento.trim();
    }

    public String getDocumento() {
        return documento.trim();
    }

    public void setDocumento(String documento) {
        this.documento = documento == null ? null : documento.trim();
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
        this.nombre = nombre == null ? null : nombre.trim();
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
        
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos == null ? null : apellidos.trim();
    }
    
    public String getNombreApellidoImpresion()
    {
        return getNombreImpresion().trim() + " " + getApellidosImpresion().trim();
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
        this.direccion = direccion == null ? null : direccion.trim();
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia == null ? null : provincia.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono == null ? null : telefono.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    public boolean isCliente(Cliente cliente) {
        return (getApellidos().equals(cliente.getApellido()) && getNombre().equals(cliente.getNombre()));
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getUidReservacion() != null ? getUidReservacion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FacturacionTicketBean)) {
            return false;
        }
        FacturacionTicketBean other = (FacturacionTicketBean) object;
        if ((this.getUidReservacion() == null && other.getUidReservacion() != null) || (this.getUidReservacion() != null && !this.uidReservacion.equals(other.uidReservacion))) {
            return false;
        }
        return true;
    } 
    
    @Override
    public String toString() {
        return "FacturacionTicketBean{" + "uidReservacion=" + uidReservacion + '}';
    }    
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}