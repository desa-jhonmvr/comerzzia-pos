/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JAnadirInvitado.java
 *
 * Created on 17-oct-2012, 16:07:00
 */
package com.comerzzia.jpos.gui.reservaciones.plannovios;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioMostrar;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorEntero;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTelefono;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import es.mpsistemas.util.log.Logger;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

/**
 *
 * @author MGRI
 */
public class JAnadirInvitado extends JVentanaDialogo implements IVista, IViewerValidationFormError {

    
    private static Logger log = Logger.getMLogger(JAnadirInvitado.class);
    
    private JDialog contenedor;
    private JSeleccionarInvitadoPlan ventana_padre;
    private PlanNovioMostrar manejador;
    private Cliente clienteDefecto = null;
    private List<IValidableForm> formulario;

    private static final int MODO_SELECCION = 0; //Selección del invitado en compra
    private static final int MODO_CREACION = 1; // Creación del Invitado
    private static final int MODO_MODIFICACION = 2; // Creación del Invitado
    private int modo;
    
    
    /** Creates new form JAnadirInvitado */
    public JAnadirInvitado() {
        initComponents();
    }
    
    public JAnadirInvitado(JSeleccionarInvitadoPlan p_anadir_invitados) {
        this.ventana_padre = p_anadir_invitados;
        initComponents();
        formulario = new LinkedList<IValidableForm>();
        inicializaValidacion();
        manejador = PlanNovioMostrar.getIstance();
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

        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_ok = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error = new javax.swing.JLabel();
        t_invitado_telefono = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_invitado_apellidos = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_invitado_nombre = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_titulo = new javax.swing.JLabel();
        lv_invitado_nombre = new javax.swing.JLabel();
        lb_invitado_apellidos = new javax.swing.JLabel();
        lb_invitado_telefono = new javax.swing.JLabel();
        t_titulo = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        lb_itulo = new javax.swing.JLabel();

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(b_cancelar.getFont().deriveFont((float)15));
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_ok.setMnemonic('a');
        b_ok.setText("Aceptar");
        b_ok.setFont(b_ok.getFont().deriveFont((float)15));
        b_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_okActionPerformed(evt);
            }
        });

        t_invitado_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_invitado_nombreActionPerformed(evt);
            }
        });

        lb_titulo.setFont(lb_titulo.getFont().deriveFont((float)18));
        lb_titulo.setText("Alta de Invitado");

        lv_invitado_nombre.setDisplayedMnemonic('n');
        lv_invitado_nombre.setLabelFor(t_invitado_nombre);
        lv_invitado_nombre.setText("Nombre:");

        lb_invitado_apellidos.setDisplayedMnemonic('p');
        lb_invitado_apellidos.setLabelFor(t_invitado_apellidos);
        lb_invitado_apellidos.setText("Apellidos:");

        lb_invitado_telefono.setDisplayedMnemonic('t');
        lb_invitado_telefono.setLabelFor(t_invitado_telefono);
        lb_invitado_telefono.setText("Teléfono:");

        t_titulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tituloActionPerformed(evt);
            }
        });

        lb_itulo.setDisplayedMnemonic('o');
        lb_itulo.setLabelFor(t_titulo);
        lb_itulo.setText("Titulo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lb_error, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addGap(43, 43, 43))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lb_titulo)
                        .addGap(124, 124, 124))))
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_invitado_apellidos)
                    .addComponent(lv_invitado_nombre)
                    .addComponent(lb_invitado_telefono)
                    .addComponent(lb_itulo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(t_titulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(t_invitado_telefono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(t_invitado_nombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(t_invitado_apellidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(t_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_itulo))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lv_invitado_nombre)
                    .addComponent(t_invitado_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_invitado_apellidos)
                    .addComponent(t_invitado_apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_invitado_telefono)
                    .addComponent(t_invitado_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        contenedor.setVisible(false);
}//GEN-LAST:event_b_cancelarActionPerformed

    private void b_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_okActionPerformed
        try {
            
            
            for (IValidableForm iv : formulario) {
                iv.validar();
            }            
                       
            if (modo == MODO_SELECCION){
                manejador.crearInvitadoGestionado(t_invitado_nombre.getText(),t_invitado_apellidos.getText(), t_invitado_telefono.getText(), t_titulo.getText());                      
                manejador.crearInvitado();
                manejador.setInvitadoGestionadoAsSeleccionado();
                contenedor.setVisible(false);
                ventana_padre.getContenedor().setVisible(false);
            }
            if (modo == MODO_CREACION){
                manejador.crearInvitadoGestionado(t_invitado_nombre.getText(),t_invitado_apellidos.getText(), t_invitado_telefono.getText(), t_titulo.getText());
                manejador.crearInvitado();
                contenedor.setVisible(false);            
            }
            if (modo == MODO_MODIFICACION){
                manejador.salvarInvitado(t_invitado_nombre.getText(),t_invitado_apellidos.getText(), t_invitado_telefono.getText(),t_titulo.getText());
                contenedor.setVisible(false);            
            }
            
        } catch (IndexOutOfBoundsException e) {
            JPrincipal.getInstance().crearAdvertencia("Debe seleccionar un invitado para aceptar. ");
        } catch (Exception e) {
            log.error("Error Añadiendo Invitado: "+e.getMessage(),e);
            lb_error.setText(e.getMessage());
        }
}//GEN-LAST:event_b_okActionPerformed

    private void t_invitado_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_invitado_nombreActionPerformed
        
}//GEN-LAST:event_t_invitado_nombreActionPerformed

    private void t_tituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tituloActionPerformed
        
    }//GEN-LAST:event_t_tituloActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ok;
    private javax.swing.JLabel lb_error;
    private javax.swing.JLabel lb_invitado_apellidos;
    private javax.swing.JLabel lb_invitado_telefono;
    private javax.swing.JLabel lb_itulo;
    private javax.swing.JLabel lb_titulo;
    private javax.swing.JLabel lv_invitado_nombre;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_invitado_apellidos;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_invitado_nombre;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_invitado_telefono;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_titulo;
    // End of variables declaration//GEN-END:variables

    private void inicializaValidacion() {

        // APELLIDOS: CAMPO OBLIGATORIO: 1 a 100
        t_invitado_apellidos.setFormatearTexto(true);
        t_invitado_apellidos.addValidador(new ValidadorObligatoriedad(), this);
        t_invitado_apellidos.addValidador(new ValidadorTexto(100), this);
        formulario.add(t_invitado_apellidos);
        // NOMBRES: CAMPO OBLIGATORIO:
        t_invitado_nombre.setFormatearTexto(true);
        t_invitado_nombre.addValidador(new ValidadorObligatoriedad(), this);
        t_invitado_nombre.addValidador(new ValidadorTexto(100), this);
        formulario.add(t_invitado_nombre);
        // TELEFONO PARTICULAR

        t_invitado_telefono.addValidador(new ValidadorTelefono(), this);
        
        formulario.add(t_invitado_telefono);
        
        t_titulo.addValidador(new ValidadorTexto(100), this);
        t_titulo.setFormatearTexto(true);
        formulario.add(t_titulo);
    }

    @Override
    public void iniciaVista() {

        for (IValidableForm iv : formulario) {
            iv.setValidacionHabilitada(false);
        }
        
        if (PlanNovioMostrar.getPlanNovio().getModo() == PlanNovioOBJ.MODO_COMPRAR_ARTICULO){
            modo =MODO_CREACION; 
            lb_titulo.setText("Alta de Invitado");
        }
        
        if (modo==MODO_MODIFICACION){
            t_invitado_nombre.setText(manejador.getInvitadoGestionado().getNombre());
            t_invitado_apellidos.setText(manejador.getInvitadoGestionado().getApellido());
            t_invitado_telefono.setText(""+manejador.getInvitadoGestionado().getTelefono()); 
            
            if (manejador.getInvitadoGestionado().getTitulo()!=null){
                t_titulo.setText(""+manejador.getInvitadoGestionado().getTitulo()); 
            }
            else{
                t_titulo.setText("");
            }
            lb_titulo.setText("Editar Invitado");
        }                
        else {
            lb_titulo.setText("Alta de Invitado");
            t_invitado_nombre.setText("");
            t_invitado_apellidos.setText("");
            t_invitado_telefono.setText("");
            t_titulo.setText("");
        }
        t_titulo.requestFocus();

        for (IValidableForm iv : formulario) {
            iv.setValidacionHabilitada(true);
        }

        clearError();
    }

    @Override
    public void addError(ValidationFormException e) {
        lb_error.setText(e.getMessage());
        URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/error.gif");
        lb_error.setIcon(new ImageIcon(myurl));
    }

    @Override
    public void clearError() {
        lb_error.setText("");
        lb_error.setIcon(null);
    }

    @Override
    public JDialog getContenedor() {
        return contenedor;
    }

    @Override
    public void setContenedor(JDialog contenedor) {
        this.contenedor = contenedor;
    }

    @Override
    public void iniciaFoco() {
        t_invitado_nombre.requestFocus();
    }

    void setClienteDefecto(Cliente invitadoActivo) {
        this.clienteDefecto = invitadoActivo;
    }

    public Cliente getClienteDefecto() {
        return clienteDefecto;
    }

    @Override
    public void accionAceptar() {
    }

    @Override
    public void limpiarFormulario() {
    }

    @Override
    public void cerrarVentana() {
        limpiarFormulario();
        contenedor.setVisible(false);
    }
    
        
    @Override
    public void accionLeerTarjetaVD() {
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }
}
