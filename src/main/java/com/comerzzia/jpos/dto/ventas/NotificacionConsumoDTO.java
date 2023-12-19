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
public class NotificacionConsumoDTO implements Serializable {

    private static final long serialVersionUID = 8351847649392426618L;

    private Integer idCmbSms;
    private String tokenSms;
    private Integer messageIdSms;
    private String idTransaccion;
    private String nombreAlmacen;
    private String fechaHora;
    private String valor;
    private String telefonoAlmacen;
    private String identificacion;

    public void NotificacionConsumoDTO() {

    }

    public Integer getIdCmbSms() {
        return idCmbSms;
    }

    public void setIdCmbSms(Integer idCmbSms) {
        this.idCmbSms = idCmbSms;
    }

    public String getTokenSms() {
        return tokenSms;
    }

    public void setTokenSms(String tokenSms) {
        this.tokenSms = tokenSms;
    }

    public Integer getMessageIdSms() {
        return messageIdSms;
    }

    public void setMessageIdSms(Integer messageIdSms) {
        this.messageIdSms = messageIdSms;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefonoAlmacen() {
        return telefonoAlmacen;
    }

    public void setTelefonoAlmacen(String telefonoAlmacen) {
        this.telefonoAlmacen = telefonoAlmacen;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

}
