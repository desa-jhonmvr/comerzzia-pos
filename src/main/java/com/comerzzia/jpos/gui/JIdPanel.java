/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIdPanel.java
 *
 * Created on 22-jul-2013, 11:25:19
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import es.mpsistemas.util.log.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author MGRI
 */
public class JIdPanel extends JPanelImagenFondo implements IViewerValidationFormError {

    
    private static Logger log = Logger.getMLogger(JIdPanel.class);
    //private JPagos ventanaPadre;
    private JDialog contenedor;
    private boolean isCancelado = false;
    
    /** Creates new form JIdPanel */
    public JIdPanel() {
        super();
        initComponents();
        super.registraEventoEnterBoton();

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                limpiarFormulario();
                contenedor.setVisible(false);
            }
        };
        addHotKey(esc, "IdentClientesc", listeneresc);

        //t_codAlm.addValidador(new ValidadorEntero(), this);
        t_codAlm.addValidador(new ValidadorTexto(3, true), this);
        t_codCaja.addValidador(new ValidadorTexto(3, true), this);
        t_id.addValidador(new ValidadorTexto(9, true), this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        t_codAlm = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_codCaja = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_id = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jError = new javax.swing.JLabel();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lb_titulo = new javax.swing.JLabel();

        t_codAlm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_codAlm.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_codAlm.setName("total"); // NOI18N
        t_codAlm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_codAlmActionPerformed(evt);
            }
        });

        t_codCaja.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_codCaja.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_codCaja.setName("total"); // NOI18N
        t_codCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_codCajaActionPerformed(evt);
            }
        });

        t_id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_id.setFont(new java.awt.Font("Tahoma", 0, 14));
        t_id.setName("total"); // NOI18N
        t_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_idActionPerformed(evt);
            }
        });

        jError.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jError.setForeground(new java.awt.Color(255, 0, 51));
        jError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jb_ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_ok1.setMnemonic('a');
        jb_ok1.setText("Aceptar");
        jb_ok1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jb_ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_ok1ActionPerformed(evt);
            }
        });
        jb_ok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jb_ok1KeyPressed(evt);
            }
        });

        jLabel3.setText("-");

        jLabel2.setText("-");

        lb_titulo.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jError, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(t_codAlm, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_codCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_id, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jError, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(t_codCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(t_codAlm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void t_codAlmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_codAlmActionPerformed
        
}//GEN-LAST:event_t_codAlmActionPerformed

    private void t_codCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_codCajaActionPerformed
        
}//GEN-LAST:event_t_codCajaActionPerformed

    private void t_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_idActionPerformed
        
}//GEN-LAST:event_t_idActionPerformed

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        if (!t_codAlm.getText().equals("") && !t_codCaja.equals("") && !t_id.getText().equals("")) {  
                
            try{
                Long idl = Long.parseLong(t_id.getText());
                isCancelado = false;
                contenedor.setVisible(false);
            }            
            catch(Exception e){
                jError.setText("El formato del identificador ha de ser un número entero");
                t_id.requestFocus();
            }
        }
        else{
            jError.setText("Faltan datos a introducir");
        }
}//GEN-LAST:event_jb_ok1ActionPerformed

    private void jb_ok1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jb_ok1KeyPressed
        
}//GEN-LAST:event_jb_ok1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jError;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_ok1;
    private javax.swing.JLabel lb_titulo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_codAlm;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_codCaja;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_id;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addError(ValidationFormException e) {
        jError.setText(e.getMessage());
    }

    @Override
    public void clearError() {
        jError.setText("");
    }
    
    
    public void limpiarFormulario() {
        isCancelado = true;
        jError.setText("");
        t_codAlm.setText("");
        t_codCaja.setText("");
        t_id.setText("");
        t_codAlm.requestFocus();
    }
    
    public JDialog getContenedor() {
        return contenedor;
    }

    public void setContenedor(JDialog contenedor) {
        this.contenedor = contenedor;
    }
    
    public void setTitulo(String titulo){
        lb_titulo.setText(titulo);
    }
    
    public String getCodAlm(){
        return t_codAlm.getText();
    }
    
    public String getCodCaja(){
        return t_codCaja.getText();
    }
    
    public String getId(){
        return t_id.getText();
    }
    
    public boolean isCancelado(){
        return isCancelado;
    }
}
