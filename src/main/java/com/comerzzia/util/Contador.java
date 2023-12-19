/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util;

/**
 *
 * @author Dren
 */
public class Contador {
    private static Integer contador=0;
    private static boolean bloqueado=false;

    public static Integer getContador() {
        return contador;
    }

    public static void setContador(Integer contador) {
        Contador.contador = contador;
    }

    public static boolean isBloqueado() {
        return bloqueado;
    }

    public static void setBloqueado(boolean bloqueado) {
        Contador.bloqueado = bloqueado;
    }
    
    
    
}
