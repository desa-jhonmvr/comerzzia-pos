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
public class UserDTO implements Serializable{
    
    private static final long serialVersionUID = 2046955512674935297L;
     private String id;
    private String email;

    public UserDTO(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
