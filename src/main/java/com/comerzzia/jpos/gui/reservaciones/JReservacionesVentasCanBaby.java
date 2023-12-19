/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JReservacionesVentas.java
 *
 * Created on 28-jun-2011, 16:35:37
 */
package com.comerzzia.jpos.gui.reservaciones;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.cotizacion.CotizacionDTO;
import com.comerzzia.jpos.dto.cotizacion.TiendaDTO;
import com.comerzzia.jpos.dto.supermaxi.RespuestaSupermaxiDTO;
import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.codigosBarra.NotAValidBarCodeException;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioMostrar;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.gui.IBusquedasArticulos;
import com.comerzzia.jpos.gui.IVenta;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JBuscar;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.eventos.SeleccionaFocusListener;
import com.comerzzia.jpos.gui.modelos.TicketTableCellRenderer;
import com.comerzzia.jpos.gui.modelos.TicketTableModel;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifaArticuloNotFoundException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.reservaciones.excepciones.ArticuloEnReservaNotFoundException;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionBean;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticuloException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueoFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifaException;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.interfazventa.OpcionesVentaManager;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponNotValidException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.cotizaciones.CotizacionException;
import com.comerzzia.jpos.servicios.cotizaciones.CotizacionesService;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendida;
import com.comerzzia.jpos.servicios.kit.KitException;
import com.comerzzia.jpos.servicios.listapda.ListaPDAException;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.util.ClienteRest;
import com.comerzzia.util.imagenes.Imagenes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.awt.Dialog;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.KeyAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Pantalla de Ventas
 *
 * @author MGRI
 */
public class JReservacionesVentasCanBaby extends JPanelImagenFondo implements IVista, IVenta, KeyListener, IViewerValidationFormError, IBusquedasArticulos, IPagoImporteCall {
    //opciones de configuracion

    public static int MODO_PAGO_ARTICULOS = 0;
    public static int MODO_ADD = 1;
    public static int MODO_BBSH = 2;
    public static int MODO_PAGO_ABONOS = 3;
    public static int MODO_AMPLIACION_ABONO_INICIAL = 4;
    public static int MODO_ADD_PLAN_NOVIOS = 5;
    public static int MODO_PAGO_ARTICULOS_PLAN_NOVIOS = 6;
    public static int MODO_COTIZACION = 7;
    int modo;
    String mail = null;
    private static final Logger log = Logger.getMLogger(JReservacionesVentasCanBaby.class);
    boolean esNumeroArticulosEitable = true;  // se consulta de la sesion en la creación del panel
    // servicios y variables
    PrintServices ts = PrintServices.getInstance();
    JPrincipal ventana_padre = null;
    ArticulosServices articulosServices = ArticulosServices.getInstance();
    TarifasServices tarifasServices = new TarifasServices();
    // definimos los paneles
    JPanel p_activo = null;
    //Seleccion de columna de la factura
    int lineaSelecionada;
    //Descuentos
    String codigobarra;
    int indexLineaAEditar = -1;
    // Entidades
    Reservacion reservacion;
    TicketS ticketReservacion;
    TicketS ticketNuevaVenta;
    boolean isFacturarOtrosDatosDisponible = true;
    boolean isAmpliacionArticulos = false;
    boolean isAmpliacionAbonoInicial = false;
    boolean aplicaPromociones = false;
    Cliente clienteComprador;
    List<ReservaArticuloBean> addArticulosReserva = null;
    PlanNovioOBJ planNovio = null;
    CotizacionBean cotizacion = null;
    public static final String URL_ENVIO_SUPERMAXI = "/cor/enviarEmailCotizacion";

    /**
     * Creates new form JReservacionesVentas
     */
    public JReservacionesVentasCanBaby() {
        super();
        initComponents();

        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);
    }

    /**
     * Constructor para añadir artículos desde un Plan novios
     *
     * @param ventana_padre
     */
    public JReservacionesVentasCanBaby(JPrincipal ventana_padre, PlanNovioOBJ planNovioOBJ, Cliente cliente, int modo) {
        super();
        this.planNovio = planNovioOBJ;
        this.ventana_padre = ventana_padre;
        this.reservacion = null;
        this.clienteComprador = cliente;
        this.aplicaPromociones = false;
        this.ticketNuevaVenta = new TicketS(false);

        this.modo = modo;

        if (modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
            ticketNuevaVenta.setCliente(cliente);
        } else if (modo == MODO_ADD_PLAN_NOVIOS) {
            ticketNuevaVenta.setCliente(planNovio.getPlan().getNovia());
        }

        try {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
        }
        initComponents();
        iniciaComponentes();

        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);
    }

    /**
     * Constructor
     *
     * @param ventana_padre
     */
    public JReservacionesVentasCanBaby(JPrincipal ventana_padre, Reservacion reservacionEnt, int modo) {
        super();
        this.ventana_padre = ventana_padre;
        this.reservacion = reservacionEnt;
        this.clienteComprador = reservacionEnt.getInvitadoActivo();
        this.ticketReservacion = reservacion.getTicket();
        this.aplicaPromociones = reservacion.getReservacion().getReservaTipo().getPermiteReservarPromocionados();
        this.ticketNuevaVenta = new TicketS(reservacion);
        this.modo = modo;

//        FACTURACIÓN DE INVITADOS 
//        if (!reservacion.getReservacion().getCodTipo().isPermiteFacturarInvitados()){
//            isFacturarOtrosDatosDisponible = false;
//        }        
//                       
// Carga de imagen de fondo   
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
        }
        initComponents();
        iniciaComponentes();

        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);

    }

    public JReservacionesVentasCanBaby(JPrincipal ventana_padre, CotizacionBean cotizacion, Cliente clienteComprador, Vendedor vendedor, int modo) {
        super();
        this.ventana_padre = ventana_padre;
        this.cotizacion = cotizacion;
        this.ticketNuevaVenta = new TicketS(false);
        this.clienteComprador = clienteComprador;
        ticketNuevaVenta.setCliente(clienteComprador);
        ticketNuevaVenta.setVendedor(vendedor);

        this.modo = modo;

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
        }
        initComponents();
        iniciaComponentes();

        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);
    }

    public void iniciaComponentes() {
        // Textos
        if (modo == MODO_ADD || modo == MODO_ADD_PLAN_NOVIOS || modo == MODO_ADD_PLAN_NOVIOS) {
            b_ventas_pagos.setText("<html><center>Añadir<br>Artículos<br/>F9</center></html>");
            b_ventas_datoscliente.setText("<html><center>Cancelar <br>Selección <br/>F2</center></html>");
        } else if (modo == MODO_PAGO_ARTICULOS || modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
            b_ventas_pagos.setText("<html><center>Realizar<br>Pagos<br/>F9</center></html>");
            b_ventas_datoscliente.setText("<html><center>Cancelar <br>Compra<br/>F2</center></html>");
        } else if (modo == MODO_COTIZACION) {
            b_ventas_pagos.setText("<html><center><br>Previsualización<br/>F9</center></html>");
            b_ventas_datoscliente.setText("<html><center>Cancelar <br>Cotización<br/>F2</center></html>");
//            b_datos_facturacion.setVisible(false);
            b_datos_facturacion.setText("<html><center> Enviar <br>Mail<br/>F5</center></html>");;
        }

        Imagenes.cambiarImagenPublicidad(jLabel30);

        // Iconos transparentes a las ventanas
        URL myurl;
        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        f_pagos2.setIconImage(icon.getImage());
        v_buscar_articulo.setIconImage(icon.getImage());
        v_datos_envio.setIconImage(icon.getImage());
        v_descuento_linea.setIconImage(icon.getImage());
        v_seleccion_linea.setIconImage(icon.getImage());
        v_pagos.setIconImage(icon.getImage());
        v_importe_a_pagar.setIconImage(icon.getImage());

        t_factura.setTableHeader(null);
        t_introduccionArticulos.getT_Codigo().addKeyListener(this);
        sp_t_factura.getViewport().setOpaque(false);
        sp_t_factura.setBorder(null);
        p_buscar_articulo.setVentana_padre((IVista) this);
        p_buscar_articulo.setContenedor(v_buscar_articulo);

        // Eventos de foco
        SeleccionaFocusListener sFL = new SeleccionaFocusListener();
        t_descuento_valor.addFocusListener(sFL);

        //como quitar el borde al scrollpanel
        Border empty = new EmptyBorder(0, 0, 0, 0);
        sp_t_factura.setBorder(empty);
        sp_t_factura.setViewportBorder(empty);

        // Inicialización de ventana de Datos de envio
        p_datos_envio.setVentana_padre(ventana_padre);
        p_datos_envio.setContenedor(v_datos_envio);
        v_datos_envio.setLocationRelativeTo(null);

        // validación del cuadro de texto de descuentos
        t_descuento_valor.addValidador(new ValidadorEntero(0, 100), this);

        registraEventoBuscar();
        crearAccionFocoTabla(this, t_factura, KeyEvent.VK_T, InputEvent.CTRL_MASK);

    }

    /**
     * Función de Inicialización al cambiar de vista
     *
     */
    @Override
    public void iniciaVista() {
        addFunctionKeys();
        ActionMap am = v_seleccion_linea.getRootPane().getActionMap();
        InputMap im = v_seleccion_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                cancelarOperacion();
            }
        };

        lineasTicket.setLineas(ticketNuevaVenta.getLineas().getLineas());

        if (isModoPagoArticulosPlanNovios() || isModoAdd() || isModoAddPlanNovios() || isModoCotizacion()) {
            b_ventas_edicion.setVisible(true);
        } else {
            b_ventas_edicion.setVisible(false);
        }

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);
        // mostramos el cliente seleccionado

        // inicializamos información de cliente
        lb_cliente_tipo_socio.setText("");
        lb_tarjeta.setIcon(null);

        if (isModoAddPlanNovios()) {
            t_venta_cliente.setText(planNovio.getPlan().getNovia().getNombreVenta());
        } else {
            t_venta_cliente.setText(clienteComprador.getNombreVenta());
            if (clienteComprador.isSocio() && clienteComprador.getTipoAfiliado() != null) {
                TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(clienteComprador.getTipoAfiliado());
                if (tipoAfiliado != null) {
                    lb_cliente_tipo_socio.setText("Tipo Afiliado " + tipoAfiliado.getDesTipoAfiliado());
                    if (tipoAfiliado.getImagenTarjetaAfiliado() != null) {
                        lb_tarjeta.setIcon(tipoAfiliado.getImagenTarjetaAfiliado());
                    }
                }
            } else {
                lb_cliente_tipo_socio.setText("");
                lb_tarjeta.setIcon(null);
            }
        }
        t_venta_cliente.setHorizontalAlignment(JLabel.CENTER);

        // Iniciamos la tabla de tickets
        // Data model y renderer de la tabla
        t_factura.setModel(new TicketTableModel(ticketNuevaVenta.getLineas()));
        t_factura.setDefaultRenderer(Object.class, new TicketTableCellRenderer());

        // Establecemos el modo en la introduccción de Artículo.
        t_introduccionArticulos.setModoCantidad(TicketTableCellRenderer.isModoConCantidad());
        // Refrescamos
        refrescaTablaTicket();

        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();
        t_introduccionArticulos.reset();

        if (modo == MODO_COTIZACION) {
            l_factura_cabecera_id.setText("COTIZACIÓN");
            l_factura_cabecera1.setText("");
        } else if (modo != MODO_ADD_PLAN_NOVIOS && modo != MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
            l_factura_cabecera_id.setText(reservacion.getReservacion().getReservaTipo().getDesTipo());
        } else {
            l_factura_cabecera_id.setText("PLAN NOVIOS");
        }

        // comprobamos si el cliente puede subir su nivel de afiliación con esta compra
        //mostrarEtiquetasSocio(false);
        jLabel_compra_socio.setVisible(false);
        /* Solicitado por Miguel Gomez el 01/10/2018
        if (ticketNuevaVenta.getCliente() != null && ticketNuevaVenta.getCliente().isSocio()) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(ticketNuevaVenta.getCliente().getTipoAfiliado());
            if (tipoAfiliado != null && tipoAfiliado.getSiguienteNivel() != null && tipoAfiliado.isCompraSiguienteNivelDefinido()) {
                jLabel_compra_socio.setVisible(true);
                jLabel_compra_socio.setText("Con una compra de $ " + tipoAfiliado.getCompraSiguienteNivel() + " será socia " + tipoAfiliado.getSiguienteNivel().getDesTipoAfiliado());
            }
        }
         */
        if (isModoAddPlanNovios() || isModoAdd()) {
            b_datos_facturacion.setEnabled(false);
        } else {
            b_datos_facturacion.setEnabled(true);
        }
        v_pagos.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        f_pagos2 = new javax.swing.JFrame();
        v_seleccion_linea = new javax.swing.JDialog();
        p_descuento_principal = new javax.swing.JPanel();
        p_seleccion_cb = new javax.swing.JPanel();
        t_seleccion_cb = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel1 = new javax.swing.JLabel();
        lb_error = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        t_seleccion_cb1 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lineasTicket = new com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket();
        v_buscar_articulo = new javax.swing.JDialog();
        p_buscar_articulo = new JBuscar(this);
        v_descuento_linea = new javax.swing.JDialog();
        P_descuento_valor = new javax.swing.JPanel();
        t_descuento_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel4 = new javax.swing.JLabel();
        o_recogida_posterior = new javax.swing.JCheckBox();
        o_env_adomicilio = new javax.swing.JCheckBox();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error_descuento = new javax.swing.JLabel();
        lb_error_ml = new com.comerzzia.jpos.gui.components.JMultilineLabel();
        v_datos_envio = new javax.swing.JDialog();
        p_datos_envio = new com.comerzzia.jpos.gui.JDatosEnvio();
        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        v_importe_a_pagar = new javax.swing.JDialog();
        p_importe_a_pagar = new com.comerzzia.jpos.gui.reservaciones.JReservacionesImportePago();
        v_editar_cantidad = new javax.swing.JDialog();
        p_editar_cantidad = new com.comerzzia.jpos.gui.devoluciones.JIntroducirCantidad();
        jPanel1 = new javax.swing.JPanel();
        p_principal = new javax.swing.JPanel();
        m_ventas = new javax.swing.JPanel();
        b_ventas_datoscliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_pagos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_appal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_otro_local = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_seleccion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_datos_facturacion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_edicion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_descuentos = new javax.swing.JPanel();
        jLabel_compra_socio = new javax.swing.JLabel();
        p_imagen_airticulo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        p_ingreso_articulos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t_introduccionArticulos = new com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp();
        jLabel_numArticulos = new javax.swing.JLabel();
        p_fatura = new javax.swing.JPanel();
        l_factura_cabecera_id = new javax.swing.JLabel();
        l_total = new javax.swing.JLabel();
        sp_t_factura = new javax.swing.JScrollPane();
        t_factura = new javax.swing.JTable();
        l_v_total = new javax.swing.JLabel();
        l_factura_cabecera1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        p_idcliente = new javax.swing.JPanel();
        t_venta_cliente = new javax.swing.JLabel();
        lb_tarjeta = new javax.swing.JLabel();
        lb_cliente_tipo_socio = new javax.swing.JLabel();
        p_publicidad = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();

        f_pagos2.setAlwaysOnTop(true);
        f_pagos2.setMinimumSize(new java.awt.Dimension(983, 591));
        f_pagos2.setResizable(false);

        javax.swing.GroupLayout f_pagos2Layout = new javax.swing.GroupLayout(f_pagos2.getContentPane());
        f_pagos2.getContentPane().setLayout(f_pagos2Layout);
        f_pagos2Layout.setHorizontalGroup(
            f_pagos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 983, Short.MAX_VALUE)
        );
        f_pagos2Layout.setVerticalGroup(
            f_pagos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );

        v_seleccion_linea.setAlwaysOnTop(true);
        v_seleccion_linea.setMinimumSize(new java.awt.Dimension(450, 200));
        v_seleccion_linea.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_seleccion_linea.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        p_descuento_principal.setMaximumSize(new java.awt.Dimension(450, 200));
        p_descuento_principal.setLayout(new java.awt.CardLayout());

        p_seleccion_cb.setMaximumSize(new java.awt.Dimension(450, 200));
        p_seleccion_cb.setMinimumSize(new java.awt.Dimension(450, 200));
        p_seleccion_cb.setName("seleccion_cb"); // NOI18N
        p_seleccion_cb.setOpaque(false);

        t_seleccion_cb.setName("descuento_cb"); // NOI18N
        t_seleccion_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_seleccion_cbActionPerformed(evt);
            }
        });
        t_seleccion_cb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_seleccion_cbKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_seleccion_cbKeyTyped(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ingrese el código de barras del artículo que desea buscar");

        lb_error.setFont(lb_error.getFont().deriveFont(lb_error.getFont().getStyle() | java.awt.Font.BOLD));
        lb_error.setForeground(new java.awt.Color(255, 51, 0));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Ingrese el código interno de artículo que desea buscar");

        t_seleccion_cb1.setName("descuento_cb"); // NOI18N
        t_seleccion_cb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_seleccion_cb1ActionPerformed(evt);
            }
        });
        t_seleccion_cb1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_seleccion_cb1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_seleccion_cb1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout p_seleccion_cbLayout = new javax.swing.GroupLayout(p_seleccion_cb);
        p_seleccion_cb.setLayout(p_seleccion_cbLayout);
        p_seleccion_cbLayout.setHorizontalGroup(
            p_seleccion_cbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_seleccion_cbLayout.createSequentialGroup()
                .addGroup(p_seleccion_cbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_seleccion_cbLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(p_seleccion_cbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(p_seleccion_cbLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(t_seleccion_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(p_seleccion_cbLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(t_seleccion_cb, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        p_seleccion_cbLayout.setVerticalGroup(
            p_seleccion_cbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_seleccion_cbLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_seleccion_cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_seleccion_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        p_descuento_principal.add(p_seleccion_cb, "seleccion_cb");

        javax.swing.GroupLayout v_seleccion_lineaLayout = new javax.swing.GroupLayout(v_seleccion_linea.getContentPane());
        v_seleccion_linea.getContentPane().setLayout(v_seleccion_lineaLayout);
        v_seleccion_lineaLayout.setHorizontalGroup(
            v_seleccion_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_seleccion_lineaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_descuento_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(514, 514, 514))
        );
        v_seleccion_lineaLayout.setVerticalGroup(
            v_seleccion_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_seleccion_lineaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_descuento_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(357, Short.MAX_VALUE))
        );

        lineasTicket.setLineas(new LinkedList());

        v_buscar_articulo.setAlwaysOnTop(true);
        v_buscar_articulo.setMinimumSize(new java.awt.Dimension(1010, 630));
        v_buscar_articulo.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_buscar_articulo.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_buscar_articulo.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_buscar_articuloWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        p_buscar_articulo.setPreferredSize(new java.awt.Dimension(1010, 630));

        javax.swing.GroupLayout v_buscar_articuloLayout = new javax.swing.GroupLayout(v_buscar_articulo.getContentPane());
        v_buscar_articulo.getContentPane().setLayout(v_buscar_articuloLayout);
        v_buscar_articuloLayout.setHorizontalGroup(
            v_buscar_articuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_articuloLayout.createSequentialGroup()
                .addComponent(p_buscar_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        v_buscar_articuloLayout.setVerticalGroup(
            v_buscar_articuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_buscar_articuloLayout.createSequentialGroup()
                .addComponent(p_buscar_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        v_descuento_linea.setAlwaysOnTop(true);
        v_descuento_linea.setMinimumSize(new java.awt.Dimension(357, 220));
        v_descuento_linea.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_descuento_linea.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_descuento_linea.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_descuento_lineaWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        P_descuento_valor.setMaximumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setMinimumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setName("descuento_valor"); // NOI18N
        P_descuento_valor.setPreferredSize(new java.awt.Dimension(357, 200));

        t_descuento_valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_descuento_valorKeyPressed(evt);
            }
        });

        jLabel4.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.editar.descuento").charAt(0));
        jLabel4.setLabelFor(t_descuento_valor);
        jLabel4.setText("Descuento:");

        o_recogida_posterior.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.editar.recogidaposterior").charAt(0));
        o_recogida_posterior.setText("Recogida Posterior");
        o_recogida_posterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_recogida_posteriorActionPerformed(evt);
            }
        });
        o_recogida_posterior.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                o_recogida_posteriorKeyTyped(evt);
            }
        });

        o_env_adomicilio.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.editar.envio").charAt(0));
        o_env_adomicilio.setText("Envio a Domicilio");
        o_env_adomicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_env_adomicilioActionPerformed(evt);
            }
        });
        o_env_adomicilio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                o_env_adomicilioKeyTyped(evt);
            }
        });

        jb_cancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        jb_cancel1.setText("Cancelar");
        jb_cancel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jb_cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancel1ActionPerformed(evt);
            }
        });
        jb_cancel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_cancel1KeyTyped(evt);
            }
        });

        jb_ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_ok1.setMnemonic('a');
        jb_ok1.setText("Aceptar");
        jb_ok1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jb_ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_ok1ActionPerformed(evt);
            }
        });
        jb_ok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_ok1KeyTyped(evt);
            }
        });

        lb_error_descuento.setForeground(new java.awt.Color(153, 0, 0));
        lb_error_descuento.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lb_error_ml.setForeground(new java.awt.Color(255, 51, 0));
        lb_error_ml.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lb_error_ml.setFont(new java.awt.Font("sansserif", 3, 14)); // NOI18N

        javax.swing.GroupLayout P_descuento_valorLayout = new javax.swing.GroupLayout(P_descuento_valor);
        P_descuento_valor.setLayout(P_descuento_valorLayout);
        P_descuento_valorLayout.setHorizontalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lb_error_ml, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_error_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(o_env_adomicilio)
                            .addComponent(o_recogida_posterior))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        P_descuento_valorLayout.setVerticalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_error_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_error_ml, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(t_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(o_recogida_posterior))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(o_env_adomicilio)
                .addGap(24, 24, 24)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout v_descuento_lineaLayout = new javax.swing.GroupLayout(v_descuento_linea.getContentPane());
        v_descuento_linea.getContentPane().setLayout(v_descuento_lineaLayout);
        v_descuento_lineaLayout.setHorizontalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_descuento_lineaLayout.createSequentialGroup()
                .addComponent(P_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_descuento_lineaLayout.setVerticalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_descuento_lineaLayout.createSequentialGroup()
                .addComponent(P_descuento_valor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        v_datos_envio.setAlwaysOnTop(true);
        v_datos_envio.setMinimumSize(new java.awt.Dimension(400, 498));
        v_datos_envio.setModal(true);
        v_datos_envio.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        v_datos_envio.getContentPane().add(p_datos_envio, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        v_pagos.setAlwaysOnTop(true);
        v_pagos.setMinimumSize(new java.awt.Dimension(1024, 660));
        v_pagos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_pagos.setLocationRelativeTo(null);
        v_pagos.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                v_pagosWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout v_pagosLayout = new javax.swing.GroupLayout(v_pagos.getContentPane());
        v_pagos.getContentPane().setLayout(v_pagosLayout);
        v_pagosLayout.setHorizontalGroup(
            v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_pagosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_pagosLayout.setVerticalGroup(
            v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_pagosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        v_importe_a_pagar.setAlwaysOnTop(true);
        v_importe_a_pagar.setMinimumSize(new java.awt.Dimension(530, 290));
        v_importe_a_pagar.setModal(true);
        v_importe_a_pagar.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        p_importe_a_pagar.setMaximumSize(new java.awt.Dimension(400, 235));
        p_importe_a_pagar.setMinimumSize(new java.awt.Dimension(400, 235));
        p_importe_a_pagar.setContenedor(v_importe_a_pagar);
        p_importe_a_pagar.setVentana_padre(ventana_padre);

        javax.swing.GroupLayout v_importe_a_pagarLayout = new javax.swing.GroupLayout(v_importe_a_pagar.getContentPane());
        v_importe_a_pagar.getContentPane().setLayout(v_importe_a_pagarLayout);
        v_importe_a_pagarLayout.setHorizontalGroup(
            v_importe_a_pagarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_importe_a_pagarLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(p_importe_a_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        v_importe_a_pagarLayout.setVerticalGroup(
            v_importe_a_pagarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_importe_a_pagarLayout.createSequentialGroup()
                .addComponent(p_importe_a_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_editar_cantidad.setAlwaysOnTop(true);
        v_editar_cantidad.setMinimumSize(new java.awt.Dimension(445, 215));

        javax.swing.GroupLayout v_editar_cantidadLayout = new javax.swing.GroupLayout(v_editar_cantidad.getContentPane());
        v_editar_cantidad.getContentPane().setLayout(v_editar_cantidadLayout);
        v_editar_cantidadLayout.setHorizontalGroup(
            v_editar_cantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_editar_cantidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_editar_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_editar_cantidadLayout.setVerticalGroup(
            v_editar_cantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_editar_cantidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_editar_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(0, 51, 153));
        setMaximumSize(new java.awt.Dimension(1094, 734));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p_principal.setFocusable(false);
        p_principal.setMaximumSize(new java.awt.Dimension(1094, 734));
        p_principal.setMinimumSize(new java.awt.Dimension(1094, 734));
        p_principal.setOpaque(false);
        p_principal.setPreferredSize(new java.awt.Dimension(1094, 734));
        p_principal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_ventas.setBackground(new java.awt.Color(255, 255, 255));
        m_ventas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_ventas_datoscliente.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.datos.cliente").charAt(0));
        b_ventas_datoscliente.setText("<html><center>Cancelar <br>Reserva <br/>F2</center></html>");
        b_ventas_datoscliente.setAlignmentY(0.0F);
        b_ventas_datoscliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        b_ventas_datoscliente.setPreferredSize(new java.awt.Dimension(90, 37));
        b_ventas_datoscliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_datosclienteActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_datoscliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 80, 56));

        b_ventas_pagos.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.pagos").charAt(0));
        b_ventas_pagos.setText("<html><center>Realizar<br>Pagos<br/>F9</center></html>");
        b_ventas_pagos.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_pagosActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 160, 55));

        b_ventas_appal.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.menu.principal").charAt(0));
        b_ventas_appal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_ventas_appal.setEnabled(false);
        b_ventas_appal.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_appal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_appalActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_appal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 80, 57));

        b_ventas_otro_local.setText("<html><center>Buscar artículo<br>F8</center></html>");
        b_ventas_otro_local.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_otro_local.setPreferredSize(new java.awt.Dimension(90, 37));
        b_ventas_otro_local.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventasBuscarArticuloActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_otro_local, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 80, 56));

        b_ventas_seleccion.setText("<html><center>Selección<br>Artículo<br/>F7</center></html>");
        b_ventas_seleccion.setActionCommand("<html><center>Selección <br>de Artículo<br/>F7</center></html>");
        b_ventas_seleccion.setAlignmentY(0.0F);
        b_ventas_seleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_seleccionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_seleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 80, 57));

        b_datos_facturacion.setMnemonic('l');
        b_datos_facturacion.setText("<html><center>Datos de<br>Factura<br/>F5</html>");
        b_datos_facturacion.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_datos_facturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_datos_facturacionActionPerformed(evt);
            }
        });
        m_ventas.add(b_datos_facturacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 57));

        b_ventas_edicion.setText("<html><center> Edición <br/>F4</center></html>");
        b_ventas_edicion.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_edicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_edicionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_edicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 80, 56));

        p_principal.add(m_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 320, -1));

        p_descuentos.setOpaque(false);
        p_descuentos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_compra_socio.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel_compra_socio.setForeground(java.awt.Color.blue);
        jLabel_compra_socio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_compra_socio.setFocusable(false);
        p_descuentos.add(jLabel_compra_socio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 360, 20));

        p_principal.add(p_descuentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 440, 90));

        p_imagen_airticulo.setForeground(new java.awt.Color(0, 51, 153));
        p_imagen_airticulo.setOpaque(false);
        p_imagen_airticulo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFocusable(false);
        p_imagen_airticulo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 400, 330));

        p_principal.add(p_imagen_airticulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 440, 350));

        p_ingreso_articulos.setOpaque(false);

        jLabel3.setDisplayedMnemonic('i');
        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel3.setLabelFor(t_introduccionArticulos.getT_Codigo());
        jLabel3.setText("Ingrese Artículo");

        t_introduccionArticulos.setNextFocusableComponent(b_ventas_datoscliente);
        t_introduccionArticulos.setOpaque(false);

        jLabel_numArticulos.setFont(jLabel_numArticulos.getFont());
        jLabel_numArticulos.setForeground(new java.awt.Color(51, 153, 255));
        jLabel_numArticulos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_numArticulos.setText("0 artículos ingresados");
        jLabel_numArticulos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout p_ingreso_articulosLayout = new javax.swing.GroupLayout(p_ingreso_articulos);
        p_ingreso_articulos.setLayout(p_ingreso_articulosLayout);
        p_ingreso_articulosLayout.setHorizontalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(139, 139, 139))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_ingreso_articulosLayout.createSequentialGroup()
                        .addGroup(p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_ingreso_articulosLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel_numArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        p_ingreso_articulosLayout.setVerticalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_numArticulos)
                .addContainerGap())
        );

        p_principal.add(p_ingreso_articulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 600, 280, 90));

        p_fatura.setOpaque(false);
        p_fatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_factura_cabecera_id.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.tabla.facturas").charAt(0));
        l_factura_cabecera_id.setFont(l_factura_cabecera_id.getFont().deriveFont(l_factura_cabecera_id.getFont().getStyle() | java.awt.Font.BOLD));
        l_factura_cabecera_id.setForeground(new java.awt.Color(51, 153, 255));
        l_factura_cabecera_id.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l_factura_cabecera_id.setLabelFor(t_factura);
        l_factura_cabecera_id.setText("ID");
        l_factura_cabecera_id.setFocusable(false);
        l_factura_cabecera_id.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p_fatura.add(l_factura_cabecera_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 210, 20));

        l_total.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        l_total.setText("Total:");
        l_total.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        l_total.setFocusable(false);
        p_fatura.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 620, -1, -1));

        sp_t_factura.setBackground(new java.awt.Color(153, 255, 255));

        t_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        t_factura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        t_factura.setFocusCycleRoot(true);
        t_factura.setGridColor(new java.awt.Color(153, 204, 255));
        t_factura.setRowHeight(18);
        t_factura.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sp_t_factura.setViewportView(t_factura);

        p_fatura.add(sp_t_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 360, 540));

        l_v_total.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        l_v_total.setForeground(new java.awt.Color(51, 153, 255));
        l_v_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_v_total.setText("0");
        l_v_total.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        l_v_total.setFocusable(false);
        p_fatura.add(l_v_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 620, 130, 30));

        l_factura_cabecera1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        l_factura_cabecera1.setLabelFor(t_factura);
        l_factura_cabecera1.setText("Tipo reserva:");
        l_factura_cabecera1.setFocusable(false);
        p_fatura.add(l_factura_cabecera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 3, 100, 30));

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        URL myurl = prefijo.getClass().getResource("/skin/" + prefijo + "/total.png");
        ImageIcon icon = new ImageIcon(myurl);
        jLabel5.setIcon(icon);
        p_fatura.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 600, 260, 80));

        p_principal.add(p_fatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(633, 21, 370, 710));

        p_idcliente.setOpaque(false);
        p_idcliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t_venta_cliente.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        t_venta_cliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_venta_cliente.setText("NOMBRE_DE_CLIENTE");
        t_venta_cliente.setFocusable(false);
        t_venta_cliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p_idcliente.add(t_venta_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 0, 402, -1));
        p_idcliente.add(lb_tarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 25, 59, 39));

        lb_cliente_tipo_socio.setFont(lb_cliente_tipo_socio.getFont().deriveFont(lb_cliente_tipo_socio.getFont().getStyle() | java.awt.Font.BOLD, 14));
        lb_cliente_tipo_socio.setForeground(new java.awt.Color(51, 153, 255));
        lb_cliente_tipo_socio.setText("tipo de Socio");
        p_idcliente.add(lb_cliente_tipo_socio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 35, 230, -1));

        p_principal.add(p_idcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 23, 430, 70));

        jLabel30.setFocusable(false);

        javax.swing.GroupLayout p_publicidadLayout = new javax.swing.GroupLayout(p_publicidad);
        p_publicidad.setLayout(p_publicidadLayout);
        p_publicidadLayout.setHorizontalGroup(
            p_publicidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        p_publicidadLayout.setVerticalGroup(
            p_publicidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        p_principal.add(p_publicidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jPanel1.add(p_principal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void b_ventas_datosclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_datosclienteActionPerformed
        log.info("ACCION CANCELAR RESERVA");
        if (modo == MODO_COTIZACION) {
            if (ventana_padre.crearVentanaConfirmacion("Esta acción cancelará la cotización actual, ¿Desea continuar?")) {
                log.debug(" debug() - Cancelamos la cotización");
                ventana_padre.irBuscarCotizacion();
                return;
            }
        } else if (ventana_padre.crearVentanaConfirmacion("Esta acción cancelará la reserva actual, ¿Desea continuar?")) {
            try {
                String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_VENTA);

                if (modo != MODO_ADD_PLAN_NOVIOS && modo != MODO_PAGO_ARTICULOS_PLAN_NOVIOS && modo != MODO_ADD_PLAN_NOVIOS && modo != MODO_COTIZACION) {
                    ServicioLogAcceso.crearAccesoLogCancelarReserva(codAdministrador, reservacion.getReservacion().getReservaTipo().getDesTipo(), reservacion.getReservacion().getUidReservacion());
                    ventana_padre.resetReservacionesClienteView();
                } else if (modo == MODO_ADD_PLAN_NOVIOS) {
                    log.debug(" debug() - Cancelamos reserva de articulos del plan");
                    ServicioLogAcceso.crearAccesoLogCancelarReserva(codAdministrador, "PLAN NOVIO", planNovio.getPlan().getPlanNovioPK().getCodalm() + "-" + planNovio.getPlan().getPlanNovioPK().getIdPlan());
                    ventana_padre.irMostrarPlanNovio();
                    return;
                } else {
                    log.debug(" debug() - Cancelamos el plan Novios");
                    ServicioLogAcceso.crearAccesoLogCancelarReserva(codAdministrador, "PLAN NOVIO", planNovio.getPlan().getPlanNovioPK().getCodalm() + "-" + planNovio.getPlan().getPlanNovioPK().getIdPlan());
                }
                // Por si se puede recuperar una venta y luego ir a obtener información del cliente
                ventana_padre.showView("ident-cliente");
                //Sesion.borrarTicketReservacion();
                ticketNuevaVenta = null;
            } catch (SinPermisosException ex) {
                log.debug("No se tiene permisos para borrar línea de tickets.");
            } catch (Exception e) {
                log.error("Error al cancelar la reserva: " + e.getMessage(), e);
            }
        }
}//GEN-LAST:event_b_ventas_datosclienteActionPerformed

    private void b_ventas_pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_pagosActionPerformed
        imprimirCotizacion(true);
//        log.info("ACCION REALIZAR RESERVACIÓN");
//
//        if (modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS || modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS || (modo == MODO_AMPLIACION_ABONO_INICIAL)) {
////            if(Sesion.getTicketReservacion() == null || Sesion.getTicketReservacion().getFacturacion()==null){
////                if(Sesion.getTicketReservacion().getCliente().getCodcli().equals(Sesion.getClienteGenerico().getCodcli())){
////                    Sesion.getTicketReservacion().setFacturacionClienteGenerico();
////                } else {
////                    Sesion.getTicketReservacion().setFacturacionCliente();
////                }
////            }            
//            accionAccederAPagos();
//        } //Si es de tipo cotización, cremaos el Documento y lo almacenamos en BBDD
//        else if (modo == JReservacionesVentasCanBaby.MODO_COTIZACION) {
//            try {
//                //Imprimimos el documento
//                cotizacion.setIdCotizacion(ServicioContadoresCaja.obtenerContadorCotizacion());
//                ticketNuevaVenta.setFacturacion(new FacturacionTicketBean(ticketNuevaVenta.getCliente()));
//                ts.limpiarListaDocumentos();
//                ticketNuevaVenta.setId_ticket(cotizacion.getIdCotizacion());
//                boolean ventanaAceptada = ts.imprimirCotizacion(ticketNuevaVenta, true);
//                if (ventanaAceptada) {
//                    ts.limpiarListaDocumentos();
//                    ts.imprimirCotizacion(ticketNuevaVenta, false);
//                    //Creamos la cotizacion en BBDD y almacenamos el documento en la BBDD
//                    CotizacionesService.crearCotizacion(cotizacion, ticketNuevaVenta, ts.getDocumentosImpresos(), DocumentosBean.COTIZACION);
//
//                    ts.limpiarListaDocumentos();
//
//                    ventana_padre.showView("ident-cliente");
//                }
//            } catch (ContadorException ex) {
//                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
//                ventana_padre.crearError("No se ha podido crear la cotización");
//            } catch (CotizacionException ex) {
//                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
//                ventana_padre.crearError("No se ha podido crear la cotización");
//            } catch (TicketPrinterException ex) {
//                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
//                ventana_padre.crearError("No se ha podido crear la cotización");
//            }
//        } else {
//            accionAddArticulos();
//        }

}//GEN-LAST:event_b_ventas_pagosActionPerformed

    private void b_ventas_appalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_appalActionPerformed
//        log.info("MENÚ PRINCIPAL");
//        ventana_padre.mostrarMenu("reservaciones-venta", "NUEVA_RESERVACION");
}//GEN-LAST:event_b_ventas_appalActionPerformed

private void b_ventasBuscarArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventasBuscarArticuloActionPerformed
    log.info("ACCION BUSCAR ARTÍCULO");
    p_buscar_articulo.iniciaVista();
    p_buscar_articulo.iniciaFoco();
    v_buscar_articulo.setLocationRelativeTo(null);
    v_buscar_articulo.setVisible(true);
}//GEN-LAST:event_b_ventasBuscarArticuloActionPerformed
    /**
     * EVENTO: selección de artículo
     *
     * @param evt
     */
private void b_ventas_seleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_seleccionActionPerformed
    log.info("ACCION SELECCIÓN DE ARTÍCULO");
    try {
        CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
        c2.show(p_descuento_principal, "seleccion_cb");

        ActionMap am = v_seleccion_linea.getRootPane().getActionMap();
        InputMap im = v_seleccion_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                cancelarOperacion();
            }
        };

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);
        v_seleccion_linea.setSize(420, 200);
        p_descuento_principal.setPreferredSize(new Dimension(420, 200));
        p_seleccion_cb.setPreferredSize(new Dimension(420, 200));

        v_seleccion_linea.setLocationRelativeTo(null);
        v_seleccion_linea.setVisible(true);

        t_seleccion_cb.setText("");
        t_seleccion_cb1.setText("");
        lb_error.setText("");
        t_seleccion_cb.requestFocus();
    } catch (Exception e) {
        log.error("No se pudo seleccionar la línea: " + e.getMessage(), e);
    }

}//GEN-LAST:event_b_ventas_seleccionActionPerformed

private void t_descuento_valorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_descuento_valorKeyPressed
    if (evt.getKeyChar() == '\n') {
        accionAplicarDescuento();
    }
}//GEN-LAST:event_t_descuento_valorKeyPressed

private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
    accionCancelarEdicionLinea();
}//GEN-LAST:event_jb_cancel1ActionPerformed

private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
    accionAplicarDescuento();
}//GEN-LAST:event_jb_ok1ActionPerformed

private void o_env_adomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_env_adomicilioActionPerformed
    if (o_env_adomicilio.isSelected()) {
        o_recogida_posterior.setSelected(false);
    }
}//GEN-LAST:event_o_env_adomicilioActionPerformed

private void o_recogida_posteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_recogida_posteriorActionPerformed
    if (o_recogida_posterior.isSelected()) {
        o_env_adomicilio.setSelected(false);
    }
}//GEN-LAST:event_o_recogida_posteriorActionPerformed

private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
    p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

private void b_datos_facturacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_datos_facturacionActionPerformed
    // NOTA: Se esta usando una misma pantalla para la facturación en reservas y Plan Novios
    //       Se usa Sesion.getReservación para enviar y recibir los datos de facturación a la ventana que los trata

    TicketS ticketAEditar = new TicketS();

    if (modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
        ticketAEditar.setCliente(ticketNuevaVenta.getCliente());
        ticketAEditar.setFacturacion(ticketNuevaVenta.getFacturacion());
    } else if (modo == JReservacionesVentasCanBaby.MODO_COTIZACION) {
        try {
            log.debug("Enviar Mail");
            mail = ventana_padre.crearVentanaMail();
            String url = Variables.getVariable(Variables.WEBSERVICE_ERP_MOVIL_ENDPOINT_URL);
            ClienteRest clienteRest = new ClienteRest();
            TiendaDTO tiendaDTO = new TiendaDTO();
            CotizacionDTO cotizacionDTO = new CotizacionDTO();
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setIdentificacion(ticketNuevaVenta.getCliente().getIdentificacion());
            clienteDTO.setTipoIdentificacion(ticketNuevaVenta.getCliente().getTipoIdentificacion());
            clienteDTO.setNombres(ticketNuevaVenta.getCliente().getNombre());
            clienteDTO.setApellidos(ticketNuevaVenta.getCliente().getApellido());
            clienteDTO.setNumeroTelefono(ticketNuevaVenta.getCliente().getTelefonoMovil());
            clienteDTO.setNumeroCelular(ticketNuevaVenta.getCliente().getTelefonoMovil());
            clienteDTO.setEmail(mail);
            clienteDTO.setDireccion(ticketNuevaVenta.getCliente().getDireccion());
            //Asigna los articulos
            List<LineaTicket> ticketArticulos = ticketNuevaVenta.getLineas().getLineas();
            ItemDTO itemDTO = null;
            List<ItemDTO> listItem = new ArrayList<ItemDTO>();
            for (LineaTicket lineaTicket : ticketArticulos) {
                itemDTO = new ItemDTO();
                itemDTO.setCodigoI(lineaTicket.getArticulo().getCodart());
                itemDTO.setDescripcion(lineaTicket.getArticulo().getDesart());
                itemDTO.setCantidad(Long.valueOf(lineaTicket.getCantidad()));
                itemDTO.setPrecioOrigen(lineaTicket.getPrecioTotal());
                itemDTO.setPrecioTotalOrigen(lineaTicket.getImporteTotal());
                listItem.add(itemDTO);
            }
            cotizacionDTO.setDatosFacturacion(clienteDTO);
            cotizacionDTO.setItems(listItem);
            tiendaDTO.setNombreComercial(Sesion.getTienda().getAlmacen().getCodemp().getNombreComercial());
            tiendaDTO.setNombreSucursal(Sesion.getTienda().getAlmacen().getDesalm());
            tiendaDTO.setDomicilioMatriz(Sesion.getTienda().getAlmacen().getCodemp().getDomicilio());
            tiendaDTO.setDomicilioSucursal(Sesion.getTienda().getAlmacen().getDomicilio());
            tiendaDTO.setRuc(Sesion.getTienda().getAlmacen().getCodemp().getCif());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fechaActual = dateFormat.format(date);
            String fechaRegion = Sesion.getTienda().getCodRegion().getDesregion().concat(", ").concat(fechaActual);
            tiendaDTO.setFecha(fechaRegion);
            cotizacionDTO.setDatosTienda(tiendaDTO);
            String mensaje = Variables.getVariable(Variables.MENSAJE_COTIZACION_XML);
            cotizacionDTO.setMsmCotizacion(mensaje);
            cotizacionDTO.setElaborado(ticketNuevaVenta.getCajero().getDesUsuario());
            cotizacionDTO.setLocal(Sesion.getTienda().getAlmacen().getDesalm());
            cotizacionDTO.setTelefono(Sesion.getTienda().getAlmacen().getTelefonos());
            RespuestaSupermaxiDTO responseSupermaxiDTO = clienteRest.clientRestPOST(url + URL_ENVIO_SUPERMAXI, cotizacionDTO, null, RespuestaSupermaxiDTO.class);
            responseSupermaxiDTO.getDescripcion();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(JReservacionesVentasCanBaby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SinPermisosException ex) {
            java.util.logging.Logger.getLogger(JReservacionesVentasCanBaby.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        ticketAEditar.setCliente(reservacion.getInvitadoActivo());
        if (reservacion.getFacturacion() != null) {
            ticketAEditar.setFacturacion(reservacion.getFacturacion());
        }
    }

    if (modo != JReservacionesVentasCanBaby.MODO_COTIZACION) {
        FacturacionTicketBean ft = JPrincipal.crearVentanaFacturacion(false, ticketAEditar);
        if (ft == null) {
            log.debug("Se canceló la edición de datos de facturación");
        } else {
            ticketNuevaVenta.setFacturacion(ft);
            // Recuperamos los datos de facturación y se los ponemos al invitado  
            if (modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
                planNovio.setDatosFacturacionTicketBean(ft);
            } else {
                reservacion.setFacturacion(ft);
            }
        }
    }
}//GEN-LAST:event_b_datos_facturacionActionPerformed

private void v_buscar_articuloWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_buscar_articuloWindowGainedFocus
    p_buscar_articulo.establecerFoco();
}//GEN-LAST:event_v_buscar_articuloWindowGainedFocus

private void b_ventas_edicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_edicionActionPerformed
    log.info("ACCION EDITAR LÍNEA");
    try {
        // ponemos la acción de eliminar ultimo elemento añadido a la lista
        lineaSelecionada = t_factura.getSelectedRow();
        if (lineaSelecionada == -1) {
            lineaSelecionada = t_factura.getRowCount() - 1;
        }
        this.indexLineaAEditar = lineaSelecionada;
        if (lineaSelecionada >= 0) {

            if (isModoPagoArticulosPlanNovios()) {
                // Desactivamos los descuentos
                t_descuento_valor.setEditable(false);
                t_descuento_valor.setFocusable(false);
                t_descuento_valor.setEnabled(false);

                iniciaFormularioDatosAdicionales(ticketNuevaVenta.getLineas().getLinea(lineaSelecionada).getDatosAdicionales());
                CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
                c2.show(p_descuento_principal, "descuento_valor");
                v_descuento_linea.setSize(370, 200);
                v_descuento_linea.setLocationRelativeTo(null);
                p_descuento_principal.setPreferredSize(new Dimension(370, 200));
                P_descuento_valor.setPreferredSize(new Dimension(370, 200));
                v_descuento_linea.setVisible(true);

                ActionMap am = v_descuento_linea.getRootPane().getActionMap();
                InputMap im = v_descuento_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

                KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
                Action listeneresc = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        // cancelarOperacion();
                        jb_cancel1ActionPerformed(ae);
                    }
                };

                im.put(esc, "IdentClientesc");
                am.put("IdentClientesc", listeneresc);
                lb_error.setText("");
                t_descuento_valor.requestFocus();
            } else if (isModoAdd() || isModoAddPlanNovios() || isModoCotizacion()) {
                Integer cantidad = ticketNuevaVenta.getLineas().getLinea(lineaSelecionada).getCantidad();
                p_editar_cantidad.setCantidad(cantidad);

                v_editar_cantidad.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
                v_editar_cantidad.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
                v_editar_cantidad.setAlwaysOnTop(true);
                p_editar_cantidad.iniciaFoco();
                v_editar_cantidad.setVisible(true);

                int nuevaCantidad = p_editar_cantidad.getCantidad();
                if (nuevaCantidad > 0) {
                    // intentamos establecer la cantidad
                    ticketNuevaVenta.getLineas().getLinea(lineaSelecionada).setCantidad(nuevaCantidad);
                    ticketNuevaVenta.getLineas().getLinea(lineaSelecionada).recalcularImportes();
                    ticketNuevaVenta.recalcularTotales();
                    refrescaTablaTicket();
                } else {
                    ventana_padre.crearAdvertencia("Debe insertar un valor positivo.");
                }
            }
        } else {
            ventana_padre.crearAdvertencia("Debe añadir una línea de ticket para poder editarla.");
        }
    } catch (Exception ex) { //SinPermisos
        //
    }
}//GEN-LAST:event_b_ventas_edicionActionPerformed

private void jb_ok1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_ok1KeyTyped
    accionAplicarDescuento();
}//GEN-LAST:event_jb_ok1KeyTyped

private void jb_cancel1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_cancel1KeyTyped
    accionCancelarEdicionLinea();
}//GEN-LAST:event_jb_cancel1KeyTyped

private void o_env_adomicilioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_env_adomicilioKeyTyped
    if (evt.getKeyChar() == '\n') {
        if (o_env_adomicilio.isSelected()) {
            o_env_adomicilio.setSelected(false);
        } else {
            o_env_adomicilio.setSelected(true);
        }
    }
}//GEN-LAST:event_o_env_adomicilioKeyTyped

private void o_recogida_posteriorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_recogida_posteriorKeyTyped
    if (evt.getKeyChar() == '\n') {
        if (o_recogida_posterior.isSelected()) {
            o_recogida_posterior.setSelected(false);
        } else {
            o_recogida_posterior.setSelected(true);
        }
    }
}//GEN-LAST:event_o_recogida_posteriorKeyTyped

    private void t_seleccion_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_seleccion_cbActionPerformed

    }//GEN-LAST:event_t_seleccion_cbActionPerformed

    private void t_seleccion_cbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cbKeyPressed
        // BUSQUEDA DE LÍNEA DENTRO DEL TICKET A PARTIR DE UN CÓDIGO DE ARTÍCULO
        try {
            if (evt.getKeyChar() == '\n') {
                if (t_seleccion_cb.getText().length() > 0) {
                    t_seleccion_cb.setText(Articulos.formateaCodigoBarras(t_seleccion_cb.getText()));
                }
                codigobarra = t_seleccion_cb.getText();
                Integer indexLinea = ticketNuevaVenta.getIndexLinea(codigobarra);
                if (indexLinea != null && t_seleccion_cb.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                } else if (t_seleccion_cb.getText().length() == 0) {
                    lb_error.setText("Ha de introducir un código de barras");
                } else {
                    lb_error.setText("No se encontro ningún artículo para ese código de barras");
                    log.debug("No se encontró ningún artículo con código de barras " + codigobarra);
                }
            }
        } catch (Exception e) {
            log.error("Error al seleccionar línea", e);
        }
    }//GEN-LAST:event_t_seleccion_cbKeyPressed

    private void t_seleccion_cbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cbKeyTyped

    }//GEN-LAST:event_t_seleccion_cbKeyTyped

    private void t_seleccion_cb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_seleccion_cb1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_seleccion_cb1ActionPerformed

    private void t_seleccion_cb1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cb1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_seleccion_cb1KeyPressed

    private void t_seleccion_cb1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cb1KeyTyped
        // BUSQUEDA DE LÍNEA DENTRO DEL TICKET A PARTIR DE UN CÓDIGO DE ARTÍCULO
        try {
            if (evt.getKeyChar() == '\n') {

                CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
                String codigoArticulo = t_seleccion_cb1.getText();
                Integer indexLinea = ticketNuevaVenta.getIndexLineaCodigoArticulo(codigoArticulo);
                if (indexLinea != null && t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                } else if (t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("El código de artículo ingresado es incorrecto");
                } else {
                    lb_error.setText("No se encontro ningún articulo con el código de artículo");
                    log.debug("No se encontro ningún artículo con el código de artículo " + codigoArticulo);
                }
            }
        } catch (Exception e) {
            log.error("Error al seleccionar línea", e);
        }
    }//GEN-LAST:event_t_seleccion_cb1KeyTyped

    private void v_descuento_lineaWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_descuento_lineaWindowGainedFocus
        o_recogida_posterior.requestFocus();
    }//GEN-LAST:event_v_descuento_lineaWindowGainedFocus

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel P_descuento_valor;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_datos_facturacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_appal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_datoscliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_edicion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_otro_local;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_pagos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_seleccion;
    private javax.swing.JFrame f_pagos2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_compra_socio;
    private javax.swing.JLabel jLabel_numArticulos;
    private javax.swing.JPanel jPanel1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_cancel1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_ok1;
    private javax.swing.JLabel l_factura_cabecera1;
    private javax.swing.JLabel l_factura_cabecera_id;
    private javax.swing.JLabel l_total;
    private javax.swing.JLabel l_v_total;
    private javax.swing.JLabel lb_cliente_tipo_socio;
    private javax.swing.JLabel lb_error;
    private javax.swing.JLabel lb_error_descuento;
    private com.comerzzia.jpos.gui.components.JMultilineLabel lb_error_ml;
    private javax.swing.JLabel lb_tarjeta;
    private com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket lineasTicket;
    private javax.swing.JPanel m_ventas;
    private javax.swing.JCheckBox o_env_adomicilio;
    private javax.swing.JCheckBox o_recogida_posterior;
    private com.comerzzia.jpos.gui.JBuscar p_buscar_articulo;
    private com.comerzzia.jpos.gui.JDatosEnvio p_datos_envio;
    private javax.swing.JPanel p_descuento_principal;
    private javax.swing.JPanel p_descuentos;
    private com.comerzzia.jpos.gui.devoluciones.JIntroducirCantidad p_editar_cantidad;
    private javax.swing.JPanel p_fatura;
    private javax.swing.JPanel p_idcliente;
    private javax.swing.JPanel p_imagen_airticulo;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesImportePago p_importe_a_pagar;
    private javax.swing.JPanel p_ingreso_articulos;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV p_pagos;
    private javax.swing.JPanel p_principal;
    private javax.swing.JPanel p_publicidad;
    private javax.swing.JPanel p_seleccion_cb;
    private javax.swing.JScrollPane sp_t_factura;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_descuento_valor;
    private javax.swing.JTable t_factura;
    private com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp t_introduccionArticulos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb1;
    private javax.swing.JLabel t_venta_cliente;
    private javax.swing.JDialog v_buscar_articulo;
    private javax.swing.JDialog v_datos_envio;
    private javax.swing.JDialog v_descuento_linea;
    private javax.swing.JDialog v_editar_cantidad;
    private javax.swing.JDialog v_importe_a_pagar;
    private javax.swing.JDialog v_pagos;
    private javax.swing.JDialog v_seleccion_linea;
    // End of variables declaration//GEN-END:variables

    protected void cerrarPanelActivo() {
        if (this.p_activo != null) {

            p_pagos.setVisible(false);
            p_activo.setVisible(false);

        }
    }

    /**
     * Función que desabilita los elementos de formulario que quedan debajo de
     * la pantalla de pagos
     */
    private void desabilita_elementos_formulario() {
        m_ventas.setVisible(false);
        b_ventas_appal.setEnabled(false);
        b_ventas_datoscliente.setEnabled(false);
        b_ventas_pagos.setEnabled(false);
        b_datos_facturacion.setEnabled(false);
        b_ventas_edicion.setEnabled(false);
        t_factura.setEnabled(false);
        t_factura.setVisible(false);
        sp_t_factura.setEnabled(false);
        sp_t_factura.setVisible(false);

        t_introduccionArticulos.setActivo(false);

    }

    /**
     * Función que habilita los elementos de formulario que quedan debajo de la
     * pantalla de pagos
     */
    protected void habilita_elementos_formulario() {
        m_ventas.setVisible(true);
        b_ventas_appal.setEnabled(true);
        b_ventas_datoscliente.setEnabled(true);
        b_ventas_pagos.setEnabled(true);
        b_datos_facturacion.setEnabled(true);
        b_ventas_edicion.setEnabled(true);
        t_factura.setEnabled(true);
        t_factura.setVisible(true);
        sp_t_factura.setEnabled(true);
        sp_t_factura.setVisible(true);

        t_introduccionArticulos.setActivo(true);

    }

    /* Acciones de teclado */
    @Override
    public void keyTyped(KeyEvent ke) {
        // Pulsado enter mientras se esta en el cuadro de insercion de articulos
        if (ke.getKeyChar() == '\n' && ke.getComponent() instanceof JButtonForm) {
            ((JButtonForm) ke.getComponent()).doClick(0);
        }
        if (t_introduccionArticulos.getT_Codigo().hasFocus() && (ke.getKeyChar() == 'x' || ke.getKeyChar() == 'X') && t_introduccionArticulos.getT_Codigo().getText().length() == 0) {
            log.info("ACCION DE INTRODUCCIÓN DE CANTIDAD");
            accionIntroducirCantidad();
        }
        if (t_introduccionArticulos.getT_Codigo().hasFocus() && ke.getKeyChar() == '\n') {
            log.info("ACCION INTRODUCCIÓN DE ARTÍCULO");
            crearLineaTicket();
        }
    }

    private void crearLineaTicket() {
        try {
            // comprobamos que existen valores correctos en el componente
            t_introduccionArticulos.checkValores(TicketTableCellRenderer.isModoConCantidad());
            // consultamos el artículo y su tarifa
            String codigo = t_introduccionArticulos.getCodArticulo();
            if (codigo.length() > 1 && codigo.substring(0, 1).equalsIgnoreCase("r")) {
                abrirBuscadorPorCodigo(codigo.substring(1));
                return;
            } else if (modo != JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS
                    && modo != JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS_PLAN_NOVIOS
                    && codigo.length() > 2
                    && (codigo.substring(0, 2).equalsIgnoreCase("bm") || codigo.substring(0, 2).equalsIgnoreCase("sk"))) {
                OpcionesVentaManager.anadeListaPDA(this.ticketNuevaVenta, codigo, this);
                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            } else if ((modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS
                    || modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS_PLAN_NOVIOS)
                    && codigo.length() > 2
                    && (codigo.substring(0, 2).equalsIgnoreCase("bm") || codigo.substring(0, 2).equalsIgnoreCase("sk"))) {
                ventana_padre.crearError("La funcionalidad de Sesión PDA no está disponible en pagos de artículos en reserva");
            } else if (codigo.length() > 0) {
                Articulos art;
                if (codigo.length() == 9 && codigo.charAt(4) == '.') {
                    art = articulosServices.getArticuloCod(codigo);
                    if (art == null) {
                        throw new ArticuloNotFoundException("No se ha encontrado artículo con código: " + codigo);
                    }
                    //Buscar codigoBarras del código artículo introducido
                    codigo = Articulos.formateaCodigoBarras(ArticulosServices.consultarCodigoBarras(codigo));
                } else {
                    /**
                     * Implementacion codigo barras RD
                     */
                    if (codigo.length() == 11) {
                        codigo = "00" + codigo;
                    } else {
                        if (codigo.length() == 12) {
                            codigo = "0" + codigo;
                        } else {
                            if (codigo.length() == 14) {
                                codigo = codigo.substring(1);
                            }
                        }
                    }
                    codigo = Articulos.formateaCodigoBarras(codigo);
                    art = articulosServices.getArticuloCB(codigo);
                }

                Tarifas tar = null;

                // Comprobamos si el artículo esta bloqeado
                BloqueosServices.isItemBloqueado(art.getCodmarca().getCodmarca(), art.getIdItem(), art.getCodart());

                tar = tarifasServices.getTarifaArticulo(art.getCodart());

                // Comprobamos, 
                // Si la reserva no es de canastillas canastilalas, que haya stock al añadir
                // Si es canastillas, stock al comprar artículo. 
                // La compra de artículos en un plan novio tampoco la validamos porque ya se hace al añadir artículos.
                if (modo != MODO_PAGO_ARTICULOS_PLAN_NOVIOS
                        && ((modo != MODO_ADD || !reservacion.isCanastilla()) || (modo == MODO_PAGO_ARTICULOS && reservacion.isCanastilla()))) {
                    if (!this.isModoCotizacion()) {
                        checkForItemFueraStock(art, t_introduccionArticulos.getCantidadInt());
                    }
                }

                if (modo == MODO_ADD || modo == MODO_ADD_PLAN_NOVIOS) {
                    // Añadimos la linea del ticket                            
                    if (modo == MODO_ADD) {
                        ticketNuevaVenta.crearLineaCompraReserva(reservacion, art, ticketReservacion, tar, codigo, modo, t_introduccionArticulos.getCantidadInt());
                    } else if (modo == MODO_ADD_PLAN_NOVIOS) {
                        ticketNuevaVenta.crearLineaCompraReserva(planNovio, art, tar, codigo, modo, t_introduccionArticulos.getCantidadInt());
                    }
                    //Imagenes.cambiarImagenArticulo(jLabel7, art.getReferenciaInterna());
                } else if (modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
                    ticketNuevaVenta.crearLineaCompraReserva(planNovio, art, tar, codigo, modo, t_introduccionArticulos.getCantidadInt());
                } else {
                    ticketNuevaVenta.crearLineaCompraReserva(reservacion, art, ticketReservacion, tar, codigo, modo, t_introduccionArticulos.getCantidadInt());
                    LineaTicket lineaTicket = ticketNuevaVenta.getLineas().getLinea(ticketNuevaVenta.getLineas().getIndexUltimaLinea());
                    accionCompruebaGarantiaExtendida(lineaTicket);
                }

                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            }
        } catch (ArticuloNotFoundException e) {
            if (modo == MODO_PAGO_ARTICULOS && !crearLineaTicketCupon()
                    || modo == MODO_ADD_PLAN_NOVIOS
                    || modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS
                    || modo == MODO_ADD) {
                log.info("No se ha encontrado el artículo");
                ventana_padre.crearError(e.getMessage());
                t_introduccionArticulos.requestFocus();
            } else {
                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            }
        } catch (TarifaArticuloNotFoundException e) {
            log.info("El artículo seleccionado no tiene tarifa");
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (TicketNuevaLineaException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (ListaPDAException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (BloqueoFoundException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (StockException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (ArticuloException e) {
            log.info(e.getMessage());
            ventana_padre.crearAdvertencia(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (ArticuloEnReservaNotFoundException e) {
            log.debug(e.getMessage());
            ventana_padre.crearInformacion(e.getMessage());
        } catch (Exception e) {
            log.error("Error al seleccionar el artículo: " + e.getMessage(), e);
            log.debug(t_introduccionArticulos.getCodArticulo());
            ventana_padre.crearError("Error al insertar el artículo");
            t_introduccionArticulos.requestFocus();
        }
    }

    public void accionCompruebaGarantiaExtendida(LineaTicket linea) throws TicketNuevaLineaException {
        log.info("Acción Comprueba Garantia Extendida");
        //rd
        Usuarios valor = null;
        try {
            if (!linea.getArticulo().isGarantiaExtendida()) {
                return;
            }
            boolean deseaGarantiaExtendida = false;
            deseaGarantiaExtendida = true;
            log.debug("accionCompruebaGarantiaExtendida() - Creando línea de garantía extendida");
            int numero = linea.getCantidad().intValue();
            if (numero > 0) {
                GarantiaExtendida garantia = new GarantiaExtendida(linea.getArticulo(), linea.getPrecioTarifaOrigen());
                Tarifas tar = tarifasServices.getTarifaArticulo(garantia.getArticuloGarantia().getCodart());
                tar.setPrecioVenta(garantia.getPrecioTotalGarantia());
                ticketNuevaVenta.crearLineaCompraReserva(reservacion, garantia.getArticuloGarantia(), ticketReservacion, tar, Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS), modo, t_introduccionArticulos.getCantidadInt());

            }
        } catch (ArticuloNotFoundException e) {
            throw new TicketNuevaLineaException("No se ha encontrado el artículo de la garantía extendida.", e);
        } catch (TarifaException ex) {
            java.util.logging.Logger.getLogger(JReservacionesVentasCanBaby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArticuloEnReservaNotFoundException ex) {
            java.util.logging.Logger.getLogger(JReservacionesVentasCanBaby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkForItemFueraStock(Articulos art, int cantidad) throws SinPermisosException, StockException {
        // Comprobamos que hay stock
        if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)) {
            //if (!ServicioStock.isStockDisponibleVenta(art, cantidad)) {
            int stockDisponibleVenta = ServicioStock.isStockDisponibleVenta(art);
            if (stockDisponibleVenta < cantidad) {
                // Pedimos autorización
                String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA, "Artículo fuera de stock");
                String tickLog = ticketNuevaVenta.getIdFactura();
                if (isModoAdd()) {
                    tickLog = reservacion.getReservacion().getIdRes();
                }
                if (isModoAddPlanNovios()) {
                    tickLog = planNovio.getCodPlanAsString();
                }
                ServicioLogAcceso.crearAccesoLogAdmitirFueraStock(codAdministrador, tickLog + "-" + art.getIdItem());
            }
        }
    }

    private void abrirBuscadorPorCodigo(String codigoArticulo) {
        v_buscar_articulo.setLocationRelativeTo(null);
        p_buscar_articulo.iniciaVista(codigoArticulo);
        p_buscar_articulo.establecerFoco();
        v_buscar_articulo.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void refrescaTablaTicket() {
        log.info("Función refrescar tabla de tickets");
        l_v_total.setText("$ " + ticketNuevaVenta.getTotales().getTotalAPagar().toString());
        jLabel_numArticulos.setText("<html><b>" + ticketNuevaVenta.getLineas().getNumLineas() + "</b> artículos ingresados</html>");
        t_factura.setModel(new TicketTableModel(ticketNuevaVenta.getLineas()));

        // Anchos de columnas de la tabla
        t_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int i = 0;
        for (int anchura : TicketTableCellRenderer.getAnchosColumna()) {
            t_factura.getColumnModel().getColumn(i).setPreferredWidth(anchura);
            i++;
        }
        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();

        // jLabel_bono.setText("$ " + Sesion.getTicketReservacion().getTotales().getTotalAfiliadoUnaCuota().toString());
        if (ticketNuevaVenta.getLineas().getLineas().isEmpty()) {
            b_ventas_pagos.setEnabled(false);
            b_datos_facturacion.setEnabled(false);
            removeHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "IdentClientF9");
        } else {
            b_ventas_pagos.setEnabled(true);
            b_datos_facturacion.setEnabled(true);
            KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
            Action listenerf9 = new AbstractAction() {

                public void actionPerformed(ActionEvent ae) {
                    b_ventas_pagosActionPerformed(ae);

                }
            };
            addHotKey(f9, "IdentClientF9", listenerf9);
        }
    }

    private void removeHotKey(KeyStroke keyStroke, String inputActionKey) {
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.remove(keyStroke);
        actionMap.remove(inputActionKey);
    }

    public void addFunctionKeys() {
        // botones de menú de la ventana
        addFuntionKeysBotonesMenu();

        // otros controles
        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    ventana_padre.compruebaAutorizacion(Operaciones.BORRAR_LINEA_VENTA);
                    lineaSelecionada = t_factura.getSelectedRow();
                    if (lineaSelecionada >= 0) {
                        ticketNuevaVenta.eliminarLineaTicket(lineaSelecionada);
                    } else {
                        ticketNuevaVenta.eliminarLineaTicket();
                    }
                    refrescaTablaTicket();
                } catch (KitException ex) {
                    log.error("No se puede eliminar el kit obligatorio.");
                    ventana_padre.crearAdvertencia(ex.getMessage());
                } catch (SinPermisosException ex) {
                    log.debug("No se tiene permisos para borrar línea de tickets.");
                }
            }
        };
        addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        Action listenerCtrlO = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {

                /*
                if(isModoPagoArticulos()){
                    reservacion.setObservaciones(ventana_padre.crearVentanaObservaciones(reservacion.getObservaciones()));
                }
                if(isModoPagoArticulosPlanNovios()){
                    planNovio.setObservaciones(ventana_padre.crearVentanaObservaciones(planNovio.getObservaciones()));
                }
                if(isModoCotizacion()){
                    ticketNuevaVenta.setObservaciones(ventana_padre.crearVentanaObservaciones(ticketNuevaVenta.getObservaciones()));
                }
                if(isModoAdd()){
                    reservacion.setObservaciones(ventana_padre.crearVentanaObservaciones(reservacion.getObservaciones()));
                }
                 
                 */
            }
        };
        addHotKey(ctrlO, "ObservacionesCtrlO", listenerCtrlO);

        KeyStroke ctrlAltD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK + InputEvent.CTRL_MASK);
        Action listenerCtrlAltD = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    if (isModoPagoArticulos() || isModoPagoArticulosPlanNovios()) {
                        ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
                        // Descuento Manual a Total
                        Integer dto = JPrincipal.crearVentanaIngresarValorEntero("Descuento de factura: ", Numero.CIEN);
                        if (dto != null) {
                            PromocionTipoDtoManualTotal.activar(dto);
                            ticketNuevaVenta.recalcularTotales();
                            refrescaTablaTicket();
                        }
                    }
                } catch (SinPermisosException ex) {
                    log.debug("No se tienen permisos para descontar el subtotal de la factura.");
                }
            }
        };
        addHotKey(ctrlAltD, "DescuentoSubtotalCtrlAltD", listenerCtrlAltD);

    }

    private void addFuntionKeysBotonesMenu() {

        KeyStroke ksBorrarLinea = KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0);
        Action listenerksBorrarLinea = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_datosclienteActionPerformed(ae);
            }
        };

        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_datosclienteActionPerformed(ae);
            }
        };
        addHotKey(f2, "IdentClientF2", listenerf2);

        t_factura.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_F2) {
                    int keyCode = keyEvent.getKeyCode();
                    String keyText = KeyEvent.getKeyText(keyCode);
                    ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, keyText);
                    b_ventas_datosclienteActionPerformed(actionEvent);
                }
            }
        });

        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                if (b_ventas_edicion.isEnabled()) {
                    b_ventas_edicionActionPerformed(ae);
                }
            }
        };
        addHotKey(f4, "IdentClientF4", listenerf4);

        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        Action listenerf5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_datos_facturacionActionPerformed(ae);

            }
        };
        addHotKey(f5, "IdentClientF5", listenerf5);

        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        Action listenerf9 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_pagosActionPerformed(ae);

            }
        };
        addHotKey(f9, "IdentClientF9", listenerf9);

        // F7: selección de artículo
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        Action listenerf7 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_seleccionActionPerformed(ae);

            }
        };
        addHotKey(f7, "IdentClientF7", listenerf7);

        // F8: búsqueda
        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        Action listenerf8 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventasBuscarArticuloActionPerformed(ae);

            }
        };
        addHotKey(f8, "IdentClientF8", listenerf8);

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_appalActionPerformed(ae);
            }
        };
        addHotKey(f12, "IdentClientF12", listenerf12);

    }

    @Override
    public void setCodigoArticulo(String cb) {
        this.t_introduccionArticulos.getT_Codigo().setText(cb);
        this.t_introduccionArticulos.getT_Codigo().requestFocus();
        crearLineaTicket();
    }

    private void cancelarOperacion() {
        v_seleccion_linea.setVisible(false);
        t_seleccion_cb.setText("");
        t_descuento_valor.setText("");
        lb_error.setText("");
        CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
        c2.show(p_descuento_principal, "seleccion_cb");
    }

    private void resetearFormularioEnvio() {
        log.info("Resetear formulario de envío");
        t_descuento_valor.setText("");
        o_env_adomicilio.setSelected(false);
        o_recogida_posterior.setSelected(false);
    }

    private void iniciaFormularioDatosAdicionales(DatosAdicionalesLineaTicket datosAdicionales) {
        log.info("Iniciar Formulario de datos adicionales");
        o_recogida_posterior.setText("Pendiente Retiro Local");
        o_env_adomicilio.setText("Entregas a Domicilio");
        t_descuento_valor.setEditable(false);
        t_descuento_valor.setEnabled(false);
        if (datosAdicionales == null) {
            t_descuento_valor.setText("0");
            o_env_adomicilio.setSelected(false);
            o_recogida_posterior.setSelected(false);
        } else {
            BigDecimal descuento = datosAdicionales.getDescuento();
            if (Numero.isMenorACero(descuento)) {
                t_descuento_valor.setText("0");
            } else {
                t_descuento_valor.setText(String.valueOf(descuento));
            }
            o_env_adomicilio.isSelected();
            o_recogida_posterior.isSelected();
            o_env_adomicilio.setSelected(datosAdicionales.isEnvioDomicilio());
            o_recogida_posterior.setSelected(datosAdicionales.isRecogidaPosterior());
        }

    }

    private void enviaFormularioDatosAdicionales() {
        log.info("Enviar formulario de Datos Adicionales");
        BigDecimal valorDescuento = BigDecimal.ZERO;
        try {
            if (!t_descuento_valor.getText().isEmpty()) {
                valorDescuento = new BigDecimal(t_descuento_valor.getText());
            } else {
                valorDescuento = BigDecimal.ZERO;
            }

            DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
            datosAdicionales.setEnvioDomicilio(o_env_adomicilio.isSelected());
            datosAdicionales.setRecogidaPosterior(o_recogida_posterior.isSelected());
            datosAdicionales.setDescuento(valorDescuento);

            ticketNuevaVenta.setDatosAdicionalesLinea(indexLineaAEditar, datosAdicionales);

        } catch (NumberFormatException e) {
            log.info("Formato numérico incorrecto");
        } catch (Exception e) {
            log.error("Error en envío de formlario de datos adicionales " + e.getMessage(), e);
        }
    }

    private void accionAccederAPagosAux() {
        log.info("Acción acceder a Pagos Aux");
        if (reservacion != null && reservacion.getReservacion().getReservaTipo().getAbonoInicial() && modo == MODO_AMPLIACION_ABONO_INICIAL) {
            crearVentanaPagos(reservacion.getAmpliacionAbono(), reservacion.getAmpliacionAbono());
        } else {
            accionCrearVentanaImportes();
        }
    }

    private void accionAccederAPagos() {
        log.info("Acción acceder a Pagos");
        try {

            if (modo == MODO_AMPLIACION_ABONO_INICIAL && ticketNuevaVenta.getCliente() == null) {
                ticketNuevaVenta.setCliente(reservacion.getReservacion().getCliente());
            }

            // Si hay Envio a domicilio para algún artículo
            if (ticketNuevaVenta.hayEnvioADomicilio()) {
                Sesion.setTicket(ticketNuevaVenta);
                p_datos_envio.inicializaFormulario();
                v_datos_envio.setVisible(true);
                if (p_datos_envio.isAceptado()) {
                    accionAccederAPagosAux();
                }
            } else {
                accionAccederAPagosAux();
            }
        } catch (Exception e) {
            log.error("Error accediendo a Pagos: " + e.getMessage(), e);
        }
    }

    @Override
    public void addError(ValidationFormException e) {
        lb_error_ml.setText(e.getMessage());
    }

    @Override
    public void clearError() {
        lb_error_ml.setText("");
    }

    private void accionAplicarDescuento() {
        log.info("Acción Aplicar Descuentos");
        try {
            t_descuento_valor.validar();
            BigDecimal valorDescuento;
            BigDecimal valorDescuentoAnterior;
            if (!t_descuento_valor.getText().isEmpty()) {
                valorDescuento = (new BigDecimal(t_descuento_valor.getText()));
            } else {
                valorDescuento = BigDecimal.ZERO;
            }
            valorDescuentoAnterior = ticketNuevaVenta.getLineas().getLinea(indexLineaAEditar).getDescuento();
            if (t_descuento_valor.getText().isEmpty()) {
                valorDescuento = BigDecimal.ZERO;
            }

            if ((valorDescuento.compareTo(valorDescuentoAnterior) != 0)) {
                // Entra aquí si el descuento ha cambiado
                // Comprobamos que el descuento no se haya modificado
                ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
            }

            enviaFormularioDatosAdicionales();
            v_descuento_linea.setVisible(false);
            refrescaTablaTicket();
        } catch (ValidationFormException ex) {
            log.debug(ex.getMessage());
        } catch (SinPermisosException e) {
            log.debug("No se tienen permisos para modificar el descuento.");
        } catch (Exception e) {
            log.error("Error aplicando el descuento", e);
        }
    }

    @Override
    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo) {
        log.info("Acción Crear Ventana de Pagos");
        try {
            v_pagos.getContentPane().removeAll();
//            if (this.modo == MODO_AMPLIACION_ABONO_INICIAL) {
//                p_pagos = new JReservacionesPagosV(ventana_padre, reservacion, importeMaximo, reservacion.getReservacion().getCodcli(), reservacion.getInvitadoSeleccionado(), "PROPIOS", importeMinimo, false);
//            }
            if (this.modo == MODO_PAGO_ABONOS || this.modo == MODO_AMPLIACION_ABONO_INICIAL) {
                p_pagos = new JReservacionesPagosV(ventana_padre, reservacion, importeMaximo, reservacion.getReservacion().getCliente(), reservacion.getInvitadoSeleccionado(), importeMinimo, true);
            } else if (this.modo == MODO_PAGO_ARTICULOS) {
                p_pagos = new JReservacionesPagosV(ventana_padre, reservacion, importeMaximo, ticketNuevaVenta, null, importeMinimo);
            } else if (this.modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {
                p_pagos = new JReservacionesPagosV(ventana_padre, planNovio, importeMaximo, ticketNuevaVenta, PlanNovioMostrar.getIstance().getInvitadoSeleccionado(), importeMinimo);
            }
            javax.swing.GroupLayout v_pagosLayout = new javax.swing.GroupLayout(v_pagos.getContentPane());
            v_pagos.getContentPane().setLayout(v_pagosLayout);
            v_pagosLayout.setHorizontalGroup(
                    v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v_pagosLayout.createSequentialGroup().addContainerGap().addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            v_pagosLayout.setVerticalGroup(
                    v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_pagosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
            p_pagos.setContenedor(v_pagos);
            v_pagos.getContentPane().add(p_pagos);
            p_pagos.iniciaVista();

            Sesion.setTicket(ticketNuevaVenta);
            v_pagos.setVisible(true);

        } catch (Exception e) {
            ventana_padre.crearError("No se pudo abrir la ventana de pagos");
            log.error("No se pudo abrir la ventana de pagos", e);
        }
    }

    private void accionCrearVentanaImportes() {
        log.info("Iniciar Ventana de Importes por el importe de la venta");
        try {
            // Mostramos la pantalla de abonos propios
            BigDecimal abonoMinimo = null;
            BigDecimal abonoMaximo = null;

            abonoMaximo = ticketNuevaVenta.getTotales().getTotalAPagar();

            crearVentanaPagos(abonoMaximo, abonoMaximo);
            //v_importe_a_pagar.setVisible(true);
        } catch (Exception e) {
            ventana_padre.crearError("No se pudo abrir la ventana de importes");
            log.error("No se pudo abrir la ventana de importes");
        }
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        t_introduccionArticulos.requestFocus();
    }

    public JPrincipal getVentanaPadre() {
        return this.ventana_padre;
    }

    public void creaVentanaBusquedaArticulos() {
        b_ventasBuscarArticuloActionPerformed(null);
    }

    private void accionAddArticulos() {
        try {
            if (modo == MODO_ADD_PLAN_NOVIOS) {
                planNovio.addArticulosPlan(ticketNuevaVenta);
                ventana_padre.crearConfirmacion("Se han añadido los artículos al Plan Novios");
                ventana_padre.irMostrarPlanNovio();
            } else {
                addArticulosReserva = reservacion.addArticulosReserva(ticketNuevaVenta, reservacion.getReservacion(), reservacion.getInvitadoSeleccionado());
                if (reservacion.getAmpliacionAbono() != null && reservacion.getAmpliacionAbono().compareTo(BigDecimal.ZERO) > 0) {
                    modo = MODO_AMPLIACION_ABONO_INICIAL;
                    accionAccederAPagos();
                    if (!p_pagos.isCancelado()) {
                        reservacion.ejecutaAnadirArticulosReserva(addArticulosReserva, null);
                        ventana_padre.crearConfirmacion("Se han añadido los artículos a la reservación");
                    }
                } else {

                    ventana_padre.crearConfirmacion("Se han añadido los artículos a la reservación");
                    ventana_padre.showView("ident-cliente");
                }
            }
        } catch (Exception e) {
            log.error("Error Añadiendo artículos a la reserva : " + e.getMessage(), e);
            ventana_padre.crearError("Error Añadiendo artículos a la reserva : ");
        }
    }

    private boolean crearLineaTicketCupon() {
        try {
            CodigoBarrasCuponPromo codigoCupon = new CodigoBarrasCuponPromo(t_introduccionArticulos.getCodArticulo());
            ticketNuevaVenta.crearLineaTicketCupon(codigoCupon);
        } catch (NotAValidBarCodeException e) {
            return false;
        } catch (CuponNotValidException e) {
            ventana_padre.crearError(e.getMessage());
        } catch (CuponException e) {
            ventana_padre.crearError(e.getMessage());
        }
        return true;
    }

    private void accionIntroducirCantidad() {
        log.info("Estableciendo foco en cantidad");
        if (TicketTableCellRenderer.isModoConCantidad()) {
            t_introduccionArticulos.getT_Cantidad().requestFocus();
        }
    }

    private void accionCancelarEdicionLinea() {
        resetearFormularioEnvio();
        t_descuento_valor.setValidacionHabilitada(false);
        v_descuento_linea.setVisible(false);
        t_introduccionArticulos.requestFocus();
        t_descuento_valor.setValidacionHabilitada(true);
        this.clearError();
    }

    private void imprimirCotizacion(boolean imprimir) {
        log.info("ACCION REALIZAR RESERVACIÓN");

        if (modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS || modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS || (modo == MODO_AMPLIACION_ABONO_INICIAL)) {
//            if(Sesion.getTicketReservacion() == null || Sesion.getTicketReservacion().getFacturacion()==null){
//                if(Sesion.getTicketReservacion().getCliente().getCodcli().equals(Sesion.getClienteGenerico().getCodcli())){
//                    Sesion.getTicketReservacion().setFacturacionClienteGenerico();
//                } else {
//                    Sesion.getTicketReservacion().setFacturacionCliente();
//                }
//            }            
            accionAccederAPagos();
        } //Si es de tipo cotización, cremaos el Documento y lo almacenamos en BBDD
        else if (modo == JReservacionesVentasCanBaby.MODO_COTIZACION) {
            try {
                //Imprimimos el documento
                cotizacion.setIdCotizacion(ServicioContadoresCaja.obtenerContadorCotizacion());
                ticketNuevaVenta.setFacturacion(new FacturacionTicketBean(ticketNuevaVenta.getCliente()));
                ts.limpiarListaDocumentos();
                ticketNuevaVenta.setId_ticket(cotizacion.getIdCotizacion());
                boolean ventanaAceptada = ts.imprimirCotizacion(ticketNuevaVenta, imprimir);
                if (ventanaAceptada) {
                    ts.limpiarListaDocumentos();
                    ts.imprimirCotizacion(ticketNuevaVenta, false);
                    //Creamos la cotizacion en BBDD y almacenamos el documento en la BBDD
                    CotizacionesService.crearCotizacion(cotizacion, ticketNuevaVenta, ts.getDocumentosImpresos(), DocumentosBean.COTIZACION);

                    ts.limpiarListaDocumentos();

                    ventana_padre.showView("ident-cliente");
                }
            } catch (ContadorException ex) {
                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
                ventana_padre.crearError("No se ha podido crear la cotización");
            } catch (CotizacionException ex) {
                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
                ventana_padre.crearError("No se ha podido crear la cotización");
            } catch (TicketPrinterException ex) {
                log.error("b_ventas_pagosActionPerformed() - No se ha podido almacenar la cotización en la base de datos", ex);
                ventana_padre.crearError("No se ha podido crear la cotización");
            }
        } else {
            accionAddArticulos();
        }
    }

    @Override
    public LineaTicket crearLineaArticulo(String codigo, Articulos art, int numero, boolean compruebaStock) throws TicketNuevaLineaException {
        try {
            if ((isModoAddPlanNovios() || isModoAdd()) && compruebaStock && (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS))) {
                //if (!ServicioStock.isStockDisponibleVenta(art, numero)) { // && !Sesion.config.isModoDesarrollo())
                //JPrincipal.getInstance().crearError("Artículo fuera de stock");
                //return null;
                // Pedimos autorización
                int stockDisponibleVenta = ServicioStock.isStockDisponibleVenta(art);
                if (stockDisponibleVenta >= numero) {
                    String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.STOCK);
                    String idLog = "";
                    if (isModoAddPlanNovios()) {
                        idLog = planNovio.getCodPlanAsString();
                    }
                    if (isModoAdd()) {
                        idLog = reservacion.getReservacion().getIdRes();
                    }
                    ServicioLogAcceso.crearAccesoLogAdmitirFueraStock(codAdministrador, idLog + "-" + art.getIdItem());
                }
            }
            Tarifas tar = tarifasServices.getTarifaArticulo(art.getCodart());
            ticketNuevaVenta.crearLineaCompraReserva(reservacion, art, ticketReservacion, tar, codigo, modo, numero);
            refrescaTablaTicket();
        } catch (SinPermisosException e) {
            log.debug("No se tiene permisos para añadir el artículo con falta de stock.");
        } catch (ArticuloEnReservaNotFoundException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        } catch (StockException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        } catch (TarifaException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        }

        return null;
    }

    public boolean isModoAddPlanNovios() {
        return this.modo == MODO_ADD_PLAN_NOVIOS;
    }

    public boolean isModoBBSH() {
        return this.modo == MODO_BBSH;
    }

    public boolean isModoCotizacion() {
        return this.modo == MODO_COTIZACION;
    }

    public boolean isModoPagoArticulosPlanNovios() {
        return this.modo == MODO_PAGO_ARTICULOS_PLAN_NOVIOS;
    }

    public boolean isModoPagoArticulos() {
        return this.modo == MODO_PAGO_ARTICULOS;
    }

    public boolean isModoAdd() {
        return this.modo == MODO_ADD;
    }

    @Override
    public void setReferenciaSesionPDA(SesionPdaBean referenciaSesionPDA) {
        this.ticketNuevaVenta.setReferenciaSesionPDA(referenciaSesionPDA);
    }

    @Override
    public boolean esTipoVenta() {
        return false;
    }
}
