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
public class NotificacionAutorizacionConsumoDTO implements Serializable {

    private String uidTicket;
    private String codAlm;
    private String codCaja;
    private String nombreAlmacen;
    private String identificacion;
    private Long credito;
    private String plastico;
    private String usuarioSolicitud;
    private BigDecimal cupoSolicitado;

    public NotificacionAutorizacionConsumoDTO() {

    }

    public NotificacionAutorizacionConsumoDTO(String uidTicket, String codAlm, String nombreAlmacen, String identificacion, Long credito, String plastico, String usuarioSolicitud, BigDecimal cupoSolicitado) {
        this.uidTicket = uidTicket;
        this.codAlm = codAlm;
        this.nombreAlmacen = nombreAlmacen;
        this.identificacion = identificacion;
        this.credito = credito;
        this.plastico = plastico;
        this.usuarioSolicitud = usuarioSolicitud;
        this.cupoSolicitado = cupoSolicitado;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Long getCredito() {
        return credito;
    }

    public void setCredito(Long credito) {
        this.credito = credito;
    }

    public String getPlastico() {
        return plastico;
    }

    public void setPlastico(String plastico) {
        this.plastico = plastico;
    }

    public String getUsuarioSolicitud() {
        return usuarioSolicitud;
    }

    public void setUsuarioSolicitud(String usuarioSolicitud) {
        this.usuarioSolicitud = usuarioSolicitud;
    }

    public BigDecimal getCupoSolicitado() {
        return cupoSolicitado;
    }

    public void setCupoSolicitado(BigDecimal cupoSolicitado) {
        this.cupoSolicitado = cupoSolicitado;
    }

}
