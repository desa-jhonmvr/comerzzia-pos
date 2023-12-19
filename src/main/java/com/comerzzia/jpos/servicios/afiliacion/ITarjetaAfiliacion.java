/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion;

/**
 *
 * @author amos
 */
public interface ITarjetaAfiliacion {
    
    public static final String TIPO_LECTURA_MANUAL      = "MANUAL";
    public static final String TIPO_LECTURA_AUTOMATICA  = "AUTOMATICA";
    
    public static final String TIPO_AFILIACION_CREDITO_SK       = "CREDITO_SUKASA";
    public static final String TIPO_AFILIACION_TARJETA_BINES    = "TARJETA_BINES";
    public static final String TIPO_AFILIACION_TARJETA_TIENDA   = "TARJETA_TIENDA";
    public static final String TIPO_AFILIACION_CEDULA           = "CEDULA";
    public static final String TIPO_AFILIACION_DESARROLLO       = "DESARROLLO";
    
    public String getNumero();
    
    public String getTipoAfiliacion();
    
    public String getTipoLectura();
    
    
}
