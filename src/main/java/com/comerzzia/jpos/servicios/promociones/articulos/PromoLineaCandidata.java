/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;

/**
 *
 * @author amos
 */
public class PromoLineaCandidata {
 
    private LineaTicket linea;
    private boolean valorLogico;

    public LineaTicket getLinea() {
        return linea;
    }

    public void setLinea(LineaTicket linea) {
        this.linea = linea;
    }

    public boolean isValorLogico() {
        return valorLogico;
    }

    public void setValorLogico(boolean valorLogico) {
        this.valorLogico = valorLogico;
    }
    
}
