package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.tickets.TicketS;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class TicketsAparcadosCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        TicketS ticket = (TicketS) object;
        etiqueta.setOpaque(true);
        JPanel panel=new JPanel();
 
        
        
        if (isSelected){
            etiqueta.setBackground(new Color(57,105,138));
            etiqueta.setForeground(Color.WHITE);
        }
        
        

        if (col == 0) {
            etiqueta.setText(((ticket.getFecha()!=null)?ticket.getFecha().getStringHora():""));
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+ticket.getCliente().getNombre()+" "+ticket.getCliente().getApellido());
        }
        else {
            etiqueta.setText("");
        }
        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
}
