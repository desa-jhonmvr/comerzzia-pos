/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.gui.reservaciones;

import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ListaArticulosReservados {
    
    private List<ArticuloReservado> articulos;

    public ListaArticulosReservados() {
        this.articulos = new LinkedList<ArticuloReservado>();
    }
    
    public ListaArticulosReservados(List<ReservaArticuloBean> lreservas) {
        this();
        for (ReservaArticuloBean rs: lreservas){
            boolean enc = false;            
            for (ArticuloReservado artRes: this.articulos){
                if (rs.getCodart().equals(artRes.getArticulo().getCodart())){
                    artRes.addCantidadReservados();
                    if (rs.getComprado()){                        
                        artRes.addComprado(rs);                        
                    }
                    enc = true;
                    break;
                }
            }
            if (!enc){
                ArticuloReservado nrs = new ArticuloReservado(rs);
                this.articulos.add(nrs);
                nrs.addCantidadReservados();
                if (rs.getComprado()){                        
                        nrs.addComprado(rs);                    }
                
            }
        }
    }
    
    
    public List<ArticuloReservado> getArticulos() {
        return articulos;
    }



    public void setArticulos(List<ArticuloReservado> articulos) {
        this.articulos = articulos;
    }
    
    
}
