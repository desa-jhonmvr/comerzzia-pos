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
public class Instrument implements Serializable {

    private static final long serialVersionUID = 7905975621590550473L;

    private SimpleToken token;
    private Credit credit;

    public Instrument() {
    }

    public Instrument(SimpleToken token,Credit credit) {
        this.token = token;
        this.credit =  credit;
    }

    public SimpleToken getToken() {
        return token;
    }

    public void setToken(SimpleToken token) {
        this.token = token;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }
    
    

}
