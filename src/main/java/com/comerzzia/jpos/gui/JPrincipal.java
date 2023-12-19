/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JPrincipal.java
 *
 * Created on 27-jun-2011, 13:04:59
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.entity.db.CabPrefactura;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.bancos.JLecturaTarjetaAfiliado;
import com.comerzzia.jpos.gui.bonoEfectivo.JConsultaBonoEfectivo;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.gui.cajas.JGestionCajas;
import com.comerzzia.jpos.gui.cajas.JCerrarCaja;
import com.comerzzia.jpos.gui.cajas.JRecuentoCaja;
import com.comerzzia.jpos.gui.clientes.JBuscarClientes;
import com.comerzzia.jpos.gui.clientes.JMostrarCliente;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.cotizaciones.JBuscarCotizaciones;
import com.comerzzia.jpos.gui.devoluciones.JDevolucionVentas;
import com.comerzzia.jpos.gui.garantia.JGarantia;
import com.comerzzia.jpos.gui.bonos.JConsultaBonosSupermaxi;
import com.comerzzia.jpos.gui.devoluciones.JDevolucionVentasOtroLocal;
import com.comerzzia.jpos.gui.giftcard.JGiftCard;
import com.comerzzia.jpos.gui.guiasremision.JNuevaGuiaRemision;
import com.comerzzia.jpos.gui.guiasremision.JNuevaGuiaRemision2;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesCliente;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesVentas;
import com.comerzzia.jpos.gui.reservaciones.JBuscarReservaciones;
import com.comerzzia.jpos.gui.reservaciones.JMostrarReservacion;
import com.comerzzia.jpos.gui.reservaciones.JMostrarReservacionCanBaby;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesDatosAdicionales;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesVentasCanBaby;
import com.comerzzia.jpos.gui.reservaciones.plannovios.JBuscarPlanNovios;
import com.comerzzia.jpos.gui.reservaciones.plannovios.JDatosPlanNovio;
import com.comerzzia.jpos.gui.reservaciones.plannovios.JMostrarPlanNovio;
import com.comerzzia.jpos.gui.sukupo.JConsultaSukupon;
import com.comerzzia.jpos.gui.ventas.JBuscarPreFactura;
import com.comerzzia.jpos.gui.ventas.JMostrarPreFactura;
import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.login.InvalidLoginException;
import com.comerzzia.jpos.servicios.login.LoginException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.ServicioCodigoOTP;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.enums.EnumEstadosValidacionOtp;
import com.comerzzia.jpos.util.thread.AutorizacionCupoThread;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.Contador;
import es.mpsistemas.util.log.Logger;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author MGRI
 */
public class JPrincipal extends JPanel implements IVista, IManejadorErrores {

    private static final Logger log = Logger.getMLogger(JPrincipal.class);
    public static String VISTA_INICIO = "ident-cliente";
    public static String VISTA_LOGIN = "login";
    public static String VISTA_GIFTCARD = "giftcard";
    public static String VISTA_BONOSUPER = "bono-super";
    public static String VISTA_BONOEFECTIVO = "bono-efectivo";
    public static String VISTA_SUKUPON = "sukupon";

    public static String VISTA_VENTAS = "ventas";
    /*
    public static String VISTA_DEVOLUCION = "devolucion";
    public static String VISTA_ABONOS = "abonos";
     */
    private static String msgPreguntaSupermaxi;
    // Solución para no pasar mas ventana padre como parametro a las ventanas
    // La instancia será establecida al iniciar JPrincipal por primera vez
    public static JPrincipal instancia = null;
    public boolean exitoDesbloqueo = false;
    public static JDialog bloqueador = null;
    JError new_p_error = null;
    private static boolean recuentoVisible = false;
    private ImageIcon iconoTransparente = new ImageIcon(this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif"));
    private static JDialog popupActivo;
    private static JPanel panelActivo;

    /**
     * @return the vistaActual
     */
    public static JPanel getVistaActual() {
        return vistaActual;
    }

    public static String getVistaActualS() {
        return vistaActualS;
    }

    public static void setVistaActualS(String aVistaActualS) {
        vistaActualS = aVistaActualS;
    }

    public static JPrincipal getInstance() {
        return instancia;
    }

    public static void setInstance(JPrincipal inst) {
        instancia = inst;
    }
    /**
     * Creates new form JPrincipal
     */
    //private JPInfoApp m_pinfoapp = null;
    Map<String, JPanel> vistasActivas = new HashMap();
    // control de vistas activas
    String vistaProcesoVenta = null;
    String vistaProcesoNuevaReservacion = null;
    JPie p_pie = null;
    // ventana de autorizaciones
    JSolicitarAutorizacionVentana v_dialog_autorizar;
    JLecturaTarjetaAfiliado v_lectura_tarjeta_afiliadoEmp;
    private static JPanel vistaActual;
    private static String vistaActualS;
    List vistasGestionDeCajas;
    JVentas jp_ventas;

    public JPrincipal() {
        log.info("CONSTRUCTOR: Se crea JPrincipal");
        //logger.log(Level.INFO, "Inicializada la aplicación");

        initComponents();

        //Guardamos la instancia
        JPrincipal.setInstance(this);

        vistasGestionDeCajas = new LinkedList();

        jf_informacion.setIconImage(iconoTransparente.getImage());
        dialog_confirm.setIconImage(iconoTransparente.getImage());
        dialog_error.setIconImage(iconoTransparente.getImage());
        v_menu.setIconImage(iconoTransparente.getImage());
        v_devolucion.setIconImage(iconoTransparente.getImage());
        v_error_validar_tarjeta.setIconImage(iconoTransparente.getImage());
        v_solicitar_identificacion.setIconImage(iconoTransparente.getImage());

        // Componentes custom
        v_dialog_autorizar = new JSolicitarAutorizacionVentana();
        v_dialog_autorizar.setAlwaysOnTop(true);
        preparaDimensionVentanaAutorizacion();
        v_dialog_autorizar.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_dialog_autorizar.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_dialog_autorizar.setResizable(false);

        // Elementos de ventana que se vana a añadir al CardLayout.        
        JLogin login_component = new JLogin(this);
        log.debug("Ventana JLogin correcta.");
        jp_ventas = new JVentas(this);
        log.debug("Ventana JVentas correcta.");
        JIdentCliente m_inicio_ident_clientes = new JIdentCliente(this);
        log.debug("Ventana JIdentCliente correcta.");
        JPerfilUsuario jp_perfil = new JPerfilUsuario(this);
        log.debug("Ventana JPerfilUsuario correcta.");
        JGestionCajas jp_movimientos = new JGestionCajas(this);
        log.debug("Ventana JGestionCajas correcta.");
        JGiftCard jp_giftcard = new JGiftCard(this);
        log.debug("Ventana JGiftCard correcta.");
        JConsultaBonosSupermaxi jp_bonosSuper = new JConsultaBonosSupermaxi(this);
        log.debug("Ventana JConsultaBonosSupermaxi correcta.");
        JConsultaBonoEfectivo jp_bonosEfectivo = new JConsultaBonoEfectivo();
        log.debug("Ventana JConsultaBonoEfectivo correcta.");
        JConsultaSukupon jp_sukupon = new JConsultaSukupon(this);
        log.debug("Ventana JConsultaSukupon correcta.");

        //Definición del cardlayout y pantallas que en el se van a incluir. (Cada una representa una vista de la aplicación)
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());

        // identificación de cliente
        jPanel_centro.add(m_inicio_ident_clientes, "ident-cliente");
        vistasActivas.put("ident-cliente", m_inicio_ident_clientes);

        // login
        jPanel_centro.add(login_component, "login");
        vistasActivas.put("login", login_component);

        // ventas
        jPanel_centro.add(jp_ventas, VISTA_VENTAS);
        vistasActivas.put(VISTA_VENTAS, jp_ventas);

        // articulos
        jPanel_centro.add(jp_perfil, "menu-perfil");
        vistasActivas.put("menu-perfil", jp_perfil);

        //movimientos de cajas
        jPanel_centro.add(jp_movimientos, "movimientos");
        vistasActivas.put("movimientos", jp_movimientos);
        vistasGestionDeCajas.add(jp_movimientos);

        // giftcard
        jPanel_centro.add(jp_giftcard, VISTA_GIFTCARD);
        vistasActivas.put(VISTA_GIFTCARD, jp_giftcard);

        // bonos Supermaxi
        jPanel_centro.add(jp_bonosSuper, VISTA_BONOSUPER);
        vistasActivas.put(VISTA_BONOSUPER, jp_bonosSuper);

        // bonos Efectivo
        jPanel_centro.add(jp_bonosEfectivo, VISTA_BONOEFECTIVO);
        vistasActivas.put(VISTA_BONOEFECTIVO, jp_bonosEfectivo);

        // Sukupon
        jPanel_centro.add(jp_sukupon, VISTA_SUKUPON);
        vistasActivas.put(VISTA_SUKUPON, jp_sukupon);

        // Iniciamos la ventana de Recuentos
        p_recuento_caja.inicializaPanel();
        p_recuento_caja.setVentana_padre(this);
        p_recuento_caja.setContenedor(v_recuento_caja);
        v_recuento_caja.setIconImage(iconoTransparente.getImage());

        p_error_validar_tarjeta.setContenedor(v_error_validar_tarjeta);

        // Ventana de bancos
        v_bancos.setLocationRelativeTo(null);
        v_bancos.setIconImage(iconoTransparente.getImage());
        p_bancos.setContenedor(v_bancos);
        p_bancos.setVentana_padre(this);

        // Ventana de bancos
        v_autorizacion_tarjeta.setLocationRelativeTo(null);
        v_autorizacion_tarjeta.setIconImage(iconoTransparente.getImage());
        p_autorizacion_tarjeta.setContenedor(v_autorizacion_tarjeta);
        p_autorizacion_tarjeta.setVentana_padre(this);

        // Ventana de autorización de tarjeta manual
        v_autorizacion_num_tarjeta.setLocationRelativeTo(null);
        v_autorizacion_num_tarjeta.setIconImage(iconoTransparente.getImage());
        p_autorizacion_num_tarjeta.setContenedor(v_autorizacion_num_tarjeta);
        p_autorizacion_num_tarjeta.setVentana_padre(this);
        //RD Autorizacion Manual Credito Directo 
        v_autorizacion_tarjeta_credito_directo.setLocationRelativeTo(null);
        v_autorizacion_tarjeta_credito_directo.setIconImage(iconoTransparente.getImage());
        p_autorizacion_tarjeta_credito_directo.setContenedor(v_autorizacion_tarjeta_credito_directo);
        p_autorizacion_tarjeta_credito_directo.setVentana_padre(this);

        //Autorizacion consumo excede el saldo
        //RD Autorizacion Manual Credito Directo 
        v_aut_credito_directo_excede_cupo.setLocationRelativeTo(null);
        v_aut_credito_directo_excede_cupo.setIconImage(iconoTransparente.getImage());
        p_aut_credito_directo_excede_cupo.setContenedor(v_aut_credito_directo_excede_cupo);
        p_aut_credito_directo_excede_cupo.setVentana_padre(this);

        // Lectura manual de tarjetas
        v_lectura_tarjeta_manual.setLocationRelativeTo(null);
        v_lectura_tarjeta_manual.setLocation(485, 405);
        v_lectura_tarjeta_manual.setIconImage(iconoTransparente.getImage());
        //v_lectura_tarjeta_manual.setUndecorated(true);               
        p_lectura_tarjeta_manual.setContenedor(v_lectura_tarjeta_manual);
        p_lectura_tarjeta_manual.setVentana_padre(this);
        // Lectura tarjetas afilados
        v_lectura_tarjeta_afiliado.setLocationRelativeTo(null);
        v_lectura_tarjeta_afiliado.setLocation(420, 300);
        v_lectura_tarjeta_afiliado.setIconImage(iconoTransparente.getImage());
        p_lectura_tarjeta_afiliado.setContenedor(v_lectura_tarjeta_afiliado);
        p_lectura_tarjeta_afiliado.setVentana_padre(this);

        // Enviar mail en Cotización
        v_envio_mail.setLocationRelativeTo(null);
        v_envio_mail.setIconImage(iconoTransparente.getImage());
        p_envia_mail.setContenedor(v_envio_mail);
        p_envia_mail.setVentana_padre(this);

        // Menú de tarjetas
        v_menu_tarjetas.setLocationRelativeTo(null);
        v_menu_tarjetas.setIconImage(iconoTransparente.getImage());
        p_menu_tarjetas.setContenedor(v_menu_tarjetas);
        p_menu_tarjetas.setVentana_padre(this);

        // Lectura de tarjetas
        v_lectura_tarjeta.setLocationRelativeTo(null);
        v_lectura_tarjeta.setIconImage(iconoTransparente.getImage());
        p_lectura_tarjeta.setContenedor(v_lectura_tarjeta);
        p_lectura_tarjeta.setVentana_padre(this);

        // Lectura de Valor
        v_ingresar_valor.setLocationRelativeTo(null);
        v_ingresar_valor.setIconImage(iconoTransparente.getImage());
        p_ingresar_valor.setContenedor(v_ingresar_valor);
        p_ingresar_valor.setVentana_padre(this);

        //RD  Campos Para Garantia Extendida Codigo Empleado
        v_ingresarCodEmple.setLocationRelativeTo(null);
        v_ingresarCodEmple.setIconImage(iconoTransparente.getImage());
        p_ingresar_cod_emple.setContenedor(v_ingresarCodEmple);
        p_ingresar_cod_emple.setVentana_padre(this);

        //RD Campos Para Selecion de Promocion 
        v_ingresarPromocion.setLocationRelativeTo(null);
        v_ingresarPromocion.setIconImage(iconoTransparente.getImage());
        p_ingresar_promocion.setContenedor(v_ingresarPromocion);
        p_ingresar_promocion.setVentana_padre(this);
        // Garantia
        v_garantia.setLocationRelativeTo(null);
        v_garantia.setIconImage(iconoTransparente.getImage());
        p_garantia.setContenedor(v_garantia);
        p_garantia.setVentana_padre(this);

        //Ayuda
        v_ayuda.setIconImage(iconoTransparente.getImage());
        p_ayuda.setContenedor(v_ayuda);

        //Observaciones
        v_observaciones.setIconImage(iconoTransparente.getImage());
        p_observaciones.setContenedor(v_observaciones);

        //Reimpresiones
        v_reimpresion.setIconImage(iconoTransparente.getImage());
        p_reimpresion.setContenedor(v_reimpresion);

        //Sukupon
        v_buscar_sukupon.setIconImage(iconoTransparente.getImage());
        p_buscar_sukupon.setContenedor(v_buscar_sukupon);

        //Sukupon
        v_buscar_bonos.setIconImage(iconoTransparente.getImage());
        p_buscar_bonos.setContenedor(v_buscar_bonos);

        //Flash de Ventas
        v_flash_ventas.setIconImage(iconoTransparente.getImage());
        p_flash_ventas.setContenedor(v_flash_ventas);

        //Envío a Domicilio
        v_envio_domicilio.setIconImage(iconoTransparente.getImage());
        p_envio_domicilio.setContenedor(v_envio_domicilio);

        // Facturacion
        v_facturacion.setLocationRelativeTo(null);
        v_facturacion.setIconImage(iconoTransparente.getImage());
        p_facturacion.setVentana_padre(this);
        p_facturacion.setContenedor(v_facturacion);

        v_autentificarCliente.setIconImage(iconoTransparente.getImage());
        p_autentificarCliente.setContenedor(v_autentificarCliente);
        v_autentificarCliente.setLocationRelativeTo(null);

        v_tarjeta_supermaxi.setIconImage(iconoTransparente.getImage());
        p_tarjeta_supermaxi.setContenedor(v_tarjeta_supermaxi);
        v_tarjeta_supermaxi.setLocationRelativeTo(null);
        v_tarjeta_supermaxi.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_tarjeta_supermaxi.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);

        v_solicitar_identificacion.setLocationRelativeTo(null);

        // Menú de identificación de documentos
        v_lectura_factura.setLocationRelativeTo(null);
        v_lectura_factura.setIconImage(iconoTransparente.getImage());
        p_lectura_factura.setContenedor(v_lectura_factura);

        // Iniciamos la ventana de anulaciones
        v_anulaciones.setIconImage(iconoTransparente.getImage());
        p_anulaciones.setContenedor(v_anulaciones);
        p_anulaciones.setVentana_padre(this);

        //Introduccion Manual de GiftCard
        v_lectura_giftcard.setIconImage(iconoTransparente.getImage());
        p_lectura_giftcard.setContenedor(v_lectura_giftcard);
        v_lectura_giftcard.setLocationRelativeTo(null);
        v_lectura_giftcard.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_lectura_giftcard.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);

        // Codigo OTP
        v_ingresarCodOTP.setLocationRelativeTo(null);
        v_ingresarCodOTP.setIconImage(iconoTransparente.getImage());
        p_ingresar_cod_otp.setContenedor(v_ingresarCodOTP);
        p_ingresar_cod_otp.setVentana_padre(this);

        log.debug("Ventanas modales cargadas.");

        iniciaVista();
        p_pie.setVisible(true);
        p_error.setContenedor(dialog_error);
        // Muestra la vista de login
        log.debug("Mostrando login.");
        showView("login");

        //Imagen de fondo de panel de devoluciones
        p_devolucion.setImagenFondo(Variables.getVariable(Variables.POS_UI_SKIN), "devoluciones.png");

        // Modificación para saber cuales son las vistas de gestión de cajas
        Sesion.setVentanaPadre(this);

        if (Sesion.isSukasa()) {
            msgPreguntaSupermaxi = "¿Tiene crédito directo sukasa o tarjeta supermaxi?";
        } else {
            msgPreguntaSupermaxi = "¿Tiene tarjeta supermaxi?";
        }
        log.debug("JPrincipal instanciado");
    }

    /**
     * Función que añade una vista si no esta ya dentro de las vistas
     * disponibles
     *
     * @param panel
     * @param nombrevista
     */
    public void addView(IVista panel, String nombrevista) {
        log.info("VISTA: Añadir " + nombrevista);
        if (!vistasActivas.containsKey(nombrevista)) {
            jPanel_centro.add((Component) panel, nombrevista);
            vistasActivas.put(nombrevista, (JPanel) panel);
        }
    }

    /**
     * Función que añade una vista si no esta o la cambia si está
     *
     * @param panel
     * @param nombrevista
     */
    public void addOrSwitchView(IVista panel, String nombrevista) {
        log.info("VISTA: Añadir o Actualizar Vista " + nombrevista);
        if (vistasActivas.containsKey(nombrevista)) {
            jPanel_centro.remove(vistasActivas.get(nombrevista));
            JPanel panelaborrar = vistasActivas.get(nombrevista);
            if (panelaborrar instanceof JMostrarReservacion) {
                ((JMostrarReservacion) panelaborrar).setVisible(false);
                ((JMostrarReservacion) panelaborrar).validate();
                ((JMostrarReservacion) panelaborrar).dispose();
            } else if (panelaborrar instanceof JBuscarReservaciones) {
                ((JBuscarReservaciones) panelaborrar).setVisible(false);
                ((JBuscarReservaciones) panelaborrar).validate();
                ((JBuscarReservaciones) panelaborrar).dispose();
            }
            vistasActivas.remove(nombrevista);
            panelaborrar.removeAll();
            panelaborrar = null;

        }
        jPanel_centro.add((Component) panel, nombrevista);
        vistasActivas.put(nombrevista, (JPanel) panel);

    }

    /**
     * Función que añade la vista de cierre de caja
     *
     */
    public void addCierreCajaView() {
        if (!vistasActivas.containsKey("cierre_caja")) {
            log.info("Se añade La vista cierre-caja ");
            JCerrarCaja cerrar_caja = new JCerrarCaja(this);
            addView(cerrar_caja, "cierre_caja");
            vistasGestionDeCajas.add(cerrar_caja);
        }
    }

    public static JPanel getPanelActivo() {
        return panelActivo;
    }

    public static void setPanelActivo(JPanel panelActivo) {
        JPrincipal.panelActivo = panelActivo;
    }

    public void addRecuentoCajaView() {
        if (!vistasActivas.containsKey("recuento-caja")) {
            log.info("Se añade La vista recuento-caja ");
            JRecuentoCaja recuento_caja = new JRecuentoCaja(this);
            addView(recuento_caja, "recuento-caja");
        }
    }

    /**
     * Funciones que añaden las vistas de reservaciones *
     */
//    public void addReservacionesClienteView() {
//        if (!vistasActivas.containsKey("reservaciones-cliente")) {
//            log.info("Se añade La vista reservaciones-cliente " );
//            JReservacionesCliente recuento_caja = new JReservacionesCliente(this);
//            addView(recuento_caja, "reservaciones-cliente");
//        }
//    }
    public void resetReservacionesClienteView() {
        if (!vistasActivas.containsKey("reservaciones-cliente")) {
            log.info("Se resetea La vista reservaciones-cliente ");
            JReservacionesCliente res_cliente = new JReservacionesCliente(this, "");
            addOrSwitchView(res_cliente, "reservaciones-cliente");
            vistaProcesoNuevaReservacion = "reservaciones-cliente";
            addView(res_cliente, "reservaciones-cliente");
        }
    }

    public void irReservacionesClienteView(String tipoAcceso) {
        log.info("Se añade La vista reservaciones-cliente ");
        JReservacionesCliente recuento_caja = new JReservacionesCliente(this, tipoAcceso);
        addOrSwitchView(recuento_caja, "reservaciones-cliente");
        vistaProcesoNuevaReservacion = "reservaciones-cliente";
        showView("reservaciones-cliente");
    }

    public void irReservacionesDatosAdicionalesView(JPrincipal ventana_padre, Cliente clienteConsultado, Vendedor vendedor, ReservaTiposBean tipoReserva) {
        log.info("Se añade La vista reservaciones-datos-adicionales ");
        JReservacionesDatosAdicionales recuento_caja = new JReservacionesDatosAdicionales(ventana_padre, clienteConsultado, vendedor, tipoReserva);
        addOrSwitchView(recuento_caja, "reservaciones-datos-adicionales");
        showView("reservaciones-datos-adicionales");
    }

    public void irBuscarReservaciones() {
        log.info("Se añade La vista buscar_reservaciones ");
        JBuscarReservaciones buscar_reservaciones = new JBuscarReservaciones(this);
        addOrSwitchView(buscar_reservaciones, "buscar_reservaciones");
        showView("buscar_reservaciones");

    }

    public void irPagoArticulos(Reservacion reserva, int modo) {
        log.info("Se añade La vista reservaciones-venta-art ");
        JReservacionesVentasCanBaby venta_art_reservaciones = new JReservacionesVentasCanBaby(this, reserva, modo);
        addOrSwitchView(venta_art_reservaciones, "reservaciones-venta-art");
        showView("reservaciones-venta-art");
    }

    public void irPagoArticulos(PlanNovioOBJ planNovio, Cliente cliente, int modo) {
        log.info("irPagoArticulos() desde plan novio  ");
        JReservacionesVentasCanBaby venta_art_reservaciones = new JReservacionesVentasCanBaby(this, planNovio, cliente, modo);
        addOrSwitchView(venta_art_reservaciones, "reservaciones-venta-art");
        showView("reservaciones-venta-art");
    }

    public void irPagoArticulos(CotizacionBean cotizacion, Cliente clienteConsultado, Vendedor vendedor, int modo) {
        log.info("irPagoArticulos() desde cotizaciones ");
        JReservacionesVentasCanBaby venta_art_reservaciones = new JReservacionesVentasCanBaby(this, cotizacion, clienteConsultado, vendedor, modo);
        addOrSwitchView(venta_art_reservaciones, "reservaciones-venta-art");
        showView("reservaciones-venta-art");
    }

    public void irBuscarClientes() {
        log.info("Se añade La vista buscar_clientes ");
        JBuscarClientes buscar_cliente = new JBuscarClientes(this);
        addOrSwitchView(buscar_cliente, "buscar_clientes");
        showView("buscar_clientes");
    }

    public void irMostrarCliente(Cliente cliente) {
        log.info("Se añade La vista mostrar el cliente ");
        JMostrarCliente mostrar_clientes = new JMostrarCliente(cliente);
        addOrSwitchView(mostrar_clientes, "mostrar_cliente");
        showView("mostrar_cliente");
    }

    public void irBuscarCotizacion() {
        log.info("Se añade La vista buscar_cotizaciones ");
        JBuscarCotizaciones buscar_cotizaciones = new JBuscarCotizaciones(this);
        addOrSwitchView(buscar_cotizaciones, "buscar_cotizaciones");
        showView("buscar_cotizaciones");
    }

    public void addReservacionesVentaView() {
        log.info("Se añade La vista reservaciones-venta ");
        JReservacionesVentas resVentas = new JReservacionesVentas(this);
        addOrSwitchView(resVentas, "reservaciones-venta");
        //addView(recuento_caja, "reservaciones-venta");        
    }

    public void addRecuperarReservacionesVentaView() {
        log.info("Se añade La vista reservaciones-venta ");
        JReservacionesVentas resVentas = new JReservacionesVentas(this, true);
        addOrSwitchView(resVentas, "reservaciones-venta");
        //addView(recuento_caja, "reservaciones-venta");        
    }

    public void showViewMenuReservaciones() {
        log.info("CAMBIO_VISTA: Acceso a Reservaciones ");
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistaProcesoNuevaReservacion != null) {
            if (vistaProcesoNuevaReservacion.equals("reservaciones-cliente")) {
                showView("reservaciones-cliente");
                log.info("CAMBIO_VISTA: a reservaciones-cliene");
            } else {
                cl.show(jPanel_centro, vistaProcesoNuevaReservacion);
                if (vistasActivas.get(vistaProcesoNuevaReservacion) != null) {
                    log.info("CAMBIO_VISTA: a " + vistaProcesoNuevaReservacion);
                    IVista ivista = (IVista) vistasActivas.get(vistaProcesoNuevaReservacion);
                    ivista.iniciaFoco();
                }
            }
        } else {
            log.info("CAMBIO_VISTA: Nueva Reservación (Por Defecto) ");
            irReservacionesClienteView("");
            showView("reservaciones-cliente");
        }
    }

    // Añadir vista de Devoluciones
    public void addDevolucionesVenta() {
        if (!vistasActivas.containsKey("devoluciones-venta")) {
            log.info("Se añade la vista Devoluciones-venta");
            JDevolucionVentas devolucion_venta = new JDevolucionVentas(this);
            addView(devolucion_venta, "devoluciones-venta");
        }
    }

    // Añadir vista de Devoluciones otro local
    public void addDevolucionesVentaOtroLocal() {
        if (!vistasActivas.containsKey("devoluciones-venta-otro-local")) {
            log.info("Se añade la vista Devoluciones-venta-otro-local");
            JDevolucionVentasOtroLocal devolucion_venta_otro_local = new JDevolucionVentasOtroLocal(this);
            addView(devolucion_venta_otro_local, "devoluciones-venta-otro-local");
        }
    }

    public boolean isCajaView() {
        boolean res = false;

        if (getVistaActual() == null) {
            res = true;
        } else {
            for (Object jc : vistasGestionDeCajas) {
                if (((JComponent) getVistaActual()).equals((JComponent) jc)) {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * para cambiar de vista
     *
     * @param view
     */
    public void showView(String view) {
        log.info("CAMBIO_VISTA - showView :" + view);
        Contador.setContador(0);
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistasActivas.containsKey(view)) {
            cl.show(jPanel_centro, view);
            if (vistasActivas.get(view) != null) {
                vistaActual = vistasActivas.get(view);
                vistaActualS = view;
                IVista ivista = (IVista) vistasActivas.get(view);
                ivista.iniciaVista();
            } else {
                log.error("ERROR CAMBIO_VISTA: El panel de la vista " + view + " a la que se intenta cambiar no existe ");
            }
        } else {
            log.error("ERROR CAMBIO_VISTA: La vista " + view + " a la que se intenta cambiar no existe ");
            crearError("No pudo cargarse la vista");
        }
    }

    public static void repintarFondo() {
        if (getVistaActual() instanceof JIdentCliente) {
            ((JPanelImagenFondo) getVistaActual()).setRepintarImagenFondo(true);
        }
    }

    /**
     * Retoma la vista de proceso de ventas activa sin reiniciar la vista
     */
    public void showViewMenuVentas() {
        log.info("CAMBIO_VISTA - showViewMenuVentas");
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistaProcesoVenta != null) {
            if (vistaProcesoVenta.equals("cierre-caja")) {
                log.info("CAMBIO_VISTA A ident-cliente");
                showView("ident-cliente");
            } else {
                cl.show(jPanel_centro, vistaProcesoVenta);
                if (vistasActivas.get(vistaProcesoVenta) != null) {
                    log.info("CAMBIO_VISTA A " + vistaProcesoVenta);
                    IVista ivista = (IVista) vistasActivas.get(vistaProcesoVenta);
                    ivista.iniciaVista();
                }
            }
        } else {
            log.info("CAMBIO_VISTA A ident-cliente (Por Defecto)");
            showView("ident-cliente");
        }
    }

    /**
     * Solución al cambio de destino de navegación al cerrar una caja con una
     * venta activa
     */
    public void initViewMenuVentas() {
        vistaProcesoVenta = "cierre-caja";
        log.info("VISTA: Visa actual de proceso de ventas cambiado a " + vistaProcesoVenta);
    }

    public void stateChanged(ChangeEvent evt) {
        log.info("Ejecutada Accion" + evt.toString());
    }

    public void actionPerformed(ActionEvent evt) {
        log.info("Ejecutada Accion" + evt.getActionCommand());

    }

    /* Muestra el pie de la aplicación tras el login*/
    public void mostrarPie() {
        log.info("VISTA: Añadiendo Pié");
        p_pie.setVisible(true);
        p_pie.setDatosAutenticacion();

    }

    private void showLogin() {
        log.info("Mostrando pantalla de Login");
        // podemos preparar un listado seleccionable de gente que puede entrar
        showView("login");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jf_informacion = new javax.swing.JFrame();
        jLabel25 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        dialog_error = new javax.swing.JDialog();
        p_error = new com.comerzzia.jpos.gui.JError();
        v_menu = new javax.swing.JDialog();
        jMenu1 = new JMenu(this,v_menu);
        dialog_confirm = new javax.swing.JDialog();
        p_confirmacion = new com.comerzzia.jpos.gui.JConfirmacion();
        v_devolucion = new javax.swing.JDialog();
        p_devolucion = new com.comerzzia.jpos.gui.devoluciones.JDevolucion();
        v_recuento_caja = new javax.swing.JDialog();
        p_recuento_caja = new com.comerzzia.jpos.gui.cajas.JRecuentoCaja();
        v_contadores = new javax.swing.JDialog();
        p_contadores = new com.comerzzia.jpos.gui.respaldo.JContadores();
        v_error_validar_tarjeta = new javax.swing.JDialog();
        p_error_validar_tarjeta = new com.comerzzia.jpos.gui.ventas.JErrorValidacionTarjeta();
        v_bancos = new javax.swing.JDialog();
        p_bancos = new com.comerzzia.jpos.gui.bancos.JBancos();
        v_autorizacion_tarjeta = new javax.swing.JDialog();
        p_autorizacion_tarjeta = new com.comerzzia.jpos.gui.bancos.JAutorizacionTarjeta();
        v_lectura_tarjeta_manual = new javax.swing.JDialog();
        p_lectura_tarjeta_manual = new com.comerzzia.jpos.gui.bancos.JLecturaTarjetaManual();
        v_menu_tarjetas = new javax.swing.JDialog();
        p_menu_tarjetas = new com.comerzzia.jpos.gui.credito.JOperacionesTarjeta();
        v_lectura_tarjeta = new javax.swing.JDialog();
        p_lectura_tarjeta = new com.comerzzia.jpos.gui.JLecturaTarjeta();
        v_solicitar_identificacion = new javax.swing.JDialog();
        p_solicitar_informacion = new javax.swing.JPanel();
        l_error1 = new javax.swing.JLabel();
        l_mensaje = new javax.swing.JLabel();
        l_error3 = new javax.swing.JLabel();
        i_error = new javax.swing.JLabel();
        t_usuario = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_error2 = new javax.swing.JLabel();
        b_ok = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        l_error = new javax.swing.JLabel();
        t_password = new com.comerzzia.jpos.gui.components.form.JPasswordFieldForm();
        v_ingresar_valor = new javax.swing.JDialog();
        p_ingresar_valor = new com.comerzzia.jpos.gui.JIngresarValor();
        v_introducir_importe = new javax.swing.JDialog();
        v_garantia = new javax.swing.JDialog();
        p_garantia = new com.comerzzia.jpos.gui.garantia.JGarantia();
        v_facturacion = new javax.swing.JDialog();
        p_facturacion = new com.comerzzia.jpos.gui.JFacturacion();
        v_autentificarCliente = new javax.swing.JDialog();
        p_autentificarCliente = new com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente();
        v_tarjeta_supermaxi = new javax.swing.JDialog();
        p_tarjeta_supermaxi = new com.comerzzia.jpos.gui.supermaxi.JTarjetaSuperMaxi();
        v_lectura_factura = new javax.swing.JDialog();
        p_lectura_factura = new com.comerzzia.jpos.gui.JIdPanel();
        v_ayuda = new javax.swing.JDialog();
        p_ayuda = new com.comerzzia.jpos.gui.JAyuda();
        v_observaciones = new javax.swing.JDialog();
        p_observaciones = new com.comerzzia.jpos.gui.JObservaciones();
        v_reimpresion = new javax.swing.JDialog();
        p_reimpresion = new com.comerzzia.jpos.gui.reimpresion.JReimpresion();
        v_envio_domicilio = new javax.swing.JDialog();
        p_envio_domicilio = new com.comerzzia.jpos.gui.guiasremision.JEnvioDomicilio();
        v_flash_ventas = new javax.swing.JDialog();
        p_flash_ventas = new com.comerzzia.jpos.gui.flashventas.JElegirFlash();
        v_anulaciones = new javax.swing.JDialog();
        p_anulaciones = new com.comerzzia.jpos.gui.cajas.anulaciones.JAnulaciones();
        v_lectura_giftcard = new javax.swing.JDialog();
        p_lectura_giftcard = new com.comerzzia.jpos.gui.giftcard.JIntroducirGiftCard();
        v_autorizacion_num_tarjeta = new javax.swing.JDialog();
        p_autorizacion_num_tarjeta = new com.comerzzia.jpos.gui.bancos.JAutorizacionNumTarjeta();
        v_autorizacion_tarjeta_credito_directo = new javax.swing.JDialog();
        p_autorizacion_tarjeta_credito_directo = new com.comerzzia.jpos.gui.bancos.JAutorizacionTarjetaCreditoDirecto();
        v_ingresarCodEmple = new javax.swing.JDialog();
        p_ingresar_cod_emple = new com.comerzzia.jpos.gui.JIngresarGarantia();
        v_ingresarPromocion = new javax.swing.JDialog();
        p_ingresar_promocion = new com.comerzzia.jpos.gui.JIngresarPromocion();
        v_buscar_sukupon = new javax.swing.JDialog();
        p_buscar_sukupon = new com.comerzzia.jpos.gui.sukupo.JConsultaSukupon(this);
        v_buscar_bonos = new javax.swing.JDialog();
        p_buscar_bonos = new com.comerzzia.jpos.gui.bonoEfectivo.JConsultaBonoEfectivo();
        v_lectura_tarjeta_afiliado = new javax.swing.JDialog();
        p_lectura_tarjeta_afiliado = new com.comerzzia.jpos.gui.bancos.JLecturaTarjetaAfiliado();
        v_ingresarCodOTP = new javax.swing.JDialog();
        p_ingresar_cod_otp = new com.comerzzia.jpos.gui.JIngresarCodigoOtp();
        v_envio_mail = new javax.swing.JDialog();
        p_envia_mail = new com.comerzzia.jpos.gui.JEnviaMail();
        v_aut_credito_directo_excede_cupo = new javax.swing.JDialog();
        p_aut_credito_directo_excede_cupo = new com.comerzzia.jpos.gui.bancos.JAutExcedeCupoTarjetaCreditoDirecto();
        jPanel_centro = new javax.swing.JPanel();

        jf_informacion.setAlwaysOnTop(true);
        jf_informacion.setFocusable(false);
        jf_informacion.setFocusableWindowState(false);
        jf_informacion.setResizable(false);
        jf_informacion.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setBackground(new java.awt.Color(204, 255, 255));
        jLabel25.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel25.setText("Información Extra:");
        jf_informacion.getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jLabel26.setBackground(new java.awt.Color(204, 255, 255));
        jLabel26.setText("Información extra 1:");

        jScrollPane3.setBackground(new java.awt.Color(204, 255, 255));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jLabel27.setText("Información extra 2:");

        jScrollPane4.setBackground(new java.awt.Color(204, 255, 255));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        jLabel28.setText("Recibir llamadas:");

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox9.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel29.setBackground(new java.awt.Color(204, 255, 255));
        jLabel29.setText("Recibircorreos:");

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox10.setMinimumSize(new java.awt.Dimension(20, 18));
        jComboBox10.setPreferredSize(new java.awt.Dimension(20, 20));

        jButton8.setText("Aceptar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Cancelar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel28)))
                    .addComponent(jLabel27)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.Alignment.LEADING, 0, 70, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel26)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel27)))
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jButton9)
                    .addComponent(jButton8))
                .addGap(47, 47, 47))
        );

        jf_informacion.getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 660, 320));

        dialog_error.setAlwaysOnTop(true);
        dialog_error.setMinimumSize(new java.awt.Dimension(500, 100));
        dialog_error.setModal(true);
        dialog_error.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        dialog_error.setResizable(false);

        javax.swing.GroupLayout dialog_errorLayout = new javax.swing.GroupLayout(dialog_error.getContentPane());
        dialog_error.getContentPane().setLayout(dialog_errorLayout);
        dialog_errorLayout.setHorizontalGroup(
            dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialog_errorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_error, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialog_errorLayout.setVerticalGroup(
            dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialog_errorLayout.createSequentialGroup()
                .addComponent(p_error, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_menu.setAlwaysOnTop(true);
        v_menu.setBackground(new java.awt.Color(0, 0, 204));
        v_menu.setMinimumSize(new java.awt.Dimension(384, 528));
        v_menu.setModal(true);
        v_menu.setUndecorated(true);
        v_menu.setResizable(false);

        javax.swing.GroupLayout v_menuLayout = new javax.swing.GroupLayout(v_menu.getContentPane());
        v_menu.getContentPane().setLayout(v_menuLayout);
        v_menuLayout.setHorizontalGroup(
            v_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        v_menuLayout.setVerticalGroup(
            v_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dialog_confirm.setAlwaysOnTop(true);
        dialog_confirm.setMinimumSize(new java.awt.Dimension(560, 180));
        dialog_confirm.setModal(true);
        dialog_confirm.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                dialog_confirmWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_confirmacion.setMaximumSize(new java.awt.Dimension(530, 180));
        p_confirmacion.setMinimumSize(new java.awt.Dimension(530, 180));
        p_confirmacion.setPreferredSize(new java.awt.Dimension(530, 180));

        javax.swing.GroupLayout dialog_confirmLayout = new javax.swing.GroupLayout(dialog_confirm.getContentPane());
        dialog_confirm.getContentPane().setLayout(dialog_confirmLayout);
        dialog_confirmLayout.setHorizontalGroup(
            dialog_confirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_confirmacion, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );
        dialog_confirmLayout.setVerticalGroup(
            dialog_confirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_confirmacion, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
        );

        v_devolucion.setMinimumSize(new java.awt.Dimension(433, 490));
        v_devolucion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_devolucion.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        p_devolucion.setMaximumSize(new java.awt.Dimension(433, 480));
        p_devolucion.setMinimumSize(new java.awt.Dimension(433, 480));
        p_devolucion.setPreferredSize(new java.awt.Dimension(433, 480));

        javax.swing.GroupLayout v_devolucionLayout = new javax.swing.GroupLayout(v_devolucion.getContentPane());
        v_devolucion.getContentPane().setLayout(v_devolucionLayout);
        v_devolucionLayout.setHorizontalGroup(
            v_devolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_devolucionLayout.createSequentialGroup()
                .addComponent(p_devolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        v_devolucionLayout.setVerticalGroup(
            v_devolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_devolucionLayout.createSequentialGroup()
                .addComponent(p_devolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        v_recuento_caja.setAlwaysOnTop(true);
        v_recuento_caja.setMinimumSize(new java.awt.Dimension(840, 660));
        v_recuento_caja.setModal(true);
        v_recuento_caja.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_recuento_caja.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_recuento_cajaWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        v_recuento_caja.getContentPane().add(p_recuento_caja, java.awt.BorderLayout.CENTER);

        v_contadores.setTitle("Introducción de contadores");
        v_contadores.setAlwaysOnTop(true);
        v_contadores.setMinimumSize(new java.awt.Dimension(435, 300));
        v_contadores.setModal(true);
        v_contadores.setName("v_contadores"); // NOI18N
        v_contadores.setResizable(false);
        v_contadores.getContentPane().add(p_contadores, java.awt.BorderLayout.CENTER);

        v_error_validar_tarjeta.setAlwaysOnTop(true);
        v_error_validar_tarjeta.setMinimumSize(new java.awt.Dimension(720, 180));
        v_error_validar_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_error_validar_tarjeta.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_error_validar_tarjetaLayout = new javax.swing.GroupLayout(v_error_validar_tarjeta.getContentPane());
        v_error_validar_tarjeta.getContentPane().setLayout(v_error_validar_tarjetaLayout);
        v_error_validar_tarjetaLayout.setHorizontalGroup(
            v_error_validar_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_error_validar_tarjetaLayout.createSequentialGroup()
                .addComponent(p_error_validar_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        v_error_validar_tarjetaLayout.setVerticalGroup(
            v_error_validar_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_error_validar_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_bancos.setAlwaysOnTop(true);
        v_bancos.setMinimumSize(new java.awt.Dimension(744, 535));
        v_bancos.setModal(true);
        v_bancos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_bancos.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_bancosLayout = new javax.swing.GroupLayout(v_bancos.getContentPane());
        v_bancos.getContentPane().setLayout(v_bancosLayout);
        v_bancosLayout.setHorizontalGroup(
            v_bancosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_bancosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_bancosLayout.setVerticalGroup(
            v_bancosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_bancosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        v_autorizacion_tarjeta.setAlwaysOnTop(true);
        v_autorizacion_tarjeta.setMinimumSize(new java.awt.Dimension(495, 400));
        v_autorizacion_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_autorizacion_tarjeta.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        p_autorizacion_tarjeta.setMinimumSize(new java.awt.Dimension(495, 400));
        p_autorizacion_tarjeta.setPreferredSize(new java.awt.Dimension(495, 500));

        javax.swing.GroupLayout v_autorizacion_tarjetaLayout = new javax.swing.GroupLayout(v_autorizacion_tarjeta.getContentPane());
        v_autorizacion_tarjeta.getContentPane().setLayout(v_autorizacion_tarjetaLayout);
        v_autorizacion_tarjetaLayout.setHorizontalGroup(
            v_autorizacion_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_autorizacion_tarjetaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autorizacion_tarjeta, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );
        v_autorizacion_tarjetaLayout.setVerticalGroup(
            v_autorizacion_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_autorizacion_tarjetaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autorizacion_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        v_lectura_tarjeta_manual.setAlwaysOnTop(true);
        v_lectura_tarjeta_manual.setMinimumSize(new java.awt.Dimension(490, 240));
        v_lectura_tarjeta_manual.setModal(true);
        v_lectura_tarjeta_manual.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_lectura_tarjeta_manual.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_lectura_tarjeta_manual.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_lectura_tarjeta_manualWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_lectura_tarjeta_manual.setMinimumSize(new java.awt.Dimension(428, 170));

        javax.swing.GroupLayout v_lectura_tarjeta_manualLayout = new javax.swing.GroupLayout(v_lectura_tarjeta_manual.getContentPane());
        v_lectura_tarjeta_manual.getContentPane().setLayout(v_lectura_tarjeta_manualLayout);
        v_lectura_tarjeta_manualLayout.setHorizontalGroup(
            v_lectura_tarjeta_manualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_lectura_tarjeta_manual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        v_lectura_tarjeta_manualLayout.setVerticalGroup(
            v_lectura_tarjeta_manualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_lectura_tarjeta_manual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_menu_tarjetas.setMinimumSize(new java.awt.Dimension(488, 293));
        v_menu_tarjetas.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_menu_tarjetas.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_menu_tarjetasLayout = new javax.swing.GroupLayout(v_menu_tarjetas.getContentPane());
        v_menu_tarjetas.getContentPane().setLayout(v_menu_tarjetasLayout);
        v_menu_tarjetasLayout.setHorizontalGroup(
            v_menu_tarjetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_menu_tarjetasLayout.createSequentialGroup()
                .addComponent(p_menu_tarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_menu_tarjetasLayout.setVerticalGroup(
            v_menu_tarjetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_menu_tarjetasLayout.createSequentialGroup()
                .addComponent(p_menu_tarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_lectura_tarjeta.setAlwaysOnTop(true);
        v_lectura_tarjeta.setMinimumSize(new java.awt.Dimension(354, 100));
        v_lectura_tarjeta.setModal(true);
        v_lectura_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_lectura_tarjeta.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_lectura_tarjetaLayout = new javax.swing.GroupLayout(v_lectura_tarjeta.getContentPane());
        v_lectura_tarjeta.getContentPane().setLayout(v_lectura_tarjetaLayout);
        v_lectura_tarjetaLayout.setHorizontalGroup(
            v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_lectura_tarjetaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        v_lectura_tarjetaLayout.setVerticalGroup(
            v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_tarjetaLayout.createSequentialGroup()
                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_solicitar_identificacion.setAlwaysOnTop(true);
        v_solicitar_identificacion.setMinimumSize(new java.awt.Dimension(370, 360));
        v_solicitar_identificacion.setModal(true);
        v_solicitar_identificacion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_solicitar_identificacion.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_solicitar_identificacion.setResizable(false);
        v_solicitar_identificacion.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_solicitar_identificacionWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        v_solicitar_identificacion.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p_solicitar_informacion.setMaximumSize(new java.awt.Dimension(350, 320));
        p_solicitar_informacion.setMinimumSize(new java.awt.Dimension(350, 320));
        p_solicitar_informacion.setPreferredSize(new java.awt.Dimension(350, 320));

        l_error1.setBackground(new java.awt.Color(204, 255, 255));
        l_error1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        l_error1.setText("Introduzca Usuario Autorizado :");

        l_mensaje.setBackground(new java.awt.Color(204, 255, 255));
        l_mensaje.setText("No tiene Permisos para realizar la operación");

        l_error3.setBackground(new java.awt.Color(204, 255, 255));
        l_error3.setDisplayedMnemonic('u');
        l_error3.setText("Usuario :");

        i_error.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/mensajes/sinPermisos_p.png"))); // NOI18N

        t_usuario.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        t_usuario.setName("user"); // NOI18N
        t_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_usuarioActionPerformed(evt);
            }
        });

        l_error2.setBackground(new java.awt.Color(204, 255, 255));
        l_error2.setDisplayedMnemonic('c');
        l_error2.setText("Contraseña :");

        b_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_ok.setMnemonic('a');
        b_ok.setText("Aceptar");
        b_ok.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_okActionPerformed(evt);
            }
        });
        b_ok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b_okKeyPressed(evt);
            }
        });

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });
        b_cancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b_cancelarKeyPressed(evt);
            }
        });

        l_error.setForeground(new java.awt.Color(255, 51, 51));
        l_error.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_error.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        t_password.setPreferredSize(new java.awt.Dimension(8, 27));
        t_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_passwordKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout p_solicitar_informacionLayout = new javax.swing.GroupLayout(p_solicitar_informacion);
        p_solicitar_informacion.setLayout(p_solicitar_informacionLayout);
        p_solicitar_informacionLayout.setHorizontalGroup(
            p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_solicitar_informacionLayout.createSequentialGroup()
                .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_solicitar_informacionLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(l_error2)
                            .addComponent(l_error3))
                        .addGap(30, 30, 30)
                        .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                    .addComponent(l_error1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_solicitar_informacionLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(l_error, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addGroup(p_solicitar_informacionLayout.createSequentialGroup()
                                .addComponent(i_error, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_mensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                            .addGroup(p_solicitar_informacionLayout.createSequentialGroup()
                                .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        p_solicitar_informacionLayout.setVerticalGroup(
            p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_solicitar_informacionLayout.createSequentialGroup()
                .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_solicitar_informacionLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(i_error, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p_solicitar_informacionLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(l_mensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(l_error1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_error3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_error2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(l_error, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_solicitar_informacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        v_solicitar_identificacion.getContentPane().add(p_solicitar_informacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        v_ingresar_valor.setAlwaysOnTop(true);
        v_ingresar_valor.setMinimumSize(new java.awt.Dimension(368, 223));
        v_ingresar_valor.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_ingresar_valor.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_ingresar_valor.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_ingresar_valorWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout v_ingresar_valorLayout = new javax.swing.GroupLayout(v_ingresar_valor.getContentPane());
        v_ingresar_valor.getContentPane().setLayout(v_ingresar_valorLayout);
        v_ingresar_valorLayout.setHorizontalGroup(
            v_ingresar_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_ingresar_valorLayout.createSequentialGroup()
                .addComponent(p_ingresar_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_ingresar_valorLayout.setVerticalGroup(
            v_ingresar_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_ingresar_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_introducir_importe.setAlwaysOnTop(true);

        javax.swing.GroupLayout v_introducir_importeLayout = new javax.swing.GroupLayout(v_introducir_importe.getContentPane());
        v_introducir_importe.getContentPane().setLayout(v_introducir_importeLayout);
        v_introducir_importeLayout.setHorizontalGroup(
            v_introducir_importeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        v_introducir_importeLayout.setVerticalGroup(
            v_introducir_importeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        v_garantia.setAlwaysOnTop(true);
        v_garantia.setMinimumSize(new java.awt.Dimension(418, 412));
        v_garantia.setModal(true);

        javax.swing.GroupLayout v_garantiaLayout = new javax.swing.GroupLayout(v_garantia.getContentPane());
        v_garantia.getContentPane().setLayout(v_garantiaLayout);
        v_garantiaLayout.setHorizontalGroup(
            v_garantiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_garantiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_garantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_garantiaLayout.setVerticalGroup(
            v_garantiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_garantiaLayout.createSequentialGroup()
                .addComponent(p_garantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        v_facturacion.setAlwaysOnTop(true);
        v_facturacion.setMinimumSize(new java.awt.Dimension(420, 600));
        v_facturacion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_facturacion.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_facturacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v_facturacionFocusGained(evt);
            }
        });

        p_facturacion.setMaximumSize(new java.awt.Dimension(400, 590));
        p_facturacion.setMinimumSize(new java.awt.Dimension(400, 590));
        p_facturacion.setPreferredSize(new java.awt.Dimension(400, 600));

        javax.swing.GroupLayout v_facturacionLayout = new javax.swing.GroupLayout(v_facturacion.getContentPane());
        v_facturacion.getContentPane().setLayout(v_facturacionLayout);
        v_facturacionLayout.setHorizontalGroup(
            v_facturacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_facturacionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_facturacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_facturacionLayout.setVerticalGroup(
            v_facturacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_facturacionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_facturacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_autentificarCliente.setAlwaysOnTop(true);
        v_autentificarCliente.setMinimumSize(new java.awt.Dimension(429, 310));
        v_autentificarCliente.setModal(true);
        v_autentificarCliente.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_autentificarClienteWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        v_autentificarCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v_autentificarClienteFocusGained(evt);
            }
        });

        p_autentificarCliente.setMinimumSize(new java.awt.Dimension(400, 261));
        p_autentificarCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                p_autentificarClienteFocusGained(evt);
            }
        });

        javax.swing.GroupLayout v_autentificarClienteLayout = new javax.swing.GroupLayout(v_autentificarCliente.getContentPane());
        v_autentificarCliente.getContentPane().setLayout(v_autentificarClienteLayout);
        v_autentificarClienteLayout.setHorizontalGroup(
            v_autentificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_autentificarClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autentificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_autentificarClienteLayout.setVerticalGroup(
            v_autentificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_autentificarClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autentificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_tarjeta_supermaxi.setAlwaysOnTop(true);
        v_tarjeta_supermaxi.setMinimumSize(new java.awt.Dimension(500, 178));
        v_tarjeta_supermaxi.setModal(true);

        p_tarjeta_supermaxi.setMinimumSize(new java.awt.Dimension(391, 104));
        p_tarjeta_supermaxi.setPreferredSize(new java.awt.Dimension(473, 148));

        javax.swing.GroupLayout v_tarjeta_supermaxiLayout = new javax.swing.GroupLayout(v_tarjeta_supermaxi.getContentPane());
        v_tarjeta_supermaxi.getContentPane().setLayout(v_tarjeta_supermaxiLayout);
        v_tarjeta_supermaxiLayout.setHorizontalGroup(
            v_tarjeta_supermaxiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_tarjeta_supermaxiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_tarjeta_supermaxi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_tarjeta_supermaxiLayout.setVerticalGroup(
            v_tarjeta_supermaxiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_tarjeta_supermaxiLayout.createSequentialGroup()
                .addComponent(p_tarjeta_supermaxi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_lectura_factura.setAlwaysOnTop(true);
        v_lectura_factura.setMinimumSize(new java.awt.Dimension(402, 185));
        v_lectura_factura.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_lectura_factura.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_lectura_factura.setResizable(false);

        javax.swing.GroupLayout v_lectura_facturaLayout = new javax.swing.GroupLayout(v_lectura_factura.getContentPane());
        v_lectura_factura.getContentPane().setLayout(v_lectura_facturaLayout);
        v_lectura_facturaLayout.setHorizontalGroup(
            v_lectura_facturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_lectura_facturaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_lectura_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_lectura_facturaLayout.setVerticalGroup(
            v_lectura_facturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_facturaLayout.createSequentialGroup()
                .addComponent(p_lectura_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_ayuda.getContentPane().add(p_ayuda, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout v_observacionesLayout = new javax.swing.GroupLayout(v_observaciones.getContentPane());
        v_observaciones.getContentPane().setLayout(v_observacionesLayout);
        v_observacionesLayout.setHorizontalGroup(
            v_observacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_observacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_observacionesLayout.setVerticalGroup(
            v_observacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_observacionesLayout.createSequentialGroup()
                .addComponent(p_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_reimpresion.setMinimumSize(new java.awt.Dimension(260,335));

        p_reimpresion.setMaximumSize(new java.awt.Dimension(260,300));
        p_reimpresion.setMinimumSize(new java.awt.Dimension(260,300));
        v_reimpresion.getContentPane().add(p_reimpresion, java.awt.BorderLayout.CENTER);

        v_envio_domicilio.setAlwaysOnTop(true);
        v_envio_domicilio.setMinimumSize(new java.awt.Dimension(620,550));
        v_envio_domicilio.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_envio_domicilioWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        v_envio_domicilio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v_envio_domicilioFocusGained(evt);
            }
        });

        p_envio_domicilio.setMaximumSize(new java.awt.Dimension(610,450));
        p_envio_domicilio.setMinimumSize(new java.awt.Dimension(610,450));

        javax.swing.GroupLayout v_envio_domicilioLayout = new javax.swing.GroupLayout(v_envio_domicilio.getContentPane());
        v_envio_domicilio.getContentPane().setLayout(v_envio_domicilioLayout);
        v_envio_domicilioLayout.setHorizontalGroup(
            v_envio_domicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_envio_domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        v_envio_domicilioLayout.setVerticalGroup(
            v_envio_domicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_envio_domicilioLayout.createSequentialGroup()
                .addComponent(p_envio_domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_flash_ventas.setMinimumSize(new java.awt.Dimension(400,280));

        javax.swing.GroupLayout v_flash_ventasLayout = new javax.swing.GroupLayout(v_flash_ventas.getContentPane());
        v_flash_ventas.getContentPane().setLayout(v_flash_ventasLayout);
        v_flash_ventasLayout.setHorizontalGroup(
            v_flash_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_flash_ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_flash_ventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_flash_ventasLayout.setVerticalGroup(
            v_flash_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_flash_ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_flash_ventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_anulaciones.setMinimumSize(new java.awt.Dimension(335, 550));

        p_anulaciones.setMinimumSize(new java.awt.Dimension(335, 540));
        p_anulaciones.setPreferredSize(new java.awt.Dimension(331, 550));

        javax.swing.GroupLayout v_anulacionesLayout = new javax.swing.GroupLayout(v_anulaciones.getContentPane());
        v_anulaciones.getContentPane().setLayout(v_anulacionesLayout);
        v_anulacionesLayout.setHorizontalGroup(
            v_anulacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_anulaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        v_anulacionesLayout.setVerticalGroup(
            v_anulacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_anulaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_lectura_giftcard.setMinimumSize(new java.awt.Dimension(520, 125));
        v_lectura_giftcard.setModal(true);

        p_lectura_giftcard.setPreferredSize(new java.awt.Dimension(501, 110));

        javax.swing.GroupLayout v_lectura_giftcardLayout = new javax.swing.GroupLayout(v_lectura_giftcard.getContentPane());
        v_lectura_giftcard.getContentPane().setLayout(v_lectura_giftcardLayout);
        v_lectura_giftcardLayout.setHorizontalGroup(
            v_lectura_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_lectura_giftcardLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_lectura_giftcard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        v_lectura_giftcardLayout.setVerticalGroup(
            v_lectura_giftcardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_giftcardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_lectura_giftcard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_autorizacion_num_tarjeta.setMinimumSize(new java.awt.Dimension(442,295));
        v_autorizacion_num_tarjeta.setModal(true);
        v_autorizacion_num_tarjeta.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_autorizacion_num_tarjeta.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_autorizacion_num_tarjetaLayout = new javax.swing.GroupLayout(v_autorizacion_num_tarjeta.getContentPane());
        v_autorizacion_num_tarjeta.getContentPane().setLayout(v_autorizacion_num_tarjetaLayout);
        v_autorizacion_num_tarjetaLayout.setHorizontalGroup(
            v_autorizacion_num_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_autorizacion_num_tarjetaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autorizacion_num_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_autorizacion_num_tarjetaLayout.setVerticalGroup(
            v_autorizacion_num_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_autorizacion_num_tarjetaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_autorizacion_num_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_autorizacion_tarjeta_credito_directo.setMinimumSize(new java.awt.Dimension(442,280));
        v_autorizacion_tarjeta_credito_directo.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_autorizacion_tarjeta_credito_directo.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        p_autorizacion_tarjeta_credito_directo.setMinimumSize(new java.awt.Dimension(429, 180));

        javax.swing.GroupLayout v_autorizacion_tarjeta_credito_directoLayout = new javax.swing.GroupLayout(v_autorizacion_tarjeta_credito_directo.getContentPane());
        v_autorizacion_tarjeta_credito_directo.getContentPane().setLayout(v_autorizacion_tarjeta_credito_directoLayout);
        v_autorizacion_tarjeta_credito_directoLayout.setHorizontalGroup(
            v_autorizacion_tarjeta_credito_directoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(v_autorizacion_tarjeta_credito_directoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_autorizacion_tarjeta_credito_directoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_autorizacion_tarjeta_credito_directo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_autorizacion_tarjeta_credito_directoLayout.setVerticalGroup(
            v_autorizacion_tarjeta_credito_directoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(v_autorizacion_tarjeta_credito_directoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_autorizacion_tarjeta_credito_directoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_autorizacion_tarjeta_credito_directo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        v_ingresarCodEmple.setMinimumSize(new java.awt.Dimension(348, 265));
        v_ingresarCodEmple.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_ingresarCodEmple.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_ingresarCodEmple.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_ingresarCodEmpleWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_ingresar_cod_emple.setMinimumSize(new java.awt.Dimension(348, 265));
        p_ingresar_cod_emple.setName(""); // NOI18N
        p_ingresar_cod_emple.setOpaque(false);
        p_ingresar_cod_emple.setPreferredSize(new java.awt.Dimension(352, 245));
        p_ingresar_cod_emple.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                p_ingresar_cod_empleFocusGained(evt);
            }
        });
        p_ingresar_cod_emple.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                p_ingresar_cod_empleKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout v_ingresarCodEmpleLayout = new javax.swing.GroupLayout(v_ingresarCodEmple.getContentPane());
        v_ingresarCodEmple.getContentPane().setLayout(v_ingresarCodEmpleLayout);
        v_ingresarCodEmpleLayout.setHorizontalGroup(
            v_ingresarCodEmpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(v_ingresarCodEmpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_ingresarCodEmpleLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_ingresar_cod_emple, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_ingresarCodEmpleLayout.setVerticalGroup(
            v_ingresarCodEmpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(v_ingresarCodEmpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_ingresarCodEmpleLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_ingresar_cod_emple, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        v_ingresarPromocion.setMinimumSize(new java.awt.Dimension(480, 380));
        v_ingresarPromocion.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_ingresarPromocion.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_ingresarPromocion.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_ingresarPromocionWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_ingresar_promocion.setMinimumSize(new java.awt.Dimension(480, 380));
        p_ingresar_promocion.setOpaque(false);
        p_ingresar_promocion.setPreferredSize(new java.awt.Dimension(480, 380));

        javax.swing.GroupLayout v_ingresarPromocionLayout = new javax.swing.GroupLayout(v_ingresarPromocion.getContentPane());
        v_ingresarPromocion.getContentPane().setLayout(v_ingresarPromocionLayout);
        v_ingresarPromocionLayout.setHorizontalGroup(
            v_ingresarPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_ingresarPromocionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_ingresar_promocion, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        v_ingresarPromocionLayout.setVerticalGroup(
            v_ingresarPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_ingresarPromocionLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(p_ingresar_promocion, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        v_buscar_sukupon.setAlwaysOnTop(true);
        v_buscar_sukupon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        v_buscar_sukupon.setLocation(new java.awt.Point(280, 150));
        v_buscar_sukupon.setMinimumSize(new java.awt.Dimension(748, 495));
        v_buscar_sukupon.setModal(true);
        v_buscar_sukupon.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        javax.swing.GroupLayout v_buscar_sukuponLayout = new javax.swing.GroupLayout(v_buscar_sukupon.getContentPane());
        v_buscar_sukupon.getContentPane().setLayout(v_buscar_sukuponLayout);
        v_buscar_sukuponLayout.setHorizontalGroup(
            v_buscar_sukuponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_sukuponLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_buscar_sukupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_buscar_sukuponLayout.setVerticalGroup(
            v_buscar_sukuponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_sukuponLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_buscar_sukupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        v_buscar_bonos.setAlwaysOnTop(true);
        v_buscar_bonos.setLocation(new java.awt.Point(280, 150));
        v_buscar_bonos.setMinimumSize(new java.awt.Dimension(748, 495));
        v_buscar_bonos.setModal(true);
        v_buscar_bonos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        javax.swing.GroupLayout v_buscar_bonosLayout = new javax.swing.GroupLayout(v_buscar_bonos.getContentPane());
        v_buscar_bonos.getContentPane().setLayout(v_buscar_bonosLayout);
        v_buscar_bonosLayout.setHorizontalGroup(
            v_buscar_bonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_bonosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_buscar_bonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_buscar_bonosLayout.setVerticalGroup(
            v_buscar_bonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_bonosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_buscar_bonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        v_lectura_tarjeta_afiliado.setAlwaysOnTop(true);
        v_lectura_tarjeta_afiliado.setMinimumSize(new java.awt.Dimension(490, 240));
        v_lectura_tarjeta_afiliado.setModal(true);
        v_lectura_tarjeta_afiliado.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_lectura_tarjeta_afiliado.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_lectura_tarjeta_afiliado.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_lectura_tarjeta_afiliadoWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_lectura_tarjeta_afiliado.setMinimumSize(new java.awt.Dimension(428, 170));

        javax.swing.GroupLayout v_lectura_tarjeta_afiliadoLayout = new javax.swing.GroupLayout(v_lectura_tarjeta_afiliado.getContentPane());
        v_lectura_tarjeta_afiliado.getContentPane().setLayout(v_lectura_tarjeta_afiliadoLayout);
        v_lectura_tarjeta_afiliadoLayout.setHorizontalGroup(
            v_lectura_tarjeta_afiliadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_tarjeta_afiliadoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_lectura_tarjeta_afiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_lectura_tarjeta_afiliadoLayout.setVerticalGroup(
            v_lectura_tarjeta_afiliadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_tarjeta_afiliadoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_lectura_tarjeta_afiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        v_ingresarCodOTP.setAlwaysOnTop(true);
        v_ingresarCodOTP.setMinimumSize(new java.awt.Dimension(500, 300));
        v_ingresarCodOTP.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_ingresarCodOTP.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_ingresarCodOTP.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_ingresarCodOTPWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout v_ingresarCodOTPLayout = new javax.swing.GroupLayout(v_ingresarCodOTP.getContentPane());
        v_ingresarCodOTP.getContentPane().setLayout(v_ingresarCodOTPLayout);
        v_ingresarCodOTPLayout.setHorizontalGroup(
            v_ingresarCodOTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_ingresarCodOTPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_ingresar_cod_otp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        v_ingresarCodOTPLayout.setVerticalGroup(
            v_ingresarCodOTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_ingresarCodOTPLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(p_ingresar_cod_otp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        v_envio_mail.setMinimumSize(new java.awt.Dimension(490, 240));
        v_envio_mail.setModal(true);
        v_envio_mail.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_envio_mail.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_envio_mailLayout = new javax.swing.GroupLayout(v_envio_mail.getContentPane());
        v_envio_mail.getContentPane().setLayout(v_envio_mailLayout);
        v_envio_mailLayout.setHorizontalGroup(
            v_envio_mailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(v_envio_mailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_envio_mailLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_envia_mail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_envio_mailLayout.setVerticalGroup(
            v_envio_mailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(v_envio_mailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_envio_mailLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_envia_mail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        v_aut_credito_directo_excede_cupo.setMinimumSize(new java.awt.Dimension(442,280));
        v_aut_credito_directo_excede_cupo.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_aut_credito_directo_excede_cupo.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        javax.swing.GroupLayout v_aut_credito_directo_excede_cupoLayout = new javax.swing.GroupLayout(v_aut_credito_directo_excede_cupo.getContentPane());
        v_aut_credito_directo_excede_cupo.getContentPane().setLayout(v_aut_credito_directo_excede_cupoLayout);
        v_aut_credito_directo_excede_cupoLayout.setHorizontalGroup(
            v_aut_credito_directo_excede_cupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_aut_credito_directo_excede_cupoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_aut_credito_directo_excede_cupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_aut_credito_directo_excede_cupoLayout.setVerticalGroup(
            v_aut_credito_directo_excede_cupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_aut_credito_directo_excede_cupoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_aut_credito_directo_excede_cupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel_centro.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_centro.setAlignmentX(0.0F);
        jPanel_centro.setAlignmentY(0.0F);
        jPanel_centro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel_centro.setMaximumSize(new java.awt.Dimension(1024, 723));
        jPanel_centro.setMinimumSize(new java.awt.Dimension(1024, 723));
        jPanel_centro.setPreferredSize(new java.awt.Dimension(1024, 723));
        jPanel_centro.setLayout(new java.awt.CardLayout());
        add(jPanel_centro, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Actualiza la información del bean de cliente
        jf_informacion.setVisible(false);
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
}//GEN-LAST:event_jButton9ActionPerformed

    private void dialog_confirmWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialog_confirmWindowGainedFocus
        p_confirmacion.iniciaVista();
    }//GEN-LAST:event_dialog_confirmWindowGainedFocus

private void v_recuento_cajaWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_recuento_cajaWindowGainedFocus
    p_recuento_caja.iniciaFoco();
}//GEN-LAST:event_v_recuento_cajaWindowGainedFocus

private void v_lectura_tarjeta_manualWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_lectura_tarjeta_manualWindowGainedFocus
    p_lectura_tarjeta_manual.iniciaFoco();
}//GEN-LAST:event_v_lectura_tarjeta_manualWindowGainedFocus

private void t_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_usuarioActionPerformed
    b_okActionPerformed(evt);
}//GEN-LAST:event_t_usuarioActionPerformed

private void b_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_okActionPerformed

    try {
        l_error.setText("");
        Sesion.desbloqueaOperacion(t_usuario.getText(), t_password.getText());
        Contador.setBloqueado(false);
        exitoDesbloqueo = true;
        bloqueador.setVisible(false);
        v_solicitar_identificacion.setVisible(false);
        t_usuario.setText("");
        t_password.setText("");

    } catch (NullPointerException ex) {
        log.warn("Nulpointer en identificación para desbloqueo de la aplicación");
    } catch (InvalidLoginException ex) {
        log.debug("Login invalido para el desbloqueo de la aplicación");
        l_error.setText("Autenticación no valida");
    } catch (LoginException ex) {
        log.error("Error al realizar el login para desbloquear la aplicación: " + ex.getMessage(), ex);
        l_error.setText("No se pudo realizar la autenticación");
    } catch (SinPermisosException ex) {
        log.debug("El usuario no tiene permisos para desbloquear la aplicación");
        l_error.setText("<html>El usuario logueado no tiene permisos <P>para realizar la operación</html>");
    }
}//GEN-LAST:event_b_okActionPerformed

private void b_okKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_b_okKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        b_okActionPerformed(null);
    }
}//GEN-LAST:event_b_okKeyPressed

private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
    this.listenerList = null;
    l_error.setText("");
    t_usuario.setText("");
    t_password.setText("");
    v_solicitar_identificacion.setVisible(false);
}//GEN-LAST:event_b_cancelarActionPerformed

private void b_cancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_b_cancelarKeyPressed
    if (evt.getKeyChar() == '\n') {
        b_cancelarActionPerformed(null);
    }
}//GEN-LAST:event_b_cancelarKeyPressed

private void t_passwordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_passwordKeyTyped
    if (evt.getKeyChar() == '\n') {
        b_okActionPerformed(null);
    }
}//GEN-LAST:event_t_passwordKeyTyped

private void v_ingresar_valorWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_ingresar_valorWindowGainedFocus
    p_ingresar_valor.iniciaFoco();
}//GEN-LAST:event_v_ingresar_valorWindowGainedFocus

private void v_solicitar_identificacionWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_solicitar_identificacionWindowGainedFocus
}//GEN-LAST:event_v_solicitar_identificacionWindowGainedFocus

private void p_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_autentificarClienteFocusGained
    p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_p_autentificarClienteFocusGained

private void v_autentificarClienteWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_autentificarClienteWindowGainedFocus
    p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_v_autentificarClienteWindowGainedFocus

private void v_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_autentificarClienteFocusGained
    p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_v_autentificarClienteFocusGained

private void v_facturacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_facturacionFocusGained
    p_facturacion.iniciaFoco();
}//GEN-LAST:event_v_facturacionFocusGained

    private void v_envio_domicilioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_envio_domicilioFocusGained
        p_envio_domicilio.iniciaFoco();
    }//GEN-LAST:event_v_envio_domicilioFocusGained

    private void v_envio_domicilioWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_envio_domicilioWindowGainedFocus
        p_envio_domicilio.iniciaFoco();
    }//GEN-LAST:event_v_envio_domicilioWindowGainedFocus

    private void v_ingresarCodEmpleWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_ingresarCodEmpleWindowGainedFocus
        p_ingresar_cod_emple.iniciaFoco();
    }//GEN-LAST:event_v_ingresarCodEmpleWindowGainedFocus

    private void p_ingresar_cod_empleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_ingresar_cod_empleFocusGained
        p_ingresar_cod_emple.iniciaFoco();
    }//GEN-LAST:event_p_ingresar_cod_empleFocusGained

    private void v_ingresarPromocionWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_ingresarPromocionWindowGainedFocus
        p_ingresar_promocion.iniciaFoco();
    }//GEN-LAST:event_v_ingresarPromocionWindowGainedFocus

    private void v_lectura_tarjeta_afiliadoWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_lectura_tarjeta_afiliadoWindowGainedFocus
        // TODO add your handling code here:
    }//GEN-LAST:event_v_lectura_tarjeta_afiliadoWindowGainedFocus

    private void v_ingresarCodOTPWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_ingresarCodOTPWindowGainedFocus
        // TODO add your handling code here:
        p_ingresar_cod_otp.iniciaFoco();
    }//GEN-LAST:event_v_ingresarCodOTPWindowGainedFocus

    private void p_ingresar_cod_empleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_ingresar_cod_empleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_ingresar_cod_empleKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ok;
    private javax.swing.JDialog dialog_confirm;
    private javax.swing.JDialog dialog_error;
    private javax.swing.JLabel i_error;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private com.comerzzia.jpos.gui.JMenu jMenu1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel_centro;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JFrame jf_informacion;
    private javax.swing.JLabel l_error;
    private javax.swing.JLabel l_error1;
    private javax.swing.JLabel l_error2;
    private javax.swing.JLabel l_error3;
    private javax.swing.JLabel l_mensaje;
    private com.comerzzia.jpos.gui.cajas.anulaciones.JAnulaciones p_anulaciones;
    private com.comerzzia.jpos.gui.bancos.JAutExcedeCupoTarjetaCreditoDirecto p_aut_credito_directo_excede_cupo;
    private com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente p_autentificarCliente;
    private com.comerzzia.jpos.gui.bancos.JAutorizacionNumTarjeta p_autorizacion_num_tarjeta;
    private com.comerzzia.jpos.gui.bancos.JAutorizacionTarjeta p_autorizacion_tarjeta;
    private com.comerzzia.jpos.gui.bancos.JAutorizacionTarjetaCreditoDirecto p_autorizacion_tarjeta_credito_directo;
    private com.comerzzia.jpos.gui.JAyuda p_ayuda;
    private com.comerzzia.jpos.gui.bancos.JBancos p_bancos;
    private com.comerzzia.jpos.gui.bonoEfectivo.JConsultaBonoEfectivo p_buscar_bonos;
    private com.comerzzia.jpos.gui.sukupo.JConsultaSukupon p_buscar_sukupon;
    private com.comerzzia.jpos.gui.JConfirmacion p_confirmacion;
    private com.comerzzia.jpos.gui.respaldo.JContadores p_contadores;
    private com.comerzzia.jpos.gui.devoluciones.JDevolucion p_devolucion;
    private static com.comerzzia.jpos.gui.JEnviaMail p_envia_mail;
    private com.comerzzia.jpos.gui.guiasremision.JEnvioDomicilio p_envio_domicilio;
    private com.comerzzia.jpos.gui.JError p_error;
    private com.comerzzia.jpos.gui.ventas.JErrorValidacionTarjeta p_error_validar_tarjeta;
    private static com.comerzzia.jpos.gui.JFacturacion p_facturacion;
    private com.comerzzia.jpos.gui.flashventas.JElegirFlash p_flash_ventas;
    private static com.comerzzia.jpos.gui.garantia.JGarantia p_garantia;
    private static com.comerzzia.jpos.gui.JIngresarGarantia p_ingresar_cod_emple;
    private static com.comerzzia.jpos.gui.JIngresarCodigoOtp p_ingresar_cod_otp;
    private static com.comerzzia.jpos.gui.JIngresarPromocion p_ingresar_promocion;
    private static com.comerzzia.jpos.gui.JIngresarValor p_ingresar_valor;
    private static com.comerzzia.jpos.gui.JIdPanel p_lectura_factura;
    private com.comerzzia.jpos.gui.giftcard.JIntroducirGiftCard p_lectura_giftcard;
    private static com.comerzzia.jpos.gui.JLecturaTarjeta p_lectura_tarjeta;
    private com.comerzzia.jpos.gui.bancos.JLecturaTarjetaAfiliado p_lectura_tarjeta_afiliado;
    private com.comerzzia.jpos.gui.bancos.JLecturaTarjetaManual p_lectura_tarjeta_manual;
    private com.comerzzia.jpos.gui.credito.JOperacionesTarjeta p_menu_tarjetas;
    private com.comerzzia.jpos.gui.JObservaciones p_observaciones;
    private com.comerzzia.jpos.gui.cajas.JRecuentoCaja p_recuento_caja;
    private com.comerzzia.jpos.gui.reimpresion.JReimpresion p_reimpresion;
    private javax.swing.JPanel p_solicitar_informacion;
    private com.comerzzia.jpos.gui.supermaxi.JTarjetaSuperMaxi p_tarjeta_supermaxi;
    private com.comerzzia.jpos.gui.components.form.JPasswordFieldForm t_password;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_usuario;
    private javax.swing.JDialog v_anulaciones;
    private javax.swing.JDialog v_aut_credito_directo_excede_cupo;
    private javax.swing.JDialog v_autentificarCliente;
    private javax.swing.JDialog v_autorizacion_num_tarjeta;
    private javax.swing.JDialog v_autorizacion_tarjeta;
    private javax.swing.JDialog v_autorizacion_tarjeta_credito_directo;
    private static javax.swing.JDialog v_ayuda;
    private javax.swing.JDialog v_bancos;
    private javax.swing.JDialog v_buscar_bonos;
    private javax.swing.JDialog v_buscar_sukupon;
    private javax.swing.JDialog v_contadores;
    private javax.swing.JDialog v_devolucion;
    private javax.swing.JDialog v_envio_domicilio;
    private static javax.swing.JDialog v_envio_mail;
    private javax.swing.JDialog v_error_validar_tarjeta;
    private static javax.swing.JDialog v_facturacion;
    private javax.swing.JDialog v_flash_ventas;
    private static javax.swing.JDialog v_garantia;
    private static javax.swing.JDialog v_ingresarCodEmple;
    private static javax.swing.JDialog v_ingresarCodOTP;
    private static javax.swing.JDialog v_ingresarPromocion;
    private static javax.swing.JDialog v_ingresar_valor;
    private javax.swing.JDialog v_introducir_importe;
    private static javax.swing.JDialog v_lectura_factura;
    private javax.swing.JDialog v_lectura_giftcard;
    private static javax.swing.JDialog v_lectura_tarjeta;
    private javax.swing.JDialog v_lectura_tarjeta_afiliado;
    private javax.swing.JDialog v_lectura_tarjeta_manual;
    private javax.swing.JDialog v_menu;
    private javax.swing.JDialog v_menu_tarjetas;
    private javax.swing.JDialog v_observaciones;
    private javax.swing.JDialog v_recuento_caja;
    private javax.swing.JDialog v_reimpresion;
    private javax.swing.JDialog v_solicitar_identificacion;
    private javax.swing.JDialog v_tarjeta_supermaxi;
    // End of variables declaration//GEN-END:variables

    void tryToClose() {
        SwingUtilities.getWindowAncestor(this).dispose();
        System.exit(0);
    }

    @Override
    public void iniciaVista() {
        log.debug("Iniciando vista Jprincipal....");
        if (p_pie == null) {
            p_pie = new JPie();
            p_pie.setMaximumSize(new java.awt.Dimension(1024, 45));
            p_pie.setMinimumSize(new java.awt.Dimension(1024, 45));
            p_pie.setPreferredSize(new java.awt.Dimension(1024, 45));
            add(p_pie, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, -1, -1));
        }
        p_pie.iniciaVista();
    }

    public String compruebaAutorizacion(Byte operacion) throws SinPermisosException {
        log.debug("PERMISOS Combrobando Autorización " + operacion);
        if (Sesion.permisos.isPuede(operacion)) {
            if (operacion.equals(Operaciones.REIMPRIMIR)) {
                return crearVentanaAutorizacion(operacion);
            } else {
                return Sesion.getUsuario().getUsuario();
            }
        } else {
            return crearVentanaAutorizacion(operacion);
        }
    }

    public String compruebaAutorizacionReserva(Byte operacion) throws SinPermisosException {
        log.debug("PERMISOS Combrobando Autorización " + operacion);
        return crearVentanaAutorizacion(operacion);
    }

    public String compruebaAutorizacionNotaCredito(Byte operacion) throws SinPermisosException {
        log.debug("PERMISOS Combrobando Autorización " + operacion);
        return crearVentanaAutorizacion(operacion);
    }

    public String compruebaAutorizacionEstatusEntrega(Byte operacion, BigDecimal valor) throws SinPermisosException {
        log.debug("PERMISOS Combrobando Autorización Estatus Entrega");
        if (Sesion.permisos.isPuedeAdministrar(operacion)) {
            //Jefe de seccion - Verificar monto
            BigDecimal valorAprobar = Constantes.VALOR_MAXIMO_APROBAR_ENVIO_SECCION;
            if (Variables.getVariable(Variables.POS_ESTATUS_ENTREGA_MONTO) != null) {
                valorAprobar = new BigDecimal(Variables.getVariable(Variables.POS_ESTATUS_ENTREGA_MONTO));
            }

            if (valor.compareTo(valorAprobar) > 0) {
                //requiere autorizacion
                return crearVentanaAutorizacion(operacion);
            } else {
                return Sesion.getUsuario().getUsuario();
            }
        } else if (Sesion.permisos.isConcedido(operacion)) {
            //Gerente
            return Sesion.getUsuario().getUsuario();
        } else {
            //Cajero
            return crearVentanaAutorizacion(operacion);
        }
    }

    //RD ventana para ingreso garantia
    private static Usuarios crearVentanaIngresarValorUsuario(int tipo, String mensaje) {
        Usuarios res = null;
        p_ingresar_cod_emple.iniciaVista();
        p_ingresar_cod_emple.iniciaFoco();
        p_ingresar_cod_emple.setMensaje(mensaje);
        popupActivo = v_ingresarCodEmple;
        v_ingresarCodEmple.setVisible(true);
        popupActivo = null;
        res = p_ingresar_cod_emple.getValor();

        return res;
    }

    private static Integer crearVentanaIngresarValorPromocion(Integer tipo, String mensaje, BigDecimal maximo) {
        Integer res = null;

        p_ingresar_promocion.iniciaVista();
        p_ingresar_promocion.iniciaFoco();
        p_ingresar_promocion.setMensaje(mensaje);
        p_ingresar_promocion.setValorMaximo(maximo);
        p_ingresar_promocion.setTipoValor(tipo);
        popupActivo = v_ingresarPromocion;
        v_ingresarPromocion.setVisible(true);
        popupActivo = null;
        res = p_ingresar_promocion.getValor();

        return res;
    }

    protected Usuarios crearVentanaGarantiaUsuario(String valor) {
        Usuarios res = null;

        res = crearVentanaIngresarValorUsuario(JIngresarGarantia.TIPO_CADENA, valor);

        return res;
    }

    public Integer crearVentanaPromocion(String valor, BigDecimal maximo) {
        Integer res = null;
        res = crearVentanaIngresarValorPromocion(JIngresarGarantia.TIPO_NUMERICO, valor, maximo);
        return res;
    }

    public String compruebaAutorizacion(Byte operacion, String mensaje) throws SinPermisosException {
        log.debug("PERMISOS Combrobando Autorización ");
        try {
            if (Sesion.permisos.isPuede(operacion)) {
                return Sesion.getUsuario().getUsuario();
            } else {
                return crearVentanaAutorizacion(operacion, mensaje);
            }
            //Si da un NullPointerException es que estamos consultando el stock en la búsqueda de artículos sin habernos logueado
        } catch (NullPointerException e) {
            if (operacion == Operaciones.STOCK) {
                return crearVentanaAutorizacion(operacion, mensaje);
            }
            throw new SinPermisosException();
        }
    }

    public String crearVentanaAutorizacion(Byte operacion) throws SinPermisosException {
        log.info("PERMISOS: Creando Pantalla de Autorización");
        preparaDimensionVentanaAutorizacion();
        v_dialog_autorizar.setLocationRelativeTo(null);
        v_dialog_autorizar.setOperacionAAutorizar(operacion);
        v_dialog_autorizar.setMensajeDefault();
        v_dialog_autorizar.setFocoInicial();
        popupActivo = v_dialog_autorizar;
        v_dialog_autorizar.setVisible(true);
        popupActivo = null;
        // Aqui esperamos a que termine la ventana modal

        return v_dialog_autorizar.autenticacionExitosa();

    }

    public String crearVentanaAutorizacion(Byte operacion, String mensaje) throws SinPermisosException {
        log.info("PERMISOS: Creando Pantalla de Autorización");
        preparaDimensionVentanaAutorizacion();
        v_dialog_autorizar.setLocationRelativeTo(null);
        v_dialog_autorizar.setOperacionAAutorizar(operacion);
        v_dialog_autorizar.setMensaje(mensaje);
        v_dialog_autorizar.setFocoInicial();
        popupActivo = v_dialog_autorizar;
        v_dialog_autorizar.setVisible(true);
        popupActivo = null;
        // Aqui esperamos a que termine la ventana modal

        return v_dialog_autorizar.autenticacionExitosa();

    }

    public void preparaDimensionVentanaAutorizacion() {
        v_dialog_autorizar.setMinimumSize(new java.awt.Dimension(370, 360)); //442
        v_dialog_autorizar.setMaximumSize(new java.awt.Dimension(370, 360));
        v_dialog_autorizar.setPreferredSize(new java.awt.Dimension(370, 360));
    }

    /*
     * Metodo auxiliar para la creación de ventanas de diálogo
     */
    private synchronized JDialog crearVentanaDialogo(String desError, int tipo, boolean modal) {
        // INICIO
        JDialog new_dialog_error = new JDialog();
        try {
            new_dialog_error.setIconImage(iconoTransparente.getImage());
        } catch (Exception e) {
            log.warn("Error al cargar icono de ventana de dialogo. El proceso continuará.", e);
        }

        if (new_p_error != null && new_p_error.getContenedor().isVisible()) {
            log.warn("Se intentaron abrir dos mensajes a la vez. El segundo ha sido ignorado.");
            return new_p_error.getContenedor();
        }

        new_p_error = new JError();
        new_p_error.setDesError(desError);
        new_p_error.setTipo(tipo);
        new_p_error.setContenedor(new_dialog_error);

        jf_informacion.setIconImage(iconoTransparente.getImage());

        new_dialog_error.setAlwaysOnTop(true);
        new_dialog_error.setMinimumSize(new java.awt.Dimension(500, 100));

        if (modal) {
            new_dialog_error.setModal(true);
            new_dialog_error.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
            new_dialog_error.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        }
        new_dialog_error.setResizable(false);

        javax.swing.GroupLayout dialog_errorLayout = new javax.swing.GroupLayout(new_dialog_error.getContentPane());
        new_dialog_error.getContentPane().setLayout(dialog_errorLayout);
        dialog_errorLayout.setHorizontalGroup(
                dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(dialog_errorLayout.createSequentialGroup().addContainerGap().addComponent(new_p_error, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        dialog_errorLayout.setVerticalGroup(
                dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialog_errorLayout.createSequentialGroup().addComponent(new_p_error, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        // FIN
        if (desError == null || desError.equals("")) {
            desError = " Se ha producido un error en la aplicación ";
        }

        new_dialog_error.setLocationRelativeTo(null);
        new_dialog_error.setSize(450, 40);
        popupActivo = new_dialog_error;
        new_dialog_error.setVisible(true);
        popupActivo = null;
        return new_dialog_error;

    }

    public boolean crearVentanaConfirmacion(String texto) {
        boolean res = false;
        p_confirmacion.setContenedor(dialog_confirm);
        p_confirmacion.setDesError(texto);
        p_confirmacion.cambiaTextoBotones("Aceptar", "Cancelar");
        dialog_confirm.setModal(true);
        dialog_confirm.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        dialog_confirm.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        dialog_confirm.setLocationRelativeTo(null);
        dialog_confirm.setAlwaysOnTop(true);
        p_confirmacion.requestFocus();
        popupActivo = dialog_confirm;
        dialog_confirm.setVisible(true);
        popupActivo = null;

        res = p_confirmacion.isAceptado();
        return res;
    }

    public boolean crearVentanaConfirmacion(String texto, String textoConfirmacion, String textoCancelacion) {
        boolean res = false;
        p_confirmacion.setContenedor(dialog_confirm);
        p_confirmacion.setDesError(texto);
        p_confirmacion.cambiaTextoBotones(textoConfirmacion, textoCancelacion);
        dialog_confirm.setModal(true);
        dialog_confirm.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        dialog_confirm.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        dialog_confirm.setLocationRelativeTo(null);
        dialog_confirm.setAlwaysOnTop(true);
        p_confirmacion.requestFocus();
        popupActivo = dialog_confirm;
        dialog_confirm.setVisible(true);
        popupActivo = null;
        res = p_confirmacion.isAceptado();
        return res;
    }

    /**
     * Inicializa y muestra la información de errores.
     *
     * @param desError
     */
    @Override
    public void crearError(String descripcion) {
        crearVentanaDialogo(descripcion, JError.TIPO_ERROR, true);
    }

    @Override
    public void crearAdvertencia(String descripcion) {
        if (!Sesion.getDatosConfiguracion().isModoDesarrollo() || !descripcion.equals(Variables.getVariable(Variables.MENSAJE_AVISO_RETIRO))) {
            crearVentanaDialogo(descripcion, JError.TIPO_ADVERTENCIA, true);
        }

    }

    @Override
    public void crearConfirmacion(String descripcion) {
        try {
            crearVentanaDialogo(descripcion, JError.TIPO_CONFIRMACION, true);
        } catch (Exception e) {
            log.error("Error lanzando ventana de confirmación. El proceso continuará. ", e);
        }
    }

    @Override
    public void crearInformacion(String descripcion) {
        try {
            crearVentanaDialogo(descripcion, JError.TIPO_INFORMACION, true);
        } catch (Exception e) {
            log.error("Error lanzando ventana de información. El proceso continuará. ", e);
        }
    }

    @Override
    public void crearSinPermisos(String descripcion) {
        try {
            crearVentanaDialogo(descripcion, JError.TIPO_SINPERMISOS, true);
        } catch (Exception e) {
            log.error("Error lanzando ventana de sin permisos. El proceso continuará. ", e);
        }
    }

    /**
     * Muestra la ventana de menu
     */
    public void mostrarMenu() {
        log.info("Mostrando menú (Por Defecto)");
        jMenu1.iniciaVista();
        v_menu.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_menu.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_menu.setSize(384, 568);
        v_menu.setLocationRelativeTo(null);
        popupActivo = v_menu;
        v_menu.setVisible(true);
        popupActivo = null;
    }

    /**
     * Muestra la ventana de menu
     *
     * @param vistaProcesoVenta identificador de la pantalla del proceso de
     * venta que llamo al menú y que permite regresar a la vista
     */
    public void mostrarMenu(String vista, String tipo) {
        log.info("Mostrando menú (Guardando vista actual)");
        if (tipo.equals("VENTA")) {
            this.vistaProcesoVenta = vista;
            log.info("Tipo VENTA, vista:" + tipo);
        } else if (tipo.equals("NUEVA_RESERVACION")) {
            this.vistaProcesoNuevaReservacion = vista;
            log.info("Tipo NUEVA RESERVACIÓN, vista:" + tipo);
        }
        jMenu1.iniciaVista();
        v_menu.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_menu.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_menu.setSize(384, 568);
        v_menu.setLocationRelativeTo(null);
        popupActivo = v_menu;
        v_menu.setVisible(true);
        popupActivo = null;

    }

    public String getVistaProcesoVenta() {
        return vistaProcesoVenta;
    }

    public void addBuscarReservacionesView() {
        if (!vistasActivas.containsKey("buscar_reservaciones")) {
            log.info("Se añade La vista Buscar Reservaciones ");
            JBuscarReservaciones buscar_reservaciones = new JBuscarReservaciones(this);
            addView(buscar_reservaciones, "buscar_reservaciones");
        }
    }

    public static void crearVentanaAyuda() {
        log.info("Creando ventana de ayuda ");
        v_ayuda.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_ayuda.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_ayuda.setSize(540, 700);
        v_ayuda.setLocationRelativeTo(null);
        v_ayuda.setVisible(true);
    }

    public static void escapeVentanaAyuda() {
        v_ayuda.setVisible(false);
    }

    public String crearVentanaObservaciones(String cadenaObservaciones) {
        log.info("Creando ventana de observaciones ");
        v_observaciones.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_observaciones.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_observaciones.setSize(400, 320);
        p_observaciones.setSize(400, 320);
        v_observaciones.setLocationRelativeTo(null);
        //p_observaciones.setCadenaObservaciones(cadenaObservaciones);

        p_observaciones.iniciaFoco();
        p_observaciones.setObservaciones(cadenaObservaciones);
        p_observaciones.leerObservaciones();
        v_observaciones.setVisible(true);
        return p_observaciones.getObservaciones();
    }

    void crearVentanaDevolucion() {
        log.info("Creando Ventana de Devolución ");
        p_devolucion.setVentana_padre(this); //valdría solo en el constructor
        p_devolucion.setContenedor(v_devolucion); //valdría solo en el constructor
        v_devolucion.setSize(380, 510);
        p_devolucion.setSize(380, 510);
        p_devolucion.iniciaVista();
        v_devolucion.setLocationRelativeTo(null);
        popupActivo = v_devolucion;
        v_devolucion.setVisible(true);
        popupActivo = null;
    }

    public void crearVentanaMenuReimpresion() {
        try {
            String compruebaAutorizacion = compruebaAutorizacion(Operaciones.REIMPRIMIR);
            log.info("Creando Ventana Menú de Reimpresión ");
            v_reimpresion.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
            v_reimpresion.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            v_reimpresion.setSize(350, 400);
            p_reimpresion.setVentana_padre(this); //valdría solo en el constructor
            p_reimpresion.iniciaFoco();
            if (!v_reimpresion.isUndecorated()) {
                v_reimpresion.setUndecorated(true);
            }
            v_reimpresion.setLocationRelativeTo(null);
            v_reimpresion.setResizable(false);
            popupActivo = v_reimpresion;
            v_reimpresion.setVisible(true);
            popupActivo = null;
        } catch (SinPermisosException e) {
            // El usuario no tiene permisos para reimprimr
        }
    }

    public void crearVentanaConsultaSukupon() {
        try {
            String compruebaAutorizacion = compruebaAutorizacion(Operaciones.PERMITE_CONSULTAR_SUKUPON);
            log.info("Creando Ventana Menú de Sukupon ");
//            v_buscar_sukupon.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
//            v_buscar_sukupon.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
//            p_buscar_sukupon.setVentana_padre(this); //valdría solo en el constructor
//            p_buscar_sukupon.iniciaFoco();
//            if (!v_buscar_sukupon.isUndecorated()) {
//                v_buscar_sukupon.setUndecorated(true);
//            }
//            v_buscar_sukupon.setLocationRelativeTo(null);
//            v_buscar_sukupon.setResizable(false);
//            popupActivo = v_buscar_sukupon;
//            v_buscar_sukupon.setVisible(true);
//            popupActivo = null;
            p_buscar_sukupon.iniciaVista(this);
            v_buscar_sukupon.setVisible(true);
        } catch (SinPermisosException e) {
            // El usuario no tiene permisos para reimprimr
        }
    }

    public void crearVentanaConsultaBonosEfectivo() {
        try {
            String compruebaAutorizacion = compruebaAutorizacion(Operaciones.PERMITE_CONSULTAR_BONOS);
            log.info("Creando Ventana Menú de Bonos Efectivo ");
            p_buscar_bonos.iniciaVista();
            v_buscar_bonos.setVisible(true);
        } catch (SinPermisosException e) {
            // El usuario no tiene permisos para reimprimr
        }
    }

    public void crearVentanaEnvioDomicilio() {
        try {
            String compruebaAutorizacion = compruebaAutorizacion(Operaciones.PERMITE_DESCARGAR_ENTREGAS);
            log.info("Creando Ventana de Envío a Domicilio ");
            v_envio_domicilio.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
            v_envio_domicilio.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            p_envio_domicilio.setVentana_padre(this);
            v_envio_domicilio.setLocationRelativeTo(null);
            v_envio_domicilio.setResizable(false);
            p_envio_domicilio.iniciaVista();
            popupActivo = v_reimpresion;
            v_envio_domicilio.setVisible(true);
            popupActivo = null;
        } catch (SinPermisosException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irVentanaPrefactura() {
        log.info("Se añade La vista buscar_reservaciones ");
        JBuscarPreFactura buscarPrefactura = new JBuscarPreFactura(this);
        addOrSwitchView(buscarPrefactura, "buscar_prefactura");
        showView("buscar_prefactura");

    }

    public void addMostrarPrefactura(CabPrefactura cabPrefactura) {
        log.info("Se añade La vista buscar_reservaciones ");
        IVista mostrarPrefactura = new JMostrarPreFactura(this, cabPrefactura);
        addOrSwitchView(mostrarPrefactura, "mostrar_prefactura");

    }

    public void addMostrarReservacionView(Reservacion reserva) {
        IVista mostrar_reservacion;

        if (reserva.getReservacion().getReservaTipo().getListaInvitados()) {
            mostrar_reservacion = new JMostrarReservacionCanBaby(reserva, this);
        } else {
            mostrar_reservacion = new JMostrarReservacion(reserva, this);
        }
        addOrSwitchView(mostrar_reservacion, "mostrar_reservacion");
    }

    public void addIdentificaClienteReservacionView(Reservacion reserva, String operacion) {

        IVista identifica_cliente_reservacion = new JReservacionesCliente(this, reserva, operacion);
        addOrSwitchView(identifica_cliente_reservacion, "identifica_cliente_reservacion");
    }

    public void addIdentificaClienteReservacionView(PlanNovioOBJ planNovioOBJ) {

        IVista identifica_cliente_reservacion = new JReservacionesCliente(planNovioOBJ);
        addOrSwitchView(identifica_cliente_reservacion, "identifica_cliente_reservacion");
        showView("identifica_cliente_reservacion");
    }

    public void addCotizacionView(CotizacionBean cotizacion) {
        IVista identifica_cliente_cotizacion = new JReservacionesCliente(cotizacion);
        addOrSwitchView(identifica_cliente_cotizacion, "identifica_cliente_cotizacion");
        showView("identifica_cliente_cotizacion");
    }

    /**
     * Esta función es llamada dede JInicioPc al arrancar la aplicación.
     */
    public void iniciaVistaActiva() {
        ((IVista) getVistaActual()).iniciaVista();
    }

    public void accionAbrirRecuentos() {
        log.debug("ACCION ABRIR RECUENTOS");
        recuentoVisible = true;
        if (Sesion.getCajaActual().isCajaParcialAbierta()) {
            v_recuento_caja.setLocationRelativeTo(null);
            p_recuento_caja.iniciaVista();
            popupActivo = v_recuento_caja;
            v_recuento_caja.setVisible(true);
            popupActivo = null;
        }
        recuentoVisible = false;
    }

    public static boolean isRecuentoVisible() {
        return recuentoVisible;
    }

    @Override
    public void iniciaFoco() {
    }

    public void creaVentanaBusquedaArticulos() {
        // 
        JLogin jl = (JLogin) vistasActivas.get("login");
        jl.accionConsultaArticulos();
    }

    // Es preciso que en los metodos sucesivos, no se inicie la vista
    public void irReservacionPagoBonos() {
        Contador.setContador(0);
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistasActivas.containsKey("mostrar_reservacion") && vistasActivas.get("mostrar_reservacion") instanceof JMostrarReservacionCanBaby) {
            cl.show(jPanel_centro, "mostrar_reservacion");
            JMostrarReservacionCanBaby panel = (JMostrarReservacionCanBaby) vistasActivas.get("mostrar_reservacion");
            panel.realizarAbono();
        }
    }

    public void irPagoGiftCard(Cliente cliente) {
        log.debug("Volvemos a la vista de Pago con gifcard con el cliente seleccionado");
        Contador.setContador(0);
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistasActivas.containsKey(VISTA_GIFTCARD) && vistasActivas.get(VISTA_GIFTCARD) instanceof JGiftCard) {
            cl.show(jPanel_centro, VISTA_GIFTCARD);
            JGiftCard panel = (JGiftCard) vistasActivas.get(VISTA_GIFTCARD);
            panel.setCliente(cliente);
        }
    }

    public void irConsultaBonoSuper(Cliente cliente) {
        log.debug("Volvemos a la vista de Pago con gifcard con el cliente seleccionado");
        Contador.setContador(0);
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistasActivas.containsKey(VISTA_BONOSUPER) && vistasActivas.get(VISTA_BONOSUPER) instanceof JConsultaBonosSupermaxi) {
            cl.show(jPanel_centro, VISTA_BONOSUPER);
            JConsultaBonosSupermaxi panel = (JConsultaBonosSupermaxi) vistasActivas.get(VISTA_BONOSUPER);
            panel.setCliente(cliente);
        }
    }

    public int crearVentanaErrorValidacionTarjeta(Pago pago, String motivo) {
        p_error_validar_tarjeta.iniciaVista();
        p_error_validar_tarjeta.iniciaTexto(pago, motivo);
        v_error_validar_tarjeta.setLocation(200, 400);
        popupActivo = v_error_validar_tarjeta;
        v_error_validar_tarjeta.setVisible(true);
        popupActivo = null;

        return p_error_validar_tarjeta.getRespuesta();
    }

    /**
     * Rd Error Validacion
     *
     * @param pago
     * @param pago2
     * @param motivo
     * @return
     */
    public int crearVentanaErrorValidacionTarjeta2(Pago pago, String pago2, String motivo) {
        p_error_validar_tarjeta.iniciaVista();
        p_error_validar_tarjeta.iniciaTexto2(pago, motivo, pago2);
        v_error_validar_tarjeta.setLocation(200, 400);
        popupActivo = v_error_validar_tarjeta;
        v_error_validar_tarjeta.setVisible(true);
        popupActivo = null;

        return p_error_validar_tarjeta.getRespuesta();
    }

    public void crearVentanaInformacionBancos(PagoCredito pago) {
        p_bancos.iniciaVista(pago);
        popupActivo = v_bancos;
        v_bancos.setVisible(true);
        popupActivo = null;
    }

    public void crearVentanaIntroduccionAutorizacion(PagoCredito pago, String mensaje, boolean ventaManual) {
        p_autorizacion_tarjeta.iniciaVista(pago, mensaje, ventaManual);
        popupActivo = v_autorizacion_tarjeta;
        v_autorizacion_tarjeta.setAlwaysOnTop(true);
        v_autorizacion_tarjeta.requestFocus();
        v_autorizacion_tarjeta.setVisible(true);
        popupActivo = null;
    }

    public void crearVentanaIntroduccionNumAutorizacion(PagoCredito pago) {
        p_autorizacion_num_tarjeta.iniciaVista(pago);
        popupActivo = v_autorizacion_num_tarjeta;
        v_autorizacion_num_tarjeta.setVisible(true);
        popupActivo = null;
    }

    /**
     * Rd ventana autorizacion Credito Directo
     *
     * @param pago
     */
    public void crearVentanaIntroduccionAutorizacionCreditoDirecto(PagoCredito pago) {
        p_autorizacion_tarjeta_credito_directo.iniciaVista(pago);
        popupActivo = v_autorizacion_tarjeta_credito_directo;
        v_autorizacion_tarjeta_credito_directo.setVisible(true);
        popupActivo = null;
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Crea la ventana de autorizaci&oacute;n cuando excede el cupo  </p>
     *
     *
     * @param pago
     * @param ticket
     */
    public void crearVentanaAutorizacionCreditoDirectoExcedeCupo(PagoCredito pago, TicketS ticket) {

        String uidAutorizacion = UUID.randomUUID().toString();
        p_aut_credito_directo_excede_cupo.iniciaVista(pago, ticket, uidAutorizacion);
        ServerSocket ss = null;
        try {
            try {
                ss = new ServerSocket(Constantes.SOCKET_AUTORIZACION_CONSUMO);
            } catch (IOException ex) {
                log.error("Error al abrir el Socket ", ex);
            }
            AutorizacionCupoThread autorizacionCupoThread = new AutorizacionCupoThread(p_aut_credito_directo_excede_cupo, ss);
            autorizacionCupoThread.start();
            popupActivo = v_aut_credito_directo_excede_cupo;
            v_aut_credito_directo_excede_cupo.setVisible(true);
        } finally {
            try {
                if (ss != null) {
                    if (!ss.isClosed()) {
                        ss.close();
                    }
                }
            } catch (IOException ex) {
                log.error("Error en el IO:" + ex.getMessage());
            }

        }
        popupActivo = null;
    }

    public TarjetaCredito crearVentanaPagoManualTarjetaCredito(MedioPagoBean medioPago, TarjetaCredito tc) {
        p_lectura_tarjeta_manual.iniciaVista(medioPago.isTarjetaSukasa(), tc);
        popupActivo = v_lectura_tarjeta_manual;
        v_lectura_tarjeta_manual.setVisible(true);
        popupActivo = null;

//        TarjetaCredito
        tc = null;

        MedioPagoBean medioPagoSeleccionado = null;
        if (p_lectura_tarjeta_manual.isEsLecturaDesdeCedula()) {
            medioPagoSeleccionado = TarjetaCredito.getBINMedioPagoNumero(p_lectura_tarjeta_manual.getTarjetaLeida());
            if (medioPagoSeleccionado == null) {
                crearError("Tarjeta de cliente asociada a medio de pago no disponible");
                tc = null;
                return tc;
            }
        } else { // Lectura normal
            medioPagoSeleccionado = medioPago;
        }

        if (p_lectura_tarjeta_manual.getTarjetaLeida() != null) {
            tc = TarjetaCreditoBuilder.create(medioPagoSeleccionado,
                    p_lectura_tarjeta_manual.getTarjetaLeida(),
                    p_lectura_tarjeta_manual.getCaducidad(),
                    p_lectura_tarjeta_manual.getCvv(),
                    p_lectura_tarjeta_manual.isEsLecturaDesdeCedula());

            if (!Sesion.getDatosConfiguracion().isModoDesarrollo() && !p_lectura_tarjeta_manual.isEsLecturaDesdeCedula()
                    && Sesion.isSukasa()
                    && !medioPago.equals(tc.getBINMedioPago())) {
                crearError("Selección de tarjeta incorrecta.");
                tc = null;
            }
        }

        return tc;
    }

    public String crearVentanaTarjetaAfilado() throws SinPermisosException {
        p_lectura_tarjeta_afiliado.limpiarFormulario();
        popupActivo = v_lectura_tarjeta_afiliado;
        v_lectura_tarjeta_afiliado.setVisible(true);
        popupActivo = null;
        String tc = null;
        try {
            tc = p_lectura_tarjeta_afiliado.autenticacionExitosa();
        } catch (MedioPagoException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tc;

    }

    public void crearVentanaMenuTarjetas() {
        popupActivo = v_menu_tarjetas;
        p_menu_tarjetas.iniciaVista();
        v_menu_tarjetas.setVisible(true);
        popupActivo = null;
    }

    public void crearVentanaMenuBonosEfectivo() {
        popupActivo = v_menu_tarjetas;
        p_menu_tarjetas.iniciaVista();
        v_menu_tarjetas.setVisible(true);
        popupActivo = null;
    }

    public static String crearVentanaLecturaTajeta() {
        String res = null;
        p_lectura_tarjeta.iniciaVista();
        p_lectura_tarjeta.iniciaFoco();
        v_lectura_tarjeta.setVisible(true);
        if (p_lectura_tarjeta.getBine() != null && !p_lectura_tarjeta.getBine().isEmpty()) {
            res = p_lectura_tarjeta.getBine();
        }
        return res;
    }

    public void irBuscarPlanesNovio() {
        log.info("Se añade La vista buscar Planes novio ");
        JBuscarPlanNovios buscar_plan_novio = new JBuscarPlanNovios();
        addView(buscar_plan_novio, "buscar_plan_novio");
        showView("buscar_plan_novio");

    }

    public void irVentanaDatosAdicionalesPlanNovio() {
        log.info("Se añade La vista datos adicionales del plan novio ");
        if (!vistasActivas.containsKey("datos_plan_novio")) {
            JDatosPlanNovio datos_plan_novio = new JDatosPlanNovio();
            addView(datos_plan_novio, "datos_plan_novio");
        }
        showView("datos_plan_novio");
    }

    public void irMostrarPlanNovio() {
        log.info("Se añade La vista mostrar el plan novio ");
        if (!vistasActivas.containsKey("mostrar_plan_novio")) {
            JMostrarPlanNovio mostrar_plan_novio = new JMostrarPlanNovio();
            addView(mostrar_plan_novio, "mostrar_plan_novio");
        }
        showView("mostrar_plan_novio");
    }

    public void irPantallaGuiaRemision() {
        log.info("Se añade La vista guia de remision ");
        if (!vistasActivas.containsKey("guia_remision")) {
            JNuevaGuiaRemision guia_remision = new JNuevaGuiaRemision();
            addView(guia_remision, "guia_remision");
        }
        showView("guia_remision");
    }

    public void irPantallaGuiaRemision2() {
        log.info("Se añade La vista guia de remision - paso 2");
        if (!vistasActivas.containsKey("guia_remision2")) {
            JNuevaGuiaRemision2 guia_remision2 = new JNuevaGuiaRemision2();
            addView(guia_remision2, "guia_remision2");
        }
        showView("guia_remision2");
    }

    public void irPlanNoviosPagoBonos() {
        Contador.setContador(0);
        CardLayout cl = (CardLayout) (jPanel_centro.getLayout());
        if (vistasActivas.containsKey("mostrar_plan_novio") && vistasActivas.get("mostrar_plan_novio") instanceof JMostrarPlanNovio) {
            cl.show(jPanel_centro, "mostrar_plan_novio");
            JMostrarPlanNovio panel = (JMostrarPlanNovio) vistasActivas.get("mostrar_plan_novio");
            panel.realizarAbono();
        }
    }

    void mostrarPantallaDesbloqueo(JDialog contenedor) {
        exitoDesbloqueo = false;
        bloqueador = contenedor;
        t_usuario.requestFocus();
        bloqueador.setVisible(false);
        if (v_dialog_autorizar != null && v_dialog_autorizar.isVisible()) {
            v_dialog_autorizar.setVisible(false);
        }
        t_usuario.requestFocus();

        popupActivo = v_solicitar_identificacion;
        v_solicitar_identificacion.setVisible(true);
        popupActivo = null;
        if (!exitoDesbloqueo) {
            reseteaPantallaDesbloqueo();
            popupActivo = bloqueador;
            bloqueador.setVisible(true);
            popupActivo = null;
        }
    }

    private void reseteaPantallaDesbloqueo() {
        t_usuario.requestFocus();
        l_error.setText("");
        t_usuario.setText("");
        t_password.setText("");
    }

    public void cerrarMenu() {
        if (v_menu.isVisible()) {
            v_menu.setVisible(false);
        }
    }

    public static Object crearVentanaIngresarValor(int tipo, String mensaje) {
        return crearVentanaIngresarValor(tipo, mensaje, new BigDecimal(Integer.MAX_VALUE));
    }

    public static BigDecimal crearVentanaIngresarValorDecimal(String mensaje, BigDecimal valorMaximo) {
        return (BigDecimal) crearVentanaIngresarValor(JIngresarValor.TIPO_DECIMAL, mensaje, valorMaximo);
    }

    public static Integer crearVentanaIngresarValorEntero(String mensaje, BigDecimal valorMaximo) {
        try {
            return ((BigDecimal) crearVentanaIngresarValor(JIngresarValor.TIPO_NUMERICO, mensaje, valorMaximo)).intValue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     *
     * @param mensaje
     * @param uidTicket
     * @param valorMaximo
     * @return
     */
    public static Integer crearVentanaIngresarOTP(String mensaje, String uidTicket, BigDecimal valorMaximo) {
        try {
            Integer valor = ((BigDecimal) crearVentanaIngresarValor(JIngresarValor.TIPO_NUMERICO, mensaje, valorMaximo)).intValue();

            ServicioCodigoOTP.validarOTP(valor.longValue(), uidTicket);
            return valor;
        } catch (Exception e) {
            return null;
        }
    }

    private static Object crearVentanaIngresarValor(int tipo, String mensaje, BigDecimal valorMaximo) {
        Object res = null;
        p_ingresar_valor.iniciaVista();
        p_ingresar_valor.iniciaFoco();
        p_ingresar_valor.setValorMaximo(valorMaximo);
        p_ingresar_valor.setTipoValor(tipo);
        p_ingresar_valor.setMensaje(mensaje);
        popupActivo = v_ingresar_valor;
        v_ingresar_valor.setVisible(true);
        popupActivo = null;
        res = p_ingresar_valor.getValor();
        return res;
    }

    public String crearVentanaMail() throws SinPermisosException {
        popupActivo = v_envio_mail;
        v_envio_mail.setVisible(true);
        popupActivo = null;
        String tc = null;
        tc = p_envia_mail.autenticacionMail();

        return tc;

    }

    protected int crearVentanaGarantiaExtendida(int maximoValor) {
        int res = 0;
        do {
            BigDecimal resAux = (BigDecimal) crearVentanaIngresarValor(JIngresarValor.TIPO_NUMERICO, "<html>Ingresar número de Garantías Extendidas</br> a comprar</html>", new BigDecimal(maximoValor));
            if (resAux != null) {
                res = resAux.intValue();
            }
            if (res > maximoValor) {
                crearError("El máximo número de garantías que puede añadir para la línea es " + maximoValor);
            }
        } while (res > maximoValor);
        return res;
    }

    protected static void crearVentanaComprobarAsociarFactura(Integer modo) {
        JGarantia.setModo(modo);
        p_garantia.iniciaVista();
        popupActivo = v_garantia;
        v_garantia.setVisible(true);
        popupActivo = null;
    }

    public static FacturacionTicketBean crearVentanaFacturacion(boolean datosRellenos, TicketS ticket) {
        FacturacionTicketBean res = null;

        p_facturacion.iniciaVista(datosRellenos, ticket);
        v_facturacion.setLocationRelativeTo(null);
        popupActivo = v_facturacion;
        v_facturacion.setVisible(true);
        popupActivo = null;
        if (!p_facturacion.isCancelado()) {
            res = p_facturacion.getObjetofacturacion();
        }
        return res;
    }

    public void crearVentanaFlashVentas() {
        log.info("Creando Ventana Flash de Ventas ");
        v_flash_ventas.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_flash_ventas.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        p_flash_ventas.setVentana_padre(this); //valdría solo en el constructor
        v_flash_ventas.setLocationRelativeTo(null);
        v_flash_ventas.setResizable(false);
        p_flash_ventas.iniciaVista();
        popupActivo = v_flash_ventas;
        v_flash_ventas.setVisible(true);
        popupActivo = null;
    }

    public void crearVentanaAnulaciones() {
        try {
            String autorizador = this.compruebaAutorizacion(Operaciones.ANULAR_DOCUMENTOS);
            Sesion.setAutorizadorAnulacion(autorizador);
            v_anulaciones.setLocationRelativeTo(null);
            v_anulaciones.setResizable(false);
            v_anulaciones.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
            v_anulaciones.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            p_anulaciones.setVentana_padre(JPrincipal.getInstance());
            if (!v_anulaciones.isUndecorated()) {
                v_anulaciones.setUndecorated(true);
            }
            v_anulaciones.setVisible(true);
            p_anulaciones.iniciaVista();
        } catch (SinPermisosException e) {
            log.debug("El usuario no tiene permisos para anular documentos");
        } catch (Exception e) {
            log.error("Error Insertando la anulación :" + e.getMessage(), e);
            this.crearError("Se produjo un error al anular el documento");
        }
    }

    public static void crearVentanaPuntos(TicketS ticket) {
        if (ticket.getPuntosTicket().getPuntosAcumulados() == 0) {
            return;
        }
        String texto = "Con esta venta usted acumula " + ticket.getPuntosTicket().getPuntosAcumulados() + " puntos. Puede aceptar o ceder los puntos a otro cliente";
        String textoAcumular = "Aceptar";// En el caso del cliente generico sería para no acumular
        String textoAcumularOroCliente = "Ceder";

        if (ticket.getCliente().isClienteGenerico()) {
            texto = "Con esta venta usted acumula " + ticket.getPuntosTicket().getPuntosAcumulados() + " puntos. ¿Desea ceder los puntos a algún cliente?";
            textoAcumular = "No";
            textoAcumularOroCliente = "Sí";
        }
        // Preguntamos como desea acumular el cliente
        boolean acumularPropio = JPrincipal.getInstance().crearVentanaConfirmacion(texto, textoAcumular, textoAcumularOroCliente);

        if (acumularPropio) { // Pulsamos en acumular
            ticket.getPuntosTicket().setClienteAcumulacion(ticket.getCliente());
        } else { // Pulsamos en acumular a otro cliente
            Cliente cli = JPrincipal.getInstance().crearVentanaIdentificacion();
            if (cli != null) {
                ticket.getPuntosTicket().setClienteAcumulacion(cli);
            } else {
                // Si no ha seleccionado ningún cliente, volvemos a hacer la pregunta
                crearVentanaPuntos(ticket);
                return;
            }
        }
    }

    public Cliente crearVentanaIdentificacion() {
        Cliente res = null;
        p_autentificarCliente.limpiarFormulario();
        p_autentificarCliente.iniciaFoco();
        popupActivo = v_autentificarCliente;
        v_autentificarCliente.setVisible(true);
        popupActivo = null;
        res = p_autentificarCliente.getCliente();

        return res;
    }

    public void crearVentanaLecturaTarjetaSupermMaxi(Cliente cliente) {
        if (Sesion.isSukasa()
                || (!cliente.isSocio() && !cliente.isEmpleado())) {
            if (crearVentanaConfirmacion(msgPreguntaSupermaxi, "Si", "No")) {
                p_tarjeta_supermaxi.limpiarFormulario();
                popupActivo = v_tarjeta_supermaxi;
                v_tarjeta_supermaxi.setVisible(true);
                popupActivo = null;
                ITarjetaAfiliacion tarjetaAfiliacion = p_tarjeta_supermaxi.getTarjetaLeida();
                if (tarjetaAfiliacion != null) {
                    cliente.setAplicaTarjetaAfiliado(true);
                    cliente.setTarjetaAfiliacion(tarjetaAfiliacion);
                } else {
                    cliente.setAplicaTarjetaAfiliado(false);
                }
            } else {
                cliente.setAplicaTarjetaAfiliado(false);
            }
        }
    }

    public String crearVentanaLecturaManualGiftCard() {
        p_lectura_giftcard.iniciaVista();
        popupActivo = v_lectura_giftcard;
        v_lectura_giftcard.setVisible(true);
        popupActivo = null;
        return p_lectura_giftcard.getNumeroGiftcard();
    }

    public IVista getView(String view) {
        IVista res = null;
        if (vistasActivas.containsKey(view) && vistasActivas.get(view) != null) {
            res = (IVista) vistasActivas.get(view);
        }
        return res;
    }

    public boolean crearVentanaInformacion(String msg, int altoTexto) {
        return crearVentanaConfirmacion(msg, altoTexto, true);
    }

    public boolean crearVentanaConfirmacion(String msg, int altoTexto) {
        return crearVentanaConfirmacion(msg, altoTexto, false);
    }

    /*
     * Metodo auxiliar para la creación de ventanas de diálogo
     */
    private boolean crearVentanaConfirmacion(String desError, int altoTexto, boolean ocultarBotones) {
        // INICIO
        JDialog new_dialog_error = new JDialog();
        new_dialog_error.setIconImage(iconoTransparente.getImage());

        JInformacion new_p_informacion = new JInformacion();
        new_p_informacion.setDesError(desError, altoTexto);
        new_p_informacion.setContenedor(new_dialog_error);
        if (ocultarBotones) {
            new_p_informacion.ocultarBotones();
        }

        jf_informacion.setIconImage(iconoTransparente.getImage());
        new_dialog_error.setAlwaysOnTop(true);
        new_dialog_error.setResizable(true);

        new_dialog_error.setModal(true);
        new_dialog_error.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        new_dialog_error.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);

        javax.swing.GroupLayout dialog_errorLayout = new javax.swing.GroupLayout(new_dialog_error.getContentPane());
        new_dialog_error.getContentPane().setLayout(dialog_errorLayout);
        dialog_errorLayout.setHorizontalGroup(
                dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(dialog_errorLayout.createSequentialGroup().addContainerGap().addComponent(new_p_informacion, JInformacion.ANCHO_DEFECTO, JInformacion.ANCHO_DEFECTO, JInformacion.ANCHO_DEFECTO).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        dialog_errorLayout.setVerticalGroup(
                dialog_errorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialog_errorLayout.createSequentialGroup().addComponent(new_p_informacion, JInformacion.ALTO_DEFECTO + altoTexto, JInformacion.ALTO_DEFECTO + altoTexto, JInformacion.ALTO_DEFECTO + altoTexto).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        if (desError == null || desError.isEmpty()) {
            desError = " Se ha producido un error en la aplicación ";
        }
        // La pantalla será 40 pixels mas alta que el contenedor
        int margenContenedor = 40;

        new_dialog_error.setMaximumSize(new java.awt.Dimension(JInformacion.ANCHO_DEFECTO, JInformacion.ALTO_DEFECTO + altoTexto + margenContenedor));
        new_dialog_error.setMinimumSize(new java.awt.Dimension(JInformacion.ANCHO_DEFECTO, JInformacion.ALTO_DEFECTO + altoTexto + margenContenedor));
        new_dialog_error.setPreferredSize(new java.awt.Dimension(JInformacion.ANCHO_DEFECTO, JInformacion.ALTO_DEFECTO + altoTexto + margenContenedor));
        new_dialog_error.setSize(JInformacion.ANCHO_DEFECTO, JInformacion.ALTO_DEFECTO + altoTexto + margenContenedor);

        new_dialog_error.setLocationRelativeTo(null);
        popupActivo = new_dialog_error;
        new_dialog_error.setVisible(true);
        popupActivo = null;

        // tenemos que establecer el texto tras sacar la ventana para que el render sepa el tamaño que va a ocumar el texto
        return new_p_informacion.isAceptado();

    }

    public static JDialog getPopupActivo() {
        return popupActivo;
    }

    public static JDialog setPopupActivo(JDialog popup) {
        return popupActivo = popup;
    }

    public static TicketId crearVentanaLecturaIdFactura(String titulo) {
        p_lectura_factura.limpiarFormulario();
        p_lectura_factura.setTitulo(titulo);
        popupActivo = v_lectura_factura;
        v_lectura_factura.setVisible(true);
        popupActivo = null;
        if (!p_lectura_factura.isCancelado()) {
            TicketId id = new TicketId();
            id.setCodAlmacen(p_lectura_factura.getCodAlm());
            id.setCodCaja(p_lectura_factura.getCodCaja());
            id.setIdTicket(new Long(p_lectura_factura.getId()));
            return id;
        } else {
            log.warn("crearVentanaLecturaIdFactura() - Se ha cancelado la lectura de identificación de documento");
        }
        return null;
    }

    /**
     *
     * @param mensaje
     * @param uidTicket
     * @param pagoCredito
     * @return
     */
    public EnumEstadosValidacionOtp crearVentanaIngresarCodigoOTP(String mensaje, String uidTicket, PagoCredito pagoCredito) {

        p_ingresar_cod_otp.iniciaVista();
        p_ingresar_cod_otp.iniciaFoco();
        p_ingresar_cod_otp.setMensaje(mensaje);
        v_ingresarCodOTP.setAlwaysOnTop(true);
        p_ingresar_cod_otp.setUidTicket(uidTicket);
        p_ingresar_cod_otp.setPagoCredito(pagoCredito);
        popupActivo = v_ingresarCodOTP;
        v_ingresarCodOTP.setVisible(true);
        popupActivo = null;

        return p_ingresar_cod_otp.getEstadoValido();
    }

}
