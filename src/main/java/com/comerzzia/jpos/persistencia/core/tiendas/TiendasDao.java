/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.core.tiendas;

import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.entity.db.SriCaja;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.comerzzia.jpos.entity.db.Tienda;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class TiendasDao {


    public static Tienda consultaTienda(EntityManager em, String codAlm) {
        Query consulta = em.createQuery("SELECT t FROM Tienda t LEFT JOIN t.almacen a LEFT JOIN FETCH t.sriTienda   WHERE t.codalm = :codalm  ");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("codalm", codAlm);        
        Tienda tarId = (Tienda) consulta.getSingleResult();        
        return tarId;
    }

    public static List<Almacen> consultaTiendas(EntityManager em) {
        Query consulta = em.createQuery("SELECT c FROM Almacen c LEFT JOIN FETCH c.codemp LEFT JOIN FETCH c.tienda ");
        return consulta.getResultList();
    }

    public static SriCaja consultaCaja(EntityManager em, String caja, String codalmSRI) {
        Query consulta = em.createQuery("SELECT s FROM SriCaja s WHERE s.sriCajaPK.codalmSri = :codalmSri AND s.codcajaSri = :codcaja ");
        consulta.setHint("eclipselink.refresh", "true");  
        consulta.setParameter("codalmSri", codalmSRI);              
        consulta.setParameter("codcaja", caja);              
        SriCaja res = (SriCaja) consulta.getSingleResult();        
        return res;
    }
    
    public static void actualizarVersion(EntityManager em, SriCaja sriCaja) {

        try {
            em.getTransaction().begin();
            em.merge(sriCaja);
            em.getTransaction().commit();
        }
        finally {
           // em.close();
        }
       
    }
}
