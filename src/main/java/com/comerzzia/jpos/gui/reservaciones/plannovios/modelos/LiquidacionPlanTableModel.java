/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class LiquidacionPlanTableModel extends AbstractTableModel {
    private String[] columnNames = {"CÓDIGO DE BARRAS", "DESCRIPCIÓN","PRECIO","IMPORTE","ESTADO"};
    private List<ArticuloPlanNovio> data;    

    
    public LiquidacionPlanTableModel(List<ArticuloPlanNovio> data) {
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
    public ArticuloPlanNovio getValueAt(int row, int col) {
        return data.get(row);
    }

}