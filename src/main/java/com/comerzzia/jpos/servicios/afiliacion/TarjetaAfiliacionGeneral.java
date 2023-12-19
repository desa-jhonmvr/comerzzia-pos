/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion;

public class TarjetaAfiliacionGeneral implements ITarjetaAfiliacion {

    private String numero;
    private String tipoLectura;
    private String tipoAfiliacion;
    
    public TarjetaAfiliacionGeneral() {
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public String getTipoAfiliacion(){
        return tipoAfiliacion;
    }

    @Override
    public String getTipoLectura() {
        return tipoLectura;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTipoAfiliacion(String tipoAfiliacion) {
        this.tipoAfiliacion = tipoAfiliacion;
    }

    public void setTipoLectura(String tipoLectura) {
        this.tipoLectura = tipoLectura;
    }
    
    
}
