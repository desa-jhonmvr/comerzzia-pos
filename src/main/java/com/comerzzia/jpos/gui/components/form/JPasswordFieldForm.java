/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components.form;

import com.comerzzia.jpos.gui.eventos.SeleccionaFocusFormListener;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.gui.validation.ValidationManager;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.InputFormVerifier;
import com.comerzzia.jpos.gui.validation.ValidadorForm;
import javax.swing.JPasswordField;

/**
 *
 * @author MGRI
 */
public class JPasswordFieldForm extends JPasswordField implements IValidableForm {

    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();

    public JPasswordFieldForm() {
        super();
        this.setBorder(new SubtleSquareBorder(true));
        addFocusListener();

    }

    private void addFocusListener() {
        this.addFocusListener(sFL);
    }

    @Override
    public void addValidador(ValidadorForm val, IViewerValidationFormError viewer) {
        // Para futuras necesidades
    }

    @Override
    public void validar() throws ValidationFormException {
        // Para futuras necesidades
    }

    @Override
    public void setValidacionHabilitada(boolean estaValidacionHabilitada) {
        // Para futuras necesidades
    }
}
