/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.bonos;

import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.TipoBono;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class BonosDao {

    private static Logger log = Logger.getMLogger(BonosDao.class);

    public static void nuevoBono(Bono bonoIn) throws Exception {
        log.info("DAO: Nuevo Bono");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.persist(bonoIn);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("Error en la creación del bono: " + e.getMessage(), e);
            throw new Exception("Error en la creación del bono");
        }
        finally {
            em.close();
        }
    }

    public static TipoBono consultaBonoImporte(Long idTipoBono) throws NoResultException, Exception {
        log.info("DAO: Consulta Bono Importe");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        TipoBono tb = null;

        try {
            Query consulta = em.createQuery("SELECT c FROM TipoBono c WHERE c.idTipoBono = :idTipoBono");
            consulta.setParameter("idTipoBono", idTipoBono);
            tb = (TipoBono) consulta.getSingleResult();
        }
        catch (NoResultException e) {
            log.debug("No se encontró el tipo de bono " + idTipoBono);
            throw new NoResultException("No se encontró el tipo de bono.");
        }
        catch (Exception e) {
            log.error("Error en la consulta para el tipo de bono ." + e.getMessage());
            throw new Exception("Error en la consulta para el tipo de bono");
        }
        finally {
            em.close();
        }
        return tb;


    }

    public static Bono consultaBono(String codAlm, Long idBono) throws NoResultException, Exception {
        log.info("DAO: Consulta Bono");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
     //   Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.bonoPK.idBono = :idbono AND b.bonoPK.codalm = :codalm");
            consulta.setParameter("idbono", idBono);
            consulta.setParameter("codalm", codAlm);
            List<Bono> list = consulta.getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            em.refresh(list.get(0));
            return list.get(0);
    
            //tb = (Bono) consulta.getSingleResult();
        }
        catch (NoResultException e) {
            log.debug("No se encontró el bono " + codAlm + "-" + idBono);
            throw new NoResultException("No se encontró el bono.");
        }
        finally {
            em.close();
        }
     //   return tb;



    }
    public static Number consultaBonoIdLike(String codAlm, Long idBono) throws NoResultException, Exception {
        log.info("DAO: Consulta Bono");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        Number tb = null;

        try {
//LIKE UPPER('%" + descripcion + "%');

//            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.bonoPK.idBono LIKE :idbono AND b.bonoPK.codalm = :codalm");
Query consulta =em.createNativeQuery("select max(ID_BONO)  from  X_BONOS_TBL  WHERE  CODALM = '"+codAlm+"' ");
//            consulta.setParameter("codalm", codAlm);
            tb = (Number) consulta.getSingleResult();
            }
        catch (NoResultException e) {
            log.debug("No se encontró el bono " + codAlm + "-" + idBono);
            throw new NoResultException("No se encontró el bono.");
        }
        finally {
            em.close();
        }
        return tb;



    }

    public static void anulaBono(Bono bono, String referenciaUso, String tipoReferenciaUso, EntityManager em, String numTicket) throws Exception {
        try {

            Bono bonocpy = em.find(Bono.class, bono.getBonoPK());
            bonocpy.setUtilizado('S');
            bonocpy.setProcesado('N');
            bonocpy.setReferenciaUso(referenciaUso);
            bonocpy.setTipoReferenciaUso(tipoReferenciaUso);
            bonocpy.setSaldoUsado(bono.getSaldoUsado());
            bonocpy.setMensaje_proceso(numTicket);

        }
        catch (Exception e) {
            log.error("Error modificando bono como utilizado al realizar pago ");
            throw e;
        }
    }

    public static Bono consultaBono(String uidNotaCredito) throws NoResultException{

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.referenciaOrigen = :referenciaOrigen AND b.utilizado = 'N'");
            consulta.setParameter("referenciaOrigen", uidNotaCredito);
            tb = (Bono) consulta.getSingleResult();
        }
        catch (NoResultException e) {
            log.debug("No se encontró el bono " +uidNotaCredito);
            throw new NoResultException("No se encontró el bono.");
        }
        finally {
            em.close();
        }
        return tb;

    }
    
    public static List<Bono> consultarBonosNotaCredito(String uidNotaCredito) throws NoResultException{
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        List<Bono> tb = new ArrayList<Bono>();

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.uidNotaCredito = :uidNotaCredito AND (b.tipoReferenciaUso != :tipoReferenciaUso OR b.tipoReferenciaUso is null) ORDER BY b.bonoPK.idBono ASC");
            consulta.setParameter("uidNotaCredito", uidNotaCredito);
            consulta.setParameter("tipoReferenciaUso", "ANULADO");           
            tb = (List<Bono>) consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("consultarBonosNotaCredito() - No se encontraron bonos " +uidNotaCredito);
            throw new NoResultException("No se encontraron bonos.");
        }
        finally {
            em.close();
        }
        return tb;        
    }
    
    public static List<Bono> consultarBonosReservacion(String uidReservacion) throws NoResultException{
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        List<Bono> tb = new ArrayList<Bono>();

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.uidReservacion = :uidReservacion AND (b.tipoReferenciaUso != :tipoReferenciaUso OR b.tipoReferenciaUso is null) ORDER BY b.bonoPK.idBono ASC");
            consulta.setParameter("uidReservacion", uidReservacion);
            consulta.setParameter("tipoReferenciaUso", "ANULADO");
            tb = (List<Bono>) consulta.getResultList();
        }
        catch (NoResultException e) {
            log.debug("consultarBonosReservacion() - No se encontraron bonos " +uidReservacion);
            throw new NoResultException("No se encontraron bonos.");
        }
        finally {
            em.close();
        }
        return tb;        
    }    
    
    public static void actualizarBono(EntityManager em, Bono b) {
        em.merge(b);
    }
    
    public static Bono consultarBonoCancelacionReservacion(String codAlm, String codReservacion) throws NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.referenciaOrigen = :referenciaOrigen AND b.bonoPK.codalm = :codalm AND b.tipoReferenciaOrigen = :tipoReferenciaOrigen AND ( b.tipoReferenciaUso != :tipoReferenciaUso OR b.tipoReferenciaUso is null )");
            consulta.setParameter("referenciaOrigen", codReservacion);
            consulta.setParameter("codalm", codAlm);
            consulta.setParameter("tipoReferenciaOrigen", BonosServices.PROCEDENCIA_RESERVA_CANCELACION);
            consulta.setParameter("tipoReferenciaUso", "ANULADO");
            
            tb = (Bono) consulta.getSingleResult();
        }
        catch (NoResultException e) {
            log.debug("No se encontró el bono " +codReservacion);
            throw new NoResultException("No se encontró el bono.");
        }
        finally {
            em.close();
        }
        return tb;        
    }
    
    public static Bono consultarBonoReservacion(String codAlm, String codReservacion) throws NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        Bono tb = null;

        try {
            Query consulta = em.createQuery("SELECT b FROM Bono b WHERE b.referenciaOrigen = :referenciaOrigen AND b.bonoPK.codalm = :codalm AND ( b.tipoReferenciaOrigen = :tipoReferenciaOrigen1 OR b.tipoReferenciaOrigen = :tipoReferenciaOrigen2) ORDER BY b.bonoPK.idBono DESC");
            consulta.setParameter("referenciaOrigen", codReservacion);
            consulta.setParameter("codalm", codAlm);
            consulta.setParameter("tipoReferenciaOrigen1", BonosServices.PROCEDENCIA_RESERVA_LIQUIDACION);
            consulta.setParameter("tipoReferenciaOrigen2", BonosServices.PROCEDENCIA_RESERVA_CANCELACION);
            
            tb = (Bono) consulta.getResultList().get(0);
        }
        catch (NoResultException e) {
            log.debug("No se encontró el bono " +codReservacion);
            throw new NoResultException("No se encontró el bono.");
        }
        catch (IndexOutOfBoundsException e) {
            log.debug("No se encontró el bono " +codReservacion);
            throw new NoResultException("No se encontró el bono.");            
        }
        finally {
            em.close();
        }
        return tb;        
    }
    
}
