/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Admin
 */
public class MyBorder implements Border {

    private Border normal = BorderFactory.createEmptyBorder();
    private Border light = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), normal);
    private Border cambios = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), normal);

    public MyBorder(Border b) {
        super();
        normal = b;
    }

    @Override
    public void paintBorder(Component cmpnt, Graphics grphcs, int i, int i1, int i2, int i3) {
        if (cmpnt.isFocusOwner() ) {
            light.paintBorder(cmpnt, grphcs, i, i1, i2, i3);
        } else if (cmpnt instanceof JTextField &&((JTextField) cmpnt).getToolTipText()!=null&& ((JTextField) cmpnt).getToolTipText().contains("Cambios")) {
            cambios.paintBorder(cmpnt, grphcs, i, i1, i2, i3);
        } else {
            normal.paintBorder(cmpnt, grphcs, i, i1, i2, i3);
        }
    }

    @Override
    public Insets getBorderInsets(Component cmpnt) {
        Insets in;
        if (cmpnt.isFocusOwner() ) {
            in = light.getBorderInsets(cmpnt);
        } else if (cmpnt instanceof JTextField &&((JTextField) cmpnt).getToolTipText()!=null&& ((JTextField) cmpnt).getToolTipText().contains("Cambios")) {
            in=cambios.getBorderInsets(cmpnt);
        } else {
            in = normal.getBorderInsets(cmpnt);
        }
        return in;
    }

    @Override
    public boolean isBorderOpaque() {

        return normal.isBorderOpaque();

    }
}
