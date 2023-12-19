/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class BusquedaReservacionesTableModel extends AbstractTableModel {
    private String[] columnNames = {"Nº RESERVA", "TIPO", "CLIENTE","NOMBRE Y APELLIDOS","ALTA","FIN RESERVA","T. SALDO RESERVACIÓN", "T. SALDO ABONADO", "Nº ABONOS", "ESTADO"};
    private List<ReservaBean> data;    

    
    public BusquedaReservacionesTableModel(List<ReservaBean> data) {
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
    public ReservaBean getValueAt(int row, int col) {
        return data.get(row);
    }

}