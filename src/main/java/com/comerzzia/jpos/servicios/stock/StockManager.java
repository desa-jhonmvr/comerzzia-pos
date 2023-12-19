/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.stock;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueoFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueosServices;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class StockManager {
    
    private static StockManager instance = null;    
    private Articulos articuloBuscado;
    private List<StockBean> listaStock;
        
    public static StockManager getInstance(){
        if (instance ==null){
            instance = new StockManager();
        }
        return instance;
        
    }

    public  List<StockBean> getListaStock() {
        if (listaStock ==null){
            listaStock = new ArrayList<StockBean>();
        }
        return listaStock;
    }

    public  void setListaStock(List<StockBean> aListaStock) {
        listaStock = aListaStock;
    }

    public void accionConsultaStockOtrosLocales() throws StockException {
        listaStock = ServicioStock.consultarStockTiendas(articuloBuscado);
    }

    public void accionConsultaStockTienda() throws StockException {
        listaStock = ServicioStock.consultarStockTienda(articuloBuscado);
    }
    
    public void accionConsultarStockTotal() throws StockException {
        listaStock = ServicioStock.consultarStockTotal(articuloBuscado);
        articuloBuscado = ArticulosServices.getInstance().getArticuloCod(articuloBuscado.getCodart());
        try {
            if(BloqueosServices.isItemBloqueado(articuloBuscado.getCodmarca().getCodmarca(), articuloBuscado.getIdItem(), articuloBuscado.getCodart())){
                listaStock.get(0).setBloqueado("S");
            } else {
                listaStock.get(0).setBloqueado("N");
            }
        }
        catch (BloqueoFoundException ex) {
            listaStock.get(0).setBloqueado("S");
        }
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
