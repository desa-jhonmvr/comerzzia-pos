/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.totales;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author amos
 */
public class TotalesEnPromocion {
    private List<TotalEnPromocion> totalesEnPromocion;
    private static TotalesEnPromocion instancia;
    
    private TotalesEnPromocion(){
        totalesEnPromocion = new ArrayList<TotalEnPromocion>();
    }
    
    public static TotalesEnPromocion getInstance(){
        if (instancia == null){
            instancia = new TotalesEnPromocion();
        }
        return instancia;
    }
    public static void getNewInstance(){
            instancia = new TotalesEnPromocion();
    }
    
    public void addTotalEnPromocion(BigDecimal total, PromocionTotal promocion){
        TotalEnPromocion totalEnPromocion = new TotalEnPromocion(total, promocion);
        totalesEnPromocion.add(totalEnPromocion);
        Collections.sort(totalesEnPromocion);
    }

    public List<Promocion> getPromocionesAplicables(TotalesXML totales, Cliente cliente) {
        List<Promocion> promocionesAplicables = new ArrayList<Promocion>();
        for (int i = 0; i < totalesEnPromocion.size(); i++) {
            TotalEnPromocion totalEnPromocion = totalesEnPromocion.get(i);
            if (totalEnPromocion.getPromocion().isAplicableACliente(cliente)
                    && totalEnPromocion.getPromocion().isAplicableAFecha()){
                if (totalEnPromocion.getPromocion().isAplicaALineasConPromocion()){
                    calculaTotalesAplicables(totales.getTotalAPagar(), totalEnPromocion, promocionesAplicables);
                }
                else{
                    calculaTotalesAplicables(totales.getTotalLineasSinPromocion(), totalEnPromocion, promocionesAplicables);
                }
            }
        }
        return promocionesAplicables;
    }

    private void calculaTotalesAplicables(BigDecimal total, TotalEnPromocion totalEnPromocion, List<Promocion> promocionesAplicables){
        int numVecesAplicable = 1;
        if (totalEnPromocion.getTotal().compareTo(BigDecimal.ZERO) != 0){
            numVecesAplicable = total.divide(totalEnPromocion.getTotal(), RoundingMode.HALF_UP).intValue();
        }
        while (numVecesAplicable > 0){
            promocionesAplicables.add(totalEnPromocion.getPromocion());
            numVecesAplicable--;
        }
    }    
    
}
