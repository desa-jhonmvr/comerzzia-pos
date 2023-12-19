/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JAnulacionDocumento.java
 *
 * Created on 11-mar-2014, 16:47:03
 */
package com.comerzzia.jpos.gui.cajas.anulaciones;


import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.servicios.anulaciones.AnulacionException;
import com.comerzzia.jpos.servicios.anulaciones.AnulacionesServices;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import es.mpsistemas.util.log.Logger;
import java.awt.Dialog;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author SMLM
 */
public class JAnulacionDocumento extends JVentanaDialogo implements IViewerValidationFormError {

    private static Logger log = Logger.getMLogger(JAnulacionDocumento.class);
    List<IValidableForm> formulario;
    String tipo = null;

    /** Creates new form JReimpresionFactura */
    public JAnulacionDocumento() {
        super();
        
        try{
            formulario = new LinkedList<IValidableForm>();  
            initComponents();
            
            //creamos la validación
            inicializaValidacion();
            //creamos el formulario
            crearFormulario();

            t_factura1.setText(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            registraEventoEnterBoton();       
            
            p_motivo.setContenedor(v_motivo);      
        } catch (Exception e) {
            
        }
    }
    
    /** Creates new form JReimpresionFactura */
    public JAnulacionDocumento(String tipo) {
        super();
        this.tipo = tipo;
        try{
            formulario = new LinkedList<IValidableForm>();
            initComponents();

            //creamos la validación
            inicializaValidacion();
            //creamos el formulario
            crearFormulario();

            t_factura1.setText(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                        
            if(tipo.equals(AnulacionesServices.FACTURA)) {
                l_documento.setText("Factura: ");
                jLabel6.setText("ANULACIÓN FACTURA");
                l_documento.setDisplayedMnemonic('F');
            }
            if(tipo.equals(AnulacionesServices.NOTA_CREDITO)) {
                l_documento.setText("Nota de Crédito: ");
                jLabel6.setText("ANULACIÓN NOTA DE CRÉDITO");
                l_documento.setDisplayedMnemonic('N');
                t_factura1.setEnabled(false);
            }
            if(tipo.equals(AnulacionesServices.COMP_GIFTCARD)){
                l_documento.setText("Recibo: ");
                jLabel6.setText("ANULACIÓN COMPRA GIFTCARD");
                l_documento.setDisplayedMnemonic('R');
            }
            if(tipo.equals(AnulacionesServices.RECA_GIFTCARD)) {
                l_documento.setText("Recibo: ");
                jLabel6.setText("ANULACIÓN RECARGA GIFTCARD");
                l_documento.setDisplayedMnemonic('R');                
            }
            if(tipo.equals(AnulacionesServices.CREDITO_ABONO)){ 
                l_documento.setText("Recibo de Pago: ");
                jLabel6.setText("ANULACIÓN PAGO CRÉDITO DIRECTO");
                l_documento.setDisplayedMnemonic('R');
            }
            if( tipo.equals(AnulacionesServices.LETRA_ABONO)) {
                l_documento.setText("Recibo de Pago: ");
                jLabel6.setText("ANULACIÓN PAGO CRÉDITO TEMPORAL");
                l_documento.setDisplayedMnemonic('R');                
            }
            
            registraEventoEnterBoton();
            
            p_motivo.setContenedor(v_motivo);      
        }        
        catch (Exception e){
                        
        }
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v_motivo = new javax.swing.JDialog();
        p_motivo = new com.comerzzia.jpos.gui.cajas.anulaciones.motivo.JMotivoAnullacion();
        l_documento = new javax.swing.JLabel();
        t_factura1 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_factura2 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_factura3 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_previsualizar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error = new javax.swing.JLabel();
        b_aceptar1 = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();

        v_motivo.setAlwaysOnTop(true);
        v_motivo.setMinimumSize(new java.awt.Dimension(434, 305));

        javax.swing.GroupLayout v_motivoLayout = new javax.swing.GroupLayout(v_motivo.getContentPane());
        v_motivo.getContentPane().setLayout(v_motivoLayout);
        v_motivoLayout.setHorizontalGroup(
            v_motivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_motivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        v_motivoLayout.setVerticalGroup(
            v_motivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(v_motivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setMaximumSize(new java.awt.Dimension(520,470));
        setMinimumSize(new java.awt.Dimension(520,470));

        l_documento.setDisplayedMnemonic('f');
        l_documento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_documento.setLabelFor(t_factura2);
        l_documento.setAlignmentY(0.0F);

        t_factura1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura1.setNextFocusableComponent(t_factura2);

        t_factura2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura2.setNextFocusableComponent(t_factura3);

        t_factura3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura3.setNextFocusableComponent(jTextArea);

        jLabel4.setText("-");

        jLabel5.setText("-");

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel6.setText("ANULACIÓN");

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_cancelar.setNextFocusableComponent(t_factura1);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_previsualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_previsualizar.setMnemonic('P');
        b_previsualizar.setText("Previsualizar");
        b_previsualizar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_previsualizar.setNextFocusableComponent(b_aceptar1);
        b_previsualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_previsualizarActionPerformed(evt);
            }
        });

        lb_error.setForeground(new java.awt.Color(204, 0, 0));

        b_aceptar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_aceptar1.setMnemonic('a');
        b_aceptar1.setText("Aceptar");
        b_aceptar1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_aceptar1.setNextFocusableComponent(b_cancelar);
        b_aceptar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aceptar1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Motivo Anulación");

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane2.setViewportView(jTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_error, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 27, Short.MAX_VALUE)))
                .addGap(49, 49, 49))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(l_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(t_factura1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(t_factura2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_factura3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(41, 41, 41))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(b_previsualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(b_aceptar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(l_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_factura1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_factura2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_factura3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_previsualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_aceptar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        accionCancelar();
}//GEN-LAST:event_b_cancelarActionPerformed

    private void b_aceptar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aceptar1ActionPerformed
        accionAceptar();
    }//GEN-LAST:event_b_aceptar1ActionPerformed

    private void b_previsualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_previsualizarActionPerformed
        accionPrevisualizar();
    }//GEN-LAST:event_b_previsualizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar1;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_previsualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm jTextArea;
    private javax.swing.JLabel l_documento;
    private javax.swing.JLabel lb_error;
    private com.comerzzia.jpos.gui.cajas.anulaciones.motivo.JMotivoAnullacion p_motivo;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura1;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura2;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura3;
    private javax.swing.JDialog v_motivo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void accionAceptar() {
        try {
            validarFormulario();
            contenedor.setVisible(false);
            if(tipo.equals(AnulacionesServices.FACTURA)){
                try{
                    TicketsAlm ticket = TicketService.consultarTicket(new Long(t_factura3.getText()), t_factura2.getText(), t_factura1.getText());
                    if(ticket.isAnulado()){
                        throw new ValidationException("La factura fue anulada con anterioridad");
                    }   
                } catch (NoResultException e) {
                    // No se ha encontrado el Ticket
                    throw new ValidationException("No se encontró el ticket");
                }
                String motivo = crearVentanaMotivoAnulacion();
                if(motivo ==  null || motivo.isEmpty()) {
                    throw new ValidationFormException("Tiene que existir un motivo de anulacion");
                }
                /**
                 *Rd Se agrega el campo Observacion
                 */
                AnulacionesServices.anularFactura(t_factura1.getText(), t_factura2.getText(), new Long(t_factura3.getText()), motivo,jTextArea.getText());
                JPrincipal.getInstance().crearConfirmacion("La factura se ha anulado correctamente");                       
            }
            if(tipo.equals(AnulacionesServices.NOTA_CREDITO)){
                if(Sesion.isSukasa()){
                    try{
                        NotasCredito nc = DevolucionesServices.consultarNotaCreditoAnulacion(t_factura1.getText(), t_factura2.getText(),new Long(t_factura3.getText()));

                        if(nc.getAnulado()=='S'){
                            throw new ValidationException("La nota de crédito ya ha sido anulada");
                        }   
                    } catch (NoResultException e) {
                         throw new AnulacionException("No se encontró la nota de crédito.", e);     
                    }
                    String motivo = crearVentanaMotivoAnulacion();
                    if(motivo ==  null || motivo.isEmpty()) {
                        throw new ValidationFormException("Tiene que existir un motivo de anulacion");
                    }  
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    AnulacionesServices.anularNotaCreditoSukasa(t_factura1.getText(), t_factura2.getText(), t_factura3.getText(), motivo,jTextArea.getText());
                } else {
                    AnulacionesServices.anularNotaCreditoBebemundo(t_factura1.getText(), t_factura2.getText(), new Long(t_factura3.getText()));
                }
                JPrincipal.getInstance().crearConfirmacion("La nota de crédito se ha anulado correctamente");                   
            }
            if(tipo.equals(AnulacionesServices.CREDITO_ABONO)){
                /**
                 *Rd Se agrega el campo Observacion
                 */
                AnulacionesServices.anularPagoCreditoDirecto(t_factura1.getText(), t_factura2.getText(), t_factura3.getText(),jTextArea.getText());
                JPrincipal.getInstance().crearConfirmacion("El pago a crédito directo se ha anulado correctamente");                   
            }                
            if(tipo.equals(AnulacionesServices.COMP_GIFTCARD)){
                /**
                 *Rd Se agrega el campo Observacion
                 */
                
                AnulacionesServices.anularCompraGiftCard(t_factura1.getText(), t_factura2.getText(), t_factura3.getText(),jTextArea.getText());
                JPrincipal.getInstance().crearConfirmacion("La compra de GiftCard se ha anulado correctamente");                   
            }
            if(tipo.equals(AnulacionesServices.RECA_GIFTCARD)){
                /**
                 *Rd Se agrega el campo Observacion
                 */
                AnulacionesServices.anularRecargaGiftCard(t_factura1.getText(), t_factura2.getText(), t_factura3.getText(),jTextArea.getText());
                JPrincipal.getInstance().crearConfirmacion("La recarga de GiftCard se ha anulado correctamente");                   
            }                
            if(tipo.equals(DocumentosBean.LETRA_ABONO)){
                /**
                 *Rd Se agrega el campo Observacion
                 */
                AnulacionesServices.anularPagoCreditoTemporal(t_factura1.getText(), t_factura2.getText(), t_factura3.getText(),jTextArea.getText());
                JPrincipal.getInstance().crearConfirmacion("El pago a crédito temporal se ha anulado correctamente");                   
            }        
        } catch (ValidationFormException ex) {
            addError(ex);
        } catch(AnulacionException ex) {
            addError(ex);
        } catch (ValidationException ex) {
            addError(ex);
        } catch (NumberFormatException ex) {
            lb_error.setText("No se encontro el documento");
        } catch (Exception ex) {
            addError(ex);
        }
        contenedor.setVisible(true);
    }

    public void accionPrevisualizar() {
        try {
            validarFormulario();
            String giftcard="";
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(this.tipo, t_factura1.getText(), t_factura2.getText(), t_factura3.getText());
            if(tipo.equals(AnulacionesServices.COMP_GIFTCARD) || tipo.equals(AnulacionesServices.RECA_GIFTCARD)){
                giftcard = DocumentosBean.GIFTCARD;
                documentoBean = DocumentosService.consultarDocByUniqueKey(giftcard, t_factura1.getText(), t_factura2.getText(), t_factura3.getText());
            }            
            if(documentoBean != null){
                if(tipo.equals(DocumentosBean.FACTURA)){
                /**
                 *Rd Se agrega el campo Observacion
                 */
             
                PrintServices.getInstance().reimpresionFactura(documentoBean,true,true,false,false,false,"", false);
                }
                if(tipo.equals(DocumentosBean.NOTA_CREDITO)){
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    PrintServices.getInstance().reimpresionNotaCredito(documentoBean,true,"");
                }
                if(tipo.equals(DocumentosBean.CREDITO_ABONO)){
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    PrintServices.getInstance().reimpresionCreditoAbono(documentoBean, true,"");
                }                
                if(tipo.equals(AnulacionesServices.COMP_GIFTCARD)){
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    PrintServices.getInstance().reimpresionGiftCard(documentoBean, true,"",Boolean.FALSE);
                }
                if(tipo.equals(AnulacionesServices.RECA_GIFTCARD)){
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    PrintServices.getInstance().reimpresionGiftCard(documentoBean, true,"",Boolean.FALSE);
                }                
                if(tipo.equals(DocumentosBean.LETRA_ABONO)){
                    /**
                     *Rd Se agrega el campo Observacion
                     */
                    PrintServices.getInstance().reimpresionLetraAbono(documentoBean, true,"");
                }   
            } else {
                JPrincipal.getInstance().crearAdvertencia("No se han encontrado documentos para los datos introducidos.");
            }        
        } catch (ValidationFormException ex) {
            addError(ex);
        } catch (DocumentoException ex) {
            addError(ex);
        } 
    }
    
    @Override
    public void limpiarFormulario() {
        this.t_factura1.setEnabled(true);
        for (IValidableForm elem : formulario) {
            elem.setValidacionHabilitada(false);
        }

        for (IValidableForm elem : formulario) {
            elem.setText("");
        }
        
        this.t_factura2.requestFocus();

        for (IValidableForm elem : formulario) {
            elem.setValidacionHabilitada(true);
        }
        
        this.lb_error.setText("");
    }

    private void crearFormulario() {
        // Elementos del formulario susceptibles de validarse o resetearse 
        formulario.add(t_factura2);
        formulario.add(t_factura3);
        /**
         *Rd Se agrega el campo Observacion
         */
        formulario.add(jTextArea);
    }

    @Override
    public void addError(ValidationFormException e) {
        lb_error.setText(e.getMessage());
    }
    
    public void addError(Exception e) {
        lb_error.setText(e.getMessage());
    }   

    @Override
    public void clearError() {
        lb_error.setText("");
    }

    private void inicializaValidacion() {
        t_factura1.addValidador(new ValidadorObligatoriedad(), this);
        t_factura1.addValidador(new ValidadorTexto(3,true),this);
        t_factura2.addValidador(new ValidadorObligatoriedad(), this);
        t_factura2.addValidador(new ValidadorTexto(3, true), this);
        t_factura3.addValidador(new ValidadorObligatoriedad(), this);
        t_factura3.addValidador(new ValidadorTexto(10), this);
        /**
         * Se agrega el campo Observacion
         */
        jTextArea.addValidador(new ValidadorObligatoriedad(), this);
    }

    private void validarFormulario() throws ValidationFormException {
        for (IValidableForm e : formulario) {
            try {
                e.validar();
            }
            catch (ValidationFormException ex) {
                throw ex;
            }
        }
    }

    public void iniciaVista() {
        limpiarFormulario();
        t_factura1.setText(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));     
        t_factura2.requestFocus();
        
            if(tipo.equals(AnulacionesServices.FACTURA)) {
                l_documento.setText("Factura: ");
                jLabel6.setText("ANULACIÓN FACTURA");
                l_documento.setDisplayedMnemonic('F');
            }
            if(tipo.equals(AnulacionesServices.NOTA_CREDITO)) {
                l_documento.setText("Nota de Crédito: ");
                jLabel6.setText("ANULACIÓN NOTA DE CRÉDITO");
                l_documento.setDisplayedMnemonic('N');
                t_factura1.setEnabled(false);
            }
            if(tipo.equals(AnulacionesServices.COMP_GIFTCARD)){
                l_documento.setText("Recibo: ");
                jLabel6.setText("ANULACIÓN COMPRA GIFTCARD");
                l_documento.setDisplayedMnemonic('R');
            }
            if(tipo.equals(AnulacionesServices.RECA_GIFTCARD)) {
                l_documento.setText("Recibo: ");
                jLabel6.setText("ANULACIÓN RECARGA GIFTCARD");
                l_documento.setDisplayedMnemonic('R');                
            }
            if(tipo.equals(AnulacionesServices.CREDITO_ABONO)){ 
                l_documento.setText("Recibo de Pago: ");
                jLabel6.setText("ANULACIÓN PAGO CRÉDITO DIRECTO");
                l_documento.setDisplayedMnemonic('R');
            }
            if( tipo.equals(AnulacionesServices.LETRA_ABONO)) {
                l_documento.setText("Recibo de Pago: ");
                jLabel6.setText("ANULACIÓN PAGO CRÉDITO TEMPORAL");
                l_documento.setDisplayedMnemonic('R');                
            }      
    }
    
        
    @Override
    public void accionLeerTarjetaVD() {
    }
    
    public String crearVentanaMotivoAnulacion() {
        log.info("Creando ventana de motivo anulacion ");     
        p_motivo.setMotivo("");
        p_motivo.clearError();
        v_motivo.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        v_motivo.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        v_motivo.setSize(400,320);
        p_motivo.setSize(400,320);
        v_motivo.setLocationRelativeTo(null);    
        p_motivo.iniciaVista();
        v_motivo.setVisible(true);
        return p_motivo.getMotivo();        
    }
}
