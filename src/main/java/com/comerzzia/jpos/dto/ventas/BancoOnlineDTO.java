/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class BancoOnlineDTO implements Serializable {

    private static final long serialVersionUID = -5372030123816263341L;

    private String codBan;
    private String nombreBanco;
    private List<MedioPagoDTO> medioPagos;

    public BancoOnlineDTO() {
    }

    public String getCodBan() {
        return codBan;
    }

    public void setCodBan(String codBan) {
        this.codBan = codBan;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public List<MedioPagoDTO> getMedioPagos() {
        return medioPagos;
    }

    public void setMedioPagos(List<MedioPagoDTO> medioPagos) {
        this.medioPagos = medioPagos;
    }

}
