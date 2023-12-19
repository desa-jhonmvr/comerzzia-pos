/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cuotas;

import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
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
 * @author AMS
 */
public class PromocionTipoNCuotasGratis extends PromocionTipoCuotas {

    private Integer numCuotas;

    public PromocionTipoNCuotasGratis(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        //Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        numCuotas = detalleXML.getNodo("numCuotas", false).getValueAsInteger();
        seleccion = new SeleccionArticuloBean(detalleXML.getNodo("impresion").getNodo("parametros"));
    }

    public Integer getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(Integer numCuotas) {
        this.numCuotas = numCuotas;
    }

    @Override
    public void aplicaPago(LineasTicket lineas, PagoCredito pago) {
        BigDecimal importeAplicable = calculaPromocionPago(lineas, pago);
        if (importeAplicable == null) {
            return;
        }
        PromocionPagoTicket promocion = new PromocionPagoTicket(this);
        promocion.setNumCuotasPromocion(numCuotas);
        promocion.setImporteBasePromocion(importeAplicable);
        promocion.setImporteCuota(importeAplicable.divide(new BigDecimal(pago.getPlanSeleccionado().getNumCuotas()), 2, BigDecimal.ROUND_HALF_DOWN));
        promocion.setImporteAhorro(promocion.getImporteCuota().multiply(new BigDecimal(numCuotas)));
        promocion.setTextoDetalleCuotasGratis(numCuotas, promocion.getImporteAhorro());
        promocion.setPrintPromocion("Recibe " + numCuotas + " cuota(s) gratis por el pago de $" + importeAplicable + " con " + pago.getMedioPagoActivo().getDesMedioPago() + " en productos participantes");
        pago.addPromocion(promocion);
    }
    
    @Override
    protected boolean isAplicableACuotas(int numCuotas) {
        return numCuotas > this.numCuotas;
    }
    
    protected BigDecimal calculaPromocionPago(LineasTicket lineas, PagoCredito pago){
        /*if (pago.isTipoPromocionAplicada(getTipoPromocion().getIdTipoPromocion())){
            log.debug(this + " - No se aplica al pago " + pago + " porque ya tiene aplicada promoción del mismo tipo.");
            return null; // no podremos aplicar sobre el mismo pago dos promociones del mismo tipo
        }*/
        
        // calculamos importe aplicable para esta promoción según artículos comprados
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        BigDecimal importePromocionable = getSeleccion().getImporteAplicable(lineas, true, lineasAplicables);
        if (pago.isTipoPromocionAplicada(getTipoPromocion().getIdTipoPromocion())){
            //Tenemos que comprobar que el importePromocionable no viene de un artículo al que ya se haya aplicado la promocion
            for(LineaTicket linea:lineasAplicables){
                boolean aplicado = false;
                for(PromocionPagoTicket promoPago: pago.getTiposPromocionesAplicadas(getTipoPromocion().getIdTipoPromocion())){             
                    if(lineas.getLineaAplicable(false, Sesion.getPromocion(promoPago.getIdPromocion()).getSeleccion(), true, linea) && !aplicado){
                        log.debug(this + " - El artículo: "+linea.getArticulo().getDesart()+" ya se encuentra aplicada a la promoción: " +Sesion.getPromocion(promoPago.getIdPromocion())+" de tipo N Cuotas, restando del total aplicable a esta promoción");
                        importePromocionable = importePromocionable.subtract(linea.getImporteTotalFinalPagado());
                        aplicado = true;
                    }
                }
            }
        }
        
        if (importePromocionable == null){ // caso de todas las líneas
            importePromocionable = pago.getPlanSeleccionado().getCuotaMasInteres().multiply(new BigDecimal(pago.getPlanSeleccionado().getNumCuotas()));
        } else {
            importePromocionable = importePromocionable.multiply(pago.getUstedPagaConIntereses()).divide(lineas.getTicket().getTotales().getTotalAPagar(),2,RoundingMode.HALF_UP);
        }
       
        // calculamos el importe al que podemos aplicar la promoción
        BigDecimal importeAplicable = importePromocionable;
        log.debug(this + " - " + pago + " - Importe promocionable es " + importePromocionable );
        if (Numero.isMenorOrIgualACero(importeAplicable)){
            log.debug(this + " - " + pago + " - No se aplica a porque el importe aplicable es cero: " + importePromocionable + " = " + importeAplicable);
            return null; // no hay importe pendiente de promocionar
        }
        if (Numero.isMenor(pago.getUstedPagaConIntereses(), importeAplicable)){
            log.debug(this + " - " + pago + " - Importe aplicable mayor a importe del voucher del pago: " + pago.getUstedPagaConIntereses() + " Se aplicará sólo al importe del voucher del pago." );
            importeAplicable = pago.getUstedPagaConIntereses();
        }
        return importeAplicable;
    }
    
    public String isAplicableFormasPago(TicketS ticket){
        List<LineaTicket> lineasAplicadas = new ArrayList<LineaTicket>();
        BigDecimal importePromocionable = getSeleccion().getImporteAplicable(ticket.getLineas(), true, lineasAplicadas);
        if(Numero.isMenorOrIgualACero(importePromocionable)){
            log.debug(this + " - No se va a aplicar la promoción porque no se ha llevado ninguno de los artículos configurados");
            return null;
        }
        BigDecimal importePromocionado = ticket.getPagos().getImportePromocionado(getIdPromocion());
        if(importePromocionado == null || Numero.isMenorOrIgualACero(importePromocionado)){
            log.debug(this + " - No se va a aplicar la promoción porque el importe promocionado es 0");
            return null;
        } 
        PromocionPagoTicket promocion = new PromocionPagoTicket(this);
        for(Pago pago:ticket.getPagos().getPagos()){
            if(pago instanceof PagoCredito){
                PagoCredito pagoCredito = (PagoCredito)pago;
                for(PromocionPagoTicket promo:pagoCredito.getPromociones()){
                    if(promo.getIdPromocion().equals(promocion.getIdPromocion())){
                        log.debug(this + " - La promoción se va a aplicar al pago: "+pagoCredito.getMedioPagoActivo().getDesMedioPago());
                        for(LineaTicket linea: lineasAplicadas){
                            ticket.putLineaPromocion(linea,super.getIdPromocion());
                        }                        
                        return null;
                    }
                }
            }
        }
        log.debug(this + " - No se va a aplicar la promoción porque no se ha seleccionado ninguno de los pagos");
        return getMensajeNoAplicablePagos();
    }
}
