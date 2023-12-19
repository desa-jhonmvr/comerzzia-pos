/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.promocion.modelos;

import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class PromocionTableModel extends AbstractTableModel {

    private String[] columnNames = {" ID PROMOCION ", " TIPO  ", "DESCUENTO "};
    private List<PromocionLineaTicket> data;

    public PromocionTableModel(List<PromocionLineaTicket> data) {
        this.data = data;
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
    public PromocionLineaTicket getValueAt(int row, int col) {
        return data.get(row);
    }
}