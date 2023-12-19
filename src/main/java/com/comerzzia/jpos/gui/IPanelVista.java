/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui;


import javax.swing.JComponent;


/**
 *
 * @author MGRI
 */
public interface IPanelVista {
    
    public abstract String getTitle();
    public abstract void activate() throws Exception;
    public abstract boolean deactivate();
    public abstract JComponent getComponent();
}


