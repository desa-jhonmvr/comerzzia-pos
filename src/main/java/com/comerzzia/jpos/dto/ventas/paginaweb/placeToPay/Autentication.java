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
public class Autentication implements Serializable{
    
    private static final long serialVersionUID = 430480366780142440L;
    
    private String login;
    private String seed;
    private String nonce;
    private String tranKey;

    public Autentication() {
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTranKey() {
        return tranKey;
    }

    public void setTranKey(String tranKey) {
        this.tranKey = tranKey;
    }
    
    
    
    
    
}
