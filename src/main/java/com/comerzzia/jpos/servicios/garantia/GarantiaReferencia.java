/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.garantia;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class GarantiaReferencia {
    private Articulos articulo;
    private TicketOrigen ticketOrigen;
    private LineaTicket lineaOrigen;
    private BigDecimal preciotTotalFinalPagadoArticuloAsociado;
    private BigDecimal importeTotalFinalPagadoArticuloAsociado;
    private String refTicketOrigen;

    public GarantiaReferencia(LineaTicket lineaOrigen) {
        this.lineaOrigen = lineaOrigen;
        this.articulo = lineaOrigen.getArticulo();
    }
    
    public GarantiaReferencia(Articulos articulo, TicketOrigen ticketOrigen) {
        this.articulo = articulo;
        this.ticketOrigen = ticketOrigen;
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

    public LineaTicket getLineaOrigen() {
        return lineaOrigen;
    }

    public void setLineaOrigen(LineaTicket lineaOrigen) {
        this.lineaOrigen = lineaOrigen;
    }

    public BigDecimal getImporteTotalFinalPagadoArticuloAsociado() {
        return importeTotalFinalPagadoArticuloAsociado;
    }

    public void setImporteTotalFinalPagadoArticuloAsociado(BigDecimal importeTotalFinalPagadoArticuloAsociado) {
        this.importeTotalFinalPagadoArticuloAsociado = importeTotalFinalPagadoArticuloAsociado;
    }    

    public BigDecimal getPreciotTotalFinalPagadoArticuloAsociado() {
        return preciotTotalFinalPagadoArticuloAsociado;
    }

    public void setPreciotTotalFinalPagadoArticuloAsociado(BigDecimal preciotTotalFinalPagadoArticuloAsociado) {
        this.preciotTotalFinalPagadoArticuloAsociado = preciotTotalFinalPagadoArticuloAsociado;
    }

    public String getRefTicketOrigen() {
        return refTicketOrigen;
    }

    public void setRefTicketOrigen(String refTicketOrigen) {
        this.refTicketOrigen = refTicketOrigen;
    }
    
}
