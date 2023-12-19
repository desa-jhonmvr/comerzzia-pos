/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TipoClienteBean;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author SMLM
 */
public class TipoClienteListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoClienteBean) {
            TipoClienteBean mec = (TipoClienteBean) value;
            setText(mec.getDesTipoCliente());
        }
        return this;
    }
}
