/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.db.CajaDet;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class MovimientosTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        CajaDet elemento = (CajaDet) o;
        etiqueta.setOpaque(false);
        Color colorValorPositivo= new Color (12,141,201);
        Color colorValorNegativo= new Color (155,68,4);
        
        if (elemento.getAbono()!=null && elemento.getAbono().compareTo(BigDecimal.ZERO) < 0){
            etiqueta.setForeground(colorValorNegativo);
        }
        else {
            etiqueta.setForeground(colorValorPositivo);        
        }
        
        if (col == 0) {
            SimpleDateFormat formateador= new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            etiqueta.setText(formateador.format(elemento.getFecha()));
        }
        else if (col == 1) {
            if (elemento.getCargo() != null) {                
                etiqueta.setText(" $ " +elemento.getCargo().setScale(2, RoundingMode.HALF_UP).toString() );
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else {
                etiqueta.setText("$ 0.00 ");
            }
        }
        else if (col == 2) {
            if (elemento.getAbono() != null) {
                etiqueta.setText(" $ " +elemento.getAbono().setScale(2, RoundingMode.HALF_UP).toString() + "     ");
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);                
            }
            else {
                etiqueta.setText("$ 0.00 ");
            }
        }
        else if (col == 3) {
            etiqueta.setText(elemento.getDocumento());
        }
        else if (col == 4) {
            etiqueta.setText(elemento.getConcepto());
        }
        else if (col == 5) {
            etiqueta.setText(elemento.getCodmedpag().getDesmedpag());
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;

    }
}
