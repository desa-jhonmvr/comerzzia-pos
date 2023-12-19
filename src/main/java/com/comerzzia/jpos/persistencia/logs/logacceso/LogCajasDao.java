/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.logs.logacceso;

import com.comerzzia.jpos.entity.db.LogCierreCaja;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author MGRI
 */
public class LogCajasDao {
    
       private static Logger log = Logger.getMLogger(LogCajasDao.class);
       
      
       public static void crearAccesoLog(LogCierreCaja logCA, EntityManager em) throws LogException {
                
        try {
            em.persist(logCA);
        }
        catch (Exception ex) {
            log.error("Error guardando el Log de caja",ex);
            throw new LogException();
        }
        finally {
            
        }        
    }       
}
