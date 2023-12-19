/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class CollectRequestInput implements Serializable {

    private static final long serialVersionUID = -1757528301232576330L;

    private String cabIdPedido;
    private CollectRequest collectRequest;
    private String codAlm;

    public CollectRequestInput() {
    }

    public CollectRequestInput(String cabIdPedido, CollectRequest collectRequest, String codAlm) {
        this.cabIdPedido = cabIdPedido;
        this.collectRequest = collectRequest;
        this.codAlm = codAlm;
    }

    public String getCabIdPedido() {
        return cabIdPedido;
    }

    public void setCabIdPedido(String cabIdPedido) {
        this.cabIdPedido = cabIdPedido;
    }

    public CollectRequest getCollectRequest() {
        return collectRequest;
    }

    public void setCollectRequest(CollectRequest collectRequest) {
        this.collectRequest = collectRequest;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

}
