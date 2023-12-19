/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class SeleccionArticuloBean {

    protected static final byte TIPO_TODOS = 1;
    protected static final byte TIPO_MARCA = 2;
    protected static final byte TIPO_SECCION = 3;
    protected static final byte TIPO_COLECCION = 4;
    protected static final byte TIPO_ARTICULO = 5;
    protected static final byte TIPO_SUBSECCION = 6;
    protected static final byte TIPO_CATEGORIA = 7;
    protected static final byte TIPO_DESCRIPCION = 8;
    protected static final byte TIPO_EXCLUIDO = 9;
    private byte tipo;
    private boolean hayTipoMarca;
    private boolean hayTipoSeccion;
    private boolean hayTipoColeccion;
    private boolean hayTipoArticulo;
    private boolean hayTipoSubSeccion;
    private boolean hayCategoria;
    private boolean hayDescripcion;
    private boolean hayExcluidos;
    private List<ItemSeleccionArticuloBean> itemsMarca;
    private List<ItemSeleccionArticuloBean> itemsSeccion;
    private List<ItemSeleccionArticuloBean> itemsColeccion;
    private List<ItemSeleccionArticuloBean> itemsArticulo;
    private List<ItemSeleccionArticuloBean> itemsSubSeccion;
    private List<ItemSeleccionArticuloBean> itemsCategoria;
    private List<ItemSeleccionArticuloBean> itemsDescripcion;
    private List<ItemSeleccionArticuloBean> itemsExcluidos;
    
    private List<ItemSeleccionArticuloBean> items;

    public SeleccionArticuloBean(XMLDocumentNode nodo) throws XMLDocumentNodeNotFoundException {

        List<XMLDocumentNode> itemsNodo = nodo.getHijos();
        items = new ArrayList<ItemSeleccionArticuloBean>(itemsNodo.size());
        itemsMarca = new ArrayList<ItemSeleccionArticuloBean>();
        itemsSeccion = new ArrayList<ItemSeleccionArticuloBean>();
        itemsColeccion = new ArrayList<ItemSeleccionArticuloBean>();
        itemsArticulo = new ArrayList<ItemSeleccionArticuloBean>();
        itemsSubSeccion = new ArrayList<ItemSeleccionArticuloBean>();
        itemsCategoria = new ArrayList<ItemSeleccionArticuloBean>();
        itemsDescripcion = new ArrayList<ItemSeleccionArticuloBean>();
        itemsExcluidos = new ArrayList<ItemSeleccionArticuloBean>();
        
        for (XMLDocumentNode itemNodo : itemsNodo) {
            ItemSeleccionArticuloBean item = new ItemSeleccionArticuloBean(itemNodo);
            hayTipoMarca = hayTipoMarca || (item.getTipo() == TIPO_MARCA);
            hayTipoSeccion = hayTipoSeccion || (item.getTipo() == TIPO_SECCION);
            hayTipoColeccion = hayTipoColeccion || (item.getTipo() == TIPO_COLECCION);
            hayTipoArticulo = hayTipoArticulo || (item.getTipo() == TIPO_ARTICULO);
            hayTipoSubSeccion = hayTipoSubSeccion || (item.getTipo() == TIPO_SUBSECCION);
            hayCategoria = hayCategoria || (item.getTipo() == TIPO_CATEGORIA);
            hayDescripcion = hayDescripcion || (item.getTipo() == TIPO_DESCRIPCION);
            hayExcluidos = hayExcluidos || (item.getTipo() == TIPO_EXCLUIDO);
            boolean anyadirTipo = true;
            if(item.getTipo() == TIPO_MARCA){
                itemsMarca.add(item);
            } else if (item.getTipo() == TIPO_SECCION){
                itemsSeccion.add(item);
            } else if (item.getTipo() == TIPO_COLECCION){
                itemsColeccion.add(item);
            } else if (item.getTipo() == TIPO_ARTICULO){
                itemsArticulo.add(item);
            } else if (item.getTipo() == TIPO_SUBSECCION){
                itemsSubSeccion.add(item);
            } else if (item.getTipo() == TIPO_CATEGORIA){
                itemsCategoria.add(item);
            } else if (item.getTipo() == TIPO_DESCRIPCION){
                itemsDescripcion.add(item);
            } else if (item.getTipo() == TIPO_EXCLUIDO){
                itemsExcluidos.add(item);
            } else {
                anyadirTipo = false;
            }
            if(anyadirTipo){
                items.add(item);
            }
        }
        if(!hayTipoMarca && !hayTipoSeccion && !hayTipoColeccion && !hayTipoArticulo && !hayTipoSubSeccion && !hayCategoria && !hayDescripcion && !hayExcluidos){
            tipo = TIPO_TODOS;
        }

    }

    public List<PromoLineaCandidata> getAplicablesCheck(LineasTicket lineas, BigDecimal precioMinimo, List<LineaTicket> lineasAplicables) {
        List<LineaTicket> candidatas = new ArrayList<LineaTicket>();
        List<PromoLineaCandidata> aplicables = new ArrayList<PromoLineaCandidata>();

        if (isTipoTodos()) {
            if (lineas.getContains(false, candidatas, precioMinimo) > 0) {
                for (LineaTicket candidata : candidatas) {
                    PromoLineaCandidata promoLinea = new PromoLineaCandidata();
                    promoLinea.setLinea(candidata);
                    promoLinea.setValorLogico(false);
                    aplicables.add(promoLinea);
                }
            }
            return aplicables;
        }
        lineas.getContains(false, candidatas, precioMinimo, this, aplicables, lineasAplicables);
        return aplicables;
    }

    public boolean isAplicable(boolean soloSinPromocion, LineasTicket lineas) {
        return lineas.getIsAplicable(soloSinPromocion, this);
    }

    public Integer getCantidadAplicableSuma(boolean soloSinPromocion, LineasTicket lineas, List<LineaTicket>lineasAplicables) {
        if (isTipoTodos()) {
            return 0;
        }

        Integer cantidad = lineas.getCantidadAplicable(soloSinPromocion, this,lineasAplicables);
        if (cantidad>0) {
            return cantidad;
        }
        return -1;
    }

    public Integer getCantidadProporcionalAplicable(LineasTicket lineas, BigDecimal intervalo, List<LineaTicket> lineasAplicables) {
        if (isTipoTodos()) {
            return 1;
        }
        return lineas.getCantidadProporcionalAplicable(this, intervalo, lineasAplicables);
    }
    
    public BigDecimal getImporteAplicable(LineasTicket lineas, boolean esConDescuento, List<LineaTicket> lineasAplicables) {
        return lineas.getImporteAplicable(false, this, esConDescuento, lineasAplicables);
    }

    public boolean isTipoTodos() {
        return tipo == TIPO_TODOS;
    }
    
    public byte getTipo() {
        return tipo;
    }

    public boolean isHayTipoMarca() {
        return hayTipoMarca;
    }

    public boolean isHayTipoSeccion() {
        return hayTipoSeccion;
    }

    public boolean isHayTipoColeccion() {
        return hayTipoColeccion;
    }

    public boolean isHayTipoArticulo() {
        return hayTipoArticulo;
    }

    public boolean isHayTipoSubSeccion() {
        return hayTipoSubSeccion;
    }

    public boolean isHayCategoria() {
        return hayCategoria;
    }

    public boolean isHayDescripcion() {
        return hayDescripcion;
    }

    public List<ItemSeleccionArticuloBean> getItemsMarca() {
        return itemsMarca;
    }

    public List<ItemSeleccionArticuloBean> getItemsSeccion() {
        return itemsSeccion;
    }

    public List<ItemSeleccionArticuloBean> getItemsColeccion() {
        return itemsColeccion;
    }

    public List<ItemSeleccionArticuloBean> getItemsArticulo() {
        return itemsArticulo;
    }

    public List<ItemSeleccionArticuloBean> getItemsSubSeccion() {
        return itemsSubSeccion;
    }

    public List<ItemSeleccionArticuloBean> getItemsCategoria() {
        return itemsCategoria;
    }

    public List<ItemSeleccionArticuloBean> getItemsDescripcion() {
        return itemsDescripcion;
    }

    public boolean isHayExcluidos() {
        return hayExcluidos;
    }
    
    public boolean isHayAlgunTipo(){
        return hayTipoMarca || hayTipoSeccion || hayTipoSubSeccion || hayTipoColeccion || hayCategoria || hayDescripcion;
    }

    public List<ItemSeleccionArticuloBean> getItemsExcluidos() {
        return itemsExcluidos;
    }
    
    
}
