/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JDatosEnvio.java
 *
 * Created on 05-sep-2011, 16:00:46
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.envioDomicilio.EnvioDomicilioDTO;
import com.comerzzia.jpos.dto.ProcesoEnvioDomicilioDTO;
import com.comerzzia.jpos.dto.envioDomicilio.DatosCamionDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorCelularEcuador;
import com.comerzzia.jpos.gui.validation.ValidadorEmail;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.articulos.ArticulosDao;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.envio.domicilio.DateIterator;
import com.comerzzia.jpos.servicios.envio.domicilio.DiaEntrega;
import com.comerzzia.jpos.servicios.envio.domicilio.GvtParametroEntregaBean;
import com.comerzzia.jpos.servicios.envio.domicilio.ServicioEnvioDomicilio;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosDeEnvio;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.log.Logger;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.persistence.NoResultException;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MGRI
 */
public class JDatosEnvio extends JVentanaDialogo implements IVentanaDialogo, IViewerValidationFormError {

    private static final Logger log = Logger.getMLogger(JDatosEnvio.class);
    private boolean aceptado;
    private List<IValidableForm> formulario;
    private List<LineaTicket> listItem = new ArrayList<>();
    private List<LineaTicket> listItemDomicilio = new ArrayList<>();
    private List<LineaTicket> listaItemD = new ArrayList<>();
    private List<ItemDTO> itemListActualizar = new ArrayList<>();
    private List<ItemDTO> itemAct = new ArrayList<>();
    private List<LineaTicket> itemList = new ArrayList<>();
    DatosDeEnvio datosEnvio = null;
    //EnvioDomicilioDTO envioDomicilioDTO = new EnvioDomicilioDTO();
    //private List<EnvioDomicilioDTO> listEnvioDomicilioDTO = new ArrayList<>();
    LineaTicket lineaDomicilio = null;
    DefaultTableModel modeloDir;
    DefaultTableModel modeloItem;
    private EnvioDomicilioDTO envioDomicilio = new EnvioDomicilioDTO();
    private List<EnvioDomicilioDTO> envioDomicilioList = new ArrayList<>();
    private ClienteDTO cliente = new ClienteDTO();
    private List<ClienteDTO> listCliente = new ArrayList<>();
    int filas;
    private String cedula = null;
    private String direccion = null;
    private ProcesoEnvioDomicilioDTO trazabilidadEnvioDomicilioDTO = new ProcesoEnvioDomicilioDTO();
    private List<DiaEntrega> diasEntrega = new ArrayList<DiaEntrega>();
    private String fechaComoCadena = null;
    private GvtParametroEntregaBean gvtParametroEntregaBean = new GvtParametroEntregaBean();
    LineaTicket itemListDir = new LineaTicket();
    private final JPrincipal ventana_padre;

    /**
     * Creates new form JDatosEnvio
     *
     */
    public JDatosEnvio() {
        super();
        initComponents();
        this.registraEventoEnterBoton();
        inicializaValidacion();
        iniciaFoco();
        datosEnvio = null;
        jb_ok1.setVisible(false);
        this.ventana_padre = JPrincipal.getInstance();
        cargaModeloDireccion();
        cargaModeloItem();
    }

    public void cargaModeloDireccion() {
        modeloDir = new DefaultTableModel();
        modeloDir.addColumn("Cédula");
        modeloDir.addColumn("Nombre");
        modeloDir.addColumn("Apellido");
        modeloDir.addColumn("Direccion");
        modeloDir.addColumn("Ciudad");
        modeloDir.addColumn("Telefono");
        modeloDir.addColumn("Movil");
        this.tablaDireccion.setModel(modeloDir);
    }

    public void cargaModeloItem() {
        modeloItem = new DefaultTableModel();
        modeloItem.addColumn("Codigo");
        modeloItem.addColumn("Descripcion");
        modeloItem.addColumn("Cantidad");
        modeloItem.addColumn("Insta");
        modeloItem.addColumn("N");
        this.tablaListaItem.setModel(modeloItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        t_env_nombre = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_env_apellido = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_envio_apellido = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        t_env_telefono = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_env_movil = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        l_env_horario = new javax.swing.JComboBox();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        t_env_direccion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_env_direccion = new javax.swing.JLabel();
        lb_error = new javax.swing.JLabel();
        lb_env_direccion1 = new javax.swing.JLabel();
        t_env_ciudad = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaListaItem = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDireccion = new javax.swing.JTable();
        jb_agregar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_actualizar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jlcantidad = new javax.swing.JLabel();
        t_env_cantidad = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jb_actualizarItem = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        t_env_cedula = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel7 = new javax.swing.JLabel();
        o_env_instalacion = new javax.swing.JCheckBox();
        jTextArea1 = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        jLabel11 = new javax.swing.JLabel();
        lb_env_direccion4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        l_env_camion = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        t_env_mail = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_env_fechaEntrega = new javax.swing.JComboBox<>();
        jb_eliminar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_item = new javax.swing.JLabel();
        lb_env_sector = new javax.swing.JLabel();
        t_env_sector = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();

        setMinimumSize(new java.awt.Dimension(592, 523));
        setPreferredSize(new java.awt.Dimension(592, 523));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel6.setText("DATOS DE ENVÍO");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 191, 29));

        jLabel5.setDisplayedMnemonic('r');
        jLabel5.setLabelFor(t_env_nombre);
        jLabel5.setText("Observación :");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, 10));

        t_env_nombre.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_nombre.setNextFocusableComponent(t_env_apellido);
        t_env_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_nombreActionPerformed(evt);
            }
        });
        t_env_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_env_nombreKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_nombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_nombreKeyTyped(evt);
            }
        });
        add(t_env_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 240, -1));

        t_env_apellido.setFont(new java.awt.Font("Comic Sans MS", 0, 14));
        t_env_apellido.setNextFocusableComponent(t_env_sector);
        t_env_apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_apellidoActionPerformed(evt);
            }
        });
        t_env_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_apellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_apellidoKeyTyped(evt);
            }
        });
        add(t_env_apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 240, 30));

        lb_envio_apellido.setDisplayedMnemonic('p');
        lb_envio_apellido.setLabelFor(t_env_apellido);
        lb_envio_apellido.setText("Apellido :");
        add(lb_envio_apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        jLabel8.setDisplayedMnemonic('t');
        jLabel8.setLabelFor(t_env_telefono);
        jLabel8.setText("Fecha de Entrega :");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        t_env_telefono.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_telefono.setNextFocusableComponent(t_env_movil);
        t_env_telefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_telefonoActionPerformed(evt);
            }
        });
        t_env_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_telefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_telefonoKeyTyped(evt);
            }
        });
        add(t_env_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 110, -1));

        t_env_movil.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_movil.setNextFocusableComponent(l_env_horario);
        t_env_movil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_movilActionPerformed(evt);
            }
        });
        t_env_movil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_movilKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_movilKeyTyped(evt);
            }
        });
        add(t_env_movil, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 100, -1));

        jLabel9.setDisplayedMnemonic('m');
        jLabel9.setLabelFor(t_env_movil);
        jLabel9.setText("Móvil :");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, -1, -1));

        jLabel10.setDisplayedMnemonic('h');
        jLabel10.setLabelFor(l_env_horario);
        jLabel10.setText("Camión :");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, -1, -1));

        l_env_horario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "08:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-14:00", "14:00-16:00", "16:00-18:00" }));
        l_env_horario.setNextFocusableComponent(l_env_camion);
        l_env_horario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_env_horarioActionPerformed(evt);
            }
        });
        l_env_horario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                l_env_horarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_env_horarioKeyTyped(evt);
            }
        });
        add(l_env_horario, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 110, -1));

        jb_ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_ok1.setMnemonic('a');
        jb_ok1.setText("Aceptar");
        jb_ok1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jb_ok1.setNextFocusableComponent(jb_cancel1);
        jb_ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_ok1ActionPerformed(evt);
            }
        });
        add(jb_ok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 30, 40));

        jb_cancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        jb_cancel1.setText("Cancelar");
        jb_cancel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jb_cancel1.setNextFocusableComponent(tablaListaItem);
        jb_cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancel1ActionPerformed(evt);
            }
        });
        jb_cancel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jb_cancel1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_cancel1KeyTyped(evt);
            }
        });
        add(jb_cancel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 430, 120, 40));

        t_env_direccion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_direccion.setNextFocusableComponent(t_env_ciudad);
        t_env_direccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_direccionActionPerformed(evt);
            }
        });
        t_env_direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_direccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_direccionKeyTyped(evt);
            }
        });
        add(t_env_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 240, -1));

        lb_env_direccion.setDisplayedMnemonic('d');
        lb_env_direccion.setLabelFor(t_env_direccion);
        lb_env_direccion.setText("Dirección :");
        add(lb_env_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        lb_error.setForeground(new java.awt.Color(255, 0, 0));
        lb_error.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lb_errorKeyPressed(evt);
            }
        });
        add(lb_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 490, 19));

        lb_env_direccion1.setDisplayedMnemonic('C');
        lb_env_direccion1.setLabelFor(t_env_ciudad);
        lb_env_direccion1.setText("Mail :");
        add(lb_env_direccion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        t_env_ciudad.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_ciudad.setNextFocusableComponent(t_env_mail);
        t_env_ciudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_ciudadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_ciudadKeyTyped(evt);
            }
        });
        add(t_env_ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 240, -1));

        tablaListaItem.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaListaItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaListaItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaListaItemKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(tablaListaItem);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 400, 140));

        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyTyped(evt);
            }
        });

        tablaDireccion.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaDireccionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaDireccionKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDireccion);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 690, 140));

        jb_agregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_agregar.setMnemonic('a');
        jb_agregar.setText("Agregar F1");
        jb_agregar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jb_agregar.setNextFocusableComponent(jb_actualizar);
        jb_agregar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jb_agregarFocusLost(evt);
            }
        });
        jb_agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_agregarActionPerformed(evt);
            }
        });
        jb_agregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jb_agregarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jb_agregarKeyReleased(evt);
            }
        });
        add(jb_agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 140, 40));

        jb_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_actualizar.setMnemonic('a');
        jb_actualizar.setText("Actualizar");
        jb_actualizar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jb_actualizar.setNextFocusableComponent(jb_eliminar);
        jb_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_actualizarActionPerformed(evt);
            }
        });
        jb_actualizar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jb_actualizarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_actualizarKeyTyped(evt);
            }
        });
        add(jb_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 390, 120, 40));

        jlcantidad.setText("Cantidad:");
        add(jlcantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 60, -1));

        t_env_cantidad.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_cantidad.setNextFocusableComponent(o_env_instalacion);
        t_env_cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_cantidadActionPerformed(evt);
            }
        });
        t_env_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_cantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_cantidadKeyTyped(evt);
            }
        });
        add(t_env_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 60, -1));

        jb_actualizarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_actualizarItem.setMnemonic('a');
        jb_actualizarItem.setText("Actualizar");
        jb_actualizarItem.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 10)); // NOI18N
        jb_actualizarItem.setNextFocusableComponent(tablaListaItem);
        jb_actualizarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_actualizarItemActionPerformed(evt);
            }
        });
        jb_actualizarItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jb_actualizarItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_actualizarItemKeyTyped(evt);
            }
        });
        add(jb_actualizarItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, 110, 30));

        t_env_cedula.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_cedula.setNextFocusableComponent(t_env_nombre);
        t_env_cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_cedulaActionPerformed(evt);
            }
        });
        t_env_cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_env_cedulaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_cedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_cedulaKeyTyped(evt);
            }
        });
        add(t_env_cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 140, -1));

        jLabel7.setDisplayedMnemonic('r');
        jLabel7.setLabelFor(t_env_nombre);
        jLabel7.setText("A recoger por :");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        o_env_instalacion.setText("Instalación");
        o_env_instalacion.setLabel("Instalación");
        o_env_instalacion.setNextFocusableComponent(jb_actualizarItem);
        o_env_instalacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_env_instalacionActionPerformed(evt);
            }
        });
        o_env_instalacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                o_env_instalacionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                o_env_instalacionKeyTyped(evt);
            }
        });
        add(o_env_instalacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setNextFocusableComponent(jb_agregar);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        add(jTextArea1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 240, 70));

        jLabel11.setDisplayedMnemonic('r');
        jLabel11.setLabelFor(t_env_nombre);
        jLabel11.setText("Cédula :");
        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, 10));

        lb_env_direccion4.setDisplayedMnemonic('C');
        lb_env_direccion4.setLabelFor(t_env_ciudad);
        lb_env_direccion4.setText("Ciudad :");
        add(lb_env_direccion4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel12.setDisplayedMnemonic('h');
        jLabel12.setLabelFor(l_env_horario);
        jLabel12.setText("Horario de Envío :");
        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        l_env_camion.setNextFocusableComponent(l_env_fechaEntrega);
        l_env_camion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                l_env_camionItemStateChanged(evt);
            }
        });
        l_env_camion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_env_camionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                l_env_camionMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                l_env_camionMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                l_env_camionMouseReleased(evt);
            }
        });
        l_env_camion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_env_camionActionPerformed(evt);
            }
        });
        l_env_camion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_env_camionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                l_env_camionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_env_camionKeyTyped(evt);
            }
        });
        add(l_env_camion, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 260, 70, -1));

        jLabel13.setDisplayedMnemonic('t');
        jLabel13.setLabelFor(t_env_telefono);
        jLabel13.setText("Teléfono :");
        add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        t_env_mail.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_mail.setNextFocusableComponent(t_env_telefono);
        t_env_mail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_mailKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_mailKeyTyped(evt);
            }
        });
        add(t_env_mail, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 240, -1));

        l_env_fechaEntrega.setNextFocusableComponent(jTextArea1);
        l_env_fechaEntrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_env_fechaEntregaActionPerformed(evt);
            }
        });
        l_env_fechaEntrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_env_fechaEntregaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                l_env_fechaEntregaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_env_fechaEntregaKeyTyped(evt);
            }
        });
        add(l_env_fechaEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 240, 25));

        jb_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        jb_eliminar.setText("Eliminar");
        jb_eliminar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jb_eliminar.setNextFocusableComponent(jb_cancel1);
        jb_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_eliminarActionPerformed(evt);
            }
        });
        jb_eliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jb_eliminarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jb_eliminarKeyTyped(evt);
            }
        });
        add(jb_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 390, 120, 40));
        add(lb_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 400, 20));

        lb_env_sector.setDisplayedMnemonic('d');
        lb_env_sector.setLabelFor(t_env_direccion);
        lb_env_sector.setText("Sector :");
        add(lb_env_sector, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        t_env_sector.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_env_sector.setNextFocusableComponent(t_env_direccion);
        t_env_sector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_env_sectorActionPerformed(evt);
            }
        });
        t_env_sector.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_env_sectorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_env_sectorKeyTyped(evt);
            }
        });
        add(t_env_sector, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 240, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void t_env_apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_apellidoActionPerformed
}//GEN-LAST:event_t_env_apellidoActionPerformed

    private void t_env_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_telefonoActionPerformed
}//GEN-LAST:event_t_env_telefonoActionPerformed

    private void t_env_movilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_movilActionPerformed
}//GEN-LAST:event_t_env_movilActionPerformed

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        accionAceptar();
    }//GEN-LAST:event_jb_ok1ActionPerformed

    private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
        limpiarFormularioInicial();
        accionCancelar();
}//GEN-LAST:event_jb_cancel1ActionPerformed

    private void jb_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_agregarActionPerformed
        // TODO add your handling code here: 
        if (!t_env_cedula.getText().equals("") && !t_env_direccion.getText().equals("")) {
            if (!itemListActualizar.isEmpty()) {
                String[] datos = new String[9];
                datos[0] = t_env_cedula.getText();
                datos[1] = t_env_nombre.getText();
                datos[2] = t_env_apellido.getText();
                datos[3] = t_env_sector.getText();
                datos[4] = t_env_direccion.getText();
                datos[5] = t_env_ciudad.getText();
                datos[6] = t_env_telefono.getText();
                datos[7] = t_env_movil.getText();
                datos[8] = l_env_horario.getSelectedItem().toString();
                modeloDir.addRow(datos);
                cliente = new ClienteDTO();
                ItemDTO itemDir = new ItemDTO();
                List<ItemDTO> itmList = new ArrayList<ItemDTO>();
                envioDomicilio = new EnvioDomicilioDTO();
                itemList = new ArrayList<>();
                envioDomicilio.setCiudad(t_env_ciudad.getText());
                envioDomicilio.setSector(t_env_sector.getText());
                cliente.setIdentificacion(t_env_cedula.getText());
                cliente.setNombres(t_env_nombre.getText());
                cliente.setApellidos(t_env_apellido.getText());
                cliente.setDireccion(t_env_direccion.getText());
                cliente.setEmail(t_env_mail.getText());
                cliente.setNumeroTelefono(t_env_telefono.getText());
                cliente.setNumeroCelular(t_env_movil.getText());
                cliente.setProvincia(t_env_ciudad.getText());
                DatosCamionDTO datosCamion = new DatosCamionDTO();
                try {
                    datosCamion = new DatosCamionDTO(l_env_horario.getSelectedItem().toString(), l_env_camion.getSelectedItem().toString(), fechaComoCadena);
                } catch (Exception e) {
                    datosCamion.setFechaEntrega(new Date().toString());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                fechaComoCadena = null;
                if (l_env_fechaEntrega.getSelectedItem() != null) {
                    for (DiaEntrega d : diasEntrega) {
                        if (l_env_fechaEntrega.getSelectedItem().toString().equals(d.getTexto())) {
                            fechaComoCadena = sdf.format(d.getFecha());
                        }
                    }
                    datosCamion.setFechaEntrega(fechaComoCadena);

                }
                envioDomicilio.setObservacion(jTextArea1.getText());
                envioDomicilio.setDatosCamion(datosCamion);
                limpiarFormulario();
                Long cantItem = 0L;
                int i = 0;
                itemListDir = new LineaTicket();
                for (LineaTicket listEnvio : listItemDomicilio) {
//                itemList = new ArrayList<>();
                    for (ItemDTO itm : itemListActualizar) {
                        if (listEnvio.getArticulo().getCodart().equals(itm.getCodigoI()) && itm.getIdLinea().equals(listEnvio.getIdlinea())) {
                            itemListDir = new LineaTicket();
                            cantItem = Long.valueOf(listEnvio.getCantidad()) - itm.getCantidad();
                            if (cantItem > 0) {
                                itemDir = new ItemDTO();
                                i = i + 1;
                                itemDir.setIdLinea(i);
                                itemDir.setCodigoI(itm.getCodigoI());
                                itemDir.setDescripcion(itm.getDescripcion());
                                itemDir.setCantidad(cantItem);
                                itemDir.setInstalacion("N");
                                itmList.add(itemDir);
                                itemListDir.setIdlinea(i);
                                itemListDir.setArticulo(listEnvio.getArticulo());
                                itemListDir.setCantidad(cantItem.intValue());
                                itemListDir.setInstalacion("N");
                                itemList.add(itemListDir);
                            }
                        }
                    }
                }
                envioDomicilio.setDatosContacto(cliente);
                listaItemD.addAll(listItemDomicilio);
                for (ItemDTO items : itemListActualizar) {
                    if (items.getCantidad() == 0) {
                        itemListActualizar.remove(items);
                    }
                }
                envioDomicilio.setItemDtoLista(itemListActualizar);
                envioDomicilioList.add(envioDomicilio);
                listItemDomicilio = new ArrayList<LineaTicket>();
                listItemDomicilio.addAll(itemList);
                itemListActualizar = new ArrayList<>();
                itemListActualizar.addAll(itmList);
                itemsEntregaDomicilio(itemList);
                tablaDireccion.requestFocus();
                if (itemList.isEmpty()) {
                    accionAceptarTodo();
                }
            } else {
                accionAceptarTodo();
            }
            t_env_cedula.requestFocus();
        } else {
            lb_error.setText("Debe colocar todos los campos.");
            t_env_cedula.requestFocus();
        }

    }//GEN-LAST:event_jb_agregarActionPerformed

    private void t_env_direccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_direccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_direccionActionPerformed

    private void jb_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_actualizarActionPerformed
        // TODO add your handling code here:
        String[] datos = new String[13];
        datos[0] = t_env_cedula.getText();
        datos[1] = t_env_nombre.getText();
        datos[2] = t_env_apellido.getText();
        datos[3] = t_env_sector.getText();
        datos[4] = t_env_direccion.getText();
        datos[5] = t_env_ciudad.getText();
        datos[6] = t_env_mail.getText();
        datos[7] = t_env_telefono.getText();
        datos[8] = t_env_movil.getText();
        datos[9] = l_env_horario.getSelectedItem().toString();
        datos[10] = l_env_camion.getSelectedItem().toString();
        datos[11] = l_env_fechaEntrega.getSelectedItem().toString();
        datos[12] = jTextArea1.getText();
        for (int i = 0; i < tablaDireccion.getColumnCount(); i++) {
            modeloDir.setValueAt(datos[i], filas, i);
        }
    }//GEN-LAST:event_jb_actualizarActionPerformed

    private void l_env_horarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_env_horarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_horarioActionPerformed

    private void t_env_cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_cantidadActionPerformed

    private void tablaDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaDireccionKeyPressed
        // TODO add your handling code here:
//        t_env_nombre.requestFocus();
    }//GEN-LAST:event_tablaDireccionKeyPressed

    private void tablaDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaDireccionKeyReleased
        // TODO add your handling code here:
        cedula = null;
        direccion = null;
        int selection = tablaDireccion.getSelectedRow();
        filas = selection;
        cedula = tablaDireccion.getValueAt(selection, 0).toString();
        direccion = tablaDireccion.getValueAt(selection, 3).toString();
        if (itemList.isEmpty()) {
            for (EnvioDomicilioDTO dato : envioDomicilioList) {
                if (dato.getDatosContacto().getIdentificacion().equals(tablaDireccion.getValueAt(selection, 0).toString()) && dato.getDatosContacto().getDireccion().equals(tablaDireccion.getValueAt(selection, 3).toString())) {
                    t_env_cedula.setText(dato.getDatosContacto().getIdentificacion());
                    t_env_nombre.setText(dato.getDatosContacto().getNombres());
                    t_env_apellido.setText(dato.getDatosContacto().getApellidos());
                    t_env_direccion.setText(dato.getDatosContacto().getDireccion());
                    t_env_ciudad.setText(dato.getDatosContacto().getProvincia());
                    t_env_mail.setText(dato.getDatosContacto().getEmail());
                    t_env_telefono.setText(dato.getDatosContacto().getNumeroTelefono());
                    t_env_movil.setText(dato.getDatosContacto().getNumeroCelular());
                    l_env_horario.setSelectedItem(dato.getDatosCamion().getHorario());
                    l_env_camion.setSelectedItem(dato.getDatosCamion().getCamion());
                    l_env_fechaEntrega.setSelectedItem(dato.getDatosCamion().getFechaEntrega());
                    jTextArea1.setText(dato.getObservacion());
                }
            }
        }
        if (!t_env_cedula.getText().equals("")) {
            List<LineaTicket> list = new ArrayList<LineaTicket>();
            LineaTicket lineaTicket = new LineaTicket();
            ArticulosDao articulosDao = new ArticulosDao();
            Articulos articulos = new Articulos();
            for (EnvioDomicilioDTO listEnvio : envioDomicilioList) {
                if (listEnvio.getDatosContacto().getIdentificacion().equals(tablaDireccion.getValueAt(selection, 0).toString()) || listEnvio.getDatosContacto().getDireccion().equals(tablaDireccion.getValueAt(selection, 3).toString())) {
                    for (ItemDTO item : listEnvio.getItemDtoLista()) {
                        lineaTicket = new LineaTicket();
                        articulos = articulosDao.getArticuloCod(item.getCodigoI());
                        lineaTicket.setArticulo(articulos);
                        lineaTicket.setCantidad(item.getCantidad().intValue());
                        lineaTicket.setInstalacion(item.getInstalacion());
                        list.add(lineaTicket);
                    }
                }
            }
            itemsEntregaDomicilio(list);
        }
    }//GEN-LAST:event_tablaDireccionKeyReleased

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed
        // TODO add your handling code here:
//        t_env_nombre.requestFocus();
    }//GEN-LAST:event_jScrollPane1KeyPressed

    private void jScrollPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1KeyReleased

    private void jb_agregarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_agregarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_agregarKeyPressed

    private void jb_agregarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_agregarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_agregarKeyReleased

    private void tablaDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaDireccionKeyTyped
        // TODO add your handling code here:
        //Se debe colocar las teclas - | - | 
        t_env_nombre.requestFocus();
    }//GEN-LAST:event_tablaDireccionKeyTyped

    private void tablaListaItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaListaItemKeyReleased
        // TODO add your handling code here:
        int selection = tablaListaItem.getSelectedRow();
        t_env_cantidad.setText(tablaListaItem.getValueAt(selection, 2).toString());
        lb_item.setText(tablaListaItem.getValueAt(selection, 1).toString());
        if (tablaListaItem.getValueAt(selection, 3).toString().equals("S")) {
            o_env_instalacion.setSelected(true);
        } else {
            o_env_instalacion.setSelected(false);
        }
        filas = selection;
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_tablaListaItemKeyReleased

    private void jb_actualizarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_actualizarItemActionPerformed
        // TODO add your handling code here:
        String valorInstalacion = null;

        if (modeloItem.getValueAt(filas, 2) != null) {

            long cantidadAnterior = Long.parseLong(modeloItem.getValueAt(filas, 2).toString());
            long cantidadIngresar = 0l;
            if (t_env_cantidad.getText() != null) {
                cantidadIngresar = Long.parseLong(t_env_cantidad.getText());
            }
            if (cantidadIngresar > cantidadAnterior) {
                ventana_padre.crearError("No puede ingresar una cantidad mayor a la que está en la factura");
                return;
            }
        }
        String[] datos = new String[4];
        datos[2] = t_env_cantidad.getText();
        modeloItem.setValueAt(datos[2], filas, 2);
        if (o_env_instalacion.isSelected()) {
            valorInstalacion = "S";
        } else {
            valorInstalacion = "N";
        }
        datos[3] = valorInstalacion;
        modeloItem.setValueAt(datos[3], filas, 3);
        itemAct = new ArrayList<>();
        ItemDTO item = new ItemDTO();
        int selection = tablaListaItem.getSelectedRow();
        item.setCodigoI(tablaListaItem.getValueAt(selection, 0).toString());
        item.setDescripcion(tablaListaItem.getValueAt(selection, 1).toString());
        String cantidad = tablaListaItem.getValueAt(selection, 2).toString();
        item.setCantidad(Long.valueOf(cantidad));
        item.setInstalacion(tablaListaItem.getValueAt(selection, 3).toString());
        item.setIdLinea((Integer) tablaListaItem.getValueAt(selection, 4));
        for (ItemDTO i : itemListActualizar) {
            if (tablaListaItem.getValueAt(selection, 0).toString().equals(i.getCodigoI()) && tablaListaItem.getValueAt(selection, 4).equals(i.getIdLinea())) {
                itemAct.add(item);
            } else {
                itemAct.add(i);
            }
        }
        itemListActualizar = new ArrayList<>();
        itemListActualizar.addAll(itemAct);
        o_env_instalacion.setSelected(false);
        ItemDTO itemAct = new ItemDTO();
        List<ItemDTO> listItemAct = new ArrayList<>();
        EnvioDomicilioDTO envioAct = new EnvioDomicilioDTO();
        List<EnvioDomicilioDTO> listEnvioAct = new ArrayList<>();
        if (itemListActualizar.isEmpty()) {
            for (EnvioDomicilioDTO dato : envioDomicilioList) {
                envioAct = new EnvioDomicilioDTO();
                envioAct.setUidTicket(dato.getUidTicket());
                envioAct.setDatosContacto(dato.getDatosContacto());
                envioAct.setObservacion(dato.getObservacion());
                envioAct.setDatosCamion(dato.getDatosCamion());
                if (dato.getDatosContacto().getIdentificacion().equals(cedula) && dato.getDatosContacto().getDireccion().equals(direccion)) {
                    for (ItemDTO i : dato.getItemDtoLista()) {
                        itemAct = new ItemDTO();
                        itemAct.setIdLinea(i.getIdLinea());
                        itemAct.setCodigoI(i.getCodigoI());
                        itemAct.setEntregaDomicilio("S");
                        itemAct.setInstalacion(i.getInstalacion());
                        if (tablaListaItem.getValueAt(selection, 0).toString().equals(i.getCodigoI()) && tablaListaItem.getValueAt(selection, 4).equals(i.getIdLinea())) {
                            String cant = tablaListaItem.getValueAt(selection, 2).toString();
                            itemAct.setCantidad(Long.valueOf(cant));
                        } else {
                            itemAct.setCantidad(i.getCantidad());
                        }
                        listItemAct.add(itemAct);
                        envioAct.setItemDtoLista(listItemAct);
                    }
                } else {
                    envioAct.setItemDtoLista(dato.getItemDtoLista());
                }
                listEnvioAct.add(envioAct);
            }
            envioDomicilioList = new ArrayList<>();
            envioDomicilioList.addAll(listEnvioAct);
        }

    }//GEN-LAST:event_jb_actualizarItemActionPerformed

    private void tablaListaItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaListaItemKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        } else {
            t_env_cantidad.requestFocus();
        }
    }//GEN-LAST:event_tablaListaItemKeyTyped

    private void jb_agregarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jb_agregarFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_agregarFocusLost

    private void jScrollPane1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1KeyTyped

    private void t_env_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_cedulaActionPerformed

    private void t_env_cedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_cedulaKeyPressed
        try {
            // TODO add your handling code here:
            if (t_env_cedula.getText().length() >= 10) {
                Cliente cliente = ClientesServices.getInstance().consultaClienteIdenti(t_env_cedula.getText());
                t_env_cedula.setText(cliente.getIdentificacion());
                t_env_nombre.setText(cliente.getNombre());
                t_env_apellido.setText(cliente.getApellido());
                t_env_direccion.setText(cliente.getDireccion());
                t_env_ciudad.setText(cliente.getPoblacion());
                t_env_mail.setText(cliente.getEmail());
                t_env_telefono.setText(cliente.getTelefono1());
                t_env_movil.setText(cliente.getTelefonoMovil());
            }
        } catch (ClienteException ex) {
            java.util.logging.Logger.getLogger(JDatosEnvio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoResultException ex) {
            java.util.logging.Logger.getLogger(JDatosEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_t_env_cedulaKeyPressed

    private void t_env_cedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_cedulaKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_cedulaKeyReleased

    private void t_env_cedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_cedulaKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_cedulaKeyTyped

    private void o_env_instalacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_env_instalacionActionPerformed

    }//GEN-LAST:event_o_env_instalacionActionPerformed

    private void o_env_instalacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_env_instalacionKeyTyped
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_o_env_instalacionKeyTyped

    private void t_env_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_nombreKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_nombreKeyTyped

    private void t_env_nombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_nombreKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_nombreKeyReleased

    private void t_env_nombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_nombreKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_nombreKeyPressed

    private void t_env_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_nombreActionPerformed

    }//GEN-LAST:event_t_env_nombreActionPerformed

    private void l_env_camionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_env_camionActionPerformed
        // TODO add your handling code here:
//        l_env_camion.getSelectedItem().toString();
//        GvtParametroEntregaBean param = new GvtParametroEntregaBean();
//        try {
//            param = new GvtParametroEntregaBean();
//            List<GvtParametroEntregaBean> parametro = ServicioEnvioDomicilio.getParametrosEntrega(Sesion.getTienda().getCodalm(), Long.parseLong(l_env_camion.getSelectedItem().toString()));
//            for (GvtParametroEntregaBean p : parametro) {
//                param.setPenDomingo(p.getPenDomingo());
//                param.setPenLunes(p.getPenLunes());
//                param.setPenMartes(p.getPenMartes());
//                param.setPenMiercoles(p.getPenMiercoles());
//                param.setPenJueves(p.getPenJueves());
//                param.setPenViernes(p.getPenViernes());
//                param.setPenSabado(p.getPenSabado());
//            }
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(JDatosEnvio.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        getParametrosEntregaI
//        if (param != null && param.getPenDomingo() != null) {
//            Date d1 = new Date();
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, 30);
//            Date d2 = cal.getTime();
//            Iterator<Date> i = new DateIterator(d1, d2);
//            SimpleDateFormat formato = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
//            DiaEntrega diaEntrega;
//            while (i.hasNext()) {
//                Date date = i.next();
//                Calendar fecha = Calendar.getInstance();
//                fecha.setTime(date);
//                int dia = fecha.get(Calendar.DAY_OF_WEEK);
//                if (dia == 1 && param.getPenDomingo().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 2 && param.getPenLunes().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 3 && param.getPenMartes().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 4 && param.getPenMiercoles().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 5 && param.getPenJueves().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 6 && param.getPenViernes().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//                if (dia == 7 && param.getPenSabado().equals(1L)) {
//                    diaEntrega = new DiaEntrega();
//                    diaEntrega.setFecha(date);
//                    diaEntrega.setTexto(formato.format(date));
//                    diasEntrega.add(diaEntrega);
//                }
//            }
//        }
//        else {
//            art.appendJavaScript(ERPMensajes.ERPJGrowl.error("Debe configurar los días que el camión estará disponible", 6000));
//        }
    }//GEN-LAST:event_l_env_camionActionPerformed

    private void lb_errorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lb_errorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lb_errorKeyPressed

    private void jb_actualizarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_actualizarKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_actualizarKeyReleased

    private void jb_actualizarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_actualizarKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_actualizarKeyTyped

    private void jb_cancel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_cancel1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_cancel1KeyReleased

    private void jb_cancel1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_cancel1KeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_cancel1KeyTyped

    private void t_env_apellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_apellidoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_apellidoKeyReleased

    private void t_env_apellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_apellidoKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_apellidoKeyTyped

    private void t_env_direccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_direccionKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_direccionKeyReleased

    private void t_env_direccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_direccionKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_direccionKeyTyped

    private void t_env_ciudadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_ciudadKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_ciudadKeyReleased

    private void t_env_ciudadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_ciudadKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_ciudadKeyTyped

    private void t_env_mailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_mailKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_mailKeyReleased

    private void t_env_mailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_mailKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_mailKeyTyped

    private void t_env_telefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_telefonoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_telefonoKeyReleased

    private void t_env_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_telefonoKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_telefonoKeyTyped

    private void t_env_movilKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_movilKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_movilKeyReleased

    private void t_env_movilKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_movilKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_movilKeyTyped

    private void l_env_horarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_horarioKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_horarioKeyReleased

    private void l_env_horarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_horarioKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_horarioKeyTyped

    private void l_env_camionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_camionKeyReleased
        // TODO add your handling code here:
        l_env_camion.getSelectedItem().toString();
        try {
            llenarComboCamion(l_env_camion.getSelectedItem().toString());
        } catch (Exception ex) {
            ventana_padre.crearError(ex.getMessage());

        }
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_camionKeyReleased

    private void l_env_camionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_camionKeyTyped
        // TODO add your handling code here:
        l_env_camion.getSelectedItem().toString();
        try {
            llenarComboCamion(l_env_camion.getSelectedItem().toString());
        } catch (Exception ex) {
            ventana_padre.crearError(ex.getMessage());

        }
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_camionKeyTyped

    private void jTextArea1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jTextArea1KeyReleased

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void t_env_cantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_cantidadKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_cantidadKeyReleased

    private void t_env_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_cantidadKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_t_env_cantidadKeyTyped

    private void jb_actualizarItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_actualizarItemKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_actualizarItemKeyReleased

    private void jb_actualizarItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_actualizarItemKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_jb_actualizarItemKeyTyped

    private void l_env_fechaEntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_env_fechaEntregaActionPerformed

    }//GEN-LAST:event_l_env_fechaEntregaActionPerformed

    private void l_env_camionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_camionKeyPressed
        // TODO add your handling code here:
        l_env_camion.getSelectedItem().toString();
        try {
            llenarComboCamion(l_env_camion.getSelectedItem().toString());
        } catch (Exception ex) {
            ventana_padre.crearError(ex.getMessage());

        }
    }//GEN-LAST:event_l_env_camionKeyPressed

    private void l_env_fechaEntregaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_fechaEntregaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_fechaEntregaKeyPressed

    private void jb_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_eliminarActionPerformed
        // TODO add your handling code here:
        int selection = tablaDireccion.getSelectedRow();
        if (selection >= 0) {
            modeloDir.removeRow(selection);
        }
        for (EnvioDomicilioDTO envio : envioDomicilioList) {
            if (envio.getDatosContacto().getIdentificacion().equals(cedula) && envio.getDatosContacto().getDireccion().equals(direccion)) {
                envioDomicilioList.remove(envio);
                itemListActualizar.addAll(envio.getItemDtoLista());
                for (ItemDTO itm : envio.getItemDtoLista()) {
                    itemListDir = new LineaTicket();
                    ArticulosServices articulosServices = ArticulosServices.getInstance();
                    Articulos articulos = articulosServices.getArticuloCod(itm.getCodigoI());
                    itemListDir.setArticulo(articulos);
                    itemListDir.setCantidad(itm.getCantidad().intValue());
                    itemListDir.setInstalacion(itm.getEntregaDomicilio());
                    itemList.add(itemListDir);
                }
            }
        }
        envioDomicilioList.size();
        itemListActualizar.size();
        listItemDomicilio.size();
        itemsEntregaDomicilio(itemList);
        limpiarFormulario();
        t_env_cedula.requestFocus();
    }//GEN-LAST:event_jb_eliminarActionPerformed

    private void jb_eliminarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_eliminarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_eliminarKeyReleased

    private void jb_eliminarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_eliminarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_eliminarKeyTyped

    private void l_env_fechaEntregaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_fechaEntregaKeyReleased
        // TODO add your handling code here: 
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_fechaEntregaKeyReleased

    private void l_env_fechaEntregaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_env_fechaEntregaKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_l_env_fechaEntregaKeyTyped

    private void o_env_instalacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o_env_instalacionKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            jb_agregar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            tablaDireccion.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_F3) {
            tablaListaItem.requestFocus();
        }
    }//GEN-LAST:event_o_env_instalacionKeyReleased

    private void l_env_camionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_env_camionMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_camionMouseReleased

    private void l_env_camionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_env_camionMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_camionMouseClicked

    private void l_env_camionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_env_camionMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_camionMouseEntered

    private void l_env_camionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_env_camionMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_l_env_camionMousePressed

    private void l_env_camionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_l_env_camionItemStateChanged
        // TODO add your handling code here:
        try {
            if (l_env_camion.getSelectedItem() != null) {
                llenarComboCamion(l_env_camion.getSelectedItem().toString());
            }
        } catch (Exception ex) {
            ventana_padre.crearError(ex.getMessage());

        }
    }//GEN-LAST:event_l_env_camionItemStateChanged

    private void t_env_sectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_env_sectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_sectorActionPerformed

    private void t_env_sectorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_sectorKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_sectorKeyReleased

    private void t_env_sectorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_env_sectorKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_t_env_sectorKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm jTextArea1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_actualizar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_actualizarItem;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_agregar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_cancel1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_eliminar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_ok1;
    private javax.swing.JLabel jlcantidad;
    private javax.swing.JComboBox<Long> l_env_camion;
    private javax.swing.JComboBox<String> l_env_fechaEntrega;
    private javax.swing.JComboBox l_env_horario;
    private javax.swing.JLabel lb_env_direccion;
    private javax.swing.JLabel lb_env_direccion1;
    private javax.swing.JLabel lb_env_direccion4;
    private javax.swing.JLabel lb_env_sector;
    private javax.swing.JLabel lb_envio_apellido;
    private javax.swing.JLabel lb_error;
    private javax.swing.JLabel lb_item;
    private javax.swing.JCheckBox o_env_instalacion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_apellido;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_cantidad;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_cedula;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_ciudad;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_direccion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_mail;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_movil;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_nombre;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_sector;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_env_telefono;
    private javax.swing.JTable tablaDireccion;
    private javax.swing.JTable tablaListaItem;
    // End of variables declaration//GEN-END:variables


    /*
    DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
    datosAdicionales.setApellidosEnvio(t_env_apellido.getText());
    datosAdicionales.setMovilEnvio(t_env_movil.getText());
    datosAdicionales.setNombreEnvio(t_env_nombre.getText());
    datosAdicionales.setTelefonoEnvio(t_env_telefono.getText());
    datosAdicionales.setHorarioEnvio(l_env_horario.getSelectedItem().toString());
    
    t_env_apellido.setText(Sesion.getTicket().getCliente().getApellido());
    t_env_movil.setText(Sesion.getTicket().getCliente().getTelefonoMovil());
    t_env_nombre.setText(Sesion.getTicket().getCliente().getNombre());
    t_env_telefono.setText(Sesion.getTicket().getCliente().getTelefonoParticular());
    l_env_horario.setSelectedIndex(0);
     */
    @Override
    public void accionAceptar() {
        lb_error.setText("");
        if (!t_env_telefono.getText().equals("") || !t_env_movil.getText().equals("")) {
            try {
                validarFormulario();
                aceptado = true;
                datosEnvio = new DatosDeEnvio(t_env_apellido.getText(), t_env_movil.getText(), t_env_nombre.getText(), t_env_telefono.getText(), l_env_horario.getSelectedItem().toString(), t_env_direccion.getText(), t_env_ciudad.getText());
                //Hay que hacer esta comprobación para el caso de que vengamos de plan novio
                if (Sesion.getTicket() != null) {
                    Sesion.getTicket().setDatosEnvio(datosEnvio);
                }
                cerrarVentana();
            } catch (ValidationFormException e) {
                log.debug(e.getMessage());
            } catch (Exception e) {
                log.error("No se han podido establecer los datos de envío", e);
            }
        } else {
            lb_error.setText("Debe tener al menos un número de teléfono.");
            t_env_telefono.requestFocus();
        }
    }

    public void accionAceptarTodo() {
        lb_error.setText("");
        try {
            aceptado = true;
            datosEnvio = new DatosDeEnvio(t_env_apellido.getText(), t_env_movil.getText(), t_env_nombre.getText(), t_env_telefono.getText(), l_env_horario.getSelectedItem().toString(), t_env_direccion.getText(), t_env_ciudad.getText());
            List<EnvioDomicilioDTO> listEnvioDomicilioDTO = new ArrayList<>();
            for (EnvioDomicilioDTO envio : envioDomicilioList) {
                EnvioDomicilioDTO envioDomicilioDTO = new EnvioDomicilioDTO(Sesion.getTicket().getUid_ticket(), Sesion.getTicket().getTienda(), Sesion.getTicket().getCajero().getUsuario(), envio.getObservacion(), envio.getDatosContacto(), envio.getDatosCamion(), envio.getItemDtoLista(), envio.getCiudad(), envio.getSector());
                envio.setUidTicket(Sesion.getTicket().getUid_ticket());
                listEnvioDomicilioDTO.add(envioDomicilioDTO);
            }
            //Hay que hacer esta comprobación para el caso de que vengamos de plan novio
            if (Sesion.getTicket() != null) {
                String factura = Sesion.getTicket().getTienda().concat(Sesion.getTicket().getCodcaja()).concat(llenarCeros((int) Sesion.getTicket().getId_ticket(), 9));
                ProcesoEnvioDomicilioDTO procesoEnvioDomicilioDTO = new ProcesoEnvioDomicilioDTO();
                procesoEnvioDomicilioDTO.setFactura(factura);
                procesoEnvioDomicilioDTO.setLugSri(Sesion.getTicket().getTienda());
                procesoEnvioDomicilioDTO.setListEnvioDomicilioDTO(listEnvioDomicilioDTO);
                Sesion.getTicket().setProcesoEnvioDomicilioDTO(procesoEnvioDomicilioDTO);
            }
            cerrarVentana();
        } catch (Exception e) {
            log.error("No se han podido establecer los datos de envío", e);
        }
    }

    @Override
    public void limpiarFormulario() {
        t_env_cedula.setText("");
        t_env_nombre.setText("");
        t_env_apellido.setText("");
        t_env_sector.setText("");
        t_env_direccion.setText("");
        t_env_ciudad.setText("");
        t_env_mail.setText("");
        t_env_telefono.setText("");
        t_env_movil.setText("");
        l_env_horario.setSelectedIndex(0);
//        l_env_camion.setSelectedIndex(0);
//        l_env_fechaEntrega.setSelectedIndex(0);
        jTextArea1.setText("");
        t_env_cantidad.setText("");
        o_env_instalacion.setSelected(false);
    }

    public void limpiarFormularioInicial() {
        t_env_cedula.setText("");
        t_env_nombre.setText("");
        t_env_apellido.setText("");
        t_env_sector.setText("");
        t_env_direccion.setText("");
        t_env_ciudad.setText("");
        t_env_mail.setText("");
        t_env_telefono.setText("");
        t_env_movil.setText("");
        l_env_horario.setSelectedIndex(0);
        jTextArea1.setText("");
        t_env_cantidad.setText("");
        o_env_instalacion.setSelected(false);
        listItemDomicilio.clear();
        itemList.clear();
        itemListActualizar.clear();
        envioDomicilioList.clear();
        cargaModeloDireccion();
        cargaModeloItem();
    }

    public void inicializaFormulario() {
        try {
            jb_eliminar.setVisible(true);
            t_env_cedula.requestFocus();
            datosEnvio = null;
            aceptado = false;
            if (!envioDomicilioList.isEmpty()) {
                if (!envioDomicilioList.get(0).getUidTicket().equals(Sesion.getTicket().getUid_ticket())) {
                    limpiarFormularioInicial();
                }
            }
            if (Sesion.getTicket().getCliente().getNombre().equals("CLIENTE GENERICO")) {
                t_env_cedula.setText("");
                t_env_nombre.setText("");
                t_env_apellido.setText("");
                t_env_sector.setText("");
                t_env_direccion.setText("");
                t_env_ciudad.setText("");
                t_env_mail.setText("");
                t_env_telefono.setText("");
                t_env_movil.setText("");
                l_env_horario.setSelectedIndex(0);
                jTextArea1.setText("");
                t_env_cantidad.setText("");
                o_env_instalacion.setSelected(false);
            } else {
                t_env_direccion.setText(Sesion.getTicket().getCliente().getDireccion());
                t_env_apellido.setText(Sesion.getTicket().getCliente().getApellido());
                t_env_movil.setText(Sesion.getTicket().getCliente().getTelefonoMovil());
                t_env_nombre.setText(Sesion.getTicket().getCliente().getNombre());
                t_env_cedula.setText(Sesion.getTicket().getCliente().getCodcli());
                t_env_ciudad.setText(Sesion.getTicket().getCliente().getPoblacion());
                if (Sesion.getTicket().getCliente().getTelefonoParticular() != null && Sesion.getTicket().getCliente().getTelefonoParticular().length() == 9) {
                    t_env_telefono.setText(Sesion.getTicket().getCliente().getTelefonoParticular());
                } else {
                    t_env_telefono.setText("");
                }
                t_env_cantidad.setText("");
                o_env_instalacion.setSelected(false);
                t_env_cedula.requestFocus();
            }
            l_env_horario.setSelectedIndex(0);
            try {
                gvtParametroEntregaBean.llenar_combo(l_env_camion);
                llenarComboCamion(l_env_camion.getSelectedItem().toString());
            } catch (Exception e) {
                l_env_camion.setSelectedItem("");
            }
            listItemDomicilio = new ArrayList<>();
            List<ItemDTO> itemActualizar = new ArrayList<>();
            ItemDTO itemI = new ItemDTO();
            if (Sesion.getTicket() != null) {
                listItem = Sesion.getTicket().getLineas().getLineas();
                lineaDomicilio = new LineaTicket();
                listItemDomicilio = new ArrayList<>();
                if (envioDomicilioList.isEmpty()) {
                    int i = 0;
                    for (LineaTicket listItemD : listItem) {
                        if (listItemD.getDatosAdicionales() != null) {
                            if (listItemD.getDatosAdicionales().getEnvioDomicilio()) {
                                i = i + 1;
                                lineaDomicilio = new LineaTicket();
                                lineaDomicilio.setIdlinea(i);
                                lineaDomicilio.setArticulo(listItemD.getArticulo());
                                lineaDomicilio.setCantidad(listItemD.getCantidad());
                                lineaDomicilio.setInstalacion("N");
                                listItemDomicilio.add(lineaDomicilio);
                                itemI = new ItemDTO();
                                itemI.setIdLinea(i);
                                itemI.setCodigoI(listItemD.getArticulo().getCodart());
                                itemI.setDescripcion(listItemD.getArticulo().getDesart());
                                itemI.setCantidad(listItemD.getCantidad().longValue());
                                itemI.setInstalacion("N");
                                itemActualizar.add(itemI);
                            }
                        }
                    }
                } else {
                    t_env_direccion.setText(envioDomicilioList.get(0).getDatosContacto().getDireccion());
                    t_env_apellido.setText(envioDomicilioList.get(0).getDatosContacto().getApellidos());
                    t_env_mail.setText(envioDomicilioList.get(0).getDatosContacto().getEmail());
                    if (envioDomicilioList.get(0).getDatosContacto() != null) {
                        t_env_movil.setText(envioDomicilioList.get(0).getDatosContacto().getNumeroTelefono());
                    }
                    t_env_nombre.setText(envioDomicilioList.get(0).getDatosContacto().getNombres());
                    t_env_cedula.setText(envioDomicilioList.get(0).getDatosContacto().getIdentificacion());
                    t_env_ciudad.setText(envioDomicilioList.get(0).getDatosContacto().getProvincia());
                    if (envioDomicilioList.get(0).getDatosContacto() != null) {
                        t_env_telefono.setText(envioDomicilioList.get(0).getDatosContacto().getNumeroTelefono());
                    }
                    t_env_cedula.requestFocus();

                }
            }
            t_env_cedula.requestFocus();
            if (itemList.isEmpty()) {
                itemsEntregaDomicilio(listItemDomicilio);
                itemListActualizar.clear();
                itemListActualizar.addAll(itemActualizar);
            } else {
                itemsEntregaDomicilio(itemList);
                itemListActualizar.addAll(itemActualizar);
            }
        } catch (Exception ex) {
            ventana_padre.crearError(ex.getMessage());
        }

    }

    public void inicializaFormulario(Cliente cliente) {
        iniciaFoco();
        aceptado = false;
        if (cliente.getNombre().equals("CLIENTE GENERICO")) {
            t_env_apellido.setText("");
            t_env_movil.setText("");
            t_env_sector.setText("");
            t_env_direccion.setText("");
            t_env_nombre.setText("");
            t_env_telefono.setText("");
            t_env_ciudad.setText("");
        } else {
            t_env_direccion.setText(cliente.getDireccion());
            t_env_apellido.setText(cliente.getApellido());
            t_env_movil.setText(cliente.getTelefonoMovil());
            t_env_nombre.setText(cliente.getNombre());
            t_env_ciudad.setText(cliente.getPoblacion());
            if (cliente.getTelefonoParticular() != null && cliente.getTelefonoParticular().length() == 9) {
                t_env_telefono.setText(cliente.getTelefonoParticular());
            } else {
                t_env_telefono.setText("");
            }

        }
        l_env_horario.setSelectedIndex(0);
        t_env_nombre.requestFocus();
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    private void inicializaValidacion() {

        // APELLIDOS: CAMPO OBLIGATORIO: 1 a 100
        t_env_apellido.setFormatearTexto(true);
        t_env_apellido.addValidador(new ValidadorObligatoriedad(), this);
        t_env_apellido.addValidador(new ValidadorTexto(100), this);
        t_env_apellido.setFormatearTexto(true);

        // SECTOR: CAMPO OBLIGATORIO: 1 a 100
        t_env_sector.setFormatearTexto(true);
        t_env_sector.addValidador(new ValidadorObligatoriedad(), this);
        t_env_sector.addValidador(new ValidadorTexto(100), this);
        t_env_sector.setFormatearTexto(true);

        // NOMBRES: CAMPO OBLIGATORIO: 1 a 100
        t_env_nombre.setFormatearTexto(true);
        t_env_nombre.addValidador(new ValidadorObligatoriedad(), this);
        t_env_nombre.addValidador(new ValidadorTexto(100), this);
        t_env_nombre.setFormatearTexto(true);

        // CEDULA: CAMPO OBLIGATORIO: 1 a 100
        t_env_cedula.setFormatearTexto(true);
        t_env_cedula.addValidador(new ValidadorObligatoriedad(), this);
        t_env_cedula.addValidador(new ValidadorTexto(100), this);
        t_env_cedula.setFormatearTexto(true);

        // DIRECCION: CAMPO OBLIGATORIO: 1 a 400 
        t_env_direccion.setFormatearTexto(true);
        t_env_direccion.addValidador(new ValidadorObligatoriedad(), this);
        t_env_direccion.addValidador(new ValidadorTexto(new Integer(0), new Integer(400)), this);
        t_env_direccion.setFormatearTexto(true);

        // CIUDAD: CAMPO OBLIGATORIO: 1 a 100
        t_env_ciudad.setFormatearTexto(true);
        t_env_ciudad.addValidador(new ValidadorObligatoriedad(), this);
        t_env_ciudad.addValidador(new ValidadorTexto(100), this);
        t_env_ciudad.setFormatearTexto(true);

        // TELEFONO PARTICULAR
        t_env_telefono.addValidador(new ValidadorTexto(new Integer(9), new Integer(9)), this);
        t_env_telefono.addValidador(new ValidadorEntero(), this);

        //  CELULAR
        t_env_movil.addValidador(new ValidadorCelularEcuador(), this);

        // MAIL
        t_env_mail.addValidador(new ValidadorEmail(), this);
        t_env_mail.addValidador(new ValidadorTexto(400), this);

//        // validador de fecha de entrega
//        l_env_fechaEntrega.addValidador(new ValidadorObligatoriedad(), this);
//        l_env_fechaEntrega.addValidador(new ValidadorFecha(), thisinic
        //CANTIDAD
        t_env_cantidad.addValidador(new ValidadorEntero(0, 9999), this);

        formulario = new LinkedList<IValidableForm>();
        formulario.add(t_env_apellido);
        formulario.add(t_env_sector);
        formulario.add((t_env_direccion));
        formulario.add(t_env_movil);
        formulario.add(t_env_nombre);
        formulario.add(t_env_cedula);
        formulario.add(t_env_telefono);
        formulario.add(t_env_ciudad);
        formulario.add(t_env_mail);
        formulario.add(t_env_cantidad);
    }

    private void validarFormulario() throws ValidationFormException {
        for (IValidableForm elem : formulario) {
            //elem.requestFocus();
            elem.validar();
        }
    }

    @Override
    public void addError(ValidationFormException e) {
        lb_error.setText(e.getMessage());
    }

    @Override
    public void clearError() {

        lb_error.setText("");
        lb_error.setIcon(null);
    }

    @Override
    public void accionLeerTarjetaVD() {
    }

    private void iniciaFoco() {
        t_env_cedula.requestFocus();
    }

    public DatosDeEnvio getDatosEnvio() {
        return datosEnvio;
    }

    private void itemsEntregaDomicilio(List<LineaTicket> listItemDomicilio) {
        cargaModeloItem();
        Object[] datos = new Object[5];
        for (int i = 0; i < listItemDomicilio.size(); i++) {
            datos[0] = listItemDomicilio.get(i).getArticulo().getCodart() + "";
            datos[1] = listItemDomicilio.get(i).getArticulo().getDesart() + "";
            datos[2] = listItemDomicilio.get(i).getCantidad() + "";
            if (listItemDomicilio.get(i).getInstalacion() != null) {
                datos[3] = listItemDomicilio.get(i).getInstalacion() + "";
            } else {
                datos[3] = "N";
            }

            datos[4] = i + 1;
            modeloItem.addRow(datos);
        }
        tablaListaItem.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaListaItem.getColumnModel().getColumn(1).setPreferredWidth(550);
        tablaListaItem.getColumnModel().getColumn(2).setPreferredWidth(50);
        tablaListaItem.getColumnModel().getColumn(3).setPreferredWidth(50);
        tablaListaItem.getColumnModel().getColumn(4).setPreferredWidth(5);
        tablaListaItem.setModel(modeloItem);
    }

//    private void listaDirecciones(List<ClienteDTO> cliente) {
//        cargaModeloDireccion();
//        Object[] datos = new Object[8];
//        for (int i = 0; i < cliente.size(); i++) {
//            datos[0] = cliente.get(i).getIdentificacion();
//            datos[1] = cliente.get(i).getNombres();
//            datos[2] = cliente.get(i).getApellidos();
//            datos[3] = cliente.get(i).getDireccion();
//            datos[4] = cliente.get(i).getProvincia();
//            datos[5] = cliente.get(i).getNumeroTelefono();
//            datos[6] = cliente.get(i).getNumeroCelular();
//            datos[7] = cliente.get(i).getHorario();
//            modeloDir.addRow(datos);
//        }
//    }
    private String llenarCeros(int num, int len) {
        String ns = String.valueOf(num);

        if (ns.length() > len) {
            ns = ns.substring(0, len);
        }

        while (ns.length() < len) {
            ns = "0" + ns;
        }

        return ns;
    }

    /**
     *
     * @param combo_fechaEntrega
     */
    public void llenar_comboEntrega(JComboBox<String> combo_fechaEntrega) {

        combo_fechaEntrega.removeAllItems();
        for (DiaEntrega d : diasEntrega) {
            combo_fechaEntrega.addItem(d.getTexto());
        }

    }

    /**
     *
     * @param camion
     * @throws java.lang.Exception
     */
    public void llenarComboCamion(String camion) throws Exception {
        GvtParametroEntregaBean param = new GvtParametroEntregaBean();
        diasEntrega = new ArrayList<>();
        try {
            param = new GvtParametroEntregaBean();
            List<GvtParametroEntregaBean> parametro = ServicioEnvioDomicilio.getParametrosEntrega(Sesion.getTienda().getCodalm(), Long.parseLong(camion));
            for (GvtParametroEntregaBean p : parametro) {
                param.setPenDomingo(p.getPenDomingo());
                param.setPenLunes(p.getPenLunes());
                param.setPenMartes(p.getPenMartes());
                param.setPenMiercoles(p.getPenMiercoles());
                param.setPenJueves(p.getPenJueves());
                param.setPenViernes(p.getPenViernes());
                param.setPenSabado(p.getPenSabado());
                param.setPenId(p.getPenId());
            }
        } catch (Exception ex) {
            throw new Exception("Error al consultar los par\u00E1metros de entrega ");
        }
//        getParametrosEntregaI
        if (param.getPenDomingo() != null) {
            Date d1 = new Date();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 30);
            Date d2 = cal.getTime();
            Iterator<Date> i = new DateIterator(d1, d2);
            SimpleDateFormat formato = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
            DiaEntrega diaEntrega;
            while (i.hasNext()) {
                Date date = i.next();
                int dia = Fechas.getDateByCalendar(date, Calendar.DAY_OF_WEEK);
                if (dia == 1 && param.getPenDomingo().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 2 && param.getPenLunes().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 3 && param.getPenMartes().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 4 && param.getPenMiercoles().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 5 && param.getPenJueves().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 6 && param.getPenViernes().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
                if (dia == 7 && param.getPenSabado().equals(1L)) {
                    if (ServicioEnvioDomicilio.validarCuposDisponiblesPorFechaCamion(param.getPenId(), Fechas.dateToString(date, Fechas.DATE_DATA_FORMAT))) {
                        diaEntrega = new DiaEntrega(formato.format(date), date);
                        diasEntrega.add(diaEntrega);
                    }
                    continue;
                }
            }
            llenar_comboEntrega(l_env_fechaEntrega);
        }
    }

}
