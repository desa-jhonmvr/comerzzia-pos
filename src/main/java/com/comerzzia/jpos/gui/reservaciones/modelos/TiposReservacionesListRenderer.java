/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author MGRI
 */
public class TiposReservacionesListRenderer extends DefaultListCellRenderer {
    
    @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ReservaTiposBean) {
                    ReservaTiposBean mec = (ReservaTiposBean) value;
                    setText(mec.getDesTipo());
                }
                return this;
            }
    
}
