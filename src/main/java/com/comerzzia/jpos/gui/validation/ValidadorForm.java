/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;

/**
 *
 * @author MGRI
 */
public abstract class ValidadorForm {
    public abstract void validar(Component comp) throws ValidationFormException;
}
