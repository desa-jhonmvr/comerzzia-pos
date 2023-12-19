/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JVentaEntreLocales.java
 *
 * Created on 20-sep-2011, 13:40:39
 */
package com.comerzzia.jpos.gui;

import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.util.Vector;

/**
 *
 * @author MGRI
 */
public class JVentaEntreLocales extends JVentanaDialogo {

    /** Creates new form JVentaEntreLocales */
    public JVentaEntreLocales() {
        initComponents();
        registraEventoEnterBoton();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        tiendasKeySelectionManager1 = new com.comerzzia.jpos.gui.modelos.TiendasKeySelectionManager();
        sesion1 = new com.comerzzia.jpos.servicios.login.Sesion();
        sesion1.listaTiendas=Sesion.listaTiendas;
        tiendasListRenderer1 = new com.comerzzia.jpos.gui.modelos.TiendasListRenderer();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jb_ok1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jb_cancel1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        l_tienda_origen = new javax.swing.JComboBox();
        l_tienda_destino = new javax.swing.JComboBox();
        t_codigo_confirmacion = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setDisplayedMnemonic('t');
        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 16));
        jLabel1.setLabelFor(l_tienda_origen);
        jLabel1.setText("Tienda en la que se encuentran los artículos");

        jLabel2.setDisplayedMnemonic('i');
        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 0, 16)); // NOI18N
        jLabel2.setLabelFor(l_tienda_destino);
        jLabel2.setText("Tienda en la que se recogerán los artículos");

        jb_ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        jb_ok1.setMnemonic('a');
        jb_ok1.setText("Aceptar");
        jb_ok1.setFont(new java.awt.Font("Tahoma", 0, 18));
        jb_ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_ok1ActionPerformed(evt);
            }
        });

        jb_cancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        jb_cancel1.setText("Cancelar");
        jb_cancel1.setFont(new java.awt.Font("Tahoma", 0, 18));
        jb_cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancel1ActionPerformed(evt);
            }
        });

        l_tienda_origen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        l_tienda_origen.setKeySelectionManager(tiendasKeySelectionManager1);
        l_tienda_origen.setRenderer(tiendasListRenderer1);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaTiendas}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE, sesion1, eLProperty, l_tienda_origen);
        bindingGroup.addBinding(jComboBoxBinding);

        l_tienda_destino.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        l_tienda_destino.setKeySelectionManager(tiendasKeySelectionManager1);
        l_tienda_destino.setRenderer(tiendasListRenderer1);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listaTiendas}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE, sesion1, eLProperty, l_tienda_destino);
        bindingGroup.addBinding(jComboBoxBinding);

        jLabel3.setDisplayedMnemonic('c');
        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 0, 16)); // NOI18N
        jLabel3.setLabelFor(t_codigo_confirmacion);
        jLabel3.setText("Código de confirmación");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(l_tienda_destino, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(l_tienda_origen, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(t_codigo_confirmacion, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addGap(61, 61, 61))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_tienda_origen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_tienda_destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_codigo_confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jb_ok1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_ok1ActionPerformed
        accionAceptar();
}//GEN-LAST:event_jb_ok1ActionPerformed

    private void jb_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancel1ActionPerformed
        accionCancelar();
}//GEN-LAST:event_jb_cancel1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_cancel1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm jb_ok1;
    private javax.swing.JComboBox l_tienda_destino;
    private javax.swing.JComboBox l_tienda_origen;
    private com.comerzzia.jpos.servicios.login.Sesion sesion1;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_codigo_confirmacion;
    private com.comerzzia.jpos.gui.modelos.TiendasKeySelectionManager tiendasKeySelectionManager1;
    private com.comerzzia.jpos.gui.modelos.TiendasListRenderer tiendasListRenderer1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    @Override
    public void accionAceptar() {
        try{
        validarFormulario();
        String almacenOrigen=((Almacen)l_tienda_origen.getSelectedItem()).getDesalm(); 
        String codAlmacenOrigen=((Almacen)l_tienda_origen.getSelectedItem()).getCodalm(); 
        String almacenDestino=((Almacen)l_tienda_destino.getSelectedItem()).getDesalm(); 
        String codAlmacenDestino=((Almacen)l_tienda_destino.getSelectedItem()).getCodalm(); 
        Sesion.getTicket().crearDatosVentaEntreLocales(codAlmacenOrigen,codAlmacenDestino, almacenOrigen,almacenDestino, t_codigo_confirmacion.getText());
        this.getVentana_padre().crearConfirmacion("Se ha establecido la venta en otro local");
        this.getContenedor().setVisible(false);
        }
        catch (ValidationException e){
            this.getVentana_padre().crearError(e.getMessage());                    
        }        
        catch (Exception e){
            this.getVentana_padre().crearError("No se pudo realizar la venta entre locales");                    
        }
    }

    @Override
    public void limpiarFormulario() {
        l_tienda_origen.setSelectedIndex(0);
        l_tienda_destino.setSelectedIndex(0);
        t_codigo_confirmacion.setText("");
    }

    private void validarFormulario() throws ValidationException {
        if (l_tienda_destino.getSelectedItem().equals(l_tienda_origen.getSelectedItem())){
            throw new ValidationException("La tienda de origen y destino han de ser distintas");
        }
    }

    void iniciaVista() {
        
       if (Sesion.getTicket().esVentaEntreLocales()){
            l_tienda_origen.setSelectedItem(new Almacen(Sesion.getTicket().getVentaEntreLocales().getCodTiendaOrigen()));
            l_tienda_destino.setSelectedItem(new Almacen(Sesion.getTicket().getVentaEntreLocales().getCodTiendaDestino()));
            t_codigo_confirmacion.setText("");
        }
        l_tienda_origen.requestFocus();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }
}
