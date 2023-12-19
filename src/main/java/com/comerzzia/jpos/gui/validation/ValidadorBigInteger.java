/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.math.BigInteger;

/**
 *
 * @author MGRI
 */
public class ValidadorBigInteger extends ValidadorForm {

    private static Logger log = Logger.getMLogger(ValidadorEntero.class);

    public ValidadorBigInteger() {
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if (!((JTextFieldForm) comp).getText().isEmpty()) {
            IValidableForm cmp = ((IValidableForm) comp);
            try {
                if (!cmp.getText().isEmpty()) {
                    BigInteger valor = new BigInteger(cmp.getText());
                }
            }
            catch (Exception e) {
                throw new ValidationFormException("Debe introducir un valor numérico");
            }
        }
    }
}
