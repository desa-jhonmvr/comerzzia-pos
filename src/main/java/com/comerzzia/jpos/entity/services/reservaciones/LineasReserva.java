/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author amos
 */
public class LineasReserva {

    private static final Logger log = Logger.getMLogger(LineasReserva.class);
    private List<LineaReserva> lineas;
    private List<DescuentoTicket> lineasDescuentos;

    public LineasReserva() {
        lineas = new LinkedList<LineaReserva>();
        lineasDescuentos = new ArrayList<DescuentoTicket>();
    }

    public LineaReserva nuevaLinea(String codigo, Articulos articulo, Integer cantidad, Tarifas tarifa) {
        LineaReserva linea = new LineaReserva(codigo, articulo, cantidad, tarifa);
        lineas.add(linea);
        return linea;
    }
    
    public boolean hayEnvioADomicilio(){       
        for (LineaReserva elem: lineas){
            if (elem.getDatosAdicionales()!=null && elem.getDatosAdicionales().isEnvioDomicilio())
                return true;
        }  
        return false;
    }

    public void eliminarLinea(int numLinea) {
        try {
            lineas.remove(numLinea);
        }
        catch (IndexOutOfBoundsException e) {
            log.error("Se esta intentando borrar una línea de ticket que no esta en la tabla.", e);
        }
    }

    protected void setDatosAdicionalesLinea(Integer index, DatosAdicionalesLineaTicket datosAdicionales) {
        LineaReserva linea = null;
        if (index == null) {
            linea = lineas.get(getIndexUltimaLinea());
        }
        else {
            linea = lineas.get(index);
        }
        linea.establecerDescuento(datosAdicionales.getDescuento());
        linea.setDatosAdicionales(datosAdicionales);

    }
    protected BigDecimal getDescuentoLinea(Integer index) {
                LineaReserva linea = null;
        if (index == null) {
            linea = lineas.get(getIndexUltimaLinea());
        }
        else {
            linea = lineas.get(index);
        }
        return linea.getDescuento();
    }

    /** Añade a la lista lineasContains pasada por parámetro todas las líneas del ticket
     * que contienen el artículo indicado por parámetro, siempre que dicha línea no tenga
     * ya una promoción aplicada. Devuelve el número de artículos (suma de cantidades de cada 
     * línea añadida) añadidos a la lista.
     * @param articulo
     * @param lineasContains
     * @return 
     */
    public int getContainsSinPromocion(String codArticulo, List<LineaReserva> lineasContains) {
        int cant = 0;
        for (LineaReserva linea : lineas) {
            if (!linea.isPromocionAplicada() && linea.getArticulo().getCodart().equals(codArticulo)) {
                lineasContains.add(linea);
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /** Añade a la lista lineasContains pasada por parámetro todas las líneas del ticket
     * que contienen un artículo de la categoría indicada por parámetro, siempre que dicha línea no tenga
     * ya una promoción aplicada. Devuelve el número de artículos (suma de cantidades de cada 
     * línea añadida) añadidos a la lista.
     * @param articulo
     * @param lineasContains
     * @return 
     */
    public int getContainsCategoriaSinPromocion(String codCategoria, List<LineaReserva> lineasContains) {
        int cant = 0;
        for (LineaReserva linea : lineas) {
            if (!linea.isPromocionAplicada() && linea.getArticulo().getCodcategoria().equals(codCategoria)) {
                lineasContains.add(linea);
                cant += linea.getCantidad();
            }
        }
        return cant;
    }
    
    /** Añade a la lista lineasContains pasada por parámetro todas las líneas del ticket
     * que contienen un artículo de la categoría indicada por parámetro, siempre que dicha línea no tenga
     * ya una promoción aplicada. Devuelve el número de artículos (suma de cantidades de cada 
     * línea añadida) añadidos a la lista.
     * @param articulo
     * @param lineasContains
     * @return 
     */
    public int getContainsSeccionesSinPromocion(String codSeccion, List<LineaReserva> lineasContains) {
        int cant = 0;
        for (LineaReserva linea : lineas) {
            if (!linea.isPromocionAplicada() && linea.getArticulo().getCodseccion().equals(codSeccion)) {
                lineasContains.add(linea);
                cant += linea.getCantidad();
            }
        }
        return cant;
    }
    
    /** Añade a la lista lineasContains pasada por parámetro todas las líneas del ticket
     * que contienen un artículo de la categoría indicada por parámetro, siempre que dicha línea no tenga
     * ya una promoción aplicada. Devuelve el número de artículos (suma de cantidades de cada 
     * línea añadida) añadidos a la lista.
     * @param articulo
     * @param lineasContains
     * @return 
     */
    public int getContainsSubSeccionesSinPromocion(String codSubSeccion, List<LineaReserva> lineasContains) {
        int cant = 0;
        for (LineaReserva linea : lineas) {
            if (!linea.isPromocionAplicada() && linea.getArticulo().getCodsubseccion().equals(codSubSeccion)) {
                lineasContains.add(linea);
                cant += linea.getCantidad();
            }
        }
        return cant;
    }
    
    
    public Integer getIndexLinea(String codigo) {
        for (int i = 0; i < getNumLineas(); i++) {
            if (getLinea(i).getCodigoBarras().equals(codigo)) {
                return i;
            }
        }
        return null;
    }

    public void addLineaDescuentoFinal(DescuentoTicket descuento) {
        lineasDescuentos.add(descuento);
    }

    public void resetearPromocionesMultiples() {
        for (LineaReserva linea : lineas) {
            if (linea.isPromoMultipleAplicada()) {
                linea.setPromocionLinea(null);
                linea.setPromoMultipleAplicada(false);
            }
        }
    }
    
    public void resetearLineasDescuento(){
        lineasDescuentos.clear();        
    }
    
    public int getNumLineas() {
        return lineas.size();
    }

    public int getIndexUltimaLinea() {
        return getNumLineas() - 1;
    }

    public LineaReserva getLinea(int i) {
        return lineas.get(i);
    }

    public List<LineaReserva> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaReserva> lineas) {
        this.lineas = lineas;
    }

    public List<DescuentoTicket> getDescuentos() {
        return lineasDescuentos;
    }


}
