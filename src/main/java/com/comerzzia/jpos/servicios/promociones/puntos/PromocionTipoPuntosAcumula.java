/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.puntos;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.services.ParStringBigDecimal;
import com.comerzzia.jpos.entity.services.ParStringInteger;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoPuntosAcumula extends Promocion {

    BigDecimal montoMinimo;
    Integer puntos;
    BigDecimal importe;
    List<ParStringBigDecimal> adicionalesMedioPago;
    List<ParStringInteger> adicionalesAfiliados;

    public PromocionTipoPuntosAcumula(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {

        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        montoMinimo = detalleXML.getNodo("montoMinimo").getValueAsBigDecimal();
        puntos = detalleXML.getNodo("puntos").getValueAsInteger();
        importe = detalleXML.getNodo("importe").getValueAsBigDecimal();

        adicionalesMedioPago = new ArrayList<ParStringBigDecimal>();
        adicionalesAfiliados = new ArrayList<ParStringInteger>();

        if (detalleXML.getNodo("puntosMedioPago", true) != null) {
            List<XMLDocumentNode> puntosMedioPago = detalleXML.getNodo("puntosMedioPago").getHijos();
            for (XMLDocumentNode puntoMedioPago : puntosMedioPago) {
                ParStringBigDecimal par = new ParStringBigDecimal(
                        puntoMedioPago.getNodo("codMedioPago").getValue(),
                        puntoMedioPago.getNodo("puntos").getValueAsBigDecimal());
                adicionalesMedioPago.add(par);
            }
        }

        List<XMLDocumentNode> puntosAfiliados = detalleXML.getNodo("afiliados").getHijos();
        for (XMLDocumentNode puntoAfiliados : puntosAfiliados) {
            ParStringInteger par = new ParStringInteger(
                    puntoAfiliados.getNodo("codTipoAfiliado").getValue(),
                    puntoAfiliados.getNodo("puntos").getValueAsInteger());
            adicionalesAfiliados.add(par);
        }

        seleccion = new SeleccionArticuloBean(detalleXML.getNodo("impresion").getNodo("parametros"));

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
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }

    
    public void compruebaPosibleAcumulacionPuntos(TicketS ticket){
        if (!tieneFiltroPagos()){
            return;
        }
        if (getAplicableMonto(ticket) == null){
            return;
        }
        String msg = "El cliente acumulará puntos si su forma de pago es: \n\n" + getFiltroPagosDescripcion();
        int altura = (15 * msg.split("\n").length);
        JPrincipal.getInstance().crearVentanaInformacion(msg, altura);
    }
    
    public BigDecimal getAplicableMonto(TicketS ticket){
        BigDecimal importeAplicable = seleccion.getImporteAplicable(ticket.getLineas(), true, new ArrayList<LineaTicket>());
        if (importeAplicable == null){
            importeAplicable = ticket.getTotales().getTotalPagado();
        }

        // si no supera monto mínimo no aplicamos promoción
        if (Numero.isMenor(importeAplicable, montoMinimo)) {
            log.debug(this + " - No acumula puntos porque el importeAplicable " + importeAplicable + " no supera monto mínimo: "+montoMinimo);
            return null;
        }
        return importeAplicable;
    }
    
    public String isAplicableMontoMinimo(TicketS ticket){
        log.debug(this + " - Comprobando si se acumulan puntos");
        BigDecimal importeAplicable = seleccion.getImporteAplicable(ticket.getLineas(), true, new ArrayList<LineaTicket>());
        if (importeAplicable == null){
            importeAplicable = ticket.getTotales().getTotalPagado();
        }

        if (Numero.isIgualACero(importeAplicable)) {
            log.debug(this + " - No se van a acumular puntos porque no se lleva ningún artículo de los configurados");
            return null;
        }              
       
        //Comprobamos formas de pago
        /*if(!this.isAplicableAPagos(ticket.getPagos().getPagos())){
            return getMensajeNoAplicablePagos();
        }*/        
        
        // si no supera monto mínimo no vamos a aplicar promoción
        if (Numero.isMenor(importeAplicable, montoMinimo)) {
            log.debug(this + " - El monto mínimo no permite que se vayan a acumular puntos: importeAplicable: "+importeAplicable+", montoMinimo: " + montoMinimo);
            return "El monto mínimo no permite acumular puntos ";
        }
        
        return null;
    }    
    
    public Integer getPuntos(TicketS ticket) {
        BigDecimal importeAplicable = getAplicableMonto(ticket);
        if (importeAplicable == null){
            return 0;
        }

        Integer numIntervalos = importeAplicable.divide(importe, RoundingMode.HALF_UP).intValue();

        Integer puntosSeleccion = getSeleccion().getCantidadAplicableSuma(false, ticket.getLineas(), new ArrayList<LineaTicket>());
        if (puntosSeleccion == -1) { // las lineas del ticket no permiten la aplicación de la promoción
            log.debug(toString() + " La promoción acumula cero puntos porque no cumple los criterios de selección de artículos.");
            return 0;
        }
        if (!isAplicableAPagos(ticket.getPagos().getPagos())){
            if (!ticket.isFinalizado()){
                JPrincipal.getInstance().crearInformacion("No se han seleccionado los medios de pago necesarios para aplicar la promoción de acumulación de puntos.");
                log.debug(toString() + " La promoción acumula cero puntos porque no cumple los criterios de selección de pagos.");
            }
            return 0;
        }
        Integer puntosAfiliado = 0;
        if (ticket.getCliente().isSocio()) {
            for (ParStringInteger adicionalAfiliado : adicionalesAfiliados) {
                if (adicionalAfiliado.getValorString().equals(ticket.getCliente().getTipoAfiliado())) {
                    puntosAfiliado = adicionalAfiliado.getValorInt();
                    break;
                }
            }
        }
        
        Integer puntosMediosPago = 0;
        for (ParStringBigDecimal adicionalMedioPago : adicionalesMedioPago) {
            Pago pago = ticket.getPagos().getMedioPago(adicionalMedioPago.getValorString());
            if (pago == null){ // no se ha utilizado este medio de pago
                continue;
            }
            BigDecimal porcentaje = pago.getPorcentaje(importeAplicable);
            puntosMediosPago += Numero.porcentajeR(adicionalMedioPago.getValorBigDecimal(), porcentaje).intValue();
        }
        
        Integer puntosIntervalo = puntos * numIntervalos;
        if (puntosIntervalo == 0){
            puntosIntervalo = 1;
        }

        Integer puntosTotales = puntosIntervalo + puntosSeleccion + puntosMediosPago + puntosAfiliado;
        
        log.debug(toString() + " La promoción acumula: Parte1: " + puntosIntervalo + " puntos por monto + Por tipo de afiliado: " + puntosAfiliado + " puntos.");
        log.debug(toString() + " La promoción acumula: Parte2: " + puntosSeleccion + " puntos por artículos comprados.");
        log.debug(toString() + " La promoción acumula: Parte3: " + puntosMediosPago + " puntos por formas de pago.");
        log.debug(toString() + " La promoción acumula: Total: " + puntosTotales);

        return puntosTotales;
    }

}
