package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoCuponSorteo extends PromocionTipoCupon {

    public PromocionTipoCuponSorteo(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);
    }

    @Override
    public List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException {
        throw new CuponNotValidException("El cupón indicado está asociado a una promoción que no es un descuento aplicable a una factura.");
    }
}
