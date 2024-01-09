package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class PlanesTarjetaCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        PlanPagoCredito pago = (PlanPagoCredito) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        //Color totalyAhorro = new Color(46, 106, 1);
        Font f= new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize()+1);
        //Font f= new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize()+2);

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);
        }

        if (col == 0) {
            etiqueta.setText(new Integer(row+1).toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+pago.getPlan());
        }
        else if (col == 2) {
            etiqueta.setText(pago.getDescuento() + "%");
            etiqueta.setFont(f);
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            etiqueta.setForeground(Color.red);
        }
        else if (col == 3) {
            etiqueta.setText(pago.getPorcentajeInteres().setScale(4, RoundingMode.UP) +"%  ");
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            //etiqueta.setForeground(totalyAhorro);
        }
        else if (col == 4) {
            etiqueta.setText(" $ " +pago.getImporteInteres());
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 5) {
            etiqueta.setText(pago.getNumCuotas() + " x " + pago.getCuotaMasInteres() + " = "+ " $ " + pago.getaPagarMasInteres() );
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        else if (col == 6) {
            etiqueta.setText(" $ " +pago.getTotal());
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            //etiqueta.setForeground(totalyAhorro);
        }
        else if (col == 7) {
            BigDecimal ahorroConIntereses = pago.getAhorroConInteres();
            if (ahorroConIntereses.compareTo(BigDecimal.ZERO) < 0){
                ahorroConIntereses = BigDecimal.ZERO;
            }
            etiqueta.setText(" $ " +ahorroConIntereses);
            etiqueta.setFont(f);
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setForeground(Color.red);
        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
