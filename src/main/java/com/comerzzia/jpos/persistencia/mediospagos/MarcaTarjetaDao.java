/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.mediospagos;

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
 * @author Gabriel Simbania
 */
public class MarcaTarjetaDao extends MantenimientoDao {

    private static final Logger LOG_POS = Logger.getMLogger(MarcaTarjetaDao.class);

    private static final String D_MARCAS_TARJETAS_TBL = "D_MARCAS_TARJETAS_TBL";
    private static final String D_MARCA_TAR_MEDIO_PAGO_TBL = "D_MARCAS_TAR_MEDIO_PAG_TBL";

    /**
     *
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<MarcaTarjetaBean> consultarMarcasTarjeta(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MarcaTarjetaBean> marcaTarjetaList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT M.CODMARTAR CODMARTAR, M.DESMARCA DESMARCA");
        sql.append(" FROM ").append(getNombreElementoEmpresa(D_MARCAS_TARJETAS_TBL)).append(" M ");
        sql.append(" WHERE ACTIVO = 'S'");
        sql.append(" ORDER BY ORDEN ASC ");

        try {
            pstmt = new PreparedStatement(conn, sql.toString());
            LOG_POS.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                marcaTarjetaList.add(new MarcaTarjetaBean(rs.getString("CODMARTAR"), rs.getString("DESMARCA")));
            }
            return marcaTarjetaList;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     *
     * @param conn
     * @param codMarcaTarjeta
     * @return
     * @throws SQLException
     */
    public static List<MarcaTarjetaMedioPagoBean> consultarMarcasTarjetaMedioPagoByMarca(Connection conn, String codMarcaTarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MarcaTarjetaMedioPagoBean> marcaTarjetaMedioPagList = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT M.CODMARMED , M.CODMARTAR, M.CODMEDPAG, M.DESCRIPCION, M.ACTIVO, M.ORDEN ");
        sqlBuilder.append(" FROM ").append(getNombreElementoEmpresa(D_MARCA_TAR_MEDIO_PAGO_TBL)).append(" M ");
        sqlBuilder.append(" WHERE ACTIVO = 'S' ");
        sqlBuilder.append(" and CODMARTAR = ? ");
        sqlBuilder.append(" ORDER BY M.ORDEN ASC ");

        try {
            pstmt = new PreparedStatement(conn, sqlBuilder.toString());
            pstmt.setString(1, codMarcaTarjeta);
            LOG_POS.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                marcaTarjetaMedioPagList.add(new MarcaTarjetaMedioPagoBean(rs.getLong("CODMARMED"), rs.getString("CODMARTAR"),
                        rs.getString("CODMEDPAG"), rs.getString("DESCRIPCION"), rs.getString("ACTIVO"), rs.getLong("ORDEN")));
            }
            return marcaTarjetaMedioPagList;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

}
