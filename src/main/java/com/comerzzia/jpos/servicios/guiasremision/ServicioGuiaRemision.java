/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.guiasremision;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.GuiaRemision;
import com.comerzzia.jpos.entity.db.GuiaRemisionDetalle;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.entity.ticket.IDTicketMalFormadoException;
import com.comerzzia.jpos.entity.ticket.IdentificadorTicket;
import com.comerzzia.jpos.persistencia.guiasremision.GuiaRemisionDao;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionErradaBean;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.transaccioneserradas.ServicioTransaccionesErradas;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ServicioGuiaRemision {

    private static Logger log = Logger.getMLogger(ServicioGuiaRemision.class);

    static void consultaDatosTicket(GuiaRemision gr) throws NoResultException, TicketException, IDTicketMalFormadoException {
        TicketsAlm ticket = null;
        try {
            List<LineaTicketOrigen> lineasOrigen = null;
            List<LineaTicket> lineasSalida = new ArrayList<LineaTicket>();
            
            // Parseamos el identificador del ticket. Si es incorrecto recibiremos una excepción
            IdentificadorTicket idT = new IdentificadorTicket(gr.getNumDocumento());
            // Consultamos el ticket
            ticket = TicketService.consultarTicket(idT.getIdDocumento(), idT.getCodcaja(), idT.getCodalm());
            if (ticket.isAnulado()){
                throw new NoResultException("La factura indicada se encuentra anulada. ");
            }
                
            // Consultamos sus líneas
            lineasOrigen = TicketService.consultarLineasTicket(ticket.getUidTicket());
            
            // Creamos las lineas que vamos a mostrar
            for (LineaTicketOrigen lto: lineasOrigen){
                if (lto.isEnvioDomicilioPendiente()){
                    //Se agrega el campo costo landed RD
                    LineaTicket linSal = new LineaTicket(
                            "Lin"+lto.getLineaTicketOrigenPK().getIdLinea(), 
                            lto.getCodart(),
                            lto.getCantidad(),
                            lto.getPrecioOrigen(),
                            lto.getPrecioTotalOrigen(),new BigDecimal(BigInteger.ZERO));
                    
                    DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
                    datosAdicionales.setEnvioDomicilio(true);
                    datosAdicionales.setDescuento(BigDecimal.ZERO);
                    linSal.setDatosAdicionales(datosAdicionales);
                    
                    if (lto.getCodart().getCodmarca()!=null){
                        lto.setCodMarca(lto.getCodart().getCodmarca().getCodmarca());
                    }
                    
                    // Insertamos la linea original.
                    linSal.setLineaTicketOrigen(lto);
                    lineasSalida.add(linSal);
                }            
            }
            if (lineasSalida.isEmpty()){
                throw new NoResultException("El ticket no tiene líneas pendientes de envío" +((ticket !=null)? ticket.getUidTicket():" -sin uid-"));
            }
            gr.setLineas(lineasSalida);
            
            // Parseamos el ticket para obtener los datos de facturación
            
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticket.getTicket()));
            Cliente cliente= ClientesServices.getInstance().consultaClienteDoc(ticketOrigen.getCliente().getIdentificacion(), ticketOrigen.getCliente().getTipoIdentificacion());
            if (cliente !=null){
                gr.setDestNombre(cliente.getNombre() + " " + cliente.getApellido());
                gr.setDestCedula(cliente.getIdentificacion());
                gr.setDestCodalm(ticketOrigen.getTienda());
                gr.setDestDireccion(cliente.getDireccion());                
            }
        }
        catch (ClienteException ex) {
            log.error("No se encontró el cliente del ticket "+ ((ticket!=null) ? ticket.getUidTicket(): "-- indef--" ));
        }        catch (XMLDocumentException ex) {
            log.error("No fué posible obtener información de fecturación del ticket "+ ((ticket!=null) ? ticket.getUidTicket() : "-- indef --")) ;
        }
    }

    static void guardar(GuiaRemision gr) throws GuiaRemisionException {
        // Creamos un Entity Manager
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        
        // Generamos un uid para el bean y establecemos la fecha en que se guarda la guía
        UUID uid = UUID.randomUUID();
        gr.setUidGuiaRemision(""+uid);
        gr.setFecha(new Date());
        gr.setEstado('P'); // Pendiente de impresión
        gr.setIdUsuario(Sesion.getUsuario().getIdUsuario());
        
        // Generamos el detalle en función de las líneas seleccionadas
        gr.setGuiaRemisionDetalleList(new ArrayList<GuiaRemisionDetalle>());
        short idLinea = 1;
        for (LineaTicket lin : gr.getLineas()) {
            if (lin.isEnvioEnGuiaRemision()) {
                GuiaRemisionDetalle grd = new GuiaRemisionDetalle(gr.getUidGuiaRemision(), idLinea);
                grd.setCodart(lin.getArticulo().getCodart());
                grd.setCantidad(lin.getCantidad());
                grd.setCodMarca(lin.getLineaTicketOrigen().getCodMarca());
                grd.setDescripcion(lin.getArticulo().getDesart());
                grd.setCodItem(lin.getArticulo().getIdItem());
                grd.setModelo(lin.getArticulo().getModelo());
                grd.setIdLineaOrigen(new Integer(lin.getLineaTicketOrigen().getLineaTicketOrigenPK().getIdLinea()));
                grd.setLineaTicketOrigen(lin.getLineaTicketOrigen());
                gr.getGuiaRemisionDetalleList().add(grd);
                
                
                idLinea++;
                gr.setUidTicketRef(lin.getLineaTicketOrigen().getLineaTicketOrigenPK().getUidTicket());
            }
        }
        if (gr.getGuiaRemisionDetalleList().isEmpty() && gr.isTipoFactura()){
            throw new GuiaRemisionException("No puede crear una Guía de Remisión sin incluir ningún artículo.");
        }
        
        // Guardamos los datos        
        try {
            em.getTransaction().begin();
            GuiaRemisionDao.guardar(em, gr);
            for(GuiaRemisionDetalle grd : gr.getGuiaRemisionDetalleList()){
                GuiaRemisionDao.guardar(em, grd);
                TicketService.marcarLineaOrigenEnviado(em,grd.getLineaTicketOrigen());
            }
            
            ServicioContadoresCaja.incrementarContadorGuiaRemision(Connection.getConnection(em));
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("guardar(): Error" + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(gr.getIdGuiaRemision(), TransaccionErradaBean.TIPO_GUARDANDO , TransaccionErradaBean.TIPO_TRANSACCION_GUIA_REMISION);
            throw new GuiaRemisionException("Error guardando guía de remision", e);
        }
        finally {
            em.close();
        }
    }

}
