/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.garantia;

import com.comerzzia.jpos.entity.db.GarantiaExtendidaReg;
import com.comerzzia.jpos.util.enums.EnumEstado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class GarantiasDao {

    public static void salvar(EntityManager em, GarantiaExtendidaReg garantiaReg) {
        em.persist(garantiaReg);
    }

    /**
     * Consulta la garantia extendida
     *
     * @param em
     * @param uidTicket
     * @param codArticulo
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<GarantiaExtendidaReg> consultarGarantiaAnterior(EntityManager em, String uidTicket, String codArticulo) {
        try {
            Query consulta = em.createQuery("SELECT g FROM GarantiaExtendidaReg g "
                    + "WHERE g.garantiaExtendidaRegPK.uidTicket = :uidTicket "
                    + "AND g.codart = :codart "
                    + "AND g.estado = :estado");
            consulta.setParameter("uidTicket", uidTicket);
            consulta.setParameter("codart", codArticulo);
            consulta.setParameter("estado", EnumEstado.ACTIVO);
            return consulta.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param em
     * @param uidTicket
     * @return
     */
    public static Short consultarMaxIdLineaGarantia(EntityManager em, String uidTicket) {
        try {
            Query consulta = em.createQuery("SELECT MAX(g.garantiaExtendidaRegPK.idLinea) FROM GarantiaExtendidaReg g "
                    + "WHERE g.garantiaExtendidaRegPK.uidTicket = :uidTicket "
                    + "AND g.estado = :estado");
            consulta.setParameter("uidTicket", uidTicket);
            consulta.setParameter("estado", EnumEstado.ACTIVO);
            Short max = (Short) consulta.getSingleResult();
            if (max == null) {
                return 0;
            }
            return max;
        } catch (NoResultException e) {
            return 0;
        }
    }

    /**
     * Consultar ls GarantiaExtendidaReg por los diferentes criterios
     *
     * @author Gabriel Simbania
     * @param em
     * @param uidTicket
     * @param uidTicketReferencia
     * @param codArticulo
     * @param lineaId
     * @return
     */
    public static GarantiaExtendidaReg consultarGarantiaByCriterio(EntityManager em, String uidTicket, String uidTicketReferencia, String codArticulo, int lineaId) {
        try {

            StringBuilder builder = new StringBuilder("SELECT g FROM GarantiaExtendidaReg g ");
            builder.append(" WHERE 1 = 1");
            if (uidTicket != null) {
                builder.append(" and  g.garantiaExtendidaRegPK.uidTicket = :uidTicket ");
                builder.append(" and  g.garantiaExtendidaRegPK.idLinea = :lineaId ");
            }
            if (uidTicketReferencia != null) {
                builder.append(" and  g.garantiaExtendidaRegPK.uidTicketReferencia = :uidTicketReferencia ");
                builder.append(" and  g.garantiaExtendidaRegPK.idLinea = :lineaId ");
            }
            builder.append(" AND g.codart = :codart ");
            builder.append(" AND g.estado = :estado");

            Query consulta = em.createQuery(builder.toString());
            if (uidTicket != null) {
                consulta.setParameter("uidTicket", uidTicket);
                consulta.setParameter("lineaId", lineaId);
            }
            if (uidTicketReferencia != null) {
                consulta.setParameter("uidTicketReferencia", uidTicketReferencia);
                consulta.setParameter("lineaId", lineaId);
            }
            consulta.setParameter("codart", codArticulo);
            consulta.setParameter("estado", EnumEstado.ACTIVO);
            return (GarantiaExtendidaReg) consulta.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Elimina la garantia extendida
     *
     * @param em
     * @param uidTicket
     */
    public static void delete(EntityManager em, String uidTicket) {

        Query delete = em.createQuery("DELETE FROM GarantiaExtendidaReg g WHERE g.garantiaExtendidaRegPK.uidTicket = :uidTicket");
        delete.setParameter("uidTicket", uidTicket);

        delete.executeUpdate();
    }

    /**
     * @author Gabriel Simbania
     * @description Actualiza el objeto GarantiaExtendidaReg
     * @param em
     * @param garantiaReg
     */
    public static void update(EntityManager em, GarantiaExtendidaReg garantiaReg) {
        em.merge(garantiaReg);
    }

}
