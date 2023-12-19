/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.cotizaciones.modelos;

import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionBean;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author SMLM
 */
public class BusquedaCotizacionesCellRenderer implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        CotizacionBean cotizacion = (CotizacionBean) object;
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
            etiqueta.setText(" "+cotizacion.getCodcaja());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+cotizacion.getCodalm() + " - " + cotizacion.getCodcaja() + " - " + cotizacion.getIdCotizacion());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 2) {
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd-MMM-yyyy HH:mm", new Locale("es","ES"));
            String fecha = formatoDeFecha.format(cotizacion.getFecha().getDate());
            etiqueta.setText(" "+fecha);
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 3) {
            etiqueta.setText(" "+cotizacion.getCodcli() + " - " + cotizacion.getDescli());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 4) {
            etiqueta.setText(" "+cotizacion.getTotal());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 5) {
            etiqueta.setText(" "+(cotizacion.isCaducado()?"Caducado":"Vigente"));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }        
        
        return etiqueta;
    }
    
}
