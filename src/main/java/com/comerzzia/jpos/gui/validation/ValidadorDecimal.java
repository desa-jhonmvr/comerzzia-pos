/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class ValidadorDecimal extends ValidadorForm {

    private static Logger log = Logger.getMLogger(ValidadorDecimal.class);
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;
    private Integer numDecimales;

    public ValidadorDecimal() {
        this.valorMaximo = BigDecimal.ZERO;
        this.valorMinimo = BigDecimal.ZERO;
        this.numDecimales = new Integer(2);
    }
    
    public ValidadorDecimal(BigDecimal valorMaximo, BigDecimal valorMinimo, Integer numDecimales) {
        this.valorMaximo = valorMaximo;
        this.valorMinimo = valorMinimo;
        this.numDecimales = numDecimales;
    }

    @Override
    public void validar(Component comp) throws ValidationFormException {
        if(!((JTextFieldForm)comp).getText().isEmpty()){
        IValidableForm cmp = ((IValidableForm) comp);
        try {
            BigDecimal valor = new BigDecimal(cmp.getText());
            if (valor.scale() > this.numDecimales.intValue() && valor.compareTo(BigDecimal.ZERO) != 0) {
                throw new ValidationFormException("El valor ha de tener con un máximo de " + this.numDecimales + " cifras decimales");
            }
            if (valorMinimo != null && valorMaximo!= null && (valor.compareTo(valorMinimo) < 0 || valor.compareTo(valorMaximo) > 0)) {
                throw new ValidationFormException("El campo ha de tener un valor entre " + valorMinimo + " y " + valorMaximo);
            }
            else if (valorMinimo !=null && valorMaximo==null && valor.compareTo(valorMinimo) < 0){
                throw new ValidationFormException("El número introducido ha de ser mayor a " + valorMinimo );

            }
            else if (valorMinimo ==null && valorMaximo!=null && valor.compareTo(valorMaximo) > 0){
                throw new ValidationFormException("El número introducido ha de ser menor a " + valorMaximo );

            }
        }
        catch (NumberFormatException e) {
            throw new ValidationFormException("El campo ha de ser un valor decimal correcto. ");
        }
        catch (ValidationFormException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("error de validacion no determinado para la entrada: " + cmp.getText(), e);
        }
        }
    }
}
