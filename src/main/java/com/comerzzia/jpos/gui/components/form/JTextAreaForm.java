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
import java.awt.KeyboardFocusManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public class JTextAreaForm extends JTextArea implements IValidableForm {

    private ValidationManager validadorManager;
    private static SeleccionaFocusFormListener sFL = new SeleccionaFocusFormListener();
    private static InputFormVerifier verifier = new InputFormVerifier();
    private boolean estaValidacionHabilitada;
    private boolean reformatea;
    private boolean formatearTexto = false;

    public JTextAreaForm() {
        super();
        addFocusListener();
        validadorManager = new ValidationManager(this);
        this.setInputVerifier(verifier);
        estaValidacionHabilitada = true;
        //this.getComponent(0);
        this.setBorder(new SubtleSquareBorder(true));
        Set<KeyStroke> strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }

    public JTextAreaForm(String cadena) {
        super(cadena);
        addFocusListener();
    }

    public JTextAreaForm(String cadena, int i, int j) {
        super(cadena, i, j);
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
            if (this.validadorManager != null) {
                this.validadorManager.validar();
                if (isFormatearTexto()) {
                    aplicaFormato();
                }
            }
        }
    }

    /** Aplica formato MAYMinusculas al texto del campo */
    private void aplicaFormato() {
        /*
        String textoAFormatear = this.getText();
        String textoFormateado;
        if (!textoAFormatear.isEmpty()) {
            textoFormateado = textoAFormatear.substring(0, 1).toUpperCase() + textoAFormatear.substring(1).toLowerCase();
            this.setText(textoFormateado);
        }
          */
         
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
}
