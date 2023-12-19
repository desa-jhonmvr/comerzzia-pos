/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.guiasremision.modelos;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class ArticulosGuiaRemisionTableModel extends AbstractTableModel {
    private String[] columnNames = {"Cantidad", "Unidad","Descripción","Acción"};
    private List<LineaTicket> data;    

    
    public ArticulosGuiaRemisionTableModel(List<LineaTicket> data) {
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
    public LineaTicket getValueAt(int row, int col) {
        return data.get(row);
    }

}