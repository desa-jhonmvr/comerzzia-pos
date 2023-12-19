package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
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
public class SeleccionarInvitadoPlanCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        InvitadoPlanNovio invitado = (InvitadoPlanNovio) object;
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
            if (invitado.getTitulo() != null) {
                etiqueta.setText(invitado.getTitulo());
            }
            else {
                etiqueta.setText("");
            }
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(invitado.getNombre());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else if (col == 2) {

            etiqueta.setText(" " + invitado.getApellido());
        }
        else if (col == 3) {
            if (invitado.getTelefono() != null) {
                etiqueta.setText(" " + invitado.getTelefono());
            }
            else {
                etiqueta.setText(" ");
            }
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
