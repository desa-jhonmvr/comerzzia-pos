/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JMotivoAnulacion.java
 *
 * Created on 12-mar-2014, 10:01:54
 */
package com.comerzzia.jpos.gui.cajas.anulaciones.motivo;

import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import javax.swing.SwingUtilities;
/**
 *
 * @author jmc
 */
public class JMotivoAnullacion extends JVentanaDialogo implements IViewerValidationFormError{
    
    private String motivo;
    
    /** Creates new form JMotivoAnullacion */
    public JMotivoAnullacion() {
        
        initComponents();
        t_motivo.setLineWrap(true);
        t_motivo.setWrapStyleWord(true);
        registraEventoEnterBoton();
        t_motivo.addValidador(new ValidadorTexto(300), this);
        t_motivo.addValidador(new ValidadorObligatoriedad(), this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        js_observaciones = new javax.swing.JScrollPane();
        t_motivo = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();
        b_aceptar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        l_pagos_a_cancelar = new javax.swing.JLabel();
        lb_error = new javax.swing.JLabel();

        t_motivo.setColumns(20);
        t_motivo.setRows(5);
        js_observaciones.setViewportView(t_motivo);

        b_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_aceptar.setMnemonic('a');
        b_aceptar.setText("Aceptar");
        b_aceptar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_aceptar.setName(""); // NOI18N
        b_aceptar.setNextFocusableComponent(b_cancelar);
        b_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aceptarActionPerformed(evt);
            }
        });

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_cancelar.setNextFocusableComponent(t_motivo);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        l_pagos_a_cancelar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        l_pagos_a_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_pagos_a_cancelar.setText("MOTIVO ANULACION:");

        lb_error.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(l_pagos_a_cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE))
                    .addComponent(lb_error, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(js_observaciones))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(l_pagos_a_cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(js_observaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aceptarActionPerformed
        accionAceptar();
}//GEN-LAST:event_b_aceptarActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        accionCancelar();
}//GEN-LAST:event_b_cancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private javax.swing.JScrollPane js_observaciones;
    private javax.swing.JLabel l_pagos_a_cancelar;
    private javax.swing.JLabel lb_error;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm t_motivo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void accionAceptar() {
        setMotivo(t_motivo.getText());
        limpiarFormulario();
        cerrarVentana();
    }
    
    @Override
    public void limpiarFormulario() {
        t_motivo.setText("");
    }

    public void iniciaVista() {
        t_motivo.requestFocus();
    }
     
    @Override
    public void accionCancelar(){
        limpiarFormulario();       
        cerrarVentana();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }

    @Override
    public void addError(ValidationFormException e) {
         lb_error.setText(e.getMessage());
    }

    @Override
    public void clearError() {
        lb_error.setText("");
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    @Override
    public void cerrarVentana(){
        this.contenedor.setVisible(false);
    }
    
}