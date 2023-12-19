/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.totales.PromocionTotal;
import com.comerzzia.jpos.servicios.promociones.totales.TotalesEnPromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PromocionTipoDtoPorImporte extends PromocionTotal {

    private BigDecimal importeMinimo;
    private BigDecimal descuento;
    private String textoDetallePromocion;

    public PromocionTipoDtoPorImporte(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        //Obtenemos el nodo cabecera
        XMLDocumentNode cabecera = xml.getNodo("cabecera");

        //Obtenemos los valores para el importe mínimo y para el descuento
        importeMinimo = new BigDecimal(cabecera.getNodo("importeMinimo", false).getValueAsDouble());
        descuento = new BigDecimal(cabecera.getNodo("descuento", false).getValueAsDouble());
        textoDetallePromocion = cabecera.getNodo("textoPromocion", false).getValue();
        setAplicaALineasConPromocion(cabecera.getNodo("aplicarAArticulosPromocion", false).getValueAsBoolean());

        TotalesEnPromocion.getInstance().addTotalEnPromocion(importeMinimo, this);
    }


    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_SUBTOTAL);
        promocionTicket.setTextoPromocion(getTextoDetallePromocion());
        promocionTicket.setCantidadPromocion(0);

        // aplicamos el porcentaje de descuento multiplicado por el número de veces aplicable
        BigDecimal ahorro = Numero.porcentajeR(importeMinimo, descuento);
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket(getTextoDetallePromocion(), ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
        totales.addPromocionATotal(promocionTicket);
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getImporteMinimo() {
        return importeMinimo;
    }

    public String getTextoDetallePromocion() {
        if (textoDetallePromocion == null || textoDetallePromocion.isEmpty()) {
            return "Descuento";
        }
        return textoDetallePromocion;
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
