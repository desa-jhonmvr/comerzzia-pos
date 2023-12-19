/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.core.tiendas.SriTiendas;

import com.comerzzia.jpos.entity.db.SriTienda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author SMLM
 */
public class SriTiendasDao {

    public static List<SriTienda> consultaTiendas(EntityManager em) {
        Query consulta = em.createQuery("SELECT s FROM SriTienda s WHERE s.codalminterno is not null ");
        return consulta.getResultList();
    }
    
    /**
     * @author Gabriel Simbania
     * @param em
     * @param codAlm
     * @return 
     */
    public static SriTienda consultaTiendaByCodAlm(EntityManager em, String codAlm) {
        Query consulta = em.createQuery("SELECT s FROM SriTienda s WHERE s.codalmsri =:codalmsri   ");
        consulta.setParameter("codalmsri", codAlm);
        return (SriTienda)consulta.getSingleResult();
    }
        
}
