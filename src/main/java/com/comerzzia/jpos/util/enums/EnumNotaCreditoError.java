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
public enum EnumNotaCreditoError {
    
    ERROR_SIN_ITEMS("101","No puede generar un NC sin items enviados "),
    ERROR_SIN_MOTIVO("102","No puede generar un NC sin un motivo "),
    ERROR_SUKUPON("103","Existen sukupones/billetones utilizados de la factura que desea devolver.");

    
    
     private final String codigo;
    private final String descripcion;

    private EnumNotaCreditoError(String codigo, String descripcion) {
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
