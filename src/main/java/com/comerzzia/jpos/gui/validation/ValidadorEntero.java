/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;

/**
 *
 * @author MGRI
 */
public class ValidadorEntero extends ValidadorForm {

    private static Logger log = Logger.getMLogger(ValidadorEntero.class);
    private Integer valorMinimo;
    private Integer valorMaximo;
    private Integer valorPorDefecto;

    public ValidadorEntero() {
        this.valorMaximo = Integer.MAX_VALUE;
        this.valorMinimo = Integer.MIN_VALUE;

    }

    public ValidadorEntero(Integer valorMinimo, Integer valorMaximo) {
        this.valorMaximo = valorMaximo;
        this.valorMinimo = valorMinimo;
        this.valorPorDefecto = null;

    }

    public ValidadorEntero(Integer valorMinimo, Integer valorMaximo, Integer valorPorDefecto) {
        this(valorMinimo, valorMaximo);
        this.valorPorDefecto = valorPorDefecto;
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if(!((JTextFieldForm)comp).getText().isEmpty()){
        IValidableForm cmp = ((IValidableForm) comp);
        try { 
            if (cmp.getText().isEmpty() && this.valorPorDefecto != null && comp.isEnabled() && cmp.isEditable()) {
                cmp.setText(valorPorDefecto.toString());
            }
            if (!cmp.getText().isEmpty()) {  // para campos que no sean obligatorios
                Integer valor = new Integer(cmp.getText());

                if (valor.compareTo(valorMinimo) < 0 || valor.compareTo(valorMaximo) > 0) {
                    if (valorMinimo.compareTo(Integer.MIN_VALUE) == 0) {
                        throw new ValidationFormException("El campo sólo puede tener números");
                    }
                    else {
                        throw new ValidationFormException("El campo ha de ser un número entre " + valorMinimo + " y " + valorMaximo);
                    }
                }
            }
        }
        catch (NumberFormatException e) {
            if (valorMinimo.compareTo(Integer.MIN_VALUE) == 0) {
                throw new ValidationFormException("El campo sólo puede tener números");
            }
            else {
                throw new ValidationFormException("El campo ha de ser un valor entero correcto.");
            }
        }
        }
    }
}
