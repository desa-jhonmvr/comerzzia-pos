/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author CONTABILIDAD
 */
public class ClaveAccesoSri {
    
     private static String claveGenerada;

    /**
     * Genera la clave de acceso 
     * 
     * @param fechaEmision
     * @param tipoComprobante
     * @param Ruc
     * @param ambiente
     * @param serie
     * @param numeroComprobante
     * @param codigoNumerico
     * @param tipoEmision
     * @return 
     */
    public static String generaClave(Date fechaEmision, String tipoComprobante, String ruc, String ambiente,
            String serie, String numeroComprobante, String codigoNumerico, String tipoEmision) {

        int verificador = 0;

        //si no tiene 13 digitos los complementa con ceros
        if (ruc != null && ruc.length() < 13) {
            ruc = String.format("%013d", ruc);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String fecha = dateFormat.format(fechaEmision);

        StringBuilder clave = new StringBuilder(fecha);
        clave.append(tipoComprobante);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(serie);
        clave.append(numeroComprobante);
        clave.append(codigoNumerico);
        clave.append(tipoEmision);
        
        //verificador al final
        verificador = generaDigitoModulo11(clave.toString());

        clave.append(Integer.valueOf(verificador));
        ClaveAccesoSri.claveGenerada = clave.toString();
        
        if (clave.toString().length() != 49) {
            ClaveAccesoSri.claveGenerada = null;
        }
        return ClaveAccesoSri.claveGenerada;
    }


    public static int generaDigitoModulo11(String cadena) {
        int baseMultiplicador = 7;
        int[] aux = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt("" + cadena.charAt(i));
            aux[i] = aux[i] * multiplicador;
            multiplicador++;
            if (multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }

        if (total == 0 || total == 1) {
            verificador = 0;
        } else {
            verificador = (11 - (total % 11)) == 11 ? 0 : (11 - (total % 11));
        }
        if (verificador == 10){
            verificador = 1; 
        }
        
        return verificador;
    }

 
}
