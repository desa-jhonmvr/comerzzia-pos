/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.services.ParStringInteger;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author AMS
 */
public class PromocionTipoCuponRegaloProveedorExterno extends PromocionTipoCupon {

    private static final String CADENA_SIN_IMAGEN = "_";
    
    protected List<ParStringInteger> adicionalesMedioPago;
    protected String logo;
    

    public PromocionTipoCuponRegaloProveedorExterno(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos primero a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);

        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        adicionalesMedioPago = new ArrayList<ParStringInteger>();

        try {
            List<XMLDocumentNode> puntosMedioPago = detalleXML.getNodo("puntosMedioPago").getHijos();
            for (XMLDocumentNode puntoMedioPago : puntosMedioPago) {
                ParStringInteger par = new ParStringInteger(
                        puntoMedioPago.getNodo("codMedioPago").getValue(),
                        puntoMedioPago.getNodo("puntos").getValueAsInteger());
                adicionalesMedioPago.add(par);
            }
            // ordenamos los medios de pago de mayor a menor según los puntos otorgados por cada uno
            Collections.sort(adicionalesMedioPago);
        }
        catch (Exception e) {
            // puede que no tengamos el tag. Lo ignoramos
        }
        
        // intentamos obtener el logo del cupón
        try{
            XMLDocumentNode parametrosImpresionXML = detalleXML.getNodo("impresion").getNodo("parametros");
            logo = parametrosImpresionXML.getNodo("codLogo").getValue();
            if (logo.equals(CADENA_SIN_IMAGEN)){
                logo = null;
            }
        }
        catch (Exception e){
           log.error("parsearXMLDatosPromocion() - "+this+" No se pudo leer la información del logo"); 
           logo = null;
        }       
    }

    @Override
    public List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision) {
        log.debug(this + " - Intentando emitir cupones de REGALO PROVEEDOR EXTERNO...");

        BigDecimal montoPromocional = ticket.getTotales().getTotalPagado();

        log.debug(this + " - Monto promocional:: " + montoPromocional);

        // comprobamos monto mínimo
        if (Numero.isMenor(montoPromocional, configEmision.getMontoMinimo())) {
            log.debug(this + " - El monto mínimo no permite emitir cupones: montoPromocional: "+montoPromocional+", montoMinimo: " + configEmision.getMontoMinimo());
            return null;
        }
        if (Numero.isIgualACero(montoPromocional)) {
            log.debug(this + " - No se emiten cupones porque no se lleva ningún artículo de los configurados, a pesar de que monto mínimo sea 0.");
            return null;
        }

        // calculamos número de cupones (PRIMERA PARTE)
        Integer numCuponesParte2 = 0;
        Integer numCuponesParte1 = configEmision.getNumCupones();
        Integer intervaloImporte = configEmision.getCantidad();

        if (intervaloImporte == null) {
            log.debug(toString() + " Número de cupones calculados de la PARTE 1: " + numCuponesParte1);
            log.debug(toString() + " Configuración POR FACTURA. No se emiten cupones adicionales en PARTE2. ");
        }
        else{
            numCuponesParte1 = montoPromocional.divide(new BigDecimal(intervaloImporte), RoundingMode.HALF_DOWN).intValue();
            log.debug(toString() + " Número de cupones calculados de la PARTE 1: " + numCuponesParte1);
            // comprobamos si la promoción se puede aplicar según las lineas compradas en el ticket y calculamos cuantos (SEGUNDA PARTE)
            numCuponesParte2 = getSeleccion().getCantidadProporcionalAplicable(ticket.getLineas(), new BigDecimal(intervaloImporte), new ArrayList<LineaTicket>());
            log.debug(toString() + " Multiplicador de cupones calculados de la PARTE 2: " + numCuponesParte2);
        }

        // calculamos cupones adicionales por medios de pago (TERCERA PARTE)
        Integer numCuponesParte3 = 1;
        for (ParStringInteger adicionalMedioPago : adicionalesMedioPago) {
            Pago pago = ticket.getPagos().getMedioPago(adicionalMedioPago.getValorString());
            if (pago != null) {
                numCuponesParte3 *= adicionalMedioPago.getValorInt();
                log.debug(toString() + " -  Número de cupones adicionales por " + pago.getMedioPagoActivo().getDesMedioPago() + ":  " + adicionalMedioPago.getValorInt());
            }
        }
        
        Integer numCuponesFinal = numCuponesParte1 * numCuponesParte2 * numCuponesParte3;
        
        log.debug(toString() + " -  La promoción acumula: Parte1: " + numCuponesParte1 + " cupones.");
        log.debug(toString() + " -  La promoción acumula: Parte2: " + numCuponesParte2 + " cupones.");
        log.debug(toString() + " -  La promoción acumula: Parte3: " + numCuponesParte3 + " cupones.");
        log.debug(this + " - La promoción acumula: Total: " + numCuponesFinal);

        if (numCuponesFinal==0){
            return null;
        }

        List<Cupon> resultado = new ArrayList<Cupon>();
        for (int i = 0; i < numCuponesFinal; i++) {
            Cupon cupon = new Cupon(ticket, this);
            cupon.setLogo(logo);
            cupon.setTextoAdicional("Código cupón: " + configEmision.getCuponProveedorExterno());
            cupon.setVariable(configEmision.getCuponProveedorExterno().toString());
            resultado.add(cupon);
        }
        ticket.getTicketPromociones().addPromocionCuponesPagosPrint("* Recibe " + numCuponesFinal + " cupon(es) sorteo " + getDescripcionImpresion());
        return resultado;
    }

    public String isAplicableMontoMinimo(TicketS ticket){
        log.debug(this + " - Intentando saber si se van a emitir cupones cupones de REGALO PROVEEDOR EXTERNO...");

        BigDecimal montoPromocional = ticket.getTotales().getTotalPagado();

        log.debug(this + " - Monto promocional:: " + montoPromocional);

        if (Numero.isIgualACero(montoPromocional)) {
            log.debug(this + " - No se van a emitir cupones porque no se lleva ningún artículo de los configurados, a pesar de que monto mínimo sea 0.");
            return null;
        }          
        
        //Comprobamos formas de pago
        /*if(!this.isAplicableAPagos(ticket.getPagos().getPagos())){
            return getMensajeNoAplicablePagos();
        }*/        
        
        // comprobamos monto mínimo
        if (Numero.isMenor(montoPromocional, configEmision.getMontoMinimo())) {
            log.debug(this + " - El monto mínimo no permite que se vayan a emitir cupones: montoPromocional: "+montoPromocional+", montoMinimo: " + configEmision.getMontoMinimo());
            return getMensajeNoAplicableMontoMinimo();
        }   
        
        return null;
    }
    
    @Override
    public void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException {
        throw new CuponNotValidException("El cupón indicado está asociado a una promoción que no es un descuento aplicable a una factura.");
    }

    @Override
    public Fecha getFechaValidez() {
        return configEmision.getFechaValidez();
    }
    
}