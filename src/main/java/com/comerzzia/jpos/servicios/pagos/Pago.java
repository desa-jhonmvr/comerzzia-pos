/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoRetencionFuente;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author amos
 */
public class Pago implements Comparable<Pago> {

    private static Logger log = Logger.getMLogger(Pago.class);
    public static final byte MODO_NORMAL = 1;  // MODO_VENTA 
    public static final byte MODO_GIFTCARD = 2;
    public static final byte MODO_ABONO_RESERVA = 3;
    public static final byte MODO_ABONO_PLAN = 4;
    public static final byte MODO_ARTICULOS = 6;
    public static final byte MODO_ARTICULOS_PLAN = 7;
    public static final byte MODO_CREDITO_DIRECTO = 9;
    public static final byte MODO_LETRA_CAMBIO = 10;
    public static final byte PAGO_EFECTIVO = 1;
    public static final byte PAGO_CONTADO = 2;
    public static final byte PAGO_TARJETA = 3;
    public static final byte PAGO_OTROS = 4;
    protected BigDecimal saldoInicial;
    protected BigDecimal totalSinIva;
    protected BigDecimal ustedPagaSinIva;
    protected BigDecimal total;
    protected BigDecimal ustedPaga;
    protected BigDecimal descuento;
    protected BigDecimal entregado;
    protected byte pagoActivo;
    protected MedioPagoBean medioPagoActivo;
    protected boolean clienteAfiliado;
    protected Long tipoCliente;
    protected String informacionExtra1 = "";
    protected String informacionExtra2 = "";
    protected String informacionExtra3 = "";
    protected PagoRetencionFuente retencion;
    protected PagosTicket pagos;
    private String autorizador;
    // subtotales de iva
    protected BigDecimal subtotalIva12;
    protected BigDecimal subtotalIva0;
    protected BigDecimal iva;
    protected BigDecimal impuesto;
    protected byte modalidad;
    protected Cliente cliente;
    protected List<PromocionPagoTicket> promociones;
    protected BigDecimal totalGarantiaExtendida = BigDecimal.ZERO;
    protected BigDecimal descuentoCalculado;
    protected BigDecimal yaPagado = BigDecimal.ZERO; //Almacena la suma abonada en todos los pagos
    protected BigDecimal yaPagadoEfectivo = BigDecimal.ZERO; //Almacena la suma abonada en todos los pagos
    //Campos auxiliares para anulación
    private String referencia;
    private BigDecimal compensacionGobierno;
    private TicketS ticket;
    private LineaTicket linea;
    //Id para solucionar problema varios pagos tarjeta
    private String idTramaSuper;
    //Respuesta de la consulta del Supermaxi.
    private String respuesta;

    public Pago() {

    }

    public Pago(PagosTicket pagosTicket, Cliente cliente) {
        if (cliente == null) {
            setTipoCliente(false, null);
        } else {
            setTipoCliente(cliente.isSocio(), cliente.getTipoCliente());
        }
        this.pagos = pagosTicket;
        modalidad = MODO_NORMAL;
        this.cliente = cliente;
        if (pagos != null) {
            yaPagado = pagos.getPagado();
            if (pagos.getPagoEfectivo() != null) {
                yaPagadoEfectivo = pagos.getPagoEfectivo().getTotal();
            }
        }
    }

    public Pago(PagosTicket pagosTicket, Cliente cliente, TotalesXML totales, String autorizador) {
        if (cliente == null) {
            setTipoCliente(false, null);
        } else {
            setTipoCliente(cliente.isSocio(), cliente.getTipoCliente());
        }
        this.pagos = pagosTicket;
        modalidad = MODO_NORMAL;
        this.cliente = cliente;

        if (pagos != null) {
            yaPagado = pagos.getPagado();
            if (pagos.getPagoEfectivo() != null) {
                yaPagadoEfectivo = pagos.getPagoEfectivo().getTotal();
            }
        }

        if (totales != null) {
            this.totalGarantiaExtendida = totales.getTotalGarantiaExtendida();
        }
        if (autorizador != null) {
            this.autorizador = autorizador;
        }
    }

    public void setModalidad(byte modo) {
        modalidad = modo;
    }

    public byte getModalidad() {
        return modalidad;
    }

    public boolean isClienteAfiliado() {
        return clienteAfiliado;
    }

    public Long getTipoCliente() {
        return tipoCliente;
    }

    private void setTipoCliente(boolean clienteAfiliado, Long tipoCliente) {
        this.clienteAfiliado = clienteAfiliado;
        this.tipoCliente = tipoCliente;
    }

    public void resetear(BigDecimal total) {
        this.total = total;
        saldoInicial = total;

        BigDecimal dto = medioPagoActivo.getVencimientoDefault().getDescuento(this, Sesion.isAfiliadoPromo());
        establecerDescuento(dto);
    }

    public void recalcularFromTotal(String total) throws PagoInvalidException {
        try {
            recalcularFromTotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
        } catch (NumberFormatException e) {
            //this.total = BigDecimal.ZERO;
            //ustedPaga = BigDecimal.ZERO;
            //entregado = BigDecimal.ZERO;
            throw new PagoInvalidException("El valor indicado no es un número correcto.");
        }
    }

    public void recalcularFromTotal(BigDecimal total) {
        this.total = total;
        if (this.total.compareTo(saldoInicial) > 0) {
            this.total = saldoInicial;
        }
        establecerDescuento(this.descuento);
    }

    public void recalcularFromUstedPaga(BigDecimal ustedPaga) {
        try {
            BigDecimal entregadoAux = getEntregado();
            recalcularFromUstedPaga(ustedPaga.toString());
            setEntregado(entregadoAux);
        } catch (PagoInvalidException e) {
            log.error("recalcularFromUstedPaga() - Error al recalcular: " + ustedPaga, e);
        }
    }

    public void recalcularFromUstedPagaNotaCredito(String ustedPaga, PagosTicket pagos) throws PagoInvalidNCException {
        try {
            if (pagos != null) {
                yaPagado = pagos.getPagado();
                if (pagos.getPagoEfectivo() != null) {
                    yaPagadoEfectivo = pagos.getPagoEfectivo().getTotal();
                }
            }
            if (yaPagado.compareTo(totalGarantiaExtendida) >= 0
                    || (new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(totalGarantiaExtendida) >= 0
                    || new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0)) {
                this.ustedPaga = new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP);
                entregado = this.ustedPaga;
                calculaDescuento(descuento);
                BigDecimal ustedPagaParaSaldoInicial = Numero.menosPorcentajeR(saldoInicial, descuentoCalculado);
                if (getUstedPaga().equals(ustedPagaParaSaldoInicial)) {
                    try {
                        recalcularFromTotal(saldoInicial.toString());
                    } catch (PagoInvalidException ex) {
                        java.util.logging.Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    BigDecimal a = BigDecimal.ZERO;
                    BigDecimal b = Numero.CIEN.subtract(descuento);
                    if (yaPagado.compareTo(totalGarantiaExtendida) > 0) {
                        a = Numero.CIEN.multiply(this.ustedPaga);
                        total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        if ((yaPagado.add(getUstedPaga())).compareTo(totalGarantiaExtendida) > 0) {
                            a = Numero.CIEN.multiply((yaPagado.add(this.ustedPaga)).subtract(totalGarantiaExtendida));
                            total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                            total = total.add(totalGarantiaExtendida);
                        } else {
                            total = getUstedPaga();
                        }
                    }
                    if (this.total.compareTo(saldoInicial) > 0) {
                        try {
                            recalcularFromTotal(saldoInicial.toString());
                        } catch (PagoInvalidException ex) {
                            java.util.logging.Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                BigDecimal valor = totalGarantiaExtendida.add(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP);
                throw new PagoInvalidNCException("El primer pago debe ser mayor al valor de la garantia extendida de " + valor);
            }

        } catch (NumberFormatException e) {
            this.ustedPaga = BigDecimal.ZERO;
            total = BigDecimal.ZERO;
            entregado = BigDecimal.ZERO;
            throw new PagoInvalidNCException("El valor indicado no es un número correcto.");
        }
    }

    public void recalcularFromUstedPagaSupermaxi(String ustedPaga, PagosTicket pagos) throws PagoInvalidSupermaxiException {
        try {
            if (pagos != null) {
                yaPagado = pagos.getPagado();
                if (pagos.getPagoEfectivo() != null) {
                    yaPagadoEfectivo = pagos.getPagoEfectivo().getTotal();
                }
            }
            if (yaPagado.compareTo(totalGarantiaExtendida) >= 0
                    || (new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(totalGarantiaExtendida) >= 0
                    || new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0)) {
                this.ustedPaga = new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP);
                entregado = this.ustedPaga;
                calculaDescuento(descuento);
                BigDecimal ustedPagaParaSaldoInicial = Numero.menosPorcentajeR(saldoInicial, descuentoCalculado);
                if (getUstedPaga().equals(ustedPagaParaSaldoInicial)) {
                    try {
                        recalcularFromTotal(saldoInicial.toString());
                    } catch (PagoInvalidException ex) {
                        java.util.logging.Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    BigDecimal a = BigDecimal.ZERO;
                    BigDecimal b = Numero.CIEN.subtract(descuento);
                    if (yaPagado.compareTo(totalGarantiaExtendida) > 0) {
                        a = Numero.CIEN.multiply(this.ustedPaga);
                        total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        if ((yaPagado.add(getUstedPaga())).compareTo(totalGarantiaExtendida) > 0) {
                            a = Numero.CIEN.multiply((yaPagado.add(this.ustedPaga)).subtract(totalGarantiaExtendida));
                            total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                            total = total.add(totalGarantiaExtendida);
                        } else {
                            total = getUstedPaga();
                        }
                    }
                    if (this.total.compareTo(saldoInicial) > 0) {
                        try {
                            recalcularFromTotal(saldoInicial.toString());
                        } catch (PagoInvalidException ex) {
                            java.util.logging.Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                BigDecimal valor = totalGarantiaExtendida.add(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP);
                throw new PagoInvalidSupermaxiException("El primer pago debe ser mayor al valor de la garantia extendida de " + valor);
            }

        } catch (NumberFormatException e) {
            this.ustedPaga = BigDecimal.ZERO;
            total = BigDecimal.ZERO;
            entregado = BigDecimal.ZERO;
            throw new PagoInvalidSupermaxiException("El valor indicado no es un número correcto.");
        }
    }

    public void recalcularFromUstedPaga(String ustedPaga) throws PagoInvalidException {
        try {
            if (yaPagado.compareTo(totalGarantiaExtendida) >= 0
                    || (new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(totalGarantiaExtendida) >= 0
                    || new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0)) {
                this.ustedPaga = new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP);
                entregado = this.ustedPaga;
                calculaDescuento(descuento);
                BigDecimal ustedPagaParaSaldoInicial = Numero.menosPorcentajeR(saldoInicial, descuentoCalculado);
                if (getUstedPaga().equals(ustedPagaParaSaldoInicial)) {
                    recalcularFromTotal(saldoInicial.toString());
                } else {
                    BigDecimal a = BigDecimal.ZERO;
                    BigDecimal b = Numero.CIEN.subtract(descuento);
                    if (yaPagado.compareTo(totalGarantiaExtendida) > 0) {
                        a = Numero.CIEN.multiply(this.ustedPaga);
                        total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        if ((yaPagado.add(getUstedPaga())).compareTo(totalGarantiaExtendida) > 0) {
                            a = Numero.CIEN.multiply((yaPagado.add(this.ustedPaga)).subtract(totalGarantiaExtendida));
                            total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                            total = total.add(totalGarantiaExtendida);
                        } else {
                            total = getUstedPaga();
                        }
                    }
                    //total = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
                    if (this.total.compareTo(saldoInicial) > 0) {
                        recalcularFromTotal(saldoInicial.toString());
                    }
                }
            } else {
                BigDecimal valor = totalGarantiaExtendida.add(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP);
                throw new PagoInvalidException("El primer pago debe ser mayor al valor de la garantia extendida de " + valor);
            }

        } catch (NumberFormatException e) {
            //this.ustedPaga = BigDecimal.ZERO;
            //total = BigDecimal.ZERO;
            //entregado = BigDecimal.ZERO;
            throw new PagoInvalidException("El valor indicado no es un número correcto.");
        }
    }

    public void establecerDescuento(BigDecimal descuento) {
        this.descuento = descuento;

        calculaDescuento(descuento);
        BigDecimal descuentoFundas = new BigDecimal("0.0");
        Long fundas = 0L;
        descuentoFundas = pagos.getTicket().getTotales().getTotalDescuentoMediosPagoSinImpuestosF(descuento);
        fundas = pagos.getTicket().getTotales().getTotalSinPromocionCabeceraYMedioPagoPantallaFundas();
        if (fundas == 0L) {
            if (total.compareTo(BigDecimal.ZERO) != 0) {
                ustedPaga = total.subtract(descuentoFundas);
            } else {
                ustedPaga = BigDecimal.ZERO;
            }
            entregado = getUstedPaga();
        } else {
            this.descuento = descuento;
            calculaDescuento(descuento);
            ustedPaga = total.subtract(total.multiply(this.descuentoCalculado).divide(Numero.CIEN)).setScale(2, BigDecimal.ROUND_HALF_UP);
            entregado = getUstedPaga();
        }
    }

    public void establecerDescuentoF(BigDecimal descuento) {
        this.descuento = descuento;

        calculaDescuento(descuento);

        ustedPaga = total.subtract(total.multiply(this.descuentoCalculado).divide(Numero.CIEN)).setScale(2, BigDecimal.ROUND_HALF_UP);

        entregado = getUstedPaga();
    }

    public BigDecimal calculaDescuento(BigDecimal descuento) {
        BigDecimal dto;

        if (yaPagado.compareTo(totalGarantiaExtendida) >= 0) {
            dto = descuento;
        } else {//Aún no se ha cubierto el coste de la garantía extendida
            if ((yaPagado.add(total)).compareTo(totalGarantiaExtendida) > 0) {
                BigDecimal total_aux = total.subtract(totalGarantiaExtendida);
                BigDecimal aux = total_aux.subtract(total_aux.multiply(descuento).divide(Numero.CIEN)).setScale(2, BigDecimal.ROUND_HALF_UP);
                aux = aux.add(totalGarantiaExtendida);

                dto = (Numero.CIEN).subtract(aux.multiply(Numero.CIEN).divide(total, 2, BigDecimal.ROUND_HALF_UP));
                //dto = (Numero.CIEN).subtract(aux.multiply(Numero.CIEN).divide(total, 4, BigDecimal.ROUND_HALF_UP));
                if (dto.compareTo(BigDecimal.ZERO) < 0) {
                    //dto =(Numero.CIEN.subtract(dto)).divide(Numero.CIEN);
                    //BigDecimal auxTemp = total.add(total_aux).multiply(descuento).divide(Numero.CIEN).setScale(2, BigDecimal.ROUND_HALF_UP);
                    // dto = (Numero.CIEN).subtract(auxTemp.multiply(Numero.CIEN).divide(total, 2, BigDecimal.ROUND_HALF_UP));
                    dto = BigDecimal.ZERO;
                }
            } else {
                dto = BigDecimal.ZERO;
            }
        }
        /*CAMBIO POR DIA DEL SOCIO QUE CUANDO UN CLIENTE TIENE EL 15% DE DESC LE COLOQUE 10% SR*/
        if (dto != null && pagos != null && pagos.getTicket() != null
                && pagos.getTicket().getTicketPromociones() != null && dto.longValue() > 10 && pagos.getTicket().getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            dto = new BigDecimal(10.00);
            this.descuentoCalculado = dto;
        }
        this.descuentoCalculado = dto;
        return dto;
    }

    public void establecerEntregado(String entregado) throws PagoInvalidException {
        try {
            this.entregado = new BigDecimal(entregado).setScale(2, RoundingMode.HALF_UP);
            if (getEntregado().compareTo(getUstedPaga()) < 0) {
                throw new PagoInvalidException("La cantidad entregada debe ser igual o mayor a la cantidad pagada.");
            }
        } catch (NumberFormatException e) {
            this.entregado = BigDecimal.ZERO;
            throw new PagoInvalidException("El valor indicado no es un número correcto.");
        }
    }

    public void validar() throws PagoInvalidException {
        if (getEntregado().compareTo(getUstedPaga()) < 0) {
            throw new PagoInvalidException("La cantidad entregada debe ser igual o mayor a la cantidad pagada.");
        }
        if (this.ustedPaga.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoInvalidException("El pago tiene que realizarse por una cantidad mayor que cero.");
        }
        if (this.total.compareTo(saldoInicial) > 0) {
            throw new PagoInvalidException("El total a pagar no puede ser mayor que el saldo pendiente.");
        }
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getDescuento() {
        if (descuento != null) {
            return descuento.setScale(2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void recalcularConRetencionFuente(PagoRetencionFuente pago, BigDecimal compensacion) {
        if (getMedioPagoActivo().isEfectivo() && !Numero.isIgual(getEntregado(), getUstedPaga())) {
            BigDecimal entregadoAux = Numero.redondear(new BigDecimal(this.entregado.toString()));
            setUstedPaga(getUstedPaga().subtract(pago.getUstedPaga()));
            setUstedPaga(getUstedPaga().subtract(compensacion));
            retencion = pago;
            this.entregado = entregadoAux;
        } else {
            setUstedPaga(getUstedPaga().subtract(pago.getUstedPaga()));
            setUstedPaga(getUstedPaga().subtract(compensacion));
            retencion = pago;
        }
    }

    public BigDecimal recalcularConCompensacionGobierno(BigDecimal compensacion) {
        BigDecimal compAux = compensacion;
        if (compensacion.compareTo(getUstedPaga()) > 0) {
            compAux = getUstedPaga();
        }
        if (getMedioPagoActivo().isEfectivo() && !Numero.isIgual(getEntregado(), getUstedPaga())) {
            BigDecimal entregadoAux = Numero.redondear(new BigDecimal(getEntregado().toString()));
            setUstedPaga(getUstedPaga().subtract(compAux));
            this.entregado = entregadoAux;
        } else {
            setUstedPaga(getUstedPaga().subtract(compAux));
        }
        return compensacion.subtract(compAux);
    }

    public void recalcularSinCompensacionGobierno() {
        if (compensacionGobierno == null || Numero.isIgualACero(compensacionGobierno)) {
            return;
        }
        if (getMedioPagoActivo().isEfectivo() && !Numero.isIgual(getEntregado(), getUstedPaga())) {
            BigDecimal entregadoAux = new BigDecimal(getEntregado().toString());
            recalcularFromTotal(getTotal().add(compensacionGobierno));
            this.retencion = null;
            this.entregado = entregadoAux;
        } else {
            recalcularFromTotal(getTotal().subtract(compensacionGobierno));
            retencion = null;
        }
    }

    public void recalcularSinRetencionFuente(PagoRetencionFuente pago) {
        if (getMedioPagoActivo().isEfectivo() && !Numero.isIgual(getEntregado(), getUstedPaga())) {
            BigDecimal entregadoAux = new BigDecimal(getEntregado().toString());
            recalcularFromTotal(getTotal().add(pago.getTotal()));
            this.retencion = null;
            this.entregado = entregadoAux;
        } else {
            recalcularFromTotal(getTotal().subtract(pago.getTotal()));
            retencion = null;
        }
    }

    public BigDecimal getEntregado() {
        return entregado;
    }

    public void setEntregado(BigDecimal entregado) {
        this.entregado = entregado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getUstedPaga() {
        return ustedPaga;
    }

    public BigDecimal getUstedPagaPrint() {
        return getUstedPaga();
    }

    public BigDecimal getUstedPagaConIntereses() {
        return ustedPaga.add(getImporteInteres());
    }

    public MedioPagoBean getMedioPagoActivo() {
        return medioPagoActivo;
    }

    public void setMedioPagoActivo(MedioPagoBean medioPagoActivo) {
        this.medioPagoActivo = medioPagoActivo;
    }

    public void setPagoActivo(byte pagoActivo) {
        this.pagoActivo = pagoActivo;
    }

    public byte getPagoActivo() {
        return (pagoActivo);
    }

    public boolean isPagoEfectivo() {
        return pagoActivo == PAGO_EFECTIVO;
    }

    public boolean isPagoContado() {
        return pagoActivo == PAGO_CONTADO;
    }

    public boolean isPagoOtros() {
        return pagoActivo == PAGO_OTROS;
    }

    public boolean isPagoTarjeta() {
        return pagoActivo == PAGO_TARJETA;
    }

    public boolean isPagoGiftCard() {
        return (this instanceof PagoGiftCard);
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setUstedPaga(BigDecimal ustedPaga) {
        this.ustedPaga = ustedPaga;
    }

    public String getVuelta() {
        String s = "";
        if (getEntregado() == null) {
            return s;
        }
        BigDecimal vuelta = getEntregado().subtract(getUstedPaga());
        if (retencion != null && vuelta.compareTo(retencion.entregado) == 0 && !getMedioPagoActivo().isNotaCredito()) {
            vuelta = vuelta.subtract(retencion.entregado);
            if (getMedioPagoActivo().isEfectivo()) {
                setEntregado(getEntregado().subtract(retencion.entregado));
            }
        } else if (retencion != null && vuelta.compareTo(retencion.entregado) == 0 && getMedioPagoActivo().isNotaCredito()) {
            if (getMedioPagoActivo().isEfectivo()) {
                setEntregado(getEntregado().subtract(retencion.entregado));
            }
        }

        if (!(vuelta.doubleValue() == 0)) {
            s = vuelta.toString();
        }
        return s;
    }

    /**
     * Función necesaria para impresión de vueltas en ticket de letras de cambio
     * sin alterar aplicación para bebemundo
     *
     * @return
     */
    public String getVueltaTicketSK() {
        String res = this.getVuelta();
        if (res.isEmpty()) {
            return null;
        }
        return res;
    }

    public BigDecimal getVueltaAsBD() {
        if (getEntregado() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal vuelta = getEntregado().subtract(getUstedPaga());
        return vuelta.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean tieneInformacionExtra() {
        return medioPagoActivo.isTieneInfoExtra1() || medioPagoActivo.isTieneInfoExtra2() || medioPagoActivo.isTieneInfoExtra3();
    }

    public String getInformacionExtra1() {
        return informacionExtra1;
    }

    public void setInformacionExtra1(String informacionExtra1) {
        this.informacionExtra1 = informacionExtra1;
    }

    public String getInformacionExtra2() {
        return informacionExtra2;
    }

    public void setInformacionExtra2(String informacionExtra2) {
        this.informacionExtra2 = informacionExtra2;
    }

    public String getInformacionExtra3() {
        return informacionExtra3;
    }

    public void setInformacionExtra3(String informacionExtra3) {
        this.informacionExtra3 = informacionExtra3;
    }

    public BigDecimal getAhorroExacto() {
        if (descuentoCalculado == null) {
            descuentoCalculado = descuento;
        }
        if (getRetencion() != null) {
            if (pagos.getPagoEfectivo() != null) {
                if (!getRetencion().yaPagadoEfectivo.equals(total)) {
                    total = total.add(getRetencion().getTotal());
                }
            } else {
                if (!getRetencion().yaPagado.equals(total)) {
                    total = total.add(getRetencion().getTotal());
                }
            }
        }
        return Numero.porcentaje(total, descuentoCalculado);
    }

    public BigDecimal getAhorro() {
        if (isRetenido()) {
            return this.getTotal().subtract(this.getUstedPaga()).subtract(getRetencion().getTotal());
        }
        return this.getTotal().subtract(this.getUstedPaga());
    }

    public PlanPagoCredito getPlanSeleccionado() {
        return null;
    }

    public Integer getNumCuotas() {
        if (getPlanSeleccionado() != null) {
            return getPlanSeleccionado().getNumCuotas();
        }
        return 1;
    }

    public VencimientoBean getVencimiento() {
        if (getPlanSeleccionado() != null) {
            return getPlanSeleccionado().getVencimiento();
        }
        return medioPagoActivo.getVencimientoDefault();
    }

    public boolean pintaPlan() {
        return (this.getPlanSeleccionado() != null && this.getPlanSeleccionado().getCuota() != null);
    }

    public String getDescuentoASString() {
        String res = "0";
        if (this.getDescuento() != null) {
            res = this.getDescuento().toString();
        }
        return res;
    }

    public BigDecimal getTotalSinIva() {
        return totalSinIva;
    }

    public void setTotalSinIva(BigDecimal totalSinIva) {
        this.totalSinIva = totalSinIva;
    }

    public BigDecimal getUstedPagaSinIva() {
        return ustedPagaSinIva;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setUstedPagaSinIva(BigDecimal ustedPagaSinIva) {
        this.ustedPagaSinIva = ustedPagaSinIva;
    }

    public String getTotalImprimir() {
        return Numero.redondear(total).toString();
    }

    public boolean isRetenido() {
        return retencion != null;
    }

    public PagoRetencionFuente getRetencion() {
        return retencion;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void autorizarPago() throws AutorizadorException {
    }

    public void anularAutorizacionPago(List<Pago> anuladosImpresos) throws AutorizadorException {
    }

    public DocumentosImpresosBean anularPago(Pago pago, String idReferencia, String numeroAutorizacion, DocumentosBean documentosBean, String numeroFactura) throws AutorizadorException {
        return null;
    }

    public BigDecimal getSubtotalIva0() {
        return subtotalIva0;
    }

    public void setSubtotalIva0(BigDecimal subtotalIva0) {
        this.subtotalIva0 = subtotalIva0;
    }

    public BigDecimal getSubtotalIva12() {
        return subtotalIva12;
    }

    public void setSubtotalIva12(BigDecimal subtotalIva12) {
        this.subtotalIva12 = subtotalIva12;
    }

    public boolean isValidado() {
        return true;
    }

    public boolean isFalloValidacion() {
        return false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isPromocionAplicada() {
        return promociones != null && !promociones.isEmpty();
    }

    public boolean isNCuotasAplicada() {
        return isTipoPromocionAplicada(TipoPromocionBean.TIPO_PROMOCION_N_CUOTAS_GRATIS);
    }

    public boolean isTipoPromocionAplicada(Long idTipoPromocion) {
        if (getTipoPromocionAplicada(idTipoPromocion) == null) {
            return false;
        }
        return true;
    }

    public PromocionPagoTicket getPromocionAplicada(Long idPromocion) {
        if (!isPromocionAplicada()) {
            return null;
        }
        for (PromocionPagoTicket promocionPago : promociones) {
            if (promocionPago.getIdPromocion().equals(idPromocion)) {
                return promocionPago;
            }
        }
        return null;
    }

    public PromocionPagoTicket getTipoPromocionAplicada(Long idTipoPromocion) {
        if (!isPromocionAplicada()) {
            return null;
        }
        for (PromocionPagoTicket promocionPago : promociones) {
            if (promocionPago.getIdTipoPromocion().equals(idTipoPromocion)) {
                return promocionPago;
            }
        }
        return null;
    }

    public List<PromocionPagoTicket> getTiposPromocionesAplicadas(Long idTipoPromocion) {
        List<PromocionPagoTicket> promosAplicadas = new ArrayList<PromocionPagoTicket>();
        if (!isPromocionAplicada()) {
            return promosAplicadas;
        }
        for (PromocionPagoTicket promocionPago : promociones) {
            if (promocionPago.getIdTipoPromocion().equals(idTipoPromocion)) {
                promosAplicadas.add(promocionPago);
            }
        }
        return promosAplicadas;

    }

    public List<PromocionPagoTicket> getPromociones() {
        return promociones;
    }

    // MODOS
    public boolean isModoNormal() {
        return modalidad == MODO_NORMAL;
    }

    public boolean isModoCreditoDirecto() {
        return modalidad == MODO_CREDITO_DIRECTO;
    }

    public boolean isModoLetra() {
        return modalidad == MODO_LETRA_CAMBIO;
    }

    public boolean isModoAbonoPlan() {
        return modalidad == MODO_ABONO_PLAN;
    }

    public boolean isModoGiftCard() {
        return modalidad == MODO_GIFTCARD;
    }

    public boolean isModoArticulos() {
        return modalidad == MODO_ARTICULOS;
    }

    public boolean isModoArticulosPlan() {
        return modalidad == MODO_ARTICULOS_PLAN;
    }

    public boolean isModoAbonoReserva() {
        return modalidad == MODO_ABONO_RESERVA;
    }

    public boolean isModoFactura() {
        return modalidad == MODO_NORMAL || modalidad == MODO_ARTICULOS || modalidad == MODO_ARTICULOS_PLAN;
    }

    public boolean isModoSinDescuentos() {
        if (isModoAbonoPlan() || isModoCreditoDirecto() || isModoLetra()) {
            return true;
        }
        if (Sesion.isSukasa() && isModoGiftCard()) {
            return true;
        }
        return false;
    }

    public String getUstedPagaRenderer() {
        String renderer = " $ " + getTotal().toString() + " - " + getDescuentoASString() + "% = $ ";
        return renderer + getUstedPaga() + " ";
    }

    public String getCuotasRenderer() {
        return "";
    }

    public BigDecimal getAhorroRenderer() {
        return getAhorro();
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getImporteInteres() {
        return BigDecimal.ZERO;
    }

    /* Método sólo utilizado para impresión */
    public boolean isHayImporteInteresAnulacion() {
        return (isPagoTarjeta() && ((PagoCredito) this).getPlanSeleccionado().getImporteInteres().compareTo(BigDecimal.ZERO) > 0);
    }

    /* Método sólo utilizado para impresión */
    public BigDecimal getImporteInteresAnulacion() {
        if (isPagoTarjeta()) {
            return ((PagoCredito) this).getPlanSeleccionado().getImporteInteres();
        }
        return getImporteInteres();
    }

    public BigDecimal getPorcentajeInteres() {
        return BigDecimal.ZERO;
    }

    public LineaEnTicket getDesMedioPagoAsLineas() {

        String aux = "Forma de pago: " + this.getMedioPagoActivo().getDesMedioPago();
        if ((isPagoTarjeta() || medioPagoActivo.isCreditoDirecto()) && getPlanSeleccionado() != null) { // TODO: Poner número de cuotas en devoluciones
            aux = aux + "(" + getPlanSeleccionado().getNumCuotas() + ")";
        }
        return new LineaEnTicket(aux);
    }

    public LineaEnTicket getDesMedioPagoAnulacionAsLineas() {

        String aux = "Forma de Pago: " + this.getMedioPagoActivo().getDesMedioPago();
        if (isPagoTarjeta() || this instanceof PagoCreditoSK) {
            aux = aux + "(" + getPlanSeleccionado().getNumCuotas() + ")";
            if (this instanceof PagoCreditoSK) {
                aux = aux + " " + ((PagoCreditoSK) this).getPlastico().getCedulaCliente();
            } else {
                aux = aux + " Autorización:" + ((PagoCredito) this).getCodigoValidacionManual();
            }
        }
        return new LineaEnTicket(aux);
    }

    /**
     * Devuelve el número de documento asociado a un medio de pago
     *
     */
    public String getDocumento() {
        if (this instanceof PagoCredito && !(this instanceof PagoCreditoSK)) {
            return Cadena.ofuscarTarjeta(((PagoCredito) this).getTarjetaCredito().getNumero());
        } else if (this instanceof PagoCreditoSK) {
            return (((PagoCreditoSK) this).getPlastico() != null ? ((PagoCreditoSK) this).getPlastico().getCedulaCliente() : ((TarjetaCreditoSK) ((PagoCreditoSK) this).getTarjetaCredito()).getPlastico().getCedulaCliente());
        } else {
            return "";
        }
    }

    public boolean isHayInteres() {
        return (getImporteInteres() != null && getImporteInteres().compareTo(BigDecimal.ZERO) != 0);
    }

    public String getModalidadString() {
        switch (this.modalidad) {
            case (MODO_NORMAL):
                return "Venta";
            case (MODO_GIFTCARD):
                return "Recarga GiftCard";
            case (MODO_ABONO_RESERVA):
                return "Reservación";
            case (MODO_ABONO_PLAN):
                return "Plan Novios";
            case (MODO_ARTICULOS):
                return "Reservacion";
            case (MODO_ARTICULOS_PLAN):
                return "Plan Novios";
            case (MODO_CREDITO_DIRECTO):
                return "Credito Directo";
            case (MODO_LETRA_CAMBIO):
                return "Letra Cambio";
        }
        log.error("Se ha devuelto una modalidad de pago que no esta entre las tipificadas. modalidad :" + this.modalidad);
        return "" + this.modalidad; // aquí no debe de llegar
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    @Override
    public String toString() {
        return getMedioPagoActivo().getDesMedioPago();
    }

    /**
     * Devuelve el porcentaje que representa el pago con respecto al total
     * pasado por parámetro.
     */
    public BigDecimal getPorcentaje(BigDecimal total) {
        BigDecimal porcentaje = Numero.getTantoPorCientoContenido(total, getTotal());
        if (Numero.isMayor(porcentaje, Numero.CIEN)) {
            return Numero.CIEN;
        }
        return porcentaje;
    }

    public boolean requiereAutorizacionMontoMaximo(String importeTexto) {
        BigDecimal montoMaximo = getMedioPagoActivo().getMontoMaximoAutorizacion();
        if (montoMaximo == null || !Numero.isMayorACero(montoMaximo)) {
            return false;
        }
        BigDecimal importe = new BigDecimal(importeTexto);
        return Numero.isMayor(importe, montoMaximo);
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getDescuentoCalculado() {
        return descuentoCalculado;
    }

    public void recalculaFromUstedPagaRedondeado(boolean isCompensacionAplicada) {
        //       if(getSubtotalIva12().compareTo(BigDecimal.ZERO) != 0){
        BigDecimal base12MasIva = getUstedPaga().subtract(getSubtotalIva0());
        BigDecimal porcentajeIva = Sesion.getEmpresa().getPorcentajeIva();
        if (isCompensacionAplicada) {
            porcentajeIva = porcentajeIva.subtract(VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO));
        }
        BigDecimal base12R = Numero.getAntesDePorcentajeR(base12MasIva, porcentajeIva);
        BigDecimal ivaR = Numero.porcentajeR(base12R, porcentajeIva);
        log.info("actualizando base 12 1 " + base12R);
        setSubtotalIva12(base12R);
        setIva(ivaR);
        setUstedPaga(getSubtotalIva0().add(getSubtotalIva12()).add(getIva()));
        //      }else{
        //          setUstedPaga(getSubtotalIva0());
    }
    //  }    

    public void recalculaFromUstedPagaTruncado(boolean isCompensacionAplicada) {
        if (getSubtotalIva12().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal ustedPagaAnterior = getUstedPaga();
            BigDecimal base12MasIva = getUstedPaga().subtract(getSubtotalIva0());
            BigDecimal porcentajeIva = Sesion.getEmpresa().getPorcentajeIva();
            if (isCompensacionAplicada) {
                porcentajeIva = porcentajeIva.subtract(VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO));
            }
            BigDecimal base12T = Numero.getAntesDePorcentajeR(base12MasIva, porcentajeIva);
            BigDecimal ivaR = Numero.porcentajeR(base12T, porcentajeIva);
            log.info("actualizando base 12 8 " + base12T);
            subtotalIva12 = base12T;
            iva = ivaR;
            ustedPaga = getSubtotalIva0().add(getSubtotalIva12()).add(getIva());
        } else {
            setUstedPaga(getSubtotalIva0());
        }
    }

    public boolean validarBases() {
        if (isPagoEfectivo()) {
            return true;
        }
        BigDecimal base0F = getSubtotalIva0();
        BigDecimal base12F = getSubtotalIva12();
        BigDecimal ivaF = getIva();
        BigDecimal ustedPagaF = getUstedPaga();
        if (base12F == null || ivaF == null || ustedPagaF == null) {
            return false;
        }
        BigDecimal base12MasIva = getUstedPaga().subtract(getSubtotalIva0());
        if (!base12MasIva.setScale(2, RoundingMode.HALF_UP).equals(base12F.add(ivaF).setScale(2, RoundingMode.HALF_UP))) {
            return false;
        }
        if (!base0F.add(base12F).add(ivaF).setScale(2, RoundingMode.HALF_UP).equals(ustedPagaF.setScale(2, RoundingMode.HALF_UP))) {
            return false;
        }
        return true;
    }

    public void setDescuentoCalculado(BigDecimal descuentoCalculado) {
        this.descuentoCalculado = descuentoCalculado;
    }

    public PagosTicket getPagos() {
        return pagos;
    }

    public void setPagos(PagosTicket pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getTotalGarantiaExtendida() {
        return totalGarantiaExtendida;
    }

    public void setTotalGarantiaExtendida(BigDecimal totalGarantiaExtendida) {
        this.totalGarantiaExtendida = totalGarantiaExtendida;
    }

    public BigDecimal getYaPagado() {
        return yaPagado;
    }

    public void setYaPagado(BigDecimal yaPagado) {
        this.yaPagado = yaPagado;
    }

    public BigDecimal restaGarantiaExtendida() {
        BigDecimal resta = BigDecimal.ZERO;
        if (yaPagado != null && totalGarantiaExtendida != null && yaPagado.compareTo(totalGarantiaExtendida) < 0) {
            resta = totalGarantiaExtendida.subtract(yaPagado);
        }
        return resta;
    }

    public BigDecimal getDescuentoAplicado() {
        BigDecimal res = descuento;
        if (descuentoCalculado != null) {
            res = descuentoCalculado;
        }
        return res;
    }

    @Override
    public int compareTo(Pago p) {
        if (getPagoActivo() == p.getPagoActivo()) {
            return 0;
        }
        if (isPagoEfectivo()) {
            return -1;
        } else if (isPagoTarjeta() && !p.isPagoEfectivo()) {
            return -1;
        } else if (isPagoContado() && !p.isPagoEfectivo() && !p.isPagoTarjeta()) {
            return -1;
        } else if (isPagoOtros() && !p.isPagoEfectivo() && !p.isPagoTarjeta() && !p.isPagoContado()) {
            return -1;
        }
        return 1;
    }

    public String getIdTramaSuper() {
        return idTramaSuper;
    }

    public void setIdTramaSuper(String idTramaSuper) {
        this.idTramaSuper = idTramaSuper;
    }

    public String consultarSaldo(String tident) throws AutorizadorException {
        return null;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public String consultarSaldoWeb(String idBonoSuper) throws AutorizadorException {
        return respuesta;
    }

}
