/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.logs.logacceso;

import com.comerzzia.jpos.entity.db.LogConsultaArticulo;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class LogAccesoDao {

    private static Logger log = Logger.getMLogger(LogAccesoDao.class);

    public static void crearAccesoLog(LogOperaciones logOp, EntityManager em) throws LogException {

        try {
//            long idLog = LogAccesoDao.consultarIdLog(logOp.getLogOperacionesPK().getCodAlm() ,logOp.getLogOperacionesPK().getCodCaja(), em);
//            logOp.getLogOperacionesPK().setIdLog(idLog);
            em.persist(logOp);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación", ex);
            throw new LogException();
        } finally {

        }
    }

    public static void crearAccesoLogDet(LogOperacionesDet logOp, EntityManager em) throws LogException {

        try {
//            long idLog = LogAccesoDao.consultarIdLog(logOp.getLogOperacionesPK().getCodAlm() ,logOp.getLogOperacionesPK().getCodCaja(), em);
//            logOp.getLogOperacionesPK().setIdLog(idLog);
            em.persist(logOp);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación", ex);
            throw new LogException();
        } finally {

        }
    }

    public static void crearAccesoLog(LogConsultaArticulo logCA, EntityManager em) throws LogException {

        try {
            em.persist(logCA);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación", ex);
            throw new LogException();
        } finally {

        }

    }

    public static LogOperaciones consultar(String codOperacion, String uidDiarioCaja, EntityManager em) throws LogException {
        LogOperaciones configLog = null;

        try {
            // consultamos  el valor de la tabla registro con codalm y codcaja del pos
            Query consulta = em.createQuery("SELECT c FROM LogOperaciones c WHERE c.referencia = :uid AND c.codOperacion = :cod");
            consulta.setParameter("uid", uidDiarioCaja);
            consulta.setParameter("cod", codOperacion);
            try {
                configLog = (LogOperaciones) consulta.getSingleResult();
            } catch (NoResultException e) {
                log.error("No se encontró el registro de log referente a la apertura de caja actual");
            }

            return configLog;
        } catch (Exception ex) {
            log.error(" el registro de log referente a la apertura de caja actua +" + uidDiarioCaja, ex);
            throw new LogException();
        } finally {

        }
    }

}
