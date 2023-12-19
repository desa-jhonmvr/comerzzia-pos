/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.sms;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class EnvioSmsGeneralDTO implements Serializable {

    private int idProceso;
    private String numeroTelefono;
    private List<String> datos;

    public EnvioSmsGeneralDTO() {
    }

    public EnvioSmsGeneralDTO(int idProceso, String numeroTelefono, List<String> datos) {
        this.idProceso = idProceso;
        this.numeroTelefono = numeroTelefono;
        this.datos = datos;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public List<String> getDatos() {
        return datos;
    }

    public void setDatos(List<String> datos) {
        this.datos = datos;
    }

}
