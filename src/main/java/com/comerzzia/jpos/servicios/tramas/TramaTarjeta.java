/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tramas;

import com.comerzzia.jpos.servicios.login.Sesion;

/**
 *
 * @author amos
 */
public class TramaTarjeta {

    protected String prefijo;
    protected int prefijoNumero;
    protected String tipo;
    protected static String prefijoEmpresa;

    public TramaTarjeta(String trama) throws ParserTramaException {
        try {
            // Establecemos el prefijo si no lo está
            if (prefijoEmpresa == null){
                if (Sesion.isSukasa()){
                    prefijoEmpresa = "SK";
                }
                else if (Sesion.isBebemundo()){
                    prefijoEmpresa = "BM";
                }               
            }            
            
            prefijo = trama.substring(0, 2);
            prefijoNumero = Integer.parseInt(trama.substring(2, 7));
            tipo = trama.substring(7, 8);
            System.out.println(" prefijo " + prefijo + " / prefijoNumero / " + prefijoNumero + "tipo " + tipo);
            
            if (!prefijo.equals(prefijoEmpresa)) {
                throw new ParserTramaException("Prefijo de trama incorrecto empresa: " + prefijo);
            }
            if (prefijoNumero < 0 || prefijoNumero > 10000) {
                throw new ParserTramaException("Prefijo de trama incorrecto numero: " + prefijoNumero);
            }

        }
        catch (Exception e) {
            throw new ParserTramaException("Error parseando trama de giftcard: " + trama + "\n\t" + e.getMessage(), e);
        }
    }

    public TramaTarjeta(String trama, String codMedioPago) throws ParserTramaException {
        try{
            if(trama.length()<16){
                throw new ParserTramaException("Error parseando trama de giftcard: " + trama);
            }
            trama = ignorarComienzo(trama);
            Long.parseLong(trama.substring(0,16));
        }
        catch (Exception e) {
            throw new ParserTramaException("Error parseando trama de giftcard: " + trama + "\n\t" + e.getMessage(), e);
        }        
    }
    
    //Puesto que ahora las GiftCard puede tener bines, si empieza por los comienzos de lectura de tarjeta, lo ignoramos.
    protected String ignorarComienzo(String trama) {
             //Si la trama empieza con %B, estamos leyendo la banda magnética y lo ignoramos
            if (trama.startsWith("%B")) {
                trama = trama.substring(2);
            }  
            if (trama.startsWith("B")) {
                trama = trama.substring(1);
            }
            return trama;
    }
}
