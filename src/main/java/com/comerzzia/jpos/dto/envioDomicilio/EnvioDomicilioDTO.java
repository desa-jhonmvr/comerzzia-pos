/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.envioDomicilio;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Mónica Enríquez
 */
public class EnvioDomicilioDTO implements Serializable {

    private static final long serialVersionUID = -7180662651115335437L;

    private String uidTicket;
    private String lugar;
    private String vendedor;
    private String observacion;
    private ClienteDTO datosContacto;
    private DatosCamionDTO datosCamion;
    private List<ItemDTO> itemDtoLista;
    private String ciudad;
    private String sector;

    public EnvioDomicilioDTO() {

    }

    public EnvioDomicilioDTO(String uidTicket, String lugar, String vendedor, String observacion, ClienteDTO datosContacto, DatosCamionDTO datosCamion, List<ItemDTO> itemDtoLista, String ciudad, String sector) {
        this.uidTicket = uidTicket;
        this.lugar = lugar;
        this.vendedor = vendedor;
        this.observacion = observacion;
        this.datosContacto = datosContacto;
        this.datosCamion = datosCamion;
        this.itemDtoLista = itemDtoLista;
        this.ciudad = ciudad;
        this.sector = sector;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public ClienteDTO getDatosContacto() {
        return datosContacto;
    }

    public void setDatosContacto(ClienteDTO datosContacto) {
        this.datosContacto = datosContacto;
    }

    public DatosCamionDTO getDatosCamion() {
        return datosCamion;
    }

    public void setDatosCamion(DatosCamionDTO datosCamion) {
        this.datosCamion = datosCamion;
    }

    public List<ItemDTO> getItemDtoLista() {
        return itemDtoLista;
    }

    public void setItemDtoLista(List<ItemDTO> itemDtoLista) {
        this.itemDtoLista = itemDtoLista;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

}
