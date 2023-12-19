/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.bonoEfectivo.modelos;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoBean;
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
public class BonoEfectivoCambioCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        BonoEfectivoBean bono = (BonoEfectivoBean) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        } else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }
        if (col == 0) {
            etiqueta.setText(bono.getCodAlm());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        } else if (col == 1) {
            etiqueta.setText(bono.getIdBono());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        } else if (col == 2) {
            etiqueta.setText(bono.getFechaCaducidad().getString());
        } else if (col == 3) {
            etiqueta.setText(bono.getImporte().toString());
        } else if (col == 4) {
            etiqueta.setText(bono.getUtilizado());
        }
        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
