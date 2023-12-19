/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.articulos.tarifas;

import com.comerzzia.jpos.entity.db.TarifaId;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class TarifasDao {
    
    private static final Logger log = Logger.getMLogger(TarifasDao.class);
    
    public TarifaId getTarifa(String codTar) throws Exception{
        TarifaId tarId = null;
              
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createNamedQuery("TarifaId.findByCodtar");
            consulta.setParameter("codtar", codTar);
            consulta.setHint("eclipselink.refresh", "true");
            tarId=(TarifaId) consulta.getSingleResult();           
        }
        catch (NoResultException e){
            log.debug("DAO: No se encontraron Tarifas");            
        }
        catch (Exception e){
            log.error("DAO: Error al Consultar las Tarifas",e); 
            throw e;
        }
        finally{
            em.close();
        }
        return tarId;
    }
    
    /**
     * Obtiene la tarifa para un determinado artículo
     * @param codTar
     * @param codArt
     * @return 
     */
    public Tarifas getTarifaArticulo(String codTar, String codArt) throws Exception{
        Tarifas tarifa = null;
              
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createNamedQuery("Tarifas.dameTarifa");
            consulta.setParameter("codtar", codTar);
            consulta.setParameter("codart", codArt);
            
            consulta.setHint("eclipselink.refresh", "true");
            tarifa=(Tarifas) consulta.getSingleResult();
            //em.refresh(tarifa);
        }        
        catch (NoResultException e){
            log.debug("DAO: No se encontró la Tarifa");            
        }
        catch (Exception e){
            log.error("DAO: Error al Consultar la Tarifas",e); 
            throw e;
        }
        finally{
            em.close();
        }
        return tarifa;
    }
    
}
