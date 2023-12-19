/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.documentos;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.entity.services.reservaciones.ComprobanteAbono;
import com.comerzzia.jpos.entity.services.reservaciones.ComprobanteReservacion;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardBean;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosExample;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosMapper;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosExample;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosMapper;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponSorteoSukasa;
import com.comerzzia.jpos.servicios.promociones.cupones.ServicioCupones;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.exception.PersistenceException;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author SMLM
 */
public class DocumentosService {

    protected static Logger log = Logger.getMLogger(DocumentosService.class);

    public static void crearDocumento(TicketS ticket, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {

        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(ticket.getTienda());
        documentoBean.setCodCaja(ticket.getCodcaja());
        documentoBean.setIdDocumento(String.valueOf(ticket.getId_ticket()));
        documentoBean.setFecha(ticket.getFecha());
        documentoBean.setCodCliente(ticket.getCliente().getCodcli());
        documentoBean.setMonto(ticket.getTotales().getTotalPagado());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(ticket.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + Numero.completaconCeros(documentoBean.getIdDocumento(), 9));
        documentoBean.setCodCajaEmision(ticket.getCodcaja());

        crearDocumento(documentoBean, null);
    }

    public static void crearDocumento(TicketS ticket, List<DocumentosImpresosBean> documentosImpresos, String tipo, EntityManager em) throws DocumentoException {

        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(ticket.getTienda());
        documentoBean.setCodCaja(ticket.getCodcaja());
        documentoBean.setIdDocumento(String.valueOf(ticket.getId_ticket()));
        documentoBean.setFecha(ticket.getFecha());
        documentoBean.setCodCliente(ticket.getCliente().getCodcli());
        documentoBean.setMonto(ticket.getTotales().getTotalPagado());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(ticket.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + Numero.completaconCeros(documentoBean.getIdDocumento(), 9));
        documentoBean.setCodCajaEmision(ticket.getCodcaja());

        SqlSession session = new SqlSession();
        try {
            Connection conn = Connection.getConnection(em);
            session.openSession(SessionFactory.openSession(conn));
            crearDocumento(documentoBean, null, session);
        } catch (DocumentoException e) {
            throw e;
        } catch (Exception e) {
            log.error("crearDocumento() - Se ha producido un error inesperado al crear el documento: " + e.getMessage(), e);
            throw new DocumentoException(e.getMessage(), e);
        }
    }

    public static void crearDocumentoGiftCard(LogGiftCardBean logGiftCard, List<DocumentosImpresosBean> documentosImpresos, String tipo, String observaciones) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(logGiftCard.getCodAlmacen());
        documentoBean.setCodCaja(logGiftCard.getCodCaja());
        documentoBean.setIdDocumento(String.valueOf(logGiftCard.getIdCargaGiftCard()));
        documentoBean.setFecha(logGiftCard.getFechaHora());
        documentoBean.setCodCliente(logGiftCard.getCodCliente());
        documentoBean.setMonto(logGiftCard.getAbono());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(observaciones);
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
        documentoBean.setReferencia(logGiftCard.getIdGiftCard());
        documentoBean.setCodCajaEmision(logGiftCard.getCodCaja());

        crearDocumento(documentoBean, null);
    }

    public static void crearDocumentoGiftCardPago(PagoGiftCard pagoGC, DocumentosImpresosBean documentoImpreso, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(Sesion.getTienda().getCodalmSRI());
        documentoBean.setCodCaja(Sesion.getTienda().getCajaActiva().getCodcajaSri());
        documentoBean.setIdDocumento(String.valueOf(pagoGC.getIdUsoGiftCard()));
        documentoBean.setFecha(new Fecha());
        documentoBean.setCodCliente(pagoGC.getCliente().getCodcli());
        documentoBean.setMonto(pagoGC.getTotal());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(pagoGC.getGiftCard().getObservaciones());
        List<DocumentosImpresosBean> documentosImpresos = new ArrayList<DocumentosImpresosBean>();
        documentosImpresos.add(documentoImpreso);
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCaja(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_GIFTCARD_P);
    }

    public static void crearDocumentoCotizacion(TicketS ticket, List<DocumentosImpresosBean> documentosImpresos, String tipo, SqlSession session) throws DocumentoException {

        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(ticket.getTienda());
        documentoBean.setCodCaja(ticket.getCodcaja());
        documentoBean.setIdDocumento(String.valueOf(ticket.getId_ticket()));
        documentoBean.setFecha(ticket.getFecha());
        documentoBean.setCodCliente(ticket.getCliente().getCodcli());
        documentoBean.setMonto(ticket.getTotales().getTotalAPagar());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(ticket.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(ticket.getCodcaja());

        DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
        DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);
        mapper.insert(documentoBean);

        Short i = 0;
        for (DocumentosImpresosBean impreso : documentoBean.getImpresos()) {
            impreso.setUidDocumento(documentoBean.getUidDocumento());
            impreso.setIdImpreso(i);
            mapperImpresos.insert(impreso);
            i++;
        }
    }

    public static void crearDocumentoLetraAbono(TicketS ticket, List<DocumentosImpresosBean> documentosImpresos, LetraBean letra, LetraCuotaBean letraCuota, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(letra.getCodAlmacen());
        documentoBean.setCodCaja(letraCuota.getCodcajaAbono());
        documentoBean.setIdDocumento(String.valueOf(letraCuota.getIdAbono()));
        documentoBean.setFecha(letraCuota.getFechaCobro());
        documentoBean.setCodCliente(letra.getCliente().getCodcli());
        documentoBean.setMonto(letraCuota.getTotal());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(letraCuota.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(letraCuota.getCodcajaAbono());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_LETRA);
    }

    public static void crearDocumentoCreditoAbono(TicketS ticket, List<DocumentosImpresosBean> documentosImpresos, AbonoCreditoBean abonoCredito, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(abonoCredito.getCodAlmacen());
        documentoBean.setCodCaja(abonoCredito.getCodCaja());
        documentoBean.setIdDocumento(String.valueOf(abonoCredito.getIdentificador()));
        documentoBean.setFecha(abonoCredito.getFecha());
        documentoBean.setCodCliente(abonoCredito.getCodCliente());
        documentoBean.setMonto(abonoCredito.getTotalConDto());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(abonoCredito.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(abonoCredito.getCodCaja());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_CREDITO);
    }

    public static void crearDevolucion(com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();

        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(devolucion.getNotaCredito().getCodalm());
        documentoBean.setCodCaja(devolucion.getNotaCredito().getCodcaja());
        documentoBean.setIdDocumento(String.valueOf(devolucion.getNotaCredito().getIdNotaCredito()));
        documentoBean.setFecha(new Fecha(devolucion.getNotaCredito().getFecha()));
        documentoBean.setCodCliente(devolucion.getTicketOriginal().getCliente().getCodcli());
        documentoBean.setMonto(devolucion.getNotaCredito().getTotal());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(devolucion.getDevolucion().getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + Numero.completaconCeros(documentoBean.getIdDocumento(), 9));
        documentoBean.setFechaCaducidad(new Fecha(devolucion.getNotaCredito().getFechaValidez()));
        documentoBean.setCodCajaEmision(devolucion.getNotaCredito().getCodcaja());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_NC);
    }

    public static void crearReservacion(ComprobanteReservacion reservacion, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        TicketS ticket = reservacion.getTicket();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(ticket.getTienda());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(reservacion.getCodReserva());
        documentoBean.setFecha(ticket.getFecha());
        documentoBean.setCodCliente(ticket.getCliente().getCodcli());
        documentoBean.setMonto(new BigDecimal(reservacion.getTotal()));
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(ticket.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setFechaCaducidad(reservacion.getReserva().getCaducidad());
        documentoBean.setCodCajaEmision(ticket.getCodcaja());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_RESERVACION);
    }

    public static void crearDocumentoPlanNovio(PlanNovioOBJ plan, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(plan.getPlan().getPlanNovioPK().getCodalm());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(String.valueOf(plan.getPlan().getPlanNovioPK().getIdPlan()));
        documentoBean.setFecha(new Fecha(plan.getPlan().getFechaAlta()));
        documentoBean.setCodCliente(plan.getPlan().getNovia().getCodcli());
        documentoBean.setMonto(BigDecimal.ZERO);
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(plan.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setFechaCaducidad(new Fecha(plan.getPlan().getCaducidad()));
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_PLAN_NOVIO);
    }

    public static void crearAbonoReserva(String codAlm, ComprobanteAbono comprobanteAbono, Long idAbono, List<DocumentosImpresosBean> documentosImpresos, String tipo, boolean vieneRes) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(codAlm);
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        // En plan novio el CodReserva es de la forma CODALM-CODRESERVA (del que solo nos interesa el segundo)
        if (comprobanteAbono.getCodReserva().indexOf('-') != -1) {
            documentoBean.setIdDocumento(comprobanteAbono.getCodReserva().split("-")[1] + "/" + idAbono);
        } else {
            documentoBean.setIdDocumento(comprobanteAbono.getCodReserva() + "/" + idAbono);
        }
        documentoBean.setFecha(new Fecha());
        documentoBean.setCodCliente(comprobanteAbono.getClientePagador().getCodcli());
        documentoBean.setMonto(comprobanteAbono.getPagos().getUstedPaga());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(comprobanteAbono.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        if (vieneRes) {
            crearDocumento(documentoBean, null);
        } else {
            crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_ABONO_PLAN_NOVIO);
        }
    }

    public static void crearDocumentoLiquidaRE(Reservacion reservacion, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(reservacion.getReservacion().getCodalm());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(String.valueOf(reservacion.getReservacion().getCodReservacion()));
        documentoBean.setFecha(new Fecha());
        documentoBean.setCodCliente(reservacion.getReservacion().getCliente().getCodcli());
        documentoBean.setMonto(reservacion.getComprado());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(reservacion.getObservaciones());
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_RES_LIQUIDA);
    }

    public static void crearDocumentoLiquidaPN(PlanNovio plan, List<DocumentosImpresosBean> documentosImpresos, String tipo, String observaciones) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(plan.getPlanNovioPK().getCodalm());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(String.valueOf(plan.getPlanNovioPK().getIdPlan()));
        documentoBean.setFecha(new Fecha(new Date()));
        documentoBean.setCodCliente(plan.getNovia().getCodcli());
        documentoBean.setMonto(plan.getComprado());
        documentoBean.setEstado("V"); //Por defecto a Vigente
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones(observaciones);
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_PLAN_NOVIO_LIQU);
    }

    public static void crearDocumentoBonoReserva(Bono bono, String referenciaOrigen, List<DocumentosImpresosBean> documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(bono.getBonoPK().getCodalm());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(String.valueOf(bono.getBonoPK().getIdBono()));
        documentoBean.setFecha(new Fecha());
        documentoBean.setCodCliente(bono.getCodCliente());
        documentoBean.setMonto(bono.getImporte());
        documentoBean.setEstado("V");
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones("");
        documentoBean.setImpresos(documentosImpresos);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setFechaCaducidad(new Fecha(bono.getFechaCaducidad()));
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_RES_BONO);
    }

    public static void crearDocumentoBonoPago(Bono bono, DocumentosImpresosBean documentosImpresos, String tipo) throws DocumentoException {
        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(bono.getBonoPK().getCodalm());
        documentoBean.setCodCaja(DocumentosBean.CODCAJA);
        documentoBean.setIdDocumento(String.valueOf(bono.getBonoPK().getIdBono()));
        documentoBean.setFecha(new Fecha());
        documentoBean.setCodCliente(bono.getCodCliente());
        documentoBean.setMonto(bono.getImporte());
        documentoBean.setEstado("V");
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        documentoBean.setObservaciones("");
        List<DocumentosImpresosBean> docImps = new ArrayList<DocumentosImpresosBean>();
        docImps.add(documentosImpresos);
        documentoBean.setImpresos(docImps);
        documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getIdDocumento());
        documentoBean.setFechaCaducidad(new Fecha(bono.getFechaCaducidad()));
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());

        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_BONO);
    }

    //DAINOVY_CA
    public static void crearDocumentoCintaAuditora(XCintaAuditoraTbl cintaAuditora, DocumentosImpresosBean documentosImpresos, String tipo) throws DocumentoException {

        DocumentosBean documentoBean = new DocumentosBean();
        documentoBean.setUidDocumento(UUID.randomUUID().toString());
        documentoBean.setTipo(tipo);
        documentoBean.setCodAlmacen(cintaAuditora.getXCintaAuditoraTblPK().getCodalm());
        documentoBean.setCodCaja(cintaAuditora.getXCintaAuditoraTblPK().getCodcaja());
        documentoBean.setIdDocumento(String.valueOf(cintaAuditora.getXCintaAuditoraTblPK().getIdCintaAuditora()));

        /*
        no applican
        documentoBean.setCodCliente(bono.getCodCliente());
        documentoBean.setMonto(bono.getImporte());
        documentoBean.setFechaCaducidad(new Fecha(bono.getFechaCaducidad()));
         */
        documentoBean.setEstado("V");
        documentoBean.setObservaciones("");
        documentoBean.setFecha(new Fecha(cintaAuditora.getFecha()));

        //revisar
        documentoBean.setUsuario(Sesion.getUsuario().getUsuario());
        List<DocumentosImpresosBean> docImps = new ArrayList<DocumentosImpresosBean>();
        docImps.add(documentosImpresos);
        documentoBean.setImpresos(docImps);

        documentoBean.setNumTransaccion(cintaAuditora.getXCintaAuditoraTblPK().getCodalm() + "-" + cintaAuditora.getXCintaAuditoraTblPK().getCodcaja() + "-" + String.format("%09d", cintaAuditora.getXCintaAuditoraTblPK().getIdCintaAuditora()));
        documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());
        crearDocumento(documentoBean, DocumentosImpresosBean.TIPO_CINTA_AUDITORA);
    }
    //DAINOVY_CA

    public static void crearDocumentoTest(DocumentosBean documentosBean, String tipoImpreso) throws DocumentoException {
        crearDocumento(documentosBean, tipoImpreso);
    }

    private static void crearDocumento(DocumentosBean documentosBean, String tipoImpreso) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            session.openSession(SessionFactory.openSession());
            crearDocumento(documentosBean, tipoImpreso, session);
            session.commit();
        } catch (DocumentoException e) {
            String msg = "Error registrando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static void crearDocumento(DocumentosBean documentosBean, String tipoImpreso, SqlSession session) throws DocumentoException {
        try {
            log.debug("crearDocumento() - creando documento con uid: " + documentosBean.getUidDocumento());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);
            //Por si hay documentos repetidos porque se ha anulado previamente (por ejemplo al liquidar un plan novio por segunda vez)
            try {
                if (tipoImpreso != null) {
                    DocumentosBean repe = consultarDocByUniqueKey(tipoImpreso, documentosBean.getCodAlmacen(), documentosBean.getCodCaja(), documentosBean.getIdDocumento());
                    DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
                    exampleImpresos.or().andUidDocumentoEqualTo(repe.getUidDocumento());
                    mapperImpresos.deleteByExample(exampleImpresos);
                    mapper.deleteByPrimaryKey(repe.getUidDocumento());
                }
            } catch (Exception e) {
                // No hay documentos introducidos previamente, no hacemos nada
            }
            mapper.insert(documentosBean);

            Short i = 0;
            for (DocumentosImpresosBean impreso : documentosBean.getImpresos()) {
                if (tipoImpreso == null || tipoImpreso.equals(impreso.getTipoImpreso())) {
                    impreso.setUidDocumento(documentosBean.getUidDocumento());
                    impreso.setIdImpreso(i);
                    mapperImpresos.insert(impreso);
                    i++;
                }
            }
        } catch (PersistenceException e) {
            String msg = "Error registrando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            throw new DocumentoException(msg, e);
        }
    }

    public static void crearDocumentoImpreso(DocumentosBean documentosBean, String tipoImpreso) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {

            session.openSession(SessionFactory.openSession());
            //log.debug("crearDocumento() - creando documento con uid: " + documentosBean.getUidDocumento());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);
            DocumentosImpresosExample example = new DocumentosImpresosExample();
            example.or().andUidDocumentoEqualTo(documentosBean.getUidDocumento());

            List<DocumentosImpresosBean> documentos = mapperImpresos.selectByExample(example);
            Short numeroDocumentos = 0;
            for (DocumentosImpresosBean doc : documentos) {
                if (doc.getIdImpreso() > numeroDocumentos) {
                    numeroDocumentos = doc.getIdImpreso();
                }
            }
            Short i = (short) (numeroDocumentos + 1);
            for (DocumentosImpresosBean impreso : documentosBean.getImpresos()) {
                if (tipoImpreso == null || tipoImpreso.equals(impreso.getTipoImpreso())) {
                    impreso.setUidDocumento(documentosBean.getUidDocumento());
                    impreso.setIdImpreso(i);
                    impreso.setTipoImpreso(tipoImpreso);
                    mapperImpresos.insert(impreso);
                    i++;
                }
            }
            session.commit();
        } catch (PersistenceException e) {
            String msg = "Error registrando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        }
    }

    public static DocumentosBean consultarDocumento(String uidDocumento) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.error("consultarDocumento() - consultando documento con uid: " + uidDocumento);

            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            // consultamos documento
            DocumentosBean documentosBean = mapper.selectByPrimaryKey(uidDocumento);

            // consultamos detalles
            DocumentosImpresosExample example = new DocumentosImpresosExample();
            example.or().andUidDocumentoEqualTo(uidDocumento);
            example.setOrderByClause("ID_IMPRESO");
            documentosBean.setImpresos(mapperImpresos.selectByExampleWithBLOBs(example));

            return documentosBean;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en el sistema. ";
            log.error("consultarDocumento() - " + msg + e.getMessage(), e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static DocumentosBean consultarDocByUniqueKey(String tipo, String codAlm, String codCaja, String idDoc) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarDocByUniqueKey() - consultando documento con (tipo,codAlm,codCaja,idDoc): (" + tipo + "," + codAlm + "," + codCaja + "," + idDoc + ")");
            //Por las anulaciones, para buscar un documento de tipo bono tenemos que hacer esto
            if (tipo.equals(DocumentosBean.BONO_RESERVA)) {
                try {
                    Bono bono = BonosDao.consultarBonoReservacion(codAlm, idDoc);
                    idDoc = String.valueOf(bono.getBonoPK().getIdBono());
                } catch (NoResultException e) {
                    String msg = "Error consultando el documento, no se ha encontrado documento con (tipo,codAlm,codCaja,idDoc): (" + tipo + "," + codAlm + "," + codCaja + "," + idDoc + ")";
                    log.error("consultarDocByUniqueKey() - " + msg);
                    throw new DocumentoException("No se ha encontrado el documento.");
                }
            }
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            // consultamos documento
            DocumentosExample example = new DocumentosExample();
            example.or().andTipoEqualTo(tipo).
                    andCodAlmacenEqualTo(codAlm).
                    andCodCajaEqualTo(codCaja).
                    andIdDocumentoEqualTo(idDoc);
            List<DocumentosBean> documentosBean = mapper.selectByExample(example);
            if (documentosBean.isEmpty()) {
                return null;
            }
            if (documentosBean.size() > 1) {
                String msg = "Error consultando el documento, más de un documento con (tipo,codAlm,codCaja,idDoc): (" + tipo + "," + codAlm + "," + codCaja + "," + idDoc + ")";
                log.error("consultarDocByUniqueKey() - " + msg);
                throw new DocumentoException(msg);
            }
            DocumentosBean docu = documentosBean.get(0);
            // consultamos detalles
            DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
            exampleImpresos.or().andUidDocumentoEqualTo(docu.getUidDocumento());
            exampleImpresos.setOrderByClause("ID_IMPRESO");
            docu.setImpresos(mapperImpresos.selectByExampleWithBLOBs(exampleImpresos));

            return docu;
        } catch (DocumentoException e) {
            throw new DocumentoException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error inesperado consultando documento en el sistema. ";
            log.error("consultarDocByUniqueKey() - " + msg + e.getMessage(), e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    /*
    public static DocumentosImpresosBean consultarTicket(String codAlmacen,String codCaja, String idDocumento, String tipoDocumento ) throws NoResultException, DocumentoException {
        try {
            return TicketsDao.consultarTicket(idTicket, codCaja, codAlmacen);
        } 
        catch (NoResultException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DocumentoException("No fue posible obtener el documento impreso.", e);
        }
    }
     */
    public static DocumentosBean consultarDoc(String tipo, String codAlm, String codCaja, String idDoc) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarDoc() - consultando documento con (tipo,codAlm,codCaja,idDoc): (" + tipo + "," + codAlm + "," + codCaja + "," + idDoc + ")");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            // consultamos documento
            DocumentosExample example = new DocumentosExample();
            example.or().andTipoEqualTo(tipo).
                    andCodAlmacenEqualTo(codAlm).
                    andCodCajaEqualTo(codCaja).
                    andIdDocumentoEqualTo(idDoc);
            List<DocumentosBean> documentosBean = mapper.selectByExample(example);
            if (documentosBean.isEmpty()) {
                return null;
            }
            if (documentosBean.size() > 1) {
                String msg = "Error consultando el documento, más de un documento con (tipo,codAlm,codCaja,idDoc): (" + tipo + "," + codAlm + "," + codCaja + "," + idDoc + ")";
                log.error("consultarDoc() - " + msg);
                throw new DocumentoException(msg);
            }
            DocumentosBean docu = documentosBean.get(0);
            // consultamos detalles
            DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
            exampleImpresos.or().andUidDocumentoEqualTo(docu.getUidDocumento());
            exampleImpresos.setOrderByClause("ID_IMPRESO");
            docu.setImpresos(mapperImpresos.selectByExampleWithBLOBs(exampleImpresos));

            return docu;
        } catch (DocumentoException e) {
            throw new DocumentoException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error inesperado consultando documento en el sistema. ";
            log.error("consultarDoc() - " + msg + e.getMessage(), e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static DocumentosBean consultarBonoByUniqueKey(String codAlm, String codCaja, String idDoc) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarBonoByUniqueKey() - consultando bono con (codAlm,codCaja,idDoc): (" + "," + codAlm + "," + codCaja + "," + idDoc + ")");
            //Por las anulaciones, para buscar un documento de tipo bono tenemos que hacer esto
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            // consultamos documento
            DocumentosExample example = new DocumentosExample();
            example.or().andCodAlmacenEqualTo(codAlm).
                    andCodCajaEqualTo(codCaja).
                    andIdDocumentoEqualTo(idDoc);
            List<DocumentosBean> documentosBean = mapper.selectByExample(example);
            Iterator it = documentosBean.listIterator();
            while (it.hasNext()) {
                DocumentosBean d = (DocumentosBean) it.next();
                if (!d.getTipo().equals(DocumentosBean.BONO) && !d.getTipo().equals(DocumentosBean.BONO_RESERVA)) {
                    it.remove();
                }
            }
            if (documentosBean.isEmpty()) {
                return null;
            }
            if (documentosBean.size() > 1) {
                String msg = "Error consultando el bono, más de un documento con (codAlm,codCaja,idDoc): (" + "," + codAlm + "," + codCaja + "," + idDoc + ")";
                log.error("consultarBonoByUniqueKey() - " + msg);
                throw new DocumentoException(msg);
            }
            DocumentosBean docu = documentosBean.get(0);
            // consultamos detalles
            DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
            exampleImpresos.or().andUidDocumentoEqualTo(docu.getUidDocumento());
            exampleImpresos.setOrderByClause("ID_IMPRESO");
            docu.setImpresos(mapperImpresos.selectByExampleWithBLOBs(exampleImpresos));

            return docu;
        } catch (DocumentoException e) {
            throw new DocumentoException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error inesperado consultando documento en el sistema. ";
            log.error("consultarBonoByUniqueKey() - " + msg + e.getMessage(), e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static void updateDocumentos(DocumentosBean documentosBean) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("updateDocumentos() - actualizando documento con uid: " + documentosBean.getUidDocumento());
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);

            mapper.updateByEstado(documentosBean);

            session.commit();
        } catch (PersistenceException e) {
            String msg = "Error actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentos() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentos() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static void updateDocumentosImpresos(DocumentosImpresosBean documentosImpresosBean) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("updateDocumentos() - actualizando documento impreso con uid: " + documentosImpresosBean.getUidDocumento());
            session.openSession(SessionFactory.openSession());
            DocumentosImpresosMapper mapper = session.getMapper(DocumentosImpresosMapper.class);

            mapper.updateByPrimaryKeyWithBLOBs(documentosImpresosBean);

            session.commit();
        } catch (PersistenceException e) {
            String msg = "Error actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentos() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentos() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static void updateDocumentosWithSql(DocumentosBean documentosBean, SqlSession session) throws DocumentoException {
        try {
            log.debug("updateDocumentos() - actualizando documento con uid: " + documentosBean.getUidDocumento());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);

            mapper.updateByEstado(documentosBean);
        } catch (PersistenceException e) {
            String msg = "Error actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentosWithSql() - " + msg, e);
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado actualizando documento en base de datos: " + e.getMessage();
            log.error("updateDocumentosWithSql() - " + msg, e);
            throw new DocumentoException(msg, e);
        }
    }

    public static List<DocumentosBean> consultarDocumentos(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        if (DocumentosBean.BONO.equals(paramDocumentos.getTipo()) || DocumentosBean.BONO_RESERVA.equals(paramDocumentos.getTipo())) {
            return consultarBonos(paramDocumentos);
        }
        if (DocumentosBean.GIFTCARD.equals(paramDocumentos.getTipo())) {
            return consultaGiftCards(paramDocumentos);
        }
        if (DocumentosBean.NOTA_CREDITO.equals(paramDocumentos.getTipo())) {
            return consultaNotaCredito(paramDocumentos);
        }
        if (DocumentosBean.RESERVACION.equals(paramDocumentos.getTipo())) {
            return consultaReservacion(paramDocumentos);
        }
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarDocumentos() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andFechaGreaterThanOrEqualTo(paramDocumentos.getFechaIni())
                    .andFechaLessThanOrEqualTo(paramDocumentos.getFechaFin())
                    .andTipoEqualTo(paramDocumentos.getTipo())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andOperationsWithMonto(paramDocumentos.getOperacion(), paramDocumentos.getMonto())
                    .andEstadoIn(paramDocumentos.getEstados())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitiveWithoutNull(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion())
                    .andFechaCaducidadLessThanOrEqualTo(paramDocumentos.getFechaCaducidad());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = documentos = mapper.selectByExample(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultarDocumentos() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosBean> consultarBonos(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarBonos() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andFechaGreaterThanOrEqualTo(paramDocumentos.getFechaIni())
                    .andFechaLessThanOrEqualTo(paramDocumentos.getFechaFin())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andOperationsWithMonto(paramDocumentos.getOperacion(), paramDocumentos.getMonto())
                    .andEstadoIn(paramDocumentos.getEstados())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitiveWithoutNull(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion())
                    .andFechaCaducidadLessThanOrEqualTo(paramDocumentos.getFechaCaducidad());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExampleWithBono(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultarBonos() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosBean> consultaGiftCards(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarGiftCards() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andFechaGreaterThanOrEqualTo(paramDocumentos.getFechaIni())
                    .andTipoEqualTo(paramDocumentos.getTipo())
                    .andFechaLessThanOrEqualTo(paramDocumentos.getFechaFin())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andOperationsWithMonto(paramDocumentos.getOperacion(), paramDocumentos.getMonto())
                    .andEstadoIn(paramDocumentos.getEstados())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitiveWithoutNull(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion())
                    .andFechaCaducidadLessThanOrEqualTo(paramDocumentos.getFechaCaducidad());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExampleWithGiftCard(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultarGiftCards() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosBean> consultaNotaCredito(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultaNotaCredito() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andFechaGreaterThanOrEqualTo(paramDocumentos.getFechaIni())
                    .andTipoEqualTo(paramDocumentos.getTipo())
                    .andFechaLessThanOrEqualTo(paramDocumentos.getFechaFin())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andOperationsWithMonto(paramDocumentos.getOperacion(), paramDocumentos.getMonto())
                    .andEstadoIn(paramDocumentos.getEstados())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitiveWithoutNull(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion())
                    .andFechaCaducidadLessThanOrEqualTo(paramDocumentos.getFechaCaducidad());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExampleWithNotaCredito(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultaNotaCredito() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosBean> consultaReservacion(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultaReservacion() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andFechaGreaterThanOrEqualTo(paramDocumentos.getFechaIni())
                    .andTipoEqualTo(paramDocumentos.getTipo())
                    .andFechaLessThanOrEqualTo(paramDocumentos.getFechaFin())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andOperationsWithMonto(paramDocumentos.getOperacion(), paramDocumentos.getMonto())
                    .andEstadoIn(paramDocumentos.getEstados())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitiveWithoutNull(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion())
                    .andFechaCaducidadLessThanOrEqualTo(paramDocumentos.getFechaCaducidad());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExampleWithReserva(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultaReservacion() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosImpresosBean> consultarDocumentosImpresos(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarDocumentosImpresos() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();
            example.or().andFechaBetween(paramDocumentos.getFechaIni(), paramDocumentos.getFechaFin())
                    .andTipoEqualTo(paramDocumentos.getTipo())
                    .andCodClienteEqualTo(paramDocumentos.getCodCli())
                    .andMontoLike(paramDocumentos.getMonto())
                    .andEstadoEqualTo(paramDocumentos.getEstado())
                    .andUsuarioEqualTo(paramDocumentos.getUsuario())
                    .andObservacionesLikeInsensitive(paramDocumentos.getObservaciones())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExample(example);
            List<DocumentosImpresosBean> documentosImpresos = new ArrayList<DocumentosImpresosBean>();
            //Consultamos el detalle de cada Documento
            for (DocumentosBean doc : documentos) {
                DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
                exampleImpresos.or().andUidDocumentoEqualTo(doc.getUidDocumento());
                exampleImpresos.setOrderByClause("ID_IMPRESO");
                for (DocumentosImpresosBean docImp : mapperImpresos.selectByExampleWithBLOBs(exampleImpresos)) {
                    documentosImpresos.add(docImp);
                }
            }

            return documentosImpresos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultarDocumentosImpresos() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static int consultarNumFacturas(String codCli, String tipo) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.trace("consultarNumFacturas() - consultando número de facturas");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();

            example.or().andCodClienteEqualTo(codCli)
                    .andTipoEqualTo(tipo);

            List<DocumentosBean> documentos = mapper.selectByExample(example);

            //No consultamos el detalle porque este método sólo lo usamos para consultar la cabecera
            //de los documentos, si queremos ver el documento impreso en sí, consultamos el documentoimpresobean
            return documentos.size();
        } catch (Exception e) {
            String msg = "Error inesperado consultando número de facturas en base de datos: " + e.getMessage();
            log.error("consultarNumFacturas() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<DocumentosImpresosBean> consultarDocumentosImpresosByNumero(ParametrosDocumentos paramDocumentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarDocumentosImpresos() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapper = session.getMapper(DocumentosMapper.class);
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            DocumentosExample example = new DocumentosExample();
            example.or().andTipoEqualTo(paramDocumentos.getTipo())
                    .andNumTransaccionEqualTo(paramDocumentos.getNumTransaccion());

            example.setOrderByClause("FECHA DESC");

            List<DocumentosBean> documentos = mapper.selectByExample(example);
            List<DocumentosImpresosBean> documentosImpresos = new ArrayList<DocumentosImpresosBean>();
            //Consultamos el detalle de cada Documento
            for (DocumentosBean doc : documentos) {
                DocumentosImpresosExample exampleImpresos = new DocumentosImpresosExample();
                exampleImpresos.or().andUidDocumentoEqualTo(doc.getUidDocumento());
                exampleImpresos.setOrderByClause("ID_IMPRESO");
                for (DocumentosImpresosBean docImp : mapperImpresos.selectByExampleWithBLOBs(exampleImpresos)) {
                    documentosImpresos.add(docImp);
                }
            }

            return documentosImpresos;
        } catch (Exception e) {
            String msg = "Error inesperado consultando documentos en base de datos: " + e.getMessage();
            log.error("consultarDocumentosImpresos() - " + msg, e);
            throw new DocumentoException(msg, e);
        } finally {
            session.close();
        }
    }

    public static void crearDocumentoImpresoOtroLocal(List<DocumentosImpresosBean> documentosImpresos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            session.openSession(SessionFactory.openSession());
            DocumentosImpresosMapper mapperImpresos = session.getMapper(DocumentosImpresosMapper.class);

            for (DocumentosImpresosBean impreso : documentosImpresos) {
                mapperImpresos.insert(impreso);
            }
            session.commit();
        } catch (PersistenceException e) {
            String msg = "Error registrando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        }
    }

    public static void crearDocumentosOtroLocal(DocumentosBean documentos) throws DocumentoException {
        SqlSession session = new SqlSession();
        try {
            session.openSession(SessionFactory.openSession());
            DocumentosMapper mapperDocumentos = session.getMapper(DocumentosMapper.class);

            mapperDocumentos.insert(documentos);
            session.commit();
        } catch (PersistenceException e) {
            String msg = "Error registrando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nuevo documento en base de datos: " + e.getMessage();
            log.error("crearDocumento() - " + msg, e);
            session.rollback();
            throw new DocumentoException(msg, e);
        }
    }

    /**
     * @author Gabriel Simbania
     * @description Metodo test para generar los cupones
     *
     * @param codCliente
     * @param fechaFactura
     * @param nombreCupon
     * @param direccionCupon
     * @param fechaFacturaEnCupon
     * @param codigoAlmacen
     * @param fechaValidez
     * @param uidTicket
     * @param numeroFactura
     * @param tipoReferenciaOrigen
     * @param uidDocumentoImpreso
     * @param tipoPromocionBean
     * @param tipoDocumentoImpreso
     * @param idPromocion
     * @param numeroCupones
     * @throws Exception
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static void reconstruirCupones(String codCliente,
            String fechaFactura,
            String nombreCupon,
            String direccionCupon,
            String fechaFacturaEnCupon,
            String codigoAlmacen,
            String fechaValidez,
            String uidTicket,
            String numeroFactura,
            String tipoReferenciaOrigen,
            String uidDocumentoImpreso,
            Long tipoPromocionBean,
            String tipoDocumentoImpreso,
            Long idPromocion,
            int numeroCupones) throws Exception {
        if (false) {

            log.debug("Inicia cupones");
            /*Variables*/

            try {
                List<Cupon> cuponesEmitidos = new ArrayList<>();
                PromocionBean promocionBean = new PromocionBean();
                promocionBean.setIdPromocion(idPromocion);
                promocionBean.setTipoPromocion(new TipoPromocionBean(tipoPromocionBean, ""));
                PromocionTipoCuponSorteoSukasa promocion = new PromocionTipoCuponSorteoSukasa(promocionBean);
                List<DocumentosImpresosBean> impresos = new ArrayList<>();
                for (int i = 1; i <= numeroCupones; i++) {
                    Cupon cupon = new Cupon();
                    cupon.setCodAlmacen(codigoAlmacen);
                    cupon.setFechaExpedicion(new Fecha(fechaFactura, "dd-MMM-yyyy HH:mm"));
                    cupon.setPromocion(promocion);
                    cupon.setReferenciaOrigen(uidTicket);
                    cupon.setTipoReferenciaOrigen(tipoReferenciaOrigen);
                    cupon.setCodCliente(codCliente);
                    cupon.setFechaValidez(new Fecha(fechaValidez, "dd-MMM-yyyy HH:mm"));
                    cuponesEmitidos.add(cupon);

                }

                ServicioCupones.obtenerIdCupones(cuponesEmitidos);
                for (Cupon cupon : cuponesEmitidos) {
                    CodigoBarrasCuponPromo codBarrasCupon = new CodigoBarrasCuponPromo(cupon.getIdCupon());
                    cupon.setCodBarras(codBarrasCupon.getCodigoBarras());

                    DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                    documentoImpreso.setTipoImpreso(tipoDocumentoImpreso);
                    documentoImpreso.setUidDocumento(uidDocumentoImpreso);

                    /**
                     * ********** Verificar el formato para que se guarde en el
                     * blob de los documentos impresos y modificar el texto con
                     * los datos de la factura ***********
                     */
                    String blob = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                            + "<output>\n"
                            + "	<ticket>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">" + cupon.getCodBarras() + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">COMOHOGAR S.A       SUKASA WEB</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">AV. GRAL. ENRIQUEZ VIA A</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">COTOGCHOA/SANGOLQUI</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"11\">    R.U.C: </text>\n"
                            + "			<text align=\"left\" length=\"29\">1790746119001</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">QUITO, " + fechaFacturaEnCupon + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\">PARTICIPA Y GANA POR UN VIAJE DE LUJO PA</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\">A TI Y 3 ACOMPAÑANTES A MARRUECOS</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\"/>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\">Cupón : " + cupon.getCodBarras() + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\">Factura : " + numeroFactura + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\" bold=\"true\">Fecha de la factura: " + fechaFactura + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">  SOCIA: CED: " + codCliente + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">  Nombre: " + nombreCupon + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">  Dirección: " + direccionCupon + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">  Fecha de expedición: " + fechaFactura + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"left\" length=\"40\">  Fecha de caducidad: 06-jul-2023 07:00</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\">PROMOCION VALIDA: DEL 31 DE MARZO AL 2 D</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\"> JULIO DEL 2023\n"
                            + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\">SORTEO: 9 DE JULIO DE 2023\n"
                            + "</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\">APLICAN RESTRICCIONES</text>\n"
                            + "		</line>\n"
                            + "		<line>\n"
                            + "			<text align=\"center\" length=\"40\">.</text>\n"
                            + "		</line>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "		<line/>\n"
                            + "	</ticket>\n"
                            + "</output>";

                    documentoImpreso.setImpreso(blob.getBytes("UTF-8"));

                    impresos.add(documentoImpreso);
                }

                EntityManagerFactory emf = Sesion.getEmf();
                EntityManager em = emf.createEntityManager();

                try {
                    em.getTransaction().begin();

                    Connection conn = Connection.getConnection(em);
                    ServicioCupones.crear(conn, cuponesEmitidos, null);
                    em.getTransaction().commit();

                    DocumentosBean doc = new DocumentosBean();
                    doc.setUidDocumento(uidDocumentoImpreso);

                    doc.setImpresos(impresos);

                    DocumentosService.crearDocumentoImpreso(doc, tipoDocumentoImpreso);
                } catch (Exception e) {
                    log.error("escribirTicket() - Error escribiendo Ticket", e);
                    em.getTransaction().rollback();
                } finally {
                    em.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.debug("Finaliza cupones");
        }

    }

}
