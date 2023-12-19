/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.util.enums.EnumEstado;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Table(name = "D_PEDIDO_ONLINE_TBL")
public class PedidoOnlineTbl implements Serializable {

    private static final long serialVersionUID = 3475274259473990655L;

    @Id
    @Basic(optional = false)
    @Column(name = "UID_PEDIDO")
    private String uidPedido;

    @Column(name = "ID_PEDIDO")
    private String idPedido;

    @Column(name = "REFERENCIA_PEDIDO")
    private String referenciaPedido;

    @Column(name = "FECHA")
    private Timestamp fecha;

    @Column(name = "CODCLI")
    private String codCli;

    @Column(name = "ESTADO")
    private EnumEstado estado;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "UID_TICKET")
    private String uidTicket;

    public PedidoOnlineTbl(String uidPedido, String idPedido, String referenciaPedido, Timestamp fecha, String codCli, EnumEstado estado, String status, String uidTicket) {
        this.uidPedido = uidPedido;
        this.idPedido = idPedido;
        this.referenciaPedido = referenciaPedido;
        this.fecha = fecha;
        this.codCli = codCli;
        this.estado = estado;
        this.status = status;
        this.uidTicket = uidTicket;
    }

    public PedidoOnlineTbl() {

    }

    public String getUidPedido() {
        return uidPedido;
    }

    public void setUidPedido(String uidPedido) {
        this.uidPedido = uidPedido;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getReferenciaPedido() {
        return referenciaPedido;
    }

    public void setReferenciaPedido(String referenciaPedido) {
        this.referenciaPedido = referenciaPedido;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getCodCli() {
        return codCli;
    }

    public void setCodCli(String codCli) {
        this.codCli = codCli;
    }

    public EnumEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumEstado estado) {
        this.estado = estado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

}
