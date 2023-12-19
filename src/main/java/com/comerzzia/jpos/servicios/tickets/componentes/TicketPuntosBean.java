/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCanjeablePuntos;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class TicketPuntosBean {

    protected static Logger log = Logger.getMLogger(TicketPuntosBean.class);
    private Integer puntosAcumulados;
    private Integer puntosConsumidos;
    private Cliente clienteAcumulacion;
    private List<String> articulosRechazadosPuntos;
    private TicketS ticket;
    private List<Long> idPromociones;

    public TicketPuntosBean(TicketS ticket) {
        articulosRechazadosPuntos = new ArrayList<String>();
        idPromociones = new ArrayList<Long>();
        this.ticket = ticket;
        puntosAcumulados = 0;
        puntosConsumidos = 0;
    }

    public Cliente getClienteAcumulacion() {
        return clienteAcumulacion;
    }

    public void setClienteAcumulacion(Cliente clienteAcumulacion) {
        this.clienteAcumulacion = clienteAcumulacion;
    }

    public Integer getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public String getPuntosDisponiblesRenderer(){
        return "Puntos totales disponibles: " + (Sesion.getTicket().getCliente().obtenerPuntos() - puntosConsumidos);
    }
    
    private void addArticuloRechazado(String codigo) {
        if (articulosRechazadosPuntos == null) {
            articulosRechazadosPuntos = new ArrayList<String>();
        }
        articulosRechazadosPuntos.add(codigo);
    }

    public boolean permiteAcumularPuntos() {
        return (clienteAcumulacion != null && !clienteAcumulacion.isClienteGenerico());
    }

    public boolean isArticuloRechazado(String codigo) {
        if (articulosRechazadosPuntos == null) {
            return false;
        }
        return articulosRechazadosPuntos.contains(codigo);
    }

    private ComboArticuloCanjeablePuntos getLineaCandidataCanjeoPuntos() {
        if (Sesion.promocionesCanjeoPuntos.isEmpty()) {
            log.debug("No hay ninguna promoción de canjeo de puntos cargada.");
        }
        for (PromocionTipoPuntosCanjeo promocion : Sesion.promocionesCanjeoPuntos) {
            if (promocion.isAplicableALineas(ticket.getLineas())
                    && promocion.isAplicableAFecha()
                    && promocion.isAplicableACliente(ticket.getCliente())) {
                ComboArticuloCanjeablePuntos lineaCanjeable = promocion.getLineaCandidata(ticket);
                if (lineaCanjeable != null) {
                    return lineaCanjeable;
                }// si no tenemos línea, probamos con la siguiente promoción
            }
        }
        return null;
    }

    public void resetearAcumulacion(){
        clienteAcumulacion = null;
        puntosAcumulados = 0;
    }
    public void resetearCanjeo(){
        if (isConsumePuntos()){
            puntosConsumidos = 0;
            articulosRechazadosPuntos = null;
            ticket.getLineas().resetArticulosCanjeados();
            TicketPromocionesFiltrosPagos promoFiltroPagos = ticket.getTicketPromociones().getPromocionesFiltrosPagos();
            for (PromocionTipoPuntosCanjeo promocion : Sesion.promocionesCanjeoPuntos) {
                promoFiltroPagos.resetPromocion(promocion.getIdPromocion());
                ticket.getLineas().resetearPromocion(promocion.getIdPromocion());
            }
        }
    }
    
    public boolean canjeaPuntosTicket() {
        if (ticket.getCliente().isClienteGenerico()) {
            log.debug("El cliente es genérico, no se aplica canjeo de puntos.");
            return false;
        }
        boolean canjeoPuntosAplicado = false;
        Integer puntosDisponibles = 0;
        ComboArticuloCanjeablePuntos lineaCanjeable = getLineaCandidataCanjeoPuntos();
        if (lineaCanjeable == null) {
            log.debug("No existe ninguna línea de artículo que permita ser canjeada por puntos.");
            return false;
        }
        while (lineaCanjeable != null) {
            puntosDisponibles = ticket.getCliente().obtenerPuntos() - puntosConsumidos;
            if (puntosDisponibles.compareTo(lineaCanjeable.getCantidad()) >= 0) {
                String msg = "¿Desea utilizar "
                        + lineaCanjeable.getCantidad()
                        + " puntos para obtener un "
                        + lineaCanjeable.getDescuento()
                        + " % de descuento en el artículo " + lineaCanjeable.getDescripcion()
                        + "?\n(Actualmente dispone de " + puntosDisponibles + " puntos acumulados)";

                boolean aceptado;
                aceptado = JPrincipal.getInstance().crearVentanaConfirmacion(msg);
                if (aceptado) {
                    lineaCanjeable.getLinea().incrementeCanjeoPuntosCantidadAceptada();
                    canjeoPuntosAplicado = true;
                    puntosConsumidos += lineaCanjeable.getCantidad();
                    ticket.getTicketPromociones().getPromocionesFiltrosPagos().addPromocionesValidaMontoMinimo(lineaCanjeable.getPromocion());
                    if (lineaCanjeable.getPromocion().tieneFiltroPagos()) {
                        TicketPromocionesFiltrosPagos promoFiltroPagos = ticket.getTicketPromociones().getPromocionesFiltrosPagos();
                        promoFiltroPagos.addPromocion(lineaCanjeable.getPromocion(), aceptado);
                    }
                }
                else {
                    addArticuloRechazado(lineaCanjeable.getCodigo());
                }
            }
            else {
                log.debug("El cliente no tiene puntos suficientes para el artículo: " + lineaCanjeable.getCodigo() + " / " + lineaCanjeable.getDescripcion());
                addArticuloRechazado(lineaCanjeable.getCodigo());
            }
            lineaCanjeable = getLineaCandidataCanjeoPuntos();
        }
        if (canjeoPuntosAplicado) {
            ticket.recalcularTotales();
        }
        return canjeoPuntosAplicado;

    }

    public void compruebaPuntosTicket() {
        // Generamos puntos acumulados
        for (PromocionTipoPuntosAcumula promocion : Sesion.promocionesAcumulacionPuntos) {
            if (promocion.isAplicableACliente(ticket.getCliente())
                    && promocion.isAplicableAFecha()
                    && promocion.isReaplicable(ticket)) {
                promocion.compruebaPosibleAcumulacionPuntos(ticket);
            }
        }
    }

    public void acumulaPuntosTicket() {
        // Generamos puntos acumulados
        puntosAcumulados = 0;
        for (PromocionTipoPuntosAcumula promocion : Sesion.promocionesAcumulacionPuntos) {
            log.debug("Intentando acumular puntos para la promoción " + promocion);
            if (promocion.isAplicableACliente(ticket.getCliente())
                    && promocion.isAplicableAFecha()
                    && promocion.isReaplicable(ticket)) {
                puntosAcumulados += promocion.getPuntos(ticket);
            }
        }
    }

    public Integer getPuntosConsumidos() {
        if (puntosConsumidos == null) {
            return 0;
        }
        return puntosConsumidos;
    }

    public boolean isAcumulaPuntos() {
        return puntosAcumulados > 0 && permiteAcumularPuntos();
    }

    public boolean isClienteAcumulaPuntos() {
        return isAcumulaPuntos() && isPuntosParaCliente();
    }

    public boolean isConsumePuntos() {
        return puntosConsumidos != null && puntosConsumidos > 0;
    }

    public Integer getPuntosAnteriores() {
        return ticket.getCliente().obtenerPuntos();
    }

    public Integer getNuevoSaldoPuntos() {
        Integer res = getPuntosAnteriores() - getPuntosConsumidos();
        if (isClienteAcumulaPuntos()) {
            res = res + getPuntosAcumulados();
        }
        return res;
    }

    public boolean isPuntosParaCliente() {
        return (ticket.getCliente().equals(clienteAcumulacion));
    }

    public boolean isPromocionActiva() {
        return isClienteAcumulaPuntos() || isConsumePuntos() || isCedePuntos();
    }

    public boolean isCedePuntos() {
        return isAcumulaPuntos() && !isPuntosParaCliente();
    }

    public List<Long> getIdPromociones() {
        return idPromociones;
    }

    public void setIdPromociones(List<Long> idPromociones) {
        this.idPromociones = idPromociones;
    }
   
}
