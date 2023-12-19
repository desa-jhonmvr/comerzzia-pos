/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos.combo;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author MGRI
 */
public class ComboGenericoRenderer implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
     
    @Override
    public Component getListCellRendererComponent(JList list, Object valor, int index, boolean isSelected, boolean cellHasFocus) {
    
    //creamos la etiqueta por defecto
    JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, valor, index,isSelected, cellHasFocus);
    
    if (valor instanceof IComboComponent){
    renderer.setText(((IComboComponent)valor).getComboTexto());
    }
    
    return renderer;
    }
    
}
