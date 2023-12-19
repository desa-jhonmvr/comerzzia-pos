/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.facturaElectronica;

import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author SMLM
 */
public class ConstantesEFacturacion {
    //Tipos de ambiente
    public static final int AMBIENTE_PRUEBA = 1;
    public static final int AMBIENTE_PRODUCCION = 2;
    
    //Tipos de emision
    public static final int EMISION_NORMAL = 1;
    public static final int EMISION_INDISPONIBILIDAD = 2;
    
    //Comprobantes
    public static final String COMPROBANTE_FACTURA = "01";
    public static final String COMPROBANTE_NOTACREDITO = "04";
    public static final String COMPROBANTE_NOTADEBITO = "05";
    public static final String COMPROBANTE_GUIAREMISION = "06";
    public static final String COMPROBANTE_RETENCION = "07";
    
    //Identificaciones
    public static final String IDENTIFICACION_RUC = "04";
    public static final String IDENTIFICACION_CEDULA = "05";
    public static final String IDENTIFICACION_PASAPORTE = "06";
    public static final String IDENTIFICACION_CONSUMIDOR_FINAL = "07";
    public static final String IDENTIFICACION_EXTERIOR = "08";    
    public static final String IDENTIFICADOR_PLACA = "09";    
    
    //Tipos de impuesto
    public static final int TIPO_IVA = 2;
    public static final int TIPO_ICE = 3;
    public static final int TIPO_IRBPNR = 5;
    
    //Porcentaje de impuestos
    public static final int PORCENTAJE_0 = 0;
    public static final int PORCENTAJE_12 = 2;
    public static final int PORCENTAJE_14 = 3;
    public static final int PORCENTAJE_NO_IMPUESTO = 6;
    public static final int PORCENTAJE_EXENTO = 7;
    
    //Moneda
    public static final String MONEDA_DOLAR = "DOLAR";
    
    //Propina
    public static final Double PROPINA = 0.00;
    
    //Exento
    public static final Double VALOR_EXENTO = 0.00;
    
    //Códigosde impuestos de Comerzzia
    public static final String COD_IMP_NORMAL = "1";
    public static final String COD_IMP_EXCENTO = "0";
    
    //Códigos de tarifas
    public static final int TARIFA_IMP_0 = 0;
    public static final int TARIFA_IMP_12 = 12;
    public static final int TARIFA_IMP_14 = 14;
    
    //Compensación del gobierno
    public static final int CODIGO_COMP = 1;
    public static final int TARIFA_COMP = 2;
    
    public static final String ATRIBUTO_COMP = "Compensación Solidaria ";
    public static final String ATRIBUTO_TOTAL_PAGAR = "VALOR A PAGAR:";
    
    public static final Fecha JUNIO_2016 = new Fecha("01/06/2016", Fecha.PATRON_FECHA_CORTA);
    public static final Fecha JUNIO_2017 = new Fecha("01/06/2017", Fecha.PATRON_FECHA_CORTA);
}
