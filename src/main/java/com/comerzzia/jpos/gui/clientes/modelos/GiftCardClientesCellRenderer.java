/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.clientes.modelos;

import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author SMLM
 */
public class GiftCardClientesCellRenderer implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        DocumentosBean documento = (DocumentosBean) object;
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
            String idDoc = documento.getIdDocumento().toString();
            if(documento.getTipo().equals(DocumentosBean.GIFTCARD)){
                idDoc=Numero.completaconCeros(documento.getIdDocumento(),8).toString();
            }
            etiqueta.setText(" "+documento.getCodAlmacen()+"-"+documento.getCodCaja()+"-"+idDoc);
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            etiqueta.setText(" "+documento.getFecha().getStringHora());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 2) {
            etiqueta.setText(" "+documento.getReferencia()==null?"":documento.getReferencia());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 3) {
            etiqueta.setText(" $"+documento.getMonto().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 4) {
            etiqueta.setText(" $"+documento.getSaldoGiftCard().toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);            
        }        
        else if (col == 5) {
            if(documento.getEstado().equals("A")){
                etiqueta.setText(" Anulada");
            } else if (documento.getEstado().equals("V")){
                etiqueta.setText(" Vigente");
            } else if (documento.getEstado().equals("L")){
                etiqueta.setText(" Liquidada");
            } else if (documento.getEstado().equals("C")){
                etiqueta.setText(" Caducada");
            }
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }   
              
        return etiqueta;
    }
    
}
