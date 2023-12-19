/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JMostrarReservacion.java
 *
 * Created on 15-sep-2011, 12:28:24
 */
package com.comerzzia.jpos.gui.reservaciones;

import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
import com.comerzzia.jpos.entity.gui.reservaciones.ListaArticulosReservados;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.reservaciones.modelos.MostrarReservacionesCanBabyCellRenderer1;
import com.comerzzia.jpos.gui.reservaciones.modelos.MostrarReservacionesCanBabyTableModel1;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.util.imagenes.Imagenes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Admin
 */
public class JMostrarReservacionCanBaby extends JPanelImagenFondo implements IVista, IPagoImporteCall {

    private String STIPO = "";
    private String SRESERVA = "la reserva";
    private String SBABYSHOWER = "el Babyshower";
        
    
    private static Logger log = Logger.getMLogger(JMostrarReservacionCanBaby.class);
    private JPrincipal ventana_padre;
    private Reservacion reservacion;
    private BigDecimal totalPagado;
    private BigDecimal totalImporte;
    private BigDecimal totalAbonado;
    private BigDecimal restanteAbonos;
    private BigDecimal porAbonar;
    JReservacionListaAbonos p_listarAbonos;
    private static SimpleDateFormat formateador = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
    List<ArticuloReservado> listaArticulos;
    private boolean isPermiteEdicionPropietario = false;
    int lineaSelecionada;

    /** Creates new form JMostrarReservacion */
    public JMostrarReservacionCanBaby() {
        super();
        initComponents();
    }

    public JMostrarReservacionCanBaby(Reservacion reservacionEnt) {
        super();
        // Logs
        try {
            log.debug("MOSTRANDO RESERVACION " + reservacionEnt.getReservacion().getUidReservacion());
            log.info("Código :" + reservacionEnt.getReservacion().getReservaTipo().getCodTipo());
            log.info("Descripción :" + reservacionEnt.getReservacion().getReservaTipo().getDesTipo());
            log.info("Liquidado :" + reservacionEnt.getReservacion().getLiquidado());
            log.info("Procesado :" + reservacionEnt.getReservacion().getProcesado());
            log.info("Cancelada :" + reservacionEnt.getReservacion().getCancelado());

            this.reservacion = reservacionEnt;
            initComponents();
            this.ventana_padre = Sesion.getVentanaPadre();
            // Imagen de fondo
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            try {
                
                URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_reservas_canastillabbsh.png");

                this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
            }
            catch (IOException ex) {
                log.error("No se pudo cargar la imagen de fondo de cierre de caja");
            }

           // Imagenes.cambiarImagenPublicidad(jLabel30);

            Border empty = new EmptyBorder(0, 0, 0, 0);
            jScrollPane1.setViewportBorder(empty);
            tb_articulos.setBorder(empty);
            jScrollPane1.getViewport().setOpaque(false);

            // Inicializamos el total real abonado
            reservacion.calculaTotales();
            lb_abono_realizado.setText("( $" + reservacion.getAbonosReales().toString() + " sin descuentos)");

            String tipoReserva = reservacion.getReservacion().getReservaTipo().getDesTipo().toLowerCase();
            tipoReserva = tipoReserva.substring(0, 1).toUpperCase() + tipoReserva.substring(1, tipoReserva.length());
            //Rellenamos los datos de la reserva
            JTipoReservacion.setText(tipoReserva);
            Fecha alta = reservacion.getReservacion().getFechaAlta();
            JFechaAlta.setText(alta.toString());
            Fecha fin = reservacion.getReservacion().getCaducidad();
            JFechaFin.setText(fin.toString());
            JEstado.setText(reservacion.getReservacion().getEstado());

            // Datos adicionales
            lb_nombre_organizadora.setText(reservacion.getReservacion().getNombreOrganizadora() + " " + reservacion.getReservacion().getApellidosOrganizadora());
            lb_telefono_organizadora.setText(reservacion.getReservacion().getTelefonoOrganizadora());

            lb_direccion_evento.setText(reservacion.getReservacion().getDireccionEvento());
            lb_fecha_evento.setText(reservacion.getReservacion().getFechaHoraEvento().getStringHora());

            // Hay que considerar posibles reservas que se han hecho con el sistema y que no disponen del nuevo campo
            if (reservacion.getReservacion().getCodReservacion() != null) {
                lb_num_res.setText(reservacion.getReservacion().getCodReservacion().toString());
            }

            refrescarTotales();

            URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
            ImageIcon icon = new ImageIcon(myurl);
            v_pagos.setIconImage(icon.getImage());
            v_seleccionar_invitado.setIconImage(icon.getImage());
            v_liquidacion_parcial.setIconImage(icon.getImage());
            v_listarAbonos.setIconImage(icon.getImage());
            v_listarAbonos.setLocationRelativeTo(null);
            v_detalle_articulo.setIconImage(icon.getImage());
            v_detalle_articulo.setLocationRelativeTo(null);
            p_detalle_articulo.setContenedor(v_detalle_articulo);
            p_detalle_articulo.setVentana_padre(ventana_padre);
            p_seleccionar_invitado.setVentana_padre(ventana_padre);
            p_seleccionar_invitado.setContenedor(v_seleccionar_invitado);
            v_valor.setLocationRelativeTo(this);
            p_valor.setVentana_padre(getVentana_padre());
            p_valor.setContenedor(v_valor);
            v_autentificarCliente.setIconImage(icon.getImage());
            p_autentificarCliente.setContenedor(v_autentificarCliente);
            v_autentificarCliente.setLocationRelativeTo(null);

            //isPermiteEdicionPropietario = (reservacion.getInvitadoActivo().equals(reservacion.getReservacion().getCodcli()) && reservacion.getReservacion().isAbierta());
            isPermiteEdicionPropietario = true;

            crearAccionFocoTabla(this, tb_articulos, KeyEvent.VK_T, InputEvent.CTRL_MASK);
            iniciaVista();
            estableceDatosGenerales();
            //
            if (reservacion.isCanastilla()){
                STIPO = SRESERVA;
            }
            else{
                STIPO = SBABYSHOWER;
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            ventana_padre.crearError("No se pudo mostrar "+STIPO);
        }

    }

    private void refrescarTabla() {
        List<ReservaArticuloBean> articulos = reservacion.getReservacion().getReservaArticuloList();
        ListaArticulosReservados constructorListaArticulos = new ListaArticulosReservados(articulos);
        listaArticulos = constructorListaArticulos.getArticulos();
        MostrarReservacionesCanBabyTableModel1 modelo = new MostrarReservacionesCanBabyTableModel1(listaArticulos);
        tb_articulos.setModel(modelo);

        // Tamaños de la tabla
        tb_articulos.getColumnModel().getColumn(0).setPreferredWidth(30);
        tb_articulos.getColumnModel().getColumn(1).setPreferredWidth(22);
        tb_articulos.getColumnModel().getColumn(2).setPreferredWidth(202);
        tb_articulos.getColumnModel().getColumn(3).setPreferredWidth(10);
        tb_articulos.getColumnModel().getColumn(4).setPreferredWidth(10);
        tb_articulos.getColumnModel().getColumn(5).setPreferredWidth(18);
        //Dejamos 8px para la barra lateral 
    }

    public JMostrarReservacionCanBaby(Reservacion reservacion, JPrincipal ventana_padre) {
        this(reservacion);
        this.ventana_padre = ventana_padre;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        v_seleccionar_invitado = new javax.swing.JDialog();
        p_seleccionar_invitado = new JSeleccionarInvitado(ventana_padre, reservacion);
        v_liquidacion_parcial = new javax.swing.JDialog();
        v_listarAbonos = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        v_detalle_articulo = new javax.swing.JDialog();
        p_detalle_articulo = new com.comerzzia.jpos.gui.reservaciones.JDetalleArticulo();
        v_valor = new javax.swing.JDialog();
        p_valor = new com.comerzzia.jpos.gui.reservaciones.JCantidad();
        v_autentificarCliente = new javax.swing.JDialog();
        p_autentificarCliente = new com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente();
        v_ident_cliente = new javax.swing.JDialog();
        p_ident_cliente = new com.comerzzia.jpos.gui.reservaciones.JReservacionesCliente(this.getVentana_padre(),"reservacion");
        jLabel6 = new javax.swing.JLabel();
        b_abonos_propios = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel30 = new javax.swing.JLabel();
        b_comprar_articulo = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_liquidar_reserva = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar_reserva = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_listar_abonos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_menu_ppal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_add_articulos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel9 = new javax.swing.JLabel();
        tp_panel = new javax.swing.JTabbedPane();
        tab_datos_generales = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        t_cli_cliente_nombre = new javax.swing.JLabel();
        t_cli_documento = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JTipoDocumento = new javax.swing.JLabel();
        lb_nombre = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_nombre1 = new javax.swing.JLabel();
        lb_nombre_organizadora = new javax.swing.JLabel();
        lb_nombre2 = new javax.swing.JLabel();
        lb_telefono_organizadora = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lb_direccion_evento = new javax.swing.JLabel();
        lb_nombre3 = new javax.swing.JLabel();
        lb_nombre4 = new javax.swing.JLabel();
        lb_fecha_evento = new javax.swing.JLabel();
        JTipoDocumento1 = new javax.swing.JLabel();
        t_cli_telefono = new javax.swing.JLabel();
        JTipoDocumento2 = new javax.swing.JLabel();
        t_cli_direccion = new javax.swing.JLabel();
        lb_nombre5 = new javax.swing.JLabel();
        t_cli_apellidos = new javax.swing.JLabel();
        tab_articulos = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_articulos = new javax.swing.JTable();
        tab_abonos = new com.comerzzia.jpos.gui.components.JPanelImagenFondo();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        t_total_abonado = new javax.swing.JTextField();
        t_comprado = new javax.swing.JTextField();
        lb_porAbonar = new javax.swing.JLabel();
        t_n_abonos = new javax.swing.JTextField();
        lb_abono_realizado = new javax.swing.JLabel();
        lb_abonos_disponibles = new javax.swing.JLabel();
        t_restante_abonos = new javax.swing.JTextField();
        t_por_abonar = new javax.swing.JTextField();
        t_total_reserva = new javax.swing.JTextField();
        JTipoReservacion1 = new javax.swing.JLabel();
        JTipoReservacion = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        JFechaAlta = new javax.swing.JLabel();
        lb_invitado_seleccionado = new javax.swing.JLabel();
        JFechaFin = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        JEstado = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lb_num_res = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

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
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_seleccionar_invitado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        v_seleccionar_invitadoLayout.setVerticalGroup(
            v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
            .addGroup(v_seleccionar_invitadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(v_seleccionar_invitadoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(p_seleccionar_invitado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        v_liquidacion_parcial.setAlwaysOnTop(true);
        v_liquidacion_parcial.setMinimumSize(new java.awt.Dimension(780, 450));
        v_liquidacion_parcial.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_liquidacion_parcial.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        javax.swing.GroupLayout v_liquidacion_parcialLayout = new javax.swing.GroupLayout(v_liquidacion_parcial.getContentPane());
        v_liquidacion_parcial.getContentPane().setLayout(v_liquidacion_parcialLayout);
        v_liquidacion_parcialLayout.setHorizontalGroup(
            v_liquidacion_parcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 746, Short.MAX_VALUE)
        );
        v_liquidacion_parcialLayout.setVerticalGroup(
            v_liquidacion_parcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 334, Short.MAX_VALUE)
        );

        v_listarAbonos.setAlwaysOnTop(true);
        v_listarAbonos.setMinimumSize(new java.awt.Dimension(830, 490));
        v_listarAbonos.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                v_listarAbonosWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout v_listarAbonosLayout = new javax.swing.GroupLayout(v_listarAbonos.getContentPane());
        v_listarAbonos.getContentPane().setLayout(v_listarAbonosLayout);
        v_listarAbonosLayout.setHorizontalGroup(
            v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 907, Short.MAX_VALUE)
        );
        v_listarAbonosLayout.setVerticalGroup(
            v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );

        v_detalle_articulo.setAlwaysOnTop(true);
        v_detalle_articulo.setMinimumSize(new java.awt.Dimension(680, 456));
        v_detalle_articulo.setModal(true);

        javax.swing.GroupLayout v_detalle_articuloLayout = new javax.swing.GroupLayout(v_detalle_articulo.getContentPane());
        v_detalle_articulo.getContentPane().setLayout(v_detalle_articuloLayout);
        v_detalle_articuloLayout.setHorizontalGroup(
            v_detalle_articuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_detalle_articuloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_detalle_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_detalle_articuloLayout.setVerticalGroup(
            v_detalle_articuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_detalle_articuloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_detalle_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_valor.setAlwaysOnTop(true);
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

        v_autentificarCliente.setAlwaysOnTop(true);
        v_autentificarCliente.setMinimumSize(new java.awt.Dimension(429, 310));
        v_autentificarCliente.setModal(true);
        v_autentificarCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v_autentificarClienteFocusGained(evt);
            }
        });

        p_autentificarCliente.setMinimumSize(new java.awt.Dimension(400, 261));

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

        v_ident_cliente.setAlwaysOnTop(true);
        v_ident_cliente.setIconImages(null);
        v_ident_cliente.setMinimumSize(new java.awt.Dimension(1024, 720));
        v_ident_cliente.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_ident_cliente.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        p_ident_cliente.setMinimumSize(new java.awt.Dimension(1024, 720));

        javax.swing.GroupLayout p_ident_clienteLayout = new javax.swing.GroupLayout(p_ident_cliente);
        p_ident_cliente.setLayout(p_ident_clienteLayout);
        p_ident_clienteLayout.setHorizontalGroup(
            p_ident_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 855, Short.MAX_VALUE)
        );
        p_ident_clienteLayout.setVerticalGroup(
            p_ident_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout v_ident_clienteLayout = new javax.swing.GroupLayout(v_ident_cliente.getContentPane());
        v_ident_cliente.getContentPane().setLayout(v_ident_clienteLayout);
        v_ident_clienteLayout.setHorizontalGroup(
            v_ident_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_ident_clienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_ident_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(51, 51, 51))
        );
        v_ident_clienteLayout.setVerticalGroup(
            v_ident_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_ident_clienteLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(p_ident_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 98, -1, -1));

        b_abonos_propios.setText("<html><center>Realizar<br>Abono<br>F3</center></html>");
        b_abonos_propios.setPreferredSize(new java.awt.Dimension(127, 48));
        b_abonos_propios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_abonos_propiosActionPerformed(evt);
            }
        });
        add(b_abonos_propios, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 633, 90, 80));

        jLabel30.setFocusable(false);
        add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 138, 563));

        b_comprar_articulo.setText("<html><center>Comprar<br>Artículo<br>F4 </center></html>");
        b_comprar_articulo.setEnabled(false);
        b_comprar_articulo.setPreferredSize(new java.awt.Dimension(127, 48));
        b_comprar_articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_comprar_articuloActionPerformed(evt);
            }
        });
        add(b_comprar_articulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 633, 90, 80));

        b_liquidar_reserva.setLabel("<html><center>Rematar reservación<br>F5</center></html>");
        b_liquidar_reserva.setMargin(new java.awt.Insets(0, -3, 0, -1));
        b_liquidar_reserva.setPreferredSize(new java.awt.Dimension(127, 48));
        b_liquidar_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_liquidar_reservaActionPerformed(evt);
            }
        });
        add(b_liquidar_reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 633, 90, 80));

        b_cancelar_reserva.setLabel("<html><center>Anular reservación<br>F6</center></html>");
        b_cancelar_reserva.setMargin(new java.awt.Insets(0, -3, 0, -1));
        b_cancelar_reserva.setPreferredSize(new java.awt.Dimension(127, 48));
        b_cancelar_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_reservaActionPerformed(evt);
            }
        });
        add(b_cancelar_reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 633, 90, 80));

        b_listar_abonos.setText("<html><center>Listar Abonos <br/>F7</center></html>");
        b_listar_abonos.setMaximumSize(new java.awt.Dimension(94, 48));
        b_listar_abonos.setMinimumSize(new java.awt.Dimension(94, 48));
        b_listar_abonos.setPreferredSize(new java.awt.Dimension(94, 48));
        b_listar_abonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_listar_abonosActionPerformed(evt);
            }
        });
        add(b_listar_abonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 633, 90, 80));

        b_menu_ppal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_menu_ppal.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_menu_ppal.setNextFocusableComponent(b_abonos_propios);
        b_menu_ppal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menu_ppalActionPerformed(evt);
            }
        });
        add(b_menu_ppal, new org.netbeans.lib.awtextra.AbsoluteConstraints(692, 633, 90, 80));

        b_add_articulos.setText("<html><center>Añadir Artículos<br/>F8</center></html>");
        b_add_articulos.setEnabled(false);
        b_add_articulos.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_add_articulos.setNextFocusableComponent(b_menu_ppal);
        b_add_articulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_add_articulosActionPerformed(evt);
            }
        });
        add(b_add_articulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 633, 90, 80));
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 98, -1, -1));

        tab_datos_generales.setOpaque(false);

        t_cli_cliente_nombre.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cli_cliente_nombre.setText("Nombre");

        t_cli_documento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cli_documento.setText("RUC,CED,PAS y Número");

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel11.setText("Reservado a cliente");

        JTipoDocumento.setFont(JTipoDocumento.getFont().deriveFont(JTipoDocumento.getFont().getStyle() | java.awt.Font.BOLD));
        JTipoDocumento.setForeground(new java.awt.Color(0, 102, 255));
        JTipoDocumento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JTipoDocumento.setText("Tipo documento:");

        lb_nombre.setFont(lb_nombre.getFont().deriveFont(lb_nombre.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre.setText("Nombre: ");

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel12.setText("Organizadora");

        lb_nombre1.setFont(lb_nombre1.getFont().deriveFont(lb_nombre1.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre1.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre1.setText("Nombre: ");

        lb_nombre_organizadora.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lb_nombre_organizadora.setText("Nombre y Apellidos");

        lb_nombre2.setFont(lb_nombre2.getFont().deriveFont(lb_nombre2.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre2.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre2.setText("Teléfono:");

        lb_telefono_organizadora.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lb_telefono_organizadora.setText("Teléfono");

        jLabel13.setFont(jLabel13.getFont().deriveFont(jLabel13.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel13.setText("Evento");

        lb_direccion_evento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lb_direccion_evento.setText("Dirección");

        lb_nombre3.setFont(lb_nombre3.getFont().deriveFont(lb_nombre3.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre3.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre3.setText("Dirección:");

        lb_nombre4.setFont(lb_nombre4.getFont().deriveFont(lb_nombre4.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre4.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre4.setText("Fecha:");

        lb_fecha_evento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lb_fecha_evento.setText("Fecha");

        JTipoDocumento1.setFont(JTipoDocumento1.getFont().deriveFont(JTipoDocumento1.getFont().getStyle() | java.awt.Font.BOLD));
        JTipoDocumento1.setForeground(new java.awt.Color(0, 102, 255));
        JTipoDocumento1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JTipoDocumento1.setText("Teléfono:");

        t_cli_telefono.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cli_telefono.setText("Teléfono");

        JTipoDocumento2.setFont(JTipoDocumento2.getFont().deriveFont(JTipoDocumento2.getFont().getStyle() | java.awt.Font.BOLD));
        JTipoDocumento2.setForeground(new java.awt.Color(0, 102, 255));
        JTipoDocumento2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JTipoDocumento2.setText("Dirección:");

        t_cli_direccion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cli_direccion.setText("Dirección");

        lb_nombre5.setFont(lb_nombre5.getFont().deriveFont(lb_nombre5.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre5.setForeground(new java.awt.Color(0, 102, 255));
        lb_nombre5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nombre5.setText("Apellidos:");

        t_cli_apellidos.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cli_apellidos.setText("Apellidos");

        javax.swing.GroupLayout tab_datos_generalesLayout = new javax.swing.GroupLayout(tab_datos_generales);
        tab_datos_generales.setLayout(tab_datos_generalesLayout);
        tab_datos_generalesLayout.setHorizontalGroup(
            tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addComponent(JTipoDocumento1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(t_cli_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addComponent(JTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(t_cli_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11)
                    .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                            .addComponent(lb_nombre5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(t_cli_apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                            .addComponent(lb_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(t_cli_cliente_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                                .addComponent(lb_nombre3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lb_direccion_evento, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                                .addComponent(lb_nombre4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lb_fecha_evento, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                            .addComponent(JTipoDocumento2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(t_cli_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                                .addComponent(lb_nombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lb_nombre_organizadora, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                                .addComponent(lb_nombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lb_telefono_organizadora, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(1275, Short.MAX_VALUE))
        );
        tab_datos_generalesLayout.setVerticalGroup(
            tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_cli_cliente_nombre)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lb_nombre)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_cli_apellidos)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lb_nombre5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(JTipoDocumento))
                    .addComponent(t_cli_documento))
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(JTipoDocumento1))
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_cli_telefono)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(JTipoDocumento2))
                    .addComponent(t_cli_direccion))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lb_nombre1))
                    .addComponent(lb_nombre_organizadora))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nombre2)
                    .addComponent(lb_telefono_organizadora))
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_datos_generalesLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lb_nombre3))
                    .addComponent(lb_direccion_evento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_datos_generalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nombre4)
                    .addComponent(lb_fecha_evento))
                .addGap(348, 348, 348))
        );

        tp_panel.addTab("<html><u>D</u>ATOS GENERALES</html>", tab_datos_generales);
        tab_datos_generales.getAccessibleContext().setAccessibleName("general");

        tab_articulos.setOpaque(false);

        jLabel19.setFont(jLabel19.getFont().deriveFont(jLabel19.getFont().getStyle() | java.awt.Font.BOLD, 18));
        jLabel19.setText("Artículos");

        tb_articulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tb_articulos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tb_articulos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_articulosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_articulos);

        javax.swing.GroupLayout tab_articulosLayout = new javax.swing.GroupLayout(tab_articulos);
        tab_articulos.setLayout(tab_articulosLayout);
        tab_articulosLayout.setHorizontalGroup(
            tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_articulosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(1219, Short.MAX_VALUE))
        );
        tab_articulosLayout.setVerticalGroup(
            tab_articulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_articulosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
        );

        tp_panel.addTab("<html><u>A</u>RTICULOS</html>", tab_articulos);
        tab_articulos.getAccessibleContext().setAccessibleName("articulos");

        tab_abonos.setOpaque(false);

        jLabel26.setFont(jLabel26.getFont().deriveFont(jLabel26.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel26.setForeground(new java.awt.Color(0, 102, 255));
        jLabel26.setText("Nº abonos:");

        jLabel27.setFont(jLabel27.getFont().deriveFont(jLabel27.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel27.setForeground(new java.awt.Color(0, 102, 255));
        jLabel27.setText("Total abonos:");

        jLabel25.setFont(jLabel25.getFont().deriveFont(jLabel25.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel25.setForeground(new java.awt.Color(0, 102, 255));
        jLabel25.setText("Total comprado:");

        jLabel24.setFont(jLabel24.getFont().deriveFont(jLabel24.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel24.setForeground(new java.awt.Color(0, 102, 255));
        jLabel24.setText("Total reservación:");

        t_total_abonado.setText("jTextField1");
        t_total_abonado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_abonado.setEnabled(false);

        t_comprado.setText("jTextField1");
        t_comprado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_comprado.setEnabled(false);

        lb_porAbonar.setFont(lb_porAbonar.getFont().deriveFont(lb_porAbonar.getFont().getStyle() | java.awt.Font.BOLD));
        lb_porAbonar.setForeground(new java.awt.Color(0, 102, 255));
        lb_porAbonar.setText("Por abonar:");

        t_n_abonos.setText("jTextField1");
        t_n_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_n_abonos.setEnabled(false);

        lb_abono_realizado.setFont(lb_abono_realizado.getFont().deriveFont(lb_abono_realizado.getFont().getSize()-1f));
        lb_abono_realizado.setText("( Real )");

        lb_abonos_disponibles.setFont(lb_abonos_disponibles.getFont().deriveFont(lb_abonos_disponibles.getFont().getStyle() | java.awt.Font.BOLD));
        lb_abonos_disponibles.setForeground(new java.awt.Color(0, 102, 255));
        lb_abonos_disponibles.setText("Abonos disponibles:");

        t_restante_abonos.setText("jTextField1");
        t_restante_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_restante_abonos.setEnabled(false);

        t_por_abonar.setText("jTextField1");
        t_por_abonar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_por_abonar.setEnabled(false);

        t_total_reserva.setText("jTextField1");
        t_total_reserva.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_reserva.setEnabled(false);

        JTipoReservacion1.setFont(JTipoReservacion1.getFont().deriveFont(JTipoReservacion1.getFont().getStyle() | java.awt.Font.BOLD, 18));
        JTipoReservacion1.setText("Abonos");

        javax.swing.GroupLayout tab_abonosLayout = new javax.swing.GroupLayout(tab_abonos);
        tab_abonos.setLayout(tab_abonosLayout);
        tab_abonosLayout.setHorizontalGroup(
            tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_abonosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTipoReservacion1)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(t_total_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(t_n_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(lb_abonos_disponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(t_restante_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(t_comprado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab_abonosLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(lb_abono_realizado, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(t_total_abonado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tab_abonosLayout.createSequentialGroup()
                                .addGap(153, 153, 153)
                                .addComponent(lb_porAbonar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(46, 46, 46)
                        .addComponent(t_por_abonar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1195, Short.MAX_VALUE))
        );
        tab_abonosLayout.setVerticalGroup(
            tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_abonosLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(JTipoReservacion1)
                .addGap(34, 34, 34)
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_total_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_n_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_restante_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel26)
                            .addComponent(lb_abonos_disponibles))))
                .addGap(12, 12, 12)
                .addGroup(tab_abonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel25))
                    .addComponent(t_comprado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel27))
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lb_abono_realizado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(t_total_abonado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_abonosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lb_porAbonar))
                    .addComponent(t_por_abonar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(622, Short.MAX_VALUE))
        );

        tp_panel.addTab("<html>A<u>B</u>ONOS</html>", tab_abonos);
        tab_abonos.getAccessibleContext().setAccessibleName("abonos");

        add(tp_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, -1, -1));
        tp_panel.getAccessibleContext().setAccessibleName("Datos Generales");

        JTipoReservacion.setFont(JTipoReservacion.getFont().deriveFont(JTipoReservacion.getFont().getStyle() | java.awt.Font.BOLD, 18));
        JTipoReservacion.setText("Datos Reservación");
        add(JTipoReservacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, -1));

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel10.setForeground(new java.awt.Color(0, 102, 255));
        jLabel10.setText("Fecha de Alta:");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 89, -1));

        JFechaAlta.setText("jLabel10");
        add(JFechaAlta, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 67, -1));

        lb_invitado_seleccionado.setFont(lb_invitado_seleccionado.getFont().deriveFont(lb_invitado_seleccionado.getFont().getStyle() | java.awt.Font.BOLD));
        lb_invitado_seleccionado.setForeground(new java.awt.Color(0, 102, 255));
        add(lb_invitado_seleccionado, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 590, 20));

        JFechaFin.setText("jLabel10");
        add(JFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 79, -1));

        jLabel33.setFont(jLabel33.getFont().deriveFont(jLabel33.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel33.setForeground(new java.awt.Color(0, 102, 255));
        jLabel33.setText("Estado:");
        add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 54, -1));

        JEstado.setFont(JEstado.getFont().deriveFont(JEstado.getFont().getStyle() | java.awt.Font.BOLD));
        JEstado.setText("Estado");
        add(JEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 93, -1));

        jLabel34.setFont(jLabel34.getFont().deriveFont(jLabel34.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel34.setForeground(new java.awt.Color(0, 102, 255));
        jLabel34.setText("Número de reserva:");
        add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, 130, -1));

        lb_num_res.setFont(lb_num_res.getFont().deriveFont(lb_num_res.getFont().getStyle() | java.awt.Font.BOLD));
        lb_num_res.setText("NumRes");
        add(lb_num_res, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, 140, -1));

        jLabel28.setFont(jLabel28.getFont().deriveFont(jLabel28.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel28.setForeground(new java.awt.Color(0, 102, 255));
        jLabel28.setText("Fecha Fín:");
        add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 70, 63, -1));
    }// </editor-fold>//GEN-END:initComponents

private void b_menu_ppalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menu_ppalActionPerformed
    accionMenu();
}//GEN-LAST:event_b_menu_ppalActionPerformed

private void b_abonos_propiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_abonos_propiosActionPerformed

    accionAbonosPropios();


}//GEN-LAST:event_b_abonos_propiosActionPerformed

private void b_comprar_articuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_comprar_articuloActionPerformed

    accionComprarArticulo();

}//GEN-LAST:event_b_comprar_articuloActionPerformed

private void tb_articulosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_articulosKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        if (listaArticulos.size() > 0) {
            int indiceSeleccionado = new Integer(tb_articulos.getSelectionModel().getMaxSelectionIndex());
            ArticuloReservado articulo = listaArticulos.get(tb_articulos.getSelectedRow());
            accionDetalleArticulo(articulo);
            ListSelectionModel selectionModelaux = tb_articulos.getSelectionModel();
            selectionModelaux.setSelectionInterval(indiceSeleccionado, indiceSeleccionado);

        }
    }
    else if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
        tb_articulos.transferFocusBackward();
    }
    else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
        tb_articulos.transferFocus();
    }

}//GEN-LAST:event_tb_articulosKeyPressed

private void b_liquidar_reservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_liquidar_reservaActionPerformed
    if (b_liquidar_reserva.isEnabled()) {

        if (compruebaUsuarioPropietario()) {
            try {
                String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.LIQUIDAR_RESERVA);

                List<ReservaArticuloBean> articulosNoComprados = new LinkedList<ReservaArticuloBean>();
                for (ReservaArticuloBean articulo : reservacion.getReservacion().getReservaArticuloList()) {
                    if (!articulo.getComprado()) {
                        articulosNoComprados.add(articulo);
                    }
                }
                // SI LA RESERVA TIENE BONOS 
                if (reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()) {
                    // RESERVA QUE PERMITE ABONOS : BABYSOHER
                    if ( !articulosNoComprados.isEmpty()) {
                        //mostrar ventana liquidacion parcial
                        v_liquidacion_parcial.setLocationRelativeTo(null);

                        JReservacionesLiquidacionParcial p_liquidacion_parcial = new JReservacionesLiquidacionParcial(articulosNoComprados, totalAbonado);
                        crearVentanaLiquidacion(p_liquidacion_parcial);
                        if (!p_liquidacion_parcial.isCancelado()) {
                            liquidar(p_liquidacion_parcial.getArticulosSeleccionados());
                            ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());

                            if (restanteAbonos.compareTo(BigDecimal.ZERO) > 0) {
                                try {
                                    //ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                                    String procedencia = "";
                                    if (STIPO.equals(SRESERVA)){
                                         procedencia = BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION;
                                    }
                                    else{
                                        procedencia = BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION_BABYSHOWER;
                                    }
                                    BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), procedencia, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                                }
                                catch (Exception ex) {
                                    log.error("Error creando bono en liquidación");
                                    ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                                }
                            }
                            //COMPROBANTE
                            ReservacionesServicios.comprobanteArticulosReserva(reservacion, listaArticulos);
                        }
                        else{
                            for (ReservaArticuloBean reservaArticulo : articulosNoComprados) {
                                reservaArticulo.setDescuento(null);
                            }
                        }
                    }
                    else {
                        // Liquidamos directamente
                        liquidar(reservacion.getReservacion().getReservaArticuloList());
                        ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());

                        if (restanteAbonos.compareTo(BigDecimal.ZERO) > 0) {
                            try {
                                //ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                                String procedencia = "";
                                    if (STIPO.equals(SRESERVA)){
                                         procedencia = BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION;
                                    }
                                    else{
                                        procedencia = BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION_BABYSHOWER;
                                    }
                                BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), procedencia, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                            }
                            catch (Exception ex) {
                                log.error("Error creando bono en liquidación");
                                ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                            }
                        }
                        //COMPROBANTE
                            ReservacionesServicios.comprobanteArticulosReserva(reservacion, listaArticulos);
                    }
                    

                }
                else {
                    // RESERVA QUE NO PERMITE ABONOS : CANASTILLA
                    rematarConComprobante();
                }
                refrescarTabla();
                JEstado.setText(reservacion.getReservacion().getEstado());
                iniciaVista();
            }
            catch (TicketException ex) {
                log.error("Error imprimiendo ticket de listado de artículos: " + ex.getMessage(), ex);
            }
            catch (SinPermisosException ex) {
                log.info("No se autentificó el cajero para realizar la operación");
            }
            catch (TicketPrinterException ex) {
                log.error("Error imprimiendo ticket de reservación: " + ex.getMessage(), ex);
                ventana_padre.crearError(ex.getMessage());
            }
            catch (ReservasException ex) {
                log.error("Error liquidando reservación: " + ex.getMessage(), ex);
                ventana_padre.crearError(ex.getMessage());
            }
            catch (DocumentoException ex) {
                log.error("Error liquidando reservación: " + ex.getMessage(), ex);
                ventana_padre.crearError(ex.getMessage());               
            }
        }
    }
}//GEN-LAST:event_b_liquidar_reservaActionPerformed

private void b_cancelar_reservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_reservaActionPerformed
    if (b_cancelar_reserva.isEnabled()) {

        if ((reservacion.isCanastilla() && !reservacion.isTodosArticulosComprados()) || !reservacion.isCanastilla()) {

            if (compruebaUsuarioPropietario()) {
                try {
                    String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);

                    if (ventana_padre.crearVentanaConfirmacion("¿Está seguro que desea anular "+STIPO+"?")) {
                        //Desea cancelar.
                        try {
                            ServicioLogAcceso.crearAccesoLogCancelarReserva(compruebaAutorizacion, reservacion.getReservacion());

                            reservacion.getReservacion().setCancelado(true);
                            reservacion.getReservacion().setFechaLiquidacion(new Fecha());
                            ReservacionesServicios.modificarReserva(reservacion.getReservacion());

                            reservacion.setAbonosRestantes(restanteAbonos);
                            try {
                                if (reservacion.getAbonosRestantesReales().compareTo(BigDecimal.ZERO) > 0) {
                                    String procedencia = "";
                                    if (STIPO.equals(SRESERVA)){
                                         procedencia = BonosServices.PROCEDENCIA_RESERVA_CANCELACION;
                                    }
                                    else{
                                        procedencia = BonosServices.PROCEDENCIA_RESERVA_CANCELACION_BABYSHOWER;
                                    }
                                    BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), procedencia, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                                }

                            }
                            catch (Exception ex) {
                                log.error("Error creando bono en liquidación");
                                ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                            }

                            //COMPROBANTE
                            ReservacionesServicios.comprobanteArticulosReserva(reservacion, listaArticulos);

                            JEstado.setText(reservacion.getReservacion().getEstado());
                            iniciaVista();
                        }
                        catch (ReservasException ex) {
                            log.error("Error al anular "+STIPO);
                            ventana_padre.crearAdvertencia(ex.getMessage());
                        }
                        catch (Exception ex) {
                            log.error("Error al anular la reservación: " + ex.getMessage(), ex);
                            ventana_padre.crearError("Error al anular la reservación.");
                        }
                    }
                    else {
                        //No desea cancelar.
                    }
                }
                catch (SinPermisosException ex) {
                    log.error("Error de permisos al seleccionar cliente.", ex);
                }
            }
        }
        else if (reservacion.isCanastilla()) {
            ventana_padre.crearAdvertencia("La canastilla no puede ser anulada porque ya fue comprada en su totalidad");
        }
    }


}//GEN-LAST:event_b_cancelar_reservaActionPerformed

private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
    p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

private void b_listar_abonosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_listar_abonosActionPerformed
    accionListarAbons();
}//GEN-LAST:event_b_listar_abonosActionPerformed

private void v_listarAbonosWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_listarAbonosWindowGainedFocus
    p_listarAbonos.iniciaFoco();
}//GEN-LAST:event_v_listarAbonosWindowGainedFocus

private void b_add_articulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_add_articulosActionPerformed
    accionAddArticulos();
}//GEN-LAST:event_b_add_articulosActionPerformed

private void v_seleccionar_invitadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_seleccionar_invitadoFocusGained
    p_seleccionar_invitado.iniciaVista();
}//GEN-LAST:event_v_seleccionar_invitadoFocusGained

private void v_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_autentificarClienteFocusGained
    p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_v_autentificarClienteFocusGained

    private void accionMenu() {
        try {
            ventana_padre.mostrarMenu();
        }
        catch (Exception e) {
            log.error("No se pudo mostrar el menú desde el detalle de la reservación", e);
            ventana_padre.crearError(null);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JEstado;
    private javax.swing.JLabel JFechaAlta;
    private javax.swing.JLabel JFechaFin;
    private javax.swing.JLabel JTipoDocumento;
    private javax.swing.JLabel JTipoDocumento1;
    private javax.swing.JLabel JTipoDocumento2;
    private javax.swing.JLabel JTipoReservacion;
    private javax.swing.JLabel JTipoReservacion1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_abonos_propios;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_add_articulos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar_reserva;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_comprar_articulo;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_liquidar_reserva;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_listar_abonos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_menu_ppal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_abono_realizado;
    private javax.swing.JLabel lb_abonos_disponibles;
    private javax.swing.JLabel lb_direccion_evento;
    private javax.swing.JLabel lb_fecha_evento;
    private javax.swing.JLabel lb_invitado_seleccionado;
    private javax.swing.JLabel lb_nombre;
    private javax.swing.JLabel lb_nombre1;
    private javax.swing.JLabel lb_nombre2;
    private javax.swing.JLabel lb_nombre3;
    private javax.swing.JLabel lb_nombre4;
    private javax.swing.JLabel lb_nombre5;
    private javax.swing.JLabel lb_nombre_organizadora;
    private javax.swing.JLabel lb_num_res;
    private javax.swing.JLabel lb_porAbonar;
    private javax.swing.JLabel lb_telefono_organizadora;
    private com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente p_autentificarCliente;
    private com.comerzzia.jpos.gui.reservaciones.JDetalleArticulo p_detalle_articulo;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesCliente p_ident_cliente;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV p_pagos;
    private com.comerzzia.jpos.gui.reservaciones.JSeleccionarInvitado p_seleccionar_invitado;
    private com.comerzzia.jpos.gui.reservaciones.JCantidad p_valor;
    private javax.swing.JLabel t_cli_apellidos;
    private javax.swing.JLabel t_cli_cliente_nombre;
    private javax.swing.JLabel t_cli_direccion;
    private javax.swing.JLabel t_cli_documento;
    private javax.swing.JLabel t_cli_telefono;
    private javax.swing.JTextField t_comprado;
    private javax.swing.JTextField t_n_abonos;
    private javax.swing.JTextField t_por_abonar;
    private javax.swing.JTextField t_restante_abonos;
    private javax.swing.JTextField t_total_abonado;
    private javax.swing.JTextField t_total_reserva;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_abonos;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_articulos;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo tab_datos_generales;
    private javax.swing.JTable tb_articulos;
    private javax.swing.JTabbedPane tp_panel;
    private javax.swing.JDialog v_autentificarCliente;
    private javax.swing.JDialog v_detalle_articulo;
    private javax.swing.JDialog v_ident_cliente;
    private javax.swing.JDialog v_liquidacion_parcial;
    private javax.swing.JDialog v_listarAbonos;
    private javax.swing.JDialog v_pagos;
    private javax.swing.JDialog v_seleccionar_invitado;
    private javax.swing.JDialog v_valor;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        //Cambiado para que el botón de rematar reservación esté desactivado para Sukasa
        if(Sesion.isSukasa()) {
            b_liquidar_reserva.setEnabled(false);
        }
        
        // Establecemos textos a los botones
        if (reservacion.isCanastilla()) {
            b_liquidar_reserva.setText("<html><center>Rematar canastilla<br>F5</center></html>");
            b_cancelar_reserva.setText("<html><center>Anular canastilla<br>F6</center></html>");
        }
        else{
            b_liquidar_reserva.setText("<html><center>Rematar Babyshower<br>F5</center></html>");
            b_cancelar_reserva.setText("<html><center>Anular Babyshower<br>F6</center></html>");
        }

        // En función del típo de reservación cargada muestro los botones correspondientes
        log.info("Iniciando vista de Mostrar Reservación");
        b_add_articulos.setEnabled(false);

        // GENERAL
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

        // REALIZAR ABONOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()
                && !this.reservacion.getReservacion().getCancelado()
                && !this.reservacion.getReservacion().isCaducada()) {
            
            if (reservacion.getInvitadoSeleccionado() == null) {
                b_abonos_propios.setEnabled(false);
            }
            else {
                b_abonos_propios.setEnabled(true);
            }                
                // Creamos la acción.     
                KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
                Action listenerf3 = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        accionAbonosPropios();
                    }
                };
                addHotKey(f3, "ResF3", listenerf3);

            // ALT+B
            KeyStroke altb = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_MASK);
            Action listeneraltb = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    tp_panel.setSelectedIndex(2);
                }
            };
            addHotKey(altb, "IdentClientaltb", listeneraltb);



            b_abonos_propios.requestFocus();
        }
        else {
            b_abonos_propios.setEnabled(false);
            b_listar_abonos.setEnabled(false);
            tp_panel.remove(tab_abonos);
            tab_abonos.setEnabled(false);
            if (!this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()) {
                b_abonos_propios.setVisible(false);
            }
        }

        // COMPRAR ARTICULOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteCompra()
                && !this.reservacion.getReservacion().getCancelado()) { //permiteCompra no será nunca null

            if (reservacion.getInvitadoSeleccionado() == null) {
                b_comprar_articulo.setEnabled(false);
            }
            else {
                b_comprar_articulo.setEnabled(true);
            }
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){ 
                
                // Creamos la acción.     
                KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
                Action listenerf4 = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        accionComprarArticulo();
                    }
                };
                addHotKey(f4, "ResF4", listenerf4);
                b_comprar_articulo.requestFocus();
            }
            else{
                b_comprar_articulo.setEnabled(false);
            }
        }
        else {
            b_comprar_articulo.setEnabled(false);
        }

        // LIQUIDAR RESERVACION Y CANCELARLA
        if (!this.reservacion.getReservacion().getLiquidado()
                && !this.reservacion.getReservacion().getCancelado() && isPermiteEdicionPropietario) {
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){ 

                KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
                Action listenerf5 = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        b_liquidar_reservaActionPerformed(ae);
                    }
                };
                addHotKey(f5, "ResF5", listenerf5);

                KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
                Action listenerf6 = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        b_cancelar_reservaActionPerformed(ae);
                    }
                };
                addHotKey(f6, "ResF6", listenerf6);
            }
            else{
                b_cancelar_reserva.setEnabled(false);
                b_liquidar_reserva.setEnabled(false);
                b_menu_ppal.requestFocus();
            }
        }
        else {
            b_cancelar_reserva.setEnabled(false);
            b_liquidar_reserva.setEnabled(false);
            b_menu_ppal.requestFocus();
            //Foco en menu
        }

        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        Action listenerf7 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionListarAbons();
            }
        };
        addHotKey(f7, "ResF7", listenerf7);

        // AÑADIR ARTICULOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && isPermiteEdicionPropietario
                && !this.reservacion.getReservacion().getCancelado()
                && !this.reservacion.getReservacion().isCaducada()) { //permiteCompra no será nunca null
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){ 

                b_add_articulos.setEnabled(true);
                // Creamos la acción.     
                KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
                Action listenerf8 = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        accionAddArticulos();
                    }
                };
                addHotKey(f8, "ResF8", listenerf8);
                b_add_articulos.requestFocus();
            }
            else{
                b_add_articulos.setEnabled(false);
            }
        }
        else {
            b_add_articulos.setEnabled(false);
        }


        // ACCIÓN PARA MENU

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_menu_ppalActionPerformed(ae);
            }
        };
        addHotKey(f12, "ResF12", listenerf12);

        if (!isPermiteEdicionPropietario && this.reservacion.getReservacion().getLiquidado()
                && !this.reservacion.getReservacion().getCancelado()) {

            KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
            Action listenerCtrlMenos = new AbstractAction() {

                public void actionPerformed(ActionEvent ae) {
                    accionEliminarArticulo();
                    refrescarReservacion();

                }
            };
            addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);
        }
        
        accionInicializarOpcionesInvitado();


        //Realizar foco
        if (!this.reservacion.getReservacion().getLiquidado()
                && !this.reservacion.getReservacion().getCancelado()
                && !this.reservacion.isTodosArticulosComprados()) {
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){
                b_comprar_articulo.requestFocus();
            } 
            else {
                b_menu_ppal.requestFocus();
            }
        }
        else {
            b_menu_ppal.requestFocus();
        }
    }

    public JPrincipal getVentana_padre() {
        return ventana_padre;
    }

    public void setVentana_padre(JPrincipal ventana_padre) {
        this.ventana_padre = ventana_padre;
    }

    public void accionAbonosPropios() {
        if (b_abonos_propios.isEnabled()) {
            if (!reservacion.isCanastilla()) {
                accionSeleccionarInvitado();
            }
            if (reservacion.getInvitadoSeleccionado() != null) {
                reservacion.setArticuloSeleccionado(null);

                ventana_padre.addIdentificaClienteReservacionView(reservacion, JReservacionesCliente.OPERACIONRESERVAPAGOABONO);
                ventana_padre.showView("identifica_cliente_reservacion");

            }
        }
    }


    /**
     * Crea una ventana de Pagos
     * @param importe 
     */
    @Override
    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo) {
        log.debug("creción de la ventana de pagos");
        v_pagos.getContentPane().removeAll();

        p_pagos = new JReservacionesPagosV(ventana_padre, reservacion, importeMaximo, reservacion.getInvitadoActivo(), reservacion.getInvitadoSeleccionado(), importeMinimo, true);
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
        v_pagos.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
        v_pagos.requestFocus();
        p_pagos.iniciaVista();
        v_pagos.setVisible(true);
        p_pagos.isCancelado();
    }

    public void crearVentanaLiquidacion(JReservacionesLiquidacionParcial p_liquidacion_parcial) {
        boolean aplicarDescuento = JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea aplicar un descuento adicional a los artículos comprados?", "Sí", "No");
        BigDecimal descuentoCompraConAbonos = null;
        if (aplicarDescuento){
            try{
                ventana_padre.compruebaAutorizacion(Operaciones.DESCUENTO_COMPRA_ABONOS);
                while (descuentoCompraConAbonos == null || descuentoCompraConAbonos.compareTo(BigDecimal.ZERO)<0){
                    descuentoCompraConAbonos = JPrincipal.crearVentanaIngresarValorDecimal("Indique el descuento que desea aplicar:", Numero.CIEN);
                }
            }
            catch(SinPermisosException e){
            }
        }
        
        log.debug("Creación de la ventana de liquidación...");
        v_liquidacion_parcial.getContentPane().removeAll();

        javax.swing.GroupLayout v_liquidacionParcialLayout = new javax.swing.GroupLayout(v_liquidacion_parcial.getContentPane());
        v_liquidacion_parcial.getContentPane().setLayout(v_liquidacionParcialLayout);
        v_liquidacionParcialLayout.setHorizontalGroup(
                v_liquidacionParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v_liquidacionParcialLayout.createSequentialGroup().addContainerGap().addComponent(p_liquidacion_parcial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        v_liquidacionParcialLayout.setVerticalGroup(
                v_liquidacionParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_liquidacionParcialLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_liquidacion_parcial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        p_liquidacion_parcial.setContenedor(v_liquidacion_parcial);
        v_liquidacion_parcial.getContentPane().add(p_liquidacion_parcial);
        p_liquidacion_parcial.iniciaVista();
        p_liquidacion_parcial.setDescuento(descuentoCompraConAbonos);
        p_liquidacion_parcial.setContenedor(v_liquidacion_parcial);
        v_liquidacion_parcial.setVisible(true);
        

    }

    private void accionComprarArticulo() {
        if (b_comprar_articulo.isEnabled()) {
            if (!reservacion.isCanastilla()) {
                accionSeleccionarInvitado();
            }
            if (reservacion.getInvitadoSeleccionado() != null || reservacion.isCanastilla()) {
                try {


                    ventana_padre.addIdentificaClienteReservacionView(reservacion, JReservacionesCliente.OPERACIONRESERVAPAGOARTICULO);
                    ventana_padre.showView("identifica_cliente_reservacion");

                    // Abrir pantalla de pagos para la reserva
                    //ventana_padre.irPagoArticulos(reservacion, JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS);
                }
                catch (IndexOutOfBoundsException e) {
                    log.debug("No se ha seleccionado ningún artículo");
                    ventana_padre.crearAdvertencia("Debe seleccionar un Artículo. ");
                }
                catch (Exception e) {
                    log.error("Error abonando artículo: " + e.getMessage(), e);
                    ventana_padre.crearError(null);
                }
            }
        }
    }

    private void liquidar(List<ReservaArticuloBean> lista) throws ReservasException, TicketPrinterException {
        BigDecimal abonosRestantesReales = reservacion.getAbonosRestantesReales();
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
                    porAbonar = (totalImporte.subtract(totalPagado)).subtract(restanteAbonos);
                    t_comprado.setText(totalPagado.toString());
                    t_restante_abonos.setText(restanteAbonos.toString());
                    t_por_abonar.setText(porAbonar.toString());

                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + porAbonar);
                }
                catch (ReservasException ex) {
                    log.error("Error al modificar la reserva en la liquidación", ex);
                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + porAbonar);
                    ventana_padre.crearError(ex.getMessage());
                }
            }
        }
        reservacion.setAbonosRestantes(restanteAbonos);       
        reservacion.getReservacion().setLiquidado(true);
        reservacion.getReservacion().setFechaLiquidacion(new Fecha());
        reservacion.setAbonosRestantesReales(abonosRestantesReales);
        ReservacionesServicios.modificarReserva(reservacion.getReservacion());
        log.debug("Iniciando impresión del ticket");
        PrintServices.getInstance().limpiarListaDocumentos();
        TicketS facturaArticulosReserva = ReservacionesServicios.facturaArticulosReserva(reservacion, true);
        try {
            DocumentosService.crearDocumento(facturaArticulosReserva, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
        } catch (DocumentoException ex) {
            log.error("liquidar() - No se pudo guardar el documento en la BBDD");
        }
        PrintServices.getInstance().limpiarListaDocumentos();
        log.debug("Finalizada impresión del ticket");
    }

    private void rematarConComprobante() throws ReservasException, TicketPrinterException {
        try {
            reservacion.getReservacion().setLiquidado(true);
            ReservacionesServicios.modificarReserva(reservacion.getReservacion());
            log.debug("Iniciando impresión del ticket");
            ReservacionesServicios.comprobanteArticulosReserva(reservacion, listaArticulos);
            log.debug("Finalizada impresión del ticket");
        }
        catch (TicketException ex) {
            log.error("Error al crear comprobante de remate de reserva: " + ex.getMessage(), ex);
            ventana_padre.crearError("No se pudo imprimir el comprobante de remate de reserva");
        }
        catch (DocumentoException ex) {
            log.error("Error al crear comprobante de remate de reserva: " + ex.getMessage(), ex);
            ventana_padre.crearError("No se pudo imprimir el comprobante de remate de reserva");           
        }
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
    }

    private void accionListarAbons() {
        if (b_listar_abonos.isEnabled()) {
            crearPantallaAbono();
            v_listarAbonos.setVisible(true);
        }
    }

    private void crearPantallaAbono() {
        // construimos el nuevo panel de lista de abonos
        p_listarAbonos = new com.comerzzia.jpos.gui.reservaciones.JReservacionListaAbonos(reservacion);

        // creamos el layout para insertar nuestro panel en la ventana
        javax.swing.GroupLayout v_listarAbonosLayout = new javax.swing.GroupLayout(v_listarAbonos.getContentPane());
        v_listarAbonos.getContentPane().setLayout(v_listarAbonosLayout);
        v_listarAbonosLayout.setHorizontalGroup(
                v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_listarAbonosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_listarAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        v_listarAbonosLayout.setVerticalGroup(
                v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_listarAbonosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_listarAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

        p_listarAbonos.setContenedor(v_listarAbonos);
    }

    private void accionInicializarOpcionesInvitado() {

        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteCompra()
                && !this.reservacion.getReservacion().getCancelado()
                && !this.reservacion.isTodosArticulosComprados()) {
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){
                b_comprar_articulo.setEnabled(true);
            }
        }

        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()
                && !this.reservacion.getReservacion().getCancelado()) {
            b_abonos_propios.setEnabled(true);
        }


    }

    private void accionSeleccionarInvitado() {

        v_seleccionar_invitado.setLocationRelativeTo(null);
        p_seleccionar_invitado.setContenedor(v_seleccionar_invitado);
        p_seleccionar_invitado.iniciaVista();
        v_seleccionar_invitado.setVisible(true);

        if (reservacion.getInvitadoSeleccionado() != null) {
            lb_invitado_seleccionado.setText("Invitado : " + reservacion.getInvitadoSeleccionado().getNombre() + " " + reservacion.getInvitadoSeleccionado().getApellido());
            accionInicializarOpcionesInvitado();
        }

    }

    private void accionDetalleArticulo(ArticuloReservado articulo) {
        p_detalle_articulo.setArticuloReservado(articulo, isPermiteEdicionPropietario);
        p_detalle_articulo.setReservacion(reservacion);
        v_detalle_articulo.setVisible(true);
        refrescarReservacion();
    }

    private void accionAddArticulos() {
        try {
            if (b_add_articulos.isEnabled()) {
                // Creamos una ventana de ventas en modo ADD               
                if (compruebaUsuarioPropietario()) {
                    ventana_padre.irPagoArticulos(reservacion, JReservacionesVentasCanBaby.MODO_ADD);
                }
            }
        }
        catch (Exception e) {
            log.error("Error añadiendo artículos a la reserva: " + e.getMessage(), e);
            ventana_padre.crearError("Error añadiendo artículos a "+STIPO+": ");
        }
    }

    protected void refrescarReservacion() {
        try {
            int indiceSeleccionado = new Integer(tb_articulos.getSelectionModel().getMaxSelectionIndex());
            reservacion.refrescar();
            refrescarTotales();
            refrescarTabla();
            tb_articulos.getSelectionModel().setSelectionInterval(indiceSeleccionado, indiceSeleccionado);
            habilitaoDesPagoArticulos();
        }
        catch (ReservasException ex) {
            ventana_padre.crearError(ex.getMessage());
        }
    }

    private void refrescarTotales() {
        //Rellenamos los datos de los abonos
        reservacion.calculaTotales();

        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        BigDecimal comprado = new BigDecimal(BigInteger.ZERO);
        totalAbonado = new BigDecimal(BigInteger.ZERO);
        porAbonar = new BigDecimal(BigInteger.ZERO);
        // Guardamos pagado en la variable de clase
        this.totalPagado = comprado;
        for (ReservaArticuloBean art : reservacion.getReservacion().getReservaArticuloList()) {
            total = total.add(art.getPrecioTotal());
            if (art.getComprado()) {
                comprado = comprado.add(art.getPrecioTotal());
            }

        }
        // Guardamos el Importe total de la reservación en la variable de clase
        this.totalImporte = total;
        List<ReservaAbonoBean> abonos = this.reservacion.getReservacion().getReservaAbonoList();
        totalAbonado = reservacion.getTotalAbonado();
        restanteAbonos = reservacion.getAbonosRestantes();
        porAbonar = (total.subtract(comprado)).subtract(restanteAbonos);

        t_comprado.setText(comprado.toString());
        t_n_abonos.setText(String.valueOf(abonos.size()));
        t_total_reserva.setText(total.toString());
        t_total_abonado.setText(totalAbonado.toString());

        tb_articulos.setDefaultRenderer(Object.class, new MostrarReservacionesCanBabyCellRenderer1());
        refrescarTabla();

        t_restante_abonos.setText(restanteAbonos.toString());
        t_por_abonar.setText(porAbonar.toString());
    }

    private void accionEliminarArticulo() {
        try {
            String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);
            log.debug("Inicio operación De eliminación de artículo de "+STIPO);
            if (compruebaUsuarioPropietario()) {
                if (tb_articulos.getSelectedRow() >= 0) {
                    lineaSelecionada = tb_articulos.getSelectedRow();
                }
                else {
                    lineaSelecionada = listaArticulos.size() - 1;
                }
                if (lineaSelecionada >= 0 && ventana_padre.crearVentanaConfirmacion("Esta acción Eliminará el artículo de "+STIPO+", ¿Desea continuar?")) {
                    p_valor.iniciaFoco();
                    v_valor.setVisible(true);
                    //int indiceSeleccionado = new Integer(tb_articulos.getSelectionModel().getMaxSelectionIndex());
                    ArticuloReservado articuloReservado = listaArticulos.get(lineaSelecionada);
                    int numeroElim = 0;
                    int disponibles = articuloReservado.getCantidadReservados() - articuloReservado.getCantidadComprados();
                    if (p_valor.getCantidad() > 0) {
                        if (disponibles >= p_valor.getCantidad()) {
                            reservacion.removeArticulosReserva(reservacion, articuloReservado.getArticulo(), p_valor.getCantidad());
                            refrescarReservacion();
                            if (reservacion.getReservacion().getReservaArticuloList().isEmpty() && reservacion.isCanastilla()) {
                                reservacion.getReservacion().setCancelado(true);
                                reservacion.getReservacion().setFechaLiquidacion(new Fecha());
                                try {
                                    ReservacionesServicios.modificarReserva(reservacion.getReservacion());
                                    ServicioLogAcceso.crearAccesoLogCancelarReserva(compruebaAutorizacion, reservacion.getReservacion());
                                    ventana_padre.showView("ident-cliente");
                                    ventana_padre.crearAdvertencia("Se anuló "+STIPO+" por no poseer artículos");
                                }
                                catch (ReservasException ex) {
                                    ventana_padre.crearError("Se eliminó el artículo, pero hubo un error al anular "+STIPO);
                                    log.error("accionEliminarArticulo() - Error actualizando "+STIPO, ex);
                                }

                            }
                        }
                        else {
                            getVentana_padre().crearError("El número de artículos a eliminar de "+STIPO+" no puede superar al número de artículos reservados no comprados");
                        }
                    }
                    else {
                        getVentana_padre().crearError("Debe introducir un valor numérico válido");
                    }
                }
            }
        }
        catch (SinPermisosException ex) {
            log.warn("No se tiene permisos para anular "+STIPO);
        }
    }

    private boolean compruebaUsuarioPropietario() {
        if (reservacion.getInvitadoActivo() == null) {
            v_autentificarCliente.setVisible(true);
            try {
                reservacion.autenticaPropietario(p_autentificarCliente.getCliente());
            }
            catch (ClienteException e) {
                ventana_padre.crearError(e.getMessage());
            }
        }
        return (reservacion.getInvitadoActivo() != null);
    }

    public void realizarAbono() {
        accionAbono();
    }

    private void accionAbono() {
        try {
            BigDecimal abonoMinimo = null;
            
            if (reservacion.isCanastilla() && !reservacion.getInvitadoActivo().isSocio() && reservacion.getTicket() == null) {
                reservacion.setTicket(new TicketS());
            }            
            abonoMinimo = reservacion.getReservacion().getCuotaInicial();
            if (abonoMinimo == null){
                abonoMinimo = BigDecimal.ZERO;
            }
            if (this.reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()) {
                log.debug("El tipo de pago: " + this.reservacion.getReservacion().getReservaTipo().getCodTipo() + " permite abonos parciales.");
            }
                       
            crearVentanaPagos(abonoMinimo,  new BigDecimal("1000"));            
            // cuando sale de la ventana importe
            if (p_pagos.getPagoRealizado() != null) {
                BigDecimal importeAAbonar;
                importeAAbonar = p_pagos.getPagoRealizado();
                log.debug("Importe a pagar introducido: " + importeAAbonar.toString());

                if (p_pagos.isCancelado()) {
                }
            }
        }
        catch (Exception ex) {
            log.error("Error realizando abono: " + ex.getMessage(), ex);
            ventana_padre.crearError("Ocurrió un error realizando el abono.");
        }
    }

    private void estableceDatosGenerales() {
        t_cli_cliente_nombre.setText(reservacion.getReservacion().getCliente().getNombreImpresion());
        t_cli_apellidos.setText(reservacion.getReservacion().getCliente().getApellidoImpresion());

        t_cli_direccion.setText(reservacion.getReservacion().getCliente().getDireccion());

        if (reservacion.getReservacion().getCliente().getTelefono1() != null && !reservacion.getReservacion().getCliente().getTelefono1().isEmpty()) {
            t_cli_telefono.setText(reservacion.getReservacion().getCliente().getTelefono1());
        }
        else if (reservacion.getReservacion().getCliente().getTelefonoMovil() != null && !reservacion.getReservacion().getCliente().getTelefonoMovil().isEmpty()) {
            t_cli_telefono.setText(reservacion.getReservacion().getCliente().getTelefonoMovil());
        }
        else {
            t_cli_telefono.setText("");
        }

        t_cli_documento.setText(reservacion.getReservacion().getCliente().getTipoIdentificacion() + " - " + reservacion.getReservacion().getCliente().getIdentificacion());
    }

    private void habilitaoDesPagoArticulos() {
        if (!this.reservacion.getReservacion().getLiquidado()
                && isPermiteEdicionPropietario
                && !this.reservacion.getReservacion().getCancelado()
                && !this.reservacion.isTodosArticulosComprados()) {
            if(this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())){
                b_comprar_articulo.setEnabled(true);
            }
        }
        else {
            b_comprar_articulo.setEnabled(false);
        }
    }
}