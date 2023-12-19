package com.comerzzia.jpos.servicios.promociones.detalles;

import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCantidadBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.promociones.IndicePromocionLinea;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetallePromocionComboSeccion extends DetallePromocion {


    private BigDecimal descuento;
    private List<ComboArticuloCantidadBean> secciones = new ArrayList<ComboArticuloCantidadBean>();

    
    public Map<String, IndicePromocionLinea> getLineasAplicables(LineasTicket lineas){
        // si la promoción no tiene definida ninguna categoría, no podemos aplicarla
        if (secciones.isEmpty()){
            return null;
        }
        Map<String, IndicePromocionLinea> lineasPromocion = new HashMap<String, IndicePromocionLinea>();
        for (ComboArticuloCantidadBean comboSeccion : secciones) {
           List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
           int cant = lineas.getContainsSecciones(true, comboSeccion.getCodigo(), lineasAplicables);
           // si la cantidad de artículos es menor que la exigida por la promoción, no existen líneas aplicables
           if (cant < comboSeccion.getCantidad()){
               return null;
           }
           IndicePromocionLinea indicePromo = new IndicePromocionLinea();
           indicePromo.setLineas(lineasAplicables);
           lineasPromocion.put(comboSeccion.getCodigo(), indicePromo);
        }
        return lineasPromocion;
    }


    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public List<ComboArticuloCantidadBean> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<ComboArticuloCantidadBean> secciones) {
        this.secciones = secciones;
    }

    
}
