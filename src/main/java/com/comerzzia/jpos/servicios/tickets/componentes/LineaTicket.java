/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import com.comerzzia.jpos.servicios.kit.KitReferencia;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.articulos.PromoMedioPago;
import com.comerzzia.jpos.servicios.promociones.articulos.SukuponLinea;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class LineaTicket implements Comparable<LineaTicket> {

    private static final Logger log = Logger.getMLogger(LineaTicket.class);

    private Integer idlinea;
    private Articulos articulo;
    private String codigoBarras;
    private Integer cantidad;
    private BigDecimal precioTarifaOrigen; // precio tarifa original sin ningún tipo de promociones ni descuentos
    private BigDecimal precioTotalTarifaOrigen; // precio tarifa + impuestos
    private BigDecimal descuento; // % descuento manual o por promoción por descuento
    private BigDecimal descuentoPrecio; // cantidad descuento manual o por promoción por descuento sobre el precio
    private BigDecimal descuentoPrecioTotal; // cantidad descuento manual o por promoción por descuento sobre el precio total
    private BigDecimal descuentoImporte; // cantidad descuento manual o por promoción por descuento sobre el importe
    private BigDecimal descuentoImporteTotal; // cantidad descuento manual o por promoción por descuento sobre el importe total
    private BigDecimal precio; // precioSinDto + (dto aplicado manual o promoción por dto)
    private BigDecimal precioTotal; // precio + impuestos
    private BigDecimal precioTotalSinRedondear; // precio + impuestos SIN redondear
    private BigDecimal importe; // precio * cantidad
    private BigDecimal importeTotal; // precioTotal * cantidad
    private BigDecimal impuestos; // importeTotal - impuestos
    private BigDecimal importeFinalPagado; // importe final pagado restando todas los descuentos
    private BigDecimal importeFinalPagadoSinRedondear; //importe final pagado restando todos los descuentos SIN redondear
    private BigDecimal importeTotalFinalPagado; // importe total final pagado restando todas los descuentos
    private BigDecimal importeTotalFinal;
    private BigDecimal importeTotalFinalPagadoSinRedondear; // importe total final pagado restando todas los descuentos SIN redondear
    private BigDecimal descuentoFinal; // % descuento final entre el precio de tarifa y el precio final pagado
    private BigDecimal descuentoFinalDev; // % descuento final entre el precio de tarifa y el precio final pagado de la factura origen (solo para uso en devoluciones)
    private BigDecimal descuentoFinalFunda;
    private BigDecimal interes; //interes calculado por credito

    private DescuentoTicket impresionLineaDescuento; // linea de descuento que se imprimirá junto a esta línea

    private PromocionLineaTicket promocionLinea;
    private List<PromocionLineaTicket> promocionLineaList = new ArrayList<>(); //Para mantener en un listado las diferentes promociones y despues escoger la mejor
    private boolean promoUnitariaAplicada; // promociones siempre afectan sólo a una línea (dto y de precio)
    private boolean promoMultipleAplicada; // promociones que afectan a un conjunto de líneas
    private boolean articuloSeleccionado = false; //Campo para saber si el articulo ya fue seleccionado en la factura

    private String descripcionAdicional;
    private BigDecimal importePantalla;
    private BigDecimal importeTotalPantalla;
    private BigDecimal precioPantalla;
    private BigDecimal precioTotalPantalla;

    // importesAuxiliares para ticket
    BigDecimal importeTotalFinalMenosPromocionCabeceraYMedioPago;
    BigDecimal importeTotalFinalMenosPromocionCabeceraYMedioPagoSinImpuesto;
    BigDecimal importeTotalFinalTrasPromocionSubtotales;
    BigDecimal importeTotalFinalMenosDescuentosMedioPagos;

    BigDecimal porcentajeDescuentosPromocionesLineas;
    BigDecimal porcentajeDescuentosSubtotales;
    BigDecimal porcentajeDescuentosMedioPagos;

    BigDecimal descuentosPromocionesLineas;
    BigDecimal descuentosSubtotales;
    BigDecimal descuentosMedioPagos;
    private boolean garantiaGratuita = false;
    private String instalacion;

    private DatosAdicionalesLineaTicket datosAdicionales;

    // Campos específicos auxiliares
    private Character envioEnGuiaRemision;
    private LineaTicketOrigen lineaTicketOrigen;
    private KitReferencia referenciaKit;
    private GarantiaReferencia referenciaGarantia;
    private int canjeoPuntosCantidadAceptada;
    private List<SukuponLinea> sukuponesEmitidos;

    // Campo adicional para devoluciones
    private String codimp;

    // Acumulado de descuentos ofertados para la línea
    private BigDecimal descuentosAcumulados;

    // Para devoluciones
    private int lineaOriginal;
    private boolean devolucion;

    // Para facturación
    private boolean lineaFacturacion = false;

    //Impuestos
    private BigDecimal impuestosIce;
    private boolean pmpArticulo = false;

    //Compensación del gobierno de la línea
    private BigDecimal compensacionLinea;
    private BigDecimal porcentajeIva;
    //Cambio requerimiento Garantia
    private String codEmpleado;
    //Costo Landed RD
    private BigDecimal costoLanded;
    //CATEGORIA 
    private String codCategoria;
    //PRECIO REAL
    private BigDecimal precioReal;

    private boolean pedidoFacturado;
    private String observacionPedidoFacturado;

    private boolean devuelto;
    private Integer stockDisponible;
    private Integer stockDisponibleBodega;
    private Integer stockDisponibleLocales;
    private List<PromoMedioPago> listaPromocion;
    private Long itemBase;
    private String desArticuloBase;
    private BigDecimal precioBase;
    private Boolean kitObligatorio;
    private String kitReferenciaOrigen;
    private boolean entregaDomicilio = false;

    public LineaTicket(String codigo, Articulos art, Integer cantidad, BigDecimal precioTarifa, BigDecimal precioTotalTarifa, BigDecimal costolanded) {
        articulo = art;
        codigoBarras = codigo;
        this.cantidad = cantidad;
        precioTarifaOrigen = precioTarifa;
        this.costoLanded = costolanded;
        precioTotalTarifaOrigen = precioTotalTarifa;
        descuento = new BigDecimal(0);
        devolucion = false;
        recalcularPrecios();
        redondear();
    }

    // Para las devoluciones
    public LineaTicket(String codigo, Articulos art, Integer cantidad, BigDecimal precioTarifa, BigDecimal precioTotalTarifa, boolean devolucion) {
        articulo = art;
        codigoBarras = codigo;
        this.cantidad = cantidad;
        precioTarifaOrigen = precioTarifa;
        precioTotalTarifaOrigen = precioTotalTarifa;
        descuento = new BigDecimal(0);
        this.devolucion = devolucion;
        recalcularPrecios();
        redondear();
    }

    public LineaTicket(String codigo, Articulos art, Integer cantidad, BigDecimal precioTarifa, BigDecimal precioTotalTarifa, BigDecimal costolanded, String codCategoria, BigDecimal precioReal) {
        articulo = art;
        codigoBarras = codigo;
        this.cantidad = cantidad;
        precioTarifaOrigen = precioTarifa;
        this.costoLanded = costolanded;
        precioTotalTarifaOrigen = precioTotalTarifa;
        descuento = new BigDecimal(0);
        devolucion = false;
        this.codCategoria = codCategoria;
        if (!isGarantiaExtendida()) {
            this.precioReal = precioReal;
        } else {
            this.precioReal = precioTarifa;
        }
        recalcularPrecios();
        redondear();
    }

    public LineaTicket(XMLDocumentNode linea) throws TicketException {
        try {
            idlinea = linea.getAtributoValueAsInteger(TagTicketXML.ATR_LINEA_IDLINEA, false);
            articulo = new Articulos();
            articulo.setCodart(linea.getNodo(TagTicketXML.TAG_LINEA_CODART).getValue());
            articulo.setDesart(linea.getNodo(TagTicketXML.TAG_LINEA_DESART).getValue());
            articulo.setCodimp(linea.getNodo(TagTicketXML.TAG_LINEA_CODIMP).getValue());
            articulo.setModelo(linea.getNodo(TagTicketXML.TAG_LINEA_MODELO).getValue());
            try {
                if (linea.getNodo(TagTicketXML.TAG_LINEA_GARANTIA_ORIGINAL) != null) {
                    articulo.setGarantiaOriginal(Integer.parseInt(linea.getNodo(TagTicketXML.TAG_LINEA_GARANTIA_ORIGINAL).getValue()));
                }
            } catch (XMLDocumentNodeNotFoundException e) {

            }
            codigoBarras = linea.getNodo(TagTicketXML.TAG_LINEA_CODBARRAS).getValue();
            cantidad = linea.getNodo(TagTicketXML.TAG_LINEA_CANTIDAD).getValueAsInteger();
            precioTotalTarifaOrigen = linea.getNodo(TagTicketXML.TAG_LINEA_PRECIO_TOTAL_TARIFA_ORIGEN).getValueAsBigDecimal();
            precioTarifaOrigen = linea.getNodo(TagTicketXML.TAG_LINEA_PRECIO_TARIFA_ORIGEN).getValueAsBigDecimal();
            descuento = linea.getNodo(TagTicketXML.TAG_LINEA_DESCUENTO).getValueAsBigDecimal();
            try {
                descuentoFinalDev = linea.getNodo(TagTicketXML.TAG_LINEA_DESCUENTO_FINAL).getValueAsBigDecimal();
            } catch (XMLDocumentNodeNotFoundException e) {
                descuentoFinalDev = BigDecimal.ZERO;
            }
            precioTotal = linea.getNodo(TagTicketXML.TAG_LINEA_PRECIO_TOTAL).getValueAsBigDecimal();
            precio = linea.getNodo(TagTicketXML.TAG_LINEA_PRECIO).getValueAsBigDecimal();
            importeTotal = linea.getNodo(TagTicketXML.TAG_LINEA_IMPORTE_TOTAL).getValueAsBigDecimal();
            importe = linea.getNodo(TagTicketXML.TAG_LINEA_IMPORTE).getValueAsBigDecimal();
            importeTotalFinalPagado = linea.getNodo(TagTicketXML.TAG_LINEA_IMPORTE_TOTAL_FINAL).getValueAsBigDecimal();
            importeFinalPagado = linea.getNodo(TagTicketXML.TAG_LINEA_IMPORTE_FINAL).getValueAsBigDecimal();
            interes = linea.getNodo(TagTicketXML.TAG_LINEA_INTERES).getValueAsBigDecimal();
            codimp = linea.getNodo(TagTicketXML.TAG_LINEA_CODIMP).getValue();
            try {
                porcentajeIva = linea.getNodo(TagTicketXML.TAG_PORCENTAJE_IVA).getValueAsBigDecimal();
            } catch (XMLDocumentNodeNotFoundException ignore) {
            }
            try {
                linea.getNodo(TagTicketXML.TAG_PROMOCION, false);
                promoUnitariaAplicada = true;
                promoMultipleAplicada = true;
            } catch (XMLDocumentNodeNotFoundException e) {
                promoUnitariaAplicada = false;
                promoMultipleAplicada = false;
            }
            //Kit de instalación
            try {
                XMLDocumentNode kitInstalacion = linea.getNodo(TagTicketXML.TAG_KIT, false);
                Articulos art = ArticulosServices.getInstance().getArticuloCod(kitInstalacion.getNodo(TagTicketXML.TAG_KIT_ARTICULO).getValue());
                referenciaKit = new KitReferencia(art);
            } catch (XMLDocumentNodeNotFoundException e) {
            } catch (XMLDocumentException e) {
                log.error("LineaTicket() - No se ha podido recuperar los kit de instalación, error al formar el ticket origen", e);
            }
            //Extension de Garantia
            try {
                XMLDocumentNode garantiaExtendida = linea.getNodo(TagTicketXML.TAG_GARANTIA, false);
                Articulos art = ArticulosServices.getInstance().getArticuloCod(garantiaExtendida.getNodo(TagTicketXML.TAG_GARANTIA_ARTICULO).getValue());
                XMLDocumentNode tickOrigen = garantiaExtendida.getNodo(TagTicketXML.TAG_GARANTIA_UID_FACTURA, true);
                if (tickOrigen != null) {
                    TicketOrigen ticket;
                    try {
                        ticket = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) TicketService.consultarTicket(tickOrigen.getValue()).getTicket()));
                    } catch (TicketException | XMLDocumentException | NoResultException ex) {
                        throw new TicketException("No se pudo obtener la factura de referencia para la extensión de garantía para la línea " + idlinea + ": " + ex.getMessage(), ex);
                    }
                    referenciaGarantia = new GarantiaReferencia(art, ticket);
                } else {
                    referenciaGarantia = new GarantiaReferencia(this);
                    referenciaGarantia.setArticulo(art);
                }
                referenciaGarantia.setPreciotTotalFinalPagadoArticuloAsociado(garantiaExtendida.getNodo(TagTicketXML.TAG_GARANTIA_PRECIO).getValueAsBigDecimal());
                referenciaGarantia.setImporteTotalFinalPagadoArticuloAsociado(garantiaExtendida.getNodo(TagTicketXML.TAG_GARANTIA_IMPORTE).getValueAsBigDecimal());
            } catch (XMLDocumentNodeNotFoundException e) {
            } catch (XMLDocumentException e) {
                log.error("LineaTicket() - No se ha podido recuperar la garantia extendida, error al formar el ticket origen", e);
            }
            //Datos adicionales
            datosAdicionales = new DatosAdicionalesLineaTicket();
            try {
                if (linea.getNodo(TagTicketXML.TAG_LINEA_ENVIO_DOMICILIO) != null) {
                    datosAdicionales.setEnvioDomicilio(linea.getNodo(TagTicketXML.TAG_LINEA_ENVIO_DOMICILIO).getValue().equals("S"));
                }
            } catch (XMLDocumentNodeNotFoundException ex) {
            }

            try {
                if (linea.getNodo(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR) != null) {
                    datosAdicionales.setRecogidaPosterior(linea.getNodo(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR).getValue().equals("S"));
                }
            } catch (XMLDocumentNodeNotFoundException ex) {
            }

        } catch (XMLDocumentNodeNotFoundException ex) {
            log.error("Error parseando línea de ticket: " + ex.getMessage());
            throw new TicketException("Error parseando línea de ticket.", ex);
        }
    }

    public LineaTicket() {
    }

    public void recalcularPrecios() {
        descuentoPrecio = precioTarifaOrigen.multiply(descuento.divide(Numero.CIEN));
        if (cantidad > 0 && articulo.isArticuloTieneIva()) {
            descuentoPrecioTotal = Numero.masPorcentaje(precioTarifaOrigen, Sesion.getEmpresa().getPorcentajeIva()).multiply(descuento.divide(Numero.CIEN));
        } else {
            descuentoPrecioTotal = precioTotalTarifaOrigen.multiply(descuento.divide(Numero.CIEN));
        }

        //   descuentoPrecio = Numero.redondear(descuentoPrecio);
        precioTotalSinRedondear = precioTotalTarifaOrigen.subtract(descuentoPrecioTotal);
        descuentoPrecioTotal = Numero.redondear(descuentoPrecioTotal);
        precio = precioTarifaOrigen.subtract(descuentoPrecio);
        precioTotal = precioTotalTarifaOrigen.subtract(descuentoPrecioTotal);
        recalcularImportes();
    }

    public void recalcularImportes() {
        if (devolucion) {
            importeTotal = precioTotal.multiply(new BigDecimal(cantidad));
        } else if (cantidad > 0 && articulo.isArticuloTieneIva()) {
            importeTotal = Numero.redondear(Numero.masPorcentaje(precio, Sesion.getEmpresa().getPorcentajeIva()).multiply(new BigDecimal(cantidad)));
        } else {
            importeTotal = precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(cantidad));
        }
        importe = precio.multiply(new BigDecimal(cantidad)).setScale(2, BigDecimal.ROUND_HALF_UP);

        descuentoImporte = descuentoPrecio.multiply(new BigDecimal(cantidad));
        if (cantidad > 0 && articulo.isArticuloTieneIva()) {
            descuentoImporteTotal = Numero.masPorcentaje(precioTarifaOrigen, Sesion.getEmpresa().getPorcentajeIva()).multiply(descuento.divide(Numero.CIEN)).multiply(new BigDecimal(cantidad));
        } else {
            descuentoImporteTotal = descuentoPrecioTotal.multiply(new BigDecimal(cantidad));
        }
        if (descuento.equals(Numero.CIEN)) {
            descuentoImporteTotal = importeTotalPantalla;
        }

        impuestos = importeTotal.subtract(importe);
        recalcularFinalPagado(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    protected void recalcularFinalPagado(BigDecimal dtoPromoSubtotales, BigDecimal dtoPagos) {
        log.debug("recalcularFinalPagado>>>>>");
        log.debug("dtoPromoSubtotales>>>>>"+dtoPromoSubtotales);
        log.debug("dtoPagos>>>>>"+dtoPagos);
        importeTotalFinalPagado = importeTotal.subtract(getImporteTotalPromocion());
        importeTotalFinalMenosPromocionCabeceraYMedioPago = importeTotalFinalPagado;
        descuentosPromocionesLineas = getImporteTotalPromocion();
        if (!isGarantiaExtendida()) {
            importeTotalFinalPagado = Numero.menosPorcentaje(importeTotalFinalPagado, dtoPromoSubtotales);
        }
        importeTotalFinalTrasPromocionSubtotales = importeTotalFinalPagado;
        descuentosSubtotales = importeTotalFinalMenosPromocionCabeceraYMedioPago.subtract(importeTotalFinalTrasPromocionSubtotales);
        porcentajeDescuentosSubtotales = dtoPromoSubtotales;

        importeTotalFinalMenosDescuentosMedioPagos = importeTotalFinalPagado;
        if (!(getReferenciaGarantia() != null && !garantiaGratuita)) {
            importeTotalFinalPagado = Numero.menosPorcentaje(importeTotalFinalPagado, dtoPagos);
        }
        porcentajeDescuentosMedioPagos = dtoPagos;
        descuentosMedioPagos = importeTotalFinalMenosDescuentosMedioPagos.subtract(importeTotalFinalPagado);

        BigDecimal importeTarifaOrigen = precioTotalTarifaOrigen.multiply(new BigDecimal(cantidad));

        if (importeTarifaOrigen.compareTo(BigDecimal.ZERO) == 0
                || importeTarifaOrigen.toString().equals("0.0")
                || importeTarifaOrigen.toString().equals("0.00")
                || importeTarifaOrigen.toString().equals("0.000")
                || importeTarifaOrigen.toString().equals("0.0000")) {
            descuentoFinal = BigDecimal.ZERO;
        } else {
            if (getReferenciaGarantia() != null && !garantiaGratuita) {
                if ((descuento != null && descuento.compareTo(BigDecimal.ZERO) != 0) || (descuentosSubtotales != null && descuentosSubtotales.compareTo(BigDecimal.ZERO) != 0)) {
                    try {
                        importeTotalFinalPagadoSinRedondear = precioTotalSinRedondear.multiply(new BigDecimal(cantidad)).subtract(getImporteTotalPromocion());
                        importeTotalFinalPagadoSinRedondear = Numero.menosPorcentaje(importeTotalFinalPagadoSinRedondear, dtoPromoSubtotales);
                        // importeTotalFinalPagadoSinRedondear = Numero.menosPorcentaje(importeTotalFinalPagadoSinRedondear, dtoPagos);
                        descuentoFinal = Numero.getTantoPorCientoMenos(importeTarifaOrigen, importeTotalFinalPagadoSinRedondear);
                    } catch (Exception e) {
                        log.error("recalcularFinalPagado() - Se ha producido un error al calcular el descuentoFinal: " + e.getMessage(), e);
                        descuentoFinal = Numero.getTantoPorCientoMenos(importeTarifaOrigen, importeTotalFinalPagado);
                    }
                } else {
                    descuentoFinal = BigDecimal.ZERO;
                }
            } else {
                try {
                    importeTotalFinalPagadoSinRedondear = precioTotalSinRedondear.multiply(new BigDecimal(cantidad)).subtract(getImporteTotalPromocion());
                    importeTotalFinalPagadoSinRedondear = Numero.menosPorcentaje(importeTotalFinalPagadoSinRedondear, dtoPromoSubtotales);
                    importeTotalFinalPagadoSinRedondear = Numero.menosPorcentaje(importeTotalFinalPagadoSinRedondear, dtoPagos);
                    descuentoFinal = Numero.getTantoPorCientoMenos(importeTarifaOrigen, importeTotalFinalPagadoSinRedondear);
                } catch (Exception e) {
                    log.error("recalcularFinalPagado() - Se ha producido un error al calcular el descuentoFinal: " + e.getMessage(), e);
                    descuentoFinal = Numero.getTantoPorCientoMenos(importeTarifaOrigen, importeTotalFinalPagado);
                }
            }
        }

        if (getArticulo().getCodimp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
            if (tieneDescuento()) {
                //importeFinalPagado = Numero.getAntesDePorcentaje(Numero.redondear(importeTotalFinalPagado), Sesion.getEmpresa().getPorcentajeIva());
                importeFinalPagado = Numero.getAntesDePorcentaje(importeTotalFinalPagado, Sesion.getEmpresa().getPorcentajeIva());
                //importeFinalPagado = Numero.menosPorcentaje(precioTarifaOrigen.multiply(new BigDecimal(cantidad)), descuentoFinal);
            } else {
                importeFinalPagado = precioTarifaOrigen.multiply(new BigDecimal(cantidad));
            }
        } else {
            importeFinalPagado = importeTotalFinalPagado;
        }
        descuentoFinal = Numero.redondear(descuentoFinal);
    }

    public boolean tieneDescuento() {
        return Numero.esDiferenciaMayorAUnCentavo(precioTotalTarifaOrigen.multiply(new BigDecimal(cantidad)), importeTotalFinalPagado);
    }

    public void redondear() {
        precioTotal = precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        importe = importe.setScale(2, BigDecimal.ROUND_HALF_UP);
        importeTotal = importeTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        precio = precio.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void recalcularAhorroPromocion() {
        BigDecimal ahorro = promocionLinea.getPrecioTarifa().multiply(new BigDecimal(cantidad)).subtract(importe);
        BigDecimal ahorroTotal = promocionLinea.getPrecioTarifaTotal().multiply(new BigDecimal(cantidad)).subtract(importeTotal);
        promocionLinea.setImportesAhorro(ahorro, ahorroTotal);
    }

    public void setDescuentoManualLinea(BigDecimal descuento) {
        DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
        datosAdicionales.setDescuento(descuento);
        establecerDescuento(datosAdicionales.getDescuento());
        setDatosAdicionales(datosAdicionales);
    }

    /**
     * Establece descuento manual por pantalla. No debe ser llamado desde fuera,
     * sólo al establecer datos adicionales
     */
    public void establecerDescuento(BigDecimal descuento) {
        if (Numero.isMayorACero(descuento)) {
            this.descuento = descuento;
            setPreciosPantalla(precioTarifaOrigen, precioTotalTarifaOrigen);
            recalcularPrecios();
            setImpresionLineaDescuento("Descuento");
        } else {
            this.descuento = BigDecimal.ZERO;
            recalcularPrecios();
            impresionLineaDescuento = null;
            resetPreciosPantalla();
        }
    }

    public void resetDescuento() {
        this.descuento = BigDecimal.ZERO;
        recalcularPrecios();
        impresionLineaDescuento = null;
        resetPreciosPantalla();
    }

    public boolean isPromoUnitariaAplicada() {
        return promoUnitariaAplicada;
    }

    public boolean isPromoMultipleAplicada() {
        return promoMultipleAplicada;
    }

    public boolean isPromocionAplicada() {
        return isPromoUnitariaAplicada() || isPromoMultipleAplicada();
    }

    public boolean isPromocionAplicadaLineaCompleta() {
        return isPromocionAplicada();
        /* // TODO: PROMOCIONES: Para la aplicación de más de una promoción a una línea con cantidad mayor a uno 
         * hay que buscar otra solución, ya que no tenemos considerados la aplicación de dos promociones distintas sobre la 
         * misma línea. Una posibilidad sería romper la linea en caso de que se aplique una promoción a cantidad menor a la 
         * cantidad de la línea
        if (isPromocionAplicada()){
            return promocionLinea.getCantidadPromocion().equals(cantidad);
        }
        return false;*/
    }

    public boolean tieneImpresionDescuento() {
        return impresionLineaDescuento != null;
    }

    /**
     * Ordena de por precio descendente
     */
    @Override
    public int compareTo(LineaTicket linea) {
        return linea.getPrecioTotal().compareTo(getPrecioTotal());
    }

    public void resetPreciosPantalla() {
        this.precioPantalla = null;
        this.precioTotalPantalla = null;
        importePantalla = null;
        importeTotalPantalla = null;
    }

    public void setPreciosPantalla(BigDecimal precioPantalla, BigDecimal precioTotalPantalla) {
        this.precioPantalla = precioPantalla;
        this.precioTotalPantalla = precioTotalPantalla;
        importePantalla = precioPantalla.multiply(new BigDecimal(cantidad));

        if (devolucion) {
            importeTotalPantalla = precioTotalPantalla.multiply(new BigDecimal(cantidad));
        } else if (cantidad > 1 && articulo.isArticuloTieneIva()) {
            importeTotalPantalla = Numero.redondear(Numero.masPorcentaje(precioPantalla, Sesion.getEmpresa().getPorcentajeIva()).multiply(new BigDecimal(cantidad)));
        } else {
            importeTotalPantalla = precioTotalPantalla.multiply(new BigDecimal(cantidad));
        }
    }

    public String getImportePantalla() {
        return "$ " + getImportePantallaSinDolar();
    }

    public String getImportePantallaSinDolar() {
        if (importePantalla != null) {
            return "" + importePantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "" + importe.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getImporteTotalPantalla() {
        if (importeTotalPantalla != null) {
            return "$ " + importeTotalPantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + importeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getImporteTotalPantallaSinDolar() {
        if (importeTotalPantalla != null) {
            return "" + importeTotalPantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "" + importeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getPrecioPantalla() {
        return "$ " + getPrecioPantallaSinDolar();
    }

    public String getPrecioPantallaSinDolar() {
        return getPrecioPantallaSinDolar(4);
    }

    public String getPrecioPantallaSinDolar(int decimales) {
        if (precioPantalla != null) {
            return precioPantalla.setScale(decimales, BigDecimal.ROUND_HALF_UP).toString();
        }
        return precio.setScale(decimales, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getPrecioTotalPantalla() {
        if (precioTotalPantalla != null) {
            return "$ " + precioTotalPantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private BigDecimal getImportePromocion() {
        if (isPromoMultipleAplicada()) {
            return getPromocionLinea().getImportePromocion();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getImporteTotalPromocion() {
        if (isPromoMultipleAplicada()) {
            return getPromocionLinea().getImporteTotalPromocion();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getImporteTotalAhorro() {
        if (isPromoMultipleAplicada()) {
            return getPromocionLinea().getImporteTotalAhorro();
        }
        return BigDecimal.ZERO;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getDescuentoScale() {
        return descuento.setScale(4, RoundingMode.HALF_UP);
    }

    public Integer getIdlinea() {
        return idlinea;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setIdlinea(Integer idlinea) {
        this.idlinea = idlinea;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public PromocionLineaTicket getPromocionLinea() {
        return promocionLinea;
    }

    public void setPromocionLinea(PromocionLineaTicket promocionLinea) {
        this.promocionLinea = promocionLinea;
        if (promocionLinea != null) {
            this.promoMultipleAplicada = promocionLinea.isTipoLineaMultiple();
            this.promoUnitariaAplicada = promocionLinea.isTipoLineaUnitaria();
        } else {
            this.promoUnitariaAplicada = false;
            this.promoMultipleAplicada = false;
        }
    }

    public DatosAdicionalesLineaTicket getDatosAdicionales() {
        return datosAdicionales;
    }

    public void setDatosAdicionales(DatosAdicionalesLineaTicket datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    public DescuentoTicket getImpresionLineaDescuento() {
        return impresionLineaDescuento;
    }

    public void setImpresionLineaDescuento(String impresionLineaDescuento) {
        this.impresionLineaDescuento = new DescuentoTicket(impresionLineaDescuento, descuentoImporteTotal, descuentoImporte);
    }

    public String getDescripcionAdicional() {
        return descripcionAdicional;
    }

    public void setDescripcionAdicional(String descripcionAdicional) {
        this.descripcionAdicional = descripcionAdicional;
    }

    public BigDecimal getImporteFinalPagado() {
        return importeFinalPagado;
    }

    public void setImporteFinalPagado(BigDecimal importeFinalPagado) {
        this.importeFinalPagado = importeFinalPagado;
    }

    public BigDecimal getImporteFinalPagadoPantalla() {
        return importeFinalPagado.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getImporteFinalPagadoPantallaMenosMediosPago() {
        return importeTotalFinalMenosDescuentosMedioPagos.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDescuentoPrecio() {
        return descuentoPrecio;
    }

    public void setDescuentoPrecio(BigDecimal descuentoPrecio) {
        this.descuentoPrecio = descuentoPrecio;
    }

    public BigDecimal getDescuentoImporte() {
        return descuentoImporte;
    }

    public void setDescuentoImporte(BigDecimal descuentoImporte) {
        this.descuentoImporte = descuentoImporte;
    }

    public BigDecimal getDescuentoImporteTotal() {
        return descuentoImporteTotal;
    }

    public void setDescuentoImporteTotal(BigDecimal descuentoImporteTotal) {
        this.descuentoImporteTotal = descuentoImporteTotal;
    }

    public BigDecimal getPrecioTotalSinRedondear() {
        return precioTotalSinRedondear;
    }

    public void setPrecioTotalSinRedondear(BigDecimal precioTotalSinRedondear) {
        this.precioTotalSinRedondear = precioTotalSinRedondear;
    }

    public BigDecimal getImporteTotalFinalPagadoSinRedondear() {
        return importeTotalFinalPagadoSinRedondear;
    }

    public void setImporteTotalFinalPagadoSinRedondear(BigDecimal importeTotalFinalPagadoSinRedondear) {
        this.importeTotalFinalPagadoSinRedondear = importeTotalFinalPagadoSinRedondear;
    }

    public void setImportePantalla(BigDecimal importePantalla) {
        this.importePantalla = importePantalla;
    }

    public void setImporteTotalPantalla(BigDecimal importeTotalPantalla) {
        this.importeTotalPantalla = importeTotalPantalla;
    }

    public void setPrecioPantalla(BigDecimal precioPantalla) {
        this.precioPantalla = precioPantalla;
    }

    public void setPrecioTotalPantalla(BigDecimal precioTotalPantalla) {
        this.precioTotalPantalla = precioTotalPantalla;
    }

    public BigDecimal getImporteTotalFinalTrasPromocionSubtotales() {
        return importeTotalFinalTrasPromocionSubtotales;
    }

    public void setImporteTotalFinalTrasPromocionSubtotales(BigDecimal importeTotalFinalTrasPromocionSubtotales) {
        this.importeTotalFinalTrasPromocionSubtotales = importeTotalFinalTrasPromocionSubtotales;
    }

    public BigDecimal getImporteTotalFinalMenosDescuentosMedioPagos() {
        return importeTotalFinalMenosDescuentosMedioPagos;
    }

    public void setImporteTotalFinalMenosDescuentosMedioPagos(BigDecimal importeTotalFinalMenosDescuentosMedioPagos) {
        this.importeTotalFinalMenosDescuentosMedioPagos = importeTotalFinalMenosDescuentosMedioPagos;
    }

    public boolean isDevolucion() {
        return devolucion;
    }

    public void setDevolucion(boolean devolucion) {
        this.devolucion = devolucion;
    }

    public BigDecimal getImporteTotalFinalMenosPromocionCabeceraYMedioPago() {
        return importeTotalFinalMenosPromocionCabeceraYMedioPago.subtract(impuestos).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getImporteTotalFinalMenosPromocionCabeceraYMedioPagoGetter() {
        return importeTotalFinalMenosPromocionCabeceraYMedioPago;
    }

    public void setImporteTotalFinalMenosPromocionCabeceraYMedioPago(BigDecimal importeTotalFinalMenosPromocionCabeceraYMedioPago) {
        this.importeTotalFinalMenosPromocionCabeceraYMedioPago = importeTotalFinalMenosPromocionCabeceraYMedioPago;
    }

    public BigDecimal getImporteTotalFinalPagado() {
        return importeTotalFinalPagado;
    }

    public void setImporteTotalFinalPagado(BigDecimal importeTotalFinalPagado) {
        this.importeTotalFinalPagado = importeTotalFinalPagado;
    }

    public BigDecimal getPrecioTarifaOrigen() {
        return precioTarifaOrigen;
    }

    public BigDecimal getPrecioTarifaOrigenPantalla() {
        return precioTarifaOrigen.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrecioTarifaOrigen(BigDecimal precioTarifaOrigen) {
        this.precioTarifaOrigen = precioTarifaOrigen;
    }

    public BigDecimal getPrecioTotalTarifaOrigen() {
        return precioTotalTarifaOrigen;
    }

    public void setPrecioTotalTarifaOrigen(BigDecimal precioTotalTarifaOrigen) {
        this.precioTotalTarifaOrigen = precioTotalTarifaOrigen;
    }

    public boolean isDescuentoAplicado() {
        if (this.descuento == null) {
            return false;
        }
        return this.descuento.compareTo(BigDecimal.ZERO) != 0;
    }

    public BigDecimal getDescuentoPrecioTotal() {
        return descuentoPrecioTotal;
    }

    public BigDecimal getPrecioUnitario() {
        return precio.divide(new BigDecimal(cantidad), 2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getPrecioTotalUnitario() {
        return precioTotal.divide(new BigDecimal(cantidad), 2, RoundingMode.HALF_DOWN);
    }

    public boolean tieneGarantiaOriginal() {
        return articulo.isGarantiaOriginal();
    }

    public boolean tieneKitInstalacion() {
        return articulo.isKitInstalacion();
    }

    public Character getEnvioEnGuiaRemision() {
        return this.envioEnGuiaRemision;
    }

    public void setEnvioEnGuiaRemision(Character envioEnGuiaRemision) {
        this.envioEnGuiaRemision = envioEnGuiaRemision;
    }

    public boolean isEnvioEnGuiaRemision() {
        return this.envioEnGuiaRemision != null && this.envioEnGuiaRemision == 'S';
    }

    public GarantiaReferencia getReferenciaGarantia() {
        return referenciaGarantia;
    }

    public void setReferenciaGarantia(GarantiaReferencia referenciaGarantia) {
        this.referenciaGarantia = referenciaGarantia;
    }

    public KitReferencia getReferenciaKit() {
        return referenciaKit;
    }

    public void setReferenciaKit(KitReferencia referenciaKit) {
        this.referenciaKit = referenciaKit;
    }

    public boolean isEnvioDomicilio() {
        return (this.datosAdicionales != null && this.datosAdicionales.isEnvioDomicilio());
    }

    public boolean isPendienteEntrega() {
        return (this.isEnvioDomicilio() || isRecogidaPosterior());
    }

    public boolean isRecogidaPosterior() {
        return (this.datosAdicionales != null && this.datosAdicionales.isRecogidaPosterior());
    }

    public boolean isIntercambio() {
        return (this.datosAdicionales != null && this.datosAdicionales.isIntercambio());
    }

    public LineaTicketOrigen getLineaTicketOrigen() {
        return lineaTicketOrigen;
    }

    public void setLineaTicketOrigen(LineaTicketOrigen lineaTicketOrigen) {
        this.lineaTicketOrigen = lineaTicketOrigen;
    }

    public int getCanjeoPuntosCantidadAceptada() {
        return canjeoPuntosCantidadAceptada;
    }

    public void incrementeCanjeoPuntosCantidadAceptada() {
        this.canjeoPuntosCantidadAceptada++;
    }

    public void resetCanjeoPuntosCantidadAceptada() {
        this.canjeoPuntosCantidadAceptada = 0;
    }

    public boolean isCanjeoPuntosCantidadCompleta() {
        return canjeoPuntosCantidadAceptada == cantidad;
    }

    @Override
    public String toString() {
        return getCodigoBarras() + " - " + getArticulo().getDesart();
    }

    // Importe final pagado / artículos de la línea
    public BigDecimal getImporteUnitario() {
        // Ponemos rounding mode UP para que el cliente no se crea engañado si el redondeo diese una cifra que multiplicada
        // por el número de artículos, sea menor de lo indicado como importe unificado.
        return getImporteFinalPagado().divide(new BigDecimal(cantidad)).setScale(2, RoundingMode.UP);
    }

    public BigDecimal getDescuentoFinal() {
        return descuentoFinal;
    }

    public void setDescuentoFinal(BigDecimal descuentoFinal) {
        this.descuentoFinal = descuentoFinal;
    }

    public BigDecimal getDescuentoFinalDev() {
        return descuentoFinalDev;
    }

    public BigDecimal getDescuentoFinalDevTicket() {
        return descuentoFinalDev.setScale(2, RoundingMode.HALF_DOWN);
    }

    public void setDescuentoFinalDev(BigDecimal descuentoFinalDev) {
        this.descuentoFinalDev = descuentoFinalDev;
    }

    public void addSukuponEmitido(SukuponLinea cupon) {
        if (sukuponesEmitidos == null) {
            sukuponesEmitidos = new ArrayList<SukuponLinea>();
        }
        sukuponesEmitidos.add(cupon);
    }

    public List<SukuponLinea> getSukuponesEmitidos() {
        return sukuponesEmitidos;
    }

    public boolean tieneSukuponesEmitidos() {
        return sukuponesEmitidos != null && !sukuponesEmitidos.isEmpty();
    }

    public String getCodimp() {
        return codimp;
    }

    public void setCodimp(String codimp) {
        this.codimp = codimp;
    }

    boolean isGarantiaExtendida() {
        return getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS));
    }

    public BigDecimal getDescuentosAcumulados() {
        return descuentosAcumulados;
    }

    public BigDecimal getDescuentosAcumuladosString() {
        return descuentosAcumulados.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDescuentosAcumuladosMenosMedioPagoString() {
        BigDecimal res = descuentosAcumulados.setScale(2, RoundingMode.HALF_UP);
        if (descuentosMedioPagos != null) {
            res = res.subtract(descuentosMedioPagos);
        }
        return res;
    }

    public BigDecimal getDescuentosAcumuladosMenosCabeceraYMedioPagoString() {
        BigDecimal res = descuentosAcumulados.setScale(2, RoundingMode.HALF_UP);
        if (descuentosMedioPagos != null) {
            res = res.subtract(descuentosMedioPagos);
            res = res.subtract(descuentosSubtotales);
        }
        return res;
    }

    public void setDescuentosAcumulados(BigDecimal descuentosAcumulados) {
        this.descuentosAcumulados = descuentosAcumulados;
    }

    public int getLineaOriginal() {
        return lineaOriginal;
    }

    public void setLineaOriginal(int lineaOriginal) {
        this.lineaOriginal = lineaOriginal;
    }

    public BigDecimal getDescuentosPromocionesLineas() {
        return descuentosPromocionesLineas;
    }

    public void setDescuentosPromocionesLineas(BigDecimal descuentosPromocionesLineas) {
        this.descuentosPromocionesLineas = descuentosPromocionesLineas;
    }

    public BigDecimal getDescuentosSubtotales() {
        return descuentosSubtotales;
    }

    public void setDescuentosSubtotales(BigDecimal descuentosSubtotales) {
        this.descuentosSubtotales = descuentosSubtotales;
    }

    public BigDecimal getDescuentosMedioPagos() {
        return descuentosMedioPagos;
    }

    public void setDescuentosMedioPagos(BigDecimal descuentosMedioPagos) {
        this.descuentosMedioPagos = descuentosMedioPagos;
    }

    public BigDecimal getPorcentajeDescuentosPromocionesLineas() {
        return porcentajeDescuentosPromocionesLineas;
    }

    public void setPorcentajeDescuentosPromocionesLineas(BigDecimal porcentajeDescuentosPromocionesLineas) {
        this.porcentajeDescuentosPromocionesLineas = porcentajeDescuentosPromocionesLineas;
    }

    public BigDecimal getPorcentajeDescuentosSubtotales() {
        return porcentajeDescuentosSubtotales;
    }

    public void setPorcentajeDescuentosSubtotales(BigDecimal porcentajeDescuentosSubtotales) {
        this.porcentajeDescuentosSubtotales = porcentajeDescuentosSubtotales;
    }

    public BigDecimal getPorcentajeDescuentosMedioPagos() {
        return porcentajeDescuentosMedioPagos;
    }

    public void setPorcentajeDescuentosMedioPagos(BigDecimal porcentajeDescuentosMedioPagos) {
        this.porcentajeDescuentosMedioPagos = porcentajeDescuentosMedioPagos;
    }

    public BigDecimal getImpuestosFinalR() {
        return Numero.redondear(getImporteTotalFinalPagado().subtract(getImporteFinalPagado()));
    }

    public BigDecimal getImporteDescuentoFinal() {
        if (this.getArticulo().getPmp() == null) {
            return Numero.redondear(this.getPrecioTarifaOrigen().setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(cantidad)).subtract(this.getImporteFinalPagado()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean isGarantiaGratuita() {
        return garantiaGratuita;
    }

    public void setGarantiaGratuita(boolean garantiaGratuita) {
        this.garantiaGratuita = garantiaGratuita;
    }

    public boolean isLineaFacturacion() {
        return lineaFacturacion;
    }

    public void setLineaFacturacion(boolean lineaFacturacion) {
        this.lineaFacturacion = lineaFacturacion;
    }

    public BigDecimal getImporteFinalPagadoSinRedondear() {
        return importeFinalPagadoSinRedondear;
    }

    public void setImporteFinalPagadoSinRedondear(BigDecimal importeFinalPagadoSinRedondear) {
        this.importeFinalPagadoSinRedondear = importeFinalPagadoSinRedondear;
    }

    public BigDecimal getCompensacionLinea() {
        return compensacionLinea;
    }

    public void setCompensacionLinea(BigDecimal compensacionLinea) {
        this.compensacionLinea = compensacionLinea;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(String codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public BigDecimal getCostoLanded() {
        return costoLanded;
    }

    public void setCostoLanded(BigDecimal costoLanded) {
        this.costoLanded = costoLanded;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public BigDecimal getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(BigDecimal precioReal) {
        this.precioReal = precioReal;
    }

    public List<PromocionLineaTicket> getPromocionLineaList() {
        return promocionLineaList;
    }

    public void setPromocionLineaList(List<PromocionLineaTicket> promocionLineaList) {
        this.promocionLineaList = promocionLineaList;
    }

    public boolean isArticuloSeleccionado() {
        return articuloSeleccionado;
    }

    public void setArticuloSeleccionado(boolean articuloSeleccionado) {
        this.articuloSeleccionado = articuloSeleccionado;
    }

    public boolean isPedidoFacturado() {
        return pedidoFacturado;
    }

    public void setPedidoFacturado(boolean pedidoFacturado) {
        this.pedidoFacturado = pedidoFacturado;
    }

    public String getObservacionPedidoFacturado() {
        return observacionPedidoFacturado;
    }

    public void setObservacionPedidoFacturado(String observacionPedidoFacturado) {
        this.observacionPedidoFacturado = observacionPedidoFacturado;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public Integer getStockDisponibleBodega() {
        return stockDisponibleBodega;
    }

    public void setStockDisponibleBodega(Integer stockDisponibleBodega) {
        this.stockDisponibleBodega = stockDisponibleBodega;
    }

    public Integer getStockDisponibleLocales() {
        return stockDisponibleLocales;
    }

    public void setStockDisponibleLocales(Integer stockDisponibleLocales) {
        this.stockDisponibleLocales = stockDisponibleLocales;
    }
    
    public BigDecimal getImpuestosIce() {
        return impuestosIce;
    }

    public void setImpuestosIce(BigDecimal impuestosIce) {
        this.impuestosIce = impuestosIce;
    }

    public boolean isPmpArticulo() {
        return pmpArticulo;
    }

    public void setPmpArticulo(boolean pmpArticulo) {
        this.pmpArticulo = pmpArticulo;
    }

    public BigDecimal getDescuentoFinalFunda() {
        return descuentoFinalFunda;
    }

    public void setDescuentoFinalFunda(BigDecimal descuentoFinalFunda) {
        this.descuentoFinalFunda = descuentoFinalFunda;
    }

    public BigDecimal getImporteTotalFinal() {
        return importeTotalFinal;
    }

    public void setImporteTotalFinal(BigDecimal importeTotalFinal) {
        this.importeTotalFinal = importeTotalFinal;
    }

    public List<PromoMedioPago> getListaPromocion() {
        return listaPromocion;
    }

    public void setListaPromocion(List<PromoMedioPago> listaPromocion) {
        this.listaPromocion = listaPromocion;
    }

    public Long getItemBase() {
        return itemBase;
    }

    public void setItemBase(Long itemBase) {
        this.itemBase = itemBase;
    }

    public String getDesArticuloBase() {
        return desArticuloBase;
    }

    public void setDesArticuloBase(String desArticuloBase) {
        this.desArticuloBase = desArticuloBase;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public BigDecimal getImporteTotalFinalMenosPromocionCabeceraYMedioPagoSinImpuesto() {
        return importeTotalFinalMenosPromocionCabeceraYMedioPago.divide(new BigDecimal("1.12"), BigDecimal.ROUND_HALF_DOWN).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isHayPromocionesSinImpuesto() {
        return (importeTotalFinalMenosPromocionCabeceraYMedioPago.divide(new BigDecimal("1.12"), BigDecimal.ROUND_HALF_DOWN).setScale(2, RoundingMode.HALF_UP) != null);
    }

    public Boolean getKitObligatorio() {
        return kitObligatorio;
    }

    public void setKitObligatorio(Boolean kitObligatorio) {
        this.kitObligatorio = kitObligatorio;
    }

    public String getKitReferenciaOrigen() {
        return kitReferenciaOrigen;
    }

    public void setKitReferenciaOrigen(String kitReferenciaOrigen) {
        this.kitReferenciaOrigen = kitReferenciaOrigen;
    }

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

    public boolean isEntregaDomicilio() {
        return entregaDomicilio;
    }

    public void setEntregaDomicilio(boolean entregaDomicilio) {
        this.entregaDomicilio = entregaDomicilio;
    }

    /**
     * @return the interes
     */
    public BigDecimal getInteres() {
        if(interes == null){
            return BigDecimal.ZERO;
        }

        return interes;
    }

    /**
     * @param interes the interes to set
     */
    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

}
