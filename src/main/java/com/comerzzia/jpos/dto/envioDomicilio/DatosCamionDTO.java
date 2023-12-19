/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.envioDomicilio;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class DatosCamionDTO implements Serializable {

    
    private String horario;
    private String camion;
    private String fechaEntrega;

    public DatosCamionDTO(String horario, String camion, String fechaEntrega) {
        this.horario = horario;
        this.camion = camion;
        this.fechaEntrega = fechaEntrega;
    }

    public DatosCamionDTO() {
       
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCamion() {
        return camion;
    }

    public void setCamion(String camion) {
        this.camion = camion;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

}
