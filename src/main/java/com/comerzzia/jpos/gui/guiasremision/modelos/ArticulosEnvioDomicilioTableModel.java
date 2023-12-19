/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.guiasremision.modelos;

import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMLM
 */
public class ArticulosEnvioDomicilioTableModel extends AbstractTableModel {
    private String[] columnNames = {"Artículo", "Descripción","Cantidad","Estado"};
    private List<LineaTicketOrigen> data;   
    
    public ArticulosEnvioDomicilioTableModel(List<LineaTicketOrigen> data) {
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
    public LineaTicketOrigen getValueAt(int row, int col) {
        return data.get(row);
    }
    
}
