/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.puntos;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCanjeablePuntos;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;

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
public class PromocionTipoPuntosCanjeo extends Promocion {

    BigDecimal montoMinimo;
    List<ComboArticuloCanjeablePuntos> articulosCanjeables;
    List<LineaTicket> lineasAplicables;

    public PromocionTipoPuntosCanjeo(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        montoMinimo = detalleXML.getNodo("montoMinimo").getValueAsBigDecimal();
        
        articulosCanjeables = new ArrayList<ComboArticuloCanjeablePuntos>();
        List<XMLDocumentNode> articulosNodo = detalleXML.getNodo("articulos").getHijos();
        for (XMLDocumentNode articuloNodo : articulosNodo) {
            ComboArticuloCanjeablePuntos combo = new ComboArticuloCanjeablePuntos();
            combo.setCodigo(articuloNodo.getNodo("codArticulo").getValue());
            combo.setDescripcion(articuloNodo.getNodo("desArticulo").getValue());
            combo.setDescuento(articuloNodo.getNodo("descuento").getValueAsBigDecimal());
            combo.setCantidad(articuloNodo.getNodo("puntosCanjear").getValueAsInteger());
            combo.setLimite(articuloNodo.getNodo("limiteCantidad").getValueAsInteger());
            combo.setPromocion(this);
            articulosCanjeables.add(combo);
        }
        
        seleccion = new SeleccionArticuloBean(detalleXML.getNodo("impresion").getNodo("parametros"));
        lineasAplicables = new ArrayList<LineaTicket>();
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("El cálculo de descuento a una línea no es soportado para este tipo de promoción.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        for (LineaTicket linea : lineas.getLineas()) {
            int cantidadACanjear = linea.getCanjeoPuntosCantidadAceptada();
            if (cantidadACanjear == 0){
                continue;
            }
            if (linea.isPromocionAplicada()){
                continue; // se supone que no debe entrar por aquí nunca
            }
            // si la cantidad a canjear es mayor a cero, hay que aplicarle la promoción
            // buscamos el combo que aplica para esta línea
            for (ComboArticuloCanjeablePuntos combo : articulosCanjeables) {
                if (linea.getArticulo().getCodart().equals(combo.getCodigo())){
                    PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                    promocionLinea.setTextoPromocion(getDescripcionImpresion());
                    promocionLinea.setPrecioTarifa(linea.getPrecioTarifaOrigen());
                    promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotalTarifaOrigen());
                    promocionLinea.setCantidadPromocion(0);
                    linea.setPromocionLinea(promocionLinea);

                    promocionLinea.setCantidadPromocion(cantidadACanjear);
                    promocionLinea.calculaAhorroDtoPorcentaje(combo.getDescuento());

                    DescuentoTicket descuentoTicket = new DescuentoTicket();
                    descuentoTicket.setDescripcion(getDescripcionImpresion());
                    descuentoTicket.setDescuento(promocionLinea.getImporteAhorro());
                    descuentoTicket.setDescuentoTotal(promocionLinea.getImporteTotalAhorro());
                    lineas.addLineaDescuentoFinal(descuentoTicket);
                    break;
                }
            }
        }
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }

    @Override
    public boolean isAplicableACliente(Cliente cliente) {
        if (cliente.isClienteGenerico()){
            return false;
        }
        return super.isAplicableACliente(cliente);
    }



    public ComboArticuloCanjeablePuntos getLineaCandidata(TicketS ticket){
        LineasTicket lineas = ticket.getLineas();
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        BigDecimal montoAplicable = seleccion.getImporteAplicable(lineas, true, new ArrayList<LineaTicket>());
        if (montoAplicable == null){
            montoAplicable = ticket.getTotales().getTotalAPagar();
        }
        if (Numero.isMenor(montoAplicable, montoMinimo)){
            log.debug(toString() + " No se puede aplicar promoción por restricción de monto mínimo: " + montoMinimo + " > montoAplicable: " + montoAplicable);
            return null;
        }
        
        Integer cantidadCanjeada = 0;
        for (ComboArticuloCanjeablePuntos combo : articulosCanjeables) {
            // si el artículo ya lo ha rechazado el cliente para esta factura, no lo tratamos
            if (ticket.getPuntosTicket().isArticuloRechazado(combo.getCodigo())){
                continue;
            }

            // buscamos si este artículo está en el ticket
            int cantidad = lineas.getContains(true, combo.getCodigo(), lineasAplicables);
            if (cantidad == 0){ // si no está, no lo tratamos
                continue;
            }

            for (LineaTicket lineaTicket : lineasAplicables) {
                cantidadCanjeada += lineaTicket.getCanjeoPuntosCantidadAceptada();
            }
            if (cantidadCanjeada.equals(combo.getLimite())){
                log.debug(toString() + " Cantidad límite de canjeo alcanzada: " + combo.getLimite());
                continue;
            }
            for (LineaTicket lineaTicket : lineasAplicables) {
                // buscamos una línea de ticket a la que aún le resten artículos que se puedan canjear
                if (!lineaTicket.isCanjeoPuntosCantidadCompleta()){
                    combo.setLinea(lineaTicket);
                    return combo;
                }
            }
        }
        return null;
    }
    
    public boolean isAplicableMontoMinimo(LineasTicket lineas) {
        BigDecimal montoAplicable = seleccion.getImporteAplicable(lineas, true, new ArrayList<LineaTicket>());
        if (montoAplicable == null) { // el tipo de selección es todos
            montoAplicable = lineas.getTicket().getTotales().getTotalPagado();
        }
        if (Numero.isMayorOrIgual(montoAplicable, montoMinimo)) {
            return true;
        }
        log.debug(this + " Promoción no aplicada porque no cumple monto mínimo. MontoAplicable: "+montoAplicable+" ,MontoMinimo: "+montoMinimo);
        return false;

    }    
    
}

