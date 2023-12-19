/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.cotizacion;

import java.io.Serializable;

/**
 *
 * @author Mónica Enríquez
 */
public class TiendaDTO implements Serializable {

    private static final long serialVersionUID = 1866247838067723328L;
    
    private String nombreComercial;
    private String nombreSucursal;
    private String domicilioMatriz;
    private String domicilioSucursal;
    private String ruc;
    private String fecha;

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getDomicilioMatriz() {
        return domicilioMatriz;
    }

    public void setDomicilioMatriz(String domicilioMatriz) {
        this.domicilioMatriz = domicilioMatriz;
    }

    public String getDomicilioSucursal() {
        return domicilioSucursal;
    }

    public void setDomicilioSucursal(String domicilioSucursal) {
        this.domicilioSucursal = domicilioSucursal;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
  
    
}
