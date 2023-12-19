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
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

/**
 *
 * @author MGRI
 */
public class JDateChooserForm extends JDateChooser implements IValidableForm {

    private ValidationManager validadorManager;
    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();
    private static InputFormVerifier verifier = new InputFormVerifier();
    private static SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean estaValidacionHabilitada;

    public JDateChooserForm() {
        super();
        this.addFocusListener();
        validadorManager = new ValidationManager(this);
        this.setInputVerifier(verifier);
        super.jcalendar.setInputVerifier(verifier);
    }

    private void addFocusListener() {
        this.addFocusListener(sFL);
    }

    @Override
    public void addValidador(ValidadorForm val, IViewerValidationFormError viewer) {
        validadorManager.addValidador(val);
        this.validadorManager.setViewerError(viewer);
    }

    @Override
    public void validar() throws ValidationFormException {
        if (this.estaValidacionHabilitada) {
            if (this.validadorManager != null) {
                this.validadorManager.validar();
            }
        }
    }

    @Override
    public String getText() {
        return formateador.format(this.getDate());
    }

    @Override
    public void selectAll() {
        // no hace nada       
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
