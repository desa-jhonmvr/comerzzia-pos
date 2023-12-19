package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;

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
public class LiquidacionPlanCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        ArticuloPlanNovio articulo = (ArticuloPlanNovio) object;
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
            etiqueta.setText(articulo.getCodBarras());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {

            etiqueta.setText(" " + articulo.getDesArt());
        }
        else if (col == 2) {
            etiqueta.setText(" " + " $" + Numero.redondear(articulo.getPrecioTotal()).toString());
        }
        else if (col == 3) {
            etiqueta.setText(" $" + Numero.redondear(articulo.getPrecioTotal()).toString());
        }
        else if (col == 4) {            
            if (articulo.isComprado() && !articulo.isPendienteEnvio() && !articulo.isPendienteRecoger()) {
                etiqueta.setText(" COMPRADO");
            }
            else if (articulo.isPendienteEnvio() && articulo.isComprado()){
                etiqueta.setText(" P/E ");
            }
            else if (articulo.isPendienteRecoger() && articulo.isComprado() && !articulo.isPendienteEnvio()){
                etiqueta.setText(" P/R ");
            }
            else{
                etiqueta.setText(" ");
            }
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
