/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.sukupon.modelos;

import com.comerzzia.jpos.persistencia.sukupon.SukuponBean;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class SukuponCambioCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        SukuponBean sukupon = (SukuponBean) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        Font f = new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize() + 2);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        } else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }
        if (col == 0) {
            etiqueta.setText(sukupon.getCodAlm());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        } else if (col == 1) {
            etiqueta.setText(sukupon.getIdCupon());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        } else if (col == 2) {
            etiqueta.setText(sukupon.getCodBarras());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        } else if (col == 3) {
            etiqueta.setText(sukupon.getFechaExpedicion().getString());
        } else if (col == 4) {
            etiqueta.setText(sukupon.getFechaValidez().getString());
        } else if (col == 5) {
            etiqueta.setText(sukupon.getTotal().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        } else if (col == 6) {
            if (sukupon.getUtilizoItem() != null) {
                etiqueta.setText(sukupon.getUtilizoItem().toString());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            }
        } else if (col == 7) {
            etiqueta.setText(sukupon.getUtilizado());
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        } else if (col == 8) {
            if (sukupon.getSaldoItem() != null) {
                etiqueta.setText(sukupon.getSaldoItem().toString());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            }
        } else if (col == 9) {
            if (sukupon.getFactura() != null) {
                etiqueta.setText(sukupon.getFactura());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            }
        } else if (col == 10) {
            if (sukupon.getNotaCredito() != null) {
                etiqueta.setText(sukupon.getNotaCredito());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            }
        }
        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
