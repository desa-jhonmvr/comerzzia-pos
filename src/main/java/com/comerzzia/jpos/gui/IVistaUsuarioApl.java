/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui;

/**
 *
 * @author MGRI
 */
public interface IVistaUsuarioApl {
    public static final String NOMBRE_ACCION = "taskname";
    
    
    //public Usuario getUsuario(); 
    public void mostrarTarea(String sTarea);
    public void ejecutarTarea(String sTarea);
}
