package com.comerzzia.jpos.gui.validation;

import java.awt.Component;


public class ValidadorObligatoriedadTlfExcluyente extends ValidadorForm {
    Component aExcluir;
    
    public ValidadorObligatoriedadTlfExcluyente(Component aExcluir){
        this.aExcluir=aExcluir;
    }
    @Override
    public void validar(Component comp) throws ValidationFormException {
        IValidableForm cmp=((IValidableForm)comp);
        IValidableForm cmp2=((IValidableForm)aExcluir);
        if (cmp.getText().isEmpty() && cmp2.getText().isEmpty()){
            throw new ValidationFormException("Es obligatório uno de los 2 teléfonos.");
        }
    }
    
}
