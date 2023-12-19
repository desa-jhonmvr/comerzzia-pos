/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class SeleccionarInvitadoTableModel extends AbstractTableModel {
    private String[] columnNames = {"NOMBRE", "APELLIDOS","EMAIL","TELEFONO"};
    private List<ReservaInvitadoBean> data;    

    
    public SeleccionarInvitadoTableModel(List<ReservaInvitadoBean> data) {
        this.data=data;
    }

    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;

    }
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public ReservaInvitadoBean getValueAt(int row, int col) {
        return data.get(row);
    }

}