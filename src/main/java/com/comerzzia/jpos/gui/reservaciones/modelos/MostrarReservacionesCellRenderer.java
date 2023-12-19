package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class MostrarReservacionesCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        ReservaArticuloBean articulo = (ReservaArticuloBean) object;
        etiqueta.setOpaque(true);
     //   Color colorFondoSeleccion = new Color(197, 239, 253); //celeste
      //  Color colorTextoSeleccion = new Color(155, 68, 4); //chocolate

        Color colorFondoSeleccion = new Color(204, 204, 204);
        Color colorTextoSeleccion = new Color(0, 0, 0); 

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(new Color(0, 0, 0));
        }

        if (col == 0) {
            etiqueta.setText(articulo.getCodbarras());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {

            etiqueta.setText(" " + articulo.getDesart());
        }
        else if (col == 2) {
            etiqueta.setText(" " + articulo.getCantidad() + " x $" + Numero.redondear(articulo.getPrecioTotal()).toString());
        }
        else if (col == 3) {
            etiqueta.setText(" $" + Numero.redondear(articulo.getPrecioTotal().multiply(new BigDecimal(articulo.getCantidad()))).toString());
        }
        else if (col == 4) {
            etiqueta.setText((articulo.getComprado()) ? " COMPRADO" : " ");

            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
