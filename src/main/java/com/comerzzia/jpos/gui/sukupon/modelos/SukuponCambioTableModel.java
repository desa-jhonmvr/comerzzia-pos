/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.sukupon.modelos;

import com.comerzzia.jpos.persistencia.sukupon.SukuponBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class SukuponCambioTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String[] columnNames = {"LOCAL", "CUPON", "BARRAS","FECHA EXPEDICION", "FECHA VALIDEZ", "TOTAL", "UTILIZO" ,"UTILIZADO", "SALDO", "FACTURA", "NOTA CREDITO"};
    private List<SukuponBean> data;

    public SukuponCambioTableModel(List<SukuponBean> bonos) {
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
    public SukuponBean getValueAt(int row, int col) {
        return data.get(row);
    }
}
