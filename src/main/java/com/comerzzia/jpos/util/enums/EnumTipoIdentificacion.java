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
public enum EnumTipoIdentificacion {

    CEDULA("CED"),
    RUC_NATURAL("RUN"),
    RUC_JURIDICO("RUJ"),
    RUC_GENERAL("RUC"),
    PASAPORTE("PAS");

    private final String codigoPOS;

    private EnumTipoIdentificacion(String codigoPOS) {
        this.codigoPOS = codigoPOS;
    }

    /**
     * @author Gabriel Simbania
     * @param codigoPOS
     * @return
     */
    public static EnumTipoIdentificacion findDocumentTypeByCodigoPOS(String codigoPOS) {

        for (EnumTipoIdentificacion tipoDocumento : EnumTipoIdentificacion.values()) {
            if (tipoDocumento.getCodigoPOS().equals(codigoPOS)) {
                return tipoDocumento;
            }
        }
        return null;
    }

    public String getCodigoPOS() {
        return codigoPOS;
    }

}
