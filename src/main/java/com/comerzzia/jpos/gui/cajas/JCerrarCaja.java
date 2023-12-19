/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JCerrarCaja.java
 *
 * Created on 09-ago-2011, 8:17:27
 */
package com.comerzzia.jpos.gui.cajas;

import com.comerzzia.jpos.entity.db.VariableAlm;
import com.comerzzia.jpos.entity.services.cierrecaja.CierreCaja;
import com.comerzzia.jpos.gui.IManejadorErrores;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.cajas.modelos.CierreCajaTableCellRenderer;
import com.comerzzia.jpos.gui.cajas.modelos.CierreCajaTableModel;
import com.comerzzia.jpos.gui.modelos.foco.POSFocusTraversalPolicy;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.pinpad.PinPadException;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrack;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrackException;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import es.mpsistemas.util.log.Logger;
import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MGRI
 */
public class JCerrarCaja extends JPanelImagenFondo implements IVista, FocusListener {

    DefaultKeyboardFocusManager myFocusmgr = new DefaultKeyboardFocusManager();
    private JPrincipal ventana_padre = null;
    private CierreCajaTableModel modeloTablaCierreCaja = null;
    private static Logger log = Logger.getMLogger(JCerrarCaja.class);
    POSFocusTraversalPolicy politicaDeFoco;

    /**
     * Creates new form JCerrarCaja
     */
    public JCerrarCaja() {
        super();
        initComponents();
    }

    public JCerrarCaja(JPrincipal ventana_padre) {
        super();
        try {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_cerrar-caja.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
            log.error("No se pudo cargar la imagen de fondo de cierre de caja");
        }
        this.ventana_padre = ventana_padre;
        initComponents();
        URL myurl;

        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);

        v_abrir_caja.setIconImage(icon.getImage());
        v_abrir_caja.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);

        // Iniciamos la ventana de Retiros
        v_retiro.setIconImage(icon.getImage());
        v_retiro.setLocationRelativeTo(null);
        p_retiro.setVentana_padre(ventana_padre);
        p_retiro.setContenedor(v_retiro);

        // Iniciamos la ventana de Configuracion del pinpad
        v_conf_pinpad.setIconImage(icon.getImage());
        v_conf_pinpad.setLocationRelativeTo(null);
        p_conf_pinpad.setVentana_padre(ventana_padre);
        p_conf_pinpad.setContenedor(v_conf_pinpad);

        // Iniciamos la ventana de Abrir Caja
        p_apertura_caja.setVentana_padre((IManejadorErrores) this.ventana_padre);
        p_apertura_caja.setContenedor(v_abrir_caja);

        // Modelo de datos de la tabla
        tb_cierre_caja.setDefaultRenderer(Object.class, new CierreCajaTableCellRenderer());

        //Transparencia y ausencia de bordes en la tabla
        log.debug("Constructor-Transparencia y ausencia de bordes en la tabla");
        Border empty = new EmptyBorder(0, 0, 0, 0);
        js_tb_cierre_caja.setViewportBorder(empty);
        tb_cierre_caja.setBorder(empty);
        js_tb_cierre_caja.getViewport().setOpaque(false);

        b_abrir_cajon.addFocusListener(this);
        b_apertura_parcial.addFocusListener(this);
        b_cajas_recueto.addFocusListener(this);
        b_cierre_parcial_caja.addFocusListener(this);
        b_menu_ppal.addFocusListener(this);
        b_movimientos.addFocusListener(this);
        b_desactivar_pinpad.addFocusListener(this);

        Vector<Component> order = new Vector<Component>(7);
        order.add(b_apertura_parcial);
        order.add(b_cierre_parcial_caja);
        order.add(b_cajas_recueto);
        order.add(b_movimientos);
        order.add(b_abrir_cajon);
        order.add(b_menu_ppal);
        order.add(b_desactivar_pinpad);
        politicaDeFoco = new POSFocusTraversalPolicy(order);
        m_ventas.setFocusTraversalPolicy(politicaDeFoco);

        addFunctionKeys();
        //myFocusmgr.

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v_abrir_caja = new javax.swing.JDialog();
        new JAperturaCaja(this.ventana_padre,v_abrir_caja);
        p_apertura_caja = new com.comerzzia.jpos.gui.cajas.JAperturaCaja();
        v_retiro = new javax.swing.JDialog();
        p_retiro = new com.comerzzia.jpos.gui.cajas.JRetiro();
        v_conf_pinpad = new javax.swing.JDialog();
        p_conf_pinpad = new com.comerzzia.jpos.gui.cajas.JConfiguracionPinPad();
        jPanel1 = new javax.swing.JPanel();
        js_tb_cierre_caja = new javax.swing.JScrollPane();
        tb_cierre_caja = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        t_entrada_venta = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_entrada_mov = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_entrada_tot = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_salida_dev = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_salida_mov = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_salida_total = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        t_entrada_abonos = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_salida_expBonos = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        m_ventas = new javax.swing.JPanel();
        b_cierre_parcial_caja = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_movimientos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_abrir_cajon = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cajas_recueto = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_conf_pinpad = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_act_pinpad = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_apertura_parcial = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_menu_ppal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_desactivar_pinpad = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_superior = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        t_descuadre_total = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel16 = new javax.swing.JLabel();
        lb_fecha_apertura = new javax.swing.JLabel();
        lb_info1 = new javax.swing.JLabel();
        lb_info2 = new javax.swing.JLabel();
        lb_info3 = new javax.swing.JLabel();

        v_abrir_caja.setMinimumSize(new java.awt.Dimension(494, 198));
        v_abrir_caja.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        javax.swing.GroupLayout v_abrir_cajaLayout = new javax.swing.GroupLayout(v_abrir_caja.getContentPane());
        v_abrir_caja.getContentPane().setLayout(v_abrir_cajaLayout);
        v_abrir_cajaLayout.setHorizontalGroup(
            v_abrir_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_abrir_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_apertura_caja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_abrir_cajaLayout.setVerticalGroup(
            v_abrir_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_abrir_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_apertura_caja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        v_retiro.setMinimumSize(new java.awt.Dimension(378, 130));

        javax.swing.GroupLayout v_retiroLayout = new javax.swing.GroupLayout(v_retiro.getContentPane());
        v_retiro.getContentPane().setLayout(v_retiroLayout);
        v_retiroLayout.setHorizontalGroup(
            v_retiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_retiroLayout.createSequentialGroup()
                .addComponent(p_retiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        v_retiroLayout.setVerticalGroup(
            v_retiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_retiroLayout.createSequentialGroup()
                .addComponent(p_retiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(208, Short.MAX_VALUE))
        );

        v_conf_pinpad.setMinimumSize(new java.awt.Dimension(466, 320));
        v_conf_pinpad.setResizable(false);

        javax.swing.GroupLayout v_conf_pinpadLayout = new javax.swing.GroupLayout(v_conf_pinpad.getContentPane());
        v_conf_pinpad.getContentPane().setLayout(v_conf_pinpadLayout);
        v_conf_pinpadLayout.setHorizontalGroup(
            v_conf_pinpadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_conf_pinpadLayout.createSequentialGroup()
                .addComponent(p_conf_pinpad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        v_conf_pinpadLayout.setVerticalGroup(
            v_conf_pinpadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_conf_pinpadLayout.createSequentialGroup()
                .addComponent(p_conf_pinpad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setNextFocusableComponent(b_cajas_recueto);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);

        js_tb_cierre_caja.setBorder(null);

        tb_cierre_caja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Medio de Pago", "Entrada", "Salida", "Total", "Recuento", "Descuadre"
            }
        ));
        tb_cierre_caja.setFocusable(false);
        tb_cierre_caja.setOpaque(false);
        tb_cierre_caja.setRequestFocusEnabled(false);
        js_tb_cierre_caja.setViewportView(tb_cierre_caja);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(js_tb_cierre_caja, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(js_tb_cierre_caja, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 950, 300));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel1.setText("ARQUEO DE CAJA");
        jLabel1.setFocusable(false);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 317, -1));

        jPanel2.setForeground(new java.awt.Color(0, 102, 255));
        jPanel2.setFocusable(false);
        jPanel2.setOpaque(false);

        jLabel4.setForeground(new java.awt.Color(0, 102, 255));
        jLabel4.setText("Entrada (Venta):");
        jLabel4.setFocusable(false);

        jLabel5.setForeground(new java.awt.Color(0, 102, 255));
        jLabel5.setText("Entrada (Ingr.I):");
        jLabel5.setFocusable(false);

        jLabel6.setForeground(new java.awt.Color(0, 102, 255));
        jLabel6.setText("Total Entrada:");
        jLabel6.setFocusable(false);

        jLabel7.setForeground(new java.awt.Color(0, 102, 255));
        jLabel7.setText("Salida (Dev):");
        jLabel7.setFocusable(false);

        jLabel8.setForeground(new java.awt.Color(0, 102, 255));
        jLabel8.setText("Salida (Mov):");
        jLabel8.setFocusable(false);

        jLabel9.setForeground(new java.awt.Color(0, 102, 255));
        jLabel9.setText("Total Salida:");
        jLabel9.setFocusable(false);

        t_entrada_venta.setEditable(false);
        t_entrada_venta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_entrada_venta.setFocusable(false);
        t_entrada_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_entrada_ventaActionPerformed(evt);
            }
        });

        t_entrada_mov.setEditable(false);
        t_entrada_mov.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_entrada_mov.setFocusable(false);

        t_entrada_tot.setEditable(false);
        t_entrada_tot.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_entrada_tot.setFocusable(false);

        t_salida_dev.setEditable(false);
        t_salida_dev.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_salida_dev.setFocusable(false);

        t_salida_mov.setEditable(false);
        t_salida_mov.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_salida_mov.setFocusable(false);

        t_salida_total.setEditable(false);
        t_salida_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_salida_total.setFocusable(false);

        jLabel2.setForeground(new java.awt.Color(0, 102, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("$");
        jLabel2.setFocusable(false);

        jLabel11.setForeground(new java.awt.Color(0, 102, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("$");
        jLabel11.setFocusable(false);

        jLabel12.setForeground(new java.awt.Color(0, 102, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("$");
        jLabel12.setFocusable(false);

        jLabel13.setForeground(new java.awt.Color(0, 102, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("$");
        jLabel13.setFocusable(false);

        jLabel14.setForeground(new java.awt.Color(0, 102, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("$");
        jLabel14.setFocusable(false);

        jLabel15.setForeground(new java.awt.Color(0, 102, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("$");
        jLabel15.setFocusable(false);

        jLabel17.setForeground(new java.awt.Color(0, 102, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("$");
        jLabel17.setFocusable(false);

        jLabel18.setForeground(new java.awt.Color(0, 102, 255));
        jLabel18.setText("Entrada (Abonos):");
        jLabel18.setFocusable(false);

        t_entrada_abonos.setEditable(false);
        t_entrada_abonos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_entrada_abonos.setFocusable(false);
        t_entrada_abonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_entrada_abonosActionPerformed(evt);
            }
        });

        t_salida_expBonos.setEditable(false);
        t_salida_expBonos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_salida_expBonos.setFocusable(false);

        jLabel19.setForeground(new java.awt.Color(0, 102, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("$");
        jLabel19.setFocusable(false);

        jLabel20.setForeground(new java.awt.Color(0, 102, 255));
        jLabel20.setText("Salida (Exp Bonos):");
        jLabel20.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_entrada_tot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_entrada_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(t_entrada_mov, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(t_entrada_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(103, 103, 103)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_salida_mov, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(t_salida_dev, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(t_salida_expBonos, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(t_salida_total, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addGap(86, 86, 86))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(t_entrada_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(t_entrada_mov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel11))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addComponent(t_entrada_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12)
                            .addComponent(t_entrada_tot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel7)
                            .addComponent(jLabel13)
                            .addComponent(t_salida_dev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8)
                            .addComponent(jLabel14)
                            .addComponent(t_salida_mov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(t_salida_expBonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9)
                            .addComponent(jLabel15)
                            .addComponent(t_salida_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 700, 130));

        m_ventas.setBackground(new java.awt.Color(255, 255, 255));
        m_ventas.setFocusCycleRoot(true);
        m_ventas.setOpaque(false);
        m_ventas.setRequestFocusEnabled(false);
        m_ventas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_cierre_parcial_caja.setText("<html><center> Cierre parcial <br/>F8</center></html>");
        b_cierre_parcial_caja.setActionCommand("Identificar Cliente F3");
        b_cierre_parcial_caja.setFocusTraversalPolicyProvider(true);
        b_cierre_parcial_caja.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_cierre_parcial_caja.setName("CierreCaja"); // NOI18N
        b_cierre_parcial_caja.setNextFocusableComponent(b_cajas_recueto);
        b_cierre_parcial_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cierre_parcial_cajaActionPerformed(evt);
            }
        });
        m_ventas.add(b_cierre_parcial_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 100, 57));

        b_movimientos.setText("<html><center>Movimientos<br/>F3</center></html>");
        b_movimientos.setFocusTraversalPolicyProvider(true);
        b_movimientos.setMargin(new java.awt.Insets(2, 5, 2, 5));
        b_movimientos.setName("Movimientos"); // NOI18N
        b_movimientos.setNextFocusableComponent(b_abrir_cajon);
        b_movimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_movimientosActionPerformed(evt);
            }
        });
        m_ventas.add(b_movimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 100, 57));

        b_abrir_cajon.setText("<html><center>Abrir cajón<br/>F5</center></html>");
        b_abrir_cajon.setFocusTraversalPolicyProvider(true);
        b_abrir_cajon.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_abrir_cajon.setName("AbrirCajon"); // NOI18N
        b_abrir_cajon.setNextFocusableComponent(b_menu_ppal);
        b_abrir_cajon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_abrir_cajonActionPerformed(evt);
            }
        });
        m_ventas.add(b_abrir_cajon, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 100, 57));

        b_cajas_recueto.setText("<html><center>Realizar <br> Retiro<br> F7 </cener></html>");
        b_cajas_recueto.setFocusTraversalPolicyProvider(true);
        b_cajas_recueto.setName("AmpliarRetiro"); // NOI18N
        b_cajas_recueto.setNextFocusableComponent(b_movimientos);
        b_cajas_recueto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cajas_recuetoActionPerformed(evt);
            }
        });
        m_ventas.add(b_cajas_recueto, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 100, 57));

        b_conf_pinpad.setText("<html><center>Configurar <br> PinPad<br> F10 </center></html>");
        b_conf_pinpad.setFocusTraversalPolicyProvider(true);
        b_conf_pinpad.setMaximumSize(new java.awt.Dimension(85, 37));
        b_conf_pinpad.setMinimumSize(new java.awt.Dimension(85, 37));
        b_conf_pinpad.setName("AmpliarRetiro"); // NOI18N
        b_conf_pinpad.setNextFocusableComponent(b_movimientos);
        b_conf_pinpad.setPreferredSize(new java.awt.Dimension(85, 37));
        b_conf_pinpad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_conf_pinpadActionPerformed(evt);
            }
        });
        m_ventas.add(b_conf_pinpad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 90, 57));

        b_act_pinpad.setText("<html><center>Actualizar <br> PinPad<br> F11 </center></html>");
        b_act_pinpad.setFocusTraversalPolicyProvider(true);
        b_act_pinpad.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_act_pinpad.setName("Menu"); // NOI18N
        b_act_pinpad.setNextFocusableComponent(b_apertura_parcial);
        b_act_pinpad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_act_pinpadActionPerformed(evt);
            }
        });
        m_ventas.add(b_act_pinpad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 90, 57));

        b_apertura_parcial.setText("<html><center> Apertura Parcial <br/>F9</center></html>");
        b_apertura_parcial.setActionCommand("Identificar Cliente F3");
        b_apertura_parcial.setFocusTraversalPolicyProvider(true);
        b_apertura_parcial.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_apertura_parcial.setName("AperturaCaja"); // NOI18N
        b_apertura_parcial.setNextFocusableComponent(b_cierre_parcial_caja);
        b_apertura_parcial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_apertura_parcialActionPerformed(evt);
            }
        });
        m_ventas.add(b_apertura_parcial, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 57));

        b_menu_ppal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_menu_ppal.setFocusTraversalPolicyProvider(true);
        b_menu_ppal.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_menu_ppal.setName("Menu"); // NOI18N
        b_menu_ppal.setNextFocusableComponent(b_apertura_parcial);
        b_menu_ppal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menu_ppalActionPerformed(evt);
            }
        });
        m_ventas.add(b_menu_ppal, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 100, 57));

        b_desactivar_pinpad.setToolTipText("");
        b_desactivar_pinpad.setEnabled(false);
        b_desactivar_pinpad.setFocusTraversalPolicyProvider(true);
        b_desactivar_pinpad.setLabel("<html><center>Desactivar <br> Pinpad<br> F6 </center></html>");
        b_desactivar_pinpad.setName("AmpliarRetiro"); // NOI18N
        b_desactivar_pinpad.setNextFocusableComponent(b_movimientos);
        b_desactivar_pinpad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_desactivar_pinpadActionPerformed(evt);
            }
        });
        m_ventas.add(b_desactivar_pinpad, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, 100, 57));

        add(m_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 500, -1));

        p_superior.setFocusable(false);
        p_superior.setOpaque(false);

        jLabel3.setForeground(new java.awt.Color(0, 102, 255));
        jLabel3.setText("Fecha de Apertura");
        jLabel3.setFocusable(false);

        jLabel10.setForeground(new java.awt.Color(0, 102, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Descuadre Total ");
        jLabel10.setFocusable(false);

        t_descuadre_total.setEditable(false);
        t_descuadre_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_descuadre_total.setFocusable(false);

        jLabel16.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("$");
        jLabel16.setFocusable(false);

        lb_fecha_apertura.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lb_fecha_apertura.setFocusable(false);

        javax.swing.GroupLayout p_superiorLayout = new javax.swing.GroupLayout(p_superior);
        p_superior.setLayout(p_superiorLayout);
        p_superiorLayout.setHorizontalGroup(
            p_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_superiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lb_fecha_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_descuadre_total, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        p_superiorLayout.setVerticalGroup(
            p_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_superiorLayout.createSequentialGroup()
                .addGroup(p_superiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_fecha_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(t_descuadre_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(0, 0, 0))
        );

        add(p_superior, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 790, 30));
        add(lb_info1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 440, 220, 20));
        add(lb_info2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 470, 220, 20));
        add(lb_info3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 490, 220, 20));
    }// </editor-fold>//GEN-END:initComponents

    private void t_entrada_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_entrada_ventaActionPerformed
    }//GEN-LAST:event_t_entrada_ventaActionPerformed

    private void b_cierre_parcial_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cierre_parcial_cajaActionPerformed
        if (Sesion.getCajaActual().isCajaParcialAbierta() && Sesion.getCajaActual().isCajaAbierta()) {
            //Ambas cajas abiertas
            accionCierreParcial();
        } else {
            if (Sesion.getCajaActual().isCajaAbierta()) {
                // Caja abierta, pero hay que hacer apertura parcial
                this.accionCerrarCaja();
            } else {
                // Cajas cerradas
                log.error("Se pudo ejecutar la acción cierre de caja o cierre parcial de caja con ambas cajas abiertas");
            }
        }
}//GEN-LAST:event_b_cierre_parcial_cajaActionPerformed

    private void b_movimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_movimientosActionPerformed
        if (Sesion.getCajaActual().isCajaAbierta()) {
            ventana_padre.showView("movimientos");
        }

}//GEN-LAST:event_b_movimientosActionPerformed

    private void b_abrir_cajonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_abrir_cajonActionPerformed
        try {
            log.debug("SEÑAL APERTURA CAJON");
            PrintServices.getInstance().abrirCajon();
            ServicioLogAcceso.crearAccesoLogAperturaCajon();
        } catch (Exception ex) {
            log.error("Error  al abrir cajón.", ex);
        }
}//GEN-LAST:event_b_abrir_cajonActionPerformed

    private void b_cajas_recuetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cajas_recuetoActionPerformed
        accionAmpliarRetiro();

    }//GEN-LAST:event_b_cajas_recuetoActionPerformed

    private void b_apertura_parcialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_apertura_parcialActionPerformed
        if (Sesion.getCajaActual().isCajaParcialAbierta() && Sesion.getCajaActual().isCajaAbierta()) {
            //Ambas cajas abiertas
            log.error("Se pudo ejecutar la acción apertura de caja o apertura parcial de caja con ambas cajas abiertas");
        } else {
            if (Sesion.getCajaActual().isCajaAbierta()) {
                // Caja abierta, pero hay que hacer apertura parcial
                accionAperturaParcialDeCaja();
            } else {
                // Cajas cerradas
                accionAbrirCaja();
            }
        }
    }//GEN-LAST:event_b_apertura_parcialActionPerformed

private void b_menu_ppalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menu_ppalActionPerformed
    ventana_padre.mostrarMenu();
}//GEN-LAST:event_b_menu_ppalActionPerformed

private void t_entrada_abonosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_entrada_abonosActionPerformed
}//GEN-LAST:event_t_entrada_abonosActionPerformed

    private void b_conf_pinpadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_conf_pinpadActionPerformed
        if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
            JPrincipal.setPopupActivo(v_conf_pinpad);
            p_conf_pinpad.iniciaVista();
            v_conf_pinpad.setModal(true);
            v_conf_pinpad.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
            v_conf_pinpad.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            v_conf_pinpad.setLocationRelativeTo(null);
            v_conf_pinpad.setAlwaysOnTop(true);
            v_conf_pinpad.setVisible(true);
            JPrincipal.setPopupActivo(null);
            v_conf_pinpad.setVisible(false);
            if (p_conf_pinpad.isAceptar()) {
                try {
                    PinPad op = PinPad.getInstance();
                    op.getiPinPad().configuracionPinPad(p_conf_pinpad.getIp(), p_conf_pinpad.getMascara(), p_conf_pinpad.getGateway());
                    ventana_padre.crearInformacion("El pinpad se ha configurado correctamente");
                } catch (PinPadException e) {
                    log.error("Error configurando el pinpad: " + e.getMessage());
                    ventana_padre.crearError("Se ha producido un error configurando el PinPad");
                }
            }
        } else {
            try {
                //Fasttrack
                PinPadFasttrack ppf = PinPadFasttrack.getInstance();
                ppf.getiPinPad().configuracionPinPad();
                ventana_padre.crearInformacion("El pinpad se ha configurado correctamente");
            } catch (PinPadFasttrackException ex) {
                java.util.logging.Logger.getLogger(JCerrarCaja.class.getName()).log(Level.SEVERE, null, ex);
                ventana_padre.crearError("Se ha producido un error configurando el PinPad");
            }
        }
    }//GEN-LAST:event_b_conf_pinpadActionPerformed

    private void b_act_pinpadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_act_pinpadActionPerformed
        if (JPrincipal.getInstance().crearVentanaConfirmacion("¿Desea actualizar el pinpad?")) {
            try {
                PinPad op = PinPad.getInstance();
                op.getiPinPad().procesoActualizacion();
                ventana_padre.crearInformacion("El pinpad se ha actualizado correctamente");
            } catch (PinPadException e) {
                log.error("Error actualizando el pinpad: " + e.getMessage());
                ventana_padre.crearError("Se ha producido un error actualizando el PinPad");
            }
        }
    }//GEN-LAST:event_b_act_pinpadActionPerformed

    private void b_desactivar_pinpadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_desactivar_pinpadActionPerformed
        // TODO add your handling code here:
        accionActivarDesactivarPinpad();
    }//GEN-LAST:event_b_desactivar_pinpadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_abrir_cajon;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_act_pinpad;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_apertura_parcial;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cajas_recueto;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cierre_parcial_caja;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_conf_pinpad;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_desactivar_pinpad;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_menu_ppal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_movimientos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane js_tb_cierre_caja;
    private javax.swing.JLabel lb_fecha_apertura;
    private javax.swing.JLabel lb_info1;
    private javax.swing.JLabel lb_info2;
    private javax.swing.JLabel lb_info3;
    private javax.swing.JPanel m_ventas;
    private com.comerzzia.jpos.gui.cajas.JAperturaCaja p_apertura_caja;
    private com.comerzzia.jpos.gui.cajas.JConfiguracionPinPad p_conf_pinpad;
    private com.comerzzia.jpos.gui.cajas.JRetiro p_retiro;
    private javax.swing.JPanel p_superior;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_descuadre_total;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_entrada_abonos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_entrada_mov;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_entrada_tot;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_entrada_venta;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_salida_dev;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_salida_expBonos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_salida_mov;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_salida_total;
    private javax.swing.JTable tb_cierre_caja;
    private javax.swing.JDialog v_abrir_caja;
    private javax.swing.JDialog v_conf_pinpad;
    private javax.swing.JDialog v_retiro;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        JPrincipal.setPanelActivo(this);
        // llamada a la regeneración de la vista
        refrescaVista();
        if (b_cajas_recueto.isEnabled()) {
            b_cajas_recueto.requestFocus();
        } else {
            b_abrir_cajon.requestFocus();
        }

    }

    private void refrescaVista() {
        log.debug("Refresco de vista");
        try {
            CierreCaja cc = Sesion.getCajaActual().consultaMovimientosES();
            tb_cierre_caja.setModel(new CierreCajaTableModel(cc.getListaCierreCaja()));
            if (Sesion.getCajaActual().isCajaAbierta()) {
                lb_fecha_apertura.setText(formateadorFecha.format((Sesion.getCajaActual().getCajaActual().getFechaApertura())));
            }
            refrescaPanelES(cc);
            refrescaMenu();
        } catch (Exception ex) {
            log.error("No se pudo recargar la Interfaz de Arqueo de caja", ex);
        }
    }

    /**
     * Refrescamos el panel de E/S
     *
     * @param cc
     */
    private void refrescaPanelES(CierreCaja cc) {
        try {
            LecturaConfiguracion.leerDatosSesion(false);
            log.debug("Se recargaron los datos de configuración");
        } catch (Exception ex) {
            log.debug("Error al leer la cofiguración de la aplicación");
        }
        log.debug("Refrescando panel e-s");
        if (cc.getRecuentoEntrada().getMovimientos() == null) {
            cc.getRecuentoEntrada().setMovimientos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_entrada_mov.setText(cc.getRecuentoEntrada().getMovimientos().toString());

        if (cc.getRecuentoSalida().getMovimientos() == null) {
            cc.getRecuentoSalida().setMovimientos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_salida_mov.setText(cc.getRecuentoSalida().getMovimientos().toString());

        if (cc.getRecuentoEntrada().getVenta() == null) {
            cc.getRecuentoEntrada().setVenta(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_entrada_venta.setText(cc.getRecuentoEntrada().getVenta().toString());

        if (cc.getRecuentoSalida().getVenta() == null) {
            cc.getRecuentoSalida().setVenta(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_salida_dev.setText(cc.getRecuentoSalida().getVenta().toString());

        if (cc.getRecuentoEntrada().getAbonos() == null) {
            cc.getRecuentoEntrada().setAbonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_entrada_abonos.setText(cc.getRecuentoEntrada().getAbonos().toString());

        if (cc.getRecuentoSalida().getBonos() == null) {
            cc.getRecuentoSalida().setBonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_salida_expBonos.setText(cc.getRecuentoSalida().getBonos().toString());

        if (cc.getRecuentoEntrada().getSalida() == null) {
            cc.getRecuentoEntrada().setSalida(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        t_entrada_tot.setText(cc.getRecuentoEntrada().getSalida().toString());

        if (cc.getRecuentoSalida().getSalida() == null) {
            cc.getRecuentoSalida().setSalida(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        t_salida_total.setText(cc.getRecuentoSalida().getSalida().toString());

        t_descuadre_total.setText(cc.getDescuadreTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

    }

    /* EVENTOS*/
    private void addFunctionKeys() {
        // ENTER EN LOS BOTONES
        addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterBotones", listenerEnter);

        log.debug("Añadiendo eventos");
        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        Action listenerf2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionAmpliarRetiro();
            }
        };
        addHotKey(f2, "CCajaF7", listenerf2);

        // Apertura de caja
        KeyStroke fapcaj = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerApCaj = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionAbrirCaja();
            }
        };
        addHotKey(fapcaj, "ApCaj", listenerApCaj);

        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionCerrarCaja();

            }
        };
        addHotKey(f4, "CCajaF3", listenerf4);

        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        Action listenerf5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_abrir_cajonActionPerformed(ae);

            }
        };
        addHotKey(f5, "CCajaF5", listenerf5);

        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        Action listenerf3 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_movimientosActionPerformed(ae);
            }
        };
        addHotKey(f3, "CCajaF4", listenerf3);

        //Accion cierre parcial
        KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        Action aCierreParcial = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionCierreParcial();
            }
        };
        addHotKey(f8, "F8", aCierreParcial);

        //Accion Apertura Parcial
        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        Action aAperturaParcial = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionAperturaParcialDeCaja();
            }
        };
        addHotKey(f9, "F9", aAperturaParcial);
        //Accion Apertura Parcial
        KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
        Action confPinPad = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_conf_pinpadActionPerformed(ae);
            }
        };
        addHotKey(f10, "F10", confPinPad);
        //Accion Apertura Parcial
        KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        Action actPinPad = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_act_pinpadActionPerformed(ae);
            }
        };
        addHotKey(f11, "F11", actPinPad);

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_menu_ppalActionPerformed(ae);
            }
        };
        addHotKey(f12, "MenuF12", listenerf12);

        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        Action listenerf6 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                //accionActivarDesactivarPinpad();

            }
        };
        addHotKey(f6, "CCajaF6", listenerf6);

    }

    /*    
    private void addHotKey(KeyStroke keyStroke, String inputActionKey, Action listener) {
    ActionMap actionMap = this.getActionMap();
    InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(keyStroke, inputActionKey);
    actionMap.put(inputActionKey, listener);
    }       
     */

 /* ACCIONES  */
    private void accionAbrirCaja() {
        try {

            if (!Sesion.getCajaActual().isCajaAbierta()) {
                v_abrir_caja.setLocationRelativeTo(null);
                v_abrir_caja.setVisible(true);
                p_apertura_caja.iniciaFoco();
                refrescaVista();
            }
        } catch (Exception e) {
            ventana_padre.crearError(null);
        }
    }

    private void accionActivarDesactivarPinpad() {
        try {
            String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.PERMITE_CAMBIAR_ESTADO_PINPAD);
            ServicioLogAcceso.crearAccesoLogDesbloquearPos(autorizador);
            try {
                VariableAlm variableAlm = new VariableAlm();
                variableAlm.setIdVariable(VariablesAlm.PINPAD_ESTADO_AUTOMATICO);
                if (VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_ESTADO_AUTOMATICO)) {
                    if (JPrincipal.getInstance().crearVentanaConfirmacion("¿Está seguro que desea DESACTIVAR la autorización automática? Esta acción afectará a todas las cajas. Hágalo solo en caso que no exista comunicación con los proveedores de las tarjetas.")) {
                        //DESACTIVAR
                        variableAlm.setValor("N");
                        VariablesAlm variablesAlm = new VariablesAlm();
                        variablesAlm.modificaVariable(variableAlm);
                        PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_MANUAL);
                        b_desactivar_pinpad.setText("<html><center> Activar PinPad <br/>F6</center></html>");
                        ventana_padre.crearConfirmacion("Se realizó la actualización de estado del pinpad");
                    }
                } else {
                    //ACTIVAR
                    variableAlm.setValor("S");
                    VariablesAlm variablesAlm = new VariablesAlm();
                    variablesAlm.modificaVariable(variableAlm);
                    PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);
                    b_desactivar_pinpad.setText("<html><center> Desactivar PinPad <br/>F6</center></html>");
                    ventana_padre.crearConfirmacion("Se realizó la actualización de estado del pinpad");
                }

            } catch (Exception e) {
                ventana_padre.crearError(null);
            }
        } catch (SinPermisosException ex) {
            ventana_padre.crearError("No tiene permiso para esta acción");
            java.util.logging.Logger.getLogger(JCerrarCaja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void accionCierreParcial() {
        log.debug("ACCION CIERRE PARCIAL");
        try {
            log.debug("Accion Cierre parcial de caja");

            if (Sesion.getCajaActual().isCajaAbierta() && Sesion.getCajaActual().isCajaParcialAbierta()) {
                if (Sesion.getTicket() != null || Sesion.existenTicketsAparcados()) {

                    if (ventana_padre.crearVentanaConfirmacion("Existen ventas en proceso pendientes de finalizar, si cierra la caja se cancelarán, ¿Desea continuar?")) {
                        Sesion.borrarTicket();
                        Sesion.getTicketsAparcados().clear();
                        this.accionCierreParcial();
                    }
                } else {
                    Sesion.getCajaActual().cierreParcialDeCaja();
                    ventana_padre.crearConfirmacion("Se realizó el cierre parcial de caja");
                }
                refrescaVista();
            }
        } catch (Exception e) {
            log.error("No se pudo realizar el cierre parcial de caja", e);
            ventana_padre.crearError("No se pudo realizar el cierre parcial de caja");
        }
    }

    private void accionCerrarCaja() {
        log.info("ACCION CIERRE DE DÍA");
        if (Sesion.getCajaActual().isCajaAbierta() && !Sesion.getCajaActual().isCajaParcialAbierta()) {
            try {

                log.debug("Accion: Cerrar caja");
                if (Sesion.getTicket() != null) {
                    if (ventana_padre.crearVentanaConfirmacion("Existe una venta en Proceso, si cierra la caja se cancelará, ¿Desea continuar?")) {
                        Sesion.borrarTicket();
                        this.accionCerrarCaja();
                    }
                } else if (Sesion.getCajaActual().isCajaParcialAbierta()) {
                    ventana_padre.crearError("Existe una caja parcial abierta, por favor cierrela antes de realizar el cierre de día");
                    log.error("Se ha efectuado la acción cierre de caja de día existiendo una apertura parcial de caja activa");
                } else {
                    accionCerrarCajaAux();
                }
            } catch (Exception ex) {
                log.error("No se pudo completar la operación cierre de caja", ex);
                ventana_padre.crearError("No se pudo completar la operación cierre de caja");
            }
        }
    }

    private void accionCerrarCajaAux() throws Exception {
        // Cerramos caja
        String mensaje = "";
        if (PinPad.getInstance().isAutomatico()) {
            if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                try {
                    PinPad op = PinPad.getInstance();
                    op.controlPermisosPinPad();
                    op.getiPinPad().procesoControl();
                    mensaje = "Se ha realizado el proceso de control mediante pinpad.";
                } catch (PinPadException e) {
                    String error = "Se ha producido un error al realizar el proceso de control del pinpad que borra los registros de transacciones del día.¿Desea continuar sin realizar el proceso de control?";
                    if (!JPrincipal.getInstance().crearVentanaConfirmacion(error)) {
                        return;
                    }
                }
            } else {
                try {
                    //Fasttrack
                    PinPadFasttrack ppf = PinPadFasttrack.getInstance();
                    ppf.getiPinPad().procesoControl();
                    ventana_padre.crearInformacion("El pinpad se ha configurado correctamente");
                } catch (PinPadFasttrackException ex) {
                    java.util.logging.Logger.getLogger(JCerrarCaja.class.getName()).log(Level.SEVERE, null, ex);
                    ventana_padre.crearError("Se ha producido un error configurando el PinPad");
                }
            }
        }
        Sesion.getCajaActual().cierreDeCaja();
        ventana_padre.initViewMenuVentas();
        ventana_padre.crearConfirmacion("La caja se cerró satisfactoriamente. " + mensaje);
        log.info("La Caja se cerró satisfactoriamente." + mensaje);
        //Se cierran todas las sesiones del sistema
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            //Operating system is based on Windows
            Runtime.getRuntime().exec("taskkill /IM JAVA.EXE");
        } else if (os.contains("osx")) {
            //Operating system is Apple OSX based
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            //Operating system is based on Linux/Unix/*AIX
        }
    }

    private void accionAperturaParcialDeCaja() {
        try {
            log.debug("Accion Apertura Parcial de caja");

            if (Sesion.getCajaActual().isCajaAbierta() && !Sesion.getCajaActual().isCajaParcialAbierta()) {
                Sesion.getCajaActual().aperturaParcialDeCaja();
                ventana_padre.crearConfirmacion("Se realizó la apertura parcial de la caja");
                refrescaVista();
            }
        } catch (Exception e) {
            log.error("No se pudo realizar la apertura parcial de caja", e);
            ventana_padre.crearError("No se pudo realizar la apertura parcial de caja");
        }
    }

    private void refrescaMenu() {

        // Etiquetas:
        //<html><center> Apertura Parcial <br/>F9</center></html>
        //<html><center> Abrir Caja <br/>F2</center></html>
        //<html><center> Cierre de Dia <br/>F4</center></html>
        //<html><center> Cierre parcial <br/>F8</center></html>
        //Ambas cajas abiertas
        if (Sesion.getCajaActual().isCajaParcialAbierta() && Sesion.getCajaActual().isCajaAbierta()) {
            b_apertura_parcial.setText("<html><center> Apertura Parcial <br/>F9</center></html>");
            b_apertura_parcial.setEnabled(false);
            b_apertura_parcial.setFocusable(false);
            b_apertura_parcial.setVisible(true);
            b_cajas_recueto.setEnabled(true);
            b_cajas_recueto.setVisible(true);
            b_cajas_recueto.setFocusable(true);

            b_cierre_parcial_caja.setText("<html><center> Cierre parcial <br/>F8</center></html>");
            b_cierre_parcial_caja.setEnabled(true);
            b_cierre_parcial_caja.setVisible(true);
            b_cierre_parcial_caja.setFocusable(true);

            b_movimientos.setEnabled(true);
            b_movimientos.setVisible(true);
            b_movimientos.setFocusable(true);
            b_abrir_cajon.setEnabled(true);
            b_abrir_cajon.setVisible(true);
            b_abrir_cajon.setFocusable(true);
            b_menu_ppal.setEnabled(true);
            b_menu_ppal.setFocusable(true);
            b_abrir_cajon.setVisible(true);
            b_abrir_cajon.setFocusable(true);

            lb_info1.setForeground(Color.BLUE);
            lb_info1.setText("CAJA ABIERTA");
            lb_info2.setForeground(Color.BLUE);
            lb_info2.setText("APERTURA PARCIAL POR: " + Sesion.getCajaActual().getCajaParcialActual().getIdUsuario());
            lb_info3.setText(Sesion.getCajaActual().getCajaParcialActual().getDesUsuario());
            b_cajas_recueto.requestFocus();
        } else {
            // Caja abierta, pero hay que hacer apertura parcial
            if (Sesion.getCajaActual().isCajaAbierta()) {
                b_apertura_parcial.setText("<html><center> Apertura Parcial <br/>F9</center></html>");
                b_apertura_parcial.setEnabled(true);
                b_apertura_parcial.setFocusable(true);
                b_apertura_parcial.setVisible(true);
                b_cajas_recueto.setEnabled(false);
                b_cajas_recueto.setFocusable(false);

                b_cierre_parcial_caja.setText("<html><center> Cierre de Dia <br/>F4</center></html>");
                b_cierre_parcial_caja.setEnabled(true);
                b_cierre_parcial_caja.setFocusable(true);
                b_cierre_parcial_caja.setVisible(true);

                b_movimientos.setEnabled(true);
                b_movimientos.setFocusable(true);
                b_menu_ppal.setEnabled(true);
                b_menu_ppal.setFocusable(true);
                b_abrir_cajon.setVisible(true);
                b_abrir_cajon.setFocusable(true);

                lb_info1.setForeground(Color.BLUE);
                lb_info1.setText("CAJA DEL DIA ABIERTA");
                lb_info2.setForeground(Color.RED);
                lb_info2.setText("CAJA PARCIAL CERRADA");
                lb_info3.setText("");
                b_apertura_parcial.requestFocus();
            } else {
                // Cajas cerradas
                b_apertura_parcial.setText("<html><center> Abrir Caja <br/>F2</center></html>");
                b_apertura_parcial.setEnabled(true);
                b_apertura_parcial.setVisible(true);
                b_apertura_parcial.setFocusable(true);

                b_cajas_recueto.setEnabled(false);
                b_cajas_recueto.setFocusable(false);

                b_cierre_parcial_caja.setText("<html><center> Cierre de Dia <br/>F4</center></html>");
                b_cierre_parcial_caja.setEnabled(false);
                b_cierre_parcial_caja.setFocusable(false);
                b_cierre_parcial_caja.setVisible(false);

                b_movimientos.setEnabled(false);
                b_movimientos.setFocusable(false);
                b_menu_ppal.setEnabled(true);
                b_menu_ppal.setFocusable(true);
                b_abrir_cajon.setVisible(true);
                b_abrir_cajon.setFocusable(true);

                lb_info1.setForeground(Color.RED);
                lb_info1.setText("CAJA DEL DIA CERRADA");
                lb_info2.setText("");
                lb_info3.setText("");
                b_apertura_parcial.requestFocus();
            }
        }
        if (PinPad.getInstance().isAutomatico()) {
            b_conf_pinpad.setEnabled(true);
            b_act_pinpad.setEnabled(true);
        } else {
            b_conf_pinpad.setEnabled(false);
            b_act_pinpad.setEnabled(false);
        }

        if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_ESTADO_AUTOMATICO)) {
            b_desactivar_pinpad.setText("<html><center> Activar PinPad <br/>F6</center></html>");
        }
    }

    private void accionAmpliarRetiro() {
        JPrincipal.setPopupActivo(v_retiro);
        v_retiro.setVisible(true);
        JPrincipal.setPopupActivo(null);
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        b_menu_ppal.requestFocus();
    }

    @Override
    public void focusGained(FocusEvent fe) {
    }

    @Override
    public void focusLost(FocusEvent fe) {
    }
}
