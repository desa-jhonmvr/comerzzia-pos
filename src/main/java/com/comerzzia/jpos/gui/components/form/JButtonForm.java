/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components.form;

import com.comerzzia.jpos.gui.eventos.SeleccionaFocusFormListener;
import com.comerzzia.jpos.servicios.login.Apariencia;
import javax.swing.JButton;

/**
 *
 * @author MGRI
 */
public class JButtonForm extends JButton{
    
    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();
    
    public JButtonForm() {
        super();
         addFocusListener();
    }

    public JButtonForm(String desmedpag) {
        super(desmedpag);
        addFocusListener();
    }
    private void addFocusListener() {
        this.addFocusListener(sFL);
    }
    
  
}
