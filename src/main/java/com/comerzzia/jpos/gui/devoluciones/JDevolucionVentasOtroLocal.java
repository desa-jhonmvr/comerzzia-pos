package com.comerzzia.jpos.gui.devoluciones;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.gui.IBusquedasArticulos;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JBuscar;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.eventos.SeleccionaFocusListener;
import com.comerzzia.jpos.gui.modelos.TicketTableCellRenderer;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.modelos.TicketTableModel;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import static com.comerzzia.jpos.servicios.login.Sesion.cajaActual;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.util.EnumTipoDevolucion;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.imagenes.Imagenes;
import es.mpsistemas.util.fechas.Fecha;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Pantalla de Ventas
 *
 * @author MGRI
 */
public class JDevolucionVentasOtroLocal extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError, IBusquedasArticulos {

    private static Logger log = Logger.getMLogger(JDevolucionVentasOtroLocal.class);
    //opciones de configuracion
    boolean esNumeroArticulosEitable = true;  // se consulta de la sesion en la creación del panel
    // servicios y variables
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
    private DocumentoDTO documentoDTO = new DocumentoDTO();
    private Devolucion devolucionPorOtroLocal = null;

    /**
     * Creates new form JDevolucionVentas
     */
    public JDevolucionVentasOtroLocal() {
        super();
        initComponents();
    }

    /**
     * Constructor
     *
     * @param ventana_padre
     */
    public JDevolucionVentasOtroLocal(JPrincipal ventana_padre) {
        super();
        this.ventana_padre = ventana_padre;

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);

        try {
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_ventas.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
        }
        initComponents();

        t_introduccionArticulos.setVisible(false);
        p_autorizando.setVisible(false);

        Imagenes.cambiarImagenPublicidad(jLabel30);
        URL myurl;
        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        v_buscar_articulo.setIconImage(icon.getImage());
        v_seleccion_linea.setIconImage(icon.getImage());
        v_editar_cantidad.setIconImage(icon.getImage());
        f_pagos2.setIconImage(icon.getImage());

        // Inicialización de parámetros especificos de componentes de pantalla
        t_factura.setTableHeader(null);
        t_introduccionArticulos.getT_Codigo().addKeyListener(this);
        sp_t_factura.getViewport().setOpaque(false);
        sp_t_factura.setBorder(null);
        p_buscar_articulo.setVentana_padre(this);
        p_buscar_articulo.setContenedor(v_buscar_articulo);

        p_editar_cantidad.setContenedor(v_editar_cantidad);
        v_editar_cantidad.setLocationRelativeTo(null);

        SeleccionaFocusListener sFL = new SeleccionaFocusListener();

        //como quitar el borde al scrollpanel
        Border empty = new EmptyBorder(0, 0, 0, 0);
        sp_t_factura.setBorder(empty);
        sp_t_factura.setViewportBorder(empty);

        crearAccionFocoTabla(this, t_factura, KeyEvent.VK_T, InputEvent.CTRL_MASK);
        registraEventoBuscar();
    }

    /**
     * Función de Inicialización al cambiar de vista
     *
     */
    @Override
    public void iniciaVista() {
        try {
            addFunctionKeys();
            ActionMap am = v_seleccion_linea.getRootPane().getActionMap();
            InputMap im = v_seleccion_linea.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            documentoDTO = Sesion.getDocumentoDTO();
            lineasTicket.setLineas(new ArrayList<LineaTicket>());
            if (devolucionPorOtroLocal != null && devolucionPorOtroLocal.getTicketDevolucion() != null) {
                devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas().clear();
            }

            BigDecimal total = BigDecimal.ZERO;
            for (ItemDTO item : documentoDTO.getItemDtoLista()) {
                total = total.add(item.getImporteTotalFinal());
            }
            documentoDTO.setTotalFactura(total);

            KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            Action listeneresc = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    cancelarOperacion();
                }
            };

            im.put(esc, "IdentClientesc");
            am.put("IdentClientesc", listeneresc);
            // mostramos el cliente seleccionado

            // Iniciamos la tabla de tickets
            // Data model y renderer de la tabla
            refrescaTablaTicket(true);

            t_venta_cliente.setText(devolucionPorOtroLocal.getTicketDevolucion().getCliente().getNombreVenta());

            t_factura.setModel(new TicketTableModel(devolucionPorOtroLocal.getTicketDevolucion().getLineas()));
            t_factura.setDefaultRenderer(Object.class, new TicketTableCellRenderer());

            // Refrescamos
            List<LineaTicket> lineasTemp = new ArrayList<>();
            for (LineaTicket linea : devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas()) {
                for (ItemDTO item : documentoDTO.getItemDtoLista()) {
                    if (linea.getLineaOriginal() == item.getIdLinea() && linea.getArticulo().getCodart().equals(item.getCodigoI())) {
                        modificarLineaDevolucion(linea, item, item.getCantidad().intValue());
                        linea.setDevuelto(true);
                    }
                }
            }

            for (LineaTicket linea : devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas()) {
                if (linea.isDevuelto()) {
                    lineasTemp.add(linea);
                }
            }

            devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas().clear();
            devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas().addAll(lineasTemp);
            devolucionPorOtroLocal.getTicketDevolucion().recalcularTotales();
            refrescaTablaTicket(false);

            // Foco en introducción de artículos
            t_introduccionArticulos.requestFocus();
            t_introduccionArticulos.reset();
            // comprobamos si el cliente puede subir su nivel de afiliación con esta compra
        } catch (Exception ex) {
            log.error("ERROR: " + ex.getMessage(), ex);
            ventana_padre.crearError("No se puedeo realizar la devolución. " + ex.getMessage());
            return;
        }
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
        v_editar_cantidad = new javax.swing.JDialog();
        p_editar_cantidad = new com.comerzzia.jpos.gui.devoluciones.JIntroducirCantidad();
        jPanel1 = new javax.swing.JPanel();
        p_principal = new javax.swing.JPanel();
        m_ventas = new javax.swing.JPanel();
        b_ventas_datoscliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_pagos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_appal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_seleccion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ventas_edicion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_descuentos = new javax.swing.JPanel();
        p_imagen_airticulo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        p_ingreso_articulos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t_introduccionArticulos = new com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp();
        p_fatura = new javax.swing.JPanel();
        l_total = new javax.swing.JLabel();
        sp_t_factura = new javax.swing.JScrollPane();
        t_factura = new javax.swing.JTable();
        l_v_total = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        l_factura_cabecera1 = new javax.swing.JLabel();
        p_idcliente = new javax.swing.JPanel();
        t_venta_cliente = new javax.swing.JLabel();
        p_publicidad = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        p_autorizando = new javax.swing.JPanel();
        lb_autorizando = new javax.swing.JLabel();

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
        p_descuento_principal.setPreferredSize(new java.awt.Dimension(450, 200));
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

        v_editar_cantidad.setAlwaysOnTop(true);
        v_editar_cantidad.setMinimumSize(new java.awt.Dimension(445, 215));

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
            .addComponent(p_editar_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        b_ventas_datoscliente.setText("<html><center>Cancelar <br>Devolución<br/>F2</center></html>");
        b_ventas_datoscliente.setAlignmentY(0.0F);
        b_ventas_datoscliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_ventas_datoscliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        b_ventas_datoscliente.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_datoscliente.setPreferredSize(new java.awt.Dimension(90, 37));
        b_ventas_datoscliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_datosclienteActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_datoscliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 80, 57));

        b_ventas_pagos.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.pagos").charAt(0));
        b_ventas_pagos.setText("<html><center>Realizar Devolución<br>F9</center></html>");
        b_ventas_pagos.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_pagosActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 160, 57));

        b_ventas_appal.setMnemonic(java.util.ResourceBundle.getBundle("atajosTeclado").getString("tpv.ventas.menu.principal").charAt(0));
        b_ventas_appal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_ventas_appal.setEnabled(false);
        b_ventas_appal.setMargin(new java.awt.Insets(2, 0, 2, 0));
        b_ventas_appal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_appalActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_appal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 80, 57));

        b_ventas_seleccion.setText("<html><center>Selección<br>Artículo<br/>F7</center></html>");
        b_ventas_seleccion.setActionCommand("<html><center>Selección <br>de Artículo<br/>F7</center></html>");
        b_ventas_seleccion.setAlignmentY(0.0F);
        b_ventas_seleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_seleccionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_seleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 80, 57));

        b_ventas_edicion.setText("<html><center>Editar<br>Artículo<br/>F4</center></html>");
        b_ventas_edicion.setActionCommand("<html><center>Edición <br>de Artículo<br/>F4</center></html>");
        b_ventas_edicion.setAlignmentY(0.0F);
        b_ventas_edicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ventas_edicionActionPerformed(evt);
            }
        });
        m_ventas.add(b_ventas_edicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 57));

        p_principal.add(m_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 320, -1));

        p_descuentos.setOpaque(false);
        p_descuentos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
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
        jLabel3.setEnabled(false);

        t_introduccionArticulos.setEnabled(false);
        t_introduccionArticulos.setNextFocusableComponent(b_ventas_datoscliente);
        t_introduccionArticulos.setOpaque(false);

        javax.swing.GroupLayout p_ingreso_articulosLayout = new javax.swing.GroupLayout(p_ingreso_articulos);
        p_ingreso_articulos.setLayout(p_ingreso_articulosLayout);
        p_ingreso_articulosLayout.setHorizontalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        p_ingreso_articulosLayout.setVerticalGroup(
            p_ingreso_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_ingreso_articulosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(t_introduccionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        p_principal.add(p_ingreso_articulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 600, 280, 90));

        p_fatura.setOpaque(false);
        p_fatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        l_v_total.setForeground(new java.awt.Color(0, 0, 204));
        l_v_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_v_total.setText("0");
        l_v_total.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        l_v_total.setFocusable(false);
        p_fatura.add(l_v_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 620, 160, 30));

        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        URL myurl = prefijo.getClass().getResource("/skin/" + prefijo + "/total.png");
        ImageIcon icon = new ImageIcon(myurl);
        jLabel5.setIcon(icon);
        p_fatura.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 600, 260, 80));

        l_factura_cabecera1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        l_factura_cabecera1.setText("Nota de Crédito");
        l_factura_cabecera1.setFocusable(false);
        p_fatura.add(l_factura_cabecera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 130, 20));

        p_principal.add(p_fatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(633, 21, 370, 710));

        p_idcliente.setOpaque(false);
        p_idcliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t_venta_cliente.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        t_venta_cliente.setText("NOMBRE_DE_CLIENTE");
        t_venta_cliente.setFocusable(false);
        p_idcliente.add(t_venta_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 0, -1, -1));

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

        p_autorizando.setBackground(new java.awt.Color(255, 255, 255));
        p_autorizando.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lb_autorizando.setFont(lb_autorizando.getFont().deriveFont(lb_autorizando.getFont().getStyle() | java.awt.Font.BOLD, lb_autorizando.getFont().getSize()+2));
        lb_autorizando.setForeground(new java.awt.Color(0, 102, 255));
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

        p_principal.add(p_autorizando, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, -1, -1));
        p_autorizando.setVisible(false);

        jPanel1.add(p_principal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void b_ventas_datosclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_datosclienteActionPerformed
        log.info("ACCION CANCELAR DEVOLUCION");
        if (ventana_padre.crearVentanaConfirmacion("Esta acción anulará la devolución actual, ¿Desea continuar?")) {
            ventana_padre.showView("ident-cliente");
            Sesion.borrarTicket();
        }
}//GEN-LAST:event_b_ventas_datosclienteActionPerformed

    private void b_ventas_appalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_appalActionPerformed
        // ventana_padre.mostrarMenu("ventas", "DEVOLUCIONES");
}//GEN-LAST:event_b_ventas_appalActionPerformed

    @Override
    public void creaVentanaBusquedaArticulos() {
        log.info("ACCION BUSCAR ARTÍCULO");
        p_buscar_articulo.iniciaVista();
        p_buscar_articulo.iniciaFoco();
        v_buscar_articulo.setLocationRelativeTo(null);
        v_buscar_articulo.setVisible(true);
    }

    /**
     * EVENTO: selección de artículo
     *
     * @param evt
     */
private void b_ventas_seleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_seleccionActionPerformed

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

}//GEN-LAST:event_b_ventas_seleccionActionPerformed

    /**
     * EVENTO: Selección de un elemento de la tabla de ventas a partir de su
     * codigo de barras
     *
     * @param evt
     */
private void b_ventas_pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_pagosActionPerformed
    if (t_factura.getRowCount() > 0) {
        try {
            ventana_padre.showView("ident-cliente");
            EntityManagerFactory emf = Sesion.getEmf();
            EntityManager em = emf.createEntityManager();

            p_autorizando.setVisible(false);
            repintar(p_autorizando);

            documentoDTO.getItemDtoLista().clear();
            for (LineaTicket linea : devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLineas()) {
                documentoDTO.getItemDtoLista().add(new ItemDTO(linea.getArticulo().getCodart(),
                        1l,
                        linea.getCantidad().longValue(),
                        linea.getArticulo().getDesart(),
                        "",
                        linea.getLineaOriginal(),
                        linea.getPrecio(),
                        linea.getPrecioTotal(),
                        linea.getPrecioTarifaOrigen().setScale(2, BigDecimal.ROUND_HALF_UP),
                        linea.getPrecioTotalTarifaOrigen().setScale(2, BigDecimal.ROUND_HALF_UP),
                        "", "", ""));
            }

            DevolucionesServices.generarNotaCreditoOtroLocal(documentoDTO, em);
            p_autorizando.setVisible(false);
        } catch (Exception ex) {
            ventana_padre.crearError("No se pudo realizar la devolución. " + ex.getMessage());
            log.error("Error al crear la devolución: " + ex.getMessage(), ex);
        }
    } else {
        ventana_padre.crearAdvertencia("No se puede realizar una devolución sin artículos.");
    }
}//GEN-LAST:event_b_ventas_pagosActionPerformed

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
                Integer indexLinea = Sesion.getTicketDevolucion().getIndexLinea(codigobarra);
                if (indexLinea != null && t_seleccion_cb.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                } else if (t_seleccion_cb.getText().length() > 0) {
                    lb_error.setText("El código de barras ingresado es incorrecto");
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
                String codigoArticulo = t_seleccion_cb1.getText();
                Integer indexLinea = Sesion.getTicketDevolucion().getIndexLineaCodigoArticulo(codigoArticulo);
                if (indexLinea != null && t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("");
                    v_seleccion_linea.setVisible(false);
                    t_factura.requestFocus();
                    ListSelectionModel selectionModel = t_factura.getSelectionModel();
                    selectionModel.setSelectionInterval(indexLinea.intValue(), indexLinea.intValue());
                } else if (t_seleccion_cb1.getText().length() > 0) {
                    lb_error.setText("El código de artículo ingresado es incorrecto");
                } else {
                    lb_error.setText("No se encontro ningún artículo con el código de artículo");
                    log.debug("No se encontro ningún artículo con el código de artículo " + codigoArticulo);
                }
            }
        } catch (Exception e) {
            log.error("Error al seleccionar línea", e);
        }
    }//GEN-LAST:event_t_seleccion_cb1KeyTyped

    private void b_ventas_edicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ventas_edicionActionPerformed
        log.info("ACCION EDITAR LÍNEA");

        //TODO: Falta edición de la línea
        try {
            lineaSelecionada = t_factura.getSelectedRow();
            if (lineaSelecionada == -1) {
                lineaSelecionada = t_factura.getRowCount() - 1;
            }
            this.indexLineaAEditar = lineaSelecionada;
            if (lineaSelecionada >= 0) {
                ItemDTO itemSeleccionado = documentoDTO.getItemDtoLista().get(indexLineaAEditar);
                LineaTicket lineaTicketSeleccionada = devolucionPorOtroLocal.getTicketDevolucion().getLineas().getLinea(indexLineaAEditar);
                Integer cantidad = lineaTicketSeleccionada.getCantidad();
                p_editar_cantidad.setCantidad(cantidad.intValue());

                v_editar_cantidad.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
                v_editar_cantidad.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
                p_editar_cantidad.iniciaFoco();
                v_editar_cantidad.setVisible(true);

                int nuevaCantidad = p_editar_cantidad.getCantidad();
                if (nuevaCantidad > 0) {
                    // intentamos establecer la cantidad
                    modificarLineaDevolucion(lineaTicketSeleccionada, itemSeleccionado, nuevaCantidad);
                    devolucionPorOtroLocal.getTicketDevolucion().recalcularTotales();
                    refrescaTablaTicket(false);
                } else {
                    ventana_padre.crearAdvertencia("Debe insertar un valor positivo.");
                }
            } else {
                ventana_padre.crearAdvertencia("Debe añadir una línea de ticket para poder editarla.");
            }
        } catch (Exception ex) { //SinPermisos
            ventana_padre.crearError(ex.getMessage());
        }
    }//GEN-LAST:event_b_ventas_edicionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_appal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_datoscliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_edicion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_pagos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ventas_seleccion;
    private javax.swing.JFrame f_pagos2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel l_factura_cabecera1;
    private javax.swing.JLabel l_total;
    private javax.swing.JLabel l_v_total;
    private javax.swing.JLabel lb_autorizando;
    private javax.swing.JLabel lb_error;
    private com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket lineasTicket;
    private javax.swing.JPanel m_ventas;
    private javax.swing.JPanel p_autorizando;
    private com.comerzzia.jpos.gui.JBuscar p_buscar_articulo;
    private javax.swing.JPanel p_descuento_principal;
    private javax.swing.JPanel p_descuentos;
    private com.comerzzia.jpos.gui.devoluciones.JIntroducirCantidad p_editar_cantidad;
    private javax.swing.JPanel p_fatura;
    private javax.swing.JPanel p_idcliente;
    private javax.swing.JPanel p_imagen_airticulo;
    private javax.swing.JPanel p_ingreso_articulos;
    private javax.swing.JPanel p_principal;
    private javax.swing.JPanel p_publicidad;
    private javax.swing.JPanel p_seleccion_cb;
    private javax.swing.JScrollPane sp_t_factura;
    private javax.swing.JTable t_factura;
    private com.comerzzia.jpos.gui.components.ventas.JPanelIntroduccionArticulosComp t_introduccionArticulos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_seleccion_cb1;
    private javax.swing.JLabel t_venta_cliente;
    private javax.swing.JDialog v_buscar_articulo;
    private javax.swing.JDialog v_editar_cantidad;
    private javax.swing.JDialog v_seleccion_linea;
    // End of variables declaration//GEN-END:variables

    protected void cerrarPanelActivo() {
        if (this.p_activo != null) {
            p_activo.setVisible(false);
        }
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
            crearLineaTicket();
        }
    }

    private void accionIntroducirCantidad() {
        log.info("Estableciendo foco en cantidad");
        if (TicketTableCellRenderer.isModoConCantidad()) {
            t_introduccionArticulos.getT_Cantidad().requestFocus();
        }
    }

    private void crearLineaTicket() {

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

    private void refrescaTablaTicket(boolean nuevo) throws Exception {

        if (nuevo) {
            cargarDevolucion();
        }

        l_v_total.setText("$ " + devolucionPorOtroLocal.getTicketDevolucion().getTotales().getTotalAPagar().toString());

        t_factura.setModel(new TicketTableModel(devolucionPorOtroLocal.getTicketDevolucion().getLineas()));

        // Anchos de columnas de la tabla
        t_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int i = 0;
        for (int anchura : TicketTableCellRenderer.getAnchosColumna()) {
            t_factura.getColumnModel().getColumn(i).setPreferredWidth(anchura);
            i++;
        }

    }

    public void addFunctionKeys() {
        // botones de menú de la ventana
        addFuntionKeysBotonesMenu();

        // otros controles
        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    eliminarFila();
                    if (documentoDTO.getTipoDevolucion() != EnumTipoDevolucion.TIPO_DEVOLUCION_DINERO.getValor()) {
                        //t_factura.getse(documentoDTO.getItemDtoLista().size()-1);
                        if (t_factura.getSelectedRow() < 0) {
                            lineaSelecionada = documentoDTO.getItemDtoLista().size() - 1;
                        } else {
                            lineaSelecionada = t_factura.getSelectedRow();
                        }
                        if (lineaSelecionada >= 0) {
                            documentoDTO.getItemDtoLista().remove(lineaSelecionada);
                        } else if (t_factura.getRowCount() > 0) {
                            documentoDTO.getItemDtoLista().remove(lineaSelecionada);
                        }
                        documentoDTO.setTotalFactura(BigDecimal.ZERO);
                        BigDecimal total = BigDecimal.ZERO;
                        for (ItemDTO item : documentoDTO.getItemDtoLista()) {
                            total = total.add(item.getImporteTotalFinal());
                        }
                        documentoDTO.setTotalFactura(total);
                        refrescaTablaTicket(false);
                        // Selección de fila para facilitar eliminaciones consecutivas
                        if (t_factura.getRowCount() > 0 && lineaSelecionada > t_factura.getRowCount() - 1) {
                            t_factura.getSelectionModel().setSelectionInterval(lineaSelecionada - 1, lineaSelecionada - 1);
                        } else if (t_factura.getRowCount() > 0) {
                            t_factura.getSelectionModel().setSelectionInterval(lineaSelecionada, lineaSelecionada);
                        }
                    }
                } catch (Exception ex) {
                    log.error("(Accion Ctrl - ) - Error inesperado eliminando linea de devolución :" + ex.getMessage(), ex);
                }
            }
        };
        addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        Action listenerCtrlO = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                Sesion.getDevolucion().setObservaciones(ventana_padre.crearVentanaObservaciones(Sesion.getDevolucion().getObservaciones()));
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

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_appalActionPerformed(ae);
            }
        };
        addHotKey(f12, "IdentClientF12", listenerf12);

        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_ventas_edicionActionPerformed(ae);
            }
        };
        addHotKey(f4, "IdentClientF4", listenerf4);

    }

    public void setCodigoArticulo(String cb) {
        this.t_introduccionArticulos.getT_Codigo().setText(cb);
        this.t_introduccionArticulos.getT_Codigo().requestFocus();
        crearLineaTicket();
    }

    private void cancelarOperacion() {
        v_seleccion_linea.setVisible(false);
        t_seleccion_cb.setText("");

        lb_error.setText("");
        CardLayout c2 = (CardLayout) (p_descuento_principal.getLayout());
        c2.show(p_descuento_principal, "seleccion_cb");
    }

    private void cargarDevolucion() throws Exception {
        TicketOrigen ticketOrigen;

        ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(documentoDTO.getBlob()));

        Cliente clienteDevolucion = ticketOrigen.getCliente();
        TicketS ticket = new TicketS(cajaActual.getCajaActual().getUidDiarioCaja(), cajaActual.getCajaParcialActual().getUidCajeroCaja(), false);
        ticket.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
        ticket.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorNotaCredito());
        ticket.setFecha(new Fecha());
        ticket.setCliente(clienteDevolucion);
        MotivoDevolucion motivoDevolucion = new MotivoDevolucion(documentoDTO.getMotivoDevolucionDTO().getIdMotivo(), documentoDTO.getMotivoDevolucionDTO().getDescripcionMotivo());
        devolucionPorOtroLocal = new Devolucion(ticketOrigen, ticket, motivoDevolucion, documentoDTO.getEstadoMercaderia(), documentoDTO.getObservacion(), documentoDTO.getTipoDevolucion(), VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

        lineasTicket.setLineas(ticketOrigen.getLineas());

    }

    @Override
    public void addError(ValidationFormException e) {
    }

    @Override
    public void clearError() {
    }

    public JPrincipal getVentana_padre() {
        return this.ventana_padre;
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        //t_introduccionArticulos.requestFocus();
        t_factura.requestFocus();
    }

    public void modificarLineaDevolucion(LineaTicket lineaTicketSeleccionada, ItemDTO item, Integer nuevaCantidad) throws ArticuloNotFoundException, ValidationException {
        LineaTicket lineaOriginal = lineaTicketSeleccionada;
        if (lineaOriginal == null) {
            log.error("modificarLineaDevolucion() - No se ha encontrado la línea original en el ticket");
            throw new ArticuloNotFoundException();
        }
        // Cantidad devuelta
        int numeroArticulosDevueltos = 0;

        int articulosPuedeDevolver = lineaOriginal.getCantidad() - numeroArticulosDevueltos;
        if (nuevaCantidad <= articulosPuedeDevolver) {
            BigDecimal cantidadLineaOrigen = new BigDecimal(lineaOriginal.getCantidad());
            BigDecimal precioFinal = lineaOriginal.getImporteFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
            BigDecimal precioTotalFinal = lineaOriginal.getImporteTotalFinalPagado().divide(cantidadLineaOrigen, 7, RoundingMode.HALF_DOWN);
            lineaTicketSeleccionada.setPrecioTarifaOrigen(precioFinal);
            lineaTicketSeleccionada.setPrecioTotalTarifaOrigen(precioTotalFinal);
            lineaTicketSeleccionada.setCantidad(nuevaCantidad);
            lineaTicketSeleccionada.recalcularImportes();
            lineaTicketSeleccionada.setDescuentoFinalDev(lineaOriginal.getDescuentoFinalDev());
            lineaTicketSeleccionada.redondear();
            devolucionPorOtroLocal.getTicketDevolucion().getTotales().recalcularTotalesLineas(devolucionPorOtroLocal.getTicketDevolucion().getLineas());
            devolucionPorOtroLocal.getTicketDevolucion().getTotales().redondear();
            lineaTicketSeleccionada.setPrecioTarifaOrigen(lineaOriginal.getPrecioTarifaOrigen());
            item.setCantidad(nuevaCantidad.longValue());
            item.setImporteFinal(lineaTicketSeleccionada.getPrecioTarifaOrigen().setScale(2, BigDecimal.ROUND_HALF_UP));
            item.setImporteTotalFinal(lineaTicketSeleccionada.getPrecioTotalTarifaOrigen().setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            throw new ValidationException("La cantidad introducida supera a la cantidad máxima a devolver");
        }
    }

    /**
     *
     * @throws Exception
     */
    private void eliminarFila() throws Exception {
        if (devolucionPorOtroLocal.getTipoDevolucion() != EnumTipoDevolucion.TIPO_DEVOLUCION_DINERO.getValor()) {
            lineaSelecionada = t_factura.getSelectedRow();
            if (lineaSelecionada >= 0) {
                devolucionPorOtroLocal.getTicketDevolucion().eliminarLineaTicket(lineaSelecionada);
            } else if (t_factura.getRowCount() > 0) {
                devolucionPorOtroLocal.getTicketDevolucion().eliminarLineaTicket();
            }
            devolucionPorOtroLocal.recalcularCompensacion();
            refrescaTablaTicket(false);
            // Selección de fila para facilitar eliminaciones consecutivas
            if (t_factura.getRowCount() > 0 && lineaSelecionada > t_factura.getRowCount() - 1) {
                t_factura.getSelectionModel().setSelectionInterval(lineaSelecionada - 1, lineaSelecionada - 1);
            } else if (t_factura.getRowCount() > 0) {
                t_factura.getSelectionModel().setSelectionInterval(lineaSelecionada, lineaSelecionada);
            }
        }
    }

    private void repintar(javax.swing.JComponent componente) {
        Rectangle area = componente.getBounds();
        area.x = 0;
        area.y = 0;
        componente.paintImmediately(area);
    }
}
