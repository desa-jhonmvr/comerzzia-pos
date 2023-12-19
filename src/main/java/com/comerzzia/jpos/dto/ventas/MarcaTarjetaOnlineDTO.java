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
public class MarcaTarjetaOnlineDTO implements Serializable {

    private String codMarcaTarjeta;
    private String nombreMarca;
    private List<MedioPagoDTO> medioPagos;

    public MarcaTarjetaOnlineDTO() {

    }

    public MarcaTarjetaOnlineDTO(String codMarcaTarjeta, String nombreMarca, List<MedioPagoDTO> medioPagos) {
        this.codMarcaTarjeta = codMarcaTarjeta;
        this.nombreMarca = nombreMarca;
        this.medioPagos = medioPagos;
    }

    public String getCodMarcaTarjeta() {
        return codMarcaTarjeta;
    }

    public void setCodMarcaTarjeta(String codMarcaTarjeta) {
        this.codMarcaTarjeta = codMarcaTarjeta;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public List<MedioPagoDTO> getMedioPagos() {
        return medioPagos;
    }

    public void setMedioPagos(List<MedioPagoDTO> medioPagos) {
        this.medioPagos = medioPagos;
    }

}
