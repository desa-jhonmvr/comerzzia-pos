/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.online;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class DireccionEnvioOnlineDTO implements Serializable {

    private static final long serialVersionUID = 3119375871399199850L;

    private String provincia;
    private String localidad;
    private String ubigeo;
    private String callePrincipal;
    private String numeracion;
    private String calleSecundaria;
    private String referencia;
    private String nombreContacto;
    private String identificacionContacto;
    private String telefonoContacto;

    public DireccionEnvioOnlineDTO() {
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }
    
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCallePrincipal() {
        return callePrincipal;
    }

    public void setCallePrincipal(String callePrincipal) {
        this.callePrincipal = callePrincipal;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getCalleSecundaria() {
        return calleSecundaria;
    }

    public void setCalleSecundaria(String calleSecundaria) {
        this.calleSecundaria = calleSecundaria;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getIdentificacionContacto() {
        return identificacionContacto;
    }

    public void setIdentificacionContacto(String identificacionContacto) {
        this.identificacionContacto = identificacionContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

}
