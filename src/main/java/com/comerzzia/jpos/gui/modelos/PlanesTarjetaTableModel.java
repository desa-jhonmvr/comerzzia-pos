/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amos
 */
public class PlanesTarjetaTableModel extends AbstractTableModel {
    private String[] columnNames = {"n", "PLAN","DTO.","INTERÉS","TOTAL INTERÉS","CUOTAS","TOTAL", "AHORRO"};
    private List<PlanPagoCredito> data;    

    
    public PlanesTarjetaTableModel(PagoCredito pago) {
        data = pago.getPlanes();
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
    public PlanPagoCredito getValueAt(int row, int col) {
        return data.get(row);
    }

}