/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.plasticos;

/**
 *
 * @author amos
 */
public class PlasticoEstadoBean {

    private static final byte ANULADA               = 1;
    private static final byte REQUIERE_AUTORIZACION = 2;
    private static final byte VALIDA                = 3;
    
    private byte estado;
    private String estadoCta;
    
    public PlasticoEstadoBean(String estado, String subestado){
        if (estado == null || subestado == null){
            this.estado = ANULADA;
        }
        else if (estado.equalsIgnoreCase("C")){
            this.estado = ANULADA;
        } // Si el estado no es C, será A o G, en ambos casos válido. Preguntamos por el subestado
        else if (subestado.equalsIgnoreCase("NB")){
            this.estado = VALIDA;
        }
        else if (subestado.equalsIgnoreCase("B")){
            this.estado = REQUIERE_AUTORIZACION;
        }
        else{
            this.estado = ANULADA;
        }
    }

    public PlasticoEstadoBean() {
       
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

    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public String getEstadoCta() {
        return estadoCta;
    }

    public void setEstadoCta(String estadoCta) {
        this.estadoCta = estadoCta;
    }
    
    
    
}
