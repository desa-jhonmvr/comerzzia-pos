/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.respuesta;

/**
 *
 * @author SMLM
 */
public class RespuestaConsultaTarjeta extends RespuestaPinPad {
    private String numeroTarjeta;
    private String binTarjeta;
//    //plan D
//    private String fechaVenTarjeta;
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getBinTarjeta() {
        return binTarjeta;
    }

    public void setBinTarjeta(String binTarjeta) {
        this.binTarjeta = binTarjeta;
    }

//    public String getFechaVenTarjeta() {
//        return fechaVenTarjeta;
//    }
//
//    public void setFechaVenTarjeta(String fechaVenTarjeta) {
//        this.fechaVenTarjeta = fechaVenTarjeta;
//    }
    
}
