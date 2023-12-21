/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JReservacionesPagosV.java
 *
 * Created on 30-jun-2011, 13:44:55
 */
package com.comerzzia.jpos.gui.reservaciones;

//import com.comerzzia.jpos.correo.DatosCorreo;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.servicios.credito.CreditoDirectoBean;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaInvalidaException;
import com.comerzzia.jpos.servicios.tickets.ImporteInvalidoException;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.JSolicitarAutorizacionVentana;
import com.comerzzia.jpos.gui.JVentas;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.comerzzia.jpos.gui.modelos.PagosTableCellRenderer;
import com.comerzzia.jpos.gui.modelos.PagosTableModel;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoRetencionFuente;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.modelos.PlanesTarjetaTableModel;
import com.comerzzia.jpos.gui.pagos.ManejadorPagos;
import com.comerzzia.jpos.gui.ventas.JErrorValidacionTarjeta;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardBean;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import com.comerzzia.jpos.servicios.letras.LetraCambiosServices;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualBean;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualDao;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardBean;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorAutomaticoNoPermitidoException;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.clientes.tiposClientes.TiposClientes;
import com.comerzzia.jpos.servicios.giftcard.GiftCardConstraintViolationException;
import com.comerzzia.jpos.servicios.giftcard.GiftCardException;
import com.comerzzia.jpos.servicios.giftcard.GiftCardNotFoundException;
import com.comerzzia.jpos.servicios.giftcard.ServicioGiftCard;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoBono;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.tramas.ParserTramaException;
import com.comerzzia.jpos.servicios.tramas.TramaTarjetaGiftCard;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.giftcard.GiftCardAnuladaException;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoBuilder;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoNCuotasGratis;
import com.comerzzia.jpos.servicios.promociones.cupones.ConfigEmisionCupones;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponDescuentoAzar;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.stock.StockTimeOutException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromocionesFiltrosPagos;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Dialog;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.Rectangle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import com.comerzzia.jpos.entity.CupoVirtualMemoria.ValorCupo;
import com.comerzzia.jpos.persistencia.promociones.PromocionSocioBean;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorExcedeCupoException;
import com.comerzzia.jpos.pocesoMensajes.HiloNotificacionConsumo;
import com.comerzzia.jpos.servicios.credito.politica.PoliticaProteccionDatosServices;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidNCException;
import com.comerzzia.jpos.servicios.promociones.PromocionFormaPagoException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromoMedioPago;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;

/**
 *
 * @author MGRI
 */
public class JReservacionesPagosV extends JPanelImagenFondo implements IVista, KeyListener, FocusListener {

    private static final long serialVersionUID = 1L;

    /* ESTADOS !                                    */
    private static byte ESTABLECER_MEDIOS = 1;
    private static byte REALIZAR_PAGOS = 2;
    private static String PROMO_ACEPTADA = "S";
    private byte estado;
    private static final Logger log = Logger.getMLogger(JReservacionesPagosV.class);
    private boolean cancelado;
    private byte modo;
    private Reservacion reservacion;
    private TicketS ticket;
    public JPrincipal ventana_padre = null;
    private JPanel panel_pagos_activo = null;
    private JReservacionesPagosTarjetas jPagosTarjeta = null;
    private Pago pagoSimple;
    // Servicios
    private PrintServices ts = PrintServices.getInstance(); // mover a JVentaEfectuada cuando este creada
    private PagosTicket pagos;
    //Seleccion de columna del pago
    private int lineaSelecionada;
    private boolean editando;
    private JDialog contenedor;
    private ReservaInvitadoBean invitadoSeleccionado;
    private BigDecimal pagoMinimo;
    private BigDecimal pagoMaximo;
    private String cadenaTarjeta;
    private boolean leyendoTarjeta = false;
    private BigDecimal pagoRealizado;
    private int paginaPagoTarjeta = 1;
    private boolean puedePagarTodo = true;
    List<MedioPagoBean> listaPagos; // lista de pagos que admiten reservas (filtrada)
    // Para GiftCard
    TramaTarjetaGiftCard tramaGiftCard;
    GiftCardBean giftCard;
    String autorizador;
    Cliente clienteGC;
    private PagoCredito pagoCredito;
    private Cliente clienteProceso;
    private PlanNovioOBJ planNovio;
    private InvitadoPlanNovio invitadoPlanSeleccionado;
    // Para Credito Directo
    CreditoDirectoBean creditoDirecto;
    // Tipo de cliente tratado
    private Long tipoCliente = null;
    // Para Letras 
    private LetraBean letraCambio;
    private LetraCuotaBean letraCambioCuota;
    private Byte modoPago = null;
    private boolean hayAbonoMinimo;

    private String efectivo_entregado_guardado;
    private String efectivo_total_guardado;
    private String efectivo_usted_paga_guardado;
    private boolean isLiquidarReservacion = false;
    List<LineaTicket> lineasTicket;
    List<PromoMedioPago> listaPromoAceptadas;

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    public JReservacionesPagosV() {
        super();
        try {
            initComponents();
        } catch (Exception e) {
            log.error("JReservacionesPagosV : Error creando ventana" + e.getMessage(), e);
        }
    }

    /*
     * Abono inicial, ampliación, abonos a reservaciones.
     */
    public JReservacionesPagosV(JPrincipal ventana_padre, Reservacion res, BigDecimal totalAPagar, Cliente cliente, ReservaInvitadoBean invitado, BigDecimal pagoMinimo, boolean puedePagarTodo) {
        super();
        // Seteo de información básica del pago
        this.ventana_padre = ventana_padre;
        this.invitadoSeleccionado = invitado;
        this.reservacion = res;
        this.modo = Pago.MODO_ABONO_RESERVA;
        this.puedePagarTodo = puedePagarTodo;
        isLiquidarReservacion = false;

        this.ticket = new TicketS();
        if (res != null && res.getTicket() != null && res.getTicket().getLineas() != null) {
            this.ticket.setLineas(res.getTicket().getLineas());
            this.ticket.setReferenciaSesionPDA(res.getTicket().getReferenciaSesionPDA());
        }

        // CON DESCUENTO A CLIENTE EN CANASTILLA O CON CLIENTE SOCIO   
        if (cliente.isSocio() || !res.isBabyShower()) {
            this.ticket.setCliente(cliente);
            this.tipoCliente = cliente.getTipoCliente();
        } else if (res.isBabyShower()) {
            this.ticket.setCliente(res.getReservacion().getCliente());
            this.tipoCliente = res.getReservacion().getCliente().getTipoCliente();
        }

        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaTotales(totalAPagar);
        // Totales
        this.pagoMinimo = pagoMinimo;
        this.pagoMaximo = totalAPagar;

        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;
        this.clienteProceso = cliente;

        inicializaModoPago();
        iniciaPanel();

    }

    public JReservacionesPagosV(JPrincipal ventana_padre, Reservacion res, BigDecimal totalAPagar, Cliente cliente, ReservaInvitadoBean invitado, BigDecimal pagoMinimo) {
        super();
        // Seteo de información básica del pago
        this.ventana_padre = ventana_padre;
        this.invitadoSeleccionado = invitado;
        this.reservacion = res;
        this.modo = Pago.MODO_ABONO_RESERVA;
        this.puedePagarTodo = true;
        isLiquidarReservacion = true;

        this.ticket = new TicketS();
        if (res != null && res.getTicket() != null && res.getTicket().getLineas() != null) {
            this.ticket.setLineas(res.getTicket().getLineas());
            this.ticket.setReferenciaSesionPDA(res.getTicket().getReferenciaSesionPDA());
        }

        // CON DESCUENTO A CLIENTE EN CANASTILLA O CON CLIENTE SOCIO   
        if (cliente.isSocio() || !res.isBabyShower()) {
            this.ticket.setCliente(cliente);
            this.tipoCliente = cliente.getTipoCliente();
        } else if (res.isBabyShower()) {
            this.ticket.setCliente(res.getReservacion().getCliente());
            this.tipoCliente = res.getReservacion().getCliente().getTipoCliente();
        }

        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaTotales(totalAPagar);
        // Totales
        this.pagoMinimo = totalAPagar;
        this.pagoMaximo = totalAPagar;

        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;

        modoPago = Pago.MODO_NORMAL;
        iniciaPanel();

    }

    /**
     * Modo venta
     *
     */
    public JReservacionesPagosV(TicketS ticket) {
        super();

        // Seteo de información básica del pago
        this.ventana_padre = JPrincipal.getInstance();
        this.ticket = ticket;
        this.tipoCliente = ticket.getCliente().getTipoCliente();
        this.modo = Pago.MODO_NORMAL;
        this.puedePagarTodo = true;

        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaPagos();
        // Totales
        this.pagoMinimo = ticket.getTotales().getTotalAPagar();
        this.pagoMaximo = ticket.getTotales().getTotalAPagar();

        ticket.inicializaTotales(ticket.getTotales().getTotalAPagar());
        ticket.setModoVenta(true);

        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;

        inicializaModoPago();
        iniciaPanel();
    }

    /* Constructor para pagar una GiftCard */
    public JReservacionesPagosV(JPrincipal ventana_padre, BigDecimal totalAPagar, Cliente cliente, BigDecimal pagoMinimo, TramaTarjetaGiftCard tramaGiftCard, GiftCardBean giftCard, String autorizador) {

        super();

        // Seteo de información básica del pago
        this.ventana_padre = ventana_padre;
        this.invitadoSeleccionado = null;
        this.reservacion = null;
        this.pagoMinimo = pagoMinimo;
        this.pagoMaximo = totalAPagar;
        this.ticket = new TicketS();
        this.ticket.setCliente(Sesion.getClienteGenericoReset());

        this.puedePagarTodo = true;
        this.modo = Pago.MODO_GIFTCARD;
        this.tramaGiftCard = tramaGiftCard;
        this.giftCard = giftCard;

        if (this.giftCard == null) {
            this.giftCard = new GiftCardBean();
        }
        this.autorizador = autorizador;
        this.clienteGC = cliente;
        this.tipoCliente = cliente.getTipoCliente();
        // Acciones Previas
        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaTotales(totalAPagar);
        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;

        inicializaModoPago();
        iniciaPanel();
    }

    /*
     *  Caso de reserva con pago de ARTICULOS
     */
    public JReservacionesPagosV(JPrincipal ventana_padre, Reservacion res, BigDecimal totalAPagar, TicketS nuevoTicket, ReservaInvitadoBean invitado, BigDecimal pagoMinimo) {
        super();
        this.ventana_padre = ventana_padre;
        this.invitadoSeleccionado = invitado;
        this.reservacion = res;
        this.pagoMinimo = pagoMinimo;
        this.ticket = nuevoTicket;
        this.modo = Pago.MODO_ARTICULOS;
        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;

        this.tipoCliente = nuevoTicket.getCliente().getTipoCliente();

        // Acciones Previas
        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaTotales(totalAPagar);

        inicializaModoPago();
        iniciaPanel();
    }

    /* CASO DE PAGO DE ARTICULOS DESDE UN PLAN */
    public JReservacionesPagosV(JPrincipal ventana_padre, PlanNovioOBJ plan, BigDecimal totalAPagar, TicketS nuevoTicket, InvitadoPlanNovio invitado, BigDecimal pagoMinimo) {
        super();
        this.ventana_padre = ventana_padre;
        this.invitadoPlanSeleccionado = invitado;
        this.planNovio = plan;
        this.pagoMinimo = pagoMinimo;
        this.ticket = nuevoTicket;

        this.ticket.iniciaDatosBaseTicket();
        this.ticket.iniciaUID();

        this.modo = Pago.MODO_ARTICULOS_PLAN;
        cancelado = true;
        pagoRealizado = BigDecimal.ZERO;

        this.tipoCliente = ticket.getCliente().getTipoCliente();

        // Datos de facturación y de cliente        
        try {
            if (planNovio.getDatosFacturacionTicketBean() != null) {
                ticket.setFacturacion(planNovio.getDatosFacturacionTicketBean());
            } else {
                ticket.setFacturacionCliente();
            }
        } catch (NullPointerException e) {
            ticket.setFacturacionCliente();
        }
        // Acciones Previas
        accionesPrevias();

        inicializaModoPago();
        iniciaPanel();
    }

    /* CASO DE PAGO DE ABONO DESDE UN PLAN */
    public JReservacionesPagosV(PlanNovioOBJ planNovio, BigDecimal pagoMinimo, BigDecimal totalAPagar) {
        super();
        // La instancia al manejador de ventanas la sacamos ahora con el getInstance en lugar de pasarla como parámetros
        this.ventana_padre = JPrincipal.getInstance();

        // Especifico para planes
        this.invitadoPlanSeleccionado = planNovio.getInvitado();
        this.planNovio = planNovio;

        // Valores con los que trabaja la pantalla de pagos
        this.pagoMinimo = pagoMinimo;
        this.pagoMaximo = totalAPagar;
        this.pagoRealizado = BigDecimal.ZERO;
        this.ticket = new TicketS();
        this.ticket.setCliente(planNovio.getClienteSeleccionado());

        this.tipoCliente = planNovio.getClienteSeleccionado().getTipoCliente();
        this.puedePagarTodo = true;
        this.modo = Pago.MODO_ABONO_PLAN;

        // Acciones Previas
        accionesPrevias();
        // Inicialización de totales
        ticket.inicializaTotales(totalAPagar);

        cancelado = true;

        // Inicializamos el panel
        inicializaModoPago();
        iniciaPanel();
    }

    /* CASO PAGO DE UN CREDITO DIRECTO */
    public JReservacionesPagosV(CreditoDirectoBean datosCredito, TicketS ticket) {
        super();
        try {
            // La instancia al manejador de ventanas la sacamos ahora con el getInstance en lugar de pasarla como parámetros
            this.ventana_padre = JPrincipal.getInstance();
            this.creditoDirecto = datosCredito;
            // Valores con los que trabaja la pantalla de pagos
            this.pagoMinimo = ticket.getTotales().getTotalAPagar();
            this.pagoMaximo = ticket.getTotales().getTotalAPagar();
            this.pagoRealizado = BigDecimal.ZERO;
            this.ticket = ticket;
            this.tipoCliente = ticket.getCliente().getTipoCliente();

            this.puedePagarTodo = true;
            this.modo = Pago.MODO_CREDITO_DIRECTO;
            cancelado = true;

            // Acciones Previas
            accionesPrevias();

            // Inicializamos el panel
            inicializaModoPago();
            iniciaPanel();
        } catch (Exception e) {
            log.error("JReservacionesPagosV : Error creando ventana" + e.getMessage(), e);
        }
    }

    /* CASO PAGO DE UNA LETRA DE CAMBIO */
    public JReservacionesPagosV(LetraBean letra, LetraCuotaBean letraCuota) {
        super();
        // La instancia al manejador de ventanas la sacamos ahora con el getInstance en lugar de pasarla como parámetros
        this.ventana_padre = JPrincipal.getInstance();
        this.letraCambio = letra;
        this.letraCambioCuota = letraCuota;
        // Valores con los que trabaja la pantalla de pagos
        this.pagoMinimo = letraCambioCuota.getTotal();
        this.pagoMaximo = letraCambioCuota.getTotal();
        this.pagoRealizado = BigDecimal.ZERO;
        this.ticket = new TicketS();
        this.ticket.setCliente(letra.getCliente());
        this.tipoCliente = letra.getCliente().getTipoCliente();
        this.puedePagarTodo = true;
        this.modo = Pago.MODO_LETRA_CAMBIO;

        // Acciones Previas
        accionesPrevias();

        ticket.inicializaTotales(letraCuota.getTotal());
        cancelado = true;

        // Inicializamos el panel
        inicializaModoPago();
        iniciaPanel();
    }

    private void addButtonMedioPago(JPanel pPagos, Action ac, MedioPagoBean medioPago) {
        JButtonForm button = new JButtonForm(medioPago.getDesMedioPago());
        button.setAction(ac);
        button.setText(medioPago.getDesMedioPago());
        button.setPreferredSize(new Dimension(150, 22));
        ((JPanel) pPagos.getComponent(1)).add(button);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        v_informacion_extra_pago = new javax.swing.JDialog();
        jInformacionExtraPago1 = new com.comerzzia.jpos.gui.JInformacionExtraPago();
        v_lectura_tarjeta = new javax.swing.JDialog();
        p_lectura_tarjeta = new com.comerzzia.jpos.gui.JLecturaTarjeta();
        v_notaCredito = new javax.swing.JDialog();
        jPagosNotaCredito1 = new com.comerzzia.jpos.gui.JPagosNotaCredito();
        v_nota_credito = new javax.swing.JDialog();
        v_bonos = new javax.swing.JDialog();
        p_bonos = new com.comerzzia.jpos.gui.JPagosBonos();
        v_retencion = new javax.swing.JDialog();
        p_retencion = new com.comerzzia.jpos.gui.JRetencion();
        v_animacion = new javax.swing.JDialog();
        p_animacion = new com.comerzzia.jpos.gui.JAnimacion();
        p_autorizando = new javax.swing.JPanel();
        lb_autorizando = new javax.swing.JLabel();
        p_publicidad = new javax.swing.JLabel();
        js_tb_pagos = new javax.swing.JScrollPane();
        tb_pagos = new javax.swing.JTable();
        m_tipos_pagos = new javax.swing.JPanel();
        b_pago_efectivo = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_pago_contado = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_pago_tarjeta = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_pago_otros = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        m_edicion_pagos = new javax.swing.JPanel();
        b_m_volver = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_borrarpago = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_editarpago = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_procesar_pago = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        panel_superior = new javax.swing.JPanel();
        t_pagos_total = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_pagos_usted_paga = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_pagos_total = new javax.swing.JLabel();
        l_pagos_a_cancelar = new javax.swing.JLabel();
        l_pagos_pagado = new javax.swing.JLabel();
        l_pagos_saldo = new javax.swing.JLabel();
        t_pagos_pagado = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_pagos_saldo = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_abono_minimo = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_abono_minimo = new javax.swing.JLabel();
        p_pago_contenedor = new javax.swing.JLayeredPane();
        p_pago_efectivo = new javax.swing.JPanel();
        t_pago_efectivo_total = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_pagos_efectivo_total = new javax.swing.JLabel();
        l_pagos_efectivo_a_cancelar = new javax.swing.JLabel();
        t_pago_efectivo_usted_paga = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_pagos_efectivo_entregado = new javax.swing.JLabel();
        t_pago_efectivo_entregado = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_pagos_efectivo_descuento = new javax.swing.JLabel();
        l_texto_descuento = new javax.swing.JLabel();
        l_medio_pago = new javax.swing.JLabel();
        b_aceptar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_pago_tarjeta = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        p_pago_otros = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        p_pago_contado = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        p_pago_giftcard = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lb_giftcard = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lb_total_ahorro = new javax.swing.JLabel();

        v_informacion_extra_pago.setAlwaysOnTop(true);
        v_informacion_extra_pago.setMinimumSize(null);
        v_informacion_extra_pago.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_informacion_extra_pago.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_informacion_extra_pagoLayout = new javax.swing.GroupLayout(v_informacion_extra_pago.getContentPane());
        v_informacion_extra_pago.getContentPane().setLayout(v_informacion_extra_pagoLayout);
        v_informacion_extra_pagoLayout.setHorizontalGroup(
                v_informacion_extra_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jInformacionExtraPago1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        v_informacion_extra_pagoLayout.setVerticalGroup(
                v_informacion_extra_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jInformacionExtraPago1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_lectura_tarjeta.setMinimumSize(new java.awt.Dimension(354, 100));
        v_lectura_tarjeta.setModal(true);
        v_lectura_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_lectura_tarjeta.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_lectura_tarjetaWindowGainedFocus(evt);
            }

            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout v_lectura_tarjetaLayout = new javax.swing.GroupLayout(v_lectura_tarjeta.getContentPane());
        v_lectura_tarjeta.getContentPane().setLayout(v_lectura_tarjetaLayout);
        v_lectura_tarjetaLayout.setHorizontalGroup(
                v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_lectura_tarjetaLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        v_lectura_tarjetaLayout.setVerticalGroup(
                v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(v_lectura_tarjetaLayout.createSequentialGroup()
                                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(214, Short.MAX_VALUE))
        );

        v_notaCredito.setAlwaysOnTop(true);
        v_notaCredito.setMinimumSize(new java.awt.Dimension(460, 180));
        v_notaCredito.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_notaCredito.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_notaCredito.setName("resNotaCredito"); // NOI18N

        javax.swing.GroupLayout v_notaCreditoLayout = new javax.swing.GroupLayout(v_notaCredito.getContentPane());
        v_notaCredito.getContentPane().setLayout(v_notaCreditoLayout);
        v_notaCreditoLayout.setHorizontalGroup(
                v_notaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 440, Short.MAX_VALUE)
                        .addGroup(v_notaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(v_notaCreditoLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jPagosNotaCredito1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_notaCreditoLayout.setVerticalGroup(
                v_notaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                        .addGroup(v_notaCreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(v_notaCreditoLayout.createSequentialGroup()
                                        .addGap(0, 73, Short.MAX_VALUE)
                                        .addComponent(jPagosNotaCredito1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 73, Short.MAX_VALUE)))
        );

        v_nota_credito.setAlwaysOnTop(true);
        v_nota_credito.setModal(true);
        v_nota_credito.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        javax.swing.GroupLayout v_nota_creditoLayout = new javax.swing.GroupLayout(v_nota_credito.getContentPane());
        v_nota_credito.getContentPane().setLayout(v_nota_creditoLayout);
        v_nota_creditoLayout.setHorizontalGroup(
                v_nota_creditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );
        v_nota_creditoLayout.setVerticalGroup(
                v_nota_creditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );

        v_bonos.setAlwaysOnTop(true);
        v_bonos.setMinimumSize(new java.awt.Dimension(380, 140));
        v_bonos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_bonos.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_bonosLayout = new javax.swing.GroupLayout(v_bonos.getContentPane());
        v_bonos.getContentPane().setLayout(v_bonosLayout);
        v_bonosLayout.setHorizontalGroup(
                v_bonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(v_bonosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(p_bonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_bonosLayout.setVerticalGroup(
                v_bonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_bonosLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(p_bonos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(223, 223, 223))
        );

        v_retencion.setMinimumSize(new java.awt.Dimension(568, 450));
        v_retencion.setModal(true);
        v_retencion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_retencion.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_retencionWindowGainedFocus(evt);
            }

            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout v_retencionLayout = new javax.swing.GroupLayout(v_retencion.getContentPane());
        v_retencion.getContentPane().setLayout(v_retencionLayout);
        v_retencionLayout.setHorizontalGroup(
                v_retencionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(v_retencionLayout.createSequentialGroup()
                                .addComponent(p_retencion, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_retencionLayout.setVerticalGroup(
                v_retencionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(v_retencionLayout.createSequentialGroup()
                                .addComponent(p_retencion, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        v_animacion.setMinimumSize(new java.awt.Dimension(1024, 768));

        p_animacion.setMaximumSize(new java.awt.Dimension(1024, 768));

        javax.swing.GroupLayout v_animacionLayout = new javax.swing.GroupLayout(v_animacion.getContentPane());
        v_animacion.getContentPane().setLayout(v_animacionLayout);
        v_animacionLayout.setHorizontalGroup(
                v_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(v_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(v_animacionLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(p_animacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_animacionLayout.setVerticalGroup(
                v_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(v_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(v_animacionLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(p_animacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        setFont(new java.awt.Font("sansserif", 1, 14));
        setMaximumSize(new java.awt.Dimension(1094, 734));
        setMinimumSize(new java.awt.Dimension(1094, 734));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1094, 734));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p_autorizando.setBackground(new java.awt.Color(255, 255, 255));
        p_autorizando.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lb_autorizando.setFont(lb_autorizando.getFont().deriveFont(lb_autorizando.getFont().getStyle() | java.awt.Font.BOLD, lb_autorizando.getFont().getSize() + 2));
        lb_autorizando.setForeground(new java.awt.Color(51, 153, 255));
        lb_autorizando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_autorizando.setText("Autorización en proceso...");

        javax.swing.GroupLayout p_autorizandoLayout = new javax.swing.GroupLayout(p_autorizando);
        p_autorizando.setLayout(p_autorizandoLayout);
        p_autorizandoLayout.setHorizontalGroup(
                p_autorizandoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_autorizandoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb_autorizando, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                                .addContainerGap())
        );
        p_autorizandoLayout.setVerticalGroup(
                p_autorizandoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_autorizandoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb_autorizando, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                                .addContainerGap())
        );

        add(p_autorizando, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, -1, -1));
        p_autorizando.setVisible(false);
        add(p_publicidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 138, 563));

        tb_pagos.setFocusCycleRoot(true);
        tb_pagos.setGridColor(new java.awt.Color(153, 204, 255));
        tb_pagos.setRowHeight(20);
        tb_pagos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        js_tb_pagos.setViewportView(tb_pagos);

        add(js_tb_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 790, 150));

        m_tipos_pagos.setOpaque(false);

        b_pago_efectivo.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.pagos.efectivo").charAt(0));
        b_pago_efectivo.setText("<html><center> EFECTIVO  &nbsp;  ( F5 )</center></html>");
        b_pago_efectivo.setActionCommand("");
        b_pago_efectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pago_efectivoActionPerformed(evt);
            }
        });

        b_pago_contado.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.pagos.contado").charAt(0));
        b_pago_contado.setText("<html><center> CONTADO &nbsp; ( F6 )</center></html>");
        b_pago_contado.setActionCommand("");
        b_pago_contado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        b_pago_contado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pago_contadoActionPerformed(evt);
            }
        });

        b_pago_tarjeta.setText("<html><center> TARJETA  &nbsp; ( F7 )</center></html>");
        b_pago_tarjeta.setActionCommand("");
        b_pago_tarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pago_tarjetaActionPerformed(evt);
            }
        });

        b_pago_otros.setText("<html><center> OTROS  &nbsp; ( F8 )</center></html>");
        b_pago_otros.setActionCommand("");
        b_pago_otros.setNextFocusableComponent(b_m_volver);
        b_pago_otros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pago_otrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout m_tipos_pagosLayout = new javax.swing.GroupLayout(m_tipos_pagos);
        m_tipos_pagos.setLayout(m_tipos_pagosLayout);
        m_tipos_pagosLayout.setHorizontalGroup(
                m_tipos_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(m_tipos_pagosLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(m_tipos_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(b_pago_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(b_pago_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(b_pago_contado, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(b_pago_efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        m_tipos_pagosLayout.setVerticalGroup(
                m_tipos_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(m_tipos_pagosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(b_pago_efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pago_contado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pago_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pago_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(28, Short.MAX_VALUE))
        );

        add(m_tipos_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 330, 240, 190));

        m_edicion_pagos.setBackground(new java.awt.Color(255, 255, 255));
        m_edicion_pagos.setOpaque(false);
        m_edicion_pagos.setPreferredSize(new java.awt.Dimension(490, 80));
        m_edicion_pagos.setRequestFocusEnabled(false);
        m_edicion_pagos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_m_volver.setText("<html><center> Volver <br/>F2</center></html>");
        b_m_volver.setActionCommand("");
        b_m_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_volverActionPerformed(evt);
            }
        });
        m_edicion_pagos.add(b_m_volver, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 89, 56));

        b_m_borrarpago.setText("<html><center>Borrar Pago <br/>F3</center></html>");
        b_m_borrarpago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_borrarpagoActionPerformed(evt);
            }
        });
        m_edicion_pagos.add(b_m_borrarpago, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 89, 56));

        b_m_editarpago.setText("<html><center>Editar Pago<br/>F4</center></html>");
        b_m_editarpago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_editarpagoActionPerformed(evt);
            }
        });
        m_edicion_pagos.add(b_m_editarpago, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 89, 56));

        b_m_procesar_pago.setText("<html><center>Procesar Pago<br/>F9</center></html>");
        b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);
        b_m_procesar_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_procesar_pagoActionPerformed(evt);
            }
        });
        m_edicion_pagos.add(b_m_procesar_pago, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 88, 56));

        add(m_edicion_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 510, 520, 60));

        panel_superior.setOpaque(false);

        t_pagos_total.setEditable(false);
        t_pagos_total.setFocusable(false);
        t_pagos_total.setFont(new java.awt.Font("Tahoma", 0, 14));

        t_pagos_usted_paga.setEditable(false);
        t_pagos_usted_paga.setFocusable(false);
        t_pagos_usted_paga.setFont(new java.awt.Font("Tahoma", 0, 14));

        l_pagos_total.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_total.setForeground(new java.awt.Color(51, 153, 255));
        l_pagos_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_total.setLabelFor(t_pagos_total);
        l_pagos_total.setText("Valor:");

        l_pagos_a_cancelar.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_a_cancelar.setForeground(new java.awt.Color(51, 153, 255));
        l_pagos_a_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_a_cancelar.setLabelFor(t_pagos_usted_paga);
        l_pagos_a_cancelar.setText("Valor con Dscto:");

        l_pagos_pagado.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        l_pagos_pagado.setForeground(new java.awt.Color(51, 153, 255));
        l_pagos_pagado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_pagado.setLabelFor(t_pagos_pagado);
        l_pagos_pagado.setText("Subtotal:");

        l_pagos_saldo.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_saldo.setForeground(new java.awt.Color(51, 153, 255));
        l_pagos_saldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_saldo.setLabelFor(t_pagos_saldo);
        l_pagos_saldo.setText("Saldo:");

        t_pagos_pagado.setEditable(false);
        t_pagos_pagado.setText("0");
        t_pagos_pagado.setFocusable(false);
        t_pagos_pagado.setFont(new java.awt.Font("Tahoma", 0, 14));

        t_pagos_saldo.setEditable(false);
        t_pagos_saldo.setFocusable(false);
        t_pagos_saldo.setFont(new java.awt.Font("Tahoma", 0, 14));

        t_abono_minimo.setEditable(false);
        t_abono_minimo.setFocusable(false);
        t_abono_minimo.setFont(new java.awt.Font("Tahoma", 0, 14));

        lb_abono_minimo.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        lb_abono_minimo.setForeground(new java.awt.Color(51, 153, 255));
        lb_abono_minimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_abono_minimo.setText("Abono Mínimo");
        lb_abono_minimo.setFocusable(false);

        javax.swing.GroupLayout panel_superiorLayout = new javax.swing.GroupLayout(panel_superior);
        panel_superior.setLayout(panel_superiorLayout);
        panel_superiorLayout.setHorizontalGroup(
                panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_superiorLayout.createSequentialGroup()
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panel_superiorLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(l_pagos_a_cancelar))
                                        .addGroup(panel_superiorLayout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addComponent(l_pagos_total, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(t_pagos_usted_paga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_pagos_total, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(l_pagos_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(l_pagos_pagado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(t_pagos_pagado, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_pagos_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lb_abono_minimo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(t_abono_minimo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31))
        );
        panel_superiorLayout.setVerticalGroup(
                panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_superiorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(l_pagos_total)
                                        .addComponent(lb_abono_minimo)
                                        .addComponent(t_pagos_pagado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(l_pagos_pagado)
                                        .addComponent(t_pagos_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(t_pagos_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(l_pagos_saldo)
                                        .addComponent(l_pagos_a_cancelar)
                                        .addComponent(t_abono_minimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_pagos_usted_paga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        add(panel_superior, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 760, 80));

        p_pago_efectivo.setMaximumSize(new java.awt.Dimension(550, 180));
        p_pago_efectivo.setMinimumSize(new java.awt.Dimension(550, 180));
        p_pago_efectivo.setOpaque(false);

        t_pago_efectivo_total.setText("0");
        t_pago_efectivo_total.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_pago_efectivo_total.setName("total"); // NOI18N
        t_pago_efectivo_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_pago_efectivo_totalActionPerformed(evt);
            }
        });

        l_pagos_efectivo_total.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.pagos.efectivo.total").charAt(0));
        l_pagos_efectivo_total.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_efectivo_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_efectivo_total.setLabelFor(t_pago_efectivo_total);
        l_pagos_efectivo_total.setText("Valor:");

        l_pagos_efectivo_a_cancelar.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.pagos.totales.usted.paga2").charAt(0));
        l_pagos_efectivo_a_cancelar.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_efectivo_a_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_efectivo_a_cancelar.setLabelFor(t_pago_efectivo_usted_paga);
        l_pagos_efectivo_a_cancelar.setText("Usted Paga (Dscto):");

        t_pago_efectivo_usted_paga.setText("0");
        t_pago_efectivo_usted_paga.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_pago_efectivo_usted_paga.setName("aCancelar"); // NOI18N

        l_pagos_efectivo_entregado.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.pagos.efectivo.entregado").charAt(0));
        l_pagos_efectivo_entregado.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        l_pagos_efectivo_entregado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_efectivo_entregado.setLabelFor(t_pago_efectivo_entregado);
        l_pagos_efectivo_entregado.setText("Valor Recibido:");

        t_pago_efectivo_entregado.setText("0");
        t_pago_efectivo_entregado.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_pago_efectivo_entregado.setName("entregado"); // NOI18N
        t_pago_efectivo_entregado.setNextFocusableComponent(b_aceptar);

        l_pagos_efectivo_descuento.setFont(new java.awt.Font("Comic Sans MS", 1, 14));
        l_pagos_efectivo_descuento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_efectivo_descuento.setOpaque(true);

        l_texto_descuento.setFont(new java.awt.Font("Comic Sans MS", 0, 12));
        l_texto_descuento.setText("% de descuento");
        l_texto_descuento.setFocusable(false);

        l_medio_pago.setFont(new java.awt.Font("Comic Sans MS", 1, 14));
        l_medio_pago.setText("EFECTIVO");

        b_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_aceptar.setMnemonic('a');
        b_aceptar.setText("Aceptar");
        b_aceptar.setFont(new java.awt.Font("Tahoma", 0, 18));
        b_aceptar.setNextFocusableComponent(b_pago_efectivo);
        b_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_pago_efectivoLayout = new javax.swing.GroupLayout(p_pago_efectivo);
        p_pago_efectivo.setLayout(p_pago_efectivoLayout);
        p_pago_efectivoLayout.setHorizontalGroup(
                p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_efectivoLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(l_medio_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(p_pago_efectivoLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(l_pagos_efectivo_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(l_pagos_efectivo_a_cancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(l_pagos_efectivo_entregado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(t_pago_efectivo_entregado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(t_pago_efectivo_usted_paga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(t_pago_efectivo_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(p_pago_efectivoLayout.createSequentialGroup()
                                                                .addGap(4, 4, 4)
                                                                .addComponent(l_pagos_efectivo_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(l_texto_descuento)
                                                                .addGap(66, 66, 66))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_efectivoLayout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(24, 24, 24)))))
                                .addGap(64, 64, 64))
        );
        p_pago_efectivoLayout.setVerticalGroup(
                p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_efectivoLayout.createSequentialGroup()
                                .addComponent(l_medio_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(t_pago_efectivo_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(l_pagos_efectivo_total))
                                .addGap(18, 18, 18)
                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(p_pago_efectivoLayout.createSequentialGroup()
                                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(t_pago_efectivo_usted_paga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(l_pagos_efectivo_a_cancelar))
                                                .addGap(18, 18, 18)
                                                .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(t_pago_efectivo_entregado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(l_pagos_efectivo_entregado)))
                                        .addGroup(p_pago_efectivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(l_pagos_efectivo_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(l_texto_descuento)))
                                .addContainerGap(32, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_efectivoLayout.createSequentialGroup()
                                .addContainerGap(134, Short.MAX_VALUE)
                                .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        p_pago_efectivo.setBounds(0, 0, 601, 180);
        p_pago_contenedor.add(p_pago_efectivo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        p_pago_tarjeta.setMaximumSize(new java.awt.Dimension(550, 180));
        p_pago_tarjeta.setMinimumSize(new java.awt.Dimension(550, 180));
        p_pago_tarjeta.setOpaque(false);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/flecha_nav.png"))); // NOI18N

        jPanel1.setOpaque(false);

        javax.swing.GroupLayout p_pago_tarjetaLayout = new javax.swing.GroupLayout(p_pago_tarjeta);
        p_pago_tarjeta.setLayout(p_pago_tarjetaLayout);
        p_pago_tarjetaLayout.setHorizontalGroup(
                p_pago_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_pago_tarjetaLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addContainerGap(522, Short.MAX_VALUE))
                        .addGroup(p_pago_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(p_pago_tarjetaLayout.createSequentialGroup()
                                        .addContainerGap(29, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        p_pago_tarjetaLayout.setVerticalGroup(
                p_pago_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_tarjetaLayout.createSequentialGroup()
                                .addContainerGap(114, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(47, 47, 47))
                        .addGroup(p_pago_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_tarjetaLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
        );

        p_pago_tarjeta.setBounds(0, 0, 550, 180);
        p_pago_contenedor.add(p_pago_tarjeta, javax.swing.JLayeredPane.DEFAULT_LAYER);

        p_pago_otros.setMaximumSize(new java.awt.Dimension(550, 180));
        p_pago_otros.setMinimumSize(new java.awt.Dimension(550, 180));
        p_pago_otros.setOpaque(false);
        p_pago_otros.setPreferredSize(new java.awt.Dimension(550, 180));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/flecha_nav.png"))); // NOI18N

        jPanel3.setOpaque(false);

        javax.swing.GroupLayout p_pago_otrosLayout = new javax.swing.GroupLayout(p_pago_otros);
        p_pago_otros.setLayout(p_pago_otrosLayout);
        p_pago_otrosLayout.setHorizontalGroup(
                p_pago_otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_pago_otrosLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addContainerGap(522, Short.MAX_VALUE))
                        .addGroup(p_pago_otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(p_pago_otrosLayout.createSequentialGroup()
                                        .addContainerGap(28, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        p_pago_otrosLayout.setVerticalGroup(
                p_pago_otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_otrosLayout.createSequentialGroup()
                                .addContainerGap(155, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addContainerGap())
                        .addGroup(p_pago_otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_otrosLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
        );

        p_pago_otros.setBounds(0, 0, 550, 180);
        p_pago_contenedor.add(p_pago_otros, javax.swing.JLayeredPane.DEFAULT_LAYER);

        p_pago_contado.setMinimumSize(new java.awt.Dimension(550, 180));
        p_pago_contado.setOpaque(false);
        p_pago_contado.setPreferredSize(new java.awt.Dimension(550, 180));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/flecha_nav.png"))); // NOI18N

        jPanel2.setOpaque(false);

        javax.swing.GroupLayout p_pago_contadoLayout = new javax.swing.GroupLayout(p_pago_contado);
        p_pago_contado.setLayout(p_pago_contadoLayout);
        p_pago_contadoLayout.setHorizontalGroup(
                p_pago_contadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_pago_contadoLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addContainerGap(522, Short.MAX_VALUE))
                        .addGroup(p_pago_contadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(p_pago_contadoLayout.createSequentialGroup()
                                        .addContainerGap(27, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        p_pago_contadoLayout.setVerticalGroup(
                p_pago_contadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_pago_contadoLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jLabel11)
                                .addContainerGap(84, Short.MAX_VALUE))
                        .addGroup(p_pago_contadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_contadoLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
        );

        p_pago_contado.setBounds(0, 0, 550, 180);
        p_pago_contenedor.add(p_pago_contado, javax.swing.JLayeredPane.DEFAULT_LAYER);

        p_pago_giftcard.setMinimumSize(new java.awt.Dimension(550, 180));
        p_pago_giftcard.setOpaque(false);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/flecha_nav.png"))); // NOI18N

        jPanel4.setOpaque(false);

        lb_giftcard.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        lb_giftcard.setForeground(new java.awt.Color(51, 153, 255));
        lb_giftcard.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_giftcard.setText(" Pase la tarjeta para leer la GiftCard");
        jPanel4.add(lb_giftcard);

        javax.swing.GroupLayout p_pago_giftcardLayout = new javax.swing.GroupLayout(p_pago_giftcard);
        p_pago_giftcard.setLayout(p_pago_giftcardLayout);
        p_pago_giftcardLayout.setHorizontalGroup(
                p_pago_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_pago_giftcardLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addContainerGap(522, Short.MAX_VALUE))
                        .addGroup(p_pago_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(p_pago_giftcardLayout.createSequentialGroup()
                                        .addContainerGap(27, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        p_pago_giftcardLayout.setVerticalGroup(
                p_pago_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_giftcardLayout.createSequentialGroup()
                                .addContainerGap(141, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addGap(30, 30, 30))
                        .addGroup(p_pago_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_pago_giftcardLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
        );

        p_pago_giftcard.setBounds(0, 0, 550, 190);
        p_pago_contenedor.add(p_pago_giftcard, javax.swing.JLayeredPane.DEFAULT_LAYER);

        add(p_pago_contenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 310, 550, 200));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        jLabel1.setForeground(new java.awt.Color(51, 153, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Total Ahorro :");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 490, 30));

        lb_total_ahorro.setFont(new java.awt.Font("Comic Sans MS", 1, 14));
        lb_total_ahorro.setForeground(new java.awt.Color(255, 0, 0));
        lb_total_ahorro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        add(lb_total_ahorro, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 260, 80, 30));
    }// </editor-fold>                        

    // <editor-fold defaultstate="collapsed" desc="Acciones De Botones">  
    private void b_m_procesar_pagoActionPerformed(java.awt.event.ActionEvent evt) {
        if (b_m_procesar_pago.isEnabled()) {
            accionProcesarPago();
        }

    }

    private void b_m_editarpagoActionPerformed(java.awt.event.ActionEvent evt) {
        accionEditarPago();

    }

    private void b_m_borrarpagoActionPerformed(java.awt.event.ActionEvent evt) {
        accionBorrarPago();
    }

    private void b_m_volverActionPerformed(java.awt.event.ActionEvent evt) {
        accionVolver(false, true);
    }

    private void b_pago_efectivoActionPerformed(java.awt.event.ActionEvent evt) {
        accionListaPagoEfectivo();

    }

    private void b_pago_contadoActionPerformed(java.awt.event.ActionEvent evt) {
        accionListaPagoContado();
    }

    private void b_pago_tarjetaActionPerformed(java.awt.event.ActionEvent evt) {
        accionListaPagoTarjeta();
    }

    private void b_pago_otrosActionPerformed(java.awt.event.ActionEvent evt) {
        AccionListaPagoOtros();
    }

    private void t_pago_efectivo_totalActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void b_aceptarActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            Pago pagoATratar = null;
            pagoATratar = pagoSimple;

            if ((pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0 && (!ticket.getPagos().contieneEfectivo() || !l_medio_pago.getText().equals("EFECTIVO"))) || editando) {

                if (new BigDecimal(t_pago_efectivo_entregado.getText()).compareTo(Sesion.getDatosConfiguracion().getMaximoPagos()) > 0) {
                    t_pago_efectivo_entregado.setText("");
                    throw new PagoInvalidException("Ha introducido un valor de pago superior al máximo permitido");
                }
                if (pagoATratar.requiereAutorizacionMontoMaximo(t_pago_efectivo_total.getText())) {
                    try {
                        String usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, "El valor del pago requiere autorización");
                        ticket.setAutorizadorVenta(usuarioAutorizador);
                    } catch (SinPermisosException ex) {
                        log.debug("No se tienen permisos para seleccionar medio pago con importe superior a monto máximo.");
                        return;
                    }
                }
                pagoATratar.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                pagoATratar.establecerEntregado(t_pago_efectivo_entregado.getText());

                if (isModoGiftCard() && pagoSimple.isPagoTarjeta()) { // TODO: GIFTCARD amos: esto se supone que tiene que sobrar
                    pagoCredito.getPlanSeleccionado().recalcularFromTotal(pagoATratar.getEntregado(), BigDecimal.ZERO);
                }
                pagoATratar.validar();
                if (pagoATratar.tieneInformacionExtra() && !pagoATratar.isPagoOtros()) {

                    // Todo esto estará referenciando ap pago simple
                    jInformacionExtraPago1.iniciaFoco();
                    JPrincipal.setPopupActivo(v_informacion_extra_pago);
                    v_informacion_extra_pago.setVisible(true);
                    JPrincipal.setPopupActivo(null);
                }
                if (!editando) {
                    // Si es pago de credito             

                    ticket.crearNuevaLineaPago(pagoATratar);
                    System.out.println('\b');
                }
                ticket.getPagos().recalculaTotales();
                tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                setTamanosTabla();
                refrescarTotales();
                b_pago_efectivoActionPerformed(null);
                editando = false;

            } else {
                if (ticket.getPagos().contieneEfectivo() && l_medio_pago.getText().equals("EFECTIVO")) {
                    log.debug("No se permite la edición de efectivo");
                    ventana_padre.crearAdvertencia("Se ha desactivado la edición automática de efectivo.");
                } else {
                    log.debug("No se permiten pagos con saldo 0.");
                    ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                }

            }
        } catch (ImporteInvalidoException e) {
            ventana_padre.crearError(e.getMessage());
        } catch (PagoInvalidException ex) {
            log.debug("b_aceptarActionPerformed() - El pago no es valido: ustedPaga:" + t_pago_efectivo_usted_paga.getText() + " entregado:" + t_pago_efectivo_entregado.getText());
            ventana_padre.crearError(ex.getMessage());
        }
        ticket.getPagos().recalculaTotales();
        refrescarTablaPagos();
        refrescarTotales();

    }

    private void v_lectura_tarjetaWindowGainedFocus(java.awt.event.WindowEvent evt) {
        p_lectura_tarjeta.iniciaFoco();
    }

    private void v_retencionWindowGainedFocus(java.awt.event.WindowEvent evt) {
        p_retencion.iniciaFoco();
    }
    // Variables declaration - do not modify                     
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_borrarpago;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_editarpago;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_procesar_pago;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_volver;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pago_contado;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pago_efectivo;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pago_otros;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pago_tarjeta;
    private com.comerzzia.jpos.gui.JInformacionExtraPago jInformacionExtraPago1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.comerzzia.jpos.gui.JPagosNotaCredito jPagosNotaCredito1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane js_tb_pagos;
    private javax.swing.JLabel l_medio_pago;
    private javax.swing.JLabel l_pagos_a_cancelar;
    private javax.swing.JLabel l_pagos_efectivo_a_cancelar;
    private javax.swing.JLabel l_pagos_efectivo_descuento;
    private javax.swing.JLabel l_pagos_efectivo_entregado;
    private javax.swing.JLabel l_pagos_efectivo_total;
    private javax.swing.JLabel l_pagos_pagado;
    private javax.swing.JLabel l_pagos_saldo;
    private javax.swing.JLabel l_pagos_total;
    private javax.swing.JLabel l_texto_descuento;
    private javax.swing.JLabel lb_abono_minimo;
    private javax.swing.JLabel lb_autorizando;
    private javax.swing.JLabel lb_giftcard;
    private javax.swing.JLabel lb_total_ahorro;
    private javax.swing.JPanel m_edicion_pagos;
    private javax.swing.JPanel m_tipos_pagos;
    private com.comerzzia.jpos.gui.JAnimacion p_animacion;
    private javax.swing.JPanel p_autorizando;
    private com.comerzzia.jpos.gui.JPagosBonos p_bonos;
    private com.comerzzia.jpos.gui.JLecturaTarjeta p_lectura_tarjeta;
    private javax.swing.JPanel p_pago_contado;
    private javax.swing.JLayeredPane p_pago_contenedor;
    private javax.swing.JPanel p_pago_efectivo;
    private javax.swing.JPanel p_pago_giftcard;
    private javax.swing.JPanel p_pago_otros;
    private javax.swing.JPanel p_pago_tarjeta;
    private javax.swing.JLabel p_publicidad;
    private com.comerzzia.jpos.gui.JRetencion p_retencion;
    private javax.swing.JPanel panel_superior;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_abono_minimo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pago_efectivo_entregado;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pago_efectivo_total;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pago_efectivo_usted_paga;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pagos_pagado;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pagos_saldo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pagos_total;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_pagos_usted_paga;
    private javax.swing.JTable tb_pagos;
    private javax.swing.JDialog v_animacion;
    private javax.swing.JDialog v_bonos;
    private javax.swing.JDialog v_informacion_extra_pago;
    private javax.swing.JDialog v_lectura_tarjeta;
    private javax.swing.JDialog v_notaCredito;
    private javax.swing.JDialog v_nota_credito;
    private javax.swing.JDialog v_retencion;
    // End of variables declaration                   
// </editor-fold>

    protected void cambiaPanelPago(JPanel pamostrar) {
        if (panel_pagos_activo != null) {
            panel_pagos_activo.setVisible(false);
        }
        panel_pagos_activo = pamostrar;
        panel_pagos_activo.setVisible(true);
    }

    /**
     * Metodo que inicia la vista de Pagos
     */
    @Override
    public void iniciaVista() {
        log.debug("Iniciando vista de pantalla de pagos...");
        estado = JReservacionesPagosV.ESTABLECER_MEDIOS;
        // Eliminamos toda la información de los pagos anteriores
        if (reservacion != null && reservacion.getTicket() != null && reservacion.getTicket().getPagos() != null) {
            ticket.inicializaPagos(reservacion.getTicket().getPagos());
        } else {
            ticket.inicializaPagos();
        }
        pagos = ticket.getPagos();

        // Para permitir abonos por encima de un maximo
        if (pagos.getSaldo() == null) {
            pagos.setCantidadPagoBonosDefecto(BigDecimal.TEN);
        }

        // Actualizamos totales en función del ticket
        refrescarTotales();

        // Inicializamos tabla de pagos
        tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
        setTamanosTabla();
        addFunctionKeys();

        // como si hubiera pinchado en medio de pago efectivo
        b_pago_efectivoActionPerformed(null);

        if (this.pagoMinimo.compareTo(pagos.getTotal()) < 0
                && this.pagoMinimo.compareTo(BigDecimal.ZERO) != 0
                && this.reservacion != null
                && !this.reservacion.getReservacion().isTipoBabyShower()) {
            ventana_padre.crearInformacion("Abono mínimo : $ " + pagoMinimo.toString());
            hayAbonoMinimo = true;
        } else {
            hayAbonoMinimo = false;
        }
        habilitaElementos(true);
        JPrincipal.setPanelActivo(this);
        log.debug("Pantalla de pagos inicializada.");

    }

    /**
     * Inicia los valores de los cuadros de texto totales
     */
    private void refrescarTotales() {
        ticket.recalcularFinalPagado();
        t_pagos_usted_paga.setText(pagos.getUstedPaga().toString());
//        if (pagoSimple != null) {
//            if (pagoSimple.getMedioPagoActivo().isContado()) {
//                try {
//                    pagoSimple.recalcularFromUstedPagaSupermaxi(pagos.getUstedPaga().toString(), ticket.getPagos());
//                    pagoSimple.recalcularFromUstedPaga(pagos.getUstedPaga().toString());
//                } catch (PagoInvalidSupermaxiException ex) {
//                    log.debug("El pago no cubre el valor de las garantía extendida.");
//                    ventana_padre.crearError(ex.getMessage());
//                }
//            }
//        }
        t_pagos_pagado.setText(ticket.getTotales().getBase().toString());
        if (pagos.getSaldo() != null && pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            t_pagos_saldo.setForeground(Color.red);
        } else {
            t_pagos_saldo.setForeground(Color.black);
        }
        if (pagos.getSaldo() != null) {
            t_pagos_saldo.setText(pagos.getSaldo().toString());
        }
        t_pagos_total.setText(pagos.getTotal().toString());
        lb_total_ahorro.setText("$ " + pagos.getTotalAhorro().toString());
//        jLabel1.setText("<html><span color=\"black\">Subtotal "+(Sesion.getEmpresa().getPorcentajeIva().toString().trim())+"%: $"+ ticket.getTotales().getSubtotal12().toString()+ " / Subtotal 0%: $" + ticket.getTotales().getSubtotal0().toString() +"  Iva: $"+ticket.getTotales().getImpuestosString()+ "</span><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Ahorro:</span>");
        jLabel1.setText("<html><span color=\"black\">Subtotal " + (Sesion.getEmpresa().getPorcentajeIva().toString().trim()) + "%: $" + ticket.getTotales().getSubtotal12().toString() + " / Subtotal 0%: $" + ticket.getTotales().getSubtotal0().toString() + " / Iva: $" + ticket.getTotales().getImpuestosString() + "</span><span>&nbsp;&nbsp;&nbsp;Total Ahorro:</span>");
        if (pagos.isCompensacionAplicada()) {
            jLabel1.setText(jLabel1.getText() + "<br><span color=\"black\">Compensación Gobierno " + VariablesAlm.getVariable(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO) + "%: $" + ticket.getTotales().getCompensacionGobierno().toString() + "</span></html>");
        } else {
            jLabel1.setText(jLabel1.getText() + "</html>");
        }
    }

    private boolean isModoVenta() {
        return (modo == Pago.MODO_NORMAL);
    }

    private boolean isModoFactura() {
        return isModoVenta() || isModoArticulosReservacion() || isModoArticulosPlan();
    }

    private boolean isModoAbono() {
        return isModoAbonoReservacion() || isModoAbonoPlan();
    }

    private boolean isModoAbonoReservacion() {
        return (modo == Pago.MODO_ABONO_RESERVA);
    }

    private boolean isModoAbonoPlan() {
        return (modo == Pago.MODO_ABONO_PLAN);
    }

    private boolean isModoArticulosReservacion() {
        return (modo == Pago.MODO_ARTICULOS);
    }

    private boolean isModoArticulosPlan() {
        return (modo == Pago.MODO_ARTICULOS_PLAN);
    }

    private boolean isModoGiftCard() {
        return (modo == Pago.MODO_GIFTCARD);
    }

    private boolean isModoLetraCambio() {
        return (modo == Pago.MODO_LETRA_CAMBIO);
    }

    private boolean isModoCreditoDirecto() {
        return (modo == Pago.MODO_CREDITO_DIRECTO);
    }

    private boolean isModoAbonoMinimo() {
        return ((modo == Pago.MODO_ABONO_RESERVA)
                || (modo == Pago.MODO_ABONO_PLAN)
                || (modo == Pago.MODO_LETRA_CAMBIO)
                || (modo == Pago.MODO_GIFTCARD)
                || (modo == Pago.MODO_CREDITO_DIRECTO));
    }

    private void refrescarPagosEfectivos() {
        t_pago_efectivo_total.setText(pagoSimple.getTotal().toString());
        t_pago_efectivo_usted_paga.setText(pagoSimple.getUstedPaga().toString());
        t_pago_efectivo_entregado.setText(pagoSimple.getEntregado().toString());
        l_pagos_efectivo_descuento.setText(pagoSimple.getDescuento().toString());
    }

    @Override
    public void keyTyped(KeyEvent ke) {

        if (ke.getKeyChar() == '%') {
            // Shift + 5 , desactivado.
            efectivo_entregado_guardado = t_pago_efectivo_entregado.getText();
            efectivo_usted_paga_guardado = t_pago_efectivo_usted_paga.getText();
            efectivo_total_guardado = t_pago_efectivo_total.getText();
        } else if (this.leyendoTarjeta) {
            // ACCION LECTURA CARACTER A CARACTER DEL CÓDIGO DE LA TARJETA DE CRÉDITO
            this.cadenaTarjeta = cadenaTarjeta + ke.getKeyChar();
        } else if (this.leyendoTarjeta && ke.getKeyChar() == '\n') {
            try {
                // ACCION FINALIZACIÓN DE LECTURA DE TARJETA DE CREDITO
                log.debug("Accion Finalización de Lectura de código de tarjeta de crédito");

                String cadenaTarjetaLeida = new String(this.cadenaTarjeta);
                cadenaTarjeta = "";
                if (t_pago_efectivo_entregado.hasFocus()) {
                    t_pago_efectivo_entregado.setEditable(true);
                }
                if (t_pago_efectivo_total.hasFocus()) {
                    t_pago_efectivo_total.setEditable(true);
                }
                if (t_pago_efectivo_usted_paga.hasFocus()) {
                    t_pago_efectivo_usted_paga.setEditable(true);
                }
                this.leyendoTarjeta = false;
                eventoLecturaTarjeta(new String(cadenaTarjetaLeida));
            } catch (Exception e) {
                log.error("keyTyped() - Error leyendo la banda de la tarjeta: " + e.getMessage(), e);
                ventana_padre.crearError("Error leyendo la banda de la tarjeta");
            }
        } else if (ke.getKeyChar() == '\n') {
            try {
                if (t_pago_efectivo_entregado.hasFocus()) {
                    if ((pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0 && (!ticket.getPagos().contieneEfectivo() || !l_medio_pago.getText().equals("EFECTIVO"))) || editando) {
                        log.info("Acción realizar pago simple mediante intro en entregado: " + t_pago_efectivo_entregado.getText());

                        if (new BigDecimal(t_pago_efectivo_entregado.getText()).compareTo(Sesion.getDatosConfiguracion().getMaximoPagos()) > 0) {
                            t_pago_efectivo_entregado.setText("");
                            throw new PagoInvalidException("Ha introducido un valor de pago superior al máximo permitido");
                        }
                        if (pagoSimple.requiereAutorizacionMontoMaximo(t_pago_efectivo_total.getText())) {
                            try {
                                String usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, "El valor del pago requiere autorización");
                                ticket.setAutorizadorVenta(usuarioAutorizador);
                            } catch (SinPermisosException ex) {
                                log.debug("No se tienen permisos para seleccionar medio pago con importe superior a monto máximo.");
                                return;
                            }
                        }

                        pagoSimple.establecerEntregado(t_pago_efectivo_entregado.getText());
                        pagoSimple.validar();
                        if (pagoSimple.tieneInformacionExtra() && !pagoSimple.isPagoOtros()) {
                            jInformacionExtraPago1.iniciaFoco();
                            JPrincipal.setPopupActivo(v_informacion_extra_pago);
                            v_informacion_extra_pago.setVisible(true);
                            JPrincipal.setPopupActivo(null);
                        }
                        if (!editando) {
                            ticket.crearNuevaLineaPago(pagoSimple);
                        }
                        ticket.getPagos().recalculaTotales();
                        tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                        setTamanosTabla();
                        refrescarTotales();
                        b_pago_efectivoActionPerformed(null);
                        editando = false;
                    } else {
                        if (ticket.getPagos().contieneEfectivo() && l_medio_pago.getText().equals("EFECTIVO")) {
                            log.debug("No se permite la edición de efectivo");
                            ventana_padre.crearAdvertencia("Se ha desactivado la edición automática de efectivo.");
                        } else {
                            log.debug("No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    }
                } else if (t_pago_efectivo_total.hasFocus()) {
                    pagoSimple.recalcularFromTotal(t_pago_efectivo_total.getText());
                    t_pago_efectivo_usted_paga.requestFocus();
                } else if ((!pagoSimple.getMedioPagoActivo().isAdmiteVuelto() && t_pago_efectivo_usted_paga.hasFocus())
                        || pagoSimple.getMedioPagoActivo().isNotaCredito()
                        || pagoSimple.getMedioPagoActivo().isBonoEfectivo()
                        || pagoSimple.getMedioPagoActivo().isGiftCard()) {
                    BigDecimal ustedPagaIntroducido = new BigDecimal(t_pago_efectivo_usted_paga.getText());
                    BigDecimal saldoMaximo = ustedPagaIntroducido;
                    if (pagoSimple.getMedioPagoActivo().isNotaCredito()) {
                        saldoMaximo = ((PagoNotaCredito) pagoSimple).getSaldoNotaCredito();
                    } else if (pagoSimple.getMedioPagoActivo().isBonoEfectivo()) {
                        saldoMaximo = ((PagoBono) pagoSimple).getSaldoBono();
                    } else if (pagoSimple.getMedioPagoActivo().isGiftCard()) {
                        saldoMaximo = ((PagoGiftCard) pagoSimple).getSaldoGiftCard();
                    }
                    if (Numero.isMenor(saldoMaximo, ustedPagaIntroducido)) {
                        t_pago_efectivo_usted_paga.setText(saldoMaximo.toString());
                    }
                    if (pagoSimple.getMedioPagoActivo().isNotaCredito()) {
                        pagoSimple.recalcularFromUstedPagaNotaCredito(t_pago_efectivo_usted_paga.getText(), ticket.getPagos());
                    } else {
                        pagoSimple.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                    }
                    if (pagoSimple.requiereAutorizacionMontoMaximo(t_pago_efectivo_total.getText())) {
                        try {
                            String usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, "El valor del pago requiere autorización");
                            ticket.setAutorizadorVenta(usuarioAutorizador);
                        } catch (SinPermisosException ex) {
                            log.debug("No se tienen permisos para seleccionar medio pago con importe superior a monto máximo.");
                            return;
                        }
                    }
                    pagoSimple.validar();
                    if (pagoSimple.tieneInformacionExtra() && !pagoSimple.isPagoOtros()) {
                        jInformacionExtraPago1.iniciaFoco();
                        JPrincipal.setPopupActivo(v_informacion_extra_pago);
                        v_informacion_extra_pago.setVisible(true);
                        JPrincipal.setPopupActivo(null);
                    }
                    if (!editando) {
                        ticket.crearNuevaLineaPago(pagoSimple);
                    }
                    ticket.getPagos().recalculaTotales();
                    tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                    setTamanosTabla();
                    refrescarTotales();
                    b_pago_efectivoActionPerformed(null);
                    editando = false;

                } else if (t_pago_efectivo_usted_paga.hasFocus()) {
                    pagoSimple.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                    t_pago_efectivo_entregado.requestFocus();
                }

            } catch (ImporteInvalidoException e) {
                ventana_padre.crearError(e.getMessage());
            } catch (PagoInvalidException e) {
                log.debug("el pago no es valido");
                ventana_padre.crearError(e.getMessage());
            } catch (PagoInvalidNCException e) {
                log.debug("el pago no es valido Nota Credito");
                accionBorrarPago();
                ventana_padre.crearError(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                ventana_padre.crearError("No se puede realizar el pago");
            }
            refrescarPagosEfectivos();
            ticket.getPagos().recalculaTotales();
            refrescarTablaPagos();
            refrescarTotales();
        }
    }

    private void eventoLecturaTarjeta(String tarjetaLeida) {
        MedioPagoBean medioPago = TarjetaCredito.getBINMedioPagoBanda(tarjetaLeida);
        if (medioPago != null && !medioPago.isGiftCard()) { // el BIN pertenece a una tarjeta registrada
            accionPagoEventoLecturaTarjeta(medioPago, tarjetaLeida);
        } else {
            try {
                boolean esGC = false;
                TramaTarjetaGiftCard tident;
                if (!isModoGiftCard()) {
                    //Si el medio de Pago ya está cargado y ha entrado aqui, es una trama de GiftCard de Sukasa
                    if (medioPago != null) {
                        tident = new TramaTarjetaGiftCard(tarjetaLeida, medioPago.getCodMedioPago());
                    } else {
                        tident = new TramaTarjetaGiftCard(tarjetaLeida);
                    }
                    esGC = eventoLecturaTarjetaGiftCard(tident);
                }
                if (!esGC) {
                    log.warn("eventoLecturaTarjeta() - No se reconoce el código de la tarjeta : BIN " + tarjetaLeida);
                    ventana_padre.crearAdvertencia("No se reconoce el código de la tarjeta: BIN: " + tarjetaLeida);
                }
            } catch (ParserTramaException ex) {
                log.debug("eventoLecturaTarjeta() - Error leyendo la banda de la tarjeta: " + ex.getMessage(), ex);
                ventana_padre.crearError("Error leyendo la banda de la tarjeta");
            }
        }
    }

    private void accionPagoEventoLecturaTarjeta(MedioPagoBean medioPago, String tramaTarjeta) {
        if (!isModoGiftCard() || (isModoGiftCard() && medioPago.isPermitePagarGiftCard())) {
            pagoSimple.setPagoActivo(Pago.PAGO_TARJETA);
            String usuarioAutorizador = null;
            if (medioPago.isRequiereAutorizacion()) {
                try {
                    usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
                    ticket.setAutorizadorVenta(usuarioAutorizador);
                } catch (SinPermisosException ex) {
                    log.debug("No se tienen permisos para seleccionar medio pago.");
                    return;
                }
            }
            if (permiteMedioPago(medioPago)) {
                pagoTarjeta(medioPago, usuarioAutorizador, tramaTarjeta);
            } else {
                ventana_padre.crearAdvertencia("El medio de pago no esta disponible para este tipo venta");
            }
        } else {
            ventana_padre.crearAdvertencia("El medio de pago no esta disponible para este tipo venta");
        }
    }

    @SuppressWarnings({"deprecation", "deprecation"})
    private void pagoTarjeta(MedioPagoBean medioPago, String autorizador, String banda) {

        try {
            TarjetaCredito tc = null;
            if (banda == null || banda.isEmpty()) {
                if (medioPago.isRequiereAutorizacionLecturaManual()) {
                    String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, JSolicitarAutorizacionVentana.MENSAJE_AUTORIZACION_LECTURA_MANUAL);
                    ServicioLogAcceso.crearAccesoLogAutorizarPago(compruebaAutorizacion, medioPago.getCodMedioPago());
                    ticket.setAutorizadorVenta(compruebaAutorizacion);
                }
                if (medioPago.isTarjetaSukasa()) {
                    tc = ventana_padre.crearVentanaPagoManualTarjetaCredito(medioPago, null);

                    if (tc == null) {
                        medioPago = tc.getBINMedioPago();
                        return;
                    }
                } else {
                    tc = TarjetaCreditoBuilder.create(medioPago, TarjetaCredito.TARJETA_DEFECTO, false);
                }
            } else {
                log.debug("- (Inicializar) - Inicializando la tarjeta a partir de su banda");
                tc = TarjetaCreditoBuilder.create(medioPago, banda);
            }
            if (medioPago.isCreditoFilial() && tc != null) {
                pagoOtrosBonoSuperMaxiNavidad(medioPago, autorizador, tc);
                return;
            } else if (medioPago.isTarjetaSukasa()) {
                try {
                    tc.validaTarjetaCredito();
                } catch (TarjetaInvalidaException e) {
                    if (e.permiteAutorizacion) {
                        String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.ADMITIR_LISTA_NEGRA, JSolicitarAutorizacionVentana.MENSAJE_AUTORIZACION_TARJETA);
                        ServicioLogAcceso.crearAccesoLogPermitirTarjetaListaNegra(compruebaAutorizacion, tc.getNumeroOculto());
                        ticket.setAutorizadorVenta(compruebaAutorizacion);
                    } else {
                        ventana_padre.crearSinPermisos(e.getMessage());
                        return; // si no permite autorizar la tarjeta ni con clave administrador, no puede seguir con este medio de pago.
                    }
                }
            }
            pagoPlanes(medioPago, autorizador, tc);
        } catch (SinPermisosException ex) {
            log.debug("pagoTarjeta() - No se pudo autorizar la tarjeta con password de administrador. ");
        } catch (PromocionFormaPagoException ex) {
            log.error("Excepcion de Promociones", ex);
            ventana_padre.crearInformacion("Se debe colocar solo productos de la promoción Lucky Week o productos que no esten en esta promoción..");
        } catch (Exception ex) {
            log.error("Excepcion no controlada", ex);
            ventana_padre.crearError("No se ha podido leer la banda magnética de la tarjeta. Probablemente tenga algún formato no soportado por el sistema.");
        }
    }

    private void pagoPlanes(MedioPagoBean medioPago, String autorizador, TarjetaCredito tc) throws PromocionFormaPagoException {
        try {
            ticket.setAutorizadorVenta(autorizador);
            try {
                jPagosTarjeta.inicializar(medioPago, ticket, autorizador, tc, modoPago);
            } catch (PromocionArticuloException ex) {
                java.util.logging.Logger.getLogger(JReservacionesPagosV.class.getName()).log(Level.SEVERE, null, ex);
            }
            jPagosTarjeta.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
            jPagosTarjeta.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            log.debug("Mostrando ventana de planes de tarjeta...");
            log.debug("Mostrando ventana de planes de tarjeta... 2");
            
            log.info("Listado de los planes " + tb_pagos.toString());
            JPrincipal.setPopupActivo(jPagosTarjeta);
            jPagosTarjeta.setVisible(true);
            JPrincipal.setPopupActivo(null);
            tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
            setTamanosTabla();
            refrescarTotales();
            b_pago_efectivoActionPerformed(null);
        } catch (ValidationException e) {
            ventana_padre.crearError(e.getMessage());
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void removeHotKey(KeyStroke keyStroke, String inputActionKey) {
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.remove(keyStroke);
        actionMap.remove(inputActionKey);
    }

    private void addFunctionKeys() {
        // ENTER EN LOS BOTONES
        addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterBotones", listenerEnter);

        // Tabla
        crearAccionFocoTabla(this, tb_pagos, KeyEvent.VK_T, InputEvent.CTRL_MASK);

        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        Action listenerf5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_pago_efectivoActionPerformed(ae);
            }
        };
        addHotKey(f5, "IdentClientF5", listenerf5);

        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        Action listenerf6 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_pago_contadoActionPerformed(ae);
            }
        };
        addHotKey(f6, "IdentClientF6", listenerf6);

        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        Action listenerf7 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_pago_tarjetaActionPerformed(ae);

            }
        };
        addHotKey(f7, "IdentClientF7", listenerf7);

        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        Action listenerf8 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_pago_otrosActionPerformed(ae);

            }
        };
        addHotKey(f8, "IdentClientF8", listenerf8);

        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        Action listenerf9 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_m_procesar_pagoActionPerformed(ae);

            }
        };
        addHotKey(f9, "IdentClientF9", listenerf9);

        KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
        Action listenerf10 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionCambiaPaginaPagoTarjeta();
            }
        };
        addHotKey(f10, "IdentClientF10", listenerf10);

        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_m_volverActionPerformed(ae);

            }
        };
        addHotKey(f2, "IdentClientF2", listenerf2);

        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        Action listenerf3 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {

                b_m_borrarpagoActionPerformed(ae);
            }
        };
        addHotKey(f3, "IdentClientF3", listenerf3);

        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {

                b_m_editarpagoActionPerformed(ae);
            }
        };
        addHotKey(f4, "IdentClientF4", listenerf4);

        //Funciones Seleccionar lineas de pago.
        KeyStroke alt1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK);
        Action listeneralt1 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(0, 0);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };

        addHotKey(alt1, "IdentClientAlt1", listeneralt1);

        KeyStroke alt2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK);
        Action listeneralt2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(1, 1);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt2, "IdentClientAlt2", listeneralt2);

        KeyStroke alt3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK);
        Action listeneralt3 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {

                try {
                    tb_pagos.setRowSelectionInterval(2, 2);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt3, "IdentClientAlt3", listeneralt3);

        KeyStroke alt4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK);
        Action listeneralt4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(3, 3);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt4, "IdentClientAlt4", listeneralt4);

        KeyStroke alt5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK);
        Action listeneralt5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(4, 4);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt5, "IdentClientAlt5", listeneralt5);

        KeyStroke alt6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK);
        Action listeneralt6 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {

                    tb_pagos.setRowSelectionInterval(5, 5);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt6, "IdentClientAlt6", listeneralt6);

        KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK);
        Action listeneralt7 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(6, 6);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt7, "IdentClientAlt7", listeneralt7);

        KeyStroke alt8 = KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK);
        Action listeneralt8 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(8, 8);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt8, "IdentClientAlt8", listeneralt8);

        KeyStroke alt9 = KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK);
        Action listeneralt9 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    tb_pagos.setRowSelectionInterval(9, 9);
                } catch (IllegalArgumentException ex) {
                    log.info("No existe la línea a seleccionar");
                }
            }
        };
        addHotKey(alt9, "IdentClientAlt9", listeneralt9);

        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_m_borrarpagoActionPerformed(ae);
            }
        };
        addHotKey(ctrlMenos, "PagosCtrlMenos", listenerCtrlMenos);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (estado == JReservacionesPagosV.REALIZAR_PAGOS) {
                    log.debug(" Ingreso por la opcion de teclado ESC ");
                    accionCancelarPagos();
                    habilitaElementos(true);
                    estado = JReservacionesPagosV.ESTABLECER_MEDIOS;
                }
            }
        };
        addHotKey(esc, "IdentClientesc", listeneresc);

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        Action listenerCtrlO = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                if (isModoVenta()) {
                    Sesion.getTicket().setObservaciones(ventana_padre.crearVentanaObservaciones(Sesion.getTicket().getObservaciones()));
                }
                if (isModoAbonoReservacion() || isModoArticulosReservacion()) {
                    reservacion.setObservaciones(ventana_padre.crearVentanaObservaciones(reservacion.getObservaciones()));
                }
                if (isModoAbonoPlan() || isModoArticulosPlan()) {
                    planNovio.setObservaciones(ventana_padre.crearVentanaObservaciones(planNovio.getObservaciones()));
                }
                if (isModoGiftCard()) {
                    giftCard.setObservaciones(ventana_padre.crearVentanaObservaciones(giftCard.getObservaciones()));
                }
                if (isModoLetraCambio()) {
                    letraCambioCuota.setObservaciones(ventana_padre.crearVentanaObservaciones(letraCambioCuota.getObservaciones()));
                }
            }
        };
        addHotKey(ctrlO, "ObservacionesCtrlO", listenerCtrlO);

        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
        Action listenerCtrlP = new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                postfecharVoucher();
            }
        };
        addHotKey(ctrlP, "PermisosCtrlP", listenerCtrlP);
    }

    private void removeFunctionKeys() {
        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        removeHotKey(f2, "IdentClientF2");
        removeHotKey(f4, "IdentClientF4");
        removeHotKey(f5, "IdentClientF5");
        removeHotKey(f6, "IdentClientF6");
        removeHotKey(f7, "IdentClientF7");
        removeHotKey(f8, "IdentClientF8");
        removeHotKey(f9, "IdentClientF9");
        removeHotKey(f12, "IdentClientF12");
        removeHotKey(esc, "IdentClientesc");
        removeHotKey(ctrlMenos, "PagosCtrlMenos");
        removeHotKey(ctrlO, "ObservacionesCtrlO");
    }

    @Override
    public void focusGained(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof JTextFieldForm) {
            JTextFieldForm txt = (JTextFieldForm) cmpnt;
            txt.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof JTextFieldForm) {
            JTextFieldForm txt = (JTextFieldForm) cmpnt;
            try {
                if (txt.getName().equals("total")) {
                    if (txt.getText().equals("%")) {
                        txt.setText(efectivo_total_guardado);
                    }
                    pagoSimple.recalcularFromTotal(txt.getText());
                } else if (txt.getName().equals("aCancelar")) { // ustedPaga
                    if (t_pago_efectivo_usted_paga.getText().contains("%")) {
                        t_pago_efectivo_usted_paga.setText(efectivo_usted_paga_guardado);
                    }
//                    pagoSimple.recalcularFromUstedPagaSupermaxi(t_pago_efectivo_usted_paga.getText(), ticket.getPagos());
                    pagoSimple.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                } else if (txt.getName().equals("entregado")) {
                    if (t_pago_efectivo_entregado.getText().equals("%")) {
                        t_pago_efectivo_entregado.setText(efectivo_entregado_guardado);
                    }
                    pagoSimple.establecerEntregado(t_pago_efectivo_entregado.getText());
                }
            } catch (PagoInvalidException e) {
                if (!txt.getName().equals("entregado")) {
                    ventana_padre.crearError(e.getMessage());
                }
                log.debug("El pago no es valido total:" + txt.getText() + " aCancelar:" + t_pago_efectivo_usted_paga.getText() + " entregado:" + t_pago_efectivo_entregado.getText());
            }
//            catch (PagoInvalidSupermaxiException ex) {
//                log.debug("El pago no es valido total:" + txt.getText() + " aCancelar:" + t_pago_efectivo_usted_paga.getText() + " entregado:" + t_pago_efectivo_entregado.getText());
//            }
            refrescarPagosEfectivos();
        }
    }

    private void setTamanosTabla() {
        tb_pagos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb_pagos.getColumnModel().getColumn(0).setPreferredWidth(15);
        tb_pagos.getColumnModel().getColumn(1).setPreferredWidth(160);
        tb_pagos.getColumnModel().getColumn(2).setPreferredWidth(65);
        tb_pagos.getColumnModel().getColumn(3).setPreferredWidth(60);
        tb_pagos.getColumnModel().getColumn(4).setPreferredWidth(70);
        tb_pagos.getColumnModel().getColumn(5).setPreferredWidth(75);
        tb_pagos.getColumnModel().getColumn(6).setPreferredWidth(100);
        tb_pagos.getColumnModel().getColumn(7).setPreferredWidth(90);
        tb_pagos.getColumnModel().getColumn(8).setPreferredWidth(90);
        tb_pagos.getColumnModel().getColumn(9).setPreferredWidth(12);
        tb_pagos.getColumnModel().getColumn(10).setPreferredWidth(44);
    }

    private boolean permiteMedioPago(MedioPagoBean mp) {
        if (isModoAbono() && !mp.isAdmiteAbonoReservacion()) {
            return false;
        }
        if (isModoGiftCard() && !mp.isPermitePagarGiftCard()) {
            return false;
        }
        if (isModoCreditoDirecto() && !mp.isAdmiteAbonoTarjetaPropia()) {
            return false;
        }
        if (isModoLetraCambio() && !mp.isAdmitePagoCreditoTemporal()) {
            return false;
        }
        return true;
    }

    /**
     * Acciones comunes e la inicialización del panel de pagos a todos los
     * constructores
     */
    private void iniciaPanel() {
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_pagos.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
            log.error("Error al cargar la imagen en JReservacionesPagosV");
        }
        initComponents();
        registrarEventoLeerTarjeta();

        //Imagenes.cambiarImagenPublicidad(p_publicidad);
        URL myurl;

        // Abono mínimo
        if (this.pagoMinimo != null && pagoMinimo.compareTo(BigDecimal.ZERO) > 0) {
            t_abono_minimo.setText(pagoMinimo.toString());
        } else {
            t_abono_minimo.setVisible(false);
            lb_abono_minimo.setVisible(false);
        }

        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        v_informacion_extra_pago.setIconImage(icon.getImage());

        // Mod : Añadimos pagos con nota de créditos
        v_notaCredito.setIconImage(icon.getImage());
        jPagosNotaCredito1.setContenedor(v_notaCredito);
        jPagosNotaCredito1.setDesdeReservaciones(true);
        if (clienteProceso != null) {
            jPagosNotaCredito1.setCliente(clienteProceso);
        } else {
            if (clienteGC == null) {
                jPagosNotaCredito1.setCliente(ticket.getCliente());
            } else {
                jPagosNotaCredito1.setCliente(clienteGC);
            }
        }

        v_notaCredito.setLocationRelativeTo(null);
        p_pago_contado.setVisible(false);
        p_pago_efectivo.setVisible(true);
        p_pago_otros.setVisible(false);
        p_pago_tarjeta.setVisible(false);
        p_pago_giftcard.setVisible(false);
        panel_pagos_activo = p_pago_efectivo;

        // inicializamos las acciones de teclado para los cuadros de texto
        t_pago_efectivo_entregado.addKeyListener(this);
        t_pago_efectivo_total.addKeyListener(this);
        t_pago_efectivo_usted_paga.addKeyListener(this);

        // incializar ventana de pagos con tarjeta
        jPagosTarjeta = new JReservacionesPagosTarjetas(this);
        jPagosTarjeta.setLocationRelativeTo(null);
        jPagosTarjeta.setAlwaysOnTop(true);
        jPagosTarjeta.setResizable(true);
        jPagosTarjeta.setIconImage(icon.getImage());
        jPagosTarjeta.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jPagosTarjeta.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        // Retenciones
        v_retencion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_retencion.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_retencion.setResizable(true);
        v_retencion.setIconImage(icon.getImage());
        v_retencion.setLocationRelativeTo(null);
        p_retencion.setVentana_padre(ventana_padre);
        p_retencion.setContenedor(v_retencion);

        // Lectura de Tarjeta
        v_lectura_tarjeta.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_lectura_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_lectura_tarjeta.setResizable(true);
        v_lectura_tarjeta.setIconImage(icon.getImage());
        v_lectura_tarjeta.setLocationRelativeTo(null);
        p_lectura_tarjeta.setVentana_padre(ventana_padre);
        p_lectura_tarjeta.setContenedor(v_lectura_tarjeta);

        // Bonos
        v_bonos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_bonos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_bonos.setResizable(false);
        v_bonos.setLocationRelativeTo(null);
        v_bonos.setIconImage(icon.getImage());
        p_bonos.setVentana_padre(ventana_padre);
        p_bonos.setContenedor(v_bonos);

        //Transparencia y ausencia de bordes en la tabla
        Border empty = new EmptyBorder(0, 0, 0, 0);
        js_tb_pagos.setViewportBorder(empty);
        tb_pagos.setBorder(empty);
        js_tb_pagos.getViewport().setOpaque(false);

        addFunctionKeys();

        log.debug("Obtenemos medios de pago para el tipo de cliente: " + tipoCliente);

        // Generar botones mediosPago
        pintaBotonesMedioPago(1);

        for (final MedioPagoBean mp : MediosPago.getInstancia().getListaMediosPago(tipoCliente, MedioPagoBean.TIPO_CONTADO)) {
            if (!permiteMedioPago(mp)) {
                continue;
            }
            Action ac = new AbstractAction(mp.getCodMedioPago()) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    pagoSimple.setPagoActivo(Pago.PAGO_CONTADO);
                    String codPagoActivo = (String) this.getValue(Action.NAME);
                    MedioPagoBean medioPago = (mp.equals(codPagoActivo)) ? mp : null;

                    if (medioPago.isRequiereAutorizacion()) {
                        try {
                            String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
                            pagoContado(medioPago, autorizador);
                        } catch (SinPermisosException e) {
                            log.debug("No se tienen permisos para seleccionar medio pago.");
                        }
                    } else {
                        pagoContado(medioPago, null);
                    }
                }

                @SuppressWarnings("deprecation")
                private void pagoContado(MedioPagoBean medioPago, String autorizador) {
                    ticket.setAutorizadorVenta(autorizador);
                    pagoSimple.setMedioPagoActivo(medioPago);
                    pagoSimple.setAutorizador(autorizador);
                    cambiaPanelPago(p_pago_efectivo);
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);

                    if (!pagoSimple.getMedioPagoActivo().isAdmiteVuelto()) {
                        t_pago_efectivo_entregado.setEnabled(false);
                        l_pagos_efectivo_entregado.setForeground(new Color(51, 153, 255));
                        t_pago_efectivo_usted_paga.setNextFocusableComponent(b_aceptar);
                        t_pago_efectivo_usted_paga.requestFocus();
                    } else {
                        b_aceptar.setNextFocusableComponent(b_pago_efectivo);
                    }
                    if (pagoSimple.tieneInformacionExtra() && !pagoSimple.isPagoOtros()) {
                        jInformacionExtraPago1.eliminarCampos();
                        jInformacionExtraPago1.setContenedor(v_informacion_extra_pago);
                        jInformacionExtraPago1.modificaContenido(JReservacionesPagosV.this, pagoSimple);
                    }
                    l_medio_pago.setText(medioPago.getDesMedioPago());
                    pagoSimple.resetear(pagos.getSaldo());
                    refrescarPagosEfectivos();
                }
            };
            addButtonMedioPago(p_pago_contado, ac, mp);
        }

        for (final MedioPagoBean mp : MediosPago.getInstancia().getListaMediosPago(tipoCliente, MedioPagoBean.TIPO_OTROS)) {
            if (!permiteMedioPago(mp)) {
                continue;
            }
            Action ac = new AbstractAction(mp.getCodMedioPago()) {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        pagoSimple.setPagoActivo(Pago.PAGO_OTROS);
                        String codPagoActivo = (String) this.getValue(Action.NAME);
                        MedioPagoBean medioPago = (mp.equals(codPagoActivo)) ? mp : null;
                        if (medioPago.isRequiereAutorizacion()) {
                            try {
                                String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
                                pagoOtros(medioPago, autorizador);
                            } catch (SinPermisosException e) {
                                log.debug("No se tienen permisos para seleccionar medio pago.");
                            }
                        } else {
                            pagoOtros(medioPago, null);
                        }
                    } catch (Exception e) {
                        log.error("Error en la preparación de interfaz para el pago", e);
                    }
                }

                @SuppressWarnings("deprecation")
                private void pagoOtros(MedioPagoBean medioPago, String autorizador) throws Exception {
                    ticket.setAutorizadorVenta(autorizador);
                    if (medioPago.isRetencion()) {
                        pagoOtrosRetencion(autorizador);
                    } else if (medioPago.isNotaCredito()) {
                        if (isHayPagosPorHacer()) {
                            pagoOtrosNotaCredito(medioPago, autorizador);
                        } else {
                            log.debug("pagoOtrosNotaCredito() - No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    } else if (medioPago.isBonoEfectivo()) {
                        if (isHayPagosPorHacer()) {
                            pagoOtrosBonoEfectivo(medioPago, autorizador);
                        } else {
                            log.debug("pagoOtrosBonoEfectivo() - No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    } else if (medioPago.isGiftCard()) {
                        if (isHayPagosPorHacer()) {
                            pagoOtrosGiftCard(medioPago, autorizador);
                        } else {
                            log.debug("pagoOtrosGiftCard() - No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    } else if (medioPago.isCreditoTemporal()) {
                        if (pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                            pagoPlanes(medioPago, autorizador, null);
                        } else {
                            log.debug("No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    } else if (medioPago.isBonoSuperMaxiNavidad()) {
                        if (isHayPagosPorHacer()) {
                            pagoOtrosBonoSuperMaxiNavidad(medioPago, autorizador, null);
                        } else {
                            log.debug("pagoOtrosBonoSuperMaxiNavidad() - No se permiten pagos con saldo 0.");
                            ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                        }
                    }
                }
            };
            addButtonMedioPago(p_pago_otros, ac, mp);
        }
        tb_pagos.setDefaultRenderer(Object.class, new PagosTableCellRenderer());
        //Añadido para cuando pierde o gana el foco
        t_pago_efectivo_usted_paga.addFocusListener(this);
        t_pago_efectivo_entregado.addFocusListener(this);
        t_pago_efectivo_total.addFocusListener(this);

        listaPagos = new ArrayList<MedioPagoBean>();
        for (MedioPagoBean medPag : MediosPago.getInstancia().getListaMediosPago(tipoCliente, MedioPagoBean.TIPO_TARJETAS)) {
            if (permiteMedioPago(medPag)) {
                listaPagos.add(medPag);
            }
        }

        // ocultamos textos innecesarios
        deshabilitar_elementos();
    }

    private void pagoOtrosBonoSuperMaxiNavidad(MedioPagoBean medioPago, String autorizador, TarjetaCredito tc) {
        try {
//            TarjetaCredito tc = null;
            tc = ventana_padre.crearVentanaPagoManualTarjetaCredito(medioPago, tc);
            if (tc == null) {
                return;
            }
            tc.validaTarjetaCredito();
            pagoPlanes(medioPago, autorizador, tc);

            /*PagoBonoSuperMaxiNavidad pago = new PagoBonoSuperMaxiNavidad(ticket.getPagos(),ticket.getCliente(),autorizador);
            pago.setPlanSeleccionado(jPagosTarjeta.getPagoCredito().getPlanSeleccionado());
            pago.setTarjetaCredito(tc);*/
        } catch (Exception e) {
            log.error("pagoOtrosBonoSuperMaxiNavidad() - Ha ocurrido un error realizando el pago mediante bono SuperMaxi/Navideño: " + e.getMessage(), e);
            ventana_padre.crearError("No se ha podido realizar el pago mediante bono SuperMaxi/Navideño");
        }
    }

    private void pagoOtrosGiftCard(MedioPagoBean medioPago, String autorizador) {
        try {
            String autorizadorLectura = ventana_padre.compruebaAutorizacion(Operaciones.LECTURA_GIFTCARD, "<html>La tarjeta requiere autorización<br> para su lectura manual</html>");
            String numeroGiftCard = JPrincipal.getInstance().crearVentanaLecturaManualGiftCard();
            if (numeroGiftCard != null && !numeroGiftCard.equals("")) {
                try {
                    MedioPagoBean medPag = TarjetaCredito.getBINMedioPagoBanda(numeroGiftCard);
                    TramaTarjetaGiftCard tident;
                    if (medPag != null && medPag.isGiftCard()) {
                        tident = new TramaTarjetaGiftCard(numeroGiftCard, medPag.getCodMedioPago());
                    } else {
                        tident = new TramaTarjetaGiftCard(numeroGiftCard);
                    }
                    eventoLecturaTarjetaGiftCard(tident);
                } catch (ParserTramaException e) {
                    log.debug("pagoOtrosGiftCard() - Error parseando la trama de la tarjeta");
                    ventana_padre.crearError("El número de tarjeta no es válido.");
                }
            }
        } catch (SinPermisosException ex) {
            ventana_padre.crearError("No tiene permisos para lectura manual de giftcard.");
        }
    }

    @SuppressWarnings("deprecation")
    private void pagoOtrosBonoEfectivo(MedioPagoBean medioPago, String autorizador) {
        p_bonos.resetearDatos();
        v_bonos.setVisible(true);
        Bono bonoL = p_bonos.getBono();
        //cambio para bonos Rd Cliente unico
        if (!ticket.getCliente().getCodcli().equals(bonoL.getCodCliente())) {
            JReservacionesPagosV.this.ventana_padre.crearError("El cupón no pertenece a este cliente");
//            JOptionPane.showMessageDialog(p_retencion,"El cupón no pertenece a este cliente");
        } else {
            if (bonoL != null) {
                if (!ticket.getPagos().contieneBono(bonoL.getBonoPK().getCodalm(), bonoL.getBonoPK().getIdBono())) {

                    PagoBono pago = new PagoBono(ticket.getPagos(), ticket.getCliente());
                    pago.setModalidad(modoPago);
                    pago.crearPagoBono(medioPago, bonoL);
                    pago.setSaldoInicial(pagos.getSaldo());
                    pagoSimple = pago;
                    pagoSimple.setModalidad(modoPago);
                    cambiaPanelPago(p_pago_efectivo);

                    t_pago_efectivo_usted_paga.requestFocus();
                    b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);

                    t_pago_efectivo_entregado.setEnabled(false);
                    l_pagos_efectivo_entregado.setForeground(new Color(51, 153, 255));
                    t_pago_efectivo_usted_paga.setNextFocusableComponent(b_aceptar);
                    t_pago_efectivo_usted_paga.requestFocus();
                    t_pago_efectivo_entregado.setText(pago.getTotalBono().toString());
                    pagoSimple.resetear(pagos.getSaldo());
                    refrescarPagosEfectivos();
                    l_medio_pago.setText("BONO EFECTIVO");
                    pago.getBono().setSaldoUsado(pagoSimple.getUstedPaga());
                } else {
                    JReservacionesPagosV.this.ventana_padre.crearError("El bono ya ha sido utilizado en el pago actual");
                }
            }
        }
    }

    private void postfecharVoucher() {
        try {
            List<PagoCreditoSK> pagosCreditoDirecto = ticket.getPagos().getMediosPagoCreditoDirecto();
            if (pagosCreditoDirecto.isEmpty()) {
                ventana_padre.crearAdvertencia("Debe utilizar como medio de pago un crédito directo.");
                return;
            }
            PagoCreditoSK pagoCreditoSK = null;
            for (PagoCreditoSK pago : pagosCreditoDirecto) {
                if (!pago.isPosfechado()) {
                    pagoCreditoSK = pago;
                    break;
                }
            }
            if (pagoCreditoSK == null) {
                ventana_padre.crearAdvertencia("Ya ha posfechado todos los pagos de crédito directo.");
                return;
            }
            if (!TiposClientes.getInstancia().getTipoCliente(tipoCliente).getPostFecharVoucher()) {
                ventana_padre.crearAdvertencia("No puede posfechar consumos a este tipo de cliente.");
                return;
            }
            if (!ventana_padre.crearVentanaConfirmacion("¿Desea posfechar el voucher de " + pagoCreditoSK.getMedioPagoActivo().getDesMedioPago() + " por el valor de $ " + pagoCreditoSK.getUstedPagaConIntereses())) {
                return;
            }
            String autorizadorPosfechar = ventana_padre.compruebaAutorizacion(Operaciones.PERMITE_POSTFECHAR_VOUCHER);
            Integer meses = JPrincipal.crearVentanaIngresarValorEntero("<html><p>Introduzca el número de meses que desea posfechar el voucher</p></html>", Variables.getVariableAsBigDecimal(Variables.POS_CONFIG_MESES_POSFECHAR_VOUCHER));
            if (meses == null || meses == 0) {
                return;
            }
            pagoCreditoSK.setMesesPosfechado(meses);
            if (pagoCreditoSK.isMesesGraciaAplicado()) {
                Sesion.promocionMesesGracia.aplicaPosfechadoVoucher(pagoCreditoSK);
            } else {
                pagoCreditoSK.setTextoPosfechado("Recibe " + meses + " mes(es) de gracia por la compra de $" + pagoCreditoSK.getUstedPagaConIntereses() + " en " + pagoCreditoSK.getMedioPagoActivo().getDesMedioPago());
            }
            ticket.setAutorizadorVenta(autorizadorPosfechar);
            refrescarTablaPagos();
        } catch (SinPermisosException ex) {
            ventana_padre.crearError("No tiene permisos para posfechar voucher de crédito directo.");
        } catch (Exception e) {
            log.error("postfecharVoucher() - Error posfechando voucher: " + e.getMessage(), e);
            ventana_padre.crearError("Se ha producido un error inesperado al intentar posfechar el voucher.");
        }
    }

    @SuppressWarnings("deprecation")
    private void pagoOtrosNotaCredito(MedioPagoBean medioPago, String autorizador) {
        jPagosNotaCredito1.resetearDatos();

        jPagosNotaCredito1.setPagosReservaciones(ticket.getPagos());
        log.debug("pagoOtrosNotaCredito() - Apertura de ventana de Pago de nota de crédito");
        JPrincipal.setPopupActivo(v_notaCredito);
        v_notaCredito.setVisible(true);
        JPrincipal.setPopupActivo(null);
        log.debug("pagoOtrosNotaCredito() - Cierre de ventana de Pago de nota de crédito");
        NotasCredito notasCredito = jPagosNotaCredito1.getNotasCredito();
        if (notasCredito.getCodcli().equals(notasCredito.getCliProceso()) || notasCredito.getFactDocumento().equals(notasCredito.getCliProceso())) {
            pagoNCredito(notasCredito, medioPago);
            notasCredito.setCliProceso(null);
        }
    }

    private void pagoNCredito(NotasCredito notasCredito, MedioPagoBean medioPago) {

        if (notasCredito != null && notasCredito.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            PagoNotaCredito pago = new PagoNotaCredito(ticket.getPagos(), ticket.getCliente(), ticket.getTotales());
            pago.setModalidad(modoPago);
            pago.crearPagoNotaCredito(medioPago, notasCredito);
            pago.setSaldoInicial(pagos.getSaldo());
            pagoSimple = pago;
            cambiaPanelPago(p_pago_efectivo);
            t_pago_efectivo_usted_paga.requestFocus();
            b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);
            t_pago_efectivo_entregado.setEnabled(false);
            l_pagos_efectivo_entregado.setForeground(new Color(51, 153, 255));
            t_pago_efectivo_usted_paga.setNextFocusableComponent(b_aceptar);
            t_pago_efectivo_usted_paga.requestFocus();
            t_pago_efectivo_entregado.setText(pago.getSaldoNotaCredito().toString());
            pagoSimple.resetear(pagos.getSaldo());
            refrescarPagosEfectivos();
            l_medio_pago.setText("NOTA DE CRÉDITO");
            keyTypedPago();
        }
    }

    private void pagoOtrosRetencion(String autorizador) throws PagoInvalidException {
        if (!ticket.getPagos().isSaldoCero()) {
            JPrincipal.getInstance().crearAdvertencia("Antes de incluir la retención a la fuente debe abonar el saldo pendiente.");
            return;
        }
        for (Pago pago : ticket.getPagos().getPagos()) {
            if (pago instanceof PagoRetencionFuente) {
                JPrincipal.getInstance().crearAdvertencia("Solo se puede ingresar una retenci\u00F3n");
                return;
            }
        }

        if (!editando) {
            try {
                BigDecimal base = null;
                BigDecimal iva = null;
                if (isModoLetraCambio()) {
                    if (letraCambio.isRetencionAplicada()) {
                        ventana_padre.crearError("Ya se pagó una letra de la misma factura con retención a la fuente. No puede pagar de nuevo con retención a la fuente.");
                        return;
                    }
                    base = letraCambio.getTicketOrigen().getTotales().getBase();
                    iva = letraCambio.getTicketOrigen().getTotales().getImpuestos();

                }
                BigDecimal valorRetencion = null;
                if (ticket.getPagos().getPagoEfectivo() != null) {
                    valorRetencion = JPrincipal.crearVentanaIngresarValorDecimal("Indique el valor de retención:", base != null ? base : ticket.getPagos().getPagoEfectivo().getTotal());
                } else {
                    valorRetencion = JPrincipal.crearVentanaIngresarValorDecimal("Indique el valor de retención:", base != null ? base : ticket.getTotales().getBase());
                }
                if (valorRetencion == null) {
                    return;
                }
                if (valorRetencion.compareTo(BigDecimal.ZERO) < 0) {
                    JPrincipal.getInstance().crearAdvertencia("La retención debe ser positiva.");
                    return;
                }
                log.debug("pagoOtrosRetencion() - Pago con retencion " + valorRetencion);
                PagoRetencionFuente pago = ticket.getPagos().crearPagoRetencionFuente(base, iva, valorRetencion);
                pago.setAutorizador(autorizador);
                ticket.crearNuevaLineaPago(pago);
            } catch (ImporteInvalidoException e) {
                ventana_padre.crearError(e.getMessage());
            }
        }
        ticket.getPagos().recalculaTotales();
        tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
        setTamanosTabla();
        refrescarTotales();
        b_pago_efectivoActionPerformed(null);
        editando = false;
    }

    public JDialog getContenedor() {
        return contenedor;
    }

    public void setContenedor(JDialog contenedor) {
        this.contenedor = contenedor;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public BigDecimal getPagoRealizado() {
        return pagoRealizado;
    }

    public void setPagoRealizado(BigDecimal pagoRealizado) {
        this.pagoRealizado = pagoRealizado;
    }

    @Override
    public void iniciaFoco() {
        b_pago_efectivoActionPerformed(null);
    }
    // EVENTO DE LEER TARJETA
    public Action listenerTarjeta = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
            if (pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                if (Sesion.getDatosConfiguracion().isModoDesarrollo()) {
                    // Hacemos aparecer el input de introducción de bandas de la tarjeta
                    //v_lectura_tarjeta.setSize(400, 400);
                    v_lectura_tarjeta.setMinimumSize(new Dimension(550, 330));
                    p_lectura_tarjeta.setMinimumSize(new Dimension(520, 310));
                }
                p_lectura_tarjeta.iniciaVista();
                v_lectura_tarjeta.setAlwaysOnTop(true);
                v_lectura_tarjeta.setVisible(true);
                if (p_lectura_tarjeta.getBine() != null && !p_lectura_tarjeta.getBine().isEmpty()) {

                    eventoLecturaTarjeta(p_lectura_tarjeta.getBine());
                }
            } else {
                log.debug("No se permiten pagos con saldo 0.");
                ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
            }
        }
    };

    public void registrarEventoLeerTarjeta() {
        KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.SHIFT_DOWN_MASK); //% es SHIFT + 5
        addHotKey(keyEnter, "LecturaTarjeta", listenerTarjeta);
    }

    private void pintaBotonesMedioPago(int pagina) {
        int TAMANO_PAGINA = 18;
        int desde = 0;
        int hasta = 0;
        boolean pintaSiguiente = false;
        boolean pintaAnterior = false;

        List<MedioPagoBean> listaPagos = new ArrayList<MedioPagoBean>();
        for (MedioPagoBean medPag : MediosPago.getInstancia().getListaMediosPago(tipoCliente, MedioPagoBean.TIPO_TARJETAS)) {
            if (permiteMedioPago(medPag)) {
                listaPagos.add(medPag);
            }
        }

        if (listaPagos.size() <= TAMANO_PAGINA) {
            // solo hay 1 página posible
            desde = 0;
            if (listaPagos.size() < 18) {
                hasta = listaPagos.size() - 1;
            } else {
                hasta = 17;
            }
            pintaSiguiente = false;
            pintaAnterior = false;

        } else if (listaPagos.size() > TAMANO_PAGINA && pagina == 1) {
            desde = 0;
            hasta = 16;
            pintaSiguiente = true;
            pintaAnterior = false;
        } else if (listaPagos.size() > TAMANO_PAGINA && pagina > 1) {
            desde = 17;
            if (listaPagos.size() < 34) {
                hasta = listaPagos.size() - 1;
            } else {
                hasta = 32;
            }
            pintaSiguiente = false;
            pintaAnterior = true;
        }

        ((JPanel) p_pago_tarjeta.getComponent(1)).removeAll();
        p_pago_tarjeta.repaint();

        if (pintaAnterior) {
            addButtonAnterior(p_pago_tarjeta);
        }
        // INICIO DE PINTADO DE BOTÓN DE PAGO   

        for (int i = desde; i <= hasta; i++) {

            final MedioPagoBean mp = listaPagos.get(i);
            Action ac = new AbstractAction(mp.getCodMedioPago()) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (isHayPagosPorHacer()) {
                        pagoSimple.setPagoActivo(Pago.PAGO_TARJETA);
                        String codPagoActivo = (String) this.getValue(Action.NAME);
                        MedioPagoBean medioPago = (mp.equals(codPagoActivo)) ? mp : null;
                        if (medioPago.isRequiereAutorizacion()) {
                            try {
                                String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
                                pagoTarjeta(medioPago, autorizador);
                            } catch (SinPermisosException ex) {
                                log.debug("pintaBotonesMedioPago() - No se tienen permisos para seleccionar medio pago.");
                            }
                        } else {
                            pagoTarjeta(medioPago, null);
                        }
                    } else {
                        log.debug("pintaBotonesMedioPago() - No se permiten pagos con saldo 0.");
                        ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                    }
                }

                private void pagoTarjeta(MedioPagoBean medioPago, String autorizador) {
                    JReservacionesPagosV.this.pagoTarjeta(medioPago, autorizador, null);
                }
            };
            addButtonMedioPago(p_pago_tarjeta, ac, mp);
        }
        // FIN DE PINTADO DE BOTONES DE PAGO
        if (pintaSiguiente) {
            addButtonSiguiente(p_pago_tarjeta);
        }

    }

    @SuppressWarnings("deprecation")
    private void refrescaPagosTarjeta() {
        cambiaPanelPago(p_pago_tarjeta);
        if (((JPanel) p_pago_tarjeta.getComponent(1)).getComponents().length != 0) {
            b_m_procesar_pago.setNextFocusableComponent(((JPanel) p_pago_tarjeta.getComponent(1)).getComponent(0));
            ((JButtonForm) ((JPanel) p_pago_tarjeta.getComponent(1)).getComponent(((JPanel) p_pago_tarjeta.getComponent(1)).getComponents().length - 1)).setNextFocusableComponent(b_pago_efectivo);
            ((JPanel) p_pago_tarjeta.getComponent(1)).getComponent(0).requestFocus();
        } else {
            b_m_procesar_pago.setNextFocusableComponent(b_pago_efectivo);
        }
    }

    private void accionPaginaAnterior() {
        pintaBotonesMedioPago(1);
        paginaPagoTarjeta = 1;
        refrescaPagosTarjeta();
    }

    private void accionPaginaSiguiente() {
        pintaBotonesMedioPago(2);
        paginaPagoTarjeta = 2;
        refrescaPagosTarjeta();
    }

    private void accionCambiaPaginaPagoTarjeta() {
        if (panel_pagos_activo != null && panel_pagos_activo.equals(p_pago_tarjeta)) {
            if (this.paginaPagoTarjeta == 1) {
                accionPaginaSiguiente();
            } else {
                accionPaginaAnterior();
            }
        }
    }

    private void addButtonAnterior(JPanel panel) {
        JButtonForm button = new JButtonForm(" << (F10)");
        Action ac = new AbstractAction(" << (F10)") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JReservacionesPagosV.this.accionPaginaAnterior();
            }
        };
        button.setAction(ac);
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/arrow_left.png")));
        button.setText("   (F10)");
        button.setPreferredSize(new Dimension(150, 22));
        ((JPanel) panel.getComponent(1)).add(button);

    }

    private void addButtonSiguiente(JPanel panel) {
        JButtonForm button = new JButtonForm(" >> (F10)");
        Action ac = new AbstractAction(" >> (F10)") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JReservacionesPagosV.this.accionPaginaSiguiente();
            }
        };
        button.setAction(ac);
        button.setText("   (F10)");
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/arrow_right.png")));
        button.setPreferredSize(new Dimension(150, 22));
        ((JPanel) panel.getComponent(1)).add(button);

    }

    private void accionVolver(boolean recalcularVenta, boolean preguntar) {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            if (this.getContenedor() != null) {
                this.getContenedor().setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
                this.getContenedor().setModalityType(ModalityType.MODELESS);
            }
            if (!preguntar || ventana_padre.crearVentanaConfirmacion("Esta acción cancelará el pago actual, ¿Desea continuar?")) {
                if (pagos != null && pagos.hayPagosAutorizados()) {
                    if (!ventana_padre.crearVentanaConfirmacion("Se cancela los pagos por no cumplir las formas de Pago, ¿Desea salir? ")) {
                        ticket.getLineas().setGarantiaExtendidaGratuitaRechazada(false);
                        return;
                    }
                }
                cancelado = true;
                if (pagos != null) {
                    pagos.setCompensacionAplicada(false);
                }
                if (ticket != null && ticket.getTotales() != null) {
                    ticket.getTotales().setCompensacionGobierno(BigDecimal.ZERO);
                }
                if (this.getContenedor() == null) {
                    habilitaPanelVentas(recalcularVenta);
                } else {
                    // Si al volver cerramos el pago desacemos los valores de los pagos actuales
                    this.getContenedor().setVisible(false);
                    removeFunctionKeys();
                }
            }
        }
    }

    private void regresarPantallaPorPromo() {
        cancelado = true;
        if (pagos != null) {
            pagos.setCompensacionAplicada(false);
        }
        if (ticket != null && ticket.getTotales() != null) {
            ticket.getTotales().setCompensacionGobierno(BigDecimal.ZERO);
        }
        if (this.getContenedor() == null) {
            habilitaPanelVentas(true);
        } else {
            // Si al volver cerramos el pago desacemos los valores de los pagos actuales
            this.getContenedor().setVisible(false);
            removeFunctionKeys();
        }
    }

    private void accionEditarPago() {
        if (ticket.getPagos().getPagos().isEmpty()) {
            return;
        }
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            lineaSelecionada = tb_pagos.getSelectedRow();
            Pago p = null;
            if (lineaSelecionada >= 0) {
                p = ticket.editarLineaPago(lineaSelecionada);
            } else {
                p = ticket.editarLineaPago();
            }
            jInformacionExtraPago1.modificaContenido(this, p);
            editando = true;
            p.setSaldoInicial(ticket.getPagos().getSaldo().add(p.getTotal()));
            if (p.isPagoTarjeta()) {
                jPagosTarjeta.setPagoCredito((PagoCredito) p);
                jPagosTarjeta.setPlanesTableModel(new PlanesTarjetaTableModel((PagoCredito) p));

                if (((PagoCredito) p).getPlanSeleccionado() != null) {
                    jPagosTarjeta.iniciaVista(((PagoCredito) p).getPlanSeleccionado(), ticket);
                } else {
                    jPagosTarjeta.iniciaVista(ticket);
                }

                JPrincipal.setPopupActivo(jPagosTarjeta);
                jPagosTarjeta.setVisible(true);
                JPrincipal.setPopupActivo(null);
                tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                setTamanosTabla();
                refrescarTotales();
                b_pago_efectivoActionPerformed(null);
            } else if (p.isPagoOtros()) {
                // Posibilidad de editar otros pagos. Por ahora no implementada, ya que 
                // sólo el pago Retención a la fuente es tratado como otro
                ventana_padre.crearAdvertencia("El medio de pago seleccionado no permite su edición. Puede borrarlo y añadirlo de nuevo.");
            } else { // contado y efectivo
                pagoSimple = p;
                pagoSimple.setModalidad(modoPago);
                if (!pagoSimple.getMedioPagoActivo().isAdmiteVuelto()) {

                    t_pago_efectivo_entregado.setFocusable(false);
                    t_pago_efectivo_entregado.setEnabled(false);
                    l_pagos_efectivo_entregado.setForeground(new Color(51, 153, 255));
                    t_pago_efectivo_usted_paga.setNextFocusableComponent(b_aceptar);
                    //t_pago_efectivo_usted_paga.requestFocus();
                }
                cambiaPanelPago(p_pago_efectivo);
                b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);
                b_aceptar.setNextFocusableComponent(b_aceptar);
                t_pago_efectivo_usted_paga.setNextFocusableComponent(t_pago_efectivo_entregado);
                t_pago_efectivo_usted_paga.requestFocus();
            }
            refrescarTotales();
            refrescarPagosEfectivos();
        }
    }

    private void accionBorrarPago() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            lineaSelecionada = tb_pagos.getSelectedRow();
            if (lineaSelecionada >= 0) {
                ticket.eliminarLineaPagoV(lineaSelecionada);
            } else {
                ticket.eliminarLineaPagoV();
            }
            tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
            setTamanosTabla();
            refrescarTotales();
            b_pago_efectivoActionPerformed(null);
        }
    }

    private void accionProcesarPago() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            Long promoSocioNuevo = 0L;
            ticket.getPagos().recalculaTotales();
            try {
                // [VALIDACIONES DE PANTALLA DE PAGO]
                log.debug("accionProcesarPago() [ESTABLECER MEDIOS] : Validación de importes");
                if (hayAbonoMinimo && ticket.getPagos().getUstedPaga().compareTo(pagoMinimo) < 0 && pagoMinimo.compareTo(BigDecimal.ZERO) != 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - Los medios de pago no cubren el importe minimo a pagar del abono a reservación");
                    throw new ValidationException("Importe mínimo a abonar de $ " + pagoMinimo.toString());
                } else if (ticket.getPagos().getPagado().compareTo(pagoMinimo) < 0 && pagoMinimo.compareTo(BigDecimal.ZERO) != 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - Los medios de pago no cubren el importe minimo a pagar");
                    throw new ValidationException("Importe mínimo a abonar de $ " + pagoMinimo.toString());
                } else if (ticket.getPagos().getPagado().compareTo(pagoMaximo) > 0) {
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - Los medios de pago superan el importe total a pagar");
                    throw new ValidationException("Importe m\u00E1ximo a abonar es de $ " + pagoMaximo.toString());
                } else if (!puedePagarTodo && ticket.getPagos().getPagado().compareTo(pagoMaximo) >= 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - No se puede realizar un abono inicial por el total de la reserva");
                    throw new ValidationException("No se puede realizar un abono inicial por el total de la reserva");
                } else if (ticket.getPagos().getPagado().compareTo(pagoMinimo) == 0 && pagoMinimo.compareTo(BigDecimal.ZERO) == 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - No se puede realizar un pago de saldo 0");
                    throw new ValidationException("No se puede realizar un abono de saldo cero");
                }
                TicketPromocionesFiltrosPagos promoFiltroPagos = ticket.getTicketPromociones().getPromocionesFiltrosPagos();
                Long aceptaPromo = promoFiltroPagos.getPromoAceptarDS();
                listaPromoAceptadas = new ArrayList<PromoMedioPago>();
                PromoMedioPago listaPromoAceptadasTemp = new PromoMedioPago();
                if (aceptaPromo == 1L) {
                    boolean aceptada = true;
                    ticket.getTicketPromociones().getPromocionesFiltrosPagos().addPromocion(Sesion.promocionDiaSocio, aceptada);
                    for (Long diaSocio : promoFiltroPagos.getPromocionesAceptadas()) {
                        listaPromoAceptadasTemp.setIdPromo(diaSocio);
                        listaPromoAceptadasTemp.setValor(PROMO_ACEPTADA);
                        listaPromoAceptadas.add(listaPromoAceptadasTemp);
                    }
                }
                // comprobamos que los medios de pago seleccionados son los exigidos por las promociones aplicadas
//                Promocion promocion = promoFiltroPagos.isTodosPagosSeleccionados(pagos);
                if (Sesion.getTicket() != null) {
                    lineasTicket = Sesion.getTicket().getLineas().getLineas();
                    for (LineaTicket lineaPromo : lineasTicket) {
                        if (lineaPromo.getListaPromocion() != null) {
                            for (PromoMedioPago lineaPromoAceptadas : lineaPromo.getListaPromocion()) {
                                if (PROMO_ACEPTADA.equals(lineaPromoAceptadas.getValor())) {
                                    listaPromoAceptadas.add(lineaPromoAceptadas);
                                }
                            }
                        }
                    }
                    //Verificar si el cliente aplica por ser nuevo socio.

                    PromocionSocioBean promoSocio = ServicioPromocionesClientes.consultarPromoClienteSocio();
                    if (promoSocio.getId() != null) {
                        for (String ven : promoSocio.getVencimientos()) {
                            for (Pago pago : pagos.getPagos()) {
                                if (ven.equals(Long.toString(pago.getVencimiento().getIdMedioPagoVencimiento()))) {
                                    promoSocioNuevo = 1L;
                                }
                            }
                        }
                    } else {
                        promoSocioNuevo = 1L;
                    }
                }
                //Verfica si existe alguna promocion por forma de pago y item.
                Promocion promocion = promoFiltroPagos.isTodosPagosSeleccionadosPorItem(pagos, listaPromoAceptadas);
                boolean anular;

                if (!PromocionTipoDtoManualTotal.isActiva()) {
                    promoSocioNuevo = 1L;
                }

                if (promocion != null || promoSocioNuevo == 0L) {
                    if (promocion != null) {
                        anular = JPrincipal.getInstance().crearVentanaConfirmacion(promocion.getMensajeNoAplicablePagos() + "\nPromoción: " + promocion.getDescripcionImpresion() + "\n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar las formas de pago (Cancelar)", 80);
                    } else {
                        anular = JPrincipal.getInstance().crearVentanaConfirmacion("\n No se ha seleccionado la forma de pago necesaria para aplicar promoción de 20 DÓLARES POR NUEVO SOCIO.  \n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar las formas de pago (Cancelar)", 80);
                    }
                    if (anular) {
                        BigDecimal encerrarDesc = new BigDecimal(0);
                        if (ticket.getTicketPromociones().isFacturaAsociadaDiaSocio()) {
                            ticket.getTicketPromociones().desvincularFacturaDiaSocio();
                        }
                        ticket.setPromoPago(1L);
                        if (promoSocioNuevo == 0L) {
                            PromocionTipoDtoManualTotal.activarPromoDiaSocio(encerrarDesc);
                            Sesion.getTicket().recalcularTotales();
                            PromocionTipoDtoManualTotal.desActivar();
//                            refrescaTablaTicketPromo("Se encerra Descuento total Manual", encerrarDesc);
                        } else {
                            promoFiltroPagos.resetPromocion(promocion.getIdPromocion());
                            ticket.resetearLineasPromocion();
                            if (Sesion.getTicket() != null) {
                                List<LineaTicket> lineasTicket = Sesion.getTicket().getLineas().getLineas();
                                for (LineaTicket lineaPromo : lineasTicket) {
                                    List<PromoMedioPago> promoMP = new ArrayList<PromoMedioPago>();
                                    if (lineaPromo.getListaPromocion() != null) {
                                        promoMP.addAll(lineaPromo.getListaPromocion());
                                        for (PromoMedioPago p : promoMP) {
                                            if (p.getIdPromo() == promocion.getIdPromocion()) {
                                                if (PROMO_ACEPTADA.equals(p.getValor())) {
                                                    lineaPromo.getListaPromocion().clear();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            promoFiltroPagos.addPromocionRechazada(promocion);
                        }
                        regresarPantallaPorPromo();

                        //comentado para pruebas Promocion 
                        accionVolver(true, false);
                    }
                    return;
                }

                Promocion promoMontoMinimo = promoFiltroPagos.isCumpleMontoMinimo(pagos, ticket.getLineas());
                if (promoMontoMinimo != null) {
                    anular = JPrincipal.getInstance().crearVentanaConfirmacion(promoMontoMinimo.getMensajeNoAplicableMontoMinimo() + "\nPromoción: " + promoMontoMinimo.getDescripcionImpresion() + "\n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar las formas de pago o añadir artículos (Cancelar)", 80);
                    if (anular) {
                        promoFiltroPagos.resetPromocion(promoMontoMinimo.getIdPromocion());
                        promoFiltroPagos.addPromocionesRechazadasParaSiempre(promoMontoMinimo);
                        // meter promocione en rechazadas para siempre
                        accionVolver(true, false);
                    }
                    return;
                }
                if (isModoVenta()) {
                    if (!comprobarPromocionesFinales(promoFiltroPagos, true)) {
                        return;
                    }
                } else if (isModoArticulosPlan()) {
                    if (!comprobarPromocionesFinales(promoFiltroPagos, false)) {
                        return;
                    }
                }

                // comprobamos que los medios de pago de crédito directo seleccionados no fueron utilizados para las promociones aplicadas en facturas anteriores. 
                if (!promoFiltroPagos.isPagosCreditoDirectoOK(pagos)) {
                    String msg = "Promoción no aplica. Este número de crédito ya accedió previamente a la promoción.";
                    if (ticket.getTicketPromociones().isFacturaAsociadaDiaSocio()) {
                        msg = "Crédito ya accedió a la promoción.";
                    }
                    anular = JPrincipal.getInstance().crearVentanaConfirmacion(msg + "\n\n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar las formas de pago (Cancelar)", 80);
                    if (anular) {
                        promoFiltroPagos.reset();
                        accionVolver(true, false);
                    }
                    return;
                }

                // comprobamos que los medios de pago permitan garantía extendida gratuita para clientes GOLD
                if (isModoFactura()
                        && ticket.getLineas().tieneGarantiaExtendida()
                        && ticket.getCliente().isGarantiaExtendidaGratis()
                        && !ticket.getLineas().isGarantiaExtendidaGratuitaRechazada()
                        && !pagos.contieneMedioPago(Variables.getVariable(Variables.GARANTIA_EXT_MEDIO_PAGO_DESCUENTO))) {
                    anular = JPrincipal.getInstance().crearVentanaConfirmacion("Para obtener garantía extendida gratuitamente debe pagar con Tarjeta Gold. \n¿Qué desea hacer?\n - Volver y pagar el importe de la garantía extendida (Aceptar)\n - Cambiar las formas de pago (Cancelar)", 80);
                    if (anular) {
                        ticket.getLineas().setGarantiaExtendidaGratuitaRechazada(true);
                        ticket.recalcularTotales();
                        JVentas jv = (JVentas) JPrincipal.getInstance().getView(JPrincipal.VISTA_VENTAS);
                        jv.refrescaTablaTicket();
                        accionVolver(false, false);
                    }
                    return;
                }

                b_m_procesar_pago.setEnabled(false);
                refrescarTablaPagos();

                // Comprobamos si se pagó con los pagos adecuados para la promoción
                for (Pago p : ticket.getTicketPromociones().getPagosSeleccionados()) {
                    if (!ticket.getPagos().contienePago(p)) {
                        String mensajeError = "El medio de pago " + p.getMedioPagoActivo().getDesMedioPago();
                        if (p instanceof PagoCredito) {
                            mensajeError = mensajeError + " con plan " + ((PagoCredito) p).getPlanSeleccionado().getPlan();
                        }
                        mensajeError = mensajeError + " no fué seleccionado";
                        throw new ValidationException(mensajeError);
                    }
                }

                // Comprobamos si el cliente genérico ha de facturar
                boolean ventanaCancelada = false;
                if (isModoFactura()) {
                    ventanaCancelada = ManejadorPagos.comprobarFacturacionClienteGenerico(Sesion.getTicket());
                }
                if (ventanaCancelada) {
                    return;
                }
                ticket.recalcularFinalPagado();

                // [PREPARAMOS EL CAMBIO DE ESTADO]
                accionListaPagoEfectivo();
                estado = JReservacionesPagosV.REALIZAR_PAGOS;
                habilitaElementos(false);
                p_pago_efectivo.setEnabled(false);
                b_m_procesar_pago.requestFocus();
                boolean ventanaAceptada = false;

                // [OPERACIONES EN FUNCIÓN DE LA OPERACIÓN DE PAGO QUE VAMOS A EJECUTAR]
                log.debug("accionProcesarPago() [ESTABLECER MEDIOS] : Operaciones en función de operación de pago");

                if (isModoFactura()) {
                    ticket.recalcularFinalPagado();
                    ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
                    ticket.finalizarTicket(true);
                }

                if (isModoArticulosReservacion()) {
                    ticket.setObservaciones(reservacion.getObservaciones());
                    reservacion.prepararFacturaInvitado(ticket, reservacion.getReservacion(), reservacion.getFacturacion());
                    ventanaAceptada = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea finalizar la compra de artículos?", "Sí", "No");
//                    ventanaAceptada = ts.imprimirFacturaPagoReservaArticulo(ticket, reservacion.getReservacion(), invitadoSeleccionado, false, PrintServices.IMPRESORA_PANTALLA);
                } else if (isModoArticulosPlan()) {
                    ticket.setObservaciones(planNovio.getObservaciones());
                    ticket.finalizarTicket(true);
                    ventanaAceptada = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea finalizar la compra?", "Sí", "No");
//                    ventanaAceptada = ts.imprimirFacturaPagoPlanArticulo(ticket, planNovio, invitadoPlanSeleccionado, false, PrintServices.IMPRESORA_PANTALLA);
                } else if (isModoVenta()) {
                    ticket.setFecha(new Fecha());
                    ventanaAceptada = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea finalizar la venta?", "Sí", "No");
//                    ventanaAceptada = ts.imprimirTicket(ticket, false, PrintServices.IMPRESORA_PANTALLA);
                } else {
                    // En cualquiera de los otros modos, no hay que mostrar ventana de previsualización de factura.
                    // Lanzamos directamente la impresión y apertura de cajón
                    ventanaAceptada = true;
                }
                if (ventanaAceptada) {
                    JVentas jv = (JVentas) JPrincipal.getInstance().getView(JPrincipal.VISTA_VENTAS);
                    jv.limpiarFormularioEntregaDomicilio();
                    log.debug("accionProcesarPago() - Comienzo del proceso de pagos");
                    Sesion.putTiempoEjecucion("inicioProcesoPago", System.currentTimeMillis());
                    if (ticket.getPagos().abrirCajon()) {
                        log.debug("SEÑAL APERTURA CAJON");
                        ts.abrirCajon();
                    }
                    // Añadimos un pago con el medio de pago de compensación del gobierno si lo hubiera
                    if (pagos.isCompensacionAplicada()) {
                        Pago p = new Pago(pagos, ticket.getCliente());
                        MedioPagoBean mp = MediosPago.consultar(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_COMPENSACION));
                        p.setMedioPagoActivo(mp);
                        p.setTotal(ticket.getTotales().getCompensacionGobierno());
                        p.setEntregado(ticket.getTotales().getCompensacionGobierno());
                        p.setTotalSinIva(ticket.getTotales().getCompensacionGobierno());
                        p.setUstedPagaSinIva(ticket.getTotales().getCompensacionGobierno());
                        p.establecerDescuento(BigDecimal.ZERO);
                        ticket.getPagos().addPago(p);
                    }
                    accionProcesarPago();
                } else {
                    habilitaElementos(true);
                    estado = JReservacionesPagosV.ESTABLECER_MEDIOS;
                    refrescarTablaPagos();
                }
            } catch (ValidationException ex) {
                ventana_padre.crearError(ex.getLocalizedMessage());
            } catch (Exception ex) {
                habilitaElementos(true);
                estado = JReservacionesPagosV.ESTABLECER_MEDIOS;
                refrescarTablaPagos();
                ventana_padre.crearError("Error " + ex.getLocalizedMessage());
                log.error("accionProcesarPago() - No se pudo realizar el pago: " + ex.getMessage(), ex);
            }

            p_autorizando.setVisible(false);
            b_m_procesar_pago.setEnabled(true);

        } else {
            // EFECTUAR PAGO
            String errorPinPad = "";
            try {
                // comprobamos que los pagos cubren el total
                if (hayAbonoMinimo && ticket.getPagos().getUstedPaga().compareTo(pagoMinimo) < 0 && pagoMinimo.compareTo(BigDecimal.ZERO) != 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - Los medios de pago no cubren el importe minimo a pagar del abono a reservación");
                    throw new ValidationException("Importe mínimo a abonar de $ " + pagoMinimo.toString());
                } else if (ticket.getPagos().getPagado().compareTo(pagoMinimo) < 0 && pagoMinimo.compareTo(BigDecimal.ZERO) != 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - Los medios de pago no cubren el importe minimo a pagar");
                    throw new ValidationException("Importe mínimo a abonar de $ " + pagoMinimo.toString());
                } else if (ticket.getPagos().getPagado().compareTo(pagoMinimo) == 0 && pagoMinimo.compareTo(BigDecimal.ZERO) == 0) {
                    //t_pago_efectivo_entregado.requestFocus();
                    t_pago_efectivo_usted_paga.requestFocus();
                    log.debug("accionProcesarPago() - No se puede realizar un pago de saldo 0");
                    throw new ValidationException("No se puede realizar un abono de saldo cero");
                }
                ReservaArticuloBean articulo = null;

                if (isModoAbonoReservacion() || isModoArticulosReservacion()) {
                    if (reservacion.getArticuloSeleccionado() != null && reservacion.getReservacion().getReservaTipo().getPermiteCompra()) {
                        articulo = reservacion.getArticuloSeleccionado();
                    }
                }

                if (accionProcesaRetencionFuente()) {
                    return;
                }

                ticket.recalcularFinalPagado();
                p_autorizando.setVisible(true);
                repintar(p_autorizando);
                Sesion.putTiempoEjecucion("inicioProcesoValidacion", System.currentTimeMillis());
                boolean pagosAutorizados = autorizarPagos();
                Sesion.putTiempoEjecucion("finProcesoValidacion", System.currentTimeMillis());
                log.debug("accionProcesarPago() - El proceso de validación ha tardado: " + Sesion.getDameTiempo("inicioProcesoValidacion", "finProcesoValidacion") + " segundos");
                p_autorizando.setVisible(false);

                if (!pagosAutorizados) {
                    b_m_procesar_pago.setEnabled(true);
                    habilitaElementos(true);
                    estado = JReservacionesPagosV.ESTABLECER_MEDIOS;
                    refrescarTablaPagos();
                    return;
                }
                log.debug("accionProcesarPago() [ESTABLECER MEDIOS] : Se ha pasado la validación");
                if (isModoVenta()) {
                    accionProcesaVenta();
                    Sesion.putTiempoEjecucion("finProcesoPago", System.currentTimeMillis());
                    double tiempoVentana = 0;
                    try {
                        tiempoVentana = Sesion.getDameTiempo("finProcesoImpresion", "finVentanaAceptada");
                    } catch (Exception ignore) {
                    }
                    //ServicioPromociones.consultar();
                    LecturaConfiguracion.leerPromociones();
                    log.debug("accionProcesarPago() - El proceso de venta ha tardado: " + String.format("%.4f", (Sesion.getDameTiempo("inicioProcesoPago", "finProcesoPago") - tiempoVentana)) + " segundos");
                    return;
                }
                if (isModoArticulosReservacion()) {
                    reservacion.guardaNuevoPagoArticulos(ticket, reservacion.getReservacion(), reservacion.getInvitadoSeleccionado(), reservacion.getInvitadoActivo(), reservacion.getFacturacion());
                } else if (isModoArticulosPlan()) {
                    planNovio.accionPagoArticulos(ticket);
                } else if (isModoGiftCard()) {
                    // Guardamos nuevo pago de artículos
                    String tramaGC;
                    ticket.setObservaciones(giftCard.getObservaciones());
                    LogGiftCardBean logGiftCard;
                    if (this.giftCard.getIdGiftCard() == null) {
                        // Mirar pago realizado
                        logGiftCard = ServicioGiftCard.crear(tramaGiftCard, this.pagoMaximo, autorizador, ticket.getPagos(), clienteGC);
                        tramaGC = tramaGiftCard.getIdGiftCard();
                    } else {
                        logGiftCard = ServicioGiftCard.recargar(giftCard, giftCard.getSaldo().add(this.pagoMaximo), autorizador, ticket.getPagos(), clienteGC);
                        tramaGC = giftCard.getIdGiftCard();
                    }
                    //Imprimimos el comprobante con codalm - codcaja - id
                    String idGC = Numero.completaconCeros(logGiftCard.getCodAlmacen(), 3) + " - " + Numero.completaconCeros(logGiftCard.getCodCaja(), 2) + " - " + Numero.completaconCeros(logGiftCard.getIdCargaGiftCard().toString(), 7);
                    PrintServices.getInstance().limpiarListaDocumentos();
                    PrintServices.getInstance().imprimirComprobanteGiftCard(tramaGC, logGiftCard.getCodOperacion(), clienteGC, this.pagoMaximo, ticket.getPagos().getPagos(), ticket.getObservacionesLineaEnTicket(), idGC);

                    // Insertamos el documento en BBDD con el giftcard y la lista de documentos, que luego limpiamos.
                    DocumentosService.crearDocumentoGiftCard(logGiftCard, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.GIFTCARD, ticket.getObservaciones());
                    PrintServices.getInstance().limpiarListaDocumentos();

                    ventana_padre.crearConfirmacion("Carga/Recarga de Giftcard Realizada");
                } else if (isModoAbonoPlan()) {
                    planNovio.accionPagoAbono(ticket);
                    ventana_padre.crearConfirmacion("Se realizó el abono satisfactoriamente");
                } else if (isModoCreditoDirecto()) {
                    CreditoServices.realizarPago(ticket, creditoDirecto);
                } else if (isModoLetraCambio()) {
                    LetraCambiosServices.realizarPagoCuota(ticket, letraCambio, letraCambioCuota);
                    ventana_padre.crearConfirmacion("La cuota de la letra ha sido abonada correctamente.");
                } else {
                    reservacion.guardaNuevoAbonoReservacion(ticket, reservacion.getReservacion(), invitadoSeleccionado, articulo, isLiquidarReservacion);
                    reservacion.calculaAbonosReales();
                    ventana_padre.crearConfirmacion("Se realizó el abono satisfactoriamente");
                }
                //Imprimimos cheque si los hubiera    
                if (VariablesAlm.getVariableAsBoolean(VariablesAlm.POS_CONFIG_FUNC_IMPRESION_CHEQUES)) {
                    impresionCheque();
                }

                this.setPagoRealizado(ticket.getPagos().getPagado());
                cancelado = false;

                // Cerramos la ventana si no estamos en un modo venta
                if (this.contenedor != null) {
                    this.contenedor.setVisible(false);
                    if (isModoArticulosPlan() || isModoArticulosReservacion()) {
                        ventana_padre.crearConfirmacion("Compra de Artículos Realizada");
                    }
                } else {
                    // En caso de errores, no paramos la venta
                    habilitaPanelVentas(false);
                }
                ticket = null;
                Sesion.putTiempoEjecucion("finProcesoPago", System.currentTimeMillis());
                log.debug("accionProcesarPago() - El proceso de ha tardado: " + Sesion.getDameTiempo("inicioProcesoPago", "finProcesoPago") + " segundos");
                ventana_padre.showView("ident-cliente");
            } catch (ReservasException ex) {
                log.debug(" Ingreso por el catch ReservasException ");
                log.error("accionProcesarPago() - No se pudo realizar la reservación: " + ex.getMessage(), ex);
                ventana_padre.crearError(ex.getMessage());
                errorPinPad = accionCancelarPagos();
            } catch (ValidationException ex) {
                log.debug(" Ingreso por el catch ValidationException ");
                ventana_padre.crearError(ex.getLocalizedMessage());
                errorPinPad = accionCancelarPagos();
            } catch (TicketPrinterException e) {
                log.debug(" Ingreso por el catch TicketPrinterException ");
                log.error("accionProcesarPago() - Se realizo la recarga pero no pudo imprimirse el Ticket" + e.getMessage(), e);
                ventana_padre.crearError("Se realizo la recarga pero no pudo imprimirse el Ticket");
                errorPinPad = accionCancelarPagos();
            } //GiftCardException, 
            catch (GiftCardException e) {
                log.debug(" Ingreso por el catch GiftCardException ");
                ventana_padre.crearError("Error creando GiftCard");
                errorPinPad = accionCancelarPagos();
            } catch (GiftCardConstraintViolationException e) {
                log.debug(" Ingreso por el catch GiftCardConstraintViolationException ");
                ventana_padre.crearError("Esta GiftCard ha sido registrada en el sistema con anterioridad");
                errorPinPad = accionCancelarPagos();
            } catch (Exception ex) {
                log.debug(" Ingreso por el catch Exception ");
                log.error("accionProcesarPago() - No se pudo realizar la reservación: " + ex.getMessage(), ex);
                ventana_padre.crearError("Se produjo un error. No se pudo realizar la operación");
                errorPinPad = accionCancelarPagos();
            }
            if (!errorPinPad.isEmpty()) {
                ventana_padre.crearError("No se han podido anular algunos pagos del pinpad: " + errorPinPad);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void accionListaPagoEfectivo() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);
            cambiaPanelPago(p_pago_efectivo);
            pagoSimple = new Pago(ticket.getPagos(), ticket.getCliente(), ticket.getTotales(), null);
            pagoSimple.setModalidad(modoPago);
            pagoSimple.setPagoActivo(Pago.PAGO_EFECTIVO);
            pagoSimple.setMedioPagoActivo(MediosPago.getInstancia().getPagoEfectivo());
            pagoSimple.resetear(pagos.getSaldo());
            refrescarPagosEfectivos();
            t_pago_efectivo_entregado.setEnabled(true);
            l_pagos_efectivo_entregado.setForeground(Color.BLACK);
            b_aceptar.setNextFocusableComponent(b_pago_efectivo);
            t_pago_efectivo_usted_paga.setNextFocusableComponent(t_pago_efectivo_entregado);
            l_medio_pago.setText("EFECTIVO");
            //t_pago_efectivo_entregado.requestFocus();
            t_pago_efectivo_usted_paga.requestFocus();
        }
    }

    private void accionListaPagoContado() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            cambiaPanelPago(p_pago_contado);
            if (((JPanel) p_pago_contado.getComponent(1)).getComponents().length != 0) {
                b_m_procesar_pago.setNextFocusableComponent(((JPanel) p_pago_contado.getComponent(1)).getComponent(0));
                ((JButtonForm) ((JPanel) p_pago_contado.getComponent(1)).getComponent(((JPanel) p_pago_contado.getComponent(1)).getComponents().length - 1)).setNextFocusableComponent(b_pago_efectivo);
                ((JPanel) p_pago_contado.getComponent(1)).getComponent(0).requestFocus();
            } else {
                b_m_procesar_pago.setNextFocusableComponent(b_pago_efectivo);
            }
        }
    }

    private void accionListaPagoTarjeta() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            pintaBotonesMedioPago(1);
            paginaPagoTarjeta = 1;

            refrescaPagosTarjeta();
        }

    }

    @SuppressWarnings("deprecation")
    private void AccionListaPagoOtros() {
        if (estado == JReservacionesPagosV.ESTABLECER_MEDIOS) {
            cambiaPanelPago(p_pago_otros);
            if (((JPanel) p_pago_otros.getComponent(1)).getComponents().length != 0) {
                b_m_procesar_pago.setNextFocusableComponent(((JPanel) p_pago_otros.getComponent(1)).getComponent(0));
                ((JButtonForm) ((JPanel) p_pago_otros.getComponent(1)).getComponent(((JPanel) p_pago_otros.getComponent(1)).getComponents().length - 1)).setNextFocusableComponent(b_pago_efectivo);
                ((JPanel) p_pago_otros.getComponent(1)).getComponent(0).requestFocus();
            } else {
                b_m_procesar_pago.setNextFocusableComponent(b_pago_efectivo);
            }
        }
    }

    private void habilitaElementos(boolean habilita) {

        b_aceptar.setEnabled(habilita);
        b_m_borrarpago.setEnabled(habilita);
        b_m_editarpago.setEnabled(habilita);
        b_m_volver.setEnabled(habilita);
        b_pago_contado.setEnabled(habilita);
        b_pago_efectivo.setEnabled(habilita);
        b_pago_otros.setEnabled(habilita);
        b_pago_tarjeta.setEnabled(habilita);

        b_m_borrarpago.setVisible(habilita);
        b_m_editarpago.setVisible(habilita);
        b_m_volver.setVisible(habilita);

        t_pago_efectivo_entregado.setEnabled(habilita);
        t_pago_efectivo_total.setEnabled(habilita);
        t_pago_efectivo_usted_paga.setEnabled(habilita);

        if (habilita) {
            b_m_procesar_pago.setText("<html><center>Cobrar<br/>Pagos<br/>F9</center></html>");
        } else {
            b_m_procesar_pago.setText("<html><center><br/>Finalizar<br/>F9</center></html>");

        }
    }

    @Override
    public void dispose() {
        // dispose a las ventanas
        super.dispose();
        v_bonos.dispose();
        v_informacion_extra_pago.dispose();
        v_lectura_tarjeta.dispose();
        v_notaCredito.dispose();
        v_nota_credito.dispose();
        p_bonos.dispose();
        p_lectura_tarjeta.dispose();
        p_bonos = null;
        p_lectura_tarjeta = null;
        p_pago_contado = null;
        p_pago_contenedor = null;
        p_pago_efectivo = null;
        p_pago_otros = null;
        p_pago_tarjeta = null;
        p_publicidad = null;

        // Borramos todas las referencias
        v_bonos = null;
        v_informacion_extra_pago = null;
        v_lectura_tarjeta = null;
        v_notaCredito = null;
        v_nota_credito = null;

        reservacion = null;
        ticket = null;
        ventana_padre = null;
        panel_pagos_activo = null;
        jPagosTarjeta = null;
        pagoSimple = null;
        pagos = null;
        contenedor = null;
        invitadoSeleccionado = null;
        pagoMinimo = null;
        pagoMaximo = null;
        pagoRealizado = null;
        listaPagos = null;
    }

    @SuppressWarnings("deprecation")
    private boolean eventoLecturaTarjetaGiftCard(TramaTarjetaGiftCard tident) throws ParserTramaException {
        boolean esGiftCard = false;
        try {
            esGiftCard = true;
            if (!ticket.getPagos().contieneGiftCard(tident.getIdGiftCard())) {

                GiftCardBean giftCard = ServicioGiftCard.consultar(tident.getIdGiftCard());
                MedioPagoBean medioPagotGiftCard = MediosPago.getInstancia().getMedioPago(giftCard.getCodMedioPago());

                if (medioPagotGiftCard == null) {
                    log.error("eventoLecturaTarjetaGiftCard() - Medio de pago no encontrado: " + giftCard.getCodMedioPago());
                    throw new GiftCardException("No se encontró el medio de pago asociado a la tarjeta ");
                }

                if (giftCard != null && giftCard.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                    PagoGiftCard pago = new PagoGiftCard(ticket.getPagos(), ticket.getCliente());
                    pago.crearPagoGiftCard(medioPagotGiftCard, giftCard);
                    pago.setIdUsoGiftCard(ServicioContadoresCaja.obtenerContadorUsoGiftcard());
                    ServicioContadoresCaja.actualizarContadorUsoGiftcard(pago.getIdUsoGiftCard() + 1);
                    pago.setSaldoInicial(pagos.getSaldo());
                    pagoSimple = pago;
                    cambiaPanelPago(p_pago_efectivo);

                    t_pago_efectivo_usted_paga.requestFocus();
                    b_m_procesar_pago.setNextFocusableComponent(t_pago_efectivo_total);

                    t_pago_efectivo_entregado.setEnabled(false);
                    l_pagos_efectivo_entregado.setForeground(new Color(51, 153, 255));
                    t_pago_efectivo_usted_paga.setNextFocusableComponent(b_aceptar);
                    t_pago_efectivo_usted_paga.requestFocus();
                    t_pago_efectivo_entregado.setText(pago.getSaldoGiftCard().toString());
                    pagoSimple.resetear(pagos.getSaldo());
                    l_medio_pago.setText("GIFTCARD");
                    refrescarPagosEfectivos();

                } else if (giftCard != null && giftCard.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
                    ventana_padre.crearError("La giftcard indicada no tiene saldo.");
                }
            } else {
                ventana_padre.crearError("La giftcard ya ha sido utilizada en la compra actual");

            }
        } catch (GiftCardException ex) {
            esGiftCard = true;
            ventana_padre.crearError(ex.getMessage());
        } catch (GiftCardNotFoundException ex) {
            esGiftCard = true;
            ventana_padre.crearError(ex.getMessage());
        } catch (GiftCardAnuladaException ex) {
            esGiftCard = true;
            ventana_padre.crearError(ex.getMessage());
        } catch (ContadorException ex) {
            esGiftCard = true;
            ventana_padre.crearError(ex.getMessage());
        }

        return esGiftCard;

    }

    /**
     *
     * @param pag forma de pago
     * @param excedeCupo par&aacute;metro para saber si la autorizaci&oacute;n
     * por exceder el cupo
     */
    private void accionAutorizacionManual(PagoCredito pag, boolean excedeCupo) {
        // Paso 1. Si esta configrada la autorización.
        boolean soloConAutorizacion = Sesion.isValidacionManualSoloComoAdministrador();

        try {
            if (soloConAutorizacion) {
                String usuarioAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
                ticket.setAutorizadorVenta(usuarioAdministrador);
            }

            if (!pag.getMedioPagoActivo().isTarjetaSukasa()) {
                // Paso 2. Mostramos la ventana de selección de Banco
                //verificamos que el medio contenga bancos disponibles antes de mostrar pantalla RD
                //validacion creada para la que no aparesca pantalla bancos sin bancos disponibles
                if (pag.getMedioPagoActivo().getBancos().size() > 0) {
                    ventana_padre.crearVentanaInformacionBancos(pag);
                }

                // Paso 3. Mostrar Pantalla de introducción de número de autorización
                ventana_padre.crearVentanaIntroduccionAutorizacion(pag, null, ticket.isVentaManual());
                if (pag.getCodigoValidacionManual() != null && !pag.getCodigoValidacionManual().isEmpty()) {
                    pag.setFalloValidacion(false);
                    pag.setValidadoManual(true);
                    pag.setAuditoria(); // Este método asigna un nº de auditoría al medio de pago
                }
            } else {
                // Paso 3. Mostrar Pantalla de introducción de número de autorización
                if (excedeCupo) {
                    if (!Sesion.getDatosConfiguracion().isModoDesarrollo() && VariablesAlm.getVariableAsBooleanActual(Variables.ACTIVO_NOTIFICA_AUT_CONSUMO_CREDITO)) {
                        ventana_padre.crearVentanaAutorizacionCreditoDirectoExcedeCupo(pag, ticket);
                    } else {
                        ventana_padre.crearVentanaIntroduccionAutorizacionCreditoDirecto(pag);
                    }

                } else {
                    ventana_padre.crearVentanaIntroduccionAutorizacionCreditoDirecto(pag);
                }

                if (pag.getCodigoValidacionManual() != null && !pag.getCodigoValidacionManual().isEmpty()) {
                    cupoVirtualSeleccionado = null;
                    pag.setFalloValidacion(false);
                    pag.setValidadoManual(true);
                    pag.setAuditoria(); // Este método asigna un nº de auditoría al medio de pago
                }
            }

//            // Paso 3. Mostrar Pantalla de introducción de número de autorización
//            ventana_padre.crearVentanaIntroduccionAutorizacion(pag, null);
//            if (pag.getCodigoValidacionManual() != null && !pag.getCodigoValidacionManual().isEmpty()) {
//                pag.setFalloValidacion(false);
//                pag.setValidadoManual(true);
//                pag.setAuditoria(); // Este método asigna un nº de auditoría al medio de pago
//            }
        } catch (ContadorException ex) {
            log.error("accionAutorizacionManual() - No fué posible asignar un identificador  de nº de auditoría a la operación:");
        } catch (SinPermisosException ex) {
            log.debug("accionAutorizacionManual() - El usuario indicado no tiene permisos.", ex);
        }

    }

    public void refrescarTablaPagos() {
        tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
        setTamanosTabla();
    }

    private String accionCancelarPagos() {
        // Recorro los pagos validados. si hay alguno de tipo Credito envio una trama de Anulación.
        // Si falla la operación guardo la trama en una tabla de anulaciones
        log.debug("- (Accion Cancelar Pagos) - Anulamos los pagos con tarjeta");
        ticket.eliminarPagoCompensacion();
        // Mostramos el panel de espera
        p_autorizando.setVisible(true);
        repintar(p_autorizando);

        List<Pago> pagosAnuladosImpresos = new ArrayList<Pago>();
        String error = ticket.getPagos().anularAutorizacionPagos(pagosAnuladosImpresos);
        if (!pagosAnuladosImpresos.isEmpty()) {
            boolean res = ventana_padre.crearVentanaConfirmacion("¿Desea reimprimir los voucher de anulación?");
            while (res) {
                for (Pago p : pagosAnuladosImpresos) {
                    try {
                        if (p.getMedioPagoActivo().isTarjetaCredito() && !p.getMedioPagoActivo().isTarjetaSukasa()) {
                            PrintServices.getInstance().imprimirVoucherAnulacionPinPad((PagoCredito) p);
                        } else {
                            PrintServices.getInstance().imprimirVoucherAnulacion(p);
                        }

                        PrintServices.getInstance().imprimirVoucherAnulacion(p);
                    } catch (Exception e) {
                        log.error("accionCancelarPagos() - Se ha producido un error al imprimir el voucher de anulación: " + e.getMessage(), e);
                    }
                }
                res = ventana_padre.crearVentanaConfirmacion("¿Desea reimprimir los voucher de anulación?");
            }
        }
        // Quitamos el panel de espera
        p_autorizando.setVisible(false);
        refrescarTablaPagos();
        return error;

    }

    private void comprobarModo() {
        if (!isModoVenta() && !isModoAbono() && !isModoArticulosPlan() && !isModoArticulosReservacion() && !isModoCreditoDirecto() && !isModoGiftCard() && !isModoLetraCambio()) {
            throw new RuntimeException("Error inicializando pantalla de pagos. Modo NO IMPLEMENTADO: " + modo);
        }
    }

    private void inicializaModoPago() {
        comprobarModo();
        Byte modoPago = null;
        if (isModoGiftCard()) {
            modoPago = Pago.MODO_GIFTCARD;
        } else if (isModoAbonoPlan()) {
            modoPago = Pago.MODO_ABONO_PLAN;
        } else if (isModoAbonoReservacion()) {
            modoPago = Pago.MODO_ABONO_RESERVA;
        } else if (isModoCreditoDirecto()) {
            modoPago = Pago.MODO_CREDITO_DIRECTO;
        } else if (isModoLetraCambio()) {
            modoPago = Pago.MODO_LETRA_CAMBIO;
        } else {
            modoPago = Pago.MODO_NORMAL;
        }
        this.modoPago = modoPago;
    }

    private void animacionPromocion() {
        JPrincipal.setPopupActivo(v_animacion);
        v_animacion.setResizable(false);
        v_animacion.setAlwaysOnTop(true);
        v_animacion.setUndecorated(true);
        v_animacion.setModal(true);
        v_animacion.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        p_animacion.setBorder(null);
        p_animacion.setContenedor(v_animacion);
        v_animacion.setVisible(true);
        JPrincipal.setPopupActivo(null);
    }

    private boolean accionProcesaRetencionFuente() {
        PagoRetencionFuente pagrf = ticket.getPagos().contieneRetencion();
        if (pagrf == null) {
            return false;
        }
        p_retencion.iniciaVista(pagrf.getSaldoInicial(), pagrf.getTotal(), getIdTransaccion());
        p_retencion.iniciaFoco();
        JPrincipal.setPopupActivo(v_retencion);
        v_retencion.setAlwaysOnTop(true);
        v_retencion.setVisible(true);
        JPrincipal.setPopupActivo(null);
        if (!p_retencion.isExito()) {
            return true;
        }
        p_retencion.setExito(false);
        return false;
    }

    private String getIdTransaccion() {
        if (isModoFactura()) {
            return ticket.getIdFactura();
        }
        if (isModoLetraCambio()) {
            return letraCambio.getIdFactura();
        }
        return "";
    }

    private void accionProcesaVenta() {
        try {
            boolean resultadoImpresion = true;

            log.info("Acción Procesar Pago");
            // comprobamos que los pagos cubren el total
            if (ticket.getPagos().getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                t_pago_efectivo_usted_paga.requestFocus();
                throw new TicketException("Los medios de pago no cubren el importe total a pagar");
            }

            // Salvamos e imprimimos el ticket
            ticket.finalizarTicket(false);
            JPrincipal.crearVentanaPuntos(ticket);

            Sesion.putTiempoEjecucion("inicioEscrituraBBDD", System.currentTimeMillis());
            /**
             * Rd Escribir Tiket
             */
            //Escribir Tiket Final
            TicketService.escribirTicket(ticket);
            Sesion.putTiempoEjecucion("finEscrituraBBDD", System.currentTimeMillis());
            log.debug("accionProcesaVenta() - En realizar las inserciones en BBDD ha tardado " + Sesion.getDameTiempo("inicioEscrituraBBDD", "finEscrituraBBDD") + " segundos");
            //Limpiamos la lista de documentos del PrintServices
            ts.limpiarListaDocumentos();

            try {
//                if (ticket.getPagos().getMediosPagoCreditoDirecto().size() > 0) {
//               
//                //obtencion devalores para la impresion 
//                System.out.print("Fecha De Venta: ");
//                System.out.println(ticket.getFecha());
//                //////////////////////////////////////////
//                System.out.print("Factura Numero: ");
//                System.out.println(ticket.getIdFactura().substring(8));
//                //////////////////////////////////////////
//                System.out.print("Local: ");
//                System.out.println(ticket.getTienda());
//                ////////////////////////////////////////////////
//                System.out.print("Precio Final Venta: ");
//                System.out.println(ticket.getPagos().getTotal());
//                ////////////////////////////////////////////////
//                int indice=0;
//                for (int i=0;i<ticket.getPagos().getPagos().size();i++) {
//
//                    if (ticket.getPagos().getPagos().get(i) instanceof PagoCredito) {
//                        indice=i;
//                    }else{
//                        
//                    }
//                  
//                }
//                System.out.print("Plazo: ");
//                System.out.println(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses() + " Meses");
//                //////////////////////////////////////////////////
//                System.out.print("Taza de Interés de financiamiento: ");
//                System.out.println(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getPorcentajeInteres()+"%");
////                System.out.println(ticket.getPagos().get);
//                double valortotal=0;
//                DecimalFormat df = new DecimalFormat("#.00");
//                for (int i = 1; i <=ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses();  i++) {
//                    double valor = (Double.parseDouble(ticket.getPagos().getPagos().get(indice).getTotalImprimir()))/(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses());
//                    System.out.println("Detalle de Cuotas: " + "Cuota" + i + " Cuota De Capital: " + df.format(valor));
//                    valortotal=valortotal+valor;
//                }
//                 System.out.print("Total a pagar por el cliente al final del plazo: ");
//                 System.out.println(df.format(valortotal));
//                 System.out.print("Cedula Cliente: ");
//                 System.out.println(ticket.getCliente().getCodcli());     
//                }
                ts.imprimirTicket(ticket);

                //PARA PRUEBAS DE IMPRESION
                /*
                for (int i = 0; i < 20; i++)
                {
                    ts.imprimirTicket(ticket);
            }
                
                 */
            } catch (Exception e) {
                e.printStackTrace();
                resultadoImpresion = false;
            }

            log.debug("accionProcesaVenta() - Comprobando si hay que generar bonos de efectivo... ");
            BonosServices.crearBonosPagos(ticket.getPagos(), ticket.getIdFactura(), "Venta", ticket.getCliente());

            /*  if (Sesion.isSukasa()) {
                try {
                    ts.imprimirContratoExtensionGarantia(ticket);
                }
                catch (Exception e) {
                    log.error("accionProcesaVenta() - Error imprimiendo Contrato de Extensión de Garntia : " + e.getMessage(), e);
                    ventana_padre.crearError("Error imprimiendo Contratos de Extensión de Garntía ");
                }
            }*/
            // Insertamos el documento en BBDD con el ticket y la lista de documentos, que luego limpiamos.
            DocumentosService.crearDocumento(ticket, ts.getDocumentosImpresos(), DocumentosBean.FACTURA);

            /**
             * Relizamos el envio de Correo Electronico Para encuesta
             */
            // System.out.println("ANTES DE INTENTO ENVIO DE CORREO");
            // DatosCorreo datos =new DatosCorreo();
            // datos.setCorreoElectronico(ticket.getFacturacion().getEmail());
            // datos.setNumeroLocal(ticket.getTienda());
            // long initialTime2 = System.currentTimeMillis();
            // Runnable hiloEnvioCorreo = new hiloEnvioCorreo(datos, initialTime2);
            // System.out.println("ANTES DE HILO");
            // new Thread(hiloEnvioCorreo).start();
            // System.out.println("DESPUES DE HILO");
            // System.out.println("ANTES DE INTENTO ENVIO DE CORREO");
            /**
             * Relizamos insert Para el Envio De Sms
             */
            //Realizamos insert  Para el Envio  De Sms - SR se comenta por problemas con bytec
            if (!Sesion.getDatosConfiguracion().isModoDesarrollo()) {
                if (ticket.getPagos().getMediosPagoCreditoDirecto().size() > 0) {
                    for (PagoCreditoSK medioPago : ticket.getPagos().getMediosPagoCreditoDirecto()) {
                        log.debug("Envio de mensaje sms ");
                        try {
                            if (VariablesAlm.getVariableAsBooleanActual(Variables.POS_VALIDACION_PROTECCION_DATOS)) {
                                if (PoliticaProteccionDatosServices.existeAprobacionPoliticas(medioPago.getPlastico().getCedulaCliente())) {
                                    Runnable procesoHilo = new HiloNotificacionConsumo(ticket.getTienda() + ticket.getCodcaja() + String.valueOf(ticket.getId_ticket()),
                                            ticket.getCliente().getTelefonoMovil(), medioPago.getUstedPagaPrint().toString(), ticket.getUid_ticket(),
                                            medioPago.getPlastico().getCedulaCliente());
                                    new Thread(procesoHilo).start();
                                }
                            } else {
                                Runnable procesoHilo = new HiloNotificacionConsumo(ticket.getTienda() + ticket.getCodcaja() + String.valueOf(ticket.getId_ticket()),
                                        ticket.getCliente().getTelefonoMovil(), medioPago.getUstedPagaPrint().toString(), ticket.getUid_ticket(),
                                        medioPago.getPlastico().getCedulaCliente());
                                new Thread(procesoHilo).start();
                            }
                        } catch (Exception ex) {
                            log.info("ERROR INSERTANDO DATOS ENVIO CORREO " + ex);
                        }
                    }
                }
            }
            ts.limpiarListaDocumentos();

            //Imprimimos cheques si los hubiera
            if (VariablesAlm.getVariableAsBoolean(VariablesAlm.POS_CONFIG_FUNC_IMPRESION_CHEQUES)) {
                impresionCheque();
            }

            try {
                if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)) {
                    LogKardexBean logKardex = new LogKardexBean();
                    logKardex.setTipoAccion(LogKardexBean.tipoAccionVenta);
                    logKardex.setFactura(String.valueOf(ticket.getId_ticket()));
                    logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ? ticket.getAutorizadorVenta().getUsuario() : Sesion.getUsuario().getUsuario());
                    ServicioStock.aumentaStockVenta(ticket.getLineas().getLineas(), logKardex);
                    ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_51, ticket.getTienda(), false);
                }
            } catch (StockException e) {
                log.info("accionProcesaVenta() - ERROR ACTUALIZANDO STOCK. La venta ha sido realizada a pesar del error.");
                log.error("accionProcesaVenta() - ERROR ACTUALIZANDO STOCK EN SISTEMAS PROPIETARIOS DE SUKASA: " + e.getMessage(), e);
                ventana_padre.crearAdvertencia("Se finalizará el ticket aunque no se ha podido actualizar el stock, por favor, contacte con el administrador");
            } catch (StockTimeOutException e) {
                log.info("accionProcesaVenta() - ERROR ACTUALIZANDO STOCK. La venta ha sido realizada a pesar del error.");
                log.error("accionProcesaVenta() - ERROR ACTUALIZANDO STOCK EN SISTEMAS PROPIETARIOS DE SUKASA: " + e.getMessage(), e);
                ventana_padre.crearAdvertencia("Se finalizará el ticket pero no se ha podido actualizar el stock por problemas de bloqueo, por favor, contacte con el administrador");
            }

            // Borramos el ticket de sesión
            ticket = null;
            Sesion.borrarTicket();

            if (!resultadoImpresion) {
                log.error("accionProcesaVenta() - HA OCURRIDO UN ERROR AL IMPRIMIR EL TICKET");
                ventana_padre.crearError("Ha ocurrido un error al imprimir el ticket");
            }
        } catch (TicketException ex) {
            log.debug(ex.getMessage());
            ventana_padre.crearError(ex.getLocalizedMessage());
        } catch (CuponException ex) {
            log.debug(ex.getMessage());
            ventana_padre.crearError(ex.getMessage());
        } catch (DocumentoException ex) {
            log.debug(ex.getMessage());
            ventana_padre.crearError("Error creando documento en Base de Datos");
        } catch (Exception ex) {
            accionCancelarPagos();
            log.debug(ex.getMessage(), ex);
            ventana_padre.crearError("No se pudo realizar la operación");
        }
        // En caso de errores, no paramos la venta
        habilitaPanelVentas(false);
        if (PromocionTipoCuponDescuentoAzar.haEmitido) {
            animacionPromocion();
        }
        log.debug("accionProcesaVenta() - Venta completada. Volvemos a pantalla de identificación de clientes...");
        ventana_padre.showView("ident-cliente");
    }

    /**
     * Acciones previas a establecimiento de importes y de la inicialización de
     * pagos
     */
    private void accionesPrevias() {
        // Tarjeta Supermaxi
        if (!isModoLetraCambio()
                && !isModoCreditoDirecto()
                && !isModoAbonoPlan()
                && !ticket.getCliente().isSeleccionadoTarjetaAfiliado()
                && !isModoGiftCard()) {
            JPrincipal.getInstance().crearVentanaLecturaTarjetaSupermMaxi(ticket.getCliente());
        }
    }

    public void deshabilitar_elementos() {
        if (!isModoAbonoMinimo()) {
            t_abono_minimo.setVisible(false);
            lb_abono_minimo.setVisible(false);
        }
    }

    private void habilitaPanelVentas(boolean recalcularVenta) {
        JVentas jv = (JVentas) JPrincipal.getInstance().getView(JPrincipal.VISTA_VENTAS);
        jv.habilita_elementos_formulario();
        removeFunctionKeys(); // Quitamos las funciones de la pantalla de pagos
        jv.cerrarPanelActivo();
        jv.addFunctionKeys();
        if (recalcularVenta) {
            ticket.getPuntosTicket().resetearCanjeo();
            ticket.getTicketPromociones().resetearPromocionesUnitarias();
            ticket.recalcularTotales();
            if (ticket.getTicketPromociones().isFacturaAsociadaDiaSocio()) {
                ticket.getTicketPromociones().preguntarAplicacionDiaSocio();
            }
            jv.refrescaTablaTicket();
        }
        jv.estableceFoco();
    }

    private boolean isHayPagosPorHacer() {
        return (pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    private void repintar(javax.swing.JComponent componente) {
        Rectangle area = componente.getBounds();
        area.x = 0;
        area.y = 0;
        componente.paintImmediately(area);
    }
    //Cupos Virtuales Rd
    List<ValorCupo> cuposVirtuales;
    private ValorCupo cupoVirtualSeleccionado;

    private boolean autorizarPagos() {
        /**
         * Creacion Cupo Virutal Rd
         */
        //creacion Cupo Virutal Rd
        cupoVirtualSeleccionado = null;
        log.debug("autorizarPagos() - Validando autorización de todos los pagos...");
        int i = 0;
        int indiceTramaSuper = 0;
        if (isModoAbonoReservacion()) {
            ticket.getPagos().recalculaPagosBase0();
        } else {
            ticket.recalcularFinalPagadoFinal(true);
        }
        cuposVirtuales = new ArrayList<ValorCupo>();
        while (i < ticket.getPagos().getPagos().size()) {
            Pago p = ticket.getPagos().getPagos().get(i);

            ValorCupo valorObtenido = obtenerCupoVirtual(p);
            if (valorObtenido != null) {
                cuposVirtuales.add(valorObtenido);
            }
            i++;
            indiceTramaSuper++;
            p.setIdTramaSuper("" + indiceTramaSuper);
            try {
                p.setTicket(ticket);
                //Cambiar este método cuando funcione el Servicio Web del Supermaxi.
                p.autorizarPago();
                validacionPlastico(p);
            } catch (AutorizadorException e) {
                if (e instanceof AutorizadorAutomaticoNoPermitidoException) {
                    if (!((PagoCredito) p).isAdmiteAutorizacionManual()) {
                        ventana_padre.crearError("La configuración del medio de pago no admite su autorización ni automática ni manual.");
                        log.debug(" Ingreso por el autorizarPagos en el catch AutorizadorException ");
                        accionCancelarPagos();
                        return false;
                    }

                    //Validacion Para sukocina Tarjeta RD
                    PagoCredito pf = (PagoCredito) p;
                    pf.setTotalPagoAuto(p.getUstedPaga());
                    accionAutorizacionManual((PagoCredito) p, Boolean.FALSE);
                    if (!p.isValidado()) {
                        boolean volver = false;
                        log.debug(" Ingreso por la linea 4082");
                        String mensajeError = accionCancelarPagos();
                        if (mensajeError != null && !mensajeError.isEmpty()) {
                            ventana_padre.crearError(mensajeError);
                        }
                        return false;
                    }
                } else {
                    log.debug("- (Accion Procesar Pagos) - Falló la validación de la tarjeta de crédito");
                    log.debug("- (Accion Procesar Pagos) - Creando ventana de petición de acción para el cajero");
                    int res = 0;
                    //Se Genera Ventana De Error Rd
                    if (cupoVirtualSeleccionado != null) {
                        PagoCredito pf = (PagoCredito) p;
                        pf.setTotalPagoAuto(cupoVirtualSeleccionado.getTotalPago());

                        res = ventana_padre.crearVentanaErrorValidacionTarjeta2(p, e.getMessage(), cupoVirtualSeleccionado.getTotalPago().toString());
                        if (res != 2) {
                            restarvalorValidado(cupoVirtualSeleccionado);
                        }
                    } else {
                        PagoCredito pf = (PagoCredito) p;
                        pf.setTotalPagoAuto(p.getUstedPaga());
                        res = ventana_padre.crearVentanaErrorValidacionTarjeta(p, e.getMessage());
                    }
                    if (res == JErrorValidacionTarjeta.RESP_REINTENTAR) {
                        log.debug("- (Accion Procesar Pagos) - Opción Reintentar: " + res);
                        i--;
                    } else if (res == JErrorValidacionTarjeta.RESP_AUTORIZACION_MANUAL) {
                        log.debug("- (Accion Procesar Pagos) - Opción Pago Manual: " + res);
                        if (e instanceof AutorizadorExcedeCupoException) {
                            accionAutorizacionManual((PagoCredito) p, Boolean.TRUE);
                        } else {
                            accionAutorizacionManual((PagoCredito) p, Boolean.FALSE);
                        }

                        if (!p.isValidado()) {
                            log.debug(" Ingreso por el autorizarPagos en la linea 4113 ");
                            String mensajeError = accionCancelarPagos();
                            if (mensajeError != null && !mensajeError.isEmpty()) {
                                ventana_padre.crearError(mensajeError);
                            }
                            return false;
                        } else {
                            //Validacion Si Cupor Virtual excede el limite Rd
                            if (cupoVirtualSeleccionado != null) {
                                log.debug(" Ingreso por el autorizarPagos en la linea 4122 ");
                                String mensajeError = accionCancelarPagos();
                                if (mensajeError != null && !mensajeError.isEmpty()) {
                                    ventana_padre.crearError(mensajeError);
                                }
                                return false;
                            }
                        }
                    } else if (res == JErrorValidacionTarjeta.RESP_CANCELADO) {
                        log.debug("- (Accion Procesar Pagos) - Opción Cancelado: " + res);
                        log.debug(" Ingreso por el autorizarPagos en la linea 4132 ");
                        String mensajeError = accionCancelarPagos();
                        if (mensajeError != null && !mensajeError.isEmpty()) {
                            ventana_padre.crearError(mensajeError);
                        }
                        return false;
                    }
                }
            } finally {
                refrescarTablaPagos();
            }
        }
        return true;
    }

    private void restarvalorValidado(ValorCupo valor) {
        for (int i = 0; i < cuposVirtuales.size(); i++) {
            if (valor.getNumeroCredito().equals(cuposVirtuales.get(i).getNumeroCredito())) {
                cuposVirtuales.get(i).setTotalPago(cuposVirtuales.get(i).getTotalPago().subtract(valor.getTotalPago()));
            }
        }
    }

    //Validacion Del Plastico si se pasa del limite permitido Rd
    private void validacionPlastico(Pago pag) throws AutorizadorException {

        //cambio RD Para Consultar tarjeta
        // Tarjeta crédito Sukasa 
        if (pag.getMedioPagoActivo().isTarjetaSukasa()) {

            Integer numeroCredito = ((PagoCreditoSK) pag).getPlastico() != null ? ((PagoCreditoSK) pag).getPlastico().getNumeroCredito() : ((TarjetaCreditoSK) ((PagoCreditoSK) pag).getTarjetaCredito()).getPlastico().getNumeroCredito();
            for (int i = 0; i < cuposVirtuales.size(); i++) {
                if (numeroCredito.equals(cuposVirtuales.get(i).getNumeroCredito())) {
                    if (cuposVirtuales.get(i).getCupo().compareTo(pag.getUstedPaga()) == -1) {
                        if (cupoVirtualSeleccionado == null) {
                            cuposVirtuales.get(i).setTotalPago(cuposVirtuales.get(i).getTotalPago().add(pag.getUstedPaga()));
                        }
                        cupoVirtualSeleccionado = cuposVirtuales.get(i);
                        throw new AutorizadorExcedeCupoException("El cupo disponible es menor que el importe que desea pagar.");

                    } else {
                        cuposVirtuales.get(i).setCupo(cuposVirtuales.get(i).getCupo().subtract(pag.getUstedPaga()));
                        cuposVirtuales.get(i).setTotalPago(cuposVirtuales.get(i).getTotalPago().add(pag.getUstedPaga()));
                    }
                    return;
                }
            }
        }

    }
    //Metodo para obtener actual por credito Rd

    private ValorCupo obtenerCupoVirtual(Pago pag) {
        boolean verificar = true;
        ValorCupo valor = new ValorCupo();
        if (pag.getMedioPagoActivo().isTarjetaSukasa()) {
            Integer numeroCredito = ((PagoCreditoSK) pag).getPlastico() != null ? ((PagoCreditoSK) pag).getPlastico().getNumeroCredito() : ((TarjetaCreditoSK) ((PagoCreditoSK) pag).getTarjetaCredito()).getPlastico().getNumeroCredito();
            for (int i = 0; i < cuposVirtuales.size(); i++) {
                if (cuposVirtuales.get(i).getNumeroCredito().equals(numeroCredito)) {
                    verificar = false;
                }
            }
            if (verificar) {
                Connection connSukasa = new Connection();
                try {
                    if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                            && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                        connSukasa.abrirConexion(Database.getConnection());
                    } else {
                        connSukasa.abrirConexion(Database.getConnectionSukasa());
                    }
                    connSukasa.iniciaTransaccion();

                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PagoCreditoSK.class.getName()).log(Level.SEVERE, null, ex);
                }
                CupoVirtualBean cupo = new CupoVirtualBean();
                try {
                    cupo = CupoVirtualDao.consultar(connSukasa, numeroCredito);
                    valor.setNumeroCredito(numeroCredito);
                    valor.setCupo(cupo.getCupo());

                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PagoCreditoSK.class.getName()).log(Level.SEVERE, null, ex);
                }
                return valor;
            }
        }
        return null;
    }

    private void impresionCheque() throws InterruptedException {
        Boolean esPrimeraVez = true;
        for (Pago pago : ticket.getPagos().getPagos()) {
            if (pago.getMedioPagoActivo().isCheque()) {
                if (isModoGiftCard()) {
                    ticket.setCliente(clienteGC);
                }
                try {
                    if (this.ventana_padre.crearVentanaConfirmacion("¿Desea imprimir el cheque por valor de: " + pago.getUstedPaga() + " ? \n Acepte y coloque el cheque por el lado anverso", "Aceptar", "Cancelar")) {
                        PrintServices.getInstance().imprimirCheque(ticket, pago, "ANVERSO", esPrimeraVez);
                        PrintServices.getInstance().imprimirCheque(ticket, pago, "REVERSO", esPrimeraVez);
                        esPrimeraVez = false;

                        while (this.ventana_padre.crearVentanaConfirmacion("¿Desea reimprimir el cheque por valor de: " + pago.getUstedPaga() + " ?", "Sí", "No")) {
                            PrintServices.getInstance().imprimirCheque(ticket, pago, "ANVERSO", esPrimeraVez);
                            PrintServices.getInstance().imprimirCheque(ticket, pago, "REVERSO", esPrimeraVez);
                        }
                    }
                } catch (TicketPrinterException ex) {
                    log.error("impresionCheque() - No se ha podido imprimir el cheque: " + ex.getMessage(), ex);
                    this.ventana_padre.crearError("No se ha podido imprimir el cheque");
                }
            }

        }
    }

    private boolean comprobarPromocionesFinales(TicketPromocionesFiltrosPagos promoFiltroPagos, boolean vieneVenta) {
        log.debug("comprobarPromocionesFinales() - Comprobando si se van a emitir promociones finales");
        for (PromocionTipoCupon promocion : Sesion.promocionesCupones) {
            ConfigEmisionCupones configEmision = promocion.getConfigEmision();
            String promoMontoCupones = configEmision.vaAEmitirCupones(ticket);
            if (promoMontoCupones != null) {
                boolean anular = JPrincipal.getInstance().crearVentanaConfirmacion(promoMontoCupones + "\nPromoción: " + promocion.getDescripcionImpresion() + "\n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar los detalles de la venta (Cancelar)", 80);
                if (!anular) {
                    promoFiltroPagos.reset();
                    // meter promocione en rechazadas para siempre
                    accionVolver(true, false);
                    return false;
                }
            }
        }
        for (PromocionTipoPuntosAcumula promocion : Sesion.promocionesAcumulacionPuntos) {
            String mensaje = promocion.isAplicableMontoMinimo(ticket);
            if (mensaje != null) {
                boolean anular = JPrincipal.getInstance().crearVentanaConfirmacion(mensaje + "\nPromoción: " + promocion.getDescripcionImpresion() + "\n¿Qué desea hacer?\n - Continuar sin promoción (Aceptar)\n - Cambiar los detalles de la venta (Cancelar)", 80);
                if (!anular) {
                    promoFiltroPagos.reset();
                    // meter promocione en rechazadas para siempre
                    accionVolver(true, false);
                    return false;
                }
            }
        }
        if (vieneVenta) {
            log.debug("comprobarPromocionesFinales() - Comprobando si se van a emitir promociones de tipo n cuotas o m meses");
            for (PromocionTipoNCuotasGratis promocion : Sesion.promocionesNCuotas) {
                String mensaje = promocion.isAplicableFormasPago(ticket);
            }
            if (Sesion.getTicket().getTicketPromociones().isMesesGraciaAplicado()) {
                String mensaje = Sesion.promocionMesesGracia.isAplicableFormasPago(ticket);
            }
        }
        return true;
    }

    private void keyTypedPago() {
        try {
            if (t_pago_efectivo_entregado.hasFocus()) {
                if ((pagos.getSaldo().compareTo(BigDecimal.ZERO) > 0 && (!ticket.getPagos().contieneEfectivo() || !l_medio_pago.getText().equals("EFECTIVO"))) || editando) {
                    log.info("Acción realizar pago simple mediante intro en entregado: " + t_pago_efectivo_entregado.getText());

                    if (new BigDecimal(t_pago_efectivo_entregado.getText()).compareTo(Sesion.getDatosConfiguracion().getMaximoPagos()) > 0) {
                        t_pago_efectivo_entregado.setText("");
                        throw new PagoInvalidException("Ha introducido un valor de pago superior al máximo permitido");
                    }
                    if (pagoSimple.requiereAutorizacionMontoMaximo(t_pago_efectivo_total.getText())) {
                        try {
                            String usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, "El valor del pago requiere autorización");
                            ticket.setAutorizadorVenta(usuarioAutorizador);
                        } catch (SinPermisosException ex) {
                            log.debug("No se tienen permisos para seleccionar medio pago con importe superior a monto máximo.");
                            return;
                        }
                    }

                    pagoSimple.establecerEntregado(t_pago_efectivo_entregado.getText());
                    pagoSimple.validar();
                    if (pagoSimple.tieneInformacionExtra() && !pagoSimple.isPagoOtros()) {
                        jInformacionExtraPago1.iniciaFoco();
                        JPrincipal.setPopupActivo(v_informacion_extra_pago);
                        v_informacion_extra_pago.setVisible(true);
                        JPrincipal.setPopupActivo(null);
                    }
                    if (!editando) {
                        ticket.crearNuevaLineaPago(pagoSimple);
                    }
                    ticket.getPagos().recalculaTotales();
                    tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                    setTamanosTabla();
                    refrescarTotales();
                    b_pago_efectivoActionPerformed(null);
                    editando = false;
                } else {
                    if (ticket.getPagos().contieneEfectivo() && l_medio_pago.getText().equals("EFECTIVO")) {
                        log.debug("No se permite la edición de efectivo");
                        ventana_padre.crearAdvertencia("Se ha desactivado la edición automática de efectivo.");
                    } else {
                        log.debug("No se permiten pagos con saldo 0.");
                        ventana_padre.crearAdvertencia("No se permiten pagos con saldo 0.");
                    }
                }
            } else if (t_pago_efectivo_total.hasFocus()) {
                pagoSimple.recalcularFromTotal(t_pago_efectivo_total.getText());
                t_pago_efectivo_usted_paga.requestFocus();
            } else if ((!pagoSimple.getMedioPagoActivo().isAdmiteVuelto() && t_pago_efectivo_usted_paga.hasFocus())
                    || pagoSimple.getMedioPagoActivo().isNotaCredito()
                    || pagoSimple.getMedioPagoActivo().isBonoEfectivo()
                    || pagoSimple.getMedioPagoActivo().isGiftCard()) {
                BigDecimal ustedPagaIntroducido = new BigDecimal(t_pago_efectivo_usted_paga.getText());
                BigDecimal saldoMaximo = ustedPagaIntroducido;
                if (pagoSimple.getMedioPagoActivo().isNotaCredito()) {
                    saldoMaximo = ((PagoNotaCredito) pagoSimple).getSaldoNotaCredito();
                } else if (pagoSimple.getMedioPagoActivo().isBonoEfectivo()) {
                    saldoMaximo = ((PagoBono) pagoSimple).getSaldoBono();
                } else if (pagoSimple.getMedioPagoActivo().isGiftCard()) {
                    saldoMaximo = ((PagoGiftCard) pagoSimple).getSaldoGiftCard();
                }
                if (Numero.isMenor(saldoMaximo, ustedPagaIntroducido)) {
                    t_pago_efectivo_usted_paga.setText(saldoMaximo.toString());
                }
                if (pagoSimple.getMedioPagoActivo().isNotaCredito()) {
                    pagoSimple.recalcularFromUstedPagaNotaCredito(t_pago_efectivo_usted_paga.getText(), ticket.getPagos());
                } else {
                    pagoSimple.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                }
                if (pagoSimple.requiereAutorizacionMontoMaximo(t_pago_efectivo_total.getText())) {
                    try {
                        String usuarioAutorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO, "El valor del pago requiere autorización");
                        ticket.setAutorizadorVenta(usuarioAutorizador);
                    } catch (SinPermisosException ex) {
                        log.debug("No se tienen permisos para seleccionar medio pago con importe superior a monto máximo.");
                        return;
                    }
                }
                pagoSimple.validar();
                if (pagoSimple.tieneInformacionExtra() && !pagoSimple.isPagoOtros()) {
                    jInformacionExtraPago1.iniciaFoco();
                    JPrincipal.setPopupActivo(v_informacion_extra_pago);
                    v_informacion_extra_pago.setVisible(true);
                    JPrincipal.setPopupActivo(null);
                }
                if (!editando) {
                    ticket.crearNuevaLineaPago(pagoSimple);
                }
                ticket.getPagos().recalculaTotales();
                tb_pagos.setModel(new PagosTableModel(ticket.getPagos()));
                setTamanosTabla();
                refrescarTotales();
                b_pago_efectivoActionPerformed(null);
                editando = false;

            } else if (t_pago_efectivo_usted_paga.hasFocus()) {
                pagoSimple.recalcularFromUstedPaga(t_pago_efectivo_usted_paga.getText());
                t_pago_efectivo_entregado.requestFocus();
            }

        } catch (ImporteInvalidoException e) {
            ventana_padre.crearError(e.getMessage());
        } catch (PagoInvalidException e) {
            log.debug("el pago no es valido");
            ventana_padre.crearError(e.getMessage());
        } catch (PagoInvalidNCException e) {
            log.debug("el pago no es valido Nota Credito");
            accionBorrarPago();
            ventana_padre.crearError(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ventana_padre.crearError("No se puede realizar el pago");
        }
        refrescarPagosEfectivos();
        ticket.getPagos().recalculaTotales();
        refrescarTablaPagos();
        refrescarTotales();
    }

}
