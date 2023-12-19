/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.promocion.modelos;

import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Sheyla Rivera
 */
public class PromocionCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        PromocionLineaTicket promocion = (PromocionLineaTicket) object;
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
            etiqueta.setText(promocion.getIdPromocion().toString());
        }
        else if (col == 1) {
            etiqueta.setText(" " + promocion.getDesTipoPromocion());            
        }        
        else if (col == 2) {
            if(promocion.getDescuento() != null){
                etiqueta.setText(promocion.getDescuento().toString());
            }
        }
        else{
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
