/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.gui.reservaciones;

import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import es.mpsistemas.util.fechas.Fecha;
import java.util.Date;

/**
 *
 * @author MGRI
 */
public class ReservaCompra {
    
    private ReservaInvitadoBean invitado;
    private int cantidad;
    private Date fecha;

    ReservaCompra(ReservaInvitadoBean invitadoPagador, Date fechaCompra) {
        this.cantidad = 1;
        this.fecha = fechaCompra;
        this.invitado = invitadoPagador;
    }

    public ReservaInvitadoBean getInvitado() {
        return invitado;
    }

    public void setInvitado(ReservaInvitadoBean invitado) {
        this.invitado = invitado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    void addComprado() {
        this.cantidad++;
    }
    
    
    
}
