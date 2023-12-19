/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.db.RecuentoCajaDet;
import java.util.LinkedList;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class RecuentoCajaTableModel extends AbstractTableModel {

    private String[] columnNames = {"FORMA DE PAGO","CANTIDAD" ,"DENOMINACION", "VALOR"};
    private List<RecuentoCajaDet> data;    

    public RecuentoCajaTableModel(){
        super();
        data = new LinkedList();        
    }
    
    public RecuentoCajaTableModel(List<RecuentoCajaDet> mov) {
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
    public RecuentoCajaDet getValueAt(int row, int col) {
        return data.get(row);
    }
    
}
