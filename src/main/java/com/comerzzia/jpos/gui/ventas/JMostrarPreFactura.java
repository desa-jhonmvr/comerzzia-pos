/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JDevolucion.java
 *
 * Created on 16-sep-2011, 8:06:51
 */
package com.comerzzia.jpos.gui.ventas;

import com.comerzzia.jpos.entity.db.CabPrefactura;
import com.comerzzia.jpos.entity.db.DetPrefactura;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.gui.ventas.modelos.MostrarDetPrefacturaCellRenderer;
import com.comerzzia.jpos.gui.ventas.modelos.MostrarDetPrefacturaTableModel;
import com.comerzzia.jpos.persistencia.facturacion.prefactura.CabPrefacturaDao;
import com.comerzzia.jpos.persistencia.facturacion.prefactura.DetPreFacturaDao;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.util.enums.EnumEstadoPrefactura;
import com.comerzzia.util.fechas.Fechas;
import com.comerzzia.util.swing.acciones.Acciones;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Gabriel Simbania
 */
public class JMostrarPreFactura extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError {

    private static Logger LOG_POS = Logger.getMLogger(JMostrarPreFactura.class);
    private static final long serialVersionUID = 6740534169318520125L;
    
    JPrincipal ventana_padre = null;
    List<IValidableForm> formulario;
    String mensajeError = "";
    boolean procesar = false;

    //Nuevos
    List<DetPrefactura> detPrefacturas;
    MaskFormatter formatter;
    private CabPrefactura cabPrefactura;

    /**
     * @author Gabriel Simbania
     * @param ventana_padre
     * @param cabPrefactura 
     */
    
    public JMostrarPreFactura(JPrincipal ventana_padre,CabPrefactura cabPrefactura ) {
        super();
        this.ventana_padre = ventana_padre;
        this.cabPrefactura=cabPrefactura;
        try {
            formulario = new LinkedList<>();
            initComponents();
            
            JNombre.setText(cabPrefactura.getCabNombre()+" "+cabPrefactura.getCabApellido());
            JDocumento.setText(cabPrefactura.getCabCodCli());
            JEstado.setText(cabPrefactura.getCabEstado().getDescripcion());
            JFecha.setText(Fechas.dateToString(cabPrefactura.getCabFecha(),Fecha.PATRON_FECHA_CORTA));
            t_total_con_iva.setText(String.valueOf(cabPrefactura.getCabTotalConDstoConIva()));
            t_total_sin_iva.setText(String.valueOf(cabPrefactura.getCabTotalConDstoSinIva()));
            b_liquidar_reservacion.setEnabled(cabPrefactura.getCabEstado()==EnumEstadoPrefactura.PROCESADO || cabPrefactura.getCabEstado()==EnumEstadoPrefactura.PROCESADO_NOVEDAD);

            
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_buscar_reservaciones.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));

            //creamos el formulario
            crearFormulario();

            
            registraEventoEnterBoton();
            addFunctionKeys();
            //metemos los elementos en el formulario
        } catch (Exception e) {
            LOG_POS.error(e);
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

        sesion1 = new com.comerzzia.jpos.servicios.login.Sesion();
        lb_error = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_preFactura = new javax.swing.JTable();
        JNombre = new javax.swing.JLabel();
        JDocumento = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        ld_cedula = new javax.swing.JLabel();
        lb_nombre = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        b_liquidar_reservacion = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_fecha = new javax.swing.JLabel();
        JFecha = new javax.swing.JLabel();
        lb_total = new javax.swing.JLabel();
        t_total_con_iva = new javax.swing.JTextField();
        lb_total1 = new javax.swing.JLabel();
        t_total_sin_iva = new javax.swing.JTextField();
        lb_fecha1 = new javax.swing.JLabel();
        JEstado = new javax.swing.JLabel();
        lb_observacion = new javax.swing.JLabel();
        JObservacion = new javax.swing.JLabel();
        b_volver = new com.comerzzia.jpos.gui.components.form.JButtonForm();

        setMaximumSize(new java.awt.Dimension(433, 457));
        setMinimumSize(new java.awt.Dimension(433, 457));
        setPreferredSize(new java.awt.Dimension(954, 723));
        setRequestFocusEnabled(false);

        lb_error.setForeground(new java.awt.Color(204, 0, 0));
        lb_error.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lb_error.setAutoscrolls(true);

        tb_preFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ARTÍCULO", "DESCRIPCIÓN", "CANTIDAD", "PRECIO"
            }
        ));
        tb_preFactura.setRequestFocusEnabled(false);
        tb_preFactura.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tb_preFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_preFacturaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tb_preFactura);

        JNombre.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JNombre.setText("Nombre y Apellidos");

        JDocumento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JDocumento.setText("RUC,CED,PAS y Número");

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel11.setText("Datos");

        ld_cedula.setFont(ld_cedula.getFont().deriveFont(ld_cedula.getFont().getStyle() | java.awt.Font.BOLD));
        ld_cedula.setForeground(new java.awt.Color(51, 153, 255));
        ld_cedula.setText("Cédula:");
        ld_cedula.setToolTipText("");

        lb_nombre.setFont(lb_nombre.getFont().deriveFont(lb_nombre.getFont().getStyle() | java.awt.Font.BOLD));
        lb_nombre.setForeground(new java.awt.Color(51, 153, 255));
        lb_nombre.setText("Nombre: ");

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabel12.setText("Cliente");

        b_liquidar_reservacion.setActionCommand("<html><center>Facturar<br>F10</center></html>");
        b_liquidar_reservacion.setLabel("<html><center>Facturar<br>F10</center></html>");
        b_liquidar_reservacion.setPreferredSize(new java.awt.Dimension(127, 48));
        b_liquidar_reservacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_liquidar_reservacionActionPerformed(evt);
            }
        });

        lb_fecha.setFont(lb_fecha.getFont().deriveFont(lb_fecha.getFont().getStyle() | java.awt.Font.BOLD));
        lb_fecha.setForeground(new java.awt.Color(51, 153, 255));
        lb_fecha.setText("Fecha: ");

        JFecha.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JFecha.setText("Fecha");

        lb_total.setFont(lb_total.getFont().deriveFont(lb_total.getFont().getStyle() | java.awt.Font.BOLD));
        lb_total.setForeground(new java.awt.Color(51, 153, 255));
        lb_total.setText("Total sin IVA:");

        t_total_con_iva.setText("jTextField1");
        t_total_con_iva.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_con_iva.setEnabled(false);

        lb_total1.setFont(lb_total1.getFont().deriveFont(lb_total1.getFont().getStyle() | java.awt.Font.BOLD));
        lb_total1.setForeground(new java.awt.Color(51, 153, 255));
        lb_total1.setText("Total con IVA:");

        t_total_sin_iva.setText("jTextField1");
        t_total_sin_iva.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_total_sin_iva.setEnabled(false);

        lb_fecha1.setFont(lb_fecha1.getFont().deriveFont(lb_fecha1.getFont().getStyle() | java.awt.Font.BOLD));
        lb_fecha1.setForeground(new java.awt.Color(51, 153, 255));
        lb_fecha1.setText("Estado: ");

        JEstado.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JEstado.setText("Estado");

        lb_observacion.setFont(lb_observacion.getFont().deriveFont(lb_observacion.getFont().getStyle() | java.awt.Font.BOLD));
        lb_observacion.setForeground(new java.awt.Color(51, 153, 255));
        lb_observacion.setText("Observación:");

        JObservacion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        JObservacion.setText("Observacion");

        b_volver.setText("<html><center>Volver<br/>F2</center></html>");
        b_volver.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_volverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(703, 703, 703)
                        .addComponent(lb_error, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(b_liquidar_reservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(273, 273, 273))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 79, Short.MAX_VALUE)))))
                .addGap(46, 46, 46))
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lb_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_total_sin_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(66, 66, 66)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lb_total1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(t_total_con_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lb_observacion, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ld_cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(374, 374, 374))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_nombre)
                            .addComponent(JNombre))))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JDocumento)
                    .addComponent(ld_cedula))
                .addGap(26, 26, 26)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JFecha)
                    .addComponent(lb_fecha)
                    .addComponent(lb_fecha1)
                    .addComponent(JEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_total)
                    .addComponent(t_total_con_iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_total1)
                    .addComponent(t_total_sin_iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_observacion)
                    .addComponent(JObservacion))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_liquidar_reservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tb_preFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_preFacturaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
            tb_preFactura.transferFocusBackward();

        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            tb_preFactura.transferFocus();
        }
    }//GEN-LAST:event_tb_preFacturaKeyPressed

    private void b_liquidar_reservacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_liquidar_reservacionActionPerformed
        b_liquidar_reservacion.setEnabled(Boolean.FALSE);
        TicketService.crearFacturaDesdePrefactura(cabPrefactura);
        iniciaVista();
        
    }//GEN-LAST:event_b_liquidar_reservacionActionPerformed

    private void b_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_volverActionPerformed
        // TODO add your handling code here:
         ventana_padre.irVentanaPrefactura();
    }//GEN-LAST:event_b_volverActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JDocumento;
    private javax.swing.JLabel JEstado;
    private javax.swing.JLabel JFecha;
    private javax.swing.JLabel JNombre;
    private javax.swing.JLabel JObservacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_liquidar_reservacion;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_volver;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_error;
    private javax.swing.JLabel lb_fecha;
    private javax.swing.JLabel lb_fecha1;
    private javax.swing.JLabel lb_nombre;
    private javax.swing.JLabel lb_observacion;
    private javax.swing.JLabel lb_total;
    private javax.swing.JLabel lb_total1;
    private javax.swing.JLabel ld_cedula;
    private com.comerzzia.jpos.servicios.login.Sesion sesion1;
    private javax.swing.JTextField t_total_con_iva;
    private javax.swing.JTextField t_total_sin_iva;
    private javax.swing.JTable tb_preFactura;
    // End of variables declaration//GEN-END:variables

    

    

    private void crearFormulario() {
        // Elementos del formulario susceptibles de validarse o resetearse 
        
    }

     private void accionMenu() {
        try {
            ventana_padre.mostrarMenu();
        }
        catch (Exception e) {
            LOG_POS.debug("No se pudo llamar al menu", e);
            ventana_padre.crearError(null);
        }
    }
    

    @Override
    public void clearError() {
        lb_error.setText("");
    }

    @Override
    public void iniciaVista() {
        try {
            refrescarTablaPrefactura();
            cabPrefactura = CabPrefacturaDao.consultaCabeceraPrefacturaByUid(cabPrefactura.getUidCabId());
            b_liquidar_reservacion.setEnabled(cabPrefactura.getCabEstado()==EnumEstadoPrefactura.PROCESADO || cabPrefactura.getCabEstado()==EnumEstadoPrefactura.PROCESADO_NOVEDAD);
        } catch (Exception ex) {
            LOG_POS.error("Error la iniciar Vista",ex);
        }
    }

    private void refrescarTablaPrefactura() {
        try {
            
            detPrefacturas = DetPreFacturaDao.consultaDetalleByUidCabId(cabPrefactura.getUidCabId());
            MostrarDetPrefacturaTableModel modelo = new MostrarDetPrefacturaTableModel(detPrefacturas);
            tb_preFactura.setModel(modelo);
            tb_preFactura.setDefaultRenderer(Object.class, new MostrarDetPrefacturaCellRenderer());
            dibujarTablaPrefactura();

        } catch (Exception ex) {
            LOG_POS.error("No se pudo recargar la Interfaz de Pendiente de Entrega", ex);
        }
    }

    private void dibujarTablaPrefactura() {
        tb_preFactura.getColumnModel().getColumn(0).setPreferredWidth(45);
        tb_preFactura.getColumnModel().getColumn(1).setPreferredWidth(180);
        tb_preFactura.getColumnModel().getColumn(2).setPreferredWidth(45);
        tb_preFactura.getColumnModel().getColumn(3).setPreferredWidth(45);
    }

    private void addFunctionKeys() {

        LOG_POS.info("Función de acciones de teclado");

        Acciones.crearAccionFocoTabla(this, tb_preFactura);

        tb_preFactura.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tb_preFactura.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                accionEnter();
            }
        });
        
        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_volverActionPerformed(ae);
            }
        };
        addHotKey(f2, "IdentClientF2", listenerf2);
        
        KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
        Action listenerf10 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_liquidar_reservacionActionPerformed(ae);
            }
        };
        addHotKey(f10, "IdentClientF10", listenerf10);
    }

    private void accionEnter() {
        try {
            int fila = tb_preFactura.getSelectedRow();

        } catch (IndexOutOfBoundsException e) {
            JPrincipal.getInstance().crearError("Debe de realizar una búsqueda antes de cambiar una línea de factura. ");
        } catch (Exception e) {
            LOG_POS.error(e.getMessage(), e);
            JPrincipal.getInstance().crearError("Error cambiando el estado de las líneas.");
        }
    }

    @Override
    public void iniciaFoco() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addError(ValidationFormException e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
