/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;

/**
 *
 * @author MGRI
 */
public class ValidadorObligatoriedad extends ValidadorForm {

    @Override
    public void validar(Component comp) throws ValidationFormException {
        IValidableForm cmp=((IValidableForm)comp);
        if (cmp.getText().isEmpty()){
            throw new ValidationFormException("El campo es obligatorio");
        }
    }
    
}
