package com.comerzzia.jpos.gui.modelos;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MGRI
 */
public class TVentasTableModel extends DefaultTableModel {

    public TVentasTableModel(Vector consultarTicketJTable, Vector headerT) {
        super(consultarTicketJTable,headerT);
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;  //
}
    
    
}
