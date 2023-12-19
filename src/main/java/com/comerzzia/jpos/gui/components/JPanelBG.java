/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;


 import java.awt.*;
 import java.io.*;
 import java.net.URL;
 import javax.imageio.ImageIO;
 import javax.swing.JPanel;

/**
 *
 * @author MGRI
 */
public class JPanelBG extends JPanel {


    Image imagen=null;

    public void setBackground(File file) throws IOException{
        if (file==null)
            imagen=null;
        else
            imagen=ImageIO.read(file);
    }

    public void setBackground(URL url) throws IOException{
        if (url==null)
            imagen=null;
        else
            imagen=ImageIO.read(url);
    }

    @Override
    public void paint(Graphics g) {
        
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());
        
        if (imagen!=null){
            g.drawImage(imagen, 0, 0, null);
        }
        Component c;
        for (int i = 0; i < getComponentCount(); i++) {
            c = getComponent(i);
            g.translate(c.getX(), c.getY());
            c.print(g);
            g.translate(-c.getX(), -c.getY());
        }
    }        
 
}
