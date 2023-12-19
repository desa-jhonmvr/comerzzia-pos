/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.detalles.ParPromocionDetalle;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class ArticulosEnPromocion {

    protected static Logger log = Logger.getMLogger(ArticulosEnPromocion.class);
    private List<ArticuloEnPromocion> articulosEnPromocion;
    private List<ArticuloEnPromocion> articulosDiaSocio;
    private static ArticulosEnPromocion instancia;

    private ArticulosEnPromocion() {
        articulosEnPromocion = new ArrayList<ArticuloEnPromocion>();
        articulosDiaSocio = new ArrayList<ArticuloEnPromocion>();
    }

    public static ArticulosEnPromocion getInstance() {
        if (instancia == null) {
            instancia = new ArticulosEnPromocion();
        }
        return instancia;
    }

    public static void getNewInstance() {
        instancia = new ArticulosEnPromocion();
    }

    public void addArticuloEnPromocion(String codArticulo, Promocion promocion, DetallePromocion detalle) {
        // log.debug("Añadiendo artículo: "+codArticulo+" de la promoción "+promocion.toString());
        ParPromocionDetalle promocionDetalle = new ParPromocionDetalle(promocion, detalle);
        ArticuloEnPromocion articuloEnPromocion = new ArticuloEnPromocion(codArticulo);
        int i = articulosEnPromocion.indexOf(articuloEnPromocion);
        if (i >= 0) {
            articulosEnPromocion.get(i).addPromocion(promocionDetalle);
        } else {
            articuloEnPromocion.addPromocion(promocionDetalle);
            articulosEnPromocion.add(articuloEnPromocion);
        }
    }

    public void addArticuloDiaSocio(String codArticulo, Promocion promocion) {
        ParPromocionDetalle promocionDetalle = new ParPromocionDetalle(promocion, null);
        ArticuloEnPromocion articuloEnPromocion = new ArticuloEnPromocion(codArticulo);
        int i = articulosDiaSocio.indexOf(articuloEnPromocion);
        if (i >= 0) {
            articulosDiaSocio.get(i).addPromocion(promocionDetalle);
        } else {
            articuloEnPromocion.addPromocion(promocionDetalle);
            articulosDiaSocio.add(articuloEnPromocion);
        }
    }

    public ArticuloEnPromocion get(String codArticulo) {
        ArticuloEnPromocion articuloEnPromocion = new ArticuloEnPromocion(codArticulo);
        int i = 0;
        if (!Sesion.isEdicion()) {
            i = articulosEnPromocion.indexOf(articuloEnPromocion);
        } else {
            return null;
        }
        if (i < 0) {
            return null;
        }
        return articulosEnPromocion.get(i);
    }

    public ArticuloEnPromocion getDiaSocio(String codArticulo) {
        ArticuloEnPromocion articuloEnPromocion = new ArticuloEnPromocion(codArticulo);
        int i = articulosDiaSocio.indexOf(articuloEnPromocion);
        if (i < 0) {
            return null;
        }
        return articulosDiaSocio.get(i);
    }

}
