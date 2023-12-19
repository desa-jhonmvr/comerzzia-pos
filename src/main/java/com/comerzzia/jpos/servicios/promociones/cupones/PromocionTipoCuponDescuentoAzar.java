/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloMultipleBean;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.numeros.Numero;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SMLM
 */
public class PromocionTipoCuponDescuentoAzar extends PromocionTipoCupon {

    private String caducidad;
    private String caducidadImpresion;
    private Integer imprimirCada;
    private BigDecimal descuento;
    private SeleccionArticuloMultipleBean impresion;
    private SeleccionArticuloMultipleBean aplicacion;
    private Integer indiceAleatorio;
    private BigDecimal montoMinimo;
    private Integer cantidadMinima;
    public static boolean haEmitido = false;
    private boolean isAntigua;
    
    public PromocionTipoCuponDescuentoAzar(PromocionBean promocion) throws PromocionException {
        super(promocion);
        Sesion.addIndicePromocion(this);
    }
    
    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos primero a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);

        XMLDocumentNode detalle = xml.getNodo("detalles").getNodo("detalle");
        caducidad = detalle.getNodo("caducidad").getValue();
        caducidadImpresion = detalle.getNodo("caducidadImpresion").getValue();
        imprimirCada = detalle.getNodo("imprimirCada").getValueAsInteger();
        indiceAleatorio = Numero.getRandom(1, imprimirCada);
        montoMinimo = detalle.getNodo("montoMinimo").getValueAsBigDecimal();
        cantidadMinima = detalle.getNodo("cantidadMinima").getValueAsInteger();
        
        XMLDocumentNode impresion = detalle.getNodo("impresion", true) != null ? detalle.getNodo("impresion") : null;
        if(impresion.getNodo("parametros").getNodo("items", true)==null){
            isAntigua=true;
            return;
        }
        isAntigua=false;
        this.impresion = new SeleccionArticuloMultipleBean(impresion.getNodo("parametros").getNodo("items"));
       
        XMLDocumentNode aplicacion = detalle.getNodo("aplicacion", true) != null ? detalle.getNodo("aplicacion") : null;
        XMLDocumentNode parametrosAplicacion = aplicacion.getNodo("parametros", true) != null ? aplicacion.getNodo("parametros") : null;
        this.aplicacion = new SeleccionArticuloMultipleBean(aplicacion.getNodo("parametros").getNodo("items"));
        this.descuento = aplicacion.getNodo("parametros").getNodo("descuento").getValueAsBigDecimal();
    }    
    
    @Override
    public List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision) {
        haEmitido = haEmitido || false;
        log.debug("Intentando emitir cupones de CUPON DESCUENTO AZAR...");
        if(isAntigua){
            log.debug(this + ": La promoción tiene una configuración antigua");
            return null;
        }
        List<LineaTicket> aplicables = impresion.isAplicable(ticket.getLineas());
        if(aplicables.isEmpty()){
            log.debug(this + ": La selección de artículos no permite la emisión de cupones");
            return null;
        }
        BigDecimal montoAplicado = BigDecimal.ZERO;
        Integer cantidadComprada = 0;
        for(LineaTicket aplicada:aplicables){
            montoAplicado = montoAplicado.add(aplicada.getImporteTotal());
            cantidadComprada += aplicada.getCantidad();
            log.debug(this + ": La línea "+aplicada+" permite emitir cupones con un monto de: "+aplicada.getImporteTotal());
        }
        if(cantidadMinima>cantidadComprada){
            log.debug(this + ": No se han comprado suficientes artículos de la promoción para emitir el cupón, (CantidadMinima,CantidadComprada): ("+cantidadMinima+","+cantidadComprada+")");
            return null;
        }
        if(montoMinimo.compareTo(montoAplicado)>0){
            log.debug(this + ": No se emiten cupones porque no se ha alcanzado el monto mínimo, (MontoAplicado,MontoMinimo): ("+montoAplicado+","+montoMinimo+")");
            return null;
        }
        log.debug(this+": La selección de artículos permite la emisión de cupones. Mirando aleatoriedad");
        if(!indiceAleatorio.equals(Sesion.getIndicePromocion(this.getIdPromocion()))){
            log.debug(this+": No se emiten cupones por aleatoriedad");
            if(Sesion.getIndicePromocion(this.getIdPromocion()).equals(imprimirCada)){
                log.debug(this+": Reseteamos índices aleatorios");
                Sesion.actualizaIndice(this.getIdPromocion(), 1);
                indiceAleatorio = Numero.getRandom(1, imprimirCada);
            } else {
                Sesion.actualizaIndice(this.getIdPromocion(), 1 + Sesion.getIndicePromocion(this.getIdPromocion()));
            }
            return null;
        }
        log.debug(this+": Se emiten 1 cupón de la promoción DESCUENTO AZAR");
        List<Cupon> cupones = new ArrayList<Cupon>();
        Cupon cupon = new Cupon(ticket, this);
        Fecha fecha = new Fecha();
        cupon.setFechaValidez(sumaTiempo(fecha, caducidad));
        fecha = new Fecha();
        cupon.setFechaImpresion(sumaTiempo(fecha,caducidadImpresion));
        cupones.add(cupon);
        
        //ticket.getTicketPromociones().addPromocionCuponesPagosPrint("* Recibe " + 1 + " cupón de descuento " + getDescripcion());
        if(Sesion.getIndicePromocion(this.getIdPromocion()).equals(imprimirCada)){
            log.debug(this+": Reseteamos índices aleatorios");
            Sesion.actualizaIndice(this.getIdPromocion(), 1);
            indiceAleatorio = Numero.getRandom(1, imprimirCada);
        } else {
            Sesion.actualizaIndice(this.getIdPromocion(), 1 + Sesion.getIndicePromocion(this.getIdPromocion()));
        }
        haEmitido = true;
        
        return cupones;
    }

    @Override
    public void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException {
        log.debug(this + " Intentando aplicar promoción para cupón con id: " + cupon.getIdCupon());
        
        if(isAntigua) {
            log.debug(this + " La promoción tiene una configuración antigua");
            throw new CuponNotValidException("La configuración del cupón pertenece a una promoción que ya no es válida");
        }
        
        if (cupon.isUtilizado()) {
            log.debug(this + " El cupón indicado ya ha sido utilizado anteriormente.");
            throw new CuponNotValidException("El cupón indicado ya ha sido utilizado anteriormente.");
        }
        if(!cupon.getCodCliente().equals(ticket.getCliente().getCodcli())){
            log.debug(this+" El cupón debe ser usado por el mismo cliente que lo generó.");
            throw new CuponNotValidException("El cupón debe ser usado por el mismo cliente que lo generó.");
        }       
        BigDecimal importeAplicado = aplicacion.getImporteAplicable(ticket.getLineas());
        if(importeAplicado.compareTo(BigDecimal.ZERO) <= 0){
            log.debug(this+ " Los artículos comprados en esta factura no permiten aplicar el cupón indicado.");
            throw new CuponNotValidException("Los artículos comprados en esta factura no permiten aplicar el cupón indicado.");       
        }
        
        log.debug(this + ": Se aplicará el cupón a un monto aplicado de: " + importeAplicado);
        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_CUPON_DESCUENTO);
        promocionTicket.setTextoPromocion(getTextoPromocion());
        promocionTicket.setCantidadPromocion(0);
                
        BigDecimal ahorro = com.comerzzia.util.numeros.bigdecimal.Numero.porcentajeR(importeAplicado, descuento);
        log.debug(this + ": El cupón con descuento: "+descuento+"% va a generar un ahorro de "+ahorro);
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket(getTextoPromocion(), ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
        ticket.getTotales().addPromocionATotal(promocionTicket);
        log.debug(this + " Cupón aplicado correctamente.");
    }
        
    private Fecha sumaTiempo(Fecha fecha, String tiempo){
        Integer cantidad = Integer.parseInt(tiempo.substring(0, tiempo.length()-1));
        String medida = tiempo.substring(tiempo.length()-1, tiempo.length());
        if("h".equals(medida)){
            cantidad = cantidad*60;
        } else if("d".equals(medida)){
            cantidad = cantidad*60*24;
        }
        Fecha resultado = new Fecha(Fechas.sumaMinutos(fecha.getDate(), cantidad));
        return resultado;
    }
       
}
