/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.entity.gui.reservaciones.ReservaCompra;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class MostrarDetalleArticuloTableModel extends AbstractTableModel {
 private String[] columnNames = {"NOMBRE", "CANT COMPRADA", "FECHA"};
    private List<ReservaCompra> data;

    public MostrarDetalleArticuloTableModel(List<ReservaCompra> articulos) {
        this.data = articulos;
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
    public ReservaCompra getValueAt(int row, int col) {
        return data.get(row);
    }
}
