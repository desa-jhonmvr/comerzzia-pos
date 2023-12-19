/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones.plannovio;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.servicios.stock.StockTimeOutException;
import com.comerzzia.jpos.servicios.tickets.ImporteInvalidoException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.plannovios.PlanNovioDao;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.general.vendedores.VendedoresServices;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosDeEnvio;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *  Clase que contiene el plan novio y las operaciones que con el se realizan
 * @author MGRI
 */
public class PlanNovioOBJ {

    // Un plan novio puede estar siendo usado mientras se realizan las siguientes operaciones
    public static int MODO_INICIO = 0;
    public static int MODO_MOSTRANDO_PLAN = 1;
    public static int MODO_MODIFICANDO_PLAN = 2;
    public static int MODO_CREANDO_PLAN_NOVIA = 3;
    public static int MODO_CREANDO_PLAN_NOVIO = 4;
    public static int MODO_CREANDO_PLAN_DATOS_ADICIONALES = 5;
    public static int MODO_ADD_ARTICULOS = 6;
    public static int MODO_COMPRAR_ARTICULO = 7;
    public static int MODO_REALIZAR_ABONO = 8;
    public static int MODO_LIQUIDAR = 9;
    public static int MODO_MOSTRANDO_PLAN_RECIEN_CREADO = 11;
    private int modo = 0;
    // Log
    private static Logger log = Logger.getMLogger(PlanNovioOBJ.class);
    // Bean Plan novio
    private PlanNovio plan;
    // Constructores
    // Invitado seleccionado Plan
    // Cliente logueado para operación
    private Cliente clienteLogueado;             // Cliente identificado para la gestión de la reservación
    private FacturacionTicket datosFacturacion;  // Datos de facturación
    private InvitadoPlanNovio invitado;       // Ivitado que va a realizar la operación
    private Cliente clienteSeleccionado;      // Cliente que va a realizar la operación
    private Vendedor vendedorSeleccionado;      // Cliente que va a realizar la operación
    public BigInteger getCodPlanAsNumber;
    // Para Liquidación
    private List<ArticuloPlanNovio> articulosNoComprados;
    private List<ArticuloPlanNovio> articulosSeleccionados;
    private BigDecimal minimoImportePlan;
    private String observaciones;
    
    // Constructores

    public PlanNovioOBJ() {
    }

    /* Función que inicializa el plan */
    public void inicializa() {
        plan = new PlanNovio();

        plan.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
        plan.setIdUsuario(Sesion.getUsuario().getUsuario());
        
        //guardar el codigo de vendedor en el plan novio
        plan.setCodVendedor(Sesion.getUsuario().getUsuario());

        plan.setReservado(BigDecimal.ZERO);
        plan.setAbonadoSinDto(BigDecimal.ZERO);
        plan.setAbonadoConDto(BigDecimal.ZERO);
        plan.setAbonadoUtilizado(BigDecimal.ZERO);
        plan.setComprado(BigDecimal.ZERO);

        plan.setProcesado('N');
        plan.setProcesadoTienda('N');
        plan.setSolicitudLiquidacion('N');
        plan.setLiquidado('N');

        plan.setFechaAlta(new Date());

        Fecha fcaducidad = new Fecha();
        fcaducidad.sumaDias(Variables.getVariableAsInt(Variables.PLANNOVIOS_DIAS_VIGENCIA));
        plan.setCaducidad(fcaducidad.getDate());

        minimoImportePlan = Variables.getVariableAsBigDecimal(Variables.PLANNOVIOS_MINIMO_CONSUMO_PLAN);

    }

    // funciones que identifican al cliente logueado con respecto a los datos almacenados del plan
    public boolean isClienteLogueadoNovia() {
        return false;
    }

    public boolean isClienteLogueadoNovio() {
        return false;
    }

    public boolean isClienteLogueadoReservante() {
        return false;
    }

    // funciones que piden los totales de abonos y restante a abonar para llegar al minimo
    public List<PlanNovio> buscarPlanNovio(ParametrosBuscarPlanNovio param) throws PlanNovioException {
        List<PlanNovio> res = null;

        res = PlanNovioServices.buscar(param);

        return res;
    }

    // Getters y setters
    public Cliente getClienteLogueado() {
        return clienteLogueado;
    }

    public void setClienteLogueado(Cliente clienteLogueado) {
        this.clienteLogueado = clienteLogueado;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public PlanNovio getPlan() {
        return plan;
    }

    public void setPlan(PlanNovio plan) {
        this.plan = plan;
    }

    public void estableceNovia(Cliente clienteConsultado) {
        plan.setNovia(clienteConsultado);
    }

    public void estableceNovio(Cliente clienteConsultado) {
        plan.setNovio(clienteConsultado);
    }

    void crear() throws PlanNovioException {
        try {
            PlanNovioServices.crear(plan);
            modo = MODO_MOSTRANDO_PLAN_RECIEN_CREADO;
            
            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirContratoPlan(this);
            DocumentosService.crearDocumentoPlanNovio(this, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.PLAN_NOVIO);
            PrintServices.getInstance().limpiarListaDocumentos();
        }
        catch (DocumentoException ex) {
            log.error("crear() - No se pudo almacenar el contrato en los documentos",ex);
            throw new PlanNovioException("No se pudo imprimir el contrato");
        }
        catch (TicketPrinterException ex) {
            log.error("crear() - No se pudo imprimir el contrato",ex);
            throw new PlanNovioException("No se pudo imprimir el contrato");
        }
        catch (TicketException ex) {
            log.error("crear() - No se pudo imprimir el contrato",ex);
            throw new PlanNovioException("No se pudo imprimir el contrato");
        }
        catch (Exception ex) {
            log.error("crear() - No se pudo imprimir el contrato",ex);
            throw new PlanNovioException("No se pudo imprimir el contrato");
        }
    }
  

    void establecePlanBuscado(PlanNovio consultado) {
        this.plan = consultado;
        this.modo = MODO_MOSTRANDO_PLAN;
    }

    public void addArticulosPlan(TicketS ticket) {

        List<ArticuloPlanNovio> listaArt = new LinkedList<ArticuloPlanNovio>();
        BigDecimal totalAPagar = BigDecimal.ZERO;

        try {
            Long posicion = Long.valueOf(PlanNovioDao.consultaSiguienteIdLineaArticulos(plan.getPlanNovioPK().getIdPlan()));
            int i = posicion.intValue();

            for (LineaTicket lin : ticket.getLineas().getLineas()) {

                for (int j = 0; j < lin.getCantidad(); j++) {
                    ArticuloPlanNovio art = new ArticuloPlanNovio(plan.getPlanNovioPK().getIdPlan(), i, plan.getPlanNovioPK().getCodalm());
                    i++;
                    art.setFechaCompra(new Date());
                    art.setCodBarras(lin.getCodigoBarras());
                    art.setComprado(false);
                    art.setCodArt(lin.getArticulo().getCodart());
                    art.setDesArt(lin.getArticulo().getDesart());
                    art.setPrecioTarifaOrigen(lin.getPrecio());
                    art.setPrecioTotalTarifaOrigen(lin.getPrecioTotal());
                    art.setPrecio(lin.getPrecio());
                    art.setPrecioTotal(lin.getPrecioTotal());
                    art.setEntregado(false);
                    art.setDevuelto(false);
                    art.setBorrado(false);
                    art.setTotalPagadoConDscto(null);
                    art.setProcesado(false);

                    listaArt.add(art);
                    totalAPagar = totalAPagar.add(art.getPrecioTotal());
                }
            }
            PlanNovioServices.addArticulosPlan(listaArt, plan, totalAPagar, ticket.getReferenciaSesionPDA());
            try {
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionPlanNovio);
                logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ?ticket.getAutorizadorVenta().getUsuario():Sesion.getUsuario().getUsuario());
                logKardex.setFactura(String.valueOf(ticket.getId_ticket()));
                log.debug(" -- Aumentando Stock de Reserva ");
                ServicioStock.aumentaStockReserva(ticket.getLineas().getLineas(), logKardex);
                ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, ticket.getTienda(), false);
            }
            catch (StockException e) {
                log.error("addArticulosPlan() - STOCK: No fué posible aumentar el stock reservado");

            }

            PrintServices.getInstance().imprimirListaArticulosPlan(this, listaArt);

        }
        catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            //throw new PlanNovioException("No se pudo guardar el abono en la base de datos");
        }
    }

    BigDecimal getTotalPorAbonar() {
        //Calcular el valor que puede utilizar de ese local
        BigDecimal totalAbonoLocal = BigDecimal.ZERO;
        for(AbonoPlanNovio abonoPlanNovio : plan.getAbonoPlanNovioList()){
            if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getCodalm()) &&
                    (abonoPlanNovio.getEstadoLiquidacion() == null || !abonoPlanNovio.getEstadoLiquidacion().equals("L"))){
                totalAbonoLocal = totalAbonoLocal.add(abonoPlanNovio.getCantidadConDcto());
            }
        }
//        BigDecimal minimoReserva = Variables.getVariableAsBigDecimal(Variables.PLANNOVIOS_MINIMO_CONSUMO_PLAN);
       // BigDecimal porAbonar = totalAbonoLocal.subtract(plan.getComprado());
        return totalAbonoLocal;
    }

    void removeArticulo(ArticuloPlanNovio articuloReservado) throws PlanNovioException {
        PlanNovioServices.removeArticulo(articuloReservado, plan);
    }

    void consultarDetalle() throws PlanNovioException {
        plan = PlanNovioServices.consultarDetalle(plan);
        plan.refrescaTotales();
    }
    
    void consultarAbonoSinAnular() throws PlanNovioException {
        plan = PlanNovioServices.consultarAbonosSinAnular(plan);
        plan.refrescaTotales();
    }

    public List<InvitadoPlanNovio> getListaInvitados() {
        if (plan.getListaInvitados() != null) {
            return plan.getListaInvitados();
        }
        else {
            return new LinkedList<InvitadoPlanNovio>();
        }
    }

    public void accionPagoArticulos(TicketS ticket) throws PlanNovioException {
        PlanNovioServices.crearPagoArticulos(ticket, this, this.invitado, this.vendedorSeleccionado);
        try {
            // Cambio de Stock
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionPlanNovio);
            logKardex.setFactura(String.valueOf(ticket.getId_ticket()));
            logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ?ticket.getAutorizadorVenta().getUsuario():Sesion.getUsuario().getUsuario());
            log.debug(" -- Disminuyendo Stock de Reserva ");
            ServicioStock.disminuyeStockReserva(ticket.getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, ticket.getTienda(),true);
            log.debug(" -- Aumentando Stock de Venta ");
            ServicioStock.aumentaStockVenta(ticket.getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_51, ticket.getTienda(),false);
        }
        catch (StockException ex) {
            log.error("accionPagoArticulos() - STOCK: No fué posible aumentar el stock reservado");
        }
        catch (StockTimeOutException ex) {
            log.error("accionPagoArticulos() - STOCK: No fué posible aumentar el stock reservado porque la tabla está bloqueado");
        }
    }

    public FacturacionTicket getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(FacturacionTicket datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }
    
    public FacturacionTicketBean getDatosFacturacionTicketBean() {
        return new FacturacionTicketBean(datosFacturacion);      
    }
    
    public void setDatosFacturacionTicketBean(FacturacionTicketBean datosFacturacion) {
        this.datosFacturacion = new FacturacionTicket(datosFacturacion);
    }

    public InvitadoPlanNovio getInvitado() {
        return invitado;
    }

    public void setInvitado(InvitadoPlanNovio invitado) {
        this.invitado = invitado;
    }

    BigDecimal getAbonoMinimo() {
        BigDecimal abonoMinimo = null;
        //Aquí podemos establecer el abono minimo para un abono del Plan.        
        if (abonoMinimo == null) {
            abonoMinimo = BigDecimal.ZERO;
        }
        return abonoMinimo;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    void setInvitadoSeleccionado(int selectedRow) {
        if (selectedRow >= 0) {
            this.invitado = plan.getListaInvitados().get(selectedRow);
        }
    }

    public InvitadoPlanNovio getInvitado(int selectedRow) {
        InvitadoPlanNovio res = null;
        if (selectedRow >= 0) {
            res = plan.getListaInvitados().get(selectedRow);
        }
        return res;
    }

    public void accionPagoAbono(TicketS ticket) throws PlanNovioException {
        PlanNovioServices.crearAbono(this, ticket);
    }

    public String getCodPlanAsString() {
        return plan.getCodPlanAsString();
    }

    public BigInteger getCodPlanAsNumber() {
        return plan.getCodPlanAsNumber();
    }

    boolean isModoEditarDatosGenerales() {
        return (this.modo == MODO_MODIFICANDO_PLAN);
    }

    void modificarDatosGenerales() throws PlanNovioException {
        PlanNovioServices.modificarDatosGenerales(plan);
        this.modo = MODO_MOSTRANDO_PLAN;
    }

    List<AbonoPlanNovio> getListaAbonos() {
        if (plan.getListaAbonos() != null) {
            return plan.getListaAbonos();
        }
        else {
            return new LinkedList<AbonoPlanNovio>();
        }
    }

    public void refrescaTotales() {
        plan.refrescaTotales();
    }

    void accionRematarReservacion(String autorizador, BigDecimal valorRematar, Boolean esLocalOrigen) throws PlanNovioException, TicketPrinterException, TicketException, DocumentoException {

        // Modificar la reserva
        PlanNovioServices.liquidarPlan(plan, autorizador, valorRematar, esLocalOrigen);

        log.debug("Iniciando impresión del ticket y guardandolo en BBDD");
        
        PrintServices.getInstance().limpiarListaDocumentos();
        PrintServices.getInstance().imprimirListaArticulosPlan(this, null);
        DocumentosService.crearDocumentoLiquidaPN(plan,PrintServices.getInstance().getDocumentosImpresos(),DocumentosBean.LIQUIDACION_PN, observaciones);
        PrintServices.getInstance().limpiarListaDocumentos();

        // Log de liquidación
        //ServicioLogAcceso.crearAccesoLogLiquidarReserva(compruebaAutorizacion, reservacion.getReservacion());


    }

    void accionComprarConAbonos(List<ArticuloPlanNovio> lista, DatosDeEnvio datosEnvio) throws PlanNovioException, TicketPrinterException, TicketException {
        log.debug("accionComprarConAbonos() - Realizando compra de artículos con abonos en plan novios...");
        
        // Crear el ticket
        TicketS ticket = creaTicketDesdeLineas(lista);
        ticket.setDatosEnvio(datosEnvio);

        // Modificar la reserva
        PlanNovioServices.comprarConAbonos(plan, this, ticket, lista);

        // Modificamos los Stocks
        try {
            // Articulos Compardos
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionPlanNovio);
            logKardex.setFactura(String.valueOf(ticket.getId_ticket()));
            logKardex.setUsuarioAutorizacion(ticket.getAutorizadorVenta() != null ?ticket.getAutorizadorVenta().getUsuario():Sesion.getUsuario().getUsuario());
            log.debug(" -- Disminuyendo Stock de Reserva ");
            ServicioStock.disminuyeStockReserva(ticket.getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_52, ticket.getTienda(),true);
            log.debug(" -- Aumentando Stock de venta ");
            ServicioStock.aumentaStockVenta(ticket.getLineas().getLineas(), logKardex);
            ServicioStock.actualizaListaArticulosKardexVentas(ticket.getLineas().getLineas(), ServicioStock.MOVIMIENTO_51, ticket.getTienda(),false);
        }
        catch (Exception e) {
            log.error("liquidarPlan() - STOCK: No fué posible aumentar el stock reservado");
        }
    }

    public void refrescaArticulosNoComprados() {
        setArticulosNoComprados(new LinkedList<ArticuloPlanNovio>());
        for (ArticuloPlanNovio articulo : plan.getListaArticulos()) {
            if (!articulo.isCompradoOReservado()) {
                getArticulosNoComprados().add(articulo);
            }
        }
    }
    
    public List<ArticuloPlanNovio> getListaArticulos() {
        return plan.getListaArticulos();
    }
    
    public void setListaArticulos(List<ArticuloPlanNovio> listaArticulos) {
        this.plan.setListaArticulos(listaArticulos);
    }

    public boolean isPlanConArticulosNoComprados() {
        return (!articulosNoComprados.isEmpty());
    }

    public List<ArticuloPlanNovio> getArticulosNoComprados() {
        return articulosNoComprados;
    }

    public void setArticulosNoComprados(List<ArticuloPlanNovio> articulosNoComprados) {
        this.articulosNoComprados = articulosNoComprados;
    }

    public List<ArticuloPlanNovio> getArticulosSeleccionados() {
        return articulosSeleccionados;
    }

    public void setArticulosSeleccionados(List<ArticuloPlanNovio> articulosSeleccionados) {
        this.articulosSeleccionados = articulosSeleccionados;
    }

    public void setVendedorSeleccionado(Vendedor vendedor) {
        this.vendedorSeleccionado = vendedor;
    }

    public Vendedor getVendedorSeleccionado() {
        return this.vendedorSeleccionado;
    }

    boolean isMinimoAlcanzado() {
        minimoImportePlan = Variables.getVariableAsBigDecimal(Variables.PLANNOVIOS_MINIMO_CONSUMO_PLAN);
        return (minimoImportePlan.compareTo(plan.getImporteAlcanzado()) <= 0);
    }

    private TicketS creaTicketDesdeLineas(List<ArticuloPlanNovio> lista) throws PlanNovioException {

        ArticulosServices aS = ArticulosServices.getInstance();
        Articulos a = null;
        TicketS ticket = new TicketS();

        try {
            ticket.iniciaDatosBaseTicket();
            ticket.iniciaUID();

            ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
            ticket.setCliente(this.clienteLogueado);

            Vendedor vendedorPlan = null;
            if (this.plan.getCodVendedor() != null && !this.plan.getCodVendedor().isEmpty()) {
                VendedoresServices vS = new VendedoresServices();
                vendedorPlan = vS.consultarVendedor(this.plan.getCodVendedor());
            }
            ticket.setVendedor(vendedorPlan);


            // creamos una línea por artículo
            LineasTicket lineas = new LineasTicket();
            BigDecimal totalAPagar = BigDecimal.ZERO;

            Integer cantidad = 0;
            String ultimaBarra = "";
            ArticuloPlanNovio ultimoArticulo = null;
            for (ArticuloPlanNovio art : lista) {
                if(ultimaBarra.equals("")){
                    ultimaBarra = art.getCodBarras();
                    ultimoArticulo = art;
                }
                if(ultimaBarra.equals(art.getCodBarras())){
                    cantidad = cantidad + 1;
                }else{
                    a = aS.getArticuloCB(ultimaBarra);
                    //Se agrega el campo costo landed RD 
                    LineaTicket linea = new LineaTicket(
                            ultimaBarra, 
                            a, 
                            cantidad,
                            ultimoArticulo.getPrecioTarifaOrigen(),
                            ultimoArticulo.getPrecioTotalTarifaOrigen(),new BigDecimal(BigInteger.ZERO));

                    DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
                    datosAdicionales.setEnvioDomicilio(ultimoArticulo.isPendienteEnvio());
                    datosAdicionales.setRecogidaPosterior(ultimoArticulo.isPendienteRecoger());
                    BigDecimal descuentoLinea = ultimoArticulo.getDescuento();
                    if (descuentoLinea == null){
                        descuentoLinea = BigDecimal.ZERO;
                    }
                    datosAdicionales.setDescuento(descuentoLinea);
                    linea.setDatosAdicionales(datosAdicionales);
                    linea.establecerDescuento(datosAdicionales.getDescuento());
                    lineas.getLineas().add(linea);
                    totalAPagar = totalAPagar.add(linea.getPrecioTotal().multiply(BigDecimal.valueOf(cantidad)));
                    cantidad = 1;
                    ultimaBarra = art.getCodBarras();
                    ultimoArticulo = art;
                }
            }
             a = aS.getArticuloCB(ultimaBarra);
                    //Se agrega el campo costo landed RD 
            LineaTicket linea = new LineaTicket(
                    ultimaBarra, 
                    a, 
                    cantidad,
                    ultimoArticulo.getPrecioTarifaOrigen(),
                    ultimoArticulo.getPrecioTotalTarifaOrigen(),new BigDecimal(BigInteger.ZERO));

            DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
            datosAdicionales.setEnvioDomicilio(ultimoArticulo.isPendienteEnvio());
            datosAdicionales.setRecogidaPosterior(ultimoArticulo.isPendienteRecoger());
            BigDecimal descuentoLinea = ultimoArticulo.getDescuento();
            if (descuentoLinea == null){
                descuentoLinea = BigDecimal.ZERO;
            }
            datosAdicionales.setDescuento(descuentoLinea);
            linea.setDatosAdicionales(datosAdicionales);
            linea.establecerDescuento(datosAdicionales.getDescuento());
            lineas.getLineas().add(linea);
            totalAPagar = totalAPagar.add(linea.getPrecioTotal().multiply(BigDecimal.valueOf(cantidad)));
            
            ticket.setLineas(lineas);
            ticket.inicializaTotales(totalAPagar);
            // Creamos un abono por el total del ticket

            Pago pag = new Pago(ticket.getPagos(), null);
            pag.setMedioPagoActivo(MedioPagoBean.getMedioPagoAbonoReservacion());
            pag.setPagoActivo(Pago.PAGO_OTROS);
            pag.setTotal(totalAPagar);
            pag.establecerDescuento(BigDecimal.ZERO);
            pag.establecerEntregado(totalAPagar.toPlainString());

            ticket.crearNuevaLineaPago(pag);
            ticket.recalcularFinalPagado();
            ticket.getTotales().recalcularTotalesLineas(lineas);

            ticket.setFacturacionCliente();

        }
        catch (PagoInvalidException ex) {
            log.error("No se encontró la información un artículo del plan novio en base de datos. Es necesario para generar la factura: PagoInvalidException", ex);
            throw new PlanNovioException("Error generando Factura. No se pudo completar la operación ");
        }
        catch (ImporteInvalidoException ex) {
            log.error("No se encontró la información un artículo del plan novio en base de datos. Es necesario para generar la factura", ex);
            throw new PlanNovioException("Error generando Factura. No se pudo completar la operación ");
        }
        catch (ArticuloNotFoundException ex) {
            log.error("No se encontró la información un artículo del plan novio en base de datos. Es necesario para generar la factura", ex);
            throw new PlanNovioException("Error generando Factura. No se pudo completar la operación ");
        }
        catch (Exception ex) {
            log.error("Error Generando ticket: " + ex.getMessage(), ex);
            throw new PlanNovioException("Error generando Factura. No se pudo completar la operación ");

        }
        return ticket;
    }

    void accionAmpliarReserva(int diasAmpliacion) throws PlanNovioException {
        // Dias de ampliación de la reserva

        Fecha nuevaFechaCaducidad = new Fecha();
        nuevaFechaCaducidad.sumaDias(diasAmpliacion);
        plan.setCaducidad(nuevaFechaCaducidad.getDate());
        PlanNovioServices.modificarPlan(plan);
    }

    // Añade a al lista de Artículos  los nuevos artículos que hemos añadido a la reserva
    // Esta función solo ha de usarse con fines de impresión y refrescando posteriormente la lista 
    // de artículos de la reserva.
    public void addTemporalListaArticulos(List<ArticuloPlanNovio> listaArt) {
        plan.addTemporalListaArticulos(listaArt);
    }

    public void addTemporalAbono(AbonoPlanNovio nuevoAbono) {
        plan.addTemporalAbono(nuevoAbono);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
  
    
}
