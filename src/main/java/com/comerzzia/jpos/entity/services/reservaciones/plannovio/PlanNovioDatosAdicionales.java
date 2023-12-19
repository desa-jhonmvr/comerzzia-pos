/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones.plannovio;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import es.mpsistemas.util.log.Logger;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JLabel;

/**
 *
 * @author MGRI
 */
public class PlanNovioDatosAdicionales {

    //Log 
    private static Logger log = Logger.getMLogger(PlanNovioDatosAdicionales.class);
    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formateadorFechaLarga = new SimpleDateFormat("dd/MM/yyyy - HH:mm", new Locale("es", "ES"));

    public static PlanNovioOBJ getPlanNovio() {
        return planNovio;
    }

    public static void setPlanNovio(PlanNovioOBJ aPlanNovio) {
        planNovio = aPlanNovio;
    }

    public static PlanNovioDatosAdicionales getManejador() {
        return manejador;
    }

    public static void setManejador(PlanNovioDatosAdicionales aManejador) {
        manejador = aManejador;
    }

    public static PlanNovioDatosAdicionales inicia() {
        log.debug("inicia() - Iniciando el proceso de gestión de Plan Novio");
        PlanNovioDatosAdicionales res = manejador.getIstance();

        // Editamos los datos del plan novio referentes a los datos adicionales        
        setPlanNovio(PlanNovioBuscar.getInstance().getPlanNovio());

        return res;
    }

    public static PlanNovioDatosAdicionales getIstance() {
        if (manejador == null) {
            manejador = new PlanNovioDatosAdicionales();
        }
        return manejador;
    }

    public static void estableceDatosNovios(JLabel l_nombre_novia, JLabel l_apellidos_novia, JLabel l_cedula_novia, JLabel l_nombre_novio, JLabel l_apellidos_novio, JLabel l_cedula_novio, JLabel lb_boda) {
        log.debug(" estableceDatosNovios() ");
        Cliente novia = planNovio.getPlan().getNovia();
        Cliente novio = planNovio.getPlan().getNovio();

        l_nombre_novia.setText(novia.getNombre());
        l_apellidos_novia.setText(novia.getApellido());
        l_cedula_novia.setText(novia.getCodcli());
        l_nombre_novio.setText(novio.getNombre());
        l_apellidos_novio.setText(novio.getApellido());
        l_cedula_novio.setText(novio.getCodcli());

        lb_boda.setText(planNovio.getPlan().getTitulo());
    }

    public static void estableceDatosAdicionales(JTextFieldForm t_lugar_boda, JTextFieldForm t_fecha_hora_boda, JTextFieldForm t_fecha_contacto_invitados,JTextFieldForm t_telefono,JTextFieldForm t_email) {
        if (planNovio.isModoEditarDatosGenerales()) {
            
            if (planNovio.getPlan().getFechaContacto()!=null){
                t_fecha_contacto_invitados.setText(formateadorFechaCorta.format(planNovio.getPlan().getFechaContacto()));
            }
            else{
                t_fecha_contacto_invitados.setText("");
            }
            t_fecha_hora_boda.setText(formateadorFechaLarga.format(planNovio.getPlan().getFechaHoraBoda()));
            t_lugar_boda.setText(planNovio.getPlan().getLugar());
            t_telefono.setText(planNovio.getPlan().getTelefono());
            t_email.setText(planNovio.getPlan().getEmail());
        }
    }
    // Manejador de errores de la pantalla
    private IViewerValidationFormError manejadorErrores;
    // Pantalla a la que asociamos esta clase
    private static PlanNovioDatosAdicionales manejador;
    // Objeto Plan Novios
    private static PlanNovioOBJ planNovio;

    public IViewerValidationFormError getManejadorErrores() {
        return manejadorErrores;
    }

    public void setManejadorErrores(IViewerValidationFormError manejadorErrores) {
        this.manejadorErrores = manejadorErrores;
    }

    public void establecerDatosAdicionales(JTextFieldForm t_lugar_boda, JTextFieldForm t_fecha_hora_boda,  JTextFieldForm t_fecha_contacto_invitados ,JTextFieldForm t_telefono,JTextFieldForm t_email) throws PlanNovioException {
        try {
            planNovio.getPlan().setLugar(t_lugar_boda.getText());
            if (!t_fecha_contacto_invitados.getText().isEmpty()){
                planNovio.getPlan().setFechaContacto(formateadorFechaCorta.parse(t_fecha_contacto_invitados.getText()));
            }
            else{
                planNovio.getPlan().setFechaContacto(null);
            }
            planNovio.getPlan().setFechaHoraBoda(formateadorFechaLarga.parse(t_fecha_hora_boda.getText()));
            planNovio.getPlan().setTelefono(t_telefono.getText());
            planNovio.getPlan().setEmail(t_email.getText());
            
        }
        catch (Exception e) {
            log.debug("establecerDatosAdicionales() Error estableciendo Datos Adicionales", e);
            throw new PlanNovioException("Error en el ingreso de los datos Adicionales");
        }
    }

    public void crearPlan() throws PlanNovioException {
        planNovio.crear();

    }

    public void modificaPlan() throws PlanNovioException {
        planNovio.modificarDatosGenerales();
    }
}
