/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.services.ParStringInteger;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AMS
 */
public class PromocionTipoCuponRegaloCurso extends PromocionTipoCupon {

    protected List<ParStringInteger> adicionalesMedioPago;

    public PromocionTipoCuponRegaloCurso(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos primero a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);
    }

    @Override
    public List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision) {
        log.debug(this + " - Intentando emitir cupones de REGALO CURSO...");
        List<LineaTicket> lineasAplicadas = new ArrayList<LineaTicket>();
        BigDecimal montoPromocional = getSeleccion().getImporteAplicable(ticket.getLineas(), true, lineasAplicadas);
        if (montoPromocional == null){
            montoPromocional = ticket.getTotales().getTotalPagado();
        }

        BigDecimal aplicablePagos = getImporteAplicableAPagos(ticket.getPagos().getPagos());
        // si lo pagado con los medios la promoción es menor al monto promocional, no le podemos dar todo el valor del sukupon 
        if (aplicablePagos != null && Numero.isMenor(aplicablePagos, montoPromocional)){ 
            montoPromocional = aplicablePagos;
            log.debug(this + " - Los pagos no cubren el monto promocional. Nuevo monto promocional: " + montoPromocional);
        }
        
        // comprobamos monto mínimo
        if (configEmision.getMontoMinimo().compareTo(montoPromocional) > 0) {
            log.debug("El monto mínimo (" + configEmision.getMontoMinimo() + ") no permite emitir cupones para el siguiente monto promocional: " + montoPromocional);
            return null;
        }
        
        
        String clasesAccesible = rangos.getRangoAplicable(montoPromocional);
        if (clasesAccesible == null || clasesAccesible.isEmpty()) {
            log.debug(this + " - No existe rango accesible para obtener número de clases. No se emitirán cupones.");
            return null;
        }

        log.debug(toString() + " -  Número de cupones totales: " + 1);
        
        List<Cupon> resultado = new ArrayList<Cupon>();
        String variable = clasesAccesible + "|" + (configEmision.getConAcompanante() ? "S" : "N");
        Cupon cupon = new Cupon(ticket, this);
        String textoAdicional = clasesAccesible;
        if (configEmision.getConAcompanante()){
            textoAdicional += "|Con acompañante";
             textoAdicional += "| Cupos Limitados ";
        }
        else{
            textoAdicional += "|Sin acompañante";
            textoAdicional += "| Cupos Limitados ";
        }
        cupon.setTextoAdicional(textoAdicional);
        cupon.setVariable(variable);
        resultado.add(cupon);

        ticket.getTicketPromociones().addPromocionCuponesPagosPrint("* Recibe " + 1 + " cupón de cursos o talleres " + getDescripcionImpresion());
        for(LineaTicket linea: lineasAplicadas){
            ticket.putLineaPromocion(linea,super.getIdPromocion());
        }
        return resultado;
    }

    public String isAplicableMontoMinimo(TicketS ticket){
        log.debug(this + " - Intentando saber si se van a emitir cupones cupones de REGALO CURSO...");

        BigDecimal montoPromocional = getSeleccion().getImporteAplicable(ticket.getLineas(), true, new ArrayList<LineaTicket>());
        if (montoPromocional == null){
            montoPromocional = ticket.getTotales().getTotalPagado();
        }
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
