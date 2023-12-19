/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import com.comerzzia.jpos.dto.ventas.paginaweb.PlanPagoDTO;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class PedidoOnlineDTO implements Cloneable {

    private static final long serialVersionUID = 6052305066057326413L;

    private String idPedido;
    private Integer tipo;
    private Long lugId;
    private Boolean entregaDomicilio;
    private PlanPagoDTO planPedido;
    private SubtotalOnlineDTO subtotales;
    private List<ItemOnlineDTO> items;
    private List<ItemOnlineDTO> itemsAuxiliares;

    public PedidoOnlineDTO clone() throws CloneNotSupportedException {
        return (PedidoOnlineDTO) super.clone();
    }

    public PedidoOnlineDTO() {
    }
    
    

    public PedidoOnlineDTO(String idPedido, Integer tipo, Long lugId, Boolean entregaDomicilio, PlanPagoDTO planPedido, List<ItemOnlineDTO> items) {
        this.idPedido = idPedido;
        this.tipo = tipo;
        this.lugId = lugId;
        this.entregaDomicilio = entregaDomicilio;
        this.planPedido = planPedido;
        this.items = items;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Long getLugId() {
        return lugId;
    }

    public void setLugId(Long lugId) {
        this.lugId = lugId;
    }

    public Boolean getEntregaDomicilio() {
        return entregaDomicilio;
    }

    public void setEntregaDomicilio(Boolean entregaDomicilio) {
        this.entregaDomicilio = entregaDomicilio;
    }

    public PlanPagoDTO getPlanPedido() {
        return planPedido;
    }

    public void setPlanPedido(PlanPagoDTO planPedido) {
        this.planPedido = planPedido;
    }

    public SubtotalOnlineDTO getSubtotales() {
        return subtotales;
    }

    public void setSubtotales(SubtotalOnlineDTO subtotales) {
        this.subtotales = subtotales;
    }

    public List<ItemOnlineDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemOnlineDTO> items) {
        this.items = items;
    }

    public List<ItemOnlineDTO> getItemsAuxiliares() {
        return itemsAuxiliares;
    }

    public void setItemsAuxiliares(List<ItemOnlineDTO> itemsAuxiliares) {
        this.itemsAuxiliares = itemsAuxiliares;
    }
    
    
    

}
