/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.PlanNovio;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class BusquedaPlanesNoviosTableModel extends AbstractTableModel {

    private String[] columnNames = {"ID PLAN","ID SK", "NOVIA", "NOVIO", "BODA", "ALTA", "FIN RESERVA", "ESTADO"};
    private List<PlanNovio> data;

    public BusquedaPlanesNoviosTableModel(List<PlanNovio> data) {
        this.data = data;
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
    public PlanNovio getValueAt(int row, int col) {
        return data.get(row);
    }
}