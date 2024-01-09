/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

/**
 *
 * @author CONTABILIDAD
 */
public class ValorIndice {
   private String valor;
   private String indice;

   private String cuotaAPagar;
   private String interes;
   private String capitalAmortizado;
   private String capitalVivo;
   private String numeroCuota;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getCuotaAPagar() {
        return cuotaAPagar;
    }

    public void setCuotaAPagar(String cuotaAPagar) {
        this.cuotaAPagar = cuotaAPagar;
    }

    public String getInteres() {
        return interes;
    }

    public void setInteres(String interes) {
        this.interes = interes;
    }

    public String getCapitalAmortizado() {
        return capitalAmortizado;
    }

    public void setCapitalAmortizado(String capitalAmortizado) {
        this.capitalAmortizado = capitalAmortizado;
    }

    public String getCapitalVivo() {
        return capitalVivo;
    }

    public void setCapitalVivo(String capitalVivo) {
        this.capitalVivo = capitalVivo;
    }

    public String getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(String numeroCuota) {
        this.numeroCuota = numeroCuota;
    }
}
