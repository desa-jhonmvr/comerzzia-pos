/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cajas;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.CajaCajero;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.persistencia.cajas.CajaException;
import com.comerzzia.jpos.persistencia.cajas.GestionDeCajasDao;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogAccesoDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author amos
 */
public class CajasServices {

    private static Logger log = Logger.getMLogger(CajasServices.class);

    public static void cierreDeCaja(Caja cajaActual) throws Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Comprobamos cuando se cerro la caja y si es necesario crear un log en cierres de caja no realizados
            Fecha fechaApertura = new Fecha(cajaActual.getFechaApertura());
            Fecha fechaHoy = new Fecha();
            Fecha fechaAperturaMasUno = new Fecha(cajaActual.getFechaApertura());
            fechaAperturaMasUno.sumaDias(1);
            GestionDeCajasDao.actualizarCaja(cajaActual, em);

            //Guardamos el log del cierre de caja 
            ServicioLogAcceso.crearAccesoLogCierreCaja(cajaActual, em);
            try {
                if (fechaApertura.getDia() != fechaHoy.getDia() || fechaAperturaMasUno.antes(fechaHoy)) {
                    // Registramos el log de cierre incorrecto de caja
                    LogOperaciones logApertura = LogAccesoDao.consultar(ServicioLogAcceso.LOG_APERTURA_CAJA, cajaActual.getUidDiarioCaja(), em);
                    ServicioLogAcceso.crearAccesoLogCierreCajaDiaDespues(logApertura.getAutorizador(), logApertura.getUsuario(), logApertura.getReferencia(), em);
                }
            }
            catch (Exception e) {
                log.error("cierreDeCaja() - Error registrando logs de cierre de caja: " + e.getMessage(), e);
            }
            em.getTransaction().commit();
        }
        catch (CajaException e){
            throw e;
        }
        catch (Exception e) {
            log.error("cierreDeCaja() - Error realizando cierre de caja: " + e.getMessage(), e);
            throw new CajaException("Error realizando cierre de caja: " + e.getMessage(), e);
        }
        finally {
            em.close();
        }
    }
 
    public static void cierreParcialDeCaja(CajaCajero caja) throws Exception
    {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            caja.setFechaCierre(new Date());
            GestionDeCajasDao.actualizarCajaCajero(caja,em);
            try
            {
                ServicioLogAcceso.crearAccesoLogCierreParcialCaja(caja, em);
            }
            catch(Exception e)
            {
                log.error("cierreParcialDeCaja() - Error registrando el log de cierre parcial de caja(tabla: x_log_operaciones_tbl): " + e.getMessage(), e);
            }
            em.flush();
            em.getTransaction().commit();
        }
        catch (CajaException e){
            log.error("cierreParcialDeCaja() - Error realizando el cierre parcial de caja: " + e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            log.error("cierreParcialDeCaja() - Error realizando el cierre parcial de caja: " + e.getMessage(), e);
            throw new CajaException("Error realizando el cierre parcial de caja: " + e.getMessage(), e);
        }
        finally {
            em.close();
        }
    }

    
    
}
