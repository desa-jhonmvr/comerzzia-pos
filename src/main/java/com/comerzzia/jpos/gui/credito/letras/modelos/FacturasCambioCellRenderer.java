/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.credito.letras.modelos;

import com.comerzzia.jpos.persistencia.letras.LetraBean;
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
public class FacturasCambioCellRenderer implements TableCellRenderer {
    
    
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        LetraBean letra = (LetraBean) object;
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
            etiqueta.setText(letra.getCodAlmacen());  
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);    
        }
        else if (col == 1) {
            etiqueta.setText(letra.getCodCaja());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);    
        }        
        else if (col == 2) {
            etiqueta.setText(letra.getIdTicket().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        }
        else if (col == 3) {
            etiqueta.setText(letra.getFecha().getString());
        }
        else if (col == 4) {
            etiqueta.setText(" " + letra.getPlazo());
        }
        else if (col == 5) {
            if(letra.getEstado() != null){
                if(letra.getEstado().equals(LetraBean.ESTADO_COMPLETA))
                    etiqueta.setText("PAGADO");
                else if(letra.getEstado().equals(LetraBean.ESTADO_PENDIENTE)) // retención aplicada en algún pago
                    etiqueta.setText("PENDIENTE");
                else if(letra.getEstado().equals(LetraBean.ESTADO_ANULADA))
                    etiqueta.setText("ANULADA");
                else if(letra.getEstado().equals(LetraBean.ESTADO_PENDIENTE_R))
                    etiqueta.setText("PENDIENTE");
            }
            else
                etiqueta.setText("");
        }
        else if (col == 6) {
            etiqueta.setText("$ " + letra.getTotal());
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
