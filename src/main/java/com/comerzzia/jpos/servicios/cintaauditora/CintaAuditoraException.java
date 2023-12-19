/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cintaauditora;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author Sistemas
 */
public class CintaAuditoraException extends Exception{
    
    private String causa;

    public CintaAuditoraException() {
        super();
    }

    public CintaAuditoraException(String msg) {
        super(msg);
    }

    public CintaAuditoraException(String msg, Throwable e) {
        super(msg, e);
    }

    public CintaAuditoraException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public CintaAuditoraException(String msg, String msgKey) {
        super(msg, msgKey);
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }
}