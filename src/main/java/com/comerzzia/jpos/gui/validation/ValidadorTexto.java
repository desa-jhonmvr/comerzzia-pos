/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import java.awt.Component;
import javax.swing.text.JTextComponent;

/**
 *
 * @author MGRI
 */
public class ValidadorTexto extends ValidadorForm {

    private Integer longMinima;
    private Integer longMaxima;
    private boolean completarConCeros;

    public ValidadorTexto(Integer longMaxima) {
        this.longMaxima = longMaxima;
        this.completarConCeros = false;
    }

    public ValidadorTexto(Integer longMinima, Integer longMaxima) {
        this.longMaxima = longMaxima;
        this.longMinima = longMinima;
        this.completarConCeros = false;
    }

    public ValidadorTexto(Integer longitud, boolean completarConCeros) {
        this.longMaxima = longitud;
        this.longMinima = longitud;
        this.completarConCeros = completarConCeros;
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        IValidableForm cmp = ((IValidableForm) comp);
        int longTexto = cmp.getText().length();
        if (!((JTextComponent) comp).getText().isEmpty()) {
            if (completarConCeros && longMinima > longTexto) {
                String texto = cmp.getText();
                while (longMinima > longTexto) {
                    texto = "0" + texto;
                    longTexto++;
                }
                cmp.setText(texto);
            }

            if (longMinima != null && longMinima > longTexto) {
                if (longMaxima != null && longMinima.equals(longMaxima)) {
                    throw new ValidationFormException("El campo debe tener " + longMaxima + " caracteres");
                }
                else {
                    throw new ValidationFormException("El campo debe tener entre " + longMinima + " y " + longMaxima + " caracteres");
                }
            }
            else if (longMaxima < longTexto) {
                if (longMinima != null) {
                    if (longMaxima != null && longMinima.equals(longMaxima)) {
                        throw new ValidationFormException("El campo debe tener " + longMaxima + " caracteres");
                    }
                    else {
                        throw new ValidationFormException("El campo debe tener entre " + longMinima + " y " + longMaxima + " caracteres");
                    }
                }
                else {
                    throw new ValidationFormException("El campo no debe superar " + longMaxima + " caracteres");

                }
            }

        }
    }
}
