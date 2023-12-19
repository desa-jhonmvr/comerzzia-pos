package com.comerzzia.jpos.servicios.promociones.detalles;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloDtoBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetallePromocionNxMSimple extends DetallePromocion {

    private List<Articulos> articulosN = new ArrayList <Articulos>();
    private List<ComboArticuloDtoBean> articulosM = new ArrayList<ComboArticuloDtoBean>();
    private Map<String,Integer> cantArticulos = new HashMap<String, Integer>();

    
    public List<LineaTicket> getLineasAplicables(LineasTicket lineas){
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket> ();
        // vamos a obtener toda la lista de líneas para cada artículo
        for (String articulo : cantArticulos.keySet()) {
            int cant = lineas.getContains(true, articulo, lineasAplicables);
            // si no tenemos suficiente cantidad de cada artículo, no podemos aplicar la promoción
            if (cant < cantArticulos.get(articulo)){
                return null;
            }
        }
        return lineasAplicables;
    
    }

    public List<ComboArticuloDtoBean> getArticulosM() {
        return articulosM;
    }

    public List<Articulos> getArticulosN() {
        return articulosN;
    }

    public Map<String, Integer> getCantArticulos() {
        return cantArticulos;
    }

    public void addArticuloN(Articulos articulo){
        articulosN.add(articulo);
        Integer cantidad = cantArticulos.get(articulo.getCodart());
        if (cantidad == null){
            cantidad = 0;
        }
        cantArticulos.put(articulo.getCodart(), cantidad+1);
    }
    
    public void addArticuloM(ComboArticuloDtoBean articulo){
        articulosM.add(articulo);
        Integer cantidad = cantArticulos.get(articulo.getCodigo());
        if (cantidad == null){
            cantidad = 0;
        }
        cantArticulos.put(articulo.getCodigo(), cantidad+1);
    }
    
    
}
