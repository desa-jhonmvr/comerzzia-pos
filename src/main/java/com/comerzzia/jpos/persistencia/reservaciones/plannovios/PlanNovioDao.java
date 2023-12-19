/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.reservaciones.plannovios;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.ParametrosBuscarPlanNovio;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import es.mpsistemas.util.log.Logger;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class PlanNovioDao {

    private static Logger log = Logger.getMLogger(PlanNovioDao.class);

    public static PlanNovio consultarDetalle(EntityManager em, PlanNovio planParam) throws TicketException {

        String query = "Select a from ArticuloPlanNovio a WHERE a.borrado ='N' and a.articuloPlanNovioPK.idPlan = :idPlan and a.articuloPlanNovioPK.codalm = :codalm";
        Query consulta = em.createQuery(query);
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("idPlan", planParam.getPlanNovioPK().getIdPlan());
        consulta.setParameter("codalm", planParam.getPlanNovioPK().getCodalm());
        //List<Object> lo = consulta.getResultList();
        List<ArticuloPlanNovio> listaArticulos = consulta.getResultList();
        
        for(ArticuloPlanNovio art:listaArticulos){
            if(art.getUidTicket()!=null && !art.getUidTicket().isEmpty()){
                LineaTicketOrigen linea = TicketsDao.consultarLineaTicket(em, art.getUidTicket(), art.getIdLineaTicket());
                if(linea != null){
                    art.setEnvio(linea.getEnvioDomicilio());
                    art.setRecogida(linea.getRecogidaPosterior());
                }
            }
        }        
        planParam.setListaArticulos(listaArticulos);
        return planParam;
    }

    public static PlanNovio consultarAbonosSinAnular(EntityManager em, PlanNovio planParam) {
        String query = "Select a from AbonoPlanNovio a WHERE a.anulado ='N' and a.abonoPlanNovioPK.idPlan = :idPlan and a.abonoPlanNovioPK.codalm = :codalm";
        Query consulta = em.createQuery(query);
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("idPlan", planParam.getPlanNovioPK().getIdPlan());
        consulta.setParameter("codalm", planParam.getPlanNovioPK().getCodalm());
        //List<Object> lo = consulta.getResultList();
        List<AbonoPlanNovio> listaAbonos = consulta.getResultList();
        planParam = em.find(PlanNovio.class, planParam.getPlanNovioPK());
        em.refresh(planParam);
        planParam.setListaAbonos(listaAbonos);
        return planParam;        
    }
    
    public static BigInteger consultaMaximoIdAbonoPorPlan(EntityManager em, BigInteger idPlan) {
        
        BigInteger res = null;
        Query consulta = em.createQuery("SELECT MAX(r.abonoPlanNovioPK.idAbono) FROM AbonoPlanNovio r WHERE r.abonoPlanNovioPK.idPlan = :idPlan ").setHint("eclipselink.refresh", "true");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("idPlan", idPlan);
        try {
            Object result = (consulta.getSingleResult());//.getReservaAbonoPK().getIdAbono();
            if (result != null) {
                //res = (Long) result;
                res = (BigInteger) result;
            }
            else {
                res = new BigInteger("0");
            }
        }
        catch (NoResultException e) {
            res = new BigInteger("0");
        }
        return res;
    }
    
    public static BigInteger maximoIDPorCaja(EntityManager em, String codCaja) {
        BigInteger res = null;
        Query consulta = em.createQuery("SELECT MAX(p.planNovioPK.idPlan) FROM PlanNovio p WHERE p.codcaja = :codCaja ").setHint("eclipselink.refresh", "true");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("codCaja", codCaja);
        try {
            Object result = (consulta.getSingleResult());//.getReservaAbonoPK().getIdAbono();
            if (result != null) {
                //res = (Long) result;
                res = (BigInteger) result;
            }
            else {
                res = new BigInteger("0");
            }
        }
        catch (NoResultException e) {
            res = new BigInteger("0");
        }
        return res;
    }
    
    public static List<PlanNovio> buscar(EntityManager em, ParametrosBuscarPlanNovio param) {
        List<PlanNovio> res = new LinkedList<PlanNovio>();


        String query = "SELECT c FROM PlanNovio c LEFT JOIN FETCH c.novio LEFT JOIN FETCH c.novia ";
        String where = crearClausulaWhere(param);

        if (!where.isEmpty()) {
            query += " WHERE " + where;
        }
        
        // Oredenación
        query = query+  " ORDER BY c.fechaAlta DESC ";
        
        Query consulta = em.createQuery(query);
        consulta.setHint("eclipselink.refresh", "true");
        
        // Establecemos los parametros de búsqueda
        if (param.getNumeroReserva() != null) {
            consulta.setParameter("idPlan", param.getNumeroReserva());
        }

        if (param.getBoda() != null && !param.getBoda().isEmpty()) {
            consulta.setParameter("boda", "%" + param.getBoda() + "%");
        }
        if (param.getIdPlanSukasa() != null) {
            consulta.setParameter("idPlanSukasa", param.getIdPlanSukasa());
        }

        if (param.getDocumentoNovia() != null && !param.getDocumentoNovia().isEmpty()) {
            consulta.setParameter("noviaCedula", "%" + param.getDocumentoNovia() + "%");
        }
        if (param.getDocumentoNovio() != null && !param.getDocumentoNovio().isEmpty()) {
            consulta.setParameter("novioCedula", "%" + param.getDocumentoNovio() + "%");
        }
        if (param.getNombresNovia() != null && !param.getNombresNovia().isEmpty()) {
            consulta.setParameter("noviaNombre", "%" + param.getNombresNovia() + "%");
        }
        if (param.getNombresNovio() != null && !param.getNombresNovio().isEmpty()) {
            consulta.setParameter("novioNombre", "%" + param.getNombresNovio() + "%");
        }
        if (param.getApellidosNovia() != null && !param.getApellidosNovia().isEmpty()) {
            consulta.setParameter("noviaApellidos", "%" + param.getApellidosNovia() + "%");
        }
        if (param.getApellidosNovio() != null && !param.getApellidosNovio().isEmpty()) {
            consulta.setParameter("novioApellidos", "%" + param.getApellidosNovio() + "%");
        }

        if (param.getDesde() != null) {
            consulta.setParameter("fechaDesde", param.getDesde());
        }
        if (param.getHasta() != null) {
            consulta.setParameter("fechaHasta", param.getHasta());
        }
        if (param.getEstadoPlan() != null && !param.getEstadoPlan().isEmpty()
                && (param.getEstadoPlan().equals("Abierto") || (param.getEstadoPlan().equals("Caducado")))) {
            consulta.setParameter("fechaHoy", new Date());
        }
        if(param.getTiendaPlan() != null && !param.getTiendaPlan().isEmpty()){            
            consulta.setParameter("codalm", param.getTiendaPlan());
        }

        res = consulta.getResultList();

        return res;
    }

    private static String crearClausulaWhere(ParametrosBuscarPlanNovio param){
        String where = "";
        // Falta Título en la tabla de Planes 
        // if (param.getBoda()!=)
        if (param.getNumeroReserva() != null) {
            where += " c.planNovioPK.idPlan=:idPlan ";
        }

        if (param.getIdPlanSukasa() != null) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " c.idPlanSukasa=:idPlanSukasa ";
        }

        if (param.getBoda() != null && !param.getBoda().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(CONCAT(CONCAT(CONCAT('BODA:','',c.novio.apellido),'','-'),'',c.novia.apellido)) LIKE UPPER(:boda) ";
            //where += " UPPER(c.novio.codcli) LIKE UPPER(:novioCedula) OR UPPER(c.novia.codcli) LIKE UPPER(:noviaCedula) ";
        }

        if (param.getNombresNovia() != null && !param.getNombresNovia().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novia.nombre) LIKE UPPER(:noviaNombre) ";
        }

        if (param.getNombresNovio() != null && !param.getNombresNovio().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novio.nombre) LIKE UPPER(:novioNombre) ";
        }

        if (param.getApellidosNovia() != null && !param.getApellidosNovia().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novia.apellido) LIKE UPPER(:noviaApellidos) ";
        }

        if (param.getApellidosNovio() != null && !param.getApellidosNovio().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novio.apellido) LIKE UPPER(:novioApellidos) ";
        }

        if (param.getDocumentoNovia() != null && !param.getDocumentoNovia().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novia.identificacion) LIKE UPPER(:noviaCedula) ";
        }

        if (param.getDocumentoNovio() != null && !param.getDocumentoNovio().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " UPPER(c.novio.identificacion) LIKE UPPER(:novioCedula) ";
        }


        if (param.getDesde() != null) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " c.fechaAlta >= :fechaDesde ";
        }

        if (param.getHasta() != null) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            where += " c.fechaAlta <= :fechaHasta ";
        }

        if (param.getEstadoPlan() != null && !param.getEstadoPlan().isEmpty()) {
            if (!where.isEmpty()) {
                where += " AND ";
            }
            if (param.getEstadoPlan().equals("Abierto")) {
                // 
                where += " c.liquidado='N' ";
                where += " AND ";
                where += " c.caducidad> :fechaHoy";
            }
            else if (param.getEstadoPlan().equals("Liquidado")) {

                where += " c.liquidado='S' ";
            }
            else if (param.getEstadoPlan().equals("Caducado")) {
                where += " c.liquidado='N' ";
                where += " AND ";
                where += " c.caducidad<= :fechaHoy";
            }
        }
        
        if(param.getTiendaPlan()!= null && !param.getTiendaPlan().isEmpty()){            
            if(!where.isEmpty()){
                where += " AND ";            
            }
            where += " c.planNovioPK.codalm = :codalm";
        }
        return where;
    }
    
    public static void crear(EntityManager em, PlanNovio plan) {
        em.persist(plan);
    }

    public static Long consultaSiguienteIdLineaArticulos(BigInteger plan) throws PlanNovioException {
        Long res;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            // consultamos  el valor de la tabla registro con codalm y codcaja del pos
            Query consulta = em.createQuery("SELECT MAX(r.articuloPlanNovioPK.idLinea) FROM ArticuloPlanNovio r WHERE r.articuloPlanNovioPK.idPlan = :idPlan").setHint("eclipselink.refresh", "true");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("idPlan", plan);

            try {
                Object result = (consulta.getSingleResult());
                if (result != null) {
                    res = ((Long) result).longValue();
                }
                else {
                    res = new Long(0);
                }
            }
            catch (NoResultException e) {
                res = new Long(0);
            }
            return res + 1;
        }
        catch (Exception ex) {
            log.error("Excepción consultando el contador de Lineas de Artículos de un plan Novio ", ex);
            throw new PlanNovioException();
        }
        finally {
            em.close();
        }
    }

    public static void modifica(EntityManager em, PlanNovio plan) {
        em.merge(plan);
    }

    public static void addArticulos(EntityManager em, List<ArticuloPlanNovio> listaArt) {
        for (ArticuloPlanNovio apn : listaArt) {
            em.persist(apn);
        }
    }

    public static void removeArticulo(EntityManager em, ArticuloPlanNovio articuloReservado) {
        articuloReservado.setBorrado(true);
        em.merge(articuloReservado);
    }

    public static void refrescaReserva(EntityManager em, PlanNovio planParam) throws PlanNovioException {   
        PlanNovio plan = consultaPlanNovio(em, planParam.getPlanNovioPK().getCodalm(),planParam.getPlanNovioPK().getIdPlan());
        plan.setProcesado('N');
        plan.setProcesadoTienda('N');
        // Actualizamos los totales
        plan.setAbonadoConDto(planParam.getAbonadoConDto());
        plan.setAbonadoSinDto(planParam.getAbonadoSinDto());
        plan.setReservado(planParam.getReservado());
        plan.setComprado(planParam.getComprado());
        plan.setAbonadoUtilizado(planParam.getAbonadoUtilizado());
        em.merge(plan);
    }

    public static void crearInvitado(EntityManager em, InvitadoPlanNovio invitado) {
        em.persist(invitado);
    }

    public static Long consultaSiguienteIdInvitado(EntityManager em, PlanNovio plan) {
        Long res = null;
        Query consulta = em.createQuery("SELECT MAX(r.invitadoPlanNovioPK.idInvitado) FROM InvitadoPlanNovio r WHERE r.invitadoPlanNovioPK.codalm = :codalm AND r.invitadoPlanNovioPK.idPlan = :idPlan ").setHint("eclipselink.refresh", "true");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("codalm", plan.getPlanNovioPK().getCodalm());
        consulta.setParameter("idPlan", plan.getPlanNovioPK().getIdPlan());
        try {
            Object result = (consulta.getSingleResult());//.getReservaAbonoPK().getIdAbono();
            if (result != null) {
                //res = (Long) result;
                res = ((BigInteger) result).longValue();
            }
            else {
                res = new Long(0);
            }
        }
        catch (NoResultException e) {
            res = new Long(0);
        }
        return res + 1;
    }

    public static void actualizarArticulos(EntityManager em, List<ArticuloPlanNovio> listaresArt) {
        for (ArticuloPlanNovio apn : listaresArt) {
            actualizarArticulo(em, apn);
        }
    }

    private static void actualizarArticulo(EntityManager em, ArticuloPlanNovio apn) {
        em.merge(apn);
    }

    public static void modificaNoProcesado(EntityManager em, PlanNovio plan) {
        plan.setProcesado('N');
        modifica(em, plan);
    }

    public static BigInteger consultaSiguienteIdAbono(EntityManager em, PlanNovio plan) {
        BigInteger res = null;
        Query consulta = em.createQuery("SELECT MAX(r.abonoPlanNovioPK.idAbono) FROM AbonoPlanNovio r WHERE r.abonoPlanNovioPK.codalm = :codalm AND r.abonoPlanNovioPK.idPlan = :idPlan ").setHint("eclipselink.refresh", "true");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("codalm", plan.getPlanNovioPK().getCodalm());
        consulta.setParameter("idPlan", plan.getPlanNovioPK().getIdPlan());
        try {
            Object result = (consulta.getSingleResult());//.getReservaAbonoPK().getIdAbono();
            if (result != null) {
                //res = (Long) result;
                res = (BigInteger) result;
            }
            else {
                res = new BigInteger("0");
            }
        }
        catch (NoResultException e) {
            res = new BigInteger("0");
        }
        return res.add(new BigInteger("1"));
    }

    public static void crearAbono(EntityManager em, AbonoPlanNovio abono) {
        em.persist(abono);
    }

    public static void modifica(EntityManager em, ArticuloPlanNovio art) {
        em.merge(art);
    }

    public static void modifica(EntityManager em, InvitadoPlanNovio inv) {
        em.merge(inv);
    }
    
    public static void modifica(EntityManager em, AbonoPlanNovio abono) {
        em.merge(abono);
    }

    public static void refrescaInvitadosPlan(EntityManager em, PlanNovio plan) {
        Query consulta = em.createQuery("SELECT i FROM InvitadoPlanNovio i WHERE i.invitadoPlanNovioPK.idPlan = :idPlan and i.invitadoPlanNovioPK.codalm = :codalm ORDER BY i.apellido");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("idPlan", plan.getPlanNovioPK().getIdPlan());
        consulta.setParameter("codalm", plan.getPlanNovioPK().getCodalm());
        try {
            List<InvitadoPlanNovio> result = (consulta.getResultList());//.getReservaAbonoPK().getIdAbono();
            if (result != null) {
                //res = (Long) result;
                plan.setListaInvitados(result);
            }
            else {
                plan.setListaInvitados(new ArrayList());
            }
        }
        catch (NoResultException e) {
            plan.setListaInvitados(new ArrayList());
        }

    }

    public static void elimina(EntityManager em, InvitadoPlanNovio inv) {
        InvitadoPlanNovio invABorrar = em.find(InvitadoPlanNovio.class, inv.getInvitadoPlanNovioPK());
        em.remove(invABorrar);
    }
    
    public static PlanNovio consultaPlanNovio(EntityManager em, String codAlmacen, BigInteger idPlan) throws PlanNovioException{
        
        Query consulta = em.createQuery("SELECT p FROM PlanNovio p WHERE p.planNovioPK.codalm = :codalm AND p.planNovioPK.idPlan = :idPlan");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("codalm", codAlmacen);
        consulta.setParameter("idPlan", idPlan);
        
        try{
            return (PlanNovio) consulta.getSingleResult();
        }
        catch (NoResultException e){
            throw new PlanNovioException("No se ha encontrado el Plan Novio");
        }
        catch(Exception e){
            throw new PlanNovioException("Error inesperado consultando el Plan Novio",e);
        }
    }
    
    public static List<ArticuloPlanNovio> consultarArticulosUidTicket(EntityManager em, String uidTicket) throws PlanNovioException{
        Query consulta = em.createNamedQuery("ArticuloPlanNovio.findByUidTicket");
        consulta.setParameter("uidTicket", uidTicket);
        
        try{
            return (List<ArticuloPlanNovio>)consulta.getResultList();
        } catch (Exception e){
            throw new PlanNovioException("Error consultando los articulos del uidTicket: " + uidTicket,e);
        }
    }
}
