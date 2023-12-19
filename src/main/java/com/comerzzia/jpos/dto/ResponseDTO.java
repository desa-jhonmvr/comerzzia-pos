/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class ResponseDTO implements Serializable {

    private static final long serialVersionUID = 3077768348950852807L;

    private Boolean exito;
    private String codigoError;
    private String descripcion;
    private Object objetoRespuesta;

    public ResponseDTO() {
        this.exito = Boolean.FALSE;
    }

    public ResponseDTO(Boolean exito, String codigoError, String descripcion) {
        this.exito = exito;
        this.codigoError = codigoError;
        this.descripcion = descripcion;
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Object getObjetoRespuesta() {
        return objetoRespuesta;
    }

    public void setObjetoRespuesta(Object objetoRespuesta) {
        this.objetoRespuesta = objetoRespuesta;
    }

}
