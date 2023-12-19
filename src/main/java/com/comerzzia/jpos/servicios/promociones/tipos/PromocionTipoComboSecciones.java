/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCantidadBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.IndicePromocionLinea;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;

import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionComboSeccion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amos
 */
public class PromocionTipoComboSecciones extends Promocion {

    private List<DetallePromocionComboSeccion> detalles;

    public PromocionTipoComboSecciones(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionComboSeccion>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        //Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionComboSeccion detallePromocionComboSeccion = new DetallePromocionComboSeccion();
            detallePromocionComboSeccion.setFechaInicio(new Fecha(detalle.getNodo("fechaInicio", false).getValue()));
            detallePromocionComboSeccion.setFechaFin(new Fecha(detalle.getNodo("fechaFin", false).getValue()));
            detallePromocionComboSeccion.setDescuento(detalle.getNodo("descuento", false).getValueAsBigDecimal());
            detallePromocionComboSeccion.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());

            //Obtenemos el nodo articulos para obtener los hijos
            XMLDocumentNode secciones = detalle.getNodo("secciones");
            for (XMLDocumentNode seccion : secciones.getHijos()) {
                ComboArticuloCantidadBean comboSeccion = new ComboArticuloCantidadBean();
                comboSeccion.setCodigo(seccion.getNodo("codSeccion", false).getValue());
                comboSeccion.setDescripcion(seccion.getNodo("desSeccion", false).getValue());
                comboSeccion.setCantidad(seccion.getNodo("cantidad", false).getValueAsInteger());

                detallePromocionComboSeccion.getSecciones().add(comboSeccion);
            }

            this.detalles.add(detallePromocionComboSeccion);
        }
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Promoción no aplicable a línea unitaria.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        for (DetallePromocionComboSeccion detalle : detalles) {
            Map<String, IndicePromocionLinea> lineasAplicables;
            if (detalle.isFechaVigente()) {
                lineasAplicables = detalle.getLineasAplicables(lineas);
                while (lineasAplicables != null) {
                    if (lineasAplicables != null && !lineasAplicables.isEmpty()) {
                        aplica(detalle, lineasAplicables, lineas);
                    }
                    lineasAplicables.clear();
                    lineasAplicables = detalle.getLineasAplicables(lineas);
                }
            }
        }
    }

    private void aplica(DetallePromocionComboSeccion detalle, Map<String, IndicePromocionLinea> lineasAplicables, LineasTicket lineas) {
        // ordenamos las líneas por precio descendente
        for (IndicePromocionLinea lineaPromocion : lineasAplicables.values()) {
            Collections.sort(lineaPromocion.getLineas());
        }

        // variables de indices y acumulativas
        BigDecimal ahorroTotal = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo
        BigDecimal ahorro = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo

        for (ComboArticuloCantidadBean comboSeccion : detalle.getSecciones()) {
            int cantPorAplicar = comboSeccion.getCantidad();
            while (cantPorAplicar > 0) {
                // Obtenemos el conjunto de lineas aplicables para la categoria
                IndicePromocionLinea promocionLinea = lineasAplicables.get(comboSeccion.getCodigo());
                // obtenemos la línea a la que podemos aplicar
                LineaTicket linea = promocionLinea.getLineaIndice();

                // aplicamos promoción
                PromocionLineaTicket promocion = linea.getPromocionLinea();
                if (promocion == null) { // este caso se da cuando tratamos la línea por primera vez
                    promocion = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                    promocion.setTextoPromocion(detalle.getTextoPromocion());
                    promocion.setPrecioTarifa(linea.getPrecio());
                    promocion.setPrecioTarifaTotal(linea.getPrecioTotal());
                    promocion.setCantidadPromocion(0);
                    linea.setPromocionLinea(promocion);
                }
                promocion.setCantidadPromocion(promocion.getCantidadPromocion() + 1);
                promocion.calculaAhorroDtoPorcentaje(detalle.getDescuento());
                ahorroTotal = ahorroTotal.add(promocion.getImporteTotalAhorro());
                ahorro = ahorro.add(promocion.getImporteAhorro());

                // decrementamos la cantidad por aplicar para este combo
                cantPorAplicar--;

                // incrementamos índice para los artículos aplicables de esa categoría
                promocionLinea.incrementaIndice(); // TODO: PROMO - SUKASA no se tienen en cuenta líneas con más de una cantidad
            }
        }
        lineas.addLineaDescuentoFinal(new DescuentoTicket(detalle.getTextoPromocion(), ahorroTotal, ahorro));
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
}
