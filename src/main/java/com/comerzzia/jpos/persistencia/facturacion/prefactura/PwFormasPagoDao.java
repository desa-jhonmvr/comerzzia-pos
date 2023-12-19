/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.facturacion.prefactura;

import com.comerzzia.jpos.entity.db.PwFormasPago;
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
public class PwFormasPagoDao {
    
    private static final Logger LOG_POS = Logger.getMLogger(PwFormasPagoDao.class);
    
    public static List<PwFormasPago> consultaFormasPagoByUidCabId(String uidCabId) throws NoResultException {
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultaFormasPagoByUidCabId(em, uidCabId);
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
     */
    public static List<PwFormasPago> consultaFormasPagoByUidCabId(EntityManager em, String uidCabId) throws NoResultException {
        LOG_POS.info("DAO: Consulta cabecera de la prefactura");
        
        try {
            
            List<PwFormasPago> list ;
            
            String jpql = "SELECT p FROM PwFormasPago p WHERE p.cabPrefactura.uidCabId=:uidCabId and p.pwpAnulado = 'N' ";
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
