/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.logs.logsistema;

import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author amos
 */
public class LogSistemaDao extends MantenimientoDao {

    private static String TABLA_ACCESOS = "X_LOG_ACCESOS_POS_TBL";
    private static Logger log = Logger.getMLogger(LogSistemaDao.class);

    public static void registrarAcceso(Connection conn, UsuarioBean usuario, String codAlmacen, String codCaja) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "INSERT INTO " + getNombreElementoConfiguracion(TABLA_ACCESOS) + "(UID_LOG, FECHA, ID_USUARIO, USUARIO, CODALM, CODCAJA, PROCESADO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            Date d = new Date();
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setTimestamp(2, new Timestamp(d.getTime()));
            pstmt.setLong(3, usuario.getIdUsuario());
            pstmt.setString(4, usuario.getUsuario());
            pstmt.setString(5, codAlmacen);
            pstmt.setString(6, codCaja);
            pstmt.setString(7, "N");

            log.debug("registrarAcceso() - " + pstmt);

            pstmt.execute();
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {
                ;
            }
        }
    }
}
