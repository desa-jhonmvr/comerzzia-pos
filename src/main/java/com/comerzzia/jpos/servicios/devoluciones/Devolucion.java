/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.devoluciones;

import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.core.usuarios.ServicioUsuarios;
import com.comerzzia.jpos.servicios.promociones.cupones.ServicioCupones;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amos
 */
public class Devolucion {

    protected static Logger log = Logger.getMLogger(ServicioUsuarios.class);

    private TicketOrigen ticketOriginal;
    private TicketS ticketDevolucion;
    private com.comerzzia.jpos.entity.db.Devolucion devolucion;
    private BigDecimal totalDevolucion;
    private Map<Integer, ArticuloDevueltoBean> articulosDevueltos;
    private NotasCredito notaCredito;
    private String autorizador;
    private UsuarioBean autorizadorDevolucion;
    private Cupon sukuponFacturaOrigen;
    private boolean anulacion = false;
    private Long tipoDevolucion;
    private String localOrigen;
    private List<MedioPagoDTO> formasPago = new ArrayList<>();

    private String codVendedor;
    /**
     * Constructor que crea la devolución pero no las líneas de devolución Lo
     * usaremos desde Plan novio al liquidar una nota de credito
     *
     * @param ticketOriginal
     * @param ticket
     * @param motivoDevolucion
     * @param estadoMercaderia
     * @param observaciones
     * @param simple
     * @throws Exception
     */
    public Devolucion(TicketOrigen ticketOriginal, TicketS ticket, MotivoDevolucion motivoDevolucion, String estadoMercaderia, String observaciones, boolean simple) throws Exception {
        this.ticketOriginal = ticketOriginal;
        this.devolucion = new com.comerzzia.jpos.entity.db.Devolucion();
        this.devolucion.setMotivo(motivoDevolucion);
        this.devolucion.setEstadoMercaderia(estadoMercaderia);
        this.devolucion.setObservaciones(observaciones);
        this.totalDevolucion = ticket.getTotales().getTotalAPagar();
        this.ticketDevolucion = ticket;
        articulosDevueltos = DevolucionesServices.consultarArticulosDevueltos(ticketOriginal.getUid_ticket());

        sukuponFacturaOrigen = ServicioCupones.consultarSukuponFactura(ticketOriginal.getUid_ticket());
    }

    /**
     * Constructor para crearnos de forma automática las líneas de devolución
     *
     * @param ticketOriginal
     * @param ticket
     * @param motivoDevolucion
     * @param estadoMercaderia
     * @param observaciones
     * @throws Exception
     */
    public Devolucion(TicketOrigen ticketOriginal, TicketS ticket, MotivoDevolucion motivoDevolucion, String estadoMercaderia, String observaciones) throws Exception {
        this.ticketOriginal = ticketOriginal;
        this.devolucion = new com.comerzzia.jpos.entity.db.Devolucion();
        this.devolucion.setMotivo(motivoDevolucion);
        this.devolucion.setEstadoMercaderia(estadoMercaderia);
        this.devolucion.setObservaciones(observaciones);
        this.totalDevolucion = ticket.getTotales().getTotalAPagar();
        this.ticketDevolucion = ticket;
        articulosDevueltos = DevolucionesServices.consultarArticulosDevueltos(ticketOriginal.getUid_ticket());

        sukuponFacturaOrigen = ServicioCupones.consultarSukuponFactura(ticketOriginal.getUid_ticket());
        int cantidadDevueltosTotal = 0;

        /**
         * BUSCAMOS TODAS LAS LINEAS QUE PUEDEN SER DEVUELTAS Y LAS PONEMOS EN
         * EL TICKET DE DEVOLUCIÓN *
         */
        BigDecimal compensacion = ticketOriginal.getCompensacion();
        BigDecimal porcentajeComp = ticketOriginal.getPorcentajeCompensacion();
        /*if(compensacion.compareTo(BigDecimal.ZERO)>0){
            porcentajeComp = (ticketOriginal.getTotales().getImpuestos().subtract(compensacion)).divide(ticketOriginal.getTotales().getImpuestos(), 7, RoundingMode.HALF_DOWN);
        }*/
        // Para cada línea del ticket original
        for (LineaTicket lineaOriginal : ticketOriginal.getLineas()) {

            // cantidad de artículos en el ticket original
            int cantOrigen = lineaOriginal.getCantidad();

            // Agrupado Articulos devueltos
            ArticuloDevueltoBean articulosDevueltosAgrupados = articulosDevueltos.get(lineaOriginal.getIdlinea());

            int cantDevueltosLinea = 0;
            if (articulosDevueltosAgrupados != null) {
                cantDevueltosLinea = articulosDevueltosAgrupados.getCantidad();
            }

            // cantidad de artículos devueltos en la devolución actual
            int cantDevolucion = lineaOriginal.getCantidad() - cantDevueltosLinea;
            cantidadDevueltosTotal += cantDevueltosLinea;

            // cantidad que es posible de devolver
            int cantPosibleDevolucion = lineaOriginal.getCantidad() - cantDevueltosLinea;

            // Tarifa REVISAR  Tarifa para costo   
//            TarifasServices tarifasServices = new TarifasServices();
//            Tarifas tarifa = tarifasServices.getTarifaArticulo(lineaOriginal.getArticulo().getCodart());  
            ////
            Articulos art = ArticulosServices.getInstance().getArticuloCod(lineaOriginal.getArticulo().getCodart());
            // Si no encontramos el artículo en el sistema, lanzaremos una excepción
            if (art == null) {
                throw new ArticuloNotFoundException("No se ha encontrado artículo con código: " + lineaOriginal.getArticulo().getCodart());
            }
            // TODO: Posible mejora de control de error si no encintramos el artículo

            BigDecimal cantidadLineaOrigen = new BigDecimal(lineaOriginal.getCantidad()); // Para calcular el precio original

            if (cantPosibleDevolucion > 0) {
                BigDecimal precioFinal = lineaOriginal.getImporteFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
                BigDecimal precioTotalFinal = lineaOriginal.getImporteTotalFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
                BigDecimal compensacionLinea = BigDecimal.ZERO;
                if (compensacion.compareTo(BigDecimal.ZERO) > 0 && lineaOriginal.getCodimp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
                    compensacionLinea = precioFinal.multiply(porcentajeComp).divide(Numero.CIEN);
                    ticketDevolucion.getTotales().setCompensacionGobierno(ticketDevolucion.getTotales().getCompensacionGobierno().add(compensacionLinea));
                }
//            LineaTicket linea = ticketDevolucion.getLineas().nuevaLineaDevolucion(lineaOriginal.getCodigoBarras(), art, cantPosibleDevolucion, precioFinal, precioTotalFinal);
                LineaTicket linea = ticketDevolucion.getLineas().nuevaLineaDevolucion(lineaOriginal.getCodigoBarras(), art, cantPosibleDevolucion, precioFinal, precioTotalFinal);
                linea.setDatosAdicionales(lineaOriginal.getDatosAdicionales());

                //DR
                linea.setCodEmpleado(lineaOriginal.getCodEmpleado());

////          ///cambio para solucionar problema Rd Costo landed
////            List<LineaTicketOrigen> lineaPrueba = TicketService.consultarLineasTicket(ticketDevolucion.getUid_ticket());
////            for(int i =0;i<lineaPrueba.size();i++){
////            if(linea.getIdlinea().equals(lineaPrueba.get(i).getCodart())){
////              linea.setCostoLanded(lineaPrueba.get(i).getCostoLanded());
////            }
////                }
////            ///////////
////             //cambio realizado para mostrar en pantalla tarifa origen Rd
                linea.setPrecioTarifaOrigen(lineaOriginal.getPrecioTarifaOrigen());
                linea.setCompensacionLinea(compensacionLinea);
                linea.setDescuentoFinalDev(lineaOriginal.getDescuentoFinalDev());
                linea.setCodimp(lineaOriginal.getCodimp());
                if (lineaOriginal.isPromocionAplicada()) {
                    linea.setDescripcionAdicional("PROMO");
                }
                linea.setLineaOriginal(lineaOriginal.getIdlinea());
                linea.setReferenciaGarantia(lineaOriginal.getReferenciaGarantia());
                linea.setReferenciaKit(lineaOriginal.getReferenciaKit());
                linea.setPorcentajeIva(lineaOriginal.getPorcentajeIva());
                linea.setInteres(lineaOriginal.getInteres().divide(cantidadLineaOrigen,BigDecimal.ROUND_HALF_EVEN,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(cantPosibleDevolucion)));
            }
        }

        ticketDevolucion.getTotales().recalcularTotalesLineas(ticketDevolucion.getLineas());
        /*if(cantidadDevueltosTotal == 0){
            ticketDevolucion.getTotales().setTotalAPagar(ticketOriginal.getTotales().getTotalPagado().subtract(compensacion));
        }*/
        ticketDevolucion.getTotales().redondear();

        /**
         * * FIN CREACION **
         */
    }
    
    /**
     * Constructor para crearnos de forma automática las líneas de devolución
     *
     * @param ticketOriginal
     * @param ticket
     * @param motivoDevolucion
     * @param estadoMercaderia
     * @param observaciones
     * @throws Exception
     */
    public Devolucion(TicketOrigen ticketOriginal, TicketS ticket, MotivoDevolucion motivoDevolucion, String estadoMercaderia, String observaciones, Long tipoDevolucion, String localOrigen) throws Exception {

        this.ticketOriginal = ticketOriginal;
        this.devolucion = new com.comerzzia.jpos.entity.db.Devolucion();
        this.devolucion.setMotivo(motivoDevolucion);
        this.devolucion.setEstadoMercaderia(estadoMercaderia);
        this.devolucion.setObservaciones(observaciones);
        this.totalDevolucion = ticket.getTotales().getTotalAPagar();
        this.ticketDevolucion = ticket;
        this.tipoDevolucion = tipoDevolucion;
        this.localOrigen = localOrigen;
        articulosDevueltos = DevolucionesServices.consultarArticulosDevueltos(ticketOriginal.getUid_ticket());

        sukuponFacturaOrigen = ServicioCupones.consultarSukuponFactura(ticketOriginal.getUid_ticket());
        int cantidadDevueltosTotal = 0;

        /**
         * BUSCAMOS TODAS LAS LINEAS QUE PUEDEN SER DEVUELTAS Y LAS PONEMOS EN
         * EL TICKET DE DEVOLUCIÓN *
         */
        BigDecimal compensacion = ticketOriginal.getCompensacion();
        BigDecimal porcentajeComp = ticketOriginal.getPorcentajeCompensacion();
        /*if(compensacion.compareTo(BigDecimal.ZERO)>0){
            porcentajeComp = (ticketOriginal.getTotales().getImpuestos().subtract(compensacion)).divide(ticketOriginal.getTotales().getImpuestos(), 7, RoundingMode.HALF_DOWN);
        }*/
        // Para cada línea del ticket original
        for (LineaTicket lineaOriginal : ticketOriginal.getLineas()) {

            // cantidad de artículos en el ticket original
            int cantOrigen = lineaOriginal.getCantidad();

            // Agrupado Articulos devueltos
            ArticuloDevueltoBean articulosDevueltosAgrupados = articulosDevueltos.get(lineaOriginal.getIdlinea());

            int cantDevueltosLinea = 0;
            if (articulosDevueltosAgrupados != null) {
                cantDevueltosLinea = articulosDevueltosAgrupados.getCantidad();
            }

            // cantidad de artículos devueltos en la devolución actual
            int cantDevolucion = lineaOriginal.getCantidad() - cantDevueltosLinea;
            cantidadDevueltosTotal += cantDevueltosLinea;

            // cantidad que es posible de devolver
            int cantPosibleDevolucion = lineaOriginal.getCantidad() - cantDevueltosLinea;

            // Tarifa REVISAR  Tarifa para costo   
//            TarifasServices tarifasServices = new TarifasServices();
//            Tarifas tarifa = tarifasServices.getTarifaArticulo(lineaOriginal.getArticulo().getCodart());  
            ////
            Articulos art = ArticulosServices.getInstance().getArticuloCod(lineaOriginal.getArticulo().getCodart());
            // Si no encontramos el artículo en el sistema, lanzaremos una excepción
            if (art == null) {
                throw new ArticuloNotFoundException("No se ha encontrado artículo con código: " + lineaOriginal.getArticulo().getCodart());
            }
            // TODO: Posible mejora de control de error si no encintramos el artículo

            BigDecimal cantidadLineaOrigen = new BigDecimal(lineaOriginal.getCantidad()); // Para calcular el precio original

            if (cantPosibleDevolucion > 0) {
                BigDecimal precioFinal = lineaOriginal.getImporteFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
                BigDecimal precioTotalFinal = lineaOriginal.getImporteTotalFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
                BigDecimal compensacionLinea = BigDecimal.ZERO;
                if (compensacion.compareTo(BigDecimal.ZERO) > 0 && lineaOriginal.getCodimp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
                    compensacionLinea = precioFinal.multiply(porcentajeComp).divide(Numero.CIEN);
                    ticketDevolucion.getTotales().setCompensacionGobierno(ticketDevolucion.getTotales().getCompensacionGobierno().add(compensacionLinea));
                }
//            LineaTicket linea = ticketDevolucion.getLineas().nuevaLineaDevolucion(lineaOriginal.getCodigoBarras(), art, cantPosibleDevolucion, precioFinal, precioTotalFinal);
                LineaTicket linea = ticketDevolucion.getLineas().nuevaLineaDevolucion(lineaOriginal.getCodigoBarras(), art, cantPosibleDevolucion, precioFinal, precioTotalFinal);

                //DR
                linea.setCodEmpleado(lineaOriginal.getCodEmpleado());

////          ///cambio para solucionar problema Rd Costo landed
////            List<LineaTicketOrigen> lineaPrueba = TicketService.consultarLineasTicket(ticketDevolucion.getUid_ticket());
////            for(int i =0;i<lineaPrueba.size();i++){
////            if(linea.getIdlinea().equals(lineaPrueba.get(i).getCodart())){
////              linea.setCostoLanded(lineaPrueba.get(i).getCostoLanded());
////            }
////                }
////            ///////////
////             //cambio realizado para mostrar en pantalla tarifa origen Rd
                linea.setPrecioTarifaOrigen(lineaOriginal.getPrecioTarifaOrigen());
                linea.setCompensacionLinea(compensacionLinea);
                linea.setDescuentoFinalDev(lineaOriginal.getDescuentoFinalDev());
                linea.setCodimp(lineaOriginal.getCodimp());
                if (lineaOriginal.isPromocionAplicada()) {
                    linea.setDescripcionAdicional("PROMO");
                }
                linea.setLineaOriginal(lineaOriginal.getIdlinea());
                linea.setReferenciaGarantia(lineaOriginal.getReferenciaGarantia());
                linea.setReferenciaKit(lineaOriginal.getReferenciaKit());
                linea.setPorcentajeIva(lineaOriginal.getPorcentajeIva());
            }
        }

        ticketDevolucion.getTotales().recalcularTotalesLineas(ticketDevolucion.getLineas());
        /*if(cantidadDevueltosTotal == 0){
            ticketDevolucion.getTotales().setTotalAPagar(ticketOriginal.getTotales().getTotalPagado().subtract(compensacion));
        }*/
        ticketDevolucion.getTotales().redondear();

        /**
         * * FIN CREACION **
         */
    }

    public void recalcularCompensacion() {
        ticketDevolucion.getTotales().setCompensacionGobierno(BigDecimal.ZERO);
        for (LineaTicket linea : ticketDevolucion.getLineas().getLineas()) {
            ticketDevolucion.getTotales().setCompensacionGobierno(ticketDevolucion.getTotales().getCompensacionGobierno().add(linea.getCompensacionLinea()));
        }
    }

    public void modificarLineaDevolucion(LineaTicket lineaTicketSeleccionada, Integer nuevaCantidad) throws ArticuloNotFoundException, ValidationException {
        log.debug("modificarLineaDevolucion Devolucion.java");
        log.debug("lineaTicketSeleccionada.interes:" +lineaTicketSeleccionada.getInteres());
        LineaTicket lineaOriginal = ticketOriginal.getLinea(lineaTicketSeleccionada.getLineaOriginal());
        if (lineaOriginal == null) {
            log.error("modificarLineaDevolucion() - No se ha encontrado la línea original en el ticket");
            throw new ArticuloNotFoundException();
        }
        // Cantidad devuelta
        int numeroArticulosDevueltos = 0;
        ArticuloDevueltoBean articuloDevueltoAcumulado = articulosDevueltos.get(lineaTicketSeleccionada.getLineaOriginal());
        if (articuloDevueltoAcumulado != null) {
            numeroArticulosDevueltos = articuloDevueltoAcumulado.getCantidad();
        }

        int articulosPuedeDevolver = lineaOriginal.getCantidad() - numeroArticulosDevueltos;
        if (nuevaCantidad <= articulosPuedeDevolver) {
            BigDecimal cantidadLineaOrigen = new BigDecimal(lineaOriginal.getCantidad());
            BigDecimal precioFinal = lineaOriginal.getImporteFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
            BigDecimal precioTotalFinal = lineaOriginal.getImporteTotalFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
            lineaTicketSeleccionada.setPrecioTarifaOrigen(precioFinal);
            lineaTicketSeleccionada.setPrecioTotalTarifaOrigen(precioTotalFinal);
            lineaTicketSeleccionada.setCantidad(nuevaCantidad);
            lineaTicketSeleccionada.recalcularImportes();
            lineaTicketSeleccionada.setDescuentoFinalDev(lineaOriginal.getDescuentoFinalDev());
            BigDecimal nuevoInteres = lineaOriginal.getInteres().divide(BigDecimal.valueOf(lineaOriginal.getCantidad()),BigDecimal.ROUND_CEILING,RoundingMode.DOWN).multiply(BigDecimal.valueOf(nuevaCantidad)).setScale(BigDecimal.ROUND_CEILING,RoundingMode.DOWN);
            lineaTicketSeleccionada.setInteres(nuevoInteres);
            lineaTicketSeleccionada.redondear();
            ticketDevolucion.getTotales().recalcularTotalesLineas(ticketDevolucion.getLineas());
            ticketDevolucion.getTotales().redondear();
            lineaTicketSeleccionada.setPrecioTarifaOrigen(lineaOriginal.getPrecioTarifaOrigen());
        } else {
            throw new ValidationException("La cantidad introducida supera a la cantidad máxima a devolver");
        }
    }

    public String getEstadoMercaderia() {
        return devolucion.getEstadoMercaderia();
    }

    public void setEstadoMercaderia(String estadoMercaderia) {
        devolucion.setEstadoMercaderia(estadoMercaderia);
    }

    public MotivoDevolucion getMotivo() {
        return devolucion.getMotivo();
    }

    public void setMotivo(MotivoDevolucion motivo) {
        this.devolucion.setMotivo(motivo);
    }

    public String getObservaciones() {
        return devolucion.getObservaciones();
    }

    public void setObservaciones(String observaciones) {
        this.devolucion.setObservaciones(observaciones);
    }

    public TicketS getTicketDevolucion() {
        return ticketDevolucion;
    }

    public void setTicketDevolucion(TicketS ticketDevolucion) {
        this.ticketDevolucion = ticketDevolucion;
    }

    public TicketOrigen getTicketOriginal() {
        return ticketOriginal;
    }

    public void setTicketOriginal(TicketOrigen ticketOriginal) {
        this.ticketOriginal = ticketOriginal;
    }

    public BigDecimal getTotalDevolucion() {
        return totalDevolucion;
    }

    public NotasCredito getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(NotasCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    public Map<Integer, ArticuloDevueltoBean> getArticulosDevueltos() {
        return articulosDevueltos;
    }

    public com.comerzzia.jpos.entity.db.Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(com.comerzzia.jpos.entity.db.Devolucion devolucion) {
        this.devolucion = devolucion;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
        if (autorizador != null && !autorizador.isEmpty()) {
            try {
                this.autorizadorDevolucion = ServicioUsuarios.consultar(autorizador.toUpperCase());
            } catch (Exception ex) {
                log.error("setAutorizadorVenta() - Excepción consultando datos del usuario : " + autorizador);
            }
        }
    }

    public UsuarioBean getAutorizadorDevolucion() {
        return autorizadorDevolucion;
    }

    public void setAutorizadorDevolucion(UsuarioBean autorizadorDevolucion) {
        this.autorizadorDevolucion = autorizadorDevolucion;
    }

    public Cupon getSukuponFacturaOrigen() {
        return sukuponFacturaOrigen;
    }

    public boolean isAnulacion() {
        return anulacion;
    }

    public void setAnulacion(boolean anulacion) {
        this.anulacion = anulacion;
    }

    public Long getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(Long tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
    }

    public String getLocalOrigen() {
        return localOrigen;
    }

    public void setLocalOrigen(String localOrigen) {
        this.localOrigen = localOrigen;
    }

    public List<MedioPagoDTO> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(List<MedioPagoDTO> formasPago) {
        this.formasPago = formasPago;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    
}
