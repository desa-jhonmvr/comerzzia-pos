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
import javax.swing.JTextField;

/**
 *
 * @author MGRI
 */
public class JTextFieldForm extends JTextField implements IValidableForm {

    private ValidationManager validadorManager;
    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();
    private static InputFormVerifier verifier = new InputFormVerifier();
    private boolean reformatea;
    boolean estaValidacionHabilitada;
    private boolean formatearTexto = false;

    public JTextFieldForm() {
        super();
        reformatea = false;
        addFocusListener();
        validadorManager = new ValidationManager(this);
        this.setInputVerifier(verifier);
        estaValidacionHabilitada = true;
        this.setBorder(new SubtleSquareBorder(true));
    }

    public JTextFieldForm(String cadena) {
        super(cadena);
        reformatea = false;
        addFocusListener();
    }

    public JTextFieldForm(String cadena, int i) {
        super(cadena, i);
        reformatea = false;
        addFocusListener();
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
            if (this.validadorManager != null && this.validadorManager.hasValidadores()) {
                this.validadorManager.validar();
                if (formatearTexto) {
                    aplicaFormato();
                }
            }
        }
    }

    public void setReformatea(boolean b) {
        this.reformatea = true;
    }

    /** Aplica formato MAYMinusculas al texto del campo */
    private void aplicaFormato() {
        String textoAFormatear = this.getText();
        if (!textoAFormatear.isEmpty()) {
            String textoFormateado = "";
            String[] palabras = textoAFormatear.split(" ");
            for (String palabra : palabras) {
                if (!palabra.isEmpty()) {
                    textoFormateado += palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase() + " ";
                }
            }
            if (!textoAFormatear.trim().isEmpty()) {
                textoFormateado = textoFormateado.substring(0, textoFormateado.length() - 1);
                this.setText(textoFormateado);
            }
        }
    }

    @Override
    public void setValidacionHabilitada(boolean estaValidacionHabilitada) {
        this.estaValidacionHabilitada = estaValidacionHabilitada;
    }

    public boolean isFormatearTexto() {
        return formatearTexto;
    }

    public void setFormatearTexto(boolean formatearTexto) {
        this.formatearTexto = formatearTexto;
    }

    public void removeValidadorObligatoriedad() {
        validadorManager.removeValidadorObligatoriedad();
    }
    
    public void removeValidadores() {
        validadorManager.removeValidadores();
    }
}
