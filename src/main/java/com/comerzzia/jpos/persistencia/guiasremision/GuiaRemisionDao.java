/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.guiasremision;

import com.comerzzia.jpos.entity.db.GuiaRemision;
import com.comerzzia.jpos.entity.db.GuiaRemisionDetalle;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class GuiaRemisionDao {

    private static final Logger log = Logger.getMLogger(GuiaRemisionDao.class);
    

    public static void guardar(EntityManager em, GuiaRemision gr) {
        gr.setProcesado('N');
        em.persist(gr);
    }

    public static void guardar(EntityManager em, GuiaRemisionDetalle grd) {
        em.persist(grd);
    }

    public static boolean existeGuiaRemision (EntityManager em, String uidTicket) {
        Query consulta = em.createQuery("SELECT g FROM GuiaRemision g WHERE g.uidTicketRef = :uidTicketRef AND g.estado<>'A'");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("uidTicketRef",uidTicket);
        
        return consulta.getResultList().size()>0;
    }
}
