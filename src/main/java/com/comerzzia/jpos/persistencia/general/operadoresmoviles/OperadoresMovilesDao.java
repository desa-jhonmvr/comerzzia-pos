/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.general.operadoresmoviles;

import java.util.LinkedList;
import java.util.List;
import com.comerzzia.jpos.entity.db.OperadorMovil;
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
public class OperadoresMovilesDao {
    
    private static final Logger log = Logger.getMLogger(OperadoresMovilesDao.class);
    
    public List consultarOperadoresMoviles() throws Exception {
        
        List<OperadorMovil> resultado = new LinkedList();
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createQuery("SELECT c FROM OperadorMovil c ");                       
            resultado=consulta.getResultList();        
        }
        catch (NoResultException e){
            log.debug("DAO: No se encontraron Operadores Móviles");            
        }
        catch (Exception e){
            log.error("DAO: Error al Consultar los Operadores Móviles",e); 
            throw e;
        }
        finally{
            em.close();
        }
        
        return resultado;
    }
}
