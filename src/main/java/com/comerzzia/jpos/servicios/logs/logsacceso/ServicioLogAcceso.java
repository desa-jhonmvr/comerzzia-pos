/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.logs.logsacceso;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.CajaCajero;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.LogCierreCaja;
import com.comerzzia.jpos.entity.db.LogConsultaArticulo;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogAccesoDao;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogCajasDao;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author MGRI
 */
public class ServicioLogAcceso {

    public static String LOG_BUSQUEDA_ARTICULO = "BUSQUEDA_ARTICULO";
    public static String LOG_CANCELAR_VENTA = "CANCELAR_VENTA";
    public static String LOG_APLAZAR_VENTA = "APLAZAR_VENTA";
    public static String LOG_CANCELAR_VENTA_RESERVA = "CANCELAR_VENTA_RESERVA";
    public static String LOG_CANCELAR_RESERVA = "CANCELAR_RESERVA";
    public static String LOG_APERTURA_CAJA = "APERTURA_CAJA";
    public static String LOG_CIERRE_CAJA = "CIERRE_CAJA";
    public static String LOG_CIERRE_CAJA_DIA_DESPUES = "CIERRE_CAJA_DIA_DESPUES";
    public static String LOG_APERTURA_PARCIAL_CAJA = "APERTURA_PARCIAL_CAJA";
    public static String LOG_CIERRE_PARCIAL_CAJA = "CIERRE_PARCIAL_CAJA";
    public static String LOG_APERTURA_CAJON = "APERTURA_CAJON";
    public static String LOG_LIQUIDACION_RESERVA = "LIQUIDACION_RESERVA";
    public static String LOG_CANCELACION_RESERVA = "CANCELACION_RESERVA";
    public static String LOG_PERMITIR_CLIENTES_LISTA_NEGRA = "PERMITIR_CLIENTES_LISTA_NEGRA";
    public static String LOG_PERMITIR_TARJETA_LISTA_NEGRA = "PERMITIR_TARJETA_LISTA_NEGRA";
    public static String LOG_AUTORIZAR_PAGO = "AUTORIZAR_PAGO";
    public static String LOG_DESBLOQUEAR_POS = "DESBLOQUEAR_POS";
    public static String LOG_ANULACION = "ANULACION";
    public static String LOG_GIFTCARD = "CARGA_GIFTCARD";
    public static String LOG_LIQUIDAR_PLAN_NOVIOS = "LIQUIDAR_PLAN_NOVIOS";
    public static String LOG_ADMITIR_FUERA_STOCK = "ADMITIR_FUERA_STOCK";
    public static String LOG_AUTORIZAR_VENTA_TIPO_CLIENTE = "AUTORIZAR_VENTA_TIPO_CLIENTE";
    public static String LOG_AUTORIZAR_VENTA_CLIENTE_CAJERO = "AUTORIZADOR_VENTA_CLIENTE_CAJERO";
    public static String LOG_CAMBIAR_ESTADO_PINPAD = "AUTORIZADOR_CAMBIO_ESTADO_PINPAD";
    public static String LOG_AUTORIZAR_PEDIDO_FACTURADO = "AUTORIZAR_PEDIDO_FACTURADO";
    public static String MODIFICAR_CELULAR_CLIENTE = "MODIFICAR_CELULAR_CLIENTE";    
    public static String LOG_AUTORIZA_FECHA_DEVOLUCION = "AUTORIZA_FECHA_DEVOLUCION";
    public static String LOG_AUTORIZA_FECHA_CADUCADA = "AUTORIZA_FECHA_CADUCADA";

    private static Logger log = Logger.getMLogger(ServicioLogAcceso.class);

    public static void crearAccesoLogBusquedaArticulo(String codArt) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogConsultaArticulo logOp = new LogConsultaArticulo(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_BUSQUEDA_ARTICULO);
            logOp.setFechaHora(new Date());
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(codArt); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLogConsultaArticulo(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de operaciones para artículo:" + codArt, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogAplazarVenta(String codAdministrador, String idFactura) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_APLAZAR_VENTA);
            logOp.setFechaHora(new Date());
//            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(idFactura); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de cancelación de venta > venta:" + idFactura + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogAdmitirFueraStock(String codAdministrador, String idFacturaYidItem) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_ADMITIR_FUERA_STOCK);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(idFacturaYidItem); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de admisión de artículos fuera de Stock > venta-idArtículo:" + idFacturaYidItem + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogCierreCajaDiaDespues(String codAdministrador, String codUsuario, String uidCaja, EntityManager em) throws LogException {

        LogCierreCaja logOp = new LogCierreCaja(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        logOp.setCodOperacion(LOG_CIERRE_CAJA_DIA_DESPUES);
        logOp.setFechaHora(new Date());
        logOp.setUsuario(codUsuario);
        logOp.setAutorizador(codAdministrador);
        logOp.setReferencia(uidCaja); // Uid caja
        logOp.setProcesado('N');
        crearAccesoLogCierreCajaDiaDespues(logOp, em);
    }

    public static void crearAccesoLogCancelarVenta(String codAdministrador, String idFactura) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_CANCELAR_VENTA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(idFactura); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de cancelación de venta > venta:" + idFactura + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogLiquidarReserva(String codAdministrador, ReservaBean reserva) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_LIQUIDACION_RESERVA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(reserva.getCodReservacion().toString()); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de liquidación de reserva > reserva:" + reserva.getCodReservacion().toString() + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogCancelarReserva(String codAdministrador, ReservaBean reserva) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_CANCELACION_RESERVA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(reserva.getCodReservacion().toString()); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de cancelación de reserva > reserva:" + reserva.getCodReservacion().toString() + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogPermitirClienteListaNegra(String codAdministrador, Cliente cliente) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_PERMITIR_CLIENTES_LISTA_NEGRA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setReferencia(cliente.getCodcli());
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error al crear log para permitir cliente en lista negra > autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogDesbloquearPos(String codAdministrador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_DESBLOQUEAR_POS);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de desbloqueo de pos > autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogCancelarVentaReserva(String codAdministrador, String idFactura, String uidReservacion) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_CANCELAR_VENTA_RESERVA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(uidReservacion); // Uid caja
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de cancelación de venta > venta:" + idFactura + " autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogCancelarReserva(String codAdministrador, String idTipo, String uidReservacion) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_CANCELAR_RESERVA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(uidReservacion); // Uid caja
            logOp.setObservaciones(idTipo);
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de cancelación de reserva > reserva:" + uidReservacion + " autorizador:" + codAdministrador + " tipo:" + idTipo, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogCierreCaja(Caja cajaActual, EntityManager em) throws LogException {
        LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        logOp.setCodOperacion(LOG_CIERRE_CAJA);
        logOp.setFechaHora(cajaActual.getFechaCierre());
        logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
        logOp.setUsuario(Sesion.getUsuario().getUsuario());
        logOp.setReferencia(cajaActual.getUidDiarioCaja()); // Uid caja
        logOp.setProcesado('N');
        crearAccesoLog(logOp, em);
    }

    public static void crearAccesoLogAperturaCaja(Caja cajaActual, EntityManager em) throws LogException {
        LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        logOp.setCodOperacion(LOG_APERTURA_CAJA);
        logOp.setFechaHora(cajaActual.getFechaApertura());
        logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
        logOp.setUsuario(Sesion.getUsuario().getUsuario());
        logOp.setReferencia(cajaActual.getUidDiarioCaja()); // Uid caja
        logOp.setProcesado('N');
        crearAccesoLog(logOp, em);

    }

    public static void crearAccesoLogCierreParcialCaja(CajaCajero cajaCajero, EntityManager em) throws LogException {
        LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        logOp.setCodOperacion(LOG_CIERRE_PARCIAL_CAJA);
        logOp.setFechaHora(cajaCajero.getFechaCierre());
        logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
        logOp.setUsuario(Sesion.getUsuario().getUsuario());
        logOp.setReferencia(cajaCajero.getUidCajeroCaja()); // Uid caja
        logOp.setProcesado('N');
        crearAccesoLog(logOp, em);
    }

    public static void crearAccesoLogAperturaParcialCaja(CajaCajero cajaCajero, EntityManager em) throws LogException {
        LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        logOp.setCodOperacion(LOG_APERTURA_PARCIAL_CAJA);
        logOp.setFechaHora(cajaCajero.getFechaApertura());
        logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
        logOp.setUsuario(Sesion.getUsuario().getUsuario());
        logOp.setReferencia(cajaCajero.getUidCajeroCaja()); // Uid caja
        logOp.setProcesado('N');
        crearAccesoLog(logOp, em);
    }

    public static void crearAccesoLogAperturaCajon() {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_APERTURA_CAJON);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de apertura de cajón > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogAutorizarVentaTipoCliente(String autorizador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        log.debug("crearAccesoLogAutorizarVentaTipoCliente() - Creando log de acceso para tipo cliente");
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_AUTORIZAR_VENTA_TIPO_CLIENTE);
            logOp.setReferencia(Sesion.getTicket().getIdFactura());
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de autorización de venta para tipo cliente > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogAutorizarVentaClienteCajero(String autorizador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        log.debug("crearAccesoLogAutorizarVentaClienteCajero() - Creando log de acceso para tipo cajero");
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_AUTORIZAR_VENTA_CLIENTE_CAJERO);
            logOp.setReferencia(Sesion.getTicket().getIdFactura());
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de autorizador de venta para tipo cliente > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLog(LogOperaciones logOp, EntityManager em) throws LogException {
        try {
            LogAccesoDao.crearAccesoLog(logOp, em);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación " + logOp.getCodOperacion(), ex);
            throw new LogException();
        } finally {
        }
    }

    public static void crearAccesoLogDet(LogOperacionesDet logOp, EntityManager em) throws LogException {
        try {
            LogAccesoDao.crearAccesoLogDet(logOp, em);
        } catch (Exception ex) {
            log.error("Error guardando el Log detalles de operación  ", ex);
            throw new LogException();
        } finally {
        }
    }

    public static void crearAccesoLogConsultaArticulo(LogConsultaArticulo logCA, EntityManager em) throws LogException {
        try {
            LogAccesoDao.crearAccesoLog(logCA, em);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación " + logCA.getCodOperacion(), ex);
            throw new LogException();
        } finally {
        }
    }

    private static void crearAccesoLogCierreCajaDiaDespues(LogCierreCaja logCA, EntityManager em) throws LogException {
        try {
            LogCajasDao.crearAccesoLog(logCA, em);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación " + logCA.getCodOperacion(), ex);
            throw new LogException();
        } finally {
        }
    }

    public static void crearAccesoLogAnulacion() {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_ANULACION);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(Sesion.getUsuAutorizadorGestionCajas());
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de de anulación > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogGiftCard(String autorizador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_GIFTCARD);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de de anulación > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogLiquidarPlan(String autorizador, String codPlanAsString) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_LIQUIDAR_PLAN_NOVIOS);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            logOp.setReferencia(codPlanAsString);
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de de liquidación de plan :" + codPlanAsString + " > usuario:" + Sesion.getUsuario().getUsuario());
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogPermitirTarjetaListaNegra(String autorizador, String numero) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_PERMITIR_TARJETA_LISTA_NEGRA);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setReferencia(numero);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error al crear log para permitir tarjeta en lista negra > autorizador:" + autorizador, ex);
        } finally {
            em.close();
        }
    }

    public static void crearAccesoLogAutorizarPago(String autorizador, String codMedioPago) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_AUTORIZAR_PAGO);
            logOp.setFechaHora(new Date());
//            logOp.setAutorizador(autorizador);
            logOp.setReferencia(codMedioPago);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error al crear log para autorizar pago > autorizador:" + autorizador, ex);
        } finally {
            em.close();
        }
    }
    
    public static void crearAccesoLogEstadoPinpad(String codAdministrador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_CAMBIAR_ESTADO_PINPAD);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(codAdministrador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de desbloqueo de pos > autorizador:" + codAdministrador, ex);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    /**
     * 
     * @param autorizador 
     */
    public static void crearAccesoLogAutorizarPedidoFacturado(String autorizador) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        log.debug("crearAccesoLogAutorizarPedidoFacturado() - Creando log de acceso para crear el pedido facturado");
        try {
            em.getTransaction().begin();
            LogOperaciones logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(LOG_AUTORIZAR_PEDIDO_FACTURADO);
            logOp.setReferencia(Sesion.getTicket().getIdFactura());
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(autorizador);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setProcesado('N');
            crearAccesoLog(logOp, em);
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error escribiendo log de autorización de venta para tipo cliente > usuario:" + Sesion.getUsuario().getUsuario());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
