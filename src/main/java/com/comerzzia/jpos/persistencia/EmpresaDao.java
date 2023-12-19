/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia;

import com.comerzzia.jpos.entity.db.Empresa;
import com.comerzzia.jpos.servicios.core.empresa.EmpresaException;
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
public class EmpresaDao {
    
    private static final Logger log = Logger.getMLogger(EmpresaDao.class);
    
    public static Empresa consultaEmpresa() throws EmpresaException,NoResultException{
        log.info("DAO: Consulta de empresa");
        Empresa emp = null;
              
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        
        try{
            Query consulta = em.createQuery("SELECT e FROM Empresa e WHERE e.activo ='S'");
            emp=(Empresa) consulta.getSingleResult();
            //em.refresh(cli);
        }
        catch (NoResultException e){
            log.debug("No se encontro los daos de la empresa en la base de datos. Debe figurar con código 0000");
            throw new NoResultException("No se encontro los daos de la empresa en la base de datos. Debe figurar con código 0000");
        }
        catch (Exception e){
            log.error("Error en la consulta: "+e.getMessage(),e);
            throw new EmpresaException("Error en la consulta a empresa"); 
        }
        finally{
            em.close();
        }
        return emp;
    }

}
