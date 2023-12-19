/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.otp;

import com.comerzzia.jpos.entity.db.CodigoOtp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Gabriel Simbania
 */
public class CodigoOtpDao {

    /**
     *
     * @param codigoOtp
     * @param em
     * @throws Exception
     */
    public static void crear(CodigoOtp codigoOtp, EntityManager em) throws Exception {
        em.persist(codigoOtp);
    }

    /**
     *
     * @param codigoOtp
     * @param em
     * @throws Exception
     */
    public static void actualizar(CodigoOtp codigoOtp, EntityManager em) throws Exception {
        em.merge(codigoOtp);
    }

    /**
     *
     * @param em
     * @param codAlm
     * @param otp
     * @param uidTicket
     * @return
     * @throws Exception
     */
    public static CodigoOtp consultarCodigoOtp(EntityManager em, String codAlm, Long otp, String uidTicket) throws Exception {

        CodigoOtp codigoOtp = null;
        try {

            Query consulta = em.createQuery("SELECT a FROM CodigoOtp a WHERE a.codAlm= :codAlm and a.otp =:otp and a.uidTicket=:uidTicket")
                    .setHint("toplink.refresh", "true");
            consulta.setParameter("codAlm", codAlm);
            consulta.setParameter("otp", otp);
            consulta.setParameter("uidTicket", uidTicket);
            codigoOtp = (CodigoOtp) consulta.getSingleResult();
        } catch (NoResultException ex) {
            codigoOtp = null;
        }
        return codigoOtp;
    }

    /**
     * 
     * @param em
     * @param codAlm
     * @param uidTicket
     * @return
     * @throws Exception 
     */
    public static CodigoOtp consultarCodigoOtpActual(EntityManager em, String codAlm, String uidTicket) throws Exception {

        CodigoOtp codigoOtp = null;
        try {

            Query consulta = em.createQuery("SELECT a FROM CodigoOtp a WHERE a.codAlm= :codAlm and a.uidTicket=:uidTicket order by a.fechaCreacion desc")
                    .setHint("toplink.refresh", "true");
            consulta.setParameter("codAlm", codAlm);
            consulta.setParameter("uidTicket", uidTicket);
            List<CodigoOtp> lista = consulta.getResultList();

            if (!lista.isEmpty()) {
                codigoOtp = lista.get(0);
            }

        } catch (NoResultException ex) {
            codigoOtp = null;
        }
        return codigoOtp;
    }

}
