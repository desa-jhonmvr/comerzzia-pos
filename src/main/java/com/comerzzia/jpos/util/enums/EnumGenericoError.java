/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author Gabriel Simbania
 */
public enum EnumGenericoError {
    
    ERROR_GENERAL("0", "Error General no controlado: "),
    ERROR_LOGIN("1", "La caja no esta con la sesion iniciada ");
    
     private final String codigo;
    private final String descripcion;

    private EnumGenericoError(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}
