/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pocesoMensajes;

import java.io.Serializable;

/**
 *
 * @author CONTABILIDAD
 */
public class EnvioSms implements Serializable{
    
    private String mensaje;
    
    private String cedula;

    public EnvioSms(String mensaje, String cedula) {
        this.mensaje = mensaje;
        this.cedula = cedula;
    }

    
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

   
}
