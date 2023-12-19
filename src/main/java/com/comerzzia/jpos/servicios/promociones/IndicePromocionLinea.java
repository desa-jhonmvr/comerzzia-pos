/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones;

import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import java.util.List;

/**
 *
 * @author amos
 */
public class IndicePromocionLinea {
    private List<LineaTicket> lineas;
    private int indice;
    private int cantidad;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public LineaTicket getLineaIndice() {
        return lineas.get(indice);
    }

    public void setLineas(List<LineaTicket> lineas) {
        this.lineas = lineas;
    }
    
    public void incrementaIndice(){
        indice++;
    }

    public List<LineaTicket> getLineas() {
        return lineas;
    }
    
}
