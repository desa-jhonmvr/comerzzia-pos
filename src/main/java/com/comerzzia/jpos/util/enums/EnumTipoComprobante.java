/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author Mónica Enríquez
 */
public enum EnumTipoComprobante {

    FACTURA("01", "FACTURA"),
    LIQUIDACION_COMPRA("03", "LIQUIDACI\u00D3N DE COMPRA"),
    NOTA_CREDITO("04", "NOTA DE CR\u00C9DITO"),
    NOTA_DEBITO("05", "NOTA DE D\u00C9BITO"),
    GUIA_REMISION("06", "GU\u00CDA DE REMISI\u00D3N"),
    COMPROBANTE_RETENCION("07", "COMPROBANTE DE RETENCI\u00D3N");

    private final String cogidoSRI;
    private final String descripcion;

    private EnumTipoComprobante(String cogidoSRI, String descripcion) {
        this.cogidoSRI = cogidoSRI;
        this.descripcion = descripcion;
    }

    public static EnumTipoComprobante getFACTURA() {
        return FACTURA;
    }

    public static EnumTipoComprobante getLIQUIDACION_COMPRA() {
        return LIQUIDACION_COMPRA;
    }

    public static EnumTipoComprobante getNOTA_CREDITO() {
        return NOTA_CREDITO;
    }

    public static EnumTipoComprobante getNOTA_DEBITO() {
        return NOTA_DEBITO;
    }

    public static EnumTipoComprobante getGUIA_REMISION() {
        return GUIA_REMISION;
    }

    public static EnumTipoComprobante getCOMPROBANTE_RETENCION() {
        return COMPROBANTE_RETENCION;
    }

    public String getCogidoSRI() {
        return cogidoSRI;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
