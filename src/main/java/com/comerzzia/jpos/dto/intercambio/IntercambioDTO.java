/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.intercambio;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class IntercambioDTO implements Serializable {

    private static final long serialVersionUID = 7580035615086634930L;

    private String lugSRIOrigen;
    private String lugSRIDestino;
    private String numeroDocumento;
    private List<ItemDTO> itemDtoLista;
    private UsuarioDTO usuarioDTO;

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public String getLugSRIOrigen() {
        return lugSRIOrigen;
    }

    public void setLugSRIOrigen(String lugSRIOrigen) {
        this.lugSRIOrigen = lugSRIOrigen;
    }

    public String getLugSRIDestino() {
        return lugSRIDestino;
    }

    public void setLugSRIDestino(String lugSRIDestino) {
        this.lugSRIDestino = lugSRIDestino;
    }

    public List<ItemDTO> getItemDtoLista() {
        return itemDtoLista;
    }

    public void setItemDtoLista(List<ItemDTO> itemDtoLista) {
        this.itemDtoLista = itemDtoLista;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

}
