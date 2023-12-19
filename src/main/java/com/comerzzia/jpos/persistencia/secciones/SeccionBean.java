/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.secciones;

/**
 *
 * @author SMLM
 */
public class SeccionBean {
    
    private String codseccion;
    private String desseccion;
    private boolean activo;
    

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodseccion() {
        return codseccion;
    }

    public void setCodseccion(String codseccion) {
        this.codseccion = codseccion;
    }

    public String getDesseccion() {
        return desseccion;
    }

    public void setDesseccion(String desseccion) {
        this.desseccion = desseccion;
    }  
    
    public String toString() {
        return this.desseccion;
    }
}
