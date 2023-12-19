/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.components.form;

import com.comerzzia.jpos.gui.eventos.SeleccionaFocusFormListener;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.InputFormVerifier;
import com.comerzzia.jpos.gui.validation.ValidadorForm;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.gui.validation.ValidationManager;
import com.toedter.components.JSpinField;

/**
 *
 * @author MGRI
 */
public class JSpinFieldForm extends JSpinField implements IValidableForm {

    private ValidationManager validadorManager;
    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();
    private static InputFormVerifier verifier = new InputFormVerifier();
    protected JTextFieldForm textField = new JTextFieldForm();
    private boolean estaValidacionHabilitada;

    public JSpinFieldForm() {
        super();
        addFocusListener();
        validadorManager = new ValidationManager(this);
        this.setInputVerifier(verifier);
        textField.setInputVerifier(verifier);
    }

    private void addFocusListener() {
        this.addFocusListener(sFL);
    }

    @Override
    public void addValidador(ValidadorForm val, IViewerValidationFormError viewer) {
        validadorManager.addValidador(val);
        this.validadorManager.setViewerError(viewer);
        textField.addValidador(val, viewer);
    }

    @Override
    public void validar() throws ValidationFormException {
        if (this.estaValidacionHabilitada) {
            if (this.validadorManager != null) {
                //this.validadorManager.validar();
                this.textField.validar();
            }
        }
    }

    @Override
    public String getText() {
        return (Integer.toString(this.getValue()));
    }

    @Override
    public void selectAll() {
        // No hace nada
    }

    @Override
    public void setValidacionHabilitada(boolean estaValidacionHabilitada) {
        this.estaValidacionHabilitada = estaValidacionHabilitada;
    }

    @Override
    public void setText(String string) {
        // no implementado
    }

    @Override
    public boolean isEditable() {
        return true;
    }
}
