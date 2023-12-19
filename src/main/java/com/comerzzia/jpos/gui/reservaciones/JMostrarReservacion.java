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

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.reservaciones.modelos.MostrarReservacionesCellRenderer;
import com.comerzzia.jpos.gui.reservaciones.modelos.MostrarReservacionesTableModel;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.util.imagenes.Imagenes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author Admin
 */
public class JMostrarReservacion extends JPanelImagenFondo implements IVista, IPagoImporteCall {

    private static Logger log = Logger.getMLogger(JMostrarReservacion.class);
    private JPrincipal ventana_padre;
    private Reservacion reservacion;
    private BigDecimal totalPagado;
    private BigDecimal totalImporte;
    private BigDecimal totalAbonadoReal;  // Total abonado sin descuentos
    private BigDecimal totalAbonado;
    private BigDecimal restanteAbonos;
    private BigDecimal porAbonar;
    JReservacionListaAbonos p_listarAbonos;
    JReservacionListaArticulos p_listarArticulos;
    int lineaSelecionada;
    ArticulosServices articulosServices = ArticulosServices.getInstance();
    TarifasServices tarifasServices = new TarifasServices();

    /**
     * Creates new form JMostrarReservacion
     */
    public JMostrarReservacion() {
        super();
        initComponents();
    }

    public JMostrarReservacion(Reservacion reservacionEnt) {
        super();
        // Logs
        log.debug("MOSTRANDO RESERVACION " + reservacionEnt.getReservacion().getUidReservacion());
        log.info("Código :" + reservacionEnt.getReservacion().getReservaTipo().getCodTipo());
        log.info("Descripción :" + reservacionEnt.getReservacion().getReservaTipo().getDesTipo());
        log.info("Liquidado :" + reservacionEnt.getReservacion().getLiquidado());
        log.info("Procesado :" + reservacionEnt.getReservacion().getProcesado());
        log.info("Cancelada :" + reservacionEnt.getReservacion().getCancelado());

        this.reservacion = reservacionEnt;
        initComponents();
        String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
        // Imagen de fondo
        try {

            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_buscar_plan_novios.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
            log.error("No se pudo cargar la imagen de fondo de cierre de caja");
        }

        Imagenes.cambiarImagenPublicidad(jLabel30);

        // Inicializamos el total real abonado
        reservacion.calculaTotales();
        lb_abono_realizado.setText("( $" + reservacion.getAbonosReales().toString() + " con descuentos)");

        Cliente cliente = reservacion.getReservacion().getCliente();

        //Rellenamos los datos del cliente
        String tipoDocumento = cliente.getTipoIdentificacion();
        if (tipoDocumento.equals("CED")) {
            tipoDocumento = "Cédula:";
        } else if (tipoDocumento.equals("PAS")) {
            tipoDocumento = "Pasaporte:";
        } else if (tipoDocumento.equals("RUC")) {
            tipoDocumento = "RUC:";
        }
        JNombre.setText(cliente.getNombre() + " " + cliente.getApellido());
        Jafiliado.setText(cliente.getCodigoTarjetaBabysClub());
        JTipoDocumento.setText(tipoDocumento);
        JDocumento.setText(cliente.getIdentificacion());

        String tipoReserva = reservacion.getReservacion().getReservaTipo().getDesTipo().toLowerCase();
        tipoReserva = tipoReserva.substring(0, 1).toUpperCase() + tipoReserva.substring(1, tipoReserva.length());
        //Rellenamos los datos de la reserva
        JTipoReservacion.setText(tipoReserva);
        Fecha alta = reservacion.getReservacion().getFechaAlta();
        JFechaAlta.setText(alta.toString());
        Fecha fin = reservacion.getReservacion().getCaducidad();
        JFechaFin.setText(fin.toString());
        JEstado.setText(reservacion.getReservacion().getEstado());

        // Hay que considerar posibles reservas que se han hecho con el sistema y que no disponen del nuevo campo
        if (reservacion.getReservacion().getCodReservacion() != null) {
            lb_num_res.setText(reservacion.getReservacion().getCodReservacion().toString());
        }

        //Rellenamos los datos de los abonos
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
        List<ReservaAbonoBean> abonos = reservacionEnt.getReservacion().getReservaAbonoList();
        for (ReservaAbonoBean abono : abonos) {
            totalAbonado = totalAbonado.add(abono.getCantidadAbono());
        }
        restanteAbonos = reservacion.getAbonosRestantes();
        porAbonar = (total.subtract(comprado)).subtract(restanteAbonos);

        t_comprado.setText(comprado.toString());
        t_n_abonos.setText(String.valueOf(abonos.size()));
        t_total_reserva.setText(total.toString());
        t_total_abonado.setText(totalAbonado.toString());

        tb_articulos.setDefaultRenderer(Object.class, new MostrarReservacionesCellRenderer());
        refrescarTabla();

        t_restante_abonos.setText(restanteAbonos.toString());
        t_por_abonar.setText(porAbonar.toString());

        URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);
        v_importe_a_pagar.setIconImage(icon.getImage());
        v_pagos.setIconImage(icon.getImage());
        v_seleccionar_invitado.setIconImage(icon.getImage());
        v_liquidacion_parcial.setIconImage(icon.getImage());
        v_listarAbonos.setIconImage(icon.getImage());
        v_listarAbonos.setLocationRelativeTo(null);
        v_autentificarCliente.setIconImage(icon.getImage());
        p_autentificarCliente.setContenedor(v_autentificarCliente);
        v_autentificarCliente.setLocationRelativeTo(null);

        crearAccionFocoTabla(this, tb_articulos, KeyEvent.VK_T, InputEvent.CTRL_MASK);
        iniciaVista();
    }

    private void refrescarTabla() {
        List<ReservaArticuloBean> articulos = reservacion.getReservacion().getReservaArticuloList();
        MostrarReservacionesTableModel modelo = new MostrarReservacionesTableModel(articulos);
        tb_articulos.setModel(modelo);
    }

    public JMostrarReservacion(Reservacion reservacion, JPrincipal ventana_padre) {
        this(reservacion);
        this.ventana_padre = ventana_padre;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        v_importe_a_pagar = new javax.swing.JDialog();
        p_importe_a_pagar = new com.comerzzia.jpos.gui.reservaciones.JReservacionesImportePago();
        v_seleccionar_invitado = new javax.swing.JDialog();
        p_seleccionar_invitado = new JSeleccionarInvitado(this, reservacion);
        v_liquidacion_parcial = new javax.swing.JDialog();
        v_listarAbonos = new javax.swing.JDialog();
        v_autentificarCliente = new javax.swing.JDialog();
        p_autentificarCliente = new com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente();
        jDialog1 = new javax.swing.JDialog();
        JTipoReservacion = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        JNombre = new javax.swing.JLabel();
        JDocumento = new javax.swing.JLabel();
        Jafiliado = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JFechaAlta = new javax.swing.JLabel();
        JFechaFin = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        t_total_reserva = new javax.swing.JTextField();
        t_comprado = new javax.swing.JTextField();
        t_n_abonos = new javax.swing.JTextField();
        t_total_abonado = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_articulos = new javax.swing.JTable();
        b_abonos_propios = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_menu_ppal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel30 = new javax.swing.JLabel();
        b_comprar_articulo = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_liquidar_reserva = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        t_restante_abonos = new javax.swing.JTextField();
        b_cancelar_reserva = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        JTipoDocumento = new javax.swing.JLabel();
        t_por_abonar = new javax.swing.JTextField();
        JEstado = new javax.swing.JLabel();
        lb_nombre = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lb_num_res = new javax.swing.JLabel();
        jButtonForm1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel25 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lb_porAbonar = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lb_abonos_disponibles = new javax.swing.JLabel();
        lb_numero_afiliado = new javax.swing.JLabel();
        lb_abono_realizado = new javax.swing.JLabel();
        b_add_articulos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_liquidar_reservacion = new com.comerzzia.jpos.gui.components.form.JButtonForm();

        v_pagos.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

        v_seleccionar_invitado.setAlwaysOnTop(true);
        v_seleccionar_invitado.setMinimumSize(new java.awt.Dimension(719, 460));
        v_seleccionar_invitado.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_seleccionar_invitado.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

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

        jDialog1.setResizable(false);
        jDialog1.setSize(new java.awt.Dimension(600, 450));

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JTipoReservacion.setFont(JTipoReservacion.getFont().deriveFont(JTipoReservacion.getFont().getStyle() | java.awt.Font.BOLD, 16));
        JTipoReservacion.setText("Datos Reservación");
        add(JTipoReservacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, -1, -1));
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 98, -1, -1));

        JNombre.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JNombre.setText("Nombre y Apellidos");
        add(JNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 259, -1));

        JDocumento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JDocumento.setText("RUC,CED,PAS y Número");
        add(JDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 185, -1));

        Jafiliado.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Jafiliado.setText("Numero");
        add(Jafiliado, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 180, -1));

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel11.setText("Cliente");
        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, -1, -1));

        JFechaAlta.setText("jLabel10");
        add(JFechaAlta, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 90, 10));

        JFechaFin.setText("jLabel10");
        add(JFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 180, 90, 10));

        jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel14.setText("Abonos");
        add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, -1, -1));

        t_total_reserva.setText("jTextField1");
        t_total_reserva.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_reserva.setEnabled(false);
        add(t_total_reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 90, -1));

        t_comprado.setText("jTextField1");
        t_comprado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_comprado.setEnabled(false);
        add(t_comprado, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 90, -1));

        t_n_abonos.setText("jTextField1");
        t_n_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_n_abonos.setEnabled(false);
        add(t_n_abonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 230, 70, -1));

        t_total_abonado.setText("jTextField1");
        t_total_abonado.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_abonado.setEnabled(false);
        add(t_total_abonado, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 260, 70, -1));

        jLabel19.setFont(jLabel19.getFont().deriveFont(jLabel19.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel19.setText("Artículos");
        add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 90, -1));

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
        tb_articulos.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tb_articulos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_articulosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_articulos);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 360, 580, 210));

        b_abonos_propios.setText("<html><center>Realizar<br>Abono<br>F3</center></html>");
        b_abonos_propios.setPreferredSize(new java.awt.Dimension(127, 48));
        b_abonos_propios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_abonos_propiosActionPerformed(evt);
            }
        });
        add(b_abonos_propios, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 633, 90, 80));

        b_menu_ppal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_menu_ppal.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_menu_ppal.setNextFocusableComponent(tb_articulos);
        b_menu_ppal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menu_ppalActionPerformed(evt);
            }
        });
        add(b_menu_ppal, new org.netbeans.lib.awtextra.AbsoluteConstraints(788, 633, 90, 80));

        jLabel30.setFocusable(false);
        add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 70, 138, 563));

        b_comprar_articulo.setText("<html><center>Comprar<br>Artículo<br>F4 </center></html>");
        b_comprar_articulo.setPreferredSize(new java.awt.Dimension(127, 48));
        b_comprar_articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_comprar_articuloActionPerformed(evt);
            }
        });
        add(b_comprar_articulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 633, 90, 80));

        b_liquidar_reserva.setLabel("<html><center>Rematar reservación<br>F5</center></html>");
        b_liquidar_reserva.setPreferredSize(new java.awt.Dimension(127, 48));
        b_liquidar_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_liquidar_reservaActionPerformed(evt);
            }
        });
        add(b_liquidar_reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 633, 90, 80));

        t_restante_abonos.setText("jTextField1");
        t_restante_abonos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_restante_abonos.setEnabled(false);
        add(t_restante_abonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 230, 80, -1));

        b_cancelar_reserva.setLabel("<html><center>Anular reservación<br>F6</center></html>");
        b_cancelar_reserva.setPreferredSize(new java.awt.Dimension(127, 48));
        b_cancelar_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_reservaActionPerformed(evt);
            }
        });
        add(b_cancelar_reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 633, 90, 80));

        JTipoDocumento.setFont(JTipoDocumento.getFont().deriveFont(JTipoDocumento.getFont().getStyle() | java.awt.Font.BOLD));
        JTipoDocumento.setForeground(new java.awt.Color(51, 153, 255));
        JTipoDocumento.setText("Tipo documento:");
        add(JTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 100, -1));

        t_por_abonar.setText("jTextField1");
        t_por_abonar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_por_abonar.setEnabled(false);
        add(t_por_abonar, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 260, 80, -1));

        JEstado.setFont(JEstado.getFont().deriveFont(JEstado.getFont().getStyle() | java.awt.Font.BOLD));
        JEstado.setText("Estado");
        add(JEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, 60, 10));

        lb_nombre.setFont(lb_nombre.getFont().deriveFont(lb_nombre.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre.setForeground(new java.awt.Color(51, 153, 255));
        lb_nombre.setText("Nombre: ");
        add(lb_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 57, -1));

        jLabel22.setFont(jLabel22.getFont().deriveFont(jLabel22.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel22.setForeground(new java.awt.Color(51, 153, 255));
        jLabel22.setText("Fecha Fin:");
        jLabel22.setToolTipText("");
        add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, 63, 10));

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel10.setForeground(new java.awt.Color(51, 153, 255));
        jLabel10.setText("Fecha de Alta:");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 89, 10));

        lb_num_res.setFont(lb_num_res.getFont().deriveFont(lb_num_res.getFont().getStyle() | java.awt.Font.BOLD));
        lb_num_res.setText("NumRes");
        add(lb_num_res, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 180, 80, 10));

        jButtonForm1.setText("<html><center>Listar Abonos <br/>F7</center></html>");
        jButtonForm1.setMaximumSize(new java.awt.Dimension(94, 48));
        jButtonForm1.setMinimumSize(new java.awt.Dimension(94, 48));
        jButtonForm1.setPreferredSize(new java.awt.Dimension(94, 48));
        jButtonForm1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonForm1ActionPerformed(evt);
            }
        });
        add(jButtonForm1, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 633, 90, 80));

        jLabel25.setFont(jLabel25.getFont().deriveFont(jLabel25.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel25.setForeground(new java.awt.Color(51, 153, 255));
        jLabel25.setText("Total comprado:");
        add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 108, -1));

        jLabel24.setFont(jLabel24.getFont().deriveFont(jLabel24.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel24.setForeground(new java.awt.Color(51, 153, 255));
        jLabel24.setText("Total reservación:");
        add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 108, -1));

        jLabel26.setFont(jLabel26.getFont().deriveFont(jLabel26.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel26.setForeground(new java.awt.Color(51, 153, 255));
        jLabel26.setText("Nº abonos:");
        add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 68, -1));

        jLabel27.setFont(jLabel27.getFont().deriveFont(jLabel27.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel27.setForeground(new java.awt.Color(51, 153, 255));
        jLabel27.setText("Total abonos:");
        add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 82, -1));

        jLabel33.setFont(jLabel33.getFont().deriveFont(jLabel33.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel33.setForeground(new java.awt.Color(51, 153, 255));
        jLabel33.setText("Estado:");
        add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, 54, 10));

        lb_porAbonar.setFont(lb_porAbonar.getFont().deriveFont(lb_porAbonar.getFont().getStyle() | java.awt.Font.BOLD));
        lb_porAbonar.setForeground(new java.awt.Color(51, 153, 255));
        lb_porAbonar.setText("Por abonar:");
        add(lb_porAbonar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, 94, -1));

        jLabel34.setFont(jLabel34.getFont().deriveFont(jLabel34.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel34.setForeground(new java.awt.Color(51, 153, 255));
        jLabel34.setText("Núm. Reserva:");
        add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 90, 10));
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 98, -1, -1));

        lb_abonos_disponibles.setFont(lb_abonos_disponibles.getFont().deriveFont(lb_abonos_disponibles.getFont().getStyle() | java.awt.Font.BOLD));
        lb_abonos_disponibles.setForeground(new java.awt.Color(51, 153, 255));
        lb_abonos_disponibles.setText("Abonos disponibles:");
        add(lb_abonos_disponibles, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 230, 120, -1));

        lb_numero_afiliado.setFont(lb_numero_afiliado.getFont().deriveFont(lb_numero_afiliado.getFont().getStyle() | java.awt.Font.BOLD));
        lb_numero_afiliado.setForeground(new java.awt.Color(51, 153, 255));
        lb_numero_afiliado.setText("Nº Afiliado: ");
        add(lb_numero_afiliado, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 81, -1));

        lb_abono_realizado.setFont(lb_abono_realizado.getFont().deriveFont(lb_abono_realizado.getFont().getSize()-1f));
        lb_abono_realizado.setText("( Real )");
        add(lb_abono_realizado, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, 200, 20));

        b_add_articulos.setText("<html><center>Añadir Artículos<br/>F8</center></html>");
        b_add_articulos.setEnabled(false);
        b_add_articulos.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_add_articulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_add_articulosActionPerformed(evt);
            }
        });
        add(b_add_articulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 633, 90, 80));

        b_liquidar_reservacion.setText("<html><center>Liquidar<br>reservación<br>F10</center></html>");
        b_liquidar_reservacion.setPreferredSize(new java.awt.Dimension(127, 48));
        b_liquidar_reservacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_liquidar_reservacionActionPerformed(evt);
            }
        });
        add(b_liquidar_reservacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(692, 633, 90, 80));
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
    if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
        tb_articulos.transferFocusBackward();
    } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
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

                if ((totalImporte.subtract(totalPagado).compareTo(totalAbonado) > 0) && !articulosNoComprados.isEmpty()) {
                    //mostrar ventana liquidacion parcial
                    v_liquidacion_parcial.setLocationRelativeTo(null);

                    JReservacionesLiquidacionParcial p_liquidacion_parcial = new JReservacionesLiquidacionParcial(articulosNoComprados, totalAbonado);
                    crearVentanaLiquidacion(p_liquidacion_parcial);
                    if (!p_liquidacion_parcial.isCancelado()) {
                        liquidar(p_liquidacion_parcial.getArticulosSeleccionados());
                        ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());
                        if (reservacion.getAbonosRestantesReales().compareTo(BigDecimal.ZERO) > 0) {
                            try {
                                //ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                                BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                            } catch (Exception ex) {
                                log.error("Error creando bono en liquidación", ex);
                                ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                            }
                        }
                        b_add_articulos.setEnabled(false);
                    }
                } else {
                    liquidar(reservacion.getReservacion().getReservaArticuloList());
                    ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());
                    if (reservacion.getAbonosRestantesReales().compareTo(BigDecimal.ZERO) > 0) {
                        try {
                            //ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                            BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                        } catch (Exception ex) {
                            log.error("Error creando bono en liquidación");
                            ventana_padre.crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                        }
                    }
                    b_add_articulos.setEnabled(false);
                }
                refrescarTabla();
                JEstado.setText(reservacion.getReservacion().getEstado());
                iniciaVista();
            } catch (SinPermisosException ex) {
                log.error("Error de permisos al seleccionar cliente.", ex);
            } catch (TicketPrinterException ex) {
                log.error("Error imprimiendo ticket de reservación: " + ex.getMessage());
                ventana_padre.crearError(ex.getMessage());
            } catch (ReservasException ex) {
                log.error("Error liquidando reservación: " + ex.getMessage());
                ventana_padre.crearError(ex.getMessage());
            }
        }
    }
}//GEN-LAST:event_b_liquidar_reservaActionPerformed

private void b_cancelar_reservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_reservaActionPerformed
    if (b_cancelar_reserva.isEnabled()) {
        if (compruebaUsuarioPropietario()) {
            try {
                String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);

                if (ventana_padre.crearVentanaConfirmacion("¿Está seguro que desea anular la reservación?")) {
                    //Desea cancelar.
                    try {
                        ServicioLogAcceso.crearAccesoLogCancelarReserva(compruebaAutorizacion, reservacion.getReservacion());

                        b_add_articulos.setEnabled(false);
                        reservacion.getReservacion().setCancelado(true);
                        reservacion.getReservacion().setProcesadoTienda(false);
                        reservacion.getReservacion().setFechaLiquidacion(new Fecha());
                        ReservacionesServicios.modificarReserva(reservacion.getReservacion());

                        reservacion.setAbonosRestantes(restanteAbonos);
                        try {
                            if (reservacion.getAbonosRestantesReales().compareTo(BigDecimal.ZERO) > 0) {
                                BonosServices.crearBonoReserva(reservacion.getAbonosRestantesReales(), reservacion.getReservacion().getCodReservacion().toString(), BonosServices.PROCEDENCIA_RESERVA_CANCELACION, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());
                            } else {
                                //G.S. Cuando se anula la reserva afecta al kardex
                                ReservacionesServicios.generarKardexReserva(reservacion.getReservacion(), EnumTipoDocumento.BONO);
                            }
                            // Actualizamos los stocks
                            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)) {
                                accionActualizarStock();
                            }
                        } catch (Exception ex) {
                            log.error("Error creando bono en liquidación");
                            reservacion.getReservacion().setCancelado(false);
                            reservacion.getReservacion().setProcesadoTienda(false);
                            reservacion.getReservacion().setFechaLiquidacion(null);
                            ReservacionesServicios.modificarReserva(reservacion.getReservacion());
                            ventana_padre.crearError("Error : " + ex.getMessage());
                            //crearInformacion("Expedir bono por valor de: $ " + restanteAbonos.toString());
                        }

                        JEstado.setText(reservacion.getReservacion().getEstado());
                        iniciaVista();
                    } catch (ReservasException ex) {
                        log.error("Error al anular la reserva");
                        ventana_padre.crearAdvertencia(ex.getMessage());
                    } catch (Exception ex) {
                        log.error("Error al anular la reservación: " + ex.getMessage(), ex);
                        ventana_padre.crearError("Error al anular la reservación.");

                    }
                } else {
                    //No desea cancelar.
                }
            } catch (SinPermisosException ex) {
                log.error("Error de permisos al seleccionar cliente.", ex);
            }

        }
    }
}//GEN-LAST:event_b_cancelar_reservaActionPerformed

private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
    p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

private void jButtonForm1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonForm1ActionPerformed
    accionListarAbons();
}//GEN-LAST:event_jButtonForm1ActionPerformed

private void v_listarAbonosWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_listarAbonosWindowGainedFocus
    p_listarAbonos.iniciaFoco();
}//GEN-LAST:event_v_listarAbonosWindowGainedFocus

private void b_add_articulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_add_articulosActionPerformed
    if (b_add_articulos.isEnabled()) {
        accionAddArticulos();
    }
}//GEN-LAST:event_b_add_articulosActionPerformed

private void v_autentificarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v_autentificarClienteFocusGained
    p_autentificarCliente.iniciaFoco();
}//GEN-LAST:event_v_autentificarClienteFocusGained

    private void b_liquidar_reservacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_liquidar_reservacionActionPerformed
        if (b_liquidar_reservacion.isEnabled()) {
            accionLiqudarReserva();
        }
    }//GEN-LAST:event_b_liquidar_reservacionActionPerformed

    private void accionMenu() {
        try {
            ventana_padre.mostrarMenu();
        } catch (Exception e) {
            log.error("No se pudo mostrar el menú desde el detalle de la reservación");
            ventana_padre.crearError(null);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JDocumento;
    private javax.swing.JLabel JEstado;
    private javax.swing.JLabel JFechaAlta;
    private javax.swing.JLabel JFechaFin;
    private javax.swing.JLabel JNombre;
    private javax.swing.JLabel JTipoDocumento;
    private javax.swing.JLabel JTipoReservacion;
    private javax.swing.JLabel Jafiliado;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_abonos_propios;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_add_articulos;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar_reserva;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_comprar_articulo;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_liquidar_reserva;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_liquidar_reservacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_menu_ppal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jButtonForm1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_abono_realizado;
    private javax.swing.JLabel lb_abonos_disponibles;
    private javax.swing.JLabel lb_nombre;
    private javax.swing.JLabel lb_num_res;
    private javax.swing.JLabel lb_numero_afiliado;
    private javax.swing.JLabel lb_porAbonar;
    private com.comerzzia.jpos.gui.reservaciones.JAutentificaCliente p_autentificarCliente;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesImportePago p_importe_a_pagar;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV p_pagos;
    private com.comerzzia.jpos.gui.reservaciones.JSeleccionarInvitado p_seleccionar_invitado;
    private javax.swing.JTextField t_comprado;
    private javax.swing.JTextField t_n_abonos;
    private javax.swing.JTextField t_por_abonar;
    private javax.swing.JTextField t_restante_abonos;
    private javax.swing.JTextField t_total_abonado;
    private javax.swing.JTextField t_total_reserva;
    private javax.swing.JTable tb_articulos;
    private javax.swing.JDialog v_autentificarCliente;
    private javax.swing.JDialog v_importe_a_pagar;
    private javax.swing.JDialog v_liquidacion_parcial;
    private javax.swing.JDialog v_listarAbonos;
    private javax.swing.JDialog v_pagos;
    private javax.swing.JDialog v_seleccionar_invitado;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        // En función del típo de reservación cargada muestro los botones correspondientes
        log.info("Iniciando vista de Mostrar Reservación");

        //Cambiado para que el botón de rematar reservación esté desactivado para Sukasa
        if (Sesion.isSukasa()) {
            b_liquidar_reserva.setEnabled(false);
        }

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
        Action listenerCtrlO = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {

                reservacion.setObservaciones(ventana_padre.crearVentanaObservaciones(reservacion.getObservaciones()));
            }
        };
        addHotKey(ctrlO, "ObservacionesCtrlO", listenerCtrlO);

        // REALIZAR ABONOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosPropietario() != null
                && this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosPropietario()
                && !this.reservacion.getReservacion().getCancelado()
                && this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalmSRI())) {
            b_abonos_propios.setEnabled(true);
            //Si no permite abonos parciales y la reserva es de otro local, no podemos hacer abonos
            if (!this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm()) && !this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()) {
                b_abonos_propios.setEnabled(false);
                b_liquidar_reservacion.setEnabled(false);
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

            KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
            Action listenerf10 = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    accionLiqudarReserva();
                }
            };
            addHotKey(f10, "ResF10", listenerf10);

            b_abonos_propios.requestFocus();
        } else {
            b_abonos_propios.setEnabled(false);
            b_liquidar_reservacion.setEnabled(false);
        }

        // COMPRAR ARTICULOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && this.reservacion.getReservacion().getReservaTipo().getPermiteCompra()
                && !this.reservacion.getReservacion().getCancelado()) { //permiteCompra no será nunca null
            if (this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())) {

                b_comprar_articulo.setEnabled(true);
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
            } else {
                b_comprar_articulo.setEnabled(false);
            }
        } else {
            b_comprar_articulo.setEnabled(false);
        }

        // LIQUIDAR RESERVACION Y CANCELARLA
        if (!this.reservacion.getReservacion().getLiquidado()
                && !this.reservacion.getReservacion().getCancelado()) {

            if (this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())) {

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
            } else {
                b_cancelar_reserva.setEnabled(false);
                b_liquidar_reserva.setEnabled(false);
                b_menu_ppal.requestFocus();
            }
        } else {
            b_cancelar_reserva.setEnabled(false);
            b_liquidar_reserva.setEnabled(false);
            b_menu_ppal.requestFocus();
            //Foco en menu
        }

        // AÑADIR ARTICULOS
        if (!this.reservacion.getReservacion().getLiquidado()
                && !this.reservacion.getReservacion().getCancelado()) { //permiteCompra no será nunca null
            if (this.reservacion.getReservacion().getCodalm().equals(Sesion.getTienda().getCodalm())) {
                //solo para reservacion por separcion no hay que realizar la liquidacion de reservacion.
                if (this.reservacion.getReservacion().getReservaTipo().getCodTipo().equals("01")) {
                    b_liquidar_reservacion.setEnabled(false);
                }
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
            } else {
                b_comprar_articulo.setEnabled(false);
            }
        } else {
            b_comprar_articulo.setEnabled(false);
        }

        // ACCION LISTAR ABONOS
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        Action listenerf7 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionListarAbons();
            }
        };
        addHotKey(f7, "ResF7", listenerf7);

        // ACCIÓN PARA MENU
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_menu_ppalActionPerformed(ae);
            }
        };
        addHotKey(f12, "ResF12", listenerf12);

        KeyStroke ctrlMenos = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlMenos = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    if (reservacion.getReservacion().getReservaArticuloList().size() > 0) {
                        String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.CANCELAR_RESERVA);
                        if (compruebaUsuarioPropietario()) {
                            if (tb_articulos.getSelectedRow() >= 0) {
                                lineaSelecionada = tb_articulos.getSelectedRow();
                            } else {
                                lineaSelecionada = reservacion.getReservacion().getReservaArticuloList().size() - 1;
                            }
                            if (lineaSelecionada >= 0 && ventana_padre.crearVentanaConfirmacion("Esta acción Eliminará el artículo de la reserva, ¿Desea continuar?")) {
                                List lart = new LinkedList();
                                ReservaArticuloBean articuloABorar = reservacion.getReservacion().getReservaArticuloList().get(lineaSelecionada);
                                lart.add(articuloABorar);
                                ReservacionesServicios.eliminarReservaArticulos(reservacion.getReservacion(), lart);
                                // Stock
                                if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)) {
                                    ReservacionesServicios.accionEliminarArticuloStock(articuloABorar, reservacion);
                                }

                                ReservaBean reservaBeanBorrado = new ReservaBean();
                                reservaBeanBorrado.setReservaArticuloList(lart);
                                reservaBeanBorrado.setCodcaja(reservacion.getReservacion().getCodcaja());
                                reservaBeanBorrado.setCodReservacion(reservacion.getReservacion().getCodReservacion());
                                ReservacionesServicios.generarKardexReserva(reservaBeanBorrado, EnumTipoDocumento.RESERVA_LIQUIDADA);

                                refrescarReservacion();
                                if (!reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()
                                        && getPorAbonar().compareTo(BigDecimal.ZERO) <= 0 || reservacion.getReservacion().getReservaArticuloList().isEmpty()) {
                                    log.debug("Se procede a la liquidación");

                                    if (reservacion.getReservacion().getReservaArticuloList().isEmpty()) {
                                        reservacion.getReservacion().setCancelado(true);
                                        reservacion.getReservacion().setFechaLiquidacion(new Fecha());
                                        ReservacionesServicios.modificarReserva(reservacion.getReservacion());
                                        ServicioLogAcceso.crearAccesoLogCancelarReserva(compruebaAutorizacion, reservacion.getReservacion());
                                    } else if (!reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()
                                            && getPorAbonar().compareTo(BigDecimal.ZERO) <= 0) {
                                        liquidar(reservacion.getReservacion().getReservaArticuloList());
                                        ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());
                                    }

                                    if (reservacion.getAbonosReales().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                                        try {
                                            if (getPorAbonar().compareTo(BigDecimal.ZERO) < 0) {
                                                BonosServices.crearBonoReserva(Numero.menosPorcentajeR(reservacion.getAbonosRestantes(), reservacion.getPorcentajeDescuentoAbonos()), reservacion.getReservacion().getCodReservacion().toString(), BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION, reservacion.getReservacion().getCliente(), reservacion.getDescuentoEnReserva(), reservacion.getObservaciones(), reservacion.getReservacion().getUidReservacion());

                                            }
                                        } catch (Exception ex) {
                                            log.error("Error generando Bono. Expedir bono por valor de $" + getPorAbonar().negate());
                                        }
                                    }
                                    if (reservacion.getReservacion().getReservaArticuloList().isEmpty()) {
                                        ventana_padre.crearAdvertencia("Se anuló la reserva por no poseer artículos");
                                    }
                                    ventana_padre.showView("ident-cliente");
                                }
                            }
                        }

                    } else {
                        log.debug("No hay elementos que eliminar");
                        ventana_padre.crearAdvertencia("No hay artículos que eliminar");
                    }
                } catch (SinPermisosException ex) {
                    log.debug("No se tiene permisos para borrar línea de tickets.");
                } catch (TicketPrinterException ex) {
                    log.error("Error imprimiendo ticket de reservación: " + ex.getMessage());
                    ventana_padre.crearError(ex.getMessage());
                } catch (ReservasException ex) {
                    log.error("Error liquidando reservación: " + ex.getMessage());
                    ventana_padre.crearError(ex.getMessage());
                }
            }
        };
        addHotKey(ctrlMenos, "VentasCtrlMenos", listenerCtrlMenos);

        // CANCELAR RESERVACION
        //Realizar foco
        b_menu_ppal.requestFocus();

    }

    public JPrincipal getVentana_padre() {
        return ventana_padre;
    }

    public void setVentana_padre(JPrincipal ventana_padre) {
        this.ventana_padre = ventana_padre;
    }

    public void accionAbonosPropios() {
        if (b_abonos_propios.isEnabled()) {
            try {
                log.debug("Reseteamos una posible selección previa de artículo");
                reservacion.setArticuloSeleccionado(null);

                log.debug("Mostramos la pantalla de abonos propios");
                BigDecimal abonoMinimo = null;
                BigDecimal abonoMaximo = null;
                if (this.reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()) {
                    log.debug("Configuración para permitir abonos mayores a total: No se ha probado en producción esta característica");
                } else {
                    abonoMaximo = getPorAbonar();
                }

                abonoMinimo = BigDecimal.ZERO;
                log.debug("Abono máximo: " + abonoMaximo.toString());

                if (!this.reservacion.getReservacion().getReservaTipo().getPermiteAbonosParciales()) {

                    log.debug("El tipo de pago: " + this.reservacion.getReservacion().getReservaTipo().getCodTipo() + " no permite abonos parciales. Se procede al abono por el total de la reserva." + (this.totalImporte.subtract(this.totalPagado)).toString());

                    log.debug("Se crea la ventana de pagos");
                    crearVentanaPagos(this.totalImporte.subtract(this.totalPagado), this.totalImporte.subtract(this.totalPagado));
                    /*if (!p_pagos.isCancelado()) {
                        log.debug("Se procede a la liquidación");
                        reservacion.getDatosAbono().setEsultimoAbono(true);
                        liquidar(reservacion.getReservacion().getReservaArticuloList());
                        log.debug("Se realizó la liquidación");
                    }
                    else {
                        log.debug("Se canceló el pago");
                    }*/
                } else {

                    log.debug("El tipo de pago: " + this.reservacion.getReservacion().getReservaTipo().getCodTipo() + " sí permite abonos parciales. Se procede al abono con máximo de pago de $" + (this.totalImporte.subtract(this.totalPagado)).toString());

                    log.debug("Se crea la ventana de pagos");
                    crearVentanaPagos(abonoMinimo, abonoMaximo);

                    // cuando sale de la ventana importe
                    if (p_pagos.getPagoRealizado() != null) {
                        log.debug("El pago ha sido realizado por $" + p_pagos.getPagoRealizado().toString());
                        log.debug("El importe a abonar de la reserva antes del pago son $" + getPorAbonar().toString());
                        BigDecimal importeAAbonar;
                        importeAAbonar = p_pagos.getPagoRealizado();
                        if (!reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()
                                && importeAAbonar != null && importeAAbonar.compareTo(getPorAbonar()) == 0) {
                            /*if (!p_pagos.isCancelado()) {
                                log.debug("Se procede a la liquidación");
                                reservacion.getDatosAbono().setEsultimoAbono(true);
                                liquidar(reservacion.getReservacion().getReservaArticuloList());
                                log.debug("Se realizó la liquidación");
                            }
                            else {
                                log.debug("Se canceló el pago en la pantalla de pagos");
                            }*/
                        }
                    }
                }
                /*}catch (ReservasException ex) {
                log.debug("Error al realizar el abono : " + ex.getMessage(), ex);
                ventana_padre.crearAdvertencia(ex.getMessage());*/
            } catch (Exception ex) {
                log.error("Error al crear la nota de credito de la liquidación: " + ex.getMessage(), ex);
                ventana_padre.crearError("No se pudo imprimir la nota de credito de la liquidación.");
            }
        }
    }

    /**
     * Crea una ventana de Pagos
     *
     * @param importe
     */
    public void crearVentanaPagos(BigDecimal importeMinimo, BigDecimal importeMaximo) {
        log.debug("creción de la ventana de pagos");
        v_pagos.getContentPane().removeAll();

        p_pagos = new JReservacionesPagosV(ventana_padre, reservacion, importeMaximo, reservacion.getReservacion().getCliente(), reservacion.getInvitadoSeleccionado(), importeMinimo, true);
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
        v_pagos.setModal(true);
        v_pagos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_pagos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        v_pagos.setVisible(true);
        p_pagos.isCancelado();
    }

    public void crearVentanaLiquidacion(JReservacionesLiquidacionParcial p_liquidacion_parcial) {
        log.debug("creación de la ventana de liquidación");
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
        p_liquidacion_parcial.setContenedor(v_liquidacion_parcial);
        v_liquidacion_parcial.setVisible(true);

    }

    private void accionComprarArticulo() {
        if (b_comprar_articulo.isEnabled()) {
            try {
                reservacion.setArticuloSeleccionado(reservacion.getReservacion().getReservaArticuloList().get(tb_articulos.getSelectedRow()));
                if (!reservacion.getArticuloSeleccionado().getComprado()) {
                    reservacion.setArticuloSeleccionado(reservacion.getArticuloSeleccionado());

                    crearVentanaPagos(reservacion.getArticuloSeleccionado().getPrecioTotal(), reservacion.getArticuloSeleccionado().getPrecioTotal());

                } else {
                    log.debug("No se puede seleccionar el Artículo. El Artículo seleccionado ya ha sido pagado ");
                    ventana_padre.crearAdvertencia("No se puede seleccionar el Artículo. El Artículo seleccionado ya ha sido pagado ");
                }
            } catch (IndexOutOfBoundsException e) {
                log.debug("No se ha seleccionado ningún artículo");
                ventana_padre.crearAdvertencia("Debe seleccionar un Artículo. ");
            } catch (Exception e) {
                log.error("Error abonando artículo: " + e.getMessage(), e);
                ventana_padre.crearError(null);
            }
        }
    }

    private void liquidar(List<ReservaArticuloBean> lista) throws ReservasException, TicketPrinterException {
        BigDecimal abonosRestantesReales = reservacion.getAbonosRestantesReales();
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
                    setPorAbonar((totalImporte.subtract(totalPagado)).subtract(restanteAbonos));
                    t_comprado.setText(totalPagado.toString());
                    t_restante_abonos.setText(restanteAbonos.toString());
                    t_por_abonar.setText(getPorAbonar().toString());

                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + getPorAbonar());

                    // Aumentamos el stock de artículos vendidos y disminuimos el de reservados
                    listaArticulosComprados.add(art);

                } catch (ReservasException ex) {
                    log.error("Error al modificar la reserva en la liquidación");
                    log.debug("restanteAbonos: " + restanteAbonos);
                    log.debug("totalPagado: " + totalPagado);
                    log.debug("porAbonar: " + getPorAbonar());
                    ventana_padre.crearError(ex.getMessage());
                }
            }
        }
        reservacion.setAbonosRestantes(restanteAbonos);
        reservacion.setAbonosRestantesReales(abonosRestantesReales);
        reservacion.getReservacion().setLiquidado(true);
        reservacion.getReservacion().setFechaLiquidacion(new Fecha());
        TicketS ticket = ReservacionesServicios.liquidarReserva(reservacion, listaArticulosComprados);

//        accionConfigurarEntregas(ticket);
        log.debug("Finalizada liquidacion de reserva");
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
    }

    private void accionListarAbons() {
        crearPantallaAbono();
        v_listarAbonos.setVisible(true);
    }

    private void accionConfigurarEntregas(TicketS tickets) {
        crearPantallaEntregas(tickets);
        jDialog1.setVisible(true);
    }

    private void crearPantallaEntregas(TicketS tickets) {
        // construimos el nuevo panel de lista de entregas
        p_listarArticulos = new com.comerzzia.jpos.gui.reservaciones.JReservacionListaArticulos(reservacion, tickets);

        // creamos el layout para insertar nuestro panel en la ventana
        javax.swing.GroupLayout v_listarAbonosLayout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(v_listarAbonosLayout);
        v_listarAbonosLayout.setHorizontalGroup(
                v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_listarAbonosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_listarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        v_listarAbonosLayout.setVerticalGroup(
                v_listarAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_listarAbonosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_listarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jDialog1.setLocationRelativeTo(null);
        p_listarArticulos.setContenedor(jDialog1);
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

    private void accionAddArticulos() {
        try {
            if (b_add_articulos.isEnabled()) {

                //Pide confirmación de Administrador
                if (compruebaUsuarioPropietario()) {
                    ventana_padre.irPagoArticulos(reservacion, JReservacionesVentasCanBaby.MODO_ADD);
                }
            }
        } catch (Exception e) {
            log.error("Error añadiendo artículos a la reserva: " + e.getMessage(), e);
            ventana_padre.crearError("Error añadiendo artículos a la reserva: ");
        }
    }

    protected void refrescarReservacion() {
        try {
            int indiceSeleccionado = new Integer(tb_articulos.getSelectionModel().getMaxSelectionIndex());
            reservacion.refrescar();
            refrescarTotales();
            refrescarTabla();
            tb_articulos.getSelectionModel().setSelectionInterval(indiceSeleccionado, indiceSeleccionado);
        } catch (ReservasException ex) {
            ventana_padre.crearError(ex.getMessage());
        }
    }

    private void refrescarTotales() {
        try {
            //Rellenamos los datos de los abonos
            reservacion.calculaTotales();

            BigDecimal total = reservacion.getTotal();
            BigDecimal comprado = reservacion.getComprado();
            totalAbonado = new BigDecimal(BigInteger.ZERO);
            setPorAbonar(new BigDecimal(BigInteger.ZERO));
            // Guardamos pagado en la variable de clase
            this.totalPagado = comprado;

            // Guardamos el Importe total de la reservación en la variable de clase
            this.totalImporte = total;
            List<ReservaAbonoBean> abonos = this.reservacion.getReservacion().getReservaAbonoList();
            totalAbonado = reservacion.getTotalAbonado();
            restanteAbonos = reservacion.getAbonosRestantes();
            porAbonar = reservacion.getPorAbonar();

            t_comprado.setText(comprado.toString());
            t_n_abonos.setText(String.valueOf(abonos.size()));
            t_total_reserva.setText(total.toString());
            t_total_abonado.setText(totalAbonado.toString());

            tb_articulos.setDefaultRenderer(Object.class, new MostrarReservacionesCellRenderer());
            refrescarTabla();

            t_restante_abonos.setText(restanteAbonos.toString());
            t_por_abonar.setText(getPorAbonar().toString());
        } catch (Exception e) {
            log.error("Error refrescando datos de la reserva", e);
        }
    }

    private boolean compruebaUsuarioPropietario() {
        if (reservacion.getInvitadoActivo() == null) {
            p_autentificarCliente.limpiarFormulario();
            v_autentificarCliente.setVisible(true);
            try {
                reservacion.autenticaPropietario(p_autentificarCliente.getCliente());
            } catch (ClienteException e) {
                ventana_padre.crearError(e.getMessage());
            }
        }
        return (reservacion.getInvitadoActivo() != null);
    }

    public BigDecimal getPorAbonar() {
        return porAbonar;
    }

    public void setPorAbonar(BigDecimal porAbonar) {
        this.porAbonar = porAbonar;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (p_pagos != null) {
            p_pagos.dispose();
        }
        v_autentificarCliente.dispose();
        v_importe_a_pagar.dispose();
        v_liquidacion_parcial.dispose();
        v_listarAbonos.dispose();
        v_pagos.dispose();
        v_seleccionar_invitado.dispose();

        v_autentificarCliente = null;
        v_importe_a_pagar = null;
        v_liquidacion_parcial = null;
        v_listarAbonos = null;
        v_pagos = null;
        v_seleccionar_invitado = null;

        p_autentificarCliente = null;
        p_importe_a_pagar = null;
        p_listarAbonos = null;
        p_pagos = null;
        p_seleccionar_invitado = null;

        ventana_padre = null;
        reservacion = null;
        totalPagado = null;
        totalImporte = null;
        totalAbonadoReal = null;
        totalAbonado = null;
        restanteAbonos = null;
        porAbonar = null;
        p_listarAbonos = null;
    }

    private void accionActualizarStock() {
        try {
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
            logKardex.setFactura(String.valueOf(reservacion.getReservacion().getCodReservacion()));
            logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());
            for (ReservaArticuloBean ra : reservacion.getReservacion().getReservaArticuloList()) {
                if (!ra.getComprado()) {
                    log.debug("accionActualizarStock() - Disminuyendo stock de Artículo");
                    ServicioStock.disminuyeStockReserva(ra.getCodMarca(), ra.getIdItem(), 1, logKardex);
                    ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), Math.abs(ra.getCantidad()));

                }
            }
        } catch (StockException ex) {
            log.error("removeArticulosReserva() - STOCK: No fué posible disminuir el stock reservado para el artículo");
        }

    }

    private void accionActualizarStockKardex() {
        try {
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
            logKardex.setFactura(String.valueOf(reservacion.getReservacion().getCodReservacion()));
            logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());
            for (ReservaArticuloBean ra : reservacion.getReservacion().getReservaArticuloList()) {
                if (!ra.getComprado()) {
                    log.debug("accionActualizarStock() - Disminuyendo stock de Artículo");
                    ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);

                }
            }
        } catch (StockException ex) {
            log.error("removeArticulosReserva() - STOCK: No fué posible disminuir el stock reservado para el artículo");
        }

    }

    public void accionLiqudarReserva() {
        try {
            Sesion.iniciaNuevoTicket(reservacion.getReservacion().getCliente());
            reservacion.setTicket(Sesion.getTicket());
            Sesion.setReservacion(reservacion);
            for (ReservaArticuloBean reservaArticulo : reservacion.getReservacion().getReservaArticuloList()) {
                Articulos art = new Articulos();
                art = articulosServices.getArticuloCod(reservaArticulo.getCodart());

                Tarifas tar = tarifasServices.getTarifaArticulo(art.getCodart());
                Sesion.getTicketReservacion().crearLineaTicket(art.getCodart(), art, reservaArticulo.getCantidad().intValue(), tar, 0);
            }
            this.ventana_padre.addRecuperarReservacionesVentaView();
            this.ventana_padre.showView("reservaciones-venta");
        } catch (Exception e) {
            ventana_padre.crearError(e.getMessage());
        }
    }
}
