/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.guiasremision.modelos;

import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author SMLM
 */
public class ArticulosEnvioDomicilioCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        LineaTicketOrigen lt = (LineaTicketOrigen) object;
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
        
        if(col == 0){
            etiqueta.setText(""+lt.getCodart().getCodart());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if(col == 1){
            etiqueta.setText(""+lt.getCodart().getDesart());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        }
        else if(col == 2){
            etiqueta.setText(""+lt.getCantidad());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        }
        else if(col == 3){
            char c = lt.getEnvioDomicilio();
            char g = lt.getRecogidaPosterior();
            String s = "";
            if(c == 'P'){
                s="Pendiente Envío";
            } else if(g == 'P') {
                s="Pendiente Recoger";
            }else if(c == 'N' && g == 'N'){
                s="No";
            } else
            if(g == 'E'){
                s="Entregado";
            } 
            etiqueta.setText(s);
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        } else {
            etiqueta.setText("");
        }
        return etiqueta;
    }
    
}
