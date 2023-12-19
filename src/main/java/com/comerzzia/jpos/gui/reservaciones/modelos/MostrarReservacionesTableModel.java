/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class MostrarReservacionesTableModel extends AbstractTableModel {
    private String[] columnNames = {"CÓDIGO DE BARRAS", "DESCRIPCIÓN","PRECIO","IMPORTE","ESTADO"};
    private List<ReservaArticuloBean> data;    

    
    public MostrarReservacionesTableModel(List<ReservaArticuloBean> data) {
        this.data=data;
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
    public ReservaArticuloBean getValueAt(int row, int col) {
        return data.get(row);
    }

}