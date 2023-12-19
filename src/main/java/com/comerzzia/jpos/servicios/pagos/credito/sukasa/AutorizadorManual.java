/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito.sukasa;

import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author amos
 */
public class AutorizadorManual {
    
    private static final Logger log = Logger.getMLogger(AutorizadorManual.class);

    private static List<String> numerosMaestros = Arrays.asList(
            "62554", "14955", "60204", "58921", "13188", "23083", "82365", "96759", "81238", "88911", 
            "35057", "31942", "90192", "81763", "26419", "73281", "69154", "84916", "64574", "32029", 
            "70774", "17451", "62293", "41295", "76675", "53077", "18116", "39589", "24315", "13167", 
            "67960", "41912", "99406", "70623", "69301", "97768", "99174", "91590", "30969", "32389", 
            "45984", "72229", "40771", "27781", "93209", "97173", "81001", "33682", "47465", "64424");
    
    
    public static boolean autorizacionManual(String codigo, Integer numCredito, BigDecimal importe){
        try { 
            log.debug("autorizacionManual() - Realizando autorización manual de pago con tarjeta sukasa crédito directo...");
            if (codigo == null || codigo.length() != 5) {
                log.debug("autorizacionManual() - El código es NULL o menor a 5 caracteres");
                return false;
            }
            if (numerosMaestros.contains(codigo)){
                log.debug("autorizacionManual() - El código es un número maestro. Autorización correcta.");
                return true;
            }
            Fecha hoy = new Fecha();
            String diaCodigo = Cadena.getCadena(hoy.getDia().toString(), 2, "0");
            String mesCodigo = Cadena.getCadena(hoy.getMesNumero().toString(), 2, "0");
            String añoCodigo = Cadena.getCadena(hoy.getAño().toString(), 4, "0");
            String numCreditoCodigo = Cadena.getCadena(numCredito.toString(), 6, "0");
            String consumoCodigo = Cadena.getCadena(new Integer(importe.intValue()).toString(), 6, "0");

            String codigoCompleto = diaCodigo + mesCodigo + añoCodigo + numCreditoCodigo + consumoCodigo;
            String codigoAuxiliar = "";
            String codigoEsperado = "";
            for (int i = 19; i > 0; i = i - 2) {
                codigoAuxiliar += codigoCompleto.charAt(i);
            }
            for (int i = 9; i > 0; i = i - 2) {
                codigoEsperado += codigoAuxiliar.charAt(i);
            }
            if (!codigo.equals(codigoEsperado)) {
                log.debug("autorizacionManual() - codigo esperado " + codigoEsperado);
                return false;
            }
       
            return true;
        }
        catch (Exception e) {
            return false;
        }        
    }
}
