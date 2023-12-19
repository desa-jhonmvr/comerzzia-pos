/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.politica;

/**
 *
 * @author Gabriel Simbania
 */
public class ProteccionDatosBean {

    private String codCliente;
    private String nombresCliente;
    private String apellidosCliente;
    private String estadoAprobacion;

    public ProteccionDatosBean() {
    }

    public ProteccionDatosBean(String codCliente, String nombresCliente, String apellidosCliente, String estadoAprobacion) {
        this.codCliente = codCliente;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.estadoAprobacion = estadoAprobacion;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    public String getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(String estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

}
