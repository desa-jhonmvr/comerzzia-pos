/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionNxM;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoNxM extends Promocion {

    private List<DetallePromocionNxM> detalles;

    public PromocionTipoNxM(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionNxM>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        //Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionNxM detallePromocionNxM = new DetallePromocionNxM();
            detallePromocionNxM.setFechaInicio(detalle.getNodo("fechaInicio", false).getValue());
            detallePromocionNxM.setFechaFin(detalle.getNodo("fechaFin", false).getValue());
            detallePromocionNxM.setDescuento(detalle.getNodo("descuento", false).getValueAsBigDecimal());
            detallePromocionNxM.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());
            detallePromocionNxM.setCantidadN(detalle.getNodo("N", true).getValueAsInteger());
            detallePromocionNxM.setCantidadM(detalle.getNodo("M", true).getValueAsInteger());

            //Obtenemos el nodo articulos para obtener los hijos
            XMLDocumentNode articulos = detalle.getNodo("articulos");
            for (XMLDocumentNode articulo : articulos.getHijos()) {
                Articulos articuloDetalle = new Articulos();
                articuloDetalle.setCodart(articulo.getNodo("codArticulo", false).getValue());
                articuloDetalle.setDesart(articulo.getNodo("desArticulo", false).getValue());
                detallePromocionNxM.getArticulos().add(articuloDetalle);
            }

            this.detalles.add(detallePromocionNxM);
        }
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("El cálculo de descuento a una línea no es soportado para este tipo de promoción.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        for (DetallePromocionNxM detalle : detalles) {
            List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
            if (detalle.isFechaVigente()) {
                int cant = detalle.getLineasAplicables(lineas, lineasAplicables);
                if (cant >= detalle.getCantidadN()) {
                    aplica(detalle, lineasAplicables, lineas);
                }
            }
        }
    }

    private void aplica(DetallePromocionNxM detalle, List<LineaTicket> lineasAplicables, LineasTicket lineas) {
        // ordenamos las líneas por precio descendente
        Collections.sort(lineasAplicables);

        // variables de indices y acumulativas
        int cantN = detalle.getCantidadN();
        int cantM = detalle.getCantidadM();
        int numCombos = 0; // indica la cantidad de combos NxM aplicados
        int cantLinea = 0; // indica la cantidad de artículos tratados de la línea
        int index = 1; // indica el índice dentro del combo, el combo se completa cuando index = N
        BigDecimal ahorroTotal = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo
        BigDecimal ahorro = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo

        // recorremos todas las líneas aplicables. No incrementamos el índice i porque
        // tendremos que pasar por cada línea tantas veces como cantidad de artículos tenga la línea
        for (int i = 0; i < lineasAplicables.size();) {
            LineaTicket linea = lineasAplicables.get(i);
            cantLinea++; // tratamos un nuevo artículo dentro de la línea
            PromocionLineaTicket promocionLinea = linea.getPromocionLinea();
            if (promocionLinea == null) { // este caso se da cuando tratamos la línea por primera vez
                promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                promocionLinea.setTextoPromocion(detalle.getTextoPromocion());
                promocionLinea.setPrecioTarifa(linea.getPrecio());
                promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotal());
                promocionLinea.setCantidadPromocion(0);
                linea.setPromocionLinea(promocionLinea);
            }
            if (index <= cantM) { // no aplico descuento, pero marco promoción
                promocionLinea.setImporteTotalPromocion(BigDecimal.ZERO);
                promocionLinea.setImportePromocion(BigDecimal.ZERO);
                promocionLinea.setImportesAhorro(BigDecimal.ZERO, BigDecimal.ZERO);
            }
            else if (index <= cantN) {
                promocionLinea.setCantidadPromocion(promocionLinea.getCantidadPromocion() + 1);
                promocionLinea.calculaAhorroDtoPorcentaje(detalle.getDescuento());
                ahorroTotal = ahorroTotal.add(promocionLinea.getImporteTotalAhorro());
                ahorro = ahorro.add(promocionLinea.getImporteAhorro());
            }
            // comprobamos si debemos seguir en la misma linea o pasar a la siguiente linea
            if (cantLinea == linea.getCantidad()) {
                cantLinea = 0;
                i++;
            }

            index++;
            // combrobamos si hemos completado un combo
            if (index > cantN) {
                lineas.addLineaDescuentoFinal(new DescuentoTicket(detalle.getTextoPromocion(), ahorroTotal, ahorro));
                ahorroTotal = BigDecimal.ZERO; // reiniciamos el ahorro para el nuevo combo
                ahorro = BigDecimal.ZERO; // reiniciamos el ahorro para el nuevo combo
                index = 1; // reiniciamos el índice para el nuevo combo
                numCombos++; // incrementamos el número de combos creados
                // comprobamos si ya hemos completado todos los combos aplicables
                if (numCombos == lineasAplicables.size() / cantN) {
                    break; // ya hemos aplicado todos los combos posibles, salimos del bucle
                }
            }

        }

    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
}
