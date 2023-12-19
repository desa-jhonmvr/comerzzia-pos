/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.promociones;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Mónica Enríquez
 */
public final class PromocionSocioBean {

    private static final long serialVersionUID = -1039810621142136368L;

    private Long id;
    private Fecha fechaInicio;
    private Fecha fechaFin;
    private BigDecimal descuento;
    private String cedula;
    private List<String> excluirItems;
    private List<String> vencimientos;

    public PromocionSocioBean(ResultSet rs) throws SQLException {
        setId(rs.getLong("ID"));
        setFechaInicio(new Fecha(rs.getTimestamp("FECHA_INICIO")));
        setFechaFin(new Fecha(rs.getTimestamp("FECHA_FIN")));
        setDescuento(rs.getBigDecimal("DESCUENTO"));
        setCedula(rs.getString("CEDULA"));
        if (rs.getString("EXCLUIR_ITEMS") == null) {
            setExcluirItems(null);
        } else {
            setExcluirItems(rs.getString("EXCLUIR_ITEMS").split(";"));
        }
        if (rs.getString("VENCIMIENTOS") == null) {
            setVencimientos(null);
        } else {
            setVencimientos(rs.getString("VENCIMIENTOS").split(";"));
        }
    }

    public PromocionSocioBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Fecha fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<String> getExcluirItems() {
        return excluirItems;
    }

    public void setExcluirItems(String[] excluirItems) {
        if (excluirItems == null || excluirItems.length == 0) {
            this.excluirItems = null;
        } else {
            this.excluirItems = Arrays.asList(excluirItems);
        }
    }

    public List<String> getVencimientos() {
        return vencimientos;
    }

    public void setVencimientos(String[] vencimientos) {
        if (vencimientos == null || vencimientos.length == 0) {
            this.vencimientos = null;
        } else {
            this.vencimientos = Arrays.asList(vencimientos);
        }
    }

}
