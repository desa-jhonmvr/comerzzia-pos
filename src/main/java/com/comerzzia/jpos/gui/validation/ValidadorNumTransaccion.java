/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.gui.validation;

import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import java.awt.Component;

/**
 *
 * @author SMLM
 */
public class ValidadorNumTransaccion extends ValidadorForm {

    private String tipo;
    public ValidadorNumTransaccion(String tipo){
        this.tipo = tipo;
    }
    
    @Override
    public void validar(Component comp) throws ValidationFormException {
        if (!((JTextFieldForm) comp).getText().isEmpty()) {
            IValidableForm cmp = ((IValidableForm) comp);
            String valorTexto = cmp.getText();
            if(tipo.equals("Factura") || tipo.equals("Notas de Crédito")){
                String[] valores = valorTexto.split("-");
                if(valores.length != 3){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-XXXXXXXX");
                }
                try{
                    if(valores[0].length()>3 || valores[1].length()>3 || valores[2].length()>8){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-XXXXXXXX"); 
                    }
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                    Integer.parseInt(valores[2]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-XXXXXXXX");
                }
            }
            if(tipo.equals("Pagos de Créditos Directos")) {
                String[] valores = valorTexto.split("-");
                if(valores.length != 3){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-DDDD");
                }
                try{
                    if(valores[0].length()>3 || valores[1].length()>3 ){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-DDDD"); 
                    }                    
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                    Integer.parseInt(valores[2]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-DDDD");
                }               
            }
            if(tipo.equals("Pagos de Créditos Temporales")) {
                String[] valores = valorTexto.split("-");
                if(valores.length != 3){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-LLLL");
                }
                try{
                    if(valores[0].length()>3 || valores[1].length()>3 ){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-LLLL"); 
                    }                      
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                    Integer.parseInt(valores[2]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-LLLL");
                }               
            }            
            if ( tipo.equals("Plan Novio")) { 
                String[] valores = valorTexto.split("-");
                if(valores.length != 2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP");
                }
                try{
                    if(valores[0].length()>3 ){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP"); 
                    }                        
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP");
                }                
            }
            if (tipo.equals("Reservaciones")) { 
                String[] valores = valorTexto.split("-");
                if(valores.length != 2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR");
                }
                try{
                    if(valores[0].length()>3 ){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR"); 
                    }                            
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR");
                }                
            }            
            if(tipo.equals("Recibo de Abono de Plan Novio")){
                String[] valores = valorTexto.split("-");
                if(valores.length != 2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP/A");
                }       
                if(valores[0].length()>3 ){
                   throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP/A"); 
                }                   
                String[] abonos = valores[1].split("/");
                if(abonos.length!=2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-PPPP/A");
                }
            }
            if(tipo.equals("Recibo de Abono de Reservación")){
                String[] valores = valorTexto.split("-");
                if(valores.length != 2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR/A");
                } 
                if(valores[0].length()>3 ){
                   throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR/A"); 
                }                    
                String[] abonos = valores[1].split("/");
                if(abonos.length!=2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-RRRR/A");
                }                
            }            
            if (tipo.equals("Bono Efectivo")){   
                String[] valores = valorTexto.split("-");
                if(valores.length != 2){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-BBBBBBBB");
                }
                try{
                    if(valores[0].length()>3 ){
                       throw new ValidationFormException("El formato tiene que ser de tipo TTT-BBBBBBBB"); 
                    }                     
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-BBBBBBBB");
                }                
            }
            if(tipo.equals("GiftCard")) {
                String[] valores = valorTexto.split("-");
                if(valores.length != 3){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-GGGG");
                }
                try{
                    Integer.parseInt(valores[0]);
                    Integer.parseInt(valores[1]);
                    Integer.parseInt(valores[2]);
                } catch (NumberFormatException e){
                    throw new ValidationFormException("El formato tiene que ser de tipo TTT-CCC-GGGG");
                }                
            } 
        }
    }
    
}
