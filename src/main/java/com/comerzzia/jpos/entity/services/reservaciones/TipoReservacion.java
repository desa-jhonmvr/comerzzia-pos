/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

/**
 *
 * @author amos
 */
public class TipoReservacion {
    private String codigo;
    private String descripcion;

    public TipoReservacion(){
        this.codigo = "00";
        this.descripcion = "RESERVACIÓN CON ABONO";
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
}
