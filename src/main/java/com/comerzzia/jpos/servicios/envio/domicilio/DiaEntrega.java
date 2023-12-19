/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.envio.domicilio;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Mónica Enríquez
 */
public class DiaEntrega implements Serializable {

    private String texto;
    private Date fecha;

    public DiaEntrega() {
    }

    public DiaEntrega(String texto, Date fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
