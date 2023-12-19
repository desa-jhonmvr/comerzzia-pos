/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author DESARROLLO
 */
public enum EnumTipoLectura {

    MANUAL("MANUAL","01","000"),
    BANDA("BANDA","02","090"),
    CHIP("CHIP","03","005"),
    FALLBACK("FALLBACK","04","999"),
    D1("D1","05","998"),
    DEFAULT("MANUAL","01","");

    private final String descripcion;
    private final String modoLectura;
    private final String tipoLectura;

    private EnumTipoLectura(String descripcion, String modoLectura, String tipoLectura) {
        this.descripcion = descripcion;
        this.modoLectura = modoLectura;
        this.tipoLectura = tipoLectura;
    }
    
    /**
     * @author Gabriel Simbania
     * @param tipo
     * @return 
     */
    public static EnumTipoLectura findTipoLecturaByTipo(String tipo){
        EnumTipoLectura defaultTipo =EnumTipoLectura.DEFAULT;
        for( EnumTipoLectura e:  EnumTipoLectura.values()){
            if(e.getTipoLectura().equals(tipo)){
                return e;
            }
        }
        return defaultTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getModoLectura() {
        return modoLectura;
    }

    public String getTipoLectura() {
        return tipoLectura;
    }

}
