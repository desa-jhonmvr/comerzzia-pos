/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito;

import com.comerzzia.jpos.entity.db.AutorizacionConsumoCredito;
import com.comerzzia.jpos.persistencia.credito.autorizacion.AutorizacionConsumoCreditoDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Simbania
 */
public class ServicioAutorizacionConsumoCredito {

    private static final Logger LOG_POS = Logger.getMLogger(ServicioAutorizacionConsumoCredito.class);

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Registra en la base AutorizacionConsumoCredito  </p>
     *
     * @param autorizacionConsumoCredito
     * @throws Exception
     */
    public static void crear(AutorizacionConsumoCredito autorizacionConsumoCredito) throws Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            AutorizacionConsumoCreditoDao.crear(autorizacionConsumoCredito, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG_POS.error("o se pudo registrar la notificaci\u00F3n", e);
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new com.comerzzia.util.base.Exception("No se pudo registrar la notificaci\u00F3n " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Actualiza la AutorizacionConsumoCredito con los parametros
     * uidAutorizacion,numAutorizaci&oacute;n , usuarioAutoriza y
     * estadoAutorizacion
     * </p>
     *
     * @param uidAutorizacion
     * @param numAutorizacion
     * @param usuarioAutoriza
     * @param estadoAutorizacion
     * @throws Exception
     */
    public static void actualizarAutorizacion(String uidAutorizacion, String numAutorizacion, String usuarioAutoriza, String estadoAutorizacion) throws Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            AutorizacionConsumoCredito autorizacionConsumoCredito = AutorizacionConsumoCreditoDao.consultarAutorizacionConsumo(em, uidAutorizacion);
            if (autorizacionConsumoCredito != null) {
                autorizacionConsumoCredito.setFechaAprobacion(new Date());
                autorizacionConsumoCredito.setCodUsuarioAprobacion(usuarioAutoriza);
                autorizacionConsumoCredito.setCodEstadoAprobacion(estadoAutorizacion);
                autorizacionConsumoCredito.setNumAutorizacion(numAutorizacion);

                AutorizacionConsumoCreditoDao.actualizar(autorizacionConsumoCredito, em);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG_POS.error("o se pudo registrar la notificaci\u00F3n", e);
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new com.comerzzia.util.base.Exception("No se pudo registrar la notificaci\u00F3n " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Actualiza las autorizaciones que no han sido procesadas  </p>
     *
     * @param uidTicket
     * @throws Exception
     */
    public static void actualizarAutorizacionSinUso(String uidTicket) throws Exception {
        EntityManager em = null;
        try {

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            AutorizacionConsumoCredito autorizacionConsumoCredito = AutorizacionConsumoCreditoDao.consultarAutorizacionSinProcesar(em, uidTicket);
            if (autorizacionConsumoCredito != null) {
                autorizacionConsumoCredito.setCodEstadoAprobacion("C");
                AutorizacionConsumoCreditoDao.actualizar(autorizacionConsumoCredito, em);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG_POS.error("o se pudo registrar la notificaci\u00F3n", e);
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new com.comerzzia.util.base.Exception("No se pudo registrar la notificaci\u00F3n " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
