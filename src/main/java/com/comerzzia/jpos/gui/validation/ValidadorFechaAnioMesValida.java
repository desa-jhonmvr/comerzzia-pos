/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.validation;

import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MGRI
 */
public class ValidadorFechaAnioMesValida  extends ValidadorForm {
    
    private static Logger log = Logger.getMLogger(ValidadorEntero.class);
    private static SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyMM");
    boolean validarMenrFechaActual = false;
    
    public ValidadorFechaAnioMesValida() {       
        validarMenrFechaActual = false;
    }
    public ValidadorFechaAnioMesValida(boolean validarMenrFechaActual) {       
        this.validarMenrFechaActual = validarMenrFechaActual;
    }
    
    @Override
    public void validar(Component comp) throws ValidationFormException {
       //quitamos validaccion año mes
//        Date fechaTarjeta;
//            try {                
//                String fechaActualTexto = ((IValidableForm) comp).getText();
//                if (!fechaActualTexto.isEmpty()) { 
//                  if (fechaActualTexto.length()!=4){
//                      throw new ValidationFormException("El formato de caducidad  es AñoMes (AAMM)");
//                  }
//                  else{
//                      Integer parteAA = new Integer(fechaActualTexto.substring(0,2));
//                      Integer parteMM = new Integer(fechaActualTexto.substring(2,4));
//                      if (parteAA<0 || parteMM<1 || parteMM>12){
//                          throw new ValidationFormException("El formato de caducidad es AñoMes (AAMM)");
//
//                      }
//                      if (validarMenrFechaActual){
//                          Fecha hoy = new Fecha();
//                          //el hoy.getMes()+1 porque getMes() devuelve de 0-11
//                          if ((hoy.getAño()> (2000+parteAA) )||
//                              ( hoy.getAño() == (2000+parteAA) && hoy.getMes()+1>parteMM )){
//                              throw new ValidationFormException("La tarjeta está caducada");
//                          }
//                      }
//                  }                  
//                }
//            }
//            catch (ValidationFormException e) {
//                throw e;
//            } 
//            catch (Exception e) {
//                throw new ValidationFormException("El formato de caducidad es AñoMes (AAMM)");
//            }        
    }
}
    

