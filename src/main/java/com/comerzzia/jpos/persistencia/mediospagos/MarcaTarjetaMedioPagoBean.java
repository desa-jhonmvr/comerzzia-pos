/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.mediospagos;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class MarcaTarjetaMedioPagoBean implements Serializable {

    private Long codMarMed;
    private String codMarcaTarjeta;
    private String codMedPag;
    private String descripcion;
    private String activo;
    private Long orden;
    private MedioPagoBean medioPagoBean;

    public MarcaTarjetaMedioPagoBean() {
    }

    public MarcaTarjetaMedioPagoBean(Long codMarMed, String codMarcaTarjeta, String codMedPag, String descripcion, String activo, Long orden) {
        this.codMarMed = codMarMed;
        this.codMarcaTarjeta = codMarcaTarjeta;
        this.codMedPag = codMedPag;
        this.descripcion = descripcion;
        this.activo = activo;
        this.orden = orden;
    }

    public Long getCodMarMed() {
        return codMarMed;
    }

    public void setCodMarMed(Long codMarMed) {
        this.codMarMed = codMarMed;
    }

    public String getCodMarcaTarjeta() {
        return codMarcaTarjeta;
    }

    public void setCodMarcaTarjeta(String codMarcaTarjeta) {
        this.codMarcaTarjeta = codMarcaTarjeta;
    }

    public String getCodMedPag() {
        return codMedPag;
    }

    public void setCodMedPag(String codMedPag) {
        this.codMedPag = codMedPag;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public MedioPagoBean getMedioPagoBean() {
        return medioPagoBean;
    }

    public void setMedioPagoBean(MedioPagoBean medioPagoBean) {
        this.medioPagoBean = medioPagoBean;
    }

}
