/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.detalles;

import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author amos
 */
public class DetallePromocion {
    private Fecha fechaInicio;
    private Fecha fechaFin;
    private String textoPromocion;

    
    public boolean isFechaVigente(){
        Fecha hoy = new Fecha();
        return fechaInicio.antesOrEquals(hoy) && fechaFin.despuesOrEquals(hoy);
    }
    
    
    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = new Fecha(fechaInicio, Fecha.PATRON_FECHA_HORA);
        if (this.fechaInicio.getDate() == null){
            this.fechaInicio = new Fecha(fechaInicio);
        }
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = new Fecha(fechaFin, Fecha.PATRON_FECHA_HORA);
        if (this.fechaFin.getDate() == null){
            this.fechaFin = new Fecha(fechaFin);
        }
    }

    public void setFechaFin(Fecha fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getTextoPromocion() {
        if (textoPromocion == null || textoPromocion.isEmpty()){
            return "Descuento";
        }
        return textoPromocion;
    }

    public void setTextoPromocion(String textoPromocion) {
        this.textoPromocion = textoPromocion;
    }

}
