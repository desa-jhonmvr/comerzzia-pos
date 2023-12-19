/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;
import com.comerzzia.jpos.entity.db.MedioPagoCaja;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
/**
 *
 * @author MGRI
 */
public class MedioPagoKeySelectionManager implements JComboBox.KeySelectionManager {

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

        pattern = ("" + aKey).toUpperCase();


        // Busca hacia adelante en la slección. Optimización para busqueda por cadenas
        for (int i = selIx + 1; i < model.getSize(); i++) {
            String s = ((MedioPagoCaja) model.getElementAt(i)).getDesmedpag().toUpperCase();
            if (s.startsWith(pattern)) {
                return i;
            }
        }

        // Búsqueda de la cadena de selección desde atras hasta el actual
        for (int i = 0; i < selIx; i++) {
            if (model.getElementAt(i) != null) {
                String s = ((MedioPagoCaja) model.getElementAt(i)).getDesmedpag().toUpperCase();
                if (s.startsWith(pattern)) {
                    return i;
                }
            }
        }
        return -1;
    }
}

