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
public class SimpleToken implements Serializable {

    private static final long serialVersionUID = -4242198523017672805L;

    private String token;
    private String subtoken;
    private Integer installments;
    private String cvv;

    public SimpleToken() {
    }

    public SimpleToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubtoken() {
        return subtoken;
    }

    public void setSubtoken(String subtoken) {
        this.subtoken = subtoken;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

}
