/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.tiendas;

import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.entity.db.SriCaja;
import com.comerzzia.jpos.entity.db.Tienda;
import com.comerzzia.jpos.persistencia.core.tiendas.TiendasDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class TiendasServices {

    private static final Logger log = Logger.getMLogger(TiendasServices.class);
    
    
    public static Tienda consultaTienda(String codAlm) throws TiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            return TiendasDao.consultaTienda(em, codAlm);
        }
        catch (NoResultException e) {
            log.error("La tienda con la que se encuentra configurada el POS no existe en la bbdd. Código de tienda: " + codAlm);
            throw e;
        }
        catch (Exception e){
            String msg = "Error consultando tienda: " + e.getMessage();
            log.error("consultaTienda()- " + msg, e);
            throw new TiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }    
    
    public static void consultarCajaTiendaASesion(String caja, String codalm) throws TiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            Sesion.getTienda().getSriTienda().setCajaActiva(TiendasDao.consultaCaja(em,caja,codalm));
        
        }
        catch (NoResultException e) {
           log.debug("consultarCajaTienda() No se ha encontrado la caja configurada "+caja);
        }
        catch (Exception e){
            String msg = "Error consultando lista de tiendas: " + e.getMessage();
            log.error("consultarListaTiendas()- " + msg, e);
            throw new TiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }
    
     public static void actualizarVersion(SriCaja sriCaja) throws TiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            TiendasDao.actualizarVersion(em,sriCaja);
        
        }
        catch (Exception e){
            String msg = "Error actualiando la version: " + e.getMessage();
            log.error("actualizarVersion()- " + msg, e);
            throw new TiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }
    public static List<Almacen> consultarListaTiendas() throws TiendasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try{
            return TiendasDao.consultaTiendas(em);
        }
        catch (Exception e){
            String msg = "Error consultando lista de tiendas: " + e.getMessage();
            log.error("consultarListaTiendas()- " + msg, e);
            throw new TiendasException(msg, e);
        }
        finally{
            em.close();
        }
    }
    
    
}
