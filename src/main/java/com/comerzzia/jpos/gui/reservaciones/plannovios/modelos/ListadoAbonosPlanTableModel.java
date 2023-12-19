/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class ListadoAbonosPlanTableModel extends AbstractTableModel {   
    private String[] columnNames = {"FECHA", "LOCAL", "INVITADO", "CANTIDAD", "C. SIN DESCUENTOS", "LIQUIDADO"};
    private List<AbonoPlanNovio> data;    

    
    public ListadoAbonosPlanTableModel(List<AbonoPlanNovio> data) {
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
    public AbonoPlanNovio getValueAt(int row, int col) {
        return data.get(row);
    }

}