/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.notacredito;

import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoException;
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
public class NotaCreditoDao {
    
    
    private static final Logger log = Logger.getMLogger(NotaCreditoDao.class);
    
    public static void escribirNotaCredito(EntityManager em, NotasCredito nc) throws NotaCreditoException {
        try {
            em.persist(nc);
        }
        catch (Exception ex) {
            log.error("Error al salvar nota de crédito en base de datos: " + ex.getMessage(), ex);
            NotaCreditoException e = new NotaCreditoException();
            throw e;
        }        
    }
    
    public static void actualizarNotaCredito(EntityManager em, NotasCredito nc) throws NotaCreditoException {
        try {
            em.merge(nc);
        }
        catch (Exception ex) {
            log.error("Error al modificar nota de crédito en base de datos: " + ex.getMessage(), ex);
            NotaCreditoException e = new NotaCreditoException();
            throw e;
        }        
    }
    
     public static NotasCredito consultarNotaCredito(String uidNotaCredito) throws NotaCreditoException, NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT n FROM NotasCredito n WHERE n.uidNotaCredito = :uidNotaCredito");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidNotaCredito", uidNotaCredito);
            
            NotasCredito ticket =(NotasCredito) consulta.getSingleResult();
                        
            return ticket;
        }
        catch (NoResultException e) {
            log.debug("No se encontró la Nota de Crédito");
            throw e;
        }
        catch (Exception ex) {
            log.error("Error consultando nota de crédito:"+uidNotaCredito + " ", ex);
            throw new NotaCreditoException();
        }
        finally {
            em.close();
        }
    }  
    
}
