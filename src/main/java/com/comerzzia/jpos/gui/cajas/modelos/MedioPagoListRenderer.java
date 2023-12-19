/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.db.MedioPagoCaja;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author MGRI
 */
public class MedioPagoListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof MedioPagoCaja) {
            MedioPagoCaja mec = (MedioPagoCaja) value;
            setText(mec.getDesmedpag());
        }
        else if (value instanceof MedioPagoBean){
            MedioPagoBean mec = (MedioPagoBean) value;
            setText(mec.getDesMedioPago());
        }
        return this;
    }
}
