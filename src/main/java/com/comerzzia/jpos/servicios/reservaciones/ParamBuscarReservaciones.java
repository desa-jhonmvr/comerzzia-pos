/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.reservaciones;

import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import java.math.BigInteger;

/**
 *
 * @author amos
 */
public class ParamBuscarReservaciones {
    
    private BigInteger numeroReserva;
    private ReservaTiposBean tipoReserva;
    private String numeroFidelizado;
    private String tipoDocumento;
    private String documento;
    private String estado;
    private String nombre;
    private String apellidos;
    private String nombreOrg;
    private String apellidosOrg;
    private String tiendaReservacion;
    
    public ParamBuscarReservaciones(){
        numeroReserva=null;
        numeroFidelizado="";
        tipoDocumento="";
        documento="";
        estado="";
        tiendaReservacion= "";
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String doc) {
        this.documento = doc;
    }

    public String getNumeroFidelizado() {
        return numeroFidelizado;
    }

    public void setNumeroFidelizado(String numeroFidelizado) {
        this.numeroFidelizado = numeroFidelizado;
    }

    public BigInteger getNumeroReserva() {
        return numeroReserva;
    }

    public void setNumeroReserva(BigInteger numeroReserva) {
        this.numeroReserva = numeroReserva;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDoc) {
        this.tipoDocumento = tipoDoc;
    }

    public ReservaTiposBean getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(ReservaTiposBean tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreOrg() {
        return nombreOrg;
    }

    public void setNombreOrg(String nombre_org) {
        this.nombreOrg = nombre_org;
    }

    public String getApellidosOrg() {
        return apellidosOrg;
    }

    public void setApellidosOrg(String apellidos_org) {
        this.apellidosOrg = apellidos_org;
    }

    public String getTiendaReservacion() {
        return tiendaReservacion;
    }

    public void setTiendaReservacion(String tiendaReservacion) {
        this.tiendaReservacion = tiendaReservacion;
    }
}
