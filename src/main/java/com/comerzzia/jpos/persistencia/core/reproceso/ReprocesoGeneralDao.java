/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.core.reproceso;

import com.comerzzia.jpos.entity.db.ReprocesoGeneral;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel Simbania
 */
public class ReprocesoGeneralDao {
    
    public static void insert(EntityManager em, ReprocesoGeneral reprocesoGeneral){
        em.persist(reprocesoGeneral);
    }
    
}
