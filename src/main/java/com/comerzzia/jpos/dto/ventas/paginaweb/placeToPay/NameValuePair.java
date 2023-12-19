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
public class NameValuePair implements Serializable{
    
    private static final long serialVersionUID = 2719103477677855020L;
    
    private String keyword;
    private String value;
    private String displayOn;

    public NameValuePair() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayOn() {
        return displayOn;
    }

    public void setDisplayOn(String displayOn) {
        this.displayOn = displayOn;
    }
    
    
    
}
