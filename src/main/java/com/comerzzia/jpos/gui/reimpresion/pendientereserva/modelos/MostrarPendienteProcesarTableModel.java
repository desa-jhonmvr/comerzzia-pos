/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reimpresion.pendientereserva.modelos;

import com.comerzzia.jpos.entity.db.PendienteProcesar;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarPendienteProcesarTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2276690951262699072L;

    private final String[] columnNames = {"N\u00DAMERO FACTURA", "CC", "CLIENTE", "ART\u00CDCULO", "FECHA", "ERROR PROCESO"};
    private final List<PendienteProcesar> data;

    public MostrarPendienteProcesarTableModel(List<PendienteProcesar> data) {
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
    public PendienteProcesar getValueAt(int row, int col) {
        return data.get(row);
    }

}
