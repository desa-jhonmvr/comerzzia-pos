/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasAbono;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasRecibo;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasReservacion;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.EnumTipoCliente;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author amos
 */
public class Reservacion {

    private static Logger log = Logger.getMLogger(ReservacionesServicios.class);
    
    private ReservaBean reservacion;
    private TicketS ticket;
    private ReservaInvitadoBean invitadoSeleccionado;
    private ReservaArticuloBean articuloSeleccionado;
    private BigDecimal abonosRestantes;
    private BigDecimal abonosRestantesReales;
    private BigDecimal abonosReales;  // Abonos reales sin descuentos
    private BigDecimal totalAbonado;  // Total abonado contando descuentos
    private BigDecimal total; // Improte total de la reserva
    private BigDecimal comprado; // Importe comprado en la reserva
    private BigDecimal porAbonar; // Importe por Abonar de la reserva 
    private BigDecimal compradoAbonos; // Importe de Artículos comprados con abono
    private BigDecimal ampliacionAbono; // Ampliación abono que hay que hacer después de añadir artículos a una reserva
    private Cliente invitadoActivo;
    private DatosAbono datosUltimoAbono = null;
    private BigDecimal ampliacionTotal;  // importe total de la reserva mas la amploación que estamos haciendo
    private FacturacionTicketBean facturacion;
    private String observaciones;
    
    public Reservacion() {
        datosUltimoAbono = new DatosAbono();
    }

    public Reservacion(ReservaBean r) {
        this();
        this.reservacion = r;
    }

    public ReservaBean getReservacion() {
        return reservacion;
    }

    public void setReservacion(ReservaBean reservacion) {
        this.reservacion = reservacion;
    }

    public TicketS getTicket() {
        return ticket;
}

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public void iniciaNuevaReservacion(TicketS creaTicket, ReservaTiposBean tipo) throws ContadorException {
        reservacion = new ReservaBean();
        reservacion.setCodTipo(tipo.getCodTipo());
        reservacion.setReservaTipo(tipo);
        // Asignamos a la nueva reserva un identificador de reserva
        reservacion.setCodReservacion(new BigDecimal(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_RESERVA)));
        ticket = creaTicket;
    }

    public void iniciaNuevaReservacion(TicketS creaTicket, ReservaTiposBean tipo, String nombreOrganizadora, String apellidosOrganizadora, String telefonoOrganizadora, String direccionEvento, Fecha fechaEvento) throws ContadorException {
        reservacion = new ReservaBean();
        reservacion.setCodTipo(tipo.getCodTipo());
        reservacion.setReservaTipo(tipo);
        reservacion.setApellidosOrganizadora(apellidosOrganizadora);
        reservacion.setNombreOrganizadora(nombreOrganizadora);
        reservacion.setTelefonoOrganizadora(telefonoOrganizadora);
        reservacion.setDireccionEvento(direccionEvento);
        reservacion.setFechaHoraEvento(fechaEvento);
        // Asignamos a la nueva reserva un identificador de reserva
        reservacion.setCodReservacion(new BigDecimal(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_RESERVA)));
        ticket = creaTicket;
    }

    public ReservaInvitadoBean getInvitadoSeleccionado() {
        return invitadoSeleccionado;
    }

    public BigDecimal getAbonosRestantes() {
        BigDecimal compradoAbonos = new BigDecimal(BigInteger.ZERO);
        BigDecimal totalAbonado = new BigDecimal(BigInteger.ZERO);

        List<ReservaArticuloBean> listaArticulosReserva = reservacion.getReservaArticuloList();
        for (ReservaArticuloBean art : listaArticulosReserva) {
            if (art.getCompradoConAbono()) {
                compradoAbonos = compradoAbonos.add(art.getPrecioTotal());
            }
        }
        for (ReservaAbonoBean abono : reservacion.getReservaAbonoList()) {
            totalAbonado = totalAbonado.add(abono.getCantidadAbono());
        }
        return totalAbonado.subtract(compradoAbonos);
    }

    public BigDecimal getAbonosPorAbonar() {
        BigDecimal comprado = new BigDecimal(BigInteger.ZERO);
        BigDecimal totalAbonado = new BigDecimal(BigInteger.ZERO);

        List<ReservaArticuloBean> listaArticulosReserva = reservacion.getReservaArticuloList();
        for (ReservaArticuloBean art : listaArticulosReserva) {
            comprado = comprado.add(art.getPrecioTotal());
        }
        for (ReservaAbonoBean abono : reservacion.getReservaAbonoList()) {
            totalAbonado = totalAbonado.add(abono.getCantidadAbono());
        }
        return comprado.subtract(totalAbonado);
    }

    public void setAbonosRestantes(BigDecimal abonosRestantes) {
        this.abonosRestantes = abonosRestantes;
    }

    public void setAbonosRestantesReales(BigDecimal abonosRestantesReales) {
        this.abonosRestantesReales = abonosRestantesReales;
    }

    public void setInvitadoSeleccionado(ReservaInvitadoBean invitadoSeleccionado) {
        this.invitadoSeleccionado = invitadoSeleccionado;
    }

    public void guardaReservacion() throws ReservasException {
        //    
        BigDecimal totalPagos = BigDecimal.ZERO;
        BigDecimal totalAbonoEntregado = BigDecimal.ZERO;
        BigDecimal totalAPagar = BigDecimal.ZERO;
        ReservaAbonoBean nuevoAbono = null;
        List<ReservaArticuloBean> listaresArt = new LinkedList<>();
        //List listaInv = new LinkedList();
        List<ReservaAbonoBean> listaAbonos = new LinkedList<>();

        // Preparo el objeto reservación
        reservacion.setUidReservacion(ticket.getUid_ticket());
        int plazoReserva;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //G.S. Si es un empleado Comohogar tiene 60 dias de plazo
        if(EnumTipoCliente.EMPLEADO_COMOHOGAR.ordinal()==ticket.getCliente().getTipoCliente()){
            plazoReserva = Constantes.PLAZO_RESERVACION_EMPLEADO_COMOHOGAR;
        }else{
            plazoReserva = reservacion.getReservaTipo().getPlazoReservacion().intValue();
        }
        
        //cal.add(Calendar.DATE, reservacion.getReservaTipo().getPlazoReservacion().intValue());
        cal.add(Calendar.DATE, plazoReserva);
        reservacion.setCaducidad(new Fecha(cal.getTime())); 
        reservacion.setCodcli(ticket.getCliente().getCodcli());
        reservacion.setCliente(ticket.getCliente());
        reservacion.setFechaAlta(new Fecha());
        reservacion.setLiquidado(false);
        reservacion.setCancelado(false);
        reservacion.setCodalm(ticket.getTienda());
        reservacion.setIdUsuario(new BigDecimal(ticket.getCajero().getIdUsuario()));
        reservacion.setCodcaja(ticket.getCodcaja());
        reservacion.setProcesadoTienda(false);
        if (ticket.getVendedor()!=null){
            reservacion.setCodvendedor(Sesion.getAutorizadorReservacion());
        }
        
        int i; // contador para generar idlineas e idpagos


        // idLinea del ticket esta vacío. le damos un idlínea generado al crearlo
        i = 0;
        for (LineaTicket lin : ticket.getLineas().getLineas()) {

            for (int j = 0; j < lin.getCantidad(); j++) {
                ReservaArticuloBean resArt = new ReservaArticuloBean(reservacion.getUidReservacion(), new Long(i));
                i++;
                resArt.setCompradoConAbono(false);
                resArt.setFechaCompra(new Fecha());
                resArt.setReserva(reservacion);
                resArt.setCantidad(new Long(1));
                resArt.setCodbarras(lin.getCodigoBarras());
                resArt.setComprado(false);
                resArt.setCodart(lin.getArticulo().getCodart());
                resArt.setDesart(lin.getArticulo().getDesart());
                resArt.setPrecio(lin.getPrecio());
                resArt.setPrecioTotal(lin.getPrecioTotal());
                resArt.setProcesadoTienda(false);

                listaresArt.add(resArt);
                totalAPagar = totalAPagar.add(resArt.getPrecioTotal());
            }

        }
        reservacion.setReservaArticuloList(listaresArt);
        reservacion.setReservaInvitadoList(new LinkedList<ReservaInvitadoBean>());
        
        BigDecimal efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();            
        try { 
            // Construyo el abono inicial si lo tiene
            if (ticket.getPagos() != null && ticket.getPagos().getPagos() != null) {
                Long idAbono = new Long(0); // Siempre tendrá el id=0 al ser el abono inicial
                nuevoAbono = new ReservaAbonoBean(reservacion.getUidReservacion(), idAbono);
                nuevoAbono.setReserva(reservacion);
                nuevoAbono.setCodcaja(ticket.getCodcaja());
                nuevoAbono.setFechaAbono(new Fecha());
                nuevoAbono.setCajero(Sesion.getUsuario().getUsuario());
                nuevoAbono.setProcesadoTienda(false);
                nuevoAbono.setAnulado(false);
                if (this.getTicket().getFacturacion() != null) {
                    reservacion.setDatosFacturacion(this.getTicket().getFacturacion());
                    reservacion.getDatosFacturacion().setUidReservacion(this.getReservacion().getUidReservacion());
                }
                for (Pago pag : ticket.getPagos().getPagos()) {
                    //Para que en el primer abono los totales sin iva se guarden igual que con iva ya que los abonos no tienen iva
                    pag.setUstedPagaSinIva(pag.getUstedPaga());
                    pag.setTotalSinIva(pag.getTotal());
                    totalPagos = totalPagos.add(pag.getTotal());
                    totalAbonoEntregado = totalAbonoEntregado.add(pag.getUstedPaga());
                }
                nuevoAbono.setCantidadAbono(totalPagos);
                listaAbonos.add(nuevoAbono);
                reservacion.setReservaAbonoList(listaAbonos);
                reservacion.setCuotaInicial(totalPagos);
                nuevoAbono.setCantAbonoSinDto(totalAbonoEntregado);
                nuevoAbono.setPagos(TicketXMLServices.getXMLPagos(ticket.getPagos()));  
            } 
            
            ReservacionesServicios.crearReserva(reservacion, ticket);
            
            Fecha f = new Fecha();

            // Datos de facturacion
            FacturacionTicketBean ft = null;
            if (ticket != null && ticket.getFacturacion() != null) {
                ft = ticket.getFacturacion();
            }
            else {
                ft = reservacion.getDatosFacturacion();
            }
            if(ft==null){
                if(ticket.getCliente().getCodcli().equals(Sesion.getClienteGenerico().getCodcli())){
                    ticket.setFacturacionClienteGenerico();
                } else {
                    ticket.setFacturacionCliente();
                }
                ft = ticket.getFacturacion();
            }
            CodigoBarrasReservacion codigoBarrasReservacion = new CodigoBarrasReservacion(reservacion.getCodReservacion().toBigInteger());
            ComprobanteReservacion comprobanteReservacion = new ComprobanteReservacion(Numero.redondear(totalAPagar).toString(), ft, f.getString("dd/MM/yyyy - HH:mm"), reservacion.getCodReservacion().toString(), codigoBarrasReservacion, ticket, reservacion);
            String tienda = Sesion.getTienda().getAlmacen().getDesalm();
            if (tienda.length() > 40) {
                tienda.substring(0, 40);
            }
            comprobanteReservacion.setTienda(tienda);
            
            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirComprobanteReservacion(comprobanteReservacion);
            
            // Insertamos el documento en BBDD con el ticket y la lista de documentos, que luego limpiamosos.
            DocumentosService.crearReservacion(comprobanteReservacion, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.RESERVACION);
            PrintServices.getInstance().limpiarListaDocumentos();


            if (reservacion.getCodTipo().equals("00")) {
                CodigoBarrasRecibo codigoBarrasAbono = new CodigoBarrasAbono(reservacion.getCodReservacion().toBigInteger(), nuevoAbono.getIdAbono());
                ComprobanteAbono comprobanteAbono = new ComprobanteAbono(this, ticket.getPagos(), Numero.redondear(totalPagos).toString(), ft, f.getString("dd/MM/yyyy - HH:mm"), codigoBarrasAbono);
                comprobanteAbono.setTotalAbonoEntregado(totalAbonoEntregado.toString());
                comprobanteAbono.setTotalConDescuentos(totalPagos.toString());
                comprobanteAbono.setNumAbono(nuevoAbono.getIdAbono().intValue());
    
                PrintServices.getInstance().limpiarListaDocumentos();
                PrintServices.getInstance().imprimirComprobanteAbono(comprobanteAbono,false, false);
                //Como también se imprimen los Vouchers, los borramos ya que no hace falta reimprimirlos
                /*Iterator it = PrintServices.getInstance().getDocumentosImpresos().iterator();
                while(it.hasNext()){
                    DocumentosImpresosBean docimpre = (DocumentosImpresosBean) it.next();
                    if(docimpre.getTipoImpreso().equals(DocumentosImpresosBean.TIPO_PAGO)){
                        it.remove();
                    }
                }*/             
                DocumentosService.crearAbonoReserva(reservacion.getCodalm(), comprobanteAbono, new Long(0), PrintServices.getInstance().getDocumentosImpresos(),DocumentosBean.ABONO_RESERVA, true);
                PrintServices.getInstance().limpiarListaDocumentos();
                BonosServices.crearBonosPagos(ticket.getPagos(), reservacion.getCodReservacion().toString(), "Abono Reserva Inicial", reservacion.getCliente());
            }                  
        }
        catch(ReservasException ex) {
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            log.error(ex.getMessage(), ex);
            throw new ReservasException("No se pudo guardar la reservación en la base de datos");            
        }
        catch (Exception ex) {
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            log.error(ex.getMessage(), ex);
            throw new ReservasException("No se pudo guardar la reservación en la base de datos");
        }
    }

    public List<ReservaArticuloBean> addArticulosReserva(TicketS ticket, ReservaBean reservacion, ReservaInvitadoBean invitado) throws ReservasException {

        List<ReservaArticuloBean> listaresArt = new LinkedList<ReservaArticuloBean>();
        BigDecimal totalAPagar = BigDecimal.ZERO;

        ampliacionAbono = BigDecimal.ZERO;

        try {
            Long posicion = ReservacionesServicios.consultaSiguienteIdLineaArticulo(reservacion.getUidReservacion());
            int i = posicion.intValue();

            for (LineaTicket lin : ticket.getLineas().getLineas()) {
                for (int j=0;j<lin.getCantidad();j++){
                    ReservaArticuloBean resArt = new ReservaArticuloBean(reservacion.getUidReservacion(), new Long(i));
                    i++;
                    resArt.setCompradoConAbono(false);
                    resArt.setFechaCompra(new Fecha());
                    resArt.setReserva(reservacion);
                    resArt.setCantidad(new Long(1));
                    resArt.setCodbarras(lin.getCodigoBarras());
                    resArt.setComprado(false);
                    resArt.setCodart(lin.getArticulo().getCodart());
                    resArt.setDesart(lin.getArticulo().getDesart());
                    resArt.setPrecio(lin.getPrecio());
                    resArt.setPrecioTotal(lin.getPrecioTotal());
                    resArt.setProcesadoTienda(false);

                    listaresArt.add(resArt);
                    totalAPagar = totalAPagar.add(resArt.getPrecioTotal().multiply(new BigDecimal(resArt.getCantidad())));
                }
            }

            if (reservacion.getReservaTipo().getAbonoInicial()) {
                ampliacionAbono = calculaAmpliacionAbono(totalAPagar);
            }
            else {
                ampliacionAbono = BigDecimal.ZERO;
            }
            if (ampliacionAbono.compareTo(BigDecimal.ZERO) > 0) {
                //
            }
            else {
                ejecutaAnadirArticulosReserva(listaresArt, ticket.getReferenciaSesionPDA());
            }
            // Si no es de tipo canastilla, aumento el stock reservado
            if (!reservacion.isTipoCanastillas()){
                try {
                        LogKardexBean logKardex = new LogKardexBean();
                        logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
                        logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ?ticket.getAutorizadorVenta().getUsuario():Sesion.getUsuario().getUsuario());
                        logKardex.setFactura(String.valueOf(reservacion.getCodReservacion()));
                        log.debug(" -- Aumentando Stock de Reserva ");
                        ServicioStock.aumentaStockReserva(ticket.getLineas().getLineas(), logKardex);
                        ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, ticket.getTienda(), false);
                }
                catch (StockException e) {
                    log.error("addArticulosPlan() - STOCK: No fué posible aumentar el stock reservado");
                }
            }
        }

        catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ReservasException("No se pudo guardar el abono en la base de datos");
        }

        return listaresArt;
    }

    public void ejecutaAnadirArticulosReserva(List<ReservaArticuloBean> listaresArt, SesionPdaBean sesionPda) throws ReservasException {
        try {
            ReservacionesServicios.addArticulos(reservacion,listaresArt,sesionPda);
            ReservacionesServicios.generarKardexReservaArticulos(reservacion,listaresArt, EnumTipoDocumento.RESERVA);
        }
        catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ReservasException("No se pudo guardar el abono en la base de datos");
        }
    }

    public void removeArticulosReserva(Reservacion reservacion, ReservaArticuloBean articulo, int numero) {
        List<ReservaArticuloBean> lrart = new LinkedList();

        for (ReservaArticuloBean ra : reservacion.getReservacion().getReservaArticuloList()) {
            if (ra.getCodart().equals(articulo.getCodart()) && !ra.getComprado()) {
                lrart.add(ra);
                numero--;
                if (numero <= 0) {
                    break;
                }
            }
        }
        ReservacionesServicios.eliminarReservaArticulos(reservacion.getReservacion(), lrart);
    }

    public void prepararFacturaInvitado(TicketS ticket, ReservaBean reservacion, FacturacionTicketBean facturacionEstablecida) throws ReservasException {
        // Datos de facturacion
        if (facturacionEstablecida != null && facturacionEstablecida.getNombre() != null && !facturacionEstablecida.getNombre().isEmpty()) {
            ticket.setFacturacion(facturacionEstablecida);
        }
        else if (ticket.getFacturacion() == null) {
            ticket.setFacturacion(reservacion.getDatosFacturacion());
        }
    }

    public void guardaNuevoPagoArticulos(TicketS ticket, ReservaBean reservacion, ReservaInvitadoBean invitado, Cliente invitadoActivo, FacturacionTicketBean facturacionEstablecida) throws ReservasException {
        // Artículos de la reserva que se van a guardar
        List<ReservaArticuloBean> listaResArtSel = new LinkedList<ReservaArticuloBean>();
        List<ReservaArticuloBean> listaresArt = new LinkedList<ReservaArticuloBean>();

        // Totales
        BigDecimal totalAPagar = BigDecimal.ZERO;
        BigDecimal totalPagos = BigDecimal.ZERO;
        BigDecimal totalEntregado = BigDecimal.ZERO;
        BigDecimal efectivoEnCajaAnt = null;


        // Actualización de artículos de la reserva según artículos comprados.
        log.debug("Actualizando Articulos de la reserva");
        List<ReservaArticuloBean> listaArticulosEnReserva = reservacion.getReservaArticuloList();
        for (LineaTicket lin : ticket.getLineas().getLineas()) {
            for (int j = 0; j < lin.getCantidad(); j++) {
                boolean enc = false;
                for (ReservaArticuloBean ra : listaArticulosEnReserva) {
                    if (ra.getCodbarras().equals(lin.getCodigoBarras()) && !ra.getComprado()) {
                        enc = true;
                        listaresArt.add(ra);
                        ra.setCompradoConAbono(false);
                        ra.setInvitadoPagador(invitado);
                        ra.setIdInvitado(invitado.getIdInvitado());
                        ra.setFechaCompra(new Fecha());
                        ra.setReserva(reservacion);
                        ra.setCantidad(new Long(1));
                        ra.setCodbarras(lin.getCodigoBarras());
                        ra.setComprado(true);
                        ra.setPrecio(lin.getPrecio());
                        ra.setPrecioTotal(lin.getPrecioTotal());
                        ra.setPrecioTotalConDto(lin.getImporteFinalPagado());
                        ra.setProcesado(false);
                        ra.setProcesadoTienda(false);
                        ra.setUidTicket(ticket.getUid_ticket());
                        listaResArtSel.add(ra);
                        listaArticulosEnReserva.remove(ra);
                        break;
                    }
                }
                if (!enc) {
                    log.error("Error en el pago de la Reserva. No se encuentra el artículo en la reserva correspondiente a la líne a de ticket con código de barras" + lin.getCodigoBarras());
                    throw new ReservasException("Error en los artículos de la reserva");
                }
            }
            totalAPagar = totalAPagar.add(lin.getPrecioTotal().multiply(new BigDecimal(lin.getCantidad())));
        }

        // Establece los pagos de ReservaPagos
        establecePagos(totalEntregado, totalPagos, ticket);

        // Establecemos los precios sin descuento de los artículos tras saber cómo hemos pagado
        establecePreciosSinDescuento(listaresArt, totalAPagar, totalEntregado);

        efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        SqlSession sql = new SqlSession();
        try {
            em.getTransaction().begin();
            Connection conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));     
            
            // Modificamos los artículos
            for(ReservaArticuloBean ra:listaresArt){
                ReservacionesServicios.actualizarArticuloWithSql(sql,ra);
            }

            //Refrescamos la Reserva
            reservacion.setProcesadoTienda(false);
            ReservacionesServicios.modificarOnlyReservaWithSql(sql,reservacion);

            // generamos factura y procesamos pagos      
            ticket.finalizarTicket(false);
            JPrincipal.crearVentanaPuntos(ticket);

            TicketService.escribirTicket(em, ticket, true);   

            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirFacturaPagoReservaArticulo(ticket, reservacion, invitado);
            DocumentosService.crearDocumento(ticket, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
            PrintServices.getInstance().limpiarListaDocumentos();

            em.getTransaction().commit();
            
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
            logKardex.setFactura(String.valueOf(reservacion.getCodReservacion()));
            logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ?ticket.getAutorizadorVenta().getUsuario():Sesion.getUsuario().getUsuario());
            log.debug("guardaNuevoPagoArticulos() - Aumentando Stock de venta.)"); 
            ServicioStock.aumentaStockVenta(ticket.getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_51, ticket.getTienda(),false);
                
            if (!reservacion.isTipoCanastillas()){
              log.debug("guardaNuevoPagoArticulos() - Disminuyendo Stock de reserva.)"); 
              ServicioStock.disminuyeStockReserva(ticket.getLineas().getLineas(), logKardex);
              ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, ticket.getTienda(),true);
            }
        }
        catch (TicketPrinterException ex) {
            log.error(ex.getMessage(), ex);
            ticket = null;
            throw new ReservasException("Se guardó la reserva, pero hubo un error imprimiendo el ticket.");

        }
        catch (TicketException ex) {
            log.error(ex.getMessage(), ex);
            ticket = null;
            em.getTransaction().rollback();
            throw new ReservasException("Se guardó la reserva, pero hubo un error salvando el ticket.");
        }
        catch (StockException ex) {
            log.error("accionPagoArticulos - STOCK: No fué posible aumentar el stock reservado");
        }
        catch (Exception ex) {
            em.getTransaction().rollback();
            ticket = null;
            log.error(ex.getMessage(), ex);
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            throw new ReservasException("No se pudo guardar el pago en la base de datos");
        }
        finally {
            em.close();
        }
    }

    public boolean isBabyShower() {
        return (getReservacion().getReservaTipo().getPermiteAbonosInvitados() && getReservacion().getReservaTipo().getPermiteCompra());
    }

    /**
     *  Guarda un Abono a una reservación
     * @param ticket
     * @param reservacion
     * @param invitado
     * @param articulo
     * @throws ReservasException 
     */
    public void guardaNuevoAbonoReservacion(TicketS ticket, ReservaBean reservacion, ReservaInvitadoBean invitado, ReservaArticuloBean articulo, boolean isLiquidarReservacion) throws ReservasException {

        //El ticket creado no tiene el codigo de caja. se establece
        ticket.setCodcaja(Sesion.getCajaActual().getCajaActual().getCodcaja());

        if (reservacion.getUidReservacion() == null) {
            this.ticket.setTotales(ticket.getTotales());
            this.ticket.setPagos(ticket.getPagos());
            guardaReservacion(); // Significa que no se ha guardado la reservación y utilizamos el antiguo método que la guardaba

            return;
        }

        // Inicializamos totales
        BigDecimal totalPagos = BigDecimal.ZERO;
        BigDecimal totalAbonoEntregado = BigDecimal.ZERO;
        BigDecimal efectivoEnCajaAnt = null;

        // Consultamos el siguiente Id del abono
        Long idAbono = ReservacionesServicios.consultaSiguienteIdAbono(reservacion.getUidReservacion(), reservacion.getCodalm().equals(Sesion.getTienda().getCodalm()));

        // Creamos el Abono
        ReservaAbonoBean nuevoAbono = new ReservaAbonoBean(reservacion.getUidReservacion(), idAbono);
        nuevoAbono.setReserva(reservacion);
        nuevoAbono.setReservaInvitado(invitado);
        if(invitado!=null){
            nuevoAbono.setIdInvitado(invitado.getIdInvitado());
        }
        nuevoAbono.setCodcaja(ticket.getCodcaja());
        nuevoAbono.setAnulado(false);
        Fecha f = new Fecha();
        nuevoAbono.setFechaAbono(f);
        nuevoAbono.setCajero(Sesion.getUsuario().getUsuario());  
        nuevoAbono.setProcesadoTienda(false);
        
        if (ticket.getPagos() != null && ticket.getPagos().getPagos() != null) {// Si hay pagos
            //i = 0;
            List<Pago> pagosRemove=new ArrayList<>();
            for (Pago pag : ticket.getPagos().getPagos()) {
                if (pag.getMedioPagoActivo().getDesMedioPago().equals("ABONO RESERVACIÓN")) {
                    pagosRemove.add(pag);
                    
                }
            }
            for(Pago pago: pagosRemove){
                ticket.getPagos().getPagos().remove(pago);
            }
            for (Pago pag : ticket.getPagos().getPagos()) {
                totalPagos = totalPagos.add(pag.getTotal());
                totalAbonoEntregado = totalAbonoEntregado.add(pag.getUstedPaga());
            }
            // Actualizamos el total del Abono
            nuevoAbono.setCantidadAbono(totalPagos);
            nuevoAbono.setCantAbonoSinDto(totalAbonoEntregado);
        }

        // Operación de guardado
        efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        //EntityManager a central por si el abono a realizar es a otro local
        EntityManagerFactory emcentral = null;
        EntityManager emc =  null;   
        boolean central = true;
        if (reservacion.getCodalm().isEmpty() || reservacion.getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            central = false;
        }         

        try {
            em.getTransaction().begin();
            if(central){
                emcentral = Sesion.getEmfc();
                emc = emcentral.createEntityManager();   
                emc.getTransaction().begin();
            } else {
                emc = em;
            }
            

            ticket.setUid_ticket(reservacion.getUidReservacion());

            // Guardamos los pagos
            nuevoAbono.setPagos(TicketXMLServices.getXMLPagos(ticket.getPagos()));
            
            //Si insertamos el abono en central, no puede ser el último abono de la reserva ya que luego liquidamos
            this.calculaTotales();
            if(central && (this.getPorAbonar().subtract(nuevoAbono.getCantidadAbono())).compareTo(BigDecimal.ZERO)<=0){
                throw new ReservasException("El último abono de la reserva se tiene que realizar en el local donde se realizó la reservación.");
            }            
            
            // Crear el abono
            ReservacionesServicios.crearAbono(reservacion, nuevoAbono, emc);
            
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaAbonoReservacion(reservacion, nuevoAbono);
            
            String documento = nuevoAbono.getReserva().getCodalm() + "-" + nuevoAbono.getCodcaja() + "-"+ String.format("%09d", nuevoAbono.getReserva().getCodReservacion().intValue());
            TicketService.procesarMediosPagos(em, ticket.getPagos().getPagos(), referencia, nuevoAbono.getIdAbono(),"RES", documento);

            //Guardamos los datos del último abono en Memoria
            datosUltimoAbono.setPagos(ticket.getPagos());
            datosUltimoAbono.setAbono(nuevoAbono);
            reservacion.getReservaAbonoList().add(nuevoAbono);
            
            // Datos de facturacion
            FacturacionTicketBean ft = null;

            if (ticket != null && ticket.getFacturacion() != null) {
                ft = ticket.getFacturacion();
            }
            else {
                ft = reservacion.getDatosFacturacion();
            }

            CodigoBarrasRecibo codigoBarras = new CodigoBarrasAbono(reservacion.getCodReservacion().toBigInteger(), nuevoAbono.getIdAbono());
            if (ft == null) {
                ft = new FacturacionTicketBean();
                ft.setTipoDocumento("");
                ft.setDocumento("");
                if (invitado != null && invitado.getNombre() != null) {
                    ft.setNombre(invitado.getNombre());
                    ft.setApellidos(invitado.getApellido());
                    ft.setTelefono(invitado.getTelefono());
                    ft.setDireccion("");
                }
                else if (reservacion.getCodcli() != null && reservacion.getCliente().getNombre() != null) {
                    ft = new FacturacionTicketBean(reservacion.getCliente());
                }
            }

            em.getTransaction().commit();
            if(central) {
                emc.getTransaction().commit();
            }
            ComprobanteAbono comprobante = new ComprobanteAbono(this, ticket.getPagos(), Numero.redondear(totalPagos).toString(), ft, f.getString("dd/MM/yyyy - HH:mm"), codigoBarras);
            comprobante.setTotalAbonoEntregado(totalAbonoEntregado.toString());
            comprobante.setTotalConDescuentos(totalPagos.toString());
            comprobante.setObservaciones(getObservaciones());
            comprobante.setNumAbono(nuevoAbono.getIdAbono().intValue());
            
            //Imprimimos los comprobantes abono y los almacenamos en BBDD
            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirComprobanteAbono(comprobante,false, isLiquidarReservacion);
            /*Iterator it = PrintServices.getInstance().getDocumentosImpresos().iterator();
            while(it.hasNext()){
                DocumentosImpresosBean docimpre = (DocumentosImpresosBean) it.next();
                if(docimpre.getTipoImpreso().equals(DocumentosImpresosBean.TIPO_PAGO)){
                    it.remove();
                }
            }*/        
            DocumentosService.crearAbonoReserva(reservacion.getCodalm(), comprobante, idAbono, PrintServices.getInstance().getDocumentosImpresos(),DocumentosBean.ABONO_RESERVA, true);
            
            PrintServices.getInstance().limpiarListaDocumentos();
            BonosServices.crearBonosPagos(ticket.getPagos(), reservacion.getCodReservacion().toString(), "Abono Reserva", reservacion.getCliente());
        }
        catch (TicketPrinterException ex) {
            ticket = null;
            reservacion = null;
            log.error(ex.getMessage(), ex);
            throw new ReservasException("Se guardó la reserva, pero hubo un error imprimiendo el ticket.");
        }
        catch (TicketException ex) {
            ticket = null;
            reservacion = null;
            log.error(ex.getMessage(), ex);
            throw new ReservasException("Se guardó la reserva, pero hubo un error imprimiendo el ticket.");
        }
        catch(ReservasException ex){
            ticket = null;
            reservacion = null;
            log.error(ex.getMessage());
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);     
            throw new ReservasException(ex.getMessage());
        }
        catch (Exception ex) {
            ticket = null;
            reservacion = null;
            log.error(ex.getMessage(), ex);
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            throw new ReservasException("No se pudo guardar el abono en la base de datos");
        }
        finally {
            em.close();
            try{
                if(emc != null){
                    emc.close();
                }
            } catch(Exception e){
               //Ignore
            }
        }
    }

    public void setArticuloSeleccionado(ReservaArticuloBean articulo) {
        this.articuloSeleccionado = articulo;
    }

    public ReservaArticuloBean getArticuloSeleccionado() {
        return this.articuloSeleccionado;
    }

    public void calculaAbonosReales() {
        BigDecimal res = BigDecimal.ZERO;

        for (ReservaAbonoBean ra : reservacion.getReservaAbonoList()) {
            res = res.add(ra.getCantAbonoSinDto());
        }
        this.setAbonosReales(res);
    }

    public BigDecimal getAbonosReales() {
        return abonosReales;
    }

    public void setAbonosReales(BigDecimal abonosReales) {
        this.abonosReales = abonosReales;
    }

    public Cliente getInvitadoActivo() {
        return invitadoActivo;
    }

    public void setInvitadoActivo(Cliente invitadoActivo) {
        this.invitadoActivo = invitadoActivo;
    }

    private void establecePreciosSinDescuento(List<ReservaArticuloBean> listaresArt, BigDecimal totalAPagar, BigDecimal totalAbonoEntregado) {
        for (ReservaArticuloBean ra : listaresArt) {
            BigDecimal porcentaje = null;
            if (ra.getPrecioTotal().compareTo(BigDecimal.ZERO) <= 0) {
                porcentaje = BigDecimal.ZERO;
            }
            else {
                porcentaje = totalAPagar.divide(ra.getPrecioTotal().multiply(new BigDecimal(ra.getCantidad())), RoundingMode.HALF_DOWN);
            }

            BigDecimal valorEntrEquiv = totalAbonoEntregado.multiply(porcentaje);
            ra.setPrecioTotalSinDto(valorEntrEquiv);
        }
    }

    private void establecePagos(BigDecimal totalEntregado, BigDecimal totalPagos, TicketS ticket) throws ReservasException {

        if (ticket.getPagos() != null && ticket.getPagos().getPagos() != null) {// Si hay pagos
            for (Pago pag : ticket.getPagos().getPagos()) {
                totalPagos = totalPagos.add(pag.getTotal());
                totalEntregado = totalEntregado.add(pag.getUstedPaga());
            }
        }
    }

    public void refrescar() throws ReservasException {
        try {
            this.reservacion = ReservacionesServicios.consultaById(reservacion.getCodReservacion().toBigInteger());
        }
        catch (Exception ex) {
            log.error("Error refrescando la reserva " + ex.getMessage(), ex);
            throw new ReservasException("Error refrescando la reserva");
        }
    }

    public BigDecimal getTotalReservacion() {
        BigDecimal total = BigDecimal.ZERO;
        for (ReservaArticuloBean art : getReservacion().getReservaArticuloList()) {
            total = total.add(art.getPrecioTotal());
        }
        return total;
    }

    public void autenticaPropietario(Cliente cliente) throws ClienteException {
        if (cliente != null) {
            if (cliente.equals(this.getReservacion().getCliente())) {
                this.setInvitadoActivo(cliente);
            }
            else {
                this.setInvitadoActivo(null);
                throw new ClienteException("El usuario no es el propietario de la reservación");
            }
        }
    }

    public boolean isCanastilla() {
        return (!getReservacion().getReservaTipo().getPermiteAbonosInvitados()  && getReservacion().getReservaTipo().getPermiteCompra());
    }

    public void crearInvitado(Cliente clienteConsultado) throws ReservasException {

        BigInteger idInvitado = new BigInteger(ReservacionesServicios.consultaSiguienteIdInvitado(this.reservacion.getUidReservacion()).toString());
        ReservaInvitadoBean invitado = new ReservaInvitadoBean(this.reservacion.getUidReservacion(), idInvitado.longValue());
        invitado.setNombre(clienteConsultado.getNombre());
        invitado.setApellido(clienteConsultado.getApellido());
        invitado.setEmail(clienteConsultado.getEmail());
        invitado.setTelefono(clienteConsultado.getTelefono1());
        invitado.setReserva(this.reservacion);
        invitado.setProcesadoTienda(false);

        this.setInvitadoSeleccionado(invitado);
        this.getReservacion().getReservaInvitadoList().add(invitado);
        ReservacionesServicios.crearInvitado(invitado);
    }

    public DatosAbono getDatosAbono() {
        return datosUltimoAbono;
    }

    public void setDatosAbono(DatosAbono datosAbono) {
        this.datosUltimoAbono = datosAbono;
    }

    public void calculaTotalAbonado() {
        setTotalAbonado(BigDecimal.ZERO);
        for (ReservaAbonoBean abono : reservacion.getReservaAbonoList()) {
            setTotalAbonado(getTotalAbonado().add(abono.getCantidadAbono()));
        }
    }

    public void calculaTotalYComprado() {
        setTotal(BigDecimal.ZERO);
        setComprado(BigDecimal.ZERO);
        compradoAbonos = BigDecimal.ZERO;
        for (ReservaArticuloBean art : reservacion.getReservaArticuloList()) {
            setTotal(getTotal().add(art.getPrecioTotal()));
            if (art.getComprado()) {
                setComprado(getComprado().add(art.getPrecioTotal()));
            }
            if (art.getCompradoConAbono()) {
                compradoAbonos = compradoAbonos.add(art.getPrecioTotal());
            }
        }

    }

    public void calculaTotales() {
        calculaAbonosReales();
        calculaTotalAbonado();
        calculaTotalYComprado();
        abonosRestantes = totalAbonado.subtract(compradoAbonos);
        abonosRestantesReales = abonosReales.subtract(compradoAbonos);
        porAbonar = (total.subtract(comprado)).subtract(abonosRestantes);


    }

    public BigDecimal getDescuentoEnReserva() {
        BigDecimal resultado = BigDecimal.ZERO;
        if (abonosReales != null && getTotalAbonado() != null) {
            BigDecimal porcentajeMenos = (abonosReales.multiply(Numero.CIEN)).divide(getTotalAbonado(), RoundingMode.HALF_DOWN).setScale(2, RoundingMode.HALF_DOWN);
            resultado = Numero.CIEN.subtract(porcentajeMenos);
        }
        return resultado;
    }

    public BigDecimal getTotalAbonado() {
        return totalAbonado;
    }

    public void setTotalAbonado(BigDecimal totalAbonado) {
        this.totalAbonado = totalAbonado;
    }

    public boolean isTodosArticulosComprados() {
        boolean res = false;
        if (this.isCanastilla()) {
            for (ReservaArticuloBean resart : reservacion.getReservaArticuloList()) {
                if (!resart.getComprado()) {
                    return false;
                }
            }
            res = true;
        }

        return res;
    }

    public BigDecimal getPorcentajeDescuentoAbonos() {
        return Numero.getTantoPorCientoMenosR(totalAbonado, abonosReales);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getComprado() {
        return comprado;
    }

    public void setComprado(BigDecimal comprado) {
        this.comprado = comprado;
    }

    public BigDecimal getCompradoAbonos() {
        return compradoAbonos;
    }

    public void setCompradoAbonos(BigDecimal compradoAbonos) {
        this.compradoAbonos = compradoAbonos;
    }

    private BigDecimal calculaAmpliacionAbono(BigDecimal importeAdicional) {
        BigDecimal nuevoImporte = BigDecimal.ZERO;
        nuevoImporte = nuevoImporte.add(total);
        nuevoImporte = nuevoImporte.add(importeAdicional);
        this.setAmpliacionTotal(nuevoImporte);
        BigDecimal porcentajeAbonoReserva = getPorcentajeMinimoReserva();


        BigDecimal importeDebeEstarAbonado = Numero.porcentaje(nuevoImporte, porcentajeAbonoReserva);

        return importeDebeEstarAbonado.subtract(totalAbonado).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmpliacionAbono() {
        return ampliacionAbono;
    }

    public void setAmpliacionAbono(BigDecimal ampliacionAbono) {
        this.ampliacionAbono = ampliacionAbono;
    }

    public BigDecimal getPorAbonar() {
        return porAbonar;
    }

    public void setPorAbonar(BigDecimal porAbonar) {
        this.porAbonar = porAbonar;
    }

    private BigDecimal getPorcentajeMinimoReserva() {
        BigDecimal porcentajeMinimo = BigDecimal.ZERO;
        if (reservacion.getCliente().isSocio()) {
            TipoAfiliadoBean tipoAfiliado = Sesion.tiposAfiliados.get(reservacion.getCliente().getTipoAfiliado());
            if (tipoAfiliado != null && tipoAfiliado.getPorcentajeAbonoInicial() != null) {
                porcentajeMinimo = new BigDecimal(tipoAfiliado.getPorcentajeAbonoInicial());
            }
            else {
                if (reservacion.getReservaTipo().getPorcentajeAbonoInicial() != null) {
                    porcentajeMinimo = reservacion.getReservaTipo().getPorcentajeAbonoInicial();
                }
            }
        }
        else {
            if (reservacion.getReservaTipo().getPorcentajeAbonoInicial() != null) {
                porcentajeMinimo = reservacion.getReservaTipo().getPorcentajeAbonoInicial();
            }
        }
        return porcentajeMinimo;
    }

    public BigDecimal getAmpliacionTotal() {
        return ampliacionTotal;
    }

    public void setAmpliacionTotal(BigDecimal ampliacionTotal) {
        this.ampliacionTotal = ampliacionTotal;
    }

    public void setFacturacion(FacturacionTicketBean facturacion) {
        this.facturacion = facturacion;
    }

    public FacturacionTicketBean getFacturacion() {
        return (this.facturacion);
    }

    public BigDecimal getAbonosRestantesReales() {
        return abonosRestantesReales;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
     
}
