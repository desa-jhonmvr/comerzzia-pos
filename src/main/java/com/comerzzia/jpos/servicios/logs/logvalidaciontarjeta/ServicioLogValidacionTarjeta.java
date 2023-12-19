/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.logs.logvalidaciontarjeta;

import com.comerzzia.jpos.entity.db.LogValidacionTarjeta;
import com.comerzzia.jpos.persistencia.logs.logvalidaciontarjeta.LogValidacionTarjetaDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author MGRI
 */
public class ServicioLogValidacionTarjeta {

    public static Character TIPO_ENVIO = 'E';
    public static Character TIPO_RECEPCION = 'R';
    public static Character TIPO_CANCELACION = 'C';
    private static Logger log = Logger.getMLogger(ServicioLogValidacionTarjeta.class);

    /*public static void crearAccesoLogValidacionTarjeta(AutorizadorTramaPeticion peticion) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            LogValidacionTarjeta logVal = new LogValidacionTarjeta(UUID.randomUUID().toString());

            logVal.setFechaEnvio(new Date());
            logVal.setTarjeta(peticion.getNumeroTarjeta());
            logVal.setCaja(peticion.getNumeroCaja().toString());
            logVal.setTienda(Sesion.getTienda().getCodalm());
            logVal.setTrama(peticion.getLineaFichero());
            if (peticion.isTipoAutorizacion()) {
                logVal.setTipo(TIPO_ENVIO);
            }
            else if (peticion.isTipoCancelacion()) {
                logVal.setTipo(TIPO_CANCELACION);
            }

            LogValidacionTarjetaDao.crearLog(logVal, em);
            em.getTransaction().commit();
        }
        catch (Exception ex) {
            log.error("Error escribiendo log de validacion de trama: Envio > peticion:" + peticion.getLineaFichero(), ex);
        }
        finally {
            em.close();
        }

    }

    public static void crearAccesoLogValidacionTarjeta(AutorizadorTramaRespuesta respuesta) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            LogValidacionTarjeta logVal = new LogValidacionTarjeta(UUID.randomUUID().toString());

            logVal.setFechaEnvio(new Date());
            logVal.setTarjeta(respuesta.getNumeroTarjeta().toString());
            logVal.setCaja(respuesta.getNumeroCaja().toString());
            logVal.setTienda(Sesion.getTienda().getCodalm());
            logVal.setTrama(respuesta.getLineaFichero());
            logVal.setTipo(TIPO_RECEPCION);

            LogValidacionTarjetaDao.crearLog(logVal, em);
            em.getTransaction().commit();
        }
        catch (Exception ex) {
            log.error("Error escribiendo log de validacion de trama: Envio > respuesta:" + respuesta.getLineaFichero());
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }

    }*/
}
