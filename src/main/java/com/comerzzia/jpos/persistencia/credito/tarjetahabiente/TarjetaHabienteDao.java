/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.tarjetahabiente;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.CallableStatement;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

/**
 *
 * @author amos
 */
public class TarjetaHabienteDao extends MantenimientoDao {

    // REALIZAMOS TODAS LAS CONSULTAS SOBRE LA CENTRAL
    private static final String TABLA_CICLOS            = "CICLOS";       // ESQUEMA CRÉDITO // CENTRAL
    private static final String TABLA_CDETALLE          = "CDETALLE";     // ESQUEMA CRÉDITO // LOCAL SINONIMO
    private static final String TABLA_CESTADO_CTA       = "CESTADO_CTA";  // ESQUEMA CRÉDITO // LOCAL SINONIMO
    private static final String TABLA_CTIPO_CARGO       = "CTIPO_CARGO";  // ESQUEMA CRÉDITO // CENTRAL
    private static final String TABLA_CARGOS            = "CARGOS";       // ESQUEMA CRÉDITO // LOCAL SINONIMO
    private static final String TABLA_CCONTROL          = "CCONTROL";     // ESQUEMA CRÉDITO // CENTRAL
    private static final String TABLA_CSOLICITUD        = "CSOLICITUD";   // ESQUEMA CRÉDITO // LOCAL SINONIMO
    private static final String TABLA_CCREDMONEDA       = "CCREDMONEDA";  // ESQUEMA CRÉDITO // CENTRAL
    private static final String FUNC_FACRUAL_ACRUACUM   = "FACRUAL_ACRUACUM";  // ESQUEMA CRÉDITO // CENTRAL
    
        
    private static Logger log = Logger.getMLogger(TarjetaHabienteDao.class);

    public static BigDecimal consultarPagosVencidos(Connection conn, Integer numCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        sql = "SELECT SUM(CAPITAL) AS TOTAL "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CICLOS)
                + "WHERE CREDITO = ? AND MONEDAFACT = 2 ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numCredito);
            log.debug("consultarPagosVencidos() - " + pstmt);
            return consultarCantidad(pstmt);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }

    public static BigDecimal consultarMora(Connection conn, Integer numCredito) throws SQLException {
        CallableStatement cstmt = null;
        String sql = null;
        ResultSet rs = null;

        Fecha fecha = new Fecha(consultarDiaCerradoControl(conn));

        sql = "{? = call " + getNombreElementoCentralBMSK(FUNC_FACRUAL_ACRUACUM) + " (?, ?, ?)}";
        try {
            cstmt = new CallableStatement(conn, sql);
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setInt(2, numCredito);
            cstmt.setDate(3, fecha.getSQL());
            cstmt.setInt(4, 2);
                        
            log.debug("consultarMora() - " + cstmt);
            cstmt.execute();
            BigDecimal mora = cstmt.getBigDecimal(1);
            if (mora == null){
                return BigDecimal.ZERO;
            }
            return mora;
        }
        finally {
            try {
                cstmt.close();
            }
            catch (Exception ignore) {}
            try {
                rs.close();
            }
            catch (Exception ignore) {}
        }
    }
    
    public static BigDecimal consultarFacturado(Connection conn, Integer numCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        sql = "SELECT SUM(D_SIGNO*(NVL(D_CAPITAL,0)+NVL(D_INTERES,0))) AS TOTAL "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CDETALLE) 
                +   ", "  + getNombreElementoCentralBMSK(TABLA_CESTADO_CTA) 
                +   ", "  + getNombreElementoCentralBMSK(TABLA_CTIPO_CARGO) 
                + "WHERE D_CREDITO = ? "
                + "AND D_NUMESTCTA_CRUCE = E_NUMERO "
                + "AND D_CREDITO = E_CREDITO "
                + "AND T_CODIGO = D_TIPO "
                + "AND (T_MORA = 'S' OR D_TIPO = '80') "
                + "AND TRUNC(SYSDATE)- E_FECHA_MAXIMA_DE_PAGO <=0 "
                + "AND D_MONEDAFACT = E_MONEDAFACT "
                + "AND D_MONEDAFACT = 2";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numCredito);
            log.debug("consultarFacturado() - " + pstmt);
            return consultarCantidad(pstmt);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }
    
    public static BigDecimal consultarNotasDebito(Connection conn, Integer numCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        sql = "SELECT SUM(R_SIGNO*(NVL(R_SALDOCAP,0)+NVL(R_SALDOINT,0))) AS TOTAL "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CARGOS) 
                +   ", "  + getNombreElementoCentralBMSK(TABLA_CTIPO_CARGO) 
                + "WHERE R_CREDITO = ? "
                + "AND R_SIGNO = 1 "
                + "AND R_SALDOPLAZO = R_PLAZO "
                + "AND T_CODIGO = R_TIPO "
                + "AND R_TIPO = '80' "
                + "AND R_MONEDAFACT = 2";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numCredito);
            log.debug("consultarNotasDebito() - " + pstmt);
            return consultarCantidad(pstmt);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }

    public static BigDecimal consultarSaldoAFavor(Connection conn, Integer numCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        sql = "SELECT SUM(D_CAPITAL) AS TOTAL "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CDETALLE) 
                +   ", "  + getNombreElementoCentralBMSK(TABLA_CCREDMONEDA) 
                + "WHERE D_CREDITO = ? "
                + "AND D_CREDITO = C_CREDITO "
                + "AND D_TIPO = 92 "
                + "AND D_NUMESTCTA = NVL(C_NUMEST_CTA,1) "
                + "AND D_MONEDAFACT = C_MONEDAFACT "
                + "AND D_MONEDAFACT = 2";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, numCredito);
            log.debug("consultarSaldoAFavor() - " + pstmt);
            return consultarCantidad(pstmt);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }
    
    private static BigDecimal consultarCantidad(PreparedStatement pstmt) throws SQLException {
        ResultSet rs = null;
        BigDecimal cantidad = null;
        try {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cantidad = rs.getBigDecimal("TOTAL");
            }
            if (cantidad != null){
                return cantidad;
            }
            return BigDecimal.ZERO;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {}
        }
    }

    public static String consultarCiclo(Connection conn, String cedula) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        ResultSet rs = null;
        sql = "SELECT S_CICLO "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CSOLICITUD)
                + "WHERE S_ID = ? ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            log.debug("consultarCiclo() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("S_CICLO");
            }
            return "";
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
            try {
                rs.close();
            }
            catch (Exception ignore) {}
        }
    }    

    private static Date consultarDiaCerradoControl(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        ResultSet rs = null;
        sql = "SELECT CCDIA_CERRADO "
                + "FROM " + getNombreElementoCentralBMSK(TABLA_CCONTROL);
        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarDiaCerradoControl() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("CCDIA_CERRADO");
            }
            return null;
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
            try {
                rs.close();
            }
            catch (Exception ignore) {}
        }
    }    
    
    
}
