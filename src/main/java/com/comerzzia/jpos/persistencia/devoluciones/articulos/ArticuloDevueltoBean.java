/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.devoluciones.articulos;

import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MGRI
 */
public class ArticuloDevueltoBean {

    public static final boolean MODO_AGRUPADO = false;
    public static final boolean MODO_NO_AGRUPADO = true;

    //PK
    private String uidTicket;
    private int idLinea;
    private String uidNotaCredito;

    private String codart;
    private Integer cantidad;

    private boolean modoCompleto;
    private GarantiaReferencia garantiaReferencia;

    public ArticuloDevueltoBean() {
    }

    /**
     *
     * @param rs
     * @param modoCompleto Modo completo nos servirá para saber si hemso usado
     * el bean para una consulta agrupando por uid_ticketo o es una consuta
     * completa
     * @throws SQLException
     */
    public ArticuloDevueltoBean(ResultSet rs, boolean modoCompleto) throws SQLException {
        uidTicket = rs.getString("UID_TICKET");
        idLinea = rs.getInt("ID_LINEA");
        cantidad = rs.getInt("CANTIDAD");
        this.modoCompleto = false;
        if (modoCompleto) {
            uidNotaCredito = rs.getString("UID_NOTA_CREDITO");
            codart = rs.getString("CODART");
            this.modoCompleto = true;
        }
    }

    public ArticuloDevueltoBean(String uidTicket, String uidNotaCredito, int idLinea, String codart, Integer cantidad) {
        this.uidTicket = uidTicket;
        this.idLinea = idLinea;
        this.uidNotaCredito = uidNotaCredito;
        this.codart = codart;
        this.cantidad = cantidad;
        this.modoCompleto = true;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isModoCompleto() {
        return modoCompleto;
    }

    public GarantiaReferencia getGarantiaReferencia() {
        return garantiaReferencia;
    }

    public void setGarantiaReferencia(GarantiaReferencia garantiaReferencia) {
        this.garantiaReferencia = garantiaReferencia;
    }

}
