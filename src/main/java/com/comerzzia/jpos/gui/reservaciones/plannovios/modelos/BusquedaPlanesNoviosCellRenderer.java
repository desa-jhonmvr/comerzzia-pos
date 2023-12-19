/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.PlanNovio;
import es.mpsistemas.util.fechas.Fecha;
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
public class BusquedaPlanesNoviosCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        PlanNovio planNovio = (PlanNovio) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        Font f = new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize() + 2);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }

        if (col == 0) {
            etiqueta.setText(" "+ planNovio.getPlanNovioPK().getIdPlan());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            if (planNovio.getIdPlanSukasa()!=null){
                etiqueta.setText(" "+ planNovio.getIdPlanSukasa() );
            }
            else{
                etiqueta.setText(" ");
            }
        }
        else if (col == 2) {
            etiqueta.setText(" "+ planNovio.getNovia().getNombre()+ " "+ planNovio.getNovia().getApellido() );
        }
        else if (col == 3) {
            etiqueta.setText(" "+ planNovio.getNovio().getNombre()+ " "+ planNovio.getNovio().getApellido() );
        }
        else if (col == 4) {
            etiqueta.setText(" " + planNovio.getTitulo());
        }
        else if (col == 5) {
            etiqueta.setText(new Fecha(planNovio.getFechaAlta()).getString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 6) {            
            etiqueta.setText(new Fecha(planNovio.getCaducidad()).getString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 7) {
            String estado = planNovio.getEstado();
            etiqueta.setText(estado);
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else {
            etiqueta.setText("");
        }

        return etiqueta;
    }
}
