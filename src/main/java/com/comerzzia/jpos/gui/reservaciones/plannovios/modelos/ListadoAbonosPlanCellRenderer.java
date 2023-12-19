/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios.modelos;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
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
public class ListadoAbonosPlanCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        AbonoPlanNovio abono = (AbonoPlanNovio) object;
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
            SimpleDateFormat formateador= new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            etiqueta.setText(formateador.format(abono.getFecha()));
            //etiqueta.setText(abono.getFechaAbono().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
         else if (col == 1) {
            if (abono.getInvitadoPlanNovio()!=null){
                etiqueta.setText(" "+abono.getNombreAlmacenAbono());
            }
            else{
                etiqueta.setText(" ");
            }
                
        }
        else if (col == 2) {
            if (abono.getInvitadoPlanNovio()!=null){
                etiqueta.setText(" "+abono.getInvitadoPlanNovio().getNombre()+" "+abono.getInvitadoPlanNovio().getApellido());
            }
            else{
                etiqueta.setText("Abono Propio");
            }
                
        }
        else if (col == 3) {
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setText(" " +" $"+abono.getCantidadConDcto());
        }
        else if (col == 4) {
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setText(" " +" $"+abono.getCantidadSinDcto());
        } 
        else if (col == 5) {
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            if(abono.getEstadoLiquidacion() == null){
                etiqueta.setText("NO");
            }else{
                etiqueta.setText("SI");
            }
        } 
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
