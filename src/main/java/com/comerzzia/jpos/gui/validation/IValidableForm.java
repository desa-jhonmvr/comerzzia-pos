/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Color;


/**
 *
 * @author MGRI
 */
public interface IValidableForm {
    
    public static final Color colorError = new Color(236, 121, 121, 255);
    public static final Color colorSeleccion = new Color(153, 202, 253, 255);
    
    public void addValidador(ValidadorForm val, IViewerValidationFormError viewer);
    
    public void validar() throws ValidationFormException;
    public String getText();

    public void setBackground(Color colorFondo);

    public void selectAll();
    
    public void setValidacionHabilitada(boolean estaValidacionHabilitada);

    public void requestFocus();

    public void setText(String string);
    public boolean isEditable();
    public boolean isFocusable();
    public void setFocusable(boolean focusable);
    
}
