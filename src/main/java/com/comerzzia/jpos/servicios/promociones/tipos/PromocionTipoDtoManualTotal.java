/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.totales.PromocionTotal;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PromocionTipoDtoManualTotal extends PromocionTotal {

    private static boolean activa = false;
    private static BigDecimal descuento;

    public PromocionTipoDtoManualTotal(PromocionBean promocion) throws PromocionException {
        super(null);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        if (!isActiva()) {
            return;
        }
        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_MANUAL);
        promocionTicket.setTextoPromocion(getTextoDescuento());
        promocionTicket.setCantidadPromocion(0);

        // aplicamos el porcentaje de descuento multiplicado por el número de veces aplicable
        BigDecimal ahorro = Numero.porcentaje(descuento, totales.getTotalAPagar().subtract(totales.getTotalGarantiaExtendida()));
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket(getTextoDescuento(), ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
//        promocionTicket.setImporteTotaltarifaOrigen(totales.getImporteTotalTarifaOrigen());
//        promocionTicket.setImporteTotalAPagar(ahorro);
        totales.addPromocionATotal(promocionTicket);
    }

    private String getTextoDescuento() {
        return "Descuento " + descuento.intValue() + "%";
    }

    public static boolean isActiva() {
        return activa;
    }

    public static void activar(Integer descuento) {
        PromocionTipoDtoManualTotal.activa = descuento > 0;
        PromocionTipoDtoManualTotal.descuento = new BigDecimal(descuento);
    }
    
        public static void activarPromoDiaSocio(BigDecimal descuento) {
//        PromocionTipoDtoManualTotal.activa = descuento > 0;
//        PromocionTipoDtoManualTotal.descuento = new BigDecimal(descuento);
        PromocionTipoDtoManualTotal.activa = descuento.compareTo(BigDecimal.ZERO) > 0;
        PromocionTipoDtoManualTotal.descuento = descuento;
    }

    public static void desActivar() {
        PromocionTipoDtoManualTotal.activa = false;
    }

    public static BigDecimal getDescuento() {
        return descuento;
    }

}
