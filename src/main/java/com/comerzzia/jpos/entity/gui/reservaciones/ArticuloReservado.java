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
public class ArticuloReservado {
    
    private List<ReservaCompra> compradores;
    private ReservaArticuloBean articulo;
    private int cantidadReservados;
    private int cantidadComprados;
    
    public ArticuloReservado() {
        compradores = new LinkedList<ReservaCompra>();
        cantidadReservados = 0;
        cantidadComprados = 0;        
    }
    
    public ArticuloReservado(ReservaArticuloBean articulo) {
        this();
        this.articulo = articulo;
        cantidadComprados = 0;
        cantidadReservados = 0;
    }

    public ArticuloReservado(List<ReservaCompra> compradores, ReservaArticuloBean articulo, int cantidad, int conpradores) {
        this.compradores = compradores;
        this.articulo = articulo;
        this.cantidadReservados = cantidad;
        this.cantidadComprados = conpradores;        
    }

    public List<ReservaCompra> getCompradores() {
        return compradores;
    }

    public void setCompradores(List<ReservaCompra> compradores) {
        this.compradores = compradores;
    }

    public ReservaArticuloBean getArticulo() {
        return articulo;
    }

    public void setArticulo(ReservaArticuloBean articulo) {
        this.articulo = articulo;
    }

    public int getCantidadReservados() {
        return cantidadReservados;
    }

    public void setCantidadReservados(int cantidadReservados) {
        this.cantidadReservados = cantidadReservados;
    }

    public void setCantidadCompradores(int cantidadCompradores) {
        this.cantidadComprados = cantidadCompradores;
    }

    public void addCantidadReservados() {
        this.cantidadReservados++;
    }

    void addCantidadComprados() {
        this.cantidadComprados++;
    }

    void addComprado(ReservaArticuloBean rs) {
         boolean enc = false;  
         addCantidadComprados();
            for (ReservaCompra com: this.compradores){
                
                if (com.getInvitado()!=null && com.getInvitado()!=null && rs.getInvitadoPagador()!=null && rs.getInvitadoPagador().equals(com.getInvitado())){
                    com.addComprado();  
                    enc = true;
                    break;
                }
            }
          if (!enc){
              if (rs.getInvitadoPagador()!=null && rs.getFechaCompra()!=null )
                this.compradores.add(new ReservaCompra(rs.getInvitadoPagador(),rs.getFechaCompra().getDate()));
          }        
    }

    public int getCantidadComprados() {
        return cantidadComprados;
    }
   
    
    
}
