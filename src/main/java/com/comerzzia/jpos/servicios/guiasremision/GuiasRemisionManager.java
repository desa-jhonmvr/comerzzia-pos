/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.guiasremision;

import com.comerzzia.jpos.entity.db.GuiaRemision;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.entity.ticket.IDTicketMalFormadoException;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.tickets.TicketException;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class GuiasRemisionManager {

    private static final Logger log = Logger.getMLogger(GuiasRemisionManager.class);
    private static GuiaRemision gr;
    private static GuiasRemisionManager instancia = null;
    private static boolean accionVolver = false; //Marcador para indicar que acciedemos a la pantalla de guia de remisión pulsando volver
   
    private static SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd-MMM-yyyy");

    public static boolean isAccionVolver() {
        return accionVolver;
    }

    public static void setAccionVolver(boolean aAccionVolver) {
        accionVolver = aAccionVolver;
    }

   
    public GuiasRemisionManager() {
        super();
        gr = new GuiaRemision();
    }

    public static GuiasRemisionManager getInstance() {
        if (instancia == null) {
            instancia = new GuiasRemisionManager();
        }

        return instancia;
    }

    public void setParametrosGuiaRemision(String fechaInicioTraslado, String fechaFinTraslado, String documento, String local1, String local2, String ordenDespacho, String ordenSalida, String numeroPedido, String numeroTransferencia, int tipoDocumento) throws ValidationFormException {
        try {
            gr.setFechaInicio(formateadorFecha.parse(fechaInicioTraslado));
            gr.setFechaFin(formateadorFecha.parse(fechaFinTraslado));

            // Caja del local actual. Puede que cambie a la caja del documento            
            
            gr.setCodLocal1(local1);
            gr.setCodLocal2(local2);            
            gr.setTipoDocumento(tipoDocumento);
            gr.setNumDocumento(documento);

        }
        catch (ParseException ex) {
            log.error("setParametrosGuiaRemision() - Error de formato :" + ex.getMessage(), ex);
            throw new ValidationFormException("Error de formato en la lectura del formulario");
        }

    }

    public void consultaTicket() {
        try{
        ServicioGuiaRemision.consultaDatosTicket(gr);
        
        // Si no hay excepción nos movemos a la segunda pantalla.
        JPrincipal.getInstance().irPantallaGuiaRemision2();
        
        }
        catch(IDTicketMalFormadoException e){
            JPrincipal.getInstance().crearError("Ha de indicar un documento impreso válido con formato xxx-xx-xxxxxxxx");
        }                    
        catch(NoResultException e){
            JPrincipal.getInstance().crearError("El ticket no contiene ninguna línea pediente de envío");
        }
        catch(TicketException e){
            JPrincipal.getInstance().crearError("Ocurrió un error consultando las línea pediente de envío del ticket");
        }
    }

    public void accionSeleccionaArticulo(int selectedRow) {        
       
        LineaTicket lt = gr.getLineas().get(selectedRow);
        if (!lt.isEnvioEnGuiaRemision()) {           
                lt.setEnvioEnGuiaRemision('S');                
                log.debug("Línea marcada para envío.");
            }            
        
        else {
            lt.setEnvioEnGuiaRemision('N');                
            log.debug("Línea desmarcada para envío.");
        }        
    }

    public List<LineaTicket> getLineas() {
        if (gr.getLineas() != null && !gr.getLineas().isEmpty()) {
            return gr.getLineas();
        }
        return new ArrayList<LineaTicket>();
    }

    public GuiaRemision getGuiaRemision() {
        return gr;
    }

    public String getFechaInicioTrasladoStr() {
        return formateadorFecha.format(gr.getFechaInicio());
    }

    public String getFechaFinTrasladoStr() {
        return formateadorFecha.format(gr.getFechaFin());
    }


    public String getCodigoLocal1() {
        return gr.getCodLocal1();
    }
    
    public String getCodigoLocal2() {
        return gr.getCodLocal2();
    }

    public String getCodigoLocal() {
        return gr.getCodalm();
    }

    public String getNumeroDocumentoImpreso() {
        return gr.getNumDocumento();
    }

    public void accionGuardar() throws GuiaRemisionException {
        
        ServicioGuiaRemision.guardar(gr);        
    }

    public void establecerCodigoGR() throws ValidationFormException {
        try {
            gr.setCodcaja(Sesion.getCajaActual().getCajaActual().getCodcaja());
            gr.setCodalm(Sesion.getCajaActual().getCajaActual().getCodalm());
            gr.setIdGuiaRemision(ServicioContadoresCaja.obtenerContadorGuiaRemision());
        }
        catch (Exception ex) {
            log.error("Error estableciendo identificador de Guía de Remisión");            
            throw new ValidationFormException("Error obteniendo contador de Guía de Remisión");
        }
        
    }
    
    public String getIdentificador() {        
       return Numero.completaconCeros(gr.getCodalm(),3) +"-"+ Numero.completaconCeros(gr.getCodcaja(),3) +"-"+ Numero.completaconCeros(""+gr.getIdGuiaRemision(), 7);
    }

    public void reseteaListaLineas() {
        GuiasRemisionManager.gr.setLineas(new LinkedList<LineaTicket>());
    }

    /*
     * Limpia los datos de la Guía de Remisión
     */
    public void limpiarDatosGR() {
        gr.setDestNombre("");
        gr.setDestCedula("");
        gr.setDestCodalm("");
        gr.setDestDireccion("");
        gr.setDestCiudad("");
    }
}
