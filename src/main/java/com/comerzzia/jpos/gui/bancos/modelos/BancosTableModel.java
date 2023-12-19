/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.bancos.modelos;

import com.comerzzia.jpos.persistencia.mediospagos.BancoBean;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MGRI
 */
public class BancosTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private String[] columnNames = {" INSTITUCIÓN                            "," TELÉFONO 1  "," TELÉFONO 2 ","COD. ESTABLECIMIENTO"};
    private List<BancoBean> data;    

    public BancosTableModel(){
        super();
    }
    
    public BancosTableModel(List<BancoBean> bancos, String codEstablecimiento) {
        for (BancoBean banco : bancos) {
            banco.setCodEstablecimiento(codEstablecimiento);
        }
        data = bancos;
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
    public BancoBean getValueAt(int row, int col) {
        return data.get(row);
    }

    
}
