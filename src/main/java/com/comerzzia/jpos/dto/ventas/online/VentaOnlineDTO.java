/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import com.comerzzia.jpos.dto.ClienteDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class VentaOnlineDTO implements Serializable {

    private static final long serialVersionUID = 1866247838067723328L;

    private String idPedido;
    private String referenciaPedido;
    private ClienteDTO cliente;
    private ClienteDTO datosFacturacion;
    private Long lugId;
    private List<ItemOnlineDTO> items;
    private List<FormaPagoOnlineDTO> formasPago;
    private DireccionEnvioOnlineDTO direccionEnvio;

    public VentaOnlineDTO() {
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getReferenciaPedido() {
        return referenciaPedido;
    }

    public void setReferenciaPedido(String referenciaPedido) {
        this.referenciaPedido = referenciaPedido;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public ClienteDTO getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(ClienteDTO datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }

    public Long getLugId() {
        return lugId;
    }

    public void setLugId(Long lugId) {
        this.lugId = lugId;
    }

    public List<ItemOnlineDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemOnlineDTO> items) {
        this.items = items;
    }

    public List<FormaPagoOnlineDTO> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(List<FormaPagoOnlineDTO> formasPago) {
        this.formasPago = formasPago;
    }

    public DireccionEnvioOnlineDTO getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionEnvioOnlineDTO direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

}
