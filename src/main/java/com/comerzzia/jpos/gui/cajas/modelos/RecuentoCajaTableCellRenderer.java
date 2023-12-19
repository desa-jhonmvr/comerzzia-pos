/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cajas.modelos;


import com.comerzzia.jpos.entity.db.RecuentoCajaDet;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
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
public class RecuentoCajaTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        RecuentoCajaDet elemento = (RecuentoCajaDet) o;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion= new Color (197,239,253);
        Color colorTextoSeleccion= new Color (155,68,4);
        
        
        if (isSelected){
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else{
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
            
        }
      
        if (col == 0) {
            etiqueta.setText("    "+(String)elemento.getCodmedpag().getDesmedpag());
        }
        else if(col == 3){
            etiqueta.setText(" $ " +elemento.getValor().toString()+"   " );
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if(elemento.getCodmedpag().getCodmedpag().equals(MediosPago.getInstancia().getPagoEfectivo().getCodMedioPago())){
            if (col == 1) {
                etiqueta.setText("   " +elemento.getCantidad()+"   " );
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            }
            else if(col == 2){
                etiqueta.setText("   " +elemento.getSubvalor()+"   " );
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }
        else {
            etiqueta.setText("");
        }
        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}

