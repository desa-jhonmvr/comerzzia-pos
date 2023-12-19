/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

/**
 *
 * @author DESARROLLO
 */
public class BonoSupermaxi {

    private String numeroTarjeta;
    private String autorizacion;
    private String valor;

    public BonoSupermaxi(String numeroTarjeta, String autorizacion, String valor) {
        this.numeroTarjeta = numeroTarjeta;
        this.autorizacion = autorizacion;
        this.valor = valor;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
