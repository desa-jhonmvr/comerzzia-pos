/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.clientes.modelos;

import com.comerzzia.jpos.entity.db.Cliente;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author SMLM
 */
public class BusquedaClientesCellRenderer implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        Cliente cliente = (Cliente) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        Font f = new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize() + 2);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }
        
        if (col == 0) {
            etiqueta.setText(" "+cliente.getIdentificacion());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+cliente.getNombre());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 2) {
            etiqueta.setText(" "+cliente.getApellido());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 3) {
            etiqueta.setText(" "+cliente.getNumeroFacturas());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        }
                
        return etiqueta;
    }
    
}
