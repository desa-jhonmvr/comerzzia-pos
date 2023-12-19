/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
import com.comerzzia.jpos.entity.gui.reservaciones.ReservaCompra;
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
public class MostrarDetalleArticuloCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        ReservaCompra comprador = (ReservaCompra) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        SimpleDateFormat formateador= new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }

        if (col == 0) {
            etiqueta.setText(comprador.getInvitado().getNombre()+" "+comprador.getInvitado().getApellido());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            
            etiqueta.setText(" " + comprador.getCantidad());
        }
        else if (col == 2) {
            etiqueta.setText(" " + formateador.format(comprador.getFecha()));
        }       
       
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
