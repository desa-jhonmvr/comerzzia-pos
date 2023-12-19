/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 *
 * @author MGRI
 */
public class InputFormVerifier extends InputVerifier{

    
    @Override
    public boolean verify(JComponent jc) {
       boolean result=false;
        IValidableForm cmp=(IValidableForm)jc;
        try{
            cmp.validar();
            cmp.setBackground(IValidableForm.colorSeleccion);
            return true;
        }catch (ValidationFormException e){
            cmp.setBackground(IValidableForm.colorError);
            return false;
        }        
       
    }
    
}
