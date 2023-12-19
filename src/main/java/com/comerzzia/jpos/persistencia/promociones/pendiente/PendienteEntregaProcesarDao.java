/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.promociones.pendiente;

import com.comerzzia.jpos.entity.db.PendienteProcesar;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Gabriel Simbania
 */
public class PendienteEntregaProcesarDao {

    private static final Logger log_pos = Logger.getMLogger(PendienteEntregaProcesarDao.class);

    /**
     * @author Gabriel Simbania
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static List<PendienteProcesar> consultaPendienteEntregaProcesar() throws NoResultException, Exception {
        log_pos.info("DAO: Consulta Pendientes de entrega por procesar");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        //   Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT p FROM PendienteProcesar p WHERE p.procesado='N' ");

            List<PendienteProcesar> list = consulta.getResultList();
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            return list;

        } catch (NoResultException e) {
            log_pos.debug("No se encontro los pendientes de entrega por procesar ");
            throw new NoResultException("No se encontr los pendientes de entrega por procesar ");
        } finally {
            em.close();
        }
    }

    /**
     * @author Gabriel Simbania
     * @param pendiente
     * @throws ReservasException
     */
    public static void modificarPendienteProcesar(PendienteProcesar pendiente) throws ReservasException {
        log_pos.info("DAO(modificarPendienteProcesar): Modificando datos generales de PendienteProcesar");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(pendiente);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log_pos.error("Error modificando PendienteProcesar Base de datos :" + e.getMessage(), e);
            throw new ReservasException("Error modificando PendienteProcesar en Base de datos");
        } finally {
            em.close();
        }
    }

}
