/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.devoluciones;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import com.comerzzia.jpos.dto.intercambio.IntercambioDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.MotivoDevolucionDTO;
import com.comerzzia.jpos.dto.ventas.NotaCreditoLocalDTO;
import com.comerzzia.jpos.dto.ventas.NotaCreditoResponseDTO;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.Devolucion;
import com.comerzzia.jpos.entity.db.GarantiaExtendidaReg;
import com.comerzzia.jpos.entity.db.ImpuestosFact;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.persistencia.devoluciones.DevolucionesDao;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticulosDevueltosDao;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.formaPago.FormaPagoBean;
import com.comerzzia.jpos.persistencia.formaPago.FormaPagoDao;
import com.comerzzia.jpos.persistencia.garantia.GarantiasDao;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.notacredito.detalle.NotaCreditoDetalleBean;
import com.comerzzia.jpos.persistencia.notacredito.detalle.NotaCreditoDetalleDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionSocioBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionesDao;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.impuestos.ImpuestosServices;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.facturaElectronica.ENotaCreditoServices;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendidaServices;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.print.documentos.ParametrosDocumentos;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.stock.StockTimeOutException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import static com.comerzzia.jpos.servicios.tickets.TicketService.generarClaveAccesoNotaCredito;
import com.comerzzia.jpos.util.EnumTipoDevolucion;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.jpos.util.UtilUsuario;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.enums.EnumEstado;
import com.comerzzia.jpos.util.enums.EnumNotaCreditoError;
import com.comerzzia.jpos.util.exception.SocketTPVException;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.InOutStream.UtilInputOutputStream;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author MGRI
 */
public class DevolucionesServices {

    private static Logger log = Logger.getMLogger(DevolucionesServices.class);
    private static PrintServices ts = PrintServices.getInstance();
    private static final BigDecimal cienBD = new BigDecimal(100);
//    private static final BigDecimal calculoIce = new BigDecimal(BigDecimal.ZERO);
    private static PrintServices tsOtroLocal = PrintServices.getInstance(); //G.S Transaccion generada para la impresion desde otro local
    ImpuestosServices impuestosServices = ImpuestosServices.getInstance();

    public static List<MotivoDevolucion> consultaMotivosDevolucion() throws DevolucionException {
        try {
            return DevolucionesDao.consultaMotivosDevolucion();
        } catch (Exception ex) {
            log.debug("Excepcion en consulta de motivos de devolución", ex);
            throw new DevolucionException("Error en la consulta de Motivos de devolución");
        }
    }

    /**
     *
     * @param devolucion
     * @param isDevolucionOtroLocal Variable para saber si la devolucion se
     * genera desde otro local
     * @throws NotaCreditoException
     * @throws DevolucionException
     */
    public static void crearDevolucion(com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion, boolean isDevolucionOtroLocal)
            throws NotaCreditoException, DevolucionException, SocketTPVException {

        if (MediosPago.getInstancia().getPagoNotaCredito() == null) {
            log.fatal("crearDevolucion() - Error: No se puede completar la devolución porque el medio de pago NOTA DE CRÉDITO no está correctamente configurado.");
            log.warn("crearDevolucion() - Compruebe las variables del sistema y los medios de pago configurados.");
            throw new DevolucionException("No se puede completar la devolución porque el medio de pago Nota de Crédito no está configurado correctamente.");
        }

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            realizarDevolucion(em, devolucion);

            PrintServices printServices = null;
            if (isDevolucionOtroLocal) {
                printServices = tsOtroLocal;
                printServices.setImprime(false);//Para que no imprima el ticket
            } else {
                printServices = ts;
                printServices.setImprime(true);
            }

            em.getTransaction().commit();
            printServices.limpiarListaDocumentos();
            printServices.imprimirTicketDevolucionNC(devolucion);

            //Guardamos la nota de crédito en BBDD
            DocumentosService.crearDevolucion(devolucion, printServices.getDocumentosImpresos(), DocumentosBean.NOTA_CREDITO);
            printServices.limpiarListaDocumentos();

            generarKardexVentas(devolucion);

            // Control de Stock 
            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)) {
                try {
                    LogKardexBean logKardex = new LogKardexBean();
                    logKardex.setTipoAccion(LogKardexBean.tipoAccionDevolucion);
                    logKardex.setUsuarioAutorizacion(devolucion.getAutorizador());
                    logKardex.setFactura(String.valueOf(devolucion.getNotaCredito().getIdNotaCredito()));
                    log.debug("crearDevolucion() - Aumentando Stock de venta...");
                    ServicioStock.disminuyeStockVenta(devolucion.getTicketDevolucion().getLineas().getLineas(), logKardex);
                    ServicioStock.actualizaListaArticulosKardexVentas(devolucion.getTicketDevolucion().getLineas().getLineas(), ServicioStock.MOVIMIENTO_51, devolucion.getTicketOriginal().getTienda(), true);
                } catch (StockException e) {
                    log.info("crearDevolucion() - ERROR ACTUALIZANDO STOCK. La venta ha sido realizada a pesar del error.");
                    log.error("crearDevolucion() - ERROR ACTUALIZANDO STOCK EN SISTEMAS PROPIETARIOS DE SUKASA: " + e.getMessage(), e);
                } catch (StockTimeOutException e) {
                    log.info("crearDevolucion() - ERROR ACTUALIZANDO STOCK. La venta ha sido realizada a pesar del error.");
                    log.error("crearDevolucion() - ERROR ACTUALIZANDO STOCK EN SISTEMAS PROPIETARIOS DE SUKASA: " + e.getMessage(), e);
                }
            }
        } catch (NotaCreditoException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (SocketTPVException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (DevolucionException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            String msg = "Error creando nueva devolución.";
            log.error(msg, e);
            em.getTransaction().rollback();
            throw new DevolucionException(msg, e);
        } finally {
            em.close();
        }

    }

    /**
     * @author @description realiza la devolución y generación de la nota de
     * crédito
     * @param em
     * @param devolucion
     * @throws NotaCreditoException
     * @throws DevolucionException
     * @throws SocketTPVException
     */
    private static void realizarDevolucion(EntityManager em, com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion)
            throws NotaCreditoException, DevolucionException, SocketTPVException {
        Connection conn = Connection.getConnection(em);
        try {
            List<ArticuloDevueltoBean> deNadaAPendienteEnvioDomicilio = new ArrayList<>();
            // Calculamos los Articulos Devueltos
            List<ArticuloDevueltoBean> listaArticulosDevueltos = new ArrayList<ArticuloDevueltoBean>();
            // listaArticulosDevueltos = devolucion.getArticulosDevueltos();
            // la lista anterior de artículos devueltos no se debe de volver a guardar

            ArticuloDevueltoBean articuloDevuelto;

            // calculamos fecha validez nota crédito
            Fecha fechaValidez = new Fecha();
            fechaValidez.sumaDias(Variables.getVariableAsInt(Variables.POS_CONFIG_NOTAS_CREDITO_DIAS_VALIDEZ));

            // creamos nota de crédito
            NotasCredito nota = new NotasCredito();
            nota.setCodcaja(devolucion.getTicketDevolucion().getCodcaja());
            nota.setCodalm(devolucion.getTicketDevolucion().getTienda());
            nota.setFecha(new Date());
            nota.setFechaValidez(fechaValidez.getDate());
            nota.setIdNotaCredito(ServicioContadoresCaja.obtenerContadorNotaCredito());
            nota.setTotal(devolucion.getTicketDevolucion().getTotales().getTotalAPagar());
            nota.setSaldo(devolucion.getTicketDevolucion().getTotales().getTotalAPagar());
            nota.setUidNotaCredito(devolucion.getTicketDevolucion().getUid_ticket());
            nota.setAutorizador(devolucion.getAutorizador());
            nota.setValorInteres(devolucion.getTicketDevolucion().getTotales().getInteres());
            if (devolucion.isAnulacion()) {
                nota.setMotivoAnulacion("VOUCHER ANULADO");
                nota.setFechaAnulacion(new Date());
                nota.setAutorizadorAnulacion(devolucion.getAutorizador());
                nota.setSaldo(BigDecimal.ZERO);
                nota.setTipoDevolucion(EnumTipoDevolucion.TIPO_ANULACION_VOUCHER.getValor());
            }
            if (devolucion.getTipoDevolucion().equals(EnumTipoDevolucion.TIPO_DEVOLUCION_DINERO.getValor())) {
                nota.setSaldo(BigDecimal.ZERO);
            }
            nota.setTipoDevolucion(devolucion.getTipoDevolucion());
            nota.setLocalOrigen(devolucion.getLocalOrigen());
            String generarClaveAccesoTemp = generarClaveAccesoNotaCredito(nota);
            nota.setClaveAcceso(generarClaveAccesoTemp);
            TicketsAlm ticketAlm = TicketsDao.consultarTicket(devolucion.getTicketOriginal().getUid_ticket());
            nota.setFactDocumento(ticketAlm.getFactDocumento());
            devolucion.setNotaCredito(nota);

            // creamos devolución de bbdd
            Devolucion devolucionDB = new Devolucion();
            if (devolucion.getTicketDevolucion().getVendedor() != null) {
                devolucionDB.setCodvendedor(devolucion.getTicketDevolucion().getVendedor().getCodvendedor());
            }
            devolucionDB.setEstadoMercaderia(devolucion.getEstadoMercaderia());
            devolucionDB.setMotivo(devolucion.getMotivo());
            devolucionDB.setUidTicket(devolucion.getTicketOriginal().getUid_ticket());
            devolucionDB.setIdUsuario(devolucion.getTicketDevolucion().getCajero().getIdUsuario());
            devolucionDB.setObservaciones(devolucion.getObservaciones());
            devolucionDB.setUidDevolucion(devolucion.getTicketDevolucion().getUid_ticket());
            devolucionDB.setUidNotaCredito(devolucion.getTicketDevolucion().getUid_ticket());

            devolucion.getTicketDevolucion().getTotales().inicializarSubtotales();
            devolucion.getTicketDevolucion().getTotales().setBase(BigDecimal.ZERO);

            //LOG OPREACIONES PARA FECHAS DE DEVOLUCIÖN CADUCADAS
            if (devolucion.getTicketOriginal().isFechaFinDevolucionCaducada() && devolucion.getAutorizador() != null) {
                //Creación del log operaciones
                LogOperaciones logDevolucionCaducada = new LogOperaciones();
                logDevolucionCaducada = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logDevolucionCaducada.setFechaHora(new Date());
                logDevolucionCaducada.setUsuario(Sesion.getUsuario().getUsuario());
                logDevolucionCaducada.setReferencia(devolucion.getTicketOriginal().getUid_ticket());
                logDevolucionCaducada.setProcesado('N');
                logDevolucionCaducada.setAutorizador(devolucion.getAutorizador());
                logDevolucionCaducada.setCodOperacion(ServicioLogAcceso.LOG_AUTORIZA_FECHA_DEVOLUCION);
                logDevolucionCaducada.setObservaciones(devolucion.getObservaciones());
                ServicioLogAcceso.crearAccesoLog(logDevolucionCaducada, em);
            }

            // Actualizamos los artículos devueltos del ticket original
            for (LineaTicket linea : devolucion.getTicketDevolucion().getLineas().getLineas()) {
                // En notas de crédito, importe e importeFinalPagado coinciden porque no hay descuentos.
                // Los establecemos aquí como iguales porque es posible que el importe se haya calculado a partir de valores redondeados y sea erróneo
                // El correcto es el importeFinalPagado
                ///cambio para solucionar problema Rd Costo landed
                List<LineaTicketOrigen> lineaPrueba = TicketService.consultarLineasTicket(devolucion.getTicketOriginal().getUid_ticket());
                for (int i = 0; i < lineaPrueba.size(); i++) {
                    if (linea.getLineaOriginal() == lineaPrueba.get(i).getLineaTicketOrigenPK().getIdLinea()
                            && linea.getArticulo().getCodart().equals(lineaPrueba.get(i).getCodart().getCodart())) {
                        //cantidad Quitada
//                if(linea.getCantidad().equals(lineaPrueba.get(i).getCantidad())){
                        linea.setCostoLanded(lineaPrueba.get(i).getCostoLanded());
                        LineaTicketOrigen lineas = lineaPrueba.get(i);
                        char add = lineas.getRecogidaPosterior();
                        System.out.println("Se actualiza pendiente de entrega a N");
                        lineas.setRecogidaPosterior('N');
                        lineas.setEnvioDomicilio('N');
                        TicketService.modificarTicketDetalle(lineas);
                        System.out.println("Actualizado correctamente");
                        break;

//                }
                    }
                }

//           ///////////////////////////Cambio Pendientes Entrega////////////////////////
//             linea.getLineaTicketOrigen().setRecogidaPosterior(true);
//             
//             TicketService.modificarTicketDetalle(linea.getLineaTicketOrigen());
                // Anulamos promociones de control a cliente de esta factura
                // ServicioPromocionesClientes.anularPromocionesAplicadas(conn, sql, ticket.getUidTicket());
                if (devolucion.getTicketOriginal().getUid_ticket() != null) {
                    //Anular dia Socio
                    //Connection conn = Connection.getConnection(em);
                    ServicioPromocionesClientes.anularReferenciaFacturaDiaSocio(conn, devolucion.getTicketOriginal().getUid_ticket());
                }
                //cambio realizado para mostrar en pantalla tarifa origen Rd
                linea.setImporte(linea.getImporteFinalPagado());
                linea.setImporteTotal(linea.getImporteTotalFinalPagado());
                articuloDevuelto = new ArticuloDevueltoBean(devolucion.getTicketOriginal().getUid_ticket(), devolucionDB.getUidNotaCredito(), linea.getLineaOriginal(), linea.getArticulo().getCodart(), linea.getCantidad());

                //Acumulados para el cálculo de la base
                if (linea.getCodimp() != null) {
                    ConfigImpPorcentaje configImpuestos = devolucion.getTicketDevolucion().getTotales().getSubtotalesImpuestos().get(linea.getCodimp());
                    configImpuestos.setTotal(configImpuestos.getTotal().add(linea.getImporteFinalPagado()));
                } else {
                    ConfigImpPorcentaje configImpuestos = devolucion.getTicketDevolucion().getTotales().getSubtotalesImpuestos().get(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO);
                    configImpuestos.setTotal(configImpuestos.getTotal().add(Numero.redondear(linea.getImporteFinalPagado())));
                }

                articuloDevuelto.setGarantiaReferencia(linea.getReferenciaGarantia());
                devolucion.getTicketDevolucion().getTotales().setBase(devolucion.getTicketDevolucion().getTotales().getBase().add(Numero.redondear(linea.getImporteFinalPagado())));

                listaArticulosDevueltos.add(articuloDevuelto);
                deNadaAPendienteEnvioDomicilio.add(articuloDevuelto);

            }

            es.mpsistemas.util.mybatis.session.SqlSession sql = new es.mpsistemas.util.mybatis.session.SqlSession();
            sql.openSession(SessionFactory.openSession(conn));
            //anular solo un rato
//            ServicioPromocionesClientes.anularPromocionesAplicadas(conn, sql, devolucion.getTicketOriginal().getUid_ticket());
            Cupon cuponDet = new Cupon();
            cuponDet = ServicioPromocionesClientes.saldosCupon(conn, sql, devolucion);
            if (cuponDet != null) {
                devolucion.getTicketDevolucion().setCupon(cuponDet);
            }
            devolucion.getTicketDevolucion().getTotales().calculaIVA();
            BigDecimal calculoIce = new BigDecimal("00.00");
            Integer cantidad = 0;
            for (LineaTicket linea : devolucion.getTicketDevolucion().getLineas().getLineas()) {
                ImpuestosFact imp = null;
                imp = ImpuestosServices.consultaImpuestos(devolucion.getTicketOriginal().getUid_ticket());
                if (imp != null) {
                    if (linea.getArticulo().getPmp() != null) {
                        cantidad += linea.getCantidad();
                        devolucion.getTicketDevolucion().getTotales().setBaseImponibleIce(cantidad);
                        calculoIce = calculoIce.add(imp.getTarifa().multiply(new BigDecimal(linea.getCantidad())));
                        devolucion.getTicketDevolucion().getTotales().setImpuestosIce(calculoIce);

                    } else {
                        devolucion.getTicketDevolucion().getTotales().setImpuestosIce(calculoIce);
                    }
                }

            }

            log.info("INGRESO A CONSTRUIR EL XML DE LA N/C");
            // construimos xml nota de crédito
            if (devolucion.getTicketDevolucion().getTotales().getTotalAPagar().compareTo(BigDecimal.ZERO) > 0) {
                NotaCreditoXMLServices.construirXML(nota, devolucion);
                nota.setCodcli(devolucion.getTicketDevolucion().getCliente().getCodcli());
                nota.setUidDevolucion(devolucionDB.getUidDevolucion());

                CajaDet movimientoCaja = Sesion.getCajaActual().crearApunteDevolucion(nota.getTotal(), devolucion.getTicketDevolucion().getId_ticket());

                //Corregir el movimiento nota de credito
                String documento = nota.getCodalm() + "-" + nota.getCodcaja() + "-" + String.format("%09d", nota.getIdNotaCredito());
                movimientoCaja.setDocumento(documento);
                movimientoCaja.setIdDocumento(nota.getUidNotaCredito());
                movimientoCaja.setInteres(devolucion.getTicketDevolucion().getTotales().getInteres().multiply(BigDecimal.valueOf(Double.parseDouble("-1"))));


//            if (VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || devolucion.getTicketDevolucion().getCliente().isTipoRuc())) {
                log.info("INGRESO A GENERAR EL XML DE LA  F.E.");
                nota.seteNotaCredito(ENotaCreditoServices.generarNotaCreditoElectronica(nota, devolucion).getBytes("UTF-8"));
                log.info("FINALIZO A GENERAR EL XML DE LA  F.E.");
//            }
                DevolucionesDao.escribirDevolucionYNotaCredito(em, nota, devolucionDB, listaArticulosDevueltos, movimientoCaja);
                //Modifica el beneficio al nuevo socio para que pueda volver utilizar.
                PromocionSocioBean promoSocioNuevo = PromocionesDao.consultarPromoSocioNC(conn, nota.getCodcli(), devolucion.getTicketOriginal().getUid_ticket());
                if (promoSocioNuevo.getCedula() != null) {
                    boolean promoSocio = ServicioPromocionesClientes.updatePromoClienteSocioNC(devolucion.getTicketOriginal().getUid_ticket(), nota.getCodcli(), nota.getUidNotaCredito());
                    if (!promoSocio) {
                        log.error("No se modifico el descuento del cliente.");
                        throw new Exception("No se modifico el descuento del cliente.");
                    }
                }
            } else {
                try {
                    boolean totalPagar = devolucion.getTicketDevolucion().getTotales().getTotalAPagar().compareTo(BigDecimal.ZERO) > 0;
                    if (!totalPagar) {
                        throw new Exception("La nota de credito debe ser mayor a 0 ");
                    }
                } catch (Exception e) {
                    String mensaje = "La nota de credito debe ser mayor a 0 " + e.toString();
                    log.error(mensaje, e);
                    throw new ValidationException("La nota de credito debe ser mayor a 0 ");
                }
            }
            //G.S. cuando se genera la nota de crédito debe anular la extensión de garantía tiene como referencia
            for (ArticuloDevueltoBean articuloDevueltos : listaArticulosDevueltos) {

                if (articuloDevueltos.getGarantiaReferencia() != null) {
                    GarantiaExtendidaReg extendidaReg = GarantiaExtendidaServices.consultarGarantiaByCriterio(devolucion.getTicketOriginal().getUid_ticket(), null, articuloDevueltos.getGarantiaReferencia().getArticulo().getCodart(), articuloDevueltos.getIdLinea());
                    if (extendidaReg == null) {
                        extendidaReg = GarantiaExtendidaServices.consultarGarantiaByCriterio(null, devolucion.getTicketOriginal().getUid_ticket(), articuloDevueltos.getGarantiaReferencia().getArticulo().getCodart(), articuloDevueltos.getIdLinea());
                        if (extendidaReg != null) {
                            extendidaReg.setEstado(EnumEstado.ANULADO);
                            GarantiasDao.update(em, extendidaReg);
                        }
                    } else {
                        extendidaReg.setEstado(EnumEstado.ANULADO);
                        GarantiasDao.update(em, extendidaReg);
                    }
                }
            }
            // Construimos las lineas del detalle de la nota de credito
            for (int i = 0; i < devolucion.getTicketDevolucion().getLineas().getLineas().size(); i++) {
                NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
                detalle.setUidNotaCredito(devolucion.getTicketDevolucion().getUid_ticket());
                detalle.setIdCajero(devolucion.getTicketDevolucion().getCajero().getIdUsuario());

                if (devolucion.getTicketDevolucion().getVendedor() != null) {
                    detalle.setCodVendedor(devolucion.getTicketDevolucion().getVendedor().getCodvendedor());
                }
                detalle.setIdLinea(new Long(i));
                LineaTicket linea = devolucion.getTicketDevolucion().getLineas().getLineas().get(i);
                detalle.setCodart(linea.getArticulo().getCodart());
                detalle.setCodigoBarras(linea.getCodigoBarras());
                detalle.setCantidad(linea.getCantidad());
                detalle.setPrecio(linea.getPrecio());
                detalle.setPrecioTotal(linea.getPrecioTotal());
                detalle.setImporteFinal(linea.getImporte());
                detalle.setImporteTotalFinal(linea.getImporteTotal());
                detalle.setUidTicket(devolucion.getTicketOriginal().getUid_ticket());
                detalle.setCodImp(linea.getCodimp());
                detalle.setValorInteres(linea.getInteres());

                //DR agregar el vendedor del item (Comentado pq al liquidar los plan novios falla pq el cod vendedor viene null)
                detalle.setCodVendedor(linea.getCodEmpleado());

                //Se agrega costo landed RD
                detalle.setCostoLanded(linea.getCostoLanded());
                if (linea.getPorcentajeIva() != null) {
                    detalle.setPorcentaje(linea.getPorcentajeIva());
                } else {
                    if (detalle.getCodImp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
                        if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2016)) {
                            detalle.setPorcentaje(Numero.DOCE);
                        } else if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2017)) {
                            detalle.setPorcentaje(Numero.CATORCE);
                        } else {
                            detalle.setPorcentaje(Sesion.getEmpresa().getPorcentajeIva());
                        }
                    } else {
                        detalle.setPorcentaje(BigDecimal.ZERO);
                    }
                }
                if (detalle.getCodImp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL) && detalle.getImporteFinal() != null && Numero.isMayorACero(detalle.getImporteFinal())) {
                    double porcentaje = detalle.getImporteTotalFinal().doubleValue() / (detalle.getImporteFinal().doubleValue());
                    if (porcentaje < 1.11 || porcentaje > 1.16) {
                        //Calculamos el importe final en base al importe total final
                        if (linea.getPorcentajeIva() != null) {
                            detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), linea.getPorcentajeIva()));
                        } else {
                            if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2016)) {
                                detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Numero.DOCE));
                            } else if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2017)) {
                                detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Numero.CATORCE));
                            } else {
                                detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Sesion.getEmpresa().getPorcentajeIva()));
                            }
                        }
                    }
                }
                NotaCreditoDetalleDao.insert(detalle, conn);
            }

            actualizarTiketFacturaOriginal(devolucion, deNadaAPendienteEnvioDomicilio);

            if (devolucion.getFormasPago() != null) {
                for (MedioPagoDTO formaPago : devolucion.getFormasPago()) {
                    FormaPagoBean formaPagoBean = new FormaPagoBean(nota.getCodalm(), nota.getCodcaja(), nota.getIdNotaCredito(), formaPago.getCodMedPag(), formaPago.getDescripcionMedioPago(), formaPago.getCargo(), formaPago.getCruzaEfectivo());
                    FormaPagoDao.insert(conn, formaPagoBean);
                }
            }
        } catch (SocketTPVException e) {
            log.error(e.getMessage(), e);
            conn.deshacerTransaccion();
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            conn.deshacerTransaccion();
            throw new DevolucionException(e.getMessage(), e);
        }
    }

    public static void realizarNotaCreditoPlanNovios(EntityManager em, com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion, LineaTicket linea, boolean soloNotaCredito)
            throws NotaCreditoException, DevolucionException {

        Connection conn;

        try {
            log.debug("realizarNotaCreditoPlanNovios() - Realizando nota de crédito para liquidación plan novios....");
            // calculamos fecha validez nota crédito
            Fecha fechaValidez = new Fecha();
            fechaValidez.sumaDias(Variables.getVariableAsInt(Variables.POS_CONFIG_NOTAS_CREDITO_DIAS_VALIDEZ));

            // creamos nota de crédito
            NotasCredito nota = new NotasCredito();
            nota.setCodcaja(devolucion.getTicketDevolucion().getCodcaja());
            nota.setCodalm(devolucion.getTicketDevolucion().getTienda());
            nota.setFecha(new Date());
            nota.setFechaValidez(fechaValidez.getDate());
            nota.setIdNotaCredito(ServicioContadoresCaja.obtenerContadorNotaCredito());
            nota.setTotal(devolucion.getTicketDevolucion().getTotales().getTotalAPagar());
            nota.setSaldo(devolucion.getTicketDevolucion().getTotales().getTotalAPagar());
            nota.setUidNotaCredito(UUID.randomUUID().toString());
            nota.setUidDevolucion(devolucion.getTicketDevolucion().getUid_ticket());
            nota.setAutorizador(devolucion.getAutorizador());

            TicketS tick = devolucion.getTicketDevolucion();
            nota.setAutorizador(tick.getAutorizadorVenta().getUsuario());
            nota.setCodcli(tick.getCliente().getCodcli());

            devolucion.setNotaCredito(nota);

            // creamos devolución de bbdd (la devolución es necesaria para que la nota de crédito quede enganchada con la factura)
            Devolucion devolucionDB = new Devolucion();
            devolucionDB.setEstadoMercaderia(devolucion.getEstadoMercaderia());
            devolucionDB.setMotivo(devolucion.getMotivo());
            devolucionDB.setUidTicket(devolucion.getTicketOriginal().getUid_ticket());
            devolucionDB.setIdUsuario(devolucion.getTicketDevolucion().getCajero().getIdUsuario());
            devolucionDB.setObservaciones(devolucion.getObservaciones());
            devolucionDB.setUidDevolucion(devolucion.getTicketDevolucion().getUid_ticket());
            devolucionDB.setUidNotaCredito(nota.getUidNotaCredito());

            // construimos xml nota de crédito
            NotaCreditoXMLServices.construirXML(nota, devolucion);
            DevolucionesDao.escribirNotaDeCredito(em, nota, devolucionDB);
            nota.seteNotaCredito(ENotaCreditoServices.generarNotaCreditoElectronica(nota, devolucion).getBytes("UTF-8"));
            //Insertamos el artículo plan novio en x_articulos_devueltos_tbl            
            ArticuloDevueltoBean articuloDevuelto = new ArticuloDevueltoBean();
            articuloDevuelto.setUidTicket(devolucionDB.getUidTicket());
            articuloDevuelto.setUidNotaCredito(nota.getUidNotaCredito());
            articuloDevuelto.setIdLinea(linea.getIdlinea());
            articuloDevuelto.setCodart(linea.getArticulo().getCodart());
            articuloDevuelto.setCantidad(linea.getCantidad());

            conn = Connection.getConnection(em);
            ArticulosDevueltosDao.insert(conn, articuloDevuelto);

            // Construimos las lineas del detalle de la nota de credito
            NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
            detalle.setUidNotaCredito(nota.getUidNotaCredito());
            detalle.setIdCajero(devolucion.getTicketDevolucion().getCajero().getIdUsuario());
            if (devolucion.getTicketDevolucion().getVendedor() != null) {
                detalle.setCodVendedor(devolucion.getTicketDevolucion().getVendedor().getCodvendedor());
            } else {
                detalle.setCodVendedor(devolucion.getCodVendedor());
            }
            detalle.setIdLinea(new Long(0));

            detalle.setCodart(linea.getArticulo().getCodart());
            detalle.setCodigoBarras(linea.getCodigoBarras());
            detalle.setCantidad(linea.getCantidad());

            detalle.setPrecio(linea.getPrecio());
            detalle.setPrecioTotal(linea.getPrecioTotal());
            detalle.setImporteFinal(linea.getImporte());
            detalle.setImporteTotalFinal(linea.getImporteTotal());

            detalle.setUidTicket(devolucion.getTicketOriginal().getUid_ticket());
            detalle.setCodImp(linea.getArticulo().getCodimp());
            if (linea.getPorcentajeIva() != null) {
                detalle.setPorcentaje(linea.getPorcentajeIva());
            } else {
                if (detalle.getCodImp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
                    if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2016)) {
                        detalle.setPorcentaje(Numero.DOCE);
                    } else if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2017)) {
                        detalle.setPorcentaje(Numero.CATORCE);
                    } else {
                        detalle.setPorcentaje(Sesion.getEmpresa().getPorcentajeIva());
                    }
                } else {
                    detalle.setPorcentaje(BigDecimal.ZERO);
                }
            }
            if (detalle.getCodImp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL) && detalle.getImporteFinal() != null && Numero.isMayorACero(detalle.getImporteFinal())) {
                double porcentaje = detalle.getImporteTotalFinal().doubleValue() / (detalle.getImporteFinal().doubleValue());
                if (porcentaje < 1.11 || porcentaje > 1.16) {
                    //Calculamos el importe final en base al importe total final
                    if (linea.getPorcentajeIva() != null) {
                        detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), linea.getPorcentajeIva()));
                    } else {
                        if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2016)) {
                            detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Numero.DOCE));
                        } else if (devolucion.getTicketOriginal().getFecha().antes(Constantes.junioPrimero2017)) {
                            detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Numero.CATORCE));
                        } else {
                            detalle.setImporteFinal(Numero.getAntesDePorcentajeR4(detalle.getImporteTotalFinal(), Sesion.getEmpresa().getPorcentajeIva()));
                        }
                    }
                }
            }
            NotaCreditoDetalleDao.insert(detalle, conn);

            //AQUI CREA EN D_CAJA_DET_TBL
            Sesion.getCajaActual().crearApunte3(em, nota.getTotal().negate(), "LIQUIDACION RESERVA", nota.getIdNotaCreditoCompleto(), MediosPago.getInstancia().getPagoNotaCredito(), nota.getUidNotaCredito());

            String idDocumento = tick.getUid_ticket();
            String documento = tick.getTienda() + "-" + tick.getCodcaja() + "-" + String.format("%09d", tick.getId_ticket());

            MedioPagoBean mpb = MediosPago.consultar(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_ABONO_A_RESERVAS));
            Sesion.getCajaActual().crearApunte2(em, nota.getTotal(), "VENTA", documento, mpb, idDocumento);

        } catch (SQLException e) { // NOTA si se usa otra operación que use conn en el método se deberá cambiar el mensaje de la excepción
            String msg = "Error inesperado creando nueva nota de crédito para liquidación plan novios: ";
            String msg2 = "Error insertando articulos devueltos en base de datos: " + e.getMessage();
            log.error("realizarNotaCreditoPlanNovios() - " + msg);
            log.error("realizarNotaCreditoPlanNovios() - " + msg2, e);
            throw new DevolucionException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nueva nota de crédito para liquidación plan novios: " + e.getMessage();
            log.error("realizarNotaCreditoPlanNovios() - " + msg, e);
            throw new DevolucionException(msg, e);
        }
    }

    public static NotasCredito consultarNotaCreditoAnulacion(String codAlmacen, String codCaja, Long idNota) throws NotaCreditoException, ValidationException {
        try {

            NotasCredito res = DevolucionesDao.consultarNotaCredito(codAlmacen, codCaja, idNota);
            Fecha valida = new Fecha(res.getFecha());
            valida.sumaDias(1);
            Fecha hoy = new Fecha();
            if (hoy.despues(valida)) {
                throw new ValidationException("Se ha superado el periodo de anulación");
            }
            return res;
        } catch (ValidationException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof NoResultException) {
                throw new ValidationException("No existe Nota de crédito con los parámetros indicados.");
            } else if (ex instanceof NotaCreditoException) {
                throw (NotaCreditoException) ex;
            } else {
                throw new NotaCreditoException("Error obteniendo nota de crédito.", ex);
            }
        }
    }

    public static NotasCredito consultarNotaCredito(String codAlmacen, String codCaja, Long idNota) throws NotaCreditoException {

        return consultarNotaCredito(codAlmacen, codCaja, idNota, false);
    }

    public static NotasCredito consultarNotaCredito(String codAlmacen, String codCaja, Long idNota, boolean desdeReservaciones) throws NotaCreditoException {
        try {
            NotasCredito res = DevolucionesDao.consultarNotaCredito(codAlmacen, codCaja, idNota);
            if (!desdeReservaciones) {
                if (Sesion.getTicket().getPagos().contieneNotaCredito(res.getUidNotaCredito())) {
                    log.debug("La nota de credito ya se ha usado en el pago actual.");
                    throw new NotaCreditoException("La nota de credito ya se ha usado en el pago actual.");
                }
            } else {
                // No tenemos acceso a los pagos del ticket desde aquí
            }

            return res;
        } catch (Exception ex) {
            if (ex instanceof NoResultException) {
                log.debug("No existe Nota de crédito con esos parametros.");
                throw new NotaCreditoException("No existe Nota de crédito con esos parametros.");
            } else if (ex instanceof NotaCreditoException) {
                log.debug(ex.getMessage());
                throw (NotaCreditoException) ex;
            } else {
                log.debug(ex.getMessage(), ex);
                throw new NotaCreditoException("Error obteniendo nota de crédito.", ex);
            }
        }
    }

    public static void modificaNotaCredito(String uidNota, BigDecimal nuevoSaldo, String idReferencia, String tipo, String documento) throws NotaCreditoException {
        try {
            DevolucionesDao.modificaNotaCredito(uidNota, nuevoSaldo, idReferencia, tipo, documento);
        } catch (Exception ex) {
            log.debug("Error modificando la nota de crédito :" + ex.getMessage(), ex);
            throw new NotaCreditoException("Error modificando la nota de crédito.");
        }
    }

    public static Map<Integer, ArticuloDevueltoBean> consultarArticulosDevueltos(String ticketUid) throws Exception {

        Map<Integer, ArticuloDevueltoBean> resultado = new HashMap<Integer, ArticuloDevueltoBean>();
        Connection conn = new Connection();
        conn.abrirConexion(Database.getConnection());
        List<ArticuloDevueltoBean> consultarAgrupadoUidTicketLst = ArticulosDevueltosDao.consultarAgrupadoUidTicket(conn, ticketUid);

        // Introducimos los resultados en un mapa
        for (ArticuloDevueltoBean art : consultarAgrupadoUidTicketLst) {
            resultado.put(art.getIdLinea(), art);
        }

        return resultado;
    }

    public static void actualizarTiketFacturaOriginal(com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion, List<ArticuloDevueltoBean> deNadaAPendienteEnvioDomicilio) {
        try {
            DocumentosBean documentoG = DocumentosService.consultarDoc(DocumentosImpresosBean.TIPO_FACTURA, devolucion.getTicketOriginal().getTienda(), devolucion.getTicketOriginal().getCodcaja(), String.valueOf(devolucion.getTicketOriginal().getId_ticket()));

            if (documentoG != null) {
                List<DocumentosImpresosBean> impresos = documentoG.getImpresos();
                if (impresos != null) {
                    for (int i = 0; i < impresos.size(); i++) {
                        if (impresos.get(i).isTipoDocumentoFactura()) {
                            byte[] a = null;
                            a = impresos.get(i).getImpreso();
                            if (a != null) {
                                File xml = UtilInputOutputStream.transformardebytesAfile(a);
                                Document documento = leerXmlyactualizarEntrega(xml, deNadaAPendienteEnvioDomicilio);

                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                Result output = new StreamResult(xml);
                                Source input = new DOMSource(documento);
                                transformer.transform(input, output);

                                byte[] result = null;
                                result = UtilInputOutputStream.trasformarFileabytes(xml);
                                impresos.get(i).setImpreso(result);

                                DocumentosService.updateDocumentosImpresos(impresos.get(i));

                                log.debug("Documento impreso actualizado correctamente.");
                            } else {
                                JPrincipal.getInstance().crearError("Xml blob no encontrador.");
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al leer xml y actulaizar el documento impreso." + e);
            JPrincipal.getInstance().crearError("Error al leer y actualizar el documento impreso.");
        }
    }

    public static File transformardebytesAfile(byte[] by) throws IOException {
        File archivoDestino = new File("tmp");
        OutputStream out = new FileOutputStream(archivoDestino);
        out.write(by);
        out.close();
        return archivoDestino;
    }

    public static byte[] trasformarFileabytes(File archi) throws FileNotFoundException, IOException {

        FileInputStream ficheroStream = new FileInputStream(archi);
        byte contenido[] = new byte[(int) archi.length()];
        ficheroStream.read(contenido);
        return contenido;
    }

    public static Document leerXmlyactualizarEntrega(File xml, List<ArticuloDevueltoBean> deNadaAPendienteEnvioDomicilio) throws ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        try {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(xml);
            } catch (SAXException e1) {
                log.error("Error de codificación en el fichero: " + e1.getMessage());
            } catch (IOException e2) {
                log.error("Error de codificación en el fichero: " + e2.getMessage());
            } catch (Exception e3) {
                log.error("Error de codificación en el fichero: " + e3.getMessage());
            }

            Node raiz = null;
            raiz = doc.getElementsByTagName("output").item(0);
            Node nodeTick = null;
            nodeTick = doc.getElementsByTagName("ticket").item(0);
            Element elementTick = null;

            Node lineaAnterior = null;
            Element lineaAnteriorElement = null;
            Node hijoLineaAnterior = null;

            Element lineaX = null;
            NodeList nodosTexto = null;
            int cantidad = 0;

            if (raiz != null) {
                if (raiz.getNodeName().equals("output")) {
                    if (nodeTick != null) {
                        if (nodeTick.getNodeName().equals("ticket")) {
                            elementTick = (Element) nodeTick;

                            //For por las lineas
                            for (int i = 0; i < elementTick.getElementsByTagName(Constantes.LINE).getLength(); i++) {
                                lineaX = null;
                                nodosTexto = null;
                                cantidad = 0;

                                short tipoNodo = elementTick.getElementsByTagName(Constantes.LINE).item(i).getNodeType();
                                if (tipoNodo == Node.ELEMENT_NODE) {
                                    lineaX = (Element) elementTick.getElementsByTagName(Constantes.LINE).item(i);
                                    nodosTexto = lineaX.getElementsByTagName(Constantes.TEXT);
                                    cantidad = lineaX.getElementsByTagName(Constantes.TEXT).getLength();
                                }
                                //Se asegura que es una linea de item
                                if (cantidad == 6) {
                                    Formatter fmt = new Formatter();
                                    Formatter fmt2 = new Formatter();
                                    String codMarcaItemEnXML = "";
                                    String codMarca_ItemXML = "";
                                    String codItem_ItemXML = "";
                                    int idLineaXML;
                                    codMarca_ItemXML = fmt.format("%04d", Integer.parseInt(nodosTexto.item(1).getTextContent().trim())).toString();
                                    codItem_ItemXML = fmt2.format("%04d", Integer.parseInt(nodosTexto.item(2).getTextContent().trim())).toString();
                                    codMarcaItemEnXML = codMarca_ItemXML + "." + codItem_ItemXML;
                                    idLineaXML = Integer.parseInt(nodosTexto.item(0).getTextContent().trim());
                                    //Pasar de Nada a Pendiente Envio
                                    for (int j = 0; j < deNadaAPendienteEnvioDomicilio.size(); j++) {
                                        if (deNadaAPendienteEnvioDomicilio.get(j).getCodart().equals(codMarcaItemEnXML) && deNadaAPendienteEnvioDomicilio.get(j).getIdLinea() == idLineaXML) {
                                            //Encontro el item
                                            lineaAnterior = null;
                                            lineaAnteriorElement = null;
                                            hijoLineaAnterior = null;
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (!hijoLineaAnterior.getTextContent().equals("********** N/C CANTIDAD: " + deNadaAPendienteEnvioDomicilio.get(j).getCantidad() + " **********")) {
                                                            if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") || hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.replaceChild(agregarLineaNotaCredito(doc, deNadaAPendienteEnvioDomicilio.get(j).getCantidad()), lineaAnterior);
                                                            } else {
                                                                elementTick.insertBefore(agregarLineaNotaCredito(doc, deNadaAPendienteEnvioDomicilio.get(j).getCantidad()), lineaX);
                                                            }
                                                            deNadaAPendienteEnvioDomicilio.remove(j);
                                                            j--;
                                                        }
                                                    } else {
                                                        lineaAnteriorElement = (Element) lineaAnterior;
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") || hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.replaceChild(agregarLineaNotaCredito(doc, deNadaAPendienteEnvioDomicilio.get(j).getCantidad()), lineaAnterior);
                                                        } else {
                                                            elementTick.insertBefore(agregarLineaNotaCredito(doc, deNadaAPendienteEnvioDomicilio.get(j).getCantidad()), lineaX);
                                                        }
                                                        deNadaAPendienteEnvioDomicilio.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                        }
                    } else {
                        JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } catch (Exception e) {
            log.error("Error al leer xml y actulaizar el documento impreso ." + e);
        }

        return doc;
    }

    public static Element agregarLineaNotaCredito(Document doc, Integer cantidad) {
        //Elemento entrega a domicilio
        Element elementLineEntregaDomicilio = doc.createElement(Constantes.LINE);
        Element elementTextEntregaDomicilio = doc.createElement(Constantes.TEXT);
        elementTextEntregaDomicilio.setAttribute("align", "left");
        elementTextEntregaDomicilio.setAttribute("length", "40");
        elementTextEntregaDomicilio.setTextContent("********** N/C CANTIDAD: " + cantidad + " **********");
        elementLineEntregaDomicilio.appendChild(elementTextEntregaDomicilio);
        return elementLineEntregaDomicilio;
    }

    /**
     * @author Gabriel Simbania
     * @param devolucion
     */
    public static void generarKardexVentas(com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion) {

        try {
            log.debug("Creando kardex de la nota de credito");

            List<ItemDTO> itemDTOLista = new ArrayList<>();

            for (LineaTicket linea : devolucion.getTicketDevolucion().getLineas().getLineas()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setCantidad((long) linea.getCantidad());
                itemDTO.setCodigoI(linea.getArticulo().getCodmarca().getCodmarca() + "-" + linea.getArticulo().getIdItem());
                itemDTOLista.add(itemDTO);
            }

            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(devolucion.getTicketDevolucion().getCajero().getUsuario());
            NotasCredito notasCredito = devolucion.getNotaCredito();
            String numeroDocumento = Sesion.getTienda().getCodalm() + notasCredito.getCodcaja() + String.format("%09d", notasCredito.getIdNotaCredito());
            DocumentoDTO documentoDTO = new DocumentoDTO(numeroDocumento, Sesion.getTienda().getCodalmSRI(), null, itemDTOLista, usuarioDTO);
            documentoDTO.setTipoMovimiento(Constantes.MOVIMIENTO_51);
            documentoDTO.setTipoDocumento(EnumTipoDocumento.NOTA_CREDITO.getNombre());

            String documentoString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread encolarKardexThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), documentoString, Variables.getVariable(Variables.QUEUE_KARDEX_POS), Constantes.PROCESO_KARDEX_POS, notasCredito.getUidNotaCredito());
            encolarKardexThread.start();

        } catch (Throwable e) {
            log.error("Error en el kardex de la nota de credito " + e.getMessage());
        }
    }

    /**
     * @author Gabriel Simbania Genera el mensaje JMS para realizar el
     * intercambio
     * @param lugSRIOrigen
     * @param lugSRIDestino
     * @param documentoDTO
     * @param uidNotaCredito
     */
    public static void generarIntercambioPorDevolucionOtroLocal(DocumentoDTO documentoDTO, String lugSRIOrigen, String lugSRIDestino, String uidNotaCredito) {

        try {
            log.debug("Creando kardex de la nota de credito");

            for (ItemDTO item : documentoDTO.getItemDtoLista()) {
                String[] itemErp = item.getCodigoI().split("\\.");
                item.setCodigoI(Long.parseLong(itemErp[0]) + "-" + Long.parseLong(itemErp[1]));
            }

            IntercambioDTO intercambioDTO = new IntercambioDTO();
            intercambioDTO.setLugSRIOrigen(lugSRIOrigen);
            intercambioDTO.setLugSRIDestino(lugSRIDestino);
            intercambioDTO.setItemDtoLista(documentoDTO.getItemDtoLista());
            intercambioDTO.setUsuarioDTO(documentoDTO.getUsuarioDTO());
            intercambioDTO.setNumeroDocumento(documentoDTO.getNumeroDocumento());

            String intercambioString = JsonUtil.objectToJson(intercambioDTO);
            ProcesoEncolarThread encolarIntercambioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), intercambioString, Variables.getVariable(Variables.QUEUE_INTERCAMBIO_NOTA_CREDITO), Constantes.PROCESO_INTERCAMBIO_NOTA_CREDITO, uidNotaCredito);
            encolarIntercambioThread.start();

        } catch (Throwable e) {
            log.error("Error en el kardex de la nota de credito " + e.getMessage());
        }
    }

    /**
     * @author Gabriel Simbania Genera la nota de credito a paratir del
     * NotaCreditoLocalDTO
     * @param notaCreditoLocalDTO
     * @return
     * @throws java.lang.Exception
     */
    public static ResponseDTO generaNotaCreditoDesdeOtroLocal(NotaCreditoLocalDTO notaCreditoLocalDTO) throws Exception {

        DocumentoDTO documentoDTO = notaCreditoLocalDTO.getDocumentoDTO();
        String numeroFactura[] = documentoDTO.getNumeroDocumento().split("-");
        MotivoDevolucionDTO motivoDTO = notaCreditoLocalDTO.getMotivoDevolucionDTO();
        MotivoDevolucion motivoDevolucion = new MotivoDevolucion(motivoDTO.getIdMotivo(), motivoDTO.getDescripcionMotivo());
        if (motivoDevolucion == null) {
            throw new SocketTPVException(EnumNotaCreditoError.ERROR_SIN_MOTIVO.getCodigo(), EnumNotaCreditoError.ERROR_SIN_MOTIVO.getDescripcion());
        }
        String codAlm = numeroFactura[0];
        String codCaja = numeroFactura[1];
        String idDocumento = String.valueOf(Integer.parseInt(numeroFactura[2]));
        com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion = Sesion.iniciaNuevaDevolucionPorOtroLocal(codAlm, codCaja, idDocumento, motivoDevolucion, notaCreditoLocalDTO.getEstadoMercaderia(), notaCreditoLocalDTO.getObservaciones(), notaCreditoLocalDTO.getTipoDevolucion(), notaCreditoLocalDTO.getLugSRIGenera());
        List<LineaTicket> lineasAgregar = new ArrayList<>();
        List<ItemDTO> itemsListaSeleccionado = new ArrayList<>();
//        ImpuestosFact imp = null;
//        imp = ImpuestosServices.consultaImpuestos(ticket.getUid_ticket());
//        if (imp != null) {
//            lineaOrigen.setImporteIce(imp.getTarifa());
//        }
//        devolucion.getTicketOriginal().getTotales().getImpuestosIce();
//        devolucion.getTicketDevolucion().getTotales().getImpuestosIce();

        for (LineaTicket linea : devolucion.getTicketDevolucion().getLineas().getLineas()) {
            for (ItemDTO itemDTO : documentoDTO.getItemDtoLista()) {
                if (Objects.equals(linea.getLineaOriginal(), itemDTO.getIdLinea()) && itemDTO.getCodigoI().equals(linea.getArticulo().getCodart())) {
                    BigDecimal orignalPrecioTarifaOrigen = linea.getPrecioTarifaOrigen().setScale(2, BigDecimal.ROUND_HALF_UP);
                    linea.setPrecioTarifaOrigen(itemDTO.getImporteFinal().setScale(2, BigDecimal.ROUND_HALF_UP));
                    linea.setPrecioTotalTarifaOrigen(itemDTO.getImporteTotalFinal().setScale(2, BigDecimal.ROUND_HALF_UP));
                    linea.setCantidad(itemDTO.getCantidad().intValue());
                    linea.recalcularPrecios();
                    linea.setDescuentoFinalDev(linea.getDescuentoFinalDev());
                    linea.redondear();
                    devolucion.getTicketDevolucion().getTotales().recalcularTotalesLineas(devolucion.getTicketDevolucion().getLineas());
                    devolucion.getTicketDevolucion().getTotales().redondear();
                    linea.setPrecioTotalTarifaOrigen(linea.getImporte().setScale(2, BigDecimal.ROUND_HALF_UP));
                    linea.setPrecioTarifaOrigen(orignalPrecioTarifaOrigen);
                    lineasAgregar.add(linea);
                    itemsListaSeleccionado.add(itemDTO);
                }
            }
        }

        if (lineasAgregar.isEmpty()) {
            throw new SocketTPVException(EnumNotaCreditoError.ERROR_SIN_ITEMS.getCodigo(), EnumNotaCreditoError.ERROR_SIN_ITEMS.getDescripcion());
        }
        devolucion.getTicketDevolucion().getLineas().setLineas(lineasAgregar);
        devolucion.getTicketDevolucion().recalcularTotales();
        devolucion.getTicketDevolucion().getTotales().recalcularTotalesLineas(devolucion.getTicketDevolucion().getLineas());
        devolucion.getTicketDevolucion().getTotales().redondear();
        devolucion.getFormasPago().addAll(documentoDTO.getMedioPagoLista());
        devolucion.setAutorizador(notaCreditoLocalDTO.getUsuarioAutoriza());
        UsuarioBean usuario = new UsuarioBean(notaCreditoLocalDTO.getUsuarioAutoriza(), notaCreditoLocalDTO.getUsuarioAutoriza());
        devolucion.setAutorizadorDevolucion(usuario);
        DevolucionesServices.crearDevolucion(devolucion, true);

        //G.S. Se procede a colocar en procesado en 'S' para que sincronice, ya que el otro local
        // va a ser el encargado de sincronizar
        DevolucionesServices.realizarActualizarNCProcesado(devolucion.getNotaCredito(), 'S');

        UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(devolucion.getTicketDevolucion().getCajero().getUsuario());
        usuarioDTO.setCodEmpleado(Sesion.getUsuario().getIdUsuario());

        ParametrosDocumentos paramDocumentos = new ParametrosDocumentos();
        paramDocumentos.setTipo(DocumentosBean.NOTA_CREDITO);
        paramDocumentos.setNumTransaccion(devolucion.getNotaCredito().getCodalm() + "-" + devolucion.getNotaCredito().getCodcaja() + "-" + String.format("%09d", devolucion.getNotaCredito().getIdNotaCredito()));
        List<DocumentosImpresosBean> documentos = DocumentosService.consultarDocumentosImpresosByNumero(paramDocumentos);
        DocumentosBean documentosBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.NOTA_CREDITO, codAlm, devolucion.getNotaCredito().getCodcaja(), String.valueOf(devolucion.getNotaCredito().getIdNotaCredito()));

        NotaCreditoResponseDTO notaCreditoResponseDTO = new NotaCreditoResponseDTO();
        notaCreditoResponseDTO.setNotasCredito(devolucion.getNotaCredito());
        notaCreditoResponseDTO.setDocumentosBean(documentosBean);
        notaCreditoResponseDTO.setDocumentosImpresos(documentos);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setExito(Boolean.TRUE);
        responseDTO.setObjetoRespuesta(notaCreditoResponseDTO);

        DocumentoDTO notaCreditoDTO = new DocumentoDTO((devolucion.getNotaCredito().getCodalm() + "-" + devolucion.getNotaCredito().getCodcaja() + "-" + devolucion.getNotaCredito().getIdNotaCredito()), devolucion.getNotaCredito().getCodalm(), "GENERADO DESDE OTRO LOCAL", itemsListaSeleccionado, usuarioDTO);
        DevolucionesServices.generarIntercambioPorDevolucionOtroLocal(notaCreditoDTO, notaCreditoLocalDTO.getLugSRIOriginal(), notaCreditoLocalDTO.getLugSRIGenera(), devolucion.getNotaCredito().getUidNotaCredito());

        return responseDTO;
    }

    public static void generarNotaCreditoOtroLocal(DocumentoDTO documentoDTO, EntityManager em) throws Exception {
        ResponseDTO response = null;
        NotaCreditoResponseDTO notaCreditoResponseDTO;
        try {
            log.info("INGRESO A GENERAR LA N/C DESDE OTRO LOCAL");
            URL url = new URL(Variables.getVariable(Variables.WEBSERVICE_NOTACREDITO_URL) + "generarNotaCredito/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            NotaCreditoLocalDTO creditoLocalDTO = new NotaCreditoLocalDTO();
            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(Sesion.getUsuario().getUsuario());
            documentoDTO.setBlob(null);
            creditoLocalDTO.setDocumentoDTO(documentoDTO);
            creditoLocalDTO.setLugSRIOriginal(documentoDTO.getNumeroDocumento().split("-")[0]);
            creditoLocalDTO.setLugSRIGenera(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            creditoLocalDTO.setCodCajaGenera(Sesion.cajaActual.getCajaActual().getCodcaja());
            creditoLocalDTO.setUsuarioDTOGenera(usuarioDTO);
            creditoLocalDTO.setMotivoDevolucionDTO(documentoDTO.getMotivoDevolucionDTO());
            creditoLocalDTO.setEstadoMercaderia(documentoDTO.getEstadoMercaderia());
            creditoLocalDTO.setObservaciones(documentoDTO.getObservacionNC() + " CREADO: " + Sesion.getUsuario().getUsuario() + " AUTORIZA: " + documentoDTO.getUsuarioAutoriza());
            creditoLocalDTO.setTipoDevolucion(documentoDTO.getTipoDevolucion());
            creditoLocalDTO.setUsuarioAutoriza(documentoDTO.getUsuarioAutoriza());
            String input = JsonUtil.objectToJson(creditoLocalDTO);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = "";
            while ((output = br.readLine()) != null) {
                response = JsonUtil.jsonToObject(output, ResponseDTO.class);
            }
            log.info("FINALIZO LA CONSULTA AL WEB SERVICES");
            if (response.getExito()) {

                String jsonObject = JsonUtil.objectToJson(response.getObjetoRespuesta());
                notaCreditoResponseDTO = JsonUtil.jsonToObject(jsonObject, NotaCreditoResponseDTO.class);
                log.info("INICIO DE LA TRANSACCION");

                em.getTransaction().begin();
                notaCreditoResponseDTO.getNotasCredito().setProcesado('N');//Para que envie a sincronizar
                em.persist(notaCreditoResponseDTO.getNotasCredito());
                em.getTransaction().commit();
                log.info("FIN DE LA TRANSACCION");

                log.info("INICIO DE LA IMPRESION");
                DocumentosService.crearDocumentosOtroLocal(notaCreditoResponseDTO.getDocumentosBean());
                DocumentosService.crearDocumentoImpresoOtroLocal(notaCreditoResponseDTO.getDocumentosImpresos());
                PrintServices.getInstance().impresionNotaCreditoDesdeOtroLocal(notaCreditoResponseDTO.getDocumentosBean(), false, "");

                log.info("FIN DE LA IMPRESION");
            } else {
                conn.disconnect();
                throw new DocumentoException(response.getDescripcion());
            }
            conn.disconnect();

        } catch (ConnectException ex) {
            log.info("Eror en la coneccion", ex);
            throw new Exception("Error al conectar con otro local");
        } catch (MalformedURLException e) {
            log.info("Eror mal formado", e);
            throw new Exception("Error al conectar con otro local");
        } catch (IOException e) {
            log.info("Eror I/O ", e);
            throw new Exception("Error al conectar con otro local");
        } catch (DocumentoException ex) {
            log.info("Eror documento", ex);
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Actualiza el campo de procesado en la N/C  </p>
     *
     * @param notasCredito
     * @param procesado
     * @throws DevolucionException
     */
    private static void realizarActualizarNCProcesado(NotasCredito notasCredito, Character procesado) throws DevolucionException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            notasCredito.setProcesado(procesado);
            em.merge(notasCredito);
            em.getTransaction().commit();
        } catch (Exception e) {
            String msg = "Error creando nueva devolución.";
            log.error(msg, e);
            em.getTransaction().rollback();
            throw new DevolucionException(msg, e);
        } finally {
            em.close();
        }

    }
}
