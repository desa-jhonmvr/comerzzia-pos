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

import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.gui.IBusquedasArticulos;
import com.comerzzia.jpos.gui.IVenta;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JBuscar;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.JVentas;
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
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorDecimal;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticuloException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueoFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueosServices;
import com.comerzzia.jpos.servicios.interfazventa.OpcionesVentaManager;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.listapda.ListaPDAException;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.EnumTipoCliente;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.imagenes.Imagenes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Dialog;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.KeyAdapter;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
 * @author MGRI
 */
public class JReservacionesVentas extends JPanelImagenFondo implements IVista, IVenta, KeyListener, IViewerValidationFormError, IBusquedasArticulos, IPagoImporteCall {
    //opciones de configuracion

    private static final Logger log = Logger.getMLogger(JReservacionesVentas.class);
    private static final long serialVersionUID = 1L;
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
    boolean esRecuperarReserva = false;
    
    /** Creates new form JReservacionesVentas */
    public JReservacionesVentas() {
        super();
        initComponents();


    }

    /**
     * Constructor
     * 
     * @param ventana_padre 
     */
    public JReservacionesVentas(JPrincipal ventana_padre) {
        super();
        this.ventana_padre = ventana_padre;
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        }
        catch (IOException ex) {
        }
        initComponents();

        Imagenes.cambiarImagenPublicidad(jLabel30);

        URL myurl;

        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        f_pagos2.setIconImage(icon.getImage());
        v_buscar_articulo.setIconImage(icon.getImage());
        v_datos_envio.setIconImage(icon.getImage());
        v_descuento_linea.setIconImage(icon.getImage());
        v_seleccion_linea.setIconImage(icon.getImage());
        v_pagos.setIconImage(icon.getImage());
        v_pagos.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
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
        t_descuento_valor.addValidador(new ValidadorDecimal(new BigDecimal("99.99"), BigDecimal.ZERO, 4), this);
        t_cantidad_valor.addValidador(new ValidadorEntero(1, Integer.MAX_VALUE, 1), this);
        t_precio_valor.addValidador(new ValidadorDecimal(null, new BigDecimal("0.0001"), 4), this);

        registraEventoBuscar();
        crearAccionFocoTabla(this, t_factura, KeyEvent.VK_T, InputEvent.CTRL_MASK);
        
        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);    
    }

    
    /**
     * Constructor
     * 
     * @param ventana_padre 
     */
    public JReservacionesVentas(JPrincipal ventana_padre, boolean esRecuperarReserva) {
        super();
        this.ventana_padre = ventana_padre;
        this.esRecuperarReserva = esRecuperarReserva;
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        }
        catch (IOException ex) {
        }
        initComponents();

        Imagenes.cambiarImagenPublicidad(jLabel30);

        URL myurl;

        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        f_pagos2.setIconImage(icon.getImage());
        v_buscar_articulo.setIconImage(icon.getImage());
        v_datos_envio.setIconImage(icon.getImage());
        v_descuento_linea.setIconImage(icon.getImage());
        v_seleccion_linea.setIconImage(icon.getImage());
        v_pagos.setIconImage(icon.getImage());
        v_pagos.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
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
        t_descuento_valor.addValidador(new ValidadorDecimal(new BigDecimal("99.99"), BigDecimal.ZERO, 4), this);
        t_cantidad_valor.addValidador(new ValidadorEntero(1, Integer.MAX_VALUE, 1), this);
        t_precio_valor.addValidador(new ValidadorDecimal(null, new BigDecimal("0.0001"), 4), this);

        registraEventoBuscar();
        crearAccionFocoTabla(this, t_factura, KeyEvent.VK_T, InputEvent.CTRL_MASK);
        
        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);        
    }
    /**
     *  Función de Inicialización al cambiar de vista
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

        lineasTicket.setLineas(Sesion.getTicketReservacion().getLineas().getLineas());

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);
        
        // Inicializamos información de cliente
        lb_cliente_tipo_socio.setText("");
        lb_tarjeta.setIcon(null);
        
        // mostramos el cliente seleccionado
        t_venta_cliente.setText(Sesion.getTicketReservacion().getCliente().getNombreVenta());
        if (Sesion.getTicketReservacion().getCliente().isSocio() && Sesion.getTicketReservacion().getCliente().getTipoAfiliado() != null) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(Sesion.getTicketReservacion().getCliente().getTipoAfiliado());
            if (tipoAfiliado != null) {
                lb_cliente_tipo_socio.setText("Tipo Afiliado " + tipoAfiliado.getDesTipoAfiliado());
                if (tipoAfiliado.getImagenTarjetaAfiliado() != null) {
                    lb_tarjeta.setIcon(tipoAfiliado.getImagenTarjetaAfiliado());
                }
            }
        }
        else {
            lb_cliente_tipo_socio.setText("");
            lb_tarjeta.setIcon(null);
        }
        t_venta_cliente.setHorizontalAlignment(JLabel.CENTER);

        // Iniciamos la tabla de tickets
        // Data model y renderer de la tabla
        t_factura.setModel(new TicketTableModel(Sesion.getTicketReservacion().getLineas()));
        t_factura.setDefaultRenderer(Object.class, new TicketTableCellRenderer());

        // Establecemos el modo en la introduccción de Artículo.
        t_introduccionArticulos.setModoCantidad(TicketTableCellRenderer.isModoConCantidad());
        // Refrescamos
        refrescaTablaTicket();

        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();
        t_introduccionArticulos.reset();
        l_factura_cabecera_id.setText(Sesion.getReservacion().getReservacion().getReservaTipo().getDesTipo());

        // comprobamos si el cliente puede subir su nivel de afiliación con esta compra
        //mostrarEtiquetasSocio(false);
        jLabel_compra_socio.setVisible(false);
        /*if (Sesion.getTicketReservacion().getCliente().isSocio()) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(Sesion.getTicketReservacion().getCliente().getTipoAfiliado());
            Solicitado por Miguel Gomez el 01/10/2018
            if (tipoAfiliado != null && tipoAfiliado.getSiguienteNivel() != null && tipoAfiliado.isCompraSiguienteNivelDefinido()) {
                jLabel_compra_socio.setVisible(true);
                jLabel_compra_socio.setText("Con una compra de $ " + tipoAfiliado.getCompraSiguienteNivel() + " será socia " + tipoAfiliado.getSiguienteNivel().getDesTipoAfiliado());
            }
        }*/
        if (Sesion.getTicketReservacion().getCliente().isClienteGenerico()) {
            b_datos_facturacion.setEnabled(false);
        }
        else {
            b_datos_facturacion.setEnabled(true);
        }
        
        if (esRecuperarReserva) {
            t_introduccionArticulos.setActivo(false);
            b_ventas_pagos.setText("<html><center>Pagos<br/>F9</center></html>");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        v_datos_envio = new javax.swing.JDialog();
        p_datos_envio = new com.comerzzia.jpos.gui.JDatosEnvio();
        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        v_importe_a_pagar = new javax.swing.JDialog();
        p_importe_a_pagar = new com.comerzzia.jpos.gui.reservaciones.JReservacionesImportePago();
        v_editar_cantidad = new javax.swing.JDialog();
        p_editar_cantidad = new com.comerzzia.jpos.gui.devoluciones.JIntroducirCantidad();
        v_descuento_linea = new javax.swing.JDialog();
        P_descuento_valor = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);
        t_descuento_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel6 = new javax.swing.JLabel();
        o_pendiente_entrega = new javax.swing.JCheckBox();
        o_env_adomicilio = new javax.swing.JCheckBox();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error_descuento = new javax.swing.JLabel();
        lb_error_ml = new com.comerzzia.jpos.gui.components.JMultilineLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        t_cantidad_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel10 = new javax.swing.JLabel();
        t_precio_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel11 = new javax.swing.JLabel();
        jTextArea1 = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
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

        v_datos_envio.setAlwaysOnTop(true);
        v_datos_envio.setMinimumSize(new java.awt.Dimension(400, 498));
        v_datos_envio.setModal(true);

        javax.swing.GroupLayout v_datos_envioLayout = new javax.swing.GroupLayout(v_datos_envio.getContentPane());
        v_datos_envio.getContentPane().setLayout(v_datos_envioLayout);
        v_datos_envioLayout.setHorizontalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_datos_envioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_datos_envioLayout.setVerticalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_datos_envioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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
        v_editar_cantidad.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        javax.swing.GroupLayout v_editar_cantidadLayout = new javax.swing.GroupLayout(v_editar_cantidad.getContentPane());
        v_editar_cantidad.getContentPane().setLayout(v_editar_cantidadLayout);
        v_editar_cantidadLayout.setHorizontalGroup(
            v_editar_cantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_editar_cantidadLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_editar_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        v_editar_cantidadLayout.setVerticalGroup(
            v_editar_cantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_editar_cantidadLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_editar_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        v_descuento_linea.setAlwaysOnTop(true);
        v_descuento_linea.setMinimumSize(new java.awt.Dimension(360, 320));
        v_descuento_linea.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_descuento_linea.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        P_descuento_valor.setMaximumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setMinimumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setName("cantidad_valor"); // NOI18N

        t_descuento_valor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_descuento_valorActionPerformed(evt);
            }
        });
        t_descuento_valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_descuento_valorKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setText("Descuento:");

        o_pendiente_entrega.setText("Pendiente Entrega");
        o_pendiente_entrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_pendiente_entregaActionPerformed(evt);
            }
        });
        o_pendiente_entrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                o_pendiente_entregaKeyPressed(evt);
            }
        });

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
        jb_cancel1.setMnemonic('C');
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

        lb_error_descuento.setFont(new java.awt.Font("Comic Sans MS", 0, 10)); // NOI18N
        lb_error_descuento.setForeground(new java.awt.Color(153, 0, 0));
        lb_error_descuento.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lb_error_ml.setForeground(new java.awt.Color(255, 51, 0));
        lb_error_ml.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lb_error_ml.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jLabel2.setText("%");

        jLabel8.setDisplayedMnemonic('t');
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Cantidad:");

        jLabel10.setDisplayedMnemonic('P');
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setText("Precio:");

        jLabel11.setText("Motivo:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);

        P_descuento_valor.registraEventoEnterBoton();

        javax.swing.GroupLayout P_descuento_valorLayout = new javax.swing.GroupLayout(P_descuento_valor);
        P_descuento_valor.setLayout(P_descuento_valorLayout);
        P_descuento_valorLayout.setHorizontalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(t_precio_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(t_cantidad_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(144, 144, 144))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, P_descuento_valorLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                                .addGap(111, 111, 111)
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(t_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                                .addGap(107, 107, 107)
                                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(o_env_adomicilio)
                                                    .addComponent(o_pendiente_entrega))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addComponent(lb_error_descuento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_error_ml, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_descuento_valorLayout.createSequentialGroup()
                    .addContainerGap(65, Short.MAX_VALUE)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(32, Short.MAX_VALUE)))
        );
        P_descuento_valorLayout.setVerticalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(t_precio_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(t_cantidad_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(t_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel11))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(o_pendiente_entrega)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(o_env_adomicilio)))
                .addGap(27, 27, 27)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_error_descuento, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(lb_error_ml, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_descuento_valorLayout.createSequentialGroup()
                    .addContainerGap(145, Short.MAX_VALUE)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(111, Short.MAX_VALUE)))
        );

        t_descuento_valor.getAccessibleContext().setAccessibleName("Descuento:");
        t_descuento_valor.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout v_descuento_lineaLayout = new javax.swing.GroupLayout(v_descuento_linea.getContentPane());
        v_descuento_linea.getContentPane().setLayout(v_descuento_lineaLayout);
        v_descuento_lineaLayout.setHorizontalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_descuento_lineaLayout.createSequentialGroup()
                .addComponent(P_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_descuento_lineaLayout.setVerticalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(P_descuento_valor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        b_ventas_pagos.setText("<html><center>Realizar<br>Reservación<br/>F9</center></html>");
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
        if (ventana_padre.crearVentanaConfirmacion("Esta acción cancelará la reserva actual, ¿Desea continuar?")) {
            try {
                String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_VENTA);
                ServicioLogAcceso.crearAccesoLogCancelarReserva(codAdministrador, Sesion.getReservacion().getReservacion().getReservaTipo().getDesTipo(), Sesion.getReservacion().getReservacion().getUidReservacion());
                ventana_padre.resetReservacionesClienteView(); // Por si se puede recuperar una venta y luego ir a obtener información del cliente
                ventana_padre.showView("ident-cliente");
                Sesion.borrarTicketReservacion();
            }
            catch (SinPermisosException ex) {
                log.debug("No se tiene permisos para borrar línea de tickets.");
            }
            catch (Exception e) {
                log.error("Error al cancelar la reserva: " + e.getMessage(), e);
            }
        }
}//GEN-LAST:event_b_ventas_datosclienteActionPerformed

    private void b_ventas_pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_pagosActionPerformed
        log.info("ACCION REALIZAR RESERVACIÓN");
        accionAccederAPagos();

}//GEN-LAST:event_b_ventas_pagosActionPerformed

    private void b_ventas_appalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_appalActionPerformed
        //log.info("MENÚ PRINCIPAL");
        //ventana_padre.mostrarMenu("reservaciones-venta", "NUEVA_RESERVACION");
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
    }
    catch (Exception e) {
        log.error("No se pudo seleccionar la línea: " + e.getMessage(), e);
    }

}//GEN-LAST:event_b_ventas_seleccionActionPerformed

private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
    p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

private void b_datos_facturacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_datos_facturacionActionPerformed
    if (!Sesion.getTicketReservacion().getCliente().isClienteGenerico()) {

        FacturacionTicketBean ft = JPrincipal.crearVentanaFacturacion(false, Sesion.getTicketReservacion());

        if (ft == null) {
            log.debug("Se canceló la edición de datos de facturación");
        }
    }
}//GEN-LAST:event_b_datos_facturacionActionPerformed

private void v_buscar_articuloWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_buscar_articuloWindowGainedFocus
    p_buscar_articulo.establecerFoco();
}//GEN-LAST:event_v_buscar_articuloWindowGainedFocus

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
                Integer indexLinea = Sesion.getTicketReservacion().getIndexLinea(codigobarra);
                if (indexLinea != null && t_seleccion_cb.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                }
                else if (t_seleccion_cb.getText().length() > 0) {
                    lb_error.setText("El código de barras ingresado es incorrecto");
                }
                else {
                    lb_error.setText("No se encontro ningún artículo para ese código de barras");
                    log.debug("No se encontró ningún artículo con codigo de barras " + codigobarra);
                }
            }
        }
        catch (Exception e) {
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
                Integer indexLinea = Sesion.getTicketReservacion().getIndexLineaCodigoArticulo(codigoArticulo);
                if (indexLinea != null && t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                }
                else if (t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("El código de artículo ingresado es incorrecto");
                }
                else {
                    lb_error.setText("No se encontro ningún artículo con el código de artículo");
                    log.debug("No se encontro ningún articulo con el código de artículo " + codigoArticulo);
                }
            }
        }
        catch (Exception e) {
            log.error("Error al seleccionar línea", e);
        }
    }//GEN-LAST:event_t_seleccion_cb1KeyTyped

    private void b_ventas_edicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_edicionActionPerformed
        log.info("ACCION EDITAR LÍNEA");
        if (esRecuperarReserva) {
            editarRecuperar();
        } else {
            editarNuevo();
        }
    }//GEN-LAST:event_b_ventas_edicionActionPerformed

    private void t_descuento_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_descuento_valorActionPerformed

    }//GEN-LAST:event_t_descuento_valorActionPerformed

    private void t_descuento_valorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_descuento_valorKeyPressed
        if (evt.getKeyChar() == '\n') {
            accionAplicarDescuento();
        }
    }//GEN-LAST:event_t_descuento_valorKeyPressed

    private void o_pendiente_entregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_pendiente_entregaActionPerformed
        if (o_pendiente_entrega.isSelected()) {
            o_env_adomicilio.setSelected(false);
        }
    }//GEN-LAST:event_o_pendiente_entregaActionPerformed

    private void o_pendiente_entregaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_pendiente_entregaKeyPressed

    }//GEN-LAST:event_o_pendiente_entregaKeyPressed

    private void o_env_adomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_env_adomicilioActionPerformed
        if (o_env_adomicilio.isSelected()) {
            o_pendiente_entrega.setSelected(false);
        }
    }//GEN-LAST:event_o_env_adomicilioActionPerformed

    private void o_env_adomicilioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_env_adomicilioKeyTyped

    }//GEN-LAST:event_o_env_adomicilioKeyTyped

    private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
        accionCancelarEdicionLinea();
    }//GEN-LAST:event_jb_cancel1ActionPerformed

    private void jb_cancel1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_cancel1KeyTyped
        if (evt.getKeyChar() == '\n') {
            accionCancelarEdicionLinea();
        }
    }//GEN-LAST:event_jb_cancel1KeyTyped

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        accionAplicarDescuento();
    }//GEN-LAST:event_jb_ok1ActionPerformed

    private void jb_ok1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_ok1KeyTyped

    }//GEN-LAST:event_jb_ok1KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo P_descuento_valor;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_datos_facturacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_appal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_datoscliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_edicion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_otro_local;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_pagos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_seleccion;
    private javax.swing.JFrame f_pagos2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_compra_socio;
    private javax.swing.JLabel jLabel_numArticulos;
    private javax.swing.JPanel jPanel1;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm jTextArea1;
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
    private javax.swing.JCheckBox o_pendiente_entrega;
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
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cantidad_valor;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_descuento_valor;
    private javax.swing.JTable t_factura;
    private com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp t_introduccionArticulos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_precio_valor;
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
     *  Función que desabilita los elementos de formulario que quedan debajo de la pantalla de pagos
     */
    private void desabilita_elementos_formulario() {
        m_ventas.setVisible(false);
        b_ventas_appal.setEnabled(false);
        b_ventas_datoscliente.setEnabled(false);
        b_ventas_pagos.setEnabled(false);

        t_factura.setEnabled(false);
        t_factura.setVisible(false);
        sp_t_factura.setEnabled(false);
        sp_t_factura.setVisible(false);

        t_introduccionArticulos.setActivo(false);


    }

    /**
     *  Función que habilita los elementos de formulario que quedan debajo de la pantalla de pagos
     */
    protected void habilita_elementos_formulario() {
        m_ventas.setVisible(true);
        b_ventas_appal.setEnabled(true);
        b_ventas_datoscliente.setEnabled(true);
        b_ventas_pagos.setEnabled(true);
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
            }
            else if (codigo.length() > 2 && (codigo.substring(0, 2).equalsIgnoreCase("bm") || codigo.substring(0, 2).equalsIgnoreCase("sk"))) {
                OpcionesVentaManager.anadeListaPDA(Sesion.getTicketReservacion(), codigo, this);
                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            }
            else if (codigo.length() > 0) {
                Articulos art;
                if(codigo.length()==9 && codigo.charAt(4)=='.'){
                    art = articulosServices.getArticuloCod(codigo);
                    if(art == null){
                        throw new ArticuloNotFoundException("No se ha encontrado artículo con código: " + codigo);
                    }
                    //Buscar codigoBarras del código artículo introducido
                    codigo = Articulos.formateaCodigoBarras(ArticulosServices.consultarCodigoBarras(codigo));                        
                } else {
                      /**
                     *Cambio Rd para lector codigo de barras se agregan o quitan ceros segun el caso 
                     */
                    
                    if (codigo.length() == 11) {
                        codigo = "00" + codigo;
                    } else {
                        if (codigo.length() == 12) {
                            codigo = "0" + codigo;
//                            if(!(""+codigo.charAt(0)).equals("0")){
//                            codigo = "0" + codigo;
//                            }
//                        } else {
//                            if (codigo.length() == 13) {
//                                if((""+codigo.charAt(0)).equals("0")){
//                           codigo= codigo.substring(1);  
//                            }
                            
                            }else{
                                if(codigo.length()==14){
                                  codigo= codigo.substring(1);  
                                }
                            }
//                        }
                    }
                    
                    
                    log.debug("Codigo Modificado: " + codigo);
                    codigo = Articulos.formateaCodigoBarras(codigo);
                    art = articulosServices.getArticuloCB(codigo);
                }

                BloqueosServices.isItemBloqueado(art.getCodmarca().getCodmarca(), art.getIdItem(), art.getCodart());


                Tarifas tar = tarifasServices.getTarifaArticulo(art.getCodart());

                // Comprobamos que hay stock, si es canastillas no se comprueba
                if (!Sesion.getReservacion().getReservacion().isTipoCanastillas() && (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS) )){
                  //  if (!ServicioStock.isStockDisponibleVenta(art, t_introduccionArticulos.getCantidadInt())) { // && !Sesion.config.isModoDesarrollo())
                        // Pedimos autorización
                        //JPrincipal.getInstance().crearError("Artículo fuera de stock");
                        //return;
                    int stockDisponibleVenta  = ServicioStock.isStockDisponibleVenta(art);
                    if (stockDisponibleVenta < t_introduccionArticulos.getCantidadInt()){                        
                        String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.STOCK, "Artículo fuera de stock");
                        ServicioLogAcceso.crearAccesoLogAdmitirFueraStock(codAdministrador, Sesion.getTicketReservacion().getIdFactura() + "-" + art.getIdItem());
                    }
                }
                // Añadimos la linea del ticket              
                Sesion.getTicketReservacion().crearLineaTicket(codigo, art, t_introduccionArticulos.getCantidadInt(), tar, 0);
                log.debug("Intentando leer y cambiar foto.");
               // Imagenes.cambiarImagenArticulo(jLabel7, art.getReferenciaInterna());

                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            }
        }
        catch (ArticuloNotFoundException e) {
            log.info("No se ha encontrado el artículo");
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
        catch (ListaPDAException e) {
            log.error("Error añadiendo artículos desde PDA: " + e.getMessage(), e);
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
        catch (TicketNuevaLineaException e) {
            log.error("Error creando nueva línea de ticket para reservación: " + e.getMessage(), e);
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
        catch (StockException e) {
            log.error("Error consultando stock para reservación: " + e.getMessage(), e);
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
        catch (TarifaArticuloNotFoundException e) {
            log.info("El artículo seleccionado no tiene tarifa");
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
        catch (BloqueoFoundException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        }
       /* catch (SinPermisosException e) {
            log.debug("No se tiene permisos para añadir el artículo con falta de stock.");
        }*/
        catch (Exception e) {
            log.error("Error al seleccionar el artículo: " + e.getMessage(), e);
            log.debug(t_introduccionArticulos.getCodArticulo());
            ventana_padre.crearError("Error al insertar el artículo");
            t_introduccionArticulos.requestFocus();
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
        l_v_total.setText("$ " + Sesion.getTicketReservacion().getTotales().getTotalAPagar().toString());
        jLabel_numArticulos.setText("<html><b>" + Sesion.getTicketReservacion().getLineas().getNumLineas() + "</b> artículos ingresados</html>");
        t_factura.setModel(new TicketTableModel(Sesion.getTicketReservacion().getLineas()));

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
        if (Sesion.getTicketReservacion().getLineas().getLineas().isEmpty()) {
            b_ventas_pagos.setEnabled(false);
            removeHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "IdentClientF9");
        }
        else {
            b_ventas_pagos.setEnabled(true);
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
                
                ventana_padre.crearError("No se puede eliminar un art\u00EDculo, debe utilizar la opci\u00F3n desde la reserva ");
                /*try {
                    
                    
                    ventana_padre.compruebaAutorizacion(Operaciones.BORRAR_LINEA_VENTA);
                    lineaSelecionada = t_factura.getSelectedRow();
                    if (lineaSelecionada >= 0) {
                        //eliminar el item de la reserva
                        List lart = new LinkedList();
                        ReservaArticuloBean articuloABorar = Sesion.getReservacion().getReservacion().getReservaArticuloList().get(lineaSelecionada);
                        lart.add(articuloABorar);
                        ReservacionesServicios.eliminarReservaArticulos(Sesion.getReservacion().getReservacion(), lart);
                        if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)|| Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)){
                            ReservacionesServicios.accionEliminarArticuloStock(articuloABorar, Sesion.getReservacion());
                        }        
                        Sesion.getTicketReservacion().eliminarLineaTicket(lineaSelecionada);
                        
                    }
                    else {
                        Sesion.getTicketReservacion().eliminarLineaTicket();
                    }
                    refrescaTablaTicket();
                    
                }
                catch (SinPermisosException ex) {
                    log.debug("No se tiene permisos para borrar línea de tickets.");
                }*/
            }
        };
        addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);
        
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        Action listenerCtrlO = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                
                Sesion.getReservacion().setObservaciones(ventana_padre.crearVentanaObservaciones(Sesion.getReservacion().getObservaciones()));
                Sesion.getTicketReservacion().setObservaciones(Sesion.getReservacion().getObservaciones());
            }
        };
        addHotKey(ctrlO, "ObservacionesCtrlO", listenerCtrlO);        
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
        
        t_factura.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_F2){
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
        o_pendiente_entrega.setSelected(false);
    }

    private void enviaFormularioDatosAdicionales(String autorizadorDescuento) {
        log.info("Enviar formulario de Datos Adicionales");
        BigDecimal valorDescuento = BigDecimal.ZERO;
        try {
            if (!t_descuento_valor.getText().isEmpty()) {
                valorDescuento = new BigDecimal(t_descuento_valor.getText());
            }
            else {
                valorDescuento = BigDecimal.ZERO;
            }

            DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
            datosAdicionales.setEnvioDomicilio(o_env_adomicilio.isSelected());
            datosAdicionales.setRecogidaPosterior(o_pendiente_entrega.isSelected());
            datosAdicionales.setDescuento(valorDescuento);
            datosAdicionales.setAutorizador(autorizadorDescuento);

            Sesion.getTicketReservacion().setDatosAdicionalesLinea(indexLineaAEditar, datosAdicionales);

        }
        catch (NumberFormatException e) {
            log.info("Formato numérico incorrecto");
        }
        catch (Exception e) {
            log.error("Error en envío de formlario de datos adicionales " + e.getMessage(), e);
        }
    }

    private void accionAccederAPagosAux() {
        log.info("Acción acceder a Pagos Aux");
        try {
            if (Sesion.getReservacion().getReservacion().getReservaTipo().getAbonoInicial() ||
                (!Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosParciales() && esRecuperarReserva)) {
                // Ir a pagos
                if (esRecuperarReserva) {
                    for (ReservaArticuloBean articulo: Sesion.getReservacion().getReservacion().getReservaArticuloList()) {
                        for (LineaTicket linea : Sesion.getReservacion().getTicket().getLineas().getLineas()) {
                            if (articulo.getCodart().equals(linea.getArticulo().getCodart())) {
                                articulo.setDescuento(linea.getDescuento());
                            }
                        }
                    }
                }
                accionCrearVentanaImportes();
            }
            else {
                if (Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosParciales()
                        && Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosPropietario()) {

                    // Pregunta si desea realizar abono inicial
                    if (ventana_padre.crearVentanaConfirmacion("¿Desea realizar un abono inicial?")) {
                        //respuesta si
                        //accionIrAPagos();
                        accionCrearVentanaImportes();
                    }
                    else { //respuesta no: realizar reserva sin pagos
                        Sesion.getReservacion().guardaReservacion();
                        accionAumentaStockReservado();
                        ventana_padre.crearConfirmacion("Se realizó la reservación satisfactoriamente");
                        ventana_padre.showView("ident-cliente");

                    }
                }
                else {
                    // Acción de realizar reserva sin pagos
                    Sesion.getReservacion().guardaReservacion();
                    accionAumentaStockReservado();
                    ventana_padre.crearConfirmacion("Se realizó la reservación satisfactoriamente");
                    ventana_padre.showView("ident-cliente");
                }
            }
        }
        catch (ReservasException ex) {
            ventana_padre.crearError(ex.getMessage());
            log.error("Error al acceder a pagos: " + ex.getMessage(), ex);
        }
    }

    private void accionAccederAPagos() {
        log.info("Acción acceder a Pagos");
        try {
            // Si no es una reservación que permita abonos seguimos
            if ((Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosParciales()
                    && Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosPropietario())) {

                // Si hay Envio a domicilio para algún artículo
                if (Sesion.getTicketReservacion().hayEnvioADomicilio()) {
                    p_datos_envio.inicializaFormulario();
                    v_datos_envio.setVisible(true);
                    if (p_datos_envio.isAceptado()) {
                        accionAccederAPagosAux();
                    }
                }
                else {
                    accionAccederAPagosAux();
                }
            }
            else {
                accionAccederAPagosAux();
            }
        }
        catch (Exception e) {
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
        Sesion.setEdicion(true);
        log.info("Acción Aplicar Descuentos");
        try {
            t_cantidad_valor.validar();
            t_descuento_valor.validar();
            t_precio_valor.validar();
            BigDecimal valorDescuento;
            BigDecimal valorDescuentoAnterior;
            Integer cantidad_valor;
            BigDecimal precio_valor;
            BigDecimal precio_valor_anterior;
            if (!t_descuento_valor.getText().isEmpty()) {
                valorDescuento = (new BigDecimal(t_descuento_valor.getText()));
            } else {
                valorDescuento = BigDecimal.ZERO;
            }
            if (!t_cantidad_valor.getText().isEmpty()) {
                cantidad_valor = new Integer(t_cantidad_valor.getText());
            } else {
                cantidad_valor = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getCantidad();
            }

            if (!t_precio_valor.getText().isEmpty()) {
                precio_valor = new BigDecimal(t_precio_valor.getText());
            } else {
                precio_valor = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getPrecioTotal();
            }

            if (Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDatosAdicionales() == null) {
                valorDescuentoAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDescuento().setScale(2, RoundingMode.HALF_UP);
            } else {
                valorDescuentoAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDatosAdicionales().getDescuento();
            }
            if (t_descuento_valor.getText().isEmpty()) {
                valorDescuento = BigDecimal.ZERO;
            }
            LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
            precio_valor_anterior = lineaTicketAnterior.getPrecioTotalTarifaOrigen();

            boolean procede = true;
            String referencia = Sesion.getTicket().getUid_ticket() + " " + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "-" + Sesion.getTicket().getCodcaja() + "-" + Sesion.getTicket().getId_ticket();
            String motivo = jTextArea1.getText();

            String autorizadorDescuento = null;
            if ((valorDescuento.compareTo(valorDescuentoAnterior) != 0) || (precio_valor.compareTo(precio_valor_anterior) != 0)) {
                if (motivo.isEmpty() || motivo.length() > 60) {
                    procede = false;
                    lb_error_ml.setText("El motivo es obligatorio y de máximo 60 caracteres.");
                } else {
                    procede = true;
                    autorizadorDescuento = ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
                    Sesion.getTicket().setAutorizadorVenta(autorizadorDescuento);
                }
            }

            if (procede) {
                log.debug("accionAplicarDescuento() - Se ha aplicado un descuento de: " + valorDescuento + " al artículo: " + lineaTicketAnterior.getArticulo().getCodart());
                enviaFormularioDatosAdicionales(autorizadorDescuento);
                v_descuento_linea.setVisible(false);
                Sesion.setEdicion(false);
                refrescaTablaTicket();

                //Guarda autorizador y motivo
                guardar(motivo, autorizadorDescuento, referencia, lineaTicketAnterior, precio_valor, precio_valor_anterior, valorDescuento, valorDescuentoAnterior, cantidad_valor);
            }
        } catch (ValidationFormException ex) {
            log.debug(ex.getMessage());
        } catch (SinPermisosException e) {
            log.debug("No se tienen permisos para modificar el descuento.");
        } catch (Exception e) {
            log.error("Error en la Acción de Aplicar Descuento: " + e.getMessage(), e);
        }
    }

    @Override
    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo) {
        crearVentanaPagos(importeMinimo, importeMaximo, true);
    }

    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo, boolean puedePagarTodo) {
        log.info("Acción Crear Ventana de Pagos");
        try {
            v_pagos.getContentPane().removeAll();

            if (esRecuperarReserva) {
                p_pagos = new JReservacionesPagosV(ventana_padre, Sesion.getReservacion(), importeMaximo, Sesion.getReservacion().getTicket().getCliente(), null, Sesion.getReservacion().getPorAbonar());
            } else{
                p_pagos = new JReservacionesPagosV(ventana_padre, Sesion.getReservacion(), importeMaximo, Sesion.getReservacion().getTicket().getCliente(), null, importeMinimo, puedePagarTodo);
            }
            javax.swing.GroupLayout v_pagosLayout = new javax.swing.GroupLayout(v_pagos.getContentPane());
            v_pagos.getContentPane().setLayout(v_pagosLayout);
            v_pagosLayout.setHorizontalGroup(
                    v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v_pagosLayout.createSequentialGroup().addContainerGap().addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            v_pagosLayout.setVerticalGroup(
                    v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_pagosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
            p_pagos.setContenedor(v_pagos);
            v_pagos.getContentPane().add(p_pagos);
            v_pagos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
            v_pagos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);         
            v_pagos.requestFocus();
            p_pagos.iniciaVista();
            v_pagos.setVisible(true);
            
            if(esRecuperarReserva){
                if (p_pagos.getPagoRealizado() != null) {
                    log.debug("El pago ha sido realizado por $" + p_pagos.getPagoRealizado().toString());
                    log.debug("El importe a abonar de la reserva antes del pago son $" + Sesion.getReservacion().getAbonosPorAbonar().toString());
                    if (!Sesion.getReservacion().getReservacion().getReservaTipo().getAbonosMayoresATotal()) {
                        if (!p_pagos.isCancelado()) {
                            log.debug("Se procede a la liquidación");
                            Sesion.getReservacion().getDatosAbono().setEsultimoAbono(true);
                            liquidarReserva(Sesion.getReservacion().getReservacion().getReservaArticuloList());
                            log.debug("Se realizó la liquidación");
                        } else {
                            log.debug("Se canceló el pago en la pantalla de pagos");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
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

            abonoMinimo = BigDecimal.ZERO; // Si el abono tiene un importe inicial se cambia mas adelante

            if (Sesion.getReservacion().getReservacion().getReservaTipo().getAbonosMayoresATotal()) {
                // valorMaximo null significa que es ilimitado           
            }
            else {
                abonoMaximo = Sesion.getTicketReservacion().getTotales().getTotalAPagar();
                if(esRecuperarReserva && Sesion.getReservacion().getReservacion().getReservaTipo().getPermiteAbonosParciales()){
                    Sesion.getTicket().inicializaTotales(abonoMaximo);
                    Pago pag = new Pago(Sesion.getTicket().getPagos(), Sesion.getReservacion().getReservacion().getCliente());
                    pag.setMedioPagoActivo(MedioPagoBean.getMedioPagoAbonoReservacion());
                    pag.setSaldoInicial(Sesion.getReservacion().getTotalAbonado());
                    pag.setTotal(Sesion.getReservacion().getTotalAbonado());
                    BigDecimal dctoAbonos = Numero.getTantoPorCientoMenosR(Sesion.getReservacion().getTotalAbonado(), Sesion.getReservacion().getAbonosReales());
                    pag.establecerDescuento(dctoAbonos.setScale(0, RoundingMode.HALF_UP));
                    pag.establecerEntregado("" + Sesion.getReservacion().getTotalAbonado());
                    pag.recalcularFromTotal(Sesion.getReservacion().getTotalAbonado());

                    Sesion.getTicket().crearNuevaLineaPago(pag);
                }
            }

            // Comprobamos si el tipo de pago tiene un abono mínimo
            if (Sesion.getReservacion().getReservacion().getReservaTipo().getAbonoInicial()) {
                //
                BigDecimal porcentajeMinimo = BigDecimal.ZERO;
                if (Sesion.getTicketReservacion().getCliente().isSocio()) {
                    TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(Sesion.getTicketReservacion().getCliente().getTipoAfiliado());
                    if (tipoAfiliado != null && tipoAfiliado.getPorcentajeAbonoInicial() != null) {
                        porcentajeMinimo = new BigDecimal(tipoAfiliado.getPorcentajeAbonoInicial());
                    }
                    else {
                        if (Sesion.getReservacion().getReservacion().getReservaTipo().getPorcentajeAbonoInicial() != null) {
                            porcentajeMinimo = Sesion.getReservacion().getReservacion().getReservaTipo().getPorcentajeAbonoInicial();
                        }
                    }
                }
                else {
                    if (Sesion.getReservacion().getReservacion().getReservaTipo().getPorcentajeAbonoInicial() != null) {
                        porcentajeMinimo = Sesion.getReservacion().getReservacion().getReservaTipo().getPorcentajeAbonoInicial();
                    }
                }
                if(EnumTipoCliente.EMPLEADO_COMOHOGAR.ordinal()==Sesion.getTicketReservacion().getCliente().getTipoCliente()){
                    porcentajeMinimo = new BigDecimal(Constantes.PORCENTAJE_MINIMO_EMPLEADO_COMOHOGAR);
                }
                
                abonoMinimo = abonoMaximo.multiply(porcentajeMinimo).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                
                if(EnumTipoCliente.EMPLEADO_COMOHOGAR.ordinal()==Sesion.getTicketReservacion().getCliente().getTipoCliente()){
                    if (abonoMinimo.compareTo(Constantes.VALOR_MINIMO_RESERVA_EMPLEADO_COMOHOGAR)>0){
                        abonoMinimo=Constantes.VALOR_MINIMO_RESERVA_EMPLEADO_COMOHOGAR;
                    }
                }
                

            }

            p_importe_a_pagar = new JReservacionesImportePago(abonoMinimo, abonoMaximo);
            p_importe_a_pagar.setPasarelaAPagos(this);
            p_importe_a_pagar.setContenedor(v_importe_a_pagar);
            p_importe_a_pagar.setVentana_padre(ventana_padre);

            crearVentanaPagos(abonoMinimo, abonoMaximo, esRecuperarReserva);

            if (!p_pagos.isCancelado()) {
                // Gestionamos el stock
                accionAumentaStockReservado();
            }

            //v_importe_a_pagar.setVisible(true);
        }
        catch (Exception e) {
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

    private void accionIntroducirCantidad() {
        log.info("Estableciendo foco en cantidad");
        if (TicketTableCellRenderer.isModoConCantidad()) {
            t_introduccionArticulos.getT_Cantidad().requestFocus();
        }
    }

    @Override
    public LineaTicket crearLineaArticulo(String codigo, Articulos art, int numero, boolean compruebaStock) throws TicketNuevaLineaException {
        try {
            Tarifas tar = tarifasServices.getTarifaArticulo(art.getCodart());
            Sesion.getTicketReservacion().crearLineaTicket(codigo, art, numero, tar, 0);
            refrescaTablaTicket();
            return null;
        }
        catch (ArticuloException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        }
    }

    @Override
    public void setReferenciaSesionPDA(SesionPdaBean referenciaSesionPDA) {
        Sesion.getTicketReservacion().setReferenciaSesionPDA(referenciaSesionPDA);
    }

    @Override
    public boolean esTipoVenta() {
        return false;
    }

    private void accionAumentaStockReservado() {
        // Actualizamos los Stocks
        try {
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
            logKardex.setUsuarioAutorizacion(Sesion.getTicketReservacion().getAutorizadorVenta() != null ?Sesion.getTicketReservacion().getAutorizadorVenta().getUsuario():"");
            logKardex.setFactura(String.valueOf(Sesion.getReservacion().getReservacion().getCodReservacion()));
            log.debug(" -- Aumentando Stock de Reserva ");
            ServicioStock.aumentaStockReserva(Sesion.getTicketReservacion().getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(Sesion.getTicketReservacion().getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, Sesion.getTicketReservacion().getTienda(), false);
        }
        catch (StockException e) {
            log.error("addArticulosPlan() - STOCK: No fué posible aumentar el stock reservado");
        }
    }
    
    public void liberarMemoria(){
        p_pagos = null;
        p_buscar_articulo.liberarMemoria();
    }
    
    private void accionCancelarEdicionLinea() {
        resetearFormularioEnvio();
        t_descuento_valor.setValidacionHabilitada(false);
        v_descuento_linea.setVisible(false);
        t_introduccionArticulos.requestFocus();
        t_descuento_valor.setValidacionHabilitada(true);
        this.clearError();
    }
    
    private void editarNuevo(){
        try {
            // ponemos la acción de eliminar ultimo elemento añadido a la lista
            lineaSelecionada = t_factura.getSelectedRow();
            if (lineaSelecionada == -1) {
                lineaSelecionada = t_factura.getRowCount() - 1;
            }
            this.indexLineaAEditar = lineaSelecionada;
            if (lineaSelecionada >= 0) {

                Integer cantidad = Sesion.getTicketReservacion().getLineas().getLinea(lineaSelecionada).getCantidad();
                p_editar_cantidad.setCantidad(cantidad);

                v_editar_cantidad.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
                v_editar_cantidad.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
                p_editar_cantidad.iniciaFoco();
                v_editar_cantidad.setVisible(true);

                int nuevaCantidad = p_editar_cantidad.getCantidad();
                if(nuevaCantidad > 0) {
                    // intentamos establecer la cantidad
                    Sesion.getTicketReservacion().getLineas().getLinea(lineaSelecionada).setCantidad(nuevaCantidad);
                    Sesion.getTicketReservacion().getLineas().getLinea(lineaSelecionada).recalcularImportes();
                    Sesion.getTicketReservacion().recalcularTotales();
                    refrescaTablaTicket();
                } else {
                    ventana_padre.crearAdvertencia("Debe insertar un valor positivo.");
                }
            }
            else {
                ventana_padre.crearAdvertencia("Debe añadir una línea de ticket para poder editarla.");
            }
        }
        catch (Exception ex) { //SinPermisos
            //
        }
    }
    
    private void editarRecuperar(){
        try {
            // ponemos la acción de eliminar ultimo elemento añadido a la lista                    
            lineaSelecionada = t_factura.getSelectedRow();
            if (lineaSelecionada == -1) {
                lineaSelecionada = t_factura.getRowCount() - 1;
            }
            this.indexLineaAEditar = lineaSelecionada;
            if (lineaSelecionada >= 0) {
                LineaTicket lineaTicketSeleccionada = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
                t_cantidad_valor.setText(lineaTicketSeleccionada.getCantidad().toString());
                if (lineaTicketSeleccionada.isLineaFacturacion()) {
                    t_cantidad_valor.setEditable(false);
                } else {
                    t_cantidad_valor.setEditable(true);
                }
                /**
                 * RD Valor Descuento
                 */
                t_precio_valor.setText(lineaTicketSeleccionada.getPrecioTotalTarifaOrigen().toString());
                t_descuento_valor.setEditable(true);

                iniciaFormularioDatosAdicionales(Sesion.getTicket().getLineas().getLinea(lineaSelecionada).getDatosAdicionales());
                /**
                 * Se agrega campo para que se muestre el descuento actual y no
                 * 0 RD
                 */
                t_descuento_valor.setText("" + lineaTicketSeleccionada.getDescuento().setScale(2, RoundingMode.HALF_UP));
                CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
                c2.show(p_descuento_principal, "descuento_valor");

                v_descuento_linea.setSize(400, 350);
                p_descuento_principal.setPreferredSize(new Dimension(400, 350));
                P_descuento_valor.setPreferredSize(new Dimension(400, 350));

                v_descuento_linea.setLocationRelativeTo(null);
                t_precio_valor.requestFocus();
                JPrincipal.setPopupActivo(v_descuento_linea);
                v_descuento_linea.setVisible(true);
                JPrincipal.setPopupActivo(null);

                ActionMap am = v_descuento_linea.getRootPane().getActionMap();
                InputMap im = v_descuento_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

                KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
                Action listeneresc = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        jb_cancel1ActionPerformed(ae);
                    }
                };

                im.put(esc, "IdentClientesc");
                am.put("IdentClientesc", listeneresc);
                lb_error.setText("");
                t_precio_valor.requestFocus();
            } else {
                ventana_padre.crearAdvertencia("Debe añadir una línea de ticket para poder editarla.");
            }
        } catch (Exception ex) { //SinPermisos
            //
        }
    }
    
    private void iniciaFormularioDatosAdicionales(DatosAdicionalesLineaTicket datosAdicionales) {
        log.info("Iniciar Formulario de datos adicionales");
        o_pendiente_entrega.setText("Pendiente Retiro Local");
        o_env_adomicilio.setText("Entregas a Domicilio");
        if (datosAdicionales == null) {
            t_descuento_valor.setText("0");
            o_env_adomicilio.setSelected(false);
            o_pendiente_entrega.setSelected(false);
        } else {
            BigDecimal descuento = datosAdicionales.getDescuento();
            if (Numero.isMenorACero(descuento)) {
                t_descuento_valor.setText("0");
            } else {
                t_descuento_valor.setText(String.valueOf(descuento));
            }
            o_env_adomicilio.isSelected();
            o_pendiente_entrega.isSelected();
            o_env_adomicilio.setSelected(datosAdicionales.isEnvioDomicilio());
            o_pendiente_entrega.setSelected(datosAdicionales.isRecogidaPosterior());
        }

    }
    
    public void guardar(String observacion, String autorizador, String referencia, LineaTicket lineaTicketAnterior, BigDecimal precio_valor, BigDecimal precio_valor_anterior, BigDecimal valorDescuento, BigDecimal valorDescuentoAnterior, Integer cantidad_valor) {
        try {
            Long dif = 0l;
            LogOperaciones logImpresiones = new LogOperaciones();
            logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logImpresiones.setFechaHora(new Date());
            logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
            logImpresiones.setReferencia(referencia);
            logImpresiones.setProcesado('N');
            logImpresiones.setAutorizador(autorizador);
            logImpresiones.setCodOperacion("MODIFICAR DESCUENTO Y O PRECIO");
            logImpresiones.setObservaciones(observacion);
            insertarLog(logImpresiones);
            LogOperacionesDet logImpresionesDet = new LogOperacionesDet();
            logImpresionesDet = new LogOperacionesDet();
            logImpresionesDet.setUidLog(logImpresiones.getLogOperacionesPK().getUid());
            logImpresionesDet.setUidTicket(Sesion.getTicket().getUid_ticket());
            logImpresionesDet.setAutorizado(Sesion.getUsuario().getUsuario());
            logImpresionesDet.setObservaciones(observacion);
            
            if (precio_valor_anterior.compareTo(precio_valor) != 0) {
            if (observacion == "DESCUENTO GLOBAL") {
                logImpresionesDet.setCodart(null);
                logImpresionesDet.setCantidad(0);
                logImpresionesDet.setMotivoDescuento(observacion);
                logImpresionesDet.setPrecioReal(precio_valor);
                logImpresionesDet.setPrecioTotalOrigen(precio_valor_anterior);
                dif = 1l;
            } else {
                logImpresionesDet.setCodart(lineaTicketAnterior.getArticulo().getCodart());
                logImpresionesDet.setCantidad(cantidad_valor);
                logImpresionesDet.setMotivoDescuento("MODIFICAR DESCUENTO Y O PRECIO");
                logImpresionesDet.setPrecioReal(precio_valor_anterior);
                logImpresionesDet.setPrecioTotalOrigen(precio_valor);
                dif = 1l;
            }
            }
            if (valorDescuentoAnterior.compareTo(valorDescuento) != 0) {
                logImpresionesDet.setDescuentoOriginal(valorDescuentoAnterior);
                logImpresionesDet.setDescuentoModificado(valorDescuento);
                dif = 2l;
            }
            if (dif != 0) {
                insertarLogDet(logImpresionesDet);
            }
        } catch (LogException ex) {
            java.util.logging.Logger.getLogger(JVentas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertarLog(LogOperaciones logImpresiones) throws LogException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ServicioLogAcceso.crearAccesoLog(logImpresiones, em);
        em.getTransaction().commit();
    }

    public void insertarLogDet(LogOperacionesDet logImpresionesDet) throws LogException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ServicioLogAcceso.crearAccesoLogDet(logImpresionesDet, em);
        em.getTransaction().commit();
    }
    
    private void liquidarReserva(List<ReservaArticuloBean> lista) throws ReservasException, TicketPrinterException {
        BigDecimal restanteAbonos = Sesion.getReservacion().getAbonosRestantesReales();
        BigDecimal abonosRestantesReales = Sesion.getReservacion().getAbonosRestantesReales();
        BigDecimal totalPagado = BigDecimal.ZERO;
        // Lista para actualizar el stock a comprados tras liquidación 
        List<ReservaArticuloBean> listaArticulosComprados = new ArrayList<ReservaArticuloBean>();

        for (ReservaArticuloBean art : lista) {
            if (!art.getComprado()) {
                log.debug("aticulo no comprado en reserva: " + art.getCodart());
                try {
                    art.setComprado(true);
                    art.setCompradoConAbono(true);
                    ReservacionesServicios.actualizarArticulo(art);
                    restanteAbonos = restanteAbonos.subtract(art.getPrecioTotal());
                    abonosRestantesReales = abonosRestantesReales.subtract(art.getPrecioTotal());
                    totalPagado = totalPagado.add(art.getPrecioTotal());

                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + Sesion.getReservacion().getPorAbonar());

                    // Aumentamos el stock de artículos vendidos y disminuimos el de reservados
                    listaArticulosComprados.add(art);

                }
                catch (ReservasException ex) {
                    log.error("Error al modificar la reserva en la liquidación");
                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + Sesion.getReservacion().getPorAbonar());
                    ventana_padre.crearError(ex.getMessage());
                }
            }
        }
        Sesion.getReservacion().setAbonosRestantes(restanteAbonos);
        Sesion.getReservacion().setAbonosRestantesReales(abonosRestantesReales);
        Sesion.getReservacion().getReservacion().setLiquidado(true);
        Sesion.getReservacion().getReservacion().setFechaLiquidacion(new Fecha());
        TicketS ticket = ReservacionesServicios.liquidarReserva(Sesion.getReservacion(), listaArticulosComprados);
        
        log.debug("Finalizada liquidacion de reserva");
    }
}
