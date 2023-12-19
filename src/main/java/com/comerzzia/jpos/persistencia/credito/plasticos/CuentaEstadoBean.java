/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.plasticos;


/**
 *
 * @author amos
 */
public class CuentaEstadoBean {

    private static final byte ANULADA               = 1;
    private static final byte REQUIERE_AUTORIZACION = 2;
    private static final byte VALIDA                = 3;
    
    private byte estado;
    
    public CuentaEstadoBean(String estado, String subestado){
        if (estado == null || subestado == null){
            this.estado = ANULADA;
        }
        else if (estado.equalsIgnoreCase("C")){
            this.estado = ANULADA;
        } 
        else if (estado.equalsIgnoreCase("V")){
            this.estado = VALIDA;
        }
        else{
            this.estado = ANULADA;
        }
    }
    
    public boolean isAnulada(){
        return estado == ANULADA;
    }
    public boolean isRequiereAutorizacion(){
        return estado == REQUIERE_AUTORIZACION;
    }
    public boolean isValida(){
        return estado == VALIDA;
    }
    
    public String getEstadoString() {
        if(isAnulada()){
            return "Anulada";
        }
        if(isValida()){
            return "Valida";
        }
        if(isRequiereAutorizacion()){
            return "Requiere Autorización";
        }
        return "";
    }
}
