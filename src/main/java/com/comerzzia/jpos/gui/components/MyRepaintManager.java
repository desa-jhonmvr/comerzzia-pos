/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components;

import com.comerzzia.jpos.gui.JIdentCliente;
import com.comerzzia.jpos.gui.JPrincipal;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.RepaintManager;

/**
 *
 * @author amos
 */
public class MyRepaintManager extends RepaintManager {

    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
        //System.out.println(c);
        if (c instanceof JIdentCliente){
            ((JPanelImagenFondo)c).setRepintarImagenFondo(true);
        }
        else if (c instanceof JComboBox || c instanceof JLabel || c instanceof JMultilineLabel ){
            JPrincipal.repintarFondo();
        }
        super.addDirtyRegion(c, x, y, w, h);
    }

    @Override
    public void addDirtyRegion(Window window, int i, int i1, int i2, int i3) {
        JPrincipal.repintarFondo();
        super.addDirtyRegion(window, i, i1, i2, i3);
    }

    @Override
    public void addDirtyRegion(Applet applet, int i, int i1, int i2, int i3) {
        super.addDirtyRegion(applet, i, i1, i2, i3);
    }

    @Override
    public synchronized void addInvalidComponent(JComponent jc) {
        super.addInvalidComponent(jc);
    }

    @Override
    public Rectangle getDirtyRegion(JComponent jc) {
        return super.getDirtyRegion(jc);
    }

    @Override
    public Dimension getDoubleBufferMaximumSize() {
        return super.getDoubleBufferMaximumSize();
    }

    @Override
    public Image getOffscreenBuffer(Component cmpnt, int i, int i1) {
        return super.getOffscreenBuffer(cmpnt, i, i1);
    }

    @Override
    public Image getVolatileOffscreenBuffer(Component cmpnt, int i, int i1) {
        return super.getVolatileOffscreenBuffer(cmpnt, i, i1);
    }

    @Override
    public boolean isCompletelyDirty(JComponent jc) {
        return super.isCompletelyDirty(jc);
    }

    @Override
    public boolean isDoubleBufferingEnabled() {
        return super.isDoubleBufferingEnabled();
    }

    @Override
    public void markCompletelyClean(JComponent jc) {
        super.markCompletelyClean(jc);
    }

    @Override
    public void markCompletelyDirty(JComponent jc) {
        super.markCompletelyDirty(jc);
    }

    @Override
    public void paintDirtyRegions() {
        super.paintDirtyRegions();
    }

    @Override
    public synchronized void removeInvalidComponent(JComponent jc) {
        super.removeInvalidComponent(jc);
    }

    @Override
    public void setDoubleBufferMaximumSize(Dimension dmnsn) {
        super.setDoubleBufferMaximumSize(dmnsn);
    }

    @Override
    public void setDoubleBufferingEnabled(boolean bln) {
        super.setDoubleBufferingEnabled(bln);
    }

    @Override
    public synchronized String toString() {
        return super.toString();
    }

    @Override
    public void validateInvalidComponents() {
        super.validateInvalidComponents();
    }
    
    
    
}