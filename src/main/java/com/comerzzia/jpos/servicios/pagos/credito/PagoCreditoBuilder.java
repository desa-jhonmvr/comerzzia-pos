/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.PromocionFormaPagoException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PagoCreditoBuilder {

    public static PagoCredito createPagoCredito(MedioPagoBean medioPago, String autorizador,
            TarjetaCredito tc, TicketS ticket, byte modo) throws ValidationException, PromocionArticuloException, PromocionFormaPagoException {
        PagoCredito pagoCredito = null;

        if (medioPago.isBonoSuperMaxiNavidad() || medioPago.isCreditoFilial()) {
            MedioPagoBean medPag = TarjetaCredito.getBINMedioPagoBanda(tc.getNumero());
            if (medPag != null && (medPag.isBonoSuperMaxiNavidad() || medPag.isCreditoFilial())) {

                pagoCredito = new PagoBonoSuperMaxiNavidad(ticket.getPagos(), ticket.getCliente(), ticket.getIdFactura().substring(ticket.getIdFactura().length() - 6, ticket.getIdFactura().length()), autorizador);
                medioPago.setAdmiteAutorizacionManualCorriente(true);
            } else {
                throw new ValidationException("La tarjeta deslizada no es un bono supermaxi o cr\u00E9dito de temporada");
            }
        } else if (medioPago.isTarjetaSukasa()) {
            pagoCredito = new PagoCreditoSK(ticket.getPagos(), ticket.getCliente(), autorizador);
        } else if (medioPago.isCreditoTemporal()) {
            pagoCredito = new PagoCreditoLetra(ticket.getPagos(), ticket.getCliente(), autorizador);
        } else {
            pagoCredito = new PagoCredito(ticket.getPagos(), ticket.getCliente(), autorizador);
        }
        if (ticket.getTotales() != null) {
            pagoCredito.setTotalGarantiaExtendida(ticket.getTotales().getTotalGarantiaExtendida());
        }
        pagoCredito.setModalidad(modo);
        pagoCredito.setMedioPagoActivo(medioPago);
        pagoCredito.setTarjetaCredito(tc);
        if (tc != null) {
            pagoCredito.setPagoActivo(Pago.PAGO_TARJETA);
        }
        if (medioPago.isBonoSuperMaxiNavidad()) {
            String respuesta;
            String trama;
            String saldoBonoSuper;
            String saldoBonoSuperD;
            Pago p;
            p = new PagoBonoSuperMaxiNavidad(null, null, null, null);

            try {
                if (Variables.getVariable(Variables.ACTIVO_SERVICIO_WEB_SUPERMAXI).equals("S")) {
                    respuesta = p.consultarSaldoWeb(tc.getNumero());

                    trama = PagoCreditoBuilder.ignorarComienzoRespuesta(respuesta);
                    if (trama != null) {
                        saldoBonoSuper = trama.substring(0, 14);
                        int saldo = Integer.parseInt(saldoBonoSuper);
                        String saldoT = String.valueOf(saldo);
                        saldoBonoSuperD = trama.substring(14, 16);
                        String cupo = saldoT + "." + saldoBonoSuperD;
                        BigDecimal cupoTotal = new BigDecimal(cupo);
                        if (cupoTotal.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ValidationException("El bono con n\u00FAmero : " + tc.getNumero() + " no tiene saldo  ");
                        }

                        pagoCredito.setTotalPagoBono(cupoTotal);
                        pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
                        pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
                        pagoCredito.setUstedPaga(cupoTotal);
                        pagoCredito.setNumeroTarjetaBono(tc.getNumero());
                    } else {
                        pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
                        pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
                        pagoCredito.setNumeroTarjetaBono(tc.getNumero());
                    }
                } else {
                    respuesta = p.consultarSaldo(tc.getNumero());

                    trama = PagoCreditoBuilder.ignorarComienzoRespuesta(respuesta);
                    if (trama != null) {
                        saldoBonoSuper = trama.substring(0, 8);
                        int saldo = Integer.parseInt(saldoBonoSuper);
                        String saldoT = String.valueOf(saldo);
                        saldoBonoSuperD = trama.substring(8, 10);
                        String cupo = saldoT + "." + saldoBonoSuperD;
                        BigDecimal cupoTotal = new BigDecimal(cupo);
                        if (cupoTotal.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ValidationException("El bono con n\u00FAmero : " + tc.getNumero() + " no tiene saldo  ");
                        }

                        pagoCredito.setTotalPagoBono(cupoTotal);
                        pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
                        pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
                        pagoCredito.setUstedPaga(cupoTotal);
                        pagoCredito.setNumeroTarjetaBono(tc.getNumero());
                    } else {
                        pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
                        pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
                        pagoCredito.setNumeroTarjetaBono(tc.getNumero());
                    }
                }

            } catch (ValidationException e) {
                throw e;
            } catch (Exception e) {
                pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
                pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
            }
        } else {
            pagoCredito.setSaldoInicial(ticket.getPagos().getSaldo());
            pagoCredito.recalcularPlanesFromTotal(ticket.getPagos().getSaldo());
        }
        return pagoCredito;
    }

    public static String ignorarComienzoRespuesta(String trama) {
        //Si la trama empieza con %B, estamos leyendo la banda magnética y lo ignoramos
        if (trama.contains("APROBADO") || trama.contains("MAXIBONO")) {
            trama = trama.substring(151);
        } else {
            return null;
        }
        return trama;
    }
}
