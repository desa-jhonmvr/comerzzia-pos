/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.dto.ventas.online.DireccionEnvioOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class EntregaDomilicioDTO implements Serializable {

    private static final long serialVersionUID = 8787037712898749673L;

    private String edcDireccion;
    private Long lugId;
    private String codLugSRI;
    //private ClienteDTO cliente;
    private String edcNumeroFactura;
    private DireccionEnvioOnlineDTO direccionEnvio;
    private List<ItemOnlineDTO> items;

    public EntregaDomilicioDTO() {
    }

    public String getEdcDireccion() {
        return edcDireccion;
    }

    public void setEdcDireccion(String edcDireccion) {
        this.edcDireccion = edcDireccion;
    }

    public Long getLugId() {
        return lugId;
    }

    public void setLugId(Long lugId) {
        this.lugId = lugId;
    }

    public String getCodLugSRI() {
        return codLugSRI;
    }

    public void setCodLugSRI(String codLugSRI) {
        this.codLugSRI = codLugSRI;
    }

    public String getEdcNumeroFactura() {
        return edcNumeroFactura;
    }

    public void setEdcNumeroFactura(String edcNumeroFactura) {
        this.edcNumeroFactura = edcNumeroFactura;
    }

    public DireccionEnvioOnlineDTO getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionEnvioOnlineDTO direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public List<ItemOnlineDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemOnlineDTO> items) {
        this.items = items;
    }

}
