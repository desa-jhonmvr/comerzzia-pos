/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class CollectRequest implements Serializable{
    
    private static final long serialVersionUID = 2704667350783758498L;
    
    private Autentication auth;
    private Person payer;
    private PaymentRequest payment;
    private Instrument instrument ;
    private List<ItemPlaceToPay> item; 

    public CollectRequest() {
    }

    public Autentication getAuth() {
        return auth;
    }

    public void setAuth(Autentication auth) {
        this.auth = auth;
    }

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
    }

    public PaymentRequest getPayment() {
        return payment;
    }

    public void setPayment(PaymentRequest payment) {
        this.payment = payment;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public List<ItemPlaceToPay> getItem() {
        return item;
    }

    public void setItem(List<ItemPlaceToPay> item) {
        this.item = item;
    }
    
}
