/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Admin
 */
public class TicketsAparcadosTableModel extends AbstractTableModel{
    private String[] columnNames = {"FECHA","CLIENTE"};    
    List<TicketS> data=null;
    
    public TicketsAparcadosTableModel() {
        data = Sesion.getTicketsAparcados();
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
    public TicketS getValueAt(int row, int col) {
        return data.get(row);
    }
}
