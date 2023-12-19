/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.ventas.modelos;

import com.comerzzia.jpos.entity.db.CabPrefactura;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarCabPrefacturaTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 71546757418853123L;
    
    private final String[] columnNames = {"No ORDEN", "CLIENTE", "VALOR","OBSERVACION", "ESTADO"};
    private final List<CabPrefactura> data;
    
    public MostrarCabPrefacturaTableModel(List<CabPrefactura> data) {
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
    public CabPrefactura getValueAt(int row, int col) {
        return data.get(row);
    }
    
}
