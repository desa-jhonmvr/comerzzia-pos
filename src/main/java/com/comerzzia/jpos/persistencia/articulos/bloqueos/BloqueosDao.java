/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.articulos.bloqueos;

import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MGRI
 */
public class BloqueosDao {
    
    private static String TABLA = "D_ITEMBLOQUEO_TBL";
    private static Logger log = Logger.getMLogger(BloqueosDao.class);
    
     public static boolean isItemBloqueado(Connection conn, String codart, String codalm) throws SQLException {
        boolean res = false;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        
        long codalmInt = new Long(codalm); 

        sql = "SELECT COUNT(*) "
                + "FROM " + TABLA + " IB "
                + "WHERE CODART = '" + codart + "' "
                + "AND IBLUGAR = '" + codalmInt + "' "
                + "AND IBSTATUS = 'B' ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("isItemBloqueado() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1)>0){
                    res = true;
                }
            }
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


        return res;
    }
     
}
