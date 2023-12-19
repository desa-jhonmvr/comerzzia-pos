/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cotizaciones.modelos;

import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMLM
 */
public class BusquedaCotizacionesTableModel extends AbstractTableModel{
    private String[] columnNames = {"CAJA", "NÚMERO", "FECHA", "CLIENTE", "TOTAL", "ESTADO"};
    private List<CotizacionBean> data;

    public BusquedaCotizacionesTableModel(List<CotizacionBean> data){
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
    public CotizacionBean getValueAt(int row, int column) {
        return data.get(row);
    }    
}
