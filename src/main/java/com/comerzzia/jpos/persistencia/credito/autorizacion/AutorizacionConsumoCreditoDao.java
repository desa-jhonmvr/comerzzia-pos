/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.autorizacion;

import com.comerzzia.jpos.entity.db.AutorizacionConsumoCredito;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Gabriel Simbania
 */
public class AutorizacionConsumoCreditoDao {

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Registra la informaci&oacute;n de la autorizaci&oacute;n en la base  </p>
     *
     * @param autorizacionConsumoCredito
     * @param em
     * @throws Exception
     */
    public static void crear(AutorizacionConsumoCredito autorizacionConsumoCredito, EntityManager em) throws Exception {
        em.persist(autorizacionConsumoCredito);
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Actualiza la informaci&oacute;n de la autorizaci&oacute;n en la base  </p>
     *
     * @param autorizacionConsumoCredito
     * @param em
     * @throws Exception
     */
    public static void actualizar(AutorizacionConsumoCredito autorizacionConsumoCredito, EntityManager em) throws Exception {
        em.merge(autorizacionConsumoCredito);
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Consular la informaci&oacute;n de la autorizaci&oacute;n por el
     * uidAutorizacion</p>
     *
     * @param em
     * @param uidAutorizacion
     * @return
     * @throws Exception
     */
    public static AutorizacionConsumoCredito consultarAutorizacionConsumo(EntityManager em, String uidAutorizacion) throws Exception {

        AutorizacionConsumoCredito autorizacionConsumoCredito;
        try {

            Query consulta = em.createQuery("SELECT a FROM AutorizacionConsumoCredito a WHERE a.uidAutorizacion =:uidAutorizacion ")
                    .setHint("toplink.refresh", "true");
            consulta.setParameter("uidAutorizacion", uidAutorizacion);

            autorizacionConsumoCredito = (AutorizacionConsumoCredito) consulta.getSingleResult();
        } catch (NoResultException ex) {
            autorizacionConsumoCredito = null;
        }
        return autorizacionConsumoCredito;
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Consular la informaci&oacute;n de la autorizaci&oacute;n por el uidTicket
     * que no hayan sido procesados</p>
     *
     * @param em
     * @param uidTicket
     * @return
     * @throws Exception
     */
    public static AutorizacionConsumoCredito consultarAutorizacionSinProcesar(EntityManager em, String uidTicket) throws Exception {

        AutorizacionConsumoCredito autorizacionConsumoCredito;
        try {

            Query consulta = em.createQuery("SELECT a FROM AutorizacionConsumoCredito a WHERE a.uidTicket =:uidTicket and a.codEstadoAprobacion is null ")
                    .setHint("toplink.refresh", "true");
            consulta.setParameter("uidTicket", uidTicket);

            autorizacionConsumoCredito = (AutorizacionConsumoCredito) consulta.getSingleResult();
        } catch (NoResultException ex) {
            autorizacionConsumoCredito = null;
        }
        return autorizacionConsumoCredito;
    }

}
