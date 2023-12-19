/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.paymentez;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class CardDTO implements Serializable{
    
    private static final long serialVersionUID = 3228928934414446045L;
   
    private String token;

    public CardDTO() {
    }

    public CardDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
}
