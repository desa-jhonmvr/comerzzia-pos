/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.politica;

import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Gabriel Simbania
 */
public class ProteccionDatosDao {

    private static final Logger LOG_POS = Logger.getMLogger(ProteccionDatosDao.class);

    private static final String TABLA = "D_PROTECCION_DATOS_TBL";

    /**
     *
     * @param conn
     * @param codCliente
     * @return
     * @throws SQLException
     */
    public static ProteccionDatosBean consultaProteccionDatos(Connection conn, String codCliente) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT CODCLI, APELLIDOS, NOMBRES, APROBACION_POLITICAS ");
        sql.append(" FROM ").append(TABLA);
        sql.append(" WHERE CODCLI = ? ");

        try {
            pstmt = new PreparedStatement(conn, sql.toString());
            pstmt.setString(1, codCliente);
            LOG_POS.debug("consultaProteccionDatos() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ProteccionDatosBean(rs.getString("CODCLI"), rs.getString("APELLIDOS"), rs.getString("NOMBRES"), rs.getString("APROBACION_POLITICAS"));
            }
            return null;
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
