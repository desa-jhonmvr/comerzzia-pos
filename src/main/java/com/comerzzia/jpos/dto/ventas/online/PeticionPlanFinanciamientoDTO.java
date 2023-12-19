/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class PeticionPlanFinanciamientoDTO implements Serializable {

    private static final long serialVersionUID = -6967805466046919239L;

    private String identificacion;
    private Boolean esAfiliado;
    private String codBan;
    private String codMarcaTarjeta;
    private List<PedidoOnlineDTO> pedidos;

    public PeticionPlanFinanciamientoDTO() {
    }

    public PeticionPlanFinanciamientoDTO(String identificacion, Boolean esAfiliado, List<PedidoOnlineDTO> pedidos) {
        this.identificacion = identificacion;
        this.esAfiliado = esAfiliado;
        this.pedidos = pedidos;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Boolean getEsAfiliado() {
        return esAfiliado;
    }

    public void setEsAfiliado(Boolean esAfiliado) {
        this.esAfiliado = esAfiliado;
    }

    public String getCodBan() {
        return codBan;
    }

    public void setCodBan(String codBan) {
        this.codBan = codBan;
    }

    public List<PedidoOnlineDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoOnlineDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public String getCodMarcaTarjeta() {
        return codMarcaTarjeta;
    }

    public void setCodMarcaTarjeta(String codMarcaTarjeta) {
        this.codMarcaTarjeta = codMarcaTarjeta;
    }
    
    

}
