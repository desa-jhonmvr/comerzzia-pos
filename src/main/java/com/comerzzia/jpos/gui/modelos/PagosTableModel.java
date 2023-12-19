/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.pagos.Pago;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class PagosTableModel extends AbstractTableModel {

    private String[] columnNames = {"", "", "PVP", "DSCTO", "INTERÉS", "TOTAL", "PLAZO CUOTA", "       AHORRO", "     CAMBIO", "", "AUT."};
    private List<Pago> data;

    public PagosTableModel() {
        super();
    }

    public PagosTableModel(PagosTicket pagos) {
        data = pagos.getPagos();
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
    public Pago getValueAt(int row, int col) {
        return data.get(row);
    }

}
