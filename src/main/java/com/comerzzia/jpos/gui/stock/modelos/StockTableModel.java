/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.stock.modelos;

import com.comerzzia.jpos.servicios.stock.StockBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class StockTableModel extends AbstractTableModel {

    private String[] columnNamesWithBloqueo = {" LOCAL ", "STOCK ", "BQ"};
    private String[] columnNames = {" LOCAL ", "STOCK "};
    private List<StockBean> data;
    private boolean mostrarBloqueo;

    public StockTableModel(List<StockBean> stocks, boolean mostrarBloqueo) {
        this.data = stocks;
        this.mostrarBloqueo = mostrarBloqueo;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        if(mostrarBloqueo) {
            return columnNamesWithBloqueo.length;
        } else {
            return columnNames.length;            
        }

    }

    @Override
    public String getColumnName(int col) {
        if(mostrarBloqueo) {
            return columnNamesWithBloqueo[col];
        } else {
            return columnNames[col];
        }
    }

    @Override
    public StockBean getValueAt(int row, int col) {
        return data.get(row);
    }
}