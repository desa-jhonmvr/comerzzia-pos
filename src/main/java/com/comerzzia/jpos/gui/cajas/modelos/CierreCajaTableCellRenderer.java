/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;

import com.comerzzia.jpos.entity.db.MedioPagoCaja;
import com.comerzzia.jpos.entity.services.cierrecaja.LineaCierreCaja;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class CierreCajaTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        LineaCierreCaja elemento = (LineaCierreCaja) o;
        etiqueta.setOpaque(false);

        Color color = new Color(155, 68, 4);
        etiqueta.setForeground(color);

        if (col == 0) {
            etiqueta.setText(elemento.getMedioPago().getDesmedpag());
        }
        else if (col == 1) {
            if (elemento.getEntrada() != null) {
                etiqueta.setText(" $ " + elemento.getEntrada().toString() + " ");               
            }
            else {
                etiqueta.setText("");
            }
              etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);       
        }
        else if (col == 2) {
            if (elemento.getSalida() != null) {
                etiqueta.setText(" $ " +elemento.getSalida().toString() + " ");                
            }
            else {
                etiqueta.setText("$ 0.00 ");
            }
             etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 3) {
            if (elemento.getTotal() != null) {
                etiqueta.setText(" $ " +elemento.getTotal().toString() + " ");               
            }
            else {
                etiqueta.setText("$ 0.00 ");
            }
             etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 4) {
            if (elemento.getRecuento() != null) {
                etiqueta.setText(" $ " +elemento.getRecuento().toString() + " ");               
            }
            else {
                etiqueta.setText("$ 0.00 ");               
            }
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 5) {
            if (elemento.getDescuadre()!= null) {
                etiqueta.setText(" $ " +elemento.getDescuadre().toString() + " ");
            }
            else {
                if (elemento.getRecuento() == null && elemento.getTotal()!=null){
                   etiqueta.setText(" $ -" +elemento.getTotal().toString() + " "); 
                }
                else{
                    etiqueta.setText(" ");
                }
            }
             etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;

    }
}
