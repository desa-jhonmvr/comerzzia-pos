/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import DF.RespuestaProcesoPago;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.pinpad.PinPadAutomatico;
import com.comerzzia.jpos.pinpad.PinPadException;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorAutomaticoNoPermitidoException;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFastrackConsultaException;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrack;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrackAutomatico;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrackException;
import com.comerzzia.jpos.pinpad.peticion.PeticionProcesoPagos;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaLecturaTarjeta;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoPagos;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoRetencionFuente;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.promociones.PromocionFormaPagoException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import com.comerzzia.jpos.servicios.promociones.articulos.ServicioPromocionArticulo;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoCuotas;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.jpos.util.enums.EnumTipoPagoManual;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author amos
 */
public class PagoCredito extends Pago {

    public static final String CARACTER_SEPARADOR = "&";
    // TIPO PAGO DATAFAS
    public static final Character MODO_DATAFAST = 'D';
    public static final Character MODO_MEDIANET = 'M';
    private static final Logger log = Logger.getMLogger(PagoCredito.class);
    private List<PlanPagoCredito> planes;
    private BigDecimal cuota;
    private PlanPagoCredito planSeleccionado;
    private boolean validadoAutomatico;
    private boolean validadoManual;
    private String tipoValidacionManual;
    private boolean falloValidacion;
    private String codigoValidacionManual;
    private TarjetaCredito tarjetaCredito;
    private FacturacionTarjeta facturacionTarjeta;
    //Guardamos las tramas de petición y respuesta del pago pinpad
    private PeticionProcesoPagos pinpadPeticion;
    private RespuestaProcesoPagos pinpadRespuesta;
    private RespuestaProcesoPago pinpadFasttrackRespuesta;
    private int auditoria;
    // Para anular pagos
    private String uidFacturacion;
    private String cvv;
    private String fechaCaducidad;
    private BigDecimal importeInteres;
    private BigDecimal porcentajeInteres;
    private boolean filtrarConsumoMinimo;
    private boolean mesesGraciaAplicado;
    private BigDecimal totalPagoAuto;
    private BigDecimal totalPagoBono;
    private String transaccion;
    private String mensajePromocional;
    private String numeroLoteManual; //G.S. Numero de lote usado para el pos movil
    private String lote;
    private String secuencialTransaccion;
    private EnumTipoPagoManual tipoPagoManual;        //G.S. Campo usado para saber que tipo de pago manual es
    private List<LineaTicket> ticketItemPromo;
    private String numeroTarjetaBono;
    private String tramaEnvio;
    private String tramaRespuesta;
    private String numeroTarjetaReimp; //G.S. Numero de la tarjeta para usar en la reimpresion

    public BigDecimal getTotalPagoAuto() {
        return totalPagoAuto;
    }

    public void setTotalPagoAuto(BigDecimal totalPagoAuto) {
        this.totalPagoAuto = totalPagoAuto;
    }

    public PagoCredito() {
        super(null, null, null, null);
    }

    public PagoCredito(PagosTicket pagos, Cliente cliente, String autorizador) {
        super(pagos, cliente, null, autorizador);
        planes = new ArrayList<PlanPagoCredito>();
        facturacionTarjeta = new FacturacionTarjeta();
        filtrarConsumoMinimo = true;
        mesesGraciaAplicado = false;
    }

    public String getCodEstablecimiento() {
        if (planSeleccionado.getVencimiento().isCorriente()) {
            return medioPagoActivo.getCodEstCorriente();
        }
        BigDecimal interes = getPorcentajeInteres();
        if (interes != null && interes.compareTo(BigDecimal.ZERO) > 0) {
            return medioPagoActivo.getCodEstDiferidoConIntereses();
        }
        return medioPagoActivo.getCodEstDiferidoSinIntereses();
    }

    public void recalcularPlanesFromTotal(BigDecimal total) throws PromocionArticuloException, PromocionFormaPagoException {
        log.info("entra a recalcularPlanesFromTotal>>>>>>");
        this.total = total;
        this.ustedPaga = null;
        this.cuota = null;
        int cont = 0;
        int promoF = 0;
        int promoNF = 0;
        if (Sesion.getTicket() != null) {
            ticketItemPromo = Sesion.getTicket().getLineas().getLineas();
            for (LineaTicket lineaPromo : ticketItemPromo) {
                cont = cont + 1;
                int cantidadLocalComprada = ServicioPromocionArticulo.consultarPromocionFormaPago(lineaPromo.getArticulo().getCodart());
                if (cantidadLocalComprada > 0) {
                    promoF = promoF + 1;
                } else {
                    promoNF = promoNF + 1;
                }
//                Esta linea se aumenta cuando la promocion tiene el mismo descuento de afiliado a todos los meses y se aumenta esta linea para que se puedan facturar en ambas facturas las entregas a domicilio y las garantias
                if (lineaPromo.getArticulo().getCodart().equals("2581.0001") || lineaPromo.getArticulo().getCodart().equals("0557.0316") || lineaPromo.getArticulo().getCodart().equals("0557.0318")) {
                    promoF = promoF + 1;
                }
            }
            if (promoF == 1L) {
                Sesion.setAfiliadoPromo(false);
            } else if (cont == promoF) {
                Sesion.setAfiliadoPromo(true);
            } else if (cont == promoNF) {
                Sesion.setAfiliadoPromo(false);
            } else {
                throw new PromocionFormaPagoException("Debe colocar solo items de promoción por forma de pago o solo items sin promoción de Forma Pago");
            }
        }
        // estamos haciendo un doble bucle, pero los elementos de la lista serán entre 2 y 5
        obtenerListaPlanes(Sesion.isAfiliadoPromo());
        Iterator<PlanPagoCredito> it = planes.iterator();
        while (it.hasNext()) {
            PlanPagoCredito plan = it.next();
            plan.recalcularFromTotal(total, BigDecimal.ZERO);
            if (!plan.isPisoValido(saldoInicial, filtrarConsumoMinimo) && !medioPagoActivo.isTarjetaSukasa()) {
                it.remove();
            }
        }
    }

    public void recalcularPlanesFromTotalByPlan(BigDecimal total, Long idPlan) {
        this.total = total;
        this.ustedPaga = null;
        this.cuota = null;
        // estamos haciendo un doble bucle, pero los elementos de la lista serán entre 2 y 5
        obtenerListaPlanes(Sesion.isAfiliadoPromo());
        Iterator<PlanPagoCredito> it = planes.iterator();
        while (it.hasNext()) {
            PlanPagoCredito plan = it.next();
            if (idPlan != null && plan.getIdPlan().equals(idPlan)) {
                plan.recalcularFromTotal(total, BigDecimal.ZERO);
                if (!plan.isPisoValido(saldoInicial, filtrarConsumoMinimo) && !medioPagoActivo.isTarjetaSukasa()) {
                    it.remove();
                }
            } else {
                it.remove();
            }

        }
    }

    public void recalcular() {
        obtenerListaPlanes(Sesion.isAfiliadoPromo());
        Iterator<PlanPagoCredito> it = planes.iterator();
        while (it.hasNext()) {
            PlanPagoCredito plan = it.next();
            plan.recalcularFromTotal(total, BigDecimal.ZERO);
            if (!plan.isPisoValido(saldoInicial, filtrarConsumoMinimo)) {
                it.remove();
            }
        }
    }

    public void recalcularPlanesFromCuota(BigDecimal cuota) {
        this.cuota = cuota;
        this.ustedPaga = null;
        this.total = null;
        // estamos haciendo un doble bucle, pero los elementos de la lista serán entre 2 y 5
        obtenerListaPlanes(Sesion.isAfiliadoPromo());
        Iterator<PlanPagoCredito> it = planes.iterator();
        while (it.hasNext()) {
            PlanPagoCredito plan = it.next();
            plan.recalcularFromCuota(cuota);
            if (!plan.isPisoValido(saldoInicial, filtrarConsumoMinimo) && !medioPagoActivo.isTarjetaSukasa()) {
                it.remove();
            }
        }
    }

    public void recalcularPlanesFromAPagar(BigDecimal aPagar) {
        this.ustedPaga = aPagar;
        this.cuota = null;
        this.total = null;
        // estamos haciendo un doble bucle, pero los elementos de la lista serán entre 2 y 5
        obtenerListaPlanes(Sesion.isAfiliadoPromo());
        Iterator<PlanPagoCredito> it = planes.iterator();
        while (it.hasNext()) {
            PlanPagoCredito plan = it.next();
            plan.recalcularFromAPagar(aPagar);
            if (!plan.isPisoValido(saldoInicial, filtrarConsumoMinimo) && !medioPagoActivo.isTarjetaSukasa()) {
                it.remove();
            }
        }
    }

    public void recalcularPlanesFromTotal(String total) throws PagoInvalidException, PromocionArticuloException, PromocionFormaPagoException {
        try {
            BigDecimal x = new BigDecimal(total);
            if (x.compareTo(saldoInicial) > 0) {
                throw new PagoInvalidException("El total es mayor que la cantidad que se debe pagar.");
            }
            if (x.compareTo(BigDecimal.ZERO) == 0) {
                throw new PagoInvalidException("El total no puede ser cero.");
            }
            recalcularPlanesFromTotal(x);
        } catch (NumberFormatException e) {
            throw new PagoInvalidException("La cantidad indicada no es un número correcto.");
        }
    }

    public void recalcularPlanesFromCuota(String cuota) throws PagoInvalidException {
        try {
            BigDecimal x = new BigDecimal(cuota);
            if (x.compareTo(BigDecimal.ZERO) == 0) {
                throw new PagoInvalidException("El valor de la cuota no puede ser cero.");
            }
            recalcularPlanesFromCuota(x);
        } catch (NumberFormatException e) {
            throw new PagoInvalidException("La cantidad indicada no es un número correcto.");
        }
    }

    public void recalcularPlanesFromAPagar(String aPagar) throws PagoInvalidException {
        try {
            BigDecimal x = new BigDecimal(aPagar);
            if (x.compareTo(BigDecimal.ZERO) == 0) {
                throw new PagoInvalidException("El valor del boucher no puede ser cero.");
            }
            recalcularPlanesFromAPagar(x);
        } catch (NumberFormatException e) {
            throw new PagoInvalidException("La cantidad indicada no es un número correcto: " + aPagar);
        }
    }

    public void obtenerListaPlanes(boolean afiliadoPromo) {
        planes.clear();
        Iterator<VencimientoBean> it = medioPagoActivo.getPlanes().iterator();
        while (it.hasNext()) {
            VencimientoBean vencimiento = it.next();
            if (modalidad == MODO_ABONO_PLAN) {
                if (vencimiento.isCorriente() && !medioPagoActivo.isAdmiteAbonoReservacionCorriente()) {
                    continue;
                }
                if (!vencimiento.isCorriente()) { // en abonos plan novios sólo permitimos corriente, da igual el medio de pago
                    continue;
                }
            }
            if (modalidad == MODO_ABONO_RESERVA) {
                if (vencimiento.isCorriente() && !medioPagoActivo.isAdmiteAbonoReservacionCorriente()) {
                    continue;
                }
                if (!vencimiento.isCorriente() && !medioPagoActivo.isAdmiteAbonoReservacionDiferido()) {
                    continue;
                }
            } else if (modalidad == MODO_GIFTCARD) {
                if (!vencimiento.isCorriente() && !medioPagoActivo.isPermitePagarGiftCardDiferido()) {
                    continue;
                }
                if (vencimiento.isCorriente() && !medioPagoActivo.isPermitePagarGiftCardCorriente()) {
                    continue;
                }
            }
            planes.add(new PlanPagoCredito(vencimiento, this, afiliadoPromo));
        }
        Collections.sort(planes);
    }

    @Override
    public void recalcularConRetencionFuente(PagoRetencionFuente pago, BigDecimal compensacion) {
        BigDecimal pagoARestar = pago.getUstedPaga().add(compensacion);
        getPlanSeleccionado().recalcularFromAPagarSinIntereses(getPlanSeleccionado().getaPagar().subtract(pagoARestar));
        this.ustedPaga = planSeleccionado.getaPagar();
        this.entregado = this.ustedPaga;
        this.importeInteres = planSeleccionado.getImporteInteres();
        retencion = pago;
    }

    @Override
    public void recalcularSinRetencionFuente(PagoRetencionFuente pago) {
        getPlanSeleccionado().recalcularFromAPagarSinIntereses(getPlanSeleccionado().getaPagar().add(pago.getTotal()));
        this.ustedPaga = planSeleccionado.getaPagar();
        this.entregado = this.ustedPaga;
        this.importeInteres = planSeleccionado.getImporteInteres();
        retencion = null;
    }

    public String validarAutorizacionManual() {
        if (codigoValidacionManual == null || codigoValidacionManual.isEmpty()) {
            return "La aceptación requiere un código de validación correcto";
        }
        if (codigoValidacionManual.length() > 6) {
            return "Máximo de 6 cifras";
        }
        return null;
    }

    public List<PlanPagoCredito> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanPagoCredito> planes) {
        this.planes = planes;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    @Override
    public String getVuelta() {
        return ""; // Un pago a crédito nunca tiene cambio
    }

    @Override
    public PlanPagoCredito getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(int i) {
        this.planSeleccionado = planes.get(i);
        this.total = planSeleccionado.getTotal();
        this.ustedPaga = planSeleccionado.getaPagar();
        this.entregado = this.ustedPaga;
        this.descuento = planSeleccionado.getDescuento();
        this.descuentoCalculado = planSeleccionado.getDescuento();
        this.importeInteres = planSeleccionado.getImporteInteres();
        this.porcentajeInteres = planSeleccionado.getPorcentajeInteres();
        aplicaPromociones();
    }

    /**
     * No usar. Sólo utilizado para recuperación de factura desde XML en
     * TicketOrigen
     *
     * @param p
     */
    public void setPlanSeleccionado(PlanPagoCredito p) {
        this.planSeleccionado = p;
    }

    private void aplicaPromociones() {
        if (!isModoNormal()) {
            return;
        }
        mesesGraciaAplicado = false;
        promociones = new ArrayList<PromocionPagoTicket>();
        for (PromocionTipoCuotas promocionCuotas : Sesion.promocionesNCuotas) {
            if (promocionCuotas.isAplicableACliente(cliente)
                    && promocionCuotas.isAplicableALineas(Sesion.getTicket().getLineas())
                    && promocionCuotas.isAplicableAFecha()
                    && promocionCuotas.isAplicableAPago(this)
                    && promocionCuotas.isReaplicable(Sesion.getTicket())) {
                promocionCuotas.aplicaPago(Sesion.getTicket().getLineas(), this);
            }
        }
        if (Sesion.getTicket().getTicketPromociones().isMesesGraciaAplicado()
                || (Sesion.getTicket().getTicketPromociones().isFacturaAsociadaDiaSocio() && Sesion.promocionMesesGracia != null)) {
            if (Sesion.promocionMesesGracia.isAplicableACliente(cliente)
                    && Sesion.promocionMesesGracia.isAplicableALineas(Sesion.getTicket().getLineas())
                    && Sesion.promocionMesesGracia.isAplicableAFecha()
                    && Sesion.promocionMesesGracia.isAplicableAPago(this)
                    && Sesion.promocionMesesGracia.isReaplicable(Sesion.getTicket())) {
                Sesion.promocionMesesGracia.aplicaPago(Sesion.getTicket().getLineas(), this);
            }
        }
    }

    @Override
    public void autorizarPago() throws AutorizadorException {
        // Si el pago ya está autorizado no tenemos que hacer nada.
        if (isValidado()) {
            return;
        }

        // Si tenemos configurado manual, mostraremos pantalla para autorización manual
        if (PinPad.getInstance().isManual() || PinPadFasttrack.getInstance().isManual()) {
            throw new AutorizadorException("Autorización manual", this);
        }

        // Si no tenemos configurado autorización manual y este medio de pago no admite la autorización automática, mostraremos error
        if (!isAdmiteAutorizacionAutomatica()) {
            log.debug("autorizarPago() - !isAdmiteAutorizacionAutomatica() " + !isAdmiteAutorizacionAutomatica());
            throw new AutorizadorAutomaticoNoPermitidoException("No está permitida la autorización automática para este pago.", this);
        }

        // Si no tenemos configurado autorización manual y sí permitimos la automática, procedemos
        log.debug("autorizarPago() - (Validar tarjetas) - Validando tarjeta...");
        log.debug("autorizarPago() - (Validar tarjetas) - Autorizando...");
        try {
            pinpadPeticion = new PeticionProcesoPagos(this, false);
            if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                PinPad op = PinPad.getInstance();
                if (op.isAutomatico()) {
                    ((PinPadAutomatico) op.getiPinPad()).setPago(this);
                    ((PinPadAutomatico) op.getiPinPad()).setTicket(super.getTicket());
                }
                //Proceso De Permisos De PinPad
                op.controlPermisosPinPad();
                RespuestaLecturaTarjeta lectura = op.getiPinPad().lecturaTarjeta();
                getTarjetaCredito().setNumeroTarjetaPinPad(lectura.getNumeroTarjeta().trim());
                getTarjetaCredito().setNumero(lectura.getNumeroTarjeta().trim());
                log.debug("autorizarPago() - Lectura de tarjeta realizada. Número tarjeta: " + getTarjetaCredito().getNumero());
                log.debug("autorizarPago() - Realizando validación de bines...");
                MedioPagoBean medioPagoBin = getTarjetaCredito().getBINMedioPago();
                log.debug("autorizarPago() - Medio de pago obtenido a partir del bin: " + medioPagoBin);
                log.debug("autorizarPago() - Medio de pago seleccionado en el POS: " + getMedioPagoActivo());
                if (medioPagoBin == null) {
                    log.error("autorizarPago() - La tarjeta introducida en el PinPad no tiene bines configurados en el sistema.  " + getTarjetaCredito().getNumero());
                    throw new AutorizadorException("La tarjeta introducida en el PinPad no tiene bines configurados en el sistema. " + getTarjetaCredito().getNumero(), this);
                }
                if (!medioPagoBin.getCodMedioPago().equals(getMedioPagoActivo().getCodMedioPago())) {
                    log.error("autorizarPago() - La tarjeta introducida en el PinPad no coincide con la seleccionada en el POS previamente. " + getTarjetaCredito().getNumero());
                    throw new AutorizadorException("La tarjeta introducida en el PinPad no coincide con la seleccionada en el POS previamente. " + getTarjetaCredito().getNumero(), this);
                }
                pinpadRespuesta = op.getiPinPad().procesoPagos(pinpadPeticion);
            } else {
                //Fasttrack
                PinPadFasttrack ppf = PinPadFasttrack.getInstance();
                if (ppf.isAutomatico()) {
                    ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setPago(this);
                    ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setTicket(super.getTicket());
                }
                DF.RespuestaLecturaTarjeta respuesta = ppf.getiPinPad().lecturaTarjeta();
                if (respuesta.CodigoRespuesta.equals("01")) {
                    throw new PinPadFastrackConsultaException("Error en trama. ");
                } else if (respuesta.CodigoRespuesta.equals("02")) {
                    throw new PinPadFastrackConsultaException("Error de conexión con el pinpad.");
                } else if (respuesta.CodigoRespuesta.equals("20")) {
                    throw new PinPadFastrackConsultaException("Inserte correctamente la tarjeta y vuelva a intentarlo.");
                } else if (respuesta.CodigoRespuesta.equals("TO")) {
                    throw new PinPadFastrackConsultaException("El tiempo de espera ha finalizado.");
                } else if (respuesta.CodigoRespuesta.equals("ER")) {
                    throw new PinPadFastrackConsultaException("Error de conexión con el pinpad.");
                }

                getTarjetaCredito().setNumeroTarjetaPinPad(respuesta.TarjetaTruncada.trim());
                getTarjetaCredito().setNumero(respuesta.TarjetaTruncada.trim());
                log.debug("autorizarPago() - Lectura de tarjeta realizada. Número tarjeta: " + getTarjetaCredito().getNumero());
                log.debug("autorizarPago() - Realizando validación de bines...");
                MedioPagoBean medioPagoBin = getTarjetaCredito().getBINMedioPago();
                log.debug("autorizarPago() - Medio de pago obtenido a partir del bin: " + medioPagoBin);
                log.debug("autorizarPago() - Medio de pago seleccionado en el POS: " + getMedioPagoActivo());
                if (medioPagoBin == null) {
                    log.error("autorizarPago() - La tarjeta introducida en el PinPad no tiene bines configurados en el sistema.  " + getTarjetaCredito().getNumero());
                    throw new AutorizadorException("La tarjeta introducida en el PinPad no tiene bines configurados en el sistema. " + getTarjetaCredito().getNumero(), this);
                }
                if (!medioPagoBin.getCodMedioPago().equals(getMedioPagoActivo().getCodMedioPago())) {
                    log.error("autorizarPago() - La tarjeta introducida en el PinPad no coincide con la seleccionada en el POS previamente. " + getTarjetaCredito().getNumero());
                    throw new AutorizadorException("La tarjeta introducida en el PinPad no coincide con la seleccionada en el POS previamente. " + getTarjetaCredito().getNumero(), this);
                }

                pinpadFasttrackRespuesta = ppf.getiPinPad().procesoPagos(Integer.parseInt(pinpadPeticion.getTipoTransaccion()),
                        pinpadPeticion.getCodigoDiferido(), pinpadPeticion.getMontoBaseNoIva(), pinpadPeticion.getMontoBaseIva(),
                        pinpadPeticion.getImpuestoIva(), pinpadPeticion.getMontoTotal(), pinpadPeticion.getPlazoDiferido(), pinpadPeticion.getCodigoIdentificacionRed());
                pinpadRespuesta = new RespuestaProcesoPagos();
                pinpadRespuesta.setNumeroLote(pinpadFasttrackRespuesta.Lote);
                pinpadRespuesta.setNumeroAutorizacion(pinpadFasttrackRespuesta.Autorizacion);
                pinpadRespuesta.setNombreGrupoTarjeta(pinpadFasttrackRespuesta.Filler1);
                pinpadRespuesta.setSecuencialTransaccion(pinpadFasttrackRespuesta.Referencia);
                pinpadRespuesta.setNombreTarjetaHabiente(pinpadFasttrackRespuesta.TarjetaHabiente);
                pinpadRespuesta.setNombreBancoAdquiriente(pinpadFasttrackRespuesta.NombreAdquirente);
                pinpadRespuesta.setTerminalId(pinpadFasttrackRespuesta.TID);
                pinpadRespuesta.setMerchantId(pinpadFasttrackRespuesta.MID);
                pinpadRespuesta.setModoLectura(pinpadFasttrackRespuesta.ModoLectura);
                pinpadRespuesta.setAidEmv(pinpadFasttrackRespuesta.AplicacionEMV);
                pinpadRespuesta.setArqc(pinpadFasttrackRespuesta.ARQC);
                pinpadRespuesta.setValorEmv(pinpadFasttrackRespuesta.Criptograma);
                pinpadRespuesta.setPublicidad(pinpadFasttrackRespuesta.Publicidad);
                pinpadRespuesta.setIdentificacionEmv(pinpadFasttrackRespuesta.AID);
                pinpadRespuesta.setPublicidad(pinpadFasttrackRespuesta.Publicidad);
                pinpadRespuesta.setTvr(pinpadFasttrackRespuesta.TVR);
                pinpadRespuesta.setTsi(pinpadFasttrackRespuesta.TSI);
            }
            setValidadoAutomatico(true);
        } catch (PinPadException e) {
            throw new AutorizadorException("Error autorizando el pago mediante Pinpad: " + e.getMessage(), this);
        } catch (PinPadFasttrackException e) {
            java.util.logging.Logger.getLogger(PagoCredito.class.getName()).log(Level.SEVERE, null, e);
            throw new AutorizadorException("Error autorizando el pago mediante Pinpad Track: " + e.getMessage(), this);
        } catch (PinPadFastrackConsultaException e) {
            java.util.logging.Logger.getLogger(PagoCredito.class.getName()).log(Level.SEVERE, null, e);
            throw new AutorizadorException("Error en la lectura de tarjeta: " + e.getMessage(), this);
        }
    }

    @Override
    public void anularAutorizacionPago(List<Pago> anuladosImpresos) throws AutorizadorException {
        try {
            if (isValidadoManual()) {
                setValidadoManual(false);
            } else if (isValidadoAutomatico()) { // Validado automático
                pinpadPeticion = new PeticionProcesoPagos(this, true);
                if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                    PinPad op = PinPad.getInstance();
                    if (op.isAutomatico()) {
                        ((PinPadAutomatico) op.getiPinPad()).setPago(this);
                        ((PinPadAutomatico) op.getiPinPad()).setTicket(super.getTicket());
                    }
                    pinpadRespuesta = op.getiPinPad().procesoPagos(pinpadPeticion);
                } else {
                    //Fasttrack
                    PinPadFasttrack ppf = PinPadFasttrack.getInstance();
                    if (ppf.isAutomatico()) {
                        ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setPago(this);
                        ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setTicket(super.getTicket());
                    }

                    pinpadFasttrackRespuesta = ppf.getiPinPad().procesoAnulacion(Integer.parseInt(pinpadPeticion.getTipoTransaccion()),
                            pinpadRespuesta.getSecuencialTransaccion(), pinpadRespuesta.getNumeroAutorizacion(), pinpadPeticion.getSecuencialTransaccion(), pinpadPeticion.getCodigoIdentificacionRed());
                }
                setValidadoAutomatico(false);

                if (getMedioPagoActivo().isTarjetaCredito() && !getMedioPagoActivo().isTarjetaSukasa()) {
                    PrintServices.getInstance().imprimirVoucherAnulacionPinPad(this);
                } else {
                    PrintServices.getInstance().imprimirVoucherAnulacion(this);
                }
                anuladosImpresos.add(this);
            }
        } catch (PinPadException e) {
            throw new AutorizadorException("Error anulando el pago mediante pinpad: ", this);
        } catch (TicketPrinterException ex) {
            throw new AutorizadorException("Error imprimiendo Voucher de Anulación de pago mediante pinpad: ", ex);
        } catch (TicketException ex) {
            throw new AutorizadorException("Error imprimiendo Voucher de Anulación de pago mediante pinpad: ", ex);
        } catch (PinPadFasttrackException ex) {
            java.util.logging.Logger.getLogger(PagoCredito.class.getName()).log(Level.SEVERE, null, ex);
            throw new AutorizadorException("Error anulando el pago mediante pinpad: ", this);
        }
    }

    @Override
    public DocumentosImpresosBean anularPago(Pago pago, String idReferencia, String numeroAutorizacion, DocumentosBean documentosBean, String numeroFactura) throws AutorizadorException {
        try {
            if (isValidadoManual()) {
                setValidadoManual(false);
            } else if (isValidadoAutomatico()) { // Validado automático
                // else if (true){ // Validado automático
                pinpadPeticion = new PeticionProcesoPagos(this, true);
                if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                    PinPad op = PinPad.getInstance();
                    if (op.isAutomatico()) {
                        ((PinPadAutomatico) op.getiPinPad()).setPago(this);
                        ((PinPadAutomatico) op.getiPinPad()).setTicket(super.getTicket());
                    }
                    pinpadRespuesta = op.getiPinPad().procesoAnulacion(pinpadPeticion, idReferencia, numeroAutorizacion);
                } else {
                    //Fasttrack
                    PinPadFasttrack ppf = PinPadFasttrack.getInstance();
                    if (ppf.isAutomatico()) {
                        ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setPago(this);
                        ((PinPadFasttrackAutomatico) ppf.getiPinPad()).setTicket(super.getTicket());
                    }
                    pinpadFasttrackRespuesta = ppf.getiPinPad().procesoAnulacion(Integer.parseInt(pinpadPeticion.getTipoTransaccion()),
                            idReferencia, numeroAutorizacion, pinpadPeticion.getSecuencialTransaccion(), pinpadPeticion.getCodigoIdentificacionRed());
                    pinpadRespuesta = new RespuestaProcesoPagos();
                    pinpadRespuesta.setNumeroLote(pinpadFasttrackRespuesta.Lote);
                    pinpadRespuesta.setNumeroAutorizacion(pinpadFasttrackRespuesta.Autorizacion);
                    pinpadRespuesta.setNombreGrupoTarjeta(pinpadFasttrackRespuesta.Filler1);
                    pinpadRespuesta.setSecuencialTransaccion(pinpadFasttrackRespuesta.Referencia);
                    pinpadRespuesta.setNombreTarjetaHabiente(pinpadFasttrackRespuesta.TarjetaHabiente);
                    pinpadRespuesta.setNombreBancoAdquiriente(pinpadFasttrackRespuesta.NombreAdquirente);
                    pinpadRespuesta.setTerminalId(pinpadFasttrackRespuesta.TID);
                    pinpadRespuesta.setMerchantId(pinpadFasttrackRespuesta.MID);
                    pinpadRespuesta.setModoLectura(pinpadFasttrackRespuesta.ModoLectura);
                    pinpadRespuesta.setAidEmv(pinpadFasttrackRespuesta.AplicacionEMV);
                    pinpadRespuesta.setArqc(pinpadFasttrackRespuesta.ARQC);
                    pinpadRespuesta.setValorEmv(pinpadFasttrackRespuesta.Criptograma);
                    pinpadRespuesta.setPublicidad(pinpadFasttrackRespuesta.Publicidad);
                    pinpadRespuesta.setIdentificacionEmv(pinpadFasttrackRespuesta.AID);
                    pinpadRespuesta.setPublicidad(pinpadFasttrackRespuesta.Publicidad);
                    pinpadRespuesta.setTvr(pinpadFasttrackRespuesta.TVR);
                    pinpadRespuesta.setTsi(pinpadFasttrackRespuesta.TSI);
                }
                setValidadoAutomatico(false);

                if (getMedioPagoActivo().isTarjetaCredito() && !getMedioPagoActivo().isTarjetaSukasa()) {
                    transaccion = numeroFactura;
                    return PrintServices.getInstance().imprimirVoucherAnulacionPinPad(this);
                } else {
                    PrintServices.getInstance().imprimirVoucherAnulacion(this);
                }

            }
        } catch (PinPadException e) {
            throw new AutorizadorException("Error anulando el pago mediante pinpad: ", this);
        } catch (TicketPrinterException ex) {
            throw new AutorizadorException("Error imprimiendo Voucher de Anulación de pago mediante pinpad: ", ex);
        } catch (TicketException ex) {
            throw new AutorizadorException("Error imprimiendo Voucher de Anulación de pago mediante pinpad: ", ex);
        } catch (PinPadFasttrackException ex) {
            throw new AutorizadorException("Error anulando el pago mediante pinpad: ", this);
        }
        return null;
    }

    @Override
    public String getDocumento() {
        return getTarjetaCredito().getNumeroOculto();
    }

    @Override
    public boolean isValidado() {
        return (isValidadoManual() || isValidadoAutomatico());
    }

    public boolean isAdmiteAutorizacionAutomatica() {
        log.debug("getPlanSeleccionado().getVencimiento() " + getPlanSeleccionado().getVencimiento());
        if (getPlanSeleccionado().getVencimiento().isCorriente()) {
            log.debug("getPlanSeleccionado().getVencimiento() corriente " + getPlanSeleccionado().getVencimiento().isCorriente());
            return getMedioPagoActivo().isAdmiteAutorizacionAutomaticaCorriente();
        } else {
            log.debug("getPlanSeleccionado().getVencimiento() diferido" + getPlanSeleccionado().getVencimiento().isCorriente());
            return getMedioPagoActivo().isAdmiteAutorizacionAutomaticaDiferido();
        }
    }

    public boolean isAdmiteAutorizacionManual() {
        if (getPlanSeleccionado().getVencimiento().isCorriente()) {
            return getMedioPagoActivo().isAdmiteAutorizacionManualCorriente();
        } else {
            return getMedioPagoActivo().isAdmiteAutorizacionManualDiferido();
        }
    }

    public boolean isValidadoAutomatico() {
        return validadoAutomatico;
    }

    public void setValidadoAutomatico(boolean validadoPasarela) {
        this.validadoAutomatico = validadoPasarela;
    }

    public boolean isValidadoManual() {
        return validadoManual;
    }

    public void setValidadoManual(boolean validadoManual) {
        this.validadoManual = validadoManual;

    }

    public String getCodigoValidacion() {
        if (pinpadRespuesta != null) {
            return pinpadRespuesta.getCodigoRespuestaTransaccion();
        }
        return codigoValidacionManual;
    }

    public String getCodigoValidacionManual() {
        return codigoValidacionManual;
    }

    public void setCodigoValidacionManual(String codigoValidacionManual) {
        this.codigoValidacionManual = codigoValidacionManual;
    }

    public void setFiltrarConsumoMinimo(boolean filtrarConsumoMinimo) {
        this.filtrarConsumoMinimo = filtrarConsumoMinimo;
    }

    @Override
    public boolean isFalloValidacion() {
        return falloValidacion;
    }

    public void setFalloValidacion(boolean falloValidacion) {
        this.falloValidacion = falloValidacion;
    }

    public TarjetaCredito getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(TarjetaCredito tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public FacturacionTarjeta getFacturacionTarjeta() {
        return facturacionTarjeta;
    }

    public void setFacturacionTarjeta(FacturacionTarjeta facturacionTarjeta) {
        this.facturacionTarjeta = facturacionTarjeta;
    }

    public String getNumeroAutorizacionTarjeta() {
        String numeroAut;
        if (pinpadRespuesta != null) {
            numeroAut = pinpadRespuesta.getNumeroAutorizacion();
            if (numeroAut == null || numeroAut.trim().equals("") || numeroAut.trim().equals("000000")) {
                numeroAut = this.codigoValidacionManual;
            }
        } else {
            numeroAut = this.codigoValidacionManual;
        }
        return numeroAut;
    }

    public String getFechaCaducidadTarjeta() {
        return "" + ((tarjetaCredito != null && tarjetaCredito.getCaducidad() != null && tarjetaCredito.getCaducidad() != 0) ? tarjetaCredito.getCaducidad() : "");
    }

    public String getNumeroTarjeta() {
        return "" + ((tarjetaCredito != null && tarjetaCredito.getNumero() != null) ? tarjetaCredito.getNumero() : "");
    }

    public String getLecturaBandaManual() {
        return "" + ((tarjetaCredito != null && tarjetaCredito.isLecturaBandaManual() != null) ? tarjetaCredito.isLecturaBandaManual().toString() : "");
    }

    public int getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(int auditoria) {
        this.auditoria = auditoria;
    }

    public void setAuditoria() throws ContadorException {
        this.setAuditoria(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_AUDITORIA).intValue());

    }

    @Override
    public BigDecimal getPorcentajeInteres() {
        return porcentajeInteres;
    }

    public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
        this.porcentajeInteres = porcentajeInteres;
    }

    @Override
    public BigDecimal getImporteInteres() {
        return importeInteres;
    }

    public void setImporteInteres(BigDecimal importeInteres) {
        this.importeInteres = importeInteres;
    }

    public String getUidFacturacion() {
        return uidFacturacion;
    }

    public void setUidFacturacion(String uidFacturacion) {
        this.uidFacturacion = uidFacturacion;
    }

    public String getCVVFacturacion() {
        if (this.isValidadoManual() && this.getTarjetaCredito() != null) {
            return this.getTarjetaCredito().getCvv() != null ? this.getTarjetaCredito().getCvv() : "";
        }
        return "";
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public BigDecimal getAhorroRenderer() {
        BigDecimal ahorroRenderer = getPlanSeleccionado().getAhorroConInteres();
        if (ahorroRenderer.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return Numero.redondear(ahorroRenderer);
    }

    @Override
    public String getUstedPagaRenderer() {
        BigDecimal ustedPagaRenderer = getPlanSeleccionado().getaPagarMasInteres();
        String renderer = " $ " + getTotal().toString()
                + " - " + getDescuentoASString() + "%"
                + " + " + getPorcentajeInteres().toString() + "%"
                + " = $ " + ustedPagaRenderer + " ";
        return renderer;
    }

    @Override
    public String getCuotasRenderer() {
        return getPlanSeleccionado().getNumCuotas() + "  x  $ " + this.getPlanSeleccionado().getCuotaMasInteres().toString();
    }

    public void addPromocion(PromocionPagoTicket promocion) {
        this.promociones.add(promocion);
    }

    public String getTID() {
        if (planSeleccionado != null && planSeleccionado.getNumCuotas() <= 1) {
            return getModoTID(this.medioPagoActivo.getTipoPagoCorriente());
        } else if (planSeleccionado != null && planSeleccionado.getNumCuotas() > 1) {  // Con mas de una cuota es  pago diferido
            return getModoTID(this.medioPagoActivo.getTipoPagoDiferido());
        } else {
            return null; // No hay plan seleccionado
        }
    }

    public String getMID() {
        if (planSeleccionado != null && planSeleccionado.getNumCuotas() <= 1) {
            return getModoMID(this.medioPagoActivo.getTipoPagoCorriente());
        } else if (planSeleccionado != null && planSeleccionado.getNumCuotas() > 1) {  // Con mas de una cuota es  pago diferido
            return getModoMID(this.medioPagoActivo.getTipoPagoDiferido());
        } else {
            return null; // No hay plan seleccionado
        }
    }

    /**
     * Devuelve el id d eMedianet o datafast según este configurado en el medio
     * de pago
     *
     * @param tipoPago
     * @return
     */
    private String getModoTID(Character tipoPago) {
        String res = null;
        if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
            if (MODO_MEDIANET.equals(tipoPago)) {
                res = Sesion.getTienda().getSriTienda().getCajaActiva().getTidMedianet();
            } else if (MODO_DATAFAST.equals(tipoPago)) {
                res = Sesion.getTienda().getSriTienda().getCajaActiva().getTidDatafast();
            }
        } else {
            res = Sesion.getTienda().getSriTienda().getCajaActiva().getTidDatafast();
        }
        return res;
    }

    private String getModoMID(Character tipoPago) {
        String res = null;
        if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
            if (MODO_MEDIANET.equals(tipoPago)) {
                res = Cadena.completaconBlancos(VariablesAlm.getVariable(VariablesAlm.PINPAD_MID_MEDIANET), 15);
            } else if (MODO_DATAFAST.equals(tipoPago)) {
                res = Cadena.completaconBlancos(VariablesAlm.getVariable(VariablesAlm.PINPAD_MID_DATAFAST), 15);
            }
        } else {
            res = Sesion.getTienda().getSriTienda().getCajaActiva().getTidDatafast();
        }
        return res;
    }

    public boolean isDatafast() {
        if (planSeleccionado != null && planSeleccionado.getNumCuotas() <= 1) {
            return "D".equals(getTipoPago(this.medioPagoActivo.getTipoPagoCorriente()));
        } else if (planSeleccionado != null && planSeleccionado.getNumCuotas() > 1) {  // Con mas de una cuota es  pago diferido
            return "D".equals(getTipoPago(this.medioPagoActivo.getTipoPagoDiferido()));
        } else {
            return false; // No hay plan seleccionado
        }
    }

    public boolean isMedianet() {
        if (planSeleccionado != null && planSeleccionado.getNumCuotas() <= 1) {
            return "M".equals(getTipoPago(this.medioPagoActivo.getTipoPagoCorriente()));
        } else if (planSeleccionado != null && planSeleccionado.getNumCuotas() > 1) {  // Con mas de una cuota es  pago diferido
            return "M".equals(getTipoPago(this.medioPagoActivo.getTipoPagoDiferido()));
        } else {
            return false; // No hay plan seleccionado
        }
    }

    public String getTipoPago(Character tipoPago) {
        String res = null;
        if (tipoPago != null) {
            if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                if (tipoPago.equals(MODO_MEDIANET)) {
                    res = "M";
                } else if (tipoPago.equals(MODO_DATAFAST)) {
                    res = "D";
                }
            } else {
                res = "D";
            }
        }
        return res;
    }

    public boolean isMesesGraciaAplicado() {
        return mesesGraciaAplicado;
    }

    public void setMesesGraciaAplicado(boolean mesesGraciaAplicado) {
        this.mesesGraciaAplicado = mesesGraciaAplicado;
    }

    public PeticionProcesoPagos getPinpadPeticion() {
        return pinpadPeticion;
    }

    public void setPinpadPeticion(PeticionProcesoPagos pinpadPeticion) {
        this.pinpadPeticion = pinpadPeticion;
    }

    public RespuestaProcesoPagos getPinpadRespuesta() {
        return pinpadRespuesta;
    }

    public void setPinpadRespuesta(RespuestaProcesoPagos pinpadRespuesta) {
        this.pinpadRespuesta = pinpadRespuesta;
    }

    public String getTipoValidacionManual() {
        return tipoValidacionManual;
    }

    public void setTipoValidacionManual(String tipoValidacionManual) {
        this.tipoValidacionManual = tipoValidacionManual;
    }

    public boolean isSoloPagoCredito() {
        return true;
    }

    public RespuestaProcesoPago getPinpadFasttrackRespuesta() {
        return pinpadFasttrackRespuesta;
    }

    public void setPinpadFasttrackRespuesta(RespuestaProcesoPago pinpadFasttrackRespuesta) {
        this.pinpadFasttrackRespuesta = pinpadFasttrackRespuesta;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public BigDecimal getTotalPagoBono() {
        return totalPagoBono;
    }

    public void setTotalPagoBono(BigDecimal totalPagoBono) {
        this.totalPagoBono = totalPagoBono;
    }

    public String getMensajePromocional() {
        return mensajePromocional;
    }

    public void setMensajePromocional(String mensajePromocional) {
        this.mensajePromocional = mensajePromocional;
    }

    public String getNumeroLoteManual() {
        return numeroLoteManual;
    }

    public void setNumeroLoteManual(String numeroLoteManual) {
        this.numeroLoteManual = numeroLoteManual;
    }

    public EnumTipoPagoManual getTipoPagoManual() {
        return tipoPagoManual;
    }

    public void setTipoPagoManual(EnumTipoPagoManual tipoPagoManual) {
        this.tipoPagoManual = tipoPagoManual;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getNumeroTarjetaBono() {
        return numeroTarjetaBono;
    }

    public void setNumeroTarjetaBono(String numeroTarjetaBono) {
        this.numeroTarjetaBono = numeroTarjetaBono;
    }

    public String getTramaEnvio() {
        return tramaEnvio;
    }

    public void setTramaEnvio(String tramaEnvio) {
        this.tramaEnvio = tramaEnvio;
    }

    public void setNumeroTarjetaReimp(String numeroTarjetaReimp) {
        this.numeroTarjetaReimp = numeroTarjetaReimp;
    }

    public String getNumeroTarjetaReimp() {
        return numeroTarjetaReimp;
    }

    public String getTramaRespuesta() {
        return tramaRespuesta;
    }

    public void setTramaRespuesta(String tramaRespuesta) {
        this.tramaRespuesta = tramaRespuesta;
    }

}
