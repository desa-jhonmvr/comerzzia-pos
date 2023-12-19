package com.comerzzia.jpos.persistencia.reservaciones.reservainvitados;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;

public class ReservaInvitadoBean extends ReservaInvitadoKey {
    
    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private Boolean procesado;

    private Boolean procesadoTienda;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private ReservaBean reserva;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------
    
    public ReservaInvitadoBean() {
        this.procesado = false;
        this.reserva = new ReservaBean();
    }
    
    public ReservaInvitadoBean(String uidReservacion, Long idInvitado) {
        setUidReservacion(uidReservacion);
        setIdInvitado(idInvitado);
        this.reserva = new ReservaBean();
    }
    
    public ReservaInvitadoBean(ReservaInvitadoKey reservaInvitadoKey) {
        setUidReservacion(reservaInvitadoKey.getUidReservacion());
        setIdInvitado(reservaInvitadoKey.getIdInvitado());
        this.reserva = new ReservaBean();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido == null ? null : apellido.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono == null ? null : telefono.trim();
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public Boolean getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Boolean procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    public int hashCode() {
        int hash = 0;
        hash += ((getUidReservacion() != null && getIdInvitado() != null) ? super.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaInvitadoBean)) {
            return false;
        }
        ReservaInvitadoBean other = (ReservaInvitadoBean) object;
        return (this.getUidReservacion().equals(other.getUidReservacion()) && this.getIdInvitado().equals(other.getIdInvitado()));
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ReservaInvitado[ reservaInvitadoPK=" + super.toString() + " ]";
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
    
     public String getApellidosImpresion() {
        String rep;
        rep=apellido.replaceAll("&", "&amp;");
        rep=rep.replaceAll("<", "&lt;");
        rep=rep.replaceAll(">", "&gt;");
        rep=rep.replaceAll("'", "&apos;");
        rep=rep.replaceAll("\"", "&quot;");
        return rep;
    }  

    public ReservaBean getReserva() {
        return reserva;
    }

    public void setReserva(ReservaBean reserva) {
        this.reserva = reserva;
    }    
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}