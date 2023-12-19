/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.logs.logvalidaciontarjeta;

import com.comerzzia.jpos.entity.db.LogValidacionTarjeta;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author MGRI
 */
public class LogValidacionTarjetaDao {

    private static Logger log = Logger.getMLogger(LogValidacionTarjetaDao.class);

    public static void crearLog(LogValidacionTarjeta logVal, EntityManager em) throws LogException {

        try {
            em.persist(logVal);
        }
        catch (Exception ex) {
            log.error("Error guardando el Log de validación de  tarjeta", ex);
            throw new LogException();
        }
        finally {
        }
    }
}
