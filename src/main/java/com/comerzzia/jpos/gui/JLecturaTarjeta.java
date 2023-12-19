/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JLecturaTarjeta.java
 *
 * Created on 24-oct-2011, 19:25:40
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.servicios.login.Sesion;

/**
 *
 * @author MGRI
 */
public class JLecturaTarjeta extends JVentanaDialogo {
    
    private String bine="";
    
    /** Creates new form JLecturaTarjeta */
    public JLecturaTarjeta() {
        bine = "";
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        t_bine_desarrollo = new javax.swing.JTextField();

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()+2f));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Leyendo Tarjeta ...");

        t_bine_desarrollo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_bine_desarrolloKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_bine_desarrollo, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(t_bine_desarrollo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        // La acción keytyped no funcionea asociada a un panel
        
    }//GEN-LAST:event_formKeyTyped

    private void t_bine_desarrolloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_bine_desarrolloKeyTyped
        // asociamos la acción de lectura a un campo de texto
        if (evt.getKeyChar() == '\n'){
            if (Sesion.getDatosConfiguracion().isModoDesarrollo()){
                this.bine = t_bine_desarrollo.getText();               
            }
            accionAceptar();
        }
        else{
            this.bine = bine + evt.getKeyChar();
            
        }
    }//GEN-LAST:event_t_bine_desarrolloKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField t_bine_desarrollo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void accionAceptar() {
        
        cerrarVentana();
    }

    @Override
    public void limpiarFormulario() {
        
    }

    public String getBine() {
        return bine;
    }

    public void setBine(String bine) {
        this.bine = bine;
    }
    
    public void iniciaVista(){
        this.t_bine_desarrollo.setText("");
        this.bine="";
    }
    public void iniciaFoco(){
        t_bine_desarrollo.requestFocus();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }
}
