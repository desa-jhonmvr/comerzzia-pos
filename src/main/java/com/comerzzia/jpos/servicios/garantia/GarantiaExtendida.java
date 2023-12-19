package com.comerzzia.jpos.servicios.garantia;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class GarantiaExtendida {

    // TODO: GARANTÍA: la devolución de un artículo garantía extendida se puede realizar hasta un día antes de que comience
    private Articulos articuloReferencia;
    private Articulos articuloGarantia;
    private BigDecimal precioGarantia;
    private BigDecimal precioTotalGarantia;

    /**
     * @author Gabriel Simbania
     * @param articuloReferencia
     * @param subTotalGarantia
     * @param precioTotalGarantia
     * @throws ArticuloNotFoundException
     */
    public GarantiaExtendida(Articulos articuloReferencia, BigDecimal subTotalGarantia,BigDecimal precioTotalGarantia) throws ArticuloNotFoundException {
        this.articuloReferencia = articuloReferencia;
        this.articuloGarantia = ArticulosServices.getInstance().getArticuloCB(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS));
        this.precioTotalGarantia = precioTotalGarantia;
        this.precioGarantia = subTotalGarantia;

    }

    public GarantiaExtendida(Articulos articuloReferencia, BigDecimal precioTotalArticulo) throws ArticuloNotFoundException {
        this.articuloReferencia = articuloReferencia;
        this.articuloGarantia = ArticulosServices.getInstance().getArticuloCB(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS));
        BigDecimal precioAfiliadoArticulo = Numero.menosPorcentaje(precioTotalArticulo, Variables.getVariableAsBigDecimal(Variables.DESCUENTO_AFILIADO_PORCENTAJE));
        precioTotalGarantia = Numero.porcentajeR(precioAfiliadoArticulo, Variables.getVariableAsBigDecimal(Variables.GARANTIA_EXT_PORCENTAJE_CALCULO));
        if (precioTotalGarantia.compareTo(Variables.getVariableAsBigDecimal(Variables.GARANTIA_EXT_IMPORTE_MINIMO)) < 0) {
            precioTotalGarantia = Variables.getVariableAsBigDecimal(Variables.GARANTIA_EXT_IMPORTE_MINIMO);
        }
        if (articuloGarantia.getCodimp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {

            //DR Para corregir garantia extendida
            precioGarantia = Numero.getAntesDePorcentajeR(precioTotalGarantia, Sesion.getEmpresa().getPorcentajeIva());

        } else {
            precioGarantia = precioTotalGarantia;
        }
    }

    public void actualizarPreciosGarantiaTicket(LineaTicket lineaGarantia, TicketS ticket) {
        // el artículo de garantía debe tener el mismo tipo de impuesto que el artículo en sí
        lineaGarantia.getArticulo().setCodimp(articuloGarantia.getCodimp());
        lineaGarantia.setPrecio(precioGarantia);
        if (lineaGarantia.getPrecioReal() == null || lineaGarantia.getPrecioReal().compareTo(BigDecimal.ONE) == -1) {
            lineaGarantia.setPrecioReal(precioGarantia);
        }
        lineaGarantia.setPrecioTarifaOrigen(precioGarantia);
        lineaGarantia.setPrecioTotal(precioTotalGarantia);
        lineaGarantia.setPrecioTotalTarifaOrigen(precioTotalGarantia);
        if (ticket.getCliente().isGarantiaExtendidaGratis()
                && !ticket.getLineas().isGarantiaExtendidaGratuitaRechazada()) {
            lineaGarantia.setDescuentoManualLinea(Numero.CIEN);
            lineaGarantia.setGarantiaGratuita(true);
        } else {
            lineaGarantia.setGarantiaGratuita(false);
        }
        lineaGarantia.recalcularPrecios();
    }

    /**
     * @author Ganriel Simbania
     * @param lineaGarantia
     * @param ticket
     * @param garantiaGratuita 
     */
    public void actualizarPreciosGarantiaTicketOnline(LineaTicket lineaGarantia, TicketS ticket, boolean garantiaGratuita) {
        // el artículo de garantía debe tener el mismo tipo de impuesto que el artículo en sí
        lineaGarantia.getArticulo().setCodimp(articuloGarantia.getCodimp());
        lineaGarantia.setPrecio(precioGarantia);
        lineaGarantia.setPrecioReal(precioGarantia);

        lineaGarantia.setPrecioTarifaOrigen(precioGarantia);
        lineaGarantia.setPrecioTotal(precioTotalGarantia);
        lineaGarantia.setPrecioTotalTarifaOrigen(precioTotalGarantia);
        lineaGarantia.setGarantiaGratuita(garantiaGratuita);
        if (garantiaGratuita) {
            lineaGarantia.setDescuentoManualLinea(Numero.CIEN);
        }
    }

    public Articulos getArticuloGarantia() {
        return articuloGarantia;
    }

    public Articulos getArticuloReferencia() {
        return articuloReferencia;
    }

    public BigDecimal getPrecioGarantia() {
        return precioGarantia;
    }

    public BigDecimal getPrecioTotalGarantia() {
        return precioTotalGarantia;
    }
}
