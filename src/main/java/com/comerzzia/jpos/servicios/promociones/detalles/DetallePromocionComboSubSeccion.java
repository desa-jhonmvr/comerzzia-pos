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


public class DetallePromocionComboSubSeccion extends DetallePromocion {


    private BigDecimal descuento;
    private List<ComboArticuloCantidadBean> subsecciones = new ArrayList<ComboArticuloCantidadBean>();

    
    public Map<String, IndicePromocionLinea> getLineasAplicables(LineasTicket lineas){
        // si la promoción no tiene definida ninguna categoría, no podemos aplicarla
        if (subsecciones.isEmpty()){
            return null;
        }
        Map<String, IndicePromocionLinea> lineasPromocion = new HashMap<String, IndicePromocionLinea>();
        for (ComboArticuloCantidadBean comboSubSeccion : subsecciones) {
           List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
           int cant = lineas.getContainsSubSecciones(true, comboSubSeccion.getCodigo(), lineasAplicables);
           // si la cantidad de artículos es menor que la exigida por la promoción, no existen líneas aplicables
           if (cant < comboSubSeccion.getCantidad()){
               return null;
           }
           IndicePromocionLinea indicePromo = new IndicePromocionLinea();
           indicePromo.setLineas(lineasAplicables);
           lineasPromocion.put(comboSubSeccion.getCodigo(), indicePromo);
        }
        return lineasPromocion;
    }


    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public List<ComboArticuloCantidadBean> getSubSecciones() {
        return subsecciones;
    }

    public void setSubSecciones(List<ComboArticuloCantidadBean> subsecciones) {
        this.subsecciones = subsecciones;
    }

    
}
