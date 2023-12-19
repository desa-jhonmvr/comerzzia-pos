/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;

/**
 *
 * @author amos
 */
public class ItemSeleccionArticuloBean {

    private byte tipo;
    private String codMarca;
    private String codSeccion;
    private String codSubseccion;
    private String codCategoria;
    private String codArticulo;
    private String codArtExcluido;
    private String codColeccion;
    private Integer puntosAdicionales;
    private String filtroDescripcion;
    private boolean check;

    public ItemSeleccionArticuloBean(XMLDocumentNode itemNodo, boolean esMultiple) throws XMLDocumentNodeNotFoundException {
        codMarca = itemNodo.getNodo("marca").getNodo("codMarca").getValue();
        codSeccion = itemNodo.getNodo("seccion").getNodo("codSeccion").getValue();
        codSubseccion = itemNodo.getNodo("seccion").getNodo("codSubseccion").getValue();
        codCategoria = itemNodo.getNodo("seccion").getNodo("codCategoria").getValue();
        codColeccion = itemNodo.getNodo("coleccion").getNodo("codColeccion").getValue();
        codArticulo = itemNodo.getNodo("articulo").getNodo("codArticulo").getValue();
    }
        
    public ItemSeleccionArticuloBean(XMLDocumentNode itemNodo) throws XMLDocumentNodeNotFoundException {
        String tipoNodo = itemNodo.getNombre();
        if (tipoNodo.equals("marca")) {
            tipo = SeleccionArticuloBean.TIPO_MARCA;
        }
        else if (tipoNodo.equals("seccion")) {
            tipo = SeleccionArticuloBean.TIPO_SECCION;
        }
        else if (tipoNodo.equals("coleccion")) {
            tipo = SeleccionArticuloBean.TIPO_COLECCION;
        }
        else if (tipoNodo.equals("articulo")) {
            tipo = SeleccionArticuloBean.TIPO_ARTICULO;
        }
        else if (tipoNodo.equals("descripcion")) {
            tipo = SeleccionArticuloBean.TIPO_DESCRIPCION;
        }    
        else if (tipoNodo.equals("excluido")) {
            tipo = SeleccionArticuloBean.TIPO_EXCLUIDO;
        }    
        
        if (tipo == SeleccionArticuloBean.TIPO_ARTICULO){
            codArticulo = itemNodo.getNodo("codArticulo").getValue();
        }
        else if (tipo == SeleccionArticuloBean.TIPO_COLECCION){
            codColeccion = itemNodo.getNodo("codColeccion").getValue();
        }
        else if (tipo == SeleccionArticuloBean.TIPO_MARCA){
            codMarca = itemNodo.getNodo("codMarca").getValue();
        }
        else if (tipo == SeleccionArticuloBean.TIPO_EXCLUIDO){
            codArtExcluido = itemNodo.getNodo("codArtExcluido").getValue();
        }
        else if (tipo == SeleccionArticuloBean.TIPO_DESCRIPCION){
            filtroDescripcion = itemNodo.getNodo("desArticulo").getValue();
            filtroDescripcion = filtroDescripcion.toUpperCase();
            filtroDescripcion = filtroDescripcion.replace(".", "\\.");
            filtroDescripcion = filtroDescripcion.replace("%", ".*");
        }
        else if (tipo == SeleccionArticuloBean.TIPO_SECCION){
            codSeccion = itemNodo.getNodo("codSeccion").getValue();
            codSubseccion = itemNodo.getNodo("codSubseccion").getValue();
            codCategoria = itemNodo.getNodo("codCategoria").getValue();
            if (codSubseccion != null && !codSubseccion.isEmpty()){
                this.tipo = SeleccionArticuloBean.TIPO_SUBSECCION;
            }
            if (codCategoria != null && !codCategoria.isEmpty()){
                this.tipo = SeleccionArticuloBean.TIPO_CATEGORIA;
            }
        }
        try{
            puntosAdicionales = itemNodo.getNodo("puntosAdicionales").getValueAsInteger();
        }
        catch(Exception e){
            // si no trae puntos adicionales, ignoramos este parámetro
        }        
        try{
            check = itemNodo.getNodo("checkExtra").getValueAsBoolean();
        }
        catch(Exception e){
            check = false;
        }
    }

    public boolean isCheck() {
        return check;
    }

    public byte getTipo() {
        return tipo;
    }

    public String getCodMarca() {
        return codMarca;
    }

    public String getCodArtExcluido() {
        return codArtExcluido;
    }
    public String getCodSeccion() {
        return codSeccion;
    }

    public String getCodSubseccion() {
        return codSubseccion;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public String getCodColeccion() {
        return codColeccion;
    }

    public String getFiltroDescripcion() {
        return filtroDescripcion;
    }

    public Integer getPuntosAdicionales() {
        return puntosAdicionales;
    }

    public void setPuntosAdicionales(Integer puntosAdicionales) {
        this.puntosAdicionales = puntosAdicionales;
    }
    
}
