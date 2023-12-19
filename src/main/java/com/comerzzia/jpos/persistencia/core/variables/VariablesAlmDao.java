/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.core.variables;

import com.comerzzia.jpos.entity.db.VariableAlm;
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
public class VariablesAlmDao {

    private static final Logger log = Logger.getMLogger(VariablesAlmDao.class);

    public static List<VariableAlm> consultaVariables() throws Exception {

        List<VariableAlm> resultado = new LinkedList<VariableAlm>();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT v FROM VariableAlm v");
            resultado = consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("DAO: No se encontraron variables de almacen");
        }
        catch (Exception e) {
            log.error("DAO: Error al Consultar las variables de almacen", e);
            throw e;
        }
        finally {
            em.close();
        }
        return resultado;
    }
    
    public static VariableAlm consultaVariables(String variableAlm) throws Exception {
        VariableAlm alm = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT v FROM VariableAlm v where v.idVariable = :idVariable");
            consulta.setParameter("idVariable", variableAlm);
            alm = (VariableAlm) consulta.getSingleResult();
            em.refresh(alm);
        }
        catch (NoResultException e) {
            log.debug("DAO: No se encontraron variables de almacen");
        }
        catch (Exception e) {
            log.error("DAO: Error al Consultar las variables de almacen", e);
            throw e;
        }
        finally {
            em.close();
        }
        return alm;
    }
    
    public void modificaVariable(VariableAlm variableAlm) throws Exception {

        log.info("DAO: Modifica Variable");
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.merge(variableAlm);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("Error en la modificacion de la variable: " + e.getMessage(), e);
            throw new Exception("Error en la modificacion de la variable");
        }
        finally {
            em.close();
        }

    }
}
