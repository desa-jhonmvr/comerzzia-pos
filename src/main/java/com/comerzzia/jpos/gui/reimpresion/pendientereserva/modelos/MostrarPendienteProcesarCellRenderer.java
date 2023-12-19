/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reimpresion.pendientereserva.modelos;

import com.comerzzia.jpos.entity.db.PendienteProcesar;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.fechas.Fecha;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gabriel Simbania
 */
public class MostrarPendienteProcesarCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel etiqueta = new JLabel();
        PendienteProcesar pendienteProcesar = (PendienteProcesar) object;
        etiqueta.setOpaque(true);

        Color colorFondoSeleccion = new Color(204, 204, 204);
        Color colorTextoSeleccion = new Color(0, 0, 0);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        } else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(new Color(0, 0, 0));
        }

        switch (col) {
            case 0:
                etiqueta.setText(pendienteProcesar.getNumFactura());
                break;
            case 1:
                etiqueta.setText(pendienteProcesar.getTicketsAlm().getCodCli());
                break;
            case 2:
                etiqueta.setText(pendienteProcesar.getTicketsAlm().getFactNombre());
                break;
            case 3:
                etiqueta.setText(String.valueOf(pendienteProcesar.getCodArt()));
                break;
            case 4:
                etiqueta.setText(Fechas.dateToString(pendienteProcesar.getTicketsAlm().getFecha(), Fecha.PATRON_FECHA_CORTA));
                break;
            case 5:
                etiqueta.setText(pendienteProcesar.getErrorProceso());
                break;
            default:
                etiqueta.setText("");
        }

        return etiqueta;
    }

}
