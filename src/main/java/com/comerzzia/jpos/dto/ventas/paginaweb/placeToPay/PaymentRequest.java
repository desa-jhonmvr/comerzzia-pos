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
public class PaymentRequest implements Serializable{
    
    private static final long serialVersionUID = -2884549228818468609L;
    
    private String reference ;
    private String description;
    private Amount amount;
    private Boolean allowPartial;
    private Person shipping;
    private Items items;
    private List<NameValuePair> fields;
    private Recurring recurring;
    private Boolean subscribe;

    public PaymentRequest() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Boolean getAllowPartial() {
        return allowPartial;
    }

    public void setAllowPartial(Boolean allowPartial) {
        this.allowPartial = allowPartial;
    }

    public Person getShipping() {
        return shipping;
    }

    public void setShipping(Person shipping) {
        this.shipping = shipping;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public List<NameValuePair> getFields() {
        return fields;
    }

    public void setFields(List<NameValuePair> fields) {
        this.fields = fields;
    }

    public Recurring getRecurring() {
        return recurring;
    }

    public void setRecurring(Recurring recurring) {
        this.recurring = recurring;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }
    
    
    
    
    
}
