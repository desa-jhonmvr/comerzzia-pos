/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.cuposvirtuales;

import com.comerzzia.jpos.entity.db.CupoVirtual;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author amos
 */
public class CupoVirtualDao extends MantenimientoDao {

    private static String TABLA = "CUPOVIRTUAL"; // ESQUEMA SUKASA
    private static String TABLA_POS = "D_CUPOVIRTUAL_TBL";
    private static Logger log = Logger.getMLogger(CupoVirtualDao.class);

    public static CupoVirtualBean consultar(Connection conn, Integer numeroCredito) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        
        String nombreTabla = getNombreElementoSukasa(TABLA);
        if(VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")){
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }

        sql = "SELECT CREDITO, CUPO, FECHA "
                + " FROM " + nombreTabla
                + " WHERE CREDITO = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numeroCredito);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new CupoVirtualBean(rs);
            }
            return null;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {}
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }




    public static void restarCupo(Connection conn, BigDecimal cantidad, Integer numCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        String nombreTabla = getNombreElementoSukasa(TABLA);
        if(VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")){
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }
        
        sql = "UPDATE " + nombreTabla
                + " SET CUPO = CUPO - ?, FECHA = ? "
                + "WHERE CREDITO = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setBigDecimal(1, cantidad);
            pstmt.setTimestamp(2, new Fecha().getTimestamp());
            pstmt.setInt(3, numCredito);
            
            log.debug("restarCupo() - " + pstmt);

            pstmt.executeUpdate();
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }

     public static CupoVirtual consultarCupoVirtualByCredito(EntityManager em, Integer credito) throws Exception {
        Query consulta = em.createQuery("SELECT a FROM CupoVirtual a WHERE a.credito= :credito");
        consulta.setParameter("credito", credito);
        
        CupoVirtual res = (CupoVirtual)consulta.getSingleResult();
        return res;
    }
}
