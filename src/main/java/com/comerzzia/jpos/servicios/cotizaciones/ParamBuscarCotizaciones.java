/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cotizaciones;

import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author SMLM
 */
public class ParamBuscarCotizaciones {
    
    private Fecha fechaDesde;
    private Fecha fechaHasta;
    private String codcli;
    private String vendedor;
    private char estado;

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public Fecha getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Fecha fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Fecha getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Fecha fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
     
}
