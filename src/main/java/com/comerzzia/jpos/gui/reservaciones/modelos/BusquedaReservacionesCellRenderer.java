package com.comerzzia.jpos.gui.reservaciones.modelos;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class BusquedaReservacionesCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        ReservaBean reserva = (ReservaBean) object;
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
            etiqueta.setText(String.valueOf(reserva.getCodReservacion()));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(reserva.getDesTipoReserva());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 2) {
            etiqueta.setText(" " + reserva.getCodcli());
        }
        else if (col == 3) {
            etiqueta.setText(" " + reserva.getNombrecliente() + " " + reserva.getApellidoscliente());
        }
        else if (col == 4) {
            etiqueta.setText(reserva.getFechaAlta().getString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 5) {
            etiqueta.setText(reserva.getCaducidad().getString()); 
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 6) {
            BigDecimal sumaReservaTotal = BigDecimal.ZERO;
            for(ReservaArticuloBean articulo:reserva.getReservaArticuloList()){
                sumaReservaTotal = sumaReservaTotal.add(articulo.getPrecioTotal());
            }
            etiqueta.setText(String.valueOf(sumaReservaTotal));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 7) {
            BigDecimal sumaReservaAbono = BigDecimal.ZERO;
            for(ReservaAbonoBean abono:reserva.getReservaAbonoList()){
                sumaReservaAbono=sumaReservaAbono.add(abono.getCantidadAbono());
            }
            etiqueta.setText(String.valueOf(sumaReservaAbono));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 8) {
            etiqueta.setText(String.valueOf(reserva.getReservaAbonoList().size()));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 9) {
            String estado = reserva.getEstado();
            etiqueta.setText(estado);
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }
        else {
            etiqueta.setText("");
        }

        return etiqueta;
    }
}
