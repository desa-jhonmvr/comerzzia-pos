/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticuloPromociones;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import com.comerzzia.jpos.servicios.promociones.articulos.RangosPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.ServicioPromocionArticulo;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.math.RoundingMode;

/**
 *
 * @author AMS
 */
public class PromocionTipoDiaSocio extends Promocion {

    private List<ArticuloPromociones> articulos;

    public PromocionTipoDiaSocio(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        rangos = new RangosPromocion(detalleXML);

        List<XMLDocumentNode> articulosXML = detalleXML.getNodo("articulos", true).getHijos();
        if (articulosXML != null) {
            articulos = new ArrayList<ArticuloPromociones>();
            for (XMLDocumentNode articulo : articulosXML) {

                String codArticulo = articulo.getNodo("codArticulo").getValue();
                String desArticulo = articulo.getNodo("desArticulo").getValue();
                String desMarca = articulo.getNodo("desMarca").getValue();
                BigDecimal descuento = new BigDecimal (articulo.getNodo("descuento").getValue());
                Integer limiteProducto = articulo.getNodo("limiteProducto", true).getValueAsInteger();
                Integer limiteLocalProducto = 0;
                List<XMLDocumentNode> limitesXML = articulo.getNodo("limitesLocalProducto", true).getHijos();
                for(XMLDocumentNode lim:limitesXML){
                    String tienda = lim.getNodo("codTienda").getValue();
                    if(tienda.equals(Sesion.getTienda().getCodalmSRI())){
                        limiteLocalProducto = lim.getNodo("limite").getValueAsInteger();
                    }
                }
                log.debug("El límite del producto del local del artículo "+codArticulo+" es "+limiteLocalProducto);
                
                Boolean restringido = articulo.getNodo("restriccion").getValueAsBoolean();

                ArticuloPromociones art = new ArticuloPromociones();
                art.setCodArticulo(codArticulo);
                art.setDesArticulo(desArticulo);
                art.setDesMarca(desMarca);
                art.setDescuento(descuento);
                art.setLimiteProducto(limiteProducto);
                art.setLimiteLocalProducto(limiteLocalProducto);
                art.setCheck(restringido);
                articulos.add(art);

                if (art.isCheck()){
                    ArticulosEnPromocion.getInstance().addArticuloDiaSocio(art.getCodArticulo(), this);
                }
            }
        }
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
    }

    
    public void aplicaLineaDiaSocio(LineaTicket linea, TicketS ticket, int cantAntigua) throws TicketNuevaLineaException {
        try{
            for (ArticuloPromociones articulo : articulos) {
                    if (!articulo.getCodArticulo().equals(linea.getArticulo().getCodart())){
                        log.debug("El artículo "+linea.getArticulo().getCodart()+" no coincide con el artículo "+articulo.getCodArticulo());
                        continue;
                    }
                int cantLinea = linea.getCantidad();
                int cantArtMaxima = articulo.getLimiteProducto();
                int cantArtComprada = ticket.getLineas().getContains(false, articulo.getCodArticulo(), null) - cantAntigua;
                int cantTotalMaxima = ticket.getTicketPromociones().getDiaSocioCantidadAplicable();
                int cantTotalComprada = ticket.getLineas().getContainsSinGarExt(false, null) - cantAntigua;
                int cantArtMaximaLocal = articulo.getLimiteLocalProducto();

                log.debug(toString() + " Cantidad línea: " + cantLinea + " Cantidad máxima del artículo: " + cantArtMaxima + " Cantidad máxima del artículo en local: " + cantArtMaximaLocal + " Cantidad total comprada del artículo: " + cantArtComprada);
                log.debug(toString() + " Cantidad máxima total: " + cantTotalMaxima + " Cantidad comprada total: " + cantTotalComprada);

                if ((cantTotalComprada - cantLinea) == cantTotalMaxima){
                    throw new TicketNuevaLineaException("No puede acceder a más productos del día del socio.");
                }
                if (cantTotalComprada > cantTotalMaxima){
                    throw new TicketNuevaLineaException("La cantidad indicada para el artículo excede el máximo de productos del día del socio permitidos.");
                }
                if ((cantArtComprada - cantLinea) > cantArtMaxima){
                    throw new TicketNuevaLineaException("No puede acceder a más productos del día del socio iguales a este.");
                }
                if (cantArtComprada > cantArtMaxima){
                    throw new TicketNuevaLineaException("La cantidad indicada para el artículo excede el máximo para este producto del día del socio.");
                }
                int cantidadLocalComprada = ServicioPromocionArticulo.consultarPromocionDiaSocio(getIdPromocion(), articulo.getCodArticulo());
                log.debug("Se han comprado en el local "+cantidadLocalComprada+ " unidades del artículo "+articulo.getCodArticulo());
                if(cantidadLocalComprada == cantArtMaximaLocal){
                    throw new TicketNuevaLineaException("Producto del día del socio agotado");
                }
                if(cantidadLocalComprada + cantArtComprada > cantArtMaximaLocal){
                    throw new TicketNuevaLineaException("Producto del día del socio agotado, como máximo se pueden comprar: "+(cantArtMaximaLocal - cantidadLocalComprada) + " unidades");
                }
                if (!ticket.getTicketPromociones().preguntarAplicacionDiaSocio()){
                    log.debug(toString() + " Promoción del día del socio no aplicada porque no se ha pulsado el botón aceptar en la pregunta");
                    return;
                }

                PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_DIA_SOCIO);
                promocionLinea.setTextoPromocion(getDescripcionImpresion());
                promocionLinea.setPrecioTarifa(linea.getPrecioTarifaOrigen());
                promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotalTarifaOrigen());
                promocionLinea.setCantidadPromocion(cantLinea);
                linea.setPromocionLinea(promocionLinea);
                linea.setDescuento(articulo.getDescuento().setScale(4, RoundingMode.UP));
                linea.recalcularPrecios();
                linea.recalcularAhorroPromocion();

                // Establecemos parametros para mostrar línea
                linea.setPreciosPantalla(promocionLinea.getPrecioTarifa(), promocionLinea.getPrecioTarifaTotal());
                linea.setImpresionLineaDescuento(getDescripcionImpresion());

                log.debug(toString() + " -  Promoción aplicada.");
                return;
            }
            if(linea.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))){
                return;
            }
            throw new TicketNuevaLineaException("El producto no pertenece al día del socio");
        } catch (PromocionArticuloException e) {
            throw new TicketNuevaLineaException("Error consultando artículos comprados de la promoción",e);
        }

    
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Promoción no aplicable a línea unitaria.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }

    public List<ArticuloPromociones> getArticulos() {
        return articulos;
    }

    @Override
    public boolean isReaplicable(TicketS ticket) {
        if (!tieneFiltroPagosTarjetaSukasa) {
            boolean aplicadaAnteriormente = ticket.getTicketPromociones().isPromocionAplicadaAnteriormente(getIdPromocion());
            if (aplicadaAnteriormente) {
                log.debug("isReaplicable() -  No se aplica promoción DÍA DEL SOCIO porque ya fue aplicada para este cliente.");
            }
            return !aplicadaAnteriormente;
        }
        // si tiene filtro por tarjeta sukasa, tendremos que hacer la comprobación por número de crédito. 
        // esto lo comprobaremos después, cuando se incluyan los pagos
        ticket.getTicketPromociones().getPromocionesFiltrosPagos().addPromoAplicacionUnicaCredito(this);
        return true;
    }

    public Integer getCantidadAplicable(BigDecimal montoFacturaOrigen) {
        return rangos.getRangoAplicableAsInt(montoFacturaOrigen);
    }

    @Override
    public String getMensajeNoAplicablePagos() {
        return "Forma de pago incorrecta para día del socio.";
    }   
    
}
