/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromoMedioPago;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoRegaloCompra;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author amos
 */
public class TicketPromocionesFiltrosPagos {

    protected static Logger log = Logger.getMLogger(TicketPromocionesFiltrosPagos.class);
    private List<Long> promocionesAceptadas;
    private List<Long> promocionesRechazadas;
    private List<Long> promocionesRechazadasParaSiempre;
    private Set<Long> promocionesAplicacionUnicaCredito;
    private List<Long> promocionesValidaMontoMinimo;
    private TicketPromociones ticketPromociones;
    private Long promoAceptarDS = 0L;

    public TicketPromocionesFiltrosPagos(TicketPromociones ticketPromociones) {
        promocionesAceptadas = new ArrayList<Long>();
        promocionesRechazadas = new ArrayList<Long>();
        promocionesRechazadasParaSiempre = new ArrayList<Long>();
        promocionesAplicacionUnicaCredito = new HashSet<Long>();
        promocionesValidaMontoMinimo = new ArrayList<Long>();
        this.ticketPromociones = ticketPromociones;
    }

    public void addPromocionesValidaMontoMinimo(Promocion promocion) {
        if (promocionesValidaMontoMinimo.indexOf(promocion.getIdPromocion()) == -1) {
            promocionesValidaMontoMinimo.add(promocion.getIdPromocion());
        }
    }

    public void addPromocionesRechazadasParaSiempre(Promocion promocion) {
        promocionesRechazadasParaSiempre.add(promocion.getIdPromocion());
    }

    public void addPromocionAceptada(Promocion promocion) {
        if (!promocionesAceptadas.contains(promocion.getIdPromocion())) {
            promocionesAceptadas.clear();
            promocionesAceptadas.add(promocion.getIdPromocion());
            log.debug("Promoción aceptada por el cliente (Pendiente de incluir medios de pagos requeridos). " + promocion.toString());
        }
    }

    public void addPromocionRechazada(Promocion promocion) {
        if (!promocionesRechazadas.contains(promocion.getIdPromocion())) {
            promocionesRechazadas.add(promocion.getIdPromocion());
            log.debug("Promoción rechazada por el cliente. " + promocion.toString());
        }
    }

    public void addPromoAplicacionUnicaCredito(Promocion promocion) {
        promocionesAplicacionUnicaCredito.add(promocion.getIdPromocion());
    }

    public void addPromocion(Promocion promocion, boolean aceptada) {
        if (aceptada) {
            addPromocionAceptada(promocion);
        } else {
            addPromocionRechazada(promocion);
        }
    }

    public boolean isRechazada(Long idPromocion) {
        return promocionesRechazadas.contains(idPromocion);
    }

    public boolean isRechazadaParaSiempre(Long idPromocion) {
        return promocionesRechazadasParaSiempre.contains(idPromocion);
    }

    public boolean isAceptada(Long idPromocion) {
        return promocionesAceptadas.contains(idPromocion);
    }

    public Promocion isTodosPagosSeleccionados(PagosTicket pagos) {
        Long pagoAceptados = 0L;
        if (promocionesAceptadas.isEmpty()) {
            return null;
        }
        for (Long idPromocion : promocionesAceptadas) {
            Promocion promocion = Sesion.mapaPromociones.get(idPromocion);
            if (!promocion.isAplicableAPagosMixtos(pagos.getPagos(), pagoAceptados)) {
                return promocion;
            }
        }
        return null;
    }

    public Promocion isTodosPagosSeleccionadosPorItem(PagosTicket pagos, List<PromoMedioPago> listaPromoAceptadas) {
        Long pagoAceptados = 0L;
        if (promocionesAceptadas.isEmpty()) {
            return null;
        }
        if (listaPromoAceptadas != null) {
            for (PromoMedioPago promo : listaPromoAceptadas) {
//                    for (Long idPromo : promo.getIdPromo()) {
                Promocion promocion = Sesion.mapaPromociones.get(promo.getIdPromo());
                if (!promocion.isAplicableAPagosMixtos(pagos.getPagos(), pagoAceptados)) {
                    return promocion;
                }
//                    }
            }
        }
        return null;
    }

    public Promocion obtenerPromocion() {
        for (Long idPromocion : promocionesAceptadas) {
            Promocion promocion = Sesion.mapaPromociones.get(idPromocion);

            return promocion;

        }
        return null;
    }

    public Promocion isCumpleMontoMinimo(PagosTicket pagos, LineasTicket lineas) {
        for (Long idPromocion : promocionesValidaMontoMinimo) {
            Promocion promocion = Sesion.mapaPromociones.get(idPromocion);
            if (promocion instanceof PromocionTipoRegaloCompra) {
                PromocionTipoRegaloCompra promoCompra = (PromocionTipoRegaloCompra) promocion;
                if (!promoCompra.isAplicableMontoMinimo(lineas)) {
                    return promocion;
                }
            }
            if (promocion instanceof PromocionTipoPuntosCanjeo) {
                PromocionTipoPuntosCanjeo promoCanjeo = (PromocionTipoPuntosCanjeo) promocion;
                if (!promoCanjeo.isAplicableMontoMinimo(lineas)) {
                    return promocion;
                }
            }
        }
        return null;
    }

    public boolean isPagosCreditoDirectoOK(PagosTicket pagos) throws CreditoException {
        if (promocionesAplicacionUnicaCredito.isEmpty()) {
            return true;
        }

        // limpiamos de las promociones a controlar aquella que no se hayan aplicado
        Iterator<Long> it = promocionesAplicacionUnicaCredito.iterator();
        while (it.hasNext()) {
            Long idPromocion = it.next();
            if (!promocionesAceptadas.contains(idPromocion)) {
                promocionesAplicacionUnicaCredito.remove(idPromocion);
            }
        }

        try {
            Map<Long, Set<Integer>> creditosAMarcar = new HashMap<Long, Set<Integer>>();
            for (Long idPromocion : promocionesAplicacionUnicaCredito) {
                Set<Integer> creditosPromocion = new HashSet<Integer>();
                for (Pago pago : pagos.getPagos()) {
                    if (pago.getMedioPagoActivo().isTarjetaSukasa()) {
                        PagoCreditoSK pagoCredito = (PagoCreditoSK) pago;
                        log.debug("isPagosCreditoDirectoOK() - Comprobamos aplicación de promoción con ID " + idPromocion + " para el pago de crédito directo con tarjeta: " + pagoCredito.getTarjetaCredito().getNumero());
                        int numCredito = CreditoServices.consultarPlasticoPorNumero(pagoCredito.getTarjetaCredito().getNumero()).getNumeroCredito();
                        if (ServicioPromocionesClientes.existePromocionNumCredito(numCredito, idPromocion)) {
                            log.debug("isPagosCreditoDirectoOK() - La promoción con ID " + idPromocion + " no puede ser aplicada porque ya fue aplicada anteriormente para alguno de los pagos de crédito directo seleccionados.");
                            return false;
                        } else {
                            creditosPromocion.add(numCredito);
                        }
                    }
                }
                creditosAMarcar.put(idPromocion, creditosPromocion);
            }
            for (Long idPromoMarcar : creditosAMarcar.keySet()) {
                for (Integer creditoAMarcar : creditosAMarcar.get(idPromoMarcar)) {
                    ticketPromociones.addClientePromoAplicada(idPromoMarcar, creditoAMarcar);
                }
            }
            return true;
        } catch (CreditoNotFoundException e) {
            log.warn("isPagosCreditoDirectoOK() - No se encontró el número de crédito para la tarjeta indicada. No permitimos aplicar la promoción.");
            return true;
        } catch (PromocionException e) {
            log.warn("isPagosCreditoDirectoOK() - Debido al error en la consulta, NO PERMITIMOS APLICAR LA PROMOCIÓN.");
            return false;
        }
    }

    public void reset() {
        log.debug("Reseteando todas las promociones.");
        promocionesAceptadas.clear();
        promocionesRechazadas.clear();
        promocionesValidaMontoMinimo.clear();
        if (!ticketPromociones.isFacturaAsociadaDiaSocio()) {
            promocionesAplicacionUnicaCredito.clear();
        }
    }

    public void resetPromocion(Long idPromocion) {
        log.debug("Reseteando promoción: " + idPromocion);
        promocionesAceptadas.remove(idPromocion);
        promocionesRechazadas.remove(idPromocion);
        promocionesValidaMontoMinimo.remove(idPromocion);
        if (!ticketPromociones.isFacturaAsociadaDiaSocio()) {
            promocionesAplicacionUnicaCredito.remove(idPromocion);
        }
    }

    public Long getPromoAceptarDS() {
        return promoAceptarDS;
    }

    public void setPromoAceptarDS(Long promoAceptarDS) {
        this.promoAceptarDS = promoAceptarDS;
    }

    public void resetPromocionMediosPago(Long idPromocion, LineaTicket lineaTicket) {
        log.debug("Reseteando promoción Medios de Pago por item: " + idPromocion);
        if (!promocionesAceptadas.isEmpty()) {
            if (lineaTicket.getPromocionLineaList() != null) {
                for (PromocionLineaTicket listaPromoItem : lineaTicket.getPromocionLineaList()) {

                    Long idListaPromo = promocionesAceptadas.get(0);
                    if (lineaTicket.getListaPromocion() == null) {
                        if (!Objects.equals(idPromocion, idListaPromo)) {
                                PromoMedioPago promocionesItem = new PromoMedioPago();
                                List<PromoMedioPago> listaPromocionesItem = new ArrayList<PromoMedioPago>();
                                promocionesItem.setIdPromo(listaPromoItem.getIdPromocion());
                                promocionesItem.setValor("N");
                                listaPromocionesItem.add(promocionesItem);
                                lineaTicket.setListaPromocion(listaPromocionesItem);
                        } else {
                            if (listaPromoItem.getIdPromocion().equals(idPromocion)) {
                                PromoMedioPago promocionesItem = new PromoMedioPago();
                                List<PromoMedioPago> listaPromocionesItem = new ArrayList<PromoMedioPago>();
                                promocionesItem.setIdPromo(listaPromoItem.getIdPromocion());
                                promocionesItem.setValor("S");
                                listaPromocionesItem.add(promocionesItem);
                                lineaTicket.setListaPromocion(listaPromocionesItem);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Long> getPromocionesAceptadas() {
        return promocionesAceptadas;
    }

    public void setPromocionesAceptadas(List<Long> promocionesAceptadas) {
        this.promocionesAceptadas = promocionesAceptadas;
    }
}
