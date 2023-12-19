/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.db.CajaDet;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class MovimientosTableModel extends AbstractTableModel {

    private String[] columnNames = {"FECHA","ENTRADAS","SALIDAS","DOCUMENTO","CONCEPTO","FORMA DE PAGO"};
    private List<CajaDet> data;    

    public MovimientosTableModel(){
        super();
    }
    
    public MovimientosTableModel(List<CajaDet> mov) {
        data = mov;
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
    public CajaDet getValueAt(int row, int col) {
        return data.get(row);
    }
    
}
