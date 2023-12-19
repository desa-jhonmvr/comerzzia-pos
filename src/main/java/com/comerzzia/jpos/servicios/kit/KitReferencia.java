/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.kit;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;

/**
 *
 * @author amos
 */
public class KitReferencia {
    private String uidReferencia;
    private Articulos articulo;
    private TicketOrigen ticketOrigen;

    public KitReferencia(Articulos articulo, TicketOrigen ticketOrigen) {
        this.articulo = articulo;
        this.ticketOrigen = ticketOrigen;
    }
    public KitReferencia(Articulos articulo) {
        this.articulo = articulo;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public TicketOrigen getTicketOrigen() {
        return ticketOrigen;
    }

    public void setTicketOrigen(TicketOrigen ticketOrigen) {
        this.ticketOrigen = ticketOrigen;
    }

    public String getUidReferencia() {
        return uidReferencia;
    }

    public void setUidReferencia(String uidReferencia) {
        this.uidReferencia = uidReferencia;
    }
    
}
