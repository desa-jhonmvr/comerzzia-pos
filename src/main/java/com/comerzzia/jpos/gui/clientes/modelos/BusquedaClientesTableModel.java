/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.clientes.modelos;

import com.comerzzia.jpos.entity.db.Cliente;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMLM
 */
public class BusquedaClientesTableModel extends AbstractTableModel{

    private String[] columnNames = {"CC/RUC/Pasaporte","Nombres", "Apellidos", "Nº Facturas"};
    private List<Cliente> data;

    public BusquedaClientesTableModel(List<Cliente> data){
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
    public Cliente getValueAt(int row, int column) {
        return data.get(row);
    }
    
}
