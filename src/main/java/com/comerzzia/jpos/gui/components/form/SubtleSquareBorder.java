/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components.form;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class SubtleSquareBorder implements Border {

    protected int m_w = 4;
    protected int m_h = 4;
    protected Color m_topColor = Color.gray;
    protected Color m_bottomColor = Color.gray;
    protected boolean roundc = false; // Do we want rounded corners on the border?  

    public SubtleSquareBorder(boolean round_corners) {
        roundc = round_corners;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(m_h, m_w, m_h, m_w);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        w = w - 3; //3
        h = h - 3;
        x++;
        y++;
        // Bordes redondeados
        if (roundc) {
            /* FORMA 1  */
            g.setColor(m_topColor);
            g.drawLine(x, y + 2, x, y + h - 2);
            g.drawLine(x + 2, y, x + w - 2, y);
            g.drawLine(x, y + 2, x + 2, y); // Superiror Izquierdo  diagonal
            g.drawLine(x, y + h - 2, x + 2, y + h); // Abajo Izquierda diagonal  
            g.setColor(m_bottomColor);
            g.drawLine(x + w, y + 2, x + w, y + h - 2);
            g.drawLine(x + 2, y + h, x + w - 2, y + h);
            g.drawLine(x + w - 2, y, x + w, y + 2); // Arriba Derecha diagonal  
            g.drawLine(x + w, y + h - 2, x + w - 2, y + h); // Arriba Derecha diagonal  
            
            
            /* FORMA 2 
            int arc = 30;

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 220));
            g2.fillRoundRect(x, y, w, h, arc, arc);

            g2.setStroke(new BasicStroke(3f));
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(x, y, w, h, arc, arc);
            g2.dispose();           
             */
        }
        // Bordes en esquina 
        else {
            g.setColor(m_topColor);
            g.drawLine(x, y, x, y + h);
            g.drawLine(x, y, x + w, y);
            g.setColor(m_bottomColor);
            g.drawLine(x + w, y, x + w, y + h);
            g.drawLine(x, y + h, x + w, y + h);
        }
    }
}
