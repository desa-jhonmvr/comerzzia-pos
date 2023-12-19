/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromocionesFiltrosPagos;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author SMLM
 */
public class PromocionTipoDescuentoCombinado extends Promocion {

    private String textoPromocion;
    private Double descuento;

    public PromocionTipoDescuentoCombinado(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        textoPromocion = detalleXML.getNodo("textoPromocion").getValue();
        descuento = detalleXML.getNodo("descuento").getValueAsDouble();

        seleccion = new SeleccionArticuloBean(detalleXML.getNodo("impresion").getNodo("parametros"));
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Promoción no aplicable a línea unitaria.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        log.debug("Intentando aplicar promoción descuento combinado... " + toString());
        TicketS ticket = lineas.getTicket();
        if (ticket == null) {
            log.warn("ATENCIÓN: El ticket asociado a las líneas es NULL. No se aplicó promoción Descuento Combinado.");
            return;
        }
        if (tieneFiltroPagos()
                && ticket.getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(getIdPromocion())) {
            log.debug(this + ":: Promoción no aplicada por haber sido ya rechazada por el cliente anteriormente.");
            return;
        }

        List<LineaTicket> lineasAplicables = lineas.getLineasAplicables(true, seleccion);
        if (!lineasAplicables.isEmpty()) {
            // si podemos aplicar la promoción, antes comprobamos medios de pago
            //boolean aceptada = true;
            if (tieneFiltroPagos()) {
                TicketPromocionesFiltrosPagos promoFiltroPagos = ticket.getTicketPromociones().getPromocionesFiltrosPagos();
                if (promoFiltroPagos.isAceptada(getIdPromocion())) {
                    promoFiltroPagos.addPromocion(this, true);
                }
                /*if (!aceptada) {
                     String msg = "¿Acepta la promoción " + getDescripcion() + "?";
                     msg += "\nPague con:\n (Aplica restricciones) \n" + getFiltroPagosDescripcion();
                     int altura = (18 * msg.split("\n").length);
                     aceptada = JPrincipal.getInstance().crearVentanaConfirmacion(msg, altura);
                     promoFiltroPagos.addPromocion(this, aceptada);
                 }
                 if(!aceptada){
                     return;
                 }*/
            }
        }

        for (LineaTicket linea : lineasAplicables) {
            PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_UNITARIA);
            promocionLinea.setTextoPromocion(textoPromocion);
            //BigDecimal precioLinea = new BigDecimal(linea.getPrecio().toString());
            //BigDecimal precioTotalLinea = new BigDecimal(linea.getPrecioTotal().toString());
            promocionLinea.setPrecioTarifa(linea.getPrecio());
            promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotal());
            promocionLinea.setCantidadPromocion(linea.getCantidad());
            promocionLinea.setDescuento(new BigDecimal(descuento));
            promocionLinea.setTextoPromocion(textoPromocion);
            promocionLinea.setTieneFiltroPagosTarjetaSukasa(isTieneFiltroPagosTarjetaSukasa());

            linea.getPromocionLineaList().add(promocionLinea);
            //G.S. Comentado para almacenar en una lista y despues escoger la mejor promocion
            //linea.setPromocionLinea(promocionLinea);
            //linea.setDescuento(new BigDecimal(descuento));
            //linea.recalcularPrecios();
            //linea.recalcularAhorroPromocion();

            // Establecemos parametros para mostrar línea
            //linea.setPreciosPantalla(precioLinea, precioTotalLinea);
            //linea.setImpresionLineaDescuento(textoPromocion);
        }
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

}
