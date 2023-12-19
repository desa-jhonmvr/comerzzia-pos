/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.ventas.modelos;

import com.comerzzia.jpos.entity.db.CabPrefactura;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarCabPrefacturaCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel etiqueta = new JLabel();
        CabPrefactura cabPreFactura = (CabPrefactura) object;
        etiqueta.setOpaque(true);

        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        } else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(new Color(0, 0, 0));
        }

        switch (col) {
            case 0:
                etiqueta.setText(cabPreFactura.getCabIdPedido());
                break;
            case 1:
                etiqueta.setText(cabPreFactura.getCabApellido()+" "+cabPreFactura.getCabNombre());
                break;
            case 2:
                etiqueta.setText(String.valueOf(cabPreFactura.getCabTotalConDstoConIva()));
                break;
                case 3:
                etiqueta.setText(cabPreFactura.getCabObservacion());
                break;
            case 4:
                etiqueta.setText(cabPreFactura.getCabEstado().getDescripcion());
                break;
            default:
                etiqueta.setText("");
        }

        return etiqueta;
    }

}
