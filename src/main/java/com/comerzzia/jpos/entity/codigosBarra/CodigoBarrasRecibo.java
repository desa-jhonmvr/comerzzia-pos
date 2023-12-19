/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.codigosBarra;

import com.comerzzia.jpos.servicios.login.Sesion;

/**
 *
 * @author Admin
 */
public abstract class CodigoBarrasRecibo {
    
    public static final String IDENTIFICADOR_INTERNO   = "999";
    
    public static final Integer TIPO_CUPON       = 5;
    public static final Integer TIPO_RESERVACION = 0;
    public static final Integer TIPO_BONO        = 3;  
    
    
    public static final Integer SUBTIPO_CUPON_GENERAL       = 0;
    public static final Integer SUBTIPO_RESERVACION_GENERAL = 0;
    public static final Integer SUBTIPO_RESERVACION_ABONO   = 1;  
    public static final Integer SUBTIPO_BONO_EFECTIVO       = 0;  
    public static final Integer SUBTIPO_BONO_MANUAL         = 1;  
    
    private String almacen;
    protected String codBarras;
    
    public CodigoBarrasRecibo(){
        this.almacen = Sesion.getTienda().getCodalm();
    }
    
    public CodigoBarrasRecibo(String codigoBarras) throws NotAValidBarCodeException{
        codBarras = codigoBarras;
        if (codBarras==null || codBarras.length()<6){
            throw new NotAValidBarCodeException();
        }
        if (codBarras.startsWith("0" + IDENTIFICADOR_INTERNO)){
            codBarras = codBarras.substring(1);
        }
        if (!IDENTIFICADOR_INTERNO.equals(codBarras.substring(0, 3))){
            throw new NotAValidBarCodeException();
        }
        this.almacen = codBarras.substring(3, 6);
    }
    
    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    public abstract Integer getTipoCodigo();
    public abstract Integer getSubTipoCodigo();
    
    // devolverá el código de barras completo
    protected abstract String getCodigoBarras();
    
    // devuelve el prefijo del código de barras al que después habrá que concatenar el identificador concreto
    protected String getPrefijoCodigo(){
        return IDENTIFICADOR_INTERNO 
                + formatear(almacen, 3)
                + formatear(getTipoCodigo(), 2)
                + formatear(getSubTipoCodigo(), 2);
    }

    protected String formatear(Integer valor, int longitud){
        return formatear(valor.toString(), longitud);
    }
    protected String formatear(String valor, int longitud){
        while(valor.length() < longitud){
            valor = "0" + valor;
        }
        return valor;
    }
    
}
