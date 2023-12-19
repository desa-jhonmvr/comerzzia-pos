/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.ventas.modelos;

import com.comerzzia.jpos.entity.db.DetPrefactura;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarDetPrefacturaCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel etiqueta = new JLabel();
        DetPrefactura detPreFactura = (DetPrefactura) object;
        etiqueta.setOpaque(true);

        Color colorFondoSeleccion = new Color(204, 204, 204);
        Color colorTextoSeleccion = new Color(0, 0, 0);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        } else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(new Color(0, 0, 0));
        }

        switch (col) {
            case 0:
                etiqueta.setText(detPreFactura.getArticulo().getCodart());
                break;
            case 1:
                etiqueta.setText(detPreFactura.getArticulo().getDesart());
                break;
            case 2:
                etiqueta.setText(String.valueOf(detPreFactura.getDetCantidad()));
                break;
            case 3:
                etiqueta.setText("$ "+String.valueOf(detPreFactura.getDetPvpTotalConIvaConDsto()));
                break;
            default:
                etiqueta.setText("");
        }

        return etiqueta;
    }

}
