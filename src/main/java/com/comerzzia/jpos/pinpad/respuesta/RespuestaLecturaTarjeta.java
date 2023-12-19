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
public class RespuestaLecturaTarjeta extends RespuestaPinPad {
    private String numeroTarjeta;
    private String valorFijo;
    private String numeroTarjetaTruncada;
//     //Se Agrega para plan D
//    private String nomeroTarjetaEncriptado;
//    private String codRedAdquirienteCorriente;
//    private String codRedAdquirienteDiferido;
//    //Se Agrega para plan D
//    private String fechaVenTarjeta;

//    public String getNomeroTarjetaEncriptado() {
//        return nomeroTarjetaEncriptado;
//    }
//
//    public void setNomeroTarjetaEncriptado(String nomeroTarjetaEncriptado) {
//        this.nomeroTarjetaEncriptado = nomeroTarjetaEncriptado;
//    }
//
//    public String getCodRedAdquirienteCorriente() {
//        return codRedAdquirienteCorriente;
//    }
//
//    public void setCodRedAdquirienteCorriente(String codRedAdquirienteCorriente) {
//        this.codRedAdquirienteCorriente = codRedAdquirienteCorriente;
//    }
//
//    public String getCodRedAdquirienteDiferido() {
//        return codRedAdquirienteDiferido;
//    }
//
//    public void setCodRedAdquirienteDiferido(String codRedAdquirienteDiferido) {
//        this.codRedAdquirienteDiferido = codRedAdquirienteDiferido;
//    }
//
//    public String getFechaVenTarjeta() {
//        return fechaVenTarjeta;
//    }
//
//    public void setFechaVenTarjeta(String fechaVenTarjeta) {
//        this.fechaVenTarjeta = fechaVenTarjeta;
//    }
//    
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getValorFijo() {
        return valorFijo;
    }

    public void setValorFijo(String valorFijo) {
        this.valorFijo = valorFijo;
    }

    public String getNumeroTarjetaTruncada() {
        return numeroTarjetaTruncada;
    }

    public void setNumeroTarjetaTruncada(String numeroTarjetaTruncada) {
        this.numeroTarjetaTruncada = numeroTarjetaTruncada;
    }
    
}
