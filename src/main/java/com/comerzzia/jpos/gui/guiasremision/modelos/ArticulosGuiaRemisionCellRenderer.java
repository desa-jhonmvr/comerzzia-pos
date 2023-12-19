package com.comerzzia.jpos.gui.guiasremision.modelos;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;

import com.comerzzia.util.numeros.bigdecimal.Numero;
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
public class ArticulosGuiaRemisionCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        LineaTicket lt = (LineaTicket) object;
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
            etiqueta.setText(""+lt.getCantidad());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {

            etiqueta.setText(" " + 1); //TODO: GUIA REMISION - Establecer correctamente el parámetro unidad
        }
        else if (col == 2) {
            etiqueta.setText(lt.getArticulo().getDesart());
        }
        else if (col == 3) {
             if (lt.isEnvioEnGuiaRemision()) {
                etiqueta.setText(" SELECCIONADO");
            }
            else{
                etiqueta.setText(" ");
            }
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 4) {            
           
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
