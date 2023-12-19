/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago;

import com.comerzzia.jpos.persistencia.mediospagos.MediosPagoDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author amos
 */
public class MediosPagoServices {

    protected static Logger log = Logger.getMLogger(MediosPagoServices.class);

    public static boolean consultarEnListaNegra(String tarjeta) throws MedioPagoException {
        log.debug("consultarEnListaNegra() - Consultando si tarjeta está en lista negra: " + tarjeta);
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return MediosPagoDao.consultarEnListaNegra(em, tarjeta);
        }
        catch (Exception e) {
            String msg = "Error en consulta de tarjeta en lista de negra. ";
            log.error("consultarEnListaNegra() -  " + msg + e.getMessage(), e);
            throw new MedioPagoException(msg, e);
        }
        finally {
            em.close();
        }
    }

}
