package com.comerzzia.jpos.servicios.promociones.detalles;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DetallePromocionNxM extends DetallePromocion {

    private BigDecimal descuento;
    private Integer cantidadN;
    private Integer cantidadM;
    private List<Articulos> articulos = new ArrayList<Articulos>();

    
    public int getLineasAplicables(LineasTicket lineas, List<LineaTicket> lineasAplicables){
        int cantN = 0;
        for (Articulos articulo : articulos) {
           cantN += lineas.getContains(true, articulo.getCodart(), lineasAplicables);
        }
        return cantN;
    }
    
    public List<Articulos> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulos> articulos) {
        this.articulos = articulos;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Integer getCantidadN() {
        return cantidadN;
    }

    public void setCantidadN(Integer cantidadN) {
        this.cantidadN = cantidadN;
    }

    public Integer getCantidadM() {
        return cantidadM;
    }

    public void setCantidadM(Integer cantidadM) {
        this.cantidadM = cantidadM;
    }
}
