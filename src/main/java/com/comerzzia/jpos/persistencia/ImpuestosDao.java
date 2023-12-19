/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia;

import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.entity.db.ImpuestosFact;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.EntityManager;

/**
 *
 * @author MGRI
 */
public class ImpuestosDao extends MantenimientoDao {

    private static final Logger log = Logger.getMLogger(ImpuestosDao.class);
    private static String TABLA = "CONFIG_IMPUESTOS_TBL";
    private static String TABLA1 = "D_IMPUESTOS_TBL";

//    public static Impuestos consultaImpuestos(Integer codigo) throws NoResultException, Exception {
//        log.info("DAO: Consulta Bono");
//
//        EntityManagerFactory emf = Sesion.getEmf();
//        EntityManager em = emf.createEntityanager();
//        //   Bono tb = null;
//
//        try {
//            Query consulta = em.createQuery("SELECT a FROM CONFIG_IMPUESTOS_TBL a WHERE a.CODIGO = 3680");
//            consulta.setParameter("codigo", codigo);
//            List<Impuestos> list = consulta.getResultList();
//            if (list == null || list.isEmpty()) {
//                return null;
//            }
//            em.refresh(list.get(0));
//            return list.get(0);
//
//        } catch (NoResultException e) {
//            log.debug("No se encontró el Impuesto " + codigo);
//            throw new NoResultException("No se encontró el bono.");
//        } finally {
//            em.close();
//        }
//    }
    public static Impuestos consultar(Connection conn, Long codigo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Impuestos imp = null;
        String sql = null;

        sql = "SELECT  ID_IMPUESTOS,CODIGO, DESCRIPCION,TARIFA_PORCENTAJE,TARIFA_ESPECIFICA "
                + "FROM " + getNombreElementoConfiguracion(TABLA)
                + "WHERE CODIGO = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, codigo);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                imp = new Impuestos();
                imp.setIdImpuestos(rs.getLong("ID_IMPUESTOS"));
                imp.setCodigo(rs.getLong("CODIGO"));
                imp.setDescripcion(rs.getString("DESCRIPCION"));
                imp.setTarifaPorcentaje(rs.getLong("TARIFA_PORCENTAJE"));
                imp.setTarifaEspecifica(rs.getBigDecimal("TARIFA_ESPECIFICA"));
            }

            return imp;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void crearImpuestos(ImpuestosFact imp, EntityManager em) throws LogException {
        try {
            em.persist(imp);
        } catch (Exception ex) {
            log.error("Error guardando el Log de operación", ex);
            throw new LogException();
        } finally {

        }
    }

    public static ImpuestosFact consultarImpuestoFactura(Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ImpuestosFact imp = null;
        String sql = null;

        sql = "SELECT BASE_IMPONIBLE,  TARIFA, VALOR "
                + "FROM " + getNombreElementoConfiguracion(TABLA1)
                + "WHERE UID_TICKET = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                imp = new ImpuestosFact();
                imp.setBaseImponible(rs.getInt("BASE_IMPONIBLE"));
                imp.setTarifa(rs.getBigDecimal("TARIFA"));
                imp.setValor(rs.getBigDecimal("VALOR"));
            }

            return imp;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

}
