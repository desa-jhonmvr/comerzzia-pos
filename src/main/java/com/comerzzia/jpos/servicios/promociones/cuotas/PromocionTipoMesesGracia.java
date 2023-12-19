/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cuotas;

import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AMS
 */
public class PromocionTipoMesesGracia extends PromocionTipoCuotas {

    private Integer numMeses;

    public PromocionTipoMesesGracia(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        XMLDocumentNode detalleXML = xml.getNodo("detalles").getNodo("detalle");
        numMeses = detalleXML.getNodo("numMeses", false).getValueAsInteger();
        seleccion = new SeleccionArticuloBean(detalleXML.getNodo("impresion").getNodo("parametros"));
    }

    public Integer getNumMeses() {
        return numMeses;
    }

    public void setNumMeses(Integer numMeses) {
        this.numMeses = numMeses;
    }

    @Override
    public void aplicaPago(LineasTicket lineas, PagoCredito pago) {
        BigDecimal importeAplicable = pago.getUstedPagaConIntereses();
        PromocionPagoTicket promocion = new PromocionPagoTicket(this);
        promocion.setNumCuotasPromocion(numMeses);
        promocion.setTextoDetalleMesesGracia(numMeses);
        promocion.setImporteBasePromocion(importeAplicable);
        promocion.setImporteCuota(importeAplicable.divide(new BigDecimal(pago.getPlanSeleccionado().getNumCuotas()), 2, BigDecimal.ROUND_HALF_DOWN));
        promocion.setImporteAhorro(BigDecimal.ZERO);
        promocion.setPrintPromocion("Recibe " + numMeses + " mes(es) de gracia por pago de $" + importeAplicable + " con " + pago.getMedioPagoActivo().getDesMedioPago() + " en productos participantes");
        pago.addPromocion(promocion);
        pago.setMesesGraciaAplicado(true);
    }

    public void aplicaPosfechadoVoucher(PagoCreditoSK pago){
        PromocionPagoTicket promocion = pago.getPromocionAplicada(getIdPromocion());
        Integer meses = pago.getMesesPosfechado();
        promocion.setTextoDetalleMesesGracia(promocion.getNumCuotasPromocion()+meses);
        promocion.setPrintPromocion("Recibe " + (promocion.getNumCuotasPromocion() + meses) + " mes(es) de gracia por pago de $" + promocion.getImporteBasePromocion() + " con " + pago.getMedioPagoActivo().getDesMedioPago() + " en productos participantes");
    }
    
    
    @Override
    protected boolean isAplicableACuotas(int numCuotas) {
        return true;
    }
    
    public String isAplicableFormasPago(TicketS ticket){
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        BigDecimal importePromocionable = getSeleccion().getImporteAplicable(ticket.getLineas(), true, lineasAplicables);
        if(Numero.isMenorOrIgualACero(importePromocionable)){
            log.debug(this + " - No se va a aplicar la promoción porque no se ha llevado ninguno de los artículos configurados");
            return null;
        }
        PromocionPagoTicket promocion = new PromocionPagoTicket(this);
        for(Pago pago:ticket.getPagos().getPagos()){
            if(pago instanceof PagoCredito){
                PagoCredito pagoCredito = (PagoCredito)pago;
                for(PromocionPagoTicket promo:pagoCredito.getPromociones()){
                    if(promo.getIdPromocion().equals(promocion.getIdPromocion())){
                        log.debug(this + " - La promoción se va a aplicar al pago: "+pagoCredito.getMedioPagoActivo().getDesMedioPago());
                        for(LineaTicket linea: lineasAplicables){
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
