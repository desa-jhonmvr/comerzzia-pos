/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.eventos;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author MGRI
 */
public class SeleccionaFocusListener implements FocusListener {

    Color colorFondo = null;
    //private static final Color colorDefecto = new Color(255, 255, 255, 255);
    private static final Color colorSeleccion = new Color(153, 202, 253, 255);

    @Override
    public void focusGained(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof JTextField) {
            JTextField txt = (JTextField) cmpnt;
            colorFondo = txt.getBackground();  //guardo el color de fondo
            txt.setBackground(colorSeleccion);
            txt.selectAll();

        } else if (cmpnt instanceof JTextArea) {
            JTextArea txt = (JTextArea) cmpnt;
            colorFondo = txt.getBackground();  //guardo el color de fondo
            txt.setBackground(colorSeleccion);
            txt.selectAll();

        } else if (cmpnt instanceof JComboBox) {
            JComboBox txt = (JComboBox) cmpnt;
            colorFondo = txt.getBackground();  //guardo el color de fondo
            txt.setBackground(colorSeleccion);
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof JTextField) {
            JTextField txt = (JTextField) cmpnt;
            txt.setBackground(colorFondo);

        } else if (cmpnt instanceof JTextArea) {
            JTextArea txt = (JTextArea) cmpnt;            
            txt.setBackground(colorFondo);

        } else if (cmpnt instanceof JComboBox) {
            JComboBox txt = (JComboBox) cmpnt;            
            txt.setBackground(colorFondo);
        }
    }
}
