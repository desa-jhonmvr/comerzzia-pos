/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.core.impuestos.ImpuestosServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.promociones.articulos.ItemSeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.articulos.PromoLineaCandidata;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloMultipleBean;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.enums.EnumCodigoImpuestos;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author amos
 */
public class LineasTicket {

    private static final Logger log = Logger.getMLogger(LineasTicket.class);
    private List<LineaTicket> lineas;
    private List<DescuentoTicket> lineasDescuentos;
    private TicketS ticket;
    private Impuestos impuestos;
    ImpuestosServices impuestosServices = ImpuestosServices.getInstance();
    private boolean garantiaExtendidaGratuitaRechazada = false;

    public LineasTicket() {
        lineas = new LinkedList<LineaTicket>();
        lineasDescuentos = new ArrayList<DescuentoTicket>();
    }

    public LineaTicket nuevaLinea(String codigo, Articulos articulo, Integer cantidad, Tarifas tarifa) {
        //Se agrega el campo landed
        //  LineaTicket linea = new LineaTicket(codigo, articulo, cantidad, tarifa.getPrecioVenta(), tarifa.getPrecioTotal(),tarifa.getCostoLanded());
        LineaTicket linea = new LineaTicket(codigo, articulo, cantidad, tarifa.getPrecioVenta(), tarifa.getPrecioTotal(), tarifa.getPrecioCosto(), articulo.getCodcategoria(), tarifa.getPrecioReal());
        lineas.add(linea);
        return linea;
    }

    public LineaTicket nuevaLineaDevolucion(String codigo, Articulos articulo, Integer cantidad, BigDecimal precioVenta, BigDecimal precioTotal) {
        LineaTicket linea = new LineaTicket(codigo, articulo, cantidad, precioVenta, precioTotal, true);
        lineas.add(linea);
        return linea;
    }

    public int getNumGarantiasExtendidas() {
        int total = 0;
        for (LineaTicket lineaTicket : lineas) {
            if (lineaTicket.isGarantiaExtendida()) {
                total++;
            }
        }
        return total;
    }

    public boolean tieneGarantiaExtendida() {
        return getNumGarantiasExtendidas() > 0;
    }

    public boolean hayEnvioADomicilio() {
        for (LineaTicket elem : lineas) {
            if (elem.getDatosAdicionales() != null && elem.getDatosAdicionales().isEnvioDomicilio()) {
                return true;
            }
        }
        return false;
    }

    public boolean hayRecogidaPosterior() {
        for (LineaTicket elem : lineas) {
            if (elem.getDatosAdicionales() != null && elem.getDatosAdicionales().isRecogidaPosterior()) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return lineas.isEmpty();
    }

    public void eliminarLinea(int numLinea) {
        try {
            LineaTicket linea = getLinea(numLinea);
            lineas.remove(numLinea);

            // reseteamos canjeo de puntos
            ticket.getPuntosTicket().resetearCanjeo();

            // reseteamos promociones
            if (lineas.isEmpty()) {
                ticket.getTicketPromociones().getPromocionesFiltrosPagos().reset();
            } else if (linea.isPromocionAplicada()) {
                ticket.getTicketPromociones().resetearLinea(linea);
            }
        } catch (IndexOutOfBoundsException e) {
            log.error("Se esta intentando borrar una línea de ticket que no esta en la tabla.", e);
        }
    }

    public void setDatosAdicionalesLinea(Integer index, DatosAdicionalesLineaTicket datosAdicionales) {
        LineaTicket linea = null;
        if (index == null) {
            linea = lineas.get(getIndexUltimaLinea());
        } else {
            linea = lineas.get(index);
        }
        //comentado para poder realizar edicion en las promociones de todos los produtos Rd
//        if (!linea.isPromoUnitariaAplicada()) {
        linea.establecerDescuento(datosAdicionales.getDescuento());
//        }

        linea.setDatosAdicionales(datosAdicionales);

    }

    public BigDecimal getDescuentoLinea(Integer index) {
        LineaTicket linea = null;
        if (index == null) {
            linea = lineas.get(getIndexUltimaLinea());
        } else {
            linea = lineas.get(index);
        }
        return linea.getDescuento();
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket. Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContains(boolean soloSinPromocion, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen el artículo indicado por parámetro. Devuelve el
     * número de artículos (suma de cantidades de cada línea añadida) añadidos a
     * la lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     */
    public int getContains(boolean soloSinPromocion, String codArticulo, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodart().equals(codArticulo)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsCategoria(boolean soloSinPromocion, String codCategoria, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodcategoria().equals(codCategoria)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsSecciones(boolean soloSinPromocion, String codSeccion, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodseccion().equals(codSeccion)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsSubSecciones(boolean soloSinPromocion, String codSubSeccion, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodsubseccion().equals(codSubSeccion)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la marca indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsMarcas(boolean soloSinPromocion, String codMarca, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodmarca().getCodmarca().equals(codMarca)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la colección indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsColeccion(boolean soloSinPromocion, String codColeccion, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getColeccion() != null
                    && linea.getArticulo().getColeccion().equals(codColeccion)
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo con una descripción que cumpla el patrón
     * indicado por parámetro. Devuelve el número de artículos (suma de
     * cantidades de cada línea añadida) añadidos a la lista. Si se indica sólo
     * sin Promoción, sólo se tendrán en cuenta las líneas que no tengan
     * promoción aplicada.
     */
    public int getContainsDescripcion(boolean soloSinPromocion, String filtroDescripcion, List<LineaTicket> lineasContains, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getDesart().matches("(.*)" + filtroDescripcion + "(.*)")
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                if (lineasContains != null) {
                    lineasContains.add(linea);
                }
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    public int getContainsSinGarExt(boolean soloSinPromocion, BigDecimal precioMinimo) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if (!linea.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS)) && (!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && (precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                cant += linea.getCantidad();
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket. Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContains(boolean soloSinPromocion, List<LineaTicket> lineasContains) {
        return getContains(soloSinPromocion, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen el artículo indicado por parámetro. Devuelve el
     * número de artículos (suma de cantidades de cada línea añadida) añadidos a
     * la lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     */
    public int getContains(boolean soloSinPromocion, String codArticulo, List<LineaTicket> lineasContains) {
        return getContains(soloSinPromocion, codArticulo, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsCategoria(boolean soloSinPromocion, String codCategoria, List<LineaTicket> lineasContains) {
        return getContainsCategoria(soloSinPromocion, codCategoria, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsSecciones(boolean soloSinPromocion, String codSeccion, List<LineaTicket> lineasContains) {
        return getContainsSecciones(soloSinPromocion, codSeccion, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la categoría indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsSubSecciones(boolean soloSinPromocion, String codSubSeccion, List<LineaTicket> lineasContains) {
        return getContainsSubSecciones(soloSinPromocion, codSubSeccion, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la marca indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsMarcas(boolean soloSinPromocion, String codMarca, List<LineaTicket> lineasContains) {
        return getContainsMarcas(soloSinPromocion, codMarca, lineasContains, null);
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que contienen un artículo de la colección indicada por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada línea
     * añadida) añadidos a la lista. Si se indica sólo sin Promoción, sólo se
     * tendrán en cuenta las líneas que no tengan promoción aplicada.
     */
    public int getContainsColeccion(boolean soloSinPromocion, String codColeccion, List<LineaTicket> lineasContains) {
        return getContainsColeccion(soloSinPromocion, codColeccion, lineasContains, null);
    }

    /**
     * Devuelve true si la promoción recogida en SeleccionArticulosBean es
     * aplicable a alguna de las LineasTicket
     *
     * @param soloSinPromocion
     * @param seleccionArticulo
     * @return
     */
    public List<LineaTicket> getIsAplicable(boolean soloSinPromocion, SeleccionArticuloMultipleBean seleccionArticulo) {
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        if (seleccionArticulo.isTipoTodos()) {
            return this.getLineas();
        }
        for (LineaTicket linea : lineas) {
            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones            
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItems()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && seleccionArticulo.isAplicableALinea(linea, item)) {
                    lineasAplicables.add(linea);
                    break;
                }
            }
        }
        return lineasAplicables;
    }

    /**
     * Devuelve true si la promoción recogida en SeleccionArticulosBean es
     * aplicable a alguna de las LineasTicket
     *
     * @param soloSinPromocion
     * @param seleccionArticulo
     * @return
     */
    public boolean getIsAplicable(boolean soloSinPromocion, SeleccionArticuloBean seleccionArticulo) {
        if (seleccionArticulo.isTipoTodos()) {
            return true;
        }
        for (LineaTicket linea : lineas) {
            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones            

            boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                    resArticulo = true;
                    break;
                }
            }

            if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                return true;
            }

            boolean resMarca = !seleccionArticulo.isHayTipoMarca();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                    resMarca = true;
                    break;
                }
            }
            if (!resMarca && !resArticulo) {
                continue;
            }

            boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                    resSeccion = true;
                    break;
                }
            }
            if (!resSeccion && !resArticulo) {
                continue;
            }

            boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getColeccion() != null
                        && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                    resColeccion = true;
                    break;
                }
            }
            if (!resColeccion && !resArticulo) {
                continue;
            }

            boolean resExcluido = true;
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                    resExcluido = false;
                    break;
                }
            }
            if (!resExcluido) {
                continue;
            }

            boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                    resSubSeccion = true;
                    break;
                }
            }
            if (!resSubSeccion && !resArticulo) {
                continue;
            }

            boolean resCategoria = !seleccionArticulo.isHayCategoria();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                    resCategoria = true;
                    break;
                }
            }
            if (!resCategoria && !resArticulo) {
                continue;
            }

            boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                //cambio para que solo busque lo que empeiza con la palabra 
//                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado())) 
//                && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")){
//                    resDescripcion = true;
//                    break;
//                }
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getDesart().startsWith(item.getFiltroDescripcion())) {
                    resDescripcion = true;
                    break;
                }
            }
            if (!resDescripcion && !resArticulo) {
                continue;
            }

            if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve las lineas que pueden aplicarse si la promoción recogida en
     * SeleccionArticulosBean es aplicable a alguna de las LineasTicket
     *
     * @param soloSinPromocion
     * @param seleccionArticulo
     * @return
     */
    public List<LineaTicket> getLineasAplicables(boolean soloSinPromocion, SeleccionArticuloBean seleccionArticulo) {
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        for (LineaTicket linea : lineas) {

            // Si es tipo todos, no tenemos que analizar más de esta línea. Sólo comprobamos si tiene o no promoción anterior
            if (seleccionArticulo.isTipoTodos()) {
                if (!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado())) {
                    lineasAplicables.add(linea);
                }
                continue;
            }

            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones  
            boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                    resArticulo = true;
                    break;
                }
            }
            if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                lineasAplicables.add(linea);
                continue;
            }

            boolean resMarca = !seleccionArticulo.isHayTipoMarca();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                    resMarca = true;
                    break;
                }
            }
            if (!resMarca) {
                continue;
            }

            boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                    resSeccion = true;
                    break;
                }
            }
            if (!resSeccion) {
                continue;
            }

            boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getColeccion() != null
                        && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                    resColeccion = true;
                    break;
                }
            }
            if (!resColeccion) {
                continue;
            }

            boolean resExcluido = true;
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                    resExcluido = false;
                    break;
                }
            }
            if (!resExcluido) {
                continue;
            }

            boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                    resSubSeccion = true;
                    break;
                }
            }
            if (!resSubSeccion) {
                continue;
            }

            boolean resCategoria = !seleccionArticulo.isHayCategoria();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                    resCategoria = true;
                    break;
                }
            }
            if (!resCategoria) {
                continue;
            }

            boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado() && !linea.isArticuloSeleccionado()))
                        && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                    resDescripcion = true;
                    break;
                }
            }
            if (!resDescripcion) {
                continue;
            }

            if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                lineasAplicables.add(linea);
            }
        }
        return lineasAplicables;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que cumplen la condicion de seleccionArticulo. Devuelve el número
     * de artículos (suma de cantidades de cada línea añadida) añadidos a la
     * lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     *
     * @param soloSinPromocion
     * @param lineasContains
     * @param precioMinimo
     * @param aplicables
     * @param seleccionArticulo
     * @param lineasAplicadas
     * @return
     */
    public int getContains(boolean soloSinPromocion, List<LineaTicket> lineasContains, BigDecimal precioMinimo, SeleccionArticuloBean seleccionArticulo, List<PromoLineaCandidata> aplicables,
            List<LineaTicket> lineasAplicadas) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if ((precioMinimo == null || precioMinimo.compareTo(linea.getPrecioTotal()) <= 0)) {
                PromoLineaCandidata promoLinea = new PromoLineaCandidata();
                promoLinea.setValorLogico(true);
                //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones     

                boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                        resArticulo = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                    if (lineasContains != null) {
                        lineasContains.add(linea);
                    }
                    cant += linea.getCantidad();
                    promoLinea.setLinea(linea);
                    aplicables.add(promoLinea);
                    lineasAplicadas.add(linea);
                    continue;
                }

                boolean resMarca = !seleccionArticulo.isHayTipoMarca();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                        resMarca = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resMarca && !resArticulo) {
                    continue;
                }

                boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                        resSeccion = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resSeccion && !resArticulo) {
                    continue;
                }

                boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getColeccion() != null
                            && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                        resColeccion = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resColeccion && !resArticulo) {
                    continue;
                }

                boolean resExcluido = true;
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                    if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                        resExcluido = false;
                        break;
                    }
                }
                if (!resExcluido) {
                    continue;
                }

                boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                        resSubSeccion = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resSubSeccion && !resArticulo) {
                    continue;
                }

                boolean resCategoria = !seleccionArticulo.isHayCategoria();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                        resCategoria = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resCategoria && !resArticulo) {
                    continue;
                }

                boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
                for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                    if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                            && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                        resDescripcion = true;
                        promoLinea.setValorLogico(item.isCheck() && promoLinea.isValorLogico());
                        break;
                    }
                }
                if (!resDescripcion && !resArticulo) {
                    continue;
                }

                if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                    if (lineasContains != null) {
                        lineasContains.add(linea);
                    }
                    cant += linea.getCantidad();
                    promoLinea.setLinea(linea);
                    aplicables.add(promoLinea);
                    lineasAplicadas.add(linea);
                }
            }
        }
        return cant;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que cumplen la condicion de seleccionArticulo. Devuelve el número
     * de artículos (suma de cantidades de cada línea añadida) añadidos a la
     * lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     *
     * @param soloSinPromocion
     * @param seleccionArticulo
     * @param lineasAplicadas
     * @return
     */
    public int getCantidadAplicable(boolean soloSinPromocion, SeleccionArticuloBean seleccionArticulo, List<LineaTicket> lineasAplicadas) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            int puntosAdicionales = 0;
            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones  
            boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                    resArticulo = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                cant += (linea.getCantidad() * puntosAdicionales);
                lineasAplicadas.add(linea);
                continue;
            }

            boolean resMarca = !seleccionArticulo.isHayTipoMarca();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                    resMarca = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resMarca && !resArticulo) {
                continue;
            }

            boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                    resSeccion = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resSeccion && !resArticulo) {
                continue;
            }

            boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getColeccion() != null
                        && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                    resColeccion = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resColeccion && !resArticulo) {
                continue;
            }

            boolean resExcluido = true;
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                    resExcluido = false;
                    break;
                }
            }
            if (!resExcluido) {
                continue;
            }

            boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                    resSubSeccion = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resSubSeccion && !resArticulo) {
                continue;
            }

            boolean resCategoria = !seleccionArticulo.isHayCategoria();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                    resCategoria = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resCategoria && !resArticulo) {
                continue;
            }

            boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                    resDescripcion = true;
                    puntosAdicionales += item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resDescripcion && !resArticulo) {
                continue;
            }

            if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                cant += (linea.getCantidad() * puntosAdicionales);
                lineasAplicadas.add(linea);
            }
        }
        return cant;
    }

    public boolean getLineaAplicable(boolean soloSinPromocion, SeleccionArticuloBean seleccionArticulo, boolean esConDescuento, LineaTicket linea) {
        // Si es tipo todos, no tenemos que analizar más de esta línea. Sólo comprobamos si tiene o no promoción anterior
        if (seleccionArticulo.isTipoTodos()) {
            return true;
        }

        //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones 
        boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                resArticulo = true;
                break;
            }
        }
        if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
            return true;
        }

        boolean resMarca = !seleccionArticulo.isHayTipoMarca();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                resMarca = true;
                break;
            }
        }
        if (!resMarca && !resArticulo) {
            return false;
        }

        boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                resSeccion = true;
                break;
            }
        }
        if (!resSeccion && !resArticulo) {
            return false;
        }

        boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getColeccion() != null
                    && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                resColeccion = true;
                break;
            }
        }
        if (!resColeccion && !resArticulo) {
            return false;
        }

        boolean resExcluido = true;
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
            if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                resExcluido = false;
                break;
            }
        }
        if (!resExcluido) {
            return false;
        }

        boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                resSubSeccion = true;
                break;
            }
        }
        if (!resSubSeccion && !resArticulo) {
            return false;
        }

        boolean resCategoria = !seleccionArticulo.isHayCategoria();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                resCategoria = true;
                break;
            }
        }
        if (!resCategoria && !resArticulo) {
            return false;
        }

        boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
        for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
            if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                    && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                resDescripcion = true;
                break;
            }
        }
        if (!resDescripcion && !resArticulo) {
            return false;
        }

        if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que cumplen la condicion de seleccionArticulo. Devuelve el número
     * de artículos (suma de cantidades de cada línea añadida) añadidos a la
     * lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     *
     * @param soloSinPromocion
     * @param seleccionArticulo
     * @param esConDescuento
     * @param lineasAplicadas
     * @return
     */
    public BigDecimal getImporteAplicable(boolean soloSinPromocion, SeleccionArticuloBean seleccionArticulo, boolean esConDescuento, List<LineaTicket> lineasAplicadas) {
        BigDecimal acumulado = BigDecimal.ZERO;
        for (LineaTicket linea : lineas) {

            // Si es tipo todos, no tenemos que analizar más de esta línea. Sólo comprobamos si tiene o no promoción anterior
            if (seleccionArticulo.isTipoTodos()) {
                if (!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado())) {
                    if (esConDescuento) {
                        acumulado = acumulado.add(linea.getImporteTotalFinalPagado());
                    } else {
                        acumulado = acumulado.add(linea.getImporteTotal());
                    }
                }
                lineasAplicadas.add(linea);
                continue;
            }

            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones 
            boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                    resArticulo = true;
                    break;
                }
            }
            if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                if (esConDescuento) {
                    acumulado = acumulado.add(linea.getImporteTotalFinalPagado());
                } else {
                    acumulado = acumulado.add(linea.getImporteTotal());
                }
                lineasAplicadas.add(linea);
                continue;
            }

            boolean resMarca = !seleccionArticulo.isHayTipoMarca();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                    resMarca = true;
                    break;
                }
            }
            if (!resMarca && !resArticulo) {
                continue;
            }

            boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                    resSeccion = true;
                    break;
                }
            }
            if (!resSeccion && !resArticulo) {
                continue;
            }

            boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getColeccion() != null
                        && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                    resColeccion = true;
                    break;
                }
            }
            if (!resColeccion && !resArticulo) {
                continue;
            }

            boolean resExcluido = true;
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                    resExcluido = false;
                    break;
                }
            }
            if (!resExcluido) {
                continue;
            }

            boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                    resSubSeccion = true;
                    break;
                }
            }
            if (!resSubSeccion && !resArticulo) {
                continue;
            }

            boolean resCategoria = !seleccionArticulo.isHayCategoria();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                    resCategoria = true;
                    break;
                }
            }
            if (!resCategoria && !resArticulo) {
                continue;
            }

            boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                if ((!soloSinPromocion || (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()))
                        && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                    resDescripcion = true;
                    break;
                }
            }
            if (!resDescripcion && !resArticulo) {
                continue;
            }

            if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                if (esConDescuento) {
                    acumulado = acumulado.add(linea.getImporteTotalFinalPagado());
                } else {
                    acumulado = acumulado.add(linea.getImporteTotal());
                }
                lineasAplicadas.add(linea);
            }
        }
        return acumulado;
    }

    /**
     * Añade a la lista lineasContains pasada por parámetro todas las líneas del
     * ticket que cumplen la condicion de seleccionArticulo. Devuelve el número
     * de artículos (suma de cantidades de cada línea añadida) añadidos a la
     * lista. Si se indica sólo sin Promoción, sólo se tendrán en cuenta las
     * líneas que no tengan promoción aplicada.
     *
     * @param seleccionArticulo
     * @param intervalo
     * @param lineasAplicadas
     * @return
     */
    public Integer getCantidadProporcionalAplicable(SeleccionArticuloBean seleccionArticulo, BigDecimal intervalo, List<LineaTicket> lineasAplicadas) {
        Integer acumulado = 1;
        for (LineaTicket linea : lineas) {
            int puntosAdicionales = 1;
            //Comprobamos todos los tipos para ver que la línea cumple al menos una de todos los tipos de condiciones   
            boolean resArticulo = !seleccionArticulo.isHayTipoArticulo();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsArticulo()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getCodart().equals(item.getCodArticulo())) {
                    resArticulo = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (resArticulo && seleccionArticulo.isHayTipoArticulo()) {
                acumulado *= puntosAdicionales;
                lineasAplicadas.add(linea);
                continue;
            }

            boolean resMarca = !seleccionArticulo.isHayTipoMarca();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsMarca()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                    resMarca = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resMarca && !resArticulo) {
                continue;
            }

            boolean resSeccion = !seleccionArticulo.isHayTipoSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSeccion()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getCodseccion().equals(item.getCodSeccion())) {
                    resSeccion = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resSeccion && !resArticulo) {
                continue;
            }

            boolean resColeccion = !seleccionArticulo.isHayTipoColeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsColeccion()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getColeccion() != null
                        && linea.getArticulo().getColeccion().equals(item.getCodColeccion())) {
                    resColeccion = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resColeccion && !resArticulo) {
                continue;
            }

            boolean resExcluido = true;
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsExcluidos()) {
                if (linea.getArticulo().getCodart().equals(item.getCodArtExcluido())) {
                    resExcluido = false;
                    break;
                }
            }
            if (!resExcluido) {
                continue;
            }

            boolean resSubSeccion = !seleccionArticulo.isHayTipoSubSeccion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsSubSeccion()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getCodsubseccion().equals(item.getCodSubseccion())) {
                    resSubSeccion = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resSubSeccion && !resArticulo) {
                continue;
            }

            boolean resCategoria = !seleccionArticulo.isHayCategoria();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsCategoria()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getCodcategoria().equals(item.getCodCategoria())) {
                    resCategoria = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resCategoria && !resArticulo) {
                continue;
            }

            boolean resDescripcion = !seleccionArticulo.isHayDescripcion();
            for (ItemSeleccionArticuloBean item : seleccionArticulo.getItemsDescripcion()) {
                if (!linea.isPromocionAplicadaLineaCompleta() && !linea.isDescuentoAplicado()
                        && linea.getArticulo().getDesart().matches("(.*)" + item.getFiltroDescripcion() + "(.*)")) {
                    resDescripcion = true;
                    puntosAdicionales *= item.getPuntosAdicionales();
                    break;
                }
            }
            if (!resDescripcion && !resArticulo) {
                continue;
            }

            if (seleccionArticulo.isHayAlgunTipo() && resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
                acumulado *= puntosAdicionales;
                lineasAplicadas.add(linea);
            }
        }
        if (acumulado > 2) {
            acumulado = 2;
        }
        return acumulado;
    }

    public void recalcularFinalPagado(BigDecimal dtoPromoSubtotales, BigDecimal dtoPagos) {
        for (LineaTicket linea : lineas) {
            if (linea.getArticulo().getPmp() == null) {
                linea.recalcularFinalPagado(dtoPromoSubtotales, dtoPagos);
            } else {
                BigDecimal p = new BigDecimal("0.0");
                linea.recalcularFinalPagado(dtoPromoSubtotales, p);
            }
        }
        if (ticket != null) {
            recalcularTotalesBilletonMedioPago(ticket.getTotales());
        }
    }

    public Integer getIndexLinea(String codigo) {
        for (int i = 0; i < getNumLineas(); i++) {
            if (getLinea(i).getCodigoBarras().equals(codigo)) {
                return i;
            }
        }
        return null;
    }

    public Integer getIndexLineaCodigoArticulo(String codigo) {
        for (int i = 0; i < getNumLineas(); i++) {
            if (getLinea(i).getArticulo().getCodart().equals(codigo)) {
                return i;
            }
        }
        return null;
    }

    public void addLineaDescuentoFinal(DescuentoTicket descuento) {
        lineasDescuentos.add(descuento);
    }

    public void resetearPromocionesMultiples() {
        for (LineaTicket linea : lineas) {
            if (linea.isPromoMultipleAplicada()) {
                linea.setPromocionLinea(null);
            }
        }
    }

    public void resetearPromocion(Long idPromocion) {
        for (LineaTicket linea : lineas) {
            if (linea.isPromocionAplicada() && linea.getPromocionLinea().getIdPromocion().equals(idPromocion)) {
                linea.setPromocionLinea(null);
            }
        }
    }

    public void resetearLineasDescuento() {
        lineasDescuentos.clear();
    }

    public int getNumLineas() {
        return lineas.size();
    }

    public int getNumTotalArticulos() {
        int numeroArticulos = 0;
        for (LineaTicket linea : getLineas()) {
            numeroArticulos += linea.getCantidad();
        }
        return numeroArticulos;
    }

    public int getIndexUltimaLinea() {
        return getNumLineas() - 1;
    }

    public LineaTicket getLinea(int i) {
        return lineas.get(i);
    }

    public List<LineaTicket> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaTicket> lineas) {
        this.lineas = lineas;
    }

    public void setLinea(LineaTicket linea, int index) {
        lineas.set(index, linea);
    }

    public List<DescuentoTicket> getDescuentos() {
        return lineasDescuentos;
    }

    public void resetArticulosCanjeados() {
        for (LineaTicket lineaTicket : lineas) {
            lineaTicket.resetCanjeoPuntosCantidadAceptada();
        }
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public boolean isGarantiaExtendidaGratuitaRechazada() {
        return garantiaExtendidaGratuitaRechazada;
    }

    public void setGarantiaExtendidaGratuitaRechazada(boolean garantiaExtendidaGratuitaRechazada) {
        if (garantiaExtendidaGratuitaRechazada) {
            for (LineaTicket lineaTicket : lineas) {
                if (lineaTicket.isGarantiaExtendida() && lineaTicket.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))) {
                    lineaTicket.setDescuentoManualLinea(BigDecimal.ZERO);
                    lineaTicket.recalcularImportes();
                    lineaTicket.setGarantiaGratuita(false);
                }
            }
        }
        this.garantiaExtendidaGratuitaRechazada = garantiaExtendidaGratuitaRechazada;
    }

    public boolean soloLineasGarantiaExtendida() {
        boolean res = true;
        for (LineaTicket lineaTicket : lineas) {
            if (!(lineaTicket.isGarantiaExtendida() && lineaTicket.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS)))) {
                res = false;
            }
        }
        return res;
    }

    public void recalcularTotalesBilletonMedioPago(TotalesXML totales) {
        BigDecimal descuentoAcumuladoMedioPago = BigDecimal.ZERO;
        BigDecimal descuentoAcumuladoCabecera = BigDecimal.ZERO;
        BigDecimal porcentajeDescuentosMediosPago = BigDecimal.ZERO;
        BigDecimal porcentajeDescuentosCabecera = BigDecimal.ZERO;

        for (LineaTicket lineaTicket : lineas) {
            if (lineaTicket.getDescuentosMedioPagos() != null) {
                descuentoAcumuladoMedioPago = descuentoAcumuladoMedioPago.add(lineaTicket.getDescuentosMedioPagos());
            }
            if (lineaTicket.getDescuentosSubtotales() != null) {
                descuentoAcumuladoCabecera = descuentoAcumuladoCabecera.add(lineaTicket.getDescuentosSubtotales());
            }
            if (lineaTicket.getPorcentajeDescuentosMedioPagos() != null) {
                if (lineaTicket.getArticulo().getPmp() == null) {
                porcentajeDescuentosMediosPago = lineaTicket.getPorcentajeDescuentosMedioPagos(); // El porcentaje no se acumula;
                } else {
                    BigDecimal p = new BigDecimal("0.0");
                    // porcentajeDescuentosMediosPago = p;
                    lineaTicket.setPorcentajeDescuentosMedioPagos(p);
                }
            }
            if (lineaTicket.getPorcentajeDescuentosSubtotales() != null) {
                porcentajeDescuentosCabecera = lineaTicket.getPorcentajeDescuentosSubtotales(); // El porcentaje no se acumula
            }
        }

        totales.setTotalPromocionesMedioPago(descuentoAcumuladoMedioPago);
        totales.setTotalPromocionesCabecera(descuentoAcumuladoCabecera);
        totales.setDescuentoPromocionesMedioPago(porcentajeDescuentosMediosPago);
        totales.setDescuentoPromocionesCabecera(porcentajeDescuentosCabecera);
    }

    public void recalcularImpuestos(TotalesXML totales) {
        BigDecimal impuestoIce = BigDecimal.ZERO;
        Impuestos imp = null;
        Integer cantidad = 0;

        for (LineaTicket lineaTicket : lineas) {
            if (lineaTicket.getArticulo().getPmp() != null) {
                imp = impuestosServices.consultar(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo());
                impuestoIce = impuestoIce.add(imp.getTarifaEspecifica().multiply(new BigDecimal(lineaTicket.getCantidad())));
                totales.setImpuestosIce(impuestoIce);
                cantidad += lineaTicket.getCantidad();
                totales.setBaseImponibleIce(cantidad);
                totales.setTarifaIce(imp.getTarifaEspecifica());
                lineaTicket.setImpuestosIce(impuestoIce);
            }    
        }
    }


}
