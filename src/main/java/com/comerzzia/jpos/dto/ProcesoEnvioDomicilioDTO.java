/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto;

import com.comerzzia.jpos.dto.envioDomicilio.EnvioDomicilioDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Mónica Enríquez
 */
public class ProcesoEnvioDomicilioDTO implements Serializable {

    private static final long serialVersionUID = -7180662651115335437L;

    private String factura;
    private String lugSri;
    private ClienteDTO cliente;
    private List<EnvioDomicilioDTO> listEnvioDomicilioDTO;

    public ProcesoEnvioDomicilioDTO() {

    }

    public ProcesoEnvioDomicilioDTO(String factura, String lugSri, List<EnvioDomicilioDTO> listEnvioDomicilioDTO) {
        this.factura = factura;
        this.lugSri = lugSri;
        this.listEnvioDomicilioDTO = listEnvioDomicilioDTO;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getLugSri() {
        return lugSri;
    }

    public void setLugSri(String lugSri) {
        this.lugSri = lugSri;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public List<EnvioDomicilioDTO> getListEnvioDomicilioDTO() {
        return listEnvioDomicilioDTO;
    }

    public void setListEnvioDomicilioDTO(List<EnvioDomicilioDTO> listEnvioDomicilioDTO) {
        this.listEnvioDomicilioDTO = listEnvioDomicilioDTO;
    }

    

}
