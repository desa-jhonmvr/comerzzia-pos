/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticuloEnPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;

/**
 *
 * @author amos
 */
public class TicketPromociones {

    protected static Logger log = Logger.getMLogger(TicketPromociones.class);
    private boolean aplicaPromocionesLineas;
    private Map<Long, Boolean> clientePromoAnteriores;
    private Map<Long, Integer> clientePromoAplicadas; // puede estar referenciada a un cliente o a un número de crédito
    private List<Pago> pagosSeleccionados;
    private TicketS ticket;
    private Map<Long, String> mensajesPromocion;
    private List<LineaEnTicket> promocionesPrint;
    private List<LineaEnTicket> promocionesCuponesPagosPrint;
    private TicketPromocionesFiltrosPagos promocionesFiltrosPagos;
    private TicketOrigen diaSocioFacturaOrigen;
    private Integer diaSocioCantidadAplicable;
    private boolean mesesGraciaAplicado;
    
    public TicketPromociones(TicketS ticket) {
        pagosSeleccionados = new ArrayList<Pago>();
        promocionesPrint = new ArrayList<LineaEnTicket>();
        promocionesCuponesPagosPrint = new ArrayList<LineaEnTicket>();
        this.ticket = ticket;
        mesesGraciaAplicado = false;
    }

    public boolean isAplicaPromocionesLineas() {
        return aplicaPromocionesLineas;
    }

    public void setAplicaPromocionesLineas(boolean aplicaPromocionesLineas) {
        this.aplicaPromocionesLineas = aplicaPromocionesLineas;
    }

    public boolean isPromocionAplicadaAnteriormente(Long idPromocion) {
        if (ticket.getCliente().isClienteGenerico()) {
            return false;
        }
        if (clientePromoAnteriores == null) {
            clientePromoAnteriores = new HashMap<Long, Boolean>();
        }
        String codCliente = ticket.getCliente().getCodcli();
        Boolean aplicadaAnteriormente = clientePromoAnteriores.get(idPromocion);
        if (aplicadaAnteriormente != null) {
            return aplicadaAnteriormente;
        }
        try {
            aplicadaAnteriormente = ServicioPromocionesClientes.existePromocionCliente(codCliente, idPromocion);
            clientePromoAnteriores.put(idPromocion, aplicadaAnteriormente);
            return aplicadaAnteriormente;
        }
        catch (PromocionException e) {
            log.warn("isPromocionAplicadaAnteriormente() - Debido al error en la consulta, NO PERMITIMOS APLICAR LA PROMOCIÓN.");
            return true;
        }
    }

    public Map<Long, Boolean> getPromocionesClientes() {
        return clientePromoAnteriores;
    }

    public void setPromocionesClientes(Map<Long, Boolean> promocionesClientes) {
        this.clientePromoAnteriores = promocionesClientes;
    }

    public List<Pago> getPagosSeleccionados() {
        return pagosSeleccionados;
    }

    public boolean isPagosSeleccionados() {
        return pagosSeleccionados != null && !pagosSeleccionados.isEmpty();
    }

    public void setPagosSeleccionados(List<Pago> pagosSeleccionados) {
        this.pagosSeleccionados = pagosSeleccionados;
    }

    public Map<Long, Integer> getClientePromoAplicadas() {
        return clientePromoAplicadas;
    }

    public void addClientePromoAplicada(Long idPromocion) {
        if (clientePromoAplicadas == null) {
            clientePromoAplicadas = new HashMap<Long, Integer> ();
        }
        clientePromoAplicadas.put(idPromocion, null); // cliente
    }
    public void addClientePromoAplicada(Long idPromocion, Integer numCredito) {
        if (clientePromoAplicadas == null) {
            clientePromoAplicadas = new HashMap<Long, Integer> ();
        }
        clientePromoAplicadas.put(idPromocion, numCredito); 
    }

    public void resetearCuponesPagosPrint() {
        promocionesCuponesPagosPrint.clear();
    }

    public void resetear() {
        promocionesCuponesPagosPrint.clear();
        promocionesPrint.clear();
        
        if (clientePromoAplicadas != null) {
            clientePromoAplicadas.clear();
        }
    }

    public void addPromocionPrint(String texto) {
        promocionesPrint.add(new LineaEnTicket(texto));
    }
    public void addPromocionCuponesPagosPrint(String texto) {
        promocionesCuponesPagosPrint.add(new LineaEnTicket(texto));
    }

    public void addPromocionPrint(List<String> lista) {
        for (String p : lista) {
            addPromocionPrint(p);
        }
    }

    public boolean tieneMensajesPromocion() {
        if (mensajesPromocion == null) {
            return false;
        }
        for (Long idPromocion : mensajesPromocion.keySet()) {
            if (!mensajesPromocion.get(idPromocion).equals("MOSTRADO")) {
                return true;
            }
        }
        return false;
    }

    public List<String> getMensajesPromocion() {
        List<String> mensajes = new ArrayList<String>();
        if (mensajesPromocion == null) {
            return mensajes;
        }
        for (Long idPromocion : mensajesPromocion.keySet()) {
            String mensaje = mensajesPromocion.get(idPromocion);
            if (!mensaje.equals("MOSTRADO")) {
                mensajes.add(mensaje);
                mensajesPromocion.put(idPromocion, "MOSTRADO");
            }
        }
        return mensajes;
    }

    public void addMensajePromocion(Long idPromocion, String mensaje) {
        if (mensajesPromocion == null) {
            mensajesPromocion = new HashMap<Long, String>();
        }
        // si ya fue puesto un mensaje para esta promoción, no lo volvemos a poner
        if (mensajesPromocion.containsKey(idPromocion)) {
            return;
        }
        mensajesPromocion.put(idPromocion, mensaje);
    }

    public void removeMensajePromocion(Long idPromocion) {
        if (mensajesPromocion == null) {
            return;
        }
        mensajesPromocion.remove(idPromocion);
    }

    public List<LineaEnTicket> getPromocionesPrint() {
        List<LineaEnTicket> msgPromoPrint = new ArrayList<LineaEnTicket>();
        msgPromoPrint.addAll(promocionesPrint);
        msgPromoPrint.addAll(promocionesCuponesPagosPrint);
        return msgPromoPrint;
    }
    
    public TicketPromocionesFiltrosPagos getPromocionesFiltrosPagos(){
        if (promocionesFiltrosPagos == null){
            promocionesFiltrosPagos = new TicketPromocionesFiltrosPagos(this);
        }
        return promocionesFiltrosPagos;
    }

    public void resetearLinea(LineaTicket linea) {
        if (promocionesFiltrosPagos != null){
            promocionesFiltrosPagos.resetPromocion(linea.getPromocionLinea().getIdPromocion());
        }
    }

    public TicketOrigen getFacturaOrigenDiaSocio() {
        return diaSocioFacturaOrigen;
    }

    public void setFacturaOrigenDiaSocio(TicketOrigen facturaOrigenDiaSocio) {
        this.diaSocioFacturaOrigen = facturaOrigenDiaSocio;
    }
    
    public boolean isFacturaAsociadaDiaSocio(){
        return diaSocioFacturaOrigen != null;
    }

    public boolean preguntarAplicacionDiaSocio(){
            boolean aceptada = true;
            if (Sesion.promocionDiaSocio.tieneFiltroPagos()) {
                aceptada = getPromocionesFiltrosPagos().isAceptada(Sesion.promocionDiaSocio.getIdPromocion());
                if (!aceptada) {
                    String msg = "¿Aplicar promoción " + Sesion.promocionDiaSocio.getDescripcionImpresion() + "?";
                    msg += "\nEs obligatorio utilizar como medio de pago alguno de los siguientes:\n (Aplica restricciones) \n" + Sesion.promocionDiaSocio.getFiltroPagosDescripcion();
                    int altura;
                    if(Sesion.promocionDiaSocio.getSaltosLineaFiltrosPagosDescrpcion() > 10){
                        altura = (22 * msg.split("\n").length);
                    } else {
                        altura = (18 * msg.split("\n").length);
                    }
                    aceptada = JPrincipal.getInstance().crearVentanaConfirmacion(msg, altura);
                    getPromocionesFiltrosPagos().addPromocion(Sesion.promocionDiaSocio, aceptada);
                    promocionesFiltrosPagos.setPromoAceptarDS(1L);
                }
            }
            if (!aceptada) {
                promocionesFiltrosPagos.setPromoAceptarDS(2L);
                desvincularFacturaDiaSocio();
            }
            return aceptada;
        
    }
    
    public void resetearPromocionesUnitarias(){
        Iterator<LineaTicket> lineasTicketIterator = ticket.getLineas().getLineas().iterator();
        while (lineasTicketIterator.hasNext()){
            LineaTicket linea = lineasTicketIterator.next();
            if(linea.isPromoUnitariaAplicada()){
                linea.resetDescuento();
                linea.setPromocionLinea(null);
            }
        }
        
    }
    
    public void desvincularFacturaDiaSocio(){
        // quitamos la líneas de artículos restringidos sólo para día del socio
        Iterator<LineaTicket> lineasTicketIterator = ticket.getLineas().getLineas().iterator();
        while (lineasTicketIterator.hasNext()){
            LineaTicket linea = lineasTicketIterator.next();
            ArticuloEnPromocion articuloEnDiaSocio = ArticulosEnPromocion.getInstance().getDiaSocio(linea.getArticulo().getCodart());
            if (articuloEnDiaSocio != null && articuloEnDiaSocio.isRestringido()) {
                lineasTicketIterator.remove();
            }
            else{
                linea.resetDescuento();
                linea.setPromocionLinea(null);
            }
        }
        // desvinculamos factura origen
        getClientePromoAplicadas().remove(-1L);
        getClientePromoAplicadas().remove(Sesion.promocionDiaSocio.getIdPromocion());
        aplicaPromocionesLineas = true;
        diaSocioFacturaOrigen = null;
        diaSocioCantidadAplicable = null;
        promocionesFiltrosPagos.reset();
    }
    
    public void establecerFacturaDiaSocio(TicketId idTicket) throws TicketException, ValidationException{
        try {
            log.debug("establecerFacturaDiaSocio() - Estableciendo factura del día del socio");
            TicketsAlm ticketAlm = TicketsDao.consultarTicket(idTicket);
            if (ticketAlm.isAnulado()){
                log.debug("establecerFacturaDiaSocio() - La factura: "+ticketAlm.getIdentificador() + " está anulada");
                throw new ValidationException("La factura indicada se encuentra anulada.");
            }
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticketAlm.getTicket()));
            if (!ticket.getCliente().isSocio()){
                log.debug("establecerFacturaDiaSocio() - El cliente: "+ticket.getCliente().getCodcli() +" NO es socio");
                throw new ValidationException("Sólo los clientes socios pueden acceder a promoción Día del Socio.");
            }
            if (!Sesion.promocionDiaSocio.isAplicableACliente(ticket.getCliente())){
                throw new ValidationException("El tipo de afiliado exigido por la promoción no corresponde con el tipo de afiliado del cliente.");
            }
            // Probamos si el ticket ya fue utilizado como primera factura de una promoción día del socio
            if (ServicioPromocionesClientes.existeTicketDiaSocioReferenciado(ticketAlm.getUidTicket())){
                log.debug("establecerFacturaDiaSocio() - El ticket ya fue utilizado como primera factura de una promoción día del socio: "+ticketAlm.getUidTicket());
                throw new ValidationException("Cliente o crédito ya aplicó el producto del día del socio.");
            }
            // Probamos si el ticket es segunda factura de alguna promoción día del socio
            if (ServicioPromocionesClientes.existeTicketDiaSocioAplicado(ticketAlm.getUidTicket())){
                log.debug("establecerFacturaDiaSocio() - El ticket ya fue utilizado como segunda factura de alguna promoción día del socio: "+ticketAlm.getUidTicket());
                throw new ValidationException("Cliente o crédito ya aplicó el producto del día del socio.");
            }
            // esto no debería ser necesario, ya que el control siempre se debe hacer por crédito directo.
            if (!Sesion.promocionDiaSocio.isReaplicable(ticket)){
                log.debug("establecerFacturaDiaSocio() - El cliente ya uso la promoción día del socio.");
                throw new ValidationException("Cliente o crédito ya aplicó el producto del día del socio.");
            }
            diaSocioCantidadAplicable = Sesion.promocionDiaSocio.getCantidadAplicable(ticketOrigen.getTotales().getTotalPagado());
            if (diaSocioCantidadAplicable == null || diaSocioCantidadAplicable == 0){
                log.debug("establecerFacturaDiaSocio() - El importe de la factura no cumple con los rangos definidos para acceder a productos día del socio.");
                throw new ValidationException("El importe de la factura no cumple con los rangos definidos para acceder a productos día del socio.");
            }
            aplicaPromocionesLineas = false;
            diaSocioFacturaOrigen = ticketOrigen; 
            addClientePromoAplicada(-1L);
            addClientePromoAplicada(Sesion.promocionDiaSocio.getIdPromocion());
        }
        catch (PromocionException ex) {
            log.error("establecerFacturaDiaSocio() -  "  + ex.getMessage(), ex);
            throw new TicketException(ex.getMessage(), ex);
        }        
        catch (XMLDocumentException ex) {
            log.error("establecerFacturaDiaSocio() - Error parseando XML de la factura que se intenta asociar a Día del Socio: "  + ex.getMessage(), ex);
            throw new TicketException("Error parseando XML de la factura indicada." ,ex);
        }        
        catch (NoResultException ex) {
            throw new ValidationException("La factura indicada no existe en el sistema.", ex);
        }
    }

    public Integer getDiaSocioCantidadAplicable() {
        return diaSocioCantidadAplicable;
    }

    public boolean isMesesGraciaAplicado() {
        return mesesGraciaAplicado;
    }

    public void setMesesGraciaAplicado(boolean mesesGraciaAplicado) {
        this.mesesGraciaAplicado = mesesGraciaAplicado;
    }
    
    public List<LineaEnTicket> getPromocionPosfechadoVoucher(List<Pago> pagos){
        List<LineaEnTicket> lineasPosfechados = new ArrayList<LineaEnTicket>();
        for(Pago p:pagos){
            if(p.getMedioPagoActivo().isTarjetaSukasa()){
                if(!p.getMedioPagoActivo().isCreditoFilial() && !p.getMedioPagoActivo().isBonoSuperMaxiNavidad() && ((PagoCreditoSK)p).isPosfechado() && !((PagoCredito)p).isMesesGraciaAplicado()){
                    lineasPosfechados.add(new LineaEnTicket(((PagoCreditoSK)p).getTextoPosfechado()));
                }
            }
        }
        return lineasPosfechados;
    }
}
