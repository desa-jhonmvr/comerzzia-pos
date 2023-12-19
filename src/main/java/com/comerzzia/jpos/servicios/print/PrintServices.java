/*
 * Ticket Services
 */
package com.comerzzia.jpos.servicios.print;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.bonos.ComprobanteBono;
import com.comerzzia.jpos.entity.services.reservaciones.ComprobanteReservacion;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasAbono;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasBonoEfectivo;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasReservacion;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
import com.comerzzia.jpos.entity.services.cierrecaja.CierreCaja;
import com.comerzzia.jpos.servicios.core.variables.ConfigImpresion;
import com.comerzzia.jpos.servicios.credito.CreditoDirectoBean;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.entity.services.reservaciones.ComprobanteAbono;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardBean;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionErradaBean;
import com.comerzzia.jpos.persistencia.notacredito.NotaCreditoDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.DeviceTicket;
import com.comerzzia.jpos.printer.TicketParser;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.printer.escpos.DevicePrinterESCPOS;
import com.comerzzia.jpos.printer.escpos.DevicePrinterESCPOSDriver;
import com.comerzzia.jpos.printer.screen.DevicePrinterPanel;
import com.comerzzia.jpos.scripting.ScriptEngine;
import com.comerzzia.jpos.scripting.ScriptEngineVelocity;
import com.comerzzia.jpos.scripting.ScriptException;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.cotizaciones.CotizacionException;
import com.comerzzia.jpos.servicios.logs.transaccioneserradas.ServicioTransaccionesErradas;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.print.objetos.PrintBono;
import com.comerzzia.jpos.servicios.print.objetos.PrintCheque;
import com.comerzzia.jpos.servicios.print.objetos.PrintMovimientos;
import com.comerzzia.jpos.servicios.print.objetos.PrintNotaCredito;
import com.comerzzia.jpos.servicios.print.objetos.PrintPagoCredito;
import com.comerzzia.jpos.servicios.print.objetos.PrintPagoLetra;
import com.comerzzia.jpos.servicios.print.objetos.PrintSukupon;
import com.comerzzia.jpos.servicios.print.objetos.PrintTicket;
import com.comerzzia.jpos.servicios.print.objetos.PrintVoucher;
import com.comerzzia.jpos.servicios.print.objetos.PrintVoucherAnulacion;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import es.mpsistemas.util.fechas.Fecha;

import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import gnu.io.NoSuchPortException;
import java.awt.Dialog;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.persistencia.promociones.cupones.CuponesDao;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.credito.PagoBonoSuperMaxiNavidad;
import com.comerzzia.jpos.servicios.print.objetos.PrintCintaAuditora;
import com.comerzzia.jpos.servicios.print.objetos.PrintPendiente;
import com.comerzzia.jpos.servicios.print.objetos.PrintTablaAmortizacion;
import com.comerzzia.jpos.servicios.print.objetos.PrintVoucherBonoSupermaxi;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromociones;
import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.ClaveAccesoSri;
import com.comerzzia.jpos.util.StringParser;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.apache.commons.lang.StringUtils;

/**
 * Clases de servicio de soporte para Gestión de Altas y bajas de artículos en
 * el ticket de Sesion
 *
 * @author MGRI
 */
public class PrintServices {

    public static int IMPRESORA_TICKET = 1;
    public static int IMPRESORA_COMPROBANTES = 1;
    public static int IMPRESORA_PANTALLA = 0;
    public static String JOURNAL = "JOURNAL";
    public static String RECEIPT = "RECEIPT";
    public static String SLIP = "SLIP";
    public static String INICIO = "INICIO";
    public static final String MENSAJE_RESPUESTA = "No Se Encontraron impresiones para: ";
    private DeviceTicket printer1 = null;
    private TicketParser ttp1;
    private DeviceTicket printer2 = null;
    private TicketParser ttp2;
    private DeviceTicket printerPantalla = null;
    private TicketParser ttpPantalla;
    private static PrintServices printservice = null;
    private static final Logger log = Logger.getMLogger(PrintServices.class);
    private static ImageIcon iconoTransparente = null;
    private String textoCabecera = "";
    private String textoPie = "";
    private String textoReimpresionCabecera = "";
    private String textoReimpresionDetalle = "";
    private String textoAnulada = "";
    private boolean original = false;
    private boolean originalVoucher = false;
    private static final int PULGADAS_INTERLINEADO = 25;

    private List<DocumentosImpresosBean> documentosImpresos;
    private List<Pago> listBonosSupermaxi;
    private boolean imprime = true;

    private PrintServices() {
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
//            PrinterWritterRXTX.logAvailableSerialPorts();
            // Recuperar del properties y ponerlo en sesion
            if (printer1 == null) {
                printer1 = new DeviceTicket(Sesion.getDatosConfiguracion().getCadenaImpresoraTicket());
                ttp1 = new TicketParser(printer1);
            }
            // Recuperar del properties y ponerlo en sesion
            if (printer2 == null) {
                printer2 = new DeviceTicket(Sesion.getDatosConfiguracion().getCadenaImpresoraAdicional());
                ttp2 = new TicketParser(printer2);
            }
        }
        if (printerPantalla == null) {
            printerPantalla = new DeviceTicket("screen:Microsoft XPS Document Writer,receipt");
            ttpPantalla = new TicketParser(printerPantalla);
            URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/iconoTransparente.gif");
            iconoTransparente = new ImageIcon(myurl);
        }
        impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0" : Sesion.getDatosConfiguracion().getImpresoraTermica();
        documentosImpresos = new ArrayList<DocumentosImpresosBean>();
    }

    public static synchronized PrintServices getInstance() {
        if (printservice == null) {
            try {
                printservice = new PrintServices();
            } catch (Exception e) {
                log.error("getInstance()  - Error inicializando PrintServices", e);
            }
        }
        return printservice;
    }

    public boolean isImprime() {
        return imprime;
    }

    public void setImprime(boolean imprime) {
        this.imprime = imprime;
    }

    public void limpiarListaDocumentos() {
        documentosImpresos = new ArrayList<DocumentosImpresosBean>();
    }

    public List<DocumentosImpresosBean> getDocumentosImpresos() {
        return documentosImpresos;
    }

//<editor-fold defaultstate="collapsed" desc="IMPRIMIR TICKET VENTA">
    public void imprimirTicket(TicketS ticket) throws TicketPrinterException, TicketException {
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirTicket(ticket, true, IMPRESORA_PANTALLA);
        } else {
            seleccionarInterlineado(PULGADAS_INTERLINEADO);
            imprimirTicket(ticket, true, IMPRESORA_TICKET);
        }
    }

    //IMPRESIÓN TABLA DE AMORTIZACIÓN
    public void imprimirTabla(TicketS ticket) throws TicketPrinterException, TicketException {
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirTabla(ticket, true, IMPRESORA_PANTALLA);
        } else {
            if (impresoraTermica.equals("0")) {
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            imprimirTabla(ticket, true, IMPRESORA_TICKET);
        }

    }

    public double verificacionCategoriastabla(TicketS ticket) {
        double valortotalProductos = 0;
        String[] cateorias = Sesion.getDatosConfiguracion().getDATOS_TABLA_AMORTIZACION();
        boolean estado = Boolean.FALSE;
        for (int i = 0; i < ticket.getLineas().getLineas().size(); i++) {
            for (int j = 0; j < cateorias.length; j++) {
                if (ticket.getLineas().getLineas().get(i).getArticulo().getCodcategoria().equals(cateorias[j])) {
                    estado = Boolean.TRUE;
//            System.out.println(ticket.getLineas().getLinea(i).getPrecioTotal());
                    valortotalProductos = valortotalProductos + ticket.getLineas().getLinea(i).getImporteTotalFinalPagado().doubleValue();

                }
            }
            if (!estado) {

            }
        }
        return valortotalProductos;
    }

    //IMPRESIÓN DE LA TABLA DE AMORTIZACIÓN
    public void imprimirTabla(TicketS ticket, boolean imprimirCupones, int impresora) throws TicketPrinterException {
        double valortotal = verificacionCategoriastabla(ticket);
        if (valortotal == 0) {
            return;
        }
        log.debug("imprimirTicket() - Imprimiendo Ticket");
        String resource = "";
        boolean resultadoImpresion = true;
        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/ticket_tabla_amortizacion.xml"));
            //log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (ticket.getPagos().getMediosPagoCreditoDirecto().size() > 0) {
            for (int a = 0; a < ticket.getPagos().getMediosPagoCreditoDirecto().size(); a++) {
                if (valortotal == 0) {
                    return;
                }
                PrintTablaAmortizacion printTabla = null;
                if (Sesion.isSukasa()) {

                    printTabla = new PrintTablaAmortizacion(ticket, valortotal, ticket.getPagos().getMediosPagoCreditoDirecto().get(a));
                }
                for (int i = 0; i < 2; i++) {
                    if (i == 1) {
                        printTabla.setPie("ORIGINAL");
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("printTableAmortizacion", printTabla);
                    DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                    try {
                        String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
                        resultadoImpresion = generacionTicket(cadenaImprimir, impresora);
                        if (impresoraTermica.equals("2")) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (ScriptException ex) {
                        java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                if (valortotal > ticket.getPagos().getMediosPagoCreditoDirecto().get(a).getPlanSeleccionado().getaPagar().doubleValue()) {
                    valortotal = valortotal - ticket.getPagos().getMediosPagoCreditoDirecto().get(a).getPlanSeleccionado().getaPagar().doubleValue();
                    System.out.println(valortotal);
                } else {
                    valortotal = 0;
                }

            }
        }

    }

    //IMPRESION DE MOVIMIENTOS CAJA
    public void ImprimirMovimiento(int impresora, CierreCaja cc) throws TicketPrinterException {
        log.debug("imprimirTicket() - Imprimiendo Movimientos");
        String resource = "";
        boolean resultadoImpresion = true;
        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/ticket_tabla_movimientos.xml"));
        } catch (Exception e) {
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }

        PrintMovimientos printTabla = null;
        if (Sesion.isSukasa()) {

            printTabla = new PrintMovimientos(cc);

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("printMovimiento", printTabla);
        DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
        try {
            String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
            resultadoImpresion = generacionTicket(cadenaImprimir, impresora);
        } catch (ScriptException ex) {
            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //IMPRESIÓN DE PENDIENTES DE ENTREGA
    public void imprimirPendientesEntrega(int impresora, Caja caja) throws TicketPrinterException {

        log.debug("imprimirTicket() - Imprimiendo Ticket");
        String resource = "";
        boolean resultadoImpresion = true;
        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/ticket_tabla_pendientes.xml"));
            //log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: " + e.getMessage(), e);
//            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }

        PrintPendiente printTabla = null;
        List<PrintPendiente> respuesta = null;
        if (Sesion.isSukasa()) {

            printTabla = new PrintPendiente("FACTURA PENDIENTE DE DESPACHO");
            respuesta = printTabla.obtenerImpresiones(caja);

        }
        for (int i = 0; i < respuesta.size(); i++) {
//                for (int i = 0; i < 2; i++) {
//                    if (i == 1) {
//                        printTabla.setPie("ORIGINAL");
//                    }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("printPendiente", respuesta.get(i));
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
                resultadoImpresion = generacionTicket(cadenaImprimir, impresora);
            } catch (ScriptException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }

//                }
        }

    }

    public void imprimirEnvioDomicilio(int impresora, Caja caja) throws TicketPrinterException {

        log.debug("imprimirTicket() - Imprimiendo Ticket");
        String resource = "";
        boolean resultadoImpresion = true;
        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/ticket_tabla_pendientes.xml"));
            //log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: " + e.getMessage(), e);
//            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }

        PrintPendiente printTabla = null;
        List<PrintPendiente> respuesta = null;
        if (Sesion.isSukasa()) {

            printTabla = new PrintPendiente("FACTURA PENDIENTE ENTREGA A DOMICILIO");
            respuesta = printTabla.obtenerImpresionesEnvioDomicilio(caja);

        }
        for (int i = 0; i < respuesta.size(); i++) {
//                for (int i = 0; i < 2; i++) {
//                    if (i == 1) {
//                        printTabla.setPie("ORIGINAL");
//                    }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("printPendiente", respuesta.get(i));
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
                resultadoImpresion = generacionTicket(cadenaImprimir, impresora);
            } catch (ScriptException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }

//                }
        }

    }

    public void imprimirCintaAuditora(int impresora, XCintaAuditoraTbl cintaAuditoraDB) throws TicketPrinterException, DocumentoException {
        log.debug("imprimirCintaAuditora()");
        String resource = "";
        boolean resultadoImpresion = true;
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/cintaAuditora.xml"));
        } catch (Exception e) {
            log.error("imprimirCintaAuditora() - Error cargando esquema XML para la impresión de la cinta auditora: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla de cinta auditora.", e);
        }

        PrintCintaAuditora cintaAuditora = null;

        if (Sesion.isSukasa()) {
            cintaAuditora = new PrintCintaAuditora();
            cintaAuditora.construir(cintaAuditoraDB);
        }
        if (cintaAuditora != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cintaAuditora", cintaAuditora);
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_CINTA_AUDITORA);
                documentosImpresos.add(documentoImpreso);

                resultadoImpresion = generacionTicket(cadenaImprimir, impresora);

                //DocumentosService.crearDocumentoCintaAuditora(cintaAuditoraDB, documentoImpreso, DocumentosBean.CINTA_AUDITORA);
            } catch (ScriptException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    String impresoraTermica;

    public void imprimirTicket(TicketS ticket, boolean imprimirCupones, int impresora) throws TicketPrinterException, TicketException {
        log.debug("imprimirTicket() - Imprimiendo Ticket");
        String resource = "";
        boolean resultadoImpresion = true;
        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaFactura()));
            //log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (resource == null) {
            log.error("imprimirTicket() - Error cargando esquema XML para la impresión del ticket: Resource es null. ");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        }
        try {

            // Construimos el objeto impresión de ticket (para sukasa)
            PrintTicket printTicket = null;
            if (Sesion.isSukasa()) {
                printTicket = new PrintTicket(ticket);
            }

            //Cambio temporal
            int numeroCupones = 0;
            if (ticket.getCuponesEmitidos() != null) {
                numeroCupones = ticket.getCuponesEmitidos().size();
            }

            int billetonesAplicados = 0;
            for (Cupon cup : ticket.getCuponesAplicados()) {
                if (cup.getPromocion().getTipoPromocion().isPromocionTipoBilleton()) {
                    billetonesAplicados++;
                }
            }
            //Generar clave acceso
            String claveAcceso = ClaveAccesoSri.generaClave(ticket.getFecha().getDate(), "01", printTicket.getRuc(), "2",
                    ticket.getTienda() + ticket.getCodcaja(), String.format("%09d", ticket.getId_ticket()), "17907461", "1");

            log.info("claveAcceso " + claveAcceso);
            ////Impremir Cupones Cambio de Impresora 

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ticket", ticket);
            map.put("printTicket", printTicket);
            map.put("imprimirCupones", imprimirCupones);
            map.put("numeroCupones", numeroCupones);
            map.put("billetonesAplicados", billetonesAplicados);
            map.put("numeroAutorizacion", claveAcceso);
            map.put("descuentoGlobal", PromocionTipoDtoManualTotal.isActiva() ? 1 : 0);
            textoCabecera = "";
            textoPie = "ORIGINAL - CLIENTE";

            impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0" : Sesion.getDatosConfiguracion().getImpresoraTermica();
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            String cadenaImprimir = generaDocumentoImpresion(resource, map, documentoImpreso);
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_FACTURA);
//            if (ticket.getObservaciones() == null) {
//                textoReimpresionCabecera = "REVISADO";
//            } else {
//                textoReimpresionCabecera = ticket.getObservaciones();
//            }
            documentosImpresos.add(documentoImpreso);

            // Si no es impresión Previa no cambio la impresora
            if (impresora == IMPRESORA_PANTALLA) {
                resultadoImpresion = generacionTicket(cadenaImprimir, impresora);
            } else {
                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_FACTURA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    resultadoImpresion = resultadoImpresion && generacionTicket(cadenaImprimir, imprAux);
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                if (variableAsConfigImpresion.getImpresiones() == 1 && ticket.hayPagoCreditoTemporal()) {
                    // si no hay impresiones extra y se ha pagado con un credito directo, imprimimos un ticket extra
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                    resultadoImpresion = generacionTicket(cadenaImprimir, imprAux);
                }

                log.info("impresoraTermica " + impresoraTermica);
                if (impresoraTermica.equals("2")) {
                    Thread.sleep(10000);
                }
            }
            textoPie = "";
            textoCabecera = "";

            ticket.ordenarCupones();
            ///Prueba Validacion Para Credito Directo 
            ///////////////////////////////////////////////////////////////
//            //IMPRESIÓN DE LA TABLA DE AMORTIZACIÓM
            if (ticket.getPagos().getMediosPagoCreditoDirecto().size() > 0) {
                imprimirTabla(ticket);
                if (impresoraTermica.equals("2")) {
                    Thread.sleep(5000);
                }

            }

            if (imprimirCupones) {
                imprimirVouchers(ticket.getPagos(), ticket.getIdFactura(), "VENTA");
                if (impresoraTermica.equals("2")) {
                    Thread.sleep(5000);
                }
            }

            //  imprimirTextoAdicionalCreditoDirecto(ticket.getPagos().getPagos(), ticket.getCliente().getIdentificacion(), ticket.getCliente().getNombreYApellidos(), false);
            if (Sesion.isSukasa()) {
                imprimirContratoExtensionGarantia(ticket);
                if (impresoraTermica.equals("2")) {
                    Thread.sleep(7000);
                }
            }
            if (imprimirCupones && !ticket.getCuponesEmitidos().isEmpty()) {
                //Proceso Para cambio De Impresora
                imprimirCupones(ticket);
            }
            if (!resultadoImpresion) {
                //log.error("imprimirTicket() - Error imprimiendo ticket: " + e.getMessage(), e);
                //ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
                throw new TicketPrinterException("Ha ocurrido un error imprimiendo ticket.");
            }
        } catch (ScriptException e) {
            log.error("imprimirTicket() - Error imprimiendo ticket: " + e.getMessage());
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket. Script mal formado.");
        } catch (Exception e) {
            log.error("imprimirTicket() - Error imprimiendo ticket: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            throw new TicketPrinterException("Error imprimiendo ticket.");
        }
    }
    //</editor-fold>

    public void imprimirFacturaPagoReservaArticulo(TicketS ticket, ReservaBean reservacion, ReservaInvitadoBean invitado)
            throws TicketPrinterException, TicketException {
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirFacturaPagoReservaArticulo(ticket, reservacion, invitado, true, IMPRESORA_TICKET);
        } else {
            seleccionarInterlineado(PULGADAS_INTERLINEADO);
            imprimirFacturaPagoReservaArticulo(ticket, reservacion, invitado, true, IMPRESORA_PANTALLA);
        }
    }

    public boolean imprimirFacturaPagoReservaArticulo(TicketS ticket, ReservaBean reservacion, ReservaInvitadoBean invitado, boolean imprimirCupones, int impresora)
            throws TicketPrinterException, TicketException {
        log.debug("imprimirFacturaPagoReservaArticulo() - Imprimiendo factura");
        boolean ventanaAceptada = false;
        String resource = null;
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaFactura()));
        } catch (Exception e) {
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            log.error("imprimirFacturaPagoReservaArticulo() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirFacturaPagoReservaArticulo() - resource es null");
        } else {
            try {
                PrintTicket printTicket = null;
                if (Sesion.isSukasa()) {
                    printTicket = new PrintTicket(ticket);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ticket", ticket);
                map.put("printTicket", printTicket);
                map.put("reserva", reservacion);
                map.put("invitado", invitado);
                textoPie = "ORIGINAL - CLIENTE";

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_FACTURA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    ventanaAceptada = generacionTicket(resource, map, imprAux, documentoImpreso);
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                if (variableAsConfigImpresion.getImpresiones() == 1 && ticket.hayPagoCreditoTemporal()) {
                    // si no hay impresiones extra y se ha pagado con un credito directo, imprimimos un ticket extra
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                    ventanaAceptada = generacionTicket(resource, map, imprAux, null);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_FACTURA);
                documentosImpresos.add(documentoImpreso);

            } catch (NoSuchPortException e) {
                log.error("imprimirFacturaPagoReservaArticulo() - Error imprimiendo ticket: " + e.getMessage());
                ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirFacturaPagoReservaArticulo() - Error imprimiendo ticket: " + e.getMessage(), e);
                ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }

        ticket.ordenarCupones();
        if (imprimirCupones) {
            imprimirVouchers(ticket.getPagos(), ticket.getIdFactura(), "VENTA");
        }
        if (imprimirCupones && !ticket.getCuponesEmitidos().isEmpty()) {
            imprimirCupones(ticket);
        }
        return ventanaAceptada;
    }

    public void imprimirFacturaPagoPlanArticulo(TicketS ticket, PlanNovioOBJ planNovio, InvitadoPlanNovio invitado) throws TicketPrinterException, TicketException {
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            seleccionarInterlineado(PULGADAS_INTERLINEADO);
            imprimirFacturaPagoPlanArticulo(ticket, planNovio, invitado, true, IMPRESORA_TICKET);
        } else {
            imprimirFacturaPagoPlanArticulo(ticket, planNovio, invitado, true, IMPRESORA_PANTALLA);
        }
    }

    public boolean imprimirFacturaPagoPlanArticulo(TicketS ticket, PlanNovioOBJ plan, InvitadoPlanNovio invitado, boolean imprimirCupones, int impresora)
            throws TicketPrinterException, TicketException {
        log.debug("imprimirFacturaPagoPlanArticulo() - Imprimiendo factura");
        boolean ventanaAceptada = false;
        String resource = null;
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaFactura()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirFacturaPagoPlanArticulo() - Error: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
        }
        if (resource == null) {
            log.error("imprimirFacturaPagoPlanArticulo() - resource es null");
        } else {
            try {

                PrintTicket printTicket = null;
                if (Sesion.isSukasa()) {
                    printTicket = new PrintTicket(ticket);
                }

                //Generar clave acceso
                String claveAcceso = ClaveAccesoSri.generaClave(ticket.getFecha().getDate(), "01", printTicket.getRuc(), "2",
                        ticket.getTienda() + ticket.getCodcaja(), String.format("%09d", ticket.getId_ticket()), "17907461", "1");

                log.info("claveAcceso " + claveAcceso);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ticket", ticket);
                map.put("printTicket", printTicket);
                map.put("invitado", invitado);
                map.put("numeroAutorizacion", claveAcceso);
                textoPie = "ORIGINAL - CLIENTE";

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_FACTURA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    ventanaAceptada = generacionTicket(resource, map, imprAux, documentoImpreso);
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                if (variableAsConfigImpresion.getImpresiones() == 1 && ticket.hayPagoCreditoTemporal()) {
                    // si no hay impresiones extra y se ha pagado con un credito directo, imprimimos un ticket extra
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                    ventanaAceptada = generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_FACTURA);
                documentosImpresos.add(documentoImpreso);
                textoPie = "";
                textoCabecera = "";
            } catch (NoSuchPortException e) {
                log.error("imprimirFacturaPagoReservaArticulo() - Error imprimiendo ticket: " + e.getMessage());
                ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirFacturaPagoReservaArticulo() - Error imprimiendo ticket: " + e.getMessage(), e);
                ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
        //Ordenamos los cupones para que imprime primero los sukupones
        ticket.ordenarCupones();
        if (imprimirCupones) {
            imprimirVouchers(ticket.getPagos(), ticket.getIdFactura(), "VENTA");
        }
        if (imprimirCupones && !ticket.getCuponesEmitidos().isEmpty()) {
            imprimirCupones(ticket);
        }
        return ventanaAceptada;
    }

    public void imprimirCupones(TicketS ticket) throws TicketException, TicketPrinterException {
        //String impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0": Sesion.getDatosConfiguracion().getImpresoraTermica();
        for (Cupon cupon : ticket.getCuponesEmitidos()) {
            imprimirCupon(ticket, cupon);
            /*if(impresoraTermica.equals("1")){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                }
             }*/
        }
    }

    public void imprimirCupon(TicketS ticket, Cupon cupon) throws TicketException, TicketPrinterException {
        log.debug("imprimirCupon() - Imprimiendo Cupones");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaCupon()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirCupon() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirCupon() - resource es null");
        } else {
            try {

                // Soporte para Cupones de tipo SUKUPON
                PrintSukupon printSK = null;
                boolean esSukupon = Sesion.isSukasa() && cupon.getPromocion().getTipoPromocion().isPromocionTipoBilleton();

                if (Sesion.isSukasa()) {
                    printSK = new PrintSukupon(ticket, cupon);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tienda", Sesion.getTienda().getLocalForPrint());
                map.put("printSK", printSK);
                map.put("esSukupon", esSukupon);
                map.put("cupon", cupon);
                map.put("ticket", ticket);

                //ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_CUPON_CONFIG_IMPRESION);
                //int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : IMPRESORA_COMPROBANTES);
                /* if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }*/
                if (impresoraTermica.equals("0")) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                try {
                    //int numeroCupones = 0;
                    //  for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    generacionTicket(resource, map, impresora, documentoImpreso);
                    //  numeroCupones++;
                    //}
                } catch (NoSuchPortException e) {
                    log.error("Error imprimiendo ticket: " + e.getMessage());
                    log.error("Error imprimiendo cupon. No existe la impresora en el puerto indicado.");
                } catch (Exception e) {
                    log.error("imprimirCupon() - Error imprimiendo ticket: " + e.getMessage(), e);
                    log.error("No se pudo imprimir el cupon", e);
                }
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_CUPON);
                documentosImpresos.add(documentoImpreso);
                /*if(!Sesion.getDatosConfiguracion().getCadenaImpresoraAdicional()
                                              .equals(Sesion.getDatosConfiguracion().getCadenaImpresoraTicket())){
                  imprimirCupones(documentoImpreso.getImpreso(), false);
                }*/
            } catch (Exception e) {
                log.error("imprimirCupon() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketException("No se pudo imprimir el cupon", e);
            }
        }
    }

//<editor-fold defaultstate="collapsed" desc="IMPRIMIR TICKET DEVOLUCION">
    public void imprimirTicketDevolucionNC(Devolucion devolucion) throws TicketException, ScriptException, TicketPrinterException, IOException {

        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirTicketDevolucion(IMPRESORA_PANTALLA, devolucion);
        } else {
            seleccionarInterlineado(PULGADAS_INTERLINEADO);
            imprimirTicketDevolucion(IMPRESORA_TICKET, devolucion);
        }
    }

    public void imprimirTicketDevolucion(Devolucion devolucion) throws TicketException, ScriptException, TicketPrinterException, IOException {

        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirTicketDevolucion(IMPRESORA_PANTALLA, devolucion);
        } else {
            imprimirTicketDevolucion(IMPRESORA_TICKET, devolucion);
        }
    }

    private void imprimirTicketDevolucion(int impresora, Devolucion devolucion) throws TicketException, TicketPrinterException {
        log.debug("imprimirTicketDevolucion() - Imprimiendo Nota de Crédito");
        String resource = "";

        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaReciboDevolucion()));
        } catch (Exception e) {// Catch exception if any
            ServicioTransaccionesErradas.crearTransaccionErrada(devolucion.getNotaCredito().getIdNotaCredito(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_NOTA_CREDITO);
            log.error("imprimirTicketDevolucion() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirTicketDevolucion() - Error imprimiendo nota de crédito: No se pudo cargar fichero resource.");
            throw new TicketPrinterException("No se ha podido leer el archivo especificado.");
        }
        try {

            PrintNotaCredito printNC = null;
            //if (Sesion.isSukasa()) {
            printNC = new PrintNotaCredito(devolucion);
            //}

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("devolucion", devolucion);
            map.put("printNC", printNC);
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_DEVOLUCION_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                if (i == 0) {
                    textoCabecera = "";
                    textoPie = "ORIGINAL - CLIENTE";
                } else {
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                generacionTicket(resource, map, imprAux, documentoImpreso);
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_NC);
            documentosImpresos.add(documentoImpreso);
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo ticket: " + e.getMessage());
            ServicioTransaccionesErradas.crearTransaccionErrada(devolucion.getNotaCredito().getIdNotaCredito(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_NOTA_CREDITO);
            throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo ticket: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(devolucion.getNotaCredito().getIdNotaCredito(), TransaccionErradaBean.TIPO_IMPRIMIENDO, TransaccionErradaBean.TIPO_TRANSACCION_NOTA_CREDITO);
            throw new TicketException("No se pudo imprimir el ticketDevolucion");
        }
    }

    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="AUXILIARES">
    /**
     * Apertura del recurso xml de descripción de ticket
     *
     * @return
     * @throws IOException
     */
    private String leerEsquemaTicket(URL rutaEsquemaRecibo) throws IOException {
        String resource = "";
        URL myurl = rutaEsquemaRecibo;
        DataInputStream in = new DataInputStream(myurl.openStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            resource += strLine;
        }
        // Close the input stream
        in.close();
        return resource;
    }

    private String generaDocumentoImpresion(String resource, Map<String, Object> parametrosTicket, DocumentosImpresosBean documentoImpreso) throws ScriptException {
        ScriptEngine script = new ScriptEngineVelocity();
        Iterator<String> keyIterator = parametrosTicket.keySet().iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            //log.debug("añadiendo " + key + "\n");
            script.put(key, parametrosTicket.get(key));
        }
        //log.debug("Generando ticket\n");
        String s = script.eval(resource).toString();
        s = StringParser.parseaXMLDocumentosImpresos(s);

        String J = new String(s.getBytes(Charset.forName("utf-8")));

        /*
        String utf8 = "É";
        try
        {
            byte[] A = utf8.getBytes();
            byte[] B = utf8.getBytes(ISO_8859_1);
            byte[] C = utf8.getBytes("ISO-8859-15");
        } catch (Exception e) {
        }
        
        String pru = "";
        try
        {
            pru = new String(s.getBytes(),"ISO-8859-15");
        } 
        catch (Exception e) {
        }      
        
        byte[] ptext = s.getBytes(ISO_8859_1); 
        String value = new String(ptext, UTF_8);
         */
        if (documentoImpreso != null) {
            //documentoImpreso.setImpreso(s.getBytes());
            //DAINOVY
            /*
            byte[] bytes = J.getBytes(ISO_8859_1); 
            String valueR = new String(bytes, UTF_8);
            documentoImpreso.setImpreso(valueR.getBytes());
             */
            documentoImpreso.setImpreso(J.getBytes());
            /*
            documentoImpreso.setImpreso(value.getBytes());
            documentoImpreso.setImpreso(pru.getBytes());
             */
            //DAINOVY
        }
        log.debug("generaDocumentoImpresion() - Documento de impresión generado correctamente\n");

        return s;
    }

    /*
    private String generaDocumentoImpresion(String resource, Map<String, Object> parametrosTicket, DocumentosImpresosBean documentoImpreso) throws ScriptException {
        ScriptEngine script = new ScriptEngineVelocity();
        Iterator<String> keyIterator = parametrosTicket.keySet().iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            //log.debug("añadiendo " + key + "\n");
            script.put(key, parametrosTicket.get(key));
        }
        //log.debug("Generando ticket\n");
        String s = script.eval(resource).toString();
        if (documentoImpreso != null) {
            documentoImpreso.setImpreso(s.getBytes());
        }
        log.debug("generaDocumentoImpresion() - Documento de impresión generado correctamente\n");

        return s;
    }
     */
    private boolean generacionTicket(String s, Integer numeroImpresora) {
        boolean resul = true;
        Sesion.putTiempoEjecucion("inicioProcesoImpresion", System.currentTimeMillis());
        log.debug("generacionTicket() - Desde el inicio del proceso de venta hasta el inicio de la impresión ha tardado: " + Sesion.getDameTiempo("inicioProcesoPago", "inicioProcesoImpresion") + " segundos");
        try {
            if (numeroImpresora == 1) {
                TicketParser ttp11 = new TicketParser(printer1);
                ttp11.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
                ttp11.setTextoPie(textoCabeceraYPiePagina(textoPie));
                ttp11.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
                log.debug("generacionTicket()- Imprimiendo ticket por número de impresora: " + numeroImpresora);
                log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
                ttp11.printTicket(s);
                Sesion.putTiempoEjecucion("finProcesoImpresion", System.currentTimeMillis());
                log.debug("generacionTicket() - El proceso de impresión ha tardado: " + Sesion.getDameTiempo("inicioProcesoImpresion", "finProcesoImpresion") + " segundos");
                log.debug("generacionTicket()- Documento enviado a la impresora correctamente.");
            } else if (numeroImpresora == 2) {
                TicketParser ttp11 = new TicketParser(printer1);
                ttp11.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
                ttp11.setTextoPie(textoCabeceraYPiePagina(textoPie));
                ttp11.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
                log.debug("generacionTicket()- Imprimiendo ticket por número de impresora: " + numeroImpresora);
                log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
                ttp11.printTicket(s);
                Sesion.putTiempoEjecucion("finProcesoImpresion", System.currentTimeMillis());
                log.debug("generacionTicket() - El proceso de impresión ha tardado: " + Sesion.getDameTiempo("inicioProcesoImpresion", "finProcesoImpresion") + " segundos");
                log.debug("generacionTicket()- Documento enviado a la impresora correctamente.");
            } else if (numeroImpresora == 0) {
                ttpPantalla.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
                ttpPantalla.setTextoPie(textoCabeceraYPiePagina(textoPie));
                ttpPantalla.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
                log.debug("generacionTicket() - Imprimiendo ticket pantalla...");
                log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
                ttpPantalla.printTicket(s);
                DevicePrinterPanel panelTicket = (DevicePrinterPanel) printerPantalla.getDevicePrinter("1");
                panelTicket.setSize(500, 500);
                crearVentanaDialogoImpresion(panelTicket);
                Sesion.putTiempoEjecucion("finVentanaAceptada", System.currentTimeMillis());
                log.debug("generacionTicket() - Ventana de previsualización cerrada.");
                panelTicket.reset();
            }
        } catch (Exception e) {
            log.debug("generacionTicket() - Error generando ticket: " + e.getMessage(), e);
            resul = false;
        }
        textoPie = "";
        textoAnulada = "";
        textoCabecera = "";
        return resul;
    }

    //Original
    private boolean generacionTicket(String resource, Map<String, Object> parametrosTicket, Integer numeroImpresora, DocumentosImpresosBean documentoImpreso) throws ScriptException, TicketPrinterException, IOException, gnu.io.NoSuchPortException {
        boolean ventanaAceptada = false;
        ScriptEngine script = new ScriptEngineVelocity();
        Iterator<String> keyIterator = parametrosTicket.keySet().iterator();
        Sesion.putTiempoEjecucion("inicioProcesoImpresion", System.currentTimeMillis());
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            //log.debug("añadiendo " + key + "\n");
            script.put(key, parametrosTicket.get(key));
        }
        //log.debug("Generando ticket: " + resource);
        String s = script.eval(resource).toString();
        if (documentoImpreso != null) {
            documentoImpreso.setImpreso(s.getBytes());
        }

        if (numeroImpresora == 1) {
            TicketParser ttp11 = new TicketParser(printer1);
            ttp11.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttp11.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttp11.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            ttp11.setOriginal(this.original);
            log.debug("generacionTicket()- Imprimiendo ticket por número de impresora: " + numeroImpresora);
            log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
            if (imprime) {
                ttp11.printTicket(s);
            }
            log.debug("generacionTicket()- Documento enviado a la impresora correctamente.");
        } else if (numeroImpresora == 2) {
            TicketParser ttp11 = new TicketParser(printer1);
            ttp11.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttp11.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttp11.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            ttp11.setOriginal(this.original);
            log.debug("generacionTicket()- Imprimiendo ticket por número de impresora: " + numeroImpresora);
            log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
            if (imprime) {
                ttp11.printTicket(s);
            }
            log.debug("generacionTicket()- Documento enviado a la impresora correctamente.");
        } else if (numeroImpresora == 0) {
            ttpPantalla.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttpPantalla.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttpPantalla.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            ttpPantalla.setOriginal(this.original);
            log.debug("generacionTicket() - Imprimiendo ticket pantalla...");
            log.debug("generacionTicket() - El XML de impresión de ticket es: " + s);
            if (imprime) {
                ttpPantalla.printTicket(s);
            }
            DevicePrinterPanel panelTicket = (DevicePrinterPanel) printerPantalla.getDevicePrinter("1");
            panelTicket.setSize(500, 500);
            ventanaAceptada = crearVentanaDialogoImpresion(panelTicket);

            log.debug("generacionTicket() - Ventana de previsualización cerrada.");
            panelTicket.reset();
        }

        textoPie = "";
        textoAnulada = "";
        textoCabecera = "";
        return ventanaAceptada;
    }

    private boolean crearVentanaDialogoImpresion(DevicePrinterPanel panelTicket) {
        log.debug("Creando Ventana de Diálogo de impresión...");

        JDialog jDialog = new JDialog();
        jDialog.setIconImage(iconoTransparente.getImage());
        panelTicket.setjDialogParent(jDialog);
        jDialog.setAlwaysOnTop(true);
        jDialog.setMinimumSize(new java.awt.Dimension(500, 100));
        jDialog.setResizable(false);

        jDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jDialog.setSize(540, 560);
        jDialog.getContentPane().add(panelTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 500));
        jDialog.setLocationRelativeTo(null);
        jDialog.setModal(true);
        jDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        jDialog.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        log.debug("Pantalla de impresion mostrándose.");
        JPrincipal.setPopupActivo(jDialog);
        Sesion.putTiempoEjecucion("finProcesoImpresion", System.currentTimeMillis());
        log.debug("generacionTicket() - El proceso de impresión ha tardado: " + Sesion.getDameTiempo("inicioProcesoImpresion", "finProcesoImpresion") + " segundos");
        jDialog.setVisible(true);
        JPrincipal.setPopupActivo(null);
        return panelTicket.isVentanaAceptada();

    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ABRIR CAJON">
    public void abrirCajon() {
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            log.debug("APERTURA DE CAJÓN: ENVIO DE SEÑAL 1");
            printer1.getDevicePrinter("1").openDrawer();
            log.debug("APERTURA DE CAJÓN REALIZADA CON EXITO");
        }
    }

    private void seleccionarImpresora(String tipo) throws TicketPrinterException {
        try {

            if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
                log.debug("Cambiando la impresora a: " + tipo);

                try {
                    if (tipo.equals(INICIO)) {
                        ((DevicePrinterESCPOSDriver) printer1.getDevicePrinter("1")).reset();
                    }
                    ((DevicePrinterESCPOSDriver) printer1.getDevicePrinter("1")).seleccionarImpresora(tipo);
                    log.debug("Impresora cambiada correctamente a: " + tipo);
                    return; // Exito, salimos del bucle
                } catch (Exception e) {
                    log.info("La impresora no es de tipo DevicePrinterESCPOSDriver o error en selección de modo SLIP");
                }

                try {
                    if (tipo.equals(INICIO)) {
                        ((DevicePrinterESCPOS) printer1.getDevicePrinter("1")).reset();
                    }
                    ((DevicePrinterESCPOS) printer1.getDevicePrinter("1")).seleccionarImpresora(tipo);
                    log.debug("Impresora cambiada correctamente a: " + tipo);
                } catch (Exception e) {
                    log.info("La impresora no es de tipo DevicePrinterESCPOS o error en selección de modo SLIP");
                }
            }
        } catch (Exception e) {
            throw new TicketPrinterException("seleccionarImpresora() - Error cambiando el modo de la impresora a: " + tipo, e);
        }
    }

    private void seleccionarInterlineado(int pulgadas) throws TicketPrinterException {
        log.info("seleccionarInterlineado() - " + pulgadas);

        try {
            if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
                log.debug("seleccionarInterlineado() - Cambiando el interlineado a: " + (pulgadas == 0 ? "standart" : pulgadas));
                if (printer1.getClass().equals(DevicePrinterESCPOSDriver.class)) {
                    //  ((DevicePrinterESCPOSDriver)printer1.getDevicePrinter("1")).seleccionaInterlineado(pulgadas);
                    log.debug("seleccionarInterlineado() - Interlineado de impresora escposdriver cambiada correctamente a: " + (pulgadas == 0 ? "standart" : pulgadas));
                } else if (printer1.getClass().equals(DevicePrinterESCPOS.class)) {
                    // ((DevicePrinterESCPOS)printer1.getDevicePrinter("1")).seleccionaInterlineado(pulgadas);
                    log.debug("seleccionarInterlineado() - Interlineado de impresora escpos cambiada correctamente a: " + (pulgadas == 0 ? "standart" : pulgadas));
                } else {
                    if (printer1.getDevicePrinterAll() != null && printer1.getDevicePrinterAll().size() > 0) {
                        DevicePrinter prin = printer1.getDevicePrinter("1");
                        if (prin.getClass().equals(DevicePrinterESCPOSDriver.class)) {
                            // ((DevicePrinterESCPOSDriver)prin).seleccionaInterlineado(pulgadas);
                            log.debug("seleccionarInterlineado() - Interlineado de impresora escposdriver cambiada correctamente a: " + (pulgadas == 0 ? "standart" : pulgadas));
                        } else if (prin.getClass().equals(DevicePrinterESCPOS.class)) {
                            //  ((DevicePrinterESCPOS)prin).seleccionaInterlineado(pulgadas);
                            log.debug("seleccionarInterlineado() - Interlineado de impresora escpos cambiada correctamente a: " + (pulgadas == 0 ? "standart" : pulgadas));
                        }
                    } else {
                        log.debug("seleccionarInterlineado() - No se va a cambiar el interlineado porque no está para los definidos en el tipo de impresión");
                    }
                }
            }
        } catch (Exception e) {
            throw new TicketPrinterException("seleccionarImpresora() - Error cambiando el interlineado de la impresora a: " + (pulgadas == 0 ? "standart" : pulgadas), e);
        }
    }

    //</editor-fold>
    public void imprimirCheque(TicketS ticket, Pago pago, String lado, boolean esPrimeraVez) throws TicketPrinterException {
        log.debug("imprimirCheque() - Imprimiendo cheque del " + lado);
        String resourceAnverso = "", resourceReverso = "";
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_TICKET;
        }
        try {
            if (lado.equals("ANVERSO")) {
                resourceAnverso = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaChequeAnverso()));
                log.debug("El fichero que tenemos del anverso es: " + resourceAnverso);
            }
            if (lado.equals("REVERSO")) {
                resourceReverso = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaChequeReverso()));
                log.debug("El fichero que tenemos del reverso es: " + resourceReverso);
            }
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirCheque() - Error imprimiendo cheque: Error cargando la plantilla del " + lado + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo cheque: Error cargando la plantilla del " + lado + ".", e);
        }
        if (resourceAnverso == null || resourceReverso == null) {
            log.error("imprimirCheque() - Error imprimiendo cheque: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo cheque: No se ha encontrado la plantilla.");
        } else {
            try {
                // Cabecera de Sukasa
                PrintCheque printCheque = null;
                if (Sesion.isSukasa()) {
                    printCheque = new PrintCheque(ticket, pago);
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("printCheque", printCheque);
                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_CHEQUE_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());

                //seleccionarImpresora(SLIP); // El tipo de documento SLIP se seleccionará de forma automática desde 
                if (lado.equals("ANVERSO")) {
                    if (esPrimeraVez) {
                        //JPrincipal.getInstance().crearInformacion("Por favor, espere a que se impriman todos los tickets, coloque el cheque por el lado anverso y confirme la operación pulsando enter");
                        //JPrincipal.getInstance().crearInformacion("Por favor, espere a que se impriman todos los tickets, coloque el cheque por el lado anverso y confirme la operación pulsando enter");
                    } else {
                        //JPrincipal.getInstance().crearInformacion("Coloque el cheque por el lado anverso y confirme la operación pulsando enter");                       
                    }
                    Integer interlineadoAnverso = VariablesAlm.getVariableAsInt(VariablesAlm.INTERLINEADO_CHEQUE_ANVERSO);
                    seleccionarInterlineado(interlineadoAnverso);
                    generacionTicket(resourceAnverso, map, imprAux, null);
                    seleccionarInterlineado(0);
                }
                if (lado.equals("REVERSO")) {
                    JPrincipal.getInstance().crearInformacion("Pulse enter y coloque el cheque por el lado reverso");
                    Integer interlineadoReverso = VariablesAlm.getVariableAsInt(VariablesAlm.INTERLINEADO_CHEQUE_REVERSO);
                    seleccionarInterlineado(interlineadoReverso);
                    generacionTicket(resourceReverso, map, imprAux, null);
                    seleccionarInterlineado(0);

                }
                // Reseteamos la impresora              
                seleccionarImpresora(INICIO);
            } catch (ScriptException e) {
                log.error("imprimirCheque() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket. Script mal formado.");
            } catch (NoSuchPortException e) {
                log.error("imprimirCheque() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirCheque() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket.");
            }
        }
    }
//<editor-fold defaultstate="collapsed" desc="IMPRIMIR COMPROBANTE ABONO">

    public void imprimirComprobanteAbono(ComprobanteAbono comprobante, boolean isPlanNovio, boolean isLiquidarReservacion) throws TicketPrinterException, TicketException {
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirComprobanteAbono(comprobante, isPlanNovio, IMPRESORA_PANTALLA, isLiquidarReservacion);
        } else {
            imprimirComprobanteAbono(comprobante, isPlanNovio, IMPRESORA_COMPROBANTES, isLiquidarReservacion);
        }
    }

    private void imprimirComprobanteAbono(ComprobanteAbono comprobante, boolean isPlanNovio, int impresora, boolean isLiquidarReservacion) throws TicketPrinterException {
        log.debug("imprimirComprobanteAbono() - Imprimiendo comprobante de Abono");
        String resource = "";
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaAbono()));
            //log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobanteAbono() - Error imprimiendo ticket: Error cargando la plantilla." + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (resource == null) {
            log.error("imprimirComprobanteAbono() - Error imprimiendo ticket: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("comprobante", comprobante);
                map.put("facturacion", comprobante.getFacturacion());
                map.put("fecha", comprobante.getFechaYHora());
                map.put("caja", Sesion.getCajaActual().getCajaActual().getCodcaja());
                map.put("region", Sesion.getTienda().getCodRegion().getDesregion());
                map.put("total", comprobante.getTotal());
                map.put("totalAbonoEntregado", comprobante.getTotalAbonoEntregado());
                map.put("pagos", comprobante.getPagos());
                if (isPlanNovio) {
                    map.put("codReserva", comprobante.getCodReserva());
                } else {
                    map.put("codReserva", comprobante.getCodigoBarras().getAlmacen() + "-" + comprobante.getCodReserva());
                }
                map.put("cajero", comprobante.getCajero());
                map.put("codigoBarras", (CodigoBarrasAbono) comprobante.getCodigoBarras());
                map.put("empresa", Sesion.getEmpresa());
                map.put("tienda", Sesion.getTienda());
                map.put("isPlanNovio", isPlanNovio);

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ABONO_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoCabecera = "";
                        textoPie = "ORIGINAL - CLIENTE";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                if (isPlanNovio) {
                    documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_ABONO_PLAN_NOVIO);
                } else {
                    documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_ABONO_RESERVA);
                }
                documentosImpresos.add(documentoImpreso);
                if (!isLiquidarReservacion) {
                    imprimirVouchers(comprobante.getPagos(), ((CodigoBarrasAbono) comprobante.getCodigoBarras()).getCodigoBarras(), "ABONO");
                }
            } catch (ScriptException e) {
                log.error("imprimirComprobanteAbono() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket. Script mal formado.");
            } catch (NoSuchPortException e) {
                log.error("imprimirComprobanteAbono() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirComprobanteAbono() - Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket.");
            }
        }
    }
//</editor-fold>

    public void imprimirArticulosReserva(List<ArticuloReservado> articulos, Reservacion reserva) throws TicketPrinterException, TicketException {
        log.debug("imprimirArticulosReserva() - Imprimiendo artículos de Reserva");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaListadoArticulosReserva()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirArticulosReserva() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirArticulosReserva() - resource es null");
        } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("articulos", articulos);
                map.put("reserva", reserva.getReservacion());
                map.put("ticket", reserva.getTicket());
                map.put("fecha", new Fecha());
                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARTICULOS_RESERVA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }

                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoCabecera = "";
                        textoPie = "ORIGINAL - CLIENTE";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_RES_LIQUIDA);
                documentosImpresos.add(documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

//<editor-fold defaultstate="collapsed" desc="IMPRIMIR AMPLIACION TICKET">
    public void imprimirComprobanteRetiro(String importeRetiro, String fechaRetiro, UsuarioBean cajero) throws TicketPrinterException, TicketException {
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirComprobanteRetiro(importeRetiro, fechaRetiro, cajero, IMPRESORA_PANTALLA);
        } else {
            imprimirComprobanteRetiro(importeRetiro, fechaRetiro, cajero, IMPRESORA_COMPROBANTES);
        }

    }

    public void imprimirComprobanteRetiro(String importeRetiro, String fechaRetiro, UsuarioBean cajero, int impresora) throws TicketPrinterException {
        log.debug("imprimirComprobanteRetiro() - Imprimiendo comprobante de retiro");
        String resource = "";

        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteRetiro()));
            log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("Error imprimiendo ticket: Error cargando la plantilla." + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (resource == null) {
            log.error("Error imprimiendo ticket: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("importeRetiro", importeRetiro);
            map.put("fechaRetiro", fechaRetiro);
            map.put("cajero", cajero);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_RETIRO_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                if (i == 0) {
                    textoCabecera = "";
                    textoPie = "ORIGINAL - CLIENTE";
                } else {
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                generacionTicket(resource, map, imprAux, null);
            }
            textoPie = "";
            textoCabecera = "";
        } catch (ScriptException e) {
            log.error("Error imprimiendo ticket: " + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo ticket. Script mal formado.");
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo ticket: " + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo ticket: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo ticket.");
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc"RESERVACION">
    public void imprimirComprobanteReservacion(ComprobanteReservacion comprobante) throws TicketPrinterException {
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            imprimirComprobanteReservacion(comprobante, IMPRESORA_PANTALLA);
        } else {
            imprimirComprobanteReservacion(comprobante, IMPRESORA_COMPROBANTES);
        }
    }

    private void imprimirComprobanteReservacion(ComprobanteReservacion comprobante, int impresora) throws TicketPrinterException {
        log.debug("imprimirComprobanteReservacion() - Imprimiendo reservación");
        String resource = "";

        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaReservacion()));
            log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("Error imprimiendo ticket: Error cargando la plantilla." + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (resource == null) {
            log.error("Error imprimiendo ticket: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("facturacion", comprobante.getFacturacion());
                map.put("fecha", comprobante.getFechaYHora());
                map.put("total", comprobante.getTotal());
                map.put("ticket", comprobante.getTicket());
                map.put("tienda", comprobante.getTienda());
                map.put("caja", comprobante.getTicket().getCodcaja());
                map.put("codReserva", comprobante.getCodigoBarras().getAlmacen() + "-" + comprobante.getCodReserva());
                map.put("cajero", comprobante.getCajero());
                map.put("codigoBarras", (CodigoBarrasReservacion) comprobante.getCodigoBarras());
                map.put("reserva", comprobante.getReserva());
                map.put("empresa", Sesion.getEmpresa());
                map.put("region", Sesion.getTienda().getCodRegion().getDesregion());
                map.put("tiendaSRI", Sesion.getTienda());

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_RESERVACION_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_RESERVACION);
                documentosImpresos.add(documentoImpreso);
            } catch (ScriptException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. Script mal formado.");
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("Error imprimiendo ticket: " + e.getMessage(), e);
                throw new TicketPrinterException("Error imprimiendo ticket.");
            }
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BONO">

    public void imprimirComprobanteBono(ComprobanteBono comprobante, Boolean vieneFac) throws TicketPrinterException {
        log.debug("imprimirComprobanteBono() - Imprimiendo Bono");
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }

        String resource = "";

        // Poner en funcion que lee el fichero xml.
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteBono()));
            log.debug("El fichero que tenemos es: " + resource);
        } catch (Exception e) {// Catch exception if any
            log.error("Error imprimiendo ticket: Error cargando la plantilla." + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo ticket: Error cargando la plantilla.", e);
        }
        if (resource == null) {
            log.error("Error imprimiendo ticket: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        }
        try {
            String avisoLegal = VariablesAlm.getVariable(VariablesAlm.TICKET_AVISO_LEGAL_BONO);
            LineaEnTicket lineas = new LineaEnTicket(avisoLegal);

            // Cabecera de Sukasa
            PrintBono printBono = null;
            if (Sesion.isSukasa()) {
                printBono = new PrintBono(comprobante);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("fechaInicio", comprobante.getBono().getFechaInicioAsString());
            map.put("tienda", comprobante.getTienda());
            map.put("printBono", printBono);
            map.put("fechaCaducidad", comprobante.getBono().getFechaCaducidadAsString());
            map.put("fechaActual", (new Fecha(comprobante.getBono().getFechaInicio())).getStringHora());
            map.put("hora", (new Fecha(comprobante.getBono().getFechaInicio())).getString("HH:mm"));
            map.put("total", comprobante.getBono().getImporte().toString());
            map.put("cliente", comprobante.getCliente());
            map.put("cajero", comprobante.getCajero());
            map.put("codigoBarras", (CodigoBarrasBonoEfectivo) comprobante.getCodigoBarras());
            map.put("tipoBono", comprobante.getBono().getTipoReferenciaOrigen());
            map.put("referenciaOrigen", comprobante.getBono().getReferenciaOrigen());
            map.put("avisoLegal", lineas);
            map.put("identificador", comprobante.getBono().getBonoPK().getIdBono());
            map.put("hayUidNotaCredito", comprobante.getBono().hayUidNotaCredito());
            map.put("bonoObserva", comprobante.getBono());
            map.put("caja", Sesion.getCajaActual().getCajaActual().getCodcaja());
            if (comprobante.getBono().hayUidNotaCredito()) {
                NotasCredito notCred = NotaCreditoDao.consultarNotaCredito(comprobante.getBono().getUidNotaCredito());
                map.put("notaCredito", notCred);
                List<Bono> bonosUsados = BonosDao.consultarBonosNotaCredito(comprobante.getBono().getUidNotaCredito());
                List<String> consumos = new ArrayList<String>();
                if (bonosUsados.size() > 0) {
                    consumos.add((notCred.getDocumento() == null ? "" : notCred.getDocumento()) + " " + bonosUsados.get(0).getFechaExpedicionAsLineas().getTextoOriginal() + " - $" + notCred.getTotal().subtract(bonosUsados.get(0).getImporte()).toString());
                    for (int i = 1; i <= bonosUsados.size() - 1; i++) {
                        consumos.add((bonosUsados.get(i - 1).getMensaje_proceso() == null ? "" : bonosUsados.get(i - 1).getMensaje_proceso()) + " " + bonosUsados.get(i).getFechaExpedicionAsLineas().getTextoOriginal() + " - $" + bonosUsados.get(i - 1).getImporte().subtract(bonosUsados.get(i).getImporte()).toString());
                    }
                    consumos.add((bonosUsados.get(bonosUsados.size() - 1).getMensaje_proceso() == null ? "" : bonosUsados.get(bonosUsados.size() - 1).getMensaje_proceso()) + " " + bonosUsados.get(bonosUsados.size() - 1).getFechaExpedicionAsLineas().getTextoOriginal() + " - $" + bonosUsados.get(bonosUsados.size() - 1).getImporte().subtract(comprobante.getBono().getImporte()).toString());
                } else {
                    consumos.add((notCred.getDocumento() == null ? "" : notCred.getDocumento()) + " " + new Fecha(notCred.getFecha()).getString("dd/MM/yyyy") + " - $" + notCred.getTotal().subtract(comprobante.getBono().getImporte()));
                }
                map.put("consumos", consumos);
            } else {
                String reservaOrigen = "";
                if (comprobante.getBono().hayUidReservacion()) {
                    List<Bono> bonosUsados = BonosDao.consultarBonosReservacion(comprobante.getBono().getUidReservacion());
                    //El bono que pasamos como parámetro tenemos que incluirlo porque aún no esta almacenado en BBDD
                    bonosUsados.add(comprobante.getBono());
                    BigDecimal valor = BigDecimal.ZERO;
                    for (Bono bono : bonosUsados) {
                        if (bono.getImporte().compareTo(valor) > 0) {
                            valor = bono.getImporte();
                        }
                        if (bono.getTipoReferenciaOrigen().equals(BonosServices.PROCEDENCIA_RESERVA_CANCELACION)) {
                            reservaOrigen = bono.getBonoPK().getCodalm() + "-" + bono.getReferenciaOrigen();
                        }
                    }
                    map.put("valorOriginal", valor);
                    map.put("valorDebitado", valor.subtract(comprobante.getBono().getImporte()));
                    map.put("reservaOrigen", reservaOrigen);
                }
            }

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_BONO_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                        generacionTicket(resource, map, imprAux, documentoImpreso);
                    }
                    //comentar el else para que solo imprima el bono de nota de credito original y no la copia, no cambiar: variableAsConfigImpresion.getImpresiones() pq se utiliza en varios lugares 
                    /*
                    else
                    {
                        textoPie = "COPIA";
                        textoCabecera = "SIN VALIDEZ";
                        generacionTicket(resource, map, imprAux, null);
                    }
                     */

                }
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo el bono: " + e.getMessage());
                log.error("Error imprimiendo bono. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirComprobanteBono() - No se pudo imprimir el bono", e);
                log.error("Error imprimiendo bono", e);
            }
            textoPie = "";
            textoCabecera = "";
            if (vieneFac) {
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_BONO);
            } else {
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_RES_BONO);
            }
            documentosImpresos.add(documentoImpreso);
        } catch (Exception e) {
            log.error("Error imprimiendo bono: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo bono.");
        }
    }
    //</editor-fold>

    //<editor-fold  desc="GIFTCARD">
    public void imprimirComprobanteGiftCard(String tramaGC, String codOperacion, Cliente cliente, BigDecimal pagoTotal, List<Pago> pagos, LineaEnTicket lineasObservaciones, String idGC) throws TicketPrinterException {
        log.debug("imprimirComprobanteGiftCard() - Imprimiendo GiftCard");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteGiftCard()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobanteGiftCard() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirComprobanteGiftCard() - Error imprimiendo ticket: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo ticket: No se ha encontrado la plantilla.");
        }
        try {

            FacturacionTicket ft = new FacturacionTicket(cliente);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tramaGC", tramaGC);
            if (codOperacion.equals(LogGiftCardBean.OPERACION_CARGA_INICIAL)) {
                map.put("operacion", "COMPRA");
            }
            if (codOperacion.equals(LogGiftCardBean.OPERACION_RECARGA)) {
                map.put("operacion", "RECARGA");
            }
            map.put("cliente", cliente);
            map.put("facturacion", ft);
            map.put("pagoTotal", pagoTotal);
            map.put("pagos", pagos);
            map.put("cajero", Sesion.getUsuario());
            map.put("tienda", Sesion.getTienda().getLocalForPrint());
            map.put("fecha", new Fecha(new Date()).getString("dd/MM/yyyy - HH:mm"));
            map.put("idGC", idGC);
            map.put("observaciones", lineasObservaciones);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_GIFTCARD_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                if (i == 0) {
                    textoPie = "ORIGINAL - CLIENTE";
                    textoCabecera = "";
                } else {
                    textoPie = "COPIA";
                    textoCabecera = "COPIA";
                }
                generacionTicket(resource, map, imprAux, documentoImpreso);
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_GIFTCAR);
            documentosImpresos.add(documentoImpreso);
            imprimirVouchers(pagos, tramaGC, DocumentosImpresosBean.TIPO_PAGO, false);

            //Como se llama a la impresión de Vouchers, no vamos a registrar estos documentos
            /*Iterator it = documentosImpresos.iterator();
            while (it.hasNext()) {
                DocumentosImpresosBean doc = (DocumentosImpresosBean) it.next();
                if (doc.getTipoImpreso().equals(DocumentosImpresosBean.TIPO_PAGO)) {
                    it.remove();
                }
            }*/
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo comprobante GiftCard: " + e.getMessage());
            throw new TicketPrinterException("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo comprobante GiftCard: " + e.getMessage(), e);
            throw new TicketPrinterException("No se pudo imprimir el comprobante", e);
        }
    }

    public void imprimirVoucherAnulacion(Pago p) throws TicketPrinterException, TicketException {
        if (p instanceof PagoCredito) {
            imprimirVoucherAnulacion((PagoCredito) p);
        }
    }

    public void imprimirVoucherAnulacion(PagoCredito pagoCredito) throws TicketPrinterException, TicketException {
        log.debug("imprimirVoucherAnulacion() - Imprimiendo Voucher de Anulación de PagoCredito");
        if (pagoCredito.getMedioPagoActivo().isTarjetaCredito()
                && !pagoCredito.getMedioPagoActivo().isTarjetaSukasa()
                && pagoCredito.isValidadoManual()) {
            return;
        }
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteVoucherAnulacion()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirVoucherAnulacion() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirVoucherAnulacion() - Error imprimiendo voucher de anulación: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo voucher de anulación: No se ha encontrado la plantilla.");
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String hora = (sDFH.format(cal.getTime()));
            String fecha = sD.format(cal.getTime());

            // Vouchers de Sukasa
            PrintVoucherAnulacion printVoucherAnu = null;
            //if (Sesion.isSukasa()) {
            printVoucherAnu = new PrintVoucherAnulacion(pagoCredito);
            //}

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pagoCredito", pagoCredito);
            map.put("printVoucherAnu", printVoucherAnu);
            map.put("empresa", Sesion.getEmpresa());
            map.put("tienda", Sesion.getTienda());
            map.put("cajero", Sesion.getUsuario());
            map.put("fecha", fecha);
            map.put("hora", hora);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_VOUCHER_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                if (i == 0) {
                    textoPie = "ORIGINAL - CLIENTE";
                    textoCabecera = "";
                } else {
                    textoPie = "COPIA";
                    textoCabecera = "";
                }
                generacionTicket(resource, map, imprAux, documentoImpreso);
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PAGO);
            documentosImpresos.add(documentoImpreso);
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo Voucher de Anulación: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo comprobante de anulación. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo Voucher de Anulación: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante de anulación ", e);
        }
    }

    //</editor-fold>
    public void imprimirVouchers(PagosTicket pagos, String documento, String procedencia) throws TicketPrinterException, TicketException {
        imprimirVouchers(pagos.getPagos(), documento, procedencia, pagos.isCompensacionAplicada());
    }

    private void imprimirVouchers(List<Pago> pagos, String documento, String procedencia, boolean hayCompensacion) throws TicketPrinterException, TicketException {
        listBonosSupermaxi = new ArrayList<>();
        for (Pago p : pagos) {
            if (p instanceof PagoBonoSuperMaxiNavidad) {
                listBonosSupermaxi.add(p);
            }
            if (!(p instanceof PagoBonoSuperMaxiNavidad)) {
                if (p.isPagoTarjeta() || p.isPagoGiftCard()) {
                    if (p instanceof PagoCredito) {
                        imprimirVoucher((PagoCredito) p, documento, procedencia, hayCompensacion);
                    } else if (p instanceof PagoGiftCard) { // Los recibos de guiftcard solo se imprimen para sukasa
                        imprimirVoucher((PagoGiftCard) p, documento, procedencia);
                    }
                }
            }
        }
        if (!listBonosSupermaxi.isEmpty()) {
            imprimirVoucherBonoSupermaxi(listBonosSupermaxi, documento, procedencia, hayCompensacion);
        }
    }

    public DocumentosImpresosBean imprimirVoucherAnulacionPinPad(PagoCredito pagoCredito) throws TicketPrinterException, TicketException {
        log.debug("imprimirVoucherAnulacion() - Imprimiendo Voucher anulación de PagoCredito");
        if (pagoCredito.getMedioPagoActivo().isTarjetaCredito()
                && !pagoCredito.getMedioPagoActivo().isTarjetaSukasa()
                && pagoCredito.isValidadoManual()) {
            return null;
        }
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            if (pagoCredito.getPinpadRespuesta() != null) {
                resource = leerEsquemaTicket(this.getClass().getResource("/esquemaVoucherPinPadAnulacion.xml"));
            } else {
                resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteVoucherAnulacion()));
            }
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirVoucherAnulacion() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirVoucherAnulacion() - Error imprimiendo voucher: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo voucher: No se ha encontrado la plantilla.");
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String hora = (sDFH.format(cal.getTime()));
            String fecha = sD.format(cal.getTime());
            String avisoLegal = VariablesAlm.getVariable(VariablesAlm.TICKET_AVISO_LEGAL_VOUCHER);
            LineaEnTicket lineas = new LineaEnTicket(avisoLegal);
            boolean tieneFirma = Variables.getVariableAsBoolean(Variables.SWITCH_TARJETAS_VOUCHER_FIRMA);
            BigDecimal montoMinimo = Variables.getVariableAsBigDecimal(Variables.SWITCH_TARJETAS_VOUCHER_FIRMA_MONTO_MIN);
            if (tieneFirma) {
                try {
                    if (montoMinimo.compareTo(pagoCredito.getUstedPaga()) >= 0) {
                        tieneFirma = false;
                    }
                } catch (Exception e) {
                    log.error("Error leyendo monto mínimo para la existencia de firmas. La sección no se mostrará", e);
                }
            }

            String transaccion = "";
            String cedulaPropietario = "";

            // Vouchers de Sukasa
            PrintVoucher printVoucher = new PrintVoucher(pagoCredito, pagoCredito.getTransaccion(), "", false);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pagoCredito", pagoCredito);
            map.put("printVoucher", printVoucher);
            map.put("empresa", Sesion.getEmpresa());
            map.put("tienda", Sesion.getTienda());
            map.put("cajero", Sesion.getUsuario());
            map.put("fecha", fecha);
            map.put("hora", hora);
            map.put("avisoLegal", lineas);
            map.put("documento", "" + transaccion);
            map.put("cedulaPropietario", cedulaPropietario);
            map.put("tieneFirma", tieneFirma);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_VOUCHER_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "COPIA";
                        textoCabecera = "";
                        this.original = false;
                    } else {
                        textoPie = "ORIGINAL";
                        textoCabecera = "";
                    }
                    generacionTicket(resource, map, IMPRESORA_COMPROBANTES, documentoImpreso);
                    this.original = true;
                    textoPie = "ORIGINAL";
                    textoCabecera = "";
                }
                generacionTicket(resource, map, IMPRESORA_COMPROBANTES, documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("No se pudo imprimir el comprobante");
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PAGO);
            documentosImpresos.add(documentoImpreso);
            return documentoImpreso;
        } catch (Exception e) {
            log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante", e);
        }
    }

    private void imprimirVoucher(PagoCredito pagoCredito, String documento, String procedencia, boolean hayCompensacion) throws TicketPrinterException, TicketException {
        log.debug("imprimirVoucher() - Imprimiendo Voucher de PagoCredito");
//comantar Para Imprecion Plan D Voucher Manual
//comentado para hacer pruebas Impresion Voucher para autorizacion manual
//        if (pagoCredito.getMedioPagoActivo().isTarjetaCredito() 
//                && !pagoCredito.getMedioPagoActivo().isTarjetaSukasa()  
//                && pagoCredito.isValidadoManual()){
//            return;
//        }
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            if (pagoCredito.getPinpadRespuesta() != null && !TicketService.VOUCHER_MANUAL.equals(pagoCredito.getMensajePromocional())) {
                resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteVoucherPinPad()));
            } else {
                resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteVoucher()));
            }
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirVoucher() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirVoucher() - Error imprimiendo voucher: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo voucher: No se ha encontrado la plantilla.");
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String hora = (sDFH.format(cal.getTime()));
            String fecha = sD.format(cal.getTime());
            String avisoLegal = VariablesAlm.getVariable(VariablesAlm.TICKET_AVISO_LEGAL_VOUCHER);
            LineaEnTicket lineas = new LineaEnTicket(avisoLegal);
            boolean tieneFirma = Variables.getVariableAsBoolean(Variables.SWITCH_TARJETAS_VOUCHER_FIRMA);
            BigDecimal montoMinimo = Variables.getVariableAsBigDecimal(Variables.SWITCH_TARJETAS_VOUCHER_FIRMA_MONTO_MIN);
            if (tieneFirma) {
                try {
                    if (montoMinimo.compareTo(pagoCredito.getUstedPaga()) >= 0) {
                        tieneFirma = false;
                    }
                } catch (Exception e) {
                    log.error("Error leyendo monto mínimo para la existencia de firmas. La sección no se mostrará", e);
                }
            }

            String transaccion = "";
            String cedulaPropietario = "";
            if (pagoCredito.getMedioPagoActivo().isTarjetaSukasa()) {
                transaccion = documento;
                if (!pagoCredito.getMedioPagoActivo().isBonoSuperMaxiNavidad() && !pagoCredito.getMedioPagoActivo().isCreditoFilial()) {
                    cedulaPropietario = (((PagoCreditoSK) pagoCredito).getPlastico() != null ? ((PagoCreditoSK) pagoCredito).getPlastico().getCedulaCliente() : ((TarjetaCreditoSK) ((PagoCreditoSK) pagoCredito).getTarjetaCredito()).getPlastico().getCedulaCliente());
                }
            } else {
                transaccion = "" + pagoCredito.getAuditoria();
            }

            // Vouchers de Sukasa
            PrintVoucher printVoucher = new PrintVoucher(pagoCredito, documento, procedencia, hayCompensacion);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pagoCredito", pagoCredito);
            map.put("printVoucher", printVoucher);
            map.put("empresa", Sesion.getEmpresa());
            map.put("tienda", Sesion.getTienda());
            map.put("cajero", Sesion.getUsuario());
            map.put("fecha", fecha);
            map.put("hora", hora);
            map.put("avisoLegal", lineas);
            map.put("documento", "" + transaccion);
            map.put("cedulaPropietario", cedulaPropietario);
            map.put("tieneFirma", tieneFirma);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_VOUCHER_CONFIG_IMPRESION);
            //int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            /*if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }*/
            if (impresoraTermica.equals("0")) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }

            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "COPIA";
                        textoCabecera = "";
                        this.original = false;
                        originalVoucher = false;
                        map.put("originalVoucher", originalVoucher);
                        map.put("textoPie", textoPie);
                    } else {
                        textoPie = "ORIGINAL";
                        textoCabecera = "";
                        originalVoucher = true;
                        map.put("originalVoucher", originalVoucher);
                        map.put("textoPie", textoPie);
                    }
                    generacionTicket(resource, map, impresora, documentoImpreso);
                    this.original = true;
                    textoPie = "ORIGINAL";
                    textoCabecera = "";
                    originalVoucher = true;
                    map.put("originalVoucher", originalVoucher);
                    map.put("textoPie", textoPie);
                }
                generacionTicket(resource, map, impresora, documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("No se pudo imprimir el comprobante");
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PAGO);
            documentosImpresos.add(documentoImpreso);
        } catch (Exception e) {
            log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante", e);
        }
    }

    /**
     * Impresión del voucher de una GiftCard
     *
     * @param pagoGC
     * @param documento
     * @throws TicketPrinterException
     * @throws TicketException
     */
    private void imprimirVoucher(PagoGiftCard pagoGC, String documento, String procedencia) throws TicketPrinterException, TicketException {
        log.debug("imprimirVoucher() - Imprimiendo Voucher de PagoGiftCard");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteVoucher()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirVoucher() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirVoucher() - Error imprimiendo voucher: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo voucher: No se ha encontrado la plantilla.");
        }
        try {

            // Vouchers de Sukasa
            PrintVoucher printVoucher = null;
            //if (Sesion.isSukasa()) {
            printVoucher = new PrintVoucher(pagoGC, documento, procedencia);
            //}

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pagoCredito", pagoGC);
            map.put("printVoucher", printVoucher);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_VOUCHER_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            /*if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);

            }*/
            if (impresoraTermica.equals("0")) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                if (i == 0) {
                    textoPie = "ORIGINAL - CLIENTE";
                    textoCabecera = "";
                } else {
                    textoPie = "COPIA";
                    textoCabecera = "";
                }
                generacionTicket(resource, map, impresora, documentoImpreso);
            }
            textoPie = "";
            textoCabecera = "";
            try {
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_GIFTCARD_P);
                DocumentosService.crearDocumentoGiftCardPago(pagoGC, documentoImpreso, DocumentosBean.GIFTCARD_P);
            } catch (DocumentoException e) {
                log.error("Error guardando voucher de pago de giftcard: " + e.getMessage(), e);
            }
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PAGO);
            documentosImpresos.add(documentoImpreso);
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo Voucher de pago con Guif Card: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante de pago con Guif Card", e);
        }
    }

    public void imprimirRematePlanNovio(List<ArticuloPlanNovio> articulos, PlanNovioOBJ planNovio) throws TicketPrinterException, TicketException {
        log.debug("imprimirRematePlanNovio() - Rematando Plan Novio");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaListadoArticulosReserva()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirRematePlanNovio() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirRematePlanNovio() - resource es null");
        } else {

            try {

                // La lista de artículos solo se utiliza al añadir artículos al plan
                // Nos puede permitir separar los artículos nuevos de los que ya estan en el plan.
                if (articulos != null && !articulos.isEmpty()) {
                    for (ArticuloPlanNovio art : articulos) {
                        planNovio.getPlan().getListaArticulos().add(art);
                    }
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("articulos", articulos);
                map.put("reserva", planNovio.getCodPlanAsString());
                map.put("fecha", new Fecha());

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARICULOS_PLAN_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, null);
                }
                textoPie = "";
                textoCabecera = "";
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    public void imprimirFacturaCompraConAbonosPlanNovio(List<ArticuloPlanNovio> articulos, PlanNovioOBJ planNovio) throws TicketPrinterException, TicketException {
        log.debug("imprimirFacturaCompraConAbonosPlanNovio() - Facturando compras con abono en Plan Novio");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaListadoArticulosReserva()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirFacturaCompraConAbonosPlanNovio() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirFacturaCompraConAbonosPlanNovio() - resource es null");
        } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("articulos", articulos);
                map.put("reserva", planNovio.getCodPlanAsString());
                map.put("fecha", new Fecha());

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARTICULOS_RESERVA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, null);
                }
                textoPie = "";
                textoCabecera = "";
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    public boolean imprimirCotizacion(TicketS ticket, boolean pantalla) throws CotizacionException, TicketPrinterException {
        log.debug("imprimirCotizacion() - Imprimiendo cotización");
        boolean ventanaAceptada = false;
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") && !pantalla) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaCotizacion()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirCotizacion() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirCotizacion() - resource es null");
        } else {
            try {

                // Construimos el objeto impresión de ticket (para sukasa)
                PrintTicket printTicket = null;
                if (Sesion.isSukasa()) {
                    printTicket = new PrintTicket(ticket);
                }
                LineaEnTicket mensaje = new LineaEnTicket(Variables.getVariable(Variables.MENSAJE_COTIZACION_XML), false, true);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ticket", ticket);
                map.put("printTicket", printTicket);
                map.put("fecha", new Fecha());
                map.put("mensaje", mensaje);
                map.put("telefono", Sesion.getTienda().getAlmacen().getTelefonos());
                map.put("tiendaDescripcion", Sesion.getTienda().getAlmacen().getDesalm());
                textoPie = "ORIGINAL - CLIENTE";

                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                if (impresora == IMPRESORA_PANTALLA) {
                    ventanaAceptada = generacionTicket(resource, map, impresora, documentoImpreso);
                } else {
                    ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_COTIZACION_CONFIG_IMPRESION);
                    int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                    if (imprAux == IMPRESORA_TICKET) {
                        seleccionarImpresora(INICIO);
                        seleccionarInterlineado(PULGADAS_INTERLINEADO);
                    }
                    for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                        if (i == 0) {
                            textoPie = "ORIGINAL - CLIENTE";
                            textoCabecera = "";
                        } else {
                            textoPie = "COPIA";
                            textoCabecera = "COPIA";
                        }
                        ventanaAceptada = generacionTicket(resource, map, imprAux, documentoImpreso);
                    }
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_COT);
                documentosImpresos.add(documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo cotizacion: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo cotizacion. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirCotizacion() - No se pudo imprimir la cotización", e);
                throw new CotizacionException("No se pudo imprimir la cotizacion", e);
            }
        }
        return ventanaAceptada;
    }

    public void imprimirListaArticulosPlan(PlanNovioOBJ plan, List<ArticuloPlanNovio> listaArt) throws TicketPrinterException, TicketException {
        log.debug("imprimirListaArticulosPlan() - Imprimiendo Artículos de Lista de Plan Novio");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaListadoArticulosPlan()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirListaArticulosPlan() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirListaArticulosPlan() - resource es null");
            ArticuloPlanNovio art;
        } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("titulo", plan.getPlan().getTitulo());
                map.put("articulos", plan.getPlan().getListaArticulos());
                map.put("reserva", plan.getCodPlanAsString());
                map.put("fecha", new Fecha());

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARICULOS_PLAN_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PLAN_NOVIO_LIQU);
                documentosImpresos.add(documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    public void imprimirContratoPlan(PlanNovioOBJ plan) throws TicketPrinterException, TicketException {
        log.debug("imprimirContratoPlan() - Imprimiendo Contrato Plan Novio");
        String resource = null;
        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaContratoPlan()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirContratoPlan() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirContratoPlan() - resource es null");
            ArticuloPlanNovio art;
            //art.getInvitadoPlanNovio().getNombre()
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String hora = (sDFH.format(cal.getTime()));
                String fecha = sD.format(cal.getTime());

                cal.setTime(plan.getPlan().getFechaHoraBoda());
                String fechaBoda = sD.format(cal.getTime()) + " - " + (sDFH.format(cal.getTime()));

                Map<String, Object> map = new HashMap<String, Object>();

                String consumoMinimo = Variables.getVariable(Variables.PLANNOVIOS_MINIMO_CONSUMO_PLAN);

                map.put("titulo", plan.getPlan().getTitulo());
                map.put("plan", plan.getPlan());
                map.put("reserva", plan.getCodPlanAsString());
                map.put("fecha", new Fecha());
                map.put("empresa", Sesion.getEmpresa());
                map.put("tienda", Sesion.getTienda());
                map.put("cajero", Sesion.getUsuario().getDesUsuario());
                map.put("fecha", fecha);
                map.put("hora", hora);
                map.put("fechaBoda", fechaBoda);
                map.put("consumoMinimo", consumoMinimo);
                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_CONTRATO_PLAN_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }

                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PLAN_NOVIO);
                documentosImpresos.add(documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    public void imprimirContratoExtensionGarantia(TicketS ticket) throws TicketPrinterException, TicketException {
        log.debug("imprimirContratoExtensionGarantia() - Imprimiendo Extensión de Garantía");
        String resource = null;

        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaExtensionGarantia()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirContratoExtensionGarantia() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirContratoExtensionGarantia() - resource es null");
        } else {
            try {
                if (ticket.getLineas() != null && ticket.getLineas().getLineas() != null) {
                    BigDecimal importe = BigDecimal.ZERO;

                    List<LineaTicket> lineasGarantia = new ArrayList<LineaTicket>();
                    for (LineaTicket lin : ticket.getLineas().getLineas()) {
                        if (lin.getReferenciaGarantia() != null) {
                            String tickOrigen;
                            if (lin.getReferenciaGarantia().getTicketOrigen() != null) {
                                tickOrigen = lin.getReferenciaGarantia().getTicketOrigen().getIdFactura();
                            } else {
                                tickOrigen = ticket.getIdFactura();
                            }
                            lin.getReferenciaGarantia().setRefTicketOrigen(tickOrigen);
                            lineasGarantia.add(lin);
                            importe = importe.add(lin.getImporteTotalFinalPagado());
                        }
                    }

                    if (!lineasGarantia.isEmpty()) {

                        String fecha = ticket.getFecha().getStringHora();
                        String textoExtensionGarantia = Variables.getVariable(Variables.TICKET_MENSAJE_EXTENSION_GARANTIA);

                        textoExtensionGarantia = textoExtensionGarantia.replaceFirst("#P", importe.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        textoExtensionGarantia = textoExtensionGarantia.replaceFirst("#M", Variables.getVariable(Variables.GARANTIA_EXT_DURACION_MESES));

                        LineaEnTicket lineasExtensionGarantia = new LineaEnTicket(textoExtensionGarantia, false, true);
                        // leemos el id de la garantia
                        Long contadorGarantia = ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_GARANTIA);
                        String idTienda = ((Sesion.getTienda().getSriTienda().getTienda().getSriTienda().getCodalminterno() != null)
                                ? Sesion.getTienda().getSriTienda().getTienda().getSriTienda().getCodalminterno()
                                : "");
                        String idGarantia = idTienda + "-" + contadorGarantia;

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("ticket", ticket);
                        map.put("fecha", fecha);
                        map.put("lineas", lineasGarantia);
                        map.put("lineasExtensionGarantia", lineasExtensionGarantia);
                        map.put("idGarantia", idGarantia);

                        ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_EXTENSION_GARANTIA_CONFIG_IMPRESION);
                        int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : IMPRESORA_COMPROBANTES);
                        /*if (imprAux == IMPRESORA_TICKET) {
                                seleccionarImpresora(INICIO);
                                seleccionarInterlineado(PULGADAS_INTERLINEADO);
                            }*/
                        if (impresoraTermica.equals("0")) {
                            seleccionarImpresora(INICIO);
                            seleccionarInterlineado(PULGADAS_INTERLINEADO);
                        }
                        DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                        try {
                            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                                if (i == 0) {
                                    textoPie = "ORIGINAL - CLIENTE";
                                    textoCabecera = "";
                                } else {
                                    textoPie = "COPIA";
                                    textoCabecera = "COPIA";
                                }
                                generacionTicket(resource, map, imprAux, documentoImpreso);
                            }
                        } catch (NoSuchPortException e) {
                            log.error("Error imprimiendo la extensión de la garantia: " + e.getMessage());
                            log.error("Error imprimiendo Extensión de Garantia. No existe la impresora en el puerto indicado.");
                        } catch (Exception e) {
                            log.error("imprimirArticulosReserva() - No se pudo imprimir la extensión de la garantia", e);
                            log.error("No se pudo imprimir la extensión de la garantia", e);
                        }
                        textoPie = "";
                        textoCabecera = "";
                        documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_EXTGAR);
                        documentosImpresos.add(documentoImpreso);
                    }
                }
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir la extensión de la garantia", e);
                throw new TicketException("No se pudo imprimir la extensión de la garantia", e);
            }
        }
    }

    public void imprimirComprobantePagoCreditoDirecto(TicketS ticket, CreditoDirectoBean creditoDirecto, AbonoCreditoBean abonoCredito) throws TicketException, TicketPrinterException {
        log.debug("imprimirComprobantePagoCreditoDirecto() - Imprimiendo comprobante de pago con crédito directo");
        String resource = null;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobantePagoCreditoDirecto()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobantePagoCreditoDirecto() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirComprobantePagoCreditoDirecto() - resource es null");
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String hora = (sDFH.format(cal.getTime()));
                String fecha = sD.format(cal.getTime());

                PrintPagoCredito printPC = null;
                if (Sesion.isSukasa()) {
                    printPC = new PrintPagoCredito(ticket, creditoDirecto, abonoCredito);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ticket", ticket);
                map.put("printPC", printPC);
                map.put("creditoDirecto", creditoDirecto);
                map.put("fecha", new Fecha());
                map.put("empresa", Sesion.getEmpresa());
                map.put("tienda", Sesion.getTienda());
                map.put("cajero", Sesion.getUsuario().getDesUsuario());
                map.put("fecha", fecha);
                map.put("hora", hora);
                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_PAGO_CREDITO_DIRECTO_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_CREDITO);
                documentosImpresos.add(documentoImpreso);
                imprimirVouchers(ticket.getPagos(), abonoCredito.getIdAbonoCredito(), "PAGO CRED DIRECTO");

                //   imprimirTextoAdicionalCreditoDirecto(ticket.getPagos().getPagos(), ticket.getCliente().getIdentificacion(), ticket.getCliente().getNombreYApellidos(), true);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    public void imprimirComprobantePagoLetraCambio(TicketS ticket, LetraBean letra, LetraCuotaBean letraCuota) throws TicketException, TicketPrinterException {
        log.debug("imprimirComprobantePagoLetraCambio() - Imprimiendo comprobante de pago con letra de cambio");
        String resource = null;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobantePagoLetra()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobantePagoLetraCambio() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirComprobantePagoLetraCambio() - resource es null");
            ArticuloPlanNovio art;
            //art.getInvitadoPlanNovio().getNombre()
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String hora = (sDFH.format(cal.getTime()));
                String fecha = sD.format(cal.getTime());
                String fechaVencimiento = sD.format(letra.getFecha().getDate());

                PrintPagoLetra printPL = null;
                if (Sesion.isSukasa()) {
                    printPL = new PrintPagoLetra(ticket, letra, letraCuota);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ticket", ticket);
                map.put("printPL", printPL);
                map.put("letra", letra);
                map.put("letraCuota", letraCuota);
                map.put("fecha", new Fecha());
                map.put("empresa", Sesion.getEmpresa());
                map.put("tienda", Sesion.getTienda());
                map.put("cajero", Sesion.getUsuario().getDesUsuario());
                map.put("fecha", fecha);
                map.put("fechaVencimiento", fechaVencimiento);
                map.put("hora", hora);

                ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_PAGO_LETRA_CONFIG_IMPRESION);
                int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
                if (imprAux == IMPRESORA_TICKET) {
                    seleccionarImpresora(INICIO);
                    seleccionarInterlineado(PULGADAS_INTERLINEADO);
                }
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "ORIGINAL - CLIENTE";
                        textoCabecera = "";
                    } else {
                        textoPie = "COPIA";
                        textoCabecera = "COPIA";
                    }
                    generacionTicket(resource, map, imprAux, documentoImpreso);
                }
                textoPie = "";
                textoCabecera = "";
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_LETRA);
                documentosImpresos.add(documentoImpreso);
                imprimirVouchers(ticket.getPagos(), letraCuota.getIdLetraCuota(letra.getCodAlmacen()), "PAGO CRED TEMPORAL");
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo ticket: " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo ticket. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirArticulosReserva() - No se pudo imprimir el ticket", e);
                throw new TicketException("No se pudo imprimir el ticket", e);
            }
        }
    }

    /**
     * Impresión de comprobante de anulación
     *
     * @param idDocumento
     * @param fechaDocumento
     * @param tipoDocumento
     * @param pagos
     * @param totalPagado
     * @throws TicketPrinterException
     * @throws TicketException
     */
    public void imprimirComprobanteAnulacion(String idDocumento, Fecha fechaDocumento, List<Pago> pagos, String totalPagado, String tipoDocumento) throws TicketPrinterException, TicketException {
        log.debug("imprimirComprobanteAnulacion() - Imprimiendo comprobante de anulación");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sDHora = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaComprobanteAnulacion()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobanteAnulacion() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirComprobanteAnulacion() - Error imprimiendo anulación: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo anulación: No se ha encontrado la plantilla.");
        }
        try {
            // fecha y hora
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String hora = (sDFH.format(cal.getTime()));
            String fecha = sD.format(cal.getTime());

            LineaEnTicket lineas = new LineaEnTicket("RECIBO DE " + tipoDocumento + " ANULADA");

            // fecha de Transacción
            cal.setTime(fechaDocumento.getDate());
            Sesion.getTienda().getCodFormato();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("autorizador", Sesion.getAutorizadorAnulacion().getDesUsuario()); // Autorizador de la operación de anulación       
            map.put("empresa", Sesion.getEmpresa());
            map.put("idDocumento", idDocumento);
            map.put("tipoDocumento", lineas);
            map.put("totalPagado", totalPagado);
            map.put("tienda", Sesion.getTienda());
            map.put("cajero", Sesion.getUsuario());
            map.put("fecha", fecha);
            map.put("hora", hora);
            map.put("pagos", pagos);
            map.put("cajero", Sesion.getUsuario().getDesUsuario());

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ANULACION_CONFIG_IMPRESION);
            int imprAux = (impresora == IMPRESORA_PANTALLA ? IMPRESORA_PANTALLA : variableAsConfigImpresion.getImpresora());
            if (imprAux == IMPRESORA_TICKET) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }
            for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                generacionTicket(resource, map, imprAux, null);
            }
        } catch (NoSuchPortException e) {
            log.error("Error imprimiendo comprobante: " + e.getMessage(), e);
            throw new TicketPrinterException("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
        } catch (Exception e) {
            log.error("Error imprimiendo comprobante: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante de anulación", e);
        }
    }

    public void reimpresionCotizacion(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_COTIZACION_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                reimpresion(docus.getImpreso(), numeroImpresora);
            }
        } catch (Exception e) {
            log.error("reimpresionCotizacion() - Error reimprimiendo Cotizacion: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la Cotización", e);
        }
    }

    /**
     * @author Origen
     * @modified Gabriel Simbania
     * @description Impresión Por separado de facturas cupotes etc
     * @param documento
     * @param pantalla
     * @param imprimirFactura
     * @param imprimirVoucher
     * @param cupon
     * @param billeton
     * @param observacion
     * @param imprimirExtension
     * @return
     * @throws DocumentoException
     */
    public String reimpresionFactura(DocumentosBean documento, boolean pantalla, boolean imprimirFactura, boolean imprimirVoucher,
            boolean cupon, boolean billeton, String observacion, boolean imprimirExtension) throws DocumentoException {

        String mensajerespuesta = MENSAJE_RESPUESTA;
        int numeroImpresora;
        int numeroImpresoraComprobantes;
        int numerofacturas = 0;
        int numerobilletones = 0;
        int numerocupon = 0;
        int numerovoucher = 0;

        Fecha fecha = new Fecha(documento.getFecha().getDate());
        Fecha ahora = new Fecha();
        fecha.sumaMinutos(Variables.TIEMPO_PASADO);
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
            numeroImpresoraComprobantes = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_FACTURA_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
            numeroImpresoraComprobantes = IMPRESORA_COMPROBANTES;
        }
        try {
            seleccionarImpresora(INICIO);
            seleccionarInterlineado(PULGADAS_INTERLINEADO);
        } catch (TicketPrinterException ex) {
            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            int contador = 0;
            String impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0" : Sesion.getDatosConfiguracion().getImpresoraTermica();
//            if (documento.getObservaciones() == null) {
            textoReimpresionCabecera = "REIMPRESIÓN";
            textoReimpresionDetalle = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
//            } else {
//                textoReimpresionCabecera = documento.getObservaciones().concat("COPIA SIN DERECHO A CRÉDITO TRIBUTARIO");
//            }
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                boolean respuestatarjeton = false;
                esCopia(documento, numeroImpresora);
                // Si la fecha del ticket + minutos pasados, es posterior a la fecha actual, solo se imprimen los documentos de tipo FACTURA
                if (docus.isTipoDocumentoFactura() && imprimirFactura) {
                    numerofacturas++;
                    textoPie = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
                    reimpresion(docus.getImpreso(), numeroImpresora);

                    if (impresoraTermica.equals("2")) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (docus.isExtensionGarantia() && imprimirExtension) {
                    reimpresion(docus.getImpreso(), numeroImpresora);

                    if (impresoraTermica.equals("2")) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } else {
                    if (docus.isTipoFacPago() && imprimirVoucher) {
                        numerovoucher++;
                        this.original = false;
                        //textoPie="ORIGINAL - CLIENTE";
                        //textoCabecera="";
                        reimpresion(docus.getImpreso(), IMPRESORA_TICKET);
                        //textoPie="--- COPIA ---";
                        //textoCabecera="--- COPIA ---";
                        this.original = true;
                        reimpresion(docus.getImpreso(), IMPRESORA_TICKET);
                    } else {
//                        if(!ahora.despues(fecha) && !docus.isTipoFacCupon() && !docus.isTipoFacPago()){
//                            reimpresion(docus.getImpreso(), numeroImpresoraComprobantes);
//                        } else {
                        if ((docus.isTipoFacCupon() && cupon) || (docus.isTipoFacCupon() && billeton)) {
                            contador++;
                            if (contador == 1) {
                                respuestatarjeton = analisisTarjetonCupon(docus);
                            } else {
                                respuestatarjeton = false;
                            }
                            if (!respuestatarjeton) {
                                if (cupon) {
                                    numerocupon++;
                                    reimpresion(docus.getImpreso(), numeroImpresora);
                                }
                            } else {
                                if (billeton) {
                                    numerobilletones++;
                                    reimpresion(docus.getImpreso(), numeroImpresora);
                                    billeton = false;
                                }
                            }
                        }

//                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("reimpresionFactura() - Error reimprimiendo Factura: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la factura", e);
        }
        LogOperaciones logImpresiones = null;

        if (imprimirFactura) {
            if (numerofacturas == 0) {
                PrintServices ts = PrintServices.getInstance();

                try {
                    TicketS ticket = reconstruccionTicketParaReimpresion(documento);
                    ts.seleccionarInterlineado(PULGADAS_INTERLINEADO);
                    ts.imprimirTicket(ticket, false, IMPRESORA_TICKET);

                    DocumentosBean doc = new DocumentosBean();
                    doc.setUidDocumento(documento.getUidDocumento());
                    doc.setImpresos(ts.getDocumentosImpresos());
                    for (DocumentosImpresosBean docImpresosBean : ts.getDocumentosImpresos()) {
                        docImpresosBean.setOrigen(DocumentosImpresosBean.ORIGEN_REIMPRESION);
                    }
                    DocumentosService.crearDocumentoImpreso(doc, DocumentosImpresosBean.TIPO_FACTURA);
                    ts.limpiarListaDocumentos();
                    //Se desactiva la promocion manual en caso de haber existido
                    PromocionTipoDtoManualTotal.desActivar();
                } catch (Exception ex) {
                    //Se desactiva la promocion manual en caso de haber existido
                    PromocionTipoDtoManualTotal.desActivar();
                    log.error("Error al reconstuir la impresion", ex);
                    mensajerespuesta = " Error en la reimpresi\u00F3n, consulte su documento en el SRI ";
                }
            } else {
                try {
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  FACTURA");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                } catch (LogException ex) {
                    java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (cupon) {
            if (numerocupon == 0) {
                mensajerespuesta = mensajerespuesta + " (Cupones)";
            } else {
                try {
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  CUPONES");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                } catch (LogException ex) {
                    java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (billeton) {
            if (numerobilletones == 0) {
                mensajerespuesta = mensajerespuesta + " (Billetones)";
            } else {
                try {
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  BILLETONES");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                } catch (LogException ex) {
                    java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (imprimirVoucher) {
            if (numerovoucher == 0) {

                try {
                    PrintServices ts = PrintServices.getInstance();
                    //G.S. Reconstruye el Ticket en caso de no existir
                    TicketsAlm ticketsAlm = TicketService.consultarTicket(Long.parseLong(documento.getIdDocumento()), documento.getCodCaja(), documento.getCodAlmacen());
                    TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(ticketsAlm.getXMLTicket()));
                    XMLDocumentNode node = ticketOrigen.getPagos();
                    TicketS ticket = new TicketS(false);
                    ticket.setUid_ticket(ticketsAlm.getUidTicket());

                    PagosTicket pagosTicket = TicketXMLServices.construirPagoByTagPagoCredito(node, ticket);
                    if (!pagosTicket.getPagos().isEmpty()) {
                        ts.imprimirVouchers(pagosTicket, ticketOrigen.getIdFactura(), "VENTA");
                        DocumentosBean doc = new DocumentosBean();
                        doc.setUidDocumento(documento.getUidDocumento());
                        doc.setImpresos(ts.getDocumentosImpresos());
                        for (DocumentosImpresosBean docImpresosBean : ts.getDocumentosImpresos()) {
                            docImpresosBean.setOrigen(DocumentosImpresosBean.ORIGEN_REIMPRESION);
                        }
                        DocumentosService.crearDocumentoImpreso(doc, DocumentosImpresosBean.TIPO_PAGO);
                        ts.limpiarListaDocumentos();
                    } else {
                        mensajerespuesta = mensajerespuesta + " (Voucher)";
                    }
                } catch (Exception ex) {
                    log.error("Error al reimprimir el voucher " + ex.getMessage(), ex);
                    mensajerespuesta = mensajerespuesta + " (Voucher)";
                }

            } else {
                try {
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  VOUCHER");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                } catch (LogException ex) {
                    java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mensajerespuesta;
    }

    //Realiza la inserción de un movimiento cuando se va a realizar una reimpresión
    public void insertarLog(LogOperaciones logImpresiones) throws LogException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ServicioLogAcceso.crearAccesoLog(logImpresiones, em);
        em.getTransaction().commit();
    }

    /**
     * @author Gabriel Simbania
     * @description Metodo que realiza el analisis de un cupon para saber si es
     * un sukupon
     * @param cupon
     * @return
     * @throws SQLException
     * @throws TicketException
     */
    public boolean analisisTarjetonCupon(DocumentosImpresosBean cupon) throws SQLException, TicketException {
        boolean billeton = false;
        Connection conn = new Connection();
        try {
            DocumentosBean documento = DocumentosService.consultarDocumento(cupon.getUidDocumento());

            TicketsAlm ticketAlm = TicketsDao.consultarTicket(Long.parseLong(documento.getIdDocumento()), documento.getCodCaja(), documento.getCodAlmacen());

            conn.abrirConexion(Database.getConnection());
            Cupon resultado = CuponesDao.consultarCunponTarjet(conn, documento.getCodAlmacen(), documento.getCodCaja(), documento.getCodCliente(), ticketAlm.getUidTicket());
            if (resultado != null) {
                //es billeton
                billeton = true;
            }
            conn.close();
        } catch (DocumentoException ex) {
            conn.close();
            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            conn.close();
            java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billeton;
    }

    //Edición para guardar LogOperación de la operación realizada con una observación 
    public void reimpresionNotaCredito(DocumentosBean documento, boolean pantalla, String observacion) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_DEVOLUCION_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            int contadorNotaCredito = 0;
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                contadorNotaCredito++;
                esCopia(documento, numeroImpresora);
                textoPie = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
                textoReimpresionCabecera = "REIMPRESIÓN";
                textoReimpresionDetalle = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
                textoCabecera = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
                reimpresion(docus.getImpreso(), numeroImpresora);

            }
            if (contadorNotaCredito == 1) {
                LogOperaciones logImpresiones = null;
                logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getPrimeraFirmaNUsuario());
                logImpresiones.setReferencia(documento.getNumTransaccion());
                logImpresiones.setProcesado('N');
                if (pantalla) {
                    logImpresiones.setCodOperacion("VISUALIZAR  NOTA DE CREDITO");
                } else {
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  NOTA DE CREDITO");
                }
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            }
        } catch (Exception e) {
            log.error("reimpresionNotaCredito() - Error reimprimiendo Nota de Crédito: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la nota de crédito", e);
        }
    }

    /**
     * Metodo exclusivo para imprimir las N/C de otro local
     *
     * @param documento
     * @param pantalla
     * @param observacion
     * @throws DocumentoException
     */
    public void impresionNotaCreditoDesdeOtroLocal(DocumentosBean documento, boolean pantalla, String observacion) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_DEVOLUCION_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {

            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                //esCopia(documento, numeroImpresora);
                textoPie = "ORIGINAL - CLIENTE";
                //textoCabecera = "COPIA SIN DERECHO A CRÉDITO TRIBUTARIO";
                reimpresion(docus.getImpreso(), numeroImpresora);

            }

        } catch (Exception e) {
            log.error("impresionNotaCreditoDesdeOtroLocal() - Error imprimiendo Nota de Crédito de otro local: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la nota de crédito de otro local", e);
        }
    }

    /**
     * Edición para guardar LogOperación de la operación realizada
     *
     * @param documento documento para reimprimir
     * @param pantalla si va a ser visualizado por pantalla
     * @param observacion observacion por lo que fue reimpreso
     * @param imprimirVoucher bandera para saber si se reimprime el voucher
     * @throws DocumentoException
     */
    public void reimpresionGiftCard(DocumentosBean documento, boolean pantalla, String observacion, boolean imprimirVoucher) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_GIFTCARD_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            int contadorgistCard = 0;
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                contadorgistCard++;
                if (!DocumentosImpresosBean.TIPO_PAGO.equals(docus.getTipoImpreso())
                        || (DocumentosImpresosBean.TIPO_PAGO.equals(docus.getTipoImpreso()) && imprimirVoucher)) {
                    esCopia(documento, numeroImpresora);
                    reimpresion(docus.getImpreso(), numeroImpresora);
                }
                if (contadorgistCard == 1) {
                    LogOperaciones logImpresiones = null;
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  GIFTCARD");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                }
            }
        } catch (Exception e) {
            log.error("reimpresionGiftCard() - Error reimprimiendo GiftCard: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la GiftCard", e);
        }
    }

    //Edición para guardar LogOperación de la operación realizada 
    public void reimpresionLetraAbono(DocumentosBean documento, boolean pantalla, String observacion) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_PAGO_LETRA_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            int letrasAbono = 0;
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                esCopia(documento, numeroImpresora);
                reimpresion(docus.getImpreso(), numeroImpresora);
                letrasAbono++;
                if (letrasAbono == 1) {
                    LogOperaciones logImpresiones = null;
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  LETRA ABONO");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                }
            }
        } catch (Exception e) {
            log.error("reimpresionLetraAbono() - Error reimprimiendo Ticket: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir el Ticket", e);
        }
    }

    //Edición para guardar LogOperación de la operación realizada 
    public void reimpresionCreditoAbono(DocumentosBean documento, boolean pantalla, String observacion) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_PAGO_CREDITO_DIRECTO_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            int contadorCreditoAbono = 0;
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                contadorCreditoAbono++;
                esCopia(documento, numeroImpresora);
                reimpresion(docus.getImpreso(), numeroImpresora);
                if (contadorCreditoAbono > 0) {
                    LogOperaciones logImpresiones = null;
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION  CREDITO ABONO");
                    logImpresiones.setObservaciones(observacion);
                    insertarLog(logImpresiones);
                }
            }
        } catch (Exception e) {
            log.error("reimpresionCreditoAbono() - Error reimprimiendo Ticket: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir el Ticket", e);
        }
    }

    public void reimpresionReservacion(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_RESERVACION_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }

        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                esCopia(documento, numeroImpresora);
                reimpresion(docus.getImpreso(), numeroImpresora);
            }
        } catch (Exception e) {
            log.error("reimpresionReservacion() - Error reimprimiendo Reservación: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la Reservación.", e);
        }
    }

    public void reimpresionLiquidaRE(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARTICULOS_RESERVA_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                esCopia(documento, numeroImpresora);
                reimpresion(docus.getImpreso(), numeroImpresora);
            }
        } catch (Exception e) {
            log.error("reimpresionLiquidaRE() - Error reimprimiendo Liquidación: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la Liquidación.", e);
        }
    }

    public void reimpresionLiquidaPN(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ARICULOS_PLAN_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                esCopia(documento, numeroImpresora);
                reimpresion(docus.getImpreso(), numeroImpresora);
            }
        } catch (Exception e) {
            log.error("reimpresionLiquidaPN() - Error reimprimiendo Liquidación: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir la Liquidación.", e);
        }
    }

    public void reimpresionBonoReserva(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_BONO_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            int contadorCreditoAbono = 0;
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                contadorCreditoAbono++;
                esCopia(documento, numeroImpresora);
                textoCabecera = "";
                reimpresion(docus.getImpreso(), numeroImpresora);
                if (contadorCreditoAbono > 0) {
                    LogOperaciones logImpresiones = null;
                    logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                    logImpresiones.setFechaHora(new Date());
                    logImpresiones.setUsuario(Sesion.getPrimeraFirmaNUsuario());
                    logImpresiones.setReferencia(documento.getNumTransaccion());
                    logImpresiones.setProcesado('N');
                    if (Sesion.getNumeroAutorizador() != null) {
                        logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                    } else {
                        logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                    }
                    logImpresiones.setCodOperacion("REIMPRESION BONO EFECTIVO");
                    insertarLog(logImpresiones);
                }
            }
        } catch (Exception e) {
            log.error("reimpresionBonoReserva() - Error reimprimiendo Bono Reservación: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir el Bono de Reservación.", e);
        }
    }

    /**
     * Reimpresión de los comprobantess de abono
     *
     * @param documento documento para reimprimir
     * @param pantalla si va a ser visualizado por pantalla
     * @param imprimirVoucher bandera para saber si se reimprime el voucher
     * @throws DocumentoException
     */
    public void reimpresionComprobanteAbono(DocumentosBean documento, boolean pantalla, boolean imprimirVoucher) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_ABONO_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                if (!DocumentosImpresosBean.TIPO_PAGO.equals(docus.getTipoImpreso())
                        || (DocumentosImpresosBean.TIPO_PAGO.equals(docus.getTipoImpreso()) && imprimirVoucher)) {
                    esCopia(documento, numeroImpresora);
                    reimpresion(docus.getImpreso(), numeroImpresora);
                }
            }
        } catch (Exception e) {
            log.error("reimpresionComprobanteAbono() - Error reimprimiendo Comprobante Abono: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir el Comprobante de Abono.", e);
        }
    }

    private boolean esCopia(DocumentosBean documento, int numeroImpresora) {
        Fecha fecha = new Fecha(documento.getFecha().getDate());
        Fecha ahora = new Fecha();
        fecha.sumaMinutos(Variables.TIEMPO_PASADO);
        boolean ret;
        //if(ahora.despues(fecha)){
        textoPie = "--- COPIA ---";
        textoCabecera = "--- COPIA ---";
        ret = true;
        //} 
        //else {
        //    textoPie="ORIGINAL - CLIENTE";
        //    textoCabecera="";
        //    ret = false;
        //}
        if (documento.getEstado().equals("A")) {
            textoPie = "ANULADO";
            textoCabecera = "ANULADO";
            textoAnulada = "ANULADO";
        }
        return ret;
    }

    public void reimprimirDocuCliente(DocumentosBean documento, boolean pantalla) throws DocumentoException {
        int numeroImpresora;
        if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA") || pantalla) {
            numeroImpresora = IMPRESORA_PANTALLA;
        } else {
            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_FACTURA_CONFIG_IMPRESION);
            numeroImpresora = variableAsConfigImpresion.getImpresora();
        }
        if (documento.getEstado().equals("A")) {
            textoPie = "ANULADO";
            textoCabecera = "ANULADO";
        }
        try {
            for (DocumentosImpresosBean docus : documento.getImpresos()) {
                reimpresion(docus.getImpreso(), numeroImpresora);
            }
        } catch (Exception e) {
            log.error("Error reimprimiendo Documento: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir los documentos", e);
        }
    }

    /**
     * Método para imprimir un xml (en formato bytes) pasado como parámetro
     *
     * @param xml El xml a imprimir
     * @param numeroImpresora el numéro de impresora donde se imprimirá (0
     * impresora por pantalla)
     * @throws TicketPrinterException
     * @throws UnsupportedEncodingException
     */
    private void reimpresion(byte[] xml, int numeroImpresora) throws TicketPrinterException, UnsupportedEncodingException {
        //Numero de impresora siempre a 1 para reimpresión
        if (!Sesion.getDatosConfiguracion().isModoDesarrollo() && numeroImpresora != IMPRESORA_PANTALLA) {
            numeroImpresora = 1;
        }
        Sesion.putTiempoEjecucion("inicioProcesoImpresion", System.currentTimeMillis());
        if (numeroImpresora == 1) {
            ttp1.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttp1.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttp1.setTextoReimpresion(textoReimpresionCabecera(textoReimpresionCabecera));
            ttp1.setTextoReimpresionDetalle(textoReimpresionDetalle(textoReimpresionDetalle));
            ttp1.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            ttp1.setOriginal(original);
            log.debug("reimpresion()- ReImprimiendo ticket por número de impresora: " + numeroImpresora);
            log.debug("reimpresion() - El XML de reimpresión de ticket es: " + xml);
            ttp1.printTicket(new String(xml, "UTF-8"));
            log.debug("reimpresion()- Documento enviado a la impresora correctamente.");
        } else if (numeroImpresora == 2) {
            ttp2.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttp2.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttp2.setTextoReimpresion(textoReimpresionCabecera(textoReimpresionCabecera));
            ttp2.setTextoReimpresionDetalle(textoReimpresionDetalle(textoReimpresionDetalle));
            ttp2.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            ttp2.setOriginal(original);
            log.debug("reimpresion()- ReImprimiendo ticket por número de impresora: " + numeroImpresora);
            log.debug("reimpresion() - El XML de reimpresión de ticket es: " + xml);
            ttp2.printTicket(new String(xml, "UTF-8"));
            log.debug("reimpresion()- Documento enviado a la impresora correctamente.");
        } else if (numeroImpresora == 0) {
            ttpPantalla.setTextoCabecera(textoCabeceraYPiePagina(textoCabecera));
            ttpPantalla.setTextoPie(textoCabeceraYPiePagina(textoPie));
            ttpPantalla.setTextoReimpresion(textoReimpresionCabecera(textoReimpresionCabecera));
            ttpPantalla.setTextoReimpresionDetalle(textoReimpresionDetalle(textoReimpresionDetalle));
            ttpPantalla.setTextoAnulada(textoCabeceraYPiePagina(textoAnulada));
            log.debug("reimpresion() - ReImprimiendo ticket pantalla...");
            ttpPantalla.printTicket(new String(xml, "UTF-8"));
            DevicePrinterPanel panelTicket = (DevicePrinterPanel) printerPantalla.getDevicePrinter("1");
            panelTicket.setSize(500, 500);
            crearVentanaDialogoImpresion(panelTicket);
            log.debug("reimpresion() - Ventana de previsualización cerrada.");
            panelTicket.reset();
        }
        textoAnulada = "";
        textoCabecera = "";
        textoPie = "";
    }

    public void impresionPrueba() throws DocumentoException {
        try {
            //ttp1.printTicket("<ticket impresora=\"2\"><barcode type=\"CODE128\" position=\"none\">99900103000000226661</barcode> </ticket>");
            //ttp1.printTicket("<ticket impresora=\"1\"><line><text>PRUEBA DE TICKET</text></line></ticket>");
            //ttp1.printTicket("<ticket impresora=\"2\"><barcode type=\"CODE128\" position=\"none\">99900103000000226662</barcode> </ticket>");
            printer1.getDevicePrinter("1").openDrawer();

        } catch (Exception e) {
            log.error("reimpresionComprobanteAbono() - Error reimprimiendo Comprobante Abono: " + e.getMessage(), e);
            throw new DocumentoException("No se pudo reimprimir el Comprobante de Abono.", e);
        }
    }

    public void imprimirComprobantePendienteDespacho(List<LineaTicketOrigen> listaEntregados) throws TicketException, TicketPrinterException {
        log.debug("imprimirComprobantePendienteDespacho() - Imprimiendo comprobante de lista de despacho");
        String resource = null;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd/MM/yyyy");

        int impresora = IMPRESORA_PANTALLA;
        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_TICKET;
        }
        try {
            resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaFacturaPendienteDespacho()));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirComprobantePendienteDespacho() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirComprobantePendienteDespacho() - resource es null");
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String hora = (sDFH.format(cal.getTime()));
                String fecha = sD.format(cal.getTime());

                Map<String, Object> map = new HashMap<String, Object>();

                TicketsAlm ticketAlm = listaEntregados.get(0).getTicket();
                TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticketAlm.getTicket()));

                map.put("listaEntregados", listaEntregados);
                map.put("ticketAlm", ticketAlm);
                map.put("ticketOrigen", ticketOrigen);
                //map.put("empresa", Sesion.getEmpresa());
                //map.put("tienda", Sesion.getTienda());
                map.put("fecha", fecha);
                //map.put("hora", hora);

                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);

                textoPie = "";
                textoCabecera = "";
                textoAnulada = "";
                generacionTicket(resource, map, impresora, null);

            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo listado de cancelaciones pendientes " + e.getMessage());
                throw new TicketPrinterException("Error imprimiendo listado de cancelaciones pendientes. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("imprimirComprobantePendienteDespacho() - No se pudo imprimir el listado de cancelaciones pendientes", e);
                throw new TicketException("No se pudo imprimir el listado de cancelaciones pendientes", e);
            }
        }
    }

    public void imprimirTextoAdicionalCreditoDirecto(List<Pago> pagos, String documento, String nombreApellidos, boolean imprimir) throws ScriptException, TicketPrinterException {
        //   log.debug("imprimirTextoAdicionalCreditoDirecto() - Comprobando si algún pago es de tipo Crédito Directo");
        /*Comprobamos que exista algún pago de Crédito Directo*/
        for (Pago p : pagos) {
            if (p.getMedioPagoActivo().isTarjetaSukasa() || imprimir) {
                imprimir = true;
                break;
            }
        }

        if (false) {
            //    log.debug("imprimirTextoAdicionalCreditoDirecto() - Imprimiendo texto adicional para Crédito Directo");

            String resource = null;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("documento", documento != null ? documento : "");
            map.put("nombreApellidos", nombreApellidos != null ? nombreApellidos : "");
            int impresora = IMPRESORA_PANTALLA;

            if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
                impresora = IMPRESORA_COMPROBANTES;
            }
            try {
                resource = leerEsquemaTicket(this.getClass().getResource(Sesion.getDatosConfiguracion().getEsquemaCreditoDirecto()));
            } catch (Exception e) {// Catch exception if any
                // log.error("imprimirTextoAdicionalCreditoDirecto() - Error: " + e.getMessage(), e);
            }
            if (resource == null) {
                // log.error("imprimirTextoAdicionalCreditoDirecto() - resource es null");
            } else {
                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                String cadenaImprimirCreditoDirecto = generaDocumentoImpresion(resource, map, documentoImpreso);

                generacionTicket(cadenaImprimirCreditoDirecto, impresora);
                // log.debug("imprimirTextoAdicionalCreditoDirecto() - Texto adicional para créditos directos creado correctamente");
                String impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0" : Sesion.getDatosConfiguracion().getImpresoraTermica();
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_CREDITO_DIRECTO);
                documentosImpresos.add(documentoImpreso);
                if (impresoraTermica.equals("2")) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void imprimirVoucherBonoSupermaxi(List<Pago> listBonosSupermaxi, String documento, String procedencia, boolean hayCompensacion) throws TicketPrinterException, TicketException {
        log.debug("imprimirVoucherBonoSupermaxi() - Imprimiendo Voucher de Bono Supermaxi");
        String resource = null;
        int impresora = IMPRESORA_PANTALLA;

        SimpleDateFormat sDFH = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sD = new SimpleDateFormat("dd-MMM-yyyy");

        if (!Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
            impresora = IMPRESORA_COMPROBANTES;
        }
        // Recurso
        try {
            resource = leerEsquemaTicket(this.getClass().getResource("/esquemaVoucherBonoSupermaxi.xml"));
        } catch (Exception e) {// Catch exception if any
            log.error("imprimirVoucherBonoSupermaxi() - Error: " + e.getMessage(), e);
        }
        if (resource == null) {
            log.error("imprimirVoucherBonoSupermaxi() - Error imprimiendo voucher: No se ha encontrado la plantilla.");
            throw new TicketPrinterException("Error imprimiendo voucher: No se ha encontrado la plantilla.");
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String hora = (sDFH.format(cal.getTime()));
            String fecha = sD.format(cal.getTime());
            String avisoLegal = VariablesAlm.getVariable(VariablesAlm.TICKET_AVISO_LEGAL_VOUCHER);
            LineaEnTicket lineas = new LineaEnTicket(avisoLegal);

            // Vouchers de Sukasa
            PrintVoucherBonoSupermaxi printVoucherBonoSupermaxi = new PrintVoucherBonoSupermaxi(listBonosSupermaxi, documento, procedencia, hayCompensacion);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("printVoucherBonoSupermaxi", printVoucherBonoSupermaxi);
            map.put("empresa", Sesion.getEmpresa());
            map.put("tienda", Sesion.getTienda());
            map.put("cajero", Sesion.getUsuario());
            map.put("fecha", fecha);
            map.put("hora", hora);
            map.put("avisoLegal", lineas);

            ConfigImpresion variableAsConfigImpresion = Variables.getVariableAsConfigImpresion(Variables.COMPROBANTE_VOUCHER_CONFIG_IMPRESION);

            if (impresoraTermica.equals("0")) {
                seleccionarImpresora(INICIO);
                seleccionarInterlineado(PULGADAS_INTERLINEADO);
            }

            DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
            try {
                for (int i = 0; i < variableAsConfigImpresion.getImpresiones(); i++) {
                    if (i == 0) {
                        textoPie = "COPIA";
                        textoCabecera = "";
                        this.original = false;
                    } else {
                        textoPie = "ORIGINAL";
                        textoCabecera = "";
                    }
                    generacionTicket(resource, map, impresora, documentoImpreso);
                    this.original = true;
                    textoPie = "ORIGINAL";
                    textoCabecera = "";
                }
                generacionTicket(resource, map, impresora, documentoImpreso);
            } catch (NoSuchPortException e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("Error imprimiendo comprobante. No existe la impresora en el puerto indicado.");
            } catch (Exception e) {
                log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
                log.error("No se pudo imprimir el comprobante");
            }
            textoPie = "";
            textoCabecera = "";
            documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_PAGO);
            documentosImpresos.add(documentoImpreso);
        } catch (Exception e) {
            log.error("Error imprimiendo Voucher: " + e.getMessage(), e);
            throw new TicketException("No se pudo imprimir el comprobante", e);
        }
    }

    /**
     *
     * @param texto
     * @return
     */
    private String textoCabeceraYPiePagina(String texto) {
        if (texto != null) {
            return texto + (Sesion.getDatosConfiguracion().isModoDesarrollo() ? " MODO - DESARROLLO" : "");
        } else {
            return texto;
        }

    }

    /**
     *
     * @param texto
     * @return
     */
    private String textoReimpresionCabecera(String texto) {
        if (texto != null) {
            return texto;//+ (Sesion.getDatosConfiguracion().isModoDesarrollo() ? " MODO - DESARROLLO" : "");
        } else {
            return texto;
        }

    }

    /**
     *
     * @param texto
     * @return
     */
    private String textoReimpresionDetalle(String texto) {
        if (texto != null) {
            return texto;//+ (Sesion.getDatosConfiguracion().isModoDesarrollo() ? " MODO - DESARROLLO" : "");
        } else {
            return texto;
        }

    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Generacion de ticket para poder reimprimirlo </p>
     *
     * @param documento
     * @return
     * @throws NoResultException
     * @throws XMLDocumentException
     * @throws TicketException
     * @throws MedioPagoException
     */
    private TicketS reconstruccionTicketParaReimpresion(DocumentosBean documento) throws NoResultException, XMLDocumentException, TicketException, MedioPagoException {

        ArticulosServices articulosServices = ArticulosServices.getInstance();
        //G.S. Reconstruye el Ticket en caso de no existir
        TicketsAlm ticketsAlm = TicketService.consultarTicket(Long.parseLong(documento.getIdDocumento()), documento.getCodCaja(), documento.getCodAlmacen());
        TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(ticketsAlm.getXMLTicket()));

        //Reconstruccion de las lineas
        LineasTicket lineas = reconstruccionLineasParaReimpresion(ticketOrigen, articulosServices);

        FacturacionTicketBean facturacionTicketBean = new FacturacionTicketBean(ticketOrigen.getFacturacion());

        TicketS ticket = new TicketS(false);
        ticket.setCodcaja(ticketOrigen.getCodcaja());
        ticket.setId_ticket(ticketOrigen.getId_ticket());
        ticket.setFecha(ticketOrigen.getFecha());
        ticket.setUid_ticket(ticketsAlm.getUidTicket());
        ticket.setTotales(ticketOrigen.getTotales());
        ticket.setCliente(ticketOrigen.getCliente());
        ticket.setFacturacion(facturacionTicketBean);
        ticket.setCajero(ticketOrigen.getCajero());

        ticketOrigen.getTotales().setTicket(ticket);
        ticket.getTotales().inicializarDeducibles(); //TODO Modificar para que se haga por linea
        ticket.getTotales().isTotalDeducible();
        ticket.setLineas(lineas);

        lineas.recalcularTotalesBilletonMedioPago(ticket.getTotales());

        //Reconstruccion de los pagos
        ticket.setPagos(reconstruccionPagosParaReimpresion(ticketOrigen));
        ticket.getTotales().inicializarValoresReimpresion(new XMLDocument(ticketsAlm.getXMLTicket()));

        //Reconstruccion de promociones
        ticket.setPromociones(reconstruccionPromocionesReimpresiones(new XMLDocument(ticketsAlm.getXMLTicket()), ticketOrigen, ticket));

        //Reconstruccion valor Sukupon
        reconstruccionValorTotales(new XMLDocument(ticketsAlm.getXMLTicket()), ticket);

        obtenerNumeroAfiliacion(new XMLDocument(ticketsAlm.getXMLTicket()), ticket.getCliente());
        obtenerAutorizador(new XMLDocument(ticketsAlm.getXMLTicket()), ticket);
        return ticket;
    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Generacion de las lineas para poder reimprimirlo </p>
     *
     * @param ticketOrigen
     * @param articulosServices
     * @return
     */
    public LineasTicket reconstruccionLineasParaReimpresion(TicketOrigen ticketOrigen, ArticulosServices articulosServices) {

        LineasTicket lineas = new LineasTicket();
        lineas.setLineas(ticketOrigen.getLineas());

        for (LineaTicket linea : ticketOrigen.getLineas()) {
            Articulos articulo = articulosServices.getArticuloCod(linea.getArticulo().getCodart());
            linea.getArticulo().setCodmarca(articulo.getCodmarca());
            linea.getArticulo().setCodcategoria("");//Queda vacio para que no de error de null pointer
            linea.getArticulo().setIdItem(articulo.getIdItem());

            if (linea.isPromoMultipleAplicada()) {
                PromocionLineaTicket promocionLinea = new PromocionLineaTicket();
                promocionLinea.setImporteTotalPromocion(BigDecimal.ZERO);
                linea.setPromocionLinea(promocionLinea);
            }

            linea.recalcularPrecios();
            linea.recalcularImportes();
        }

        return lineas;
    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Generacion de los pagos para poder reimprimirlo </p>
     *
     *
     * @param ticketOrigen
     * @return
     * @throws TicketException
     * @throws MedioPagoException
     */
    public PagosTicket reconstruccionPagosParaReimpresion(TicketOrigen ticketOrigen) throws TicketException, TicketException, MedioPagoException {

        XMLDocumentNode nodePagos = ticketOrigen.getPagos();

        List<Pago> pagos = TicketXMLServices.getPagos(nodePagos);
        for (Pago pago : pagos) {
            if (pago instanceof PagoCredito) {
                PagoCredito pagoCredito = (PagoCredito) pago;
                MedioPagoBean medioPago = MediosPago.consultar(pagoCredito.getMedioPagoActivo().getCodMedioPago());
                TarjetaCredito tarjetaCredito = TarjetaCreditoBuilder.create(medioPago, TarjetaCredito.TARJETA_DEFECTO, false);
                tarjetaCredito.setNumero(pagoCredito.getNumeroTarjetaReimp());
                pagoCredito.setTarjetaCredito(tarjetaCredito);
            }
        }

        PagosTicket pagosTicket = new PagosTicket();
        pagosTicket.setPagos(pagos);

        return pagosTicket;

    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Generacion de las promociones para poder reimprimirlo </p>
     *
     * @param xml
     * @param ticketOrigen
     * @param ticket
     * @return
     * @throws XMLDocumentNodeNotFoundException
     */
    public TicketPromociones reconstruccionPromocionesReimpresiones(XMLDocument xml, TicketOrigen ticketOrigen, TicketS ticket) throws XMLDocumentNodeNotFoundException {

        TicketPromociones ticketPromociones = new TicketPromociones(ticket);

        XMLDocumentNode nodoPromociones = xml.getNodo(TagTicketXML.TAG_PROMOCIONES, true);
        if (nodoPromociones != null) {
            XMLDocumentNode facturaDiaSocio = nodoPromociones.getNodo(TagTicketXML.TAG_PROMO_FACTURA_DIA_SOCIO, true);
            if (facturaDiaSocio != null) {
                ticketOrigen.setUidTicketDiaSocio(facturaDiaSocio.getValue());
            }
            XMLDocumentNode nodoDescripciones = nodoPromociones.getNodo(TagTicketXML.TAG_PROMOCIONES_DESCRIPCIONES, true);

            for (XMLDocumentNode descripcion : nodoDescripciones.getHijos()) {
                ticketPromociones.addPromocionPrint(descripcion.getValue());
            }
        }

        return ticketPromociones;
    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Obtener el numero de afiliacion </p>
     *
     * @param xml
     * @param cliente
     * @throws XMLDocumentNodeNotFoundException
     */
    private void obtenerNumeroAfiliacion(XMLDocument xml, Cliente cliente) throws XMLDocumentNodeNotFoundException {

        XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);

        // Datos del cliente
        XMLDocumentNode nodoCliente = cabecera.getNodo(TagTicketXML.TAG_CLIENTE);
        TarjetaCreditoSK tarjetaCreditoSK = new TarjetaCreditoSK(TarjetaCredito.TARJETA_DEFECTO + "XXX");

        boolean afiliado = Boolean.FALSE;
        try {

            //Validamos que tenga el nodo de supermaxi
            nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_TARJETA_SUPERMAXI).getValue();
            String numeroTarjeta = nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_TARJETA_AFILIACION_NUMERO).getValue();
            tarjetaCreditoSK.setNumero(numeroTarjeta);
            cliente.setTarjetaAfiliacion(tarjetaCreditoSK);
            afiliado = Boolean.TRUE;

        } catch (XMLDocumentNodeNotFoundException ex) {

        }
        cliente.setAplicaTarjetaAfiliado(afiliado);

    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Obtener el nombre del autorizador </p>
     *
     * @param xml
     * @param ticket
     * @throws XMLDocumentNodeNotFoundException
     */
    private void obtenerAutorizador(XMLDocument xml, TicketS ticket) throws XMLDocumentNodeNotFoundException {

        XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);
        try {
            XMLDocumentNode nodoAutrizador = cabecera.getNodo(TagTicketXML.TAG_AUTORIZADOR);
            String codAutorizador = nodoAutrizador.getNodo(TagTicketXML.TAG_USUARIO_COD).getValue();
            ticket.setAutorizadorVenta(codAutorizador);
        } catch (XMLDocumentNodeNotFoundException ex) {

        }

        try {
            XMLDocumentNode nodoObservacion = cabecera.getNodo(TagTicketXML.TAG_OBSERVACION);
            String observacion = nodoObservacion.getValue();
            ticket.setObservaciones(observacion);
        } catch (XMLDocumentNodeNotFoundException ex) {

        }
    }

    /**
     * <b>author: Gabriel Simbania</b>
     * <p>
     * Reconstruye el valor de varios campos de los totales </p>
     *
     * @param xml
     * @param ticket
     * @throws XMLDocumentNodeNotFoundException
     */
    public void reconstruccionValorTotales(XMLDocument xml, TicketS ticket) throws XMLDocumentNodeNotFoundException {

        XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);
        try {
            XMLDocumentNode nodoTotales = cabecera.getNodo(TagTicketXML.TAG_TOTALES);
            XMLDocumentNode nodoPromociones = nodoTotales.getNodo(TagTicketXML.TAG_TOTALES_PROMOCIONES);

            for (XMLDocumentNode nodoPromocion : nodoPromociones.getHijos()) {
                String tipoPromocion = nodoPromocion.getNodo(TagTicketXML.TAG_PROMO_ID_TIPO).getValue();
                String importeAhorro = nodoPromocion.getNodo(TagTicketXML.TAG_PROMO_IMPORTE_AHORRO).getValue();
                if (tipoPromocion != null && (TipoPromocionBean.TIPO_PROMOCION_BILLETON == Long.parseLong(tipoPromocion))) {
                    ticket.getTotales().setTotalPromocionesCabecera(new BigDecimal(importeAhorro));
                }
            }

            try {
                XMLDocumentNode nodoDescuentoGlobal = nodoTotales.getNodo(TagTicketXML.TAG_TOTALES_DESCUENTO_GLOBAL);
                XMLDocumentNode nodoPorcentajeDescuentoGlobal = nodoTotales.getNodo(TagTicketXML.TAG_PORCENTAJE_DESCUENTO_GLOBAL);
                PromocionTipoDtoManualTotal.activar(Integer.valueOf(nodoPorcentajeDescuentoGlobal.getValue()));
                ticket.getTotales().setTotalPromocionesCabecera(new BigDecimal(nodoDescuentoGlobal.getValue()));
            } catch (XMLDocumentNodeNotFoundException ex) {

            }

        } catch (XMLDocumentNodeNotFoundException ex) {

        }

    }

}
