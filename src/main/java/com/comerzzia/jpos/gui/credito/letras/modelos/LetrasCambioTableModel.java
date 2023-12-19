/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.credito.letras.modelos;

import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class LetrasCambioTableModel  extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private String[] columnNames = {"CUOTA","VALOR","MORA","TOTAL", "FECHA VENCIMIENTO", "FECHA COBRADO"};
    private List<LetraCuotaBean> data;

    public LetrasCambioTableModel(List<LetraCuotaBean> letras) {
        this.data = letras;
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
    public LetraCuotaBean getValueAt(int row, int col) {
        return data.get(row);
    }
}