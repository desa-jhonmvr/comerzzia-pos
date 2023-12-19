/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.supermaxi;

import java.io.Serializable;

/**
 *
 * @author Mónica Enríquez
 */
public class RespuestaSupermaxiDTO implements Serializable {

    private static final long serialVersionUID = 3077768348950852807L;

    private Boolean exito;
    private String codigoError;
    private String descripcion;
    private Object objetoRespuesta;
    private String trama;

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

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

}
