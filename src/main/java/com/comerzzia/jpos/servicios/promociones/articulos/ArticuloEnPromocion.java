/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.detalles.ParPromocionDetalle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class ArticuloEnPromocion {
    private String codArticulo;
    private List<ParPromocionDetalle> promociones;
    private boolean filtrada = false;
    
    public ArticuloEnPromocion(String codArticulo) {
        this.codArticulo = codArticulo;
    }
    
    public void addPromocion(ParPromocionDetalle promocionDetalle){
        if (promociones == null){
            promociones = new ArrayList<ParPromocionDetalle>();
        }
        promociones.add(promocionDetalle);
    }

    private void eliminarMenosPrioritarias(){
        // calculamos la mayor prioridad
        int mayorPrioridad = 0;
        for (ParPromocionDetalle parPromocionDetalle : promociones) {
            if (parPromocionDetalle.getPromocion().getPrioridad() > mayorPrioridad){
                mayorPrioridad = parPromocionDetalle.getPromocion().getPrioridad();
            }
        }
        
        // si la mayor prioridad es 0, no hacemos nada
        if (mayorPrioridad == 0){
            return;
        }
        
        // si no, nos quedamos sólo con los que tengan la mayor prioridad
        List<ParPromocionDetalle> aux = new ArrayList<ParPromocionDetalle>();
        for (ParPromocionDetalle parPromocionDetalle : promociones) {
            if (parPromocionDetalle.getPromocion().getPrioridad() == mayorPrioridad){
                aux.add(parPromocionDetalle);
            }
        }
        promociones = aux;
        
    }
    
    public ParPromocionDetalle getPromocionOptima(Cliente cliente, LineaTicket linea, Tarifas tarifa) {
        if (!filtrada){
            eliminarMenosPrioritarias();
            filtrada = true;
        }
        BigDecimal mejorDescuento = new BigDecimal(0);
        ParPromocionDetalle mejorPromocion = null;

        for (ParPromocionDetalle parPromocionDetalle : promociones) {
            Promocion promocion = parPromocionDetalle.getPromocion();
            if (promocion.isAplicableACliente(cliente) && promocion.isAplicableAFecha()){
                BigDecimal descuentoCandidato = promocion.calculaDtoLineaUnitaria(linea, parPromocionDetalle.getDetalle(), tarifa);
                if (descuentoCandidato.compareTo(mejorDescuento) > 0){
                    mejorDescuento = descuentoCandidato;
                    mejorPromocion = parPromocionDetalle;
                }
            }
        }
        
        return mejorPromocion;
        
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ArticuloEnPromocion){
            return ((ArticuloEnPromocion) o).getCodArticulo().equals(getCodArticulo());
        }
        return false;
    }

    
    public String getCodArticulo() {
        return codArticulo;
    }

    public boolean isRestringido() {
        for (ParPromocionDetalle parPromocionDetalle : promociones) {
            Promocion promocion = parPromocionDetalle.getPromocion();
            if (promocion.isAplicableAFecha()){
                return true;
            }
        }
        return false;
    }

    public List<ParPromocionDetalle> getPromociones() {
        return promociones;
    }


    
    
}
