/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class TicketTableModel extends AbstractTableModel {
    private String[] columnNames = {"","","",""};
    private List<LineaTicket> lineasTicket;
    private List<DescuentoTicket> descuentos;
   
    public TicketTableModel(LineasTicket lineas) {
        lineasTicket = lineas.getLineas();
        descuentos = lineas.getDescuentos();
    }

    @Override
    public int getRowCount() {
        if (descuentos.isEmpty()){
            return lineasTicket.size();
        }
        return lineasTicket.size() + 1 + descuentos.size();
        // el 1 de más es para añadir una línea de separación
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
    public Object getValueAt(int row, int col) {
        if (row < lineasTicket.size()){
            return lineasTicket.get(row);
        }
        if (row == lineasTicket.size()){
            if (col != 1){
                return "";
            }
            return "----------------------------";
        }
        else{
            return descuentos.get(row - lineasTicket.size() -1);
        }
    }

    
}
