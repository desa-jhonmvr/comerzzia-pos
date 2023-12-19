/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui;

/**
 *
 * @author MGRI
 */
public interface IManejadorErrores {

     public boolean crearVentanaConfirmacion(String texto);
     public void crearError(String descripcion);
     public void crearAdvertencia(String descripcion);
     public void crearConfirmacion(String descripcion);
     public void crearInformacion(String descripcion);
     public void crearSinPermisos(String descripcion);

}
