/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

/**
 *
 * @author Gabriel Simbania
 */
public enum EnumEstado {
    INDETERMINADO(0L),
    ACTIVO(1L),
    BORRADO(2L),
    ANULADO(3L),
    TEMPORAL(4L);

    private Long id;

    private EnumEstado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
