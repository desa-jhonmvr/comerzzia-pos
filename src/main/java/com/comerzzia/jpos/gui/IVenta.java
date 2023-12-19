/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;

/**
 *
 * @author MGRI
 */
public interface IVenta {
    
    LineaTicket crearLineaArticulo(String codigo, Articulos art, int numero, boolean compruebaStock) throws TicketNuevaLineaException;
    public void setReferenciaSesionPDA(SesionPdaBean referenciaSesionPDA);
    public boolean esTipoVenta();
}
