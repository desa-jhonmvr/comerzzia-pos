/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.prefactura;

import com.comerzzia.jpos.util.enums.EnumEstadoPrefactura;

/**
 *
 * @author Gabriel Simbania
 */
public class ParamBuscarPrefactura {

    private EnumEstadoPrefactura enumEstadoPrefactura;
    private String numOrden;
    private String cabCodCli;
    private String cabFecha;

    public ParamBuscarPrefactura(EnumEstadoPrefactura enumEstadoPrefactura, String numOrden, String cabCodCli, String cabFecha) {
        this.enumEstadoPrefactura = enumEstadoPrefactura;
        this.numOrden = numOrden;
        this.cabCodCli = cabCodCli;
        this.cabFecha = cabFecha;
    }

    
    
    public EnumEstadoPrefactura getEnumEstadoPrefactura() {
        return enumEstadoPrefactura;
    }

    public void setEnumEstadoPrefactura(EnumEstadoPrefactura enumEstadoPrefactura) {
        this.enumEstadoPrefactura = enumEstadoPrefactura;
    }

    public String getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(String numOrden) {
        this.numOrden = numOrden;
    }

    public String getCabCodCli() {
        return cabCodCli;
    }

    public void setCabCodCli(String cabCodCli) {
        this.cabCodCli = cabCodCli;
    }

    public String getCabFecha() {
        return cabFecha;
    }

    public void setCabFecha(String cabFecha) {
        this.cabFecha = cabFecha;
    }
    
    
    
}
