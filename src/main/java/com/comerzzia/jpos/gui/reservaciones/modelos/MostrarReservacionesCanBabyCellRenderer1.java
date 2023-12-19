package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
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
public class MostrarReservacionesCanBabyCellRenderer1 implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        ArticuloReservado articulo = (ArticuloReservado) object;
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
            etiqueta.setText(articulo.getArticulo().getCodbarras());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+articulo.getArticulo().getCodart());
        }
        else if (col == 2) {            
            etiqueta.setText(" " + articulo.getArticulo().getDesart());
        }
        else if (col == 3) {
            etiqueta.setText(" " + articulo.getCantidadReservados());
        }
        else if (col == 4) {
            etiqueta.setText(" " + articulo.getCantidadComprados());
        }
        else if (col == 5) {
            etiqueta.setText((articulo.getCantidadReservados()==articulo.getCantidadComprados())?" COMPRADO":" ");

            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
       
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
