/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

/**
 *
 * @author MGRI
 */
public interface IViewerValidationFormError {
    public void addError(ValidationFormException e);

    public void clearError();
}
