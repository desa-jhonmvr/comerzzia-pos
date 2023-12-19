/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JIdentCliente.java
 *
 * Created on 28-jun-2011, 11:23:44
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.DatosCiudadanoDTO;
import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.db.Ciudad;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.OperadorMovil;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.modelos.CiudadesKeySelectionManager;
import com.comerzzia.jpos.gui.modelos.OperadoresKeySelectionManager;
import com.comerzzia.jpos.gui.modelos.VendedorKeySelectionManager;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.InputFormVerifier;
import com.comerzzia.jpos.gui.validation.ValidadorCelularEcuador;
import com.comerzzia.jpos.gui.validation.ValidadorEmail;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.gui.validation.ValidadorFecha;
import com.comerzzia.jpos.gui.validation.ValidadorFechaBebe;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidadorTextoPuro;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TipoClienteBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClienteInactiveException;
import com.comerzzia.jpos.servicios.clientes.tiposClientes.TiposClientes;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import com.comerzzia.jpos.servicios.credito.politica.PoliticaProteccionDatosServices;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.util.ClienteRest;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.ValidadorCedula;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public class JIdentCliente extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError/*Esta implementacion es para el foco borde color. , FocusListener*/ {

    private static final long serialVersionUID = 1L;
    JPrincipal ventana_padre = null;
    ClientesServices clienteService = new ClientesServices();
    Cliente clienteConsultado; //cliente consultado sobre el que se realizan la accion
    String celularAnterior;
    String tipoOperacionConfirmarCliente; // UPDATE o CREATE
    private boolean editandoFormulario;
    private static final Byte CLIENTESELECCIONADO = 0;
    private static final Byte CLIENTEFINAL = 1;
    private static final Byte OTROCLIENTE = 2;
    private List<IValidableForm> formularioCliente;
    private static final Logger log = Logger.getMLogger(JIdentCliente.class);
    private boolean bloquearAcciones = false;

    public JIdentCliente() {
        super();
        initComponents();
        bloquearAcciones = false;
    }

    public JIdentCliente(JPrincipal ventana_padre) {
        super();
        // CARGA DE LA IMAGEN DE FONDO

        try {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_cliente.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
            log.error("Intentando obtener imagen de tema in pantalla Clientes ");
        }
        this.ventana_padre = ventana_padre;
        // Modificación para solo guardar el cliente cuando ha sido editado        

        initComponents();

        if (Sesion.isSukasa()) {
            deshabilitaElementosSukasa();
        }

        //cggm 19092018 Quita la imagen de la empresa. Se remplaza por nuevos fondos creado por Marketing
//        Imagenes.cambiarImagenPublicidad(jLabel30);
//        jLabel30.setVisible(false);
        URL myurl;

        myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
        ImageIcon icon = new ImageIcon(myurl);

        jf_info_adicional.setIconImage(icon.getImage());
        creaFormularioCliente();
        // Iniciamos la ventana de facturación

        // Inicializamos la escucha de eventos de teclado
        this.t_documento.addKeyListener(this);
        this.t_numeroAfiliacion.addKeyListener(this);
        //Iluminar componentes
        inicializaValidacion();
        // Evento ENTER PARA BOTONES
        addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterBotones", listenerEnter);
        t_cliente_nombres.setFormatearTexto(true);
        t_cliente_apellidos.setFormatearTexto(true);
        t_cliente_direccion.setFormatearTexto(true);
        lb_error_ml.setBlinking(true);
        lb_error_ml.startBlinking(lb_error_ml);
        registraEventoBuscar();
        bloquearAcciones = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        operadoresMoviles = new com.comerzzia.jpos.servicios.general.operadoresmoviles.OperadoresMoviles();
        v_confirmaNuevoCliente = new javax.swing.JOptionPane();
        ciudades = new com.comerzzia.jpos.servicios.general.ciudades.Ciudades();
        ciudadesListRenderer = new com.comerzzia.jpos.gui.modelos.CiudadesListRenderer();
        sesion1 = new com.comerzzia.jpos.servicios.login.Sesion();
        jLabel14 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        t_cliente_prefijoTelefonoTrabajo = new javax.swing.JComboBox();
        t_cliente_tel_trabajo = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_fechaNacimiento = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel23 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        operadoresListRenderer = new com.comerzzia.jpos.gui.modelos.OperadoresListRenderer();
        t_cliente_estadoCivil = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        t_cliente_genero = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_cliente_informacion = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_cliente_observaciones = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        jf_info_adicional = new javax.swing.JDialog();
        jLabel25 = new javax.swing.JLabel();
        jPanel6 = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);
        KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        jPanel6.registraEventoEnterBoton();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_cliente_info3 = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_cliente_info4 = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        b_aceptar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        l_cliente_recibir_correos = new javax.swing.JCheckBox();
        l_cliente_recibir_llamadas = new javax.swing.JCheckBox();
        p_publicidad = new javax.swing.JPanel();
        p_ident_cliente = new javax.swing.JPanel();
        lb_numeroAfiliacion = new javax.swing.JLabel();
        t_numeroAfiliacion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel3 = new javax.swing.JLabel();
        l_tipoDocumento = new javax.swing.JComboBox();
        t_documento = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_vendedor = new javax.swing.JLabel();
        l_vendedor = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        p_datos_cliente = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbl_nombres = new javax.swing.JLabel();
        lb_semanasEmbarazo2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lb_cliente_preafiliado1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_cliente_apellidos = new javax.swing.JLabel();
        t_cliente_nombres = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_apellidos = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_direccion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_email = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel15 = new javax.swing.JLabel();
        t_cliente_tel_particular = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_prefijoTelefonoParticular = new javax.swing.JComboBox();
        t_cliente_operadorCelular = new javax.swing.JComboBox();
        lb_fechaNacimientoUltimoBebe1 = new javax.swing.JLabel();
        lb_fechaNacimientoUltimoBebe2 = new javax.swing.JLabel();
        lb_semanasEmbarazo1 = new javax.swing.JLabel();
        t_cliente_celular = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_Ciudad = new javax.swing.JComboBox();
        t_cliente_fechaNacimientoBebe = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cliente_semanasEmbarazo = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel1 = new javax.swing.JLabel();
        c_afiliarse = new javax.swing.JCheckBox();
        lb_cliente_tipo_socio = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lb_tarjeta = new javax.swing.JLabel();
        jLabel_puntos = new javax.swing.JLabel();
        lb_tipo_cliente = new javax.swing.JLabel();
        menu_navegacion = new javax.swing.JPanel();
        b_m_editar_cliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_facturar_otro_cliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_consumidor_final = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_informacion_extra = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_confirmar_cliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_menu_principal = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_m_seleccionar_cliente = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        p_avisos = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lb_error_icon = new javax.swing.JLabel();
        lb_error_ml = new com.comerzzia.jpos.gui.components.JMultilineLabel();

        operadoresMoviles.setListaOperadorMovil(Sesion.getDatosConfiguracion().getOperadoresMoviles().getListaOperadorMovil());

        v_confirmaNuevoCliente.setOptionType(2);

        ciudades.setListaCiudades(Sesion.getDatosConfiguracion().getCiudades().getListaCiudades());

        jLabel14.setText("Teléfono:");

        jLabel33.setDisplayedMnemonic('j');
        jLabel33.setLabelFor(t_cliente_prefijoTelefonoTrabajo);
        jLabel33.setText("trabajo :");

        t_cliente_prefijoTelefonoTrabajo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        t_cliente_prefijoTelefonoTrabajo.setEnabled(false);
        t_cliente_prefijoTelefonoTrabajo.setRenderer(ciudadesListRenderer);

        t_cliente_tel_trabajo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_tel_trabajo.setEnabled(false);
        t_cliente_tel_trabajo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        t_cliente_fechaNacimiento.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_fechaNacimiento.setEnabled(false);

        jLabel23.setDisplayedMnemonic('h');
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setLabelFor(t_cliente_fechaNacimiento);
        jLabel23.setText("Fecha");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("nacimiento:");

        t_cliente_estadoCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SOLTERO", "CASADO", "VIUDO", "SEPARADO", "UNION/LIBRE", "\t", " " }));
        t_cliente_estadoCivil.setSelectedIndex(5);
        t_cliente_estadoCivil.setEnabled(false);
        t_cliente_estadoCivil.setRenderer(ciudadesListRenderer);

        jLabel16.setDisplayedMnemonic('s');
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setLabelFor(t_cliente_estadoCivil);
        jLabel16.setText("Estado civil:");

        jLabel11.setDisplayedMnemonic('g');
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setLabelFor(t_cliente_genero);
        jLabel11.setText("Género:");

        t_cliente_genero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "M", "F" }));
        t_cliente_genero.setEnabled(false);
        t_cliente_genero.setRenderer(ciudadesListRenderer);

        t_cliente_informacion.setColumns(20);
        t_cliente_informacion.setRows(5);
        t_cliente_informacion.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_informacion.setEnabled(false);
        t_cliente_informacion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_informacion.setNextFocusableComponent(b_m_seleccionar_cliente);
        jScrollPane2.setViewportView(t_cliente_informacion);

        jLabel22.setDisplayedMnemonic('i');
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setLabelFor(t_cliente_informacion);
        jLabel22.setText("Otra información:");

        jLabel21.setDisplayedMnemonic('b');
        jLabel21.setLabelFor(t_cliente_observaciones);
        jLabel21.setText("Observaciones:");

        t_cliente_observaciones.setColumns(20);
        t_cliente_observaciones.setRows(5);
        t_cliente_observaciones.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_observaciones.setEnabled(false);
        t_cliente_observaciones.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_observaciones.setNextFocusableComponent(b_m_seleccionar_cliente);
        jScrollPane1.setViewportView(t_cliente_observaciones);

        jf_info_adicional.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setBackground(new java.awt.Color(204, 255, 255));
        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setText("Información Extra:");
        jf_info_adicional.getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jLabel26.setBackground(new java.awt.Color(204, 255, 255));
        jLabel26.setDisplayedMnemonic('i');
        jLabel26.setLabelFor(t_cliente_info3);
        jLabel26.setText("Información extra 1:");

        jScrollPane3.setBackground(new java.awt.Color(204, 255, 255));

        t_cliente_info3.setColumns(20);
        t_cliente_info3.setRows(5);
        t_cliente_info3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_info3.setEnabled(false);
        t_cliente_info3.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jScrollPane3.setViewportView(t_cliente_info3);

        jLabel27.setDisplayedMnemonic('e');
        jLabel27.setLabelFor(t_cliente_info4);
        jLabel27.setText("Información extra 2:");

        jScrollPane4.setBackground(new java.awt.Color(204, 255, 255));

        t_cliente_info4.setColumns(20);
        t_cliente_info4.setRows(5);
        t_cliente_info4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_info4.setEnabled(false);
        t_cliente_info4.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jScrollPane4.setViewportView(t_cliente_info4);

        jLabel28.setText("<html>¿Desea recibir <u>l</u>lamadas?:</html>");

        jLabel29.setBackground(new java.awt.Color(204, 255, 255));
        jLabel29.setText("<html>¿Desea <u>r</u>ecibir correos?:</html>");

        b_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_aceptar.setMnemonic('a');
        b_aceptar.setText("Aceptar");
        b_aceptar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aceptarActionPerformed(evt);
            }
        });

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        l_cliente_recibir_correos.setMnemonic('r');
        l_cliente_recibir_correos.setEnabled(false);

        l_cliente_recibir_llamadas.setMnemonic('l');
        l_cliente_recibir_llamadas.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_cliente_recibir_llamadas)
                        .addGap(61, 61, 61)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_cliente_recibir_correos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE))))
                .addGap(23, 23, 23))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l_cliente_recibir_llamadas)))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_cliente_recibir_correos)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel27))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jf_info_adicional.getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 660, 270));

        setBackground(new java.awt.Color(255, 255, 255));
        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setPreferredSize(new java.awt.Dimension(1024, 723));
        setVerifyInputWhenFocusTarget(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p_publicidad.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(p_publicidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        p_ident_cliente.setOpaque(false);
        p_ident_cliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_numeroAfiliacion.setDisplayedMnemonic('m');
        lb_numeroAfiliacion.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        lb_numeroAfiliacion.setLabelFor(t_numeroAfiliacion);
        lb_numeroAfiliacion.setText("Número Afiliación:");
        p_ident_cliente.add(lb_numeroAfiliacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 210, -1));

        t_numeroAfiliacion.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_numeroAfiliacion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_numeroAfiliacion.setName("numeroAfiliacion"); // NOI18N
        t_numeroAfiliacion.setNextFocusableComponent(l_tipoDocumento);
        t_numeroAfiliacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_numeroAfiliacionKeyPressed(evt);
            }
        });
        p_ident_cliente.add(t_numeroAfiliacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 240, -1));

        jLabel3.setDisplayedMnemonic('p');
        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel3.setLabelFor(l_tipoDocumento);
        jLabel3.setText("Tipo de Documento:");
        p_ident_cliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        l_tipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CÉDULA", "RUC Natural", "RUC Jurídico", "PASAPORTE" }));
        l_tipoDocumento.setNextFocusableComponent(t_documento);
        l_tipoDocumento.setRenderer(ciudadesListRenderer);
        l_tipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_tipoDocumentoActionPerformed(evt);
            }
        });
        l_tipoDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                l_tipoDocumentoKeyPressed(evt);
            }
        });
        p_ident_cliente.add(l_tipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 258, -1));

        t_documento.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_documento.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_documento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_documentoActionPerformed(evt);
            }
        });
        t_documento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_documentoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_documentoKeyTyped(evt);
            }
        });
        p_ident_cliente.add(t_documento, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 258, -1));

        lb_vendedor.setDisplayedMnemonic('v');
        lb_vendedor.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        lb_vendedor.setLabelFor(l_vendedor);
        lb_vendedor.setText("<html><u>V</u>endedor:</html>");
        p_ident_cliente.add(lb_vendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        l_vendedor.setSelectedItem(new Vendedor());
        l_vendedor.setKeySelectionManager(new VendedorKeySelectionManager());
        l_vendedor.setNextFocusableComponent(t_numeroAfiliacion);
        l_vendedor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vendedor) {
                    Vendedor mec = (Vendedor)value;
                    setText(mec.getNombreVendedor()+" "+mec.getApellidosVendedor());
                }
                return this;
            }
        });

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaVendedores}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sesion1, eLProperty, l_vendedor);
        bindingGroup.addBinding(jComboBoxBinding);

        l_vendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_vendedorActionPerformed(evt);
            }
        });
        p_ident_cliente.add(l_vendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 240, -1));

        jLabel36.setDisplayedMnemonic('o');
        jLabel36.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel36.setLabelFor(t_documento);
        jLabel36.setText("Documento:");
        p_ident_cliente.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, -1, -1));

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getSize()+2f));
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setLabelFor(t_cliente_nombres);
        jLabel6.setText("Presione F1 para reanudar la impresión...");
        p_ident_cliente.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 300, 21));
        jLabel6.getAccessibleContext().setAccessibleName("");

        add(p_ident_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 11, 740, 150));

        p_datos_cliente.setFocusable(false);
        p_datos_cliente.setOpaque(false);
        p_datos_cliente.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel4.setText("Datos del Cliente:");
        p_datos_cliente.add(jLabel4);
        jLabel4.setBounds(12, 13, 360, 26);

        lbl_nombres.setDisplayedMnemonic('n');
        lbl_nombres.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_nombres.setLabelFor(t_cliente_nombres);
        lbl_nombres.setText("Nombre:");
        p_datos_cliente.add(lbl_nombres);
        lbl_nombres.setBounds(10, 120, 98, 21);

        lb_semanasEmbarazo2.setDisplayedMnemonic('z');
        lb_semanasEmbarazo2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_semanasEmbarazo2.setLabelFor(t_cliente_semanasEmbarazo);
        lb_semanasEmbarazo2.setText("embarazo:");
        p_datos_cliente.add(lb_semanasEmbarazo2);
        lb_semanasEmbarazo2.setBounds(10, 330, 98, 22);

        jLabel7.setDisplayedMnemonic('c');
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setLabelFor(t_cliente_Ciudad);
        jLabel7.setText("Provincia:");
        p_datos_cliente.add(jLabel7);
        jLabel7.setBounds(6, 201, 98, 19);

        lb_cliente_preafiliado1.setDisplayedMnemonic('s');
        lb_cliente_preafiliado1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_cliente_preafiliado1.setLabelFor(c_afiliarse);
        lb_cliente_preafiliado1.setText("¿ Desea que le llamemos para afiliarse?");
        p_datos_cliente.add(lb_cliente_preafiliado1);
        lb_cliente_preafiliado1.setBounds(184, 52, 300, 14);

        jLabel9.setDisplayedMnemonic('f');
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setLabelFor(t_cliente_prefijoTelefonoParticular);
        jLabel9.setText("Teléfono:");
        p_datos_cliente.add(jLabel9);
        jLabel9.setBounds(35, 236, 69, 25);

        jLabel10.setDisplayedMnemonic('d');
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setLabelFor(t_cliente_direccion);
        jLabel10.setText("Dirección:");
        p_datos_cliente.add(jLabel10);
        jLabel10.setBounds(24, 161, 80, 17);

        jLabel12.setDisplayedMnemonic('e');
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setLabelFor(t_cliente_email);
        jLabel12.setText("Email:");
        p_datos_cliente.add(jLabel12);
        jLabel12.setBounds(353, 198, 94, 26);

        lb_cliente_apellidos.setDisplayedMnemonic('a');
        lb_cliente_apellidos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_cliente_apellidos.setLabelFor(t_cliente_apellidos);
        lb_cliente_apellidos.setText("Apellidos:");
        p_datos_cliente.add(lb_cliente_apellidos);
        lb_cliente_apellidos.setBounds(353, 116, 94, 29);

        t_cliente_nombres.setToolTipText("");
        t_cliente_nombres.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_nombres.setEnabled(false);
        t_cliente_nombres.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_nombres.setName("nombre"); // NOI18N
        p_datos_cliente.add(t_cliente_nombres);
        t_cliente_nombres.setBounds(121, 116, 226, 29);

        t_cliente_apellidos.setToolTipText("");
        t_cliente_apellidos.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_apellidos.setEnabled(false);
        t_cliente_apellidos.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        p_datos_cliente.add(t_cliente_apellidos);
        t_cliente_apellidos.setBounds(465, 116, 282, 29);

        t_cliente_direccion.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_direccion.setEnabled(false);
        t_cliente_direccion.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_direccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cliente_direccionFocusLost(evt);
            }
        });
        p_datos_cliente.add(t_cliente_direccion);
        t_cliente_direccion.setBounds(120, 157, 627, 29);

        t_cliente_email.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_email.setEnabled(false);
        t_cliente_email.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_cliente_emailActionPerformed(evt);
            }
        });
        p_datos_cliente.add(t_cliente_email);
        t_cliente_email.setBounds(465, 198, 282, 29);

        jLabel15.setDisplayedMnemonic('l');
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setLabelFor(t_cliente_celular);
        jLabel15.setText("Celular :");
        p_datos_cliente.add(jLabel15);
        jLabel15.setBounds(353, 236, 94, 26);

        t_cliente_tel_particular.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_cliente_tel_particular.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_tel_particular.setEnabled(false);
        t_cliente_tel_particular.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_tel_particular.setNextFocusableComponent(t_cliente_celular);
        p_datos_cliente.add(t_cliente_tel_particular);
        t_cliente_tel_particular.setBounds(190, 236, 98, 26);

        t_cliente_prefijoTelefonoParticular.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09" }));
        t_cliente_prefijoTelefonoParticular.setEnabled(false);
        t_cliente_prefijoTelefonoParticular.setRenderer(ciudadesListRenderer);
        p_datos_cliente.add(t_cliente_prefijoTelefonoParticular);
        t_cliente_prefijoTelefonoParticular.setBounds(121, 236, 60, 20);

        t_cliente_operadorCelular.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MOVISTAR", "ONO" }));
        t_cliente_operadorCelular.setEnabled(false);
        t_cliente_operadorCelular.setKeySelectionManager(new OperadoresKeySelectionManager());
        t_cliente_operadorCelular.setNextFocusableComponent(t_cliente_fechaNacimientoBebe);
        t_cliente_operadorCelular.setRenderer(operadoresListRenderer);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaOperadorMovil}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, operadoresMoviles, eLProperty, t_cliente_operadorCelular, "op01");
        bindingGroup.addBinding(jComboBoxBinding);

        p_datos_cliente.add(t_cliente_operadorCelular);
        t_cliente_operadorCelular.setBounds(618, 236, 129, 20);

        lb_fechaNacimientoUltimoBebe1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_fechaNacimientoUltimoBebe1.setLabelFor(t_cliente_fechaNacimientoBebe);
        lb_fechaNacimientoUltimoBebe1.setText("Fecha nacimiento");
        p_datos_cliente.add(lb_fechaNacimientoUltimoBebe1);
        lb_fechaNacimientoUltimoBebe1.setBounds(-6, 283, 110, 11);

        lb_fechaNacimientoUltimoBebe2.setDisplayedMnemonic('b');
        lb_fechaNacimientoUltimoBebe2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_fechaNacimientoUltimoBebe2.setLabelFor(t_cliente_fechaNacimientoBebe);
        lb_fechaNacimientoUltimoBebe2.setText("último bebé:");
        p_datos_cliente.add(lb_fechaNacimientoUltimoBebe2);
        lb_fechaNacimientoUltimoBebe2.setBounds(14, 287, 90, 20);

        lb_semanasEmbarazo1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_semanasEmbarazo1.setText("Semanas de");
        p_datos_cliente.add(lb_semanasEmbarazo1);
        lb_semanasEmbarazo1.setBounds(6, 317, 98, 22);

        t_cliente_celular.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_cliente_celular.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_celular.setEnabled(false);
        t_cliente_celular.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        t_cliente_celular.setNextFocusableComponent(t_cliente_operadorCelular);
        p_datos_cliente.add(t_cliente_celular);
        t_cliente_celular.setBounds(465, 236, 147, 25);

        t_cliente_Ciudad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Quito" }));
        t_cliente_Ciudad.setEnabled(false);
        t_cliente_Ciudad.setKeySelectionManager(new CiudadesKeySelectionManager());
        t_cliente_Ciudad.setNextFocusableComponent(t_cliente_email);
        t_cliente_Ciudad.setRenderer(ciudadesListRenderer);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaCiudades}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ciudades, eLProperty, t_cliente_Ciudad, "c2");
        bindingGroup.addBinding(jComboBoxBinding);

        t_cliente_Ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_cliente_CiudadActionPerformed(evt);
            }
        });
        p_datos_cliente.add(t_cliente_Ciudad);
        t_cliente_Ciudad.setBounds(116, 198, 226, 20);

        t_cliente_fechaNacimientoBebe.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_fechaNacimientoBebe.setEnabled(false);
        t_cliente_fechaNacimientoBebe.setInputVerifier(new InputFormVerifier());
        t_cliente_fechaNacimientoBebe.setNextFocusableComponent(t_cliente_semanasEmbarazo);
        t_cliente_fechaNacimientoBebe.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cliente_fechaNacimientoBebeFocusLost(evt);
            }
        });
        p_datos_cliente.add(t_cliente_fechaNacimientoBebe);
        t_cliente_fechaNacimientoBebe.setBounds(121, 283, 155, 22);

        t_cliente_semanasEmbarazo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_cliente_semanasEmbarazo.setEnabled(false);
        t_cliente_semanasEmbarazo.setInputVerifier(new InputFormVerifier());
        t_cliente_semanasEmbarazo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cliente_semanasEmbarazoFocusLost(evt);
            }
        });
        p_datos_cliente.add(t_cliente_semanasEmbarazo);
        t_cliente_semanasEmbarazo.setBounds(121, 317, 155, 22);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        p_datos_cliente.add(jLabel1);
        jLabel1.setBounds(40, 350, 701, 18);

        c_afiliarse.setEnabled(false);
        c_afiliarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_afiliarseActionPerformed(evt);
            }
        });
        p_datos_cliente.add(c_afiliarse);
        c_afiliarse.setBounds(499, 51, 21, 21);

        lb_cliente_tipo_socio.setFont(lb_cliente_tipo_socio.getFont().deriveFont(lb_cliente_tipo_socio.getFont().getStyle() | java.awt.Font.BOLD, 14));
        lb_cliente_tipo_socio.setForeground(new java.awt.Color(51, 153, 255));
        lb_cliente_tipo_socio.setText("tipo de Socio");
        p_datos_cliente.add(lb_cliente_tipo_socio);
        lb_cliente_tipo_socio.setBounds(206, 83, 160, 17);

        jLabel8.setFont(jLabel8.getFont().deriveFont(jLabel8.getFont().getStyle() | java.awt.Font.BOLD, jLabel8.getFont().getSize()+2));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("<html>Tipo Afiliado:</html>");
        p_datos_cliente.add(jLabel8);
        jLabel8.setBounds(0, 70, 100, 40);
        p_datos_cliente.add(lb_tarjeta);
        lb_tarjeta.setBounds(120, 70, 59, 39);

        jLabel_puntos.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel_puntos.setForeground(new java.awt.Color(-13395457,true));
        p_datos_cliente.add(jLabel_puntos);
        jLabel_puntos.setBounds(60, 350, 350, 20);

        lb_tipo_cliente.setFont(lb_tipo_cliente.getFont().deriveFont(lb_tipo_cliente.getFont().getStyle() | java.awt.Font.BOLD, 12));
        lb_tipo_cliente.setForeground(new java.awt.Color(51, 153, 255));
        lb_tipo_cliente.setText("Tipo Cliente:");
        p_datos_cliente.add(lb_tipo_cliente);
        lb_tipo_cliente.setBounds(370, 83, 390, 15);

        add(p_datos_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 169, 780, 408));

        menu_navegacion.setBackground(new java.awt.Color(255, 255, 255));
        menu_navegacion.setOpaque(false);
        menu_navegacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_m_editar_cliente.setText("<html><center>Editar Datos <br/>F6</center></html>");
        b_m_editar_cliente.setEnabled(false);
        b_m_editar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_editar_clienteActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_editar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 7, 85, 85));

        b_m_facturar_otro_cliente.setText("<html><center>Facturar<br/>con otros<br/>Datos<br/> F4</center></html>");
        b_m_facturar_otro_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_facturar_otro_clienteActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_facturar_otro_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 7, 85, 85));

        b_m_consumidor_final.setText("<html><center>Consumidor Final<br/>F3 </center></html>");
        b_m_consumidor_final.setAlignmentX(1.0F);
        b_m_consumidor_final.setEnabled(false);
        b_m_consumidor_final.setMargin(new java.awt.Insets(0, -5, 0, 0));
        b_m_consumidor_final.setMinimumSize(new java.awt.Dimension(90, 87));
        b_m_consumidor_final.setPreferredSize(new java.awt.Dimension(90, 87));
        b_m_consumidor_final.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_consumidor_finalActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_consumidor_final, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 7, 85, 85));

        b_m_informacion_extra.setText("<html><center>Información Extra <br/>F5</center></html>");
        b_m_informacion_extra.setEnabled(false);
        b_m_informacion_extra.setMargin(new java.awt.Insets(0, -3, 0, 0));
        b_m_informacion_extra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_informacion_extraActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_informacion_extra, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 7, 85, 85));

        b_m_confirmar_cliente.setText("<html><center>Salvar Datos<br/> F11</center></html>");
        b_m_confirmar_cliente.setEnabled(false);
        b_m_confirmar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_confirmar_clienteActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_confirmar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 7, 85, 85));

        b_m_menu_principal.setText("<html><center>Menú Principal <br/>F12</center></html>");
        b_m_menu_principal.setNextFocusableComponent(l_vendedor);
        b_m_menu_principal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_menu_principalActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_menu_principal, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 7, 85, 85));

        b_m_seleccionar_cliente.setText("<html><center>Facturar con datos<br> Ingresados<br/> F2</center></html>");
        b_m_seleccionar_cliente.setActionCommand("Identificar Cliente F3");
        b_m_seleccionar_cliente.setMargin(new java.awt.Insets(0, -3, 0, 0));
        b_m_seleccionar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_m_seleccionar_clienteActionPerformed(evt);
            }
        });
        menu_navegacion.add(b_m_seleccionar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 7, 85, 85));

        add(menu_navegacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, -1, -1));

        p_avisos.setBackground(java.awt.Color.white);
        p_avisos.setOpaque(false);
        p_avisos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        jLabel24.setText("Avisos:");
        jLabel24.setFocusable(false);
        p_avisos.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 17));
        p_avisos.add(lb_error_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 24, 24));

        lb_error_ml.setBackground(new java.awt.Color(0, 0, 0));
        lb_error_ml.setForeground(new java.awt.Color(255, 51, 0));
        lb_error_ml.setFocusable(false);
        lb_error_ml.setFont(lb_error_ml.getFont().deriveFont(lb_error_ml.getFont().getStyle() | java.awt.Font.BOLD, lb_error_ml.getFont().getSize()+1));
        lb_error_ml.setMaximumSize(new java.awt.Dimension(200, 70));
        p_avisos.add(lb_error_ml, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 230, 80));

        add(p_avisos, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 600, 330, 110));

        getAccessibleContext().setAccessibleName("j_contenedor");

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Acciones">
    // <editor-fold defaultstate="collapsed" desc="Botones">
    private void b_m_editar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_editar_clienteActionPerformed
        if (bloquearAcciones) {
            return;
        }
        bloquearAcciones = true;
        try {
            if (b_m_editar_cliente.isEnabled()) {
                log.info("EDITAR CLIENTE");
                if (jf_info_adicional.isVisible()) {
                    this.setEnabled(true);
                    jf_info_adicional.setVisible(false);
                }
                if (editandoFormulario) {
                    cerrarEdicionFormulario();
                    if (tipoOperacionConfirmarCliente.equals("CREATE")) {
                        reseteaDatosCliente();
                        t_documento.requestFocus(true);
                        b_m_editar_cliente.setEnabled(false);
                        b_m_informacion_extra.setEnabled(false);
                    } else { // si estábamos editando 
                        reseteaDatosCliente();
                        cargarCliente(false);
                    }
                } // Modificación, ahora al editar un cliente se habilitan los botones de Facturar Cliente y facturar otros datos, ante lo cual se guardará y validará el cliente. 
                else if (clienteConsultado != null) {
                    t_documento.setText(clienteConsultado.getIdentificacion());
                    t_numeroAfiliacion.setText(clienteConsultado.getCodigoTarjetaBabysClub());
                    editandoFormulario = true;
                    setEnabledFormulario(true);
                    b_m_editar_cliente.setText("<html><center>Cancelar Edición <br/>F6</center></html>");
                    b_m_facturar_otro_cliente.setEnabled(false);
                    b_m_consumidor_final.setEnabled(false);
                    b_m_menu_principal.setEnabled(false);
                    b_m_seleccionar_cliente.setEnabled(false);
                    b_m_confirmar_cliente.setEnabled(true);
                    b_m_confirmar_cliente.setNextFocusableComponent(l_vendedor);
                    t_cliente_nombres.requestFocus();
                }
            } else {
            }
        } finally {
            bloquearAcciones = false;
        }

    }//GEN-LAST:event_b_m_editar_clienteActionPerformed

    private void setEnabledFormulario(boolean enabled) {
        t_cliente_Ciudad.setEnabled(enabled);
        /*  if (l_tipoDocumento.getSelectedItem().equals("RUC Jurídico")) {
        t_cliente_apellidos.setEnabled(false);
        }
   else {*/
        t_cliente_apellidos.setEnabled(enabled);
        //}
        t_cliente_celular.setEnabled(enabled);
        t_cliente_direccion.setEnabled(enabled);
        t_cliente_email.setEnabled(enabled);
        t_cliente_estadoCivil.setEnabled(enabled);
        t_cliente_fechaNacimiento.setEnabled(enabled);

        // Si MODO BEBEMUNDO
        if (Sesion.isBebemundo()) {
            t_cliente_fechaNacimientoBebe.setEnabled(enabled);
            t_cliente_semanasEmbarazo.setEnabled(enabled);
        }
        t_cliente_genero.setEnabled(enabled);
        t_cliente_info3.setEnabled(enabled);
        t_cliente_info4.setEnabled(enabled);
        t_cliente_informacion.setEnabled(enabled);
        t_cliente_observaciones.setEnabled(enabled);
        t_cliente_nombres.setEnabled(enabled);
        t_cliente_operadorCelular.setEnabled(enabled);
        c_afiliarse.setEnabled(enabled);
        t_cliente_prefijoTelefonoParticular.setEnabled(enabled);
        t_cliente_prefijoTelefonoTrabajo.setEnabled(enabled);

        t_cliente_tel_particular.setEnabled(enabled);
        t_cliente_tel_trabajo.setEnabled(enabled);
        l_cliente_recibir_correos.setEnabled(enabled);
        l_cliente_recibir_llamadas.setEnabled(enabled);
        t_numeroAfiliacion.setEnabled(!enabled);
        t_documento.setEnabled(!enabled);
        l_tipoDocumento.setEnabled(!enabled);

        for (IValidableForm elem : formularioCliente) {
            elem.setValidacionHabilitada(enabled);
        }

    }

    private void b_m_consumidor_finalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_consumidor_finalActionPerformed
        if (bloquearAcciones) {
            return;
        }
        bloquearAcciones = true;
        try {
            log.info("SELECIÓN DE CONSUMIDOR FINAL");
            if (jf_info_adicional.isVisible()) {
                this.setEnabled(false);
                jf_info_adicional.setVisible(false);
            }
            if (this.ventana_padre != null) {
                iniciaNuevaVenta(Sesion.getClienteGenericoReset(), CLIENTEFINAL);
            }
        } finally {
            bloquearAcciones = false;
        }
    }//GEN-LAST:event_b_m_consumidor_finalActionPerformed

    private void b_m_informacion_extraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_informacion_extraActionPerformed
        if (bloquearAcciones) {
            return;
        }
        bloquearAcciones = true;
        try {
            if (b_m_informacion_extra.isEnabled()) {
                log.info("INFORMACIÓN EXTRA");
                this.setEnabled(false);
                jf_info_adicional.setSize(680, 370);
                jf_info_adicional.setLocationRelativeTo(null);
                // Si  los campos de texto estan habilitados reciben el foco
                if (t_cliente_info3.isEnabled()) {
                    t_cliente_info3.requestFocusInWindow();
                } // en otro caso el boton aceptar lo hace
                else {
                    b_aceptar.requestFocusInWindow();
                }
                jf_info_adicional.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent we) {
                        setEnabled(true);
                        t_documento.requestFocusInWindow();
                        super.windowClosing(we);
                    }
                });
                if (l_cliente_recibir_llamadas.isEnabled()) {
                    l_cliente_recibir_llamadas.requestFocus();
                } else {
                    b_aceptar.requestFocus();
                }
                JPrincipal.setPopupActivo(jf_info_adicional);
                jf_info_adicional.setVisible(true);
                JPrincipal.setPopupActivo(null);
            }
        } finally {
            bloquearAcciones = false;
        }
    }//GEN-LAST:event_b_m_informacion_extraActionPerformed

    private void b_m_confirmar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_confirmar_clienteActionPerformed
        if (bloquearAcciones) {
            return;
        }
        bloquearAcciones = true;
        try {
            if (b_m_confirmar_cliente.isEnabled()) {

                //Acción Confirmar Cliente
                try {

                    validarFormulario();
                    log.info("SALVAR CLIENTE");
                    if (jf_info_adicional.isVisible()) {
                        this.setEnabled(true);
                        jf_info_adicional.setVisible(false);
                    }

                    if (this.tipoOperacionConfirmarCliente.equals("CREATE")) {
                        jf_info_adicional.setSize(680, 390);
                        jf_info_adicional.setLocationRelativeTo(null);
                        jf_info_adicional.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                        jf_info_adicional.setModalityType(ModalityType.APPLICATION_MODAL);
                        jf_info_adicional.setEnabled(true);
                        l_cliente_recibir_llamadas.requestFocus();
                        JPrincipal.setPopupActivo(jf_info_adicional);
                        jf_info_adicional.setVisible(true);
                        JPrincipal.setPopupActivo(null);

                    }
                    accionConfirmarCliente();
                    //Modificación. ahora siempre se va a venta
                    //if (tipoOperacionConfirmarCliente.equals("CREATE")) { // Para que cuando guarde, que se vaya directamente a ventas
                    b_m_seleccionar_clienteActionPerformed(null);
                    //}
                } catch (ValidationFormException ex) {
                    log.debug("La validación de datos del cliente ha sido errónea.");
                    if (ex.getMessage().equals("Debe tener al menos un número de teléfono.")) {
                        ventana_padre.crearAdvertencia(ex.getMessage());
                        t_cliente_tel_particular.requestFocus();
                    } else {
                        ventana_padre.crearAdvertencia(ex.getMessage());
                    }
                } catch (ClienteException ex) {
                    log.error("b_m_confirmar_clienteActionPerformed() - Error confirmando cliente: " + ex.getMessage(), ex);
                    ventana_padre.crearError(ex.getMessage());
                    Vendedor v;
                }
            } else {
            }
        } finally {
            bloquearAcciones = false;
        }
    }//GEN-LAST:event_b_m_confirmar_clienteActionPerformed

    private void cerrarEdicionFormulario() {
        log.info("CERRAR EDICIÓN DE FORMULARIO");
        setEnabledFormulario(false);
        b_m_editar_cliente.setText("<html><center>Editar Datos <br/>F6</center></html>");
        //aqui
        b_m_consumidor_final.setEnabled(false);
        b_m_facturar_otro_cliente.setEnabled(true);
        b_m_seleccionar_cliente.setEnabled(true);
        b_m_confirmar_cliente.setEnabled(false);
        b_m_menu_principal.setEnabled(true);
        editandoFormulario = false;
        b_m_editar_cliente.setNextFocusableComponent(null);
        t_documento.requestFocus();
    }

    private void b_m_menu_principalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_menu_principalActionPerformed
        if (bloquearAcciones) {
            return;
        }
        bloquearAcciones = true;
        if (b_m_menu_principal.isEnabled()) {
            log.info("MENÚ PRINCIPAL");
            if (jf_info_adicional.isVisible()) {
                this.setEnabled(true);
                jf_info_adicional.setVisible(false);
            }
            ventana_padre.mostrarMenu("ident-cliente", "VENTA");
        }
        bloquearAcciones = false;
    }//GEN-LAST:event_b_m_menu_principalActionPerformed
//  </editor-fold> Acciones asignadas a Botones de la Interfaz

    // <editor-fold defaultstate="collapsed" desc="Teclado">
    @Override
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == '\n') {
            if (t_numeroAfiliacion.hasFocus()) {
                log.info("CONSULTA POR NUMERO DE AFILIADO");
                reseteaDatosCliente();
                cargarCliente(true);
            } else if (t_documento.hasFocus()) {
                t_documento.setText(t_documento.getText().toUpperCase());
                if (t_documento.getText().equals("")) {
                    log.info("RESETEO DE DATOS DE CLIENTE");
                    reseteaDatosCliente();
                } else {
                    log.info("CONSULTA POR DOCUMENTO");
                    reseteaDatosCliente();
                    cargarCliente(false);

                }
            }
        }
    }

    private void cargarCliente(boolean numeroAfiliacion) {
        if (bloquearAcciones) {
            return;
        }
        if (l_tipoDocumento.getSelectedItem().equals("RUC Jurídico")) {
            habilitarCamposClienteNatural(true);
        } else {
            habilitarCamposClienteNatural(true);
        }
        String stipoDocumento = "";
        try {
            // Identificación por numero de afiliación
            if (numeroAfiliacion) {
                log.debug("Carga de Cliente por número de afiliación");
                if (!t_numeroAfiliacion.getText().equals("")) {
                    clienteConsultado = clienteService.consultaClienteNAfil(t_numeroAfiliacion.getText());
                    establecerPuntosTotales();
                    actualizaDatosCliente();
                    tipoOperacionConfirmarCliente = "UPDATE";
                }
            } else {
                log.debug("Carga de Cliente por documento");
                // Recuperamos el tipo de documento
                if (l_tipoDocumento.getSelectedIndex() == 0) {
                    l_tipoDocumento.setSelectedIndex(0);
                    lbl_nombres.setText("Nombres:");
                    lb_cliente_apellidos.setText("Apellidos");
                    stipoDocumento = "CED";
                } else if (l_tipoDocumento.getSelectedIndex() == 1) {
                    l_tipoDocumento.setSelectedIndex(1);
                    lbl_nombres.setText("Nombres:");
                    lb_cliente_apellidos.setText("Apellidos");
                    stipoDocumento = "RUN";
                } else if (l_tipoDocumento.getSelectedIndex() == 2) {
                    l_tipoDocumento.setSelectedIndex(2);
                    lbl_nombres.setText("Nom. Comercial:");
                    lb_cliente_apellidos.setText("Raz\u00F3n social");
                    stipoDocumento = "RUJ";
                } else if (l_tipoDocumento.getSelectedIndex() == 3) {
                    l_tipoDocumento.setSelectedIndex(3);
                    lbl_nombres.setText("Nombres:");
                    lb_cliente_apellidos.setText("Apellidos");
                    stipoDocumento = "PAS";
                }
                if (!t_documento.getText().equals("")) {
                    clienteConsultado = clienteService.consultaClienteDoc(t_documento.getText(), stipoDocumento);
                    establecerPuntosTotales();
                    actualizaDatosCliente();
                    tipoOperacionConfirmarCliente = "UPDATE";
                }
            }
            if (clienteConsultado != null) {
                b_m_seleccionar_cliente.setEnabled(true);
                b_m_facturar_otro_cliente.setEnabled(true);
                b_m_informacion_extra.setEnabled(true);
                b_m_editar_cliente.setEnabled(true);
                b_m_seleccionar_cliente.requestFocus();
                TipoClienteBean tipoCliente = TiposClientes.getInstancia().getTipoCliente(clienteConsultado.getTipoCliente());
                if (tipoCliente != null) {
                    lb_tipo_cliente.setVisible(true);
                    lb_tipo_cliente.setText("Tipo Cliente: " + tipoCliente.getDesTipoCliente());
                }
            }
        } catch (NoResultException e) {

            if (compruebaDocumento() && !numeroAfiliacion) {
                if (t_documento.hasFocus()) {
                    clienteConsultado = new Cliente(t_documento.getText(), stipoDocumento);
                    if (!Sesion.getDatosConfiguracion().isModoDesarrollo() && VariablesAlm.getVariableAsBooleanActual(VariablesAlm.CONSULTA_REGISTRO_CIVIL)) {
                        ClienteRest cliente = new ClienteRest();
                        try {
                            String resultado = cliente.clientGetByHttpConnection(Variables.getVariable(Variables.WEBSERVICE_CONSULTAS_ESB_URL) + "consultaDatosRegistroCivil/" + t_documento.getText());
                            ResponseDTO responseDTO = JsonUtil.jsonToObject(resultado, ResponseDTO.class);
                            if (responseDTO.getExito()) {

                                String objectoRespuesta = JsonUtil.objectToJson(responseDTO.getObjetoRespuesta());
                                DatosCiudadanoDTO datosCiudadano = JsonUtil.jsonToObject(objectoRespuesta, DatosCiudadanoDTO.class);
                                clienteConsultado.setNombre(datosCiudadano.getNombres());
                                clienteConsultado.setApellido(datosCiudadano.getApellidos());
                                clienteConsultado.setDireccion(datosCiudadano.getDomicilio());
                                t_cliente_Ciudad.setSelectedItem(new Ciudad(Sesion.getTienda().getAlmacen().getProvincia()));
                            }
                            log.info("Respuesta servicio web " + resultado);
                        } catch (IOException ex) {
                            log.error("Error al consultar en el registro civil ", ex);
                        }
                    }
                    if (l_tipoDocumento.getSelectedItem().equals("RUC Jurídico")) {
                        // clienteConsultado.setApellido("Empresa");
                        habilitarCamposClienteNatural(true);
                    }

                    actualizaDatosCliente();
                    eliminarPuntostotales();
                    tipoOperacionConfirmarCliente = "CREATE";
                    editandoFormulario = true;
                    setEnabledFormulario(true);
                    b_m_editar_cliente.setText("<html><center>Cancelar Alta <br/>F6</center></html>");
                    b_m_editar_cliente.setEnabled(true);
                    b_m_informacion_extra.setEnabled(true);
                    b_m_facturar_otro_cliente.setEnabled(false);
                    b_m_consumidor_final.setEnabled(false);
                    b_m_confirmar_cliente.setEnabled(true);
                    b_m_menu_principal.setEnabled(false);
                    b_m_confirmar_cliente.setNextFocusableComponent(l_vendedor);
                    c_afiliarse.requestFocus(true);
                    log.debug("El cliente no existe, se va a crear un nuevo cliente.");
                    ventana_padre.crearAdvertencia("El cliente no existe, se va a crear un nuevo cliente.");
                    //t_cliente_nombres.requestFocus();
                    //reseteaDatosCliente();
                }
            } else if (!numeroAfiliacion) {
                log.info("El número de " + ((String) l_tipoDocumento.getSelectedItem()).toLowerCase() + " no es un número de documento válido.");
                ventana_padre.crearError("El número de " + ((String) l_tipoDocumento.getSelectedItem()).toLowerCase() + " no es un número de documento válido.");
            } else if (numeroAfiliacion) {
                log.error("cargarCliente() - Error cargando cliente: " + e.getMessage(), e);
                ventana_padre.crearError("El número de Afiliación no existe.");
            }
        } catch (ClienteException e) {
            log.error("cargarCliente() - Error cargando cliente: " + e.getMessage(), e);
            ventana_padre.crearError(e.getMessage());
        } catch (Exception e) {
            log.error("cargarCliente() - Error cargando cliente: " + e.getMessage(), e);
            ventana_padre.crearError("Se ha producido un error intentando cargar los datos del cliente.");
        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    // </editor-fold> Acciones asignadas a Teclado    
    // </editor-fold> 
    // <editor-fold defaultstate="collapsed" desc="Delaracion de variables">
private void b_m_facturar_otro_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_facturar_otro_clienteActionPerformed
    if (clienteConsultado != null) {
        if (jf_info_adicional.isVisible()) {
            this.setEnabled(true);
            jf_info_adicional.setVisible(false);
        }
        try {
            if (this.tipoOperacionConfirmarCliente.equals("CREATE") && clienteConsultado != null) {
                this.accionConfirmarCliente();
            }

            if (clienteConsultado.isListaNegra()) {
                String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.ADMITIR_LISTA_NEGRA);
                try {
                    ServicioLogAcceso.crearAccesoLogPermitirClienteListaNegra(compruebaAutorizacion, clienteConsultado);
                } catch (Exception e) {
                    log.error("Error creando log al permitir cliente en lista negra > administrador" + compruebaAutorizacion);
                }
            }
            if (!clienteConsultado.isActivo()) {
                throw new ClienteInactiveException("El cliente no se encuentra Activado");
            }
            if (clienteConsultado != null && clienteConsultado.getIdentificacion() != null) {
                iniciaNuevaVenta(clienteConsultado, OTROCLIENTE);
            }
        } catch (ValidationFormException ex) {
            log.debug("La validación de datos del cliente ha sido errónea.");
            log.error(ex.getMessage());
            ventana_padre.crearError(ex.getMessage());
        } catch (ClienteException ex) {
            log.error(ex.getMessage());
            ventana_padre.crearError(ex.getMessage());
        } catch (SinPermisosException ex) {
            log.error("Error de permisos al seleccionar cliente.", ex);
        } catch (ClienteInactiveException ex) {
            log.info("cliente no activo");
            ventana_padre.crearAdvertencia(ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("NullPointerException debido a no haber seleccionado un cliente antes.", ex);
        } catch (Exception ex) {
            log.error("Excepcion no controlada en facturar cliente", ex);
        }
    }
}//GEN-LAST:event_b_m_facturar_otro_clienteActionPerformed

private void b_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aceptarActionPerformed
    //((Frame)jf_info_adicional.getParent()).setEnabled(true);
    // Actualiza la información del bean de cliente

    jf_info_adicional.setVisible(false);
    t_documento.requestFocusInWindow();
    this.setEnabled(true);
}//GEN-LAST:event_b_aceptarActionPerformed

private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
    jf_info_adicional.setVisible(false);
    t_documento.requestFocusInWindow();
    jf_info_adicional.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
    this.setEnabled(true);
}//GEN-LAST:event_b_cancelarActionPerformed

private void l_vendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_vendedorActionPerformed
}//GEN-LAST:event_l_vendedorActionPerformed

private void b_m_seleccionar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_m_seleccionar_clienteActionPerformed

    try {

        if (VariablesAlm.getVariableAsBooleanActual(Variables.POS_VALIDACION_PROTECCION_DATOS)) {

            if (!PoliticaProteccionDatosServices.existeAprobacionPoliticas(clienteConsultado.getCodcli())) {

                boolean tieneCreditoActivo = Boolean.TRUE;
                try {
                    CreditoServices.consultarPlasticoPorCedula(clienteConsultado.getCodcli());
                } catch (CreditoNotFoundException ex) {
                    tieneCreditoActivo = Boolean.FALSE;
                }

                if (tieneCreditoActivo) {
                    if (JPrincipal.getInstance().crearVentanaConfirmacion("EL CLIENTE NO HA ACEPTADO TODAVÍA LA POLÍTICA DE PROTECCIÓN DE DATOS", "VERIFICAR", "CANCELAR")) {
                        PoliticaProteccionDatosServices.existeAprobacionPoliticas(clienteConsultado.getCodcli());
                    }
                } 

            } 
        } 

        if (jf_info_adicional.isVisible()) {
            this.setEnabled(true);
            jf_info_adicional.setVisible(false);
        }
        if (clienteConsultado != null) {
            if (clienteConsultado.getNombre() != null && clienteConsultado.getApellido() != null && clienteConsultado.getDireccion() != null && !clienteConsultado.getNombre().equals("") && !clienteConsultado.getApellido().equals("") && !clienteConsultado.getDireccion().equals("")) {
                if ((clienteConsultado.getTelefonoMovil() != null && !clienteConsultado.getTelefonoMovil().equals("")) || (clienteConsultado.getTelefonoParticular() != null && clienteConsultado.getTelefonoParticular().length() == 9)) {
                    try {
                        if (clienteConsultado.isListaNegra()) {
                            String compruebaAutorizacion = ventana_padre.compruebaAutorizacion(Operaciones.ADMITIR_LISTA_NEGRA);
                            try {
                                ServicioLogAcceso.crearAccesoLogPermitirClienteListaNegra(compruebaAutorizacion, clienteConsultado);
                            } catch (Exception e) {
                                log.error("Error creando log al permitir cliente en lista negra > administrador" + compruebaAutorizacion);
                            }
                        }
                        if (VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && clienteConsultado != null && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || clienteConsultado.isTipoRuc()) && (clienteConsultado.getEmail() == null || clienteConsultado.getEmail().isEmpty())) {
                            ventana_padre.crearError("Para facturar electrónicamente, el campo email es obligatorio.");
                            return;
                        }
                        if (clienteConsultado != null && clienteConsultado.getIdentificacion() != null) {
                            iniciaNuevaVenta(clienteConsultado, CLIENTESELECCIONADO);
                        }
                    } catch (SinPermisosException ex) {
                        log.error("Error de permisos al seleccionar cliente.", ex);
                    } catch (NullPointerException ex) {
                        log.error("NullPointerException debido a no haber seleccionado un cliente antes.", ex);
                    } catch (Exception ex) {
                        log.error("Excepcion no controlada", ex);
                    }
                } else {
                    ventana_padre.crearError("El usuario tiene datos obligatorios que no están rellenos. Por favor compruebe los datos.");
                }
            } else {
                ventana_padre.crearError("El usuario tiene datos obligatorios que no están rellenos. Por favor compruebe los datos.");
            }
        }

    } catch (CreditoException ex) {
        ventana_padre.crearError(ex.getMessage());
    }
}//GEN-LAST:event_b_m_seleccionar_clienteActionPerformed

private void l_tipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_tipoDocumentoActionPerformed
    //t_documento.requestFocus();
    if (l_tipoDocumento.getSelectedIndex() == 0) {
        lbl_nombres.setText("Nombres:");
        lb_cliente_apellidos.setText("Apellidos:");
    } else if (l_tipoDocumento.getSelectedIndex() == 1) {
        lbl_nombres.setText("Nombres:");
        lb_cliente_apellidos.setText("Apellidos:");
    } else if (l_tipoDocumento.getSelectedIndex() == 2) {
        lbl_nombres.setText("Nom. Comercial:");
        lb_cliente_apellidos.setText("Raz\u00F3n social");
    } else if (l_tipoDocumento.getSelectedIndex() == 3) {
        lbl_nombres.setText("Nombres:");
        lb_cliente_apellidos.setText("Apellidos:");
    }
}//GEN-LAST:event_l_tipoDocumentoActionPerformed

private void l_tipoDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_tipoDocumentoKeyPressed
    if (evt.getKeyChar() == '\n') {
        t_documento.requestFocus();
    }
}//GEN-LAST:event_l_tipoDocumentoKeyPressed

private void t_cliente_CiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_cliente_CiudadActionPerformed
    try {
        if ((Ciudad) t_cliente_Ciudad.getSelectedItem() != null);
        t_cliente_prefijoTelefonoParticular.setSelectedItem(((Ciudad) t_cliente_Ciudad.getSelectedItem()).getPrefijo());
    } catch (Exception e) {
        // 
    }
}//GEN-LAST:event_t_cliente_CiudadActionPerformed

private void t_numeroAfiliacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numeroAfiliacionKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_TAB) {
        reseteaDatosCliente();
        cargarCliente(true);
    }
}//GEN-LAST:event_t_numeroAfiliacionKeyPressed

private void t_documentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_documentoKeyTyped
    if (evt.getKeyCode() == KeyEvent.VK_TAB) {
        if (t_documento.getText().equals("")) {
            reseteaDatosCliente();
        } else {

            reseteaDatosCliente();
            cargarCliente(false);

        }
    }
}//GEN-LAST:event_t_documentoKeyTyped

private void t_documentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_documentoActionPerformed
}//GEN-LAST:event_t_documentoActionPerformed

private void c_afiliarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_afiliarseActionPerformed

    // Si MODO BEBEMUNDO
    if (Sesion.isBebemundo()) {
        if (c_afiliarse.isSelected()) {
            t_cliente_fechaNacimientoBebe.setVisible(true);
            t_cliente_fechaNacimientoBebe.setEnabled(true);
            t_cliente_semanasEmbarazo.setVisible(true);
            t_cliente_semanasEmbarazo.setEnabled(true);
            t_cliente_fechaNacimientoBebe.addValidador(new ValidadorFechaBebe(), this);
            t_cliente_semanasEmbarazo.addValidador(new ValidadorEntero(1, 46), this);
            t_cliente_semanasEmbarazo.setText("");
            lb_semanasEmbarazo2.setVisible(true);
            lb_fechaNacimientoUltimoBebe1.setVisible(true);
            lb_fechaNacimientoUltimoBebe2.setVisible(true);
            lb_semanasEmbarazo1.setVisible(true);
        } else {
            t_cliente_fechaNacimientoBebe.setVisible(false);
            t_cliente_semanasEmbarazo.setVisible(false);
            t_cliente_fechaNacimientoBebe.removeValidadorObligatoriedad();
            lb_semanasEmbarazo2.setVisible(false);
            lb_fechaNacimientoUltimoBebe1.setVisible(false);
            lb_fechaNacimientoUltimoBebe2.setVisible(false);
            lb_semanasEmbarazo1.setVisible(false);
        }
    }
}//GEN-LAST:event_c_afiliarseActionPerformed

private void t_cliente_semanasEmbarazoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cliente_semanasEmbarazoFocusLost
    if (!t_cliente_semanasEmbarazo.getText().isEmpty()) {
        // obtenemos número de semanas indicadas en formulario
        Integer semanas = Integer.parseInt(t_cliente_semanasEmbarazo.getText());

        // calculamos la nueva fecha de nacimiento a partir del número de semanas y la fecha de hoy
        Fecha nuevaFechaNacimiento = new Fecha();
        nuevaFechaNacimiento.sumaDias((42 - semanas) * 7); // a la fecha de hoy le sumamos 42 semanas menos las semanas que se llevan de embarazo

        // actualizamos formulario
        t_cliente_fechaNacimientoBebe.setText(nuevaFechaNacimiento.getString("dd-MMM-yyyy"));

        // siempre será una fecha mayor que hoy
        lb_fechaNacimientoUltimoBebe1.setText("El próximo bebé");
        lb_fechaNacimientoUltimoBebe2.setText("nacerá el:");
        evt.getOppositeComponent().requestFocus();
    }

    /*    if (!t_cliente_semanasEmbarazo.getText().equals("") && clienteConsultado.getFechaNacimientoUltimoHijo() != null && clienteConsultado.getSemanaEmbarazo() != null) {
    Fecha fechaAntigua = new Fecha(clienteConsultado.getFechaNacimientoUltimoHijo());
    fechaAntigua.sumaDias(clienteConsultado.getSemanaEmbarazo().subtract(new BigInteger(t_cliente_semanasEmbarazo.getText())).intValue() * 7);
    t_cliente_fechaNacimientoBebe.setText(fechaAntigua.getString("dd-MMM-yyyy"));
    Fecha fechaHoy = new Fecha();
    Fecha fechaComponente = new Fecha(t_cliente_fechaNacimientoBebe.getText(), "dd-MMM-yyyy");
    if (fechaComponente.getDate() != null) {
    if (fechaComponente.antes(fechaHoy)) {
    jLabel18.setText("El ultimo bebé");
    
    jLabel19.setText("nació el:");
    }
    else {
    jLabel18.setText("El próximo bebé");
    jLabel19.setText("nacerá el:");
    }
    }
    evt.getOppositeComponent().requestFocus();
    }
     * */
}//GEN-LAST:event_t_cliente_semanasEmbarazoFocusLost

private void t_cliente_fechaNacimientoBebeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cliente_fechaNacimientoBebeFocusLost

    Fecha fechaClienteConsultado = null;
    if (clienteConsultado.getFechaNacimientoUltimoHijo() != null && !clienteConsultado.getFechaNacimientoUltimoHijo().equals("")) {
        fechaClienteConsultado = new Fecha(clienteConsultado.getFechaNacimientoUltimoHijo());
    }
    Fecha fechaHoy = new Fecha();
    Fecha fechaComponente = null;
    if (t_cliente_fechaNacimientoBebe.getText() != null && !t_cliente_fechaNacimientoBebe.getText().equals("")) {
        fechaComponente = new Fecha(t_cliente_fechaNacimientoBebe.getText(), "dd-MMM-yyyy");
    }
    if (fechaComponente != null && fechaComponente.getDate() != null) {
        if (fechaComponente.antes(fechaHoy)) {
            lb_fechaNacimientoUltimoBebe1.setText("El ultimo bebé");
            lb_fechaNacimientoUltimoBebe2.setText("nació el:");
        } else {
            lb_fechaNacimientoUltimoBebe1.setText("El próximo bebé");
            lb_fechaNacimientoUltimoBebe2.setText("nacerá el:");
        }
    }

    if (fechaComponente != null && fechaClienteConsultado != null && !fechaClienteConsultado.equals(fechaComponente) && fechaComponente.despuesOrEquals(fechaHoy)) {
        Double diferenciaSemanas = Fechas.diferenciaSemanas(fechaClienteConsultado, fechaComponente);
        if (clienteConsultado.getSemanaEmbarazo() != null) {
            Integer semanasActual = clienteConsultado.getSemanaEmbarazo().intValue();
            if (fechaClienteConsultado.compareTo(fechaComponente) > 0) {
                Integer dif = semanasActual + diferenciaSemanas.intValue();
                t_cliente_semanasEmbarazo.setText(dif.toString());
            } else if (fechaClienteConsultado.compareTo(fechaComponente) < 0) {
                Integer dif = semanasActual - diferenciaSemanas.intValue();
                t_cliente_semanasEmbarazo.setText(dif.toString());
            }
        }
    } else if (fechaComponente != null && fechaClienteConsultado != null && !fechaClienteConsultado.equals(fechaComponente)) {
        t_cliente_semanasEmbarazo.setText("");
    }
    evt.getOppositeComponent().requestFocus();
}//GEN-LAST:event_t_cliente_fechaNacimientoBebeFocusLost

private void t_cliente_direccionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cliente_direccionFocusLost
    ((JTextFieldForm) evt.getComponent()).setSelectionStart(0);
    ((JTextFieldForm) evt.getComponent()).setSelectionEnd(0);
}//GEN-LAST:event_t_cliente_direccionFocusLost

    private void t_documentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_documentoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_documentoKeyPressed

    private void t_cliente_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_cliente_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_cliente_emailActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_confirmar_cliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_consumidor_final;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_editar_cliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_facturar_otro_cliente;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_informacion_extra;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_menu_principal;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_m_seleccionar_cliente;
    private javax.swing.JCheckBox c_afiliarse;
    private com.comerzzia.jpos.servicios.general.ciudades.Ciudades ciudades;
    private com.comerzzia.jpos.gui.modelos.CiudadesListRenderer ciudadesListRenderer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_puntos;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JDialog jf_info_adicional;
    private javax.swing.JCheckBox l_cliente_recibir_correos;
    private javax.swing.JCheckBox l_cliente_recibir_llamadas;
    private javax.swing.JComboBox l_tipoDocumento;
    private javax.swing.JComboBox l_vendedor;
    private javax.swing.JLabel lb_cliente_apellidos;
    private javax.swing.JLabel lb_cliente_preafiliado1;
    private javax.swing.JLabel lb_cliente_tipo_socio;
    private javax.swing.JLabel lb_error_icon;
    private com.comerzzia.jpos.gui.components.JMultilineLabel lb_error_ml;
    private javax.swing.JLabel lb_fechaNacimientoUltimoBebe1;
    private javax.swing.JLabel lb_fechaNacimientoUltimoBebe2;
    private javax.swing.JLabel lb_numeroAfiliacion;
    private javax.swing.JLabel lb_semanasEmbarazo1;
    private javax.swing.JLabel lb_semanasEmbarazo2;
    private javax.swing.JLabel lb_tarjeta;
    private javax.swing.JLabel lb_tipo_cliente;
    private javax.swing.JLabel lb_vendedor;
    private javax.swing.JLabel lbl_nombres;
    private javax.swing.JPanel menu_navegacion;
    private com.comerzzia.jpos.gui.modelos.OperadoresListRenderer operadoresListRenderer;
    private com.comerzzia.jpos.servicios.general.operadoresmoviles.OperadoresMoviles operadoresMoviles;
    private javax.swing.JPanel p_avisos;
    private javax.swing.JPanel p_datos_cliente;
    private javax.swing.JPanel p_ident_cliente;
    private javax.swing.JPanel p_publicidad;
    private com.comerzzia.jpos.servicios.login.Sesion sesion1;
    private javax.swing.JComboBox t_cliente_Ciudad;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_apellidos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_celular;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_direccion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_email;
    private javax.swing.JComboBox t_cliente_estadoCivil;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_fechaNacimiento;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_fechaNacimientoBebe;
    private javax.swing.JComboBox t_cliente_genero;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm t_cliente_info3;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm t_cliente_info4;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm t_cliente_informacion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_nombres;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm t_cliente_observaciones;
    private javax.swing.JComboBox t_cliente_operadorCelular;
    private javax.swing.JComboBox t_cliente_prefijoTelefonoParticular;
    private javax.swing.JComboBox t_cliente_prefijoTelefonoTrabajo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_semanasEmbarazo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_tel_particular;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cliente_tel_trabajo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_documento;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_numeroAfiliacion;
    private javax.swing.JOptionPane v_confirmaNuevoCliente;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
// </editor-fold>

    public void waitCursorBegin() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void waitCursorEnd() {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void iniciaNuevaVenta(Cliente cliente, Byte tipo) {
        if (!Sesion.getTienda().getCajaActiva().isEmiteFactura()) {
            ventana_padre.crearInformacion("La caja configurada no permite la emisión de facturas");
            return;
        }

        if (!Sesion.getCajaActual().isCajaParcialAbierta()) {
            ventana_padre.crearAdvertencia(Variables.getVariable(Variables.MENSAJE_AVISO_CAJA_CERRADA));
            try {
                String autorizador = ventana_padre.compruebaAutorizacion(Operaciones.GESTIONAR_CAJA);
                Sesion.setAutorizadorGestionCajas(autorizador);
                ventana_padre.addCierreCajaView();
                ventana_padre.showView("cierre_caja");
            } catch (SinPermisosException e) {
                log.debug("Cajero sin permisos para realizar la operación");
            } catch (Exception e) {
                ventana_padre.crearError(null);
            }
        } else {
            try {
                if (Sesion.getCajaActual().limiteDeRetiro()) {
                    ventana_padre.crearAdvertencia(Variables.getVariable(Variables.MENSAJE_AVISO_RETIRO));
                }
                //Cambio para que pida clave cuando es cliente comohogar
                Sesion.iniciaNuevoTicket(cliente);
                Sesion.getTicket().setVendedor((Vendedor) l_vendedor.getSelectedItem());
                String autorizador = null;
                Long tipoCliente = 1L;
                if ((TiposClientes.getInstancia().getTipoCliente(cliente.getTipoCliente())).getRequiereAutorizacionEnVenta()) {
                    String tc = null;
                    if (Objects.equals(TiposClientes.getInstancia().getTipoCliente(cliente.getTipoCliente()).getCodTipoCliente(), tipoCliente)) {
                        autorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_VENTA_TIPO_CLIENTE);
                        ServicioLogAcceso.crearAccesoLogAutorizarVentaTipoCliente(autorizador);
                        Sesion.getTicket().setAutorizadorVenta(autorizador);
                    } else {
                        tc = ventana_padre.crearVentanaTarjetaAfilado();
                        if (tc == null) {
                            autorizador = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_VENTA_TIPO_CLIENTE);
                            ServicioLogAcceso.crearAccesoLogAutorizarVentaTipoCliente(autorizador);
                            Sesion.getTicket().setAutorizadorVenta(autorizador);
                        }
                    }
                }
                if (Sesion.getUsuario().getUsuario().equals(cliente.getCodcli()) && (autorizador == null || autorizador.equals(cliente.getCodcli()))) {
                    boolean autorizado = false;
                    while (!autorizado) {
                        String autori = ventana_padre.compruebaAutorizacion(Operaciones.AUTORIZAR_VENTA_TIPO_CLIENTE);
                        if (!autori.equals(cliente.getCodcli())) {
                            autorizado = true;
                            ServicioLogAcceso.crearAccesoLogAutorizarVentaClienteCajero(autori);
                            Sesion.getTicket().setAutorizadorVenta(autori);
                        }
                    }
                }

                if (tipo == CLIENTEFINAL) {
                    this.ventana_padre.showView("ventas");
                } else if (tipo == CLIENTESELECCIONADO) {
                    this.ventana_padre.showView("ventas");
                } else if (tipo == OTROCLIENTE) {

                    //v_facturacion.setVisible(true);
                    FacturacionTicketBean ft = JPrincipal.crearVentanaFacturacion(false, Sesion.getTicket());
                    if (ft != null) {
                        lb_error_icon.setIcon(null);
                        lb_error_ml.setText("");
                        // navegamos a ventas
                        this.ventana_padre.showView("ventas");
                    }
                }
            } catch (SinPermisosException e) {
                log.debug("Usuario sin permisos para realizar la operación");
            } catch (ContadorException e) {
                log.error("Error al inicializar una nueva venta.", e);
                ventana_padre.crearError(e.getMessage());
            }
        }
        bloquearAcciones = false;
    }

    private void actualizaDatosCliente() {
        if (clienteConsultado != null) {
            t_cliente_apellidos.setText(clienteConsultado.getApellido());
            t_cliente_Ciudad.setSelectedItem(new Ciudad(clienteConsultado.getPoblacion()));
            t_cliente_direccion.setText(clienteConsultado.getDireccion());
            t_cliente_email.setText(clienteConsultado.getEmail());
            t_cliente_estadoCivil.setSelectedItem(clienteConsultado.getEstadoCivil());

            if (clienteConsultado.getFechaNacimiento() != null && (clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                t_cliente_fechaNacimiento.setText(formateadorFechaCorta.format(clienteConsultado.getFechaNacimiento()));
            } else {
                t_cliente_fechaNacimiento.setText("");
            }

            if (Sesion.isBebemundo()) {
                if (clienteConsultado.getFechaNacimientoUltimoHijo() != null && (clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                    t_cliente_fechaNacimientoBebe.setText(formateadorFechaCorta.format(clienteConsultado.getFechaNacimientoUltimoHijo()));
                } else {
                    t_cliente_fechaNacimientoBebe.setText("");
                }
            }

            if (clienteConsultado.getGenero() != null) {
                t_cliente_genero.setSelectedItem(clienteConsultado.getGenero().toString());
            } else {
                t_cliente_genero.setSelectedItem(0);
            }
            t_cliente_informacion.setText(clienteConsultado.getInfoExtra2());
            t_cliente_nombres.setText(clienteConsultado.getNombre());
            t_cliente_observaciones.setText(clienteConsultado.getObservaciones());
            t_cliente_operadorCelular.setSelectedItem(new OperadorMovil(clienteConsultado.getOperadorTlfnoMovil()));

            TipoClienteBean tipoCliente = TiposClientes.getInstancia().getTipoCliente(clienteConsultado.getTipoCliente());
            if (tipoCliente != null) {
                lb_tipo_cliente.setVisible(true);
                lb_tipo_cliente.setText("Tipo Cliente: " + tipoCliente.getDesTipoCliente());
            }

            if (clienteConsultado.isSocio()) {
                c_afiliarse.setVisible(false);

                lb_cliente_preafiliado1.setVisible(false);

                if (Sesion.isBebemundo()) {
                    lb_fechaNacimientoUltimoBebe1.setVisible(true);
                    lb_fechaNacimientoUltimoBebe2.setVisible(true);
                    lb_semanasEmbarazo1.setVisible(true);
                    lb_semanasEmbarazo2.setVisible(true);
                    t_cliente_fechaNacimientoBebe.setVisible(true);
                    t_cliente_semanasEmbarazo.setVisible(true);
                    t_cliente_fechaNacimientoBebe.addValidador(new ValidadorObligatoriedad(), this);
                    t_cliente_fechaNacimientoBebe.addValidador(new ValidadorFechaBebe(), this);
                    t_cliente_semanasEmbarazo.addValidador(new ValidadorEntero(1, 42), this);
                    Fecha fechaHoy = new Fecha();
                    Fecha fechaComponente = new Fecha(t_cliente_fechaNacimientoBebe.getText(), "dd-MMM-yyyy");
                    if (fechaComponente.getDate() != null) {
                        if (fechaComponente.antes(fechaHoy)) {
                            lb_fechaNacimientoUltimoBebe1.setText("El ultimo bebé");
                            lb_fechaNacimientoUltimoBebe2.setText("nació el:");
                        } else {
                            lb_fechaNacimientoUltimoBebe1.setText("El próximo bebé");
                            lb_fechaNacimientoUltimoBebe2.setText("nacerá el:");
                        }
                    }
                }

                lb_cliente_tipo_socio.setVisible(true);

                jLabel8.setVisible(true);
                TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(clienteConsultado.getTipoAfiliado());
                if (tipoAfiliado != null) {
                    lb_cliente_tipo_socio.setText(tipoAfiliado.getDesTipoAfiliado());
                    if (tipoAfiliado.getImagenTarjetaAfiliado() != null) {
                        lb_tarjeta.setIcon(tipoAfiliado.getImagenTarjetaAfiliado());
                    }
                }
                c_afiliarse.setSelected(true);

            } else {
                c_afiliarse.setVisible(true);
                if (clienteConsultado.getPreafiliado() == 'S') {
                    c_afiliarse.setSelected(true);
                    lb_cliente_preafiliado1.setVisible(true);
                    if (Sesion.isBebemundo()) {
                        lb_fechaNacimientoUltimoBebe1.setVisible(true);
                        lb_fechaNacimientoUltimoBebe2.setVisible(true);
                        lb_semanasEmbarazo1.setVisible(true);
                        lb_semanasEmbarazo2.setVisible(true);

                        t_cliente_fechaNacimientoBebe.setVisible(true);
                        t_cliente_semanasEmbarazo.setVisible(true);
                    }
                } else {
                    lb_cliente_preafiliado1.setVisible(true);
                    if (Sesion.isBebemundo()) {
                        lb_fechaNacimientoUltimoBebe1.setVisible(false);
                        lb_fechaNacimientoUltimoBebe2.setVisible(false);
                        lb_semanasEmbarazo1.setVisible(false);
                        lb_semanasEmbarazo2.setVisible(false);
                        t_cliente_fechaNacimientoBebe.setVisible(false);
                        t_cliente_semanasEmbarazo.setVisible(false);
                    }
                }
            }
            if (clienteConsultado.getTipoIdentificacion().equals("RUJ")) {
                lb_cliente_preafiliado1.setVisible(false);
                c_afiliarse.setVisible(false);
            }

            //t_cliente_prefijoTelefonoParticular.setSelectedItem("02");
            if (clienteConsultado.getTelefonoParticular() != null) {
                t_cliente_prefijoTelefonoParticular.setSelectedItem(clienteConsultado.getTelefonoParticular().substring(0, 2));
            }
            //t_cliente_prefijoTelefonoTrabajo.setSelectedItem("02");
            if (clienteConsultado.getFechaNacimientoUltimoHijo() != null && clienteConsultado.getFechaNacimientoUltimoHijo().after(new Date())) {
                t_cliente_semanasEmbarazo.setText("");

            }
            if (clienteConsultado.getSemanaEmbarazo() != null && (clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                t_cliente_semanasEmbarazo.setText("");
                if (!(clienteConsultado.getSemanaEmbarazo().intValue() > 42) || (clienteConsultado.getSemanaEmbarazo().intValue() < 1)) {
                    t_cliente_semanasEmbarazo.setText(clienteConsultado.getSemanaEmbarazo().toString());
                }
            }

            if (clienteConsultado.getTelefonoParticular() != null && clienteConsultado.getTelefonoParticular().length() == 9) {
                t_cliente_tel_particular.setText(clienteConsultado.getTelefonoParticular().substring(2, 9));
            }
            t_cliente_celular.setText(clienteConsultado.getTelefonoMovil());
            celularAnterior = clienteConsultado.getTelefonoMovil();
            t_cliente_info3.setText(clienteConsultado.getInfoExtra3());
            t_cliente_info4.setText(clienteConsultado.getInfoExtra4());
            if (clienteConsultado.getRecibirEmails() == 'S') {
                l_cliente_recibir_correos.setSelected(true);
            }
            if (clienteConsultado.getRecibirLlamadas() == 'S') {
                l_cliente_recibir_llamadas.setSelected(true);
            }

            t_documento.setText(clienteConsultado.getIdentificacion());

            if (clienteConsultado.getTipoIdentificacion().equals("CED")) {
                l_tipoDocumento.setSelectedIndex(0);
            } else if (clienteConsultado.getTipoIdentificacion().equals("RUN") && !clienteConsultado.getApellido().equals("Empresa")) {
                l_tipoDocumento.setSelectedIndex(1);
            } else if (clienteConsultado.getTipoIdentificacion().equals("RUJ") && clienteConsultado.getApellido().equals("Empresa")) {
                l_tipoDocumento.setSelectedIndex(2);
            } else if (clienteConsultado.getTipoIdentificacion().equals("PAS")) {
                l_tipoDocumento.setSelectedIndex(3);
            }
            lb_error_icon.setIcon(null);
            lb_error_ml.setText("");

            if (clienteConsultado.isListaNegra() || clienteConsultado.isTelefonoIncorrecto() || clienteConsultado.isEmailIncorrecto() || clienteConsultado.isCelularIncorrecto() || clienteConsultado.isDireccionIncorrecta()) {
                URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aviso.gif");
                lb_error_icon.setIcon(new ImageIcon(myurl));
                //lb_error_ml.setText("<html>");
                String infoAMostrar = "";

                if (clienteConsultado.isListaNegra()) {
                    //lb_error_ml.setText(lb_error_ml.getText() + "Atención cliente en lista negra. <P>");
                    infoAMostrar = infoAMostrar + "Atención cliente en lista negra. \n";
                }
                if (clienteConsultado.isDireccionIncorrecta()) {
                    //lb_error_ml.setText(lb_error_ml.getText() + "Teléfono incorrecto. <P>");
                    infoAMostrar = infoAMostrar + lb_error_ml.getText() + "Dirección incorrecta. ";
                }

                if (clienteConsultado.isTelefonoIncorrecto()) {
                    //lb_error_ml.setText(lb_error_ml.getText() + "Teléfono incorrecto. <P>");
                    infoAMostrar = infoAMostrar + lb_error_ml.getText() + "Teléfono incorrecto. ";
                }
                if (clienteConsultado.isEmailIncorrecto()) {
                    //lb_error_ml.setText(lb_error_ml.getText() + "Email incorrecto. <P>");
                    infoAMostrar = infoAMostrar + "Email incorrecto. ";
                }
                if (clienteConsultado.isCelularIncorrecto()) {
                    //lb_error_ml.setText(lb_error_ml.getText() + "Celular incorrecto. <P>");
                    infoAMostrar = infoAMostrar + "Celular incorrecto. ";
                }
                //lb_error_ml.setText(lb_error_ml.getText() + "</html>");
                lb_error_ml.setText(infoAMostrar);
            }

            t_numeroAfiliacion.setText(clienteConsultado.getCodigoTarjetaBabysClub());
            Fecha fechaHoy = new Fecha();

            if (Sesion.isBebemundo()) {
                Fecha fechaComponente = new Fecha(t_cliente_fechaNacimientoBebe.getText(), "dd-MMM-yyyy");
                if (fechaComponente.getDate() != null) {
                    if (fechaComponente.antes(fechaHoy)) {
                        lb_fechaNacimientoUltimoBebe1.setText("El ultimo bebé");
                        lb_fechaNacimientoUltimoBebe2.setText("nació el:");
                    } else {
                        lb_fechaNacimientoUltimoBebe1.setText("El próximo bebé");
                        lb_fechaNacimientoUltimoBebe2.setText("nacerá el :");
                    }
                }
            }
        }

    }

    private void reseteaDatosCliente() {
        clienteConsultado = null;
        b_m_seleccionar_cliente.setEnabled(false);
        b_m_facturar_otro_cliente.setEnabled(false);
        l_vendedor.setSelectedIndex(0);
        t_cliente_apellidos.setText("");
        t_cliente_Ciudad.setSelectedItem(new Ciudad(Sesion.getTienda().getAlmacen().getProvincia()));
        t_cliente_direccion.setText("");
        t_cliente_email.setText("");
        t_cliente_estadoCivil.setSelectedIndex(0);
        // Fechas pueden estar null = ""
        t_cliente_fechaNacimiento.setText("");
        t_cliente_fechaNacimientoBebe.setText("");
        t_cliente_genero.setSelectedIndex(0);
        t_cliente_informacion.setText("");
        t_cliente_nombres.setText("");
        t_cliente_observaciones.setText("");
        t_cliente_operadorCelular.setSelectedIndex(0);
        c_afiliarse.setSelected(false);

        lb_cliente_tipo_socio.setVisible(false);
        jLabel8.setVisible(false);
        lb_tipo_cliente.setVisible(false);
        t_cliente_prefijoTelefonoParticular.setSelectedItem(((Ciudad) t_cliente_Ciudad.getSelectedItem()).getPrefijo());
        t_cliente_semanasEmbarazo.setText("");
        t_cliente_tel_particular.setText("");
        t_cliente_celular.setText("");
        t_cliente_info3.setText("");
        t_cliente_info4.setText("");
        l_cliente_recibir_correos.setSelected(false);
        l_cliente_recibir_llamadas.setSelected(false);
        lb_error_icon.setIcon(null);
        lb_error_ml.setText("");
        jLabel1.setText("");
        t_cliente_fechaNacimientoBebe.removeValidadorObligatoriedad();
        lb_tarjeta.setIcon(null);
        eliminarPuntostotales();
    }

    private void resetearCamposIdentificacion() {
        l_vendedor.setSelectedIndex(0);
        l_tipoDocumento.setSelectedIndex(0);
        t_documento.setText("");
        t_numeroAfiliacion.setText("");
    }

    @Override
    public void iniciaVista() {
        //log.debug("Llamando a recolector de basura para liberar memoria...");
        //Runtime.getRuntime().gc();
        log.debug("**** Memoria máxima: " + Runtime.getRuntime().maxMemory() / 1048576 + " MB");
        log.debug("**** Memoria total: " + Runtime.getRuntime().totalMemory() / 1048576 + " MB");
        log.debug("**** Memoria libre: " + Runtime.getRuntime().freeMemory() / 1048576 + " MB");
        JPrincipal.setPanelActivo(this);
        jLabel6.setText("Ver. " + Constantes.VERSION_NUMERO_POS);

        // Si estamos en sukasa no veremos los campos referentes a embarazo
        if (Sesion.isSukasa()) {
            lb_semanasEmbarazo1.setVisible(false);
            lb_semanasEmbarazo2.setVisible(false);
            lb_fechaNacimientoUltimoBebe1.setVisible(false);
            lb_fechaNacimientoUltimoBebe2.setVisible(false);

            t_cliente_fechaNacimientoBebe.setVisible(false);
            t_cliente_fechaNacimientoBebe.setEnabled(false);
            t_cliente_semanasEmbarazo.setVisible(false);
            t_cliente_semanasEmbarazo.setEnabled(false);
        }

        habilitarCamposClienteNatural(true);
        reseteaDatosCliente();
        resetearCamposIdentificacion();
        setEnabledFormulario(false);

        b_m_confirmar_cliente.setEnabled(false);

        //Código para poder usar tabulador para cambiar foco en un textArea
        Set<KeyStroke> strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
        t_cliente_informacion.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_info3.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_info4.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_observaciones.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        t_cliente_informacion.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_info3.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_info4.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
        t_cliente_observaciones.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);

        addFunctionKeys();
        ActionMap am = jf_info_adicional.getRootPane().getActionMap();
        InputMap im = jf_info_adicional.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_cancelarActionPerformed(ae);
            }
        };

        im.put(esc, "IdentClientesc");
        am.put("IdentClientesc", listeneresc);

        if (clienteConsultado == null) {
            b_m_seleccionar_cliente.setEnabled(false);
            b_m_facturar_otro_cliente.setEnabled(false);
            b_m_editar_cliente.setEnabled(false);
            b_m_informacion_extra.setEnabled(false);
        } else {
            if (clienteConsultado.getTipoIdentificacion().equals("RUJ")) {
                lb_cliente_preafiliado1.setVisible(false);
                c_afiliarse.setVisible(false);
            }
            if (clienteConsultado.isSocio()) {
                lb_cliente_tipo_socio.setVisible(true);

                jLabel8.setVisible(true);
                TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(clienteConsultado.getTipoAfiliado());
                lb_cliente_tipo_socio.setText(tipoAfiliado.getDesTipoAfiliado());
                if (tipoAfiliado.getImagenTarjetaAfiliado() != null) {
                    lb_tarjeta.setIcon(tipoAfiliado.getImagenTarjetaAfiliado());
                }
            }

            TipoClienteBean tipoCliente = TiposClientes.getInstancia().getTipoCliente(clienteConsultado.getTipoCliente());
            if (tipoCliente != null) {
                lb_tipo_cliente.setVisible(true);
                lb_tipo_cliente.setText("Tipo Cliente: " + tipoCliente.getDesTipoCliente());
            }
        }

        //t_documento.requestFocus(true);
        t_documento.requestFocusInWindow();
        bloquearAcciones = false;
        //if (Sesion.cajaActual != null && Sesion.cajaActual.isCajaParcialAbierta() && Sesion.getCajaActual().limiteDeRetiro()) {
        //    ventana_padre.crearAdvertencia(Variables.getVariable(Variables.MENSAJE_AVISO_RETIRO));
        //}

    }

    private void validarFormulario() throws ValidationFormException {
        try {

            // Validamos primero el formulario porque la validación de fecha puede cambiar el valor del componente
            for (IValidableForm elem : formularioCliente) {
                //elem.requestFocus();
                elem.validar();
            }

            if (t_cliente_tel_particular.getText().isEmpty() && t_cliente_celular.getText().isEmpty()) {
                throw new ValidationFormException("Debe tener al menos un número de teléfono.");
            }

            if (Sesion.isBebemundo() && t_cliente_fechaNacimientoBebe != null && !t_cliente_fechaNacimientoBebe.getText().equals("")) {
                Fecha hoy = new Fecha();
                Fecha componente = new Fecha(t_cliente_fechaNacimientoBebe.getText(), "dd-MMM-yyyy");
                hoy.sumaMeses(10);
                if (!hoy.despuesOrEquals(componente)) {
                    throw new ValidationFormException("La fecha no puede ser mayor a 10 meses desde la fecha actual.");
                }
            }

            // ACTUALIZAMOS EL CLIENTE
            if (c_afiliarse.isSelected()) {
                clienteConsultado.setPreafiliado('S');
            } else {
                clienteConsultado.setPreafiliado('N');
            }
            if (l_tipoDocumento.getSelectedItem().equals("RUC Jurídico") && t_cliente_apellidos.getText().toString().isEmpty()) {
                clienteConsultado.setApellido(" ");
            }

            clienteConsultado.setApellido(t_cliente_apellidos.getText());

            if ((Ciudad) t_cliente_Ciudad.getSelectedItem() != null) {
                clienteConsultado.setPoblacion(((Ciudad) t_cliente_Ciudad.getSelectedItem()).getNombre());
            }
            clienteConsultado.setTelefonoMovil(t_cliente_celular.getText());
            clienteConsultado.setDireccion(t_cliente_direccion.getText());
            clienteConsultado.setEmail(t_cliente_email.getText());

            // ENVIAMOS LAS FECHAS
            if (!t_cliente_fechaNacimiento.getText().isEmpty()) {
                clienteConsultado.setFechaNacimiento(formateadorFechaCorta.parse(t_cliente_fechaNacimiento.getText()));
            } else {
                clienteConsultado.setFechaNacimiento(null);
            }

            // Fecha de nacimiento del último bebé
            if (Sesion.isBebemundo() && !t_cliente_fechaNacimientoBebe.getText().isEmpty() && (clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                clienteConsultado.setFechaNacimientoUltimoHijo(formateadorFechaCorta.parse(t_cliente_fechaNacimientoBebe.getText()));
            } else if (Sesion.isBebemundo() && this.tipoOperacionConfirmarCliente.equals("UPDATE") && !(clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                // no ponemos nada
            } else {
                clienteConsultado.setFechaNacimientoUltimoHijo(null);
            }

            clienteConsultado.setGenero(t_cliente_genero.getSelectedItem().toString().charAt(0));
            clienteConsultado.setInfoExtra2(t_cliente_informacion.getText());
            clienteConsultado.setNombre(t_cliente_nombres.getText());
            clienteConsultado.setInfoExtra1(t_cliente_observaciones.getText());
            if (t_cliente_operadorCelular.getSelectedItem() != null) {
                clienteConsultado.setOperadorTlfnoMovil(((OperadorMovil) t_cliente_operadorCelular.getSelectedItem()).getDesoperador());
            }
            // semanas de embarazo
            if (!t_cliente_semanasEmbarazo.getText().isEmpty() && (clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                clienteConsultado.setSemanaEmbarazo(new BigInteger(t_cliente_semanasEmbarazo.getText()));
            } else if (this.tipoOperacionConfirmarCliente.equals("UPDATE") && !(clienteConsultado.isSocio() || clienteConsultado.getPreafiliado() == 'S')) {
                // No ponemos nada
            } else {
                clienteConsultado.setSemanaEmbarazo(null);
            }
            clienteConsultado.setTelefonoParticular(t_cliente_prefijoTelefonoParticular.getSelectedItem() + t_cliente_tel_particular.getText());

            clienteConsultado.setInfoExtra3(t_cliente_info3.getText());
            clienteConsultado.setInfoExtra4(t_cliente_info4.getText());
            clienteConsultado.setRecibirEmails((l_cliente_recibir_correos.isSelected()) ? 'S' : 'N');
            clienteConsultado.setRecibirLlamadas((l_cliente_recibir_llamadas.isSelected()) ? 'S' : 'N');

            clienteConsultado.setCodigoTarjetaBabysClub(t_numeroAfiliacion.getText());
            String stipoDocumento = null;
            if (l_tipoDocumento.getSelectedIndex() == 0) {
                l_tipoDocumento.setSelectedIndex(0);
                stipoDocumento = "CED";
            } else if (l_tipoDocumento.getSelectedIndex() == 1) {
                l_tipoDocumento.setSelectedIndex(1);
                stipoDocumento = "RUN";
            } else if (l_tipoDocumento.getSelectedIndex() == 2) {
                l_tipoDocumento.setSelectedIndex(2);
                stipoDocumento = "RUJ";
            } else if (l_tipoDocumento.getSelectedIndex() == 3) {
                l_tipoDocumento.setSelectedIndex(3);
                stipoDocumento = "PAS";
            }
            clienteConsultado.setTipoIdentificacion(stipoDocumento);
        } catch (ValidationFormException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error en al validación del formulario de clientes", e);
            throw new ValidationFormException("No se pudo validar el formulario");
            //ventana_padre.crearError("");
        }
    }

    private void addFunctionKeys() {
        log.debug("Función de acciones de teclado");

        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        Action listenerf1 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    Process process = Runtime.getRuntime().exec(" sh service cups restart ");
                    InputStream i = process.getInputStream();
                    byte[] b = new byte[200];
                    i.read(b, 0, b.length);
                    log.info(new String(b));
                    log.info("Impresora: " + Sesion.getDatosConfiguracion().getCadenaImpresoraTicket() + " / " + Sesion.getDatosConfiguracion().getTipoImpresionTicket());
                    log.info("comando: " + "cupsenable " + Sesion.getDatosConfiguracion().getTipoImpresionTicket() + "/");
                    Thread.sleep(5000);
                    process = Runtime.getRuntime().exec("cupsenable " + Sesion.getDatosConfiguracion().getTipoImpresionTicket());
                    i = process.getInputStream();
                    byte[] b2 = new byte[200];
                    i.read(b2, 0, b2.length);
                    log.info(new String(b2));
                    log.info("Impresora habilitada");
                    ventana_padre.crearConfirmacion("Impresora habilitada.");
                } catch (Exception ex) {
                    log.error("No se pudo desencolar la impresora", ex);
                }

            }
        };
        addHotKey(f1, "IdentClientF1", listenerf1);

        KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        Action listenerf2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                if (!editandoFormulario) {
                    b_m_seleccionar_clienteActionPerformed(ae);
                }
            }
        };
        addHotKey(f2, "IdentClientF2", listenerf2);

//        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
//        Action listenerf3 = new AbstractAction() {
//
//            public void actionPerformed(ActionEvent ae) {
//                if (!editandoFormulario) {
//                    b_m_consumidor_finalActionPerformed(ae);
//                }
//
//            }
//        };
//        addHotKey(f3, "IdentClientF3", listenerf3);
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        Action listenerf4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                if (!editandoFormulario) {
                    b_m_facturar_otro_clienteActionPerformed(ae);
                }
            }
        };
        addHotKey(f4, "IdentClientF4", listenerf4);

        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        Action listenerf5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                b_m_informacion_extraActionPerformed(ae);

            }
        };
        addHotKey(f5, "IdentClientF5", listenerf5);

        KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        Action listenerf6 = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_m_editar_clienteActionPerformed(ae);
            }
        };
        addHotKey(f6, "IdentClientF6", listenerf6);

        KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        Action listenerf11 = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (editandoFormulario) {
                    b_m_confirmar_clienteActionPerformed(ae);
                }

            }
        };
        addHotKey(f11, "IdentClientF11", listenerf11);

        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        Action listenerf12 = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_m_menu_principalActionPerformed(ae);

            }
        };
        addHotKey(f12, "IdentClientF12", listenerf12);

    }

    private boolean compruebaRellenos() {
        boolean res = true;
        if ((t_documento.getText() == null || t_documento.getText().isEmpty()) && res) {
            res = false;
            t_documento.requestFocus();
            ventana_padre.crearError("El campo documento debe de estar relleno.");

        }

        if ((t_cliente_nombres.getText() == null || t_cliente_nombres.getText().isEmpty()) && res) {
            res = false;
            t_cliente_nombres.requestFocus();
            ventana_padre.crearError("El campo Nombre debe de estar relleno.");
        }
        if ((t_cliente_apellidos.getText() == null || t_cliente_apellidos.getText().isEmpty()) && res) {
            res = false;
            t_cliente_apellidos.requestFocus();
            ventana_padre.crearError("El campo Apellidos debe de estar relleno.");
        }
        if ((t_cliente_direccion.getText() == null || t_cliente_direccion.getText().isEmpty()) && res) {
            res = false;
            t_cliente_direccion.requestFocus();
            ventana_padre.crearError("El campo Dirección debe de estar relleno.");
        }
        if (t_cliente_tel_particular.getText() != null && !t_cliente_tel_particular.getText().isEmpty() && res) {
            try {
                Integer.parseInt(t_cliente_tel_particular.getText());
            } catch (NumberFormatException e) {
                log.debug("Falla la validación entera de telefono particular");
                res = false;
                t_cliente_tel_particular.requestFocus();
                ventana_padre.crearError("El campo Teléfono Particular debe de ser de tipo numérico.");
            }
        }
        if (!t_cliente_celular.getText().isEmpty() && res) {
            try {
                Integer.parseInt(t_cliente_celular.getText());
            } catch (NumberFormatException e) {
                log.debug("Falla la validación entera de telefono celular", e);
                res = false;
                t_cliente_celular.requestFocus();
                ventana_padre.crearError("El campo Celular debe de ser de tipo numérico.");
            }
        }

        return res;
    }

    /*  Este codigo es para poder modificar los colores de los bordes dependiendo de lo que necesitemos
     * Border old;
    
    @Override
    public void focusGained(FocusEvent fe) {
    Component cmpnt = fe.getComponent();
    if (cmpnt instanceof JTextFieldForm) {
    JTextFieldForm txt = (JTextFieldForm) cmpnt;
    if (old == null) {
    old = txt.getBorder();
    }
    
    Color border = txt.getBackground();
    txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), old));
    }
    }
    
    @Override
    public void focusLost(FocusEvent fe) {
    Component cmpnt = fe.getComponent();
    if (cmpnt instanceof JTextFieldForm) {
    JTextFieldForm txt = (JTextFieldForm) cmpnt;
    if (clienteConsultado != null && txt.getName().equals("nombre") && !txt.getText().equals(clienteConsultado.getNombre())) {
    txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), old));
    } else {
    txt.setBorder(old);
    }
    }
    
    
    }*/
    private void inicializaValidacion() {
        // APELLIDOS: CAMPO OBLIGATORIO: 1 a 100
        t_cliente_apellidos.addValidador(new ValidadorObligatoriedad(), this);
        t_cliente_apellidos.addValidador(new ValidadorTextoPuro(100), this);

        // NOMBRES: CAMPO OBLIGATORIO:
        t_cliente_nombres.addValidador(new ValidadorObligatoriedad(), this);
        t_cliente_nombres.addValidador(new ValidadorTextoPuro(100), this);

        // DOCUMENTO
        //t_numeroAfiliacion.addValidador(new ValidadorObligatoriedad(),this);
        //t_numeroAfiliacion.addValidador(new ValidadorTexto(new Integer(25), new Integer(25)), this);
        // NUMERO DE AFILIACION
        //t_documento.addValidador(new ValidadorObligatoriedad(),this);
        //t_documento.addValidador(new ValidadorTexto(new Integer(0), new Integer(45)), this);
        // DIRECCION: 
        t_cliente_direccion.setReformatea(true);
        t_cliente_direccion.addValidador(new ValidadorObligatoriedad(), this);
        t_cliente_direccion.addValidador(new ValidadorTextoPuro(new Integer(0), new Integer(400)), this);

        // EMAIL:
        t_cliente_email.setReformatea(true);
        t_cliente_email.addValidador(new ValidadorEmail(), this);
        t_cliente_email.addValidador(new ValidadorTexto(new Integer(0), new Integer(200)), this);
        // TELEFONO PARTICULAR
        //t_cliente_tel_particular.addValidador(new ValidadorObligatoriedad(), this);
        t_cliente_tel_particular.addValidador(new ValidadorTexto(new Integer(7), new Integer(7)), this);
        t_cliente_tel_particular.addValidador(new ValidadorEntero(), this);
        // TELEFONO TRABAJO
        //t_cliente_tel_trabajo.addValidador(new ValidadorTexto(new Integer(7), new Integer(7)), this);
        //t_cliente_tel_trabajo.addValidador(new ValidadorEntero(), this);
        //  CELULAR
        t_cliente_celular.addValidador(new ValidadorCelularEcuador(), this);
        // FECHA DE NACIMIENTO
        //t_cliente_fechaNacimiento.addValidador(new ValidadorFecha(new Date()), this);
        // FECHA NACIMIENTO ULTIMO BEBE
        if (Sesion.isBebemundo()) {
            t_cliente_fechaNacimientoBebe.addValidador(new ValidadorFecha(), this);
        }
        // SEMANAS DE EMBARAZO
        //t_cliente_semanasEmbarazo.addValidador(new ValidadorEntero(0, 40), this);
        // OTRA INFORMACION
        //t_cliente_observaciones.setFormatearTexto(true);
        t_cliente_observaciones.addValidador(new ValidadorTexto(new Integer(0), new Integer(400)), this);
        // OBSERVACIONES
        // t_cliente_info3.setFormatearTexto(true);
        t_cliente_info3.addValidador(new ValidadorTexto(new Integer(0), new Integer(400)), this);
        // INFO EXTRA1
        //t_cliente_info4.setFormatearTexto(true);
        t_cliente_info4.addValidador(new ValidadorTexto(new Integer(0), new Integer(400)), this);
        // INFO EXTRA2
        //t_cliente_informacion.setFormatearTexto(true);
        t_cliente_informacion.addValidador(new ValidadorTexto(new Integer(0), new Integer(400)), this);
    }

    private boolean compruebaDocumento() {
        log.info("comprueba documento");
        return ValidadorCedula.verificarIdEcuador((String) l_tipoDocumento.getSelectedItem(), t_documento.getText());
    }

    @Override
    public void addError(ValidationFormException e) {
        jLabel1.setText(e.getMessage());
        //ventana_padre.crearError(e.getMessage());
        //URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/error.gif");
        //lb_error_icon.setIcon(new ImageIcon(myurl));
    }

    @Override
    public void clearError() {

        jLabel1.setText("");
        //lb_error_icon.setIcon(null);

    }

    /**
     * Introducimos los elementos en el formulario de clienes Nos servirá para
     * habilitar y deshabilitar en bloque los elementos del formulario
     */
    private void creaFormularioCliente() {
        log.info("Crear formulario cliente");
        formularioCliente = new LinkedList();
        formularioCliente.add(t_cliente_apellidos);
        formularioCliente.add(t_cliente_celular);
        formularioCliente.add(t_cliente_direccion);
        formularioCliente.add(t_cliente_email);
        formularioCliente.add(t_cliente_fechaNacimiento);
        formularioCliente.add(t_cliente_fechaNacimientoBebe);
        formularioCliente.add(t_cliente_info3);
        formularioCliente.add(t_cliente_info4);
        formularioCliente.add(t_cliente_informacion);
        formularioCliente.add(t_cliente_nombres);
        formularioCliente.add(t_cliente_observaciones);
        formularioCliente.add(t_cliente_tel_particular);
        formularioCliente.add(t_cliente_semanasEmbarazo);
        formularioCliente.add(t_cliente_tel_trabajo);

    }

    private void validaComponenteFoco() {
        log.info("Función validaComponenteFoco()");
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focusOwner instanceof IValidableForm) {
            IValidableForm elementoActual = (IValidableForm) focusOwner;
            try {
                elementoActual.validar();
            } catch (ValidationFormException ex) {
                elementoActual.requestFocus();
            }
            return;
        }
    }

    private void accionConfirmarCliente() throws ValidationFormException, ClienteException {
        log.info("Acción Confirmar Cliente");
        if (compruebaRellenos()) {
            if (this.clienteConsultado != null) {
                // Hay que comprobar que el campo sobre el que esta el foco no de un error de validación
                validaComponenteFoco();
                // Modificación. Ahora validamos todo el formulario por si acaso
                validarFormulario();

                if (tipoOperacionConfirmarCliente.equals("CREATE")) {
                    // Manda el cliente a actualizar al servicio
                    clienteService.nuevoCliente(clienteConsultado);
                    // Muestra Un cuadro avisando de la creaciónd del cliente
                    ventana_padre.crearInformacion("Los datos del cliente han sido registrados correctamente.");
                    tipoOperacionConfirmarCliente = "UPDATE";
                    cerrarEdicionFormulario();
                    // MODIFICACIÓN PARA QUE SE SELECCIONE DIRECTAMENTE EL CLIENTE

                } else if (tipoOperacionConfirmarCliente.equals("UPDATE")) {
                    clienteService.modificaCliente(clienteConsultado, Sesion.getUsuario().getUsuario(), celularAnterior);
                    ventana_padre.crearInformacion("Los datos del cliente han sido modificados correctamente.");
                    cerrarEdicionFormulario();
                }
            }
        }
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        if (editandoFormulario) {
            t_cliente_nombres.requestFocus();
        } else {
            t_documento.requestFocus();
        }
    }

    public void creaVentanaBusquedaArticulos() {
        ventana_padre.creaVentanaBusquedaArticulos();
    }

    private void habilitarCamposClienteNatural(boolean habilitar) {
        lb_cliente_apellidos.setVisible(habilitar);
        t_cliente_apellidos.setVisible(habilitar);
    }

    /**
     * Función que personaliza los componentes mostrados para Sukasa
     */
    private void deshabilitaElementosSukasa() {
        lb_vendedor.setVisible(false);
        lb_numeroAfiliacion.setVisible(false);
        l_vendedor.setVisible(false);
        t_numeroAfiliacion.setVisible(false);
    }

    private void establecerPuntosTotales() {
        jLabel_puntos.setText("Puntos totales disponibles: " + clienteConsultado.obtenerPuntos().toString());
    }

    private void eliminarPuntostotales() {
        jLabel_puntos.setText("");
    }

}
