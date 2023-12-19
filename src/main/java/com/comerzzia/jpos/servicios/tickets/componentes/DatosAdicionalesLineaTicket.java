/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class DatosAdicionalesLineaTicket {

    private BigDecimal descuento;
    private BigDecimal incremento;
    private boolean recogidaPosterior;
    private boolean envioDomicilio;
    private boolean pedidoFacturado;
    private boolean intercambio;
    private String autorizador;

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public boolean isEnvioDomicilio() {
        return envioDomicilio;
    }

    public void setEnvioDomicilio(boolean envioDomicilio) {
        this.envioDomicilio = envioDomicilio;
    }

    public boolean getEnvioDomicilio() {
        return envioDomicilio;                                                                                                                  
    }

    public boolean isRecogidaPosterior() {
        return recogidaPosterior;
    }

    public void setRecogidaPosterior(boolean recogidaPosterior) {
        this.recogidaPosterior = recogidaPosterior;
    }

    public void setAutorizador(String autorizadorDescuento) {
        this.autorizador = autorizadorDescuento;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public boolean isPedidoFacturado() {
        return pedidoFacturado;
    }

    public void setPedidoFacturado(boolean pedidoFacturado) {
        this.pedidoFacturado = pedidoFacturado;
    }

    public BigDecimal getIncremento() {
        return incremento;
    }

    public void setIncremento(BigDecimal incremento) {
        this.incremento = incremento;
    }

    public boolean isIntercambio() {
        return intercambio;
    }

    public void setIntercambio(boolean intercambio) {
        this.intercambio = intercambio;
    }
    
}
