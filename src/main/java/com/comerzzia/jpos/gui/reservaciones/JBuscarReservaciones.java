/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JArticulos.java
 *
 * Created on 07-jul-2011, 9:33:44
 */
package com.comerzzia.jpos.gui.reservaciones;

//import com.comerzzia.jpos.entity.Articulos;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasAbono;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasReservacion;
import com.comerzzia.jpos.entity.codigosBarra.NotAReservationOfThisShopException;
import com.comerzzia.jpos.entity.codigosBarra.NotAValidBarCodeException;
import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.gui.*;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.reservaciones.modelos.BusquedaReservacionesCellRenderer;
import com.comerzzia.jpos.gui.reservaciones.modelos.BusquedaReservacionesTableModel;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.InputFormVerifier;
import com.comerzzia.jpos.gui.validation.ValidadorFecha;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasException;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.reservaciones.ParamBuscarReservaciones;
import com.comerzzia.jpos.servicios.reservaciones.ReservaNotFoundException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.util.swing.acciones.Acciones;
import es.mpsistemas.util.log.Logger;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MGRI
 */
public class JBuscarReservaciones extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError {

    
    private static Logger log = Logger.getMLogger(JBuscarReservaciones.class);
    
    JPrincipal ventana_padre = null;
    Cliente invitado;    
    List<ReservaBean> lista;

    public JBuscarReservaciones() {
        super();
        lista = new ArrayList<ReservaBean>();
        initComponents();
        
    }

    public JBuscarReservaciones(JPrincipal ventana_padre) {
        super();
        this.ventana_padre = ventana_padre;

        // Imagen de fondo
        try {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_buscar_reservaciones.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        }
        catch (IOException ex) {
            log.error("No se pudo cargar la imagen de fondo de cBúsqueda de reservaciones");
        }

        lista = new ArrayList<ReservaBean>();

        //iniciamos los componentes
        initComponents();
        t_fecha_desde.addValidador(new ValidadorFecha(), this);
        t_fecha_hasta.addValidador(new ValidadorFecha(), this);
        iniciaVista();

        // Añadimos una opción que equivale a todos los tipos de reservas


    }

    private void accionIntro(KeyEvent ke) {
        if ((t_nombre.hasFocus() || t_apellido.hasFocus() || t_fecha_desde.hasFocus() || t_fecha_hasta.hasFocus() 
                || t_numero_reserva.hasFocus() || t_numero_fidelizado.hasFocus() || t_documento.hasFocus() || t_nombre_org.hasFocus() 
                || t_apellidos_org.hasFocus() || l_tienda.hasFocus()) && ke.getKeyChar() == '\n') {
            
            JTextFieldForm focoEn = null;
            
            if (ke.getComponent() instanceof JTextFieldForm){
                focoEn = (JTextFieldForm) ke.getComponent();
            }
            realizarBusqueda(ke);
            
            if (tb_listaReservaciones.getRowCount() < 1 && focoEn !=null){
                focoEn.requestFocus();
            }
        }
    }

    private void inicia() {


        // Borde vacio ara tablas, Viewports y scrollpanels
        Border empty = new EmptyBorder(0, 0, 0, 0);
        tb_listaReservaciones.setDefaultRenderer(Object.class, new BusquedaReservacionesCellRenderer());
        refrescarTablaReservaciones(new BusquedaReservacionesTableModel(lista));

        jScrollPane1.setViewportBorder(empty);
        tb_listaReservaciones.setBorder(empty);
        jScrollPane1.getViewport().setOpaque(false);
        t_numero_reserva.addKeyListener(this);

        t_numero_fidelizado.addKeyListener(this);
        t_documento.addKeyListener(this);
        t_fecha_desde.addKeyListener(this);
        t_fecha_hasta.addKeyListener(this);
        t_nombre_org.addKeyListener(this);
        t_apellidos_org.addKeyListener(this);

        addFunctionKeys();
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

        listaTiposReservaciones = new com.comerzzia.jpos.entity.services.reservaciones.ListaTiposReservaciones();
        tiposReservacionesListRenderer = new com.comerzzia.jpos.gui.reservaciones.modelos.TiposReservacionesListRenderer();
        tiendasListRenderer1 = new com.comerzzia.jpos.gui.modelos.TiendasListRenderer();
        tiendasKeySelectionManager1 = new com.comerzzia.jpos.gui.modelos.TiendasKeySelectionManager();
        tiendasServices1 = new com.comerzzia.jpos.servicios.core.tiendas.TiendasServices();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_listaReservaciones = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        t_numero_reserva = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_numero_fidelizado = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel5 = new javax.swing.JLabel();
        l_tipoDocumento = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        t_documento = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_tipoReserva = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        l_estado = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        t_nombre = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel8 = new javax.swing.JLabel();
        t_apellido = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_fecha_desde = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_fecha_hasta = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        t_nombre_org = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel12 = new javax.swing.JLabel();
        t_apellidos_org = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_tienda = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        b_menu_ppal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error = new javax.swing.JLabel();

        List<ReservaTiposBean> lReservaciones = new LinkedList<ReservaTiposBean>(Sesion.tiposReservaciones);
        ReservaTiposBean resT = new ReservaTiposBean("TODOS");
        resT.setDesTipo("TODOS");
        lReservaciones.add (0,resT);
        listaTiposReservaciones.setListaTiposReservaciones(lReservaciones);

        tiposReservacionesListRenderer.setText("tiposReservacionesListRenderer1");

        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setPreferredSize(new java.awt.Dimension(1024, 723));

        jLabel1.setFont(jLabel1.getFont().deriveFont((float)18));
        jLabel1.setText("Búsqueda de Reservaciones");
        jLabel1.setFocusable(false);

        tb_listaReservaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_listaReservaciones.setOpaque(false);
        tb_listaReservaciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tb_listaReservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_listaReservacionesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_listaReservaciones);

        jPanel1.setOpaque(false);

        jLabel2.setDisplayedMnemonic('m');
        jLabel2.setLabelFor(t_numero_reserva);
        jLabel2.setText("Número de Reserva:");

        jLabel3.setDisplayedMnemonic('r');
        jLabel3.setLabelFor(l_tipoReserva);
        jLabel3.setText("Tipo de Reserva:");

        t_numero_reserva.setFont(new java.awt.Font("sansserif", 0, 14));
        t_numero_reserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_numero_reservaActionPerformed(evt);
            }
        });
        t_numero_reserva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numero_reservaKeyTyped(evt);
            }
        });

        t_numero_fidelizado.setFont(new java.awt.Font("sansserif", 0, 14));
        t_numero_fidelizado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numero_fidelizadoKeyTyped(evt);
            }
        });

        jLabel5.setDisplayedMnemonic('f');
        jLabel5.setLabelFor(t_numero_fidelizado);
        jLabel5.setText("Número Afiliación:");

        l_tipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CÉDULA", "RUC Natural", "RUC Jurídico", "PASAPORTE" }));
        l_tipoDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_tipoDocumentoKeyPressed(evt);
            }
        });

        jLabel4.setDisplayedMnemonic('p');
        jLabel4.setLabelFor(l_tipoDocumento);
        jLabel4.setText("Tipo de Documento:");

        jLabel36.setDisplayedMnemonic('d');
        jLabel36.setLabelFor(t_documento);
        jLabel36.setText("Documento:");

        t_documento.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_documento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_documentoKeyTyped(evt);
            }
        });

        l_tipoReserva.setPreferredSize(null);
        l_tipoReserva.setRenderer(tiposReservacionesListRenderer);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaTiposReservaciones}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, listaTiposReservaciones, eLProperty, l_tipoReserva);
        bindingGroup.addBinding(jComboBoxBinding);

        l_tipoReserva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_tipoReservaKeyPressed(evt);
            }
        });

        jLabel6.setDisplayedMnemonic('e');
        jLabel6.setLabelFor(l_estado);
        jLabel6.setText("Estado:");

        l_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Abierta", "Anulada", "Liquidada", "Caducada" }));
        l_estado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_estadoKeyPressed(evt);
            }
        });

        jLabel7.setDisplayedMnemonic('c');
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setLabelFor(t_nombre);
        jLabel7.setText("Nombre de Cliente:");

        t_nombre.setFont(new java.awt.Font("sansserif", 0, 14));
        t_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombreKeyTyped(evt);
            }
        });

        jLabel8.setDisplayedMnemonic('o');
        jLabel8.setLabelFor(t_apellido);
        jLabel8.setText("Apellidos de Cliente:");

        t_apellido.setFont(new java.awt.Font("sansserif", 0, 14));
        t_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_apellidoKeyTyped(evt);
            }
        });

        t_fecha_desde.setInputVerifier(new InputFormVerifier());
        t_fecha_desde.setPreferredSize(new java.awt.Dimension(8, 27));
        t_fecha_desde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_fecha_desdeKeyTyped(evt);
            }
        });

        t_fecha_hasta.setInputVerifier(new InputFormVerifier());
        t_fecha_hasta.setPreferredSize(new java.awt.Dimension(8, 27));
        t_fecha_hasta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_fecha_hastaKeyTyped(evt);
            }
        });

        jLabel9.setDisplayedMnemonic('s');
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setLabelFor(t_fecha_desde);
        jLabel9.setText("Desde:");

        jLabel10.setDisplayedMnemonic('h');
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setLabelFor(t_fecha_hasta);
        jLabel10.setText("Hasta:");

        jLabel11.setDisplayedMnemonic('n');
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setLabelFor(t_nombre_org);
        jLabel11.setText("Nombre Organizadora:");

        t_nombre_org.setInputVerifier(new InputFormVerifier());
        t_nombre_org.setPreferredSize(new java.awt.Dimension(8, 27));
        t_nombre_org.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombre_orgKeyTyped(evt);
            }
        });

        jLabel12.setDisplayedMnemonic('g');
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setLabelFor(t_apellidos_org);
        jLabel12.setText("Apellidos Organizadora:");

        t_apellidos_org.setInputVerifier(new InputFormVerifier());
        t_apellidos_org.setPreferredSize(new java.awt.Dimension(8, 27));
        t_apellidos_org.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_apellidos_orgKeyTyped(evt);
            }
        });

        l_tienda.setKeySelectionManager(tiendasKeySelectionManager1);
        l_tienda.setRenderer(tiendasListRenderer1);
        l_tienda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_tiendaKeyTyped(evt);
            }
        });

        jLabel13.setDisplayedMnemonic('L');
        jLabel13.setLabelFor(l_tienda);
        jLabel13.setText("Lugar:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_nombre_org, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_numero_reserva, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(t_numero_fidelizado, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(l_tipoReserva, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_fecha_desde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_fecha_hasta, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(t_apellidos_org, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(t_apellido, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(t_documento, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(l_tipoDocumento, 0, 305, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(l_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(l_tienda, 0, 181, Short.MAX_VALUE)))
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(l_tipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(t_numero_reserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(l_tipoReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(t_documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(t_numero_fidelizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(l_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_tienda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(t_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(t_fecha_desde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(t_fecha_hasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(t_nombre_org, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(t_apellidos_org, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        b_menu_ppal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_menu_ppal.setMargin(new java.awt.Insets(2, 9, 2, 9));
        b_menu_ppal.setNextFocusableComponent(t_numero_reserva);
        b_menu_ppal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menu_ppalActionPerformed(evt);
            }
        });

        lb_error.setForeground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_menu_ppal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 971, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(b_menu_ppal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void realizarBusqueda(KeyEvent evt) {
        if (evt.getKeyChar() == '\n') {
             
            BigInteger codigoReserva = null;
            try{
                String codbar = t_numero_reserva.getText().trim();
                if (codbar.length() == 14 || codbar.length()== 16 && codbar.startsWith("0")){
                    codbar = codbar.substring(1,14);
                }
                if (codbar.length()<16 && codbar.length()>10){
                    CodigoBarrasReservacion codigoReservacion=new CodigoBarrasReservacion(codbar);
                    codigoReserva = codigoReservacion.getnReserva();
                }
                else if (codbar.length()==18){
                    CodigoBarrasAbono cba = new CodigoBarrasAbono(new BigInteger(codbar.substring(0,15)),new Long(codbar.substring(15,18)));
                    CodigoBarrasReservacion codigoReservacion=new CodigoBarrasReservacion(cba.getnReserva().toString());
                    codigoReserva = codigoReservacion.getnReserva();
                    
                }
                else if (codbar.length()>0 && codbar.length()<=10){
                    codigoReserva = new BigInteger(codbar);
                }
                else if (codbar.length()>0 && codigoReserva == null){
                    throw new NotAValidBarCodeException();
                }
                t_numero_reserva.setText(codbar);
            }catch(NotAReservationOfThisShopException ex){
                t_numero_reserva.setText("");
                crearAdvertencia("La reservación entregada por el cliente no es de esta tienda.");
                return;
            }catch (NotAValidBarCodeException ex){
                crearAdvertencia("Código erroneo.");
                t_numero_reserva.setText("");
                return;
            }
            catch (Exception ex){
                crearAdvertencia("No se pudo leer el Código");
                t_numero_reserva.setText("");
                return;
            }
            try {
                lb_error.setText("");
                lista.clear();
                Date fechaDesde = new Date();
                Date fechaHasta = new Date();
                try {
                    if (!t_fecha_desde.getText().isEmpty()) {
                        fechaDesde = formateadorFechaCorta.parse(t_fecha_desde.getText().trim());
                    }
                    else {
                        fechaDesde = null;
                    }
                    if (!t_fecha_hasta.getText().isEmpty()) {
                        fechaHasta = formateadorFechaCorta.parse(t_fecha_hasta.getText().trim());
                    }
                    else {
                        fechaHasta = null;
                    }
                }
                catch (Exception e) {
                    log.error("Error en el formato de las fechas");
                    fechaDesde = null;
                    fechaHasta = null;
                }

                ParamBuscarReservaciones param = new ParamBuscarReservaciones();
                param.setDocumento(t_documento.getText().trim());
                param.setNumeroFidelizado(t_numero_fidelizado.getText().trim());
                
                if (!t_numero_reserva.getText().isEmpty() && codigoReserva!=null) {
                    param.setNumeroReserva(codigoReserva);
                }
                String tipoDoc = (String) l_tipoDocumento.getSelectedItem();
                if (tipoDoc.equals("CÉDULA")) {
                    tipoDoc = "CED";
                }
                else if (tipoDoc.equals("RUC Natural")) {
                    tipoDoc = "RUN";
                }
                else if (tipoDoc.equals("RUC Jurídico")) {
                    tipoDoc = "RUJ";
                }
                else if (tipoDoc.equals("PASAPORTE")) {
                    tipoDoc = "PAS";
                }
                param.setTipoDocumento(tipoDoc);
                param.setTipoReserva((ReservaTiposBean) l_tipoReserva.getSelectedItem());
                param.setEstado((String) l_estado.getSelectedItem());
                param.setNombre(t_nombre.getText().trim());
                param.setApellidos(t_apellido.getText().trim());
//                if (param.getTipoReserva().getListaInvitados()=='S')
//                {
                param.setNombreOrg(t_nombre_org.getText().trim());
                param.setApellidosOrg(t_apellidos_org.getText().trim());
//                }
                Almacen tiendaSeleccionada = ((Almacen) l_tienda.getSelectedItem());
                param.setTiendaReservacion(tiendaSeleccionada.getCodalm());         
                lista = ReservacionesServicios.consultar(param, fechaDesde, fechaHasta);

                if (lista.isEmpty()) {
                    crearAdvertencia("No se han encontrado reservas con los filtros indicados.");
                }
                else {
                }
                refrescarTablaReservaciones(new  BusquedaReservacionesTableModel(lista));
                if (tb_listaReservaciones.getRowCount() >= 0) {
                    ListSelectionModel selectionModel = tb_listaReservaciones.getSelectionModel();
                    selectionModel.setSelectionInterval(0, 0);
                    tb_listaReservaciones.requestFocus();
                }

            }
            catch (ReservaNotFoundException ex) {
                crearAdvertencia(ex.getMessage());
                refrescarTablaReservaciones(new  BusquedaReservacionesTableModel(lista));
            }
            catch (NumberFormatException ex){
                crearAdvertencia("El número de reserva tiene un formato no valido");
                t_numero_reserva.requestFocus();
            }        
            catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                crearAdvertencia("Error en la consulta de Reservaciones");
            }

        }
    }

private void t_numero_reservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_numero_reservaActionPerformed
}//GEN-LAST:event_t_numero_reservaActionPerformed

private void tb_listaReservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_listaReservacionesKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        try {
            Reservacion res = ReservacionesServicios.obtenerReserva(lista.get(tb_listaReservaciones.getSelectedRow()));
            res.setInvitadoActivo(invitado);
            if(res.getReservacion().isCaducada()){
                String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.VER_RESERVA_CADUCADA, JSolicitarAutorizacionVentana.MENSAJE_AUTORIZACION_RESERVA_CADUCADA);                
            }
            ventana_padre.addMostrarReservacionView(res);
            ventana_padre.showView("mostrar_reservacion");
        }
        catch (IndexOutOfBoundsException e) {
            ventana_padre.crearError("Debe de realizar una búsqueda antes de seleccionar una reservación. ");
        }
        catch (SinPermisosException ex) {
            log.debug("tb_listaReservacionesKeyPressed() - Sin permisos para consultar la reservación. ");
        }                
        catch (Exception e) {
            log.error(e.getMessage(),e);
            ventana_padre.crearError(null);
        }
    }
    else if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
        tb_listaReservaciones.transferFocusBackward();
    }
    else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
        tb_listaReservaciones.transferFocus();
    }
}//GEN-LAST:event_tb_listaReservacionesKeyPressed

private void b_menu_ppalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menu_ppalActionPerformed
    accionMenu();
}//GEN-LAST:event_b_menu_ppalActionPerformed

private void l_tipoDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_tipoDocumentoKeyPressed
    if (evt.getKeyChar() == '\n') {
        t_documento.requestFocus();
    }
}//GEN-LAST:event_l_tipoDocumentoKeyPressed

private void t_documentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_documentoActionPerformed
}//GEN-LAST:event_t_documentoActionPerformed

private void l_tipoReservaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_tipoReservaKeyPressed
    realizarBusqueda(evt);
}//GEN-LAST:event_l_tipoReservaKeyPressed

private void l_estadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_estadoKeyPressed
    realizarBusqueda(evt);
}//GEN-LAST:event_l_estadoKeyPressed

private void t_numero_reservaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numero_reservaKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_numero_reservaKeyTyped
                                    


private void t_numero_fidelizadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numero_fidelizadoKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_numero_fidelizadoKeyTyped

private void t_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombreKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_nombreKeyTyped

private void t_apellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_apellidoKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_apellidoKeyTyped

private void t_fecha_desdeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_fecha_desdeKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_fecha_desdeKeyTyped

private void t_fecha_hastaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_fecha_hastaKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_fecha_hastaKeyTyped

private void t_nombre_orgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombre_orgKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_nombre_orgKeyTyped

private void t_apellidos_orgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_apellidos_orgKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_apellidos_orgKeyTyped

private void t_documentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_documentoKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_t_documentoKeyTyped

private void l_tiendaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_tiendaKeyTyped
    accionIntro(evt);
}//GEN-LAST:event_l_tiendaKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_menu_ppal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox l_estado;
    private javax.swing.JComboBox l_tienda;
    private javax.swing.JComboBox l_tipoDocumento;
    private javax.swing.JComboBox l_tipoReserva;
    private javax.swing.JLabel lb_error;
    private com.comerzzia.jpos.entity.services.reservaciones.ListaTiposReservaciones listaTiposReservaciones;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_apellido;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_apellidos_org;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_documento;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fecha_desde;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fecha_hasta;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_nombre;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_nombre_org;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_numero_fidelizado;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_numero_reserva;
    private javax.swing.JTable tb_listaReservaciones;
    private com.comerzzia.jpos.gui.modelos.TiendasKeySelectionManager tiendasKeySelectionManager1;
    private com.comerzzia.jpos.gui.modelos.TiendasListRenderer tiendasListRenderer1;
    private com.comerzzia.jpos.servicios.core.tiendas.TiendasServices tiendasServices1;
    private com.comerzzia.jpos.gui.reservaciones.modelos.TiposReservacionesListRenderer tiposReservacionesListRenderer;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        lista.clear();

        // Cuidado. Los listeners no se deben de crear en inicia() dentro de iniciaVista porque cada vez que se entre en la pantalla se crean nuevos
        inicia();

        // Reset del formulario
        t_apellido.setText("");
        t_nombre.setText("");
        t_numero_reserva.setText("");
        t_documento.setText("");
        t_numero_fidelizado.setText("");
        t_apellidos_org.setText("");
        t_nombre_org.setText("");
        l_tipoDocumento.setSelectedIndex(0);
        l_estado.setSelectedIndex(1);
        l_tipoReserva.setSelectedIndex(0);
        // Inicio del foco
        t_numero_reserva.requestFocusInWindow();
        
        try {
            l_tienda.removeAllItems();
            l_tienda.addItem(Sesion.getTienda().getAlmacen());
            
            for(Almacen almacen: TiendasServices.consultarListaTiendas()){
                if(!almacen.equals(Sesion.getTienda().getAlmacen())){
                    l_tienda.addItem(almacen);                                                    
                }
            }            
           } catch (TiendasException e) {
            log.error("No se pudo cargar los almacenes: "+ e.getMessage());
           }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //accionIntro(ke);
    }

    private void crearAdvertencia(String msg) {

        ventana_padre.crearAdvertencia(msg);

    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void addFunctionKeys() {

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_menu_ppalActionPerformed(ae);

            }
        };
        addHotKey(f12, "IdentClientF12", listenerf12);

        KeyStroke altmast = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.VK_ALT);
        Action listeneraltmast = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tb_listaReservaciones.setColumnSelectionInterval(0, 0);
                tb_listaReservaciones.setRowSelectionInterval(0, 0);
                tb_listaReservaciones.requestFocus();
            }
        };
        addHotKey(altmast, "IdentClientaltmast", listeneraltmast);

        // Acción de acceso a Tabla
        Acciones.crearAccionFocoTabla(this, tb_listaReservaciones);

    }

    public JPrincipal getVentana_padre() {
        return ventana_padre;
    }

    public void setVentana_padre(JPrincipal ventana_padre) {
        this.ventana_padre = ventana_padre;
    }

    private void accionMenu() {
        try {
            ventana_padre.mostrarMenu();
        }
        catch (Exception e) {
            log.debug("No se pudo llamar al menú", e);
            ventana_padre.crearError(null);
        }
    }

    private void refrescarTablaReservaciones(BusquedaReservacionesTableModel table) {
        tb_listaReservaciones.setModel(table);
        tb_listaReservaciones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb_listaReservaciones.getColumnModel().getColumn(0).setPreferredWidth(75);      
        tb_listaReservaciones.getColumnModel().getColumn(1).setPreferredWidth(170);
        tb_listaReservaciones.getColumnModel().getColumn(2).setPreferredWidth(110);
        tb_listaReservaciones.getColumnModel().getColumn(3).setPreferredWidth(313);
        tb_listaReservaciones.getColumnModel().getColumn(4).setPreferredWidth(75);
        tb_listaReservaciones.getColumnModel().getColumn(5).setPreferredWidth(95);
        tb_listaReservaciones.getColumnModel().getColumn(6).setPreferredWidth(110);
        tb_listaReservaciones.getColumnModel().getColumn(7).setPreferredWidth(110);
        tb_listaReservaciones.getColumnModel().getColumn(8).setPreferredWidth(70);        
        tb_listaReservaciones.getColumnModel().getColumn(9).setPreferredWidth(70);
    }

    @Override
    public void iniciaFoco() {
        t_numero_reserva.requestFocus();
    }

    @Override
    public void addError(ValidationFormException e) {
        crearAdvertencia("El formato de la fecha es incorrecto");
    }

    @Override
    public void clearError() {
    }
    
    @Override
    public void dispose(){
        super.dispose();
        ventana_padre = null;
        listaTiposReservaciones=null;
        invitado = null;    
        lista = null;
        this.removeAll();
    }
}
