/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.supermaxi;

import java.io.Serializable;

/**
 *
 * @author Mónica Enríquez
 */
public class SolicitudSupermaxiDTO implements Serializable {

    private String uidTicket;
    private String trama;
    private String peticionJson;

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getPeticionJson() {
        return peticionJson;
    }

    public void setPeticionJson(String peticionJson) {
        this.peticionJson = peticionJson;
    }

}
