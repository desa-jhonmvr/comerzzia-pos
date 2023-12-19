/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@Table(name = "X_CUPONES_DET_TBL")
public class SukuponDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CUPON")
    private Long idCupon;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "UID_NOTA_CREDITO")
    private String uidNotaCredito;
    @Column(name = "UID_TICKET")
    private String uidTicket;

    public SukuponDetalle() {
    }

    public SukuponDetalle(ResultSet rs) throws SQLException {
        idCupon = rs.getLong("ID_CUPON");
        saldo = rs.getBigDecimal("SALDO");
        uidNotaCredito = rs.getString("UID_NOTA_CREDITO");
        uidTicket = rs.getString("UID_TICKET");

    }

    public Long getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Long idCupon) {
        this.idCupon = idCupon;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public void setUidNotaCredito(String uidNotaCredito) {
        this.uidNotaCredito = uidNotaCredito;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

}
