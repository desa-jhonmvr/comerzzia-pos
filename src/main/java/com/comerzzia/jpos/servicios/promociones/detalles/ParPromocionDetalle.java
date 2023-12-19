/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.detalles;

import com.comerzzia.jpos.servicios.promociones.Promocion;

/**
 *
 * @author amos
 */
public class ParPromocionDetalle {

    private Promocion promocion;
    private DetallePromocion detalle;
    
    public ParPromocionDetalle(Promocion promocion, DetallePromocion detalle) {
        this.promocion = promocion;
        this.detalle = detalle;
    }

    public DetallePromocion getDetalle() {
        return detalle;
    }

    public Promocion getPromocion() {
        return promocion;
    }
    
    
}
