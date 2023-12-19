/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasRecibo;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.login.Sesion;

/**
 *
 * @author Dren
 */
public class ComprobanteReservacion {
    String total;
    FacturacionTicketBean facturacion;
    String fechaYHora;
    String codReserva;
    UsuarioBean cajero;
    CodigoBarrasRecibo codigoBarras;
    TicketS ticket;
    String tipoReserva;
    private String tienda;
    private String fechaCaducidad;
    private ReservaBean reserva;
    

    public ComprobanteReservacion(String total, FacturacionTicketBean facturacion, String fechaYHora, String codReserva, CodigoBarrasRecibo codigoBarras, TicketS ticket, ReservaBean reserva){
        this.total=total;
        this.facturacion=facturacion;
        this.fechaYHora=fechaYHora;
        this.codReserva=codReserva;
        this.cajero=Sesion.getUsuario();
        this.codigoBarras=codigoBarras;
        this.ticket=ticket;
        this.reserva = reserva;
        this.fechaCaducidad = reserva.getFechaCaducidadAsString();
        this.tipoReserva = reserva.getReservaTipo().getDesTipo();
        
    }
    
    public UsuarioBean getCajero() {
        return cajero;
    }

    public void setCajero(UsuarioBean cajero) {
        this.cajero = cajero;
    }

    public String getCodReserva() {
        return codReserva;
    }

    public void setCodReserva(String codReserva) {
        this.codReserva = codReserva;
    }

    public CodigoBarrasRecibo getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(CodigoBarrasRecibo codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public FacturacionTicketBean getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(FacturacionTicketBean facturacion) {
        this.facturacion = facturacion;
    }

    public String getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(String fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public ReservaBean getReserva() {
        return reserva;
    }

    public void setReserva(ReservaBean reserva) {
        this.reserva = reserva;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
 
}
