/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionPrecio;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoPrecio extends Promocion {

    private List<DetallePromocionPrecio> detalles;

    public PromocionTipoPrecio(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionPrecio>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        //Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionPrecio detallePromoPrecio = new DetallePromocionPrecio();
            detallePromoPrecio.setCodArticulo(detalle.getNodo("codArticulo", false).getValue());
            detallePromoPrecio.setDesArticulo(detalle.getNodo("desArticulo", false).getValue());
            detallePromoPrecio.setFechaInicio(detalle.getNodo("fechaInicio", false).getValue());
            detallePromoPrecio.setFechaFin(detalle.getNodo("fechaFin", false).getValue());
            detallePromoPrecio.setPrecioTarifa(detalle.getNodo("precioTarifa", false).getValueAsBigDecimal());
            detallePromoPrecio.setPrecioTarifaConImpuestos(detalle.getNodo("precioTarifaTotal", false).getValueAsBigDecimal());
            detallePromoPrecio.setPrecioVenta(detalle.getNodo("precioVenta", false).getValueAsBigDecimal());
            detallePromoPrecio.setPrecioTotal(detalle.getNodo("precioTotal", false).getValueAsBigDecimal());
            detallePromoPrecio.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());

            if (detallePromoPrecio.isFechaVigente()) {
                // añadimos el artículo a ArticulosEnPromocion
                ArticulosEnPromocion.getInstance().addArticuloEnPromocion(detallePromoPrecio.getCodArticulo(), this, detallePromoPrecio);
                this.detalles.add(detallePromoPrecio);
            }
        }
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        // Instanciamos promoción
        PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_UNITARIA);
        promocionLinea.setTextoPromocion(detallePromocion.getTextoPromocion());
        promocionLinea.setPrecioTarifa(tarifa.getPrecioVenta());
        promocionLinea.setPrecioTarifaTotal(tarifa.getPrecioTotal());
        promocionLinea.setCantidadPromocion(linea.getCantidad());
        linea.setPromocionLinea(promocionLinea);

        // Aplicamos promoción
        DetallePromocionPrecio detalle = (DetallePromocionPrecio) detallePromocion;
        linea.setPrecio(detalle.getPrecioVenta());
        linea.setPrecioTotal(detalle.getPrecioTotal());
        linea.recalcularImportes();
        linea.recalcularAhorroPromocion();
        linea.setDescripcionAdicional("PROMO");
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        DetallePromocionPrecio detalle = (DetallePromocionPrecio) detallePromocion;
        return detalle.getPrecioTarifaConImpuestos().subtract(detalle.getPrecioTotal());
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
}
