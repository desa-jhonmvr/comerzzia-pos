/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.totales;

import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public abstract class PromocionTotal extends Promocion{

    private boolean aplicaALineasConPromocion;
    
    public PromocionTotal(PromocionBean promocion)throws PromocionException {
        super(promocion);
    }
    
    
    @Override
    protected abstract void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException;

    @Override
    public abstract void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa);

    @Override
    public abstract BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa);

    @Override
    public abstract void aplicaLineasMultiple(LineasTicket lineas);

    @Override
    public abstract void aplicaSubtotales(TotalesXML totales, LineasTicket lineas);

    public boolean isAplicaALineasConPromocion() {
        return aplicaALineasConPromocion;
    }

    public void setAplicaALineasConPromocion(boolean aplicaALineasConPromocion) {
        this.aplicaALineasConPromocion = aplicaALineasConPromocion;
    }
  
}
