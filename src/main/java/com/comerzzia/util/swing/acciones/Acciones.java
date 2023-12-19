/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.swing.acciones;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

/**
 *
 * @author MGRI
 */
public class Acciones {
    
    
        public static void crearAccionFocoTabla(JPanel panel, final JTable cmp) {
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK);
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
        Acciones.addHotKey(panel, ks, "SelecciondeLineaTabla", listenerk);
    }
        
    public static void addHotKey(JPanel panel, KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = panel.getActionMap();
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        actionMap.put(inputActionKey, listener);
    }


}
