/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.facturacion.prefactura;

import com.comerzzia.jpos.entity.db.CabPrefactura;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.prefactura.ParamBuscarPrefactura;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gabriel Simbania
 */
public class CabPrefacturaDao {

    private static final Logger LOG_POS = Logger.getMLogger(CabPrefacturaDao.class);

    /**
     * @author Gabriel Simbania
     * @param em
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static List<CabPrefactura> consultaCabeceraPrefactura(EntityManager em) throws NoResultException, Exception {
        LOG_POS.info("DAO: Consulta cabecera de la prefactura");

        try {
            Query consulta = em.createQuery("SELECT p FROM CabPrefactura p ");
            consulta.setHint("eclipselink.refresh", "true");

            List<CabPrefactura> list = consulta.getResultList();
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            return list;

        } catch (NoResultException e) {
            LOG_POS.debug("No se encontro las cabeceras de las prefacturas ");
            throw new NoResultException("No se encontro las cabeceras de las prefacturas ");
        }
    }

    /**
     * @author Gabriel Simbania
     * @param paramBuscarPrefactura
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static List<CabPrefactura> consultaCabeceraPrefacturaByCriterios(ParamBuscarPrefactura paramBuscarPrefactura) throws NoResultException, Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultaCabeceraPrefacturaByCriterios(em, paramBuscarPrefactura);
        } finally {
            em.close();
        }

    }

    /**
     * @author Gabriel Simbania
     * @param em
     * @param paramBuscarPrefactura
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static List<CabPrefactura> consultaCabeceraPrefacturaByCriterios(EntityManager em,
            ParamBuscarPrefactura paramBuscarPrefactura) throws NoResultException, Exception {
        LOG_POS.info("DAO: Consulta cabecera de la prefactura");

        try {

            List<CabPrefactura> list = new ArrayList<>();
            if (paramBuscarPrefactura.getEnumEstadoPrefactura() != null || paramBuscarPrefactura.getNumOrden() != null || !StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabCodCli()) || !StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabFecha())) {
                String jpql = "SELECT p FROM CabPrefactura p WHERE 1=1 ";
                
                if (paramBuscarPrefactura.getEnumEstadoPrefactura() != null) {
                    jpql += " AND p.cabEstado =:estado ";
                }

                if (paramBuscarPrefactura.getNumOrden() != null) {
                    jpql += " AND p.cabIdPedido =:cabIdPedido ";
                }

                if (!StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabCodCli())) {
                    jpql += " AND p.cabCodCli  LIKE CONCAT('%',:cabCodCli,'%') ";
                }

                if (!StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabFecha())) {
                    jpql += " AND p.cabFecha <=:cabFecha ";
                }

                Query consulta = em.createQuery(jpql);
                consulta.setHint("eclipselink.refresh", "true");

                if (paramBuscarPrefactura.getEnumEstadoPrefactura() != null) {
                    consulta.setParameter("estado", paramBuscarPrefactura.getEnumEstadoPrefactura());
                }

                if (paramBuscarPrefactura.getNumOrden() != null) {
                    consulta.setParameter("cabIdPedido", paramBuscarPrefactura.getNumOrden());
                }

                if (!StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabCodCli())) {
                    consulta.setParameter("cabCodCli", paramBuscarPrefactura.getCabCodCli());
                }

                if (!StringUtils.EMPTY.equals(paramBuscarPrefactura.getCabFecha())) {
                    Date date = Fechas.stringToDate(paramBuscarPrefactura.getCabFecha(), Fecha.PATRON_FECHA_CORTA);
                    consulta.setParameter("cabFecha", date);
                }

                list = consulta.getResultList();
                if (list == null || list.isEmpty()) {
                    return new ArrayList<>();
                }
            }

            return list;

        } catch (NoResultException e) {
            LOG_POS.debug("No se encontro las cabeceras de las prefacturas ");
            throw new NoResultException("No se encontro las cabeceras de las prefacturas ");
        }
    }

    public static void actualizarCabPrefactura(CabPrefactura cabPrefactura, EntityManager em) {
        em.merge(cabPrefactura);
    }

    /**
     * 
     * @param uidCabId
     * @return
     * @throws NoResultException
     * @throws Exception 
     */
    public static CabPrefactura consultaCabeceraPrefacturaByUid(String uidCabId) throws NoResultException, Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultaCabeceraPrefacturaByUid(em, uidCabId);
        } finally {
            em.close();
        }

    }

    /**
     *
     * @param em
     * @param uidCabId
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    public static CabPrefactura consultaCabeceraPrefacturaByUid(EntityManager em, String uidCabId) throws NoResultException, Exception {
        LOG_POS.info("DAO: Consulta cabecera de la prefactura");

        try {
            Query consulta = em.createQuery("SELECT p FROM CabPrefactura p where p.uidCabId =:uidCabId ");
            consulta.setParameter("uidCabId", uidCabId);
            consulta.setHint("eclipselink.refresh", "true");

            return (CabPrefactura) consulta.getSingleResult();

        } catch (NoResultException e) {
            LOG_POS.debug("No se encontro las cabeceras de las prefacturas ");
            throw new NoResultException("No se encontro las cabeceras de las prefacturas ");
        }
    }

}
