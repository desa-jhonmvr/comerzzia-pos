/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.facturacion.prefactura;

import com.comerzzia.jpos.entity.db.DetPrefactura;
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
 * @author Gabriel Simbania
 */
public class DetPreFacturaDao {

    private static final Logger LOG_POS = Logger.getMLogger(DetPreFacturaDao.class);

    public static List<DetPrefactura> consultaDetalleByUidCabId(String uidCabId) throws NoResultException, Exception {

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultaDetalleByUidCabId(em, uidCabId);
        } finally {
            em.close();
        }

    }

    /**
     * @author Gabriel Simbania
     * @param em
     * @param uidCabId
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static List<DetPrefactura> consultaDetalleByUidCabId(EntityManager em, String uidCabId) throws NoResultException, Exception {
        LOG_POS.info("DAO: Consulta cabecera de la prefactura");

        try {

            List<DetPrefactura> list;

            String jpql = "SELECT p FROM DetPrefactura p LEFT JOIN FETCH p.articulo WHERE p.cabPrefactura.uidCabId=:uidCabId "
                    + " and p.detAnulado = 'N' order by p.detLinea asc";
            Query consulta = em.createQuery(jpql);
            consulta.setHint("eclipselink.refresh", "true");

            consulta.setParameter("uidCabId", uidCabId);

            list = consulta.getResultList();
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            return list;

        } catch (NoResultException e) {
            LOG_POS.debug("No se encontro el detalle de las cabeceras ");
            throw new NoResultException("No se encontro el detalle de las cabeceras ");
        }
    }
}
