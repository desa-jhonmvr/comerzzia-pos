/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JDateChooserForm;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.toedter.calendar.JDateChooser;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MGRI
 */
public class ValidadorFechaBebe extends ValidadorForm {

    private static SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd-MMM-yyyy");
    private static SimpleDateFormat formatoDeFecha2 = new SimpleDateFormat("ddMMMyyyy");
    private static SimpleDateFormat formatoDeFecha3 = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat formatoDeFecha4 = new SimpleDateFormat("ddMMyyyy");
    Fecha fechaInicio;
    Fecha fechaFin;
    private static Logger log = Logger.getMLogger(ValidadorEntero.class);

    public ValidadorFechaBebe() {
        this.fechaInicio = new Fecha();
        this.fechaFin=new Fecha();
        fechaFin.sumaMeses(10);
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
       if(!((JTextFieldForm)comp).getText().isEmpty()){
        // Integración del componente JDateChooser
        if (comp instanceof JDateChooserForm) {
            Date fechaActual = ((JDateChooser) comp).getDate();
            Fecha fechaComponente=new Fecha(fechaActual);
            if (((fechaFin != null) && fechaComponente.despues(fechaFin))) {
                throw new ValidationFormException("La fecha introducida no es válida, ha de tener formato dd-MMM-yyyy y no ser mayor a 10 meses desde la fecha actual.");
            }
        }
        else if (comp instanceof IValidableForm) {
            String fechaActualTexto = ((IValidableForm) comp).getText();
            if (!fechaActualTexto.isEmpty()) {       // Si es vacía se guardará como vacia.
               // if (Fecha.validarFecha(fechaActualTexto, "dd-MMM-yyyy")) {
                    Date fechaActual;
                    Fecha fechaComponente;
                    try {
                        fechaActual = formatoDeFecha.parse(fechaActualTexto);
                        fechaComponente=new Fecha(fechaActual);
                        validaFechaEntre(fechaComponente,(IValidableForm)comp);
                    }
                    catch (ParseException ex) {
                        try {
                            fechaActual = formatoDeFecha2.parse(fechaActualTexto);
                            fechaComponente=new Fecha(fechaActual);
                            validaFechaEntre(fechaComponente,(IValidableForm)comp);
                        }
                        catch (ParseException ex1) {
                            try {
                                fechaActual = formatoDeFecha3.parse(fechaActualTexto);
                                fechaComponente=new Fecha(fechaActual);
                                validaFechaEntre(fechaComponente,(IValidableForm)comp);
                            }
                            catch (ParseException ex2) {
                                try {
                                    fechaActual = formatoDeFecha4.parse(fechaActualTexto);
                                    fechaComponente=new Fecha(fechaActual);
                                    validaFechaEntre(fechaComponente,(IValidableForm)comp);
                                }
                                catch (ParseException ex4) {
                                      throw new ValidationFormException("La fecha introducida no es válida, ha de tener formato dd-MMM-yyyy");
                                }
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
    }

    private void validaFechaEntre(Fecha fechaActual,IValidableForm comp) throws ValidationFormException {
        if (comp instanceof IValidableForm){
            comp.setText(formatoDeFecha.format(fechaActual.getDate()));
        }
        if (fechaActual.despues(fechaFin)) {
            throw new ValidationFormException("La fecha introducida no es válida, la fecha no puede ser mayor a "+fechaFin.getString()+".");
        }
    }
}
