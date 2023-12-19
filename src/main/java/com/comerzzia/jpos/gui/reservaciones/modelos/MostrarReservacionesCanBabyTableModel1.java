/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class MostrarReservacionesCanBabyTableModel1 extends AbstractTableModel {

    private String[] columnNames = {"CÓD. BARRAS","CÓDIGO", "DESCRIPCIÓN", "RESERVADA", "COMPRADA", "ESTADO"};
    private List<ArticuloReservado> data;

    public MostrarReservacionesCanBabyTableModel1(List<ArticuloReservado> articulos) {
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
    public ArticuloReservado getValueAt(int row, int col) {
        return data.get(row);
    }
}