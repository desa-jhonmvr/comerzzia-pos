/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MGRI
 */
public class TotalesXML {

    private static final Logger log = Logger.getMLogger(TotalesXML.class);

    private TicketS ticket; // ticket al que pertenecen los totales
    private BigDecimal importeTarifaOrigen; // total de todas las líneas sin aplicar ningún tipo de descuento ni promoción
    private BigDecimal importeTotalTarifaOrigen; // importeTarifaOrigen con iva
    private BigDecimal importeTotalImporteFinal;
    private BigDecimal totalSinPromociones; // total sin tener en cuenta promociones (excepto promoción por precio)
    private BigDecimal totalPromocionesLineas; // total de ahorro de promociones aplicadas a líneas
    private BigDecimal totalPromocionesTotales; // total de ahorro de promociones aplicadas a totales
    private BigDecimal totalPromocionesMedioPago; // total de ahorro de promociones aplicadas a totales
    private BigDecimal totalPromocionesCabecera; // total de ahorro de promociones aplicadas a totales
    private BigDecimal descuentoPromocionesMedioPago;
    private BigDecimal descuentoPromocionesCabecera;
    private BigDecimal totalPromociones; // suma de totalPromocionesLineas + totalPromocionesTotales
    private BigDecimal totalAPagar; // totalSinPromociones - totalPromociones
    private BigDecimal totalLineasSinPromocion; // total de todas las líneas que no tienen ninguna promoción ni descuento aplicado
    private BigDecimal totalAhorroPagos; // total de ahorro derivado de los descuentos por cada forma de pago
    private BigDecimal totalDtoPagos; // % total aplicado en todos los pagos para obtener el ahorro anterior
    private BigDecimal ahorroPagos; // totalAhorroPagos sin iva
    private BigDecimal totalDtoPromoSubtotales; // % total de dto aplicado al total para descontar promociones de subtotales
    private BigDecimal totalPagado; // totalAPagar - totalAhorroPagos
    private BigDecimal base; // totalPagado - impuestos
    private BigDecimal impuestos; // iva
    private BigDecimal impuestosIce; // Ice
    private Integer baseImponibleIce; //base imponible 
    private BigDecimal tarifaIce; //Tarifa Ice
    private Map<String, ConfigImpPorcentaje> subtotalesImpuestos;
    private BigDecimal totalDeducibleAlimentacion;
    private BigDecimal totalDeducibleMedicina;
    private BigDecimal totalDeducibleRopa;
    private BigDecimal totalDeducibleVivienda;
    private BigDecimal totalDeducibleEducacion;
    private BigDecimal totalDescuentosManuales; // suma de descuentos manuales
    private List<PromocionLineaTicket> promocionesATotal;
    private BigDecimal interes;
    private BigDecimal totalDescuentoFinalElectronico = BigDecimal.ZERO;
    private BigDecimal totalGarantiaExtendida = BigDecimal.ZERO;
    private BigDecimal compensacionGobierno = BigDecimal.ZERO;
    Map<BigDecimal, BigDecimal> totalesAsociadosABases; // Creamos mapas y valores inicializados de valores acumulados para calcular la base total

    public TotalesXML() {
    }

    /**
     * Construye parcialmente el objeto totales desde el XML (PARA NOTAS DE
     * CRÉDITO)
     */
    public TotalesXML(XMLDocumentNode totalesXml) throws XMLDocumentNodeNotFoundException {
        totalAPagar = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_TOTAL_A_PAGAR).getValue());
        totalPagado = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_TOTAL_PAGADO).getValue());
        impuestos = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_IMPUESTOS).getValue());
        try {
            impuestosIce = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_IMPUESTOS_ICE).getValue());
        } catch (XMLDocumentNodeNotFoundException ex) {
            impuestosIce = BigDecimal.ZERO;
        }
        
        base = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_BASE).getValue());
    }

    public TotalesXML(TicketS ticket) {
        this.ticket = ticket;
        promocionesATotal = new ArrayList<PromocionLineaTicket>();
        resetearTotales();
        inicializarSubtotales();

    }

    /**
     * Resetea todos los totales poniéndolos a cero. También vacía la lista de
     * promociones sobre subtotales.
     */
    public void resetearTotales() {
        impuestosIce = BigDecimal.ZERO;
        interes = BigDecimal.ZERO;
        totalSinPromociones = BigDecimal.ZERO;
        totalPromocionesLineas = BigDecimal.ZERO;
        base = BigDecimal.ZERO;
        impuestos = BigDecimal.ZERO;
        importeTarifaOrigen = BigDecimal.ZERO;
        importeTotalTarifaOrigen = BigDecimal.ZERO;
        resetearTotalesPromociones();
        promocionesATotal.clear();

        totalPromocionesMedioPago = BigDecimal.ZERO;
        totalPromocionesCabecera = BigDecimal.ZERO;
        totalGarantiaExtendida = BigDecimal.ZERO;
    }

    /**
     * Resetea los totales finales para recalcular incluyendo promociones sobre
     * subtotales
     */
    private void resetearTotalesPromociones() {
        totalPromocionesTotales = BigDecimal.ZERO;
        totalPromociones = BigDecimal.ZERO;
        totalAPagar = BigDecimal.ZERO;
        totalLineasSinPromocion = BigDecimal.ZERO;
        totalDescuentosManuales = BigDecimal.ZERO;
        totalPromocionesMedioPago = BigDecimal.ZERO;
        totalPromocionesCabecera = BigDecimal.ZERO;
    }

    /**
     * Redondea todas las cantidades.
     */
    public void redondear() {
        impuestos = Numero.redondear(impuestos);
        impuestosIce = Numero.redondear(impuestosIce);
        totalSinPromociones = Numero.redondear(totalSinPromociones);
        totalPromocionesLineas = Numero.redondear(totalPromocionesLineas);
        totalPromocionesTotales = Numero.redondear(totalPromocionesTotales);
        totalPromociones = Numero.redondear(totalPromociones);
        totalAPagar = Numero.redondear(totalAPagar);
        totalLineasSinPromocion = Numero.redondear(totalLineasSinPromocion);
        base = Numero.redondear(base);
        impuestos = Numero.redondear(impuestos);
        importeTarifaOrigen = Numero.redondear(importeTarifaOrigen);
        importeTotalTarifaOrigen = Numero.redondear(importeTotalTarifaOrigen);
        totalDescuentosManuales = Numero.redondear(totalDescuentosManuales);
        totalAhorroPagos = Numero.redondear(totalAhorroPagos);
        totalPagado = Numero.redondear(totalPagado);

    }

    /**
     * Recalcula los totales del ticket a partir de las líneas y las promociones
     * de estas líneas, ya sean unitarias o múltiples. No se tienen en cuenta
     * posibles promociones sobre subtotales.
     *
     * @param lineasTicket
     */
    public void recalcularTotalesLineas(LineasTicket lineasTicket) {

        resetearTotales();
        for (LineaTicket linea : lineasTicket.getLineas()) {
            totalSinPromociones = totalSinPromociones.add(linea.getImporteTotal());
            if (linea.isDescuentoAplicado() && !linea.isPromocionAplicada()) {
                totalDescuentosManuales = totalDescuentosManuales.add(linea.getDescuentoPrecioTotal());
            }
            totalPromocionesLineas = totalPromocionesLineas.add(linea.getImporteTotalAhorro());
            importeTotalTarifaOrigen = importeTotalTarifaOrigen.add(linea.getPrecioTotalTarifaOrigen().multiply(new BigDecimal(linea.getCantidad())));
            if (!linea.isPromocionAplicada() && !linea.isDescuentoAplicado()) {
                totalLineasSinPromocion = totalLineasSinPromocion.add(linea.getImporteTotal());
            }
            if (linea.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))) {
                totalGarantiaExtendida = totalGarantiaExtendida.add(linea.getImporteTotal());
            }

            // Cálculo del importe de la tarifa origen
            importeTarifaOrigen = importeTarifaOrigen.add(linea.getPrecioTarifaOrigen().multiply(new BigDecimal(linea.getCantidad())));
//            impuestosIce = impuestosIce.add(linea.getImpuestosIce());

            interes = interes.add(linea.getInteres());
            log.debug("LineaTicket<<<>>> idLinea:"+linea.getIdlinea());
            log.debug("LineaTicket<<<>>> codigo:"+linea.getArticulo().getCodart());
            log.debug("LineaTicket<<<>>> cantidad:"+linea.getCantidad());
            log.debug("LineaTicket<<<>>> interes:"+linea.getInteres());

        }
        log.debug("recalcularTotalesLineas<<<>>> interes:"+interes);
        interes = interes.setScale(BigDecimal.ROUND_CEILING,RoundingMode.HALF_UP);
        totalPromociones = totalPromocionesLineas; // no se incluyen las promociones sobre subtotales
        totalAPagar = totalSinPromociones.subtract(totalPromociones); // no se incluyen las promociones sobre subtotales
        log.debug("recalcularTotalesLineas<<<>>> getTotalAPagar:"+totalAPagar);
        totalAPagar = totalAPagar.add(interes);

        log.debug("recalcularTotalesLineas<<<>>> totalAPagar.add(interes):"+totalAPagar);
    }

    /**
     * Recalcula los totales del ticket incluyendo las promociones sobre
     * subtotales que se hayan aplicados a partir de los totales ya calculados
     * de las líneas (deben estar previamente calculados).
     */
    private void recalcularTotalesPromociones() {
        resetearTotalesPromociones();
        for (PromocionLineaTicket promocion : promocionesATotal) {
            totalPromocionesTotales = totalPromocionesTotales.add(promocion.getImporteTotalPromocion());
        }
        totalPromociones = totalPromocionesLineas.add(totalPromocionesTotales);
        totalAPagar = totalSinPromociones.subtract(totalPromociones);
    }

    /**
     * Añade una promoción aplicada sobre los subtotales, y recalcula los
     * totales teniendo en cuenta la nueva promoción.
     *
     * @param promocion
     */
    public void addPromocionATotal(PromocionLineaTicket promocion) {
        promocionesATotal.add(promocion);
        recalcularTotalesPromociones();
        ticket.getLineas().addLineaDescuentoFinal(promocion.getDescuentoTicket());
    }

    public void calcularTotalDescuentos(BigDecimal totalAhorroPagos) {
        // calculamos descuento por el ahorro de promociones sobre subtotales con respecto al total sin estas promociones
        BigDecimal totalSinPromoSubtotales = totalSinPromociones.subtract(totalGarantiaExtendida).subtract(totalPromocionesLineas);
        if (totalSinPromoSubtotales.compareTo(BigDecimal.ZERO) != 0) {
            this.totalDtoPromoSubtotales = totalPromocionesTotales.multiply(Numero.CIEN).divide(totalSinPromoSubtotales, 10, BigDecimal.ROUND_HALF_UP);
        } else {
            this.totalDtoPromoSubtotales = BigDecimal.ZERO;
        }
//         totalAhorroPagos = BigDecimal.ZERO;

        // calculamos el descuento por el ahorro de los pagos con respecto al total a pagar
        this.totalAhorroPagos = Numero.redondear(totalAhorroPagos);
        this.totalPagado = totalAPagar.subtract(totalAhorroPagos);
        try {
            this.totalDtoPagos = totalAhorroPagos.multiply(Numero.CIEN).divide(totalAPagar.subtract(totalGarantiaExtendida), 10, RoundingMode.HALF_UP);
        } catch (ArithmeticException ex) {
            this.totalDtoPagos = totalAhorroPagos.multiply(Numero.CIEN);
        }
    }

    public void calcularSubtotalesProrrateados(LineasTicket lineas) {

        // Creamos mapas y valores inicializados de valores acumulados para calcular la base total
        totalesAsociadosABases = new HashMap<BigDecimal, BigDecimal>();
        totalesAsociadosABases.put(BigDecimal.ZERO, BigDecimal.ZERO);
        // Esto es para los subtotales
        Map<String, BigDecimal> totalesAsociadosACodigosDeImpuestos = new HashMap<String, BigDecimal>();

        // reseteamos los totales
        totalDeducibleAlimentacion = BigDecimal.ZERO;
        totalDeducibleMedicina = BigDecimal.ZERO;
        totalDeducibleRopa = BigDecimal.ZERO;
        totalDeducibleEducacion = BigDecimal.ZERO;
        totalDeducibleVivienda = BigDecimal.ZERO;
        interes = BigDecimal.ZERO;
        base = BigDecimal.ZERO;
        BigDecimal margenDescuentoFinal = new BigDecimal("0.03");
        setTotalDescuentoFinalElectronico(BigDecimal.ZERO);

        for (ConfigImpPorcentaje configImpuestos : subtotalesImpuestos.values()) {
            configImpuestos.setTotal(BigDecimal.ZERO);
        }
        if (lineas == null || lineas.getLineas().isEmpty()) {
            subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO).setTotal(totalPagado);
            subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).setTotal(BigDecimal.ZERO);
            base = totalPagado;
        } else {
            // recorremos las líneas para realizar las sumas que nos interesen
            for (LineaTicket linea : lineas.getLineas()) {
                //TODO: Calcular para cada total deducible el importeFinal Pagado en función de las acumulaciones de importeTotalFinalPagado para cada porcentaje de iva              
                // si el artículo es de la familia de deducible por alimentación
                if (linea.getArticulo().isDeduclibleAlimentacion()) {
                    totalDeducibleAlimentacion = totalDeducibleAlimentacion.add(linea.getImporteFinalPagado());
                } // si el artículo es de la familia deducible por medicinas
                else if (linea.getArticulo().isDeduclibleMedicinas()) {
                    totalDeducibleMedicina = totalDeducibleMedicina.add(linea.getImporteFinalPagado());
                } // si el artículo es de la familia deducible por medicinas
                else if (linea.getArticulo().isDeduclibleRopa()) {
                    totalDeducibleRopa = totalDeducibleRopa.add(linea.getImporteFinalPagado());
                } else if (linea.getArticulo().isDeduclibleEducacion()) {
                    totalDeducibleEducacion = totalDeducibleEducacion.add(linea.getImporteFinalPagado());
                } else if (linea.getArticulo().isDeduclibleVivienda()) {
                    totalDeducibleVivienda = totalDeducibleVivienda.add(linea.getImporteFinalPagado());
                }
                if (Numero.isMayorACero(linea.getImporteDescuentoFinal())) {
                    setTotalDescuentoFinalElectronico(getTotalDescuentoFinalElectronico().add(linea.getImporteDescuentoFinal()));
                }
                // Acumulamos las bases por código de impuestos
                if (linea.getArticulo().getCodimp() != null) {
                    ConfigImpPorcentaje configImpuestos = subtotalesImpuestos.get(linea.getArticulo().getCodimp());
                    if (configImpuestos != null) {
                        if (!totalesAsociadosACodigosDeImpuestos.containsKey(linea.getArticulo().getCodimp())) {
                            totalesAsociadosACodigosDeImpuestos.put(linea.getArticulo().getCodimp(), BigDecimal.ZERO);
                        }
                        BigDecimal totalImporteFinalPagado = totalesAsociadosACodigosDeImpuestos.get(linea.getArticulo().getCodimp()).add(linea.getImporteFinalPagado());
                        totalesAsociadosACodigosDeImpuestos.put(linea.getArticulo().getCodimp(), totalImporteFinalPagado);
                    }
                }

                if (linea.getDescuentoFinal().compareTo(margenDescuentoFinal) > 0) {
                    linea.setDescuentosAcumulados(linea.getDescuentoFinal());
                } else {
                    linea.setDescuentosAcumulados(BigDecimal.ZERO);
                }

                base = base.add(linea.getImporteFinalPagado());
                interes = interes.add(linea.getInteres());
            }
            totalDeducibleAlimentacion = Numero.redondear(totalDeducibleAlimentacion);
            totalDeducibleMedicina = Numero.redondear(totalDeducibleMedicina);
            totalDeducibleRopa = Numero.redondear(totalDeducibleRopa);
            totalDeducibleEducacion = Numero.redondear(totalDeducibleEducacion);
            totalDeducibleVivienda = Numero.redondear(totalDeducibleVivienda);

        }

        // Cálculo real de subtotales
        for (Map.Entry<String, BigDecimal> entradaTAACDI : totalesAsociadosACodigosDeImpuestos.entrySet()) {
            // base asociada = total / (100 + porcentaje / 100)            
            ConfigImpPorcentaje configImpuestos = subtotalesImpuestos.get(entradaTAACDI.getKey());
            configImpuestos.setTotal(entradaTAACDI.getValue());
        }
        // calculamos impuestos y descuentos de totales y pagos sin descuentos
        BigDecimal base12 = Numero.redondear(subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getTotal());
        impuestos = Numero.porcentajeR(base12, subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getPorcentaje());
        BigDecimal totalPagadoCandidato = Numero.redondear(base).add(impuestos);
        BigDecimal diferencia = Numero.redondear(totalPagado).subtract(totalPagadoCandidato);
        if (!Numero.isIgualACero(diferencia)) {
            impuestos = impuestos.add(diferencia);
            log.warn("calcularSubtotalesProrrateados() - Ajustados centavos en impuesto por diferencia: " + diferencia + ". Total Pagado: " + totalPagado + ". Total Pagado Candidato: " + totalPagadoCandidato);
        } else {
            totalPagado = totalPagadoCandidato;
        }

        BigDecimal porcentajeIvaTotal;
        try {
            porcentajeIvaTotal = impuestos.multiply(Numero.CIEN).divide(totalPagado, 10, BigDecimal.ROUND_HALF_UP);
        } catch (ArithmeticException e) {
            porcentajeIvaTotal = impuestos.multiply(Numero.CIEN);
        }
        ahorroPagos = Numero.menosPorcentaje(totalAhorroPagos, porcentajeIvaTotal);
        BigDecimal porcentajeIva = Sesion.getEmpresa().getPorcentajeIva();
        for (Pago pago : ticket.getPagos().getPagos()) {
            pago.setTotalSinIva(Numero.menosPorcentaje(pago.getTotal(), porcentajeIvaTotal));
            pago.setUstedPagaSinIva(Numero.menosPorcentaje(pago.getUstedPaga(), porcentajeIvaTotal));
            if (ticket.getPagos().isCompensacionAplicada()) {
                totalPagado = totalPagado.subtract(compensacionGobierno);
            }
            BigDecimal porcentajePago = Numero.getTantoPorCientoContenidoCompleto(totalPagado, pago.getUstedPaga());
            if (pago.getSubtotalIva12() == null) {
                log.info("actualizando base 12 3 " + Numero.porcentajeR(subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getTotal(), porcentajePago));
                pago.setSubtotalIva12(Numero.porcentajeR(subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getTotal(), porcentajePago));
            }
            if (pago.getSubtotalIva0() == null) {
                pago.setSubtotalIva0(Numero.porcentajeR(subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO).getTotal(), porcentajePago));
            }
            if (pago.getIva() == null) {
                //pago.setIva(Numero.porcentajeR(impuestos, porcentajePago));
                pago.setIva(Numero.porcentajeR(pago.getSubtotalIva12(), porcentajeIva));
            }
            //interes = interes.add(pago.getImporteInteres());

            BigDecimal ustedPagaAnterior = pago.getUstedPaga();
            log.debug("calcularSubtotalesProrrateados() 1  pago.getTotal() " + pago.getTotal() + " " + pago.getMedioPagoActivo());
            if (pago.getSubtotalIva12().add(pago.getSubtotalIva0()).add(pago.getIva()).compareTo(pago.getUstedPaga()) != 0 && pago.getPlanSeleccionado() != null) {
                if (pago.getSubtotalIva0().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal diferenciaUstedPaga = pago.getUstedPaga().subtract(pago.getSubtotalIva12()).subtract(pago.getSubtotalIva0()).subtract(pago.getIva());
                    pago.setSubtotalIva0(pago.getSubtotalIva0().add(diferenciaUstedPaga));
                } else {
                    BigDecimal base12MasIva = ustedPagaAnterior.subtract(pago.getSubtotalIva0());
                    if (ticket.getPagos().isCompensacionAplicada()) {
                        porcentajeIvaTotal = porcentajeIvaTotal.subtract(VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO));
                    }
                    BigDecimal base12T = Numero.getAntesDePorcentajeR(base12MasIva, porcentajeIva);
                    BigDecimal ivaR = Numero.porcentajeR(base12T, porcentajeIva);
                    BigDecimal subtotalIva12 = base12T;
                    if (subtotalIva12.add(pago.getSubtotalIva0()).add(ivaR).compareTo(ustedPagaAnterior) > 0) {
                        base12T = Numero.getAntesDePorcentajeT(base12MasIva, porcentajeIva);
                    }
                    ivaR = Numero.porcentajeR(base12T, porcentajeIva);
                    subtotalIva12 = base12T;
                    pago.setIva(ivaR);
                    log.info("actualizando base 12 3 " + Numero.porcentajeR(subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getTotal(), porcentajePago));
                    pago.setSubtotalIva12(subtotalIva12);
                    pago.setUstedPaga(pago.getSubtotalIva0().add(pago.getSubtotalIva12()).add(pago.getIva()));
                    pago.setUstedPagaSinIva(pago.getSubtotalIva0().add(pago.getSubtotalIva12()));
                    if (pago.getDescuento() != null && pago.getDescuento().compareTo(BigDecimal.ZERO) != 0) {
                        pago.setTotal(pago.getSubtotalIva0().add(pago.getSubtotalIva12()).add(pago.getIva()).add(Numero.redondear(pago.getAhorro())));
                    } else {
                        pago.setTotal(pago.getSubtotalIva0().add(pago.getSubtotalIva12()).add(pago.getIva()));
                    }
                    log.debug("calcularSubtotalesProrrateados() 2  pago.getTotal() " + pago.getTotal() + " " + pago.getMedioPagoActivo());
                    if (pago.getRetencion() == null) {
                        pago.getPlanSeleccionado().recalcularFromTotal(pago.getTotal(), BigDecimal.ZERO);
                    } else {
                        pago.getPlanSeleccionado().recalcularFromTotal(pago.getTotal(), pago.getRetencion().getTotal());
                    }
                    // if(diferencia.abs().compareTo((new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_UP))) != 0){
                    ticket.getPagos().recalculaTotales();
                    // }else{
                    //     base = base.subtract(diferencia.abs());
                    // }
                    //Se debe truncar
                    log.debug("actualizar ustedPaga");
                    log.debug("calcularSubtotalesProrrateados() 3  pago.getTotal() " + pago.getTotal() + " " + pago.getMedioPagoActivo());
                }
            }
        }
        interes = Numero.redondear(interes);

        for (PromocionLineaTicket promocion : promocionesATotal) {
            BigDecimal importeAhorro = Numero.menosPorcentajeR(promocion.getImporteTotalAhorro(), porcentajeIvaTotal);
            promocion.setImportesAhorro(importeAhorro, promocion.getImporteTotalAhorro());
            promocion.getDescuentoTicket().setDescuento(promocion.getImporteAhorro());
        }

    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public String getImpuestosString() {
        return Numero.redondear(impuestos).toString();
    }

    public BigDecimal getTotalSinPromociones() {
        return totalSinPromociones;
    }

    public void setTotalSinPromociones(BigDecimal totalSinPromociones) {
        this.totalSinPromociones = totalSinPromociones;
    }

    public BigDecimal getTotalPromocionesLineas() {
        return totalPromocionesLineas;
    }

    public void setTotalPromocionesLineas(BigDecimal totalPromocionesLinea) {
        this.totalPromocionesLineas = totalPromocionesLinea;
    }

    public BigDecimal getTotalPromocionesTotales() {
        return totalPromocionesTotales;
    }

    public void setTotalPromocionesTotales(BigDecimal totalPromocionesTotales) {
        this.totalPromocionesTotales = totalPromocionesTotales;
    }

    public BigDecimal getTotalPromociones() {
        return totalPromociones;
    }

    public void setTotalPromociones(BigDecimal totalPromociones) {
        this.totalPromociones = totalPromociones;
    }

    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public BigDecimal getTotalAfiliadoUnaCuota() {
        return Numero.menosPorcentajeR(totalAPagar, BigDecimal.TEN);
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public List<PromocionLineaTicket> getPromocionesATotal() {
        return promocionesATotal;
    }

    public BigDecimal getTotalAhorroPagos() {
        return totalAhorroPagos;
    }

    public void setTotalAhorroPagos(BigDecimal totalAhorroPagos) {
        this.totalAhorroPagos = totalAhorroPagos;
    }

    public BigDecimal getAhorroPagos() {
        return ahorroPagos;
    }

    public String getTotalAhorroPagosString() {
        return Numero.redondear(totalAhorroPagos).toString();
    }

    public void setTotalDtoPagos(BigDecimal totalDtoPagos) {
        this.totalDtoPagos = totalDtoPagos;
    }

    public BigDecimal getTotalDtoPagos() {
        return totalDtoPagos;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalDtoPromoSubtotales() {
        return totalDtoPromoSubtotales;
    }

    public BigDecimal getTotalDeducibleAlimentacion() {
        return totalDeducibleAlimentacion;
    }

    public BigDecimal getTotalDeducibleMedicina() {
        return totalDeducibleMedicina;
    }

    public BigDecimal getTotalDeducibleRopa() {
        return totalDeducibleRopa;
    }

    public BigDecimal getTotalDeducibleEducacion() {
        return totalDeducibleEducacion;
    }

    public BigDecimal getTotalDeducibleVivienda() {
        return totalDeducibleVivienda;
    }

    public boolean isTotalDeducibleAlimentacion() {
        return totalDeducibleAlimentacion.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isTotalDeducibleMedicina() {
        return totalDeducibleMedicina.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isTotalDeducibleRopa() {
        return totalDeducibleRopa.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isTotalDeducibleEducacion() {
        return totalDeducibleEducacion.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isTotalDeducibleVivienda() {
        return totalDeducibleVivienda.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isTotalDeducible() {
        return getTotalDeducible().compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getTotalDeducible() {
        return totalDeducibleRopa.add(totalDeducibleAlimentacion).add(totalDeducibleMedicina).add(totalDeducibleEducacion).add(totalDeducibleVivienda);
    }

    public String getTotalDeducibleString() {
        return Numero.redondear(getTotalDeducible()).toString();
    }

    public Map<String, ConfigImpPorcentaje> getSubtotalesImpuestos() {

        return subtotalesImpuestos;
    }

    public BigDecimal getBase() {
        return base;
    }

    public BigDecimal getAhorroPagosString() {
        return Numero.redondear(ahorroPagos);
    }

    public BigDecimal getImporteTarifaOrigen() {
        return importeTarifaOrigen;
    }

    public void setImporteTarifaOrigen(BigDecimal importeTarifaOrigen) {
        this.importeTarifaOrigen = importeTarifaOrigen;
    }

    public BigDecimal getImporteTotalTarifaOrigen() {
        return importeTotalTarifaOrigen;
    }

    public void setImporteTotalTarifaOrigen(BigDecimal importeTotalTarifaOrigen) {
        this.importeTotalTarifaOrigen = importeTotalTarifaOrigen;
    }

    void recalcularTotalesLineasV(Pago p) {
        this.totalPagado = totalPagado.subtract(p.getTotal());
    }

    public BigDecimal getTotalAhorro() {
        return Numero.redondear(this.totalPromociones.add(totalAhorroPagos).add(totalDescuentosManuales));
    }

    public BigDecimal getTotalAhorroPrintBM() {
        return Numero.redondear(this.importeTotalTarifaOrigen.subtract(totalPagado));
    }

    public BigDecimal getTotalLineasSinPromocion() {
        return totalLineasSinPromocion;
    }

    public BigDecimal getInteres() {

        if(interes == null ){
            return BigDecimal.ZERO;
        }
        return interes;
    }

    public BigDecimal getBasePantalla() {
        return this.getBase().setScale(2, RoundingMode.UP);
    }

    public BigDecimal getTotalSinPromocionMedioPagoPantalla() {
        return totalPagado.setScale(2, RoundingMode.UP).add((totalPromocionesMedioPago).setScale(2, RoundingMode.HALF_DOWN));
    }

    public BigDecimal getTotalSinPromocionCabeceraYMedioPagoPantalla() {
        BigDecimal res = BigDecimal.ZERO;
        if (ticket != null && ticket.getLineas() != null) {
            for (LineaTicket linea : ticket.getLineas().getLineas()) {
                if (!linea.isGarantiaExtendida()) {
                    if (linea.getArticulo().getPmp() == null) {
                        if (linea.descuentosPromocionesLineas.compareTo(BigDecimal.ZERO) > 0) {
                            res = res.add(linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPagoSinImpuesto());
                        } else {
                            res = res.add(linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPago());
                        }
                    }
                }
            }
        }
        return res;
    }

    public BigDecimal getTotalSinPromocionCabeceraYMedioPagoPantallaF() {
        BigDecimal res = BigDecimal.ZERO;
        String iva = "1";
        if (ticket != null && ticket.getLineas() != null) {
            for (LineaTicket linea : ticket.getLineas().getLineas()) {
                if (!linea.isGarantiaExtendida()) {
                    if (linea.getArticulo().getPmp() == null) {
                        if (iva.equals(linea.getArticulo().getCodimp())) {
                            if (linea.descuentosPromocionesLineas.compareTo(BigDecimal.ZERO) > 0) {
                                res = linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPagoSinImpuesto();
                                linea.setImporteTotalFinalMenosPromocionCabeceraYMedioPago(res);
                            } else {
                                res = linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPago().multiply(new BigDecimal("1.12"));
                            }
                        } else {
                            res = res.add(linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPago());
                        }
                    }
                }
            }
        }
        return res;
    }

    public Long getTotalSinPromocionCabeceraYMedioPagoPantallaFundas() {
        Long res = 0L;
        Long suma = 0L;
        if (ticket != null && ticket.getLineas() != null) {
            for (LineaTicket linea : ticket.getLineas().getLineas()) {
                suma = suma + 1;
                if (linea.getArticulo().getPmp() != null) {
                    res = res + 1;
                } else {
                    res = 0l;
                }
            }
            res = res - suma;
        }
        return res;
    }

    public BigDecimal getTotalCotizacion() {
        BigDecimal res = BigDecimal.ZERO;
        if (ticket != null && ticket.getLineas() != null) {
            for (LineaTicket linea : ticket.getLineas().getLineas()) {
                if (!linea.isGarantiaExtendida()) {
                    res = res.add(linea.getImporteTotalFinalPagado());
                }
            }
        }
        return res;
    }

    public BigDecimal getTotalDescuentoMediosPagoSinImpuestos() {
        return Numero.porcentajeR(getTotalSinPromocionCabeceraYMedioPagoPantalla(), getDescuentoPromocionesMedioPago()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalDescuentoMediosPagoSinImpuestosF(BigDecimal descuento) {
        return Numero.porcentajeR(getTotalSinPromocionCabeceraYMedioPagoPantallaF(), descuento).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotal0() {
        return subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO).getTotal().setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotal12() {
        return subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getTotal().setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPorcentajeSubtotal() {
        return subtotalesImpuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getPorcentaje();
    }

    public void setSubtotalesImpuestos(Map<String, ConfigImpPorcentaje> subtotalesImpuestos) {
        this.subtotalesImpuestos = subtotalesImpuestos;
    }

    public void inicializarSubtotales() {
        subtotalesImpuestos = new HashMap<String, ConfigImpPorcentaje>(2);
        ConfigImpPorcentaje configImpuesto0 = new ConfigImpPorcentaje(BigDecimal.ZERO, ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO);
        ConfigImpPorcentaje configImpuesto12 = new ConfigImpPorcentaje(Sesion.getEmpresa().getPorcentajeIva(), ConfigImpPorcentaje.COD_IMPUESTO_NORMAL);

        subtotalesImpuestos.put(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO, configImpuesto0);
        subtotalesImpuestos.put(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL, configImpuesto12);
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public void calculaIVA() {
        if(interes != null && interes.compareTo(BigDecimal.ZERO) != 0){
            impuestos = totalAPagar.subtract(base).subtract(interes);
            return;
        }
        impuestos = totalAPagar.subtract(base);
    }

    public BigDecimal getTotalDescuentoFinalElectronico() {
        return totalDescuentoFinalElectronico;
    }

    public void setTotalDescuentoFinalElectronico(BigDecimal totalDescuentoFinalElectronico) {
        this.totalDescuentoFinalElectronico = totalDescuentoFinalElectronico;
    }

    public BigDecimal getTotalPromocionesMedioPago() {
        return totalPromocionesMedioPago;
    }

    public boolean isHayPromocionesMedioPago() {
        return (totalPromocionesMedioPago != null);
    }

    public boolean isHayPromocionesCabecera() {
        return (totalPromocionesCabecera != null);
    }

    public BigDecimal getTotalPromocionesMedioPagoPantalla() {
        if (totalPromocionesMedioPago != null) {
            return totalPromocionesMedioPago.setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalPromocionesMedioPagoPantallaBase() {
        if (totalPromocionesMedioPago != null) {
            return Numero.getAntesDePorcentaje(totalPromocionesMedioPago.setScale(2, RoundingMode.HALF_DOWN), Sesion.getEmpresa().getPorcentajeIva()).setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalPromocionesCabeceraPantalla() {
        if (totalPromocionesCabecera != null) {
            return Numero.getAntesDePorcentaje(totalPromocionesCabecera.setScale(2, RoundingMode.HALF_DOWN), Sesion.getEmpresa().getPorcentajeIva()).setScale(2, RoundingMode.HALF_DOWN);
            //return totalPromocionesCabecera.divide(new BigDecimal(1.12)).setScale(2,RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setTotalPromocionesMedioPago(BigDecimal totalPromocionesMedioPago) {
        this.totalPromocionesMedioPago = totalPromocionesMedioPago;
    }

    public BigDecimal getTotalPromocionesCabecera() {
        return totalPromocionesCabecera;
    }

    public void setTotalPromocionesCabecera(BigDecimal totalPromocionesBilleton) {
        this.totalPromocionesCabecera = totalPromocionesBilleton;
    }

    public BigDecimal getDescuentoPromocionesMedioPago() {
        if (descuentoPromocionesMedioPago != null) {
            return descuentoPromocionesMedioPago;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getDescuentoPromocionesMedioPagoPantalla() {
        if (descuentoPromocionesMedioPago != null) {
            return descuentoPromocionesMedioPago.setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getDescuentoPromocionesCabeceraPantalla() {
        if (totalPromocionesCabecera != null) {
            return totalPromocionesCabecera.setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setDescuentoPromocionesMedioPago(BigDecimal descuentoPromocionesMedioPago) {
        this.descuentoPromocionesMedioPago = descuentoPromocionesMedioPago;
    }

    public BigDecimal getDescuentoPromocionesCabecera() {
        return descuentoPromocionesCabecera;
    }

    public void setDescuentoPromocionesCabecera(BigDecimal descuentoPromocionesCabecera) {
        this.descuentoPromocionesCabecera = descuentoPromocionesCabecera;
    }

    public BigDecimal getTotalDescuentosManuales() {
        return totalDescuentosManuales;
    }

    public void setTotalDescuentosManuales(BigDecimal totalDescuentosManuales) {
        this.totalDescuentosManuales = totalDescuentosManuales;
    }

    public BigDecimal getTotalGarantiaExtendida() {
        return totalGarantiaExtendida;
    }

    public void setTotalGarantiaExtendida(BigDecimal totalGarantiaExtendida) {
        this.totalGarantiaExtendida = totalGarantiaExtendida;
    }

    public BigDecimal getCompensacionGobierno() {
        return compensacionGobierno;
    }

    public void setCompensacionGobierno(BigDecimal compensacionGobierno) {
        this.compensacionGobierno = compensacionGobierno;
    }

    public BigDecimal getImpuestosIce() {
        return impuestosIce;
    }

    public void setImpuestosIce(BigDecimal impuestosIce) {
        this.impuestosIce = impuestosIce;
    }

    public Integer getBaseImponibleIce() {
        return baseImponibleIce;
    }

    public void setBaseImponibleIce(Integer baseImponibleIce) {
        this.baseImponibleIce = baseImponibleIce;
    }

    public BigDecimal getTarifaIce() {
        return tarifaIce;
    }

    public void setTarifaIce(BigDecimal tarifaIce) {
        this.tarifaIce = tarifaIce;
    }

    public BigDecimal getImporteTotalImporteFinal() {
        return importeTotalImporteFinal;
    }

    public void setImporteTotalImporteFinal(BigDecimal importeTotalImporteFinal) {
        this.importeTotalImporteFinal = importeTotalImporteFinal;
    }

    public void inicializarDeducibles() {
        totalDeducibleAlimentacion = BigDecimal.ZERO;
        totalDeducibleEducacion = BigDecimal.ZERO;
        totalDeducibleMedicina = BigDecimal.ZERO;
        totalDeducibleRopa = BigDecimal.ZERO;
        totalDeducibleVivienda = BigDecimal.ZERO;
    }

    /**
     *
     * @param ticket
     */
    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    /**
     * <b>author:  </b> Gabriel Simbania Inicia valores para reconstruir el blob
     * para reimprimir
     *
     * @param xml
     * @throws XMLDocumentNodeNotFoundException
     */
    public void inicializarValoresReimpresion(XMLDocument xml) throws XMLDocumentNodeNotFoundException {

        XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);
        XMLDocumentNode totalesXml = cabecera.getNodo(TagTicketXML.TAG_TOTALES);
        ahorroPagos = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_AHORRO_PAGOS).getValue());
        totalDtoPagos = new BigDecimal(totalesXml.getNodo(TagTicketXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS).getValue());
        subtotalesImpuestos = new HashMap<String, ConfigImpPorcentaje>(2);

        XMLDocumentNode subTotalesporIva = totalesXml.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA);
        for (XMLDocumentNode impuesto : subTotalesporIva.getHijos()) {
            ConfigImpPorcentaje impPorcentaje = new ConfigImpPorcentaje(new BigDecimal(impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_PORCENTAJE).getValue()),
                    impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_CODIGO).getValue());
            impPorcentaje.setTotal(new BigDecimal(impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_IMPORTE).getValue()));
            subtotalesImpuestos.put(impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_CODIGO).getValue(), impPorcentaje);
        }

        //Registra el ahorro por el pago
        descuentoPromocionesMedioPago = totalDtoPagos;
    }

    @Override
    public String toString() {
        return "TotalesXML{" +
                "ticket=" + ticket +
                "\n, importeTarifaOrigen=" + importeTarifaOrigen +
                "\n, importeTotalTarifaOrigen=" + importeTotalTarifaOrigen +
                "\n, importeTotalImporteFinal=" + importeTotalImporteFinal +
                "\n, totalSinPromociones=" + totalSinPromociones +
                "\n, totalPromocionesLineas=" + totalPromocionesLineas +
                "\n, totalPromocionesTotales=" + totalPromocionesTotales +
                "\n, totalPromocionesMedioPago=" + totalPromocionesMedioPago +
                "\n, totalPromocionesCabecera=" + totalPromocionesCabecera +
                "\n, descuentoPromocionesMedioPago=" + descuentoPromocionesMedioPago +
                "\n, descuentoPromocionesCabecera=" + descuentoPromocionesCabecera +
                "\n, totalPromociones=" + totalPromociones +
                "\n, totalAPagar=" + totalAPagar +
                "\n, totalLineasSinPromocion=" + totalLineasSinPromocion +
                "\n, totalAhorroPagos=" + totalAhorroPagos +
                "\n, totalDtoPagos=" + totalDtoPagos +
                "\n, ahorroPagos=" + ahorroPagos +
                "\n, totalDtoPromoSubtotales=" + totalDtoPromoSubtotales +
                "\n, totalPagado=" + totalPagado +
                "\n, base=" + base +
                "\n, impuestos=" + impuestos +
                "\n, impuestosIce=" + impuestosIce +
                "\n, baseImponibleIce=" + baseImponibleIce +
                "\n, tarifaIce=" + tarifaIce +
                "\n, subtotalesImpuestos=" + subtotalesImpuestos +
                "\n, totalDeducibleAlimentacion=" + totalDeducibleAlimentacion +
                "\n, totalDeducibleMedicina=" + totalDeducibleMedicina +
                "\n, totalDeducibleRopa=" + totalDeducibleRopa +
                "\n, totalDeducibleVivienda=" + totalDeducibleVivienda +
                "\n, totalDeducibleEducacion=" + totalDeducibleEducacion +
                "\n, totalDescuentosManuales=" + totalDescuentosManuales +
                "\n, promocionesATotal=" + promocionesATotal +
                "\n, interes=" + interes +
                "\n, totalDescuentoFinalElectronico=" + totalDescuentoFinalElectronico +
                "\n, totalGarantiaExtendida=" + totalGarantiaExtendida +
                "\n, compensacionGobierno=" + compensacionGobierno +
                "\n, totalesAsociadosABases=" + totalesAsociadosABases +
                '}';
    }
}
