/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.ventas.modelos;

import com.comerzzia.jpos.entity.db.DetPrefactura;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarDetPrefacturaTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ART\u00CDCULO", "DESCRIPCI\u00D3N", "CANTIDAD", "PRECIO"};
    private final List<DetPrefactura> data;

    public MostrarDetPrefacturaTableModel(List<DetPrefactura> data) {
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
    public DetPrefactura getValueAt(int row, int col) {
        return data.get(row);
    }

}
