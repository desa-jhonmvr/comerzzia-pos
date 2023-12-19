/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.eventos;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;

/**
 *
 * @author MGRI
 */
public class PierdeFocoAccionForm implements FocusListener {

    Action accion;
    String condicion;

    public PierdeFocoAccionForm(Action accion, String condicion) {
        this.accion = accion;
        this.condicion = condicion;
    }
    
    @Override
    public void focusGained(FocusEvent fe) {
        //nada
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if (condicion == null){
            JTextFieldForm jtf=(JTextFieldForm)fe.getComponent();
        
        }
    }
    
}
