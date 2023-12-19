/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.promociones.articulos;

import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author SMLM
 */
public class PromocionArticuloDao extends MantenimientoDao {

    private static final String TABLA = "X_PROMOCIONES_ARTICULOS_TBL";
    private static final String TABLA_TICKETS = "D_TICKETS_TBL";
    private static final String TABLA_NC = "X_NOTAS_CREDITO_DET_TBL";
    private static final String TABLA_PROMO_FP = "X_PROMO_FP_ARTICULOS_TBL";
    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd/MM/yyyy");

    private static final Logger log = Logger.getMLogger(PromocionArticuloDao.class);

    public static void insert(Connection conn, PromocionArticuloBean promoArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;

        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(ID_PROMOCION, CODART, CANTIDAD, UID_TICKET, ID_LINEA) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, promoArticulo.getIdPromocion());
            pstmt.setString(2, promoArticulo.getCodart());
            pstmt.setInt(3, promoArticulo.getCantidad());
            pstmt.setString(4, promoArticulo.getUidTicket());
            pstmt.setInt(5, promoArticulo.getIdLinea());

            log.debug("insert() - " + pstmt);

            pstmt.execute();
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static int consultarPromocionDiaSocio(Connection conn, Long idPromocion, String codArt) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;
        ResultSet rs = null;

        sql = "SELECT SUM(nvl(PROMO.CANTIDAD,0)) -SUM(nvl(NC.CANTIDAD,0)) AS TOTAL  "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " PROMO "
                + "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICKETS) + "TICK "
                + "ON (PROMO.UID_TICKET = TICK.UID_TICKET) "
                + "LEFT JOIN " + getNombreElementoEmpresa(TABLA_NC) + "NC "
                + "ON (PROMO.UID_TICKET = NC.UID_TICKET) "
                + "WHERE PROMO.ID_PROMOCION = ? AND PROMO.CODART = ? AND TICK.ANULADO='N'";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idPromocion);
            pstmt.setString(2, codArt);

            log.debug("consultarPromocionDiaSocio() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static int consultarPromocionFormaPago(Connection conn, String codArt) throws SQLException  {
        PreparedStatement pstmt = null;
        String sql;
        ResultSet rs = null;

        sql = "SELECT count(*) aux  "
                + "FROM " + getNombreElementoEmpresa(TABLA_PROMO_FP) + " PROMO "
                + "WHERE PROMO.CODIGO = ? AND ACTIVO = 1 "
                + " AND FECHA_INICIAL <= ?  AND FECHA_FINAL >= ?  ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            Fecha mañana = new Fecha();
            pstmt.setString(1, codArt);
            String fecha =  formateadorFechaCorta.format(mañana.getSQL());
            pstmt.setString(2, fecha);
            pstmt.setString(3,  fecha);

            log.debug("consultarPromocionFormaPago() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

}
