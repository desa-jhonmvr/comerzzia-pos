/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloDtoBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionNxMSimple;
import com.comerzzia.jpos.servicios.tickets.TicketS;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoNxMSimple extends Promocion {

    private List<DetallePromocionNxMSimple> detalles;

    public PromocionTipoNxMSimple(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionNxMSimple>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        // Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionNxMSimple detallePromocionNxM = new DetallePromocionNxMSimple();
            detallePromocionNxM.setFechaInicio(detalle.getNodo("fechaInicio", false).getValue());
            detallePromocionNxM.setFechaFin(detalle.getNodo("fechaFin", false).getValue());
            detallePromocionNxM.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());

            // Obtenemos el nodo articulos para obtener los hijos
            XMLDocumentNode articulosN = detalle.getNodo("articulosN");
            for (XMLDocumentNode articulo : articulosN.getHijos()) {
                Articulos artN = new Articulos();
                artN.setCodart(articulo.getNodo("codArticulo", false).getValue());
                artN.setDesart(articulo.getNodo("desArticulo", false).getValue());
                detallePromocionNxM.addArticuloN(artN);
            }
            XMLDocumentNode articulosM = detalle.getNodo("articulosM");
            for (XMLDocumentNode articulo : articulosM.getHijos()) {
                ComboArticuloDtoBean combo = new ComboArticuloDtoBean();
                combo.setCodigo(articulo.getNodo("codArticulo", false).getValue());
                combo.setDescripcion(articulo.getNodo("desArticulo", false).getValue());
                combo.setDescuento(articulo.getNodo("descuento", false).getValueAsBigDecimal());
                detallePromocionNxM.addArticuloM(combo);
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
        for (DetallePromocionNxMSimple detalle : detalles) {
            if (detalle.isFechaVigente()) {
                List<LineaTicket> lineasAplicables = detalle.getLineasAplicables(lineas);
                while (lineasAplicables != null) {
                    aplica(detalle, lineasAplicables, lineas);
                    lineasAplicables = detalle.getLineasAplicables(lineas); // intentamos volver a aplicar la promoción
                }
            }
        }
    }

    private void aplica(DetallePromocionNxMSimple detalle, List<LineaTicket> lineasAplicables, LineasTicket lineas) {
        BigDecimal ahorro = BigDecimal.ZERO; // indica el ahorro acumulado en aplicar la promoción
        BigDecimal ahorroTotal = BigDecimal.ZERO;

        // recorremos todos los artículos N
        for (Articulos articuloN : detalle.getArticulosN()) {
            // buscamos en la lista de líneas aplicables alguna con el artículo
            Iterator<LineaTicket> it = lineasAplicables.iterator();
            while (it.hasNext()) {
                LineaTicket linea = it.next(); // TODO: PROMO - tener en cuenta líneas con cantidad > 1
                // tendrá que ser una líneas sin promoción aplicada y con el mismo artículo
                if (!linea.isPromocionAplicada() && linea.getArticulo().getCodart().equals(articuloN.getCodart())) {
                    PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                    promocionLinea.setTextoPromocion(detalle.getTextoPromocion());
                    promocionLinea.setPrecioTarifa(linea.getPrecio());
                    promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotal());
                    promocionLinea.setCantidadPromocion(0);
                    promocionLinea.setImporteTotalPromocion(BigDecimal.ZERO);
                    promocionLinea.setImportePromocion(BigDecimal.ZERO);
                    promocionLinea.setImportesAhorro(BigDecimal.ZERO, BigDecimal.ZERO);
                    linea.setPromocionLinea(promocionLinea);
                    it.remove();
                    break;
                }
            }
        }

        // recorremos todos los artículos M
        for (ComboArticuloDtoBean articuloM : detalle.getArticulosM()) {
            // buscamos en la lista de líneas aplicables alguna con el artículo
            Iterator<LineaTicket> it = lineasAplicables.iterator();
            while (it.hasNext()) {
                LineaTicket linea = it.next(); // TODO: PROMO - tener en cuenta líneas con cantidad > 1
                // tendrá que ser una líneas sin promoción aplicada y con el mismo artículo
                if (!linea.isPromocionAplicada() && linea.getArticulo().getCodart().equals(articuloM.getCodigo())) {
                    PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                    promocionLinea.setTextoPromocion(detalle.getTextoPromocion());
                    promocionLinea.setPrecioTarifa(linea.getPrecio());
                    promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotal());
                    promocionLinea.setCantidadPromocion(1);
                    promocionLinea.calculaAhorroDtoPorcentaje(articuloM.getDescuento());
                    ahorro = ahorro.add(promocionLinea.getImporteAhorro());
                    ahorroTotal = ahorroTotal.add(promocionLinea.getImporteTotalAhorro());
                    linea.setPromocionLinea(promocionLinea);
                    it.remove();
                    break;
                }
            }
        }
        lineas.addLineaDescuentoFinal(new DescuentoTicket(detalle.getTextoPromocion(), ahorroTotal, ahorro));

    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
}
