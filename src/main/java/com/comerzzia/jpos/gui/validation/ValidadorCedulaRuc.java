/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import com.comerzzia.util.ValidadorCedula;

/**
 *
 * @author MGRI
 */
public class ValidadorCedulaRuc extends ValidadorForm {

    private static Logger log = Logger.getMLogger(ValidadorCedulaRuc.class);
    private IFormTipoIdentificacion formTipoIdentificacion;

    public ValidadorCedulaRuc(IFormTipoIdentificacion form) {
        this.formTipoIdentificacion = form;
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if(!((JTextFieldForm)comp).getText().isEmpty()){
            IValidableForm cmp = ((IValidableForm) comp);
            String tipo = formTipoIdentificacion.getTipoIdentificacion();
            if(!tipo.isEmpty()){
                if (!ValidadorCedula.verificarIdEcuador(tipo, cmp.getText())){
                    throw new ValidationFormException("El valor introducido no es una identificación válida.");
                }
            }
        }
    }
}
