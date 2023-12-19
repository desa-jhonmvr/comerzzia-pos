/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.bonoEfectivo;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

/**
 *
 * @author SMLM
 */
public class BonoEfectivoBean {

    private String codAlm;
    private String idBono;
    private Fecha fechaCaducidad;
    private String utilizado;
    private BigDecimal importe;

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getIdBono() {
        return idBono;
    }

    public void setIdBono(String idBono) {
        this.idBono = idBono;
    }

    public Fecha getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Fecha fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(String utilizado) {
        this.utilizado = utilizado;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

}
