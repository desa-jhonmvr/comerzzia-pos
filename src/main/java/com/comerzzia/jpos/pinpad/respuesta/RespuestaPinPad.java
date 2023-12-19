/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.respuesta;

/**
 *
 * @author SMLM
 */
public class RespuestaPinPad {
    private String respuesta;
    private String tipoMensaje;
    private String codigoRespuestaMensaje;
    private String mensajeRespuesta;
//    //nuevo campo comun filler plan D
//    private String filler;
    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getCodigoRespuestaMensaje() {
        return codigoRespuestaMensaje;
    }

    public void setCodigoRespuestaMensaje(String codigoRespuestaMensaje) {
        this.codigoRespuestaMensaje = codigoRespuestaMensaje;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

//    public String getFiller() {
//        return filler;
//    }
//
//    public void setFiller(String filler) {
//        this.filler = filler;
//    }
//    
}
