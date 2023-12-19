/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JLogin.java
 *
 * Created on 27-jun-2011, 15:52:19
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tramas.ParserTramaException;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.components.form.SubtleSquareBorder;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesCliente;
import com.comerzzia.jpos.servicios.login.InvalidLoginException;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.LoginException;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.util.Constantes;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public class JLogin extends JPanelImagenFondo implements IVista, KeyListener, FocusListener {

    /* Variables */
    private static final Logger log = Logger.getMLogger(JLogin.class);
    JPrincipal ventana_padre = null;

    /** Creates new form JLogin */
    public JLogin() {
        // super(); No llamamos a la superclase para evitar el evento bloqueo de la pantalla.
        initComponents();

    }

    // Constructor con referencia a su clase contenedora para el cambio de vista
    public JLogin(JPrincipal ventana_padre) {
        try {
            //super();
            URL myurl = null;
            try {
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                String ruta = "/skin/" + prefijo + "/" + prefijo + "_login.png";
                log.debug("Intentando obtener imagen de tema a partir de: " + ruta);
                myurl = this.getClass().getResource(ruta);
                this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
            }
            catch (Exception e) {
                log.error("Error obteniendo imágen de tema para pantalla de login a través de URL: " + myurl, e);
            }
            log.debug("Imagen de fondo cargada.");
            this.ventana_padre = ventana_padre;
            initComponents();
            URL iconurl;
            log.debug("Componentes iniciados en Jprincipal.");

            iconurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
            ImageIcon icon = new ImageIcon(iconurl);
            v_buscar_articulos.setIconImage(icon.getImage());
            // Lectura de Tarjeta
            v_lectura_tarjeta.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
            v_lectura_tarjeta.setResizable(false);
            v_lectura_tarjeta.setIconImage(icon.getImage());
            v_lectura_tarjeta.setLocationRelativeTo(null);
            p_lectura_tarjeta.setVentana_padre(ventana_padre);
            p_lectura_tarjeta.setContenedor(v_lectura_tarjeta);

            // borde redondeado para login
            jt_password1.setBorder(new SubtleSquareBorder(true));
            // Iniciamos la ventana de insertar apunte
            p_buscar_articulos.setVentana_padre(this);
            p_buscar_articulos.setContenedor(v_buscar_articulos);
            jt_password1.addKeyListener(this);
            addFuntionKeys();
            jt_password1.addFocusListener(this);
            jt_usuario1.addFocusListener(this);

            // ENTER A BOTONES OPCION 2 -> Asignando a cada formulario la acción
            addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterBotones", listenerEnter);

            //ENTER A BOTONES OPCION 1 -> Asignando la acción, que es generica a cada botón
            //jb_cancel1.addKeyListener(this.eventoEnter);
            //jt_usuario1.requestFocus();

            log.debug("Registrando eventos buscar y leer configuración.");
            registraEventoBuscar();
            registraEventoLeerConfiguracion();
            probarConexionBonosSupermaxi();

            jt_usuario1.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent fe) {
                    log.debug("FOCO HA IDO A CAMPO USUARIO DE LOGIN");
                }

                @Override
                public void focusLost(FocusEvent fe) {               
                }
            });
        }
        catch (Exception e) {
            log.error("Error construyendo pantalla de login: ", e);
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

        v_buscar_articulos = new javax.swing.JDialog();
        p_buscar_articulos = new JBuscar(this);
        v_lectura_tarjeta = new javax.swing.JDialog();
        p_lectura_tarjeta = new com.comerzzia.jpos.gui.JLecturaTarjeta();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jt_usuario1 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jt_password1 = new com.comerzzia.jpos.gui.components.form.JPasswordFieldForm();
        jl_error1 = new javax.swing.JLabel();
        b_consultaArticulos = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel9 = new javax.swing.JLabel();

        v_buscar_articulos.setMinimumSize(new java.awt.Dimension(1010, 630));
        v_buscar_articulos.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        v_buscar_articulos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        v_buscar_articulos.setResizable(false);

        p_buscar_articulos.setPreferredSize(new java.awt.Dimension(1010, 630));
        v_buscar_articulos.getContentPane().add(p_buscar_articulos, java.awt.BorderLayout.CENTER);

        v_lectura_tarjeta.setMinimumSize(new java.awt.Dimension(400, 150));

        p_lectura_tarjeta.setMinimumSize(new java.awt.Dimension(400, 118));

        javax.swing.GroupLayout v_lectura_tarjetaLayout = new javax.swing.GroupLayout(v_lectura_tarjeta.getContentPane());
        v_lectura_tarjeta.getContentPane().setLayout(v_lectura_tarjetaLayout);
        v_lectura_tarjetaLayout.setHorizontalGroup(
            v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, v_lectura_tarjetaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        v_lectura_tarjetaLayout.setVerticalGroup(
            v_lectura_tarjetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_lectura_tarjetaLayout.createSequentialGroup()
                .addComponent(p_lectura_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1024, 723));
        setMinimumSize(new java.awt.Dimension(1024, 723));
        setPreferredSize(new java.awt.Dimension(1024, 723));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(1094, 734));
        jPanel3.setMinimumSize(new java.awt.Dimension(1094, 734));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(1094, 734));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setDisplayedMnemonic('u');
        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setLabelFor(jt_usuario1);
        jLabel6.setText("Usuario");
        jLabel6.setToolTipText("");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, 119, 29));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setDisplayedMnemonic('c');
        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 153, 255));
        jLabel7.setLabelFor(jt_password1);
        jLabel7.setText("V 1.3.19");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 550, 50, 20));
        jLabel7.getAccessibleContext().setAccessibleName("V 1.3.11");

        jt_usuario1.setFont(new java.awt.Font("Comic Sans MS", 0, 20)); // NOI18N
        jt_usuario1.setName("tfUsuario"); // NOI18N
        jt_usuario1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_usuario1ActionPerformed(evt);
            }
        });
        jPanel3.add(jt_usuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 160, -1));

        jt_password1.setFont(new java.awt.Font("Comic Sans MS", 0, 20)); // NOI18N
        jt_password1.setName("tfContraseña"); // NOI18N
        jt_password1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_password1ActionPerformed(evt);
            }
        });
        jPanel3.add(jt_password1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 160, -1));

        jl_error1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jl_error1.setForeground(new java.awt.Color(255, 51, 0));
        jPanel3.add(jl_error1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 340, 20));

        b_consultaArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/lupa.png"))); // NOI18N
        b_consultaArticulos.setMnemonic('o');
        b_consultaArticulos.setText("Consulta de artículos");
        b_consultaArticulos.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        b_consultaArticulos.setMaximumSize(new java.awt.Dimension(179, 31));
        b_consultaArticulos.setMinimumSize(new java.awt.Dimension(179, 31));
        b_consultaArticulos.setNextFocusableComponent(jt_usuario1);
        b_consultaArticulos.setPreferredSize(new java.awt.Dimension(179, 31));
        b_consultaArticulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_consultaArticulosActionPerformed(evt);
            }
        });
        jPanel3.add(b_consultaArticulos, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 540, 210, 40));

        jb_ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_ok1.setMnemonic('a');
        jb_ok1.setText("Aceptar");
        jb_ok1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jb_ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_ok1ActionPerformed(evt);
            }
        });
        jb_ok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jb_ok1KeyPressed(evt);
            }
        });
        jPanel3.add(jb_ok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 150, 40));

        jb_cancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        jb_cancel1.setText("Cancelar");
        jb_cancel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jb_cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancel1ActionPerformed(evt);
            }
        });
        jPanel3.add(jb_cancel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 450, 150, 40));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setDisplayedMnemonic('c');
        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 153, 255));
        jLabel9.setLabelFor(jt_password1);
        jLabel9.setText("Contraseña");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 340, 119, 25));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jt_usuario1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_usuario1ActionPerformed
        jb_ok1ActionPerformed(evt);
    }//GEN-LAST:event_jt_usuario1ActionPerformed

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        try {
            jl_error1.setText("");
            Sesion.iniciaSesion(jt_usuario1.getText(), jt_password1.getText());
            ventana_padre.compruebaAutorizacion(Operaciones.EJECUTAR);
            resetearFormulario();
            desregistraEventoLeerConfiguracion();
            if (Sesion.getUsuario().isClaveCaducada() || Sesion.getUsuario().isClaveCortaCaducada() || Sesion.getUsuario().isPrimerAcceso()) {
                String msg = Sesion.getUsuario().getMensajeClaveCaducada();
                ventana_padre.crearAdvertencia(msg);
                ventana_padre.showView("menu-perfil");
            }
            else {
                ventana_padre.showView("ident-cliente");
            }
            ventana_padre.mostrarPie();
        }
        catch (InvalidLoginException e) {
            jl_error1.setText(e.getMessage());
        }
        catch (LoginException e) {
            log.error("Error intentando acceder al sistema: " + e.getMessage(), e);
            jl_error1.setText("En estos momentos no es posible acceder al sistema");
        }
        catch (SinPermisosException e) {
            ventana_padre.crearSinPermisos("No tiene permisos para ejecutar la aplicación");
        }


}//GEN-LAST:event_jb_ok1ActionPerformed

    private void b_consultaArticulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_consultaArticulosActionPerformed
        // Carga en inicio la pantalla de busqueda de articulos sin login previo
        accionConsultaArticulos();
}//GEN-LAST:event_b_consultaArticulosActionPerformed

    private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
        resetearFormulario();
    }//GEN-LAST:event_jb_cancel1ActionPerformed

    private void jt_password1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_password1ActionPerformed
        jb_ok1ActionPerformed(evt);
    }//GEN-LAST:event_jt_password1ActionPerformed

    private void jb_ok1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_ok1KeyPressed
        if (evt.getKeyChar() == '\n' && evt.getComponent() instanceof JButtonForm) {
            ((JButtonForm) evt.getComponent()).doClick(0);
        }

    }//GEN-LAST:event_jb_ok1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_consultaArticulos;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_cancel1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_ok1;
    private javax.swing.JLabel jl_error1;
    private com.comerzzia.jpos.gui.components.form.JPasswordFieldForm jt_password1;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm jt_usuario1;
    private com.comerzzia.jpos.gui.JBuscar p_buscar_articulos;
    private com.comerzzia.jpos.gui.JLecturaTarjeta p_lectura_tarjeta;
    private javax.swing.JDialog v_buscar_articulos;
    private javax.swing.JDialog v_lectura_tarjeta;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciaVista() {
        this.jt_usuario1.setText("");
        this.jt_password1.setText("");
        jLabel7.setText(Constantes.VERSION_NUMERO_POS);
        jl_error1.setText("");

        if (Sesion.config.isModoDesarrollo()) {
            this.jt_usuario1.setText(Sesion.config.getUsuarioModoDesarrollo());
            this.jt_password1.setText(Sesion.config.getPasswordModoDesarrollo());
        }
        jt_usuario1.requestFocus();
        registrarEventoLeerTarjeta();
    }

    public void resetearFormulario() {
        this.jt_usuario1.setText("");
        this.jt_password1.setText("");
        jt_usuario1.requestFocus();
    }

    /* Acciones de teclado */
    @Override
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            jb_cancel1ActionPerformed(null);
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void addFuntionKeys() {
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                jb_cancel1ActionPerformed(ae);
            }
        };
        addHotKey(esc, "IdentClientesc", listeneresc);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof JTextFieldForm) {
            JTextFieldForm txt = (JTextFieldForm) cmpnt;
            txt.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
    }

    @Override
    public void iniciaFoco() {
        log.info("Iniciando Foco");
        jt_usuario1.requestFocus();
    }

    public void accionConsultaArticulos() {
        if (JPrincipal.getVistaActual() instanceof JLogin || JPrincipal.getVistaActual() instanceof JIdentCliente || JPrincipal.getVistaActual() instanceof JReservacionesCliente) {
            p_buscar_articulos.iniciaVista();
            v_buscar_articulos.setLocationRelativeTo(null);
            v_buscar_articulos.setVisible(true);
        }
    }

    @Override
    public void creaVentanaBusquedaArticulos() {
        ventana_padre.creaVentanaBusquedaArticulos();
    }

    private void registraEventoLeerConfiguracion() {
        Action listenerBuscar = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent act) {
                if (JPrincipal.getVistaActual() instanceof JLogin) {
                    try {
                        LecturaConfiguracion.leerDatosSesion(false);
                        
                        ventana_padre.crearConfirmacion("Se recargaron los datos de configuración");
                    }
                    catch (Exception ex) {
                        ventana_padre.crearError("Error al leer la cofiguración de la aplicación");
                    }
                }
            }
        };
        KeyStroke keyBuscar = KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.ALT_MASK); //10 es CTRL + ALT
        addHotKey(keyBuscar, "ReleerConfigPOS", listenerBuscar);
    }

      private void probarConexionBonosSupermaxi() {
        Action listenerBuscar1 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent act) {
                 String mensajeRecibido = null;
        DataInputStream entrada;
        DataOutputStream salida;
        byte buffer[] = new byte[512];
    
        Socket socket = null;
       
            //El formato de la cadena es host:puerto
                 try {
                    socket = new Socket("192.168.36.25", 6019);
                     ventana_padre.crearConfirmacion("Conexion correcta");
                 } catch (Exception ex) {
                    ex.printStackTrace();
                     ventana_padre.crearConfirmacion("error " + ex.getMessage());
                 }finally{
                    if(socket != null){
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            java.util.logging.Logger.getLogger(JLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }                
                 }
          
            }
        };
        KeyStroke keyBuscar1 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.ALT_MASK); //10 es CTRL + ALT
        addHotKey(keyBuscar1, "ProbarConexionSMX", listenerBuscar1);
    }
      
      
    private void desregistraEventoLeerConfiguracion() {
        removeHotKey(KeyEvent.VK_5, InputEvent.SHIFT_DOWN_MASK, "LecturaTarjetaUsuInicio");
    }

    private void accionLeerTarjetaUsu() {
        if (JPrincipal.getVistaActualS().equals(JPrincipal.VISTA_LOGIN)) {
            p_lectura_tarjeta.iniciaVista();
            v_lectura_tarjeta.setVisible(true);
            if (p_lectura_tarjeta.getBine() != null && !p_lectura_tarjeta.getBine().isEmpty()) {
                eventoLecturaTarjetaUsu(p_lectura_tarjeta.getBine());
            }
        }
    }
    public Action listenerTarjeta = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
            accionLeerTarjetaUsu();
        }
    };

    public void registrarEventoLeerTarjeta() {
        KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.SHIFT_DOWN_MASK); //% es SHIFT + 5
        addHotKey(keyEnter, "LecturaTarjetaUsuInicio", listenerTarjeta);
    }
   
    private void eventoLecturaTarjetaUsu(String tarjetaLeida) {
        try{
            Pattern patron = Pattern.compile("%[a-zA-Z0-9]*_ñ[a-zA-Z0-9]*_");
            Matcher matcher = patron.matcher(tarjetaLeida);           
            if(!matcher.find()){
                throw new ParserTramaException("Formato de cadena incorrecto");
            }   
            String[] cadenas = tarjetaLeida.split("_");
            String id = cadenas[0].substring(1);
            if(!id.equals(cadenas[1].substring(1))){
                throw new ParserTramaException("Las cadenas de la tarjeta no son iguales");
            }
            accionLoginTarjeta(id);
        } catch (Exception ex) {
            log.debug("No se pudo leer la tarjeta", ex);
            jl_error1.setText("Error leyendo tarjeta");            
        }      
    }

    private void accionLoginTarjeta(String tident) {
        try {
            jl_error1.setText("");
            Sesion.iniciaSesion(tident);
            ventana_padre.compruebaAutorizacion(Operaciones.EJECUTAR);
            resetearFormulario();
            desregistraEventoLeerConfiguracion();
            if (Sesion.getUsuario().isClaveCaducada() || Sesion.getUsuario().isClaveCortaCaducada() || Sesion.getUsuario().isPrimerAcceso()) {
                String msg = Sesion.getUsuario().getMensajeClaveCaducada();
                ventana_padre.crearAdvertencia(msg);
                ventana_padre.showView("menu-perfil");
            }
            else {
                ventana_padre.showView("ident-cliente");
            }
            ventana_padre.mostrarPie();
        }
        catch (InvalidLoginException e) {
            jl_error1.setText(e.getMessage());
        }
        catch (LoginException e) {
            log.error("Error intentando acceder al sistema: " + e.getMessage(), e);
            jl_error1.setText("En estos momentos no es posible acceder al sistema");
        }
        catch (SinPermisosException e) {
            ventana_padre.crearSinPermisos("No tiene permisos para ejecutar la aplicación");
        }
    }
}