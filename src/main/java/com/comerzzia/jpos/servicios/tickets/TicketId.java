/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets;

/**
 *
 * @author amos
 */
public class TicketId {
    private String codAlmacen;
    private String codCaja;
    private Long idTicket;

    public TicketId() {
    }

    public TicketId(String codAlmacen, String codCaja, Long idTicket) {
        this.codAlmacen = codAlmacen;
        this.codCaja = codCaja;
        this.idTicket = idTicket;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }
    
    
}
