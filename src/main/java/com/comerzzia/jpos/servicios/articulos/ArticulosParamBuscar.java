/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.articulos;

import com.comerzzia.util.cadenas.Cadena;

/**
 *
 * @author amos
 */
public class ArticulosParamBuscar {
    private String codigo; // puede ser código de artículo o código de barras
    private String descripcion;
    private String codTarifa;
    private String uidActividad;
    
    // Para búsquedas en la vista de artículos
    private String marca;
    private String proveedor;
    private String seccion;
    private String subseccion;
    private String modelo;
    private String codigoBarras;
    private String codAlmLocal;
    private String codAlmCentral;
    private String codAlmCentral2;


    public ArticulosParamBuscar() {
        //Por defecto los parámetros de búsqueda serán cadenas vacías
        marca = "";
        proveedor  = "";
        seccion = "";
        subseccion = "";
        modelo = "";
        codigoBarras = "";
        codAlmLocal = "";  
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = getCodArticuloSukasa(codigo);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodTarifa() {
        return codTarifa;
    }

    public void setCodTarifa(String codTarifa) {
        this.codTarifa = codTarifa;
    }

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getSubseccion() {
        return subseccion;
    }

    public void setSubseccion(String subseccion) {
        this.subseccion = subseccion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCodAlmLocal() {
        return codAlmLocal;
    }

    public void setCodAlmLocal(String codAlmLocal) {
        this.codAlmLocal = codAlmLocal;
    }

    public String getCodAlmCentral() {
        return codAlmCentral;
    }

    public void setCodAlmCentral(String codAlmCentral) {
        this.codAlmCentral = codAlmCentral;
    }

    public String getCodAlmCentral2() {
        return codAlmCentral2;
    }

    public void setCodAlmCentral2(String codAlmCentral2) {
        this.codAlmCentral2 = codAlmCentral2;
    }
    
    public static String getCodArticuloSukasa(String codArticulo){
        if (codArticulo == null || codArticulo.isEmpty()){
        	return codArticulo;
        }
        String[] codigos = codArticulo.split("\\.");
        if (codigos.length != 2){
        	return codArticulo;
        }
        if (codigos[0].isEmpty()){
            return "." + Cadena.completaconCeros(codigos[1], 4);
        }
        return Cadena.completaconCeros(codigos[0], 4) + "." + Cadena.completaconCeros(codigos[1], 4);
    	
    }    
    
    public boolean isFiltroVacio() {
        if((descripcion == null || descripcion.isEmpty()) && (marca == null || marca.isEmpty()) && (codigo == null || codigo.isEmpty()) &&
                (codigoBarras == null || codigoBarras.isEmpty()) && (seccion == null || seccion.isEmpty()) && (subseccion == null || subseccion.isEmpty())
                && (modelo == null || modelo.isEmpty())){
            return true;
        }
        return false;
    }
}
