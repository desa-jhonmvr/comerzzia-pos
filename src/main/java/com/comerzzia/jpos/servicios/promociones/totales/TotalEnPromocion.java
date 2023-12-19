/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.totales;

import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class TotalEnPromocion implements Comparable<TotalEnPromocion> {
    private BigDecimal total;
    private PromocionTotal promocion;
    
    public TotalEnPromocion(BigDecimal total, PromocionTotal promocion) {
        this.total = total;
        this.promocion = promocion;
    }
    

    public BigDecimal getTotal() {
        return total;
    }

    public PromocionTotal getPromocion() {
        return promocion;
    }

    
    @Override
    public int compareTo(TotalEnPromocion t) {
        return t.getTotal().compareTo(getTotal());
    }

    
    
}
