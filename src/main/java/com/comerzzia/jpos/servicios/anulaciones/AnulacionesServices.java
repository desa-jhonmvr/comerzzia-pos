/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.anulaciones;

import com.comerzzia.jpos.dto.ventas.TramaCreditoDTO;
import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.CupoVirtual;
import com.comerzzia.jpos.entity.db.Devolucion;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardBean;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.persistencia.devoluciones.DevolucionesDao;
import com.comerzzia.jpos.persistencia.cajas.CajaException;
import com.comerzzia.jpos.persistencia.cajas.GestionDeCajasDao;
import com.comerzzia.jpos.persistencia.cajas.MovimientoCajaException;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoException;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualDao;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticulosDevueltosDao;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.garantia.GarantiasDao;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardDao;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardBean;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardDao;
import com.comerzzia.jpos.persistencia.guiasremision.GuiaRemisionDao;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.notacredito.NotaCreditoDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.plannovios.PlanNovioDao;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import com.comerzzia.jpos.servicios.giftcard.GiftCardException;
import com.comerzzia.jpos.servicios.giftcard.ServicioGiftCard;
import com.comerzzia.jpos.servicios.letras.LetraCambioException;
import com.comerzzia.jpos.servicios.letras.LetraCambiosServices;
import com.comerzzia.jpos.servicios.login.CajaSesion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.promociones.puntos.ServicioPuntos;
import com.comerzzia.jpos.servicios.reservaciones.ReservaNotFoundException;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioServices;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author amos
 */
public class AnulacionesServices {

    private static Logger log = Logger.getMLogger(AnulacionesServices.class);
    public static final String CONCEPTO_ANULACION = "ANU";
    public static String DOCUMENTO_ANULACION = "ANU";
    public static final String FACTURA = "FACTURA";
    public static final String NOTA_CREDITO = "NOTA_CRED";
    public static final String CREDITO_ABONO = "CRED_ABONO";
    public static final String LETRA_ABONO = "LETR_ABONO";
    public static final String COMP_GIFTCARD = "GIFTCARD_COMP";
    public static final String RECA_GIFTCARD = "GIFTCARD_RECA";
    public static final String ABONO_RESERVA = "RE_ABONO";
    public static final String ABONO_PLAN_NOVIO = "PN_ABONO";
    public static final String BONO_RESERVA = "RE_BONO";

    public static void anularNotaCreditoBebemundo(String codAlmacen, String codCaja, Long idNota) throws ValidationException, AnulacionException {

        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + String.valueOf(idNota);
        CajaSesion cajaSesion = Sesion.getCajaActual();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!cajaSesion.isCajaAbierta() || !cajaSesion.isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular la nota de crédito.");
            }
            em.getTransaction().begin();
            NotasCredito nc = DevolucionesServices.consultarNotaCreditoAnulacion(codAlmacen, codCaja, idNota);

            if (nc.getAnulado() == 'S') {
                throw new ValidationException("La nota de crédito ya ha sido anulada");
            }

            Fecha fechaNC = new Fecha(nc.getFecha());
            Fecha hoy = new Fecha();

            // Si No estamos en el mismo dia
            if (!(fechaNC.getDia() == hoy.getDia() && fechaNC.getMesNumero() == hoy.getMesNumero() && fechaNC.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede Anular una factura a fecha distinta de hoy. Fecha de NC: " + fechaNC.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("La nota de crédito ha de ser de fecha de hoy para tramitar una Anulación");
            }
            // Obtener factura para comprobar si es por el importe total
            if (nc.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("La nota de crédito ya fue utilizada. ");
            }
            TicketOrigen to = null;
            try {
                Devolucion devolucion = DevolucionesDao.consultarDevolucion(em, nc);
                TicketsAlm ticket = TicketsDao.consultarTicket(em, devolucion.getUidTicket());
                to = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticket.getTicket()));
                BigDecimal olgura = new BigDecimal("0.1");
                BigDecimal limiteSuperior = nc.getSaldo().add(olgura);
                BigDecimal limiteInferior = nc.getSaldo().subtract(olgura);

                if (to.getTotales().getTotalPagado().compareTo(limiteSuperior) > 0
                        || to.getTotales().getTotalPagado().compareTo(limiteInferior) < 0) {
                    throw new ValidationException("<html>La nota de crédito ha de ser por el total <br/>de la factura para poder hacer una anulación</html>");
                }
            } catch (NoResultException e) {
                throw new ValidationException("No se encontró ningún ticket asociado a la Nota de Crédito");
            } catch (ValidationException e) {
                throw e;
            } catch (TicketException e) {
                throw new ValidationException("No se encontró ningún ticket asociado a la Nota de Crédito");
            } catch (Exception e) {
                throw new Exception();
            }

            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaFactura(to.getUid_ticket());
            anularPagos(em, null, to.getListaPagos(), referencia);
            em.flush();
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, cajaSesion.getCajaActual().getUidDiarioCaja());
            CajaDet apunte = new CajaDet(cajaSesion, numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, nc.getSaldo(), MediosPago.getInstancia().getPagoNotaCredito(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunte, em);

            // actualizamos nota de crédito
            nc.setSaldo(BigDecimal.ZERO);
            nc.setSaldoUsado(null);
            // Establecemos la NC como anulada
            nc.setAnulado('S');

            DevolucionesDao.modificaNotaCredito(nc, em);

            //Actualizamos el documento reimpreso asociado a la nota de credito
            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.NOTA_CREDITO, codAlmacen, codCaja, String.valueOf(idNota));
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            em.getTransaction().commit();
        } catch (ValidationException e) {
            log.warn("anularNotaCreditoBebemundo() - " + e.getMessage());
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (NotaCreditoException e) {
            log.error("anularNotaCreditoBebemundo() - Error en la Anulacion: Error en la lectura/validación/actualización de la Nota de Crédito ", e);
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en la lectura/validación/actualización de la Nota de Crédito ", e);
        } catch (MovimientoCajaException e) {
            log.error("anularNotaCreditoBebemundo() - Error en la Anulacion: Error en el registro de movimientos de caja", e);
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en el registro de movimientos de caja", e);
        } catch (CajaException e) {
            log.error("anularNotaCreditoBebemundo() - Error en la Anulacion: Error en el registro de movimientos de caja", e);
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en el registro de movimientos de caja", e);
        } catch (Exception e) {
            log.error("anularNotaCreditoBebemundo() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularNotaCreditoSukasa(String codAlmacen, String codCaja, String idNota, String motivo, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + idNota;
        CajaSesion cajaSesion = Sesion.getCajaActual();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        Connection connSukasa = new Connection();
        SqlSession sql = new SqlSession();
        Boolean vienePlanNovio = false;
        try {
            if (!cajaSesion.isCajaAbierta() || !cajaSesion.isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular la nota de crédito.");
            }
            log.debug("anularNotaCreditoSukasa() - Realizando anulación de nota de crédito: " + codAlmacen + "-" + codCaja + "-" + idNota);
            connSukasa.abrirConexion(Database.getConnectionSukasa());

            em.getTransaction().begin();
            NotasCredito nc = DevolucionesDao.consultarNotaCreditoWithEm(codAlmacen, codCaja, new Long(idNota), em);

            if (nc.getAnulado() == 'S') {
                throw new ValidationException("La nota de crédito ya ha sido anulada");
            }

            if (nc.geteNotaCredito() != null) {
                throw new ValidationException("<html>La nota de crédito no se puede anular <br/>porque tiene un documento electrónico asociado</html>");
            }
            Fecha fechaNC = new Fecha(nc.getFecha());
            Fecha hoy = new Fecha();

            // Si No estamos en el mismo dia
            if (!(fechaNC.getDia() == hoy.getDia() && fechaNC.getMesNumero() == hoy.getMesNumero() && fechaNC.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede Anular una nota de crédito a fecha distinta de hoy. Fecha de NC: " + fechaNC.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("La nota de crédito ha se ser de fecha de hoy para tramitar una Anulación");
            }
            // Comprobamos que la nota de crédito no se haya usado
            if (nc.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("La nota de crédito ya fue utilizada. ");
            }

            Devolucion devolucion = DevolucionesDao.consultarDevolucion(em, nc);
            if (devolucion.getEstadoMercaderia() != null && devolucion.getEstadoMercaderia().indexOf("IDPLAN") != -1) {
                vienePlanNovio = true;
            }
            TicketsAlm ticket = TicketsDao.consultarTicket(em, devolucion.getUidTicket());
            TicketOrigen to = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticket.getTicket()));

            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, cajaSesion.getCajaActual().getUidDiarioCaja());
            CajaDet apunte = new CajaDet(cajaSesion, numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, nc.getSaldo(), MediosPago.getInstancia().getPagoNotaCredito(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunte, em);

            conn = Connection.getConnection(em);

            //Borrar de X_ARTICULOS_DEVUELTOS los artículos que se han devuelto
            ArticulosDevueltosDao.deletePorUidNotaCredito(conn, devolucion.getUidNotaCredito());

            if (vienePlanNovio) {
                //Cambiar la cabecera del plan Novio para volverlo a abrir y anular la factura asociada al plan novio, borrar de x_devoluciones la linea correspondiente y anular la nota de crédito
                String[] estadoMercaderia = devolucion.getEstadoMercaderia().split(":");
                String codAlm = estadoMercaderia[1].split("-")[0].trim();
                BigInteger idPlan = new BigInteger(estadoMercaderia[1].split("-")[1].trim());
                PlanNovio plan = PlanNovioDao.consultaPlanNovio(em, codAlm, idPlan);

                plan.setLiquidado('N');
                plan.setFechaLiquidacion(null);
                plan.setProcesado('N');
                PlanNovioDao.modifica(em, plan);

                nc.setProcesado('N');
                nc.setAnulado('S');
                nc.setSaldo(BigDecimal.ZERO);
                nc.setSaldoUsado(null);
                nc.setMotivoAnulacion(motivo);
                nc.setFechaAnulacion(new Date());
                nc.setAutorizadorAnulacion(Sesion.getAutorizadorAnulacion().getUsuario());
                DevolucionesDao.modificaNotaCredito(nc, em);

                //Factura asociada a la liquidación del plan novio
                anularFactura(ticket.getCodAlm(), ticket.getCodCaja(), ticket.getIdTicket(), em, connSukasa, conn, true, motivo, observacion);

                //Actualizamos el documento reimpreso asociado a la nota de credito y al Plan novio liquidacion
                sql.openSession(SessionFactory.openSession(conn));

                DocumentosBean documentoBeanNC = DocumentosService.consultarDocByUniqueKey(DocumentosBean.NOTA_CREDITO, nc.getCodalm(), nc.getCodcaja(), String.valueOf(nc.getIdNotaCredito()));
                documentoBeanNC.setEstado("A");
                DocumentosService.updateDocumentosWithSql(documentoBeanNC, sql);

                DocumentosBean documentoBeanPN = DocumentosService.consultarDocByUniqueKey(DocumentosBean.LIQUIDACION_PN, plan.getPlanNovioPK().getCodalm(), DocumentosBean.CODCAJA, String.valueOf(plan.getPlanNovioPK().getIdPlan()));
                documentoBeanPN.setEstado("A");
                DocumentosService.updateDocumentosWithSql(documentoBeanPN, sql);
            } else {
                //Actualizamos la NC                
                nc.setProcesado('N');
                nc.setAnulado('S');
                nc.setSaldo(BigDecimal.ZERO);
                nc.setSaldoUsado(null);
                nc.setFechaAnulacion(new Date());
                nc.setMotivoAnulacion(motivo);
                nc.setAutorizadorAnulacion(Sesion.getAutorizadorAnulacion().getUsuario());
                DevolucionesDao.modificaNotaCredito(nc, em);

                //Actualizamos el documento reimpreso asociado a la nota de credito
                conn = Connection.getConnection(em);
                sql.openSession(SessionFactory.openSession(conn));
                DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.NOTA_CREDITO, codAlmacen, codCaja, String.valueOf(idNota));
                documentoBean.setEstado("A");
                DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            }

            log.debug("anularNotaCreditoSukasa() - Actualizamos el kardex.");
            // Control de stock 
            try {
                List<LineaTicketOrigen> consultaLineasTicket = TicketService.consultarLineasTicket(devolucion.getUidTicket());
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionAnulacion);
                logKardex.setUsuarioAutorizacion(Sesion.getAutorizadorAnulacion().getUsuario());
                logKardex.setFactura(idNota);
                for (LineaTicketOrigen lin : consultaLineasTicket) {
                    ServicioStock.aumentaStockVenta(lin.getCodMarca(), lin.getIdItem(), lin.getCantidad(), logKardex);
                    ServicioStock.actualizaKardex(lin.getCodart().getCodart(), ServicioStock.MOVIMIENTO_51, codAlmacen, (long) (lin.getCantidad() * -1));
                }
            } catch (Exception e) {
                log.error("anularFactura() - Error modificando stock: " + e.getMessage());
            }

            em.getTransaction().commit();
            PrintServices.getInstance().imprimirComprobanteAnulacion(nc.getIdNotaCreditoCompleto(), new Fecha(nc.getFecha()), to.getListaPagos(), to.getTotales().getTotalPagado().toString(), "NOTA DE CRÉDITO");
            log.debug("anularNotaCreditoSukasa() - Nota Crédito anulada con éxito.");

            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + ticket.getIdTicket());
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION  NOTA CREDITO");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ValidationException e) {
            log.warn("anularNotaCreditoSukasa() - " + e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (NumberFormatException e) {
            log.error("anularNotaCreditoSukasa() - " + e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("No se encontró la nota de crédito.", e);
        } catch (NotaCreditoException e) {
            log.error("anularNotaCreditoSukasa() - Error en la Anulacion: Error en la lectura/validación/actualización de la Nota de Crédito ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en la lectura/validación/actualización de la Nota de Crédito ", e);
        } catch (MovimientoCajaException e) {
            log.error("anularNotaCreditoSukasa() - Error en la Anulacion: Error en el registro de movimientos de caja", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en el registro de movimientos de caja", e);
        } catch (CajaException e) {
            log.error("anularNotaCreditoSukasa() - Error en la Anulacion: Error en el registro de movimientos de caja", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en el registro de movimientos de caja", e);
        } catch (PlanNovioException e) {
            log.error("anularNotaCreditoSukasa() - Error en la Anulacion: Error en la consulta de Plan Novio", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error en la consulta de Plan Novio", e);
        } catch (NoResultException e) {
            log.debug("anularNotaCreditoSukasa() - No se encontró la nota de crédito");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("No se encontró la nota de crédito.", e);
        } catch (Exception e) {
            log.error("anularNotaCreditoSukasa() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            em.close();
            connSukasa.cerrarConexion();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularFactura(String codAlmacen, String codCaja, Long idTicket, String motivo, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + String.valueOf(idTicket);
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection connSukasa = new Connection();
        Connection conn = null;
        try {
            em.getTransaction().begin();
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            conn = Connection.getConnection(em);
            anularFactura(codAlmacen, codCaja, idTicket, em, connSukasa, conn, false, motivo, observacion);
            em.getTransaction().commit();
        } catch (ValidationException e) {
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionException e) {
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (SQLException e) {
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (Exception e) {
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    private static void anularFactura(String codAlmacen, String codCaja, Long idTicket, EntityManager em, Connection connSukasa, Connection conn, boolean vieneNotaCredito, String motivo, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + String.valueOf(idTicket);
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular la factura.");
            }
            log.debug("anularFactura() - Realizando anulación de factura: " + codAlmacen + "-" + codCaja + "-" + idTicket);

            log.debug("anularFactura() - Consultamos factura en base de datos...");
            TicketsAlm ticket = TicketService.consultarTicket(idTicket, codCaja, codAlmacen);

            log.debug("anularFactura() - Comprobamos que no se haya realizado una devolución");
            List<ArticuloDevueltoBean> articulosDevueltos = ArticulosDevueltosDao.consultarAgrupadoUidTicket(conn, ticket.getUidTicket());

            if (!articulosDevueltos.isEmpty()) {
                log.debug("anularFactura() - Anulación denegada. Se encontró una devolución previa sobre la factura a anular sobre ticket:" + ticket.getUidTicket());
                throw new ValidationException("<html>Anulación denegada. <br/>Se encontró una devolución previa sobre la factura a anular.</html>");
            }

            byte[] xmlTicket = (byte[]) ticket.getTicket();
            log.debug("anularFactura() - Procesamos XML de la factura...");
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(xmlTicket));
            Fecha fechaCreacion = ticketOrigen.getFecha();
            Fecha hoy = new Fecha();
            if (!(fechaCreacion.getDia() == hoy.getDia() && fechaCreacion.getMesNumero() == hoy.getMesNumero() && fechaCreacion.getAño().intValue() == hoy.getAño().intValue())) {
                throw new ValidationException("No se pueden anular facturas de fechas diferentes a la de hoy");
            }

            if (ticket.isAnulado()) {
                throw new ValidationException("La factura fue anulada con anterioridad");
            }
            if (ticket.geteTicket() != null) {
                throw new ValidationException("<html>La factura no se puede anular <br/>porque tiene un documento electrónico asociado</html>");
            }
            if (GuiaRemisionDao.existeGuiaRemision(em, ticket.getUidTicket())) {
                throw new ValidationException("Existe una guía de referencia de esta factura.");
            }
            if (ServicioPromocionesClientes.existeTicketDiaSocioReferenciado(ticket.getUidTicket())) {
                throw new ValidationException("<html>La factura fue utilizada como primera factura en una promoción día del socio.<br/> Debe anular la segunda factura previamente.</html>");
            }

            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaFactura(ticketOrigen.getUid_ticket());
            anularPagos(em, connSukasa, ticketOrigen.getListaPagos(), referencia);

            //Borramos las garantias extendidas que pudiera tener la factura
            GarantiasDao.delete(em, ticket.getUidTicket());
            for (LineaTicket l : ticketOrigen.getLineas()) {
                if (l.getReferenciaGarantia() != null && l.getReferenciaGarantia().getTicketOrigen() != null) {
                    GarantiasDao.delete(em, l.getReferenciaGarantia().getTicketOrigen().getUid_ticket());
                }
            }

            // Guardamos el Ticket como anulado y los movimientos de caja
            ticket.setProcesado('N');
            ticket.setAnulado('S');
            ticket.setFechaAnulacion(new Date());
            ticket.setMotivoAnulacion(motivo);
            ticket.setAutorizadorAnulacion(Sesion.getAutorizadorAnulacion().getUsuario());
            TicketService.modificarTicket(em, ticket);

            //Actualizamos el documento reimpreso asociado a la factura
            sql.openSession(SessionFactory.openSession(conn));
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.FACTURA, codAlmacen, codCaja, String.valueOf(idTicket));
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Anulamos los posibles articulos comprados en una reservacion
            ReservacionesServicios.anularCompraArticulo(sql, ticketOrigen.getUid_ticket());
            //Anulamos los posibles articulos comprados en un plan novio
            PlanNovioServices.anularCompraArticulo(em, codAlmacen, ticketOrigen.getUid_ticket());

            // Anulamos la acumulación de puntos
            String clientePuntos = ticketOrigen.getClienteAcumulacion();
            // Consultamos los puntos
            Integer puntosCliente = ServicioPuntos.consultarPuntosCliente(clientePuntos);

            if (ticketOrigen.isFacturaAcumulaPuntos()
                    && ((clientePuntos.equals(ticket.getCodCli()) && puntosCliente >= ticketOrigen.getPuntosAcumulados() - ticketOrigen.getPuntosConsumidos())
                    || (!clientePuntos.equals(ticket.getCodCli()) && puntosCliente >= ticketOrigen.getPuntosAcumulados()))) {

                ServicioPuntos.anularPuntos(sql, ticket.getUidTicket());
            } else if (ticketOrigen.isFacturaAcumulaPuntos()) {
                log.warn("anularFactura() - El cliente que acumuló los puntos de la factura no posee saldo de puntos suficiente para anular la factura");
                throw new ValidationException("Saldo de puntos insuficiente para la anulación.");
            }

            // Anulamos promociones de control a cliente de esta factura
            ServicioPromocionesClientes.anularPromocionesAplicadas(conn, sql, ticket.getUidTicket(),null);
            if (ticketOrigen.getUidTicketDiaSocio() != null) {
                ServicioPromocionesClientes.anularReferenciaFacturaDiaSocio(conn, ticketOrigen.getUidTicketDiaSocio());
            }

            PrintServices.getInstance().imprimirComprobanteAnulacion(ticketOrigen.getIdFactura(), ticketOrigen.getFecha(), ticketOrigen.getListaPagos(), ticketOrigen.getTotales().getTotalPagado().toString(), "FACTURA");

            log.debug("anularFactura() - Factura anulada con éxito.");
            log.debug("anularFactura() - Pagos y factura anulados correctamente. Actualizamos stock en KDX...");

            // Control de stock 
            try {
                List<LineaTicketOrigen> consultaLineasTicket = TicketService.consultarLineasTicket(ticket.getUidTicket());
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionAnulacion);
                logKardex.setUsuarioAutorizacion(Sesion.getAutorizadorAnulacion().getUsuario());
                logKardex.setFactura(String.valueOf(idTicket));
                for (LineaTicketOrigen lin : consultaLineasTicket) {
                    ServicioStock.aumentaStockVenta(lin.getCodMarca(), lin.getIdItem(), lin.getCantidad() * -1, logKardex);
                    ServicioStock.actualizaKardex(lin.getCodart().getCodart(), ServicioStock.MOVIMIENTO_51, codAlmacen, (long) (lin.getCantidad()));
                }
            } catch (Exception e) {
                log.error("anularFactura() - Error modificando stock: " + e.getMessage());
            }

            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + ticket.getIdTicket());
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION  FACTURA");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoResultException e) {
            // No se ha encontrado el Ticket
            log.debug("anularFactura() -No se encontró el ticket");
            throw new ValidationException("No se encontró el ticket");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularFactura() - " + e.getMessage(), e);
            throw new AnulacionException(e.getMessage(), e);
        } catch (ReservasException e) {
            log.error("anularFactura() - " + e.getMessage(), e);
            throw new AnulacionException("Error al actualizar los articulos de la reserva", e);
        } catch (PlanNovioException e) {
            log.error("AnularFactura() -" + e.getMessage(), e);
            throw new AnulacionException("Error al actualizar los articulos del plan novio", e);
        } catch (Exception e) {
            log.error("anularFactura() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        }
    }

    public static void anularAbonoReservacion(String codAlmacen, String codReserva, String idAbono, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codReserva + "/" + idAbono;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular el abono a reservación.");
            }
            log.debug("anularAbonoReservacion() - Realizando anulación de abono a reservacion: " + codAlmacen + "-" + codReserva + "/" + idAbono);
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }

            em.getTransaction().begin();

            log.debug("anularAbonoReservacion() - Consultamos reservación en base de datos...");
            ReservaBean reserva = ReservacionesServicios.consultaById(new BigInteger(codReserva));
            Reservacion reservacion = new Reservacion(reserva);

            //No podemos anular el abono si la reserva está liquidada o anulada
            if (reserva.isLiquidado()) {
                throw new ValidationException("La reservación fue liquidada con anterioridad");
            }
            if (reserva.isCancelada()) {
                throw new ValidationException("La reservación fue anulada con anterioridad");
            }

            ReservaAbonoBean abonoReserva = null;
            reservacion.calculaTotalAbonado();
            BigDecimal totalAbonado = reservacion.getTotalAbonado();
            for (ReservaAbonoBean abono : reserva.getReservaAbonoList()) {
                if (abono.getIdAbono().equals(new Long(idAbono))) {
                    abonoReserva = abono;
                    totalAbonado = totalAbonado.subtract(abono.getCantidadAbono());
                }
            }
            if (abonoReserva == null) {
                throw new ValidationException("No se encontro el abono a reservación o el abono está anulado.");
            }

            //Si la reserva requiere abono inicial, no podemos restar un abono de forma que tengamos menos cantidad que 
            // el porcentaje de abono inicial
            if (reserva.getReservaTipo().getAbonoInicial()) {
                if (totalAbonado.compareTo((reserva.getReservaTipo().getPorcentajeAbonoInicial().multiply(reservacion.getTotalReservacion())).divide(new BigDecimal(100))) < 0) {
                    throw new ValidationException("<html>No se puede anular el abono porque la reserva necesita<br/>un abono inicial de al menos " + reserva.getReservaTipo().getPorcentajeAbonoInicial() + "% del total</html>");
                }
            }

            Fecha fechaAbono = abonoReserva.getFechaAbono();
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaAbono.getDia() == hoy.getDia() && fechaAbono.getMesNumero() == hoy.getMesNumero() && fechaAbono.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular un abono reservación a fecha distinta de hoy. Fecha de Abono: " + fechaAbono.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("El abono a reservación ha se ser de fecha de hoy para tramitar una Anulación");
            }

            //La reservacion tiene que tener saldo suficiente para poder restar la cantidad del abono que se desea anular
            if (abonoReserva.getCantidadAbono().compareTo(reservacion.getAbonosRestantes()) > 0) {
                throw new ValidationException("La reservación no tiene saldo suficiente para restar el abono que se pretende anular.");
            }

            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(abonoReserva.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaAbonoReservacion(reserva, abonoReserva);
            anularPagos(em, connSukasa, pagos, referencia);

            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));

            //Actualizamos el abono
            abonoReserva.setAnulado(true);
            abonoReserva.setProcesadoTienda(false);
            ReservacionesServicios.actualizarAbonoWithSql(abonoReserva, sql);

            //Actualizamos la reserva para ponerla como no procesada
            reserva.setProcesadoTienda(false);
            ReservacionesServicios.modificarOnlyReservaWithSql(sql, reserva);

            //Actualizamos el documento reimpreso asociado al abono
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.ABONO_RESERVA, codAlmacen, DocumentosBean.CODCAJA, codReserva + "/" + idAbono);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Imprimimos el comprobante anulación
            String idDocumento = codAlmacen + "-" + codReserva + "/" + String.valueOf(idAbono);
            PrintServices.getInstance().imprimirComprobanteAnulacion(idDocumento, abonoReserva.getFechaAbono(), pagos, abonoReserva.getCantidadAbono().toString(), "ABONO A RESERVACION");

            em.getTransaction().commit();
            log.debug("anularAbonoReservacion() - Abono a reservación anulado con éxito.");

            log.debug("anularAbonoReservacion() - Pagos y abono a reservación anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + idDocumento);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION ABONO RESERVACION");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoResultException e) {
            // No se ha encontrado la Reservacion
            log.debug("anularAbonoReservacion() - No se encontró la reservación");
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la reservación");
        } catch (ReservaNotFoundException e) {
            // No se ha encontrado la Reservacion
            log.debug("anularAbonoReservacion() -No se encontró la reservación");
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la reservación");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularAbonoReservacion() - " + e.getMessage(), e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (DocumentoException e) {
            log.error("anularAbonoReservacion() - Error actualizando documento asociado al pago crédito directo ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (ReservasException e) {
            log.error("anularAbonoReservacion() - Error actualizando la reservación ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error actualizando la reservación ", e);
        } catch (XMLDocumentException e) {
            log.error("anularAbonoReservacion() - " + e.getMessage(), e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos del abono a reservacion.");
        } catch (Exception e) {
            log.error("anularAbonoReservacion() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.cerrarConexion();
            }
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularPagoCreditoDirecto(String codAlmacen, String codCaja, String identificador, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + identificador;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular el pago a crédito directo.");
            }
            log.debug("anularPagoCreditoDirecto() - Realizando anulación de pago a credito directo: " + codAlmacen + "-" + codCaja + "-" + identificador);
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            
            em.getTransaction().begin();

            log.debug("anularPagoCreditoDirecto() - Consultamos pago a credito directo en base de datos...");
            AbonoCreditoBean abonoCredito = CreditoServices.consultarAbonoCredito(codAlmacen, codCaja, new Long(identificador));
            if (abonoCredito == null) {
                throw new ValidationException("No se ha encontrado el pago a crédito directo.");
            }
            if (abonoCredito.getAnulado()) {
                throw new ValidationException("El pago a crédito directo ya ha sido anulado");
            }

            Fecha fechaPago = abonoCredito.getFecha();
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaPago.getDia() == hoy.getDia() && fechaPago.getMesNumero() == hoy.getMesNumero() && fechaPago.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular un pago a crédito directo a fecha distinta de hoy. Fecha de Pago: " + fechaPago.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("El pago a crédito directo ha se ser de fecha de hoy para tramitar una Anulación");
            }

            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(abonoCredito.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaCredito(abonoCredito);
            log.debug("anularPagoCreditoDirecto()1");
            anularPagos(em, connSukasa, pagos, referencia);
            log.debug("anularPagoCreditoDirecto()2");
            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));

            //Actualizamos el pago a crédito directo
            abonoCredito.setAnulado(true);
            abonoCredito.setProcesado(false);
            CreditoServices.updateAbonoCredito(abonoCredito, sql);

            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                CupoVirtual cupoVirtual = CupoVirtualDao.consultarCupoVirtualByCredito(em, abonoCredito.getNumCredito());
                if (cupoVirtual != null) {
                    cupoVirtual.setCupo(cupoVirtual.getCupo().subtract(abonoCredito.getTotalConDto()));
                    cupoVirtual.setProcesado("N");
                    em.merge(cupoVirtual);
                    Integer numeroCredito = abonoCredito.getNumCredito();
                    TramaCreditoDTO creditoDTO = new TramaCreditoDTO(numeroCredito, abonoCredito.getTotalConDto().negate(), Sesion.getTienda().getCodalm());
                    String tramaCredito = JsonUtil.objectToJson(creditoDTO);
                    log.info("Trama credito " + tramaCredito);
                    ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), tramaCredito, Variables.getVariable(Variables.QUEUE_CREDITO_CUPO), Constantes.PROCESO_CREDITO_CUPO, UUID.randomUUID().toString());
                    envioDomicilioThread.start();
                }
            } else {
                //Actualizamos el cupo
                log.debug("anularPagoCreditoDirecto()3");
                CupoVirtualDao.restarCupo(connSukasa, abonoCredito.getTotalConDto(), abonoCredito.getNumCredito());
                log.debug("anularPagoCreditoDirecto()4");
            }

            //Actualizamos el documento reimpreso asociado al pago crédito directo
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.CREDITO_ABONO, codAlmacen, codCaja, identificador);
            if (documentoBean != null) {
                documentoBean.setEstado("A");
                DocumentosService.updateDocumentosWithSql(documentoBean, sql);
            }

            //Imprimimos el comprobante anulación
            PrintServices.getInstance().imprimirComprobanteAnulacion(abonoCredito.getIdAbonoCredito(), abonoCredito.getFecha(), pagos, abonoCredito.getTotalSinDto().toString(), "PAGO A CRÉDITO DIRECTO");

            em.getTransaction().commit();
            log.debug("anularPagoCreditoDirecto() - Pago a crédito directo anulado con éxito.");

            log.debug("anularPagoCreditoDirecto() - Pagos y pago a crédito directo anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + identificador);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION PAGO CREDITO DIRECTO");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoResultException e) {
            // No se ha encontrado el Ticket
            log.debug("anularPagoCreditoDirecto() - No se encontró el pago a credito directo");
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró el pago a crédito directo");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularPagoCreditoDirecto() - " + e.getMessage(), e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (XMLDocumentException e) {
            log.error("anularPagoCreditoDirecto() - " + e.getMessage(), e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos del crédito directo.");
        } catch (DocumentoException e) {
            log.error("anularPagoCreditoDirecto() - Error actualizando documento asociado al pago crédito directo ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (SQLException e) {
            log.error("anularPagoCreditoDirecto() - Error actualizando cupo ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error al actualizar el cupo ", e);
        } catch (Exception e) {
            log.error("anularPagoCreditoDirecto() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            em.getTransaction().rollback();
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularCompraGiftCard(String codAlmacen, String codCaja, String idCargaGiftCard, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + idCargaGiftCard;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular la compra de GiftCard.");
            }
            log.debug("anularCompraGiftCard() - Realizando anulación de compra de GiftCard: " + codAlmacen + "-" + codCaja + "-" + idCargaGiftCard);
            
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            
            em.getTransaction().begin();
            conn = Connection.getConnection(em);

            log.debug("anularCompraGiftCard() - Consultamos compra de GiftCard en base de datos...");
            LogGiftCardBean logGiftCard = LogGiftCardDao.consultar(conn, codAlmacen, codCaja, new Long(idCargaGiftCard));
            if (logGiftCard == null || logGiftCard.getCodOperacion().equals(LogGiftCardBean.OPERACION_RECARGA)) {
                throw new ValidationException("No se ha encontrado la compra a GiftCard.");
            }
            if (logGiftCard.isAnulado()) {
                throw new ValidationException("La compra a GiftCard ya ha sido anulada");
            }

            Fecha fechaPago = logGiftCard.getFechaHora();
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaPago.getDia() == hoy.getDia() && fechaPago.getMesNumero() == hoy.getMesNumero() && fechaPago.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular una compra a GiftCard a fecha distinta de hoy. Fecha de Pago: " + fechaPago.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("La compra a GiftCard ha se ser de fecha de hoy para tramitar una Anulación");
            }
            //Comprobamos que no se hayan echo recarga con esa GiftCard
            List<LogGiftCardBean> listaLogGiftCard = LogGiftCardDao.consultarUsos(conn, logGiftCard.getIdGiftCard());
            if (listaLogGiftCard.size() > 1) {
                throw new ValidationException("La GiftCard ya se ha recargado");
            }
            //Comprobamos que no se haya usado la giftcard
            GiftCardBean giftCard = GiftCardDao.consultar(conn, logGiftCard.getIdGiftCard());
            if (!giftCard.getSaldo().equals(logGiftCard.getAbono())) {
                throw new ValidationException("La GiftCard ya se ha utilizado");
            }

            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(logGiftCard.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaGiftCard(logGiftCard.getIdGiftCard());
            anularPagos(em, connSukasa, pagos, referencia);

            sql.openSession(SessionFactory.openSession(conn));

            //Actualizamos tanto el LogGiftCard como la GiftCard
            logGiftCard.setProcesado(false);
            logGiftCard.setAnulado(true);
            giftCard.setProcesado(false);
            giftCard.setAnulado(true);
            giftCard.setSaldo(BigDecimal.ZERO);
            LogGiftCardDao.actualizarLogGiftCard(conn, logGiftCard);
            GiftCardDao.actualizarGiftCard(conn, giftCard);

            //Actualizamos el documento reimpreso asociado a la compra de giftCard
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.GIFTCARD, codAlmacen, codCaja, idCargaGiftCard);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Imprimimos el comprobante anulación
            PrintServices.getInstance().imprimirComprobanteAnulacion(logGiftCard.getIdGiftCardAsString(), logGiftCard.getFechaHora(), pagos, logGiftCard.getAbono().toString(), "COMPRA GIFTCARD");

            em.getTransaction().commit();
            log.debug("anularCompraGiftCard() - Compra a GiftCard anulado con éxito.");

            log.debug("anularCompraGiftCard() - Pagos y compra a GiftCard anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + idCargaGiftCard);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION COMPRA GIFTCARD");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (GiftCardException e) {
            // No se ha encontrado el Ticket
            log.debug("anularCompraGiftCard() - No se encontró la compra de GiftCard");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la compra de GiftCard");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularCompraGiftCard() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (XMLDocumentException e) {
            log.error("anularCompraGiftCard() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos de la compra de GiftCard.");
        } catch (DocumentoException e) {
            log.error("anularCompraGiftCard() - Error actualizando documento asociado a la compra de GiftCard ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (SQLException e) {
            log.error("anularCompraGiftCard() - Error en la consulta de GiftCard ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error al consultar la GiftCard ", e);
        } catch (Exception e) {
            log.error("anularCompraGiftCard() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularRecargaGiftCard(String codAlmacen, String codCaja, String idCargaGiftCard, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + idCargaGiftCard;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular la recarga de GiftCard.");
            }
            log.debug("anularRecargaGiftCard() - Realizando anulación de recarga de GiftCard: " + codAlmacen + "-" + codCaja + "-" + idCargaGiftCard);
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            em.getTransaction().begin();
            conn = Connection.getConnection(em);

            log.debug("anularRecargaGiftCard() - Consultamos recarga de GiftCard en base de datos...");
            LogGiftCardBean logGiftCard = LogGiftCardDao.consultar(conn, codAlmacen, codCaja, new Long(idCargaGiftCard));
            if (logGiftCard == null || logGiftCard.getCodOperacion().equals(LogGiftCardBean.OPERACION_CARGA_INICIAL)) {
                throw new ValidationException("No se ha encontrado la recarga a GiftCard.");
            }
            if (logGiftCard.isAnulado()) {
                throw new ValidationException("La recarga a GiftCard ya ha sido anulada");
            }

            Fecha fechaPago = logGiftCard.getFechaHora();
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaPago.getDia() == hoy.getDia() && fechaPago.getMesNumero() == hoy.getMesNumero() && fechaPago.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular una recarga a GiftCard a fecha distinta de hoy. Fecha de Pago: " + fechaPago.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("La recarga a GiftCard ha se ser de fecha de hoy para tramitar una Anulación");
            }

            //Comprobamos que el saldo de la GiftCard es suficiente para anular la recarga
            GiftCardBean giftCard = GiftCardDao.consultar(conn, logGiftCard.getIdGiftCard());
            if (giftCard.getSaldo().compareTo(logGiftCard.getAbono()) < 0) {
                throw new ValidationException("La GiftCard no tiene saldo suficiente para anular la recarga");
            }

            sql.openSession(SessionFactory.openSession(conn));

            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(logGiftCard.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaGiftCard(logGiftCard.getIdGiftCard());
            anularPagos(em, connSukasa, pagos, referencia);

            //Actualizamos tanto el LogGiftCard como la GiftCard
            logGiftCard.setProcesado(false);
            logGiftCard.setAnulado(true);
            giftCard.setProcesado(false);
            giftCard.setSaldo(giftCard.getSaldo().subtract(logGiftCard.getAbono()));
            LogGiftCardDao.actualizarLogGiftCard(conn, logGiftCard);
            GiftCardDao.actualizarGiftCard(conn, giftCard);

            //Actualizamos el documento reimpreso asociado a la compra de giftCard
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.GIFTCARD, codAlmacen, codCaja, idCargaGiftCard);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Imprimimos el comprobante anulación
            PrintServices.getInstance().imprimirComprobanteAnulacion(logGiftCard.getIdGiftCardAsString(), logGiftCard.getFechaHora(), pagos, logGiftCard.getAbono().toString(), "RECARGA GIFTCARD");

            em.getTransaction().commit();
            log.debug("anularRecargaGiftCard() - Compra a GiftCard anulado con éxito.");

            log.debug("anularRecargaGiftCard() - Pagos y compra a GiftCard anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + idCargaGiftCard);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION RECARGA GIFTCARD");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (GiftCardException e) {
            // No se ha encontrado el Ticket
            log.debug("anularRecargaGiftCard() -No se encontró la recarga de GiftCard");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la recarga de GiftCard");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularRecargaGiftCard() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (XMLDocumentException e) {
            log.error("anularRecargaGiftCard() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos de la recarga de GiftCard.");
        } catch (DocumentoException e) {
            log.error("anularRecargaGiftCard() - Error actualizando documento asociado a la recarga de GiftCard ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (SQLException e) {
            log.error("anularRecargaGiftCard() - Error consultando la GiftCard ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error al consultar la GiftCard ", e);
        } catch (Exception e) {
            log.error("anularRecargaGiftCard() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularPagoCreditoTemporal(String codAlmacen, String codCaja, String idAbono, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + idAbono;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular el pago a crédito temporal.");
            }
            log.debug("anularPagoCreditoTemporal() - Realizando anulación de pago a crédito temporal: " + codAlmacen + "-" + codCaja + "-" + idAbono);
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            em.getTransaction().begin();
            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));

            LetraCuotaBean letraCuota = LetraCambiosServices.consultarLetraCuota(sql, codAlmacen, codCaja, new Long(idAbono));

            if (letraCuota == null) {
                throw new ValidationException("No se ha encontrado la cuota de letra");
            }
            if (!letraCuota.getEstado().equals(LetraCuotaBean.ESTADO_COBRADA)) {
                throw new ValidationException("El estado de la letra tiene que ser cobrada");
            }

            Fecha fechaPago = letraCuota.getFechaCobro();
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaPago.getDia() == hoy.getDia() && fechaPago.getMesNumero() == hoy.getMesNumero() && fechaPago.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular un pago a crédito temporal a fecha distinta de hoy. Fecha de Pago: " + fechaPago.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("El pago a crédito temporal ha se ser de fecha de hoy para tramitar una Anulación");
            }

            //No puedes anular una cuota de letra si hay una cuota de letra cobrada con vencimiento superior 
            LetraBean letra = LetraCambiosServices.consultarLetra(letraCuota.getUidLetra());
            if (letra.getUltimaCuotaCobrada().getFechaVencimiento().despues(letraCuota.getFechaVencimiento())) {
                throw new ValidationException("<html>No se puede anular el pago a crédito temporal <br/>porque hay otro pago cobrado con fecha de vencimiento superior.</html>");
            }

            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(letraCuota.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaLetra(letra, letraCuota);
            anularPagos(em, connSukasa, pagos, referencia);

            String idLetraCuota = letraCuota.getIdLetraCuota(codAlmacen);
            Fecha fechaCobro = letraCuota.getFechaCobro();
            //Actualizamos la cuota de letra
            letraCuota.setFechaCobro(null);
            letraCuota.setEstado(LetraCuotaBean.ESTADO_PENDIENTE);
            letraCuota.setMora(BigDecimal.ZERO);
            letraCuota.setProcesado(false);
            letraCuota.setPagos(null);
            letraCuota.setIdAbono(null);
            letraCuota.setCodcajaAbono(null);
            letraCuota.setUsuarioAbono(null);

            LetraCambiosServices.actualizaLetraCuota(sql, letraCuota);

            // Comprobamos si tenemos que actualizar estado de letra
            if (!letra.isCuotasPendientesCobro()) {
                LetraCambiosServices.actualizarEstadoLetra(sql, letra.getUidLetra(), LetraBean.ESTADO_PENDIENTE);
            }

            //Actualizamos el documento reimpreso asociado a la compra de giftCard
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.LETRA_ABONO, codAlmacen, codCaja, idAbono);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Imprimimos el comprobante anulación
            PrintServices.getInstance().imprimirComprobanteAnulacion(idLetraCuota, fechaCobro, pagos, letraCuota.getValor().toString(), "PAGO A CRÉDITO TEMPORAL");

            em.getTransaction().commit();
            log.debug("anularPagoCreditoTemporal() - Pago a crédito temporal anulado con éxito.");

            log.debug("anularPagoCreditoTemporal() - Pagos y pago a crédito temporal anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + idAbono);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("PAGO CREDITO TEMPORAL");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (LetraCambioException e) {
            // No se ha encontrado la cuota de letra
            log.debug("anularPagoCreditoTemporal() -No se encontró la cuota de letra");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la cuota de letra");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularPagoCreditoTemporal() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (XMLDocumentException e) {
            log.error("anularPagoCreditoTemporal() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos del pago a crédito temporal.");
        } catch (DocumentoException e) {
            log.error("anularPagoCreditoTemporal() - Error actualizando documento asociado al pago de crédito temporal ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (SQLException e) {
            log.error("anularPagoCreditoTemporal() - Error consultando el pago a crédito temporal ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error al consultar el pago a crédito temporal ", e);
        } catch (Exception e) {
            log.error("anularPagoCreditoTemporal() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularBonoAnulacionReservacion(String codAlmacen, String codCaja, String codReservacion, String motivo, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codCaja + "-" + codReservacion;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular el bono de anulación a reservacion.");
            }
            log.debug("anularBonoAnulacionReservacion() - Realizando anulación de bono de anulación a reservación: " + codAlmacen + "-" + codCaja + "-" + codReservacion);
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            em.getTransaction().begin();
            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));

            ReservaBean reserva = ReservacionesServicios.consultaById(new BigInteger(codReservacion));
            if (reserva == null) {
                throw new ValidationException("No se encontró la reservación asociada al bono");
            }

            Bono bono = BonosDao.consultarBonoCancelacionReservacion(codAlmacen, codReservacion);
            if (bono == null) {
                throw new ValidationException("No se encontró el bono");
            }
            Fecha fechaBono = new Fecha(bono.getFechaExpedicion());
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaBono.getDia() == hoy.getDia() && fechaBono.getMesNumero() == hoy.getMesNumero() && fechaBono.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular un bono de anulación de una reservacion a fecha distinta de hoy. Fecha de Pago: " + fechaBono.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("El bono de anulación de reservacion debe ser a fecha de hoy.");
            }

            //Actualizamos tanto el bono para ponerlo a estado ANULADO como la Reserva
            bono.setProcesado('N');
            bono.setUtilizado('S');
            bono.setReferenciaUso(reserva.getUidReservacion());
            bono.setSaldoUsado(null);
            bono.setTipoReferenciaUso("ANULADO");
            BonosDao.actualizarBono(em, bono);

            reserva.setProcesado(false);
            reserva.setCancelado(false);
            reserva.setFechaLiquidacion(null);
            reserva.setBono("N");
            ReservacionesServicios.modificarOnlyReservaWithSql(sql, reserva);

            //Actualizamos el documento reimpreso asociado a la compra de reserva
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.BONO_RESERVA, codAlmacen, codCaja, codReservacion);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Actualizamos el documento asociado a la creacion de la reserva
            DocumentosBean docu = DocumentosService.consultarDocByUniqueKey(DocumentosBean.RESERVACION, reserva.getCodalm(), DocumentosBean.CODCAJA, reserva.getCodReservacion().toString());
            docu.setEstado("V");
            DocumentosService.updateDocumentosWithSql(docu, sql);

            //Imprimimos el comprobante anulación
            PrintServices.getInstance().imprimirComprobanteAnulacion(codAlmacen + "-" + codReservacion, new Fecha(bono.getFechaExpedicion()), new ArrayList<Pago>() {
            }, bono.getImporte().toString(), "BONO DE ANULACIÓN A RESERVACIÓN");

            em.getTransaction().commit();
            log.debug("anularBonoAnulacionReservacion() - Bono de anulación reservación anulado con éxito.");

            log.debug("anularBonoAnulacionReservacion() - Pagos y bono de anulación reservación anulados correctamente. Actualizamos stock...");

            //Tenemos que actualizar el Stock porque la Reserva ha vuelto a estar abierta
            try {
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionAnulacion);
                logKardex.setUsuarioAutorizacion(Sesion.getAutorizadorAnulacion().getUsuario());
                logKardex.setFactura(String.valueOf(codReservacion));
                for (ReservaArticuloBean ra : reserva.getReservaArticuloList()) {
                    if (!ra.getComprado()) {
                        log.debug("anularBonoAnulacionReservacion() - Aumentando stock de Artículo");
                        ServicioStock.aumentaStockReserva(ra.getCodMarca(), ra.getIdItem(), 1, logKardex);
                        ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_52, codAlmacen, (long) (ra.getCantidad() * -1));
                    }
                }
            } catch (StockException ex) {
                log.error("addArticulosReserva() - STOCK: No fué posible aumentar el stock reservado para el artículo");
            }

            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + codAlmacen + "-" + codCaja);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACIÓN BONO ANULACIÓN RESERVACIÓN");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (DocumentoException e) {
            log.error("anularBonoAnulacionReservacion() - Error actualizando documento asociado al bono de anulación a reservación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (SQLException e) {
            log.error("anularBonoAnulacionReservacion() - Error consultando el bono de anulación a reservación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error al consultar el bono de anulación a reservación ", e);
        } catch (NoResultException e) {
            // No se ha encontrado el Bono
            log.debug("anularBonoAnulacionReservacion() - No se encontró el bono de anulación a reservación");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró el bono de anulación a reservación");
        } catch (ReservaNotFoundException e) {
            //No se ha encontrado la Reservacion
            log.debug("anularBonoAnulacionReservacion() - No se encontró la reservación");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró la reservación asoaciada al bono");
        } catch (Exception e) {
            log.error("anularBonoAnulacionReservacion() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    public static void anularAbonoPlanNovio(String codAlmacen, String codPlanNovio, String idAbono, String observacion) throws ValidationException, AnulacionException {
        DOCUMENTO_ANULACION = "ANU:" + codAlmacen + "-" + codPlanNovio + "/" + idAbono;
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Connection conn = null;
        SqlSession sql = new SqlSession();
        try {
            if (!Sesion.getCajaActual().isCajaAbierta() || !Sesion.getCajaActual().isCajaParcialAbierta()) {
                throw new ValidationException("La caja debe estar abierta para anular el abono a plan novio.");
            }
            log.debug("anularAbonoPlanNovio() - Realizando anulación de abono a plan novio: " + codAlmacen + "-" + codPlanNovio + "/" + idAbono);
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            em.getTransaction().begin();
            log.debug("anularAbonoPlanNovio() - Consultamos plan novio en base de datos...");

            PlanNovio planNovio = PlanNovioDao.consultaPlanNovio(em, codAlmacen, new BigInteger(codPlanNovio));
            PlanNovioOBJ planNovioObj = new PlanNovioOBJ();
            planNovioObj.setPlan(planNovio);

            //No podemos anular el abono si el plan novio está liquidada
            if (planNovio.isLiquidado()) {
                throw new ValidationException("El plan novio fue liquidado con anterioridad");
            }

            AbonoPlanNovio abonoPlanNovio = null;
            planNovio.refrescaTotales();
            BigDecimal totalAbonado = planNovio.getAbonadoSinDto();
            for (AbonoPlanNovio abono : planNovio.getAbonoPlanNovioList()) {
                if (abono.getAbonoPlanNovioPK().getIdAbono().equals(new BigInteger(idAbono))
                        && abono.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getCodalm())) {
                    abonoPlanNovio = abono;
                }
            }
            if (abonoPlanNovio == null) {
                throw new ValidationException("No se encontro el abono a plan novio o el abono está anulado o el abono pertence a otro local.");
            }
            if (abonoPlanNovio.getAnulado() == 'S') {
                throw new ValidationException("El abono a plan novio ya ha sido anulado");
            }
            if (abonoPlanNovio.getEstadoLiquidacion() != null && abonoPlanNovio.getEstadoLiquidacion().equals('S')) {
                throw new ValidationException("El abono a plan novio ha sido liquidado");
            }
            Fecha fechaAbono = new Fecha(abonoPlanNovio.getFecha());
            Fecha hoy = new Fecha();
            // Si No estamos en el mismo dia
            if (!(fechaAbono.getDia() == hoy.getDia() && fechaAbono.getMesNumero() == hoy.getMesNumero() && fechaAbono.getAño().intValue() == hoy.getAño().intValue())) {
                log.warn("No se puede anular un abono a plan novio a fecha distinta de hoy. Fecha de Abono: " + fechaAbono.getString() + " fecha actual: " + hoy.getString());
                throw new ValidationException("El abono a plan novio ha se ser de fecha de hoy para tramitar una Anulación");
            }

            //El plan novio tiene que tener saldo suficiente para poder restar la cantidad del abono que se desea anular
            if (abonoPlanNovio.getCantidadConDcto().compareTo(planNovio.getAbonadoSinUtilizar()) > 0) {
                throw new ValidationException("El plan novio no tiene saldo suficiente para restar el abono que se pretende anular.");
            }

            //Anulamos los pagos del abono a plan Novio
            List<Pago> pagos = TicketXMLServices.getPagos((new XMLDocument(abonoPlanNovio.getPagos())).getRoot());
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaAbonoPlanNovios(planNovioObj, abonoPlanNovio);
            anularPagos(em, connSukasa, pagos, referencia);

            conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));

            //Actualizamos el abono a plan novio y el plan novio
            abonoPlanNovio.setProcesado('N');
            abonoPlanNovio.setAnulado('S');
            PlanNovioDao.modifica(em, abonoPlanNovio);
            //Al llamar a refrescaTotales actualizamos campos segun los abonos
            planNovio.setProcesado('N');
            planNovio.refrescaTotales();
            PlanNovioDao.modifica(em, planNovio);

            //Actualizamos el documento reimpreso asociado al abono
            DocumentosBean documentoBean = DocumentosService.consultarDocByUniqueKey(DocumentosBean.ABONO_PLAN_NOVIO, codAlmacen, DocumentosBean.CODCAJA, codPlanNovio + "/" + idAbono);
            documentoBean.setEstado("A");
            DocumentosService.updateDocumentosWithSql(documentoBean, sql);

            //Imprimimos el comprobante anulación
            String idDocumento = codAlmacen + "-" + codPlanNovio + "/" + String.valueOf(idAbono);
            PrintServices.getInstance().imprimirComprobanteAnulacion(idDocumento, new Fecha(planNovio.getFechaAlta()), pagos, abonoPlanNovio.getCantidadSinDcto().toString(), "ABONO A PLAN NOVIO");

            em.getTransaction().commit();
            log.debug("anularAbonoPlanNovio() - Abono a plan novio anulado con éxito.");

            log.debug("anularAbonoPlanNovio() - Pagos y abono a plan novio anulados correctamente.");
            //log anulación RD
            try {
                LogOperaciones logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
                logImpresiones.setFechaHora(new Date());
                logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
                logImpresiones.setReferencia("" + idDocumento);
                logImpresiones.setProcesado('N');
                if (Sesion.getNumeroAutorizador() != null) {
                    logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
                } else {
                    logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
                }
                logImpresiones.setCodOperacion("ANULACION PLAN NOVIOS");
                logImpresiones.setObservaciones(observacion);
                insertarLog(logImpresiones);
            } catch (LogException ex) {
                java.util.logging.Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoResultException e) {
            // No se ha encontrado el Plan Novio
            log.debug("anularAbonoPlanNovio() - No se encontró el plan novio");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró el plan novio");
        } catch (PlanNovioException e) {
            // No se ha encontrado el Plan Novio
            log.debug("anularAbonoPlanNovio() - No se encontró el plan novio");
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new ValidationException("No se encontró el plan novio");
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw e;
        } catch (AnulacionNoPosibleException e) {
            log.error("anularAbonoPlanNovio() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException(e.getMessage(), e);
        } catch (DocumentoException e) {
            log.error("anularAbonoPlanNovio() - Error actualizando documento asociado al abono de plan novio ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } catch (XMLDocumentException e) {
            log.error("anularAbonoPlanNovio() - " + e.getMessage(), e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error al obtener los pagos del abono de plan novio.");
        } catch (Exception e) {
            log.error("anularAbonoPlanNovio() - Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
            connSukasa.deshacerTransaccion();
            try {
                conn.deshacerTransaccion();
            } catch (Exception et) {
            }
            throw new AnulacionException("Error en la Anulacion: Error inesperado en el proceso de anulación ", e);
        } finally {
            connSukasa.cerrarConexion();
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    private static void anularPagos(EntityManager em, Connection connSukasa, List<Pago> pagosTicket, ReferenciaTicket referencia) throws TicketException, XMLDocumentException, ValidationException, CajaException, NotaCreditoException, AnulacionNoPosibleException, Exception {
        CajaSesion cajaSesion = Sesion.getCajaActual();

        BigDecimal importeADevolver = BigDecimal.ZERO;

        int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaSesion.getCajaActual().getUidDiarioCaja());

        List<PagoCredito> pagosCredito = new ArrayList<PagoCredito>();
        List<PagoCreditoSK> pagosCreditoSK = new ArrayList<PagoCreditoSK>();
        List<Pago> pagos = new ArrayList<Pago>();

        List<NotasCredito> pagosNC = new ArrayList<NotasCredito>();
        List<Bono> bonosAAnularSinUso = new ArrayList<Bono>(); // Bonos a anular sin usar
        List<Bono> bonosAAnularUsados = new ArrayList<Bono>(); // Bonos a anular que estaban usados

        List<Bono> pagosBono = new ArrayList<Bono>();          // Bonos a devolver
        List<Pago> pagoGiftCard = new ArrayList<Pago>();
        List<String> pagoLetra = new ArrayList<String>();      // uids de las letras a anular

        MedioPagoBean medioPagoLetra = null;
        MedioPagoBean medioPagoGiftCard = null;

        log.debug("anularPagos() - Procesamos pagos realizados...");
        for (Pago pag : pagosTicket) {
            boolean esTarjetaCredito = false;
            boolean esTarjetaSukasa = false;
            if (pag instanceof PagoCredito) {
                MedioPagoBean pagoVigente = MediosPago.getInstancia().getMedioPago(pag.getMedioPagoActivo().getCodMedioPago());

                if (pagoVigente.isTarjetaSukasa()) {
                    esTarjetaSukasa = true;
                } else if (pagoVigente.isTarjetaCredito()) {
                    esTarjetaCredito = true;
                }
            }
            if (esTarjetaCredito) {
                pagosCredito.add((PagoCredito) pag);
            } else if (esTarjetaSukasa) {
                pagosCreditoSK.add((PagoCreditoSK) pag);
            } else if (pag.getMedioPagoActivo().isNotaCredito()) { //NOTA DE CRÉDITO
                NotasCredito nc = NotaCreditoDao.consultarNotaCredito(pag.getReferencia());
                // Actualizamos el Saldo de la NC
                nc.setSaldo(nc.getTotal());
                nc.setSaldoUsado(null);
                nc.setProcesado('N');

                //Tenemos que actualizar el Documento asociado a la NC a vigente
                DocumentosBean docu = DocumentosService.consultarDocByUniqueKey(DocumentosBean.NOTA_CREDITO, nc.getCodalm(), nc.getCodcaja(), String.valueOf(nc.getIdNotaCredito()));
                docu.setEstado("V");
                Connection conn = Connection.getConnection(em);
                SqlSession sql = new SqlSession();
                sql.openSession(SessionFactory.openSession(conn));
                DocumentosService.updateDocumentosWithSql(docu, sql);
                // Solo buscamos abonos generados si no se consumió la nota de credito.
                if (pag.getEntregado().compareTo(pag.getUstedPaga()) != 0) {
                    // Buscamos cualquier Bono generado por esta Nota de Crédito  
                    recorreBonosGenerados(nc.getIdNotaCreditoCompleto(), referencia.getIdReferencia(), bonosAAnularSinUso, bonosAAnularUsados);
                }

                pagosNC.add(nc);
            } else if (pag.getMedioPagoActivo().isBonoEfectivo()) { //BONO

                boolean tratarBonosGenerados = false;
                try {
                    String[] par = pag.getReferencia().split("-");
                    Bono b = BonosDao.consultaBono(par[0], new Long(par[1]));
                    b.setProcesado('N');
                    b.setUtilizado('N');
                    b.setSaldoUsado(null);
                    b.setReferenciaUso(null);
                    b.setTipoReferenciaUso(null);
                    pagosBono.add(b);

                    if (pag.getEntregado().compareTo(pag.getUstedPaga()) != 0) {
                        tratarBonosGenerados = true;
                    }
                } catch (Exception e) {
                    log.error("Error consultando bono con parámetros : " + pag.getReferencia());
                    throw e;
                }

                // Tratamos los bonos generados
                if (tratarBonosGenerados) {
                    recorreBonosGenerados(pag.getReferencia(), referencia.getIdReferencia(), bonosAAnularSinUso, bonosAAnularUsados);
                }
            } else if (pag.getMedioPagoActivo().isGiftCard()) { //GIFTCARD                    
                if (medioPagoGiftCard == null) {
                    medioPagoGiftCard = MediosPago.getInstancia().getMedioPago(pag.getMedioPagoActivo().getCodMedioPago());
                }

                pagoGiftCard.add(pag);
            } else if (pag.getMedioPagoActivo().isCreditoDirecto()) { // LETRA
                //Letra de Cambio
                if (medioPagoLetra == null) {
                    medioPagoLetra = MediosPago.getInstancia().getMedioPago(pag.getMedioPagoActivo().getCodMedioPago());
                }
                pagoLetra.add(pag.getReferencia());
            } else {
                pagos.add(pag);
            }
        }

        // PROCESAMOS LAS DEVOLUCIONES                     
        log.debug("anularPagos() - Pagos con tarjeta crédito: " + pagosCredito.size()
                + " // Pagos con tarjeta sukasa  : " + pagosCreditoSK.size()
                + " // Pagos con Nota de Crédito : " + pagosNC.size()
                + " // Pagos con Bonos           : " + pagosBono.size()
                + " // Pagos con Letras de cambio: " + pagoLetra.size()
                + " // Resto de pagos (serán devueltos como efectivo): " + pagos.size());

        log.debug("anularPagos() - Anulamos pagos...");

        // Si hay bonos utilizados, lanzamos excepción
        if (bonosAAnularUsados != null && !bonosAAnularUsados.isEmpty()) {
            log.warn("anularPagos() - Existen Bonos utilizados asociados a la factura");
            throw new ValidationException("Existen Bonos utilizados asociados a la factura");
        }

        Connection conn = Connection.getConnection(em);

        // Pagos Credito Directo
        if (!pagosCreditoSK.isEmpty()) {
            for (PagoCreditoSK pag : pagosCreditoSK) {
                if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                        && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                    CupoVirtual cupoVirtual = CupoVirtualDao.consultarCupoVirtualByCredito(em, pag.getPlastico().getNumeroCredito());
                    if (cupoVirtual != null) {
                        cupoVirtual.setCupo(cupoVirtual.getCupo().add(pag.getUstedPaga()));
                        cupoVirtual.setProcesado("N");
                        em.merge(cupoVirtual);
                        Integer numeroCredito = pag.getPlastico().getNumeroCredito();
                        TramaCreditoDTO creditoDTO = new TramaCreditoDTO(numeroCredito, pag.getUstedPaga().negate(), Sesion.getTienda().getCodalm());
                        String tramaCredito = JsonUtil.objectToJson(creditoDTO);
                        log.info("Trama credito " + tramaCredito);
                        ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), tramaCredito, Variables.getVariable(Variables.QUEUE_CREDITO_CUPO), Constantes.PROCESO_CREDITO_CUPO, UUID.randomUUID().toString());
                        envioDomicilioThread.start();
                    }
                } else {
                    //Actualizamos el cupo
                    log.debug("anularPagoCreditoDirecto()3");
                    CupoVirtualDao.restarCupo(connSukasa, pag.getUstedPaga().negate(), pag.getPlastico().getNumeroCredito());
                    log.debug("anularPagoCreditoDirecto()4");
                }
                CajaDet apunte = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, pag.getUstedPaga().negate(), pag.getMedioPagoActivo(), CajaSesion.TIPO_ANULACION, 'N');
                GestionDeCajasDao.crearApunte(apunte, em);
                numerolinea++;
            }
            //conn.commit();
        }
        // Pagos Tarjeta de Crédito
        for (PagoCredito pag : pagosCredito) {
            CajaDet apunte = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, pag.getUstedPaga().negate(), pag.getMedioPagoActivo(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunte, em);
            numerolinea++;
        }
        // Pagos Nota de Crédito
        for (NotasCredito pNC : pagosNC) {
            NotaCreditoDao.actualizarNotaCredito(em, pNC);

            CajaDet apunteSalida = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, pNC.getTotal().negate(), MediosPago.getInstancia().getPagoNotaCredito(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunteSalida, em);
            numerolinea++;
        }
        // Bonos devueltos
        for (Bono b : pagosBono) {
            BonosDao.actualizarBono(em, b);

            CajaDet apunteSalida = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, b.getImporte().negate(), MediosPago.getInstancia().getPagoBono(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunteSalida, em);
            numerolinea++;
        }
        // Bonos anulados
        for (Bono b : bonosAAnularSinUso) {
            BonosDao.actualizarBono(em, b);

            CajaDet apunteEntrada = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, b.getImporte(), MediosPago.getInstancia().getPagoBono(), CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunteEntrada, em);
            numerolinea++;
        }
        // Giftcard
        for (Pago pg : pagoGiftCard) {
            GiftCardBean gc = ServicioGiftCard.consultar(pg.getReferencia());
            BigDecimal saldoAntiguo = gc.getSaldo();

            gc.setSaldo(gc.getSaldo().add(pg.getUstedPaga()));
            gc.setProcesado("N");

            GiftCardDao.actualizaSaldoJPA(em, gc, saldoAntiguo);

            CajaDet apunteEntrada = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, pg.getUstedPaga().negate(), medioPagoGiftCard, CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunteEntrada, em);
            numerolinea++;
        }
        // Letra
        for (String uidLetra : pagoLetra) {
            LetraBean letra = LetraCambiosServices.consultarLetra(uidLetra);

            LetraCambiosServices.anularLetra(conn, uidLetra);

            CajaDet apunteEntrada = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, letra.getTotal().negate(), medioPagoLetra, CajaSesion.TIPO_ANULACION, 'N');
            GestionDeCajasDao.crearApunte(apunteEntrada, em);
            numerolinea++;

        }

        // Resto de Pagos
        for (Pago pago : pagos) {
            if (!pago.getMedioPagoActivo().isAbonoReservacion()) {
                CajaDet apunte = new CajaDet(cajaSesion.getCajaActual(), cajaSesion.getCajaParcialActual(), numerolinea, CONCEPTO_ANULACION, DOCUMENTO_ANULACION, null, pago.getUstedPaga().negate(), pago.getMedioPagoActivo(), CajaSesion.TIPO_ANULACION, 'N');
                GestionDeCajasDao.crearApunte(apunte, em);
                importeADevolver = importeADevolver.add(pago.getUstedPaga());
                numerolinea++;
            }
        }

        Sesion.getCajaActual().sumaEfectivoEnCaja(importeADevolver.negate());
    }

    private static void recorreBonosGenerados(String uid, String uidTicket, List<Bono> bonosAAnularSinUso, List<Bono> bonosAAnularUsados) throws AnulacionNoPosibleException {
        try {
            Bono b = BonosDao.consultaBono(uid);
            // Anulamos el Bono
            if (b.getUtilizado() == 'N') {
                b.setProcesado('N');
                b.setUtilizado('S');
                b.setSaldoUsado(null);
                b.setReferenciaUso(uidTicket);
                b.setTipoReferenciaUso("ANULADO");
                bonosAAnularSinUso.add(b);
            } else {
                bonosAAnularUsados.add(b);
                //recorreBonosGenerados(b.getBonoPK().getCodalm() + "-" + b.getBonoPK().getIdBono(), uidTicket, bonosAAnularSinUso, bonosAAnularUsados);
            }
        } catch (NoResultException e) {
            throw new AnulacionNoPosibleException("<html>El Bono por el valor restante de la <br/>nota de crédito ya fué utilizado</html>");
        }
    }

    public static void insertarLog(LogOperaciones logImpresiones) throws LogException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ServicioLogAcceso.crearAccesoLog(logImpresiones, em);
        em.getTransaction().commit();
    }
}
