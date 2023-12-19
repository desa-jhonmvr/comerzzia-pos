/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.devoluciones;

import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.db.Devolucion;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticulosDevueltosDao;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionErradaBean;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.transaccioneserradas.ServicioTransaccionesErradas;
import com.comerzzia.jpos.util.db.Connection;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class DevolucionesDao {

    private static Logger log = Logger.getMLogger(DevolucionesDao.class);

    public static void escribirDevolucionYNotaCredito(EntityManager em, NotasCredito nota, Devolucion devolucionDB, List<ArticuloDevueltoBean> articulosDevueltos, CajaDet movimiento) throws Exception {
        log.info("escribirDevolucionYNotaCredito() - Escribir Nota de Crédito...");
        try {
            Connection conn = new Connection();
            //guardamos la nota de credito
            escribirNotaDeCredito(em, nota, devolucionDB);

            conn = Connection.getConnection(em);

            for (ArticuloDevueltoBean art : articulosDevueltos) {
                ArticulosDevueltosDao.insert(conn, art);
            }
            // registramos movimiento de devolución
            em.persist(movimiento);
        } catch (Exception e) {
            log.error("escribirDevolucionYNotaCredito() - Error registrando en Base de datos devolución y nota de crédito: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(nota.getIdNotaCredito(), TransaccionErradaBean.TIPO_GUARDANDO, TransaccionErradaBean.TIPO_TRANSACCION_NOTA_CREDITO);
            throw e;
        }

    }

    public static void escribirNotaDeCredito(EntityManager em, NotasCredito nota, Devolucion devolucion) throws Exception {
        log.debug("escribirNotaDeCredito() - Escribir Nota de Crédito...");
        try {
            //guardamos la nota de credito
            em.persist(nota);

            //guardamos la devolucion
            em.persist(devolucion);

            //incrementamos el contador de nota de credito.
            ServicioContadoresCaja.incrementarContadorNotaCredito(Connection.getConnection(em));
        } catch (Exception e) {
            log.error("escribirNotaDeCredito() - Error registrando en Base de datos nota de crédito: " + e.getMessage(), e);
            ServicioTransaccionesErradas.crearTransaccionErrada(nota.getIdNotaCredito(), TransaccionErradaBean.TIPO_GUARDANDO, TransaccionErradaBean.TIPO_TRANSACCION_NOTA_CREDITO);
            throw e;
        }

    }

    public static void actualizarNotaDeCredito(NotasCredito nota) throws Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.info("DAO: Escribir Nota de Crédito");

        try {
            em.getTransaction().begin();
            //guardamos la nota de credito
            em.merge(nota);

            //incrementamos el contador de nota de credito.
            ServicioContadoresCaja.incrementarContadorNotaCredito(Connection.getConnection(em));

            em.getTransaction().commit();

        } catch (Exception e) {
            log.error("Error en la consulta de Motivos de devolucion ", e);
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static NotasCredito consultarNotaCredito(String codAlmacen, String codCaja, Long idNota) throws Exception {
        log.debug("DAO: Consultar Nota de Crédito");
        NotasCredito res = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT n FROM NotasCredito n WHERE n.idNotaCredito = :idNotaCredito AND n.codcaja = :codcaja AND n.codalm = :codalm");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("idNotaCredito", idNota);
            consulta.setParameter("codcaja", codCaja);
            consulta.setParameter("codalm", codAlmacen);
            res = (NotasCredito) consulta.getSingleResult();
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error en la consulta de Nota de crédito ", e);
            throw e;
        } finally {
            em.close();
        }
        return res;
    }

    public static NotasCredito consultarNotaCreditoWithEm(String codAlmacen, String codCaja, Long idNota, EntityManager em) throws Exception {
        log.debug("DAO: Consultar Nota de Crédito");
        NotasCredito res = null;

        try {
            Query consulta = em.createQuery("SELECT n FROM NotasCredito n WHERE n.idNotaCredito = :idNotaCredito AND n.codcaja = :codcaja AND n.codalm = :codalm");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("idNotaCredito", idNota);
            consulta.setParameter("codcaja", codCaja);
            consulta.setParameter("codalm", codAlmacen);
            res = (NotasCredito) consulta.getSingleResult();
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error en la consulta de Nota de crédito ", e);
            throw e;
        }
        return res;
    }

    public static void modificaNotaCredito(String uidNota, BigDecimal nuevoSaldo, String idReferencia, String tipo, String documento) throws Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            modificaNotaCredito(uidNota, nuevoSaldo, null, em, idReferencia, tipo, documento);
            em.getTransaction().commit();
        } catch (NotaCreditoException e) {
            throw e;
        } catch (Exception e) {
            log.error("modificaNotaCredito() - Error actualizando nota de credito: " + uidNota, e);
            throw new NotaCreditoException("Error actualizando nota de credito: " + uidNota, e);
        } finally {
            em.close();
        }
    }

    public static void modificaNotaCredito(String uidNota, BigDecimal nuevoSaldo, BigDecimal saldoUsado, EntityManager em, String idReferencia, String tipo, String documento) throws NotaCreditoException {
        try {
            Query consulta = em.createQuery("SELECT n FROM NotasCredito n WHERE n.uidNotaCredito = :uidNotaCredito");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidNotaCredito", uidNota);
            NotasCredito notaCredito = (NotasCredito) consulta.getSingleResult();
            notaCredito.setSaldo(nuevoSaldo);
            notaCredito.setReferenciaUso(idReferencia);
            notaCredito.setTipo(tipo);
            notaCredito.setDocumento(documento);
            notaCredito.setSaldoUsado(saldoUsado);
            actualizarNotaCreditoSQL(notaCredito, em);
        } catch (NotaCreditoException e) {
            throw e;
        } catch (Exception e) {
            log.error("modificaNotaCredito() - Error actualizando nota de credito: " + uidNota, e);
            throw new NotaCreditoException("Error actualizando nota de credito: " + uidNota, e);
        }
    }

    public static NotasCredito consultarNotaCreditoByUid(String uidNota, EntityManager em) throws NotaCreditoException {
        NotasCredito notaCredito = null;
        try {
            Query consulta = em.createQuery("SELECT n FROM NotasCredito n WHERE n.uidNotaCredito = :uidNotaCredito");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidNotaCredito", uidNota);
            notaCredito = (NotasCredito) consulta.getSingleResult();
        } catch (Exception e) {
            log.error("modificaNotaCredito() - Error actualizando nota de credito: " + uidNota, e);
            throw new NotaCreditoException("Error actualizando nota de credito: " + uidNota, e);
        }
        return notaCredito;
    }

    public static void modificaNotaCredito(NotasCredito notaCredito, EntityManager em) throws NotaCreditoException {
        try {
            log.debug("modificaNotaCredito() - Actualizando nota de crédito...");
            notaCredito.setProcesado('N');
            em.merge(notaCredito);
        } catch (Exception e) {
            log.error("modificaNotaCredito() - Error actualizando nota de credito: " + notaCredito.getIdNotaCreditoCompleto(), e);
            throw new NotaCreditoException("Error actualizando nota de credito: " + notaCredito.getIdNotaCreditoCompleto(), e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Motivos de devolucion">
    //Motivos de devolución
    public static List<MotivoDevolucion> consultaMotivosDevolucion() throws Exception {

        List<MotivoDevolucion> res = new LinkedList<MotivoDevolucion>();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        log.debug("Consultando motivos de devolución... ");

        try {
            Query consulta = em.createQuery("SELECT c FROM MotivoDevolucion c");
            res = consulta.getResultList();
        } catch (NoResultException e) {
            log.debug("No existen Motivos de devolucion ");
        } catch (Exception e) {
            log.error("Error en la consulta de Motivos de devolucion ", e);
            throw e;
        } finally {
            em.close();
        }
        return res;
    }
    //</editor-fold>

    //<editor-fold defaultstate="open" desc="Articulos Devueltos">
//    public static List<ArticulosDevueltosBean> consultaArticulosDevueltos(String ticketUid) throws Exception {
//        List<ArticulosDevueltos> res = new LinkedList();
//        EntityManagerFactory emf = Sesion.getEmf();
//        EntityManager em = emf.createEntityManager();
//
//        log.debug("Consultando Artículos devueltos");
//
//        try {
//            Query consulta = em.createQuery("SELECT c FROM ArticulosDevueltos c WHERE c.articulosDevueltosPK.uidTicket=:uidTicket");
//            consulta.setHint("eclipselink.refresh", "true");
//            consulta.setParameter("uidTicket", ticketUid);
//            res = consulta.getResultList();
//        }
//        catch (NoResultException e) {
//            log.debug("No existen artículos devueltos para el ticket consultado ");
//        }
//        catch (Exception e) {
//            log.error("Error en la consulta de Artículos devueltos ", e);
//            throw e;
//        }
//        finally {
//            em.close();
//        }
//        return res;
//
//    }
    //</editor-fold>
//    public static List<ArticulosDevueltos> consultaArticulosDevueltosWithConexion(EntityManager em, String ticketUid) throws Exception {
//        List<ArticulosDevueltos> res = new LinkedList();
//
//        log.debug("Consultando Artículos devueltos");
//
//        try {
//            Query consulta = em.createQuery("SELECT c FROM ArticulosDevueltos c WHERE c.articulosDevueltosPK.uidTicket=:uidTicket");
//            consulta.setHint("eclipselink.refresh", "true");
//            consulta.setParameter("uidTicket", ticketUid);
//            res = consulta.getResultList();
//        }
//        catch (NoResultException e) {
//            log.debug("No existen artículos devueltos para el ticket consultado ");
//        }
//        catch (Exception e) {
//            log.error("Error en la consulta de Artículos devueltos ", e);
//            throw e;
//        }
//        return res;
//
//    }
//    public static List<ArticulosDevueltos> consultaArticulosDevueltosByUidNotaCredito(String uidNotaCredito) throws Exception {
//        List<ArticulosDevueltos> res = new LinkedList();
//        EntityManagerFactory emf = Sesion.getEmf();
//        EntityManager em = emf.createEntityManager();
//
//        log.debug("Consultando Artículos devueltos");
//
//        try {
//            Query consulta = em.createQuery("SELECT c FROM ArticulosDevueltos c WHERE c.uidNotaCredito=:uidNotaCredito");
//            consulta.setHint("eclipselink.refresh", "true");
//            consulta.setParameter("uidNotaCredito", uidNotaCredito);
//            res = consulta.getResultList();
//        }
//        catch (NoResultException e) {
//            log.debug("No existen artículos devueltos para el ticket consultado ");
//        }
//        catch (Exception e) {
//            log.error("Error en la consulta de Artículos devueltos ", e);
//            throw e;
//        }
//        finally {
//            em.close();
//        }
//        return res;
//
//    }    
//    public static void insertarArticulosDevueltos(EntityManager em, ArticulosDevueltos articuloDevuelto) throws Exception {
//        log.info("insertarArticulosDevueltos() - Escribir Nota de Crédito...");
//        try {
//            em.merge(articuloDevuelto);
//        }
//        catch (Exception e) {
//            log.error("insertarArticulosDevueltos() - Error registrando en Base de datos articulosDevueltos: " + e.getMessage(), e);
//            throw e;
//        }        
//    }
    public static Devolucion consultarDevolucion(EntityManager em, NotasCredito nc) throws NoResultException, Exception {
        Devolucion res = null;
        try {
            Query consulta = em.createQuery("SELECT d FROM Devolucion d WHERE d.uidNotaCredito = :uidNotaCredito");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidNotaCredito", nc.getUidNotaCredito());
            res = (Devolucion) consulta.getSingleResult();
        } catch (NoResultException e) {
            log.debug("No existe devolución asociada a la nota de crédito uid: " + nc.getUidNotaCredito());
            throw e;
        } catch (Exception e) {
            log.error("Error en la consulta de Artículos devueltos ,Error: " + e.getMessage(), e);
            throw e;
        }

        return res;
    }

    public static List<Devolucion> consultarDevolucion(EntityManager em, String uidTicket) throws Exception {
        List<Devolucion> res = null;
        try {
            Query consulta = em.createQuery("SELECT d FROM Devolucion d WHERE d.uidTicket = :uidTicket");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidTicket", uidTicket);
            res = (List<Devolucion>) consulta.getResultList();
        } catch (NoResultException e) {
            log.debug("No existe devolución asociada al ticket con uid: " + uidTicket);
            return null;
        } catch (Exception e) {
            log.error("Error en la consulta de Devoluciones, Error: " + e.getMessage(), e);
            throw e;
        }

        return res;
    }

//    public static void borrarArticulosDevueltos(EntityManager em, List<ArticulosDevueltos> articulosDevueltos) throws Exception {
//        try{
//            for(ArticulosDevueltos articuloDevuelto : articulosDevueltos){
//                ArticulosDevueltos aBorrar = em.merge(articuloDevuelto);
//                em.remove(aBorrar);
//            }
//        } catch (Exception e) {
//            log.error("borrarArticulosDevueltos() - Error borrando la devolución, Error: "+e.getMessage(),e);
//            throw e;
//        }
//    }
    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Actualiza la nota de credito mediante SQL, para que siempre grabe el
     * procesado en 'N' </p>
     *
     *
     * @param notaCredito
     * @param em
     * @throws NotaCreditoException
     */
    public static void actualizarNotaCreditoSQL(NotasCredito notaCredito, EntityManager em) throws NotaCreditoException {
        try {

            log.debug("actualizarNotaCredito() - Actualizando nota de crédito por query...");

            StringBuilder sqlBuilder = new StringBuilder("UPDATE X_NOTAS_CREDITO_TBL n SET n.SALDO = ?, n.REFERENCIA_USO= ?, ");
            sqlBuilder.append(" n.TIPO=?, n.DOCUMENTO=?, n.SALDO_USADO = ?, n.PROCESADO = 'N'");
            sqlBuilder.append(" where n.UID_NOTA_CREDITO = ? ");

            Query query = em.createNativeQuery(sqlBuilder.toString());

            query.setParameter(1, notaCredito.getSaldo());
            query.setParameter(2, notaCredito.getReferenciaUso());
            query.setParameter(3, notaCredito.getTipo());
            query.setParameter(4, notaCredito.getDocumento());
            query.setParameter(5, notaCredito.getSaldoUsado());
            query.setParameter(6, notaCredito.getUidNotaCredito());
            query.executeUpdate();

        } catch (Exception e) {
            log.error("actualizarNotaCredito() - Error actualizando nota de credito: " + notaCredito.getIdNotaCreditoCompleto(), e);
            throw new NotaCreditoException("Error actualizando nota de credito: " + notaCredito.getIdNotaCreditoCompleto(), e);
        }
    }

}
