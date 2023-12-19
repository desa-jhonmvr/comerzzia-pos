package com.comerzzia.jpos.gui.bancos.modelos;

import com.comerzzia.jpos.persistencia.mediospagos.BancoBean;
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
public class BancosTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        BancoBean banco = (BancoBean) object;
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
            etiqueta.setText(banco.getDesBan());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else if (col == 1) {
            etiqueta.setText(banco.getTelefono1());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else if (col == 2) {
            etiqueta.setText(banco.getTelefono2());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else if (col == 3) {
            etiqueta.setText(banco.getCodEstablecimiento());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }        
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
