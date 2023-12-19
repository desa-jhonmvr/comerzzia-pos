/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.cotizacion;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Mónica Enríquez
 */
public class CotizacionDTO implements Serializable {

    private static final long serialVersionUID = 1866247838067723328L;

    private ClienteDTO datosFacturacion;
    private List<ItemDTO> items;
    private TiendaDTO datosTienda;
    private String msmCotizacion;
    private String elaborado;
    private String local;
    private String telefono;

    public CotizacionDTO() {
    }

    public ClienteDTO getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(ClienteDTO datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public TiendaDTO getDatosTienda() {
        return datosTienda;
    }

    public void setDatosTienda(TiendaDTO datosTienda) {
        this.datosTienda = datosTienda;
    }

    public String getMsmCotizacion() {
        return msmCotizacion;
    }

    public void setMsmCotizacion(String msmCotizacion) {
        this.msmCotizacion = msmCotizacion;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
 
}
