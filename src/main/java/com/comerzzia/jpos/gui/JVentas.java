/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JVentas.java
 *
 * Created on 28-jun-2011, 16:35:37
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.codigosBarra.NotAValidBarCodeException;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
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
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.garantia.JGarantia;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorDecimal;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.persistencia.kit.KitBean;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.promociones.PromocionSocioBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrack;
import com.comerzzia.jpos.servicios.articulos.ArticuloException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueoFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifaException;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendida;
import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import com.comerzzia.jpos.servicios.interfazventa.OpcionesVentaManager;
import com.comerzzia.jpos.servicios.kit.KitException;
import com.comerzzia.jpos.servicios.kit.KitReferencia;
import com.comerzzia.jpos.servicios.kit.ServicioKit;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponNotValidException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.listapda.ListaPDAException;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPuntosBean;
import com.comerzzia.jpos.util.enums.EnumCodigoItemBase;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.imagenes.Imagenes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.awt.event.KeyAdapter;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.apache.commons.lang.StringUtils;

/**
 * Pantalla de Ventas
 *
 * @author MGRI
 */
public class JVentas extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError, IBusquedasArticulos, IVenta {
    //opciones de configuracion

    /* ESTADOS !                                    */
 /* Para las acciones                            */
 /* VENTA : Estamos en venta                     */
 /* Activado en iniciaVista                      */
 /* PAGOS : Estamos en pagos                     */
 /* Activado en metodo accionIrAPagos()          */
    private static byte VENTA = 1;
    private static byte PAGOS = 2;
    private byte estado;
    private static final Logger log = Logger.getMLogger(JVentas.class);
    boolean esNumeroArticulosEitable = true;  // se consulta de la sesion en la creación del panel
    // servicios y variables
    PrintServices ts = PrintServices.getInstance();
    JPrincipal ventana_padre = null;
    ArticulosServices articulosServices = ArticulosServices.getInstance();
    TarifasServices tarifasServices = new TarifasServices();
    // definimos los paneles
    protected JReservacionesPagosV p_pagos = null;
    private PromocionSocioBean promoSocio = null;
    JPanel p_activo = null;
    //Seleccion de columna de la factura
    int lineaSelecionada;
    //Descuentos
    String codigobarra;
    String codigoArticulo;
    int indexLineaAEditar = -1;
    public boolean esArticuloFacturacion = false;
    public int cantidadFacturacion = 0;
    private Long aux = 0L;
    private Long auxGarantia = 0L;

    /**
     * Creates new form JVentas
     */
    public JVentas() {
        super();
        initComponents();
        p_pagos_contenedor.setVisible(false);

    }

    /**
     * Constructor
     *
     * @param ventana_padre
     */
    public JVentas(JPrincipal ventana_padre) {
        super();
        this.ventana_padre = ventana_padre;
        initComponents();

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        Imagenes.cambiarImagenPublicidad(jLabel30);
        URL myurl;
        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        v_datos_envio.setIconImage(icon.getImage());
        v_buscar_articulo.setIconImage(icon.getImage());
        v_descuento_linea.setIconImage(icon.getImage());
        v_seleccion_linea.setIconImage(icon.getImage());
        v_venta_entre_locales.setIconImage(icon.getImage());
        f_pagos2.setIconImage(icon.getImage());
        p_fondo_imagen.setImagenFondo(prefijo, "ventas.png");
        // Inicialización de parámetros especificos de componentes de pantalla
        p_pagos_contenedor.setVisible(false);
        t_factura.setTableHeader(null);
        t_introduccionArticulos.getT_Codigo().addKeyListener(this);
        sp_t_factura.getViewport().setOpaque(false);
        sp_t_factura.setBorder(null);
        p_buscar_articulo.setVentana_padre(this);
        p_buscar_articulo.setContenedor(v_buscar_articulo);

        //como quitar el borde al scrollpanel
        Border empty = new EmptyBorder(0, 0, 0, 0);
        sp_t_factura.setBorder(empty);
        sp_t_factura.setViewportBorder(empty);

        // Inicialización de ventana de Datos de envio
        p_datos_envio.setVentana_padre(ventana_padre);
        p_datos_envio.setContenedor(v_datos_envio);
        v_datos_envio.setLocationRelativeTo(null);

        // Inicialización de la ventana de Venta en otras tiendas
        p_venta_entre_locales.setVentana_padre(ventana_padre);
        p_venta_entre_locales.setContenedor(v_venta_entre_locales);

        // validación del cuadro de texto de descuentos
        t_descuento_valor.addValidador(new ValidadorDecimal(new BigDecimal("99.99"), BigDecimal.ZERO, 4), this);
        t_cantidad_valor.addValidador(new ValidadorEntero(1, Integer.MAX_VALUE, 1), this);
        t_precio_valor.addValidador(new ValidadorDecimal(null, new BigDecimal("0.0001"), 4), this);
        t_incremento_valor.addValidador(new ValidadorDecimal(new BigDecimal("150"), BigDecimal.ZERO, 4), this);
        registraEventoBuscar();
        crearAccionFocoTabla(this, t_factura, KeyEvent.VK_T, InputEvent.CTRL_MASK);
    }

    /**
     * Función de Inicialización al cambiar de vista
     *
     */
    @Override
    public void iniciaVista() {
        JPrincipal.setPanelActivo(this);
        liberarMemoria();
        estado = JVentas.VENTA;
        addFunctionKeys();
        ActionMap am = v_seleccion_linea.getRootPane().getActionMap();
        InputMap im = v_seleccion_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent ae) {
                cancelarOperacion();
            }
        };

        lineasTicket.setLineas(Sesion.getTicket().getLineas().getLineas());

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);
        // mostramos el cliente seleccionado
        t_venta_cliente.setText(Sesion.getTicket().getCliente().getNombreVenta());

        // Reseteamos la tarjeta del cliente.
        lb_cliente_tipo_socio.setText("");
        lb_tarjeta.setIcon(null);

        // Iniciamos la tabla de tickets
        // Data model y renderer de la tabla
        t_factura.setModel(new TicketTableModel(Sesion.getTicket().getLineas()));
        t_factura.setDefaultRenderer(Object.class, new TicketTableCellRenderer());

        // Establecemos el modo en la introduccción de Artículo.  
        t_introduccionArticulos.setModoCantidad(TicketTableCellRenderer.isModoConCantidad());
        jTextArea1.setText(StringUtils.EMPTY);
        // Refrescamos
        refrescaTablaTicket();

        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();
        t_introduccionArticulos.reset();
        l_factura_cabecera_id.setText(Sesion.getTicket().getIdFactura());

        jLabel_puntos.setVisible(false);
        //Comprobamos si el cliente es socioOrigen para mostrar el mensaje
        if (!Sesion.getTicket().getCliente().isSocioOrigen()) {
            jLabel_puntos.setVisible(true);
            jLabel_puntos.setText(Variables.getVariable(Variables.MENSAJE_NO_SOCIOS));
        }

        // comprobamos si el cliente puede subir su nivel de afiliación con esta compra
        jLabel_compra_socio.setVisible(false);
        /* Solicitado por Miguel Gomez el 01/10/2018
        if (Sesion.getTicket().getCliente().isSocio()) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(Sesion.getTicket().getCliente().getTipoAfiliado());
            if (tipoAfiliado != null && tipoAfiliado.getSiguienteNivel() != null && tipoAfiliado.isCompraSiguienteNivelDefinido()) {
                jLabel_compra_socio.setVisible(true);
                jLabel_compra_socio.setText("Con una compra de $ " + tipoAfiliado.getCompraSiguienteNivel() + " será socia " + tipoAfiliado.getSiguienteNivel().getDesTipoAfiliado());
            }
        }*/
        b_datos_facturacion.setEnabled(true);

        // Validamos que el cliente tenga tarjeta Supermaxi
        if (!Sesion.getTicket().getCliente().isSeleccionadoTarjetaAfiliado()) {
            JPrincipal.getInstance().crearVentanaLecturaTarjetaSupermMaxi(Sesion.getTicket().getCliente());
        }
        if (Sesion.getTicket().getCliente().isSocio() && Sesion.getTicket().getCliente().getTipoAfiliado() != null) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(Sesion.getTicket().getCliente().getTipoAfiliado());
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

        if (Sesion.isBebemundo()) {
            b_ventas_appal.setEnabled(false);
        }
        log.debug("Pantalla de venta inicializada.");
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
        P_descuento_valor = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);
        t_descuento_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel4 = new javax.swing.JLabel();
        o_pendiente_entrega = new javax.swing.JCheckBox();
        o_env_adomicilio = new javax.swing.JCheckBox();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error_descuento = new javax.swing.JLabel();
        lb_error_ml = new com.comerzzia.jpos.gui.components.JMultilineLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        t_cantidad_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel8 = new javax.swing.JLabel();
        t_precio_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel10 = new javax.swing.JLabel();
        jTextArea1 = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        o_pedido_facturado = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        t_incremento_valor = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel12 = new javax.swing.JLabel();
        t_valor_calculado = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        o_intercambio = new javax.swing.JCheckBox();
        v_datos_envio = new javax.swing.JDialog();
        p_datos_envio = new com.comerzzia.jpos.gui.JDatosEnvio();
        v_venta_entre_locales = new javax.swing.JDialog();
        p_venta_entre_locales = new com.comerzzia.jpos.gui.JVentaEntreLocales();
        jPanel1 = new javax.swing.JPanel();
        p_pagos_contenedor = new com.comerzzia.jpos.gui.components.JPanelTransparente();
        f_pagos = new javax.swing.JPanel();
        p_principal = new javax.swing.JPanel();
        m_ventas = new javax.swing.JPanel();
        b_ventas_datoscliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_devolucion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_datos_facturacion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_edicion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_pagos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_appal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_otro_local = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_seleccion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_descuentos = new javax.swing.JPanel();
        jLabel_compra_socio = new javax.swing.JLabel();
        jLabel_puntos = new javax.swing.JLabel();
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
        lb_tienda_destino = new javax.swing.JLabel();
        lb_tienda_origen = new javax.swing.JLabel();
        p_idcliente = new javax.swing.JPanel();
        t_venta_cliente = new javax.swing.JLabel();
        lb_tarjeta = new javax.swing.JLabel();
        lb_cliente_tipo_socio = new javax.swing.JLabel();
        p_publicidad = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        p_fondo_imagen = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);

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
        v_seleccion_linea.setMinimumSize(new java.awt.Dimension(400, 200));
        v_seleccion_linea.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_seleccion_linea.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_seleccion_linea.setResizable(false);

        p_descuento_principal.setMinimumSize(new java.awt.Dimension(400, 200));
        p_descuento_principal.setLayout(new java.awt.CardLayout());

        p_seleccion_cb.setMaximumSize(new java.awt.Dimension(450, 200));
        p_seleccion_cb.setMinimumSize(new java.awt.Dimension(450, 200));
        p_seleccion_cb.setName("seleccion_cb"); // NOI18N
        p_seleccion_cb.setOpaque(false);
        p_seleccion_cb.setPreferredSize(new java.awt.Dimension(450, 200));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
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
        v_buscar_articulo.setIconImages(null);
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
        v_descuento_linea.setMinimumSize(new java.awt.Dimension(360, 320));
        v_descuento_linea.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_descuento_linea.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        P_descuento_valor.setMaximumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setMinimumSize(new java.awt.Dimension(357, 200));
        P_descuento_valor.setName("cantidad_valor"); // NOI18N
        P_descuento_valor.setPreferredSize(new java.awt.Dimension(357, 200));

        t_descuento_valor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_descuento_valorFocusLost(evt);
            }
        });
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

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Precio Calculado");

        o_pendiente_entrega.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.editar.recogidaposterior").charAt(0));
        o_pendiente_entrega.setText("Pendiente Retiro Local");
        o_pendiente_entrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_pendiente_entregaActionPerformed(evt);
            }
        });
        o_pendiente_entrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                o_pendiente_entregaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                o_pendiente_entregaKeyTyped(evt);
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
        lb_error_ml.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_error_ml.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lb_error_ml.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jLabel2.setText("%");

        jLabel6.setDisplayedMnemonic('t');
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setLabelFor(t_cantidad_valor);
        jLabel6.setText("Cantidad:");

        t_cantidad_valor.setNextFocusableComponent(t_descuento_valor);

        jLabel8.setDisplayedMnemonic('P');
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setLabelFor(t_precio_valor);
        jLabel8.setText("Precio:");

        t_precio_valor.setNextFocusableComponent(t_cantidad_valor);
        t_precio_valor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_precio_valorFocusLost(evt);
            }
        });

        jLabel10.setText("Motivo:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setNextFocusableComponent(jb_ok1);

        o_pedido_facturado.setText("Pedido Facturado");
        o_pedido_facturado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_pedido_facturadoActionPerformed(evt);
            }
        });
        o_pedido_facturado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                o_pedido_facturadoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setLabelFor(t_incremento_valor);
        jLabel11.setText("Incremento:");

        t_incremento_valor.setText("0.00");
        t_incremento_valor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_incremento_valorFocusLost(evt);
            }
        });
        t_incremento_valor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_incremento_valorActionPerformed(evt);
            }
        });
        t_incremento_valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_incremento_valorKeyPressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jLabel12.setText("%");

        t_valor_calculado.setFont(new java.awt.Font("Comic Sans MS", 0, 20)); // NOI18N
        t_valor_calculado.setForeground(new java.awt.Color(255, 0, 0));
        t_valor_calculado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        t_valor_calculado.setText("0.00");
        t_valor_calculado.setToolTipText("");
        t_valor_calculado.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setLabelFor(t_descuento_valor);
        jLabel13.setText("Descuento:");

        o_intercambio.setText("Intercambio");
        o_intercambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_intercambioActionPerformed(evt);
            }
        });

        P_descuento_valor.registraEventoEnterBoton();

        javax.swing.GroupLayout P_descuento_valorLayout = new javax.swing.GroupLayout(P_descuento_valor);
        P_descuento_valor.setLayout(P_descuento_valorLayout);
        P_descuento_valorLayout.setHorizontalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_descuento_valorLayout.createSequentialGroup()
                .addComponent(lb_error_descuento, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addGap(389, 389, 389))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_descuento_valorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(o_env_adomicilio)
                                    .addComponent(o_pendiente_entrega))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(o_pedido_facturado)
                                    .addComponent(o_intercambio)))
                            .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_error_ml, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_incremento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel8))
                                        .addGap(14, 14, 14))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_descuento_valorLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(t_cantidad_valor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(t_precio_valor, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                    .addComponent(t_descuento_valor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(5, 5, 5)
                                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13)
                                        .addComponent(t_valor_calculado, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4))))))
                .addGap(30, 30, 30))
        );
        P_descuento_valorLayout.setVerticalGroup(
            P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_descuento_valorLayout.createSequentialGroup()
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(t_precio_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(t_cantidad_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel13)))
                    .addGroup(P_descuento_valorLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_valor_calculado)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(t_incremento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(3, 3, 3)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(o_pendiente_entrega)
                    .addComponent(o_pedido_facturado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(o_env_adomicilio)
                    .addComponent(o_intercambio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(lb_error_ml, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(P_descuento_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout v_descuento_lineaLayout = new javax.swing.GroupLayout(v_descuento_linea.getContentPane());
        v_descuento_linea.getContentPane().setLayout(v_descuento_lineaLayout);
        v_descuento_lineaLayout.setHorizontalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_descuento_lineaLayout.createSequentialGroup()
                .addComponent(P_descuento_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 37, Short.MAX_VALUE))
        );
        v_descuento_lineaLayout.setVerticalGroup(
            v_descuento_lineaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(P_descuento_valor, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
        );

        v_datos_envio.setAlwaysOnTop(true);
        v_datos_envio.setMinimumSize(new java.awt.Dimension(800, 700));
        v_datos_envio.setModal(true);

        p_datos_envio.setPreferredSize(new java.awt.Dimension(700, 700));

        javax.swing.GroupLayout v_datos_envioLayout = new javax.swing.GroupLayout(v_datos_envio.getContentPane());
        v_datos_envio.getContentPane().setLayout(v_datos_envioLayout);
        v_datos_envioLayout.setHorizontalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_datos_envioLayout.createSequentialGroup()
                .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_datos_envioLayout.setVerticalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        v_venta_entre_locales.setAlwaysOnTop(true);
        v_venta_entre_locales.setMinimumSize(new java.awt.Dimension(464, 400));
        v_venta_entre_locales.setModal(true);

        javax.swing.GroupLayout v_venta_entre_localesLayout = new javax.swing.GroupLayout(v_venta_entre_locales.getContentPane());
        v_venta_entre_locales.getContentPane().setLayout(v_venta_entre_localesLayout);
        v_venta_entre_localesLayout.setHorizontalGroup(
            v_venta_entre_localesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_venta_entre_localesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_venta_entre_locales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_venta_entre_localesLayout.setVerticalGroup(
            v_venta_entre_localesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_venta_entre_localesLayout.createSequentialGroup()
                .addComponent(p_venta_entre_locales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(0, 51, 153));
        setMaximumSize(new java.awt.Dimension(1094, 734));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p_pagos_contenedor.setBackground(new java.awt.Color(0, 0, 0));
        p_pagos_contenedor.setMaximumSize(new java.awt.Dimension(1094, 734));
        p_pagos_contenedor.setMinimumSize(new java.awt.Dimension(1094, 734));
        p_pagos_contenedor.setPreferredSize(new java.awt.Dimension(1094, 734));
        p_pagos_contenedor.setTran(0.3F);
        p_pagos_contenedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        f_pagos.setMaximumSize(new java.awt.Dimension(983, 591));
        f_pagos.setMinimumSize(new java.awt.Dimension(983, 591));
        f_pagos.setOpaque(false);
        f_pagos.setPreferredSize(new java.awt.Dimension(983, 591));

        javax.swing.GroupLayout f_pagosLayout = new javax.swing.GroupLayout(f_pagos);
        f_pagos.setLayout(f_pagosLayout);
        f_pagosLayout.setHorizontalGroup(
            f_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 983, Short.MAX_VALUE)
        );
        f_pagosLayout.setVerticalGroup(
            f_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );

        p_pagos_contenedor.add(f_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jPanel1.add(p_pagos_contenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        p_principal.setFocusable(false);
        p_principal.setMaximumSize(new java.awt.Dimension(1094, 734));
        p_principal.setMinimumSize(new java.awt.Dimension(1094, 734));
        p_principal.setOpaque(false);
        p_principal.setPreferredSize(new java.awt.Dimension(1094, 734));
        p_principal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_ventas.setBackground(new java.awt.Color(255, 255, 255));
        m_ventas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_ventas_datoscliente.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.datos.cliente").charAt(0));
        b_ventas_datoscliente.setAlignmentY(0.0F);
        b_ventas_datoscliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_ventas_datoscliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        b_ventas_datoscliente.setLabel("<html><center>Cancelar <br> Venta <br/>F2</center></html>");
        b_ventas_datoscliente.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_datoscliente.setPreferredSize(new java.awt.Dimension(90, 37));
        b_ventas_datoscliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_datosclienteActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_datoscliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 56));

        b_ventas_devolucion.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.devolucion").charAt(0));
        b_ventas_devolucion.setText("<html><center>Aplazar venta<br/>F3</center></html>");
        b_ventas_devolucion.setActionCommand("Identificar Cliente F3");
        b_ventas_devolucion.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_devolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_devolucionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_devolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 80, 57));

        b_datos_facturacion.setMnemonic('l');
        b_datos_facturacion.setText("<html><center>Datos de<br>Factura<br/>F5</html>");
        b_datos_facturacion.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_datos_facturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_datos_facturacionActionPerformed(evt);
            }
        });
        m_ventas.add(b_datos_facturacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 80, 57));

        b_ventas_edicion.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.edicion").charAt(0));
        b_ventas_edicion.setText("<html><center> Edición <br/>F4</center></html>");
        b_ventas_edicion.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_edicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_edicionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_edicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 80, 56));

        b_ventas_pagos.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.pagos").charAt(0));
        b_ventas_pagos.setText("<html><center>Pagos<br/>F9</center></html>");
        b_ventas_pagos.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_pagosActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 80, 55));

        b_ventas_appal.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.menu.principal").charAt(0));
        b_ventas_appal.setText("<html><center>Asociar Factura <br/>F8</center></html>");
        b_ventas_appal.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_appal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_appalActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_appal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 80, 57));

        b_ventas_otro_local.setText("<html><center>Venta otro Local<br>F6</center></html>");
        b_ventas_otro_local.setActionCommand("<html><center>Pago desde otro Local<br>F6</center></html>");
        b_ventas_otro_local.setAlignmentY(0.1F);
        b_ventas_otro_local.setFont(b_ventas_otro_local.getFont());
        b_ventas_otro_local.setMargin(new java.awt.Insets(-1, -6, -1, -6));
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
        m_ventas.add(b_ventas_seleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 80, 57));

        p_principal.add(m_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 320, -1));

        p_descuentos.setOpaque(false);
        p_descuentos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_compra_socio.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel_compra_socio.setForeground(new java.awt.Color(51, 153, 255));
        jLabel_compra_socio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_compra_socio.setText("$ 0");
        jLabel_compra_socio.setFocusable(false);
        p_descuentos.add(jLabel_compra_socio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 360, -1));

        jLabel_puntos.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel_puntos.setForeground(new java.awt.Color(51, 153, 255));
        jLabel_puntos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_puntos.setText("$ 0");
        jLabel_puntos.setFocusable(false);
        p_descuentos.add(jLabel_puntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 360, -1));

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
        jLabel_numArticulos.setLabelFor(t_introduccionArticulos.getT_Codigo());
        jLabel_numArticulos.setText("0 artículos ingresados");
        jLabel_numArticulos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout p_ingreso_articulosLayout = new javax.swing.GroupLayout(p_ingreso_articulos);
        p_ingreso_articulos.setLayout(p_ingreso_articulosLayout);
        p_ingreso_articulosLayout.setHorizontalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel_numArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        p_ingreso_articulosLayout.setVerticalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_numArticulos)
                .addContainerGap())
        );

        p_principal.add(p_ingreso_articulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 600, 280, 90));

        p_fatura.setOpaque(false);
        p_fatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_factura_cabecera_id.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.tabla.facturas").charAt(0));
        l_factura_cabecera_id.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        l_factura_cabecera_id.setForeground(new java.awt.Color(51, 153, 255));
        l_factura_cabecera_id.setLabelFor(t_factura);
        l_factura_cabecera_id.setText("ID");
        l_factura_cabecera_id.setFocusable(false);
        p_fatura.add(l_factura_cabecera_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 190, 20));

        l_total.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        l_total.setText("Total:");
        l_total.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        l_total.setFocusable(false);
        p_fatura.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 620, -1, -1));

        sp_t_factura.setBackground(new java.awt.Color(153, 255, 255));

        t_factura.setFont(t_factura.getFont());
        t_factura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        t_factura.setFocusCycleRoot(true);
        t_factura.setGridColor(new java.awt.Color(153, 204, 255));
        t_factura.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_factura.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                t_facturaComponentResized(evt);
            }
        });
        sp_t_factura.setViewportView(t_factura);

        p_fatura.add(sp_t_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 360, 540));

        l_v_total.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        l_v_total.setForeground(new java.awt.Color(51, 153, 255));
        l_v_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_v_total.setText("0");
        l_v_total.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        l_v_total.setFocusable(false);
        p_fatura.add(l_v_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 620, 160, 30));

        l_factura_cabecera1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        l_factura_cabecera1.setLabelFor(t_factura);
        l_factura_cabecera1.setText("Factura:");
        l_factura_cabecera1.setFocusable(false);
        p_fatura.add(l_factura_cabecera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 12, 70, -1));

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        URL myurl = prefijo.getClass().getResource("/skin/" + prefijo + "/total.png");
        ImageIcon icon = new ImageIcon(myurl);
        jLabel5.setIcon(icon);
        p_fatura.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 600, 260, 80));

        lb_tienda_destino.setFont(new java.awt.Font("Comic Sans MS", 0, 9)); // NOI18N
        lb_tienda_destino.setForeground(new java.awt.Color(255, 51, 51));
        lb_tienda_destino.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        p_fatura.add(lb_tienda_destino, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 170, 20));

        lb_tienda_origen.setFont(new java.awt.Font("Comic Sans MS", 0, 9)); // NOI18N
        lb_tienda_origen.setForeground(new java.awt.Color(255, 51, 51));
        lb_tienda_origen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        p_fatura.add(lb_tienda_origen, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 30, 180, 20));

        p_principal.add(p_fatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(633, 21, 370, 710));

        p_idcliente.setOpaque(false);
        p_idcliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t_venta_cliente.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        t_venta_cliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_venta_cliente.setText("NOMBRE_DE_CLIENTE");
        t_venta_cliente.setFocusable(false);
        p_idcliente.add(t_venta_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, 418, -1));
        p_idcliente.add(lb_tarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 25, 59, 39));

        lb_cliente_tipo_socio.setFont(lb_cliente_tipo_socio.getFont().deriveFont(lb_cliente_tipo_socio.getFont().getStyle() | java.awt.Font.BOLD, 14));
        lb_cliente_tipo_socio.setForeground(new java.awt.Color(51, 153, 255));
        lb_cliente_tipo_socio.setText("tipo de Socio");
        p_idcliente.add(lb_cliente_tipo_socio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 35, 220, -1));

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

        p_fondo_imagen.setMaximumSize(new java.awt.Dimension(1094, 734));
        p_fondo_imagen.setMinimumSize(new java.awt.Dimension(1094, 734));

        javax.swing.GroupLayout p_fondo_imagenLayout = new javax.swing.GroupLayout(p_fondo_imagen);
        p_fondo_imagen.setLayout(p_fondo_imagenLayout);
        p_fondo_imagenLayout.setHorizontalGroup(
            p_fondo_imagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1094, Short.MAX_VALUE)
        );
        p_fondo_imagenLayout.setVerticalGroup(
            p_fondo_imagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 734, Short.MAX_VALUE)
        );

        add(p_fondo_imagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void b_ventas_datosclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_datosclienteActionPerformed
        if (ventana_padre.crearVentanaConfirmacion("Esta acción cancelará la venta actual, ¿Desea continuar?")) {
            try {
                String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_VENTA);
                ServicioLogAcceso.crearAccesoLogCancelarVenta(codAdministrador, Sesion.getTicket().getIdFactura());
                p_datos_envio.limpiarFormularioInicial();
                removeFunctionKeys();
                ventana_padre.showView("ident-cliente");
                auxGarantia = 0L;
                Sesion.borrarTicket();
            } catch (SinPermisosException ex) {
                log.debug("No se tiene permisos para borrar línea de tickets.");
            }
        }
}//GEN-LAST:event_b_ventas_datosclienteActionPerformed

    private void b_ventas_devolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_devolucionActionPerformed
        log.debug("Aplazando venta...");
        try {
            String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.APLAZAR_VENTA);
            ServicioLogAcceso.crearAccesoLogAplazarVenta(codAdministrador, Sesion.getTicket().getIdFactura());
            log.debug("Salvamos en memoria la venta actual...");
            Sesion.aparcarTicket();
            log.debug("Venta aplazada correctamente.");
            ventana_padre.crearInformacion("Venta aplazada correctamente.");
            ventana_padre.showView("ident-cliente");
        } catch (SinPermisosException ex) {
            log.debug("No se tiene permisos para aplazar la venta.");
        }
}//GEN-LAST:event_b_ventas_devolucionActionPerformed

    private void b_datos_facturacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_datos_facturacionActionPerformed
        FacturacionTicketBean ft = null;
        ft = JPrincipal.crearVentanaFacturacion(Sesion.getTicket().getFacturacion() == null, Sesion.getTicket());
        if (ft != null) {
            ventana_padre.showView("ventas");
        } else {
            log.debug("Se canceló la edición de datos de facturación");
        }
}//GEN-LAST:event_b_datos_facturacionActionPerformed

    private void b_ventas_edicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_edicionActionPerformed
        log.info("ACCION EDITAR LÍNEA");
        try {
            //jTextArea1.setText(StringUtils.EMPTY);
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
                //Comentado if else  para que se peuda modificar el valor ddescuento RD
//                if (Sesion.getTicket().getLineas().getLinea(lineaSelecionada).isPromoUnitariaAplicada()) {
//                t_descuento_valor.setEditable(false);
//                }
//                else {
                t_descuento_valor.setEditable(true);

//                }
                iniciaFormularioDatosAdicionales(Sesion.getTicket().getLineas().getLinea(lineaSelecionada).getDatosAdicionales());
                /**
                 * Se agrega campo para que se muestre el descuento actual y no
                 * 0 RD
                 */
//                t_descuento_valor.setText(""+lineaTicketSeleccionada.getDescuento());
                t_descuento_valor.setText("" + lineaTicketSeleccionada.getDescuento().setScale(4, RoundingMode.HALF_UP));
                CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
                c2.show(p_descuento_principal, "descuento_valor");

                v_descuento_linea.setSize(450, 350);
                p_descuento_principal.setPreferredSize(new Dimension(400, 350));
                P_descuento_valor.setPreferredSize(new Dimension(400, 350));

                v_descuento_linea.setLocationRelativeTo(null);
//                if (!lineaTicketSeleccionada.getArticulo().getCodseccion().equals(Constantes.SECCION_OBSEQUIOS)) {
//                    t_cantidad_valor.requestFocus();
//                }
                t_cantidad_valor.requestFocus();
                JPrincipal.setPopupActivo(v_descuento_linea);
                v_descuento_linea.setVisible(true);
                JPrincipal.setPopupActivo(null);

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
                t_precio_valor.requestFocus();
            } else {
                ventana_padre.crearAdvertencia("Debe añadir una línea de ticket para poder editarla.");
            }
        } catch (Exception ex) { //SinPermisos
            //
            ex.printStackTrace();
        }
}//GEN-LAST:event_b_ventas_edicionActionPerformed

    private void b_ventas_pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_pagosActionPerformed
        try {
            //Refrescar estado pinpad

            if (VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_ESTADO_AUTOMATICO)) {
                PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);
                PinPadFasttrack.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);
            } else {
                PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_MANUAL);
                PinPadFasttrack.getInstance().setTipo(DatosConfiguracion.VALIDACION_MANUAL);
            }
            Boolean variableVentaManual = VariablesAlm.getVariableAsBooleanActual(VariablesAlm.FACTURA_VENTA_MANUAL);
            Sesion.getTicket().setVentaManual(Boolean.FALSE);
            if (variableVentaManual != null && variableVentaManual) {
                if (JPrincipal.getInstance().crearVentanaConfirmacion("\t\t¿Es una venta manual?", "Si", "No")) {
                    Sesion.getTicket().setVentaManual(Boolean.TRUE);
                    PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_MANUAL);
                    PinPadFasttrack.getInstance().setTipo(DatosConfiguracion.VALIDACION_MANUAL);
                }
            }
            if (Sesion.getTicket().getFacturacion() == null) {
                if (Sesion.getTicket().getCliente().getCodcli().equals(Sesion.getClienteGenerico().getCodcli())) {
                    Sesion.getTicket().setFacturacionClienteGenerico();
                } else {
                    Sesion.getTicket().setFacturacionCliente();
                }
            }
            accionAccederAPagos();
        } catch (Exception e) {
            ventana_padre.crearError("Se ha producido un error en el sistema al intentar abrir la pantalla de pagos. Avise al administrador del sistema, por favor, e inténtelo de nuevo.");
            habilita_elementos_formulario();
            addFuntionKeysBotonesMenu();
            log.error("Error en transición de venta a pagos: " + e.getMessage(), e);
        }
}//GEN-LAST:event_b_ventas_pagosActionPerformed

    private void b_ventas_appalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_appalActionPerformed
        if (Sesion.isSukasa()) {
            accionAsociarFactura();
        }
}//GEN-LAST:event_b_ventas_appalActionPerformed

private void b_ventasBuscarArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventasBuscarArticuloActionPerformed
    accionVentaEntreLocales();
}//GEN-LAST:event_b_ventasBuscarArticuloActionPerformed
    /**
     * EVENTO: selección de artículo
     *
     * @param evt
     */
private void b_ventas_seleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_seleccionActionPerformed

    try {
        log.info("ACCION SELECCIONAR ARTÍCULO");
        CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
        c2.show(p_descuento_principal, "seleccion_cb");

        ActionMap am = v_seleccion_linea.getRootPane().getActionMap();
        InputMap im = v_seleccion_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent ae) {
                cancelarOperacion();
            }
        };

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);
        v_seleccion_linea.setSize(400, 200);
        p_descuento_principal.setPreferredSize(new Dimension(420, 200));
        p_seleccion_cb.setPreferredSize(new Dimension(420, 200));

        v_seleccion_linea.setLocationRelativeTo(null);
        JPrincipal.setPopupActivo(v_seleccion_linea);
        v_seleccion_linea.setVisible(true);
        JPrincipal.setPopupActivo(null);

        t_seleccion_cb.setText("");
        t_seleccion_cb1.setText("");
        lb_error.setText("");
        t_seleccion_cb.requestFocus();

    } catch (Exception e) {
        log.error("No se pudo seleccionar la línea: " + e.getMessage(), e);
    }
}//GEN-LAST:event_b_ventas_seleccionActionPerformed

private void t_seleccion_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_seleccion_cbActionPerformed
}//GEN-LAST:event_t_seleccion_cbActionPerformed

private void t_seleccion_cbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cbKeyTyped
}//GEN-LAST:event_t_seleccion_cbKeyTyped

    /**
     * EVENTO: Selección de un elemento de la tabla de ventas a partir de su
     * codigo de barras
     *
     * @param evt
     */
private void t_seleccion_cbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_seleccion_cbKeyPressed
    // BUSQUEDA DE LÍNEA DENTRO DEL TICKET A PARTIR DE UN CÓDIGO DE ARTÍCULO
    try {
        if (evt.getKeyChar() == '\n') {
            if (t_seleccion_cb.getText().length() > 0) {
                t_seleccion_cb.setText(Articulos.formateaCodigoBarras(t_seleccion_cb.getText()));
            }
            CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
            codigobarra = t_seleccion_cb.getText();
            Integer indexLinea = Sesion.getTicket().getIndexLinea(codigobarra);
            if (indexLinea != null && t_seleccion_cb.getText().length() > 0) {
                lb_error.setText("");
                v_seleccion_linea.setVisible(false);
                t_factura.requestFocus();
                ListSelectionModel selectionModel = t_factura.getSelectionModel();
                selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
            } else if (t_seleccion_cb.getText().length() > 0) {
                lb_error.setText("El código de barras ingresado es incorrecto");
            } else {
                lb_error.setText("No se encontro ningún articulo para ese código de barras");
                log.debug("No se encontró ningún artículo con codigo de barras " + codigobarra);
            }
        }
    } catch (Exception e) {
        log.error("Error al seleccionar línea", e);
    }
}//GEN-LAST:event_t_seleccion_cbKeyPressed

private void v_buscar_articuloWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_buscar_articuloWindowGainedFocus
    p_buscar_articulo.establecerFoco();
}//GEN-LAST:event_v_buscar_articuloWindowGainedFocus

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
                codigoArticulo = t_seleccion_cb1.getText();
                Integer indexLinea = Sesion.getTicket().getIndexLineaCodigoArticulo(codigoArticulo);
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
                    log.debug("No se encontro ningún articulo con el código de artículo " + codigoArticulo);
                }
            }
        } catch (Exception e) {
            log.error("Error al seleccionar línea", e);
        }
    }//GEN-LAST:event_t_seleccion_cb1KeyTyped

    private void jb_ok1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_ok1KeyTyped

    }//GEN-LAST:event_jb_ok1KeyTyped

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        accionAplicarDescuento();
    }//GEN-LAST:event_jb_ok1ActionPerformed

    private void jb_cancel1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_cancel1KeyTyped
        if (evt.getKeyChar() == '\n') {
            accionCancelarEdicionLinea();
        }
    }//GEN-LAST:event_jb_cancel1KeyTyped

    private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
        accionCancelarEdicionLinea();
    }//GEN-LAST:event_jb_cancel1ActionPerformed

    private void o_env_adomicilioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_env_adomicilioKeyTyped

    }//GEN-LAST:event_o_env_adomicilioKeyTyped

    private void o_env_adomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_env_adomicilioActionPerformed
        if (o_env_adomicilio.isSelected()) {
            o_pendiente_entrega.setSelected(false);
            o_pedido_facturado.setVisible(true);
            o_pedido_facturado.setSelected(false);
            o_intercambio.setVisible(true);
            o_pedido_facturado.setSelected(false);
        } else {
            o_pedido_facturado.setVisible(false);
            o_pedido_facturado.setSelected(false);
            o_intercambio.setVisible(false);
            o_intercambio.setSelected(false);
        }
    }//GEN-LAST:event_o_env_adomicilioActionPerformed

    private void o_pendiente_entregaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_pendiente_entregaKeyPressed

    }//GEN-LAST:event_o_pendiente_entregaKeyPressed

    private void o_pendiente_entregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_pendiente_entregaActionPerformed
        if (o_pendiente_entrega.isSelected()) {
            o_env_adomicilio.setSelected(false);
            o_pedido_facturado.setVisible(true);
            o_pedido_facturado.setSelected(false);
            o_intercambio.setVisible(true);
            o_intercambio.setSelected(false);
        } else {
            o_pedido_facturado.setVisible(false);
            o_intercambio.setVisible(false);
            o_pedido_facturado.setSelected(false);
        }
    }//GEN-LAST:event_o_pendiente_entregaActionPerformed

    private void t_descuento_valorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_descuento_valorKeyPressed
        if (evt.getKeyChar() == '\n') {
            lostFocusDescuento();
            accionAplicarDescuento();
        }
    }//GEN-LAST:event_t_descuento_valorKeyPressed

    private void t_descuento_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_descuento_valorActionPerformed

    }//GEN-LAST:event_t_descuento_valorActionPerformed

    private void o_pedido_facturadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_pedido_facturadoActionPerformed
        /* LineaTicket lineaTicket = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
        lineaTicket.setPedidoFacturado(o_pedido_facturado.isSelected());
        lineaTicket.setObservacionPedidoFacturado(jTextArea1.getText());*/

    }//GEN-LAST:event_o_pedido_facturadoActionPerformed

    private void o_pedido_facturadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_pedido_facturadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_o_pedido_facturadoKeyTyped

    private void t_incremento_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_incremento_valorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_incremento_valorActionPerformed

    private void t_incremento_valorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_incremento_valorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_incremento_valorKeyPressed

    private void t_descuento_valorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_descuento_valorFocusLost
        lostFocusDescuento();
    }//GEN-LAST:event_t_descuento_valorFocusLost

    private void lostFocusDescuento() {
        LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
        if (!isNumeric(t_descuento_valor.getText())) {
            t_descuento_valor.setText("0.00");
        }
        Tarifas tarifa = null;
        try {
            tarifa = tarifasServices.getTarifaArticulo(lineaTicketAnterior.getArticulo().getCodart());
        } catch (TarifaException ex) {
            java.util.logging.Logger.getLogger(JVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!t_descuento_valor.getText().isEmpty() && !t_descuento_valor.getText().equals("0.00") && !t_descuento_valor.getText().equals("0.0000")) {
            t_incremento_valor.setText("0.00");
            BigDecimal nuevoPrecio = tarifa.getPrecioTotal().subtract((tarifa.getPrecioTotal()).multiply(new BigDecimal(t_descuento_valor.getText())).divide(new BigDecimal(100)));
            t_valor_calculado.setText(nuevoPrecio.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            t_precio_valor.setText(tarifa.getPrecioTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        if (!t_descuento_valor.getText().isEmpty() && !t_descuento_valor.getText().equals("0.00")
                && !t_incremento_valor.getText().isEmpty() && !t_incremento_valor.getText().equals("0.00")) {
            t_valor_calculado.setText(lineaTicketAnterior.getPrecioTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            t_precio_valor.setText(lineaTicketAnterior.getPrecioTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
    }
    private void t_incremento_valorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_incremento_valorFocusLost
        // TODO add your handling code here:
        if (!isNumeric(t_incremento_valor.getText())) {
            t_incremento_valor.setText("0.00");
        }
        LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
        DatosAdicionalesLineaTicket datosAdicionales = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDatosAdicionales();
        BigDecimal valorIncrementoAnterior = BigDecimal.ZERO;
        if (datosAdicionales != null) {
            valorIncrementoAnterior = datosAdicionales.getIncremento();
        }
        Tarifas tarifa = null;
        try {
            tarifa = tarifasServices.getTarifaArticulo(lineaTicketAnterior.getArticulo().getCodart());
        } catch (TarifaException ex) {
            java.util.logging.Logger.getLogger(JVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        BigDecimal valorIncrementoNuevo = new BigDecimal(t_incremento_valor.getText());
        if (!t_incremento_valor.getText().isEmpty() && !t_incremento_valor.getText().equals("0.00")
                && valorIncrementoAnterior.compareTo(valorIncrementoNuevo) != 0) {
            t_descuento_valor.setText("0.00");
            BigDecimal nuevoPrecio = tarifa.getPrecioTotal().add((tarifa.getPrecioTotal()).multiply(new BigDecimal(t_incremento_valor.getText())).divide(new BigDecimal(100)));
            t_valor_calculado.setText(nuevoPrecio.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            t_precio_valor.setText(nuevoPrecio.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        if (!t_incremento_valor.getText().isEmpty() && t_incremento_valor.getText().equals("0.00")
                && !t_descuento_valor.getText().isEmpty() && t_descuento_valor.getText().equals("0.00")) {
            t_valor_calculado.setText(tarifa.getPrecioTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            t_precio_valor.setText(tarifa.getPrecioTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
    }//GEN-LAST:event_t_incremento_valorFocusLost

    private void t_precio_valorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_precio_valorFocusLost
        // TODO add your handling code here:
        t_valor_calculado.setText(t_precio_valor.getText());
    }//GEN-LAST:event_t_precio_valorFocusLost

    private void t_facturaComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_t_facturaComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_t_facturaComponentResized

    private void o_intercambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_intercambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_o_intercambioActionPerformed

    private void o_pendiente_entregaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_pendiente_entregaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_o_pendiente_entregaKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo P_descuento_valor;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_datos_facturacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_appal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_datoscliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_devolucion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_edicion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_otro_local;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_pagos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_seleccion;
    private javax.swing.JPanel f_pagos;
    private javax.swing.JFrame f_pagos2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_compra_socio;
    private javax.swing.JLabel jLabel_numArticulos;
    private javax.swing.JLabel jLabel_puntos;
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
    private javax.swing.JLabel lb_tienda_destino;
    private javax.swing.JLabel lb_tienda_origen;
    private com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket lineasTicket;
    private javax.swing.JPanel m_ventas;
    private javax.swing.JCheckBox o_env_adomicilio;
    private javax.swing.JCheckBox o_intercambio;
    private javax.swing.JCheckBox o_pedido_facturado;
    private javax.swing.JCheckBox o_pendiente_entrega;
    private com.comerzzia.jpos.gui.JBuscar p_buscar_articulo;
    private com.comerzzia.jpos.gui.JDatosEnvio p_datos_envio;
    private javax.swing.JPanel p_descuento_principal;
    private javax.swing.JPanel p_descuentos;
    private javax.swing.JPanel p_fatura;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo p_fondo_imagen;
    private javax.swing.JPanel p_idcliente;
    private javax.swing.JPanel p_imagen_airticulo;
    private javax.swing.JPanel p_ingreso_articulos;
    private com.comerzzia.jpos.gui.components.JPanelTransparente p_pagos_contenedor;
    private javax.swing.JPanel p_principal;
    private javax.swing.JPanel p_publicidad;
    private javax.swing.JPanel p_seleccion_cb;
    private com.comerzzia.jpos.gui.JVentaEntreLocales p_venta_entre_locales;
    private javax.swing.JScrollPane sp_t_factura;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cantidad_valor;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_descuento_valor;
    private javax.swing.JTable t_factura;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_incremento_valor;
    private com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp t_introduccionArticulos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_precio_valor;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb1;
    private javax.swing.JLabel t_valor_calculado;
    private javax.swing.JLabel t_venta_cliente;
    private javax.swing.JDialog v_buscar_articulo;
    private javax.swing.JDialog v_datos_envio;
    private javax.swing.JDialog v_descuento_linea;
    private javax.swing.JDialog v_seleccion_linea;
    private javax.swing.JDialog v_venta_entre_locales;
    // End of variables declaration//GEN-END:variables

    public void cerrarPanelActivo() {
        if (this.p_activo == null) {
            return;
        }
        p_pagos_contenedor.setVisible(false);
        p_pagos.setVisible(false);
        p_activo.setVisible(false);
        if (!p_pagos.isCancelado()) {
            //desabilita_elementos_formulario();
            //removeFunctionKeys();
            Sesion.getTicket().recalcularTotales();
            refrescaTablaTicket();
        }

    }

    /**
     * Función que desabilita los elementos de formulario que quedan debajo de
     * la pantalla de pagos
     */
    private void desabilita_elementos_formulario() {
        m_ventas.setVisible(false);
        b_datos_facturacion.setEnabled(false);
        b_ventas_appal.setEnabled(false);
        b_ventas_datoscliente.setEnabled(false);
        b_ventas_devolucion.setEnabled(false);
        b_ventas_edicion.setEnabled(false);
        b_ventas_pagos.setEnabled(false);
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
    public void habilita_elementos_formulario() {
        m_ventas.setVisible(true);
        b_datos_facturacion.setEnabled(true);
        if (Sesion.isSukasa()) {
            b_ventas_appal.setEnabled(true);
        }
        b_ventas_datoscliente.setEnabled(true);
        b_ventas_devolucion.setEnabled(true);
        b_ventas_edicion.setEnabled(true);
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
            log.debug("Crear línea ticket");
            // comprobamos que existen valores correctos en el componente
            t_introduccionArticulos.checkValores(TicketTableCellRenderer.isModoConCantidad());
            // consultamos el artículo y su tarifa
            String codigo = t_introduccionArticulos.getCodArticulo();
            if (codigo.length() > 1 && codigo.substring(0, 1).equalsIgnoreCase("r")) {
                abrirBuscadorPorCodigo(codigo.substring(1));
                return;
            }
            if (codigo.length() > 2 && (codigo.substring(0, 2).equalsIgnoreCase("bm") || codigo.substring(0, 2).equalsIgnoreCase("sk"))) {
                OpcionesVentaManager.anadeListaPDA(Sesion.getTicket(), codigo, this);
                t_introduccionArticulos.reset();
                refrescaTablaTicket();
            } else if (codigo.length() > 0) {
                Articulos art;
                if (codigo.length() == 9 && codigo.charAt(4) == '.') {
                    log.debug("crearLineaTicket() - Buscando artículo por código de artículo: " + codigo);
                    art = articulosServices.getArticuloCod(codigo);
                    if (art == null) {
                        throw new ArticuloNotFoundException("No se ha encontrado o se encuentra inactivo el artículo con código: " + codigo);
                    }
                    //Buscar codigoBarras del código artículo introducido
                    codigo = Articulos.formateaCodigoBarras(ArticulosServices.consultarCodigoBarras(codigo));
                } else {
                    log.debug("crearLineaTicket() - Buscando artículo por código de barras: " + codigo);
                    /**
                     * Cambio Rd para lector codigo de barras se agregan o
                     * quitan ceros segun el caso
                     */
                    if (codigo.length() < 11) {
                        throw new ArticuloNotFoundException("El código de barra no puede ser menor a 11 dígitos:  " + codigo);
                    }
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

                        } else {
                            if (codigo.length() == 14) {
                                codigo = codigo.substring(1);
                            }
                        }
//                        }
                    }
                    log.debug("Codigo Modificado: " + codigo);
                    codigo = Articulos.formateaCodigoBarras(codigo);
                    art = articulosServices.getArticuloCB(codigo);
                }

                // Comprobamos que no es un artículo Bloqueado
                BloqueosServices.isItemBloqueado(art.getCodmarca().getCodmarca(), art.getIdItem(), art.getCodart());

                // Tratamiento de venta a otros locales
                if (Sesion.getTicket().esVentaEntreLocales() && !art.isVentaOtroLocal()) {
                    ventana_padre.crearAdvertencia("El articulo introducido no esta disponible para la venta entre locales");
                    ventana_padre.compruebaAutorizacion(Operaciones.VENTA_A_OTRO_LOCAL);
                }
                if (codigo.equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))) {
                    accionCrearLineaGarantia();
                    t_introduccionArticulos.reset();
                } else if (codigo.equals(Variables.getVariable(Variables.PROMO_DIA_SOCIO_ASOCIACION_CODBARRAS))) {
                    accionAsociarFacturaDiaSocio();
                    t_introduccionArticulos.reset();
                } else if (codigo.equals(Variables.getVariable(Variables.PROMO_ITEM_BASE_ASOCIACION_CODBARRAS))) {
                    if (Sesion.getAutorizaItemBase() == null) {
                        Sesion.setAutorizaItemBase(EnumCodigoItemBase.ITEM_BASE_ACTIVO.getCodigo());
                        t_introduccionArticulos.reset();
                    } else {
                        Sesion.setAutorizaItemBase(EnumCodigoItemBase.ITEM_BASE_MAS.getCodigo());
                        t_introduccionArticulos.reset();
                    }

                } else {
                    accionCrearLineaArticulo(codigo, art);
                }
            }
        } catch (SinPermisosException ex) {
            log.info("El Cajero no tiene permisos para realizar la Acción");
        } catch (ArticuloNotFoundException e) {
            // Si no se encuentra un artículo con el código indicado, comprobamos is es un cupón
            if (!crearLineaTicketCupon()) {
                log.debug("No se encontró el Artículo.");
                ventana_padre.crearError(e.getMessage());
            } else {
                refrescaTablaTicket();
                t_introduccionArticulos.reset();
            }
        } catch (TicketNuevaLineaException e) {
            log.error("Error creando nueva línea de ticket: " + e.getMessage());
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (ListaPDAException e) {
            log.error("Error añadiendo artículos desde PDA: " + e.getMessage(), e);
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (BloqueoFoundException e) {
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (Exception e) {
            log.error("Error al leer el Artículo.", e);
            ventana_padre.crearError("Error Consultando datos del Artículo");
            t_introduccionArticulos.requestFocus();
        }
    }

    private boolean crearLineaTicketCupon() {
        try {
            CodigoBarrasCuponPromo codigoCupon = new CodigoBarrasCuponPromo(t_introduccionArticulos.getCodArticulo());
            Sesion.getTicket().crearLineaTicketCupon(codigoCupon);
        } catch (NotAValidBarCodeException e) {
            return false;
        } catch (CuponNotValidException e) {
            ventana_padre.crearError(e.getMessage());
        } catch (CuponException e) {
            ventana_padre.crearError(e.getMessage());
        } catch (Exception e) {
            log.error("crearLineaTicketCupon() - Error inesperado intentando aplicar cupón: " + e.getMessage(), e);
            ventana_padre.crearError("Error intentando aplicar cupón.");
        }
        return true;
    }

    private void abrirBuscadorPorCodigo(String codigoArticulo) {
        v_buscar_articulo.setLocationRelativeTo(null);
        p_buscar_articulo.iniciaVista(codigoArticulo);
        p_buscar_articulo.establecerFoco();
        JPrincipal.setPopupActivo(v_buscar_articulo);
        v_buscar_articulo.setVisible(true);
        JPrincipal.setPopupActivo(null);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    public void refrescaTablaTicket() {
        log.debug("Refresca la tabla ticket");
        l_v_total.setText("$ " + Sesion.getTicket().getTotales().getTotalAPagar().toString());
        jLabel_numArticulos.setText("<html><b>" + Sesion.getTicket().getLineas().getNumLineas() + "</b> artículos ingresados</html>");
        t_factura.setModel(new TicketTableModel(Sesion.getTicket().getLineas()));

        // Anchos de columnas de la tabla. Mod
        t_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int i = 0;
        for (int anchura : TicketTableCellRenderer.getAnchosColumna()) {
            t_factura.getColumnModel().getColumn(i).setPreferredWidth(anchura);
            i++;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                t_factura.scrollRectToVisible(t_factura.getCellRect(t_factura.getRowCount() - 1, 0, true));
            }
        });
        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();

        //jLabel_bono.setText("$ " + Sesion.getTicket().getTotales().getTotalAfiliadoUnaCuota().toString());
        if (Sesion.getTicket().esVentaEntreLocales()) {
            lb_tienda_origen.setText("LOCAL VENTA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaOrigen());
            lb_tienda_destino.setText("RECOGIDA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaDestino());
        } else {
            //reseteo de etiquetas
            lb_tienda_origen.setText("");
            lb_tienda_destino.setText("");
        }
        //jLabel_puntos.setText(Sesion.getTicket().getPuntosTicket().getPuntosDisponiblesRenderer());

    }

    public void limpiarFormularioEntregaDomicilio() {
        p_datos_envio.limpiarFormularioInicial();
    }

    public void refrescaTablaTicket(String motivo, Integer dto) {
        log.debug("Refresca la tabla ticket");
        l_v_total.setText("$ " + Sesion.getTicket().getTotales().getTotalAPagar().toString());
        jLabel_numArticulos.setText("<html><b>" + Sesion.getTicket().getLineas().getNumLineas() + "</b> artículos ingresados</html>");
        t_factura.setModel(new TicketTableModel(Sesion.getTicket().getLineas()));

        //  LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
        String referencia = Sesion.getTicket().getUid_ticket() + " " + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "-" + Sesion.getTicket().getCodcaja() + "-" + Sesion.getTicket().getId_ticket();
        BigDecimal importeTotalTarifaOrigen = Sesion.getTicket().getLineas().getTicket().getTotales().getImporteTotalTarifaOrigen();
        BigDecimal importeTotalTarifa = Sesion.getTicket().getLineas().getTicket().getTotales().getTotalAPagar();
        BigDecimal descPorce = BigDecimal.valueOf(dto);
        guardar(motivo, null, referencia, null, importeTotalTarifaOrigen, importeTotalTarifa, descPorce, null, null);
        // Anchos de columnas de la tabla. Mod
        t_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int i = 0;
        for (int anchura : TicketTableCellRenderer.getAnchosColumna()) {
            t_factura.getColumnModel().getColumn(i).setPreferredWidth(anchura);
            i++;
        }

        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();

        //jLabel_bono.setText("$ " + Sesion.getTicket().getTotales().getTotalAfiliadoUnaCuota().toString());
        if (Sesion.getTicket().esVentaEntreLocales()) {
            lb_tienda_origen.setText("LOCAL VENTA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaOrigen());
            lb_tienda_destino.setText("RECOGIDA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaDestino());
        } else {
            //reseteo de etiquetas
            lb_tienda_origen.setText("");
            lb_tienda_destino.setText("");
        }

    }

    public void refrescaTablaTicketPromo(String motivo, BigDecimal dto) {
        log.debug("Refresca la tabla ticket");
        l_v_total.setText("$ " + Sesion.getTicket().getTotales().getTotalAPagar().toString());
        jLabel_numArticulos.setText("<html><b>" + Sesion.getTicket().getLineas().getNumLineas() + "</b> artículos ingresados</html>");
        t_factura.setModel(new TicketTableModel(Sesion.getTicket().getLineas()));

        //  LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
        String referencia = Sesion.getTicket().getUid_ticket() + " " + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "-" + Sesion.getTicket().getCodcaja() + "-" + Sesion.getTicket().getId_ticket();
        BigDecimal importeTotalTarifaOrigen = Sesion.getTicket().getLineas().getTicket().getTotales().getImporteTotalTarifaOrigen();
        BigDecimal importeTotalTarifa = Sesion.getTicket().getLineas().getTicket().getTotales().getTotalAPagar();
        BigDecimal descPorce = dto;
//        guardar(motivo, null, referencia, null, importeTotalTarifaOrigen, importeTotalTarifa, descPorce, null, null);
        // Anchos de columnas de la tabla. Mod
        t_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int i = 0;
        for (int anchura : TicketTableCellRenderer.getAnchosColumna()) {
            t_factura.getColumnModel().getColumn(i).setPreferredWidth(anchura);
            i++;
        }

        // Foco en introducción de artículos
        t_introduccionArticulos.requestFocus();

        //jLabel_bono.setText("$ " + Sesion.getTicket().getTotales().getTotalAfiliadoUnaCuota().toString());
        if (Sesion.getTicket().esVentaEntreLocales()) {
            lb_tienda_origen.setText("LOCAL VENTA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaOrigen());
            lb_tienda_destino.setText("RECOGIDA: " + Sesion.getTicket().getVentaEntreLocales().getDesTiendaDestino());
        } else {
            //reseteo de etiquetas
            lb_tienda_origen.setText("");
            lb_tienda_destino.setText("");
        }

    }

    private void removeHotKey(KeyStroke keyStroke, String inputActionKey) {
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.remove(keyStroke);
        actionMap.remove(inputActionKey);
    }

    public void addFunctionKeys() {

        // Ponemos en modo venta el formulario
        estado = JVentas.VENTA;

        // botones de menú de la ventana
        addFuntionKeysBotonesMenu();
        // otros controles
        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    aux = 1L;
                    String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.BORRAR_LINEA_VENTA);
                    Sesion.getTicket().setAutorizadorVenta(autorizador);
                    lineaSelecionada = t_factura.getSelectedRow();
                    if (lineaSelecionada >= 0) {
                        LineaTicket linea = Sesion.getTicket().getLineas().getLinea(lineaSelecionada);

                        Sesion.getTicket().eliminarLineaTicket(lineaSelecionada);
                        calculoPromoDescNuevoSocio(null);
                        p_datos_envio.limpiarFormularioInicial();
                        Sesion.getTicket().setProcesoEnvioDomicilioDTO(null);

                        if (linea.isLineaFacturacion()) {
                            String respuesta = "";
                            try {
                                String factura = Sesion.getTicket().getIdFactura();
                                String[] fact = factura.split("-");
                                respuesta = ArticulosServices.anularPedidoFacturado(linea.getArticulo().getCodart(), fact[2]);
                                ventana_padre.crearAdvertencia(respuesta);
                            } catch (Exception ex) {
                                log.debug("No se ha podido anular el pedido facturado");
                            }
                        }
                    } else {
                        Sesion.getTicket().eliminarLineaTicket();
                    }
                    refrescaTablaTicket();
                } catch (KitException ex) {
                    log.error("Error al eliminar el kit " + ex.getMessage());
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
                Sesion.getTicket().setObservaciones(ventana_padre.crearVentanaObservaciones(Sesion.getTicket().getObservaciones()));
            }
        };
        addHotKey(ctrlO, "ObservacionesCtrlO", listenerCtrlO);

        KeyStroke ctrlAltD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK + InputEvent.CTRL_MASK);
        Action listenerCtrlAltD = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
                    Integer dto = JPrincipal.crearVentanaIngresarValorEntero("Descuento de factura: ", Numero.CIEN);
                    if (dto != null) {
                        PromocionTipoDtoManualTotal.activar(dto);
                        Sesion.getTicket().recalcularTotales();
                        String motivo = "DESCUENTO GLOBAL";
                        refrescaTablaTicket(motivo, dto);
                        //Guarda autorizador y motivo
                    }
                } catch (SinPermisosException ex) {
                    log.debug("No se tienen permisos para descontar el subtotal de la factura.");
                }
            }
        };
        addHotKey(ctrlAltD, "DescuentoSubtotalCtrlAltD", listenerCtrlAltD);

        KeyStroke ctrlAltG = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_MASK + InputEvent.CTRL_MASK);
        Action listenerCtrlAltG = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
                    BigDecimal desc = JPrincipal.crearVentanaIngresarValorDecimal("Descuento de factura: ", Numero.CIEN);
                    BigDecimal porcDesc = (desc.multiply(Numero.CIEN)).divide(Sesion.getTicket().getTotales().getTotalAPagar(), 8, RoundingMode.HALF_UP);
                    if (desc != null) {
                        PromocionTipoDtoManualTotal.activarPromoDiaSocio(porcDesc);
                        Sesion.getTicket().recalcularTotales();
                        String motivo = "DESCUENTO GLOBAL";
                        refrescaTablaTicketPromo(motivo, desc);
                        //Guarda autorizador y motivo
                    }
                } catch (SinPermisosException ex) {
                    log.debug("No se tienen permisos para descontar el subtotal de la factura.");
                }
            }
        };
        addHotKey(ctrlAltG, "DescuentoSubtotalCtrlAltG", listenerCtrlAltG);
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

        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        Action listenerf3 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_devolucionActionPerformed(ae);
            }
        };
        addHotKey(f3, "IdentClientF3", listenerf3);

        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_edicionActionPerformed(ae);

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

        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        Action listenerf6 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionVentaEntreLocales();

            }
        };
        addHotKey(f6, "IdentClientF6", listenerf6);

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
                b_ventas_appalActionPerformed(ae);

            }
        };
        addHotKey(f8, "IdentClientF8", listenerf8);

    }

    private void removeFunctionKeys() {
        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        KeyStroke ctrlAltD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK + InputEvent.CTRL_MASK);
        KeyStroke ctrlAltG = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_MASK + InputEvent.CTRL_MASK);
        removeHotKey(f2, "IdentClientF2");
        removeHotKey(f3, "IdentClientF3");
        removeHotKey(f4, "IdentClientF4");
        removeHotKey(f5, "IdentClientF5");
        removeHotKey(f7, "IdentClientF7");
        removeHotKey(f8, "IdentClientF8");
        removeHotKey(f9, "IdentClientF9");
        removeHotKey(ctrlMenos, "VentasCtrlMenos");
        removeHotKey(ctrlO, "ObservacionesCtrlO");
        removeHotKey(ctrlAltD, "DescuentoSubtotalCtrlAltD");
        removeHotKey(ctrlAltG, "DescuentoSubtotalCtrlAltG");

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
        auxGarantia = 0L;
    }

    private void resetearFormularioEnvio() {
        log.info("Resetear formulario de envío");
        t_descuento_valor.setText("");
        o_env_adomicilio.setSelected(false);
        o_pendiente_entrega.setSelected(false);
    }

    private void iniciaFormularioDatosAdicionales(DatosAdicionalesLineaTicket datosAdicionales) {
        log.info("Iniciar Formulario de datos adicionales");

        clearError();
        String[] codigosHabilitados = Variables.getVariable(Variables.FACTURACION_ESPECIAL_CODIGO).split(",");
        Boolean habilitarPrecio = false;
        for (String codigo : codigosHabilitados) {
            if (Sesion.getTicket().getLineas().getLinea(lineaSelecionada).getArticulo().getCodart().equals(codigo)) {
                habilitarPrecio = true;
                break;
            }
        }
        t_precio_valor.setEnabled(habilitarPrecio);
        t_incremento_valor.setEnabled(!habilitarPrecio);
        t_descuento_valor.setEnabled(!habilitarPrecio);

        //o_pendiente_entrega.setText("Pendiente Retiro Local");
        o_env_adomicilio.setText("Entregas a Domicilio");
        if (datosAdicionales == null) {
            t_descuento_valor.setText("0");
            o_env_adomicilio.setSelected(false);
            o_pendiente_entrega.setSelected(false);
            o_pedido_facturado.setSelected(false);
            t_incremento_valor.setText("0.00");
            t_valor_calculado.setText(Sesion.getTicket().getLineas().getLinea(lineaSelecionada).getPrecioTotalTarifaOrigen().toString());
        } else {
            BigDecimal descuento = datosAdicionales.getDescuento();
            if (Numero.isMenorACero(descuento)) {
                t_descuento_valor.setText("0");
            } else {
                t_descuento_valor.setText(String.valueOf(descuento));
                BigDecimal nuevoPrecio = Sesion.getTicket().getLineas().getLinea(lineaSelecionada).getPrecioTotal();
                t_valor_calculado.setText(nuevoPrecio.toString());
            }
            BigDecimal incremento = datosAdicionales.getIncremento();
            if (Numero.isMenorACero(incremento)) {
                t_incremento_valor.setText("0");
            } else {
                t_incremento_valor.setText(String.valueOf(incremento));
            }
            o_env_adomicilio.isSelected();
            o_pendiente_entrega.isSelected();
            o_env_adomicilio.setSelected(datosAdicionales.isEnvioDomicilio());
            o_pendiente_entrega.setSelected(datosAdicionales.isRecogidaPosterior());
            o_pedido_facturado.setSelected(datosAdicionales.isPedidoFacturado());
            o_intercambio.setSelected(datosAdicionales.isIntercambio());
        }
        if (o_pedido_facturado.isSelected()) {
            o_pedido_facturado.setVisible(true);
            o_intercambio.setVisible(true);
        } else {
            o_pedido_facturado.setVisible(false);
            o_intercambio.setVisible(false);
        }

    }

    private void enviaFormularioDatosAdicionales(String autorizadorDescuento, Integer cantidad_valor, BigDecimal precio_valor) {
        log.info("Enviar formulario de Datos Adicionales");
        BigDecimal valorDescuento = BigDecimal.ZERO;
        try {
            if (t_descuento_valor.getText().isEmpty()) {
                t_descuento_valor.setText("0");
            }
            valorDescuento = new BigDecimal(t_descuento_valor.getText());
            DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
            datosAdicionales.setEnvioDomicilio(o_env_adomicilio.isSelected());
            datosAdicionales.setRecogidaPosterior(o_pendiente_entrega.isSelected());
            datosAdicionales.setPedidoFacturado(o_pedido_facturado.isSelected());
            datosAdicionales.setIntercambio(o_intercambio.isSelected());
            datosAdicionales.setDescuento(valorDescuento);
            datosAdicionales.setAutorizador(autorizadorDescuento);
            datosAdicionales.setIncremento(new BigDecimal(t_incremento_valor.getText()));

            LineaTicket linea = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
            Tarifas tarifa = tarifasServices.getTarifaArticulo(linea.getArticulo().getCodart());
            if (!tarifa.getPrecioTotal().equals(precio_valor)) {
                tarifa.cambiarPrecioTotal(precio_valor, linea.getArticulo());
            }

            // Linea nueva reemplaza
            //Creamos la nueva línea, la sustituimos por la que estamos editando y luego la borramos
            LineaTicket lineaNueva = Sesion.getTicket().crearLineaTicket(linea.getCodigoBarras(), linea.getArticulo(), cantidad_valor, tarifa, linea.getCantidad());
            if (linea.getCodEmpleado() != null) {
                lineaNueva.setCodEmpleado(linea.getCodEmpleado());
            } else {
                lineaNueva.setCodEmpleado(Sesion.getUsuario().getUsuario());
            }
            if (linea.getReferenciaGarantia() != null) {
                lineaNueva.setReferenciaGarantia(linea.getReferenciaGarantia());
            }
            if (linea.getReferenciaKit() != null) {
                lineaNueva.setReferenciaKit(linea.getReferenciaKit());
            }
            lineaNueva.setPedidoFacturado(linea.isPedidoFacturado());
            lineaNueva.setObservacionPedidoFacturado(linea.getObservacionPedidoFacturado());
            lineaNueva.setStockDisponible(linea.getStockDisponible());
            lineaNueva.setStockDisponibleBodega(linea.getStockDisponibleBodega());
            lineaNueva.setListaPromocion(linea.getListaPromocion());
            int indexNuevaLinea = Sesion.getTicket().getLineas().getNumLineas() - 1;
            Sesion.getTicket().setDatosAdicionalesLinea(indexNuevaLinea, datosAdicionales);
            Sesion.getTicket().getLineas().setLinea(lineaNueva, indexLineaAEditar);
            Sesion.getTicket().eliminarLineaTicket(indexNuevaLinea);
            lineaNueva.setPromocionLinea(linea.getPromocionLinea());
        } catch (NumberFormatException e) {
            log.info("Formato numérico incorrecto");
        } catch (TicketNuevaLineaException e) {
            log.error("Error creando nueva línea de ticket: " + e.getMessage());
            ventana_padre.crearError(e.getMessage());
            t_introduccionArticulos.requestFocus();
        } catch (Exception e) {
            log.error("Error en envío de formlario de datos adicionales " + e.getMessage(), e);
        }
    }

    private void accionIrAPagos() {
        // mostramos mensajes de acumulación de puntos.
        log.debug("Comprobamos posible acumulación de puntos...");
        Sesion.getTicket().getPuntosTicket().compruebaPuntosTicket();

        log.debug("Desactivando elementos del formulario de ventas...");
        desabilita_elementos_formulario();

        log.debug("Desactivamos teclas rápidas...");
        removeFunctionKeys();

        log.debug("Instanciamos ventana de pagos...");
        f_pagos.removeAll();
        p_pagos = null;
        //Runtime.getRuntime().gc();
        p_pagos = new JReservacionesPagosV(Sesion.getTicket());
        f_pagos.add(p_pagos);

        // cerramos el panel activo
        p_pagos.setSize(983, 591);

        log.debug("Mostramos pantalla de pagos...");
        p_pagos_contenedor.setVisible(true);
        f_pagos.setVisible(true);
        p_pagos.setVisible(true);
        p_activo = p_pagos;
        estado = JVentas.PAGOS;
        p_pagos.iniciaVista();
    }

    private void accionAccederAPagos() {
        if (Sesion.getTicket().getLineas().getNumLineas() == 0) {
            log.debug("No se puede acceder a pagos sin comprar ningún artículo");
            ventana_padre.crearAdvertencia("El ticket está vacío.");
            return;
        }

        if (Sesion.getTicket().getTicketPromociones().tieneMensajesPromocion()) {
            List<String> mensajes = Sesion.getTicket().getTicketPromociones().getMensajesPromocion();
            for (String mensaje : mensajes) {
                int altura = (15 * mensaje.split("\n").length);
                JPrincipal.getInstance().crearVentanaInformacion(mensaje, altura);
            }
            return;
        }

        // ofrecemos compra de artículos con puntos
        log.debug("Intentamos aplicar canjeo de puntos...");
        TicketPuntosBean ticketPuntos = Sesion.getTicket().getPuntosTicket();
        if (!ticketPuntos.isConsumePuntos()) {
            boolean canjeoRealizado = ticketPuntos.canjeaPuntosTicket();
            if (canjeoRealizado) {
                log.debug("Canjeo de puntos aplicado.");
                refrescaTablaTicket();
                return; // si se ha canjeado, volvemos a la pantalla para que el cliente vea el resultado
            } else {
                log.debug("No se aplicó ningún canjeo de puntos.");
            }
        }

        // Si hay Envio a domicilio para algún artículo            
        if (Sesion.getTicket().hayEnvioADomicilio()) {
            p_datos_envio.inicializaFormulario();
            JPrincipal.setPopupActivo(v_datos_envio);
            v_datos_envio.setVisible(true);
            JPrincipal.setPopupActivo(null);
            if (p_datos_envio.isAceptado()) {
                accionIrAPagos();
            }
        } else {
            accionIrAPagos();
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
        log.info("Acción Aplicar Descuento");
        try {
            LineaTicket lineaTicketSeleccionada = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
            if (!lineaTicketSeleccionada.getArticulo().getCodseccion().equals(Constantes.SECCION_OBSEQUIOS)) {
                t_cantidad_valor.validar();
                t_descuento_valor.validar();
                t_precio_valor.validar();
            }
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
                valorDescuentoAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDescuento().setScale(4, RoundingMode.HALF_UP);
            } else {
                valorDescuentoAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getDatosAdicionales().getDescuento();
            }
            if (t_descuento_valor.getText().isEmpty()) {
                valorDescuento = BigDecimal.ZERO;
            }
            LineaTicket lineaTicketAnterior = Sesion.getTicket().getLineas().getLinea(indexLineaAEditar);
            precio_valor_anterior = lineaTicketAnterior.getPrecioTotalTarifaOrigen();

            boolean procede = true;
//            boolean procedeStock = true;
            String referencia = Sesion.getTicket().getUid_ticket() + " " + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "-" + Sesion.getTicket().getCodcaja() + "-" + Sesion.getTicket().getId_ticket();
            String motivo = jTextArea1.getText();
            if (o_pedido_facturado.isSelected()) {
                Long stockDisponible = 0L;
                try {
                    stockDisponible = ServicioStock.consultaDisponibleCD(Integer.parseInt(lineaTicketAnterior.getArticulo().getCodmarca().getCodmarca()), lineaTicketAnterior.getArticulo().getIdItem());
                } catch (StockException e) {
                    lb_error_ml.setText("Error al consultar el stock. Consulte con el Administrador");
                    log.error("Error al consultar el stock disponible");
                    procede = false;
                    return;
                }
                if (stockDisponible < cantidad_valor && procede) {
                    lb_error_ml.setText("No existe stock disponible para pedido facturado.Stock actual: " + stockDisponible);
                    procede = false;
                    return;
                }

                if (BloqueosServices.validarItemBloqueado(lineaTicketAnterior.getArticulo().getCodart())) {
                    lb_error_ml.setText("El articulo se encuentra bloqueado, no puede hacer un pedido facturado");
                    procede = false;
                    return;
                }
                if (procede) {
                    lineaTicketAnterior.setObservacionPedidoFacturado(motivo);
                    lineaTicketAnterior.setPedidoFacturado(o_pedido_facturado.isSelected());
                }
            }

            int stockDisponibleVenta = ServicioStock.isStockDisponibleVenta(lineaTicketSeleccionada.getArticulo());
            int cantidadEnVenta = cantidadItemPorVenta(lineaTicketSeleccionada.getArticulo()) - Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).getCantidad();
            Integer stockDisponibleVentaBodega = null;
            Integer stockDisponibleVentaLocales = null;

            if (stockDisponibleVenta < cantidad_valor + cantidadEnVenta) {
                try {
                    stockDisponibleVentaBodega = ServicioStock.consultaDisponibleCD(Integer.parseInt(lineaTicketSeleccionada.getArticulo().getCodmarca().getCodmarca()), lineaTicketSeleccionada.getArticulo().getIdItem()).intValue();
                    if (0 >= stockDisponibleVentaBodega) {
                        stockDisponibleVentaLocales = ServicioStock.consultaDisponibleLocales(lineaTicketSeleccionada.getArticulo().getItmId()).intValue();
                    }
                } catch (StockException e) {
                    stockDisponibleVentaBodega = null;
                    stockDisponibleVentaLocales = null;
                }
                //Pedimos autorizacion
                if (0 >= stockDisponibleVentaBodega) {
                    try {
                        String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.STOCK, "Artículo fuera de stock");
                        Sesion.getTicket().setAutorizadorVenta(codAdministrador);
                        ServicioLogAcceso.crearAccesoLogAdmitirFueraStock(codAdministrador, Sesion.getTicket().getIdFactura() + "-" + lineaTicketSeleccionada.getArticulo().getIdItem());
                        procede = true;
                    } catch (Exception e) {
                        lb_error_ml.setText("Error en la clave del administrador");
                        log.error("Error en la clave del administrador");
                        procede = false;
                        return;
                    }
                }
            }

            if (stockDisponibleVentaBodega != null) {
                Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).setStockDisponibleBodega(stockDisponibleVentaBodega);
            }
            if (stockDisponibleVentaLocales != null) {
                Sesion.getTicket().getLineas().getLinea(indexLineaAEditar).setStockDisponibleLocales(stockDisponibleVentaLocales);
            }

            String autorizadorDescuento = null;
            if ((valorDescuento.compareTo(valorDescuentoAnterior) != 0) || (precio_valor.compareTo(precio_valor_anterior) != 0)) {
                /*
                // Entra aquí si el descuento o el precio ha cambiado
                // Comprobamos que el descuento no se haya modificado
                autorizadorDescuento = ventana_padre.compruebaAutorizacion(Operaciones.EDITAR_LINEA_VENTA);
                Sesion.getTicket().setAutorizadorVenta(autorizadorDescuento);*/

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
                enviaFormularioDatosAdicionales(autorizadorDescuento, cantidad_valor, precio_valor);
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
                if ("DESCUENTO GLOBAL".equals(observacion)) {
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
            if (valorDescuentoAnterior != null && valorDescuentoAnterior.compareTo(valorDescuento) != 0) {
                logImpresionesDet.setCodart(lineaTicketAnterior.getArticulo().getCodart());
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

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        t_introduccionArticulos.requestFocus();
    }

    private void accionVentaEntreLocales() {
        log.info("ACCION VENTA ENTRE LOCALES");
        try {
            if (!Sesion.getTicket().esVentaEntreLocales() && Sesion.getTicket().getLineas().getNumLineas() > 0) {
                throw new ValidationException("Solo se puede crear una venta entre locales con el ticket vacío");
            }
            String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.VENTA_A_OTRO_LOCAL);
            Sesion.getTicket().setAutorizadorVentaOtroLocal(autorizador);
            v_venta_entre_locales.setLocationRelativeTo(null);
            p_venta_entre_locales.iniciaVista();
            v_venta_entre_locales.setVisible(true);
            refrescaTablaTicket();

        } catch (SinPermisosException ex) {
            ventana_padre.crearSinPermisos("No tiene permisos para realizar una venta entre locales");
        } catch (ValidationException ex) {
            ventana_padre.crearSinPermisos(ex.getMessage());
        }
    }

    @Override
    public void creaVentanaBusquedaArticulos() {
        if (estado == JVentas.VENTA) {
            log.info("ACCION BUSCAR ARTÍCULO");
            p_buscar_articulo.iniciaVista();
            p_buscar_articulo.iniciaFoco();
            v_buscar_articulo.setLocationRelativeTo(null);
            JPrincipal.setPopupActivo(v_buscar_articulo);
            v_buscar_articulo.setVisible(true);
            JPrincipal.setPopupActivo(null);
        }
    }

    private void accionIntroducirCantidad() {
        log.info("Estableciendo foco en cantidad");
        if (TicketTableCellRenderer.isModoConCantidad()) {
            t_introduccionArticulos.getT_Cantidad().requestFocus();
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
            if (Sesion.getTicket().getCliente().isGarantiaExtendidaGratis()) {
                deseaGarantiaExtendida = true;
            } /**
             * RD Se crea para solventar el problema de usuario que vende la
             * garantia
             */
            else {
                //Se crea para solventar el problema de usuario que vende la garantia
                valor = JPrincipal.getInstance().crearVentanaGarantiaUsuario("<html>¿Desea comprar la EXTENSIÓN DE GARANTÍA del producto: " + linea.getArticulo().getCodart() + "? </bt></br> Ingresar el codigo de empleado</html>");
//                deseaGarantiaExtendida = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea comprar la EXTENSIÓN DE GARANTÍA del producto: "+linea.getArticulo().getCodart()+"?");
                if (valor == null || valor.getIdUsuario() == null) {
                    return;
                }

            }
//            if (!deseaGarantiaExtendida) {
//                return;
//            }
            log.debug("accionCompruebaGarantiaExtendida() - Creando línea de garantía extendida");
            int numero = linea.getCantidad().intValue();
            if (numero > 1 && !Sesion.getTicket().getCliente().isGarantiaExtendidaGratis()) {
                numero = JPrincipal.getInstance().crearVentanaGarantiaExtendida(numero);
            }
            if (numero > 0) {
                GarantiaExtendida garantia = new GarantiaExtendida(linea.getArticulo(), linea.getPrecioTotalTarifaOrigen());
                LineaTicket lineaGarantia = crearLineaArticulo(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS), garantia.getArticuloGarantia(), numero, false);
                /**
                 * RD Cambio para requerimiento Garantia
                 */

                if (valor != null) {
                    lineaGarantia.setCodEmpleado(valor.getUsuario());

                }
                GarantiaReferencia referencia = new GarantiaReferencia(linea);
                lineaGarantia.setReferenciaGarantia(referencia);
                garantia.actualizarPreciosGarantiaTicket(lineaGarantia, Sesion.getTicket());
                Sesion.getTicket().recalcularTotales();
            }
        } catch (ArticuloNotFoundException e) {
            throw new TicketNuevaLineaException("No se ha encontrado el artículo de la garantía extendida.", e);
        }
    }

    /**
     * Método que añade una línea de tipo artículo al ticket.
     *
     * @param codigo
     * @param art
     * @throws TarifaArticuloNotFoundException
     * @throws IOException
     * @throws Exception
     */
    private void accionCrearLineaArticulo(String codigo, Articulos art) throws TicketNuevaLineaException, SQLException {
        log.debug("Acción crear línea artículo");
        int numero = Integer.valueOf(t_introduccionArticulos.getCantidad());

        if (cantidadFacturacion > 0 && esArticuloFacturacion) {
            numero = cantidadFacturacion;
        }

        LineaTicket linea = crearLineaArticulo(codigo, art, numero, true, esArticuloFacturacion);

        /* if (linea.isLineaFacturacion()) {
            String respuesta = "";
            try {
                String factura = Sesion.getTicket().getIdFactura();
                String[] fact = factura.split("-");
                respuesta = ArticulosServices.generarPedidoFacturado(linea.getArticulo().getCodart(), Sesion.getTienda().getCodalmSRI(), Sesion.getUsuario().getIdUsuario(), fact[2], "", numero);
                ventana_padre.crearAdvertencia(respuesta);

            } catch (ArticuloException ex) {
                log.error("No se ha podido generar el pedido facturado. " + ex);
            }
        }*/
        t_introduccionArticulos.reset();
        //Reseteamos los parámetros
        cantidadFacturacion = 0;
        esArticuloFacturacion = false;

        // Si el artículo posee garantía extendida preguntamos si el cliente quiere comprar la extensión
        if (linea != null) {
            accionCompruebaGarantiaExtendida(linea);
            accionCompruebaKitInstalacion(linea);
        }
        refrescaTablaTicket();
    }

    private void accionAsociarFacturaDiaSocio() throws TicketNuevaLineaException {
        log.debug("Acción asociar factura dia del socio");
        if (Sesion.promocionDiaSocio == null || !Sesion.promocionDiaSocio.isAplicableAFecha()) {
            throw new TicketNuevaLineaException("No hay ninguna promoción Día del Socio vigente en el sistema.");
        }
        if (!Sesion.getTicket().getLineas().getLineas().isEmpty()) {
            throw new TicketNuevaLineaException("Para asociar una factura anterior y aplicar promoción Día del socio, el ticket actual no puede tener artículos añadidos.");
        }
        TicketId ticketId = JPrincipal.crearVentanaLecturaIdFactura("Factura");
        if (ticketId == null) {
            return;
        }
        try {
            Sesion.getTicket().getTicketPromociones().establecerFacturaDiaSocio(ticketId);
        } catch (TicketException ex) {
            throw new TicketNuevaLineaException(ex.getMessage(), ex);
        } catch (ValidationException ex) {
            throw new TicketNuevaLineaException(ex.getMessage(), ex);
        }
    }

    private void accionCrearLineaGarantia() throws TicketNuevaLineaException {
        log.debug("Accion crear línea de garantía");
        JPrincipal.crearVentanaComprobarAsociarFactura(JGarantia.MODO_GARANTIA);
        // Consultamos la garantia y creamos la linea
        if (JGarantia.getArticuloReferencia() != null) {
            try {
                int numero = 1; // siempre compra 1 garantía en el caso de compra posterior
                Tarifas tarifa = tarifasServices.getTarifaArticulo(JGarantia.getArticuloReferencia().getCodart());
                GarantiaExtendida garantia = new GarantiaExtendida(JGarantia.getArticuloReferencia(), tarifa.getPrecioTotal());
                LineaTicket lineaGarantia = crearLineaArticulo(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS), garantia.getArticuloGarantia(), numero, false);
                garantia.actualizarPreciosGarantiaTicket(lineaGarantia, Sesion.getTicket());
                GarantiaReferencia referencia = new GarantiaReferencia(garantia.getArticuloReferencia(), JGarantia.getTicketOrigen());
                referencia.setLineaOrigen(JGarantia.getLineaReferencia());
                lineaGarantia.setReferenciaGarantia(referencia);

                Sesion.getTicket().recalcularTotales();
                refrescaTablaTicket();
            } catch (ArticuloNotFoundException e) {
                throw new TicketNuevaLineaException("No se ha encontrado el artículo de garantía extendida.", e);
            } catch (ArticuloException e) {
                throw new TicketNuevaLineaException(e.getMessage(), e);
            }
        }
    }

    @Override
    public LineaTicket crearLineaArticulo(String codigoBarras, Articulos art, int numero, boolean compruebaStock) throws TicketNuevaLineaException {
        esArticuloFacturacion = false;
        cantidadFacturacion = 0;
        return crearLineaArticulo(codigoBarras, art, numero, compruebaStock, false);
    }

    public LineaTicket crearLineaArticulo(String codigoBarras, Articulos art, int numero, boolean compruebaStock, boolean esArticuloFacturacion) throws TicketNuevaLineaException {
        log.debug("Acción crear Línea Artículo");
        try {
            if (numero <= 0) {
                return null;
            }
            int stockDisponibleVenta = 0;
            Integer stockDisponibleVentaBodega = null;
            Integer stockDisponibleVentaLocales = null;
            Tarifas tarifa = tarifasServices.getTarifaArticulo(art.getCodart());
            BigDecimal valorItem = BigDecimal.valueOf(numero).multiply(tarifa.getPrecioTotal());
            if (compruebaStock) {
                stockDisponibleVenta = ServicioStock.isStockDisponibleVenta(art);
                int cantidadEnVenta = cantidadItemPorVenta(art);
                if (stockDisponibleVenta < (numero + cantidadEnVenta)) {
                    String valorLimite = Variables.getVariable(Variables.POS_LIMITE_VALOR_STOCK);
                    if (valorItem.compareTo(new BigDecimal(valorLimite)) > 0) {
                        try {
                            stockDisponibleVentaBodega = ServicioStock.consultaDisponibleCD(Integer.parseInt(art.getCodmarca().getCodmarca()), art.getIdItem()).intValue();
                            if (0 >= stockDisponibleVentaBodega) {
                                stockDisponibleVentaLocales = ServicioStock.consultaDisponibleLocales(art.getItmId()).intValue();
                            }

                        } catch (StockException e) {
                            stockDisponibleVentaBodega = null;
                            stockDisponibleVentaLocales = null;
                        }
                        // Pedimos autorización
                        if (0 >= stockDisponibleVentaBodega) {
                            String codAdministrador = ventana_padre.compruebaAutorizacion(Operaciones.STOCK, "Artículo fuera de stock");
                            Sesion.getTicket().setAutorizadorVenta(codAdministrador);
                            ServicioLogAcceso.crearAccesoLogAdmitirFueraStock(codAdministrador, Sesion.getTicket().getIdFactura() + "-" + art.getIdItem());
                        }
                    }
                }
            }
            LineaTicket linea = Sesion.getTicket().crearLineaTicket(codigoBarras, art, numero, tarifa, 0);
            linea.setLineaFacturacion(esArticuloFacturacion);
            linea.setStockDisponible(stockDisponibleVenta);
            if (stockDisponibleVentaBodega != null) {
                linea.setStockDisponibleBodega(stockDisponibleVentaBodega);
            }
            if (stockDisponibleVentaLocales != null) {
                linea.setStockDisponibleLocales(stockDisponibleVentaLocales);
            }
            //  Imagenes.cambiarImagenArticulo(jLabel7, art.getReferenciaInterna());
            //Descuento de clientes nuevos.
            if (auxGarantia == 0L) {
                calculoPromoDescNuevoSocio(art.getCodart());
            }
            return linea;
        } catch (SinPermisosException e) {
            log.debug("No se tiene permisos para añadir el artículo con falta de stock.");
        } catch (StockException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        } catch (ArticuloException e) {
            throw new TicketNuevaLineaException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @author Gabriel Simbania
     * @param articulo
     * @return
     */
    private int cantidadItemPorVenta(Articulos articulo) {

        int cantidad = 0;

        for (LineaTicket lineaTicket : lineasTicket.getLineas()) {
            if (lineaTicket.getArticulo().equals(articulo)) {
                cantidad += lineaTicket.getCantidad();
            }
        }

        return cantidad;
    }

    public void accionCompruebaKitInstalacion(LineaTicket linea) throws TicketNuevaLineaException {
        if (linea.getArticulo().isKitInstalacion()) {
            linea.setKitReferenciaOrigen(UUID.randomUUID().toString());
            KitReferencia referencia = new KitReferencia(linea.getArticulo());
            referencia.setUidReferencia(linea.getKitReferenciaOrigen());
            List<KitBean> codartKits;
            List<KitBean> codartKitsOpcionales;
            try {
                codartKits = ServicioKit.consultarKitsArticulo(referencia.getArticulo(), Boolean.TRUE);
                codartKitsOpcionales = ServicioKit.consultarKitsArticulo(referencia.getArticulo(), Boolean.FALSE);

                if (!codartKits.isEmpty()) {
                    accionAñadeKitIstalacion(referencia, codartKits, Boolean.TRUE);
                }
                if (!codartKitsOpcionales.isEmpty() && JPrincipal.getInstance().crearVentanaConfirmacion("¿Añadir los Items del KIT DE INSTALACIÓN del producto: " + linea.getArticulo().getCodart() + "?")) {
//                KitReferencia referencia = new KitReferencia(linea.getArticulo());
                    codartKits = ServicioKit.consultarKitsArticulo(referencia.getArticulo(), Boolean.FALSE);
                    accionAñadeKitIstalacion(referencia, codartKitsOpcionales, Boolean.FALSE);
                }
            } catch (KitException e) {
                throw new TicketNuevaLineaException("Error consultando kits de instalación en base de datos.", e);
            }
        }
    }

    private void accionAñadeKitIstalacion(KitReferencia referencia, List<KitBean> codartKits, Boolean obligatorio) throws TicketNuevaLineaException {
        try {
            //List<KitBean> codartKits = ServicioKit.consultarKitsArticulo(referencia.getArticulo());
            for (KitBean codArtKit : codartKits) {
                Articulos articuloKit = ArticulosServices.getInstance().getArticuloCod(codArtKit.getCodArtKit());
                String codBarras = ArticulosServices.consultarCodigoBarras(articuloKit.getCodart());
                DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
                datosAdicionales.setEnvioDomicilio(true);
                datosAdicionales.setDescuento(BigDecimal.ZERO);
                LineaTicket linea = crearLineaArticulo(codBarras, articuloKit, 1, false);
                linea.setDatosAdicionales(datosAdicionales);
                linea.setKitObligatorio(obligatorio);
                linea.setReferenciaKit(referencia);
            }
        } catch (ArticuloNotFoundException e) {
            throw new TicketNuevaLineaException("No se ha encontrado el artículo del kit de instalación.", e);
        } catch (ArticuloException e) {
            throw new TicketNuevaLineaException("Error consultando artículos de kits de instalación en base de datos.", e);
        }
    }

    private void accionAsociarFactura() {
        JPrincipal.crearVentanaComprobarAsociarFactura(JGarantia.MODO_KIT);
        if (JGarantia.getArticuloReferencia() != null) {
            try {
                Articulos articuloReferencia = JGarantia.getArticuloReferencia();
                KitReferencia referencia = new KitReferencia(articuloReferencia, JGarantia.getTicketOrigen());
                List<KitBean> codartKits = ServicioKit.consultarKitsArticulo(referencia.getArticulo(), null);
                accionAñadeKitIstalacion(referencia, codartKits, null);
                refrescaTablaTicket();
            } catch (Exception e) {
                log.error(e.getMessage());
                ventana_padre.crearError(e.getMessage());
                refrescaTablaTicket();
                t_introduccionArticulos.requestFocus();
            }
        }
    }

    private void accionCancelarEdicionLinea() {
        resetearFormularioEnvio();
        p_datos_envio.limpiarFormularioInicial();
        t_descuento_valor.setValidacionHabilitada(false);
        v_descuento_linea.setVisible(false);
        t_introduccionArticulos.requestFocus();
        t_descuento_valor.setValidacionHabilitada(true);
        this.clearError();
    }

    @Override
    public void setReferenciaSesionPDA(SesionPdaBean referenciaSesionPDA) {
        Sesion.getTicket().setReferenciaSesionPDA(referenciaSesionPDA);
    }

    @Override
    public boolean esTipoVenta() {
        return true;
    }

    public void estableceFoco() {
        t_introduccionArticulos.requestFocus();
    }

    public void liberarMemoria() {
        p_pagos = null;
        p_buscar_articulo.liberarMemoria();
    }

    public void setCantidadArticulo(int cantidad) {
        cantidadFacturacion = cantidad;
    }

    public void setFacturarArticulo(Boolean facturar) {
        esArticuloFacturacion = facturar;
    }

    public static boolean isNumeric(final String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c != '.') {
                    return false;
                }
            }
        }

        return true;

    }

    public void calculoPromoDescNuevoSocio(String articulo) {
        try {
            Long auxExcluir = 0L;
            BigDecimal encerrarDesc = new BigDecimal(0);
            try {
                promoSocio = ServicioPromocionesClientes.consultarPromoClienteSocio();
            } catch (PromocionException ex) {
                log.error(ex.getMessage());
                ventana_padre.crearError(ex.getMessage());
            }
//            if (auxGarantia == 0L) {
            if (aux == 1L) {
                PromocionTipoDtoManualTotal.activarPromoDiaSocio(encerrarDesc);
                Sesion.getTicket().recalcularTotales();
                refrescaTablaTicketPromo("Se encerra Descuento total Manual", encerrarDesc);
                aux = 0L;
            }
            if (promoSocio.getId() != null) {
                if (articulo != null) {
                    for (String articuloExcluir : promoSocio.getExcluirItems()) {
//                        if (!articuloExcluir.equals(articulo)) {
//                            auxExcluir = auxExcluir + 1;
//                        }else{
//                            auxExcluir
//                        }
                        if (auxGarantia == 0L) {
                            if (!articuloExcluir.equals(articulo)) {
                                BigDecimal condicionValor = new BigDecimal(100);
                                if (Sesion.getTicket().getTotales().getTotalAPagar().compareTo(condicionValor) > 0) {
                                    PromocionTipoDtoManualTotal.activarPromoDiaSocio(encerrarDesc);
                                    Sesion.getTicket().recalcularTotales();
                                    refrescaTablaTicketPromo("Se encerra Descuento total Manual", encerrarDesc);
                                    String total = Sesion.getTicket().getTotales().getTotalAPagar().toString();
                                    BigDecimal desc = promoSocio.getDescuento();
                                    BigDecimal porcDesc = (desc.multiply(Numero.CIEN)).divide(Sesion.getTicket().getTotales().getTotalAPagar(), 8, RoundingMode.HALF_UP);
                                    PromocionTipoDtoManualTotal.activarPromoDiaSocio(porcDesc);
                                    Sesion.getTicket().recalcularTotales();
                                    String motivo = "DESCUENTO GLOBAL";
                                    refrescaTablaTicketPromo(motivo, porcDesc);
                                }
                            } else {
                                PromocionTipoDtoManualTotal.activarPromoDiaSocio(encerrarDesc);
                                Sesion.getTicket().recalcularTotales();
                                refrescaTablaTicketPromo("Se encerra Descuento total Manual", encerrarDesc);
                                ventana_padre.crearInformacion("No aplica a productos del dia del socio..");
                                auxGarantia = 1L;
                            }
                        }

                    }
                }
            }
//            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ventana_padre.crearError(ex.getMessage());
        }
    }

}
