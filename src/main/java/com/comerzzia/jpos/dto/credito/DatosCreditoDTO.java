/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.credito;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class DatosCreditoDTO implements Serializable {

    private String cedula;
    private String plastico;
    private Long credito;
    private BigDecimal cupo;
    private String estatus;
    private String sestatus;
    private String ssestatus;

    public DatosCreditoDTO() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPlastico() {
        return plastico;
    }

    public void setPlastico(String plastico) {
        this.plastico = plastico;
    }

    public Long getCredito() {
        return credito;
    }

    public void setCredito(Long credito) {
        this.credito = credito;
    }

    public BigDecimal getCupo() {
        return cupo;
    }

    public void setCupo(BigDecimal cupo) {
        this.cupo = cupo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getSestatus() {
        return sestatus;
    }

    public void setSestatus(String sestatus) {
        this.sestatus = sestatus;
    }

    public String getSsestatus() {
        return ssestatus;
    }

    public void setSsestatus(String ssestatus) {
        this.ssestatus = ssestatus;
    }

}
