/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.clientes.modelos;

import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMLM
 */
public class FacturaClientesTableModel extends AbstractTableModel{

    private String[] columnNames = {"Nro. Factura","Fecha Transacción", "Monto", "Estado"};
    private List<DocumentosBean> data;
    
    public FacturaClientesTableModel(List<DocumentosBean> data) {
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
    public Object getValueAt(int row, int column) {
        return data.get(row);
    }
    
}
