/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

/**
 *
 * @author gtrujillo
 */
public enum EnumTipoDevolucion {
    TIPO_CONSUMIR_NC("Consumo de cliente", 1L),
    TIPO_DEVOLUCION_DINERO("Devolución de dinero", 2L),
    TIPO_ANULACION_VOUCHER("Anulación Voucher", 3L),
    TIPO_DEVOLUCION_DINERO_CONFIRMADO("Devolución dinero Confirmado", 4L);
    private final String observacion;
    private final Long valor;

    private EnumTipoDevolucion(String observacion, Long valor) {
        this.observacion = observacion;
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public Long getValor() {
        return valor;
    }
}
