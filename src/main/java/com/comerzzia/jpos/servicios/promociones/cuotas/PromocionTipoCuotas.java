/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cuotas;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public abstract class PromocionTipoCuotas extends Promocion{

    public PromocionTipoCuotas(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }
    
    
    @Override
    protected abstract void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException;

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public boolean isAplicableAPago(Pago pago) {
        if (!super.isAplicableAPago(pago)){
            return false;
        }
        if (!(pago instanceof PagoCredito)){
            return false;
        }
        PagoCredito pagoCredito = (PagoCredito) pago;
        int numCuotas = pagoCredito.getPlanSeleccionado().getNumCuotas();
        return isAplicableACuotas(numCuotas);
    }
    
    
    
    public abstract void aplicaPago(LineasTicket lineas, PagoCredito pago);

    protected abstract boolean isAplicableACuotas(int numCuotas);
    
    
}
