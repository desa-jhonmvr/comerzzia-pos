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
public enum EnumEstadoPrefactura {
    
    CREADO("Creado"),
    PROCESADO("Procesado"),
    PROCESADO_NOVEDAD("Procesado con Novedad"),
    ENTREGADO("Entregado"),
    ANULADO("Anulado"),
    FACTURADO("Facturado");
    
    private final String descripcion;

    private EnumEstadoPrefactura(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public static EnumEstadoPrefactura getByDescripcion(String descripcion){
        
        for(EnumEstadoPrefactura e: EnumEstadoPrefactura.values()){
            if(e.getDescripcion().equals(descripcion)){
                return e;
            }
        }
        
        return null;
        
    }

    

    public String getDescripcion() {
        return descripcion;
    }
    
    
    
}
