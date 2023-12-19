/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromoLineaCandidata;
import com.comerzzia.jpos.servicios.promociones.articulos.SukuponLinea;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AMS
 */
public class PromocionTipoBilleton extends PromocionTipoCupon {

    public PromocionTipoBilleton(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // llamamos a la clase padre para que parsee los datos generales del cupon y la configuración de impresión    
        super.parsearXMLDatosPromocion(xml);
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
    }

    @Override
    public List<Cupon> emiteCupones(TicketS ticket, ConfigEmisionCupones configEmision) {
        log.debug("Intentando emitir cupones de BILLETON-SUKUPON...");
        // comprobamos que no tenga aplicado sukupón 
        if (ticket.tieneSukuponAplicado()) {
            log.debug(this + " La factura ya tiene aplicado un sukupón. No se emite sukupón nuevo. ");
            return null;
        }
        // comprobamos si la promoción se puede aplicar según las lineas compradas en el ticket
        List<PromoLineaCandidata> aplicables = getSeleccion().getAplicablesCheck(ticket.getLineas(), configEmision.getMontoMinimo(), new ArrayList<LineaTicket>());
        if (aplicables.isEmpty()) { // las lineas del ticket no permiten la aplicación de la promoción
            log.debug(this + " La selección de artículos aplicables no permite emitir cupones.");
            return null;
        }

        if (Sesion.configBilleton == null) {
            log.debug(this + " La configuración de Billetón no se ha cargado correctamente. Imposible emitir cupones.");
            return null;
        }

        boolean porcentaje = true;
        if (configEmision.getTipo().equals("R")) {
            porcentaje = false;
        }

        BigDecimal acumulado = BigDecimal.ZERO;
        BigDecimal montoPromocional = BigDecimal.ZERO;
        log.debug(this + " - Tenemos " + aplicables.size() + " lineas que pueden aplicarse con Billetón. Sumamos todos los descuentos para cada una de ellas...");
        Map<LineaTicket, SukuponLinea> lineasConEmision = new HashMap<LineaTicket, SukuponLinea>();
        for (PromoLineaCandidata lineaAplicable : aplicables) {
            boolean auspiciante = lineaAplicable.isValorLogico();
            BigDecimal precio = lineaAplicable.getLinea().getPrecioTarifaOrigen();
            ConfiguracionBilletonDetalleBean config = Sesion.configBilleton.getDetalleAplicable(configEmision.getTipo(), auspiciante, precio);
            if (config != null) {
                BigDecimal valor = config.getValor();
                if (porcentaje) {
                    valor = Numero.porcentajeR(lineaAplicable.getLinea().getPrecioTarifaOrigen(), valor);
                }
                valor = valor.multiply(new BigDecimal(lineaAplicable.getLinea().getCantidad()));
                acumulado = acumulado.add(valor);
                log.debug(this + " - Por la línea " + lineaAplicable.getLinea().toString() + " con precio: " + precio + ", se obtiene un descuento de $: " + valor);
                SukuponLinea sukupon = new SukuponLinea();
                sukupon.setAuspiciante(auspiciante);
                sukupon.setValor(Numero.redondear(valor));
                sukupon.setArticulo(lineaAplicable.getLinea().getArticulo());
                sukupon.setCantidad(lineaAplicable.getLinea().getCantidad());
                sukupon.setImporte(lineaAplicable.getLinea().getPrecioTarifaOrigen());
                sukupon.setImporteTotal(lineaAplicable.getLinea().getImporte());
                lineasConEmision.put(lineaAplicable.getLinea(), sukupon);
                montoPromocional = montoPromocional.add(lineaAplicable.getLinea().getImporteTotal());
            } else {
                log.debug(this + " - La línea " + lineaAplicable.getLinea().toString() + " no tiene una configuración de Billetón aplicable. No se dará descuento por ella.");
            }
        }
        if (acumulado.compareTo(BigDecimal.ZERO) == 0) {
            log.debug(this + " - No se pudo aplicar a ninguna línea ningún descuento. No podemos emitir el Billetón. ");
            return null;
        }
        acumulado = Numero.redondear(acumulado);
        log.debug(this + " - Acumulado en Billetón: " + acumulado);

        /*
        if (Numero.isMenor(montoPromocional, configEmision.getMontoComprasMinimo())){
            log.debug(this + " - . No podemos emitir el Billetón. El monto promocional (" + montoPromocional + ") es menor al monto mínimo: " + configEmision.getMontoComprasMinimo());
            return null;
        }
         */
        BigDecimal aplicablePagos = getImporteAplicableAPagos(ticket.getPagos().getPagos());
        // si lo pagado con los medios la promoción es menor al monto promocional, no le podemos dar todo el valor del sukupon 
        if (aplicablePagos != null && Numero.isMenor(aplicablePagos, montoPromocional)) {
            log.debug(this + " - Los pagos no cubren el monto promocional, calculamos porcentaje para reducir el valor del billetón.");
            BigDecimal porcentajePago = Numero.getTantoPorCientoContenido(montoPromocional, aplicablePagos);
            acumulado = Numero.porcentajeR(acumulado, porcentajePago);
            log.debug(this + " - Acumulado en Billetón tras reducir % pagos: " + acumulado);
        }

        List<Cupon> resultado = new ArrayList<Cupon>();
        Cupon cupon = new Cupon(ticket, this);
        cupon.setTextoAdicional("VALOR DE SUKUPÓN: $ " + acumulado);
        cupon.setVariable(acumulado.toString());
        cupon.setIdPromocion(this.getIdPromocion());
        cupon.setIdTipoPromocion(this.getIdTipoPromocion());
        resultado.add(cupon);
        ticket.getTicketPromociones().addPromocionCuponesPagosPrint("Recibe $ " + acumulado + " de " + super.getTextoPromocion());
        if (ticket.isFinalizado()) {
            for (LineaTicket lineaTicket : lineasConEmision.keySet()) {
                SukuponLinea sukupon = lineasConEmision.get(lineaTicket);
                sukupon.setSukupon(cupon);
                lineaTicket.addSukuponEmitido(sukupon);
                ticket.putLineaPromocion(lineaTicket, super.getIdPromocion());
            }
        }

        return resultado;
    }

    @Override
    public void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException {
        if (!ticket.getCuponesAplicados().isEmpty()) { // Suponemos que sólo se podrán aplicar cupones de tipo Billetón. Los de tipo descuento serán utilizados sólo por Bebemundo que no utiliza el Billetón.
            throw new CuponNotValidException("Sólo puede utilizar un SUKUPÓN en cada compra.");
        }
        if (ticket.getLineas().getNumLineas() == 0) {
            throw new CuponNotValidException("No puede aplicar el cupón sin incluir antes ningún artículo.");
        }
        if (cupon.isUtilizado()) {
            throw new CuponNotValidException("El SUKUPON indicado ya ha sido utilizado anteriormente.");
        }
        if (!cupon.getCodCliente().equals(ticket.getCliente().getCodcli())) {
            throw new CuponNotValidException("El SUKUPON indicado fue emitido para un cliente distinto al de esta factura.");
        }

        BigDecimal ahorro = new BigDecimal(cupon.getSaldo());
        if (ahorro.compareTo(ticket.getTotales().getTotalAPagar().subtract(ticket.getTotales().getTotalGarantiaExtendida())) >= 0) {
            if (ticket.getTotales().getTotalGarantiaExtendida().compareTo(BigDecimal.ZERO) > 0) {
                throw new CuponNotValidException("El valor del SUKUPON no puede ser utilizado para pagar garantías extendidas.");
            } else {
                throw new CuponNotValidException("El valor del SUKUPON debe ser menor al importe de la factura.");
            }
        }
        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_CUPON_DESCUENTO);
        promocionTicket.setTextoPromocion(getTextoPromocion());
        promocionTicket.setCantidadPromocion(0);
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket("DESCTO. SUKUPÓN", ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
        ticket.getTotales().addPromocionATotal(promocionTicket);
    }

    @Override
    public Fecha getFechaValidez() {
        return configEmision.getFechaValidez();
    }

}
