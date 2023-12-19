/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class SeleccionArticuloMultipleBean {
    
    protected static Logger log = Logger.getMLogger(SeleccionArticuloMultipleBean.class);

    protected static final byte TIPO_TODOS = 1;
    protected static final byte TIPO_MARCA = 2;
    protected static final byte TIPO_SECCION = 3;
    protected static final byte TIPO_MARCA_SECCION = 4;
    protected static final byte TIPO_COLECCION = 5;
    protected static final byte TIPO_ARTICULO = 6;
    protected static final byte TIPO_MARCA_SUBSECCION = 7;
    protected static final byte TIPO_MARCA_CATEGORIA = 8;
    protected static final byte TIPO_SUBSECCION = 9;
    protected static final byte TIPO_CATEGORIA = 10;
    protected static final byte TIPO_DESCRIPCION = 11;
    private byte tipo;
    private List<ItemSeleccionArticuloBean> items;

    public SeleccionArticuloMultipleBean(XMLDocumentNode nodo) throws XMLDocumentNodeNotFoundException {
        List<XMLDocumentNode> itemsNodo = nodo.getHijos();
        items = new ArrayList<ItemSeleccionArticuloBean>();

        for (XMLDocumentNode itemNodo : itemsNodo) {
            String atrTipo = itemNodo.getNombre();
            if (!atrTipo.equals("item")) {
                continue;
            }
            ItemSeleccionArticuloBean item = new ItemSeleccionArticuloBean(itemNodo, true);
            items.add(item);
        }
        // Puede que el tipo lo hayamos concretado aún más dentro del item. Lo actualizamos.
        if (items.isEmpty()){
            tipo = TIPO_TODOS;
        }
    }

    public List<LineaTicket> isAplicable(LineasTicket lineas) {
        // Si es de tipo todo, retornamos true
        if (isTipoTodos()) {
            return lineas.getLineas(); 
        }
        // Suponemos que si algún item permite la aplicación, entonces la selección se dice aplicable.
        // No tratamos acumulado de cantidad aplicable para todos los items por eficiencia, aunque se podría hacer
        return lineas.getIsAplicable(false, this);
    }
    
    public BigDecimal getImporteAplicable(LineasTicket lineas) {
        List<LineaTicket> aplicables = new ArrayList<LineaTicket>();
        if(isTipoTodos()) {
           aplicables = lineas.getLineas(); 
        } else {
            aplicables = lineas.getIsAplicable(true, this);
        }
        BigDecimal importe = BigDecimal.ZERO;
        for(LineaTicket aplicada:aplicables){
            importe = importe.add(aplicada.getImporteTotal());
            log.debug("De la línea "+aplicada.toString()+" se aplican "+aplicada.getImporteTotal());
        }
        
        return importe;
    }

    public boolean isTipoTodos() {
        return tipo == TIPO_TODOS;
    }
    
    public byte getTipo(){
        return tipo;
    }

    public List<ItemSeleccionArticuloBean> getItems(){
        return items;
    }
    
    public boolean isAplicableALinea(LineaTicket linea, ItemSeleccionArticuloBean item){
        boolean res = true;
        //Comparamos las marcas
        if(item.getCodMarca() != null && !item.getCodMarca().isEmpty() && linea.getArticulo().getCodmarca().getCodmarca() != null && !linea.getArticulo().getCodmarca().getCodmarca().isEmpty()){
            res = res && item.getCodMarca().equals(linea.getArticulo().getCodmarca().getCodmarca());
        }
        //Comparamos las categorias
        if(item.getCodCategoria() != null && !item.getCodCategoria().isEmpty() && linea.getArticulo().getCodcategoria() != null && !linea.getArticulo().getCodcategoria().isEmpty()){
            res = res && item.getCodCategoria().equals(linea.getArticulo().getCodcategoria());
        }  else {
            //Comparamos las subsecciones
           if(item.getCodSubseccion() != null && !item.getCodSubseccion().isEmpty() && linea.getArticulo().getCodsubseccion() != null && !linea.getArticulo().getCodsubseccion().isEmpty()){
               res = res && item.getCodSubseccion().equals(linea.getArticulo().getCodsubseccion());
           } else {
                //Comparamos las secciones
               if(item.getCodSeccion() != null && !item.getCodSeccion().isEmpty() && linea.getArticulo().getCodseccion() != null && !linea.getArticulo().getCodseccion().isEmpty()){
                   res = res && item.getCodSeccion().equals(linea.getArticulo().getCodseccion());
               }                 
           }             
        }      

        //Comparamos las colecciones
        if(item.getCodColeccion() != null && !item.getCodColeccion().isEmpty() && linea.getArticulo().getColeccion()!= null && !linea.getArticulo().getColeccion().isEmpty()){
            res = res && item.getCodColeccion().equals(linea.getArticulo().getColeccion());
        }          
        //Comparamos los artículos
        if(item.getCodArticulo() != null && !item.getCodArticulo().isEmpty() && linea.getArticulo().getCodart()!= null && !linea.getArticulo().getCodart().isEmpty()){
            res = res && item.getCodArticulo().equals(linea.getArticulo().getCodart());
        }            
        return res;
    }
}
