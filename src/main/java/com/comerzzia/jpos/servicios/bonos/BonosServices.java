/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bonos;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasBonoEfectivo;
import com.comerzzia.jpos.entity.codigosBarra.NotAValidBarCodeException;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.TipoBono;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoBono;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author MGRI
 */
public class BonosServices {

    public static String BONOEFECTIVO = "BONOEFECTIVO";
    public static String BONOSDESCUENTO = "BONOSDESCUENTO";
    private static TipoBono tipoBonoImporte;
    private static Logger log = Logger.getMLogger(BonosServices.class);
    public static String PROCEDENCIA_RESERVA_LIQUIDACION = "LIQUIDACION RESERVA";
    public static String PROCEDENCIA_RESERVA_CANCELACION = "ANULACIÓN RESERVA";
    public static String PROCEDENCIA_RESERVA_LIQUIDACION_BABYSHOWER = "LIQUIDACION BABYSHOWER";
    public static String PROCEDENCIA_RESERVA_CANCELACION_BABYSHOWER = "CANCELACION BABYSHOWER";
    public static String PROCEDENCIA_NOTA_CREDITO = "NOTA DE CREDITO";
    public static String PROCEDENCIA_BONO_NOTA_CREDITO = "BONO POR NOTA DE CREDITO";
    public static String PROCEDENCIA_BONO_RESERVA_LIQUIDACION = "BONO POR LIQUIDACION RESERVA";
    public static String PROCEDENCIA_BONO_RESERVA_CANCELACION = "BONO POR ANULACIÓN RESERVA";
    public static String PROCEDENCIA_BONO_RESERVA_LIQUIDACION_BABYSHOWER = "BONO POR LIQUIDACION BABYSHOWER";
    public static String PROCEDENCIA_BONO_RESERVA_CANCELACION_BABYSHOWER = "BONO POR CANCELACION BABYSHOWER";

    public static String LLAMADO_DESDE_VENTAS = "Venta";
    public static String LLAMADO_DESDE_GIFTCARD = "Giftcard";
    public static String LLAMADO_DESDE_RESERVA_ABONO_INICIAL = "Abono Reserva Inicial";
    public static String LLAMADO_DESDE_RESERVA_ABONO = "Abono Reserva";
    public static String LLAMADO_DESDE_CREDITO_DIRECTO = "Abono Credito Directo";
    public static String LLAMADO_DESDE_LETRA = "Abono Letra";

    public static String LLAMADO_DESDE_RESERVAS_STR = "RESERVAS";
    public static String LLAMADO_DESDE_VENTAS_STR = "VENTAS";

    // Bono de Reservación
    public static Bono crearBonoReserva(BigDecimal importe, String referenciaOrigen, String tipoReferenciaOrigen, Cliente cliente, BigDecimal descuento, String observaciones, String uidReservacion) throws TicketPrinterException, Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Bono b = crearBono(em, importe, referenciaOrigen, tipoReferenciaOrigen, cliente, descuento, "Reservacion", referenciaOrigen, false, null, uidReservacion, observaciones, null);
            DocumentosService.crearDocumentoBonoReserva(b, referenciaOrigen, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.BONO_RESERVA);
            SqlSession sql = new SqlSession();
            Connection conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));
            ReservacionesServicios.actualizarCampoBono(new BigInteger(referenciaOrigen), sql, "S");
            em.getTransaction().commit();
            //G.S. Unicamente cuando se genere un bono por una reserva
            if (b.getUidReservacion() != null) {
                ReservaBean reservaBean = ReservacionesServicios.consultaById(BigInteger.valueOf(Long.parseLong(referenciaOrigen)));
                ReservacionesServicios.generarKardexReserva(reservaBean, EnumTipoDocumento.BONO);
            }

            return b;
        } catch (Exception e) {
            log.error("Error en la creación del bono: " + e.getMessage(), e);
            em.getTransaction().rollback();
            throw new Exception("Error en la creación del bono");
        } finally {
            em.close();
        }
    }

    //Bono de Factura
    public static Bono crearBonoVuelta(Pago pago, String referenciaOrigen, String tipoReferenciaOrigen, Cliente cliente, String idTransaccion, String uidNotaCredito, String uidReservacion, Date fechaCaducidad) throws TicketPrinterException, Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Bono b = crearBono(em, pago.getVueltaAsBD(), referenciaOrigen, tipoReferenciaOrigen, cliente, pago.getDescuento(), pago.getModalidadString(), idTransaccion, true, uidNotaCredito, uidReservacion, null, fechaCaducidad);
            DocumentosService.crearDocumentoBonoPago(b, PrintServices.getInstance().getDocumentosImpresos().get(PrintServices.getInstance().getDocumentosImpresos().size() - 1), DocumentosBean.BONO);
            em.getTransaction().commit();
            return b;
        } catch (Exception e) {
            log.error("Error en la creación del bono: " + e.getMessage(), e);
            em.getTransaction().rollback();
            throw new Exception("Error en la creación del bono");
        } finally {
            em.close();
        }
    }

    private static Bono crearBono(EntityManager em, BigDecimal importe, String referenciaOrigen, String tipoReferenciaOrigen, Cliente cliente, BigDecimal descuento, String tipoTransaccion, String idTransaccion, boolean vieneFactura, String uidNotaCredito, String uidReservacion, String observaciones, Date fechaCaducidad) throws TicketPrinterException, Exception {
        try {
            Bono b = new Bono(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_BONO), VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

            b.setIdTipoBono(tipoBonoImporte.getIdTipoBono());
            if (descuento == null) {
                b.setDescuento(new BigDecimal(tipoBonoImporte.getDescuento()).setScale(2, RoundingMode.DOWN));
            } else {
                b.setDescuento(descuento.setScale(2, RoundingMode.DOWN));
            }
            b.setTipoTransaccion(tipoTransaccion);
            b.setTransaccion(idTransaccion);
            b.setSoloSinPromocion(tipoBonoImporte.getSoloSinPromocion());
            b.setUtilizado('N');
            b.setTipoReferenciaOrigen(tipoReferenciaOrigen);
            b.setReferenciaOrigen(referenciaOrigen);
            b.setImporte(importe);

            Fecha ahora = new Fecha();
            if (fechaCaducidad != null) {
                b.setFechaCaducidad(fechaCaducidad);
            } else {
                Fecha fechaCaducidadCasoNulo = new Fecha();
                if (tipoBonoImporte == null || tipoBonoImporte.getPlazo() == null) {
                    LecturaConfiguracion.leerTipoBonoEfectivo();
                }
                fechaCaducidadCasoNulo.sumaDias(tipoBonoImporte.getPlazo().intValue());
                b.setFechaCaducidad(fechaCaducidadCasoNulo.getDate());
            }

            b.setFechaExpedicion(ahora.getDate());
            b.setFechaInicio(ahora.getDate());
            b.setProcesado('N');
            b.setCodCliente(cliente.getCodcli());
            b.setUidNotaCredito(uidNotaCredito);
            b.setUidReservacion(uidReservacion);
            b.setObservaciones(observaciones);

            try {
                em.persist(b);
                String referencia = b.getBonoPK().getCodalm() + b.getBonoPK().getIdBono();

                if (tipoReferenciaOrigen.startsWith(PROCEDENCIA_RESERVA_CANCELACION) || tipoReferenciaOrigen.startsWith(PROCEDENCIA_RESERVA_CANCELACION) || tipoReferenciaOrigen.startsWith(PROCEDENCIA_RESERVA_LIQUIDACION) || tipoReferenciaOrigen.startsWith(PROCEDENCIA_RESERVA_LIQUIDACION)
                        || tipoReferenciaOrigen.endsWith(LLAMADO_DESDE_RESERVAS_STR)) {
                    Sesion.getCajaActual().crearApunteExpedicionBono(importe, referencia, false, em);
                } else {
                    Sesion.getCajaActual().crearApunteExpedicionBono(importe, referencia, true, em);
                }
            } catch (Exception e) {
                log.error("Error en la creación del bono: " + e.getMessage(), e);
                throw new Exception("Error en la creación del bono");
            }

            // Impresión del bono
            CodigoBarrasBonoEfectivo cbbe = new CodigoBarrasBonoEfectivo(b.getBonoPK().getIdBono());
            ComprobanteBono comprobante = new ComprobanteBono(b, cbbe, cliente);

            PrintServices.getInstance().imprimirComprobanteBono(comprobante, vieneFactura);

            return b;
        } catch (TicketPrinterException ex) {
            log.error("Error al crear contador para el bono: " + ex.getMessage(), ex);
            throw ex;
        } catch (ContadorException ex) {
            log.error("Error al crear contador para el bono: " + ex.getMessage(), ex);
            throw new Exception("Error al crear el bono");
        } catch (Exception ex) {
            log.error("Error al crear el bono: " + ex.getMessage(), ex);
            throw new Exception("Error al crear el bono");
        }
    }

    public static TipoBono getTipoBonoImporte() {
        return tipoBonoImporte;
    }

    public static void setTipoBonoImporte(TipoBono aTipoBonoImporte) {
        tipoBonoImporte = aTipoBonoImporte;
    }

    public static void crearBonosPagos(PagosTicket pagos, String idTransaccion, String tipoTransaccion, Cliente cliente) {
        BigDecimal valorADevolver = BigDecimal.ZERO;

        String tipoRefOrigen = "";
        for (Pago p : pagos.getPagos()) {
            if (p.getMedioPagoActivo().isNotaCredito()) {
                try {
                    if (p.getVuelta() != null && p.getVueltaAsBD().compareTo(BigDecimal.ZERO) > 0) {
                        PagoNotaCredito pnc = (PagoNotaCredito) p;
                        // Calculamos la procedencia
                        tipoRefOrigen = BonosServices.PROCEDENCIA_NOTA_CREDITO;
                        if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_VENTAS)) {
                            tipoRefOrigen = tipoRefOrigen + "_VENTAS";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_RESERVA_ABONO) || tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_RESERVA_ABONO_INICIAL)) {
                            tipoRefOrigen = tipoRefOrigen + "_RESERVAS";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_CREDITO_DIRECTO)) {
                            tipoRefOrigen = tipoRefOrigen + "_CREDITODIRECTO";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_LETRA)) {
                            tipoRefOrigen = tipoRefOrigen + "_CREDITOTEMPORAL";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_GIFTCARD)) {
                            tipoRefOrigen = tipoRefOrigen + "_GIFTCARD";
                        }

                        crearBonoVuelta(p, pnc.getIdNotaCreditoCompleto(), tipoRefOrigen, cliente, idTransaccion, pnc.getUidNotaCredito(), null, pnc.getFechaCaducidad());
                    }
                } catch (Exception ex) {
                    log.error("Error al crear un bono " + ex.getMessage());
                    valorADevolver = valorADevolver.add(((PagoNotaCredito) p).getSaldoNotaCredito());
                }
            } else if (p.getMedioPagoActivo().isBonoEfectivo()) {
                try {
                    if (p.getVuelta() != null && p.getVueltaAsBD().compareTo(BigDecimal.ZERO) > 0) {
                        PagoBono pb = (PagoBono) p;

                        if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_NOTA_CREDITO)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_NOTA_CREDITO;
                        } else if (pb.getBono().getTipoReferenciaOrigen().equals(BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION;
                        } else if (pb.getBono().getTipoReferenciaOrigen().equals(BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION_BABYSHOWER)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION_BABYSHOWER;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION_BABYSHOWER)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION_BABYSHOWER;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_RESERVA_CANCELACION)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_RESERVA_CANCELACION_BABYSHOWER)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_CANCELACION_BABYSHOWER;
                        } else if (pb.getBono().getTipoReferenciaOrigen().startsWith(BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION_BABYSHOWER)) {
                            tipoRefOrigen = BonosServices.PROCEDENCIA_BONO_RESERVA_LIQUIDACION_BABYSHOWER;
                        } else {
                            tipoRefOrigen = pb.getBono().getTipoReferenciaOrigen();
                        }
                        if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_VENTAS)) {
                            tipoRefOrigen = tipoRefOrigen + "_VENTAS";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_RESERVA_ABONO) || tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_RESERVA_ABONO_INICIAL)) {
                            tipoRefOrigen = tipoRefOrigen + "_RESERVAS";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_CREDITO_DIRECTO)) {
                            tipoRefOrigen = tipoRefOrigen + "_CREDITODIRECTO";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_LETRA)) {
                            tipoRefOrigen = tipoRefOrigen + "_CREDITOTEMPORAL";
                        } else if (tipoTransaccion.equals(BonosServices.LLAMADO_DESDE_GIFTCARD)) {
                            tipoRefOrigen = tipoRefOrigen + "_GIFTCARD";
                        }

                        crearBonoVuelta(p, pb.getBono().getBonoPK().getCodalm() + "-" + pb.getBono().getBonoPK().getIdBono(), tipoRefOrigen, cliente, idTransaccion, pb.getBono().getUidNotaCredito(), pb.getBono().getUidReservacion(), pb.getBono().getFechaCaducidad());
                    }
                } catch (Exception ex) {
                    log.error("Error al crear un bono " + ex.getMessage());
                    if (p instanceof PagoNotaCredito) {
                        valorADevolver = valorADevolver.add(((PagoNotaCredito) p).getSaldoNotaCredito());
                    }
                }
            }
        }

    }

    public static Number consultaBonoPorId(String codAlm, Long id_bono) {
        Number consulta = null;
        try {

            consulta = BonosDao.consultaBonoIdLike(codAlm, id_bono);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(BonosServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consulta;
    }

    /**
     * Consulta el bono a travez del código de barras y el tipo de bono
     * @param codigoBarras
     * @param tipoBonoALeer
     * @return
     * @throws NoResultException
     * @throws BonoException
     * @throws NotAValidBarCodeException
     * @throws BonoInvalidException 
     */
    public static Bono consultaBono(String codigoBarras, String tipoBonoALeer) throws NoResultException, BonoException, NotAValidBarCodeException, BonoInvalidException {
        Bono consulta = null;

        String codAlm;
        Long id_bono;

        try {
            // Miramos de que tipo de bono se trata:
            if (tipoBonoALeer.equals(BONOEFECTIVO)) {
                // Sacamos el codigo de almacen e identificador que corresponde al codigo de barras
                CodigoBarrasBonoEfectivo codBar = new CodigoBarrasBonoEfectivo(codigoBarras);
                codAlm = codBar.getAlmacen();
                id_bono = codBar.getIdBono();
                consulta = BonosDao.consultaBono(codAlm, id_bono);
            }
            Date fechaActual = DateUtils.truncate(new Date(), Calendar.DATE);
            Date fechaCaducidad = DateUtils.truncate(consulta.getFechaCaducidad(), Calendar.DATE);
            Date fechaInicio = DateUtils.truncate(consulta.getFechaInicio(), Calendar.DATE);
            if (fechaCaducidad.before(fechaActual)) {
                log.debug("El bono ha caducado");
                throw new BonoInvalidException("El bono ha caducado");
            }
            if (fechaInicio.after(fechaActual)) {
                // Error: el bono no es valido aun;
                log.debug("El bono no se encuentra en período de validez");
                throw new BonoInvalidException("El bono no se encuentra en período de validez");
                // Cerrar ventana
                // Mostrar error
            }
            if (consulta.getUtilizado() == 'S') {
                // Error: el bono ha sido utilizado o tiene valor nulo;
                log.debug("El bono ha sido ya utilizado o tiene valor nulo");
                throw new BonoInvalidException("El bono ha sido ya utilizado.");
                // Cerrar ventana
                // Mostrar error
            }

        } catch (BonoException e) {
            throw e;
        } catch (BonoInvalidException e) {
            throw e;
        } catch (NotAValidBarCodeException e) {
            throw e;
        } catch (NoResultException e) {
            throw new BonoException("El bono ha sido ya utilizado.");
        } catch (Exception e) {
            log.error("consultaBono() ", e);
            throw new BonoException("Error leyendo bono.");
        }
        return consulta;
    }
}
