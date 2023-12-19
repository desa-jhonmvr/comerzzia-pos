/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.variables;

import com.comerzzia.jpos.servicios.print.PrintServices;
import es.mpsistemas.util.log.Logger;
import javax.print.PrintService;

/**
 *
 * @author MGRI
 */
public class ConfigImpresion {
    
    private static final Logger log = Logger.getMLogger(ConfigImpresion.class);
    
    private Integer impresora;
    private Integer impresiones;    
    
    public ConfigImpresion (String valor){    
        try{
            String[] par = valor.split("/");                
            
            if (par[0].equals("A")){
                impresora = PrintServices.IMPRESORA_TICKET;
            }
            else if (par[0].equals("B")){
                impresora = PrintServices.IMPRESORA_COMPROBANTES;
            }
            else if (par[0].equals("P")){
                impresora = PrintServices.IMPRESORA_PANTALLA;
            }
            else{
                log.error("Error parseando variable de configuración de Impresión. Impresora invalida: "+par[0] +" utilizando impresora por defecto");
                impresora = 1;
            }
            
            impresiones = Integer.valueOf(par[1]);
        }
        catch (Exception e){
            log.error("Error parseando variable de configuración de Impresión: "+valor + "  Se utilizarán valores por defecto");
            impresora = 1;
            impresiones = 1;            
        }        
    }

    public Integer getImpresora() {
        return impresora;
    }

    public void setImpresora(Integer impresora) {
        this.impresora = impresora;
    }

    public Integer getImpresiones() {
        return impresiones;
    }

    public void setImpresiones(Integer impresiones) {
        this.impresiones = impresiones;
    }
    
    public boolean isHayConfigImpresion(){
        return (this.impresiones!=null && this.impresora!=null);
    }
    
    
}
