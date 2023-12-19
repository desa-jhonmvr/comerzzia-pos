/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

/**
 *
 * @author MGRI
 */
public class DatosDeEnvio {

    private String nombreEnvio;
    private String apellidosEnvio;
    private String direccion;
    private String telefonoEnvio;
    private String movilEnvio;
    private String horarioEnvio;
    private String ciudad;

    public DatosDeEnvio(String apellido, String movil, String nombre, String telefono, String horario, String direccion, String ciudad) {
        this.nombreEnvio = nombre;
        this.apellidosEnvio = apellido;
        this.telefonoEnvio = telefono;
        this.movilEnvio = movil;
        this.horarioEnvio = horario;
        this.direccion=direccion;
        this.ciudad = ciudad;
    }

    public String getNombreEnvio() {
        return nombreEnvio;
    }

    public void setNombreEnvio(String nombreEnvio) {
        this.nombreEnvio = nombreEnvio;
    }

    public String getApellidosEnvio() {
        return apellidosEnvio;
    }

    public void setApellidosEnvio(String apellidosEnvio) {
        this.apellidosEnvio = apellidosEnvio;
    }

    public String getTelefonoEnvio() {
        return telefonoEnvio;
    }

    public void setTelefonoEnvio(String telefonoEnvio) {
        this.telefonoEnvio = telefonoEnvio;
    }

    public String getMovilEnvio() {
        return movilEnvio;
    }

    public void setMovilEnvio(String movilEnvio) {
        this.movilEnvio = movilEnvio;
    }

    public String getHorarioEnvio() {
        return horarioEnvio;
    }

    public void setHorarioEnvio(String horarioEnvio) {
        this.horarioEnvio = horarioEnvio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
}
