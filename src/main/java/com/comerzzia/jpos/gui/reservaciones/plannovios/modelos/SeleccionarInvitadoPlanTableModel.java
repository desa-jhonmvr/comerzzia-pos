/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class SeleccionarInvitadoPlanTableModel extends AbstractTableModel {
    private String[] columnNames = {"TITULO","NOMBRE", "APELLIDOS","TELEFONO"};
    private List<InvitadoPlanNovio> data;    

    
    public SeleccionarInvitadoPlanTableModel(List<InvitadoPlanNovio> data) {
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
    public InvitadoPlanNovio getValueAt(int row, int col) {
        return data.get(row);
    }

}