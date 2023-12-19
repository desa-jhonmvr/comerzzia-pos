/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JDateChooserForm;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.toedter.calendar.JDateChooser;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author MGRI
 */
public class ValidadorFechaHora extends ValidadorForm {

    private static SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy - HH:mm", new Locale("es", "ES"));
    private static SimpleDateFormat formatoDeFecha2 = new SimpleDateFormat("ddMMyyyy HHmm", new Locale("es", "ES"));
    Date fechaInicio;
    Date fechaFin;
    private static Logger log = Logger.getMLogger(ValidadorEntero.class);

    public ValidadorFechaHora() {
        this.fechaFin = null;
        this.fechaInicio = new Date(0);
    }

    public ValidadorFechaHora(Date fechaFin) {
        this.fechaFin = fechaFin;
        this.fechaInicio = new Date(0);
    }

    public ValidadorFechaHora(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if (!((JTextFieldForm) comp).getText().isEmpty()) {
            // Integración del componente JDateChooser
            if (comp instanceof JDateChooserForm) {
                Date fechaActual = ((JDateChooser) comp).getDate();
                if (fechaActual.before(fechaInicio) || ((fechaFin != null) && fechaActual.after(fechaFin))) {
                    throw new ValidationFormException("La fecha introducida no es válida, ha de tener formato dd/MM/yyyy - HH:mm");
                }
            }
            else if (comp instanceof IValidableForm) {
                String fechaActualTexto = ((IValidableForm) comp).getText();
                String diaS;
                String mesS;
                Integer diaI;
                Integer mesI;

                if (!fechaActualTexto.isEmpty()) {       // Si es vacía se guardará como vacia.
                    // if (Fecha.validarFecha(fechaActualTexto, "dd-MMM-yyyy")) {
                    Date fechaActual;
                    try {
                        fechaActual = formatoDeFecha.parse(fechaActualTexto);
                        //formato dd-MMM-yyyy
                        int posicionSeparador = fechaActualTexto.indexOf("/");
                        if (posicionSeparador > 0) {
                            diaI = Integer.parseInt(fechaActualTexto.substring(0, posicionSeparador));
                            if (diaI > 31 || diaI < 1) {
                                throw new ValidationFormException("Ha introducido un día invalido para el formato dd/MM/yyyy - HH:mm");
                            }
                        }

                        validaFechaEntre(fechaActual, (IValidableForm) comp);
                    }
                    catch (ParseException ex) {
                        try {
                            fechaActual = formatoDeFecha2.parse(fechaActualTexto);
                            
                            try {
                                diaI = Integer.parseInt(fechaActualTexto.substring(0, 2));
                            }
                            catch (NumberFormatException e) {
                                diaI = Integer.parseInt(fechaActualTexto.substring(0, 1));
                            }

                            if (diaI > 31 || diaI < 1) {
                                throw new ValidationFormException("Ha introducido un día invalido para el formato ddMMMyyyy hhmm");
                            }
                            validaFechaEntre(fechaActual, (IValidableForm) comp);

                        }
                        catch (ParseException ex1) {

                            throw new ValidationFormException("La fecha introducida no es válida, ha de tener formato dd/MM/yyyy - HH:mm");

                        }
                    }
                }
            }
        }
    }

    private void validaFechaEntre(Date fechaActual, IValidableForm comp) throws ValidationFormException {
        if (comp instanceof IValidableForm) {
            comp.setText(formatoDeFecha.format(fechaActual));
        }
        if (fechaActual.before(fechaInicio)) {
            throw new ValidationFormException("La fecha introducida no es válida, es anterior a la fecha actual");
        }
        else if (((fechaFin != null) && fechaActual.after(fechaFin))) {
            throw new ValidationFormException("La fecha introducida no es válida, es posterior a la fecha máxima permitida");

        }
    }
}
