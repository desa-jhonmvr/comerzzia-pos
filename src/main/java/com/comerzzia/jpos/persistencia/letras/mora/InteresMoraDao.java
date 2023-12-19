/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.letras.mora;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author amos
 */
public class InteresMoraDao extends MantenimientoDao {

    private static String TABLA         = "CMORA";   // ESQUEMA CREDITO
    private static Logger log = Logger.getMLogger(InteresMoraDao.class);

    public static BigDecimal consultarInteres(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT M_INTERES "
                + "FROM " + getNombreElementoCredito(TABLA)
                + "WHERE M_FDESDE < ? AND M_FHASTA IS NULL AND M_MONEDA = 2 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setTimestamp(1, new Fecha());
            log.debug("consultarInteres() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("M_INTERES");
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

}
