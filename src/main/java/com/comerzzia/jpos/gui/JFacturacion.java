/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFacturacion.java
 *
 * Created on 29-jul-2011, 13:50:05
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.entity.db.Ciudad;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.modelos.CiudadesKeySelectionManager;
import com.comerzzia.jpos.gui.validation.IFormTipoIdentificacion;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorEmail;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTelefono;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.ValidadorCedula;
import es.mpsistemas.util.log.Logger;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JDialog;

/**
 *
 * @author MGRI
 */
public class JFacturacion extends JPanelImagenFondo implements IVista, KeyListener, IViewerValidationFormError, IFormTipoIdentificacion {

    private static final Logger log = Logger.getMLogger(JFacturacion.class);
    private List<IValidableForm> formularioFacturacion;
    private JPrincipal ventana_padre = null;
    private JDialog contenedor = null;
    private FacturacionTicketBean objetofacturacion;
    private boolean cancelado = false;
    private boolean mostrado = false;
    private boolean nuevaFacturacion = false;
    private Cliente clienteBuscado;
    // Objeto ticket con el que trabajar
    private TicketS ticket;

    public FacturacionTicketBean getObjetofacturacion() {
        return objetofacturacion;
    }

    public void setObjetofacturacion(FacturacionTicketBean objetofacturacion) {
        this.objetofacturacion = objetofacturacion;
    }

    /** Creates new form JFacturacion */
    public JFacturacion() {
        super();
        initComponents();
        objetofacturacion = new FacturacionTicketBean();
        addFunctionKeys();
        inicializaValidacion();
        creaFormularioCliente();
        iniciaVista();
        
        t_fact_ident.requestFocus();
    }

    public JFacturacion(JPrincipal ventana_padre, JDialog contenedor) {
        super();
        try {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_cliente.png");

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        }
        catch (IOException ex) {
            log.error("Intentando obtener imagen de tema in pantalla Facturación ");
        }
        objetofacturacion = new FacturacionTicketBean();
        this.ventana_padre = ventana_padre;
        this.contenedor = contenedor;
        initComponents();

        addFunctionKeys();
        creaFormularioCliente();
        inicializaValidacion();

        iniciaVista();
        t_fact_apellidos.setFormatearTexto(true);
        t_fact_nombre.setFormatearTexto(true);
        t_fact_direccion.setFormatearTexto(true);
    }

    private void creaFormularioCliente() {
        formularioFacturacion = new LinkedList();
        formularioFacturacion.add(t_fact_apellidos);
        formularioFacturacion.add(t_fact_direccion);
        formularioFacturacion.add(t_fact_ident);
        formularioFacturacion.add(t_fact_nombre);
        formularioFacturacion.add(t_fact_telefono_particular);
        formularioFacturacion.add(t_fact_email);
    }

    private void inicializaValidacion() {
        log.info("Inicializando Validadores");
        t_fact_apellidos.addValidador(new ValidadorTexto(1, 60), this);
        t_fact_apellidos.addValidador(new ValidadorObligatoriedad(), this);
        t_fact_direccion.addValidador(new ValidadorTexto(1, 300), this);
        t_fact_direccion.addValidador(new ValidadorObligatoriedad(), this);
        t_fact_ident.addValidador(new ValidadorTexto(1, 20), this);
        t_fact_nombre.addValidador(new ValidadorTexto(0, 60), this);
        t_fact_nombre.addValidador(new ValidadorObligatoriedad(), this);
        t_fact_telefono_particular.addValidador(new ValidadorObligatoriedad(), this);
        t_fact_telefono_particular.addValidador(new ValidadorTelefono(), this);
        t_fact_email.addValidador(new ValidadorEmail(), this);
        t_fact_email.addValidador(new ValidadorTexto(new Integer(1), new Integer(200)), this);
        t_fact_email.addValidador(new ValidadorObligatoriedad(), this);
        t_fact_apellidos.setFormatearTexto(true);
        t_fact_nombre.setFormatearTexto(true);
        t_fact_direccion.setFormatearTexto(true);
        t_fact_ident.requestFocus();
    }

    private boolean validarFormulario() throws ValidationFormException {
        boolean valido = true;
        boolean mensaje = false;
        for (IValidableForm e : formularioFacturacion) {
            try {
                e.validar();
            }
            catch (ValidationFormException ex) {
                log.debug("Excepción de validación de formulario: " + ex.getMessage());
                throw ex;
            }
        }  

        return valido;

    }

    private void activarValidacion() {
        for (IValidableForm e : formularioFacturacion) {
            e.setValidacionHabilitada(true);
        }
    }

    private void desactivarValidacion() {
        for (IValidableForm e : formularioFacturacion) {
            e.setValidacionHabilitada(false);
        }
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

        v_factura_ciudades = Sesion.getDatosConfiguracion().getCiudades();
        ciudadesListRenderer1 = new com.comerzzia.jpos.gui.modelos.CiudadesListRenderer();
        p_publicidad = new javax.swing.JPanel();
        b_aceptar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        t_fact_nombre = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_fact_ident = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_fact_direccion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        l_fact_tipo_ident = new javax.swing.JComboBox();
        t_fact_telefono_particular = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel10 = new javax.swing.JLabel();
        t_fact_apellidos = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel11 = new javax.swing.JLabel();
        l_fact_ciudad = new javax.swing.JComboBox();
        lb_error = new javax.swing.JLabel();
        lb_email = new javax.swing.JLabel();
        t_fact_email = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();

        setMaximumSize(new java.awt.Dimension(400, 540));
        setMinimumSize(new java.awt.Dimension(400, 540));
        setPreferredSize(new java.awt.Dimension(400, 520));

        p_publicidad.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanel1.setPreferredSize(new java.awt.Dimension(366, 530));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Datos de Facturación");

        jLabel2.setDisplayedMnemonic('t');
        jLabel2.setLabelFor(l_fact_tipo_ident);
        jLabel2.setText("<html><u>T</u>ipo de Documento :</html>");

        jLabel3.setDisplayedMnemonic('d');
        jLabel3.setLabelFor(t_fact_ident);
        jLabel3.setText("<html><u>D</u>ocumento :</html>");

        jLabel7.setDisplayedMnemonic('l');
        jLabel7.setLabelFor(t_fact_telefono_particular);
        jLabel7.setText("<html>Te<u>l</u>éfono :</html>");

        jLabel4.setDisplayedMnemonic('n');
        jLabel4.setLabelFor(t_fact_nombre);
        jLabel4.setText("<html><u>N</u>ombre :</html>");

        jLabel5.setDisplayedMnemonic('r');
        jLabel5.setLabelFor(t_fact_direccion);
        jLabel5.setText("<html>Di<u>r</u>ección:</hml>");

        t_fact_nombre.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        t_fact_ident.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        t_fact_ident.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_fact_identKeyTyped(evt);
            }
        });

        t_fact_direccion.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        l_fact_tipo_ident.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CÉDULA", "RUC Natural", "RUC Jurídico", "PASAPORTE" }));

        t_fact_telefono_particular.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        jLabel10.setDisplayedMnemonic('o');
        jLabel10.setLabelFor(l_fact_ciudad);
        jLabel10.setText("<html>Pr<u>o</u>vincia:</html>");

        t_fact_apellidos.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        jLabel11.setDisplayedMnemonic('p');
        jLabel11.setLabelFor(t_fact_apellidos);
        jLabel11.setText("<html>A<u>p</u>ellidos :</html>");

        l_fact_ciudad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Quito" }));
        l_fact_ciudad.setKeySelectionManager(new CiudadesKeySelectionManager());
        l_fact_ciudad.setRenderer(ciudadesListRenderer1);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaCiudades}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE, v_factura_ciudades, eLProperty, l_fact_ciudad, "c1");
        bindingGroup.addBinding(jComboBoxBinding);

        lb_error.setForeground(new java.awt.Color(204, 0, 0));
        lb_error.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lb_email.setDisplayedMnemonic('e');
        lb_email.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_email.setLabelFor(t_fact_email);
        lb_email.setText("<html><u>E</u>mail :</html>");
        lb_email.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        t_fact_email.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lb_email, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(t_fact_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_fact_ident, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_fact_nombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_fact_apellidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_fact_direccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(t_fact_telefono_particular, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(l_fact_ciudad, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(l_fact_tipo_ident, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(41, 41, 41))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_fact_tipo_ident, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_ident, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_fact_ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_telefono_particular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fact_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_email, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 400, Short.MAX_VALUE)
                    .addComponent(p_publicidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 265, Short.MAX_VALUE)
                    .addComponent(p_publicidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 275, Short.MAX_VALUE)))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void b_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aceptarActionPerformed
        // Establecemos al cliente como el cliente consultado. Actualiza datos de cliente en el ticket  
        try{
            if(mostrado){
                if (validarFormulario()) {
                    try{
                        String tipo = getTipoIdentificacion();
                        String numIdenti = t_fact_ident.getText();
                        clienteBuscado = ClientesServices.getInstance().consultaClienteDoc(numIdenti, tipo);
                    } catch (ClienteException ex){
                        log.debug("Excepción creando el cliente: " + ex.getMessage());
                        ventana_padre.crearError(ex.getMessage()); 
                        return;
                    } catch (NoResultException ex){
                        recogeCampos();
                        try {
                            if (!compruebaDocumento()) {
                                ventana_padre.crearAdvertencia("El número de documento introducido no es " + l_fact_tipo_ident.getSelectedItem().toString().toLowerCase() + " válido");
                                return;
                            }                
                            ClientesServices.getInstance().nuevoCliente(clienteBuscado);
                        } catch (ClienteException ex1) {
                            log.debug("Excepción creando el cliente: " + ex1.getMessage());
                            ventana_padre.crearError(ex1.getMessage()); 
                            return;
                        }
                    }                    
                    cancelado = false;
                    recogeFormulario();
                    ticket.setFacturacion(objetofacturacion);
                    this.getContenedor().setVisible(false);
                    //limite de retiro

                }
            }
            else {
                ventana_padre.crearAdvertencia("Debe seleccionar algún cliente");
            }
        } catch(ValidationFormException e){
            addError(e);
        }
    }//GEN-LAST:event_b_aceptarActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // Cerramos el Jdialog
        for (IValidableForm e : formularioFacturacion) {
            e.setValidacionHabilitada(false);
        }
        t_fact_ident.requestFocus();
        reseteaFormulario();
        for (IValidableForm e : formularioFacturacion) {
            e.setValidacionHabilitada(true);
        }
        lb_error.setText("");
        this.getContenedor().setVisible(false);

        objetofacturacion = new FacturacionTicketBean();
        cancelado = true;

    }//GEN-LAST:event_b_cancelarActionPerformed

    private void t_fact_identKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_fact_identKeyTyped
        if(evt.getKeyChar() == '\n')
        try {
            String tipo = getTipoIdentificacion();
            String numIdenti = t_fact_ident.getText();
            if(numIdenti==null || numIdenti.isEmpty()) {    
                log.debug("No se puede introducir un cliente vacío o nulo");
                ventana_padre.crearError("No se puede introducir un cliente vacío");
                return;
            }              
            clienteBuscado = ClientesServices.getInstance().consultaClienteDoc(numIdenti, tipo);
            rellenaCampos();
            if(!clienteBuscado.isActivo()){
                lb_error.setText("El cliente no está activo.");
            }
            muestraCampos();
        }
        catch (ClienteException ex) {
            log.debug("Excepción consultando de cliente: " + ex.getMessage());
            ventana_padre.crearError(ex.getMessage());
        } catch (NoResultException ex) {
            if(!compruebaDocumento()){
                ventana_padre.crearAdvertencia("El número de documento introducido no es " + l_fact_tipo_ident.getSelectedItem().toString().toLowerCase() + " válido");
                return;
            }          
            ventana_padre.crearAdvertencia("No existen clientes para los datos introducidos, se va a crear un nuevo cliente");
            t_fact_nombre.setText("");
            t_fact_apellidos.setText("");
            t_fact_direccion.setText("");
            l_fact_ciudad.setSelectedItem(new Ciudad(Sesion.getTienda().getAlmacen().getProvincia()));
            t_fact_telefono_particular.setText("");
            t_fact_email.setText("");
            lb_error.setText("");            
            muestraCampos();   
        }
    }//GEN-LAST:event_t_fact_identKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private com.comerzzia.jpos.gui.modelos.CiudadesListRenderer ciudadesListRenderer1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox l_fact_ciudad;
    private javax.swing.JComboBox l_fact_tipo_ident;
    private javax.swing.JLabel lb_email;
    private javax.swing.JLabel lb_error;
    private javax.swing.JPanel p_publicidad;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_apellidos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_direccion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_email;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_ident;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_nombre;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_fact_telefono_particular;
    private com.comerzzia.jpos.servicios.general.ciudades.Ciudades v_factura_ciudades;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        cancelado = true; // Esta cancelado hasta que no se acepte
        lb_error.setText("");
        t_fact_ident.requestFocus();

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    public JPrincipal getVentana_padre() {
        return ventana_padre;
    }

    public void setVentana_padre(JPrincipal ventana_padre) {
        this.ventana_padre = ventana_padre;
    }

    public JDialog getContenedor() {
        return contenedor;
    }

    public void setContenedor(JDialog contenedor) {
        this.contenedor = contenedor;
    }

    /**
     * Recoge los elementos del formulario
     */
    private void recogeFormulario() {
        // Datos de facturación
        this.objetofacturacion.setNombre(t_fact_nombre.getText());
        this.objetofacturacion.setApellidos(t_fact_apellidos.getText());
        this.objetofacturacion.setDocumento(t_fact_ident.getText());
        this.objetofacturacion.setDireccion(t_fact_direccion.getText());
        this.objetofacturacion.setProvincia(((Ciudad) l_fact_ciudad.getSelectedItem()).getNombre());
        //this.objetofacturacion.setTelefono(t_fact_telefono.getText());
        this.objetofacturacion.setTelefono(t_fact_telefono_particular.getText());
        this.objetofacturacion.setEmail(t_fact_email.getText());

        String stipoDocumento = getTipoIdentificacion();
        this.objetofacturacion.setTipoDocumento(stipoDocumento);


    }

    private void addFunctionKeys() {
        // ENTER EN LOS BOTONES
        addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterBotones", listenerEnter);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_cancelarActionPerformed(ae);
            }
        };
        addHotKey(esc, "IdentClientesc", listeneresc);

        KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, InputEvent.ALT_MASK);
        Action listeneralt7 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                b_aceptarActionPerformed(ae);
            }
        };
        addHotKey(alt7, "IdentClientalt7", listeneralt7);
    }

    public void reseteaFormulario() {

        // Datos de facturación
        t_fact_nombre.setText("");
        t_fact_apellidos.setText("");
        t_fact_ident.setText("");
        t_fact_direccion.setText("");
        l_fact_ciudad.setSelectedItem(new Ciudad(Sesion.getTienda().getAlmacen().getProvincia()));
        t_fact_telefono_particular.setText("");
        t_fact_email.setText("");
        l_fact_tipo_ident.setSelectedIndex(0);
        ocultaCampos();
    }


    public boolean isCancelado() {
        return cancelado;
    }

    @Override
    public void addError(ValidationFormException e) {
        lb_error.setText(e.getMessage());
    }

    @Override
    public void clearError() {
        lb_error.setText("");
    }

    private boolean compruebaDocumento() {
        return ValidadorCedula.verificarIdEcuador((String) l_fact_tipo_ident.getSelectedItem(), t_fact_ident.getText());
    }

    @Override
    public String getTipoIdentificacion() {
        if (l_fact_tipo_ident.getSelectedIndex() == 0) {
            l_fact_tipo_ident.setSelectedIndex(0);
            return "CED";
        }
        else if (l_fact_tipo_ident.getSelectedIndex() == 1) {
            l_fact_tipo_ident.setSelectedIndex(1);
            return "RUN";
        }
        else if (l_fact_tipo_ident.getSelectedIndex() == 2) {
            l_fact_tipo_ident.setSelectedIndex(2);
            return "RUJ";
        }
        else if (l_fact_tipo_ident.getSelectedIndex() == 3) {
            l_fact_tipo_ident.setSelectedIndex(3);
            return "PAS";
        }
        return "";
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        t_fact_ident.requestFocus();
    }

    public void iniciaVista(boolean nuevaFacturacion, TicketS ticket) {
        this.nuevaFacturacion = nuevaFacturacion;
        cancelado = true; // Esta cancelado hasta que no se acepte
        this.ticket = ticket;
        // Desactivamos validación
        desactivarValidacion();
        if (ticket.getFacturacion() != null && !nuevaFacturacion && compruebaFacturacion()) {
            if (ticket.getFacturacion().getTipoDocumento().equals("CED")) {
                l_fact_tipo_ident.setSelectedIndex(0);
            }
            else if (ticket.getFacturacion().getTipoDocumento().equals("RUN")) {
                l_fact_tipo_ident.setSelectedIndex(1);
            }
            else if (ticket.getFacturacion().getTipoDocumento().equals("RUJ")) {
                l_fact_tipo_ident.setSelectedIndex(2);
            }
            else if (ticket.getFacturacion().getTipoDocumento().equals("PAS")) {
                l_fact_tipo_ident.setSelectedIndex(3);
            }
            t_fact_nombre.setText(ticket.getFacturacion().getNombre());
            t_fact_apellidos.setText(ticket.getFacturacion().getApellidos());
            t_fact_ident.setText(ticket.getFacturacion().getDocumento());
            t_fact_direccion.setText(ticket.getFacturacion().getDireccion());
            l_fact_ciudad.setSelectedItem(new Ciudad(ticket.getFacturacion().getProvincia()));
            t_fact_email.setText(ticket.getFacturacion().getEmail());
            t_fact_telefono_particular.setText(ticket.getFacturacion().getTelefono());
            muestraCampos();
        }
        else {        
            t_fact_nombre.setText("");
            t_fact_apellidos.setText("");
            t_fact_ident.setText("");
            t_fact_direccion.setText("");
            l_fact_ciudad.setSelectedItem(new Ciudad(Sesion.getTienda().getAlmacen().getProvincia()));
            t_fact_telefono_particular.setText("");
            t_fact_email.setText("");
            l_fact_tipo_ident.setSelectedIndex(0);
            lb_error.setText("");
            ocultaCampos();
        }
        
        iniciaFoco();

        // Reactivamos validación
        activarValidacion();

    }
    
    private void ocultaCampos() {
        mostrado = false;
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jLabel7.setVisible(false);
        jLabel10.setVisible(false);
        jLabel11.setVisible(false);
        lb_email.setVisible(false);
        t_fact_nombre.setVisible(false);
        t_fact_apellidos.setVisible(false);
        t_fact_direccion.setVisible(false);
        t_fact_email.setVisible(false);
        l_fact_ciudad.setVisible(false);
        t_fact_telefono_particular.setVisible(false);
    }
    
    private void muestraCampos() {
        mostrado = true;
        jLabel4.setVisible(true);
        jLabel5.setVisible(true);
        jLabel7.setVisible(true);
        jLabel10.setVisible(true);
        jLabel11.setVisible(true);
        lb_email.setVisible(true);
        t_fact_nombre.setVisible(true);
        t_fact_nombre.requestFocus(true);
        t_fact_apellidos.setVisible(true);
        t_fact_email.setVisible(true);
        t_fact_direccion.setVisible(true);
        l_fact_ciudad.setVisible(true);
        t_fact_telefono_particular.setVisible(true);
    }
    
    private void rellenaCampos() {
        t_fact_nombre.setText(clienteBuscado.getNombre());
        t_fact_apellidos.setText(clienteBuscado.getApellido());
        t_fact_email.setText(clienteBuscado.getEmail());
        t_fact_direccion.setText(clienteBuscado.getDireccion());
        l_fact_ciudad.setSelectedItem(new Ciudad(clienteBuscado.getPoblacion()));
        //Si el teléfono es distinto de null y su longitud es mayor que el prefijo
        if(clienteBuscado.getTelefono1()!=null && clienteBuscado.getTelefono1().length()>2){
            t_fact_telefono_particular.setText(clienteBuscado.getTelefono1());        
        } else {
            t_fact_telefono_particular.setText(clienteBuscado.getTelefonoMovil());
        }
    }
    
    private void recogeCampos() {
        clienteBuscado = new Cliente(t_fact_ident.getText(),getTipoIdentificacion());
        clienteBuscado.setIdTratImpuestos(1);
        clienteBuscado.setCodcli(t_fact_ident.getText());
        clienteBuscado.setNombre(t_fact_nombre.getText());
        clienteBuscado.setApellido(t_fact_apellidos.getText());
        clienteBuscado.setDireccion(t_fact_direccion.getText());
        clienteBuscado.setEmail(t_fact_email.getText());
        
        clienteBuscado.setPoblacion(((Ciudad)l_fact_ciudad.getSelectedItem()).getNombre());
        if(t_fact_telefono_particular.getText().length() == 9){
            clienteBuscado.setTelefono1(t_fact_telefono_particular.getText());       
        }
        if(t_fact_telefono_particular.getText().length() == 10){
            clienteBuscado.setTelefonoMovil(t_fact_telefono_particular.getText());       
        }        
    }    
    
    private boolean compruebaFacturacion(){
        return ticket.getFacturacion() != null 
                && ticket.getFacturacion().getNombre() != null 
                && !ticket.getFacturacion().getNombre().isEmpty() 
                && ticket.getFacturacion().getApellidos() != null 
                && !ticket.getFacturacion().getApellidos().isEmpty() 
                && ticket.getFacturacion().getDireccion() != null 
                && !ticket.getFacturacion().getDireccion().isEmpty() 
                && ticket.getFacturacion().getTelefono() != null 
                && !ticket.getFacturacion().getTelefono().isEmpty() 
                && ticket.getFacturacion().getEmail() != null 
                && !ticket.getFacturacion().getEmail().isEmpty();
    }
}
