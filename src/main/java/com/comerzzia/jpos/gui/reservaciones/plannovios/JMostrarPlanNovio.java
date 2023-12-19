/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JMostrarPlanNovio.java
 *
 * Created on 04-oct-2012, 14:23:17
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioMostrar;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JIngresarValor;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.reservaciones.IPagoImporteCall;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesVentasCanBaby;
import com.comerzzia.jpos.gui.reservaciones.plannovios.modelos.ArticulosPlanesNoviosCellRenderer;
import com.comerzzia.jpos.gui.reservaciones.plannovios.modelos.ArticulosPlanesNoviosTableModel;
import com.comerzzia.jpos.gui.reservaciones.plannovios.modelos.ListadoAbonosPlanCellRenderer;
import com.comerzzia.jpos.gui.reservaciones.plannovios.modelos.ListadoAbonosPlanTableModel;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorEmail;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.gui.validation.ValidadorFecha;
import com.comerzzia.jpos.gui.validation.ValidadorFechaHora;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTelefono;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MGRI
 */
public class JMostrarPlanNovio extends JPanelImagenFondo implements IVista, IViewerValidationFormError, IPagoImporteCall {

    private static int PESTANHA_DATOS_GENERALES = 0;
    private static int PESTANHA_ARTICULOS = 1;
    private static int PESTANHA_ABONOS = 2;
    private static Logger log = Logger.getMLogger(JMostrarPlanNovio.class);
    private static PlanNovioMostrar manejador = null;
    private JPrincipal ventana_padre;
    private List<IValidableForm> formulario;

    /** Creates new form JMostrarPlanNovio */
    public JMostrarPlanNovio() {
        initComponents();

        //Ocultamos el campo direccion
        lb_direccion.setVisible(false);
        t_direccion.setVisible(false);
        lb_numero_invitados.setVisible(false);
        t_numero_inviados.setVisible(false);
        
        ventana_padre = JPrincipal.getInstance();

        // VENTANAS
        URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        v_valor.setIconImage(icon.getImage());
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        try {

            URL myurl2 = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_plan_novios.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl2.getPath())));
        }
        catch (IOException ex) {
            log.error("No se pudo cargar la imagen de fondo de cierre de caja");
        }
        catch (Exception e) {
            log.error("No se pudo cargar la imagen de fondo de cBúsqueda de reservaciones: " + e.getMessage(), e);
        }

      //  Imagenes.cambiarImagenPublicidad(jLabel30);

        v_valor.setLocationRelativeTo(this);
        p_valor.setVentana_padre(ventana_padre);
        p_valor.setContenedor(v_valor);

        v_seleccionar_invitado.setIconImage(icon.getImage());
        v_seleccionar_invitado.setLocationRelativeTo(this);
        p_seleccionar_invitado.setVentana_padre(ventana_padre);
        p_seleccionar_invitado.setContenedor(v_seleccionar_invitado);

        v_liquidacion_parcial.setIconImage(icon.getImage());
        v_liquidacion_parcial.setLocationRelativeTo(null);
        p_liquidacion_parcial.setContenedor(v_liquidacion_parcial);
        p_liquidacion_parcial.setVentana_padre(this.ventana_padre);
        
        v_datos_envio.setIconImage(icon.getImage());
        v_datos_envio.setLocationRelativeTo(null);
        p_datos_envio.setContenedor(v_datos_envio);
        p_datos_envio.setVentana_padre(this.ventana_padre);        

        v_autentificarCliente.setIconImage(icon.getImage());
        p_autentificarCliente.setContenedor(v_autentificarCliente);
        v_autentificarCliente.setLocationRelativeTo(null);

        // TABLA DE ARTÍCULO
        Border empty = new EmptyBorder(0, 0, 0, 0);
        js_tabla_articulos.setViewportBorder(empty);
        tb_articulos.setBorder(empty);
        js_tabla_articulos.getViewport().setOpaque(false);

        js_tb_abonos.setViewportBorder(empty);
        tb_abonos.setBorder(empty);
        js_tb_abonos.getViewport().setOpaque(false);
        tb_abonos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tb_articulos.setDefaultRenderer(Object.class, new ArticulosPlanesNoviosCellRenderer());
        PlanNovioMostrar.setTablaArticulos(tb_articulos);
        //inicializarValidacion();
        //inicializarFormulario();
        setFunctionsKeys();

        inicializarValidacion();
        inicializarFormulario();
        desactivaFormulario();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v_valor = new javax.swing.JDialog();
        p_valor = new com.comerzzia.jpos.gui.reservaciones.JCantidad();
        v_seleccionar_invitado = new javax.swing.JDialog();
        p_seleccionar_invitado = new com.comerzzia.jpos.gui.reservaciones.plannovios.JSeleccionarInvitadoPlan(1);
        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        v_liquidacion_parcial = new javax.swing.JDialog();
        p_liquidacion_parcial = new com.comerzzia.jpos.gui.reservaciones.plannovios.JPlanLiquidacionParcial();
        v_autentificarCliente = new javax.swing.JDialog();
        p_autentificarCliente = new com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente();
        v_datos_envio = new javax.swing.JDialog();
        p_datos_envio = new com.comerzzia.jpos.gui.JDatosEnvio();
        b_abonos_propios = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_comprar_articulo = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_liquidar_reserva = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_menu_ppal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_add_articulos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        tp_panel = new javax.swing.JTabbedPane();
        tab_datos_generales = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        l_nombre_novia = new javax.swing.JLabel();
        l_apellidos_novia = new javax.swing.JLabel();
        l_cedula_novia = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        l_cedula_novio = new javax.swing.JLabel();
        l_apellidos_novio = new javax.swing.JLabel();
        l_nombre_novio = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        t_telefono_contacto = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        t_emails = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_direccion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_direccion = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        t_fecha_hora_boda = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_lugar_boda = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        t_numero_inviados = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_numero_invitados = new javax.swing.JLabel();
        t_fecha_contacto_invitados = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel31 = new javax.swing.JLabel();
        lb_boda = new javax.swing.JLabel();
        tab_articulos = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        jLabel19 = new javax.swing.JLabel();
        js_tabla_articulos = new javax.swing.JScrollPane();
        tb_articulos = new javax.swing.JTable();
        tab_abonos = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        t_comprado = new javax.swing.JTextField();
        lb_porAbonar = new javax.swing.JLabel();
        t_n_abonos = new javax.swing.JTextField();
        JTipoReservacion1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        t_total_reserva = new javax.swing.JTextField();
        lb_abonos_disponibles = new javax.swing.JLabel();
        t_total_abonado = new javax.swing.JTextField();
        lb_abono_realizado = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        t_por_abonar = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        t_restante_abonos = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        js_tb_abonos = new javax.swing.JScrollPane();
        tb_abonos = new javax.swing.JTable();
        JTipoReservacion = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lb_fecha_alta = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lb_fecha_fin = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lb_estado = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lb_num_plan = new javax.swing.JLabel();
        b_gestionar_invitados = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_editar_plan = new com.comerzzia.jpos.gui.components.form.JButtonForm();

        v_valor.setMinimumSize(new java.awt.Dimension(380, 140));
        v_valor.setModal(true);

        javax.swing.GroupLayout v_valorLayout = new javax.swing.GroupLayout(v_valor.getContentPane());
        v_valor.getContentPane().setLayout(v_valorLayout);
        v_valorLayout.setHorizontalGroup(
            v_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_valorLayout.createSequentialGroup()
                .addComponent(p_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_valorLayout.setVerticalGroup(
            v_valorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_valorLayout.createSequentialGroup()
                .addComponent(p_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_seleccionar_invitado.setAlwaysOnTop(true);
        v_seleccionar_invitado.setMinimumSize(new java.awt.Dimension(719, 460));
        v_seleccionar_invitado.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_seleccionar_invitado.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_seleccionar_invitado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v_seleccionar_invitadoFocusGained(evt);
            }
        });

        javax.swing.GroupLayout v_seleccionar_invitadoLayout = new javax.swing.GroupLayout(v_seleccionar_invitado.getContentPane());
        v_seleccionar_invitado.getContentPane().setLayout(v_seleccionar_invitadoLayout);
        v_seleccionar_invitadoLayout.setHorizontalGroup(
            v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
            .addGroup(v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_seleccionar_invitadoLayout.createSequentialGroup()
                    .addGap(0, 10, Short.MAX_VALUE)
                    .addComponent(p_seleccionar_invitado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
        );
        v_seleccionar_invitadoLayout.setVerticalGroup(
            v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
            .addGroup(v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_seleccionar_invitadoLayout.createSequentialGroup()
                    .addGap(0, 14, Short.MAX_VALUE)
                    .addComponent(p_seleccionar_invitado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 15, Short.MAX_VALUE)))
        );

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

        v_liquidacion_parcial.setMinimumSize(new java.awt.Dimension(780, 450));
        v_liquidacion_parcial.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_liquidacion_parcial.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        javax.swing.GroupLayout v_liquidacion_parcialLayout = new javax.swing.GroupLayout(v_liquidacion_parcial.getContentPane());
        v_liquidacion_parcial.getContentPane().setLayout(v_liquidacion_parcialLayout);
        v_liquidacion_parcialLayout.setHorizontalGroup(
            v_liquidacion_parcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_liquidacion_parcialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_liquidacion_parcial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_liquidacion_parcialLayout.setVerticalGroup(
            v_liquidacion_parcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_liquidacion_parcialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(p_liquidacion_parcial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

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

        v_datos_envio.setMinimumSize(new java.awt.Dimension(400, 498));
        v_datos_envio.setModal(true);

        javax.swing.GroupLayout v_datos_envioLayout = new javax.swing.GroupLayout(v_datos_envio.getContentPane());
        v_datos_envio.getContentPane().setLayout(v_datos_envioLayout);
        v_datos_envioLayout.setHorizontalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_datos_envioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_datos_envioLayout.setVerticalGroup(
            v_datos_envioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_datos_envioLayout.createSequentialGroup()
                .addComponent(p_datos_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        b_abonos_propios.setText("<html><center>Realizar<br>Abono<br>F3</center></html>");
        b_abonos_propios.setPreferredSize(new java.awt.Dimension(127, 48));
        b_abonos_propios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_abonos_propiosActionPerformed(evt);
            }
        });

        b_comprar_articulo.setText("<html><center>Comprar<br>Artículo<br>F4 </center></html>");
        b_comprar_articulo.setPreferredSize(new java.awt.Dimension(127, 48));
        b_comprar_articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_comprar_articuloActionPerformed(evt);
            }
        });

        b_liquidar_reserva.setText("<html><center>Liquidar reservación<br>F5</center></html>");
        b_liquidar_reserva.setMargin(new java.awt.Insets(0, -3, 0, -1));
        b_liquidar_reserva.setPreferredSize(new java.awt.Dimension(127, 48));
        b_liquidar_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_liquidar_reservaActionPerformed(evt);
            }
        });

        b_menu_ppal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_menu_ppal.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_menu_ppal.setNextFocusableComponent(tp_panel);
        b_menu_ppal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menu_ppalActionPerformed(evt);
            }
        });

        b_add_articulos.setText("<html><center>Añadir Artículos<br/>F8</center></html>");
        b_add_articulos.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_add_articulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_add_articulosActionPerformed(evt);
            }
        });

        tp_panel.setMaximumSize(new java.awt.Dimension(1080, 630));

        tab_datos_generales.setMaximumSize(new java.awt.Dimension(1080, 600));
        tab_datos_generales.setMinimumSize(new java.awt.Dimension(1080, 600));
        tab_datos_generales.setOpaque(false);
        tab_datos_generales.setPreferredSize(new java.awt.Dimension(1080, 600));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DATOS DE LA NOVIA");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("DATOS DEL NOVIO");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("DATOS DE LA BODA");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setOpaque(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombres:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Apellidos:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Cédula:");

        l_nombre_novia.setFont(l_nombre_novia.getFont().deriveFont(l_nombre_novia.getFont().getSize()+2f));
        l_nombre_novia.setForeground(new java.awt.Color(51, 153, 255));

        l_apellidos_novia.setFont(l_apellidos_novia.getFont().deriveFont(l_apellidos_novia.getFont().getSize()+2f));
        l_apellidos_novia.setForeground(new java.awt.Color(51, 153, 255));

        l_cedula_novia.setFont(l_cedula_novia.getFont().deriveFont(l_cedula_novia.getFont().getSize()+2f));
        l_cedula_novia.setForeground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(l_apellidos_novia, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l_nombre_novia, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(l_cedula_novia, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_nombre_novia, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_apellidos_novia, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_cedula_novia, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(451, 126));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Cédula:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Apellidos:");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Nombres:");

        l_cedula_novio.setFont(l_cedula_novio.getFont().deriveFont(l_cedula_novio.getFont().getSize()+2f));
        l_cedula_novio.setForeground(new java.awt.Color(51, 153, 255));

        l_apellidos_novio.setFont(l_apellidos_novio.getFont().deriveFont(l_apellidos_novio.getFont().getSize()+2f));
        l_apellidos_novio.setForeground(new java.awt.Color(51, 153, 255));

        l_nombre_novio.setFont(l_nombre_novio.getFont().deriveFont(l_nombre_novio.getFont().getSize()+2f));
        l_nombre_novio.setForeground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(l_apellidos_novio, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_nombre_novio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(l_cedula_novio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_nombre_novio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_apellidos_novio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(l_cedula_novio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setOpaque(false);

        t_telefono_contacto.setEditable(false);
        t_telefono_contacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_telefono_contactoActionPerformed(evt);
            }
        });

        jLabel17.setDisplayedMnemonic('t');
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Teléfono:");

        jLabel18.setDisplayedMnemonic('e');
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Email:");

        t_emails.setEditable(false);

        t_direccion.setEditable(false);

        lb_direccion.setDisplayedMnemonic('d');
        lb_direccion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_direccion.setText("Dirección:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_direccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_telefono_contacto, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_emails, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(246, 246, 246))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_telefono_contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_emails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setOpaque(false);

        t_fecha_hora_boda.setEditable(false);

        t_lugar_boda.setEditable(false);

        jLabel26.setDisplayedMnemonic('l');
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Lugar de la Boda:");

        jLabel27.setDisplayedMnemonic('f');
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Fecha y Hora de la Boda:");

        t_numero_inviados.setEditable(false);

        lb_numero_invitados.setDisplayedMnemonic('n');
        lb_numero_invitados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_numero_invitados.setText("Número de invitados:");

        t_fecha_contacto_invitados.setEditable(false);

        jLabel31.setDisplayedMnemonic('i');
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Fecha para contactar Invitados:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_numero_invitados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_fecha_hora_boda, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_lugar_boda, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(t_fecha_contacto_invitados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addComponent(t_numero_inviados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                .addGap(152, 152, 152))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_lugar_boda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_hora_boda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_numero_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_numero_inviados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_contacto_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lb_boda.setFont(lb_boda.getFont().deriveFont(lb_boda.getFont().getStyle() | java.awt.Font.BOLD, lb_boda.getFont().getSize()+2));
        lb_boda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout tab_datos_generalesLayout = new javax.swing.GroupLayout(tab_datos_generales);
        tab_datos_generales.setLayout(tab_datos_generalesLayout);
        tab_datos_generalesLayout.setHorizontalGroup(
            tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 904, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6))
                        .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_boda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(1080, Short.MAX_VALUE))
        );
        tab_datos_generalesLayout.setVerticalGroup(
            tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_boda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );

        tp_panel.addTab("<html><u>D</u>ATOS GENERALES</html>", tab_datos_generales);

        tab_articulos.setMaximumSize(new java.awt.Dimension(1030, 600));
        tab_articulos.setOpaque(false);
        tab_articulos.setPreferredSize(new java.awt.Dimension(1030, 600));

        jLabel19.setFont(jLabel19.getFont().deriveFont(jLabel19.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel19.setText("Artículos");

        tb_articulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "CÓD BARRAS", "DESCRIPCIÓN", "RESERVADA", "COMPRADA", "ESTADO"
            }
        ));
        tb_articulos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tb_articulos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_articulosKeyPressed(evt);
            }
        });
        js_tabla_articulos.setViewportView(tb_articulos);

        javax.swing.GroupLayout tab_articulosLayout = new javax.swing.GroupLayout(tab_articulos);
        tab_articulos.setLayout(tab_articulosLayout);
        tab_articulosLayout.setHorizontalGroup(
            tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_articulosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(js_tabla_articulos, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(1214, Short.MAX_VALUE))
        );
        tab_articulosLayout.setVerticalGroup(
            tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_articulosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(js_tabla_articulos, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        tp_panel.addTab("<html><u>A</u>RTICULOS</html>", tab_articulos);

        tab_abonos.setMaximumSize(new java.awt.Dimension(1024, 500));
        tab_abonos.setOpaque(false);
        tab_abonos.setPreferredSize(new java.awt.Dimension(1024, 500));

        t_comprado.setText("jTextField1");
        t_comprado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_comprado.setEnabled(false);

        lb_porAbonar.setFont(lb_porAbonar.getFont().deriveFont(lb_porAbonar.getFont().getStyle() | java.awt.Font.BOLD));
        lb_porAbonar.setForeground(new java.awt.Color(51, 153, 255));
        lb_porAbonar.setText("Ab. disponibles local actual:");

        t_n_abonos.setText("jTextField1");
        t_n_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_n_abonos.setEnabled(false);

        JTipoReservacion1.setFont(JTipoReservacion1.getFont().deriveFont(JTipoReservacion1.getFont().getStyle() | java.awt.Font.BOLD, 18));
        JTipoReservacion1.setText("Abonos");

        jLabel32.setFont(jLabel32.getFont().deriveFont(jLabel32.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel32.setForeground(new java.awt.Color(51, 153, 255));
        jLabel32.setText("Nº abonos:");

        t_total_reserva.setText("jTextField1");
        t_total_reserva.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_reserva.setEnabled(false);

        lb_abonos_disponibles.setFont(lb_abonos_disponibles.getFont().deriveFont(lb_abonos_disponibles.getFont().getStyle() | java.awt.Font.BOLD));
        lb_abonos_disponibles.setForeground(new java.awt.Color(51, 153, 255));
        lb_abonos_disponibles.setText("Tot. abonos local actual:");

        t_total_abonado.setText("jTextField1");
        t_total_abonado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_abonado.setEnabled(false);

        lb_abono_realizado.setFont(lb_abono_realizado.getFont().deriveFont(lb_abono_realizado.getFont().getSize()-1f));
        lb_abono_realizado.setText("( Real )");

        jLabel24.setFont(jLabel24.getFont().deriveFont(jLabel24.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel24.setForeground(new java.awt.Color(51, 153, 255));
        jLabel24.setText("Total reservación:");

        t_por_abonar.setText("jTextField1");
        t_por_abonar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_por_abonar.setEnabled(false);

        jLabel25.setFont(jLabel25.getFont().deriveFont(jLabel25.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel25.setForeground(new java.awt.Color(51, 153, 255));
        jLabel25.setText("Total comprado:");

        t_restante_abonos.setText("jTextField1");
        t_restante_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_restante_abonos.setEnabled(false);

        jLabel35.setFont(jLabel35.getFont().deriveFont(jLabel35.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel35.setForeground(new java.awt.Color(51, 153, 255));
        jLabel35.setText("Total abonos:");

        tb_abonos.setDefaultRenderer(Object.class, new ListadoAbonosPlanCellRenderer());
        tb_abonos.setModel(new ListadoAbonosPlanTableModel(new LinkedList()));
        tb_abonos.setMinimumSize(new java.awt.Dimension(0, 660));
        tb_abonos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_abonosKeyPressed(evt);
            }
        });
        js_tb_abonos.setViewportView(tb_abonos);

        javax.swing.GroupLayout tab_abonosLayout = new javax.swing.GroupLayout(tab_abonos);
        tab_abonos.setLayout(tab_abonosLayout);
        tab_abonosLayout.setHorizontalGroup(
            tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_abonosLayout.createSequentialGroup()
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(js_tb_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tab_abonosLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTipoReservacion1)
                            .addGroup(tab_abonosLayout.createSequentialGroup()
                                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tab_abonosLayout.createSequentialGroup()
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(t_total_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(t_n_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(lb_porAbonar))
                                    .addGroup(tab_abonosLayout.createSequentialGroup()
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(t_comprado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(t_total_abonado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(tab_abonosLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(lb_abono_realizado, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(tab_abonosLayout.createSequentialGroup()
                                        .addGap(520, 520, 520)
                                        .addComponent(lb_abonos_disponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(t_por_abonar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_restante_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(15, 15, 15)))
                .addContainerGap(1145, Short.MAX_VALUE))
        );
        tab_abonosLayout.setVerticalGroup(
            tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_abonosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(JTipoReservacion1)
                .addGap(38, 38, 38)
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_total_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_n_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_porAbonar)
                        .addComponent(t_restante_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel32))))
                .addGap(12, 12, 12)
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel25))
                    .addComponent(t_comprado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel35))
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lb_abono_realizado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_total_abonado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_abonos_disponibles)
                        .addComponent(t_por_abonar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(js_tb_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        tp_panel.addTab("<html>A<u>B</u>ONOS</html>", tab_abonos);

        JTipoReservacion.setFont(JTipoReservacion.getFont().deriveFont(JTipoReservacion.getFont().getStyle() | java.awt.Font.BOLD, 18));
        JTipoReservacion.setText("Datos de cabecera del plan");

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel10.setForeground(new java.awt.Color(51, 153, 255));
        jLabel10.setText("Fecha de Alta:");

        lb_fecha_alta.setText("jLabel10");

        jLabel28.setFont(jLabel28.getFont().deriveFont(jLabel28.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel28.setForeground(new java.awt.Color(51, 153, 255));
        jLabel28.setText("Fecha Fin:");

        lb_fecha_fin.setText("jLabel10");

        jLabel33.setFont(jLabel33.getFont().deriveFont(jLabel33.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel33.setForeground(new java.awt.Color(51, 153, 255));
        jLabel33.setText("Estado:");

        lb_estado.setFont(lb_estado.getFont().deriveFont(lb_estado.getFont().getStyle() | java.awt.Font.BOLD));
        lb_estado.setText("Estado");

        jLabel34.setFont(jLabel34.getFont().deriveFont(jLabel34.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel34.setForeground(new java.awt.Color(51, 153, 255));
        jLabel34.setText("Identificador de plan:");

        lb_num_plan.setFont(lb_num_plan.getFont().deriveFont(lb_num_plan.getFont().getStyle() | java.awt.Font.BOLD));
        lb_num_plan.setText("NumRes");

        b_gestionar_invitados.setText("<html><center>Gestionar<br>Invitados<br>F2</center></html>");
        b_gestionar_invitados.setPreferredSize(new java.awt.Dimension(127, 48));
        b_gestionar_invitados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_gestionar_invitadosActionPerformed(evt);
            }
        });

        b_editar_plan.setText("<html><center>Editar<br>Plan<br>F1</center></html>");
        b_editar_plan.setPreferredSize(new java.awt.Dimension(127, 48));
        b_editar_plan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_editar_planActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b_editar_plan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_gestionar_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_abonos_propios, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_comprar_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_liquidar_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_add_articulos, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menu_ppal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tp_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 2007, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTipoReservacion)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(lb_fecha_alta, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(lb_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(lb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lb_num_plan, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(1205, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(JTipoReservacion)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel28)
                    .addComponent(lb_fecha_fin)
                    .addComponent(jLabel33)
                    .addComponent(lb_estado)
                    .addComponent(jLabel34)
                    .addComponent(lb_num_plan)
                    .addComponent(lb_fecha_alta))
                .addGap(18, 18, 18)
                .addComponent(tp_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_menu_ppal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_add_articulos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_liquidar_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_comprar_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_abonos_propios, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_gestionar_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_editar_plan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(452, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_abonos_propiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_abonos_propiosActionPerformed
        accionRealizarAbono();
    }//GEN-LAST:event_b_abonos_propiosActionPerformed

    private void b_comprar_articuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_comprar_articuloActionPerformed
        accionComprarArticulo();
    }//GEN-LAST:event_b_comprar_articuloActionPerformed

    private void b_liquidar_reservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_liquidar_reservaActionPerformed
        accionRematarReservacion();
}//GEN-LAST:event_b_liquidar_reservaActionPerformed

    private void b_menu_ppalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menu_ppalActionPerformed
        accionMenu();
}//GEN-LAST:event_b_menu_ppalActionPerformed

    private void b_add_articulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_add_articulosActionPerformed
        accionAddArticulos();
}//GEN-LAST:event_b_add_articulosActionPerformed

    private void tb_articulosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_articulosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
            tb_articulos.transferFocusBackward();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            tb_articulos.transferFocus();
        }
    }//GEN-LAST:event_tb_articulosKeyPressed

    private void b_gestionar_invitadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_gestionar_invitadosActionPerformed
        accionSeleccionarInvitado();
    }//GEN-LAST:event_b_gestionar_invitadosActionPerformed

    private void t_telefono_contactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_telefono_contactoActionPerformed
}//GEN-LAST:event_t_telefono_contactoActionPerformed

    private void v_seleccionar_invitadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_seleccionar_invitadoFocusGained
        p_seleccionar_invitado.iniciaVista();
}//GEN-LAST:event_v_seleccionar_invitadoFocusGained

    private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
        p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

    private void b_editar_planActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_editar_planActionPerformed
        accionEdiarPlan();
    }//GEN-LAST:event_b_editar_planActionPerformed

    private void tb_abonosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_abonosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
            tb_abonos.transferFocusBackward();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            tb_abonos.transferFocus();
        }
}//GEN-LAST:event_tb_abonosKeyPressed

    private void v_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_autentificarClienteFocusGained
        p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_v_autentificarClienteFocusGained

    private void p_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_autentificarClienteFocusGained
        p_autentificarCliente.iniciaFoco();
    }//GEN-LAST:event_p_autentificarClienteFocusGained

    private void v_autentificarClienteWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_autentificarClienteWindowGainedFocus
        p_autentificarCliente.iniciaFoco();
    }//GEN-LAST:event_v_autentificarClienteWindowGainedFocus
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JTipoReservacion;
    private javax.swing.JLabel JTipoReservacion1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_abonos_propios;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_add_articulos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_comprar_articulo;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_editar_plan;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_gestionar_invitados;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_liquidar_reserva;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_menu_ppal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane js_tabla_articulos;
    private javax.swing.JScrollPane js_tb_abonos;
    private javax.swing.JLabel l_apellidos_novia;
    private javax.swing.JLabel l_apellidos_novio;
    private javax.swing.JLabel l_cedula_novia;
    private javax.swing.JLabel l_cedula_novio;
    private javax.swing.JLabel l_nombre_novia;
    private javax.swing.JLabel l_nombre_novio;
    private javax.swing.JLabel lb_abono_realizado;
    private javax.swing.JLabel lb_abonos_disponibles;
    private javax.swing.JLabel lb_boda;
    private javax.swing.JLabel lb_direccion;
    private javax.swing.JLabel lb_estado;
    private javax.swing.JLabel lb_fecha_alta;
    private javax.swing.JLabel lb_fecha_fin;
    private javax.swing.JLabel lb_num_plan;
    private javax.swing.JLabel lb_numero_invitados;
    private javax.swing.JLabel lb_porAbonar;
    private com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente p_autentificarCliente;
    private com.comerzzia.jpos.gui.JDatosEnvio p_datos_envio;
    private com.comerzzia.jpos.gui.reservaciones.plannovios.JPlanLiquidacionParcial p_liquidacion_parcial;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV p_pagos;
    private com.comerzzia.jpos.gui.reservaciones.plannovios.JSeleccionarInvitadoPlan p_seleccionar_invitado;
    private com.comerzzia.jpos.gui.reservaciones.JCantidad p_valor;
    private javax.swing.JTextField t_comprado;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_direccion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_emails;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fecha_contacto_invitados;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fecha_hora_boda;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_lugar_boda;
    private javax.swing.JTextField t_n_abonos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_numero_inviados;
    private javax.swing.JTextField t_por_abonar;
    private javax.swing.JTextField t_restante_abonos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_telefono_contacto;
    private javax.swing.JTextField t_total_abonado;
    private javax.swing.JTextField t_total_reserva;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_abonos;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_articulos;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_datos_generales;
    private javax.swing.JTable tb_abonos;
    private javax.swing.JTable tb_articulos;
    private javax.swing.JTabbedPane tp_panel;
    private javax.swing.JDialog v_autentificarCliente;
    private javax.swing.JDialog v_datos_envio;
    private javax.swing.JDialog v_liquidacion_parcial;
    private javax.swing.JDialog v_pagos;
    private javax.swing.JDialog v_seleccionar_invitado;
    private javax.swing.JDialog v_valor;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        log.debug("iniciaVista()");
        iniciaVista(PESTANHA_DATOS_GENERALES);
    }

    public void iniciaVista(int pestanha) {
        log.debug("iniciaVista() pestaña:" + pestanha);
        tp_panel.setSelectedIndex(pestanha);
        // Aquí iniciaVista ha de recibir el objeto del Plan noivios        
        manejador = PlanNovioMostrar.inicia();
        manejador.setManejadorErrores(this);
        manejador.setPantallaMostrarPlan(this);
        // Establecemos usuario propietario del plan y posibilidad de realizar todas las operaciones sobre este
        manejador.accionSeleccionarComoPropietario();
        
        // Lectura de los datos del novio y la novia
        PlanNovioMostrar.estableceDatosNovios(l_nombre_novia, l_apellidos_novia, l_cedula_novia, l_nombre_novio, l_apellidos_novio, l_cedula_novio, lb_boda);
        PlanNovioMostrar.estableceDatosAdicionales(t_lugar_boda, t_fecha_hora_boda, t_fecha_contacto_invitados,t_telefono_contacto,t_emails);
        PlanNovioMostrar.estableceDatosCabecera(lb_fecha_alta, lb_fecha_fin, lb_estado, lb_num_plan);
        refrescar();

        if (manejador.isModoLiquidado()) {
            b_abonos_propios.setEnabled(false);
            b_add_articulos.setEnabled(false);
            b_comprar_articulo.setEnabled(false);
            b_editar_plan.setEnabled(false);
            b_liquidar_reserva.setEnabled(false);
            b_gestionar_invitados.setEnabled(false);
        }
        else if (manejador.isModoCaducado()) {
            b_abonos_propios.setEnabled(false);
            b_add_articulos.setEnabled(false);
            b_comprar_articulo.setEnabled(false);
            if(manejador.getPertenecePlanTienda()){
                b_editar_plan.setEnabled(true);
            }
            else{
                b_editar_plan.setEnabled(false);
            }
            b_liquidar_reserva.setEnabled(false);
            b_gestionar_invitados.setEnabled(false);

            try {
                if (ventana_padre.crearVentanaConfirmacion("¿Desea Ampliar la fecha de caducidad de la reserva?")) {
                    String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);

                    Object res = JPrincipal.crearVentanaIngresarValor(JIngresarValor.TIPO_NUMERICO, "<html>Indique los días de vigencia<br> a partir de Hoy</html>");
                    if (res != null) {
                        manejador.accionAmpliarReserva(((BigDecimal)res).intValue());
                        iniciaVista();
                    }
                }
            }
            catch (SinPermisosException e) {
            }
            catch (PlanNovioException e) {
                ventana_padre.crearError(e.getMessage());
            }
            catch (Exception e) {
                log.error("iniciaVista()- " + e.getMessage(), e);
                ventana_padre.crearError(null);
            }
        }
        else {
            if(manejador.getPertenecePlanTienda()){
                b_add_articulos.setEnabled(true);
                b_comprar_articulo.setEnabled(true);
                b_editar_plan.setEnabled(true);
                b_liquidar_reserva.setEnabled(true);
                b_gestionar_invitados.setEnabled(true);
                //true provisional
                b_abonos_propios.setEnabled(true);
            }
            else{
                b_add_articulos.setEnabled(false);
                b_comprar_articulo.setEnabled(false);
                b_editar_plan.setEnabled(false);
                b_liquidar_reserva.setEnabled(true);
                b_gestionar_invitados.setEnabled(true);
                //false provisional
                b_abonos_propios.setEnabled(true);
            }
        }

        iniciaFoco();

    }

    @Override
    public void iniciaFoco() {
        if (b_gestionar_invitados.isEnabled()){
            b_gestionar_invitados.requestFocus();
        }
        else{
            b_menu_ppal.requestFocus();
        }
    }

    @Override
    public void addError(ValidationFormException e) {
        ventana_padre.crearError(e.getMessage());
    }

    @Override
    public void clearError() {
    }

    private void setFunctionsKeys() {

        // ACCIONES DE PESTAÑA
        // ALT+D
        KeyStroke altd = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK);
        Action listeneraltd = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tp_panel.setSelectedIndex(0);
            }
        };
        addHotKey(altd, "IdentClientaltd", listeneraltd);
        // ALT+A
        KeyStroke alta = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK);
        Action listeneralta = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tp_panel.setSelectedIndex(1);
            }
        };
        addHotKey(alta, "IdentClientaltv", listeneralta);

        // ALT+B
        KeyStroke altb = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_MASK);
        Action listeneraltb = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tp_panel.setSelectedIndex(2);
            }
        };
        addHotKey(altb, "IdentClientaltb", listeneraltb);

        // EDITAR PLAN
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        Action listenerf1 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionEdiarPlan();
            }
        };
        addHotKey(f1, "ResF1PlanNovio", listenerf1);


        // GESTIONAR INVITADOS
        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionSeleccionarInvitado();
            }
        };
        addHotKey(f2, "ResF2PlanNovio", listenerf2);


        // REALIZAR ABONO
        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        Action listenerf3 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionRealizarAbono();
            }
        };
        addHotKey(f3, "ResF3PlanNovio", listenerf3);


        // AÑADIR ARTICULOS
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionComprarArticulo();
            }
        };
        addHotKey(f4, "ResF4PlanNovio", listenerf4);

        // REMATAR RESERVACIÓN
        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        Action listenerf5 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionRematarReservacion();
            }
        };
        addHotKey(f5, "ResF5PlanNovio", listenerf5);

        // AÑADIR ARTICULOS

        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        Action listenerf8 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionAddArticulos();
            }
        };
        addHotKey(f8, "ResF8PlanNovio", listenerf8);

        // ELIMINAR ARTICULOS

        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionEliminarArticulo();
            }
        };
        addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);

        // Accion Especial para tabla que dependerá de la pestaña activa
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK);
        Action listenerk = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JTable cmp = null;
                if (tp_panel.getSelectedIndex() == PESTANHA_ABONOS) {
                    cmp = tb_abonos;
                }
                else if (tp_panel.getSelectedIndex() == PESTANHA_ARTICULOS) {
                    cmp = tb_articulos;
                }
                else {
                    return;
                }

                if (cmp.getRowCount() >= 0) {
                    ListSelectionModel selectionModel = cmp.getSelectionModel();
                    selectionModel.setSelectionInterval(0, 0);
                }
                cmp.requestFocus();
            }
        };
        addHotKey(ks, "SelecciondeLineaTabla", listenerk);

        // ACCIÓN PARA MENU
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_menu_ppalActionPerformed(ae);
            }
        };
        addHotKey(f12, "ResF12", listenerf12);
    }

    private void accionMenu() {
        try {
            ventana_padre.mostrarMenu();
        }
        catch (Exception e) {
            log.error("No se pudo mostrar el menú desde el detalle del Plan Novios", e);
            ventana_padre.crearError(null);
        }
    }

    private void accionAddArticulos() {
        try {
            if (b_add_articulos.isEnabled()) {
                if (compruebaUsuarioPropietario()) {
                    // Creamos una ventana de ventas en modo ADD               
                    if (compruebaUsuarioPropietario()) {
                        ventana_padre.irPagoArticulos(PlanNovioMostrar.getPlanNovio(), null, JReservacionesVentasCanBaby.MODO_ADD_PLAN_NOVIOS);
                    }
                }
            }
        }
        catch (SinPermisosException e) {
            ventana_padre.crearAdvertencia(e.getMessage());
        }
        catch (Exception e) {
            log.error("Error añadiendo artículos al plan novioa: " + e.getMessage(), e);
            ventana_padre.crearError("Error añadiendo artículos al plan ");
        }
    }

    private void accionEliminarArticulo() {
        try {
            String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);
            int lineaSeleccionada;
            log.debug("Inicio operación De eliminación de artículo del Plan Novios");
            if (compruebaUsuarioPropietario()) {
                if (tb_articulos.getSelectedRow() >= 0) {
                    lineaSeleccionada = tb_articulos.getSelectedRow();
                }
                else {
                    lineaSeleccionada = manejador.getUltimaLinea();
                }
                if (lineaSeleccionada >= 0 && ventana_padre.crearVentanaConfirmacion("Esta acción Eliminará el artículo del Plan Novios, ¿Desea continuar?")) {
                    manejador.removeArticulosReserva(lineaSeleccionada);
                    iniciaVista(PESTANHA_ARTICULOS);
                }
            }
        }
        catch (SinPermisosException ex) {
            log.warn("No se tiene permisos para anular ");
        }
    }

    private boolean compruebaUsuarioPropietario() throws SinPermisosException {
        boolean res = false;
        if (!manejador.isPropietarioAutenticado()) {
            p_autentificarCliente.limpiarFormulario();
            p_autentificarCliente.iniciaFoco();
            v_autentificarCliente.setVisible(true);
            try {
                if (p_autentificarCliente.getCliente() != null) {
                    res = manejador.autenticaPropietario(p_autentificarCliente.getCliente());
                    if (res == false) {
                        throw new SinPermisosException("El usuario Autentificado no es el Novio o la Novia");
                    }
                }
            }
            catch (SinPermisosException e) {
                throw e;
            }
            catch (Exception e) {
                log.error("compruebaUsuarioPropietario()-Error consultando cliente: " + e.getMessage(), e);
                ventana_padre.crearError(e.getMessage());
            }
        }
        else {
            res = true;
        }
        return res;
    }

    private void refrescar() {
        refrescaTotales();
        refrescaTablaPlanes();
        refrescaTablaAbonos();
    }

    private void refrescaTotales() {
        PlanNovioMostrar.estableceTotales(t_total_reserva, t_comprado, t_total_abonado, lb_abono_realizado, t_n_abonos, t_restante_abonos, t_por_abonar);
    }

    private void refrescaTablaPlanes() {
        log.debug("refrescaTablaPlanes() ");
        try {
            tb_articulos.setModel(new ArticulosPlanesNoviosTableModel(manejador.getListaArticulos()));
            tb_articulos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            int i = 0;
            for (int tam : manejador.getAnchosColumnasTablaResultadoPlanes()) {
                tb_articulos.getColumnModel().getColumn(i).setPreferredWidth(tam);
                i++;
            }
        }
        catch (Exception e) {
            log.error("Error refrescando tabla de Artículos: " + e.getMessage(), e);
            ventana_padre.crearError("Error consultando datos de la reserva");
        }
    }

    private void refrescaTablaAbonos() {

        log.debug("refrescaTablaPlanes() ");
        try {
            tb_abonos.setModel(new ListadoAbonosPlanTableModel(manejador.getListaAbonos()));
            tb_abonos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            int i = 0;
            for (int tam : manejador.getAnchosColumnasTablaAbonosResultadoPlanes()) {
                tb_abonos.getColumnModel().getColumn(i).setPreferredWidth(tam);
                i++;
            }
            List<Almacen> almacenes = TiendasServices.consultarListaTiendas();
            for(AbonoPlanNovio abonoPlanNovio : PlanNovioMostrar.getPlanNovio().getPlan().getAbonoPlanNovioList()){
                for(Almacen almacen : almacenes){
                    if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(almacen.getCodalm())){
                        abonoPlanNovio.setNombreAlmacenAbono(almacen.getDesalm());
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("Error refrescando tabla de Abonos: " + e.getMessage(), e);
            ventana_padre.crearError("Error consultando datos de la reserva");
        }
    }

    private void accionComprarArticulo() {
        if (b_comprar_articulo.isEnabled()) {
            accionSeleccionarInvitado(JSeleccionarInvitadoPlan.MODO_SELECCION);

            if (manejador.getInvitadoSeleccionado() != null) {
                try {
                    manejador.getPlanNovio().setModo(PlanNovioOBJ.MODO_COMPRAR_ARTICULO);
                    ventana_padre.addIdentificaClienteReservacionView(manejador.getPlanNovio());
                    ventana_padre.showView("identifica_cliente_reservacion");

                    // Abrir pantalla de pagos para la reserva
                    //ventana_padre.irPagoArticulos(reservacion, JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS);
                }
                catch (Exception e) {
                    log.error("Error abonando artículo: " + e.getMessage(), e);
                    manejador.getPlanNovio().setModo(PlanNovioOBJ.MODO_MOSTRANDO_PLAN);
                    ventana_padre.crearError(null);
                }
            }
        }
    }

    public void accionSeleccionarInvitado(int modo) {

        v_seleccionar_invitado.setLocationRelativeTo(null);
        p_seleccionar_invitado.setContenedor(v_seleccionar_invitado);

        p_seleccionar_invitado.setModo(modo);

        p_seleccionar_invitado.iniciaVista();
        v_seleccionar_invitado.setVisible(true);

    }

    private void accionRealizarAbono() {
        if (b_abonos_propios.isEnabled()) {
            accionSeleccionarInvitado(JSeleccionarInvitadoPlan.MODO_SELECCION);

            if (manejador.getInvitadoSeleccionado() != null) {
                try {
                    manejador.getPlanNovio().setModo(PlanNovioOBJ.MODO_REALIZAR_ABONO);
                    ventana_padre.addIdentificaClienteReservacionView(manejador.getPlanNovio());
                    ventana_padre.showView("identifica_cliente_reservacion");

                    // Abrir pantalla de pagos para la reserva
                    //ventana_padre.irPagoArticulos(reservacion, JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS);
                }
                catch (Exception e) {
                    log.error("Error realizando abonando: " + e.getMessage(), e);
                    manejador.getPlanNovio().setModo(PlanNovioOBJ.MODO_MOSTRANDO_PLAN);
                    ventana_padre.crearError(null);
                }
            }
        }
    }

    public void realizarAbono() {
        accionAbono();
    }

    private void accionAbono() {
        try {
            BigDecimal abonoMinimo = null;
            //Se establecer el abono mímino de un abono
            abonoMinimo = manejador.getAbonoMinimo(); // Abono mínimo

            abonoMinimo = (BigDecimal) ventana_padre.crearVentanaIngresarValor(JIngresarValor.TIPO_DECIMAL, "<html>INTRODUZCA EL IMPORTE QUE QUIERE ABONAR</html>");
            if (abonoMinimo != null) {
                crearVentanaPagos(abonoMinimo, abonoMinimo);
                // cuando sale de la ventana importe
                if (p_pagos.getPagoRealizado() != null) {
                    BigDecimal importeAAbonar;
                    importeAAbonar = p_pagos.getPagoRealizado();
                    log.debug("Importe a pagar introducido: " + importeAAbonar.toString());

                    if (p_pagos.isCancelado()) {
                    }
                }
            }
        }
        catch (Exception ex) {
            log.error("Error realizando abono: " + ex.getMessage(), ex);
            ventana_padre.crearError("Ocurrió un error realizando el abono.");
        }
    }

    @Override
    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo) {
        log.debug("creción de la ventana de pagos");
        v_pagos.getContentPane().removeAll();

        p_pagos = new JReservacionesPagosV(PlanNovioMostrar.getPlanNovio(), importeMinimo, importeMaximo);
        javax.swing.GroupLayout v_pagosLayout = new javax.swing.GroupLayout(v_pagos.getContentPane());
        v_pagos.getContentPane().setLayout(v_pagosLayout);
        v_pagosLayout.setHorizontalGroup(
                v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v_pagosLayout.createSequentialGroup().addContainerGap().addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        v_pagosLayout.setVerticalGroup(
                v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_pagosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        p_pagos.setContenedor(v_pagos);
        v_pagos.getContentPane().add(p_pagos);
        //v_pagos.setModal(true);
        //v_pagos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_pagos.requestFocus();
        p_pagos.iniciaVista();
        v_pagos.setVisible(true);
        p_pagos.isCancelado();
    }

    private void inicializarValidacion() {

        t_direccion.addValidador(new ValidadorObligatoriedad(), this);
        t_direccion.addValidador(new ValidadorTexto(400), this);

        t_telefono_contacto.addValidador(new ValidadorObligatoriedad(), this);
        ValidadorTelefono validadorTelefono = new ValidadorTelefono();
        validadorTelefono.esHTML=false;
        t_telefono_contacto.addValidador(validadorTelefono, this);

        t_emails.addValidador(new ValidadorEmail(), this);
        t_emails.addValidador(new ValidadorTexto(400), this);

        t_lugar_boda.addValidador(new ValidadorObligatoriedad(), this);
        t_lugar_boda.addValidador(new ValidadorTexto(400), this);
        // validador de fecha/hora
        t_fecha_hora_boda.addValidador(new ValidadorObligatoriedad(), this);
        t_fecha_hora_boda.addValidador(new ValidadorFechaHora(), this);

        t_numero_inviados.addValidador(new ValidadorEntero(0, 9999), this);

        t_fecha_contacto_invitados.addValidador(new ValidadorObligatoriedad(), this);
        t_fecha_contacto_invitados.addValidador(new ValidadorFecha(), this);
    }

    private void inicializarFormulario() {
        formulario = new LinkedList<IValidableForm>();
        formulario.add(t_direccion);
        formulario.add(t_telefono_contacto);
        formulario.add(t_emails);
        formulario.add(t_lugar_boda);
        formulario.add(t_fecha_hora_boda);
        formulario.add(t_numero_inviados);
        formulario.add(t_fecha_contacto_invitados);
    }

    private void activaFormulario() {
        for (IValidableForm ivf : formulario) {
            ivf.setValidacionHabilitada(true);
            ivf.setFocusable(true);
        }
    }

    private void desactivaFormulario() {
        for (IValidableForm ivf : formulario) {
            ivf.setValidacionHabilitada(false);
            ivf.setFocusable(false);
        }
    }

    private void accionEdiarPlan() {
        if (b_editar_plan.isEnabled()) {

            try {
                if (compruebaUsuarioPropietario()) {
                    manejador.getPlanNovio().setModo(PlanNovioOBJ.MODO_MODIFICANDO_PLAN);
                    ventana_padre.irVentanaDatosAdicionalesPlanNovio();
                }
            }
            catch (SinPermisosException e) {
                ventana_padre.crearAdvertencia(e.getMessage());
            }
        }
    }

    private void accionRematarReservacion() {
        if (b_liquidar_reserva.isEnabled()) {
            try {
                if (compruebaUsuarioPropietario()) {
                    try {
                        // Comprobar que se ha pagado el minimo. En caso contrario se requiere permiso de administración
                        String compruebaAutorizacion;
                        if (!manejador.isMinimoPlanAlcanzado()) {
                            boolean confirmado = ventana_padre.crearVentanaConfirmacion("No se ha cumplido el monto establecido. ¿Procesar Liquidación?");
                            if (!confirmado) {
                                throw new SinPermisosException();
                            }
                            compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.LIQUIDAR_RESERVA, "<html>Confirme permitir Liquidación</br> por debajo de mínimo</html>");
                            manejador.crearLogAccesoRematarPlan(compruebaAutorizacion);
                        }
                        else{
                            compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.LIQUIDAR_RESERVA, "<html>Confirme permitir Liquidación</html>");
                            manejador.crearLogAccesoRematarPlan(compruebaAutorizacion);
                        }

                        boolean confirmado = ventana_padre.crearVentanaConfirmacion("La acción Liquidará el plan y generará una nota de crédito por el total de los abonos sin usar. ¿Desea Continuar?");
                        if (confirmado) {
                            //Validar si el local es el dueno del plan o no
                            BigDecimal valorLiquidar = BigDecimal.ZERO;
                            if(Sesion.getTienda().getAlmacen().getCodalm().equals(manejador.getPlanNovio().getPlan().getPlanNovioPK().getCodalm())){
                                //Local propietario, buscar si tiene abonos pendientes de otros locales
                                Boolean pendienteLiquidar = false;
                                for(AbonoPlanNovio abonoPlanNovio : manejador.getPlanNovio().getPlan().getAbonoPlanNovioList()){
                                    if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono() != null && !abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getAlmacen().getCodalm()) &&
                                            abonoPlanNovio.getEstadoLiquidacion() == null && abonoPlanNovio.getAnulado().equals('N')){
                                        pendienteLiquidar = true;
                                        break;
                                    }else if (abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getAlmacen().getCodalm())  && abonoPlanNovio.getAnulado().equals('N')
                                            && abonoPlanNovio.getEstadoLiquidacion() == null){
                                        valorLiquidar = valorLiquidar.add(abonoPlanNovio.getCantidadSinDcto());
                                    }
                                }
                                if(!pendienteLiquidar){
                                    manejador.accionRematarReservacion(valorLiquidar.subtract(manejador.getPlanNovio().getPlan().getAbonadoUtilizado()), true);
                                }else{
                                    ventana_padre.crearAdvertencia("Deben liquidarse los abonos de otros locales antes de realizar la liquidación en el local dueño del plan novios");
                                }
                            }else{
                                //Local no propietario
                                for(AbonoPlanNovio abonoPlanNovio : manejador.getPlanNovio().getPlan().getAbonoPlanNovioList()){
                                    if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getAlmacen().getCodalm()) &&
                                            abonoPlanNovio.getEstadoLiquidacion() == null &&  abonoPlanNovio.getAnulado().equals('N')){
                                        valorLiquidar = valorLiquidar.add(abonoPlanNovio.getCantidadSinDcto());
                                    }
                                }
                                if(valorLiquidar.compareTo(BigDecimal.ZERO) == 0){
                                    ventana_padre.crearAdvertencia("no tiene abonos pendientes de liquidar en ese local.");
                                }else{
                                    manejador.accionRematarReservacion(valorLiquidar, false);
                                }
                                
                            }
                            
                            // Refrescar la vista
                            this.iniciaVista();
                        }
                    }
                    catch (SinPermisosException ex) {
                    }
                }
            }
            catch (SinPermisosException e) {
                ventana_padre.crearAdvertencia(e.getMessage());
            }
        }
    }

    public void accionComprarConAbonos() {
        if (b_liquidar_reserva.isEnabled()) {
            try {
                if (compruebaUsuarioPropietario()) {

                    // Refrescamos la lista de artículos no comprados
                    manejador.refrescaArticulosNoComprados();

                    // Mostramos la ventana de liquidación
                    if (manejador.getPlanNovio().getArticulosNoComprados().isEmpty()) {
                        ventana_padre.crearAdvertencia("El Plan no tiene artículos por comprar");
                        return;
                    }
                    boolean aplicarDescuento = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea aplicar un descuento adicional a los artículos comprados?", "Sí", "No");
                    BigDecimal descuentoCompraConAbonos = null;
                    if (aplicarDescuento){
                        try{
                            ventana_padre.compruebaAutorizacion(Operaciones.DESCUENTO_COMPRA_ABONOS);
                                descuentoCompraConAbonos = JPrincipal.crearVentanaIngresarValorDecimal("Indique el descuento que desea aplicar:", Numero.CIEN);
                                if(descuentoCompraConAbonos == null || descuentoCompraConAbonos.compareTo(BigDecimal.ZERO)<0){
                                    descuentoCompraConAbonos = null;
                                }
                        }
                        catch(SinPermisosException e){
                            log.debug("Sin permisos para pedir descuento");
                        }
                    }
                    manejador.setDescuentoCompraAbonos(descuentoCompraConAbonos);
                    p_liquidacion_parcial.iniciaVista();
                    v_liquidacion_parcial.setVisible(true);
                    if (!p_liquidacion_parcial.isCancelado()) {
                        // SE PROCEDE A LIQUIDAR
                        boolean hayPendienteEnvio = false;
                        for(ArticuloPlanNovio artPlan:p_liquidacion_parcial.getArticulosSeleccionados()){
                            if(artPlan.isPendienteEnvio()){
                                hayPendienteEnvio = true;
                                break;
                            }
                        }
                        if(hayPendienteEnvio){
                            p_datos_envio.inicializaFormulario(manejador.getPlanNovio().getClienteLogueado());
                            v_datos_envio.setVisible(true);
                            if(p_datos_envio.isAceptado()){
                                manejador.accionComprarConAbonos(p_liquidacion_parcial.getArticulosSeleccionados(), p_datos_envio.getDatosEnvio());
                            } else {
                                ventana_padre.crearAdvertencia("Para envío a domicilio hay que seleccionar unos datos de envío");
                            }
                        } else {
                            manejador.accionComprarConAbonos(p_liquidacion_parcial.getArticulosSeleccionados(), null);
                        }
                        this.iniciaVista(PESTANHA_ARTICULOS);
                    }
                }
            }
            catch (SinPermisosException e) {
                ventana_padre.crearAdvertencia(e.getMessage());
            }
        }
    }

    private void accionSeleccionarInvitado() {
        if (b_gestionar_invitados.isEnabled()) {
            try {
                if (compruebaUsuarioPropietario()) {
                    accionSeleccionarInvitado(JSeleccionarInvitadoPlan.MODO_MODIFICACION);
                }
            }
            catch (SinPermisosException e) {
                ventana_padre.crearAdvertencia(e.getMessage());
            }
        }
    }
}
