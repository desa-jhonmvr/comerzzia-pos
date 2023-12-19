/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import javax.swing.UIDefaults;

/**
 *
 * @author MGRI
 */
public class Apariencia {
    
    private static  UIDefaults aparienciaBoton;
    private static  UIDefaults aparienciaCuadroTexto;

    public  static UIDefaults getAparienciaBoton() {
        return aparienciaBoton;
    }

    public static void setAparienciaBoton(UIDefaults aparienciaBoton) {
        Apariencia.aparienciaBoton = aparienciaBoton;
    }

    public static UIDefaults getAparienciaCuadroTexto() {
        return aparienciaCuadroTexto;
    }

    public static void setAparienciaCuadroTexto(UIDefaults aparienciaCuadroTexto) {
        Apariencia.aparienciaCuadroTexto = aparienciaCuadroTexto;
    }
}
