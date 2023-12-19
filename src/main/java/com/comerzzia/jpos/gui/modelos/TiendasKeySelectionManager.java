/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.entity.db.Almacen;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author MGRI
 */
public class TiendasKeySelectionManager implements JComboBox.KeySelectionManager {

    long lastKeyTime = 0;
    String pattern = "";

    public int selectionForKey(char aKey, ComboBoxModel model) {
        // Find index of selected item
        int selIx = 01;
        Object sel = model.getSelectedItem();
        if (sel != null) {
            for (int i = 0; i < model.getSize(); i++) {
                if (sel.equals(model.getElementAt(i))) {
                    selIx = i;
                    break;
                }
            }
        }

        // 
        long curTime = System.currentTimeMillis();

        // Para hacer selecciones mediante cadena de pulsaciones
        /*
        if (curTime - lastKeyTime < 300) {
        pattern += ("" + aKey).toLowerCase();
        } else {
        pattern = ("" + aKey).toLowerCase();
        }
         */
        pattern = ("" + aKey).toUpperCase();

        // Guarda el tiempo para saber si hay que añadir a la cadena o no en la siguiente pulsación
        //lastKeyTime = curTime;

        // Busca hacia adelante en la slección. Optimización para busqueda por cadenas

        for (int i = selIx + 1; i < model.getSize(); i++) {
            String s = ((Almacen) model.getElementAt(i)).getDesalm().toUpperCase();
            if (s.startsWith(pattern)) {
                return i;
            }
        }

        // Búsqueda de la cadena de selección desde atras hasta el actual
        for (int i = 0; i < selIx; i++) {
            if (model.getElementAt(i) != null) {
                String s = ((Almacen) model.getElementAt(i)).getDesalm().toUpperCase();
                if (s.startsWith(pattern)) {
                    return i;
                }
            }
        }
        return -1;
    }
}



