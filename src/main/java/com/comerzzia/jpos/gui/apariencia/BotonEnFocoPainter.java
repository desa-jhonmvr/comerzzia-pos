/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.apariencia;
import com.sun.java.swing.Painter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author MGRI
 */
public class BotonEnFocoPainter  implements Painter {

    @Override
    public void paint(Graphics2D g, Object t, int w, int h) {
        g.setStroke(new BasicStroke(2f));
        g.setColor(new Color(226,255,208));
        g.fillOval(1, 1, w - 3, h - 3);
        g.setColor(new Color(34,49,248));
        g.drawOval(1, 1, w - 3, h - 3);
    }
}

