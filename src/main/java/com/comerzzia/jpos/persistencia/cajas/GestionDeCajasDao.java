/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.cajas;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.CajaCajero;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.RecuentoCajaDet;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class GestionDeCajasDao {
    // logger

    private static Logger log = Logger.getMLogger(GestionDeCajasDao.class);

    /**
     * Consulta, si existe, la caja actual abierta
     * @return
     * @throws Exception 
     */
    public static Caja consultaCajaAbierta(String codAlm, String codCaja) throws Exception {
        log.debug("DAO : Consultando la caja Abierta");
        Caja res = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT c FROM Caja c WHERE c.fechaCierre is NULL AND c.codalm=:codAlm AND c.codcaja=:codCaja");
            consulta.setParameter("codAlm", codAlm);
            consulta.setParameter("codCaja", codCaja);
            res = (Caja) consulta.getSingleResult();
            em.refresh(res);
        }
        catch (NoResultException e) {
            log.debug("No hay caja abierta");
        }
        catch (Exception e) {
            log.error("Apertura de Caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;
    }

    /**
     * Consulta, si existe, la apertura parcial de una caja
     * @return CajaCajero
     * @throws Exception 
     */
    public static CajaCajero consultaCajaParcialAbierta(Caja CajaActual) {
        CajaCajero res = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT c FROM CajaCajero c WHERE c.fechaCierre is NULL AND c.uidDiarioCaja=:UidDiarioCaja");
            consulta.setParameter("UidDiarioCaja", CajaActual);
            res = (CajaCajero) consulta.getSingleResult();
            em.refresh(res);
        }
        catch (NoResultException e) {
            log.debug("No hay caja de cajero abierta");
        }
        catch (Exception e) {
            log.error("Apertura de Caja ", e);
        }
        finally {
            em.close();
        }
        return res;
    }

    /**
     * Crea la Apertura de una nueva caja, la primera apertura parcial y el ingreso inicial
     * @param caja
     * @throws Exception 
     */
    public static void aperturaDeCaja(Caja caja, CajaCajero cajaCajero, CajaDet apunte) throws Exception {

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(caja);
            em.persist(cajaCajero);
            apunte.setCaja(caja);
            apunte.setCajaCajero(cajaCajero);
            em.persist(apunte);
            ServicioLogAcceso.crearAccesoLogAperturaCaja(caja, em);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    /**
     * Crea la Apertura parcial de caja
     * @param caja
     * @throws Exception 
     */
    public static void aperturaParcialDeCaja(CajaCajero caja) throws Exception {

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(caja);
            ServicioLogAcceso.crearAccesoLogAperturaParcialCaja(caja, em);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("Error en la apertura parcial de caja", e);
            throw e;
        }
        finally {
            em.close();
        }
    }

    public static BigDecimal consultaEfectivoEnCaja(String uidDiarioCaja) throws Exception {
        BigDecimal salida = BigDecimal.ZERO;
        Object[] res;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Movimientods de caja");

        try {
            Query consulta = em.createQuery("SELECT SUM(c.cargo), SUM(c.abono) FROM CajaDet c LEFT JOIN  c.codmedpag p WHERE c.caja.uidDiarioCaja = :uidDiarioCaja AND c.codmedpag.codmedpag= :codMedPagEfectivo ");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            consulta.setParameter("codMedPagEfectivo", Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_EFECTIVO));
            Object o = consulta.getSingleResult();
            res = (Object[]) o;
            if (res[0] == null) {
                res[0] = BigDecimal.ZERO;
            }
            if (res[1] == null) {
                res[1] = BigDecimal.ZERO;
            }
            salida = ((BigDecimal) res[0]).add((BigDecimal) res[1]);
        }
        catch (NoResultException e) {
            log.debug("No existen movimientos de caja ");

        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return salida;
    }

    public static Date consultaFechaUltimaAmpliaciónRetiro(String uidDiarioCaja) throws NoResultException, Exception {
        Date res = null; // Si no hay ultima ampliación de retiro
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        log.debug("Consultando La fecha de la ultima ampliación de retiro");

        try {
            Query consulta = em.createQuery("SELECT MAX(c.fecha) FROM CajaDet c LEFT JOIN  c.codmedpag p WHERE c.caja.uidDiarioCaja = :uidDiarioCaja AND c.codmedpag.codmedpag= :codMedPagEfectivo AND c.concepto='A-R'");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            consulta.setParameter("codMedPagEfectivo", Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_EFECTIVO));
            res = (Date) consulta.getSingleResult();
        }
        catch (NoResultException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }

        return res;
    }

    public static BigDecimal consultaLimiteDeRetiro(String uidDiarioCaja, Date fecha) throws Exception {
        BigDecimal salida = BigDecimal.ZERO;
        Object[] res;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando El Limite de retiro actual para una caja");

        try {
            Query consulta = em.createQuery("SELECT SUM(c.cargo), SUM(c.abono) FROM CajaDet c LEFT JOIN  c.codmedpag p WHERE c.caja.uidDiarioCaja = :uidDiarioCaja AND c.codmedpag.codmedpag= :codMedPagEfectivo AND c.fecha < :fecha");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            consulta.setParameter("codMedPagEfectivo", Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_EFECTIVO));
            consulta.setParameter("fecha", fecha);
            Object o = consulta.getSingleResult();
            res = (Object[]) o;
            if (res[0] == null) {
                res[0] = BigDecimal.ZERO;
            }
            if (res[1] == null) {
                res[1] = BigDecimal.ZERO;
            }
            salida = ((BigDecimal) res[0]).add((BigDecimal) res[1]);
        }
        catch (NoResultException e) {
            log.debug("No existen movimientos de caja ");

        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return salida;
    }

    public static void actualizarCaja(Caja caja, EntityManager em) throws CajaException {
        try{
            em.merge(caja);
        }
        catch (Exception e){
            log.error("actualizarCaja() - Error actualizando caja en base de datos: " + e.getMessage(), e);
            throw new CajaException("Error actualizando caja en base de datos: " + e.getMessage(), e);
        }
    }

    public static void actualizarCajaCajero(CajaCajero caja, EntityManager em) throws CajaException {
        try{
            em.merge(caja);
        }
        catch (Exception e){
            log.error("actualizarCajaCajero() - Error actualizando caja cajero en base de datos (tabla D_CAJA_CAJERO_TBL) : " + e.getMessage(), e);
            throw new CajaException("Error actualizando caja cajero en base de datos: " + e.getMessage(), e);
        }
    }

    public static void crearApunte(CajaDet apunte, EntityManager em) throws MovimientoCajaException {
        try{
            em.persist(apunte);
        }
        catch (Exception e){
            log.error("crearApunte() - Error registrando apunte de caja en base de datos: " + e.getMessage(), e);
            throw new MovimientoCajaException("Error creando apunte de caja en base de datos: " + e.getMessage(), e);
        }
    }

    public static void crearApunte(CajaDet apunte) throws MovimientoCajaException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            crearApunte(apunte, em);
            em.getTransaction().commit();
        }
        catch (MovimientoCajaException e){
            throw e;
        }
        catch (Exception e) {
            log.error("crearApunte() - Error registrando apunte de caja en base de datos: " + e.getMessage(), e);
            throw new MovimientoCajaException("Error creando apunte de caja en base de datos: " + e.getMessage(), e);
        }
        finally {
            em.close();
        }
    }
    
    

    //<editor-fold defaultstate="collapsed" desc="MOVIMIENTOS">
    /**
     *  Consulta y devuelve el siguiente numero de línea para un cargo o abono
     * @param uidDiarioCaja
     * @return
     */
    public static int consultaSiguenteNumeroLinea(String uidDiarioCaja) throws CajaException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultaSiguenteNumeroLinea(em, uidDiarioCaja);
        }
        catch (CajaException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("consultaSiguienteNumeroLinea() - Error consultando próximo número de linea de caja: " + e.getMessage(), e);
            throw new CajaException("Error consultando próximo número de linea de caja: " + e.getMessage(), e);
        }
        finally {
            em.close();
        }
    }
    
     public static int consultaSiguenteNumeroLinea(EntityManager em,String uidDiarioCaja) throws CajaException {
        int numerolinea = -1;
        log.debug("consultaSiguenteNumeroLinea() - Consultando siguiente numero de línea...");
        try {
            Query consulta = em.createQuery("SELECT MAX(c.cajaDetPK.linea) FROM CajaDet c WHERE c.caja.uidDiarioCaja = :uidDiarioCaja");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            numerolinea = ((Number) consulta.getSingleResult()).intValue();
            return (numerolinea + 1);
        }
        catch (NoResultException e) {
            log.warn("consultaSiguienteNumeroLinea() - No hay resultados en la consulta.");
            return (0);
        }
        catch (Exception e) {
            log.error("consultaSiguienteNumeroLinea() - Error consultando próximo número de linea de caja: " + e.getMessage(), e);
            throw new CajaException("Error consultando próximo número de linea de caja: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta los movimientos de caja
     * @param uidDiarioCaja uid de la caja para la que consultamos los movimientos
     * @return Lista de movimientos de la caja
     * @throws Exception
     */
    public static List<CajaDet> consultaMovimientos(String uidDiarioCaja) throws Exception {
        List<CajaDet> res = new LinkedList<CajaDet>();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Movimientods de caja");

        try {
            Query consulta = em.createQuery("SELECT c FROM CajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.caja.uidDiarioCaja = :uidDiarioCaja AND c.tipo='M' ORDER BY c.fecha DESC");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.error("No existen movimientos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;

    }

    /**
     * Consulta los movimientos de tipo venta
     * @param uidDiarioCaja
     * @return 
     */
    public static List<CajaDet> consultaVentas(String uidDiarioCaja) throws Exception {
        List<CajaDet> res = new LinkedList<CajaDet>();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Movimientods de caja");

        try {
            Query consulta = em.createQuery("SELECT c FROM CajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.caja.uidDiarioCaja = :uidDiarioCaja AND c.tipo<>'M' ORDER BY c.fecha DESC");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.error("No existen movimientos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;


    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RECUENTOS DE CAJA">
    /**
     *  Consulta y devuelve el siguiente numero de línea para un recuento
     * @param uidDiarioCaja
     * @return
     */
    public static int consultaSiguenteNumeroLineaRecuento(String uidCajeroCaja) {

        int numerolinea = -1;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando siguiente numero de línea");

        try {
            Query consulta = em.createQuery("SELECT MAX(c.recuentoCajaDetPK.linea) FROM RecuentoCajaDet c WHERE c.recuentoCajaDetPK.uidCajeroCaja = :uidCajeroCaja");
            consulta.setParameter("uidCajeroCaja", uidCajeroCaja);
            numerolinea = ((Number) consulta.getSingleResult()).intValue();
        }
        catch (NoResultException e) {
            log.info("consultaSiguienteNumeroLinea: No hay ninguna linea de recuento para la caja");
            return (0);
        }
        catch (Exception e) {
            log.error("consultaSiguienteNumeroLinea ", e);
        }
        finally {
            em.close();
        }

        return (numerolinea + 1);
    }

    /**
     * Consulta los recuentos para la caja actual
     * @param uidDiarioCaja
     * @return
     * @throws Exception
     */
    public static List<RecuentoCajaDet> consultaRecuento(String uidCajeroCaja) throws Exception {
        List<RecuentoCajaDet> res = new LinkedList();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Movimientods de caja");

        try {
            Query consulta = em.createQuery("SELECT c FROM RecuentoCajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.recuentoCajaDetPK.uidCajeroCaja = :uidCajeroCaja");
            consulta.setParameter("uidCajeroCaja", uidCajeroCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            res = new LinkedList();
            log.error("No existen movimientos de caja ");
        }
        catch (Exception e) {
            log.info("No hay ninguna linea de recuento para la caja para el uid caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;

    }

    /** Guarda las lineas de recuento para la el recuento actual
     *
     * @param recuento
     */
    public static void guardarRecuento(List<RecuentoCajaDet> recuento, String uidCajeroCaja) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query consulta = em.createQuery("DELETE FROM RecuentoCajaDet c WHERE c.recuentoCajaDetPK.uidCajeroCaja = :uidCajeroCaja");
            consulta.setParameter("uidCajeroCaja", uidCajeroCaja);
            consulta.executeUpdate();
            for (RecuentoCajaDet linRec : recuento) {
                em.persist(linRec);
            }
            em.flush();
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }
    //</editor-fold>

    public static List consultaTotalesESMovimientos(String uidDiarioCaja) throws Exception {
        List res = new LinkedList();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Movimientods de caja");

        try {
            Query consulta = em.createQuery("SELECT c.codmedpag ,SUM(c.cargo), SUM(c.abono) FROM CajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.caja.uidDiarioCaja = :uidDiarioCaja GROUP BY c.codmedpag");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("No existen movimientos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;
    }

    public static List consultaTotalesESRecuentos(String uidCajeroCaja) throws Exception {
        List res = new LinkedList();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Recuentos de caja");

        try {
            Query consulta = em.createQuery("SELECT c.codmedpag, SUM(c.valor) FROM RecuentoCajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.recuentoCajaDetPK.uidCajeroCaja = :uidCajeroCaja GROUP BY c.codmedpag");
            consulta.setParameter("uidCajeroCaja", uidCajeroCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("No existen recuentos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de recuento de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;
    }

    public static List consultaTotalesRecuento(String uidDiarioCaja) throws Exception {
        List res = new LinkedList();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Recuentos de caja");

        try {
            Query consulta = em.createQuery("SELECT  c.tipo, SUM(c.cargo), SUM(c.abono) FROM CajaDet c WHERE c.caja.uidDiarioCaja = :uidDiarioCaja GROUP BY c.tipo ORDER BY c.tipo ");
            consulta.setParameter("uidDiarioCaja", uidDiarioCaja);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("No existen recuentos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de recuento de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;
    }
    
    public static List<CajaDet> consultaDetalleMovimientos(String idDocumento) throws Exception {
        List<CajaDet> res = new LinkedList<CajaDet>();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando Detalle Movimientos de caja");

        try {
            Query consulta = em.createQuery("SELECT c FROM CajaDet c LEFT JOIN FETCH c.codmedpag WHERE c.idDocumento = :idDocumento ORDER BY c.fecha DESC");
            consulta.setParameter("idDocumento", idDocumento);
            res = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.error("No existen movimientos de caja ");
        }
        catch (Exception e) {
            log.error("Error en la consulta de movimientos de caja ", e);
            throw e;
        }
        finally {
            em.close();
        }
        return res;

    }
}
