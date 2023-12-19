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
public class ValidadorCelularEcuador extends ValidadorForm {

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if(!((JTextFieldForm)comp).getText().isEmpty()){
        IValidableForm cmp = ((IValidableForm) comp);
        String valorTexto = cmp.getText();
        if (!valorTexto.equals("")&&
            valorTexto.length() == 10) {
            try {

                Integer valorNumerico = new Integer(valorTexto);
                if (!valorTexto.substring(0, 1).equals("0")) {
                    throw new ValidationFormException("El campo ha de ser un celular válido que empiece por 09");
                }
                if (!valorTexto.substring(1, 2).equals("9")) {
                    throw new ValidationFormException("El campo ha de ser un celular válido que empiece por 09");
                }
            }
            catch (NumberFormatException e) {
                throw new ValidationFormException("El campo ha de ser un celular válido que empiece por 09");
            }
        }
        else{
            if(!valorTexto.equals(""))
        throw new ValidationFormException("El campo celular ha de ser de 10 dígitos");
        }
        }
    }
}
