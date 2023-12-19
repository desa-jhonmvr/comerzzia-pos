/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoBono;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoRetencionFuente;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author amos
 */
public class PagosTicket {

    private static Logger log = Logger.getMLogger(PagosTicket.class);
    private List<Pago> pagos;
    private BigDecimal ustedPaga;
    private BigDecimal saldo;
    private BigDecimal pagado;
    private BigDecimal total;
    private Pago pagoEfectivo;
    private BigDecimal totalAhorro;
    private BigDecimal cantidadPagoBonosDefecto;
    private BigDecimal totalIntereses;
    private TicketS ticket;
    private boolean compensacionAplicada = false;

    
    public PagosTicket() {
        
    }
    
    public PagosTicket(TicketS ticket) {
        pagos = new ArrayList<Pago>();
        this.total = ticket.getTotales().getTotalAPagar();
        resetearTotales();
        this.ticket = ticket;
    }

    public Pago getPagoEfectivo() {
        return pagoEfectivo;
    }

    public Pago getPagoCompensacion() {
        for (Pago pago : pagos) {
            if (pago.getMedioPagoActivo().isCompensacion()) {
                return pago;
            }
        }
        return null;
    }

    private void setPagoEfectivo(Pago pago) {
        if (pagoEfectivo != null) {
            pagos.remove(pagoEfectivo);
        }
        pagoEfectivo = pago;
        pagos.add(pagoEfectivo);
    }

    public boolean contieneNotaCredito(String uidNotaCredito) {
        boolean res = false;
        int i = 0;
        while (i < pagos.size() && !res) {
            if (pagos.get(i) instanceof PagoNotaCredito) {
                if (((PagoNotaCredito) pagos.get(i)).getUidNotaCredito().equals(uidNotaCredito)) {
                    res = true;
                }
            }
            i++;
        }
        return res;
    }

    public boolean contieneBono(String codAlm, Long idbono) {
        boolean res = false;
        int i = 0;
        while (i < pagos.size() && !res) {
            if (pagos.get(i) instanceof PagoBono) {
                if (((PagoBono) pagos.get(i)).getBono().getBonoPK().getCodalm().equals(codAlm)
                        && ((PagoBono) pagos.get(i)).getBono().getBonoPK().getIdBono() == idbono) {
                    res = true;
                }
            }
            i++;
        }
        return res;
    }

    public boolean contieneMedioPago(String codMedioPago) {
        for (Pago pago : pagos) {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals(codMedioPago)) {
                return true;
            }
        }
        return false;
    }

    public Pago getMedioPago(String codMedioPago) {
        for (Pago pago : pagos) {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals(codMedioPago)) {
                return pago;
            }
        }
        return null;
    }

    public BigDecimal getPagado(String codMedioPago) {
        BigDecimal cantidad = BigDecimal.ZERO;
        for (Pago pago : pagos) {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals(codMedioPago)) {
                cantidad = cantidad.add(pago.getUstedPaga());
            }
        }
        return cantidad;
    }

    public boolean contieneGiftCard(String idGiftCard) {
        boolean res = false;
        int i = 0;
        while (i < pagos.size() && !res) {
            if (pagos.get(i) instanceof PagoGiftCard) {
                if (((PagoGiftCard) pagos.get(i)).getGiftCard().getIdGiftCard().equals(idGiftCard)) {
                    res = true;
                }
            }
            i++;
        }
        return res;
    }

    public BigDecimal getMovimientoEfectivo() {
        BigDecimal res = BigDecimal.ZERO;
        if (this.pagoEfectivo != null) {
            if (this.pagoEfectivo.getUstedPaga() != null) {
                res = this.pagoEfectivo.getUstedPaga();
            }
        }
        return res;
    }

    public void addPago(Pago pag) {
        if (pag.isPagoEfectivo()) {
            setPagoEfectivo(pag);
        } else {
            pagos.add(pag);
        }
        recalculaTotales();
    }

    public void recalculaTotales() {
        resetearTotales();
        for (Pago pago : pagos) {
            log.debug("Antes " + pago.getMedioPagoActivo() + " Saldo: " + saldo + " Pago Total: " + pago.getTotal() + " Usted Paga: " + ustedPaga);
            if (pago.getMedioPagoActivo().isRetencion() || pago.getMedioPagoActivo().isCompensacion()) {
                ustedPaga = ustedPaga.add(pago.getTotal());
            } else {
                ustedPaga = ustedPaga.add(pago.getUstedPaga());
                saldo = saldo.subtract(pago.getTotal());
                //saldo = saldo.subtract(pago.getUstedPaga());
                totalAhorro = totalAhorro.add(pago.getAhorroRenderer());
                totalIntereses = totalIntereses.add(pago.getImporteInteres());
                if (pago.getRetencion() != null && (pago.getMedioPagoActivo().isTarjetaCredito()
                        || pago.getMedioPagoActivo().isTarjetaSukasa() || pago.getMedioPagoActivo().isCreditoTemporal())) {
                    saldo = saldo.subtract(pago.getRetencion().getTotal());
                }
            }
            if (saldo.compareTo((new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_UP))) == 0) {
                saldo = BigDecimal.ZERO;
            }
            if (saldo.compareTo((new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP))) < 0) {
                if (pago.getMedioPagoActivo().isRetencion()) {
                    saldo = BigDecimal.ZERO;
                }
            }

            log.debug("Despues " + pago.getMedioPagoActivo() + " Saldo: " + saldo + " Pago Total: " + pago.getTotal() + " Usted Paga: " + ustedPaga);
        }
        pagado = total.subtract(saldo);

        if (!ticket.isModoVenta()) {
            return;
        }

        // Compensación gobierno
        if (!isSaldoCero()) {
            if (compensacionAplicada) {
                compensacionAplicada = false;
                for (Pago pago : pagos) {
                    pago.recalcularSinCompensacionGobierno();
                }
            }
        }
        if (compensacionAplicada || !isSaldoCero()) {
            return;
        }
        ticket.recalcularFinalPagado();

        BigDecimal porcentaje = VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO);
        BigDecimal base = ticket.getTotales().getSubtotal12();
        BigDecimal compensacion = Numero.porcentajeR(base, porcentaje);

        if (Numero.isMayorACero(porcentaje)) {
            BigDecimal compensacion_Aux = compensacion;
            if (pagoEfectivo != null) {
                compensacion_Aux = pagoEfectivo.recalcularConCompensacionGobierno(compensacion_Aux);
            }
            Pago pagoTarjeta = null;
            Pago pagoOtros = null;
            Pago pagoContado = null;
            Pago pagoLetra = null;
            List<Pago> pagosAux = new ArrayList<Pago>();
            for (Pago pago : pagos) {
                pagosAux.add(pago);
            }
            Collections.sort(pagosAux);
            for (Pago pago : pagosAux) {
                if (Numero.isMayorACero(compensacion_Aux)) {
                    if (pago.isPagoTarjeta() && pagoTarjeta == null) {
                        pagoTarjeta = pago;
                        compensacion_Aux = pagoTarjeta.recalcularConCompensacionGobierno(compensacion_Aux);
                    } else if (pago.isPagoContado() && pagoContado == null) {
                        pagoContado = pago;
                        compensacion_Aux = pagoContado.recalcularConCompensacionGobierno(compensacion_Aux);
                    } else if (pago.isPagoOtros() && pagoOtros == null) {
                        pagoOtros = pago;
                        compensacion_Aux = pagoOtros.recalcularConCompensacionGobierno(compensacion_Aux);
                    } else {
                        pagoLetra = pago;
                        compensacion_Aux = pagoLetra.recalcularConCompensacionGobierno(compensacion_Aux);
                    }
                }
            }
            if (compensacion.compareTo(BigDecimal.ZERO) > 0) {
                ticket.getTotales().setCompensacionGobierno(compensacion);
                compensacionAplicada = true;
            }
        }
        // Fin pago compensación
    }

    private void resetearTotales() {
        log.debug("Refrescando totales ");
        ustedPaga = BigDecimal.ZERO;
        pagado = BigDecimal.ZERO;
        totalAhorro = BigDecimal.ZERO;
        totalIntereses = BigDecimal.ZERO;
        saldo = total;
    }

    public BigDecimal calculaAhorroTotal() {
        BigDecimal ahorroTotal = BigDecimal.ZERO;
        for (Pago pago : pagos) {
            if (pago instanceof PagoRetencionFuente) {
                continue;
            }
            ahorroTotal = ahorroTotal.add(pago.getAhorroExacto());
        }
        return ahorroTotal; //.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isSaldoCero() {
        return this.saldo.compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getSaldo() {
        if (saldo == null) {
            return cantidadPagoBonosDefecto;
        }
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUstedPaga() {
        return ustedPaga;
    }

    public void setUstedPaga(BigDecimal ustedPaga) {
        this.ustedPaga = ustedPaga;
    }

    public void eliminarPago(int i) {
        try {
            Pago p = pagos.get(i);
            if (p.isPagoEfectivo()) {
                pagoEfectivo = null;
            }
            if (p instanceof PagoRetencionFuente) {
                // si estamos borrando una retención a la fuente buscamos el pago retenido para recalcularlo
                for (Pago pago : pagos) {
                    if (pago.isRetenido()) {
                        pago.recalcularSinRetencionFuente((PagoRetencionFuente) p);
                    }
                }
            }
            pagos.remove(i);
            recalculaTotales();
        } catch (ArrayIndexOutOfBoundsException aex) {
            //No existe el pago que se intena borrar
        } catch (Exception e) {
            log.error("Excepción no controlada al eliminar pago", e);
        }
    }

    public int getIndexUltimaLinea() {
        return pagos.size() - 1;
    }

    public BigDecimal getTotalAhorro() {
        return totalAhorro;
    }

    public PagoRetencionFuente crearPagoRetencionFuente(BigDecimal base, BigDecimal iva, BigDecimal valorRetencion) throws PagoInvalidException {
        ticket.recalcularFinalPagado();

        PagoRetencionFuente pagoRetencion = new PagoRetencionFuente(this);
        if (base == null) {
            if (ticket.getPagos().getPagoEfectivo() != null) {
                pagoRetencion.inicializar(ticket.getPagos().getPagoEfectivo().getTotal(), ticket.getPagos().getPagoEfectivo().getIva(), ticket.getPagos().getPagoEfectivo().getDescuento(), valorRetencion);
            } else {
                pagoRetencion.inicializar(ticket.getTotales().getBase(), ticket.getTotales().getImpuestos(), ticket.getTotales().getTotalDtoPagos(), valorRetencion);
            }
        } else {
            pagoRetencion.inicializar(base, iva, BigDecimal.ZERO, valorRetencion);
        }

        BigDecimal compensacion = BigDecimal.ZERO;
        if (compensacionAplicada) {
            BigDecimal porcentaje = VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO);
            BigDecimal base12 = ticket.getTotales().getSubtotal12();
            compensacion = Numero.porcentajeR(base12, porcentaje);
        }

        if (pagoEfectivo != null && pagoEfectivo.getTotal().compareTo(valorRetencion) > 0) {
            pagoEfectivo.recalcularConRetencionFuente(pagoRetencion, compensacion);
        } else {
            Pago pagoTarjeta = null;
            Pago pagoOtros = null;
            Pago pagoContado = null;
            Pago pagoLetra = null;
            for (Pago pago : pagos) {
                if (pago.isPagoTarjeta() && pagoTarjeta == null) {
                    pagoTarjeta = pago;
                } else if (pago.isPagoContado() && pagoContado == null) {
                    pagoContado = pago;
                } else if (pago.isPagoOtros() && pagoOtros == null) {
                    pagoOtros = pago;
                } else {
                    pagoLetra = pago;
                }
            }
            if (pagoTarjeta != null) {
                pagoTarjeta.recalcularConRetencionFuente(pagoRetencion, compensacion);
            } else if (pagoContado != null) {
                pagoContado.recalcularConRetencionFuente(pagoRetencion, compensacion);
            } else if (pagoOtros != null) {
                pagoOtros.recalcularConRetencionFuente(pagoRetencion, compensacion);
            } else if (pagoLetra != null) {
                pagoLetra.recalcularConRetencionFuente(pagoRetencion, compensacion);
            }
        }
        return pagoRetencion;
    }

    public PagoRetencionFuente contieneRetencion() {
        PagoRetencionFuente res = null;
        int i = 0;
        while (i < pagos.size() && res == null) {
            if (pagos.get(i) instanceof PagoRetencionFuente) {
                res = (PagoRetencionFuente) pagos.get(i);
            }
            i++;
        }

        return res;
    }

    public boolean contieneEfectivo() {
        Boolean contiene = false;
        if (pagoEfectivo != null) {
            contiene = true;
        }
        return contiene;
    }

    public BigDecimal getCantidadPagoBonosDefecto() {
        return cantidadPagoBonosDefecto;
    }

    public BigDecimal getImportePromocionado(Long idPromocion) {
        BigDecimal acumulado = BigDecimal.ZERO;
        for (Pago pago : pagos) {
            PromocionPagoTicket promocion = pago.getPromocionAplicada(idPromocion);
            if (promocion != null && promocion.getIdPromocion().equals(idPromocion)) {
                acumulado = acumulado.add(promocion.getImporteBasePromocion());
            }
        }
        return acumulado;
    }

    public void setCantidadPagoBonosDefecto(BigDecimal cantidadPagoBonosDefecto) {
        this.cantidadPagoBonosDefecto = cantidadPagoBonosDefecto;
    }

    public boolean abrirCajon() {
        boolean result = false;
        for (Pago p : pagos) {
            if (p.getMedioPagoActivo().isAbrirCajon()) {
                result = true;
                break;
            }
        }

        return result;
    }

    public String anularAutorizacionPagos(List<Pago> anuladosImpresos) {
        log.debug("anularAutorizacionPagos() - Anulando todos los pagos realizados...");
        log.debug("anularAutorizacionPagos() - Se van a anular: " + pagos.size() + " pagos");
        String error = "";
        for (int i = 0; i < pagos.size(); i++) {
            Pago p = pagos.get(i);
            try {
                log.debug("anularAutorizacionPagos() - Anulando el pago: " + p.toString());
                p.anularAutorizacionPago(anuladosImpresos);
                log.debug("anularAutorizacionPagos() - El pago " + p.toString() + " ha sido anulado correctamente");
            } catch (AutorizadorException e) {
                if (p instanceof PagoCredito) {
                    boolean res = JPrincipal.getInstance().crearVentanaConfirmacion("El pago con " + p.getMedioPagoActivo().getDesMedioPago() + " de " + p.getUstedPaga() + " no ha podido ser anulado.", "Reintentar", "Cancelar");
                    if (res) { // Reintentar
                        i--;
                    } else { // Cancelar
                        ((PagoCredito) p).setValidadoAutomatico(false);
                        error += "El pago con " + p.getMedioPagoActivo().getDesMedioPago() + " de " + p.getUstedPaga() + " no ha podido ser anulado.\n ";
                    }
                } else {
                    error += "El pago con " + p.getMedioPagoActivo().getDesMedioPago() + " de " + p.getUstedPaga() + " no ha podido ser anulado.\n ";
                }
            }
        }
        return error;
    }
 
    public boolean hayPagosAutorizados() {
        for (Pago p : pagos) {
            if (p.isValidado()) {
                return true;
            }
        }
        return false;
    }

    public boolean contienePago(Pago pag) {
        boolean res = false;
        for (Pago p : pagos) {
            if (p.getMedioPagoActivo().equals(pag.getMedioPagoActivo())) {
                if (pag instanceof PagoCredito) {
                    if (((PagoCredito) pag).getPlanSeleccionado().getIdPlan() == ((PagoCredito) p).getPlanSeleccionado().getIdPlan()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return res;
    }

    /**
     * Devuelve el cambio del pago efectivo
     *
     * @return
     */
    public BigDecimal getCambio() {
        BigDecimal cambio = BigDecimal.ZERO;
        if (pagoEfectivo != null) {
            cambio = pagoEfectivo.getVueltaAsBD();
        }

        return cambio.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Devuelve el cambio de todos los pagos
     */
    public BigDecimal getCambioTotal() {
        BigDecimal cambio = BigDecimal.ZERO;
        for (Pago p : pagos) {
            if (p instanceof PagoRetencionFuente) {
                continue;
            }
            cambio = cambio.add(p.getVueltaAsBD());
        }
        return cambio.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean contieneMedioPagoCreditoTemporal() {
        for (Pago p : pagos) {
            if (p.getMedioPagoActivo().isCreditoTemporal()) {
                return true;
            }
        }
        return false;
    }

    public List<PagoCreditoSK> getMediosPagoCreditoDirecto() {
        List<PagoCreditoSK> pagosCreditoDirecto = new ArrayList<PagoCreditoSK>();
        for (Pago p : pagos) {
            if (p.getMedioPagoActivo().isTarjetaSukasa()) {
                pagosCreditoDirecto.add((PagoCreditoSK) p);
            }
        }
        return pagosCreditoDirecto;
    }

    public boolean isCompensacionAplicada() {
        return compensacionAplicada;
    }

    public void setCompensacionAplicada(boolean compensacionAplicada) {
        this.compensacionAplicada = compensacionAplicada;
    }

    public boolean contieneTarjetaCredito() {
        if (pagos != null) {
            for (Pago p : pagos) {
                if (p instanceof PagoCredito) {
                    return true;
                }
            }
        }
        return false;
    }

    public void recalculaPagosBase0() {
        log.debug("recalculaPagosBase0() - Recalculando la base 0 de los pagos");
        if (pagos != null) {
            for (Pago p : pagos) {
                p.setSubtotalIva0(p.getUstedPaga());
                p.setSubtotalIva12(BigDecimal.ZERO);
                p.setIva(BigDecimal.ZERO);
            }
        }
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

}
