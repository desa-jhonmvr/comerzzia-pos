/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;

import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JBloqueo;
import com.comerzzia.jpos.gui.JPie;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.Contador;
import es.mpsistemas.util.log.Logger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

/**
 *
 * @author MGRI
 */
public class JPanelImagenFondo extends JPanel{
    
    private boolean repintarImagenFondo = true;
    private Image imagenFondo = null;
    protected static SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd-MMM-yyyy");
    protected static SimpleDateFormat formateadorFecha2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final Logger log = Logger.getMLogger(JPanelImagenFondo.class);


    public JPanelImagenFondo() {
        if (!(this instanceof  JPie)){
            registrarEventoCerrarSesion();
            registrarEventoBloqueoAplicacion();
            registrarEventoAyudaAplicacion();
            registrarEventoRegresoFoco();
            registrarEventoEscapeAplicacion();
            
        }
        else{
            addKeyListener(listenerContador);
        }
    }
    
    /**
     * Construnctor creado para los paneles con fondo, los cuales no deben ignorar
     * @param filtraEventoEscape 
     */
    public JPanelImagenFondo(boolean filtraEventoEscape) {
        if (! filtraEventoEscape){
            // no vamos a llamarlo con booleano a false
        }
        registrarEventoCerrarSesion();
        registrarEventoBloqueoAplicacion();
        registrarEventoAyudaAplicacion();
        registrarEventoRegresoFoco();   
    }

    // Acción listener a la tecla escape
    protected Action listenerEsc = new AbstractAction() {        
        @Override
        public void actionPerformed(ActionEvent act) {
            log.debug(" Consumimos evento tecla ESC en panel de tipo imagen fondo");            
        }
    };
    
    // Acción para enter por defecto para panel
    protected static Action listenerEnter = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner instanceof JButton) {
                ((JButton) focusOwner).doClick(0);
            } else if (focusOwner instanceof JCheckBox) {
                ((JCheckBox) focusOwner).doClick(0);
            }
            JPrincipal.escapeVentanaAyuda();
        }
    };
    public Action listenerBloqueo = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
            if (Sesion.getUsuario() != null) {
                log.debug("actionPerformed() - Se ha pulsado tecla de bloqueo de pantalla. Bloqueando POS...");
                if (!JPrincipal.isRecuentoVisible()){
                    JDialog v_bloqueo = creaPanelDeBloqueo();
                    v_bloqueo.setVisible(true);
                }
                else{
                    log.debug("Pantalla de recuento activa. No permitimos bloqueo de pantalla.");
                }
            }
        }
    };
    
    
    public Action listenerAyuda = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            log.debug("actionPerformed() - Se ha pulsado tecla de pantalla de ayuda.");
            JPrincipal.crearVentanaAyuda();
            
        }
    };
    
    public Action listenerCerrarSesion = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            tratarEventoCierreSesion();
        }
    };
    
    public void tratarEventoCierreSesion(){
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        if (inputMap.get(f12)==null){
            return;
        }
        log.debug("Hacemos logout");
        Sesion.cerrarSesion();
        JPrincipal.getInstance().showView("login");
        
        
    }
            
    
    
    public Action listenerFocoReturn = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
                log.debug("actionPerformed() - Se ha pulsado tecla de retorno de foco.");
                if (JPrincipal.getPopupActivo() != null){
                    log.debug("actionPerformed() - Popup modal activo. Retornamos foco al popup...");
                    JPrincipal.getPopupActivo().requestFocus();
                }
                else if (JPrincipal.getPanelActivo() != null){
                    log.debug("actionPerformed() - Panel activo. Moviendo foco a panel: " + JPrincipal.getPanelActivo());
                    JPrincipal.getPanelActivo().requestFocus();
                    if (JPrincipal.getPanelActivo() instanceof IVista){
                        ((IVista)JPrincipal.getPanelActivo()).iniciaFoco();
                    }
                }
        }
    };
    
    
    public KeyListener listenerContador=new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            Contador.setContador(0);
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };



    public JDialog creaPanelDeBloqueo() {
        Contador.setBloqueado(true);
        JPrincipal.getInstance().cerrarMenu();
        JDialog v_bloqueo = new javax.swing.JDialog();
        v_bloqueo.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK).getKeyCode()) {
                    // No hacemos nada ante un ALT+F4
                }
            }
        });
        v_bloqueo.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);
        JBloqueo p_bloqueo = new JBloqueo();
        p_bloqueo.setContenedor(v_bloqueo);

        v_bloqueo.setAlwaysOnTop(true);
        v_bloqueo.setBackground(new java.awt.Color(0, 0, 204));
        v_bloqueo.setMinimumSize(new java.awt.Dimension(1024, 768));
        v_bloqueo.setModal(true);
        v_bloqueo.setResizable(false);
        v_bloqueo.setUndecorated(true);
        v_bloqueo.setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_bloqueo.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        v_bloqueo.setLocationRelativeTo(null);
        javax.swing.GroupLayout v_menuLayout = new javax.swing.GroupLayout(v_bloqueo.getContentPane());
        v_bloqueo.getContentPane().setLayout(v_menuLayout);
        v_menuLayout.setHorizontalGroup(
                v_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(p_bloqueo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        v_menuLayout.setVerticalGroup(
                v_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(p_bloqueo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        return v_bloqueo;
    }
    

    public void registraEventoEnterBoton() {
        KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        addHotKey(keyEnter, "EnterBotones", listenerEnter);
    }

    
    public void registrarEventoEscapeAplicacion() {
        try{
            KeyStroke keyESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); 
            addHotKey(keyESC, "ESCAPEPOS", listenerEsc);
        }
        catch(Exception e){            
            log.error("registrarEventoBloqueoAplicacion() - Error registrando evento de ESCAPE",e);
        }
    }
    
    
    public void registrarEventoBloqueoAplicacion() {
        try{
            KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_X, 10); //10 es CTRL + ALT
            addHotKey(keyEnter, "BloqueoPOS", listenerBloqueo);
        }
        catch(Exception e){            
            log.error("registrarEventoBloqueoAplicacion() - Error registrando evento de bloqueo",e);
        }
    }
    
    public void registrarEventoCerrarSesion(){
        try{
            KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK);
            addHotKey(keyEnter, "cerrarSesionPOS", listenerCerrarSesion);
        }
        catch(Exception e){
            log.error("registrarEventoCerrarSesion() - Error registrando evento de cerrar sesión");
            
        }
    }
    
    public void registrarEventoAyudaAplicacion(){
        try{
            KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_T,10);//10 es CTRL + ALT
            addHotKey(keyEnter, "AyudaPOS", listenerAyuda);
        }
        catch(Exception e){
            log.error("registrarEventoAyudaAplicacion() - Error registrando evento de ayuda", e);
        }
    }

    public void registrarEventoRegresoFoco() {
        try{
            KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.ALT_DOWN_MASK); //10 es CTRL + ALT
            addHotKey(keyEnter, "RegresoFoco", listenerFocoReturn);
        }
        catch(Exception e){            
            log.error("registrarEventoRegresoFoco() - Error registrando evento de regreso de foco ",e);
        }
    }
    
    
    public void setImagenFondo(String tema, String imagen) {
        try {
            String prefijo = tema;
            URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_" + imagen);

            this.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
        } catch (IOException ex) {
        }
    }

    public void setImagenFondo(File file) throws IOException {
        if (file == null) {
            imagenFondo = null;
        } else {
            setImagenFondo(ImageIO.read(file));
        }
    }

    public void setImagenFondo(URL url) throws IOException {
        if (url == null) {
            imagenFondo = null;
        } else {
            setImagenFondo(ImageIO.read(url));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getImagenFondo() != null) {
            /*if (this instanceof JIdentCliente) {
            if (repintarImagenFondo) {
            g.drawImage(getImagenFondo(), 0, 0, null);
            repintarImagenFondo = false;
            }
            }
            else {*/
            g.drawImage(getImagenFondo(), 0, 0, null);
            //}
        }
    }

    public Image getImagenFondo() {
        return imagenFondo;
    }

    public void setImagenFondo(Image imagenFondo) {
        this.imagenFondo = imagenFondo;
    }

    protected void crearAccionFoco(JPanelImagenFondo panel, final JComponent cmp, int tecla, int modificador) {
        KeyStroke ks = KeyStroke.getKeyStroke(tecla, modificador);
        Action listenerk = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                cmp.requestFocus();
            }
        };
        addHotKey(panel, ks, "SelecionComponente", listenerk);
    }

    protected void crearAccionFocoTabla(JPanelImagenFondo panel, final JTable cmp, int tecla, int modificador) {
        KeyStroke ks = KeyStroke.getKeyStroke(tecla, modificador);
        Action listenerk = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (cmp.getRowCount() >= 0) {
                    ListSelectionModel selectionModel = cmp.getSelectionModel();
                    selectionModel.setSelectionInterval(0, 0);
                }
                cmp.requestFocus();
            }
        };
        addHotKey(panel, ks, "SelecciondeLineaTabla", listenerk);
    }

    protected void addHotKey(JPanelImagenFondo panel, KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = panel.getActionMap();
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        actionMap.put(inputActionKey, listener);
    }

    protected void addHotKey(KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        
        actionMap.put(inputActionKey, listener);
    }
    
    public void removeHotKey(int tecla,int modificador,String nombre){
        KeyStroke keyARetirar = KeyStroke.getKeyStroke(tecla, modificador); //10 es CTRL + ALT       
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.remove(keyARetirar);
        actionMap.remove(nombre);              
    }

    public void setRepintarImagenFondo(boolean repintarImagenFondo) {
        this.repintarImagenFondo = repintarImagenFondo;
    }

    public void registraEventoBuscar() {
        Action listenerBuscar = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent act) {
                creaVentanaBusquedaArticulos();
            }
        };
        KeyStroke keyBuscar = KeyStroke.getKeyStroke(KeyEvent.VK_B, 10); //10 es CTRL + ALT
        addHotKey(keyBuscar, "BusquedaPOS", listenerBuscar);

    }

    public void retiraEventoBuscar(){
        KeyStroke keyBuscar = KeyStroke.getKeyStroke(KeyEvent.VK_B, 10); //10 es CTRL + ALT       
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.remove(keyBuscar);
        actionMap.remove("BusquedaPOS");
              
    }
    
    public void creaVentanaBusquedaArticulos() {
    }

    public void dispose(){
        imagenFondo = null;
    }
    
}
