/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumEstado;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Mónica Enríquez
 */
@Entity
@Table(name = "D_ENVIO_DOMICILIO_TBL")
public class EnvioDomicilioTbl implements Serializable {

    private static final long serialVersionUID = 3475274259473990655L;

    @Id
    @Basic(optional = false)
    @Column(name = "UID_TICKET")
    private String uidTicket;

    @Column(name = "CEDULA")
    private String cedula;

    @Column(name = "NOMBRES")
    private String nombres;

    @Column(name = "APELLIDOS")
    private String apellidos;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "CIUDAD")
    private String ciudad;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "MOVIL")
    private String movil;

    @Column(name = "HORARIO")
    private String horario;

    @Column(name = "CAMION")
    private String camion;

    @Column(name = "FECHA_ENTREGA")
    private String fechaEntrega;

    @Column(name = "OBSERVACION")
    private String observacion;

    @Column(name = "CODART")
    private String codArt;

    @Column(name = "CANTIDAD")
    private Long cantidad;

    @Column(name = "INSTALACION")
    private String instalacion;

    @Column(name = "LUGAR")
    private String lugar;

    @Column(name = "FACTURA")
    private String factura;

    @Column(name = "VENDEDOR")
    private String vendedor;

    @Column(name = "ESTADO")
    private EnumEstado estado;

    public EnvioDomicilioTbl() {
    }

    public EnvioDomicilioTbl(String uidTicket, String cedula, String nombres, String apellidos, String direccion, String ciudad, String mail, String telefono, String movil, String horario, String camion, String fechaEntrega, String observacion, String codArt, Long cantidad, String instalacion, String lugar, String factura, String vendedor, EnumEstado estado) {
        this.uidTicket = uidTicket;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.mail = mail;
        this.telefono = telefono;
        this.movil = movil;
        this.horario = horario;
        this.camion = camion;
        this.fechaEntrega = fechaEntrega;
        this.observacion = observacion;
        this.codArt = codArt;
        this.cantidad = cantidad;
        this.instalacion = instalacion;
        this.lugar = lugar;
        this.factura = factura;
        this.vendedor = vendedor;
        this.estado = estado;
    }

    
    
    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codArt) {
        this.codArt = codArt;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public EnumEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumEstado estado) {
        this.estado = estado;
    }

    

}
