/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.core.contadores.caja;

import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author amos
 */
public class ContadoresCajaDao extends MantenimientoDao {

    private static String TABLA = "X_CONFIG_CONTADORES_CAJA_TBL";
    private static Logger log = Logger.getMLogger(ContadoresCajaDao.class);

    public static Long consultar(Connection conn, String idContador) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT VALOR "
                    + "FROM " + getNombreElementoEmpresa(TABLA)
                    + "WHERE CONTADOR = ? AND CODALM = ? AND CODCAJA = ? ";

            pstmt = new PreparedStatement(conn, sql);

            pstmt.setString(1, idContador);
            pstmt.setString(2, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(3, Sesion.config.getCodcaja());
            
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("VALOR");
            }
            return null;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }
        
    //consultar contador caja 
        public static Long consultarPorCaja(Connection conn, String idContador,String caja) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT VALOR "
                    + "FROM " + getNombreElementoEmpresa(TABLA)
                    + "WHERE CONTADOR = ? AND CODALM = ? AND CODCAJA = ? ";

            pstmt = new PreparedStatement(conn, sql);

            pstmt.setString(1, idContador);
            pstmt.setString(2, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(3, caja);
            
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("VALOR");
            }
            return null;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }
    
    public static void insert(Connection conn, String idContador, Long valor) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;


        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(CONTADOR, CODALM, CODCAJA, VALOR, FECHA_REINICIO) "
                + " VALUES (?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, idContador);
            pstmt.setString(2, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(3, Sesion.config.getCodcaja());
            pstmt.setLong(4, valor);
            pstmt.setDate(5, new Fecha().getSQL());

            log.debug("insert() - " + pstmt);

            pstmt.execute();
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }

    
    
    public static boolean incrementar(Connection conn, String idContador) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET VALOR = VALOR + 1 "
                + "WHERE CONTADOR = ? AND CODALM = ? AND CODCAJA = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, idContador);
            pstmt.setString(2, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(3, Sesion.config.getCodcaja());

            log.debug("incrementar() - " + pstmt);

            return (pstmt.executeUpdate() > 0);
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }   


    public static boolean reiniciar(Connection conn, String idContador, Long valor) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET VALOR = ? "
                + "WHERE CONTADOR = ? AND CODALM = ? AND CODCAJA = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, valor);
            pstmt.setString(2, idContador);
            pstmt.setString(3, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(4, Sesion.config.getCodcaja());

            log.debug("reiniciar() - " + pstmt);

            return (pstmt.executeUpdate() > 0);
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }   

    public static void reiniciar(Connection conn, Fecha fechaReinicio) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "DELETE FROM " + getNombreElementoEmpresa(TABLA)
                + "WHERE CODALM = ? AND CODCAJA = ? "
                + "AND (FECHA_REINICIO < ? OR FECHA_REINICIO IS NULL) "
                + "AND (CONTADOR = 'FACTURA' OR CONTADOR = 'NOTA_CREDITO' OR CONTADOR = 'GUIA_REMISION')";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            pstmt.setString(2, Sesion.config.getCodcaja());
            pstmt.setDate(3, fechaReinicio.getSQL());
            log.debug("reiniciar() - " + pstmt);

            pstmt.execute();
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }   
    

}
