/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "D_PEDIDO_ONLINE_DIRECC_TBL")
public class PedidoOnlineDireccionTbl implements Serializable {

    private static final long serialVersionUID = 7849382038417596216L;

    @Id
    @JoinColumn(name = "UID_PEDIDO", referencedColumnName = "UID_PEDIDO")//,insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PedidoOnlineTbl pedidoOnlineTbl;

    @Column(name = "PROVINCIA")
    private String provincia;

    @Column(name = "LOCALIDAD")
    private String localidad;

    @Column(name = "CALLE_PRINCIPAL")
    private String callePrincipal;

    @Column(name = "NUMERACION")
    private String numeracion;

    @Column(name = "CALLE_SECUNDARIA")
    private String calleSecundaria;

    @Column(name = "REFERENCIA")
    private String referencia;

    @Column(name = "NOMBRE_CONTACTO")
    private String nombreContacto;

    @Column(name = "IDENTIFICACION_CONTACTO")
    private String identificacionContacto;

    @Column(name = "TELEFONO_CONTACTO")
    private String telefonoContacto;

    @Column(name = "UBIGEO")
    private String ubigeo;

    public PedidoOnlineDireccionTbl() {
    }

    public PedidoOnlineDireccionTbl(PedidoOnlineTbl pedidoOnlineTbl, String provincia, String localidad, String callePrincipal, String numeracion, String calleSecundaria, String referencia, String nombreContacto, String identificacionContacto, String telefonoContacto,
            String ubigeo) {
        this.pedidoOnlineTbl = pedidoOnlineTbl;
        this.provincia = provincia;
        this.localidad = localidad;
        this.callePrincipal = callePrincipal;
        this.numeracion = numeracion;
        this.calleSecundaria = calleSecundaria;
        this.referencia = referencia;
        this.nombreContacto = nombreContacto;
        this.identificacionContacto = identificacionContacto;
        this.telefonoContacto = telefonoContacto;
        this.ubigeo = ubigeo;
    }

    public PedidoOnlineTbl getPedidoOnlineTbl() {
        return pedidoOnlineTbl;
    }

    public void setPedidoOnlineTbl(PedidoOnlineTbl pedidoOnlineTbl) {
        this.pedidoOnlineTbl = pedidoOnlineTbl;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCallePrincipal() {
        return callePrincipal;
    }

    public void setCallePrincipal(String callePrincipal) {
        this.callePrincipal = callePrincipal;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getCalleSecundaria() {
        return calleSecundaria;
    }

    public void setCalleSecundaria(String calleSecundaria) {
        this.calleSecundaria = calleSecundaria;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getIdentificacionContacto() {
        return identificacionContacto;
    }

    public void setIdentificacionContacto(String identificacionContacto) {
        this.identificacionContacto = identificacionContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

}
