package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoCuponDescuento extends PromocionTipoCupon {

    private boolean compatibleConOtros;
    private boolean multiplesUsos;
    private BigDecimal montoMinimoCompra;
    private BigDecimal descuento;
    private boolean soloSinPromocion;
    private BigDecimal montoMaximo;
    private String codMarca;
    private String codSeccion;
    private String codSubseccion;
    private String codCategoria;
    private String codArticulo;

    public PromocionTipoCuponDescuento(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos primero a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);

        // ahora parseamos el nodo de aplicación
        XMLDocumentNode detalle = xml.getNodo("detalles").getNodo("detalle");

        XMLDocumentNode aplicacion = detalle.getNodo("aplicacion", true) != null ? detalle.getNodo("aplicacion") : null;
        XMLDocumentNode parametrosAplicacion = aplicacion.getNodo("parametros", true) != null ? aplicacion.getNodo("parametros") : null;


        compatibleConOtros = (parametrosAplicacion.getNodo("compatibleConOtros", false).getValueAsBoolean());
        multiplesUsos = parametrosAplicacion.getNodo("multiplesUsos", true) != null ? parametrosAplicacion.getNodo("multiplesUsos").getValueAsBoolean() : false;
        montoMinimoCompra = (parametrosAplicacion.getNodo("montoMinimo", false).getValueAsBigDecimal());
        descuento = (parametrosAplicacion.getNodo("descuento", false).getValueAsBigDecimal());
        soloSinPromocion = (parametrosAplicacion.getNodo("soloSinPromocion", false).getValueAsBoolean());
        montoMaximo = (parametrosAplicacion.getNodo("montoMaximo", false).getValueAsBigDecimal());
        codMarca = (parametrosAplicacion.getNodo("codMarca").getValue().isEmpty() ? null : parametrosAplicacion.getNodo("codMarca").getValue());
        codSeccion = (parametrosAplicacion.getNodo("codSeccion").getValue().isEmpty() ? null : parametrosAplicacion.getNodo("codSeccion").getValue());
        codSubseccion = (parametrosAplicacion.getNodo("codSubseccion").getValue().isEmpty() ? null : parametrosAplicacion.getNodo("codSubseccion").getValue());
        codCategoria = (parametrosAplicacion.getNodo("codCategoria").getValue().isEmpty() ? null : parametrosAplicacion.getNodo("codCategoria").getValue());
        codArticulo = (parametrosAplicacion.getNodo("codArticulo").getValue().isEmpty() ? null : parametrosAplicacion.getNodo("codArticulo").getValue());
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public String getCodMarca() {
        return codMarca;
    }

    public String getCodSeccion() {
        return codSeccion;
    }

    public String getCodSubseccion() {
        return codSubseccion;
    }

    public boolean isCompatibleConOtros() {
        return compatibleConOtros;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getMontoMaximo() {
        return montoMaximo;
    }

    public BigDecimal getMontoMinimoCompra() {
        return montoMinimoCompra;
    }

    public boolean isSoloSinPromocion() {
        return soloSinPromocion;
    }

    public boolean isMultiplesUsos() {
        return multiplesUsos;
    }

    @Override
    public List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException{
        log.debug(this + " Intentando aplicar promoción para cupón con id: " + cupon.getIdCupon());
        if (!ticket.getCuponesAplicados().isEmpty()) {
            // Suponemos que no se aplicarán Billetones si se están aplicando este tipo de cupones. Los primeros son de Sukasa, los segundos de Bebemundo
            PromocionTipoCupon promocionTipoCupon = ticket.getCuponesAplicados().get(0).getPromocion(); 
            if (promocionTipoCupon.getTipoPromocion().isPromocionTipoCuponDescuento()){
                if (!((PromocionTipoCuponDescuento)promocionTipoCupon).isCompatibleConOtros()) {
                    log.debug(this + " No se puede aplicar cupón porque en la factura actual ya se están aplicando cupones no compatibles con otros. ");
                    throw new CuponNotValidException("En la factura actual ya se está aplicando un cupón que no es compatible con más cupones.");
                }
            }
        }
        if (!isMultiplesUsos() && cupon.isUtilizado()) {
            log.debug(this + " El cupón indicado ya ha sido utilizado anteriormente.");
            throw new CuponNotValidException("El cupón indicado ya ha sido utilizado anteriormente.");
        }
        if (!isCompatibleConOtros() && !ticket.getCuponesAplicados().isEmpty()) {
            throw new CuponNotValidException("El cupón indicado no permite su aplicación combinada con los que ya tiene aplicados en la factura.");
        }

        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        int cant;
        if (getCodMarca() != null) { // se ha indicado sólo marca
            cant = ticket.getLineas().getContainsMarcas(isSoloSinPromocion(), getCodMarca(), lineasAplicables);
        }
        else if (getCodCategoria() != null) { // se ha indicado sólo categoría
            cant = ticket.getLineas().getContainsCategoria(isSoloSinPromocion(), getCodCategoria(), lineasAplicables);
        }
        else if (getCodSubseccion() != null) { // se ha indicado sólo subsección
            cant = ticket.getLineas().getContainsSubSecciones(isSoloSinPromocion(), getCodSubseccion(), lineasAplicables);
        }
        else if (getCodSeccion() != null) { // se ha indicado sólo sección
            cant = ticket.getLineas().getContainsSecciones(isSoloSinPromocion(), getCodSeccion(), lineasAplicables);
        }
        else if (getCodArticulo() != null) { // se ha indicado sólo un artículo
            cant = ticket.getLineas().getContains(isSoloSinPromocion(), getCodArticulo(), lineasAplicables);
        }
        else { // todos son null, no se ha indicado nada, afecta a todos los artículos
            cant = ticket.getLineas().getContains(isSoloSinPromocion(), lineasAplicables);
        }

        // la cantidad no es un parámetro que se evalúe, lo dejamos escrito para futuras ampliaciones.

        // comprobamos si tenemos líneas a las que aplicar el cupón
        if (lineasAplicables.isEmpty()) {
            throw new CuponNotValidException("Los artículos comprados en esta factura no permiten aplicar el cupón indicado.");
        }

        // comprobamos monto
        BigDecimal monto = BigDecimal.ZERO;
        for (LineaTicket linea : lineasAplicables) {
            monto = monto.add(linea.getImporteTotal());
        }
        if (getMontoMinimoCompra().compareTo(BigDecimal.ZERO) > 0
                && getMontoMinimoCompra().compareTo(monto) > 0) {
            throw new CuponNotValidException("No ha alcanzado el monto mínimo de compra en los artículos correspondientes para poder aplicar el descuento del cupón.");
        }

        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_CUPON_DESCUENTO);
        promocionTicket.setTextoPromocion(getTextoPromocion());
        promocionTicket.setCantidadPromocion(0);

        // comprobamos monto máximo. Si el monto es mayor que el máximo, utilizaremos el máximo
        if (getMontoMaximo().compareTo(BigDecimal.ZERO) > 0
                && getMontoMaximo().compareTo(monto) < 0) {
            monto = getMontoMaximo();
        }

        // aplicamos el porcentaje de descuento multiplicado por el número de veces aplicable
        BigDecimal ahorro = Numero.porcentajeR(monto, getDescuento());
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket(getTextoPromocion(), ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
        ticket.getTotales().addPromocionATotal(promocionTicket);
        log.debug(this + " Cupón aplicado correctamente.");
    }

}
