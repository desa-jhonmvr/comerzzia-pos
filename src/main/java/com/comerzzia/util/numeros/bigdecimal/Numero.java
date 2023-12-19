/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.numeros.bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 * @author MGRI
 */
public class Numero {

    public static final BigDecimal CIEN = new BigDecimal(100);
    public static final BigDecimal CIENDOCE = new BigDecimal(112);
    public static final BigDecimal CIENCATORCE = new BigDecimal(114);
    public static final BigDecimal DOCE = new BigDecimal(12);
    public static final BigDecimal CATORCE = new BigDecimal(14);
    public static final BigDecimal CERO = BigDecimal.ZERO;
    
    /** Devuelve el resultado de calcular a la cantidad indicada el tanto por ciento pasado. 
     * Ejemplo: porcentaje(80,20) = 16 // 20% de 80 = 16
     * @param cantidad
     * @param porcentaje
     * @return 
     */
    public static BigDecimal porcentaje(BigDecimal cantidad, BigDecimal porcentaje){
        return cantidad.multiply(porcentaje).divide(CIEN);
    }

    /** Devuelve el resultado de restar a la cantidad indicada el tanto por ciento pasado. 
     * Ejemplo: menosPorcentaje(80,20) = 64 // 80 - 20% = 64
     * @param cantidad
     * @param porcentaje
     * @return 
     */
    public static BigDecimal menosPorcentaje(BigDecimal cantidad, BigDecimal porcentaje){
        return cantidad.subtract(porcentaje(cantidad, porcentaje));
    }

    public static BigDecimal masPorcentaje(BigDecimal cantidad, BigDecimal porcentaje){
        return cantidad.add(porcentaje(cantidad, porcentaje));
    }

    public static BigDecimal porcentajeR(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear(porcentaje(cantidad, porcentaje));
    }
    
     public static BigDecimal porcentajeT(BigDecimal cantidad, BigDecimal porcentaje){
        return truncar(porcentaje(cantidad, porcentaje));
    }
    public static BigDecimal porcentajeR4(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear4(porcentaje(cantidad, porcentaje));
    }
    
    public static BigDecimal menosPorcentajeR(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear(menosPorcentaje(cantidad, porcentaje));
    }
    public static BigDecimal masPorcentajeR(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear(masPorcentaje(cantidad, porcentaje));
    }
    public static BigDecimal menosPorcentajeR4(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear4(menosPorcentaje(cantidad, porcentaje));
    }
    public static BigDecimal masPorcentajeR4(BigDecimal cantidad, BigDecimal porcentaje){
        return redondear4(masPorcentaje(cantidad, porcentaje));
    }

    public static BigDecimal redondear(BigDecimal cantidad){
        if (cantidad == null){
            return null;
        }
        return cantidad.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public static BigDecimal truncar(BigDecimal cantidad){
        if (cantidad == null){
            return null;
        }
        return cantidad.setScale(2, RoundingMode.DOWN);
    }
    
    public static BigDecimal redondear4(BigDecimal cantidad){
        if (cantidad == null){
            return null;
        }
        return cantidad.setScale(4, BigDecimal.ROUND_HALF_UP);
    }
    
    public static BigDecimal calculaTotalDesdePorcentaje (BigDecimal ustedPaga,BigDecimal descuento){
      BigDecimal divisor = Numero.CIEN.subtract(descuento);
      BigDecimal dividendo = ustedPaga.multiply(Numero.CIEN);
      BigDecimal resultado = dividendo.divide(divisor,4, RoundingMode.HALF_UP);
      return redondear(resultado);
    }
    
    public static boolean valida(String numero) {

        boolean resultado = false;
        try {
            Double.parseDouble(numero);
            resultado=true;
        } catch (Exception e) {
        }
        return resultado;
    }
    
    /** Devuelve la cantidad de a la que tras aplicar el porcentaje indicado resulta la cantidad indicada.
     * ejemplo: getAntesDePorcentaje(112, 12) = 100 >> porque 100 + 12% = 112     */
    public static BigDecimal getAntesDePorcentaje (BigDecimal resultado, BigDecimal porcentaje){
        return resultado.multiply(CIEN).divide(CIEN.add(porcentaje), 4, RoundingMode.HALF_DOWN);
    }
    public static BigDecimal getAntesDePorcentajeR (BigDecimal resultado, BigDecimal porcentaje){
        return redondear(getAntesDePorcentaje(resultado, porcentaje));
    }
    
    public static BigDecimal getAntesDePorcentajeR4 (BigDecimal resultado, BigDecimal porcentaje){
        return redondear4(getAntesDePorcentaje(resultado, porcentaje));
    }    
    
    public static BigDecimal getAntesDePorcentajeT (BigDecimal resultado, BigDecimal porcentaje){
        return truncar(getAntesDePorcentaje(resultado, porcentaje));
    }
            

    /** Devuelve el tanto por ciento de la cantMayor que representa la cantMenor. 
     * Por ejemplo, getTantoPorCientoContenido(50, 20) = 40, Es decir: 40% de 50 = 20 */
    public static BigDecimal getTantoPorCientoContenido(BigDecimal cantMayor, BigDecimal cantMenor) {
        return (cantMenor.multiply(CIEN)).divide(cantMayor, RoundingMode.HALF_DOWN).setScale(2, RoundingMode.HALF_DOWN);
    }
    
    public static BigDecimal getTantoPorCientoContenidoCompleto(BigDecimal cantMayor, BigDecimal cantMenor) {
        return (cantMenor.multiply(CIEN)).divide(cantMayor, RoundingMode.HALF_DOWN);
    }

    /** Devuelve el tanto por ciento restado a una cantidad para poder obtener una segunda cantidad. 
     * Ejemplo: getTantoPorCientoMenosR(50, 20) = 60, Es decir: 50 - 60% = 20
     */
    public static BigDecimal getTantoPorCientoMenosR(BigDecimal cantidadMayor, BigDecimal cantidadMenor){
        if(esMenorAUnCentavo(cantidadMayor)) {
            return CIEN;
        }
        return (cantidadMayor.subtract(cantidadMenor)).multiply(CIEN).divide(cantidadMayor, 2, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal getTantoPorCientoMenos(BigDecimal cantidadMayor, BigDecimal cantidadMenor){
        return (cantidadMayor.subtract(cantidadMenor)).multiply(CIEN).divide(cantidadMayor, 10, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal multiplica(BigDecimal bigDecimal, Integer integer){
        return bigDecimal.multiply(new BigDecimal(integer));
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

    public static String completaconCeros(int aInt, int i) {
        return completaconCeros(String.valueOf(aInt), i);
    }
    
    public static boolean isMenor (BigDecimal a, BigDecimal b){
        return a.compareTo(b) < 0;
    }
    public static boolean isMenorACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) < 0;
    }
    public static boolean isMayor (BigDecimal a, BigDecimal b){
        return a.compareTo(b) > 0;
    }
    public static boolean isMayorACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) > 0;
    }
    public static boolean isIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) == 0;
    }
    public static boolean isIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) == 0;
    }
    public static boolean isMenorOrIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) <= 0;
    }
    public static boolean isMenorOrIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) <= 0;
    }
    public static boolean isMayorOrIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) >= 0;
    }
    public static boolean isMayorOrIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean esMenorAUnCentavo(BigDecimal numero){
        if(numero.compareTo(new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP))<=0 && numero.compareTo(new BigDecimal(-0.01).setScale(2, RoundingMode.HALF_UP))>=0){
            return true;
        }
        return false;
    }
    
    public static boolean esDiferenciaMayorAUnCentavo(BigDecimal numero1, BigDecimal numero2){
        if(numero1.subtract(numero2).abs().compareTo(new BigDecimal(0.02).setScale(2, RoundingMode.HALF_UP))>=0){
            return true;
        }
        return false;
    }
}
