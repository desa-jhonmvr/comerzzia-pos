/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class MotivoDevolucionDTO implements Serializable {

    private static final long serialVersionUID = -4351195588814895971L;

    private Integer idMotivo;
    private String descripcionMotivo;

    public MotivoDevolucionDTO() {
    }

    public MotivoDevolucionDTO(Integer idMotivo, String descripcionMotivo) {
        this.idMotivo = idMotivo;
        this.descripcionMotivo = descripcionMotivo;
    }

    public Integer getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescripcionMotivo() {
        return descripcionMotivo;
    }

    public void setDescripcionMotivo(String descripcionMotivo) {
        this.descripcionMotivo = descripcionMotivo;
    }

}
