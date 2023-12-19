/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.giftcard.logs;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author DESARROLLO
 */
public class LogGiftCardConsumoBean {

    private String uidConsumo;
    private String uidTicket;
    private String idGiftcard;
    private String codAlmacen;
    private String usuario;
    private Fecha fecha;
    private String codOperacion;
    private BigDecimal consumo;
    private String tipoConsumo;
    private boolean procesado;

    public LogGiftCardConsumoBean() {
    }

    public LogGiftCardConsumoBean(String uidTicket, String idGiftcard, String codAlmacen, String usuario, String codOperacion, BigDecimal consumo, String tipoConsumo) {
        this.uidConsumo = UUID.randomUUID().toString();
        this.uidTicket = uidTicket;
        this.idGiftcard = idGiftcard;
        this.codAlmacen = codAlmacen;
        this.usuario = usuario;
        this.fecha = new Fecha();
        this.codOperacion = codOperacion;
        this.consumo = consumo;
        this.tipoConsumo = tipoConsumo;
        this.procesado = false;
    }

    public String getUidConsumo() {
        return uidConsumo;
    }

    public void setUidConsumo(String uidConsumo) {
        this.uidConsumo = uidConsumo;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public String getIdGiftcard() {
        return idGiftcard;
    }

    public void setIdGiftcard(String idGiftcard) {
        this.idGiftcard = idGiftcard;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getCodOperacion() {
        return codOperacion;
    }

    public void setCodOperacion(String codOperacion) {
        this.codOperacion = codOperacion;
    }

    public BigDecimal getConsumo() {
        return consumo;
    }

    public void setConsumo(BigDecimal consumo) {
        this.consumo = consumo;
    }

    public String getTipoConsumo() {
        return tipoConsumo;
    }

    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
    }

    public String getProcesado() {
        return procesado ? "S" : "N";
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado.equalsIgnoreCase("S");
    }
}
