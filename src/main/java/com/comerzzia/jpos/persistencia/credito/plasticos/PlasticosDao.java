/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.plasticos;

import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class PlasticosDao extends MantenimientoDao {

    private static String TABLA = "PLASTICO";   // ESQUEMA CREDITO
    private static String TABLA_ESTADO = "CESTACTUAL"; // ESQUEMA CREDITO
    private static String TABLA_POS = "D_PLASTICO_TBL";
    private static String TABLA_ESTADO_POS = "D_CESTACTUAL_TBL";
    private static String TABLA_PARAM_ESTADO = "PARAM_ESTADOS_CREDITO";

    private static Logger log = Logger.getMLogger(PlasticosDao.class);

    public static PlasticoBean consultarPorNumTarjeta(Connection conn, String numeroTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        String nombreTabla = getNombreElementoCredito(TABLA);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }

        sql = "SELECT P_NUM, P_CREDITO, P_NUEVO, P_ID, P_FECHAVENCE "
                + "FROM " + nombreTabla
                + " WHERE P_NUM = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, numeroTarjeta);
            log.debug("consultarPorNumTarjeta() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlasticoBean(rs);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static PlasticoBean consultarPorNumTarjetaPrefactura(Connection conn, String numeroTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        String nombreTabla = getNombreElementoCredito(TABLA);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }

        sql = "SELECT P_NUM, P_CREDITO, P_NUEVO, P_ID, P_FECHAVENCE "
                + "FROM " + nombreTabla
                + " WHERE P_NUM = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, numeroTarjeta);
            log.debug("consultarPorNumTarjeta() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlasticoBean(rs);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static List<PlasticoBean> consultarTodasPorCedulaCliente(Connection conn, String cedula) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<PlasticoBean> res = new ArrayList<PlasticoBean>();

        String nombreTabla = getNombreElementoCredito(TABLA);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }

        sql = "SELECT P_NUM, P_CREDITO, P_NUEVO, P_ID, P_FECHAVENCE "
                + "FROM " + nombreTabla
                + " WHERE P_ID = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            log.debug("consultarTodasPorCedulaCliente() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                res.add(new PlasticoBean(rs));
            }
            return res;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static String consultarNumeroPorCedula(Connection conn, String cedula) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        String nombreTabla = getNombreElementoCredito(TABLA);
        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "SELECT MAX (p.p_num), MAX (p.p_credito) "
                + "FROM " + nombreTabla + " p "
                + "WHERE p_id = ? "
                + "AND p.p_num NOT IN ( "
                + "SELECT c.a_plastico "
                + "FROM " + nombreTablaEstado + " c "
                + "WHERE c.a_tipo = 'P' "
                + "AND c.a_estatus = 'C' "
                + "AND c.a_fechault IS NULL "
                + "AND c.a_plastico = p.p_num) "
                + "AND p.p_credito NOT IN ( "
                + "SELECT c.a_credito "
                + "FROM " + nombreTablaEstado + " c "
                + "WHERE c.a_tipo = 'C' "
                + "AND c.a_estatus = 'C' "
                + "AND c.a_fechault IS NULL "
                + "AND c.a_credito = p.p_credito) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            log.debug("consultarNumeroPorCedula() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static PlasticoEstadoBean consultarEstado(Connection conn, String numeroTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "SELECT A.A_ESTATUS, A.A_SESTATUS "
                + "FROM " + nombreTablaEstado + " A "
                + "WHERE A.A_PLASTICO = ? "
                + "AND A.A_FECHAULT IS NULL "
                + "AND A.A_TIPO = 'P' "
                + "AND A.A_ESTATUS IN ('A','G') "
                + "AND A.A_PLASTICO NOT IN ( SELECT C.A_PLASTICO FROM " + nombreTablaEstado + " C "
                + " WHERE C.A_TIPO = 'P' "
                + " AND C.A_ESTATUS = 'C' "
                + " AND C.A_FECHAULT IS NULL "
                + " AND A.A_PLASTICO = C.A_PLASTICO )";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, numeroTarjeta);
            log.debug("consultarEstado() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlasticoEstadoBean(rs.getString("A_ESTATUS"), rs.getString("A_SESTATUS"));
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static CuentaEstadoBean consultarEstadoCuenta(Connection conn, Integer numeroCuenta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "SELECT A.A_ESTATUS, A.A_SESTATUS "
                + "FROM " + nombreTablaEstado + " A "
                + "WHERE A.A_CREDITO = ? "
                + "AND A.A_FECHAULT IS NULL "
                + "AND A.A_TIPO = 'C' "
                + "AND A.A_ESTATUS IN ('V') ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numeroCuenta);
            log.debug("consultarEstadoCuenta() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new CuentaEstadoBean(rs.getString("A_ESTATUS"), rs.getString("A_SESTATUS"));
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static String consultarEstadoPorTarjeta(Connection conn, String numeroTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "select mensaje_pos from " + TABLA_PARAM_ESTADO + " a "
                + "where exists(select null from " + nombreTablaEstado + " b "
                + "where  a.estado = b.estadocta "
                + "AND b.A_FECHAULT IS NULL "
                + "and b.A_PLASTICO = ? ) "
                + "and rownum <= 1 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, numeroTarjeta);
            log.debug("consultarEstadoCuenta() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static String consultarEstadoPorTarjetaStatusAnt(Connection conn, String numeroTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "select mensaje_pos from " + TABLA_PARAM_ESTADO + " a "
                + "where exists(select null from " + nombreTablaEstado + " b "
                + "where   nvl(b.a_estatus,0) = nvl(a.a_estatus,0) "
                + "and nvl(b.a_sestatus,0) =  nvl(a.a_sestatus,0) "
                + "and nvl(b.a_ssestatus,0) =  nvl(a.a_ssestatus,0) "
                + "AND b.A_FECHAULT IS NULL "
                + "and b.A_PLASTICO = ? ) "
                + "and rownum <= 1 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, numeroTarjeta);
            log.debug("consultarEstadoCuenta() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static List<PlasticoBean> consultarActivasPorCedulaCliente(Connection conn, String cedula) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<PlasticoBean> res = new ArrayList<PlasticoBean>();

        String nombreTabla = getNombreElementoCredito(TABLA);
        String nombreTablaEstado = getNombreElementoCredito(TABLA_ESTADO);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
            nombreTablaEstado = TABLA_ESTADO_POS;
        }

        sql = "SELECT P_NUM, P_CREDITO, P_NUEVO, P_ID, P_FECHAVENCE "
                + "FROM " + nombreTabla + " P, " + nombreTablaEstado + " A "
                + "WHERE A.A_FECHAULT IS NULL AND A.A_TIPO = 'P' AND A.A_ESTATUS IN ('A','G') AND A.A_PLASTICO = P.P_NUM AND P_ID = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            log.debug("consultarTodasPorCedulaCliente() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                res.add(new PlasticoBean(rs));
            }
            return res;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     *
     * @param conn
     * @param identificacion
     * @param inicioBin
     * @param finBin
     * @return
     * @throws SQLException
     */
    public static PlasticoBean consultarPorBinTarjetaIdentificacion(Connection conn, String identificacion,
            String inicioBin, String finBin) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        String nombreTabla = getNombreElementoCredito(TABLA);
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_POS;
        }

        sql.append("SELECT P_NUM, P_CREDITO, P_NUEVO, P_ID, P_FECHAVENCE ");
        sql.append("FROM ").append(nombreTabla);
        sql.append(" WHERE P_NUM = LIKE  '").append(inicioBin).append("%").append(finBin).append("' ");
        sql.append("AND P_ID = ?");

        try {
            pstmt = new PreparedStatement(conn, sql.toString());
            pstmt.setString(1, inicioBin);
            pstmt.setString(2, finBin);
            pstmt.setString(3, identificacion);
            log.debug("consultarPorBinTarjetaIdentificacion() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlasticoBean(rs);
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

}
