/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import java.io.Serializable; 
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class BdgRequerimientoDTO implements Serializable {

    private static final long serialVersionUID = -7180662651115335437L;

    private String numeroFactura;
    private String lugSRI;
    private Long lugId;
    private String observacion;
    private Long tipoRequerimiento;
    private String solicitante;
    private Long entregaDomicilio;
    private List<ItemDTO> itemDtoLista;
    private UsuarioDTO usuarioDTO;

    /*Getters and Setters*/
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getLugSRI() {
        return lugSRI;
    }

    public void setLugSRI(String lugSRI) {
        this.lugSRI = lugSRI;
    }

    public Long getLugId() {
        return lugId;
    }

    public void setLugId(Long lugId) {
        this.lugId = lugId;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(Long tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public List<ItemDTO> getItemDtoLista() {
        return itemDtoLista;
    }

    public void setItemDtoLista(List<ItemDTO> itemDtoLista) {
        this.itemDtoLista = itemDtoLista;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public Long getEntregaDomicilio() {
        return entregaDomicilio;
    }

    public void setEntregaDomicilio(Long entregaDomicilio) {
        this.entregaDomicilio = entregaDomicilio;
    }

}
