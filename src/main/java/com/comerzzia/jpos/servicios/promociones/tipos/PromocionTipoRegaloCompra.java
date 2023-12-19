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
import com.comerzzia.jpos.servicios.promociones.articulos.ArticuloPromociones;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromocionesFiltrosPagos;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author AMS
 */
public class PromocionTipoRegaloCompra extends Promocion {

    private String mensajeRegalo;
    private String mensajeRegaloCompleto;
    private BigDecimal montoMinimoCompra;
    private List<ArticuloPromociones> articulosRegalo;
    private List<String> clientes;

    public PromocionTipoRegaloCompra(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode clientesVentaHistorico = xml.getNodo("cabecera").getNodo("clientes", true);
        if (clientesVentaHistorico != null) {
            String cadenaClientes = clientesVentaHistorico.getValue();
            if (cadenaClientes != null && !cadenaClientes.isEmpty()) {
                String[] array = cadenaClientes.split(";");
                clientes = Arrays.asList(array);
            }
        }

        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        mensajeRegalo = detalleXML.getNodo("mensajeRegalo", false).getValue();
        mensajeRegaloCompleto = mensajeRegalo + ":\n\n";
        montoMinimoCompra = detalleXML.getNodo("montoMinimoCompra", true).getValueAsBigDecimal();
        List<XMLDocumentNode> articulosXML = detalleXML.getNodo("articulosRegalo", true).getHijos();
        if (articulosXML != null) {
            articulosRegalo = new ArrayList<ArticuloPromociones>();
            for (XMLDocumentNode articulo : articulosXML) {

                String codArticulo = articulo.getNodo("codArticulo").getValue();
                String desArticulo = articulo.getNodo("desArticulo").getValue();
                String desMarca = articulo.getNodo("desMarca").getValue();
                BigDecimal descuento = articulo.getNodo("descuento").getValueAsBigDecimal();

                ArticuloPromociones art = new ArticuloPromociones();
                art.setCodArticulo(codArticulo);
                art.setDesArticulo(desArticulo);
                art.setDesMarca(desMarca);
                art.setDescuento(Numero.redondear(descuento));

                articulosRegalo.add(art);
                
                mensajeRegaloCompleto +=  "  - " + art.getCodArticulo() + " " + art.getDesArticulo() + "\n";

            }
        }

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
        log.debug("Intentando aplicar promoción regalo artículo... " + toString());
        TicketS ticket = lineas.getTicket();
        if (ticket == null) {
            log.warn(this+":: ATENCIÓN: El ticket asociado a las líneas es NULL. No se aplicó promoción Regalo Artículo.");
            return;
        }
        if(ticket.getTicketPromociones().getPromocionesFiltrosPagos().isRechazadaParaSiempre(getIdPromocion())){
            log.debug(this+":: Promoción no aplicada por haber sido ya rechazada por el cliente al no cumplir el monto mínimo");
            return;
        }
        if (tieneFiltroPagos()
                && ticket.getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(getIdPromocion())) {
            log.debug(this+":: Promoción no aplicada por haber sido ya rechazada por el cliente anteriormente.");
            return;
        }

        // comprobamos compra histórica
        if (clientes != null) {
            if (clientes.isEmpty()) {
                log.debug(this+":: Promoción no aplicada porque ningún cliente cumple requisito de compra histórica.");
                return;
            }
            if (ticket.getCliente().isClienteGenerico()) {
                log.debug(this+":: Promoción no aplicada porque requiere compra histórica del cliente, y en este caso no aplica para Consumidor Final.");
                return;
            }
            if (!clientes.contains(ticket.getCliente().getCodcli())) {
                log.debug(this+":: Promoción no aplicada porque requiere compra histórica del cliente, y no la cumple.");
                return;
            }
        }

        BigDecimal ahorro = BigDecimal.ZERO;
        BigDecimal ahorroTotal = BigDecimal.ZERO;
        List<LineaTicket> lineasAccesibles = new ArrayList<LineaTicket>();
        List<LineaTicket> lineasAplicadas = new ArrayList<LineaTicket>();
        List<String> printPromocion = new ArrayList<String>();
        // Recorremos los artículos que regalamos para ver si hay alguno incluido en la factura.
        for (ArticuloPromociones articulo : articulosRegalo) {
            lineasAccesibles.clear();
            int cant = lineas.getContains(true, articulo.getCodArticulo(), lineasAccesibles);
            // si el artículo no está incluido en la factura, mostramos mensaje al cajero
            if (cant == 0) {
                ticket.getTicketPromociones().addMensajePromocion(getIdPromocion(), mensajeRegaloCompleto);
                log.debug(this + ":: No se incluye en la factura el artículo de regalo. Mostraremos aviso al cajero.");
                continue;
            }
            // quitamos mensaje aviso si ya estaba incluido
            ticket.getTicketPromociones().removeMensajePromocion(getIdPromocion());

            // asumimos que sólo vamos a regalar una unidad de cada artículo
            LineaTicket lineaAccesible = lineasAccesibles.get(0);
            PromocionLineaTicket promocionLinea = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_LINEA_MULTIPLE);
            promocionLinea.setTextoPromocion(getDescripcionImpresion());
            promocionLinea.setPrecioTarifa(lineaAccesible.getPrecioTarifaOrigen());
            promocionLinea.setPrecioTarifaTotal(lineaAccesible.getPrecioTotalTarifaOrigen());
            promocionLinea.setCantidadPromocion(1);
            promocionLinea.calculaAhorroDtoPorcentaje(articulo.getDescuento());
            lineaAccesible.setPromocionLinea(promocionLinea);
            lineasAplicadas.add(lineaAccesible);
            ahorroTotal = ahorroTotal.add(promocionLinea.getImporteTotalAhorro());
            ahorro = ahorro.add(promocionLinea.getImporteAhorro());
            printPromocion.add("El artículo " + lineaAccesible.getArticulo().getCodart()
                    + " " + lineaAccesible.getArticulo().getDesart()
                    + " tiene un " + articulo.getDescuento() + "% de descuento por la aplicación de la promoción "
                    + getDescripcionImpresion());
        }



        // si logramos aplicar descuento a algún artículo, añadimos línea descuento
        if (ahorroTotal.compareTo(BigDecimal.ZERO) > 0) {
            // si podemos aplicar la promoción, antes comprobamos medios de pago
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
            if (!aceptada) {
                for (LineaTicket linea : lineasAplicadas) {
                    linea.setPromocionLinea(null);
                }
                return;
            } else { */
                ticket.getTicketPromociones().getPromocionesFiltrosPagos().addPromocionesValidaMontoMinimo(this);
            //}
            DescuentoTicket descuentoTicket = new DescuentoTicket();
            descuentoTicket.setDescripcion(getDescripcionImpresion());
            descuentoTicket.setDescuento(ahorro);
            descuentoTicket.setDescuentoTotal(ahorroTotal);
            lineas.addLineaDescuentoFinal(descuentoTicket);
            ticket.getTicketPromociones().addClientePromoAplicada(getIdPromocion());
            ticket.getTicketPromociones().addPromocionPrint(printPromocion);
            log.debug(this + ":: Promoción aplicada.");
        }
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }

    public List<ArticuloPromociones> getArticulosRegalo() {
        return articulosRegalo;
    }

    public void setArticulosRegalo(List<ArticuloPromociones> articulosRegalo) {
        this.articulosRegalo = articulosRegalo;
    }

    public String getMensajeRegalo() {
        return mensajeRegalo;
    }

    public void setMensajeRegalo(String mensajeRegalo) {
        this.mensajeRegalo = mensajeRegalo;
    }

    @Override
    public boolean isReaplicable(TicketS ticket) {
        if (!tieneFiltroPagosTarjetaSukasa) {
            boolean aplicadaAnteriormente = ticket.getTicketPromociones().isPromocionAplicadaAnteriormente(getIdPromocion());
            if (aplicadaAnteriormente) {
                log.debug(this + ":: isReaplicable() -  No se aplica promoción REGALO ARTÍCULO porque ya fue aplicada para este cliente.");
            }
            return !aplicadaAnteriormente;
        }
        // si tiene filtro por tarjeta sukasa, tendremos que hacer la comprobación por número de crédito. 
        // esto lo comprobaremos después, cuando se incluyan los pagos
        ticket.getTicketPromociones().getPromocionesFiltrosPagos().addPromoAplicacionUnicaCredito(this);
        return true;
    }

    @Override
    public boolean isAplicableALineas(LineasTicket lineas) {
        BigDecimal montoAplicable = seleccion.getImporteAplicable(lineas, true, new ArrayList<LineaTicket>());
        if (montoAplicable == null) { // el tipo de selección es todos
            montoAplicable = lineas.getTicket().getTotales().getTotalAPagar();
        }

        List<LineaTicket> lineasAccesibles = new ArrayList<LineaTicket>();   
        List<LineaTicket> lineasRegalo = lineas.getLineasAplicables(false, this.seleccion); 
        BigDecimal sumaArticulosRegalo = BigDecimal.ZERO;
        for (ArticuloPromociones articulo : articulosRegalo) {
             lineasAccesibles.clear();
             int cant = lineas.getContains(true, articulo.getCodArticulo(), lineasAccesibles);
             // si el artículo no está incluido en la factura, mostramos mensaje al cajero
             if (cant == 0) {
                 continue;
             }
            for(LineaTicket linea:lineasRegalo){
                if(linea.getArticulo().getCodart().equals(articulo.getCodArticulo())){
                    sumaArticulosRegalo = sumaArticulosRegalo.add(linea.getImporteTotalFinalPagado());
                }
            }
        }     
        log.debug(this + ":: Hay "+sumaArticulosRegalo+" de artículos regalo, restando del montoAplicable: "+montoAplicable);
        montoAplicable = montoAplicable.subtract(sumaArticulosRegalo);
        
        if(Numero.isIgualACero(montoMinimoCompra) && Numero.isIgualACero(montoAplicable)){
            log.debug(this + ":: Promoción no aplicada porque no se lleva ninguno de los artículos de la condición, aunque monto mínimo aplicable sea 0.");
            return false;
        }
        
        if (Numero.isMayorOrIgual(montoAplicable, montoMinimoCompra)) {
            return true;
        }
        log.debug(this + ":: Promoción no aplicada porque no cumple monto mínimo. MontoAplicable: "+montoAplicable+" ,MontoMinimo: "+montoMinimoCompra);
        return false;

    }
    
    public boolean isAplicableMontoMinimo(LineasTicket lineas) {
        BigDecimal montoAplicable = seleccion.getImporteAplicable(lineas, true, new ArrayList<LineaTicket>());
        if (montoAplicable == null) { // el tipo de selección es todos
            montoAplicable = lineas.getTicket().getTotales().getTotalPagado();
        }
         
        //Comprobamos si alguna de las líneas que aplican en la promoción es alguna de las líneas de regalo
        List<LineaTicket> lineasRegalo = lineas.getLineasAplicables(false, this.seleccion);       
        BigDecimal sumaArticulosRegalo = BigDecimal.ZERO;
        for (ArticuloPromociones articulo : articulosRegalo) {
            for(LineaTicket linea:lineasRegalo){
                if(linea.getArticulo().getCodart().equals(articulo.getCodArticulo())){
                    sumaArticulosRegalo = sumaArticulosRegalo.add(linea.getImporteTotalFinalPagado());
                }
            }
        }     
        log.debug(this + " Hay "+sumaArticulosRegalo+" de artículos regalo, restando del montoAplicable: "+montoAplicable);
        montoAplicable = montoAplicable.subtract(sumaArticulosRegalo);        

        if(Numero.isIgualACero(montoMinimoCompra) && Numero.isIgualACero(montoAplicable)){
            log.debug(this + ":: Promoción no aplicada porque no se lleva ninguno de los artículos de la condición, aunque monto mínimo aplicable sea 0.");
            return false;
        }
                
        if (Numero.isMayorOrIgual(montoAplicable, montoMinimoCompra)) {
            return true;
        }
        log.debug(this + ":: Promoción no aplicada porque no cumple monto mínimo. MontoAplicable: "+montoAplicable+" ,MontoMinimo: "+montoMinimoCompra);
        return false;

    }
}
