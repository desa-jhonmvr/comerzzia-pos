/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;

import com.comerzzia.jpos.gui.IManejadorErrores;
import com.comerzzia.jpos.gui.JPrincipal;
import es.mpsistemas.util.log.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public abstract class JVentanaDialogo extends JPanelImagenFondo {

    private static final Logger log = Logger.getMLogger(JVentanaDialogo.class);
    private IManejadorErrores ventana_padre;
    protected JDialog contenedor;
    private String tramaTarjeta;

    public JVentanaDialogo() {
        registrarEventoCerrarSesion();
        registrarEventoBloqueoAplicacion();
        registrarEventoAyudaAplicacion();
        registrarEventoRegresoFoco();
        setTeclasNavegacionPorDefecto();
    }

    public JVentanaDialogo(IManejadorErrores ventana_padre, JDialog contenedor) {
        registrarEventoCerrarSesion();
        registrarEventoBloqueoAplicacion();
        registrarEventoAyudaAplicacion();
        registrarEventoRegresoFoco();

        //if (ventana_padre instanceof JPrincipal){
        this.ventana_padre = ventana_padre;
        this.contenedor = contenedor;
        setTeclasNavegacionPorDefecto();
        //}
    }

    public IManejadorErrores getVentana_padre() {
        return ventana_padre;
    }

    public void setVentana_padre(IManejadorErrores ventana_padre) {
        this.ventana_padre = ventana_padre;
    }

    public JDialog getContenedor() {
        return contenedor;
    }

    public void setContenedor(JDialog contenedor) {
        this.contenedor = contenedor;
    }

    /**
     * Funciones que añade un evento de lectura de tarjeta. a las pantallas que
     * lo necesiten
     */
    public Action listenerTarjeta = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent act) {
            accionLeerTarjetaVDPadre();
        }

    };

    protected void accionLeerTarjetaVDPadre() {

        // llamamos a la ventana que va a leer la tarjeta
        this.setTramaTarjeta(JPrincipal.crearVentanaLecturaTajeta());

        accionLeerTarjetaVD();

    }

    public void registrarEventoLeerTarjeta() {
        KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.SHIFT_DOWN_MASK); //% es SHIFT + 5
        addHotKey(keyEnter, "LecturaTarjeta", listenerTarjeta);
    }

    protected void addHotKey(KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = this.getActionMap();
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        actionMap.put(inputActionKey, listener);
    }

    protected void setTeclasNavegacionPorDefecto() {
        // ESC
        try {
//            KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
//            Action listenerescVD = new AbstractAction() {
//                public void actionPerformed(ActionEvent ae) {
//                    accionCancelar();
//                }
//            };
//            addHotKey(esc, "IdentClientesc", listenerescVD);
            registrarEventoEscapeAplicacion();
        } catch (Exception e) {
            log.error("Error registrando eventos para pantalla");
        }

        // ENTER
        /*
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Action listenerenter = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {                
                accionAceptar();
            }
        };
        addHotKey(enter, "IdentCliententer", listenerenter);
         */
        // ALT+A
        /*
        KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, InputEvent.ALT_MASK);
        Action listeneralt7 = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                accionAceptar();
            }
        };
        addHotKey(alt7, "IdentClientalt7", listeneralt7);
         */
    }

    public void cerrarVentana() {
        limpiarFormulario();
        contenedor.setVisible(false);
    }
    
    public abstract void accionAceptar();

    public void accionCancelar() {
        limpiarFormulario();
        cerrarVentana();
    }

    public abstract void limpiarFormulario();

    public abstract void accionLeerTarjetaVD();

    public String getTramaTarjeta() {
        return tramaTarjeta;
    }

    public void setTramaTarjeta(String tramaTarjeta) {
        this.tramaTarjeta = tramaTarjeta;
    }

    public JPrincipal getVentanaPadreJPrincipal() {

        if (ventana_padre instanceof JPrincipal) {
            return (JPrincipal) ventana_padre;
        } else {
            return null;
        }
    }

    @Override
    public void registrarEventoEscapeAplicacion() {
        try {
            KeyStroke keyESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            addHotKey(keyESC, "ESCAPEPOSVD", listenerEscVD);
        } catch (Exception e) {
            log.error("registrarEventoBloqueoAplicacion() - Error registrando evento de ESCAPE", e);
        }
    }

    // Acción listener a la tecla escape
    protected Action listenerEscVD = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent act) {
            accionCancelar();
        }
    };

}
