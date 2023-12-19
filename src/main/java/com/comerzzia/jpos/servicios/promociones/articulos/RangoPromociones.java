/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author AMS
 */
public class RangoPromociones implements Comparable<RangoPromociones> {

    private BigDecimal inicio;
    private BigDecimal fin;
    private String descripcion;

    
    public RangoPromociones() {
    }

    public boolean isAplicable(BigDecimal valor){
       // if ((inicio.compareTo(valor) <=0) && (fin.compareTo(valor) >= 0)){
        if ((inicio.setScale(2, RoundingMode.UP).compareTo(valor) <=0) && (fin.setScale(2, RoundingMode.UP).compareTo(valor) >= 0)){
            return true;
        }
        return false;
    }

    public RangoPromociones(BigDecimal inicio, BigDecimal fin, String descripcion) {
        this.inicio = inicio;
        this.fin = fin;
        this.descripcion = descripcion;
    }

    public BigDecimal getFin() {
        return fin;
    }

    public BigDecimal getInicio() {
        return inicio;
    }

    public void setFin(BigDecimal fin) {
        this.fin = fin;
    }

    public void setInicio(BigDecimal inicio) {
        this.inicio = inicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int compareTo(RangoPromociones t) {
        return inicio.compareTo(t.inicio);
    }

}
