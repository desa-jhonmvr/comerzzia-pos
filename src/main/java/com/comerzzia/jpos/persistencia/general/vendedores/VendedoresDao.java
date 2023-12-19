/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.general.vendedores;

import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class VendedoresDao {

    private static final Logger log = Logger.getMLogger(VendedoresDao.class);
    public List consultarVendedores(String codAlm) throws Exception {
        
        List<Vendedor> resultado = new LinkedList();
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createQuery("SELECT v FROM Vendedor v WHERE v.codAlm = :codalm");
            consulta.setParameter("codalm", codAlm);            
            resultado=consulta.getResultList();        
        }
        catch (NoResultException e){
            log.debug("DAO: No se encontraron Vendedores");            
        }
        catch (Exception e){
            log.error("DAO: Error al Consultar los Vendedores"); 
            throw e;
        }
        finally{
            em.close();
        }
        
        return resultado;
    }
    
    public Vendedor consultarVendedor(String codVendedor) throws Exception{
        Vendedor resultado = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createQuery("SELECT v FROM Vendedor v WHERE v.codvendedor = :codvendedor");
            consulta.setParameter("codvendedor", codVendedor);            
            resultado=(Vendedor)consulta.getSingleResult();   
            em.refresh(resultado);
        }
        catch (NoResultException e){
            log.debug("DAO: No se encontró el Vendedor");            
        }
        catch (Exception e){
            log.error("DAO: Error al Consultar el Vendedor",e); 
            throw e;
        }
                
        finally{
            em.close();
        }
        
        return resultado;
        
    }
    
}
