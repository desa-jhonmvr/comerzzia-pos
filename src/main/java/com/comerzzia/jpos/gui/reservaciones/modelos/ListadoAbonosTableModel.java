/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class ListadoAbonosTableModel extends AbstractTableModel {   
    private String[] columnNames = {"FECHA", "NRO ABONO" , "AUTOR", "Q. CON DSCTO", "Q. SIN DSCTO"};
    private List<ReservaAbonoBean> data;    

    
    public ListadoAbonosTableModel(List<ReservaAbonoBean> data) {
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
    public ReservaAbonoBean getValueAt(int row, int col) {
        return data.get(row);
    }

}