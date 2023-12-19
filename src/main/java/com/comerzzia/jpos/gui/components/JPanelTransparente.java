/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author MGRI
 */

public class JPanelTransparente extends JPanel{

private float tran= 0.5f;

public JPanelTransparente(){

}

@Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    AlphaComposite old = (AlphaComposite) g2.getComposite();
    g2.setComposite(AlphaComposite.SrcOver.derive(getTran()));
    super.paintComponent(g);
    g2.setComposite(old);
}

public float getTran() {
return tran;
}

public void setTran(float tran) {
this.tran = tran;
}

}