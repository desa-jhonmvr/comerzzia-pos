/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MGRI
 */
public class ValidadorHora extends ValidadorForm {

    private static SimpleDateFormat formatoDeHora = new SimpleDateFormat("HH-mm");
    private static SimpleDateFormat formatoDeHora2 = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat formatoDeHora3 = new SimpleDateFormat("HHmm");
    private static Logger log = Logger.getMLogger(ValidadorEntero.class);

    public void validar(Component comp) throws ValidationFormException {
        if (!((JTextFieldForm) comp).getText().isEmpty()) {
            String horaTexto = ((IValidableForm) comp).getText();
            if (!horaTexto.isEmpty()) {
                Date horaActual;
                try {
                    horaActual = formatoDeHora.parse(horaTexto);
                    validaHora(horaActual, (IValidableForm) comp);
                }
                catch (ParseException ex) {
                    try {
                        horaActual = formatoDeHora2.parse(horaTexto);
                        validaHora(horaActual, (IValidableForm) comp);
                    }
                    catch (ParseException ex1) {
                        try {
                            horaActual = formatoDeHora3.parse(horaTexto);
                            validaHora(horaActual, (IValidableForm) comp);

                        }
                        catch (ParseException ex4) {
                            throw new ValidationFormException("La hora introducida no es válida, ha de tener formato HH:mm");
                        }

                    }
                }
                /*}
                else {
                throw new ValidationFormException("La fecha introducida no es válida, ha de tener formato dd-MMM-yyyy");
                }
                 * */

            }
        }
    }

    private void validaHora(Date fechaActual, IValidableForm comp) {
        comp.setText(formatoDeHora2.format(fechaActual));
    }
}
