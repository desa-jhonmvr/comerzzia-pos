/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.facturacion.tarjetas;

import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.persistencia.facturacion.tarjetas.FacturacionTarjetasDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SMLM
 */
public class ServicioFacturacionTarjetas {

    protected static Logger log = Logger.getMLogger(ServicioFacturacionTarjetas.class);

    public static void insertarFacturacionTarjeta(FacturacionTarjeta ft) throws FacturacionTarjetaException {

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            log.debug("insertarFacturacionTarjeta() - Insertando la facturación de la tarjeta: " + ft.getId());
            em.getTransaction().begin();
            ft.setProcesado('N');
            FacturacionTarjetasDao.crearFacturacionTarjeta(ft, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.debug("insertarFacturacionTarjeta() - Ha ocurrido un error insertando la facturación de la tarjeta: " + e.getMessage(), e);
            throw new FacturacionTarjetaException("Ha ocurrido un error insertando la facturación de la tarjeta", e);
        } finally {
            em.close();
        }

    }

    public static void insertarFacturacionTarjeta(FacturacionTarjeta ft, EntityManager em) throws FacturacionTarjetaException, Exception {

        log.debug("insertarFacturacionTarjeta() - Insertando la facturación de la tarjeta: " + ft.getId());
        ft.setProcesado('N');
        FacturacionTarjetasDao.crearFacturacionTarjeta(ft, em);

    }

    public static void actualizarFacturacionTarjeta(FacturacionTarjeta ft) throws FacturacionTarjetaException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            log.debug("insertarFacturacionTarjeta() - Insertando la facturación de la tarjeta: " + ft.getId());
            em.getTransaction().begin();
            FacturacionTarjetasDao.actualizarFacturacionTarjeta(ft, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.debug("insertarFacturacionTarjeta() - Ha ocurrido un error insertando la facturación de la tarjeta: " + e.getMessage(), e);
            throw new FacturacionTarjetaException("Ha ocurrido un error insertando la facturación de la tarjeta", e);
        } finally {
            em.close();
        }
    }

    public static FacturacionTarjeta obtenerMaximoSecuencialPinPad(String emisor, String caja) throws FacturacionTarjetaException {
        FacturacionTarjeta facturacion = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            log.debug("obteniendoMaximoValor() - obteniendo maximo valor: ");
            facturacion = FacturacionTarjetasDao.consultarMaximafacturaPinPad(emisor, caja);
        } catch (Exception e) {
            log.debug("insertarFacturacionTarjeta() - Ha ocurrido un error insertando la facturación de la tarjeta: " + e.getMessage(), e);
            throw new FacturacionTarjetaException("Ha ocurrido un error insertando la facturación de la tarjeta", e);
        } finally {
            em.close();
        }
        return facturacion;
    }
}
