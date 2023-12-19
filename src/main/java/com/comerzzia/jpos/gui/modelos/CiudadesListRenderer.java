/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.entity.db.Ciudad;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author MGRI
 */
public class CiudadesListRenderer extends DefaultListCellRenderer {
    
    @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Ciudad) {
                    Ciudad mec = (Ciudad) value;
                    setText(mec.getNombre());
                }
                return this;
            }
    
}
