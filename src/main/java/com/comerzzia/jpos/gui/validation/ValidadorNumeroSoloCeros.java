/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;

/**
 *
 * @author amos
 */
public class ValidadorNumeroSoloCeros extends ValidadorForm{

    
    @Override
    public void validar(Component comp) throws ValidationFormException {
        IValidableForm cmp = ((IValidableForm) comp);
        String texto = cmp.getText().replace("0", "");
        if (texto.equals("")){
            throw new ValidationFormException("El campo no puede contener solamente ceros" );
        }
    }
    
    
    
}
