/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.core.tiendas.SriTiendas;


import com.comerzzia.jpos.entity.db.SriTienda;
import com.comerzzia.jpos.persistencia.core.tiendas.SriTiendas.SriTiendasDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SMLM
 */
public class SriTiendasServices {
    
    private static final Logger log = Logger.getMLogger(SriTiendasServices.class);

    public static List<SriTienda> consultarListaTiendas() throws SriTiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            return SriTiendasDao.consultaTiendas(em);
        }
        catch (Exception e){
            String msg = "Error consultando lista de tiendas: " + e.getMessage();
            log.error("consultarListaTiendas()- " + msg, e);
            throw new SriTiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }
    
    /**
     * @author Gabriel Simbania
     * @param codAlm
     * @return
     * @throws SriTiendasException 
     */
     public static SriTienda consultaTiendaByCodAlm(String codAlm) throws SriTiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            return SriTiendasDao.consultaTiendaByCodAlm(em,codAlm );
        }
        catch (Exception e){
            String msg = "Error consultando la tienda: " + e.getMessage();
            log.error("consultaTiendaByCodAlm()- " + msg, e);
            throw new SriTiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }
}
