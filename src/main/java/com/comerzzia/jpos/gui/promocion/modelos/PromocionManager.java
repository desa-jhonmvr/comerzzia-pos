/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.promocion.modelos;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class PromocionManager {
    
    private static PromocionManager instance = null;    
    private Articulos articuloBuscado;
    private List<PromocionLineaTicket> listaPromocion;
        
    public static PromocionManager getInstance(){
        if (instance ==null){
            instance = new PromocionManager();
        }
        return instance;
        
    }

    public  List<PromocionLineaTicket> getListaPromocion() {
        if (listaPromocion ==null){
            listaPromocion = new ArrayList<PromocionLineaTicket>();
        }
        return listaPromocion;
    }

    public  void setListaPromocion(List<PromocionLineaTicket> aListaStock) {
        listaPromocion = aListaStock;
    }

    

    public void setArticuloBuscado(Articulos articulo) {
        this.articuloBuscado = articulo;
    }

    public Articulos getArticuloBuscado() {
        return articuloBuscado;
    }

    public String getNombreArticulo() {
        return articuloBuscado.getCodart()+"-"+articuloBuscado.getDesart();
    }
    
}
