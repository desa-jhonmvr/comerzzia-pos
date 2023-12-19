/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ValidationManager {

    private List<ValidadorForm> validadores;
    private Component component;
    private IViewerValidationFormError viewerError;

    public ValidationManager(Component comp) {
        this.component = comp;
        validadores = new LinkedList();
    }

    public void addValidador(ValidadorForm val) {
        validadores.add(val);
    }

    public void validar() throws ValidationFormException {
        if (viewerError != null && validadores!=null) {
            viewerError.clearError();
            try {
                for (ValidadorForm val : validadores) {
                    val.validar(component);
                }
            }
            catch (ValidationFormException e) {
                if (viewerError != null) {
                    viewerError.addError(e);
                    component.requestFocus();
                }
                throw (e);
            }
        }
    }

    public void setViewerError(IViewerValidationFormError viewerError) {
        this.viewerError = viewerError;
    }

    public boolean hasValidadores() {
        return !(this.validadores.isEmpty());
    }
    
    public void removeValidadorObligatoriedad(){
        Iterator<ValidadorForm> it=validadores.iterator();
        while(it.hasNext()){
            if(it.next() instanceof ValidadorObligatoriedad)
                it.remove();
            
        }
    }

    public void removeValidadores() {
        validadores = new LinkedList();
    }
    
}
