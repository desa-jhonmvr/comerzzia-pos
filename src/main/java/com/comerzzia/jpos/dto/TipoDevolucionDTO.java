/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto;

import java.io.Serializable;

/**
 *
 * @author gtrujillo
 */
public class TipoDevolucionDTO implements Serializable {

    private static final long serialVersionUID = 2064353703124421183L;

    private Long idTipoDevolucion;
    private String descripcionDevolucion;

    public TipoDevolucionDTO() {
    }

    public TipoDevolucionDTO(Long idTipoDevolucion, String descripcionDevolucion) {
        this.idTipoDevolucion = idTipoDevolucion;
        this.descripcionDevolucion = descripcionDevolucion;
    }

    public Long getIdTipoDevolucion() {
        return idTipoDevolucion;
    }

    public void setIdTipoDevolucion(Long idTipoDevolucion) {
        this.idTipoDevolucion = idTipoDevolucion;
    }

    public String getDescripcionDevolucion() {
        return descripcionDevolucion;
    }

    public void setDescripcionDevolucion(String descripcionDevolucion) {
        this.descripcionDevolucion = descripcionDevolucion;
    }

}
