/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.general.ciudades;

import java.util.LinkedList;
import java.util.List;
import com.comerzzia.jpos.entity.db.Ciudad;
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
public class CiudadesDao {

    private static final Logger log = Logger.getMLogger(CiudadesDao.class);

    public List consultarCiudades() throws Exception {
        log.info("DAO: Consulta de ciudades");
        List<Ciudad> resultado = new LinkedList();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT c FROM Ciudad c ");
            resultado = consulta.getResultList();
        }
        catch (NoResultException e){
            log.info("DAO: No se encontraron ciudades en la Base de Datos");            
        }
        catch (Exception e){
            log.error("DAO: Error al consultar ciudades en la Base de Datos");
            throw e;
        }
        finally {
            em.close();
        }

        return resultado;
    }
}
