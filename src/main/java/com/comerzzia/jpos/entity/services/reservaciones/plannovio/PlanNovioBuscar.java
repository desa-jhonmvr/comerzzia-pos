/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones.plannovio;

import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import es.mpsistemas.util.log.Logger;
import java.util.List;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.gui.reservaciones.plannovios.modelos.BusquedaPlanesNoviosTableModel;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import java.util.LinkedList;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * Esta calse contiene las operaciones que se pueden hacer sobre el plan novio 
 * en la pantalla de búsquedas de plane (Búsquedas y creación de un nuevo plan).
 * 
 * @author MGRI
 */
public class PlanNovioBuscar {

    public static String OPERACION_NUEVO_PLAN = "nuevo";
    public static String OPERACION_BUSCAR_PLAN = "busqueda";
    public static String operacion = null;
    //Log 
    private static Logger log = Logger.getMLogger(PlanNovioBuscar.class);

    public static List<PlanNovio> getListaPlanesBusqueda() {
        return listaPlanesBusqueda;
    }

    public static void setListaPlanesBusqueda(List<PlanNovio> aListaPlanesBusqueda) {
        listaPlanesBusqueda = aListaPlanesBusqueda;
        
    }

    public static PlanNovioOBJ getPlanNovio() {
        return planNovio;
    }

    public static void setPlanNovio(PlanNovioOBJ aPlanNovio) {
        planNovio = aPlanNovio;
    }

    public static ParametrosBuscarPlanNovio getPlanNovioParam() {
        return planNovioParam;
    }

    public static void setPlanNovioParam(ParametrosBuscarPlanNovio aPlanNovioParam) {
        planNovioParam = aPlanNovioParam;
    }

    public static JTable getTablaResultadoPlanes() {
        return tablaResultadoPlanes;
    }

    public static void setTablaResultadoPlanes(JTable aTablaResultadoPlanes) {
        tablaResultadoPlanes = aTablaResultadoPlanes;
    }

    public static int[] getAnchosColumnasTablaResultadoPlanes() {
        return anchosColumnasTablaResultadoPlanes;
    }

    public static void setAnchosColumnasTablaResultadoPlanes(int[] aAnchosColumnasTablaResultadoPlanes) {
        anchosColumnasTablaResultadoPlanes = aAnchosColumnasTablaResultadoPlanes;
    }
    // Manejador de errores de la pantalla
    private IViewerValidationFormError manejadorErrores;
    // Listado de resultados en lsita
    private static int[] anchosColumnasTablaResultadoPlanes = {60,60, 160, 160, 250, 75, 95, 90};
    private static JTable tablaResultadoPlanes;
    // instancia del manejador
    private static PlanNovioBuscar manejador;
    private static List<PlanNovio> listaPlanesBusqueda;
    private static PlanNovioOBJ planNovio;
    private static ParametrosBuscarPlanNovio planNovioParam;

    public PlanNovioBuscar() {
        
    }

    /** 
     * Inicia el proceso gestión de Plan Novios
     *  Devolviendo una instancia de la clase manejador
     */
    public static PlanNovioBuscar inicia() {
        log.debug("inicia() - Iniciando el proceso de gestión de Plan Novio");
        if (manejador == null) {
            manejador = new PlanNovioBuscar();
        }
        listaPlanesBusqueda = null;
        setPlanNovio(new PlanNovioOBJ());
        setPlanNovioParam(new ParametrosBuscarPlanNovio());
        
        return manejador;
    }

    /**
     * Recupera la instancia sin iniciarla
     * @return 
     */
    public static PlanNovioBuscar getInstance() {
        if (manejador == null) {
            manejador = new PlanNovioBuscar();
        }
        return manejador;
    }

    //Acción de pantalla 
    public void setOperacion(String modo) {
        operacion = modo;
        if (operacion.equals(OPERACION_NUEVO_PLAN)){
            this.planNovio = new PlanNovioOBJ();
            this.planNovio.setModo(PlanNovioOBJ.MODO_CREANDO_PLAN_NOVIA);
        }
    }

    //Acciones de tipo de operación 
    public boolean isOperacionNuevoPlan() {
        boolean res = false;
        if (operacion != null && operacion.equals(OPERACION_NUEVO_PLAN)) {
            res = true;
        }
        return res;
    }

    public boolean isOperacionBusqueda() {
        boolean res = false;
        if (operacion != null && operacion.equals(OPERACION_BUSCAR_PLAN)) {
            res = true;
        }
        return res;
    }

    // Acciones de pantalla
    public void buscarPlanNovio() {
        log.debug("buscarPlanNovio() ");
        try {
            getManejadorErrores().clearError();            
            setListaPlanesBusqueda(PlanNovioBuscar.getInstance().getPlanNovio().buscarPlanNovio(getPlanNovioParam()));
            if (listaPlanesBusqueda.isEmpty()){
                manejadorErrores.addError(new ValidationFormException("No se encontró ningún Plan Novios con los filtros indicados."));
            }
            
        }
        catch (PlanNovioException ex) {
            setListaPlanesBusqueda((List<PlanNovio>) new LinkedList());
            log.error("Error en la consulta al plan novios");
            getManejadorErrores().addError(new ValidationFormException("Error en la consulta al plan novios"));
        }
    }

    public void refrescaTablaPlanes() {
        log.debug("refrescaTablaPlanes() ");
        if (getListaPlanesBusqueda() == null) {
            log.debug("Se refrescó la tabla de planes sin resultados en la tabla");
            listaPlanesBusqueda = ((List<PlanNovio>) new LinkedList());
        }
        tablaResultadoPlanes.setModel(new BusquedaPlanesNoviosTableModel(listaPlanesBusqueda));
        tablaResultadoPlanes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int i = 0;
        for (int tam : getAnchosColumnasTablaResultadoPlanes()) {
            tablaResultadoPlanes.getColumnModel().getColumn(i).setPreferredWidth(tam);
            i++;
        }
    }

    public void estableceParametrosBusqueda(List<IValidableForm> formularioBusqueda, JComboBox l_estado, JComboBox tienda_buscar) {
        getPlanNovioParam().estableceParametros(formularioBusqueda);
        String estadoSeleccionado = ((String)l_estado.getSelectedItem()).trim();
        if (estadoSeleccionado.equals("Todos")){
            estadoSeleccionado = "";
        }
        getPlanNovioParam().setEstadoPlan(estadoSeleccionado);
        Almacen tiendaSeleccionada = ((Almacen) tienda_buscar.getSelectedItem());
        getPlanNovioParam().setTiendaPlan(tiendaSeleccionada.getCodalm());
    }   

    public void seleccionaPlan(int selectedRow) {
        
         PlanNovio consultado = listaPlanesBusqueda.get(tablaResultadoPlanes.getSelectedRow());
         planNovio.establecePlanBuscado(consultado); 
    }

    public IViewerValidationFormError getManejadorErrores() {
        return manejadorErrores;
    }

    public void setManejadorErrores(IViewerValidationFormError manejadorErrores) {
        this.manejadorErrores = manejadorErrores;
    }
}
