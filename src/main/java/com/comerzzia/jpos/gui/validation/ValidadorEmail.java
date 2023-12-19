/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.util.base.ValidadorTPV;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;


/**
 *
 * @author MGRI
 */
public class ValidadorEmail extends ValidadorForm{

    private static Logger log = Logger.getMLogger(ValidadorEmail.class);
    
    @Override
    public void validar(Component comp) throws ValidationFormException {
        if ((!((IValidableForm)comp).getText().equals(""))&&
            !ValidadorTPV.validarEmail(((IValidableForm)comp).getText())){
            throw new ValidationFormException("El email introducido no es válido");
        }
    }
    
}
