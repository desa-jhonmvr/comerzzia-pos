/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.promociones.reserva;

import com.comerzzia.jpos.entity.db.ReservaProcesar;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author DESARROLLO
 */
public class ReservaProcesarDao {

    private static final Logger log = Logger.getMLogger(ReservaProcesarDao.class);

    /**
     * @author Gabriel Simbania
     * @return
     * @throws NoResultException
     * @throws Exception 
     */
    public static List<ReservaProcesar> consultaReservaProcesar() throws NoResultException, Exception {
        log.info("DAO: Consulta Reservacion por procesar");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        //   Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT p FROM ReservaProcesar p WHERE p.procesado='N' ");

            List<ReservaProcesar> list = consulta.getResultList();
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            return list;

        } catch (NoResultException e) {
            log.debug("No se encontro Reservaciones por procesar ");
            throw new NoResultException("No se encontro Reservacion por procesar ");
        } finally {
            em.close();
        }
    }
    
}
