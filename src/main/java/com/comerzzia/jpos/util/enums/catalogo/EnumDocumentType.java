/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums.catalogo;

/**
 *
 * @author Gabriel Simbania
 * @description Enumerador para los tipos de documento de place to Pay
 */
public enum EnumDocumentType {

    CEDULA("CED", "CI"),
    RUC_NATURAL("RUN", "RUC"),
    RUC_JURIDICO("RUJ", "RUC"),
    PASAPORTE("PAS", "PPN");

    private String codigoPOS;
    private String codigoPlaceToPay;

    private EnumDocumentType(String codigoPOS, String codigoPlaceToPay) {
        this.codigoPOS = codigoPOS;
        this.codigoPlaceToPay = codigoPlaceToPay;
    }

    /**
     * @author Gabriel Simbania
     * @param codigoPOS
     * @return 
     */
    public static EnumDocumentType findDocumentTypeByCodigoPOS(String codigoPOS){
        
         for(EnumDocumentType enumDocumentType: EnumDocumentType.values()){
             if(enumDocumentType.getCodigoPOS().equals(codigoPOS)){
                 return enumDocumentType;
             }
         }
         return null;
    }

    public String getCodigoPOS() {
        return codigoPOS;
    }

    public void setCodigoPOS(String codigoPOS) {
        this.codigoPOS = codigoPOS;
    }

    public String getCodigoPlaceToPay() {
        return codigoPlaceToPay;
    }

    public void setCodigoPlaceToPay(String codigoPlaceToPay) {
        this.codigoPlaceToPay = codigoPlaceToPay;
    }

}
