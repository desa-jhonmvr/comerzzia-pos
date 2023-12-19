/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboCantidadPrecioBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocionCombo;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;

import es.mpsistemas.util.fechas.Fecha;
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
public class PromocionTipoCombo extends Promocion {

    private List<DetallePromocionCombo> detalles;

    public PromocionTipoCombo(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        detalles = new ArrayList<DetallePromocionCombo>();

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detallesXML = xml.getNodo("detalles");

        //Recorremos la lista de hijos y la vamos añadiendo a la lista de detalles
        for (XMLDocumentNode detalle : detallesXML.getHijos()) {
            DetallePromocionCombo detallePromocionCombo = new DetallePromocionCombo();
            detallePromocionCombo.setFechaInicio(new Fecha(detalle.getNodo("fechaInicio", false).getValue()));
            detallePromocionCombo.setFechaFin(new Fecha(detalle.getNodo("fechaFin", false).getValue()));
            detallePromocionCombo.setTextoPromocion(detalle.getNodo("textoPromocion", true).getValue());
            detallePromocionCombo.setCodArticulo(detalle.getNodo("codArticulo", true).getValue());
            detallePromocionCombo.setDesArticulo(detalle.getNodo("desArticulo", true).getValue());
            detallePromocionCombo.setPrecioTarifa(detalle.getNodo("precioTarifa", true).getValueAsBigDecimal());
            detallePromocionCombo.setPrecioTarifaConImpuestos(detalle.getNodo("precioTarifaTotal", true).getValueAsBigDecimal());


            //Obtenemos el nodo articulos para obtener los hijos
            XMLDocumentNode combos = detalle.getNodo("combos");
            List<ComboCantidadPrecioBean> listaCombos = new ArrayList<ComboCantidadPrecioBean>();
            for (XMLDocumentNode combo : combos.getHijos()) {
                ComboCantidadPrecioBean comboDetalle = new ComboCantidadPrecioBean();
                comboDetalle.setPrecioVenta(combo.getNodo("precioVenta", false).getValueAsBigDecimal());
                comboDetalle.setPrecioTotal(combo.getNodo("precioTotal", false).getValueAsBigDecimal());
                comboDetalle.setCantidad(combo.getNodo("cantidad", false).getValueAsInteger());
                comboDetalle.setImporte(combo.getNodo("importe", false).getValueAsDouble());
                comboDetalle.setImporteTotal(combo.getNodo("importeTotal", false).getValueAsDouble());
                comboDetalle.setId(comboDetalle.getCantidad().toString());
                listaCombos.add(comboDetalle);
            }
            detallePromocionCombo.setCombos(listaCombos);

            this.detalles.add(detallePromocionCombo);
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
        for (DetallePromocionCombo detalle : detalles) {
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

    private void aplica(DetallePromocionCombo detalle, List<LineaTicket> lineasAplicables, LineasTicket lineas, Integer cantidadAplicable) {
        // variables de indices y acumulativas
        int cantLinea = 0; // indica la cantidad de artículos tratados de la línea
        int index = 1; // indica el índice dentro del combo, el combo se completa cuando index = cantidadCombo
        BigDecimal ahorroTotal = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo
        BigDecimal ahorro = BigDecimal.ZERO; // indica el ahorro acumulado en cada combo

        // obtenemos el combo aplicable para la cantidad de artículos disponibles
        ComboCantidadPrecioBean combo = detalle.getComboOptimo(cantidadAplicable);


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
                promocionLinea = new PromocionLineaTicket(this,PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
                promocionLinea.setTextoPromocion(detalle.getTextoPromocion());
                promocionLinea.setPrecioTarifa(linea.getPrecio());
                promocionLinea.setPrecioTarifaTotal(linea.getPrecioTotal());
                promocionLinea.setCantidadPromocion(0);
                linea.setPromocionLinea(promocionLinea);
            }
            // aplicamos promoción sobre un artículo más de la línea
            promocionLinea.setCantidadPromocion(promocionLinea.getCantidadPromocion() + 1);
            promocionLinea.calculaAhorroDtoPrecio(combo.getPrecioVenta(), combo.getPrecioTotal());
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
