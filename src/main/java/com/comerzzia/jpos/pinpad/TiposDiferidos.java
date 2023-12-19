/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad;

import es.mpsistemas.util.log.Logger;

/**
 *
 * @author SMLM
 */
public class TiposDiferidos {
    
    protected static Logger log = Logger.getMLogger(TiposDiferidos.class);
    
    public static String dameDiferido(String codigo){
        if(codigo.equals("00")){
            return "PE";
        } else if(codigo.equals("04")){
            return "PR";
        } else if(codigo.equals("05")){
            return "PR";
        } else if(codigo.equals("06")){
            return "PS";
        } else if(codigo.equals("07")){
            return "S1";
        } else if(codigo.equals("09")){
            return "SM";
        } else if(codigo.equals("10")){
            return "PB";
        } else if(codigo.equals("50")){
            return "DP";
        } else if(codigo.equals("52")){
            return "RP";
        } else if(codigo.equals("53")){
            return "A1";
        } else if(codigo.equals("72")){
            return "CC";
        } else if(codigo.equals("73")){
            return "CE";
        } else if(codigo.equals("74")){
            return "PP";
        } else if(codigo.equals("75")){
            return "CT";
        } else if(codigo.equals("01")){
            return "CF";
        } else if(codigo.equals("02")){
            return "CF";
        } else if(codigo.equals("03")){
            return "PI";
        } else if(codigo.equals("51")){
            return "PE";
        }
        log.warn("dameDiferido() - Se ha recibido un diferido que no está registrado, devolvemos el código");
        return codigo;
    }
}
