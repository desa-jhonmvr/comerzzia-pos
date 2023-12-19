/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionDto;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromocionesFiltrosPagos;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoDto extends Promocion {

    private List<DetallePromocionDto> detalles;

    public PromocionTipoDto(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionDto>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        //Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionDto detallePromoDto = new DetallePromocionDto();
            detallePromoDto.setCodArticulo(detalle.getNodo("codArticulo", false).getValue());
            detallePromoDto.setDesArticulo(detalle.getNodo("desArticulo", false).getValue());
            detallePromoDto.setFechaInicio(detalle.getNodo("fechaInicio", false).getValue());
            detallePromoDto.setFechaFin(detalle.getNodo("fechaFin", false).getValue());
            detallePromoDto.setDescuento(detalle.getNodo("descuento", false).getValueAsDouble());
            detallePromoDto.setPrecioTarifaConImpuestos(detalle.getNodo("precioTarifaTotal", false).getValueAsBigDecimal());
            detallePromoDto.setPrecioTarifa(detalle.getNodo("precioTarifa", false).getValueAsBigDecimal());
            detallePromoDto.setPrecioVenta(detalle.getNodo("precioVenta", false).getValueAsBigDecimal());
            detallePromoDto.setPrecioTotal(detalle.getNodo("precioTotal", false).getValueAsBigDecimal());
            detallePromoDto.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());

            if (detallePromoDto.isFechaVigente()) {
                this.detalles.add(detallePromoDto);
                // añadimos el artículo a ArticulosEnPromocion
                ArticulosEnPromocion.getInstance().addArticuloEnPromocion(detallePromoDto.getCodArticulo(), this, detallePromoDto);
            }
        }
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        log.debug(this + " - Intentando aplicar promoción de descuento");
        if (tieneFiltroPagos()
                && ticket.getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(getIdPromocion())) {
            log.debug(this + ":: Promoción no aplicada por haber sido ya rechazada por el cliente anteriormente.");
            return;
        }
        boolean aceptada = true;
        if (tieneFiltroPagos()) {
            TicketPromocionesFiltrosPagos promoFiltroPagos = ticket.getTicketPromociones().getPromocionesFiltrosPagos();
            /*aceptada = promoFiltroPagos.isAceptada(getIdPromocion());
            if (!aceptada) {
                String msg = "¿Acepta la promoción " + getDescripcion() + "?";
                msg += "\nPague con:\n (Aplica restricciones) \n" + getFiltroPagosDescripcion();
                int altura = (18 * msg.split("\n").length);
                aceptada = JPrincipal.getInstance().crearVentanaConfirmacion(msg, altura);*/
                if(!promoFiltroPagos.isRechazada(getIdPromocion())){
                    promoFiltroPagos.addPromocion(this, aceptada);
                }
            }
        /*}
        if (!aceptada){
            return;
        }*/
        
        //G.S. comentado para que almacene en una lista y despues 
        //linea.setPromocionLinea(crearPromoLineaTicket(ticket, linea, detallePromocion, tarifa));
        PromocionLineaTicket promocion=crearPromoLineaTicket(ticket, linea, detallePromocion, tarifa);
        linea.getPromocionLineaList().add(promocion);

        // Aplicamos promoción
       // DetallePromocionDto detalle = (DetallePromocionDto) detallePromocion;
       // linea.setDescuento(new BigDecimal(detalle.getDescuento()));
       // linea.recalcularPrecios();
       // linea.recalcularAhorroPromocion();

        // Establecemos parametros para mostrar línea
        //linea.setPreciosPantalla(tarifa.getPrecioVenta(), tarifa.getPrecioTotal());
        //linea.setImpresionLineaDescuento(detalle.getTextoPromocion());
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        DetallePromocionDto detalle = (DetallePromocionDto) detallePromocion;
        return detalle.getPrecioTarifaConImpuestos().subtract(detalle.getPrecioTotal());
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
    
    /**
     * @author Gabriel Simbania
     * @description crea una nueva promocion
     * @param ticket
     * @param linea
     * @param detallePromocion
     * @param tarifa
     * @return 
     */
    public PromocionLineaTicket crearPromoLineaTicket(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa){
        // Instanciamos la promoción
        PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_UNITARIA);
        promocionLinea.setTextoPromocion(detallePromocion.getTextoPromocion());
        promocionLinea.setPrecioTarifa(tarifa.getPrecioVenta());
        promocionLinea.setPrecioTarifaTotal(tarifa.getPrecioTotal());
        promocionLinea.setCantidadPromocion(linea.getCantidad());
        promocionLinea.setDescuento(new BigDecimal(((DetallePromocionDto)detallePromocion).getDescuento()));
        
        return promocionLinea;
        
    }
}
