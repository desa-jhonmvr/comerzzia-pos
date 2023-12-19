/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JConsultaLetrasFacturaCambio.java
 *
 * Created on 02-oct-2012, 13:21:01
 */
package com.comerzzia.jpos.gui.credito.letras;

import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.credito.JOperacionesTarjeta;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV;
import com.comerzzia.jpos.gui.credito.letras.modelos.LetrasCambioCellRenderer;
import com.comerzzia.jpos.gui.credito.letras.modelos.LetrasCambioTableModel;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.letras.LetraCambioManager;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public class JConsultaLetrasFacturaCambio extends JVentanaDialogo {

    protected static Logger log = Logger.getMLogger(JConsultaLetrasFacturaCambio.class);
    public static LetraCambioManager manejador;

    /** Creates new form JConsultaLetrasFacturaCambio */
    public JConsultaLetrasFacturaCambio() {
        initComponents();
        registraEventoEnterBoton();
        crearAccionFocoTabla(this, tb_listaLetras, KeyEvent.VK_T, InputEvent.CTRL_MASK);

        URL myurl;
        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);

        v_pagos.setLocationRelativeTo(null);
        v_pagos.setIconImage(icon.getImage());
        p_pagos.setContenedor(v_pagos);


        addFunctionKeys();
        manejador = LetraCambioManager.getInstance();
        tb_listaLetras.setDefaultRenderer(Object.class, new LetrasCambioCellRenderer());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        lcm = new com.comerzzia.jpos.servicios.letras.LetraCambioManager();
        v_pagos = new javax.swing.JDialog();
        p_pagos = new com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV();
        jLabel1 = new javax.swing.JLabel();
        b_volver = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jPanel3 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lb_local = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lb_fecha = new javax.swing.JLabel();
        lb_caja = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lb_plazo = new javax.swing.JLabel();
        lb_factura = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lb_valor = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lb_nombre = new javax.swing.JLabel();
        lb_interes = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        lb_total = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lb_cedula = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        lb_direccion = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        lb_telefono1 = new javax.swing.JLabel();
        lb_direccion_oficina = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        lb_telefono2 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        b_pagar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_listaLetras = new javax.swing.JTable();
        b_pagar_sin_mora = new com.comerzzia.jpos.gui.components.form.JButtonForm();

        v_pagos.setMinimumSize(new java.awt.Dimension(1024, 660));
        v_pagos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
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

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+7));
        jLabel1.setText("CONSULTA DE SALDO DE CRÉDITO TEMPORAL");

        b_volver.setText("<html><center>Volver <br/>F2</center></html>");
        b_volver.setNextFocusableComponent(b_pagar);
        b_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_volverActionPerformed(evt);
            }
        });

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Local:");

        lb_local.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_local.setText("blas");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Fecha:");

        lb_fecha.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fecha.setText("blas");

        lb_caja.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_caja.setText("blas");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Caja:");

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Plazo:");

        lb_plazo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_plazo.setText("blas");

        lb_factura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_factura.setText("blas");

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Factura:");

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Valor:");

        lb_valor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_valor.setText("blas");

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Nombre:");

        lb_nombre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_nombre.setText("blas");

        lb_interes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_interes.setText("blas");

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Intereses:");

        lb_total.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_total.setText("blas");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Total:");

        lb_cedula.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_cedula.setText("blas");

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Cédula:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_local, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_caja, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_factura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_nombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_cedula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_fecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_plazo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_valor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_interes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(lb_local)
                    .addComponent(lb_fecha)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(lb_caja)
                    .addComponent(lb_plazo)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(lb_factura)
                    .addComponent(lb_valor)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lb_nombre)
                    .addComponent(lb_interes)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(lb_cedula)
                    .addComponent(lb_total)
                    .addComponent(jLabel41))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Dirección:");

        lb_direccion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_direccion.setText("blas");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Teléfono");

        lb_telefono1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_telefono1.setText("blas");

        lb_direccion_oficina.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_direccion_oficina.setText("blas");

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Dirección Oficina:");

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Teléfono");

        lb_telefono2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_telefono2.setText("blas");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_direccion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_direccion_oficina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_telefono1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_telefono2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(lb_direccion)
                    .addComponent(lb_telefono1)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(lb_direccion_oficina)
                    .addComponent(lb_telefono2)
                    .addComponent(jLabel51))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel44.setFont(jLabel44.getFont().deriveFont(jLabel44.getFont().getStyle() | java.awt.Font.BOLD, jLabel44.getFont().getSize()+1));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Datos Factura:");

        jLabel65.setFont(jLabel65.getFont().deriveFont(jLabel65.getFont().getStyle() | java.awt.Font.BOLD, jLabel65.getFont().getSize()+1));
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel65.setText("Datos Cliente:");

        b_pagar.setText("<html><center>Pagar <br/>F9</center></html>");
        b_pagar.setNextFocusableComponent(b_pagar_sin_mora);
        b_pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pagarActionPerformed(evt);
            }
        });

        tb_listaLetras.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_listaLetras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${facturaSeleccionada.listaLetras}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, lcm, eLProperty, tb_listaLetras);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${cuota}"));
        columnBinding.setColumnName("Cuota");
        columnBinding.setColumnClass(Long.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fechaCobro}"));
        columnBinding.setColumnName("Fecha Cobro");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fechaVencimiento}"));
        columnBinding.setColumnName("Fecha Vencimiento");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${valor}"));
        columnBinding.setColumnName("Valor");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        tb_listaLetras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_listaLetrasKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tb_listaLetras);

        b_pagar_sin_mora.setText("<html><center>Pagar Sin Mora<br/>F10</center></html>");
        b_pagar_sin_mora.setNextFocusableComponent(tb_listaLetras);
        b_pagar_sin_mora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pagar_sin_moraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pagar_sin_mora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_pagar_sin_mora, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void b_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_volverActionPerformed
        accionAtras();
    }//GEN-LAST:event_b_volverActionPerformed

    private void b_pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pagarActionPerformed
        if (b_pagar.isEnabled()) {
            accionPagar();
        }
}//GEN-LAST:event_b_pagarActionPerformed

    private void tb_listaLetrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_listaLetrasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            accionSeleccionarLetra();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            b_volver.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            accionAtras();
        }
}//GEN-LAST:event_tb_listaLetrasKeyPressed

    private void v_pagosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_v_pagosWindowOpened
        p_pagos.iniciaFoco();
}//GEN-LAST:event_v_pagosWindowOpened

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        b_volver.requestFocus();
    }//GEN-LAST:event_formFocusGained

    private void b_pagar_sin_moraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pagar_sin_moraActionPerformed
        if (b_pagar_sin_mora.isEnabled()) {
            accionPagarSinMora();
        }
    }//GEN-LAST:event_b_pagar_sin_moraActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pagar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_pagar_sin_mora;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_volver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_caja;
    private javax.swing.JLabel lb_cedula;
    private javax.swing.JLabel lb_direccion;
    private javax.swing.JLabel lb_direccion_oficina;
    private javax.swing.JLabel lb_factura;
    private javax.swing.JLabel lb_fecha;
    private javax.swing.JLabel lb_interes;
    private javax.swing.JLabel lb_local;
    private javax.swing.JLabel lb_nombre;
    private javax.swing.JLabel lb_plazo;
    private javax.swing.JLabel lb_telefono1;
    private javax.swing.JLabel lb_telefono2;
    private javax.swing.JLabel lb_total;
    private javax.swing.JLabel lb_valor;
    private com.comerzzia.jpos.servicios.letras.LetraCambioManager lcm;
    private com.comerzzia.jpos.gui.reservaciones.JReservacionesPagosV p_pagos;
    private javax.swing.JTable tb_listaLetras;
    private javax.swing.JDialog v_pagos;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private void accionSeleccionarLetra() {
    }

    @Override
    public void accionAceptar() {
    }

    @Override
    public void limpiarFormulario() {
    }

    public void iniciaVista() {
        try{
            limpiarFormulario();

            estableceDatosFactura();
            refrescaTabla();
            
            // Activación/Desactivación de acción de pagos
            if (manejador.getLetraSeleccionada().isCuotasPendientesCobro()){
                b_pagar.setEnabled(true);
                b_pagar_sin_mora.setEnabled(true);
            }
            else{
                b_pagar.setEnabled(false);
                b_pagar_sin_mora.setEnabled(false);
            }
            b_volver.requestFocus();
            
        }
        catch(Exception e){
            log.error("iniciaVista() - Error inicializando vista de Letra de Cambio: " + e.getMessage(), e );
        }
    }

    void inicaiFoco() {
        b_volver.requestFocus();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }

    private void addFunctionKeys() {
        KeyStroke f9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
        Action listenerf9 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionPagar();
            }
        };
        addHotKey(f9, "LTRFCF9", listenerf9);
        
        KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
        Action listenerf10 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                accionPagarSinMora();
            }
        };
        addHotKey(f10, "LTRFCF10", listenerf10);        

        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_volverActionPerformed(ae);
            }
        };
        addHotKey(f2, "LTRFCF2", listenerf2);

    }

    private void estableceDatosFactura() {
        lb_caja.setText(manejador.getLetraSeleccionada().getCodCaja());
        lb_factura.setText(manejador.getLetraSeleccionada().getIdTicket().toString());
        lb_fecha.setText(manejador.getLetraSeleccionada().getFecha().getStringHora());
        lb_interes.setText("$ " + manejador.getLetraSeleccionada().getImporteIntereses().toString());
        lb_local.setText(manejador.getLetraSeleccionada().getCodAlmacen() + " " + Sesion.getTienda().getAlmacen().getDesalm());
        lb_plazo.setText("" + manejador.getLetraSeleccionada().getPlazo());
        lb_valor.setText("$ " + manejador.getLetraSeleccionada().getTotal().toString());
        lb_total.setText("$ " +manejador.getLetraSeleccionada().getTotalConInteresesR().toString());
        // Info del cliente
        lb_nombre.setText(manejador.getLetraSeleccionada().getCliente().getNombreYApellidos());
        lb_cedula.setText(manejador.getLetraSeleccionada().getCliente().getIdentificacion());
        lb_direccion.setText(manejador.getLetraSeleccionada().getCliente().getDireccion());
        lb_direccion_oficina.setText(manejador.getLetraSeleccionada().getCliente().getDireccion());
        lb_telefono1.setText(manejador.getLetraSeleccionada().getCliente().getTelefono1());
        lb_telefono2.setText(manejador.getLetraSeleccionada().getCliente().getTelefonoMovil());
        
    }

    private void refrescaTabla() {
        tb_listaLetras.setModel(new LetrasCambioTableModel(manejador.getLetraSeleccionada().getCuotas()));

    }

    private void accionPagar() {
        if (b_pagar.isEnabled()) {
            if (manejador.getLetraSeleccionada().isCuotasPendientesCobro()) {
                contenedor.setVisible(false);
                limpiarFormulario();
                contenedor.setVisible(false);
                crearVentanaPagos(manejador.getLetraSeleccionada());
                limpiarFormulario();
            }
        }
    }
    
    private void accionPagarSinMora() {
        if (b_pagar_sin_mora.isEnabled()) {
            if (manejador.getLetraSeleccionada().isCuotasPendientesCobro()) {
                try {
                    String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.PAGO_CREDITO_TEMPORAL_SIN_MORA, "<html>Autorizar Pago a Credito Temporal<br/>sin Mora</html>");
                    manejador.getLetraSeleccionada().getProximaCuotaCobro().setNoPagarMora(true);
                    contenedor.setVisible(false);
                    limpiarFormulario();
                    contenedor.setVisible(false);
                    crearVentanaPagos(manejador.getLetraSeleccionada());
                    limpiarFormulario();
                }
                catch (SinPermisosException ex) {
                    log.debug(" El usuario no tien permiso para realizar la operación");
                }
            }
        }
    }    

    private void crearVentanaPagos(LetraBean letra) {
        v_pagos.getContentPane().removeAll();
        p_pagos = new JReservacionesPagosV(letra, letra.getProximaCuotaCobro());
        javax.swing.GroupLayout v_pagosLayout = new javax.swing.GroupLayout(v_pagos.getContentPane());
        v_pagos.getContentPane().setLayout(v_pagosLayout);
        v_pagosLayout.setHorizontalGroup(
                v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v_pagosLayout.createSequentialGroup().addContainerGap().addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        v_pagosLayout.setVerticalGroup(
                v_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_pagosLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(p_pagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        p_pagos.setContenedor(v_pagos);
        v_pagos.getContentPane().add(p_pagos);
        v_pagos.requestFocus();
        p_pagos.iniciaVista();
        v_pagos.setVisible(true);
        p_pagos.isCancelado();
    }

    public void establecerFoco() {
        b_volver.requestFocus();
    }
    
    private void accionAtras() {
        cerrarVentana();
        JOperacionesTarjeta.getInstance().volverAConsultaLetrasFactura();
    }
}
