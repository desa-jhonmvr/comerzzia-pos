/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.devoluciones.modelos;

import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DESARROLLO
 */
public class MostrarFormasPagoTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2276690951262699072L;

    private final String[] columnNames = {"MEDIO PAGO", "VALOR", "CRUZAR"};
    private final List<MedioPagoDTO> data;

    public MostrarFormasPagoTableModel(List<MedioPagoDTO> data) {
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
    public MedioPagoDTO getValueAt(int row, int col) {
        return data.get(row);
    }

}
