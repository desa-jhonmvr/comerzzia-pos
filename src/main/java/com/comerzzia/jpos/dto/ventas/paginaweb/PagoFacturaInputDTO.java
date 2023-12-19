/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class PagoFacturaInputDTO implements Serializable {

    private static final long serialVersionUID = 6604925365863559077L;

    private String uidCabId;
    private String cabIdPedido;
    private String paymentObject;
    private String codAlm;
    private Integer tipoPago;
    private String autorizacion;

    public PagoFacturaInputDTO() {
    }

    public PagoFacturaInputDTO(String cabIdPedido, String paymentObject, String codAlm, Integer tipoPago,
            String autorizacion,String uidCabId) {
        this.cabIdPedido = cabIdPedido;
        this.paymentObject = paymentObject;
        this.codAlm = codAlm;
        this.tipoPago = tipoPago;
        this.autorizacion = autorizacion;
        this.uidCabId = uidCabId;
    }

    public String getCabIdPedido() {
        return cabIdPedido;
    }

    public void setCabIdPedido(String cabIdPedido) {
        this.cabIdPedido = cabIdPedido;
    }

    public String getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(String paymentObject) {
        this.paymentObject = paymentObject;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getUidCabId() {
        return uidCabId;
    }

    public void setUidCabId(String uidCabId) {
        this.uidCabId = uidCabId;
    }

}
