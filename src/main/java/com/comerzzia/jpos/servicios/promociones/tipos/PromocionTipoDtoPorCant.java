/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboCantidadDtoBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;

import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionDtoPorCant;
import com.comerzzia.jpos.servicios.tickets.TicketS;
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
public class PromocionTipoDtoPorCant extends Promocion {

    private List<DetallePromocionDtoPorCant> detalles;

    public PromocionTipoDtoPorCant(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionDtoPorCant>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        // Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionDtoPorCant detallePromocionDto = new DetallePromocionDtoPorCant();
            detallePromocionDto.setCodArticulo(detalle.getNodo("codArticulo", false).getValue());
            detallePromocionDto.setDesArticulo(detalle.getNodo("desArticulo", false).getValue());
            detallePromocionDto.setFechaInicio(detalle.getNodo("fechaInicio", false).getValue());
            detallePromocionDto.setFechaFin(detalle.getNodo("fechaFin", false).getValue());
            detallePromocionDto.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());
            detallePromocionDto.setPrecioTarifa(detalle.getNodo("precioTarifa", true).getValueAsBigDecimal());
            detallePromocionDto.setPrecioTarifaConImpuestos(detalle.getNodo("precioTarifaTotal", true).getValueAsBigDecimal());

            // Obtenemos el nodo articulos para obtener los hijos
            XMLDocumentNode descuentos = detalle.getNodo("descuentos");
            List<ComboCantidadDtoBean> listaCombos = new ArrayList<ComboCantidadDtoBean>();
            for (XMLDocumentNode descuento : descuentos.getHijos()) {
                ComboCantidadDtoBean descuentoDetalle = new ComboCantidadDtoBean();
                descuentoDetalle.setCantidad(descuento.getNodo("cantidad", false).getValueAsInteger());
                descuentoDetalle.setDescuento(descuento.getNodo("porcentaje", false).getValueAsBigDecimal());
                listaCombos.add(descuentoDetalle);
            }
            detallePromocionDto.setCombos(listaCombos);

            this.detalles.add(detallePromocionDto);
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
        for (DetallePromocionDtoPorCant detalle : detalles) {
            List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
            if (detalle.isFechaVigente() && detalle.getCantComboMayor() > 0) {
                // obtenemos todas las líneas sin promoción que tienen el artículo del detalle
                int cant = lineas.getContains(true, detalle.getCodArticulo(), lineasAplicables);
                // comprobamos si tenemos una cantidad >= que la menor cantidad registrada en los combos
                if (cant >= detalle.getCantComboMenor()) {
                    aplica(detalle, lineasAplicables, lineas, cant);
                }
            }
        }
    }

    private void aplica(DetallePromocionDtoPorCant detalle, List<LineaTicket> lineasAplicables, LineasTicket lineas, Integer cantidadAplicable) {
        // variables de indices y acumulativas
        int cantLinea = 0; // indica la cantidad de artículos tratados de la línea
        int index = 1; // indica el índice dentro del combo, el combo se completa cuando index = cantidadCombo
        BigDecimal ahorroTotal = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo
        BigDecimal ahorro = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo

        // obtenemos el combo aplicable para la cantidad de artículos disponibles
        ComboCantidadDtoBean combo = detalle.getComboOptimo(cantidadAplicable);


        // recorremos todas las líneas aplicables. No incrementamos el índice i porque
        // tendremos que pasar por cada línea tantas veces como cantidad de artículos tenga la línea
        for (int i = 0; i < lineasAplicables.size();) {
            // si no tenemos combo para aplicar, hemos terminado
            if (combo == null) {
                break;
            }
            // obtenemos siguiente línea aplicable
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
            // aplicamos promoción sobre un artículo más de la línea
            promocionLinea.setCantidadPromocion(promocionLinea.getCantidadPromocion() + 1);
            promocionLinea.calculaAhorroDtoPorcentaje(combo.getDescuento());
            ahorroTotal = ahorroTotal.add(promocionLinea.getImporteTotalAhorro());
            ahorro = ahorro.add(promocionLinea.getImporteAhorro());
            cantidadAplicable--;

            // comprobamos si debemos seguir en la misma linea o pasar a la siguiente
            if (cantLinea == linea.getCantidad()) {
                cantLinea = 0;
                i++;
            }

            index++;

            // combrobamos si hemos completado un combo
            if (index > combo.getCantidad()) {
                lineas.addLineaDescuentoFinal(new DescuentoTicket(detalle.getTextoPromocion(), ahorroTotal, ahorro));
                ahorroTotal = BigDecimal.ZERO; // reiniciamos el ahorro para el nuevo combo
                ahorro = BigDecimal.ZERO; // reiniciamos el ahorro para el nuevo combo
                index = 1; // reiniciamos el índice para el nuevo combo
                combo = detalle.getComboOptimo(cantidadAplicable);// obtenemos el combo aplicable para la cantidad de artículos restantes
            }
        }
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }
}
