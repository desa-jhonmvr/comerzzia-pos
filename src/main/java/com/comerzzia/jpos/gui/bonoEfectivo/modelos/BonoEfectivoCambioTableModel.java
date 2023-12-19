/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.bonoEfectivo.modelos;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class BonoEfectivoCambioTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String[] columnNames = {"LOCAL", "BONO", "FECHA CADUCIDAD", "IMPORTE", "UTILIZADO"};
    private List<BonoEfectivoBean> data;

    public BonoEfectivoCambioTableModel(List<BonoEfectivoBean> bonos) {
        this.data = bonos;
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
    public BonoEfectivoBean getValueAt(int row, int col) {
        return data.get(row);
    }
}
