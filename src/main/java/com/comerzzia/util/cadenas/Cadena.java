/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.cadenas;

/**
 *
 * @author amos
 */
public class Cadena {
    
    public static String getCadena(String x, Integer size, String left){
        while (x.length() < size){
            x = left + x;
        }
        return x;
    }

    public static String completaconCeros(String valor, int longitudNecesaria) {
        if (valor == null) {
            valor = "0";
        }
        while (valor.length() < longitudNecesaria) {
            valor = "0" + valor;
        }
        return valor;
    }

     public static String completaconBlancos(String valor, int longitudNecesaria) {
        if (valor == null) {
            valor = "";
        }
        while (valor.length() < longitudNecesaria) {
            valor = valor + " ";
        }
        return valor;
    }

     /**
      *  Ocultamos la tarjeta con Xs excepto los primeros 6 caracteres y los últimos 4
      * @param numeroTarjeta
      * @return 
      */
    public static String ofuscarTarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null){
            return "";
        }
        if (numeroTarjeta.length()<=10){
            return numeroTarjeta;
        }
        String res = numeroTarjeta.substring(0,6) + " ";
        for (int i = 0; i< numeroTarjeta.length()-10;i++){
            res = res+"X";
        }
        res = res + " "+ numeroTarjeta.substring(numeroTarjeta.length()-4,numeroTarjeta.length());
        return res;
    }
 
      /**
      *  Ocultamos la tarjeta con Xs excepto los primeros 6 caracteres y los últimos 3
      * @param numeroTarjeta
      * @return 
      */
    public static String ofuscar3Tarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null){
            return "";
        }
        if (numeroTarjeta.length()<=9){
            return numeroTarjeta;
        }
        String res = numeroTarjeta.substring(0,6) + " ";
        for (int i = 0; i< numeroTarjeta.length()-9;i++){
            res = res+"X";
        }
        res = res + " "+ numeroTarjeta.substring(numeroTarjeta.length()-3,numeroTarjeta.length());
        return res;
    }
         
     public static String primerApellido(String apellido){
         String primerApellido = "";
         if (apellido.contains(" ")){ // Apellido compuesto
             int posPrimerBlanco = apellido.indexOf(" ");
             primerApellido = apellido.substring(0, posPrimerBlanco);
             
             if (primerApellido.toUpperCase().equals("DE") || primerApellido.toUpperCase().equals("DEL")){ // Si el apellido empieza por de o del
                 String restoApellido = apellido.substring(posPrimerBlanco+1,apellido.length());
                 primerApellido += " ";
 
                 if (restoApellido.toUpperCase().startsWith("LA ")){ // Si justo despues hay un la, lo incluimos
                     primerApellido += "la ";
                     restoApellido = restoApellido.substring(3,restoApellido.length());
                 }                 
                 if (restoApellido.contains(" ")){
                     int posicionApellidoExtra = restoApellido.indexOf(" ");
                     primerApellido += restoApellido.substring(0,posicionApellidoExtra);
                 }
                 else{
                     primerApellido += restoApellido;
                 }                 
             }       
         }
         else{ // Solo hay un apellido
             primerApellido = apellido;
         }
         return primerApellido;
     }

     public static int contarCaracter(String cadena, char c){
         int res = 0;
         for(int i=0;i<cadena.length();i++){
             if(cadena.charAt(i)==c){
                 res++;
             }
         }
         return res;
     }
     public static String eliminaCeros(String cadena){
         int i=0;
         while(i<cadena.length()-1){
             if(cadena.charAt(i)!='0'){
                 return cadena.substring(i, cadena.length());
             }
             i++;
         }
         return cadena.substring(i);
     }
}
