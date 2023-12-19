/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.services.cierrecaja.LineaCierreCaja;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class CierreCajaTableModel extends AbstractTableModel {

    private String[] columnNames = {"MEDIO DE PAGO","ENTRADA","SALIDA","TOTAL","RECUENTO","DESCUADRE"};
    private List<LineaCierreCaja> data;    

    public CierreCajaTableModel(){
        super();
    }
    
    public CierreCajaTableModel(TreeMap mov) {             
        
        data = new ArrayList(mov.values());      
      
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
    public LineaCierreCaja getValueAt(int row, int col) {
        return data.get(row);
    }
}
