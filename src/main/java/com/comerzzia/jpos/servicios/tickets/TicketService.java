/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.envioDomicilio.EnvioDomicilioDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ProcesoEnvioDomicilioDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import com.comerzzia.jpos.dto.ventas.BdgRequerimientoDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.dto.ventas.EntregaDomilicioDTO;
import com.comerzzia.jpos.dto.ventas.TramaCreditoDTO;
import com.comerzzia.jpos.dto.ventas.online.FormaPagoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.VentaOnlineDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.CompletarOrdenDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.PagoFacturaInputDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.TrazabilidadEntregaDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.paymentez.DebitRequestDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.CollectRequest;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.CabPrefactura;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.CupoVirtual;
import com.comerzzia.jpos.entity.db.DetPrefactura;
import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.PedidoOnlineTbl;
import com.comerzzia.jpos.entity.db.PwFormasPago;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.services.ParIdValor;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.articulos.ArticulosDao;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.persistencia.devoluciones.DevolucionesDao;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.persistencia.cajas.CajaException;
import com.comerzzia.jpos.persistencia.cajas.GestionDeCajasDao;
import com.comerzzia.jpos.persistencia.cajas.MovimientoCajaException;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualDao;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.persistencia.facturacion.prefactura.CabPrefacturaDao;
import com.comerzzia.jpos.persistencia.facturacion.prefactura.DetPreFacturaDao;
import com.comerzzia.jpos.persistencia.facturacion.prefactura.PwFormasPagoDao;
import com.comerzzia.jpos.persistencia.facturacion.tarjetas.FacturacionTarjetasDao;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionErradaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.pedido.online.Pedido.PedidoOnlineDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionSocioBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.pinpad.fasttrack.PinPadFasttrack;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;
import com.comerzzia.jpos.servicios.articulos.ArticuloException;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.impuestos.ImpuestosServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import com.comerzzia.jpos.servicios.envio.domicilio.ServicioEnvioDomicilio;
import com.comerzzia.jpos.servicios.facturaElectronica.EFacturaServices;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.ServicioFacturacionTarjetas;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendida;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendidaServices;
import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import com.comerzzia.jpos.servicios.giftcard.ServicioGiftCard;
import com.comerzzia.jpos.servicios.letras.LetraCambiosServices;
import com.comerzzia.jpos.servicios.listapda.ListaPDAServices;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.logs.transaccioneserradas.ServicioTransaccionesErradas;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoBuilder;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoBono;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.PagoPrefacturaException;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoLetra;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.pedido.online.ServicioPedidoOnline;
import com.comerzzia.jpos.servicios.placeToPay.ServicioPagoTarjetasOnline;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.promociones.articulos.ServicioPromocionArticulo;
import com.comerzzia.jpos.servicios.promociones.clientes.ServicioPromocionesClientes;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.ServicioCupones;
import com.comerzzia.jpos.servicios.promociones.puntos.ServicioPuntos;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.util.ClaveAccesoSri;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.StringParser;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.jpos.util.UtilUsuario;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.enums.EnumEstadoPagoPrefactura;
import com.comerzzia.jpos.util.enums.EnumEstadoPrefactura;
import com.comerzzia.jpos.util.enums.EnumEstadoTrazabilidad;
import com.comerzzia.jpos.util.enums.EnumTipoComprobante;
import com.comerzzia.jpos.util.enums.EnumTipoIdentificacion;
import com.comerzzia.jpos.util.enums.EnumTipoPagoManual;
import com.comerzzia.jpos.util.enums.EnumTipoPagoPaginaWeb;
import com.comerzzia.util.ClienteRest;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.ValidadorCedula;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class TicketService {

    private static final Logger log = Logger.getMLogger(TicketService.class);

    public static final String VOUCHER_MANUAL = "VOUCHER MANUAL";
    public static final int maxLength = 3000;

    public static byte[] consultarXmlTicket(Long idTicket, String codCaja, String codAlmacen) throws TicketException {
        try {
            return TicketsDao.consultarXmlTicket(idTicket, codCaja, codAlmacen);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new TicketException("No fue posible obtener el xml del ticket.", e);
        }
    }

    public static TicketsAlm consultarTicket(Long idTicket, String codCaja, String codAlmacen) throws NoResultException, TicketException {
        try {
            return TicketsDao.consultarTicket(idTicket, codCaja, codAlmacen);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new TicketException("No fue posible obtener el xml del ticket.", e);
        }
    }

    public static TicketsAlm consultarTicket(String uidTicket) throws NoResultException, TicketException {
        try {
            return TicketsDao.consultarTicket(uidTicket);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new TicketException("No fue posible obtener el xml del ticket.", e);
        }
    }

    public static TicketsAlm consultarTicketUid(EntityManager em, String uidTicket, String codCaja, String codAlmacen, Date fechacero, Date fechaactual) throws NoResultException, TicketException {
        try {
            return TicketsDao.consultarTicketuid(em, uidTicket, codCaja, codAlmacen, fechacero, fechaactual);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new TicketException("No fue posible obtener el xml del ticket.", e);
        }
    }

    public static void procesarMediosPagos(EntityManager em, List<Pago> pagos, ReferenciaTicket referencia, String tipo, String documento)
            throws PagoInvalidException, Exception {
        procesarMediosPagos(em, pagos, referencia, null, tipo, documento);
    }

    /**
     *
     * @param em
     * @param pagos
     * @param referencia Para uso en anulación de abonos
     * @throws PagoInvalidException
     * @throws Exception
     */
    public static void procesarMediosPagos(EntityManager em, List<Pago> pagos, ReferenciaTicket referencia, Long idAbono,
            String tipo, String documento)
            throws PagoInvalidException, Exception {
        Connection connSukasa = new Connection();
        List<ParIdValor> listaPagosNC = new LinkedList<ParIdValor>();
        List<PagoGiftCard> listaPagosGiftCard = new LinkedList<PagoGiftCard>();
        List<Bono> listaPagosBono = new LinkedList<Bono>();
        List<PagoCreditoLetra> listaPagosLetras = new LinkedList<PagoCreditoLetra>();
        List<PagoCredito> listaPagosTarjeta = new LinkedList<PagoCredito>();
        List<PagoCreditoSK> listaPagosTarjetaSK = new LinkedList<PagoCreditoSK>();
        Pago pagoEfectivo = null;

        log.debug("procesarMediosPagos() - Procesamos los movimientos de caja...");
        Character tipoMovimiento = referencia.getTipoMovimientoCaja();
        List<CajaDet> listaMovimientos = Sesion.getCajaActual().crearApunteVenta(pagos, referencia, tipoMovimiento, idAbono);
        for (CajaDet caj : listaMovimientos) {
            if (referencia.isVentaOnline()) {
                caj.setPrefactura("S");
            }
            em.persist(caj);
        }
        log.debug("\t\tMovimientos de caja registrados: " + listaMovimientos.size());

        log.debug("procesarMediosPagos() - Recorremos lista de medios de pago para su tratamiento...");
        // Recorre los medios de pago y determina si pertenece a algunos de los pagos a tratar
        if (pagos != null) {
            for (Pago pag : pagos) {
                // Efectivo 
                if (pag.isPagoEfectivo()) {
                    pagoEfectivo = pag; // sólo puede haber uno
                }
                // Notas de crédito
                if (pag.getMedioPagoActivo().isNotaCredito()) {
                    listaPagosNC.add(new ParIdValor(((PagoNotaCredito) pag).getUidNotaCredito(), BigDecimal.ZERO, ((PagoNotaCredito) pag).getUstedPaga()));
                }
                // Bonos efectivo
                if (pag.getMedioPagoActivo().isBonoEfectivo()) {
                    listaPagosBono.add(((PagoBono) pag).getBono());
                }
                // GiftCard
                if (pag.getMedioPagoActivo().isGiftCard()) {
                    listaPagosGiftCard.add(((PagoGiftCard) pag));
                }
                //cambio RD Para Consultar tarjeta
                // Tarjeta crédito Sukasa 
                if (pag.getMedioPagoActivo().isTarjetaSukasa()) {
                    listaPagosTarjetaSK.add(((PagoCreditoSK) pag));
                } else // Tarjeta crédito normal
                if (pag.getMedioPagoActivo().isTarjetaCredito() || pag.getMedioPagoActivo().isBonoSuperMaxiNavidad() || pag.getMedioPagoActivo().isCreditoFilial()) {
                    listaPagosTarjeta.add(((PagoCredito) pag));
                }

                // Letras
                if (pag.getMedioPagoActivo().isCreditoTemporal()) {
                    listaPagosLetras.add((PagoCreditoLetra) pag);
                }
            }
        }

        // actualizamos valor de efectivo en caja
        if (pagoEfectivo != null) {
            Sesion.getCajaActual().sumaEfectivoEnCaja(pagoEfectivo.getUstedPaga());
        }

        log.debug("\tActualizamos bonos efectivos utilizados...");
        for (Bono pb : listaPagosBono) {
            BonosDao.anulaBono(pb, referencia.getIdReferencia(), referencia.getTipoReferencia(), em, referencia.getNumTicket());
        }
        log.debug("\t\tBonos utilizados: " + listaPagosBono.size());

        log.debug("\tActualizamos notas de crédito utilizadas...");
        for (ParIdValor notas : listaPagosNC) {
            DevolucionesDao.modificaNotaCredito(notas.getId(), notas.getValor(), notas.getValorUsado(), em,
                    referencia.getIdReferencia(), tipo, documento);
        }
        log.debug("\t\tNotas de crédito utilizadas: " + listaPagosNC.size());

        log.debug("\tActualizamos giftCards utilizadas...");
        for (PagoGiftCard pagoGiftCard : listaPagosGiftCard) {
            ServicioGiftCard.actualizarSaldo(em, pagoGiftCard.getGiftCard(),
                    pagoGiftCard.getGiftCard().getSaldo().subtract(pagoGiftCard.getUstedPaga()), pagoGiftCard.getUstedPaga(), referencia);

        }
        log.debug("\t\tGiftCards utilizadas: " + listaPagosGiftCard.size());

        log.debug("\tRegistramos información de tarjetas de crédito utilizadas...");
        //Comentar este if/else si se muestra siempre la pantalla de autorización tarjeta
        //if (!PinPad.getInstance().isManual()) {
        log.debug("\t\tTarjetas de crédito utilizadas: " + listaPagosTarjeta.size());
        if (!referencia.isVentaOnline()) {
            for (PagoCredito pagoC : listaPagosTarjeta) {
                log.debug("\t\tRegistrando tarjeta de crédito: " + pagoC.toString());
                FacturacionTarjeta fa = new FacturacionTarjeta(pagoC, referencia);
                //Cambio numero de autorizacion manual RD
                if (!pagoC.isValidadoAutomatico() && pagoC.isValidadoManual()) {
                    if (pagoC.getCodigoValidacionManual() != null && !pagoC.getCodigoValidacionManual().equals("")) {
                        fa.setNumeroAutorizacion(pagoC.getCodigoValidacionManual());
                    }
                    String mensajePromocional = VOUCHER_MANUAL;
                    pagoC.setMensajePromocional(mensajePromocional);
                    fa.setMensajePromocional(mensajePromocional);
                    //G.S Para los pagos manuales
                    if (pagoC.getTipoPagoManual() != null) {
                        fa.setPosMovil(pagoC.getTipoPagoManual().getCodigo());
                        fa.setLote(pagoC.getNumeroLoteManual());
                        if (EnumTipoPagoManual.DATA_LINK.equals(pagoC.getTipoPagoManual())) {
                            fa.setValor(pagoC.getNumeroTarjeta());
                            String numeroAutorizacion = pagoC.getCodigoValidacionManual().length() > 6 ? pagoC.getCodigoValidacionManual().substring(0, 5) : pagoC.getCodigoValidacionManual();
                            fa.setNumeroAutorizacion(numeroAutorizacion);
                        }
                    }

                }
                fa.setProcesado('N');
//                if (pagoC.getPinpadFasttrackRespuesta() != null) {
//                    if (pagoC.getPinpadFasttrackRespuesta().ObtenerTrama().length() >= maxLength) {
//                        fa.setTramaEnvio(pagoC.getTramaEnvio().substring(1, 2900));
//                        fa.setTramaRespuesta(pagoC.getPinpadFasttrackRespuesta().ObtenerTrama().substring(1, 2900));
//                    } else {
//                        fa.setTramaEnvio(pagoC.getTramaEnvio());
//                        fa.setTramaRespuesta(pagoC.getPinpadFasttrackRespuesta().ObtenerTrama());
//                    }
//                }
                if (fa.getTramaEnvio() == null) {
                    fa.setTramaEnvio(pagoC.getTramaEnvio());
                }
                if (fa.getTramaRespuesta() == null) {
                    fa.setTramaRespuesta(pagoC.getTramaRespuesta());
                }
                FacturacionTarjetasDao.actualizarFacturacionTarjeta(fa, em);
            }
        }
        //} else {
        //    log.debug("\t\tNo se registra información: Autorización MANUAL activada. ");
        //}

        log.debug("\tRegistramos letras utilizadas...");
        if (!listaPagosLetras.isEmpty()) {
            java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
            for (PagoCreditoLetra pago : listaPagosLetras) {
                LetraCambiosServices.crearLetra(conn, pago, referencia.getIdReferencia());
            }
        }
        log.debug("\t\tLetras utilizadas: " + listaPagosLetras.size());

        try {
            if (!listaPagosTarjetaSK.isEmpty()) {
                log.debug("\tActualizamos cupo de tarjetas de sukasa utilizadas...");
                if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                        && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                    connSukasa.abrirConexion(Database.getConnectionSukasa());
                    connSukasa.iniciaTransaccion();
                }
                Integer numeroCredito;
                for (PagoCreditoSK pago : listaPagosTarjetaSK) {
                    numeroCredito = ((PagoCreditoSK) pago).getPlastico() != null ? ((PagoCreditoSK) pago).getPlastico().getNumeroCredito() : ((TarjetaCreditoSK) ((PagoCreditoSK) pago).getTarjetaCredito()).getPlastico().getNumeroCredito();
                    if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                            && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                        CupoVirtual cupoVirtual = CupoVirtualDao.consultarCupoVirtualByCredito(em, numeroCredito);
                        if (cupoVirtual != null) {
                            cupoVirtual.setCupo(cupoVirtual.getCupo().subtract(pago.getUstedPaga()));
                            cupoVirtual.setProcesado("N");
                            em.merge(cupoVirtual);
                        }
                    } else {
                        CupoVirtualDao.restarCupo(connSukasa, pago.getUstedPaga(), numeroCredito);
                    }
                }
                log.debug("\t\tTarjetas Sukasa utilizadas: " + listaPagosTarjetaSK.size());
                if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                        && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("1")) {
                    connSukasa.commit();
                    connSukasa.finalizaTransaccion();
                }
                if (!listaPagosTarjetaSK.isEmpty() && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                        && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                    for (PagoCreditoSK pago : listaPagosTarjetaSK) {
                        numeroCredito = ((PagoCreditoSK) pago).getPlastico() != null ? ((PagoCreditoSK) pago).getPlastico().getNumeroCredito() : ((TarjetaCreditoSK) ((PagoCreditoSK) pago).getTarjetaCredito()).getPlastico().getNumeroCredito();
                        TramaCreditoDTO creditoDTO = new TramaCreditoDTO(numeroCredito, pago.getUstedPaga().negate(), Sesion.getTienda().getCodalm());
                        String tramaCredito = JsonUtil.objectToJson(creditoDTO);
                        log.info("Trama credito " + tramaCredito);
                        ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), tramaCredito, Variables.getVariable(Variables.QUEUE_CREDITO_CUPO), Constantes.PROCESO_CREDITO_CUPO, UUID.randomUUID().toString());
                        envioDomicilioThread.start();
                    }
                }
            }
        } catch (Exception e) {
            log.error("procesarMediosPagos() - Error procesando pago de tarjeta sukasa. Error actualizando cupo. " + e.getMessage(), e);
            throw new CreditoException("Error procesando pago de tarjeta sukasa. Error actualizando cupo.", e);
        } finally {
            connSukasa.cerrarConexion();
        }
    }

    public static void escribirTicket(TicketS ticket) throws TicketException, CuponException {
        escribirTicket(ticket, true, true);
    }

    public static void escribirTicket(TicketS ticket, boolean procesarPagos, boolean realizarRecalculo) throws TicketException, CuponException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            escribirTicket(em, ticket, procesarPagos, realizarRecalculo);
            em.getTransaction().commit();
            //G.S. Encola los pedidos facturados
            generarPedidoFacturado(ticket, ticket.getUid_ticket());
            generarEnvioDomicilio(ticket, ticket.getUid_ticket());
        } catch (Exception e) {
            log.error("escribirTicket() - Error escribiendo Ticket", e);
            em.getTransaction().rollback();
            throw new TicketException("Se produjo un error. No se ha realizado la venta", e);
        } finally {
            em.close();
        }
    }

    public static void modificarTicket(EntityManager em, TicketsAlm ticket) throws TicketException {
        try {
            TicketsDao.modificarTicket(em, ticket);
        } catch (Exception e) {
            log.error("modificaTicket() - Error modificando Ticket :" + e.getMessage(), e);
            throw new TicketException("modificaTicket() - Error actualizando Ticket : " + e.getMessage(), e);
        }
    }

    public static void modificarTicketDetalle(LineaTicketOrigen ticket) throws TicketException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TicketsDao.modificarEnvioYRecogida(em, ticket);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("modificarTicketDetalle() - Error modificando Línea de Ticket:" + e.getMessage(), e);
            throw new TicketException("modificarTicketDetalle() - Error actualizando Línea de Ticket : " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

//    public static void recostruccionTikets(){
//         TicketsAlm respuesta = TicketsDao.consultarTicket(new Long(68134),"020","001");
//        GarantiaExtendidaServices.consultarArticuloGarantia(respuesta.getUidTicket(), codArticulo);
//    }
//    
    public static List<LineaTicketOrigen> obtenerPendientes(EntityManager em) {
        List<LineaTicketOrigen> respuestaPendientes = TicketsDao.consultarTicketPendientesEntrega(em, 'P');
        return respuestaPendientes;

    }

    public static List<LineaTicketOrigen> obtenerDomicilio(EntityManager em) {
        List<LineaTicketOrigen> respuestaPendientes = TicketsDao.consultarTicketPendientesEnvio(em, 'P');
        return respuestaPendientes;

    }

    public static void escribirTicket(EntityManager em, TicketS ticket, boolean procesarPagos) throws CuponException, TicketException {
        escribirTicket(em, ticket, procesarPagos, Boolean.TRUE);
    }

    public static void escribirTicket(EntityManager em, TicketS ticket, boolean procesarPagos, boolean realizarRecalculo) throws CuponException, TicketException {
        // recalculamos el total final pagado para cada línea
        ticket.recalcularFinalPagado();
        ticket.recalcularFinalPagadoFinal(false);

        // obtenemos ids para los cupones que se emitirán
        ServicioCupones.obtenerIdCupones(ticket.getCuponesEmitidos());

        // Creamos el objeto ticketAlm que será el que se salvará en base de datos
        TicketsAlm ticketAlm = new TicketsAlm();
        ticketAlm.setFecha(new Date());
        ticketAlm.setIdTicket(ticket.getId_ticket());
        ticketAlm.setUidTicket(ticket.getUid_ticket());
        ticketAlm.setCodCaja(ticket.getCodcaja());
        ticketAlm.setCodAlm(Sesion.getTienda().getCodalm());
        ticketAlm.setCodCli(ticket.getCliente().getCodcli());
        ticketAlm.setUsuario(Sesion.getUsuario().getUsuario());
        ticketAlm.setTotalConDstoConIva(ticket.getTotales().getTotalPagado());
        ticketAlm.setTotalConDstoSinIva(ticket.getTotales().getBase());
        ticketAlm.setTotalSinDstoConIva(ticket.getTotales().getImporteTotalTarifaOrigen());//
        ticketAlm.setTotalSinDstoSinIva(ticket.getTotales().getImporteTarifaOrigen());//
        if (ticket.getCabPrefactura() != null) {
            ticketAlm.setCabPrefactura(ticket.getCabPrefactura());
        }

        if (ticket.getFacturacion() != null) {
            ticketAlm.setFactTipoDoc(ticket.getFacturacion().getTipoDocumento());
            ticketAlm.setFactDocumento(ticket.getFacturacion().getDocumento());
            ticketAlm.setFactNombre(ticket.getFacturacion().getNombre() + " " + ticket.getFacturacion().getApellidos());
            ticketAlm.setFactTelefonon(ticket.getFacturacion().getTelefono());
            ticketAlm.setFactDireccion(ticket.getFacturacion().getDireccion());
            ticketAlm.setFactProvincia(ticket.getFacturacion().getProvincia());
            ticketAlm.setFactEmail(ticket.getFacturacion().getEmail());
        } else {
            ticketAlm.setFactTipoDoc(ticket.getCliente().getTipoIdentificacion());
            ticketAlm.setFactDocumento(ticket.getCliente().getCif());
            ticketAlm.setFactNombre(ticket.getCliente().getNombre() + " " + ticket.getCliente().getApellido());
            ticketAlm.setFactTelefonon(ticket.getCliente().getTelefonoFacturacion());
            ticketAlm.setFactDireccion(ticket.getCliente().getDireccion());
            ticketAlm.setFactProvincia(ticket.getCliente().getProvincia());
            ticketAlm.setFactEmail(ticket.getCliente().getEmail());
        }

        //rescatamos el efectivo previo en caja
        BigDecimal efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();

        try {
            if (procesarPagos) { // procesamos antes los pagos porque necesitamos que se procesen antes de crear XML
                log.debug("escribirTicket() - Procesamos pagos y movimientos de caja...");
                ReferenciaTicket referencia = ReferenciaTicket.getReferenciaFactura(ticket);
                procesarMediosPagos(em, ticket.getPagos().getPagos(), referencia, "FAC", referencia.getNumTicket());
            }
            log.debug("escribirTicket() - Salvamos el ticket en base de datos...");
            ticketAlm.setTicket(TicketXMLServices.getXMLTicket(ticket));

            // registramos posibles líneas de garantía extendida
            GarantiaExtendidaServices.salvarItemsGarantia(em, ticket);

            //G.S. Asigna el vendedor del EGO al articulo
            asignarVendedorExtencionGarantia(ticket);

            ///////////////////////////
            //if (VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || ticket.getCliente().isTipoRuc())) {
            ticketAlm.seteTicket(EFacturaServices.generaFacturaElectronica(ticket).getBytes("UTF-8"));

            //AQUI
            String generarClaveAccesoTemp = generarClaveAcceso(ticket);
            //}
            ticketAlm.setClaveAcceso(generarClaveAccesoTemp);
            TicketsDao.escribirTicket(em, ticketAlm);
            em.flush(); // Obligamos a que se realice la operación en base de datos antes de salvar las lineas
            // registramos las lineas del ticket en base de datos
            if (Sesion.isSukasa()) {
                log.debug("escribirTicket() - Salvamos las líneas del ticket en base de datos...");
                salvarLineas(em, ticket);
            }

            if (ticket.tieneSesionPDAAsociada()) {
                log.debug("escribirTicket() - Marcamos sesión PDA como utilizada...");
                ListaPDAServices.marcarComoUtilizado(ticket.getReferenciaSesionPDA());
            }
            Connection conn = Connection.getConnection(em);

            // Tratamiento de cupones.
            ServicioCupones.crear(conn, ticket.getCuponesEmitidos(), ticket.getLineas().getLineas());

            ServicioCupones.marcarCuponesUtilizados(conn, ticket);
            if (ticket.getProcesoEnvioDomicilioDTO() != null) {
                if (ticket.getProcesoEnvioDomicilioDTO().getFactura() != null) {
                    ServicioEnvioDomicilio.insertarEnvioDomicilio(ticket.getProcesoEnvioDomicilioDTO(), em);
                }
            }

            //Inserta la factura con descuento por ser nuevo Socio
            PromocionSocioBean promoNuevoSocio = ServicioPromocionesClientes.consultarPromoClienteSocio(ticket.getCliente().getCodcli());
            if (promoNuevoSocio.getCedula() != null) {
                boolean promoSocio = ServicioPromocionesClientes.updatePromoClienteSocio();
                if (!promoSocio) {
                    log.error("No se modifico el descuento del cliente.");
                    throw new Exception("No se modifico el descuento del cliente.");
                }
            }
            // Tratamiento de puntos.
            ServicioPuntos.acumularPuntos(conn, ticket);
            ServicioPuntos.consumirPuntos(conn, ticket);

            // Tratamiento de promociones de clientes
            ServicioPromocionesClientes.registrarPromocionesAplicadas(conn, ticket);

            // Tratamiento de promociones de artículos
            ServicioPromocionArticulo.insert(conn, ticket);

            //Guardar Impuestos
            ImpuestosServices.guardarImpuestos(ticket.getUid_ticket(), ticket, em);

            //G.S. Encola las ventas DEBE IR AL FINAL
            generarKardexVentas(ticket, ticket.getUid_ticket());

        } catch (Exception e) {
            log.error("escribirTicket() - Error creando Ticket :" + e.getMessage(), e);
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            ServicioTransaccionesErradas.crearTransaccionErrada(ticket.getId_ticket(), e, TransaccionErradaBean.TIPO_TRANSACCION_FACTURA);
            em.getTransaction().rollback();
            throw new TicketException("escribirTicket() - Error creando Ticket : " + e.getMessage(), e);
        }
    }

    private static void salvarLineas(EntityManager em, TicketS ticket) throws TicketException {
        LineasTicket lineas = ticket.getLineas();
        log.debug("salvarLineas() - Registrando Lineas de Ticket");
        for (LineaTicket linea : lineas.getLineas()) {
            log.debug("salvarLineas() - Registrando línea de ticket...");
            LineaTicketOrigen linTO = new LineaTicketOrigen(ticket.getUid_ticket(), linea.getIdlinea().shortValue());

            linTO.setCantidad(linea.getCantidad());
            if (ticket.getVendedor() != null && ticket.getVendedor().getCodvendedor() != null) {
                linTO.setCodVendedor(ticket.getVendedor().getCodvendedor());
            }
            //DR cambio para poner el vendedor en el detalle del ticket

            if (linea.getCodEmpleado() != null && !linea.getCodEmpleado().isEmpty()) {
                linTO.setCodVendedor(linea.getCodEmpleado());
            } else {
                String codVendedor = ticket.getCajero().getUsuario();
                linTO.setCodVendedor(codVendedor);
            }

            linTO.setCodart(linea.getArticulo());
            linTO.setCodigoBarras(linea.getCodigoBarras());
            linTO.setEnvioDomicilio(linea.isEnvioDomicilio());

            linTO.setIdCajero(ticket.getCajero().getIdUsuario());

            linTO.setImporteFinal(Numero.redondear(linea.getImporteFinalPagadoSinRedondear()));
            linTO.setImporteTotalFinal(Numero.redondear(linea.getImporteTotalFinalPagado()));
            linTO.setPrecioOrigen(Numero.redondear(linea.getPrecioTarifaOrigen()));
            linTO.setPrecioTotalOrigen(Numero.redondear(linea.getPrecioTotalTarifaOrigen()));
            linTO.setRecogidaPosterior(linea.isRecogidaPosterior());
            linTO.setCodImp(linea.getArticulo().getCodimp());
            //Se Agrega Costo Landed
            linTO.setCostoLanded(linea.getCostoLanded());
            //Se Agrega la categoria
            linTO.setCodCategoria(linea.getCodCategoria());
            linTO.setPedidoFacturado(linea.isPedidoFacturado());
            //Se Agrega Precio vigente del item
            linTO.setPrecioReal(linea.getPrecioReal());
            if (linTO.getCodImp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
                linTO.setPorcentaje(Sesion.getEmpresa().getPorcentajeIva());
                // linTO.setImporteTotalFinal(Numero.redondear(linea.getImporteFinalPagadoSinRedondear().multiply(BigDecimal.ONE.add(Sesion.getEmpresa().getPorcentajeIva().divide(new BigDecimal(100))))));
            } else {
                linTO.setPorcentaje(BigDecimal.ZERO);
            }

            TicketsDao.escribirLineaTicket(em, linTO);
        }
    }

    public static void marcarLineaOrigenEnviado(EntityManager em, LineaTicketOrigen lin) throws TicketException {

        try {
            lin.setEnvioDomicilioEntregado();
            TicketsDao.modificarLineaOrigen(em, lin);
        } catch (Exception e) {
            log.error("marcarLineaOrigenEnviado() - No se pudo modificar la línea de la factura", e);
            throw new TicketException("No se pudo modificar la línea de la factura", e);
        }

    }

    public static LineaTicketOrigen consultarLineaTicketOrigen(String uidTicket, Long idLinea) throws TicketException, NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            return TicketsDao.consultarLineaTicket(em, uidTicket, idLinea);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            log.error("consultarLineaTicketOrigen() - No se pudo consultar la linea del ticket", e);
            throw new TicketException("Error. No se pudo consultar la linea del ticket", e);
        } finally {
            em.close();
        }
    }

    public static List<LineaTicketOrigen> consultarLineasTicket(String uidTicket) throws NoResultException, TicketException {
        List<LineaTicketOrigen> res = new ArrayList<LineaTicketOrigen>();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            res = TicketsDao.consultarLineasTicket(em, uidTicket);
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            log.error("consultarLineasTicket() - No se pudieron consultar las lineas del ticket", e);
            throw new TicketException("Error. No se pudieron consultar las lineas del ticket", e);
        } finally {
            em.close();
        }
        return res;
    }

    /**
     * @author Mónica Enríquez
     * @param ticket
     * @return
     */
    public static String generarClaveAcceso(TicketS ticket) {
        String claveAcceso = null;
        try {
            String ruc = Sesion.getEmpresa().getCif();
            claveAcceso = ClaveAccesoSri.generaClave(ticket.getFecha().getDate(), EnumTipoComprobante.FACTURA.getCogidoSRI(), ruc, "2",
                    ticket.getTienda() + ticket.getCodcaja(), String.format("%09d", ticket.getId_ticket()), Constantes.VALOR_DEFECTO_CLAVE_ACCESO, "1");
        } catch (Exception ex) {
            log.error("Error ", ex);
        }
        return claveAcceso;
    }

    /**
     * @author Mónica Enríquez
     * @param notaCredito
     * @return
     */
    public static String generarClaveAccesoNotaCredito(NotasCredito notaCredito) {
        String claveAcceso = null;
        try {
            String ruc = Sesion.getEmpresa().getCif();
            claveAcceso = ClaveAccesoSri.generaClave(notaCredito.getFecha(), EnumTipoComprobante.NOTA_CREDITO.getCogidoSRI(), ruc, "2",
                    notaCredito.getCodalm() + notaCredito.getCodcaja(), String.format("%09d", notaCredito.getIdNotaCredito()), Constantes.VALOR_DEFECTO_CLAVE_ACCESO, "1");
        } catch (Exception ex) {
            log.error("Error ", ex);
        }
        return claveAcceso;
    }

    /**
     * @author Gabriel Simbania
     * @param ticket
     * @param cabIdPedido
     */
    private static void generarCompletarOrdenPrefactura(TicketS ticket, CabPrefactura cabPrefactura) {

        try {

            String ruc = Sesion.getEmpresa().getCif();

            String claveAcceso = ClaveAccesoSri.generaClave(ticket.getFecha().getDate(), "01", ruc, "2",
                    ticket.getTienda() + ticket.getCodcaja(), String.format("%09d", ticket.getId_ticket()), Constantes.VALOR_DEFECTO_CLAVE_ACCESO, "1");

            CompletarOrdenDTO completarOrdenDTO = new CompletarOrdenDTO(cabPrefactura.getCabIdPedido(), claveAcceso, ticket.getFecha().getDate().getTime() / 1000, ticket.getTienda(), ticket.getTotales().getTotalPagado());

            String completarOrdenString = JsonUtil.objectToJson(completarOrdenDTO);
            ProcesoEncolarThread encolar = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), completarOrdenString, Variables.getVariable(Variables.QUEUE_COMPLETAR_ORDEN_PREFACTURA), Constantes.PROCESO_PRE_FACTURA_COMPLETAR_ORDEN, ticket.getUid_ticket());
            encolar.start();

            EntityManager em = Sesion.getEmf().createEntityManager();
            try {
                cabPrefactura.setCabEstadoTrazabilidad(EnumEstadoTrazabilidad.ENCOLADO);
                em.getTransaction().begin();
                CabPrefacturaDao.actualizarCabPrefactura(cabPrefactura, em);
                em.getTransaction().commit();
            } catch (Exception ex) {
                em.getTransaction().rollback();
                log.error("Error ", ex);
            } finally {
                em.close();
            }
        } catch (Throwable e) {
            log.error("No se pudo encolar la orden de la prefactura " + e.getMessage(), e);
        }

    }

    /**
     * @author Gabriel Simbania
     * @description se asigna en el objeto LineaTicket el codigo del vendor que
     * realizo la venta de la garantia extendida
     * @param ticket
     */
    private static void asignarVendedorExtencionGarantia(TicketS ticket) {

        for (LineaTicket linea : ticket.getLineas().getLineas()) {
            GarantiaReferencia referencia = linea.getReferenciaGarantia();
            if (referencia != null) {
                LineaTicket ticketOrigen = referencia.getLineaOrigen();
                ticketOrigen.setCodEmpleado(linea.getCodEmpleado());
            }
        }

    }

    /**
     * @author Gabriel Simbania
     * @param ticket
     * @param uidTicket
     */
    private static void generarPedidoFacturado(TicketS ticket, String uidTicket) {

        try {
            log.debug("Creando el pedido facturado");

            BdgRequerimientoDTO bdgRequerimientoDTO = generarRequerimientoFacturado(ticket);

            if (!bdgRequerimientoDTO.getItemDtoLista().isEmpty()) {
                String requerimientoString = JsonUtil.objectToJson(bdgRequerimientoDTO);
                ProcesoEncolarThread pedidoFacturadoThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), requerimientoString, Variables.getVariable(Variables.QUEUE_PEDIDO_FACTURADO), Constantes.PROCESO_PEDIDO_FACTURADO, uidTicket);
                pedidoFacturadoThread.start();
            }
        } catch (Throwable e) {
            log.error("Error en el pedido facturado " + e.getMessage());
        }
    }

    /**
     * @author Mónica Enriquez
     * @param ticket
     * @param uidTicket
     */
    private static void generarEnvioDomicilio(TicketS ticket, String uidTicket) {

        try {
            log.debug("Creando el envio a domicilio");
            ArticulosDao articulosDao = new ArticulosDao();
            List<EnvioDomicilioDTO> listEnvioDomicilio = new ArrayList<>();

            for (EnvioDomicilioDTO listenvio : ticket.getProcesoEnvioDomicilioDTO().getListEnvioDomicilioDTO()) {
                for (ItemDTO item : listenvio.getItemDtoLista()) {
                    Articulos articulos = articulosDao.getArticuloCod(item.getCodigoI());
                    item.setCodigoI(articulos.getCodmarca().getCodmarca() + "-" + articulos.getIdItem().toString());
                }
                listEnvioDomicilio.add(listenvio);
            }

            ProcesoEnvioDomicilioDTO procesoEnvioDomicilioDTO = new ProcesoEnvioDomicilioDTO(ticket.getProcesoEnvioDomicilioDTO().getFactura(), ticket.getProcesoEnvioDomicilioDTO().getLugSri(), listEnvioDomicilio);

            ClienteDTO datosCliente = new ClienteDTO();
            if (ticket.getFacturacion() != null) {
                datosCliente.setIdentificacion(ticket.getFacturacion().getDocumento());
                datosCliente.setNombres(ticket.getFacturacion().getNombre());
                datosCliente.setApellidos(ticket.getFacturacion().getApellidos());
                datosCliente.setNumeroTelefono(ticket.getFacturacion().getTelefono());
                datosCliente.setNumeroCelular(ticket.getFacturacion().getTelefono());
                datosCliente.setEmail(ticket.getFacturacion().getEmail());
            } else {
                datosCliente.setIdentificacion(ticket.getCliente().getIdentificacion());
                datosCliente.setNombres(ticket.getCliente().getNombre());
                datosCliente.setApellidos(ticket.getCliente().getApellido());
                datosCliente.setNumeroTelefono(ticket.getCliente().getTelefonoFacturacion());
                datosCliente.setNumeroCelular(ticket.getCliente().getFax());
                datosCliente.setEmail(ticket.getCliente().getEmail());

            }
            procesoEnvioDomicilioDTO.setCliente(datosCliente);

            if (!procesoEnvioDomicilioDTO.getListEnvioDomicilioDTO().isEmpty()) {
                String entregaDomicilioString = JsonUtil.objectToJson(procesoEnvioDomicilioDTO);
                ProcesoEncolarThread pedidoFacturadoThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), entregaDomicilioString, Constantes.QUEUE_ENVIO_DOMICILIO, Constantes.PROCESO_ENVIO_DOMICILIO_POS, uidTicket);
                pedidoFacturadoThread.start();
            }
        } catch (Throwable e) {
            log.error("Error el envio a domicilio " + e.getMessage());
        }
    }

    /**
     * @author Gabriel Simbania
     * @param ticket
     * @return
     */
    private static BdgRequerimientoDTO generarRequerimientoFacturado(TicketS ticket) {

        log.debug("Creando el requerimiento");

        String observacion = null;
        List<ItemDTO> itemDTOLista = new ArrayList<>();
        for (LineaTicket linea : ticket.getLineas().getLineas()) {
            if (linea.isPedidoFacturado()) {
                observacion = linea.getObservacionPedidoFacturado();
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setCantidad((long) linea.getCantidad());
                itemDTO.setIdLinea(linea.getIdlinea());
                itemDTO.setEntregaDomicilio(linea.isEnvioDomicilio() ? "P" : "N");
                itemDTO.setCodigoI(linea.getArticulo().getCodmarca().getCodmarca() + "-" + linea.getArticulo().getIdItem());
                itemDTOLista.add(itemDTO);
            }
        }

        UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(ticket.getCajero().getUsuario());

        BdgRequerimientoDTO bdgRequerimientoDTO = new BdgRequerimientoDTO();
        bdgRequerimientoDTO.setNumeroFactura(Sesion.getTienda().getCodalm() + ticket.getCodcaja() + String.format("%09d", ticket.getId_ticket()));
        bdgRequerimientoDTO.setLugSRI(Sesion.getTienda().getCodalmSRI());
        bdgRequerimientoDTO.setObservacion(observacion);
        bdgRequerimientoDTO.setSolicitante(ticket.getCajero().getUsuario());
        bdgRequerimientoDTO.setItemDtoLista(itemDTOLista);
        bdgRequerimientoDTO.setUsuarioDTO(usuarioDTO);

        return bdgRequerimientoDTO;

    }

    /**
     * Encola la informaci&oacute;n para generar el convenio de entrega, la
     * trazabilidad y el requerimiento facturado
     *
     * @author Gabriel Simbania
     * @param ticket
     * @param ventaOnlineDTO
     */
    private static void generarConvenioEntregaTrazabilidad(TicketS ticket, VentaOnlineDTO ventaOnlineDTO, Cliente cliente) {

        try {
            log.debug("Creando el pedido facturado");

            String observacion = null;

            EntregaDomilicioDTO entregaDomilicioDTO = new EntregaDomilicioDTO();
            entregaDomilicioDTO.setEdcNumeroFactura(Sesion.getTienda().getCodalm() + ticket.getCodcaja() + String.format("%09d", ticket.getId_ticket()));
            entregaDomilicioDTO.setCodLugSRI(Sesion.getTienda().getCodalm());
            entregaDomilicioDTO.setEdcDireccion(observacion);
            entregaDomilicioDTO.setDireccionEnvio(ventaOnlineDTO.getDireccionEnvio());

            ItemOnlineDTO itemDomicilio = devuelveItemEntregaDomicilio(ventaOnlineDTO.getItems());

            List<ItemOnlineDTO> itemsEliminar = new ArrayList<>();

            for (ItemOnlineDTO itemOnlineDTO : ventaOnlineDTO.getItems()) {
                if (!itemOnlineDTO.isEntregaDomicilio()) {
                    itemsEliminar.add(itemOnlineDTO);
                }
            }
            ventaOnlineDTO.getItems().removeAll(itemsEliminar);
            entregaDomilicioDTO.setItems(ventaOnlineDTO.getItems());

            TrazabilidadEntregaDTO trazabilidad = new TrazabilidadEntregaDTO();
            trazabilidad.setEntregaDomilicio(entregaDomilicioDTO);
            trazabilidad.setNumeroFactura(entregaDomilicioDTO.getEdcNumeroFactura());
            trazabilidad.setNumeroPedido(ventaOnlineDTO.getIdPedido());
            trazabilidad.setValorFactura(ventaOnlineDTO.getFormasPago().get(0).getaPagar());
            trazabilidad.setReferenciaPedido(ventaOnlineDTO.getReferenciaPedido());
            trazabilidad.setValorDescuento(ticket.getTotales().getImporteTotalTarifaOrigen().subtract(ticket.getTotales().getTotalPagado()));
            ticket.getTotales().getImporteTotalTarifaOrigen();

            if (itemDomicilio != null) {
                trazabilidad.setGastoEnvio(itemDomicilio.getItmPvpUnitario());
            }

            String direccion = ventaOnlineDTO.getDireccionEnvio().getCallePrincipal() != null ? ventaOnlineDTO.getDireccionEnvio().getCallePrincipal() : "";
            direccion = direccion.concat(" ").concat(ventaOnlineDTO.getDireccionEnvio().getCalleSecundaria() != null ? ventaOnlineDTO.getDireccionEnvio().getCalleSecundaria() : "");
            direccion = direccion.concat(" ").concat(ventaOnlineDTO.getDireccionEnvio().getNumeracion() != null ? ventaOnlineDTO.getDireccionEnvio().getNumeracion() : "");

            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNombres(ventaOnlineDTO.getCliente().getNombres());
            clienteDTO.setApellidos(ventaOnlineDTO.getCliente().getApellidos());
            clienteDTO.setIdentificacion(ventaOnlineDTO.getCliente().getIdentificacion());
            clienteDTO.setDireccion(direccion);
            clienteDTO.setNumeroCelular(ventaOnlineDTO.getDireccionEnvio().getTelefonoContacto());
            clienteDTO.setEmail(ventaOnlineDTO.getCliente().getEmail());
            trazabilidad.setCliente(clienteDTO);

            //Agrega el objeto para generar el requerimiento facturado
            trazabilidad.setRequerimiento(generarRequerimientoFacturado(ticket));

            if (!ventaOnlineDTO.getItems().isEmpty()) {
                String entregaDomicilio = JsonUtil.objectToJson(trazabilidad);
                ProcesoEncolarThread pedidoFacturadoThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), entregaDomicilio, Constantes.QUEUE_TRAZABILIDAD_ONLINE, Constantes.PROCESO_TRAZABILIDAD_CONVENIO_ENTREGA, ticket.getUid_ticket());
                pedidoFacturadoThread.start();
            }
        } catch (Throwable e) {
            log.error("Error en el convenio de entrega " + e.getMessage());
        }
    }

    /**
     * Devuelve el &iacute;tem que es la entrega a domicilio
     *
     * @author Gabriel Simbania
     * @param itemOnlineList
     * @return
     */
    private static ItemOnlineDTO devuelveItemEntregaDomicilio(List<ItemOnlineDTO> itemOnlineList) {
        ItemOnlineDTO itemDomicilio = null;

        for (ItemOnlineDTO item : itemOnlineList) {
            if (!item.isEntregaDomicilio()) {
                return item;
            }
        }

        return itemDomicilio;
    }

    /**
     * Encola la informaci&oacute;n para generar el kardex de ventas
     *
     * @author Gabriel Simbania
     * @param ticket
     * @param uidTicket
     */
    public static void generarKardexVentas(TicketS ticket, String uidTicket) {

        try {
            log.debug("Creando el kardex de las ventas");

            List<ItemDTO> itemDTOLista = new ArrayList<>();
            for (LineaTicket linea : ticket.getLineas().getLineas()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setCantidad((long) linea.getCantidad() * -1);
                itemDTO.setCodigoI(linea.getArticulo().getCodmarca().getCodmarca() + "-" + linea.getArticulo().getIdItem());
                itemDTOLista.add(itemDTO);
            }

            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(ticket.getCajero().getUsuario());
            String numeroDocumento = Sesion.getTienda().getCodalm() + ticket.getCodcaja() + String.format("%09d", ticket.getId_ticket());
            DocumentoDTO documentoDTO = new DocumentoDTO(numeroDocumento, Sesion.getTienda().getCodalmSRI(), null, itemDTOLista, usuarioDTO);
            documentoDTO.setTipoDocumento(EnumTipoDocumento.FACTURA.getNombre());
            documentoDTO.setTipoMovimiento(Constantes.MOVIMIENTO_51);

            String documentoString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread encolarKardexThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), documentoString, Variables.getVariable(Variables.QUEUE_KARDEX_POS), Constantes.PROCESO_KARDEX_POS, uidTicket);
            encolarKardexThread.start();

        } catch (Throwable e) {
            log.error("Error en el kardex de las ventas " + e.getMessage());
        }
    }

    /**
     * M&eacute;todo que realiza la facturaci&oacute;n a partir de un DTO que
     * viene desde la p&aacute;gina web
     *
     * @author Gabriel Simbania
     * @param ventaOnlineDTO
     * @return
     * @throws ClienteException
     * @throws ContadorException
     * @throws SQLException
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     * @throws NoResultException
     * @throws XMLDocumentException
     * @throws TicketException
     * @throws ImporteInvalidoException
     * @throws MovimientoCajaException
     * @throws CajaException
     * @throws Exception
     */
    public static synchronized String crearFacturaVentaLineaPos(VentaOnlineDTO ventaOnlineDTO)
            throws ClienteException, ContadorException, SQLException, ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException, NoResultException, XMLDocumentException, TicketException, ImporteInvalidoException, MovimientoCajaException, CajaException, Exception {

        if (Sesion.getUsuario() == null || Sesion.cajaActual == null || Sesion.cajaActual.getCajaActual() == null || Sesion.cajaActual.getCajaParcialActual() == null) {

            //byte[] decodedBytes = Base64.getDecoder().decode(Sesion.config.getPasswordVentaOnline());
            //String password = new String(decodedBytes);
            //Inicia sesion automatica
            log.info("Inicio la sesion para el usuario " + Sesion.config.getUsuarioVentaOnline());
            Sesion.iniciaSesion(Sesion.config.getUsuarioVentaOnline(), Sesion.config.getPasswordVentaOnline());
            //throw new SocketTPVException("Caja sin iniciar sesion ");
        }

        log.info("INGRESO AL METODO CREAR FACTURA EN LINEA");

        //ResponseDTO responseDTO = new ResponseDTO();
        consultarCrearClienteByDTO(ventaOnlineDTO.getCliente());

        LineasTicket lineasTicket = new LineasTicket();
        Connection conn = new Connection();
        PrintServices ts = PrintServices.getInstance();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();


        /*Informacion cliente y afiliado*/
        Cliente cliente = ClientesServices.getInstance().consultaClienteIdenti(ventaOnlineDTO.getCliente().getIdentificacion());
        cliente.setAplicaTarjetaAfiliado(true);

        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        /*Se comenta hasta que se pueda obtener el número de la tarjeta*/
        //tarjeta.setNumero(cliente.getCodcli());
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_CEDULA);
        tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_AUTOMATICA);
        cliente.setTarjetaAfiliacion(tarjeta);

        /*Crear el ticket en el sesion*/
        Sesion.iniciaNuevoTicket(cliente);
        TicketS ticket = Sesion.getTicket();

        /* Inicia la transaccionalidad*/
        try {
            em.getTransaction().begin();
            ts.limpiarListaDocumentos();
            PedidoOnlineTbl pedido = PedidoOnlineDao.consultarPedidoOnline(em, ventaOnlineDTO.getIdPedido());

            if (pedido != null) {
                throw new Exception("Ya existe el pedido " + pedido.getIdPedido());
            }

            ingresarInformacionAdicional(ventaOnlineDTO);

            /*Insertar el pedido online*/
            ServicioPedidoOnline.insertarPedidoOnline(ventaOnlineDTO, ticket.getUid_ticket(), em);

            /*Crear el objeto para los datos  facturacion */
            //Comentado no se envia los datos de facturacion
            /* if (ventaOnlineDTO.getDatosFacturacion() != null) {
                FacturacionTicketBean facturacion = new FacturacionTicketBean();
                facturacion.setNombre(cliente.getNombre());
                facturacion.setApellidos(cliente.getApellido());
                facturacion.setDocumento(cliente.getIdentificacion());
                facturacion.setDireccion(ventaOnlineDTO.getDatosFacturacion().getDireccion());
                facturacion.setProvincia(cliente.getPoblacion());
                facturacion.setTelefono(ventaOnlineDTO.getDatosFacturacion().getNumeroTelefono());
                facturacion.setTipoDocumento(cliente.getTipoIdentificacion());
                facturacion.setEmail(cliente.getEmail());
                ticket.setFacturacion(facturacion);
            } else {*/
            ticket.setFacturacionCliente(cliente);
            ticket.setVentaOnline(true);
            //}

            /* Validacion Pinpad*/
            PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);
            PinPadFasttrack.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);

            //No calcula las promociones
            ticket.getTicketPromociones().setAplicaPromocionesLineas(Boolean.FALSE);
            lineasTicket.setLineas(Sesion.getTicket().getLineas().getLineas());

            conn.abrirConexion(Database.getConnection());

            /*Asignar los articulos*/
            asignarArticulosOnline(ventaOnlineDTO.getItems(), ticket);

            //Genera el pago
            generarFormaPagoOnline(ventaOnlineDTO, ticket, em);

            //Impresion de los cupones en la pagina online
            if (VariablesAlm.getVariableAsBooleanActual(Variables.POS_GENERACION_CUPONES_ONLINE)) {
                //Generar los cupones
                //Carga en el mapa las promociones para los cupones
                LecturaConfiguracion.leerPromociones();
                ticket.finalizarTicket(false);
            }

            //Registrar la factura
            escribirTicket(em, ticket, true, false);

            em.getTransaction().commit();
            generarConvenioEntregaTrazabilidad(ticket, ventaOnlineDTO, cliente);

        } catch (ClienteException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } catch (SQLException | ArticuloException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } catch (Throwable ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }

        try {
            ts.imprimirTicket(ticket);
            DocumentosService.crearDocumento(ticket, ts.getDocumentosImpresos(), DocumentosBean.FACTURA);
        } catch (Exception ex) {
            log.error("Error al imprimir la factura " + ex.getMessage(), ex);
        }

        return ticket.getTienda() + "-" + ticket.getCodcaja() + "-" + ticket.getId_ticket();

    }

    /**
     * Ingresa informacionAdicional
     *
     * @param items
     */
    private static void ingresarInformacionAdicional(VentaOnlineDTO ventaOnlineDTO) {
        for (ItemOnlineDTO itemOnlineDTO : ventaOnlineDTO.getItems()) {
            BigDecimal descuento = itemOnlineDTO.getItmPrecioFinanciamiento().subtract(itemOnlineDTO.getItmPvpUnitario());
            itemOnlineDTO.setValorDescuento(descuento.abs());
        }
    }

    /**
     * M&eacute;todo que consulta si exisite el cliente en la base de datos y en
     * caso de no existir lo inserta
     *
     * @author Gabriel Simbania
     * @param clienteDTO
     * @throws ClienteException
     */
    private static void consultarCrearClienteByDTO(ClienteDTO clienteDTO) throws ClienteException, ValidationException {

        ClientesServices clientesServices = ClientesServices.getInstance();
        Cliente cliente = null;

        try {
            cliente = clientesServices.consultaClienteIdenti(clienteDTO.getIdentificacion());
        } catch (NoResultException ex) {
            log.info("No existe el cliente se procede a registrar");
        }

        if (cliente == null) {

            EnumTipoIdentificacion tipoIdentificacion = EnumTipoIdentificacion.findDocumentTypeByCodigoPOS(clienteDTO.getTipoIdentificacion());

            if (null == tipoIdentificacion) {
                throw new ValidationException("No existe el tipo de identificación " + clienteDTO.getTipoIdentificacion());
            } else {
                switch (tipoIdentificacion) {
                    case CEDULA:
                        if (!ValidadorCedula.verificarIdEcuador(ValidadorCedula.CEDULA, clienteDTO.getIdentificacion())) {
                            throw new ValidationException("El número de cédula es inválido");
                        }
                        break;
                    case RUC_GENERAL:
                        if (!ValidadorCedula.verificarIdEcuador(ValidadorCedula.RUC, clienteDTO.getIdentificacion())) {
                            throw new ValidationException("El ruc es inválido");
                        }
                        break;
                    default:
                        break;
                }
            }

            cliente = new Cliente(clienteDTO.getIdentificacion(), clienteDTO.getTipoIdentificacion());
            cliente.setNombre(clienteDTO.getNombres());
            cliente.setApellido(clienteDTO.getApellidos());
            cliente.setCodcli(clienteDTO.getIdentificacion());
            cliente.setDomicilio(clienteDTO.getDireccion());
            cliente.setTelefono1(clienteDTO.getNumeroTelefono());
            cliente.setFax(clienteDTO.getNumeroCelular());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setActivo('S');
            cliente.setPoblacion(clienteDTO.getProvincia() != null ? clienteDTO.getProvincia() : "PICHINCHA");
            clientesServices.nuevoCliente(cliente);
        }
    }

    /**
     * M&eacute;todo para asignar los articulos online
     *
     * @author Gabriel Simbania
     * @param itemOnlineLista
     * @param ticket
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     * @throws NoResultException
     * @throws XMLDocumentException
     * @throws TicketException
     */
    private static void asignarArticulosOnline(List<ItemOnlineDTO> itemOnlineLista, TicketS ticket) throws ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException, NoResultException, XMLDocumentException, TicketException, MedioPagoException {

        HashMap<Long, LineaTicket> lineaMap = new HashMap<>();
        ArticulosServices articulosServices = ArticulosServices.getInstance();
        ticket.getLineas().setLineas(new ArrayList<LineaTicket>());

        //Asigna los articulos
        for (ItemOnlineDTO itemDTO : itemOnlineLista) {

            Articulos articulos = articulosServices.getArticuloCod(StringParser.convertCodigoITocodArt(itemDTO.getItmCodigoI()));

            String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());

            TarifasServices tarifasServices = new TarifasServices();

            if (!Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS).equals(codBarras)) {
                Tarifas tarifa = tarifasServices.getTarifaArticulo(articulos.getCodart());
                LineaTicket lineaTicket = ticket.crearLineaTicketOnline(codBarras, articulos, itemDTO.getItmCantidad().intValue(), tarifa, itemDTO);
                lineaMap.put(itemDTO.getIdLinea(), lineaTicket);
            } else {
                crearExtensionGarantiaOnline(lineaMap, itemOnlineLista, ticket, itemDTO, articulos);
            }
        }

        //Asignar Extension de garantia
        //Asigna extension garantia gratis para sukasa gold
        //asignarGarantiaGold(lineaMap, ticket,detPrefacturas.get(0).getCabPrefactura().getUidCabId() );
    }

    /**
     *
     * @param ventaOnlineDTO
     * @param ticket
     * @param em
     * @throws ValidationException
     * @throws ImporteInvalidoException
     * @throws MovimientoCajaException
     * @throws CajaException
     * @throws Exception
     */
    private static void generarFormaPagoOnline(VentaOnlineDTO ventaOnlineDTO, TicketS ticket,
            EntityManager em) throws ValidationException, ImporteInvalidoException, MovimientoCajaException, CajaException, Exception {

        FormaPagoOnlineDTO formaPagoOnlineDTO = ventaOnlineDTO.getFormasPago().get(0);

        ticket.inicializaTotales(ticket.getTotales().getTotalAPagar());
        ticket.getTotales().setTotalPagado(formaPagoOnlineDTO.getaPagar());

        BigDecimal subTotalIva = formaPagoOnlineDTO.getSubtotalIva();
        BigDecimal subTotalIvaCero = formaPagoOnlineDTO.getSubtotalIvaCero();
//        for (LineaTicket linea : ticket.getLineas().getLineas()) {
//
//            BigDecimal subtotalIva = Numero.menosPorcentajeR(linea.getImporteFinalPagado(), ventaOnlineDTO.getFormasPago().get(0).getPorcentajeDescuento());
//
//            if (ConfigImpPorcentaje.COD_IMPUESTO_NORMAL.equals(linea.getArticulo().getCodimp())) {
//                subTotalIva = subTotalIva.add(subtotalIva);
//            } else {
//                subTotalIvaCero = subTotalIvaCero.add(subtotalIva);
//            }
//        }

        for (FormaPagoOnlineDTO formaPagoDTO : ventaOnlineDTO.getFormasPago()) {

            MedioPagoBean medioPago = MediosPago.consultarByIdPlan(formaPagoDTO.getIdPlan());
            MediosPago mediosPagoTodo = new MediosPago(new HashMap<String, MedioPagoBean>());
            mediosPagoTodo.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            //Se reemplaza el medio de pago con todos los planes de financiamiento
            medioPago = mediosPagoTodo.getMedioPago(medioPago.getCodMedioPago());
            //MedioPagoBean medioPago = TarjetaCredito.getBINMedioPagoBanda(formaPagoDTO.getNumeroTarjeta());
            //TarjetaCredito tarjetaCredito = getTarjetaCreditoByNumero(medioPago, formaPagoDTO.getNumeroTarjeta());
            TarjetaCredito tarjetaCredito = TarjetaCreditoBuilder.create(medioPago, TarjetaCredito.TARJETA_DEFECTO, false);
            PagoCredito pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, null, tarjetaCredito, ticket, (byte) 1);
            pagoCredito.setEntregado(formaPagoDTO.getaPagar());
            pagoCredito.setUstedPaga(formaPagoDTO.getaPagar());

            tarjetaCredito.setNumero(formaPagoDTO.getNumeroTarjeta());

            if (medioPago.isTarjetaSukasa()) {
                String[] datos = formaPagoDTO.getNumeroTarjeta().split("X");
                String binInicio = datos[0];
                String binFin = datos[datos.length - 1];
                tarjetaCredito.setNumero(CreditoServices.consultarPorBinTarjetaIdentificacion(ticket.getCliente().getIdentificacion(), binInicio, binFin).getNumeroTarjeta());
            }
            Integer posicion = null;
            for (PlanPagoCredito plan : pagoCredito.getPlanes()) {
                if (Objects.equals(plan.getNumCuotas(), formaPagoDTO.getNumCuotas())) {
                    posicion = pagoCredito.getPlanes().indexOf(plan);
                    break;
                }
            }

            if (pagoCredito.getPlanes().isEmpty()) {
                throw new Exception("No se puede recuperar los planes de pago");
            }

            if (posicion == null) {
                throw new Exception("No existe el plan de financiamiento a " + formaPagoDTO.getNumCuotas() + " meses para el medio de pago seleccionado");
            }
            pagoCredito.setPlanSeleccionado(posicion);

            PlanPagoCredito plan = pagoCredito.getPlanSeleccionado();

            plan.setDescuento(formaPagoDTO.getPorcentajeDescuento());
            plan.recalcularFromTotal(plan.getTotal(), null);
            BigDecimal diferencia = formaPagoDTO.getaPagar().subtract(plan.getaPagar());

            if (diferencia.abs().compareTo(new BigDecimal("0.01")) >= 0 && diferencia.abs().compareTo(new BigDecimal("0.05")) <= 0) {
                BigDecimal diferenciaItem = plan.getaPagar().multiply(Constantes.CIEN);
                BigDecimal porcentajeDescuento = Constantes.CIEN.subtract(diferenciaItem.divide(formaPagoDTO.getaPagar(), 6, BigDecimal.ROUND_HALF_UP).setScale(4, BigDecimal.ROUND_HALF_UP));
                formaPagoDTO.setPorcentajeDescuento(formaPagoDTO.getPorcentajeDescuento().subtract(porcentajeDescuento));
                plan.setDescuento(formaPagoDTO.getPorcentajeDescuento());
                plan.recalcularFromTotal(plan.getTotal(), null);
                diferencia = formaPagoDTO.getaPagar().subtract(plan.getaPagar());
            }

            if (diferencia.abs().compareTo(new BigDecimal("0.02")) >= 0) {
                throw new Exception("El total de la factura " + plan.getaPagar() + " no es igual al enviado por pagina web " + formaPagoDTO.getaPagar());
            }

            pagoCredito.setDescuento(formaPagoDTO.getPorcentajeDescuento());
            pagoCredito.setDescuentoCalculado(formaPagoDTO.getPorcentajeDescuento());
            //pagoCredito.calculaDescuento(formaPagoDTO.getPorcentajeDescuento());
            pagoCredito.setUstedPagaSinIva(subTotalIvaCero);
            pagoCredito.setUstedPaga(plan.getaPagar());
            pagoCredito.setEntregado(plan.getaPagar());
            pagoCredito.setPorcentajeInteres(formaPagoDTO.getPorcentajeInteres());
            pagoCredito.setImporteInteres(formaPagoDTO.getImporteInteres());

            pagoCredito.setSubtotalIva12(subTotalIva);
            pagoCredito.setSubtotalIva0(subTotalIvaCero);
            BigDecimal iva = pagoCredito.getUstedPaga().subtract(subTotalIvaCero).subtract(subTotalIva);
            pagoCredito.setIva(iva);

            Sesion.getTicket().crearNuevaLineaPago(pagoCredito);

            //Crea la informacion de la D_CAJA_DET_TBL
            /*int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, Sesion.cajaActual.getCajaActual().getUidDiarioCaja());
            CajaDet apunte = new CajaDet(Sesion.cajaActual.getCajaActual(), Sesion.cajaActual.getCajaParcialActual(), numerolinea, "VENTA", ticket.getIdFactura(), null, formaPagoDTO.getaPagar(), medioPago, Sesion.cajaActual.TIPO_VENTA, 'N');
            apunte.setIdDocumento(ticket.getUid_ticket());
            apunte.setReferencia(formaPagoDTO.getNumeroTarjeta());
            apunte.setPrefactura("S");

            //Registrar apunte
            GestionDeCajasDao.crearApunte(apunte, em);*/
            //Registra la Facturacion
            if (!medioPago.isTarjetaSukasa()) {
                FacturacionTarjeta ft = new FacturacionTarjeta(pagoCredito, ticket, "O");
                ft.setTipoTransaccion("FACTURA");
                ft.setIdReferencia(ticket.getUid_ticket());
                ft.setTrackI(String.valueOf(ticket.getId_ticket()));
                ft.setInteres(formaPagoDTO.getImporteInteres());
                ft.setValor(formaPagoDTO.getNumeroTarjeta());
                ft.setInteresPropio(formaPagoDTO.getPorcentajeInteres());
                ft.setLote(formaPagoOnlineDTO.getLote());
                ft.setSecuencialTransaccion(formaPagoDTO.getReferencia());
                String numeroAutorizacion = formaPagoDTO.getnReferenciaAutorizacion();
                if (numeroAutorizacion != null && numeroAutorizacion.length() > 6) {
                    ft.setNumeroAutorizacion(numeroAutorizacion.substring(0, 6));
                } else {
                    ft.setNumeroAutorizacion(numeroAutorizacion);
                }
                ft.setPrefactura("S");
                pagoCredito.setLote(formaPagoDTO.getLote());
                pagoCredito.setSecuencialTransaccion(formaPagoDTO.getReferencia());
                pagoCredito.setFacturacionTarjeta(ft);
                pagoCredito.setUidFacturacion(ft.getId());
                pagoCredito.setCodigoValidacionManual(numeroAutorizacion);
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft, em);
            } else {
                tarjetaCredito.validaTarjetaCredito();
            }
        }

    }

    /**
     * @author Gabriel Simbania
     * @param cabPrefactura
     */
    public static void crearFacturaDesdePrefactura(CabPrefactura cabPrefactura) {

        LineasTicket lineasTicket = new LineasTicket();
        Connection conn = new Connection();
        PrintServices ts = PrintServices.getInstance();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            cabPrefactura = CabPrefacturaDao.consultaCabeceraPrefacturaByUid(cabPrefactura.getUidCabId());

            if (EnumEstadoPagoPrefactura.PENDIENTE.equals(cabPrefactura.getCabEstadoPago())) {
                throw new PagoPrefacturaException("No se puede realizar la factura el pago se encuentra PENDIENTE\n "
                        + " Favor intentar en 5 minutos  ");
            } else if (EnumEstadoPagoPrefactura.RECHAZADO.equals(cabPrefactura.getCabEstadoPago())) {
                throw new PagoPrefacturaException("No se puede realizar la factura el pago se encuentra RECHAZADO ");
            }
            em.getTransaction().begin();
            ts.limpiarListaDocumentos();
            Cliente cliente = ClientesServices.getInstance().consultaClienteIdenti(cabPrefactura.getCabCodCli());
            cliente.setAplicaTarjetaAfiliado(true);
            TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
            tarjeta.setNumero(cliente.getCodcli());
            tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_CEDULA);
            tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_AUTOMATICA);
            cliente.setTarjetaAfiliacion(tarjeta);
            //Crea el cliente
            Sesion.iniciaNuevoTicket(cliente);
            TicketS ticket = Sesion.getTicket();
            FacturacionTicketBean facturacion = new FacturacionTicketBean();
            facturacion.setNombre(cabPrefactura.getCabNombre());
            facturacion.setApellidos(cabPrefactura.getCabApellido());
            facturacion.setDocumento(cabPrefactura.getCabFactIdentificacion());
            facturacion.setDireccion(cabPrefactura.getCabDireccion());
            facturacion.setProvincia(cliente.getPoblacion());
            facturacion.setTelefono(cabPrefactura.getCabTelefono());
            facturacion.setTipoDocumento(cabPrefactura.getCabTipoDoc());
            facturacion.setEmail(cabPrefactura.getCabEmail());
            ticket.setFacturacion(facturacion);

            PinPad.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);
            PinPadFasttrack.getInstance().setTipo(DatosConfiguracion.VALIDACION_AUTOMATICA);

            ticket.setCabPrefactura(cabPrefactura);
            //No calcula las promociones
            ticket.getTicketPromociones().setAplicaPromocionesLineas(Boolean.FALSE);

            conn.abrirConexion(Database.getConnection());
            List<DetPrefactura> detPrefacturas = DetPreFacturaDao.consultaDetalleByUidCabId(cabPrefactura.getUidCabId());
            lineasTicket.setLineas(Sesion.getTicket().getLineas().getLineas());

            //Asignar los articulos a la factura
            asignarArticulos(detPrefacturas, ticket);

            //Genera el pago
            generarFormaPago(cabPrefactura, ticket, em);

            //Registrar la factura
            escribirTicket(em, ticket, false, false);
            cabPrefactura.setCabEstado(EnumEstadoPrefactura.FACTURADO);
            cabPrefactura.setUidTicket(ticket.getUid_ticket());
            cabPrefactura.setCabAutorizador(Sesion.getAutorizadorPrefactura());

            CabPrefacturaDao.actualizarCabPrefactura(cabPrefactura, em);

            //Realizar el cobro
            realizarCobroTarjetasCredito(cabPrefactura, ticket);
            em.getTransaction().commit();

            //TODO verificar si se debe completar la orden
            generarCompletarOrdenPrefactura(ticket, cabPrefactura);
            generarKardexReservaPrefactura(detPrefacturas, cabPrefactura, EnumTipoDocumento.RESERVA_PREFACTURA_FACTURADA);

            try {
                ts.imprimirTicket(ticket);
                DocumentosService.crearDocumento(ticket, ts.getDocumentosImpresos(), DocumentosBean.FACTURA);
            } catch (Exception ex) {
                log.error("Error al imprimir la factura " + ex.getMessage(), ex);
            }
            JPrincipal.getInstance().crearInformacion("Se creo la factura " + ticket.getTienda() + ticket.getCodcaja() + String.format("%09d", ticket.getId_ticket()) + " correctamente ");

        } catch (PagoPrefacturaException ex) {
            JPrincipal.getInstance().crearError(ex.getMessage());
            em.getTransaction().rollback();
        } catch (PagoInvalidException ex) {
            log.error("Error al pagar la prefactura ", ex);
            JPrincipal.getInstance().crearError("Se produjo un error al realizar el pago " + ex.getMessage());
            em.getTransaction().rollback();
        } catch (Exception ex) {
            log.error("Error al grabar la prefactura ", ex);
            JPrincipal.getInstance().crearError("Se produjo un error al generar la factura " + ex.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    log.error("Error SQL ", ex);
                }
            }
        }
    }

    /**
     * @author Gabriel Simbania
     * @param detPrefacturas
     * @param ticket
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     */
    private static void asignarArticulos(List<DetPrefactura> detPrefacturas, TicketS ticket) throws ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException, NoResultException, XMLDocumentException, TicketException {

        HashMap<Long, LineaTicket> lineaMap = new HashMap<>();
        //Asigna los articulos
        for (DetPrefactura det : detPrefacturas) {
            if (!det.getDetIsGarantiaExtendida()) {
                Articulos articulos = det.getArticulo();
                String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());
                TarifasServices tarifasServices = new TarifasServices();
                Tarifas tarifa = tarifasServices.getTarifaArticulo(articulos.getCodart());
                LineaTicket lineaTicket = ticket.crearLineaTicketPrefactura(codBarras, articulos, det.getDetCantidad(), tarifa, det);
                lineaMap.put(det.getDetLinea(), lineaTicket);
            }
        }

        //Asignar Extension de garantia
        for (DetPrefactura extensionGarantiaArt : detPrefacturas) {
            if (extensionGarantiaArt.getDetIsGarantiaExtendida() && extensionGarantiaArt.getUidFacturaReferenciaEgo() == null) {
                crearExtensionGarantiaPrefactura(lineaMap, detPrefacturas, ticket, extensionGarantiaArt);
            } else if (extensionGarantiaArt.getDetIsGarantiaExtendida() && extensionGarantiaArt.getUidFacturaReferenciaEgo() != null) {
                crearExtensionGarantiaPrefacturaFacturaExterna(extensionGarantiaArt, ticket);
            }
        }

        //Asigna extension garantia gratis para sukasa gold
        //asignarGarantiaGold(lineaMap, ticket,detPrefacturas.get(0).getCabPrefactura().getUidCabId() );
    }

    /**
     * @author Gabriel Simbania
     * @description Asigna extension garantia gratis para sukasa gold
     * @param lineaMap
     * @param ticket
     * @param uidCabId
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     * @throws NoResultException
     * @throws XMLDocumentException
     * @throws TicketException
     */
    private static void asignarGarantiaGold(HashMap<Long, LineaTicket> lineaMap, TicketS ticket, String uidCabId) throws ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException, NoResultException, XMLDocumentException, TicketException {

        TarifasServices tarifasServices = new TarifasServices();
        List<PwFormasPago> formaPagoList = PwFormasPagoDao.consultaFormasPagoByUidCabId(uidCabId);
        PwFormasPago formaPago = formaPagoList.get(0);
        if (EnumTipoPagoPaginaWeb.CREDITO_DIRECTO.equals(formaPago.getPwpTipoPago())) {
            //Asigna los articulos
            for (Map.Entry<Long, LineaTicket> det : lineaMap.entrySet()) {
                LineaTicket linea = det.getValue();
                boolean garantiaGratis = false;
                if (det.getValue().getArticulo().isGarantiaExtendida()) {
                    if (Sesion.getTicket().getCliente().isGarantiaExtendidaGratis()) {
                        garantiaGratis = true;
                    }

                    if (garantiaGratis) {
                        GarantiaExtendida garantia = new GarantiaExtendida(linea.getArticulo(), linea.getPrecioTotalTarifaOrigen());
                        Tarifas tarifa = tarifasServices.getTarifaArticulo(garantia.getArticuloGarantia().getCodart());
                        LineaTicket lineaGarantia = ticket.crearLineaTicket(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS), garantia.getArticuloGarantia(), 1, tarifa, 0);
                        lineaGarantia.setCodEmpleado(Variables.getVariable(Variables.USUARIO_EGO_DEFECTO));
                        lineaGarantia.setGarantiaGratuita(Boolean.TRUE);
                        lineaGarantia.setDescuentoManualLinea(Numero.CIEN);

                        GarantiaReferencia referencia = new GarantiaReferencia(linea);
                        lineaGarantia.setReferenciaGarantia(referencia);
                        garantia.actualizarPreciosGarantiaTicket(lineaGarantia, Sesion.getTicket());
                        Sesion.getTicket().recalcularTotales();
                    }
                }

            }
        }

    }

    /**
     * @author Gabriel Simbania
     * @param lineaMap
     * @param detPrefacturas
     * @param ticket
     * @param extensionGarantiaArt
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     */
    private static void crearExtensionGarantiaPrefactura(HashMap<Long, LineaTicket> lineaMap, List<DetPrefactura> detPrefacturas, TicketS ticket,
            DetPrefactura extensionGarantiaArt) throws ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException {
        DetPrefactura referencia = null;
        for (DetPrefactura ref : detPrefacturas) {
            if (Objects.equals(ref.getDetLinea(), extensionGarantiaArt.getDetLineaReferenciaEgo())) {
                referencia = ref;
                break;
            }
        }
        if (referencia == null) {
            throw new ArticuloException("La extensi\00F3n de garant\u00EDa de la l\u00EDnea " + extensionGarantiaArt.getDetLinea() + " no tiene referencia");
        }

        Articulos articulos = extensionGarantiaArt.getArticulo();
        String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());
        TarifasServices tarifasServices = new TarifasServices();
        Tarifas tarifa = tarifasServices.getTarifaArticulo(articulos.getCodart());
        LineaTicket lineaTicket = ticket.crearLineaTicketPrefactura(codBarras, articulos, extensionGarantiaArt.getDetCantidad(), tarifa, extensionGarantiaArt);
        lineaTicket.setCodEmpleado(Variables.getVariable(Variables.USUARIO_EGO_DEFECTO));

        LineaTicket lineaTicketOrigen = lineaMap.get(referencia.getDetLinea());

        //G.S. Referencia del EGO desde otra factura
        GarantiaExtendida garantia = new GarantiaExtendida(articulos, extensionGarantiaArt.getDetPvpTotalSinIvaConDsto(), extensionGarantiaArt.getDetPvpTotalConIvaConDsto());
        garantia.actualizarPreciosGarantiaTicketOnline(lineaTicket, Sesion.getTicket(), extensionGarantiaArt.getDetIsGarantiaExtendidaGratis());
        GarantiaReferencia garantiaReferencia = new GarantiaReferencia(lineaTicketOrigen);
        lineaTicket.setReferenciaGarantia(garantiaReferencia);

    }

    /**
     * @author Gabriel Simbania
     * @param extensionGarantiaArt
     * @param ticket
     * @throws NoResultException
     * @throws XMLDocumentException
     * @throws TicketException
     * @throws ArticuloNotFoundException
     * @throws ArticuloException
     * @throws TicketNuevaLineaException
     */
    private static void crearExtensionGarantiaPrefacturaFacturaExterna(DetPrefactura extensionGarantiaArt, TicketS ticket) throws NoResultException, XMLDocumentException, TicketException, ArticuloNotFoundException, ArticuloException, TicketNuevaLineaException {

        TicketsAlm ticketsAlmOrigen = TicketService.consultarTicket(extensionGarantiaArt.getUidFacturaReferenciaEgo());
        TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(ticketsAlmOrigen.getXMLTicket()));
        LineaTicket lineaReferencia = ticketOrigen.getLinea(extensionGarantiaArt.getDetLineaReferenciaEgo().intValue());
        if (lineaReferencia == null) {
            throw new TicketNuevaLineaException("No existe la el articulo de referncia para la l\u00EDnea " + extensionGarantiaArt.getDetLinea());
        }

        Articulos articulo = extensionGarantiaArt.getArticulo();
        Articulos ariculoReferencia = ArticulosServices.getInstance().getArticuloCod(lineaReferencia.getArticulo().getCodart());
        lineaReferencia.setArticulo(ariculoReferencia);
        String codBarras = ArticulosServices.consultarCodigoBarras(articulo.getCodart());
        TarifasServices tarifasServices = new TarifasServices();
        Tarifas tarifa = tarifasServices.getTarifaArticulo(articulo.getCodart());
        LineaTicket lineaGarantia = ticket.crearLineaTicketPrefactura(codBarras, articulo, extensionGarantiaArt.getDetCantidad(), tarifa, extensionGarantiaArt);
        lineaGarantia.setCodEmpleado(Variables.getVariable(Variables.USUARIO_EGO_DEFECTO));

        GarantiaExtendida garantia = new GarantiaExtendida(ariculoReferencia, extensionGarantiaArt.getDetPvpTotalSinIvaConDsto(), extensionGarantiaArt.getDetPvpTotalConIvaConDsto());
        garantia.actualizarPreciosGarantiaTicketOnline(lineaGarantia, Sesion.getTicket(), extensionGarantiaArt.getDetIsGarantiaExtendidaGratis());
        GarantiaReferencia garantiaReferencia = new GarantiaReferencia(ariculoReferencia, ticketOrigen);
        garantiaReferencia.setLineaOrigen(lineaReferencia);
        lineaGarantia.setReferenciaGarantia(garantiaReferencia);

    }

    /**
     * @author Gabriel Simbania
     * @param detPrefacturas
     * @param cabPrefactura
     * @param enumTipoDocumento
     */
    public static void generarKardexReservaPrefactura(List<DetPrefactura> detPrefacturas, CabPrefactura cabPrefactura, EnumTipoDocumento enumTipoDocumento) {

        try {
            log.debug("Creando la reserva para el kardex");

            List<ItemDTO> itemDTOLista = new ArrayList<>();
            for (DetPrefactura linea : detPrefacturas) {
                ItemDTO itemDTO = new ItemDTO();
                if (EnumTipoDocumento.RESERVA_PREFACTURA_FACTURADA.equals(enumTipoDocumento)) {
                    itemDTO.setCantidad((long) linea.getDetCantidad());
                } else {
                    itemDTO.setCantidad((long) linea.getDetCantidad() * -1);
                }

                itemDTO.setCodigoI(linea.getArticulo().getCodmarca().getCodmarca() + "-" + linea.getArticulo().getIdItem());
                itemDTOLista.add(itemDTO);
            }

            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(Sesion.getUsuario().getUsuario());
            String numeroDocumento = Sesion.getTienda().getCodalm() + "-" + cabPrefactura.getUidCabId();
            DocumentoDTO documentoDTO = new DocumentoDTO(numeroDocumento, Sesion.getTienda().getCodalmSRI(), null, itemDTOLista, usuarioDTO);
            documentoDTO.setTipoDocumento(enumTipoDocumento.getNombre());
            documentoDTO.setTipoMovimiento(Constantes.MOVIMIENTO_52);

            String documentoString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread encolarKardexThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), documentoString, Variables.getVariable(Variables.QUEUE_KARDEX_POS), Constantes.PROCESO_KARDEX_POS, cabPrefactura.getUidCabId());
            encolarKardexThread.start();

        } catch (Throwable e) {
            log.error("Error en la reserva de la prefactura del kardex " + e.getMessage(), e);
        }
    }

    /**
     * @author Gabriel Simbania
     * @param cabPrefactura
     * @param ticket
     * @param em
     * @throws ValidationException
     * @throws ImporteInvalidoException
     * @throws MovimientoCajaException
     * @throws CajaException
     * @throws Exception
     */
    private static void generarFormaPago(CabPrefactura cabPrefactura, TicketS ticket,
            EntityManager em) throws ValidationException, ImporteInvalidoException, MovimientoCajaException, CajaException, Exception {

        List<PwFormasPago> formasPagoListaPW = PwFormasPagoDao.consultaFormasPagoByUidCabId(cabPrefactura.getUidCabId());
        ticket.inicializaTotales(ticket.getTotales().getTotalAPagar());
        ticket.getTotales().setTotalPagado(cabPrefactura.getCabTotalConDstoConIva());
        ticket.getTotales().setBase(cabPrefactura.getCabTotalConDstoSinIva());
        ticket.getTotales().setImporteTotalTarifaOrigen(cabPrefactura.getCabTotalSinDstoConIva());
        ticket.getTotales().setImporteTarifaOrigen(cabPrefactura.getCabTotalSinDstoSinIva());
        //ticket.getTotales().setTotalDtoPagos(cabPrefactura.getCabDescuentoFormaPago());

        for (PwFormasPago formasPagoPW : formasPagoListaPW) {
            MedioPagoBean medioPago = TarjetaCredito.getBINMedioPagoBanda(formasPagoPW.getPwpNumeroTarjeta());
            TarjetaCredito tarjetaCredito = getTarjetaCreditoByNumero(medioPago, formasPagoPW.getPwpNumeroTarjeta());
            PagoCredito pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, null, tarjetaCredito, ticket, (byte) 1);

            pagoCredito.setEntregado(formasPagoPW.getPwpValorTotal());
            pagoCredito.setTotalSinIva(formasPagoPW.getPwpValorBase0().add(formasPagoPW.getPwpValorBase12()));
            pagoCredito.setUstedPagaSinIva(formasPagoPW.getPwpValorBase0());
            pagoCredito.setUstedPaga(formasPagoPW.getPwpValorTotal());
            if (EnumTipoPagoPaginaWeb.CREDITO_DIRECTO.equals(formasPagoPW.getPwpTipoPago())) {
                String autorizacion = JsonUtil.getElementJson(formasPagoPW.getPwpNAutorizacion(), "autorizacion");
                pagoCredito.setCodigoValidacionManual(autorizacion);
            }
            int posicion = 0;
            for (PlanPagoCredito plan : pagoCredito.getPlanes()) {
                if (Objects.equals(plan.getNumCuotas(), formasPagoPW.getPwpNumeroMeses().intValue())) {
                    posicion = pagoCredito.getPlanes().indexOf(plan);
                    break;
                }
            }

            if (pagoCredito.getPlanes().isEmpty()) {
                throw new Exception("No se puede recuperar los planes de pago");
            }
            pagoCredito.setPlanSeleccionado(posicion);

            PlanPagoCredito plan = pagoCredito.getPlanSeleccionado();
            if (plan.getDescuento().compareTo(cabPrefactura.getCabDescuentoFormaPago()) == 0) {
                BigDecimal diferencia = formasPagoPW.getPwpValorTotal().subtract(plan.getaPagar());
                if (diferencia.abs().compareTo(new BigDecimal("0.05")) >= 0) {
                    throw new Exception("El total de la factura " + plan.getaPagar() + " no es igual al enviado por pagina web " + formasPagoPW.getPwpValorTotal());
                }
            } else {
                plan.setDescuento(cabPrefactura.getCabDescuentoFormaPago());
                plan.recalcularFromTotal(cabPrefactura.getCaTotalPvpConPromocion(), null);
            }
            pagoCredito.setSubtotalIva12(formasPagoPW.getPwpValorBase12());
            pagoCredito.setSubtotalIva0(formasPagoPW.getPwpValorBase0());
            pagoCredito.setTotal(plan.getTotal());
            pagoCredito.setTotalSinIva(cabPrefactura.getCabTotalSinDstoSinIva());
            pagoCredito.setDescuento(cabPrefactura.getCabDescuentoFormaPago());
            pagoCredito.calculaDescuento(cabPrefactura.getCabDescuentoFormaPago());
            pagoCredito.setUstedPagaSinIva(formasPagoPW.getPwpValorBase0());
            pagoCredito.setUstedPaga(plan.getaPagar());
            pagoCredito.setEntregado(plan.getaPagar());

            Sesion.getTicket().crearNuevaLineaPago(pagoCredito);
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, Sesion.cajaActual.getCajaActual().getUidDiarioCaja());
            CajaDet apunte = new CajaDet(Sesion.cajaActual.getCajaActual(), Sesion.cajaActual.getCajaParcialActual(), numerolinea, "VENTA", ticket.getIdFactura(), null, formasPagoPW.getPwpValorTotal(), medioPago, Sesion.cajaActual.TIPO_MOVIMIENTO, 'N');
            apunte.setIdDocumento(ticket.getUid_ticket());
            apunte.setPrefactura("S");

            //Registrar apunte
            GestionDeCajasDao.crearApunte(apunte, em);

            //Registra la Facuturacion
            if (!medioPago.isTarjetaSukasa()) {
                FacturacionTarjeta ft = new FacturacionTarjeta(pagoCredito, ticket, "O");
                ft.setTipoTransaccion("FACTURA");
                ft.setIdReferencia(ticket.getUid_ticket());
                ft.setTrackI(String.valueOf(ticket.getId_ticket()));
                String numeroAutorizacion = formasPagoPW.getPwpNAutorizacion();
                if (numeroAutorizacion != null && numeroAutorizacion.length() > 6) {
                    ft.setNumeroAutorizacion(numeroAutorizacion.substring(0, 5));
                } else {
                    ft.setNumeroAutorizacion(numeroAutorizacion);
                }
                ft.setPrefactura("S");
                pagoCredito.setFacturacionTarjeta(ft);
                ServicioFacturacionTarjetas.insertarFacturacionTarjeta(ft, em);
            }
        }
    }

    /**
     * @author Gabriel Simbania
     * @param medioPago
     * @param numeroTarjeta
     * @return
     * @throws CreditoException
     * @throws CreditoNotFoundException
     */
    private static TarjetaCredito getTarjetaCreditoByNumero(MedioPagoBean medioPago, String numeroTarjeta) throws CreditoException, CreditoNotFoundException {

        String banda = "%B" + numeroTarjeta + "&";
        if (medioPago.isTarjetaSukasa()) {
            PlasticoBean plasticoBean = CreditoServices.consultarPlasticoPorNumero(numeroTarjeta);
            TarjetaCreditoSK tarjetaCreditoSK = new TarjetaCreditoSK(banda, plasticoBean);
            return tarjetaCreditoSK;
        } else {
            TarjetaCredito tarjetaCredito = new TarjetaCredito(banda);
            return tarjetaCredito;
        }
    }

    /**
     * @authorization Gabriel Simbania
     * @param cabPrefactura
     * @param ticketS
     * @throws Exception
     */
    private static void realizarCobroTarjetasCredito(CabPrefactura cabPrefactura, TicketS ticketS) throws PagoInvalidException, NoSuchAlgorithmException {

        List<PwFormasPago> formasPagoListaPW = PwFormasPagoDao.consultaFormasPagoByUidCabId(cabPrefactura.getUidCabId());

        //No realice el consumo si es pruebas
        //if (!Sesion.getDatosConfiguracion().isModoDesarrollo()) {
        for (PwFormasPago formasPago : formasPagoListaPW) {
            //Pago con Place to Pay
            if (!EnumTipoPagoPaginaWeb.CREDITO_DIRECTO.equals(formasPago.getPwpTipoPago())) {

                if (!EnumEstadoPagoPrefactura.PAGADO.equals(cabPrefactura.getCabEstadoPago())) {

                    ServicioPagoTarjetasOnline servicioPagoTarjetasOnline = new ServicioPagoTarjetasOnline();

                    String inputObject = null;
                    if (EnumTipoPagoPaginaWeb.PLACE_TO_PAY.equals(formasPago.getPwpTipoPago())) {
                        CollectRequest collectRequest = servicioPagoTarjetasOnline.crearCollectRequest(ticketS, formasPago.getPwpNAutorizacion(), formasPago.getPwpNumeroMeses(), cabPrefactura.getUidCabId());
                        inputObject = JsonUtil.objectToJson(collectRequest);
                    } else if (EnumTipoPagoPaginaWeb.PAYMENTEZ.equals(formasPago.getPwpTipoPago())) {
                        DebitRequestDTO debitRequest = servicioPagoTarjetasOnline.crearDebitRequest(ticketS, formasPago.getPwpNAutorizacion(), cabPrefactura.getCabIdPedido(), formasPago.getPwpNumeroMeses(), cabPrefactura.getUidCabId());
                        inputObject = JsonUtil.objectToJson(debitRequest);
                    }

                    PagoFacturaInputDTO pagoFacturaInputDTO = new PagoFacturaInputDTO(cabPrefactura.getCabIdPedido(), inputObject, cabPrefactura.getCabCodAlm(), formasPago.getPwpTipoPago().ordinal(),
                            formasPago.getPwpNAutorizacion(), cabPrefactura.getUidCabId());
                    String url = Variables.getVariable(Variables.WEBSERVICE_PAGINA_WEB_ESB_URL);
                    ResponseDTO responseDTO = null;
                    try {
                        log.info("JSON: Peticion" + JsonUtil.objectToJson(pagoFacturaInputDTO));
                        ClienteRest clienteRest = new ClienteRest();
                        responseDTO = clienteRest.clientRestPOST(url + "pagoFacturaOnline", pagoFacturaInputDTO, null, ResponseDTO.class);

                    } catch (ConnectException ex) {
                        log.error("Servicio ESB no disponible " + url, ex);
                        throw new PagoInvalidException("Servicio ESB no disponible ");
                    } catch (IOException ex) {
                        log.error("No se pudo realizar el pago IOException ", ex);
                        throw new PagoInvalidException("No se pudo realizar el pago " + ex.getMessage());
                    }
                    if (responseDTO.getExito()) {
                        responseDTO.getObjetoRespuesta();
                    } else {
                        throw new PagoInvalidException("No se pudo realizar el pago al proveedor " + responseDTO.getDescripcion());
                    }

                }
            }
        }
        //}
    }

    /**
     *
     * @param lineaMap
     * @param items
     * @param ticket
     * @param extensionGarantiaArt
     * @param articulos
     * @throws ArticuloException
     * @throws ArticuloNotFoundException
     * @throws TicketNuevaLineaException
     */
    private static void crearExtensionGarantiaOnline(HashMap<Long, LineaTicket> lineaMap, List<ItemOnlineDTO> items, TicketS ticket,
            ItemOnlineDTO extensionGarantiaArt, Articulos articulos) throws ArticuloException, ArticuloNotFoundException, TicketNuevaLineaException {
        ItemOnlineDTO referencia = null;
        for (ItemOnlineDTO ref : items) {
            if (Objects.equals(ref.getIdLinea(), extensionGarantiaArt.getIdLineaReferencia())) {
                referencia = ref;
                break;
            }
        }
        if (referencia == null) {
            throw new ArticuloException("La extensi\00F3n de garant\u00EDa de la l\u00EDnea " + extensionGarantiaArt.getIdLinea() + " no tiene referencia");
        }

        //Articulos articulos = extensionGarantiaArt.getArticulo();
        String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());
        TarifasServices tarifasServices = new TarifasServices();
        Tarifas tarifa = tarifasServices.getTarifaArticulo(articulos.getCodart());
        tarifa.cambiarPrecioTotal(extensionGarantiaArt.getItmPvpUnitario(), articulos);
        LineaTicket lineaTicket = ticket.crearLineaTicketOnline(codBarras, articulos, extensionGarantiaArt.getItmCantidad().intValue(), tarifa, extensionGarantiaArt);
        lineaTicket.setCodEmpleado(Variables.getVariable(Variables.USUARIO_EGO_DEFECTO));

        LineaTicket lineaTicketOrigen = lineaMap.get(referencia.getIdLinea());

        //G.S. Referencia del EGO 
        boolean garantiaGratis = Boolean.FALSE;
        BigDecimal precioConIva = extensionGarantiaArt.getItmPvpUnitario();
        if (extensionGarantiaArt.getItmPrecioTotal().compareTo(BigDecimal.ZERO) == 0) {
            garantiaGratis = Boolean.TRUE;
            precioConIva = extensionGarantiaArt.getItmPvpUnitario();
        }
        BigDecimal precioSinIva;
        if (extensionGarantiaArt.getItmCobraIva()) {
            BigDecimal divisor = Constantes.CIEN.add(Sesion.getEmpresa().getPorcentajeIva()).divide(Constantes.CIEN, 6, BigDecimal.ROUND_HALF_UP);
            precioSinIva = precioConIva.divide(divisor, 6, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            precioSinIva = precioConIva;
        }

        GarantiaExtendida garantia = new GarantiaExtendida(articulos, precioSinIva, precioConIva);
        garantia.actualizarPreciosGarantiaTicketOnline(lineaTicket, Sesion.getTicket(), garantiaGratis);
        GarantiaReferencia garantiaReferencia = new GarantiaReferencia(lineaTicketOrigen);
        lineaTicket.setReferenciaGarantia(garantiaReferencia);
        lineaMap.put(extensionGarantiaArt.getIdLinea(), lineaTicket);

    }

}
