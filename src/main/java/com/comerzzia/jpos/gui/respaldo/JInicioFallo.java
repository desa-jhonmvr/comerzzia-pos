/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JInicioFallo.java
 *
 * Created on 25-ene-2012, 14:18:30
 */
package com.comerzzia.jpos.gui.respaldo;

import es.mpsistemas.util.log.Logger;
import java.awt.BorderLayout;

/**
 *
 * @author MGRI
 */
public class JInicioFallo extends javax.swing.JFrame {

    private static final Logger log = Logger.getMLogger(JInicioFallo.class);

    private static boolean posmaster;
    private boolean modoReserva;
        
    /** Constructor */
    public JInicioFallo() {
        initComponents();
        posmaster = false;        
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(EXIT_ON_CLOSE);    
    }

    /** 
     * Código de generación de formulario
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p_error = new com.comerzzia.jpos.gui.respaldo.JErrorArranque();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(p_error, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void initFrame(boolean posmaster) {
        this.posmaster = posmaster;

        setVisible(true);
        if (posmaster){
            p_error.activaBotonRespaldo();            
        }
        else{
            p_error.desactivaBotonRespaldo();
        }
        
        if (!modoReserva){
            p_error.ocultaBotonRespaldo();
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.respaldo.JErrorArranque p_error;
    // End of variables declaration//GEN-END:variables
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        p_error.tryToClose();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    public void addError(String error) {
        p_error.addError(error);
    }

    public void setModoReserva(boolean modoReserva) {
        this.modoReserva = modoReserva;             
    }
}
