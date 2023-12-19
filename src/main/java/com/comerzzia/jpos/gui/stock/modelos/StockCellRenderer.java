/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.stock.modelos;

import com.comerzzia.jpos.servicios.stock.StockBean;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class StockCellRenderer implements TableCellRenderer {

    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        StockBean stock = (StockBean) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);


        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }

        if (col == 0) {
            if(stock.getDesAlm().equals("STOCK TOTAL")){
                etiqueta.setText(stock.getDesAlm().toString());
            } else {
                etiqueta.setText(stock.getCodAlm() + " - " + stock.getDesAlm().toString());
            }
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else if (col == 1) {
            etiqueta.setText(" " + stock.getStock());            
        }        
        else if (col == 2) {
            etiqueta.setText(stock.getBloqueado());
        }
        else{
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
