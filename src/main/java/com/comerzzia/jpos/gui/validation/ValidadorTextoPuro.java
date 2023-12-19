/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;

/**
 *
 * @author amos
 */
public class ValidadorTextoPuro extends ValidadorTexto{

    public ValidadorTextoPuro(Integer longitud, boolean completarConCeros) {
        super(longitud, completarConCeros);
    }

    public ValidadorTextoPuro(Integer longMinima, Integer longMaxima) {
        super(longMinima, longMaxima);
    }

    public ValidadorTextoPuro(Integer longMaxima) {
        super(longMaxima);
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        super.validar(comp);
        IValidableForm cmp = ((IValidableForm) comp);
        String texto = cmp.getText();
        if (texto.contains("&") 
                || texto.contains("<")
                || texto.contains(">")
                || texto.contains("%")
                || texto.contains("\\")
                || texto.contains("/")
                || texto.contains("[")
                || texto.contains("]")
                || texto.contains("{")
                || texto.contains("}")
                ){
            throw new ValidationFormException("El campo no puede contener caracteres extraños: <,>,\\,/,[,],{,},&,% " );
        }
    }
    
    
    
}
