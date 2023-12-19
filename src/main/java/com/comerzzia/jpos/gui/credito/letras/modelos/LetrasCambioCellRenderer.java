/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.credito.letras.modelos;

import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class LetrasCambioCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        LetraCuotaBean letra = (LetraCuotaBean) object;
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
            etiqueta.setText(letra.getCuota().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText("$ " + letra.getValor());
        }
        else if (col == 2) {
            etiqueta.setText("$ " + letra.getImporteMora());
        }
        else if (col == 3) {            
            etiqueta.setText("$ " + letra.getTotal());
        }
        else if (col == 4) {            
            etiqueta.setText(" " + letra.getFechaVencimiento().getString());
        }
        else if (col == 5) {
            if (letra.getFechaCobro() != null){
                etiqueta.setText(" " + letra.getFechaCobro().getString());
            }
            else if (letra.getEstado().equals("A")){
                etiqueta.setText(" ANULADA");
            }
            else{
                etiqueta.setText(" PENDIENTE");
            }
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
