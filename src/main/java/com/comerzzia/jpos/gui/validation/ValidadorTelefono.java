/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import java.awt.Component;

/**
 *
 * @author MGRI
 */
public class ValidadorTelefono extends ValidadorForm {

    public boolean esHTML = true;
    
    @Override
    public void validar(Component comp) throws ValidationFormException {
        if (!((JTextFieldForm) comp).getText().isEmpty()) {
            IValidableForm cmp = ((IValidableForm) comp);
            String valorTexto = cmp.getText();
            if (!valorTexto.equals("")
                    && (valorTexto.length() == 10 || valorTexto.length() == 9)) {
                try {
                    if (valorTexto.length() == 10) {
                        if (!valorTexto.substring(0, 1).equals("0")) {
                            throw new ValidationFormException("Un celular ha de comenzar por 09.");
                        }
                        if (!valorTexto.substring(1, 2).equals("9")) {
                            throw new ValidationFormException("Un celular ha de comenzar por 09");
                        }                        
                    }
                    else if (valorTexto.length() == 9){
                        if (!valorTexto.substring(0, 1).equals("0")) {
                            throw new ValidationFormException("El teléfono ha de ir precedido del prefijo");
                        }
                        if (valorTexto.substring(0, 2).equals("09")) {
                            throw new ValidationFormException("El celular debe tener una longitud de 10 dígitos.");
                        }
                    }
                    
                }
                catch (NumberFormatException e) {
                    if(esHTML) {
                        throw new ValidationFormException("<html>Ha de introducir un celular o teléfono fijo válidos<br/>(tener en cuenta prefijo)</html>");
                    } else {
                        throw new ValidationFormException("Ha de introducir un celular o teléfono fijo válidos(tener en cuenta prefijo)");
                    }
                }
            }
            else {
                if(esHTML) {
                    throw new ValidationFormException("<html>Ha de introducir un celular o teléfono fijo válidos<br/>(tener en cuenta prefijo)</html>");
                } else {
                    throw new ValidationFormException("Ha de introducir un celular o teléfono fijo válidos(tener en cuenta prefijo)");
                }
            }
        }
    }
}
