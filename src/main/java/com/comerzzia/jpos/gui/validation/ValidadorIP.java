/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.gui.validation;

import java.awt.Component;

/**
 *
 * @author SMLM
 */
public class ValidadorIP extends ValidadorForm {

    @Override
    public void validar(Component comp) throws ValidationFormException {
        IValidableForm cmp = ((IValidableForm) comp);
        String text = cmp.getText();
        if(text == null || text.isEmpty()){
            throw new ValidationFormException("El campo es obligatorio");
        }
        String[] ip = text.split("\\.");
        if(ip.length != 4){
            throw new ValidationFormException("El campo debe tener el formato XXX.XXX.XXX.XXX");
        }
        for(String s:ip){
            int i;
            try{
                 i = Integer.parseInt(s);
            } catch (Exception e){
                throw new ValidationFormException("El campo debe tener el formato XXX.XXX.XXX.XXX");
            }
            if(i<0 || i>255){
                throw new ValidationFormException("Ninguno de los campos puede ser menor que 0 o mayor que 256");
            }
        }
    }
    
}
