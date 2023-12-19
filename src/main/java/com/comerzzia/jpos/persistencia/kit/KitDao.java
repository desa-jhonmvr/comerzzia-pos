/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.kit;

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
 * @author MGRI
 */
public class KitDao extends MantenimientoDao {

    private static String TABLA = "D_KIT_INSTALACION_TBL";

    private static Logger log = Logger.getMLogger(KitDao.class);

    public static List<KitBean> consultarKitsArticulo(Connection conn, String codArt, Boolean obligatorio) throws SQLException {

        List<KitBean> listaKit = new ArrayList<KitBean>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("select  CODART, CODART_KIT");
        sql.append(" from ").append(TABLA);
        sql.append(" where CODART= ? ");
        sql.append(" and ACTIVO = 'S' ");

        if (obligatorio != null) {
            sql.append(" and OBLIGATORIO = ? ");
        }

        try {
            pstmt = new PreparedStatement(conn, sql.toString());
            pstmt.setString(1, codArt);

            if (obligatorio != null) {
                pstmt.setString(2, obligatorio ? "S" : "N");
            }
            log.debug("consultarKitsArticulo() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                KitBean kitB = new KitBean();
                kitB.setCodArt(rs.getString("CODART"));
                kitB.setCodArtKit(rs.getString("CODART_KIT"));
                listaKit.add(kitB);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }

        return listaKit;

    }

}
